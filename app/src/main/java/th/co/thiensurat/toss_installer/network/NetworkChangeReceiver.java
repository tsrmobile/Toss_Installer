package th.co.thiensurat.toss_installer.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			String status = NetworkUtil.getConnectivityStatusString(context);
			if (status.equals("Wifi enabled") || status.equals("Mobile data enabled")) {
				//NetworkErrorActivity.getInstance().detectWifiConnected("connect");
			} else {
				//NetworkErrorActivity.getInstance().detectWifiConnected("not connect");
			}
		} catch (Exception e) {

		}
	}
}
