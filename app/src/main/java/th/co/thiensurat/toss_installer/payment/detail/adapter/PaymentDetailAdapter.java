package th.co.thiensurat.toss_installer.payment.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.ViewHolder> {

    private Context context;
    private String productCode;
    private List<ProductItem> productItemList;

    public PaymentDetailAdapter(Context activity) {
        this.context = activity;
    }

    public void setProductList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }

    @Override
    public PaymentDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_payment_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentDetailAdapter.ViewHolder holder, int position) {
        ProductItem item = productItemList.get(position);
        holder.textViewName.setText(item.getProductName());
        holder.textViewModel.setText(item.getProductModel());
        holder.textViewSerial.setText(item.getProductSerial());
        /*try {
            if (item.getProductCode().equals(productCode)) {
                holder.textViewName.setText(item.getProductName());
                holder.textViewModel.setText(item.getProductModel());
                holder.textViewSerial.setText(item.getProductSerial());
            } else {
                holder.linearLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.textViewName.setText(item.getProductName());
            holder.textViewModel.setText(item.getProductModel());
            holder.textViewSerial.setText(item.getProductSerial());
        }*/
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_model) TextView textViewModel;
        @BindView(R.id.textview_serial) TextView textViewSerial;
        @BindView(R.id.main_layout) LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
