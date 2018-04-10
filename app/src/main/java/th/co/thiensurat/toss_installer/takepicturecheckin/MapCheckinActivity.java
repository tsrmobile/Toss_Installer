package th.co.thiensurat.toss_installer.takepicturecheckin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.takepicturecheckin.result.CheckinResultActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class MapCheckinActivity extends BaseMvpActivity<MapCheckinInterface.Presenter>
        implements MapCheckinInterface.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {

    private LatLng latLng;
    private JobItem jobItem;
    private Uri pictureUri;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private double longitude;
    private double latitude;
    private LocationRequest locationRequest;
    private Location currentLocation;

    private String id = "-1";
    private String serial;
    private String productcode;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private ImageConfiguration imageConfiguration;

    @Override
    public MapCheckinInterface.Presenter createPresenter() {
        return MapCheckinPresenter.create(MapCheckinActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_map_checkin;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.floating_camera) Button floatingActionButtonCamera;
    @BindView(R.id.map_layout) LinearLayout linearLayoutMap;

    @Override
    public void bindView() {
        ButterKnife.bind(this);
        floatingActionButtonCamera.setOnClickListener(onCapture());
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(MapCheckinActivity.this);
        imageConfiguration = new ImageConfiguration(MapCheckinActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (checkGPSEnable()) {
            buildGoogleApiClient();
        } else {
            displayLocationSettingsRequest();
        }
        googleApiClient.connect();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        getPresenter().checkImage(jobItem.getOrderid(), Constance.IMAGE_TYPE_CHECKIN);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("เช็คอินตำแหน่ง");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        productcode = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_PRODUCT_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.menu_gps) {
            if (checkLocationPermission() && checkGPSEnable()) {
                if (googleApiClient.isConnected()) {
                    getCurrentLocation();
                } else {
                    if (googleApiClient == null) {
                        buildGoogleApiClient();
                        googleApiClient.connect();
                    }
                    getCurrentLocation();
                }
            } else {
                displayLocationSettingsRequest();
            }
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(MapCheckinActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void moveMap() {
        if (mMap != null) {
            mMap.clear();
        }
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        moveMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(this);

        LatLng latLng = new LatLng(13.881021, 100.405851);
        mMap.addMarker(new MarkerOptions().position(latLng).title("").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            currentLocation = location;
            if (mMap != null)
                mMap.clear();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            moveMap();
            if (googleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }
        } catch (Exception e) {
            Log.e("location exception", e.getMessage());
        }
    }

    private void getCurrentLocation() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MapCheckinActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapCheckinActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapCheckinActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(MapCheckinActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MapCheckinActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    googleApiClient.connect();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                customDialog.dialogFail("Permission denied");
            }
        } else if (requestCode == Constance.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                customDialog.dialogFail("กรุณาเลือก \"อนุญาต\" หรือ Allow\\nเพื่อให้แอพฯ สามารถบันทึกภาพได้");
            } else {
                captureScreen();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constance.REQUEST_CHECKIN_RESULT) {
                buildGoogleApiClient();
                googleApiClient.connect();
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == Constance.REQUEST_CHECKIN_RESULT) {
                buildGoogleApiClient();
                googleApiClient.connect();
                id = data.getStringExtra("Image_ID");
                Log.e("image id", data.getStringExtra("Image_ID"));
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(MapCheckinActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    private View.OnClickListener onCapture() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButtonCamera.startAnimation(new AnimateButton().animbutton());
                verifyStoragePermissions(MapCheckinActivity.this);
            }
        };
    }

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, Constance.PERMISSIONS_STORAGE,
                    Constance.REQUEST_EXTERNAL_STORAGE
            );
        } else {
            captureScreen();
        }
    }

    private boolean checkGPSEnable() {
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void resultCheckin() {
        Intent intent = new Intent(MapCheckinActivity.this, CheckinResultActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        startActivityForResult(intent, Constance.REQUEST_CHECKIN_RESULT);
    }

    private void displayLocationSettingsRequest() {
        buildGoogleApiClient();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        buildGoogleApiClient();
                        googleApiClient.connect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapCheckinActivity.this, Constance.REQUEST_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    public void captureScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                Bitmap bitmap = snapshot;
                try {
                    File imageFile = imageConfiguration.createImageByType(jobItem.getOrderid(), "", Constance.IMAGE_TYPE_CHECKIN);
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    pictureUri = Uri.fromFile( imageFile );
                } catch (FileNotFoundException e) {

                } catch (IOException e) {
                }

                if (id.equals("-1")) {
                    getPresenter().saveImageUrl(jobItem.getOrderid(), Constance.IMAGE_TYPE_CHECKIN, pictureUri.getPath().toString(), productcode);
                } else {
                    getPresenter().editImageUrl(id, pictureUri.getPath().toString());
                }
            }
        };
        mMap.snapshot(callback);
    }
}
