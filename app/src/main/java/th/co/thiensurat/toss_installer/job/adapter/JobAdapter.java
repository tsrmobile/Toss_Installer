package th.co.thiensurat.toss_installer.job.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperAdapter;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperViewHolder;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private StringBuilder sb;
    private ClickListener clickListener;
    private final OnStartDragListener dragListener;
    private final List<String> item = new ArrayList<>();
    private OnCustomerListChangedListener onCustomerListChangedListener;

    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobAdapter(FragmentActivity activity, OnStartDragListener dragStartListener,
                      OnCustomerListChangedListener listChangedListener) {
        this.context = activity;
        this.dragListener = dragStartListener;
        this.onCustomerListChangedListener= listChangedListener;
    }

    public void setJobList(List<JobItem> jobItemList) {
        this.jobItemList = jobItemList;
    }

    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JobAdapter.ViewHolder holder, int position) {
        sb = new StringBuilder();
        JobItem item = jobItemList.get(position);
        holder.textViewNumber.setText(String.valueOf(position +1));
        holder.textViewName.setText(item.getTitle() + "" + item.getFirstName() + " " + item.getLastName());

        for (ProductItem productItem : item.getProduct()) {
            holder.textViewProduct.setText(item.getOrderid() + "\n" + productItem.getProductName() + "\nจำนวน " + productItem.getProductQty());
        }

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
        holder.textViewTime.setText(timeFormat.format(date));
        holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
        holder.textViewEndTime.setText(timeFormat.format(endDate));

        for (AddressItem addressItem : item.getAddress()) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                int pos = position;
                sb.append(addressItem.getAddrDetail());
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : " ต." + item.getAddress().get(pos).getSubdistrict());
                sb.append("\n");
                sb.append((addressItem.getDistrict().equals("")) ? "" : "อ." + addressItem.getDistrict());
                sb.append((addressItem.getProvince().equals("")) ? "" : " จ." + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());

                holder.textViewAddress.setText(sb.toString());
                holder.textViewPhone.setText((addressItem.getPhone().equals("")) ? "-" : addressItem.getPhone());
                holder.textViewMobile.setText((addressItem.getMobile().equals("")) ? "-" : addressItem.getMobile());
            }
        }
        holder.imageViewDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public void setItemClick(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(jobItemList, fromPosition, toPosition);
        onCustomerListChangedListener.onNoteListChanged(jobItemList);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

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
        @BindView(R.id.drag_handle) ImageView imageViewDrag;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onItemClick() );
            textViewPhone.setOnClickListener( onLocalPhone() );
            textViewMobile.setOnClickListener( onMobilePhone() );
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

        private View.OnClickListener onLocalPhone() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.callLocal(view, getPosition());
                    }
                }
            };
        }

        private View.OnClickListener onMobilePhone() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.callMobile(view, getPosition());
                    }
                }
            };
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }

    public interface ClickListener{
        void itemClick(View view, int position);
        void callLocal(View view, int position);
        void callMobile(View view, int position);
    }

    private String ConvertDateFormat(String strdate) {
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
        int year = 0, month = 0, day = 0;
        String d, m;
        try {
            Date date = dbFormat.parse(strdate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DATE);
            if (day < 10) {
                d = "0" + day;
            } else {
                d = "" + day;
            }

            if (month < 10) {
                m = "0" + month;
            } else {
                m = "" + month;
            }
            return String.format("%s/%s/%s", d, m, year+543);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
