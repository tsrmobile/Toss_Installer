package th.co.thiensurat.toss_installer.takepicture.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;

/**
 * Created by teerayut.k on 11/16/2017.
 */

public class TakePictureAdapter extends RecyclerView.Adapter<TakePictureAdapter.ViewHolder> {

    private Context context;
    private ClickListener clickListener;
    private List<ImageItem> imageItemList = new ArrayList<ImageItem>();

    public TakePictureAdapter(Context context) {
        this.context = context;
    }

    public void setPictureItem(List<ImageItem> itemList) {
        this.imageItemList = itemList;
    }

    @Override
    public TakePictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_picture_installation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TakePictureAdapter.ViewHolder holder, int position) {
        ImageItem item = imageItemList.get(position);
        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageItemList.size();
    }

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view) ImageView imageView;
        @BindView(R.id.layout_remove) RelativeLayout relativeLayoutRemove;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener( onItemClick() );
            relativeLayoutRemove.setOnClickListener( onDel() );
        }

        private View.OnClickListener onItemClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.itemClicked(view, getPosition());
                    }
                }
            };
        }

        private View.OnClickListener onDel() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.delClicked(view, getPosition());
                    }
                }
            };
        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
        void delClicked(View view, int position);
    }
}
