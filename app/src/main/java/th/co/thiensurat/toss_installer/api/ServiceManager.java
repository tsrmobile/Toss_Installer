package th.co.thiensurat.toss_installer.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;

import static th.co.thiensurat.toss_installer.api.ApiURL.BASE_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private static ServiceManager instance;
    private static ApiService api;
    private Call<AuthItemResultGroup> requestAuthenCall;

    public interface ServiceManagerCallback<T>{
        void onSuccess(T result);

        void onFailure(Throwable t);
    }

    public static ServiceManager getInstance(){
        if( instance == null ){
            instance = new ServiceManager();
        }
        return instance;
    }

    private ServiceManager(){
    }

    public static void setApi( ApiService mockApi ){
        api = mockApi;
    }

    /*************************************************Authentication*************************************************/
    public Call<AuthItemResultGroup> auth(RequestAuth body) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getAuthen(body);
    }

    public void getAuth(List<RequestAuth.authenBody> items, final ServiceManagerCallback<AuthItemResultGroup> callback) {
        RequestAuth body = new RequestAuth();
        body.setBody(items);

        requestAuthenCall = auth( body );
        requestAuthenCall.enqueue(new Callback<AuthItemResultGroup>() {
            @Override
            public void onResponse(Call<AuthItemResultGroup> call, Response<AuthItemResultGroup> response) {
                Log.e("request Authen", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<AuthItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /*************************************************End********************************************************/

    /*************************************************Load Summary***********************************************/
    public Call<DashboardItemResultGroup> summary(String data, String empid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getSummary(data, empid);
    }

    public void getSummary(String data, String empid, final ServiceManagerCallback<DashboardItemResultGroup> callback) {
        summary(data, empid).enqueue(new Callback<DashboardItemResultGroup>() {
            @Override
            public void onResponse(Call<DashboardItemResultGroup> call, Response<DashboardItemResultGroup> response) {
                Log.e("request Summary", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<DashboardItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /*************************************************End********************************************************/

    /*************************************************Load Job list**********************************************/
    public Call<JobItemResultGroup> joblist(String data, String id) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getJobList(data, id);
    }

    public void getJob(String data, String id, final ServiceManagerCallback<JobItemResultGroup> callback) {
        joblist(data, id).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request Job list", response + ", " + response.body().getStatus());
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<JobItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /*************************************************End********************************************************/

    /********************************************Load Install item************************************************/
    public Call<InstallItemResultGroup> installItem(String data, String action) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getInstallItem(data, action);
    }

    public void loadInstallItem(String data, String action, final ServiceManagerCallback<InstallItemResultGroup> callback) {
        installItem(data, action).enqueue(new Callback<InstallItemResultGroup>() {
            @Override
            public void onResponse(Call<InstallItemResultGroup> call, Response<InstallItemResultGroup> response) {
                Log.e("request Install item", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<InstallItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /*************************************************End********************************************************/

    /********************************************Get distance************************************************/
    public Call distance(String units, String origins, String destination, String key) {
        return Service.newInstance( GOOGLE_BASE_URL )
                .getApi( api )
                .getDistance(units, origins, destination, key);
    }
    
    public void getDistance(String units, String origins, String destination, String key, final ServiceManagerCallback callback) {
        distance(units, origins, destination, key).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Distance", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /************************************************End*****************************************************/

    /********************************************Cancel order***********************************************/
    public Call cancel(String action, String note, String status, String empid, String orderid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUpdate(action, note, status, empid, orderid);
    }

    public void requestCancel(String note, String status, String empid, String orderid, final ServiceManagerCallback callback) {
        cancel("cancel", note, status, empid, orderid).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Cancel", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }

    /*************************************************End***************************************************/
}
