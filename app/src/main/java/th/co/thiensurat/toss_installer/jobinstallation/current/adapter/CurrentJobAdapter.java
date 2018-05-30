package th.co.thiensurat.toss_installer.jobinstallation.current.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.Utils;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperAdapter;
import th.co.thiensurat.toss_installer.utils.helper.ItemTouchHelperViewHolder;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;

import static com.google.android.gms.internal.zzahn.runOnUiThread;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_BASE_URL;
import static th.co.thiensurat.toss_installer.utils.Utils.ConvertDateFormat;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class CurrentJobAdapter extends RecyclerView.Adapter<CurrentJobAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private String origins;
    private Context context;
    private StringBuilder sb;
    private StringBuilder stringBuilder;
    private ClickListener clickListener;
    private ChangeTintColor changeTintColor;
    private final OnStartDragListener dragListener;
    private final List<String> item = new ArrayList<>();
    private OnCustomerListChangedListener onCustomerListChangedListener;

    private Thread thread;
    private String distance;
    private String destination;
    private ApiService service;
    private List<JobItem> jobItemList = new ArrayList<>();

    public CurrentJobAdapter(FragmentActivity activity, OnStartDragListener dragStartListener,
                             OnCustomerListChangedListener listChangedListener) {
        this.context = activity;
        this.dragListener = dragStartListener;
        this.onCustomerListChangedListener= listChangedListener;
        changeTintColor = new ChangeTintColor(context);
    }

    public void setJobItemList(List<JobItem> jobItems, String origins) {
        this.jobItemList = jobItems;
        this.origins = origins;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        sb = new StringBuilder();
        stringBuilder = new StringBuilder();
        JobItem item = jobItemList.get(position);
        holder.textViewNumber.setText(String.valueOf(position +1));
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

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date date = null;
        Date endDate = null;
        try {
            date = sdf.parse(item.getInstallStartDate());
            endDate = sdf.parse(item.getInstallEndDate());
        } catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            if (item.getInstallStartDate().isEmpty()) {
                holder.textViewBegin.setVisibility(View.GONE);
                holder.textViewDate.setVisibility(View.GONE);
                holder.textViewTime.setVisibility(View.GONE);
            } else {
                holder.textViewBegin.setVisibility(View.VISIBLE);
                holder.textViewBegin.setText("เริ่มติดตั้ง: ");
                holder.textViewDate.setText(ConvertDateFormat(item.getInstallStartDate()));
                holder.textViewTime.setText(timeFormat.format(date) + " น.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!item.getDuedate().isEmpty()) {
                    date = sdf.parse(item.getDuedate());
                    holder.textViewBegin.setVisibility(View.VISIBLE);
                    holder.textViewBegin.setText("วันที่นัดชำระ: ");
                    holder.textViewDate.setText(ConvertDateFormat(item.getDuedate()));
                    holder.textViewTime.setText(timeFormat.format(date) + " น.");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                holder.textViewBegin.setVisibility(View.GONE);
                holder.textViewDate.setVisibility(View.GONE);
                holder.textViewTime.setVisibility(View.GONE);
            }
        }

        try {
            if (item.getInstallEndDate().isEmpty()) {
                holder.textViewUntil.setVisibility(View.GONE);
                holder.textViewEndDate.setVisibility(View.GONE);
                holder.textViewEndTime.setVisibility(View.GONE);
            } else {
                holder.textViewUntil.setVisibility(View.VISIBLE);
                holder.textViewUntil.setText("จนถึง: ");
                holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
                holder.textViewEndTime.setText(timeFormat.format(endDate) + " น.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!item.getInstallEnd().isEmpty()) {
                    holder.textViewUntil.setVisibility(View.VISIBLE);
                    holder.textViewUntil.setText("วันที่ติดตั้ง: ");
                    holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
                    holder.textViewEndTime.setText(timeFormat.format(endDate) + " น.");
                }
            } catch (Exception e2) {
                e.printStackTrace();
                holder.textViewUntil.setVisibility(View.GONE);
                holder.textViewEndDate.setVisibility(View.GONE);
                holder.textViewEndTime.setVisibility(View.GONE);
            }
        }

        /*try {
            if (item.getInstallEnd().isEmpty()) {
                holder.textViewUntil.setVisibility(View.GONE);
                holder.textViewEndDate.setVisibility(View.GONE);
                holder.textViewEndTime.setVisibility(View.GONE);
            } else  {
                holder.textViewUntil.setVisibility(View.VISIBLE);
                holder.textViewUntil.setText("วันที่ติดตั้ง: ");
                holder.textViewEndDate.setText(ConvertDateFormat(item.getInstallEndDate()));
                holder.textViewEndTime.setText(timeFormat.format(endDate) + " น.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.textViewUntil.setVisibility(View.GONE);
            holder.textViewEndDate.setVisibility(View.GONE);
            holder.textViewEndTime.setVisibility(View.GONE);
        }

        try {
            if (item.getDuedate().isEmpty()) {
                holder.textViewBegin.setVisibility(View.GONE);
                holder.textViewDate.setVisibility(View.GONE);
                holder.textViewTime.setVisibility(View.GONE);
            } else {
                date = sdf.parse(item.getDuedate());
                holder.textViewBegin.setVisibility(View.VISIBLE);
                holder.textViewBegin.setText("วันที่นัดชำระ: ");
                holder.textViewDate.setText(ConvertDateFormat(item.getDuedate()));
                holder.textViewTime.setText(timeFormat.format(date) + " น.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.textViewBegin.setVisibility(View.GONE);
            holder.textViewBegin.setVisibility(View.GONE);
            holder.textViewUntil.setVisibility(View.GONE);
        }*/

        List<AddressItem> addressItems  = item.getAddress();
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

                destination = addressItem.getSubdistrict() + "+" + addressItem.getDistrict() + "+" + addressItem.getProvince();
            }
        }

        changeTintColor.setTextViewDrawableColor(holder.textViewDistanceTitle, R.color.colorPrimaryDark);

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


        synchronized (context) {
            thread = new Thread(new Runnable() {
                public void run(){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GOOGLE_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    service = retrofit.create(ApiService.class);
                    Call call = service.getDistance("imperial", origins, destination, "AIzaSyDubyVjVoTC31vIbKIk7ggi2-vFZC3nFkc");
                    call.enqueue(new Callback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {

                            }
                            Log.e("distance", response + "");
                            Gson gson = new Gson();
                            try {
                                JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                                JSONArray jsonArr = jsonArray.getJSONObject(0).getJSONArray("elements");
                                JSONObject jsonObjDis = jsonArr.getJSONObject(0).getJSONObject("distance");
                                JSONObject jsonObjDur = jsonArr.getJSONObject(0).getJSONObject("duration");
                                distance = " \t\t" + Utils.ConvertMItoKM(jsonObjDis.getString("text")) + "\t"
                                        + Utils.ConvertDurationToThai(jsonObjDur.getString("text"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.textViewDistance.setText(distance);
                                    }
                                });
                            } catch (JSONException e) {
                                Log.e("exception in adapter", e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e("Distance failre", t.getLocalizedMessage());
                        }
                    });
                }
            });
        }
        thread.start();
    }

    public synchronized void stopThread() {
        if (thread != null) {
            thread.interrupt();
        }
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
        @BindView(R.id.title_until) TextView textViewUntil;
        @BindView(R.id.title_begin) TextView textViewBegin;
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
