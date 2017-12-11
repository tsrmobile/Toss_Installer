package th.co.thiensurat.toss_installer.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;

import static th.co.thiensurat.toss_installer.api.ApiURL.AUTH_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.INSTALL_ITEM_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.ITEM_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.JOB_URL;
import static th.co.thiensurat.toss_installer.api.ApiURL.SUMMARY_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @POST( AUTH_URL )
    Call<AuthItemResultGroup> getAuthen(@Body RequestAuth auth);

    @GET( SUMMARY_URL )
    Call<DashboardItemResultGroup> getSummary(@Query("data") String data, @Query("empid") String empid);

    @GET( JOB_URL )
    Call<JobItemResultGroup> getJobList(@Query("data") String data, @Query("empid") String id, @Query("location") String location);

    @GET( ITEM_URL )
    Call<InstallItemResultGroup> getInstallItem(@Query("data") String data, @Query("action") String action);
}
