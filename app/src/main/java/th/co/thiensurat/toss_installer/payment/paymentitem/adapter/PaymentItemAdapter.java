package th.co.thiensurat.toss_installer.payment.paymentitem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

public class PaymentItemAdapter extends RecyclerView.Adapter<PaymentItemAdapter.ViewHolder> {

    private String code;
    private JobItem jobItem;
    private Context context;
    private StringBuilder stringBuilder;
    private ClickListener clickListener;
    private List<ProductItem> productItemList = new ArrayList<>();

    public PaymentItemAdapter(Context context) {
        this.context = context;
    }

    public void setProductItem(JobItem jobItem, List<ProductItem> productItemList) {
        this.jobItem = jobItem;
        this.productItemList = productItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewNumber.setText(String.valueOf(position +1));
        ProductItem item = productItemList.get(position);
        int qty = 0;
        stringBuilder = new StringBuilder();
        String temp = item.getProductCode();
        String temp2 = "";
        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem productItem = productItemList.get(i);
            if (temp2.isEmpty()) {
                temp2 = temp;
                qty = Integer.parseInt(productItem.getProductQty());
                //stringBuilder.append(contno + "\n" + productItem.getProductName() + " จำนวน " + qty + " เครื่อง/ชิ้น");
            } else if (temp2.equals(temp)) {
                qty += Integer.parseInt(productItem.getProductQty());
                //stringBuilder.append(contno + "\n" + productItem.getProductName() + " จำนวน " + qty + " เครื่อง/ชิ้น");
            } else if (temp2 != temp) {
                temp2 = temp;
                qty = Integer.parseInt(productItem.getProductQty());
                //stringBuilder.append(contno + "\n" + productItem.getProductName() + " จำนวน " + qty + " เครื่อง/ชิ้น");
            }

            stringBuilder.append(jobItem.getContno() + "\n" + productItem.getProductName() + " จำนวน " + qty + " เครื่อง/ชิ้น");
        }

        holder.textViewName.setText(jobItem.getTitle() + jobItem.getFirstName() + " " + jobItem.getLastName());
        holder.textViewCode.setText(item.getProductCode());
        holder.textViewProduct.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public String getCode() {
        return code;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_number) TextView textViewNumber;
        @BindView(R.id.textview_product) TextView textViewProduct;
        @BindView(R.id.textview_product_code) TextView textViewCode;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onClickListener());
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        code = textViewCode.getText().toString();
                        clickListener.onItemClick(v, getPosition());
                    }
                }
            };
        }
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}
