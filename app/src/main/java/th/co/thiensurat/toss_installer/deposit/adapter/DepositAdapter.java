package th.co.thiensurat.toss_installer.deposit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.deposit.deposit.DepositActivity;
import th.co.thiensurat.toss_installer.deposit.item.DepositItem;
import th.co.thiensurat.toss_installer.utils.Utils;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {

    private float sum = 0;
    private int pos = 0;
    private Context context;
    private SelectedAll selectedAll;
    private List<DepositItem> depositItemList = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public DepositAdapter(Context activity) {
        this.context = activity;
    }

    public void setDepositItemList(List<DepositItem> depositItem) {
        this.depositItemList = depositItem;
    }

    @Override
    public DepositAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_deposit_by_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DepositAdapter.ViewHolder holder, final int position) {
        //float sum = 0;
        final DepositItem depositItem = depositItemList.get(position);
        if (hasEqual(position)) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.textViewDate.setText(Utils.ConvertDateFormat(depositItem.getDate()));
        } else {
            holder.linearLayout.setVisibility(View.GONE);
        }

        holder.textViewDate.setText(Utils.ConvertDateFormat(depositItem.getDate()));
        holder.textViewContno.setText(depositItem.getContno());
        holder.textViewAmount.setText(df.format(Float.parseFloat(depositItem.getAmount())));
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositItem.setSelected(!depositItem.isSelected());
                holder.linearLayoutItem.setBackgroundColor(depositItem.isSelected() ? context.getResources().getColor(R.color.LightSkyBlue) : Color.TRANSPARENT);
                notifyDataSetChanged();
                ((DepositActivity)context).setSum();
            }
        });

        holder.linearLayoutItem.setBackgroundColor(depositItem.isSelected() ? context.getResources().getColor(R.color.LightSkyBlue) : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return depositItemList.size();
    }

    public void setSelectedAll(SelectedAll selectedAll) {
        this.selectedAll = selectedAll;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.datetime_layout) LinearLayout linearLayout;
        @BindView(R.id.date_payment) TextView textViewDate;
        @BindView(R.id.summary) TextView textViewSummary;
        @BindView(R.id.textview_contno) TextView textViewContno;
        @BindView(R.id.textview_amount) TextView textViewAmount;
        @BindView(R.id.layout_item) LinearLayout linearLayoutItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linearLayout.setOnClickListener(onSelect());
        }

        private View.OnClickListener onSelect() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedAll != null) {
                        selectedAll.onSelected(v, getPosition());
                    }
                }
            };
        }
    }

    private boolean hasEqual(int position){
        if (position == 0)
            return true;
        return !depositItemList.get(position).getDate().equals(depositItemList.get(position - 1).getDate());
    }

    public interface SelectedAll {
        void onSelected(View view, int position);
    }
}
