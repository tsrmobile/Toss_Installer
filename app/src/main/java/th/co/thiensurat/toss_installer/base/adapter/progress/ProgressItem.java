package th.co.thiensurat.toss_installer.base.adapter.progress;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;
import th.co.thiensurat.toss_installer.base.adapter.BaseItemType;


/**
 * Created by thekhaeng on 4/6/2017 AD.
 */

public class ProgressItem extends BaseItem {
    public ProgressItem(){
        super( BaseItemType.TYPE_PROGRESS );
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags ){
        super.writeToParcel( dest, flags );
        dest.writeInt( this.type );
    }

    protected ProgressItem( Parcel in ){
        super( in );
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<ProgressItem> CREATOR = new Parcelable.Creator<ProgressItem>(){
        @Override
        public ProgressItem createFromParcel( Parcel source ){
            return new ProgressItem( source );
        }

        @Override
        public ProgressItem[] newArray( int size ){
            return new ProgressItem[size];
        }
    };
}
