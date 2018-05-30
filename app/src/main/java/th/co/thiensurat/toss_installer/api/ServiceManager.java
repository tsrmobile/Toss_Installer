package th.co.thiensurat.toss_installer.api;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.payment.activity.PaymentActivity;
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
    private Call<Object> requestPayment;
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
    public Call backup(RequestBody requestBody, MultipartBody.Part file, MultipartBody.Part filezip) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestBackup(requestBody, file, filezip);
    }

    public void requestBackup(RequestBody requestBody, MultipartBody.Part file, MultipartBody.Part filezip, final ServiceManagerCallback callback) {
        backup(requestBody, file, filezip).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
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

    public Call<Object> check(String data, String description) {
        return Service.newInstance(BASE_URL)
                .getApi(api)
                .checkDataBackup(data, description);
    }

    public void requestCheckBackup(String data, String description, final ServiceManagerCallback<Object> callback) {
        check(data, description).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request Check data", response + "");
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

    /*************************************************Load Job list**********************************************/
    public Call<JobItemResultGroup> jobPayment(String data, String id) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getJobPayment(data, id);
    }

    public void requestJobPayment(String data, String id, final ServiceManagerCallback<JobItemResultGroup> callback) {
        jobPayment(data, id).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request Job payment", response + ", " + response.body().getStatus());
                if (callback != null) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<JobItemResultGroup> call, Throwable t) {
                try {
                    if (callback != null) {
                        callback.onFailure(t);
                    }
                } catch (Exception e) {

                }
            }
        });
    }
    /*************************************************End********************************************************/

    /********************************************Change duedate**************************************************/
    public Call<Object> dueDate(String orderid, String duedate, String empid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .updateDueDate(orderid, duedate, empid);
    }

    public void requestUpdateDueDate(String orderid, String duedate, String empid, final ServiceManagerCallback<Object> callback) {
        dueDate(orderid, duedate, empid).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request duedate", response + "");
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
    /*************************************************End********************************************************/

    /******************************************Get receipt number************************************************/
    public Call<Object> receiptNumber(String data, String contno) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getReceiptNumber(data, contno);
    }

    public void requestReceiptNumber(String data, String contno, final ServiceManagerCallback<Object> callback) {
        receiptNumber(data, contno).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request Job payment", response + "");
                if( callback != null ){
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
    /*************************************************End********************************************************/

    /************************************************Payment*****************************************************/
    public Call<Object> payment(RequestPayment body) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestPayment(body);
    }

    public void requestPayment(List<RequestPayment.paymentBody> items, final ServiceManagerCallback<Object> callback) {
        RequestPayment body = new RequestPayment();
        body.setBody(items);

        requestPayment = payment( body );
        requestPayment.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request pay", response + "");
                if( callback != null ){
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
    /*****************************************************End**********************************************************/

    /**
     *
     * New api
     *
     */

    /*************************************************Update token**************************************************/
    public Call<Object> token(String id, String token) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .updateToken(id, token);
    }

    public void updateFCMToken(String id, String token, final ServiceManagerCallback callback) {
        token(id, token).enqueue(new Callback<Object>() {


            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request update token", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("FCM", t.getLocalizedMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }

    /******************************************************End******************************************************/

    /*************************************************Update token**************************************************/
    public Call<Object> latlon(String id, double lat, double lon) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .updateLocation(id, lat, lon);
    }

    public void updateLatLon(String id, double lat, double lon, final ServiceManagerCallback callback) {
        latlon(id, lat, lon).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("request update LatLon", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("failure update LatLon", t.getLocalizedMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /******************************************************End******************************************************/

    /*************************************************Get List**************************************************/
    public Call<JobItemResultGroup> getList(String data, String empid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getListProduct(data, empid);
    }

    public void getListProduct(String data, String empid, final ServiceManagerCallback callback) {
        getList(data, empid).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request get list", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<JobItemResultGroup> call, Throwable t) {
                Log.e("failure list", t.getLocalizedMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /******************************************************End******************************************************/

    /**********************************************Get list detail**********************************************/
    public Call<JobItemResultGroup> getDetail(String orderid) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getListDetail(orderid);
    }

    public void getDetailList(String orderid, final ServiceManagerCallback callback) {
        getDetail(orderid).enqueue(new Callback<JobItemResultGroup>() {
            @Override
            public void onResponse(Call<JobItemResultGroup> call, Response<JobItemResultGroup> response) {
                Log.e("request list detail", response + "");
                if( callback != null ){
                    callback.onSuccess( response.body() );
                }
            }

            @Override
            public void onFailure(Call<JobItemResultGroup> call, Throwable t) {
                Log.e("failure list detail", t.getLocalizedMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
            }
        });
    }
    /****************************************************End****************************************************/

    /*************************************************Close Job*************************************************/
    public Call close(RequestBody requestBody) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .requestCloseJob(requestBody);
    }

    public void closeJob(RequestBody requestBody, final ServiceManagerCallback callback) {
        close(requestBody).enqueue(new Callback<Object>() {

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

}
