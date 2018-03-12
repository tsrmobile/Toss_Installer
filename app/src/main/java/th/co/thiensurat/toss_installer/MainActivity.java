package th.co.thiensurat.toss_installer;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.itembalance.ItemBalanceFragment;
import th.co.thiensurat.toss_installer.itemlist.ItemlistFragment;
import th.co.thiensurat.toss_installer.jobinstallation.JobInstallFragment;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.network.NetworkChangeReceiver;
import th.co.thiensurat.toss_installer.payment.PaymentFragment;
import th.co.thiensurat.toss_installer.setting.SettingFragment;
import th.co.thiensurat.toss_installer.setting.backupandrestore.BackupAndRestoreFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_SETTINGS;

public class MainActivity extends BaseMvpActivity<MainInterface.Presenter> implements MainInterface.View {

    private String speed;
    private String empid;
    private File signPath;
    private ImageConfiguration imageConfiguration;

    private static Context context;
    private String itemIntent;
    private boolean clickBackAain;
    private MenuItem menuItemClicked;
    private CustomDialog customDialog;
    private ImageView imageViewProfile;
    private TextView textViewName, textViewTitle, textViewCode;

    public static MainActivity getInstance() {
        return new MainActivity();
    }

    @Override
    public MainInterface.Presenter createPresenter() {
        return MainPresenter.create(MainActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) FrameLayout containner;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        context = getApplicationContext();
        customDialog = new CustomDialog(MainActivity.this);
        imageConfiguration = new ImageConfiguration(MainActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        try {
            if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_POSITION).equals("ช่างติดตั้งผลิตภัณฑ์")) {
                setMenu();
            } else if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_POSITION).equals("พนักงานเก็บเงิน")) {
                setMenuPaymet();
            } else {
                setMenu();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void initialize() {
        loadHomePage();

        try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            if (!signPath.exists()) {
                employeeSign();
            }
        } catch (Exception e) {
            employeeSign();
        }

        try {
            boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(MainActivity.this);
            if (isNetworkAvailable) {
                getPresenter().getAddressNotSync();
            }
        } catch (Exception e) {

        }
    }

    private void loadHomePage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, JobInstallFragment.getInstance()).addToBackStack(null).commit();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    private void setHeaderMenu() {
        LayoutInflater inflater = getLayoutInflater();
        View header = navigationView.inflateHeaderView(R.layout.menu_header);
        textViewName = (TextView) header.findViewById(R.id.name);
        textViewCode = (TextView) header.findViewById(R.id.empid);
        imageViewProfile = (ImageView) header.findViewById(R.id.image_profile);

        String title = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE);
        String firstname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME);
        String lastname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME);

        String fullname = title + "" + firstname + " " + lastname;
        textViewName.setText(fullname);
        textViewCode.setText("รหัส: " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    }

    private void setMenuPaymet() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on Item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemClicked = menuItem;
                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView.inflateMenu(R.menu.pay_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(menuItemClicked != null)
                    handleSelectedMenuPayment(menuItemClicked);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                menuItemClicked = null;
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setHeaderMenu();
    }

    private void setMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on Item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemClicked = menuItem;
                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView.inflateMenu(R.menu.main_menu);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(menuItemClicked != null)
                    handleSelectedMenu(menuItemClicked);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                menuItemClicked = null;
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setHeaderMenu();
    }

    private void handleSelectedMenu(MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        switch (menuItem.getItemId()){
            case R.id.menu_assignment :
                if (currentFragment instanceof JobInstallFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, JobInstallFragment.getInstance(), "JobInstallFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_archive :
                if (currentFragment instanceof ItemlistFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, ItemlistFragment.getInstance(), "ItemlistFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_item_balance :
                if (currentFragment instanceof ItemBalanceFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, ItemBalanceFragment.getInstance(), "ItemBalanceFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_payment:
                if (currentFragment instanceof SettingFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, PaymentFragment.getInstance(), "PaymentFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_setting :
                if (currentFragment instanceof SettingFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, SettingFragment.getInstance(), "SettingFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_backup :
                if (currentFragment instanceof BackupAndRestoreFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, BackupAndRestoreFragment.getInstance(), "BackupAndRestoreFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_logout :
                delDatabase();
                break;
            default: break;
        }
    }

    private void handleSelectedMenuPayment(MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        switch (menuItem.getItemId()){
            case R.id.menu_payment:

                break;
            case R.id.menu_setting :
                if (currentFragment instanceof SettingFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, SettingFragment.getInstance(), "SettingFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_backup :
                if (currentFragment instanceof BackupAndRestoreFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, BackupAndRestoreFragment.getInstance(), "BackupAndRestoreFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_logout :
                delDatabase();
                break;
            default: break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            if (clickBackAain) {
                finish();
                return true;
            }
            this.clickBackAain = true;
            Toast.makeText(MainActivity.this, "กด BACK อีกครั้งเพื่อออกจากแอพ", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    clickBackAain=false;
                }
            }, 2000);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    private void delDatabase() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("ออกจากระบบ!")
            .setContentText("กรุณาสำรองข้อมูล\nก่อนออกจากระบบ")
            .showCancelButton(true)
            .setCancelText("สำรองข้อมูล")
            .setConfirmText("ยืนยัน")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                    File f = new File("/data/data/" + getPackageName() + "/databases/" + Constance.DBNAME);
                    if (f.exists()) {
                        if (f.delete()) {
                            MyApplication.getInstance().getPrefManager().clear();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            finish();
                        }
                    } else {
                        MyApplication.getInstance().getPrefManager().clear();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        finish();
                    }
                }
            })
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

                    if (currentFragment instanceof BackupAndRestoreFragment) {
                        sweetAlertDialog.dismiss();
                    } else {
                        sweetAlertDialog.dismiss();
                        transaction.replace(R.id.container, BackupAndRestoreFragment.getInstance(), "BackupAndRestoreFragment").addToBackStack(null).commit();
                    }
                }
            }).show();
    }

    private void employeeSign() {
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("คำเตือน!")
                .setContentText("ยังไม่มีลายเซ็นต์พนักงาน\\nกรุณาเพิ่มลายเซ็นต์")
                .showCancelButton(true)
                .setCancelText("ยกเลิก")
                .setConfirmText("เพิ่มลายเซ็นต์")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (currentFragment instanceof SettingFragment) {
                            drawerLayout.closeDrawers();
                        } else {
                            transaction.replace(R.id.container, SettingFragment.getInstance(), "SettingFragment").addToBackStack(null).commit();
                        }
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                }).show();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void setAddressSync(List<RequestUpdateAddress.updateBody> updateBodyList) {
        if (updateBodyList.size() > 0) {
            getPresenter().requestAddressSync(updateBodyList);
        }
    }

    /*public void detectWifiConnected(final int state) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                switch (state) {
                    case 1:
                        Log.e("Internet speed", "WI-FI");
                        speed = "wifi";
                        break;
                    case 2:
                        Log.e("Internet speed", "Mobile");
                        speed = "cellular";
                        break;
                    case 3:
                        Log.e("Internet speed", "100kbps");
                        speed = "very low";
                        break;
                    case 4:
                        Log.e("Internet speed", "100-400kbps");
                        speed = "low";
                        break;
                    case 5:
                        Log.e("Internet speed", "400-1000kbps");
                        speed = "normal";
                        break;
                    case 6:
                        Log.e("Internet speed", "600-1400kbps");
                        speed = "hi";
                        break;
                }
            }
        });
    }*/

    @Override
    public void showNotificationSyncIcon() {
        Context application = getApplicationContext();

        Intent resultIntent = new Intent(application, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(application, 0, resultIntent, 0);

        NotificationManager nmgr = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("ซิงค์อัติโนมัติ")
                .setSmallIcon(R.drawable.ic_sync_black_36dp);
        Notification mNotification = mBuilder.build();
        nmgr.notify(0, mNotification);
    }
}
