package th.co.thiensurat.toss_installer.itemlist.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItemAdapter extends RecyclerView.Adapter<InstallItemAdapter.ViewHolder> {

    private Context context;
    private ChangeTintColor changeTintColor;
    private List<InstallItem> installItemList = new ArrayList<InstallItem>();

    public InstallItemAdapter(FragmentActivity activity) {
        this.context = activity;
        changeTintColor = new ChangeTintColor(context);
    }

    public void setInstallItemList(List<InstallItem> installItemList) {
        this.installItemList = installItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_installitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InstallItem item = installItemList.get(position);
        if (item.getAStockStatus().equals("T")) {
            holder.textViewStatus.setText("ยังไม่ได้เบิก");
            holder.viewColor.setBackgroundColor(context.getResources().getColor(R.color.Orange));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.Orange);
        } else if (item.getAStockStatus().equals("F")) {
            holder.textViewStatus.setText("เบิกแล้ว");
            holder.viewColor.setBackgroundColor(context.getResources().getColor(R.color.LimeGreen));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.LimeGreen);
        } else if (item.getAStockStatus().equals("1")) {
            holder.textViewStatus.setText("ยังไม่ได้ติดตั้ง");
            holder.viewColor.setBackgroundColor(context.getResources().getColor(R.color.Orange));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.Orange);
        }

        holder.textViewNumber.setText((position + 1) + ".");
        holder.textViewSerial.setText(item.getProduct_SerialNum());
        holder.textViewName.setText(item.getProduct_Name());

    }

    @Override
    public int getItemCount() {
        return installItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.serial_number) TextView textViewSerial;
        @BindView(R.id.number_list) TextView textViewNumber;
        @BindView(R.id.status_color) View viewColor;
        @BindView(R.id.product_name) TextView textViewName;
        @BindView(R.id.product_status) TextView textViewStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
