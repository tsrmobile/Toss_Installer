package th.co.thiensurat.toss_installer.installation.adapter;

import android.content.Context;
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
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.Constance;

/**
 * Created by teerayut.k on 11/15/2017.
 */

public class InstallationAdapter extends RecyclerView.Adapter<InstallationAdapter.ViewHolder> {

    private Context context;
    private ClickListener clickListener;
    private List<ProductItem> productItemList = new ArrayList<ProductItem>();

    public InstallationAdapter(Context context) {
        this.context = context;
    }

    public void setInstallItemList(List<ProductItem> itemList) {
        this.productItemList = itemList;
    }


    @Override
    public InstallationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_installation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstallationAdapter.ViewHolder holder, int position) {
        ProductItem item = productItemList.get(position);
        holder.textViewName.setText((item.getProductItemCode().equals("-")) ? item.getProductName() : item.getProductItemName());
        if (item.getProductItemQty().equals("0")) {
            holder.textViewQTY.setText(item.getProductQty());
        } else {
            holder.textViewQTY.setText(item.getProductItemQty());
        }
        //holder.textViewQTY.setText((item.getProductItemQty().equals("")) ? item.getProductQty() : item.getProductItemQty());
        holder.textViewSerial.setText((item.getProductSerial().equals("")) ? "" : item.getProductSerial());
        holder.textViewStatus.setText(item.getProductStatus());

        if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_READY)) {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.LimeGreen));
        } else if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_WAIT)) {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.Red));
        } else {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productname) TextView textViewName;
        @BindView(R.id.productqty) TextView textViewQTY;
        @BindView(R.id.productstatus) TextView textViewStatus;
        @BindView(R.id.productserial) TextView textViewSerial;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onClickListener() );
            itemView.setOnLongClickListener( onLongClickListener() );
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.ClickedListener(view, getPosition());
                    }
                }
            };
        }

        private View.OnLongClickListener onLongClickListener() {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (clickListener !=null) {
                        clickListener.LongClickedListener(view, getPosition());
                        return true;
                    }
                    return false;
                }
            };
        }
    }

    public interface ClickListener{
        void ClickedListener(View view, int position);
        void LongClickedListener(View view, int position);
    }
}
