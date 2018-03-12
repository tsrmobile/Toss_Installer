package th.co.thiensurat.toss_installer.jobinstallation.unfinish.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.ls.LSInput;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.Constance;

import static th.co.thiensurat.toss_installer.utils.Utils.ConvertDateFormat;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobUnFinishAdapter extends RecyclerView.Adapter<JobUnFinishAdapter.ViewHolder> {

    private Context context;
    private StringBuilder sb;
    private StringBuilder stringBuilder;
    private ClickListener clickListener;
    private ChangeTintColor changeTintColor;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobUnFinishAdapter(FragmentActivity activity) {
        this.context = activity;
        changeTintColor = new ChangeTintColor(context);
    }

    public void setJobUnFinishItem(List<JobItem> jobItem) {
        this.jobItemList = jobItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sb = new StringBuilder();
        stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());
        JobItem item = jobItemList.get(position);
        int posi = (position +1);
        holder.textViewNumber.setText(String.valueOf(posi));
        holder.textViewName.setText(item.getTitle() + "" + item.getFirstName() + " " + item.getLastName());
        String temp = item.getOrderid();
        String temp2 = "";

        for (int i = 0; i < item.getProduct().size(); i++) {
            ProductItem productItem = item.getProduct().get(i);
            if (temp2.isEmpty()) {
                temp2 = temp;
                stringBuilder.append(item.getOrderid() + "\n" + (i + 1) + ". " + productItem.getProductName() + " จำนวน " + productItem.getProductQty() + " เครื่อง/ชิ้น");
            } else if (temp2.equals(temp)) {
                stringBuilder.append("\n" + (i + 1) + ". " + productItem.getProductName() + " จำนวน " + productItem.getProductQty() + " เครื่อง/ชิ้น");
            }
        }

        holder.textViewProduct.setText(stringBuilder.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date date = null;
        Date endDate = null;
        try {
            date = sdf.parse(item.getInstallStartDate());
            endDate = sdf.parse(item.getInstallEndDate());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        holder.textViewDate.setText(ConvertDateFormat(item.getInstallStartDate()));
        holder.textViewTime.setText(timeFormat.format(date) + " น.");

        holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
        holder.textViewEndTime.setText(timeFormat.format(endDate) + " น.");

        List<AddressItem> addressItems = item.getAddress();
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
                holder.textViewPhone.setText((addressItem.getPhone().equals("")) ? "-" : addressItem.getPhone());
                holder.textViewMobile.setText((addressItem.getMobile().equals("")) ? "-" : addressItem.getMobile());
            }
        }

        changeTintColor.setTextViewDrawableColor(holder.textViewDistanceTitle, R.color.colorPrimaryDark);

        holder.textViewDistance.setVisibility(View.GONE);
        holder.textViewDistanceTitle.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public void setItemClick(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_distance_title) TextView textViewDistanceTitle;
        @BindView(R.id.textview_distance) TextView textViewDistance;
        @BindView(R.id.textview_number) TextView textViewNumber;
        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_product) TextView textViewProduct;
        @BindView(R.id.textview_address) TextView textViewAddress;
        @BindView(R.id.textview_date) TextView textViewDate;
        @BindView(R.id.textview_time) TextView textViewTime;
        @BindView(R.id.textview_end_date) TextView textViewEndDate;
        @BindView(R.id.textview_end_time) TextView textViewEndTime;
        @BindView(R.id.textview_phone) TextView textViewPhone;
        @BindView(R.id.textview_mobile) TextView textViewMobile;
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
