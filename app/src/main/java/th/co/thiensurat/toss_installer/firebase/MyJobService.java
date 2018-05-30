package th.co.thiensurat.toss_installer.firebase;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.thiensurat.toss_installer.api.ApiService;
import th.co.thiensurat.toss_installer.api.ServiceManager;

import static th.co.thiensurat.toss_installer.api.ApiURL.BASE_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_BASE_URL;

/**
 * Created by teerayut.k on 1/12/2018.
 */

public class MyJobService extends JobService {

    private ApiService service;
    private JobParameters jobParameters;
    private ServiceManager serviceManager;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        /*this.jobParameters = jobParameters;
        String id = jobParameters.getExtras().getString("empid");
        double lat = jobParameters.getExtras().getDouble("lat");
        double lon = jobParameters.getExtras().getDouble("lon");

        Log.e("start service", id + ": " + lat + ", " + lon);

        updateLocation(id, lat, lon);*/
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void updateLocation(String id, double lat, double lon) {
        serviceManager = ServiceManager.getInstance();
        serviceManager.updateLatLon(id, lat, lon, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        Log.e("msg", jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        Log.e("msg", jsonObject.getString("message"));
                    } else {
                        Log.e("msg", jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }
}
