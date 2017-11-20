package th.co.thiensurat.toss_installer.dashboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItem;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> {

    private Context context;
    private List<DashboardItem> dashboardItems = new ArrayList<DashboardItem>();

    public DashboardItemAdapter(FragmentActivity activity) {
        this.context = activity;
    }

    public void setDashboardList(List<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

    @Override
    public DashboardItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardItemAdapter.ViewHolder holder, int position) {
        DashboardItem item = dashboardItems.get(position);

        holder.textViewTotal.setText(String.valueOf(item.getJobnow()));
        holder.textViewTotal2.setText(String.valueOf(item.getJoball()));
        holder.textViewTotal3.setText(String.valueOf(item.getJobsuccess()));
    }

    @Override
    public int getItemCount() {
        return dashboardItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dashboard_total) TextView textViewTotal;
        @BindView(R.id.dashboard_total2) TextView textViewTotal2;
        @BindView(R.id.dashboard_total3) TextView textViewTotal3;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
