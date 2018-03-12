package th.co.thiensurat.toss_installer.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class NetworkUtil {
	
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int MOBILE_SPEED_1 = 3;
	public static int MOBILE_SPEED_2 = 4;
	public static int MOBILE_SPEED_3 = 5;
	public static int MOBILE_SPEED_4 = 6;
	public static int TYPE_NOT_CONNECTED = 0;
	
	
	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX
					|| activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
				return TYPE_WIFI;
			} else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				if (activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE || activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP
						|| activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD) {
					return MOBILE_SPEED_3;
				} else if (activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS || activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
						|| activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0 || activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A
						|| activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B || activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA) {
					return MOBILE_SPEED_2;
				} else if (activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
					return MOBILE_SPEED_1;
				} else {
					Log.e("type", activeNetwork.getSubtypeName() + ", ");
					return TYPE_MOBILE;
				}
			}
		}
		return TYPE_NOT_CONNECTED;
	}
}
