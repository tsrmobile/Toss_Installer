package th.co.thiensurat.toss_installer.job.all.adapter;

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
import th.co.thiensurat.toss_installer.job.adapter.JobAdapter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.Utils;

import static th.co.thiensurat.toss_installer.utils.Utils.splitDate;
import static th.co.thiensurat.toss_installer.utils.Utils.splitMonth;

/**
 * Created by teerayut.k on 12/6/2017.
 */

public class AllJobAdapter extends RecyclerView.Adapter<AllJobAdapter.ViewHolder> {

    private Context context;
    private StringBuilder sb;
    private ClickListener clickListener;
    private ChangeTintColor changeTintColor;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public AllJobAdapter(FragmentActivity activity) {
        this.context = activity;
        changeTintColor = new ChangeTintColor(context);
    }

    public void setJobItemList(List<JobItem> jobItemList) {
        this.jobItemList = jobItemList;
    }

    @Override
    public AllJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllJobAdapter.ViewHolder holder, int position) {
        sb = new StringBuilder();
        JobItem item = jobItemList.get(position);
        List<ProductItem> productItemList = item.getProduct();

        holder.textViewDate.setText(splitDate(item.getInstallStartDate()) + "\n" + splitMonth(item.getInstallStartDate()));
        holder.textViewOrder.setText(item.getOrderid() + "\n" + productItemList.get(0).getProductName());
        holder.textViewName.setText(item.getTitle() + "" + item.getFirstName() + " " + item.getLastName());

        if ("21".equals(item.getStatus())) {
            holder.textViewStatus.setText("ยังไม่ได้ติดตั้ง");
            holder.viewStatusColor.setBackgroundColor(context.getResources().getColor(R.color.Orange));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.Orange);
        } else if ("01".equals(item.getStatus())) {
            holder.textViewStatus.setText("ติดตั้งแล้ว");
            holder.viewStatusColor.setBackgroundColor(context.getResources().getColor(R.color.LimeGreen));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.LimeGreen);
        } else if ("91".equals(item.getStatus())) {
            holder.textViewStatus.setText("ยกเลิกการติดตั้ง");
            holder.viewStatusColor.setBackgroundColor(context.getResources().getColor(R.color.Red));
            holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_black_18dp, 0, 0, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewStatus, R.color.Red);
        }

        for (AddressItem addressItem : item.getAddress()) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                //int pos = position;
                sb.append(addressItem.getAddrDetail());
                sb.append("\n");
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : "" + addressItem.getSubdistrict());
                sb.append((addressItem.getDistrict().equals("")) ? "" : " " + addressItem.getDistrict());
                sb.append("\n");
                sb.append((addressItem.getProvince().equals("")) ? "" : "" + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());

                holder.textViewAddr.setText(sb.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_date) TextView textViewDate;
        @BindView(R.id.event_order_id) TextView textViewOrder;
        @BindView(R.id.customer_name) TextView textViewName;
        @BindView(R.id.customer_addr) TextView textViewAddr;
        @BindView(R.id.event_status) TextView textViewStatus;
        @BindView(R.id.event_status_color) View viewStatusColor;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onClickListener());
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.itemClick(view, getPosition());
                    }
                }
            };
        }
    }

    public interface ClickListener {
        void itemClick(View view, int position);
    }
}
