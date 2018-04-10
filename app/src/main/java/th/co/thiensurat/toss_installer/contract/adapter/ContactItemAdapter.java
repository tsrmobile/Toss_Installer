package th.co.thiensurat.toss_installer.contract.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

/**
 * Created by teerayut.k on 3/5/2018.
 */

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private Context context;
    private DecimalFormat df = new DecimalFormat("#,###.00");
    private List<ProductItem> productItemList = new ArrayList<ProductItem>();

    public ContactItemAdapter(Context context) {
        this.context = context;
    }

    public void setContactItem(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_product_in_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductItem item = productItemList.get(position);
        holder.textViewName.setText(item.getProductName());
        holder.textViewModel.setText(item.getProductModel());
        holder.textViewSerial.setText(item.getProductSerial());
        holder.textViewPrice.setText(df.format(Float.parseFloat(item.getProductPrice())) + " บาท");

        if (!item.getProductDiscount().equals("0")) {
            holder.textViewDiscount.setText(item.getProductDiscount() + " บาท");
            float discount = Float.parseFloat(item.getProductDiscountPercent());
            holder.textViewTotal.setText(df.format(Float.parseFloat(item.getProductPrice()) - discount) + " บาท");
        } else if (!item.getProductDiscountPercent().equals("0")) {
            holder.textViewDiscount.setText(item.getProductDiscountPercent() + "%");
            float discount = Float.parseFloat(item.getProductDiscountPercent()) / 100;
            holder.textViewTotal.setText(df.format(Float.parseFloat(item.getProductPrice()) * discount) + " บาท");
        } else {
            holder.textViewDiscount.setText("0 บาท");
            holder.textViewTotal.setText(df.format(Float.parseFloat(item.getProductPrice())) + " บาท");
        }

        try {
            if (item.getProductPayType().equals("1")) {
                holder.linearLayout.setVisibility(View.GONE);
            } else {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.textViewPreriod.setText(item.getProductPayPeriods() + " งวด");
                holder.textViewPerPreriod.setText(df.format(Float.parseFloat(item.getProductPayPerPeriods())) + " บาท");
            }
        } catch(Exception e) {
            Log.e("Adapter exception", e.getMessage());
            holder.linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_model) TextView textViewModel;
        @BindView(R.id.textview_serial) TextView textViewSerial;
        @BindView(R.id.discount_price) TextView textViewDiscount;
        @BindView(R.id.normal_price) TextView textViewPrice;
        @BindView(R.id.total_price) TextView textViewTotal;
        @BindView(R.id.layout_installment) LinearLayout linearLayout;
        @BindView(R.id.textview_preriod) TextView textViewPreriod;
        @BindView(R.id.textview_perpreriod) TextView textViewPerPreriod;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
