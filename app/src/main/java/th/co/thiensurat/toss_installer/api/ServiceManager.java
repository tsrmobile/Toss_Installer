package th.co.thiensurat.toss_installer.api;

import android.util.Log;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static th.co.thiensurat.toss_installer.api.ApiURL.BASE_FOLDER;
import static th.co.thiensurat.toss_installer.api.ApiURL.BASE_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private static ServiceManager instance;
    private static ApiService api;
    private Call<Object> dataUpdate;
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

    /*************************************************Load Job list**********************************************/
    public Call<JobItemResultGroup> job(String data, String id) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getJob(data, id);
    }

    public void requestJob(String data, String id, final ServiceManagerCallback<JobItemResultGroup> callback) {
        job(data, id).enqueue(new Callback<JobItemResultGroup>() {
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

    /**********************************************Get Contact number*************************************************/
    public Call contact(String orderid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getContact( orderid );
    }

    public void requestContact(String orderid, final ServiceManagerCallback callback) {
        contact(orderid).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Contact", response + "");
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
    /******************************************************End*******************************************************/

    /**********************************************Update address*************************************************/
    public Call update(RequestUpdateAddress updateBody) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUpdateAddress(updateBody);
    }

    public void requestUpdateAddr(List<RequestUpdateAddress.updateBody> body, final ServiceManagerCallback callback) {
        RequestUpdateAddress requestUpdateAddress = new RequestUpdateAddress();
        requestUpdateAddress.setBody(body);

        dataUpdate = update(requestUpdateAddress);
        dataUpdate.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Update", response + "");
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
    /***************************************************End******************************************************/

    /**********************************************Update job finish*************************************************/
    /*public Call jobFinish(String action, String orderid, String start, String end, String usercode) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUpdateJobFinish(action, orderid, start, end, usercode);
    }

    public void requestUpdateJobFinish(String action, String orderid, String start, String end, String usercode, final ServiceManagerCallback callback) {
        jobFinish(action, orderid, start, end, usercode).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Finish", response + "");
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
    }*/
    /*****************************************************End********************************************************/

    /*************************************************Job Success****************************************************/
    /*public Call<JobItemResultGroup> getSuccess(String data, String empid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getJobSuccess(data, empid);
    }

    public void requestJobSuccess(String data, String empid, final ServiceManagerCallback<JobItemResultGroup> callback) {
        getSuccess(data, empid).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request Finish", response + "");
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
    }*/
    /*****************************************************End********************************************************/

    /***********************************************Job UnSuccess****************************************************/
    /*public Call<JobItemResultGroup> getUnSuccess(String data, String empid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getJobUnSuccess(data, empid);
    }

    public void requestJobUnSuccess(String data, String empid, final ServiceManagerCallback<JobItemResultGroup> callback) {
        getUnSuccess(data, empid).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request Un finish", response + "");
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
    }*/
    /*****************************************************End********************************************************/

    /***********************************************Update Serial****************************************************/
    public Call updateSerial(String action, String orderid, String productcode, String serial) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUpdateSerial(action, orderid, productcode, serial);
    }

    public void requestUpdateSerial(String action, String orderid, String productcode, String serial, final ServiceManagerCallback callback) {
        updateSerial(action, orderid, productcode, serial).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Un finish", response + "");
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

    public Call updateStatus(String action, String orderid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUpdateStatus(action, orderid);
    }

    public void requestUPdateStatus(String action, String orderid, final ServiceManagerCallback callback) {
        updateStatus(action, orderid).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Status", response + "");
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
    /***************************************************End**********************************************************/

    /***************************************************Upload**********************************************************/
    public Call upload(RequestBody requestBody, List<MultipartBody.Part> image) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUploadImage(requestBody, image);
    }

    public void requestUpload(RequestBody requestBody, List<MultipartBody.Part> body, final ServiceManagerCallback callback) {
        upload(requestBody, body).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request Upload", response + "");
                if( callback != null ){
                    Log.e("response body", response.body() + "");
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }

    public Call uploadData(RequestBody requestBody) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestUploadData(requestBody);
    }

    public void requestUploadData(RequestBody requestBody, final ServiceManagerCallback callback) {
        uploadData(requestBody).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request Upload data", response + "");
                if( callback != null ){
                    Log.e("response body", response.body() + "");
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /*****************************************************End***********************************************************/

    /*********************************************Back up or restore****************************************************/
    public Call backup(RequestBody requestBody, MultipartBody.Part file) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestBackup(requestBody, file);
    }

    public void requestBackup(RequestBody requestBody, MultipartBody.Part file, final ServiceManagerCallback callback) {
        backup(requestBody, file).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("request Back up", response + "");
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

    public Call<ResponseBody> restore(String url) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .downloadFileWithDynamicUrlAsync( url );
    }

    public void requestRestore(String url, final ServiceManagerCallback<ResponseBody> callback) {
        restore(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("request Restore", response + "");
                if( callback != null ){
                    if (response.code() == 404) {
                        callback.onSuccess( null );
                    } else if (response.code() == 200){
                        callback.onSuccess( response.body() );
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /****************************************************End************************************************************/
}
