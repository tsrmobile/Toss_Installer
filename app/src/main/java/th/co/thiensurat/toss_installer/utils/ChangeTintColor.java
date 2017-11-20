package th.co.thiensurat.toss_installer.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by teerayut.k on 11/8/2017.
 */

public class ChangeTintColor {

    private Context context;

    public ChangeTintColor(Context context) {
        this.context = context;
    }

    public ChangeTintColor(FragmentActivity context) {
        this.context = context;
    }

    public void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public void setEditTextDrawableColor(EditText editText, int color) {
        for (Drawable drawable : editText.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }
}
