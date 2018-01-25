package th.co.thiensurat.toss_installer.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.request.RequestFinish;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.ContactResultGroup;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.data.DataResultGroup;

import static th.co.thiensurat.toss_installer.api.ApiURL.AUTH_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.CONTACT_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.GOOGLE_MAP_API_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.INSTALL_ITEM_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.ITEM_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.SUMMARY_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.UPDATE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @POST( AUTH_URL )
    Call<AuthItemResultGroup> getAuthen(@Body RequestAuth auth);

    @GET( SUMMARY_URL )
    Call<DashboardItemResultGroup> getSummary(@Query("data") String data, @Query("empid") String empid);

    @GET( JOB_URL )
    Call<JobItemResultGroup> getJobList(@Query("data") String data, @Query("empid") String id);

    @GET( ITEM_URL )
    Call<InstallItemResultGroup> getInstallItem(@Query("data") String data, @Query("action") String action);

    @GET( UPDATE_URL )
    Call<Object> requestUpdate(@Query("action") String action, @Query("cancelnote") String note, @Query("statusid") String status, @Query("empid") String empid, @Query("orderid") String orderid);

    @POST( UPDATE_URL )
    Call<Object> requestUpdateAddress(@Body RequestUpdateAddress update);

    @GET( UPDATE_URL )
    Call<Object> requestUpdateJobFinish(@Query("action") String action, @Query("orderid") String orderid, @Query("installstart") String start, @Query("installend") String end, @Query("usercode") String usercode);

    @Multipart
    @POST( UPDATE_URL )
    Call<Object> requestJobFinish(@Query("action") String action, @Part RequestFinish body);

    @GET( GOOGLE_MAP_API_URL )
    Call<Object> getDistance(@Query("units") String imperial, @Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String key);

    @GET ( CONTACT_URL )
    Call<ContactResultGroup> getContact();
}
