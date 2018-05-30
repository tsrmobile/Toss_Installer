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
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

public class PaymentItemAdapter extends RecyclerView.Adapter<PaymentItemAdapter.ViewHolder> {

    private String code;
    private String status;
    private JobItem jobItem;
    private Context context;
    private DBHelper dbHelper;
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
        String temp = item.getProductCode();
        String temp2 = "";
        if (temp2.isEmpty()) {
            temp2 = temp;
            qty = Integer.parseInt(item.getProductQty());
        } else if (temp2.equals(temp)) {
            temp2 = temp;
            qty += Integer.parseInt(item.getProductQty());
        } else {
            temp2 = temp;
            qty = Integer.parseInt(item.getProductQty());
        }

        holder.textViewName.setText("เลขที่สัญญา: " + jobItem.getContno() + "\nเลขที่อ้างอิง: " + jobItem.getOrderid()
                + "\nการชำระเงิน: " + ((item.getProductPayType().equals("2")) ? "เงินผ่อน" : "เงินสด") + "");
        holder.textViewCode.setText(item.getProductCode());
        holder.textViewProduct.setText(item.getProductName() + " จำนวน " + qty + " เครื่อง/ชิ้น");

        if (checkPayment(item.getProductCode())) {
            holder.textViewStatus.setText("จ่ายแล้ว");
        } else {
            holder.textViewStatus.setText("");
        }
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

    public String getStatus() {
        return status;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_number) TextView textViewNumber;
        @BindView(R.id.textview_product) TextView textViewProduct;
        @BindView(R.id.textview_product_code) TextView textViewCode;
        @BindView(R.id.payment_status) TextView textViewStatus;
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
                        status = textViewStatus.getText().toString();
                        clickListener.onItemClick(v, getPosition());
                    }
                }
            };
        }
    }

    private boolean checkPayment(String code) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getPaymentStatus(code);
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}
