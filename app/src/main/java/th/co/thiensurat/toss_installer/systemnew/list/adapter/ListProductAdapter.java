package th.co.thiensurat.toss_installer.systemnew.list.adapter;

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
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.payment.paymentfragment.paymentjob.PaymentFragment;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {

    private Context context;
    private StringBuilder sb;
    private StringBuilder stringBuilder;
    private ClickListener clickListener;
    private List<JobItem> jobItemList = new ArrayList<>();

    public ListProductAdapter(FragmentActivity context) {
        this.context = context;
    }

    public void setJobItemList(List<JobItem> jobItems) {
        this.jobItemList = jobItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sendproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sb = new StringBuilder();
        stringBuilder = new StringBuilder();
        JobItem item = jobItemList.get(position);
        holder.textViewNumber.setText(String.valueOf((position + 1)));

        holder.textViewName.setText(item.getTitle() + "" + item.getFirstName() + " " + item.getLastName());

        String temp = item.getOrderid();
        String temp2 = "";
        for (int i = 0; i < item.getProduct().size(); i++) {
            ProductItem productItem = item.getProduct().get(i);
            if (temp2.isEmpty()) {
                temp2 = temp;
                stringBuilder.append("เลขที่สั่งซื้อ #" + item.getOrderid() + "\n" + (i + 1) + ". " + productItem.getProductName() + " จำนวน " + productItem.getProductQty() + " เครื่อง/ชิ้น");
            } else if (temp2.equals(temp)) {
                stringBuilder.append("\n" + (i + 1) + ". " + productItem.getProductName() + " จำนวน " + productItem.getProductQty() + " เครื่อง/ชิ้น");
            }
        }

        holder.textViewProduct.setText(stringBuilder.toString());

        List<AddressItem> addressItems  = item.getAddress();
        for (int i = 0; i < addressItems.size(); i++) {
            AddressItem addressItem = addressItems.get(i);
            if (addressItem.getAddressType().equals("AddressInstall")) {
                sb.append("ที่อยู่: " + addressItem.getAddrDetail());
                sb.append("\n");
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : "" + item.getAddress().get(i).getSubdistrict());
                sb.append((addressItem.getDistrict().equals("")) ? "" : " " + addressItem.getDistrict());
                sb.append("\n");
                sb.append((addressItem.getProvince().equals("")) ? "" : "" + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());

                holder.textViewAddress.setText(sb.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_number) TextView textViewNumber;
        @BindView(R.id.textview_name) TextView textViewName;
        @BindView(R.id.textview_product) TextView textViewProduct;
        @BindView(R.id.textview_address) TextView textViewAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onClick());
        }

        private View.OnClickListener onClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
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
