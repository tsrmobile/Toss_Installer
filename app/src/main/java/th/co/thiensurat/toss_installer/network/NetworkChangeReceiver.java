package th.co.thiensurat.toss_installer.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import th.co.thiensurat.toss_installer.MainActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			//MainActivity.getInstance().detectWifiConnected(NetworkUtil.getConnectivityStatus(context));
		} catch (Exception e) {

		}
	}
}
