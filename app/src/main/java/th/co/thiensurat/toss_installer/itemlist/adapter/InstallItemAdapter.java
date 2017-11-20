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
import th.co.thiensurat.toss_installer.job.adapter.JobAdapter;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItemAdapter extends RecyclerView.Adapter<InstallItemAdapter.ViewHolder> {

    private Context context;
    private List<InstallItem> installItemList = new ArrayList<InstallItem>();

    public InstallItemAdapter(FragmentActivity activity) {
        this.context = activity;
    }

    public void setInstallItemList(List<InstallItem> installItemList) {
        this.installItemList = installItemList;
    }

    @Override
    public InstallItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_installitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstallItemAdapter.ViewHolder holder, int position) {
        InstallItem installItem = installItemList.get(position);
        holder.textViewOrderID.setText(installItem.getOrderid());
        holder.textViewName.setText(installItem.getProductCode() + " " + installItem.getProductName());
        holder.textViewQty.setText(installItem.getProductQty());
    }

    @Override
    public int getItemCount() {
        return installItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderid) TextView textViewOrderID;
        @BindView(R.id.productname) TextView textViewName;
        @BindView(R.id.productqty) TextView textViewQty;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
