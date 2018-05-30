package th.co.thiensurat.toss_installer;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.systemnew.detail.NewDetailActivity;
import th.co.thiensurat.toss_installer.systemnew.list.ListProductFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.GPSTracker;
import th.co.thiensurat.toss_installer.utils.LocationMonitoringService;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_PERMISSIONS;

public class Main2Activity extends BaseMvpActivity<Main2Interface.Presenter>
        implements Main2Interface.View {

    private String empid;
    private GPSTracker gps;
    private boolean clickBackAain;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private boolean boolean_permission;

    private double latitue;
    private double longitude;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public void onSuccess() {

    }

    @Override
    public Main2Interface.Presenter createPresenter() {
        return Main2Presenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_main2;
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
        /*NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            if (!MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_ORDER_ID).isEmpty()) {
                Intent intent = new Intent(Main2Activity.this, NewDetailActivity.class);
                intent.putExtra(Constance.KEY_ORDER_ID,
                        MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_ORDER_ID));
                startActivityForResult(intent, Constance.REQUEST_NEW_DETAIL);
            }
        } catch (Exception e) {
            Log.e("Bundle", e.getLocalizedMessage());
        }*/
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {
        empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
        customDialog = new CustomDialog(this);

        /*if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPLOYEE_TYPE).equals("02")) {
            loadJobFragment();
        } else if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPLOYEE_TYPE).equals("03")) {
            loadJobFragment();
        } else {
            customDialog.dialogFail("คุณไม่ได้รับอนุญาต\nให้ใช้แอพพลิเคชั่นี้");
        }*/

        loadJobFragment();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

                        if (latitude != null && longitude != null) {
                            try {
                                if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LAT).equals(latitude)
                                        && MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LON).equals(longitude)) {
                                } else {
                                    getPresenter().updateCurrentLocation(empid, Double.parseDouble(latitude), Double.parseDouble(longitude));
                                }
                            } catch (Exception e) {
                                MyApplication.getInstance().getPrefManager().setLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            }
                            Log.e("Location", "Latitude : " + latitude + "\n Longitude: " + longitude);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.emp_detail) TextView textViewEmpDetail;
    //@BindView(R.id.image_profile) ImageView imageViewProfile;
    //@BindView(R.id.button_signout) FloatingActionButton buttonSignout;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        //buttonSignout.setOnClickListener(onSignout());
    }

    private void loadJobFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, ListProductFragment.getInstance()).addToBackStack(null).commit();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("รายการ");
        setSupportActionBar(toolbar);

        /*String title = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE);
        String firstname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME);
        String lastname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME);
        String empDetail = title + "" + firstname + " " + lastname;
        textViewEmpDetail.setText(empDetail);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()) {
            if (isGooglePlayServicesAvailable()) {
                Intent intent = new Intent(Main2Activity.this, LocationMonitoringService.class);
                startService(intent);
            }
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constance.REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constance.REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Constance.REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(Main2Activity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            } else {
                customDialog.dialogFail("Permission denied");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    /*private View.OnClickListener onSignout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        };
    }

    private void signout()  {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("ออกจากระบบ!")
                .showCancelButton(true)
                .setCancelText("ยกเลิก")
                .setConfirmText("ยืนยัน")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        MyApplication.getInstance().getPrefManager().clear();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }*/

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, LocationMonitoringService.class));
        super.onDestroy();
    }
}
