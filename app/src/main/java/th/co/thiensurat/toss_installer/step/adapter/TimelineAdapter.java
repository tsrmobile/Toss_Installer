package th.co.thiensurat.toss_installer.step.adapter;

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

/**
 * Created by teerayut.k on 1/30/2018.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private Context context;
    private ClickListener clickListener;
    private List<String> strings = new ArrayList<String>();

    public TimelineAdapter(Context context) {
        this.context = context;
    }

    public void setTimelineItem(List<String> stringList) {
        this.strings = stringList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int number = (position + 1);
        String name = strings.get(position);
        holder.stepNumber.setText(String.valueOf(number));
        holder.stepName.setText(name);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.stepnumber) TextView stepNumber;
        @BindView(R.id.stepname) TextView stepName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onClickListener() );
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.stepClick(view, getPosition());
                    }
                }
            };
        }
    }

    public interface ClickListener {
        void stepClick(View view, int position);
    }
}
