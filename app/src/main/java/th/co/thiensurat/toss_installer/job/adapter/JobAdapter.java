package th.co.thiensurat.toss_installer.job.adapter;

import android.annotation.SuppressLint;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.ApiService;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.DistanceItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.GPSTracker;
import th.co.thiensurat.toss_installer.utils.Utils;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperAdapter;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperViewHolder;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;

import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_BASE_URL;
import static th.co.thiensurat.toss_installer.utils.Utils.ConvertDateFormat;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private String origins;
    private Context context;
    private String distance;
    private StringBuilder sb;
    private GPSTracker gpsTracker;
    private ClickListener clickListener;
    private ChangeTintColor changeTintColor;
    private final OnStartDragListener dragListener;
    private final List<String> item = new ArrayList<>();
    private OnCustomerListChangedListener onCustomerListChangedListener;

    private String destination;
    private ApiService service;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobAdapter(FragmentActivity activity, OnStartDragListener dragStartListener,
                      OnCustomerListChangedListener listChangedListener) {
        this.context = activity;
        this.dragListener = dragStartListener;
        this.onCustomerListChangedListener= listChangedListener;

        gpsTracker = new GPSTracker(context);
        changeTintColor = new ChangeTintColor(context);
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
        holder.textViewTime.setText(timeFormat.format(date) + " น.");
        holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
        holder.textViewEndTime.setText(timeFormat.format(endDate) + " น.");

        for (AddressItem addressItem : item.getAddress()) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                int pos = position;
                sb.append(addressItem.getAddrDetail());
                sb.append("\n");
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : "" + item.getAddress().get(pos).getSubdistrict());
                sb.append((addressItem.getDistrict().equals("")) ? "" : " " + addressItem.getDistrict());
                sb.append("\n");
                sb.append((addressItem.getProvince().equals("")) ? "" : "" + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());

                holder.textViewAddress.setText(sb.toString());
                holder.textViewPhone.setText((addressItem.getPhone().equals("")) ? "-" : addressItem.getPhone());
                holder.textViewMobile.setText((addressItem.getMobile().equals("")) ? "-" : addressItem.getMobile());

                destination = addressItem.getSubdistrict() + "+" + addressItem.getDistrict() + "+" + addressItem.getProvince();
            }
        }
        holder.imageViewDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(holder);
                } else {
                    return false;
                }
                return false;
            }
        });

        changeTintColor.setTextViewDrawableColor(holder.textViewDistanceTitle, R.color.colorPrimaryDark);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        origins = String.valueOf(gpsTracker.getLatitude()) + "," + String.valueOf(gpsTracker.getLongitude());

        service = retrofit.create(ApiService.class);
        Call call = service.getDistance("imperial", origins, destination, "AIzaSyDubyVjVoTC31vIbKIk7ggi2-vFZC3nFkc");
        call.enqueue(new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("in adapter", response.body().toString());
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    JSONArray jsonArr = jsonArray.getJSONObject(0).getJSONArray("elements");
                    JSONObject jsonObjDis = jsonArr.getJSONObject(0).getJSONObject("distance");
                    JSONObject jsonObjDur = jsonArr.getJSONObject(0).getJSONObject("duration");
                    holder.textViewDistance.setText("\t" + Utils.ConvertMItoKM(jsonObjDis.getString("text")) + " "
                            + Utils.ConvertDurationToThai(jsonObjDur.getString("text")));
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("exception in adapter", e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

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
}
