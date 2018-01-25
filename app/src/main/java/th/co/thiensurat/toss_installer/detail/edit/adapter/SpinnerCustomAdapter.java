package th.co.thiensurat.toss_installer.detail.edit.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;


/**
 * Created by teera-s on 11/11/2016 AD.
 */

public class SpinnerCustomAdapter extends ArrayAdapter<String> {

    private Context activity;
    private List data;
    public Resources res;
    private String header;
    LayoutInflater inflater;
    DataItem model;

    /*************  CustomAdapter Constructor *****************/
    public SpinnerCustomAdapter(Context activitySpinner, int textViewResourceId, List objects, Resources resLocal, String header) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;
        this.header = header;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        /***** Get each Model object from Arraylist ********/
        model = null;
        model = (DataItem) data.get(position);

        TextView textView = (TextView) row.findViewById(R.id.row_item);

        if(position == 0) {
            switch (header) {
                case "province" :
                    textView.setText(activity.getResources().getString(R.string.spinner_province));
                    textView.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    break;
                case "district" :
                    textView.setText(activity.getResources().getString(R.string.spinner_district));
                    textView.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    break;
                case "subdistrict" :
                    textView.setText(activity.getResources().getString(R.string.spinner_sub_district));
                    textView.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    break;
                default: break;
            }
        } else {
            textView.setText(model.getDataName());
            textView.setTextColor(activity.getResources().getColor(R.color.Black));
        }
        return row;
    }
}
