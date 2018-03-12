package th.co.thiensurat.toss_installer.stepview.adapter;

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
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;

/**
 * Created by teerayut.k on 2/14/2018.
 */

public class StepViewAdapter extends RecyclerView.Adapter<StepViewAdapter.ViewHolder> {

    private Context context;
    private ClickListerner clickListerner;
    private ChangeTintColor changeTintColor;
    private List<String> stringList;
    private List<String> stringListValues = new ArrayList<String>();

    public StepViewAdapter(Context context) {
        this.context = context;
        initialEvent();
        changeTintColor = new ChangeTintColor(context);
    }

    public void setValueAdapter(List<String> strings) {
        this.stringListValues = strings;
    }

    private void initialEvent() {
        stringList = new ArrayList<>();
        stringList.add("ตรวจสอบและแก้ไขข้อมูล");
        stringList.add("สแกนสินค้าที่จะติดตั้ง");
        stringList.add("ถ่ายรูปการติดตั้ง");
        stringList.add("ถ่ายรูปบัตรประชาชน");
        stringList.add("ถ่ายรูปที่อยู่อาศัย");
        stringList.add("เช็คอินและถ่ายรูปตำแหน่งที่ติดตั้ง");
        stringList.add("พิมพ์สัญญาและใบรับการติดตั้ง");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = stringList.get(position);
        String value = stringListValues.get(position);
        holder.textViewName.setText((position + 1) + ". " +name);

        if (value.equals("1")) {
            holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_black_18dp, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewName, R.color.LimeGreen);
        } else {
            holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_circle_black_18dp, 0);
            changeTintColor.setTextViewDrawableColor(holder.textViewName, R.color.DarkGray);
        }

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void setClickListerner(ClickListerner clickListerner) {
        this.clickListerner = clickListerner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name) TextView textViewName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onClickListener() );
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListerner != null) {
                        clickListerner.clickListener(view, getPosition());
                    }
                }
            };
        }

    }

    public interface ClickListerner {
        void clickListener(View view, int position);
    }
}
