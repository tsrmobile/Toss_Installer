package th.co.thiensurat.toss_installer.api;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.contract.item.ContactItem;

import static th.co.thiensurat.toss_installer.api.ApiURL.AUTH_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.BACKUP_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.CLOSE_JOB_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.CONTACT_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.FCM_TOKEN_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_MAP_API_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.ITEM_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_PAYMENT_PAY;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_PAYMENT_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_UPDATE_DUEDATE;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.LIST_PRODUCT_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.LOCATION_UPDATE;
import static th.co.thiensurat.toss_installer.api.ApiURL.UPDATE_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.UPLOAD_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @POST( AUTH_URL )
    Call<AuthItemResultGroup> getAuthen(@Body RequestAuth auth);

    @GET( JOB_URL )
    Call<JobItemResultGroup> getJob(@Query("data") String data, @Query("empid") String id);

    @POST( UPDATE_URL )
    Call<Object> requestUpdateAddress(@Body RequestUpdateAddress update);

    @GET( ITEM_URL )
    Call<InstallItemResultGroup> getInstallItem(@Query("data") String data, @Query("action") String action);

    @GET( UPDATE_URL )
    Call<Object> requestUpdateSerial(@Query("action") String action, @Query("orderid") String orderid, @Query("productcode") String productcode, @Query("serial") String serial);

    @GET( GOOGLE_MAP_API_URL )
    Call<Object> getDistance(@Query("units") String imperial, @Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String key);

    @GET( UPDATE_URL )
    Call<Object> requestUpdate(@Query("action") String action, @Query("cancelnote") String note, @Query("statusid") String status, @Query("empid") String empid, @Query("orderid") String orderid);

    @GET( UPDATE_URL )
    Call<Object> requestUpdateStatus(@Query("action") String action, @Query("orderid") String orderid);

    @GET( CONTACT_URL )
    Call<Object> getContact(@Query("orderid") String orderid);

    @Multipart
    @POST( UPLOAD_URL )
    Call<Object> requestUploadImage(@Part("description")RequestBody requestBody, @Part List<MultipartBody.Part> image);

    @Multipart
    @POST( UPDATE_URL )
    Call<Object> requestUploadData(@Part("description")RequestBody requestBody);

    @Multipart
    @POST( BACKUP_URL )
    //Call<Object> requestBackup(@Part("description") RequestBody description, @Part List<MultipartBody.Part> file);
    Call<Object> requestBackup(@Part("description") RequestBody description, @Part MultipartBody.Part file, @Part MultipartBody.Part filezip);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    @GET( BACKUP_URL )
    Call<Object> checkDataBackup(@Query("data") String data, @Query("description") String description);

    @GET( JOB_PAYMENT_URL )
    Call<JobItemResultGroup> getJobPayment(@Query("data") String data, @Query("empid") String id);

    @GET( JOB_PAYMENT_URL )
    Call<Object> getReceiptNumber(@Query("data") String data, @Query("contno") String contno);

    @POST( JOB_PAYMENT_PAY )
    Call<Object> requestPayment(@Body RequestPayment payment);

    @GET( JOB_UPDATE_DUEDATE )
    Call<Object> updateDueDate(@Query("orderid") String orderid, @Query("duedate") String duedate, @Query("empid") String id);


    /**
     * New api
     *
     * @param id
     * @param token
     * @return
     */
    @GET( FCM_TOKEN_URL )
    Call<Object> updateToken(@Query("id") String id, @Query("token") String token);

    @GET( LOCATION_UPDATE )
    Call<Object> updateLatlon(@Query("id") String id, @Query("lat") double lat, @Query("lon") double lon);

    @GET( LOCATION_UPDATE )
    Call<Object> updateLocation(@Query("id") String id, @Query("lat") double lat, @Query("lon") double lon);

    @GET( LIST_PRODUCT_URL )
    Call<JobItemResultGroup> getListProduct(@Query("data") String data, @Query("empid") String empid);

    @GET( LIST_PRODUCT_URL )
    Call<JobItemResultGroup> getListDetail(@Query("orderid") String orderid);

    @Multipart
    @POST( CLOSE_JOB_URL )
    Call<Object> requestCloseJob(@Part("description")RequestBody requestBody);
}
