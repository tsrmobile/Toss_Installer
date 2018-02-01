package th.co.thiensurat.toss_installer.jobinstallation.finish.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.ApiService;
import th.co.thiensurat.toss_installer.job.adapter.JobAdapter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperAdapter;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperViewHolder;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;

import static th.co.thiensurat.toss_installer.utils.Utils.ConvertDateFormat;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobFinishAdapter extends RecyclerView.Adapter<JobFinishAdapter.ViewHolder> {

    private Context context;
    private StringBuilder sb;
    private ChangeTintColor changeTintColor;
    private ClickListener clickListener;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobFinishAdapter(FragmentActivity activity) {
        this.context = activity;
        changeTintColor = new ChangeTintColor(context);
    }

    public void setJobItem(List<JobItem> jobItem) {
        this.jobItemList = jobItem;
    }

    @Override
    public JobFinishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_job_finish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobFinishAdapter.ViewHolder holder, int position) {
        sb = new StringBuilder();
        JobItem item = jobItemList.get(position);
        holder.textViewNumber.setText(String.valueOf(position +1));
        holder.textViewName.setText(item.getTitle() + "" + item.getFirstName() + " " + item.getLastName());

        for (ProductItem productItem : item.getProduct()) {
            holder.textViewProduct.setText(item.getOrderid() + "\n" + productItem.getProductName() + "\nจำนวน " + productItem.getProductQty());
        }

        List<AddressItem> addressItems = new ArrayList<AddressItem>();
        addressItems = item.getAddress();
        for (int i = 0; i < addressItems.size(); i++) {
            AddressItem addressItem = addressItems.get(i);
            if (addressItem.getAddressType().equals("AddressInstall")) {
                sb.append(addressItem.getAddrDetail());
                sb.append("\n");
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : "" + item.getAddress().get(i).getSubdistrict());
                sb.append((addressItem.getDistrict().equals("")) ? "" : " " + addressItem.getDistrict());
                sb.append("\n");
                sb.append((addressItem.getProvince().equals("")) ? "" : "" + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());

                holder.textViewAddress.setText(sb.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public void setItemClick(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_number) TextView textViewNumber;
        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_product) TextView textViewProduct;
        @BindView(R.id.textview_address) TextView textViewAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onItemClick() );
        }

        private View.OnClickListener onItemClick() {
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

    public interface ClickListener{
        void itemClick(View view, int position);
    }
}
