package th.co.thiensurat.toss_installer;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.payment.detail.CustomerPaymentDetailActivity;
import th.co.thiensurat.toss_installer.productbalance.activity.ProductBalanceActivity;
import th.co.thiensurat.toss_installer.productwithdraw.WithdrawProductActivity;
import th.co.thiensurat.toss_installer.jobinstallation.JobActivity;
import th.co.thiensurat.toss_installer.payment.activity.PaymentActivity;
import th.co.thiensurat.toss_installer.setting.ConfigurationActivity;
import th.co.thiensurat.toss_installer.setting.backupandrestore.BackupAndRestoreActivity;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.ThaiBaht;

public class MainActivity extends BaseMvpActivity<MainInterface.Presenter> implements MainInterface.View {

    private String empid;
    private File signPath;
    private ImageConfiguration imageConfiguration;

    private long WAIT_TIME = 60*60*1000;

    private static Context context;
    private String itemIntent;
    private boolean clickBackAain;
    private TextView textViewTitle;
    private MenuItem menuItemClicked;
    private CustomDialog customDialog;

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
    @BindView(R.id.emp_detail) TextView textViewEmpDetail;
    @BindView(R.id.image_profile) ImageView imageViewProfile;
    @BindView(R.id.button_sale) RelativeLayout relativeLayoutButtonSale;
    @BindView(R.id.button_install) RelativeLayout relativeLayoutButtonInstall;
    @BindView(R.id.button_payment) RelativeLayout relativeLayoutButtonPayment;
    @BindView(R.id.button_deposit) RelativeLayout relativeLayoutButtonDeposit;
    @BindView(R.id.button_settings) RelativeLayout relativeLayoutButtonSettings;
    @BindView(R.id.button_import) RelativeLayout relativeLayoutButtonImport;
    @BindView(R.id.button_balance) RelativeLayout relativeLayoutButtonBalance;
    @BindView(R.id.button_logout) RelativeLayout relativeLayoutButtonLogout;
    /*@BindView(R.id.container) FrameLayout containner;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;*/
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
        relativeLayoutButtonSale.setOnClickListener(onSale());
        relativeLayoutButtonInstall.setOnClickListener(onInstall());
        relativeLayoutButtonPayment.setOnClickListener(onPayment());
        relativeLayoutButtonDeposit.setOnClickListener(onDeposit());
        relativeLayoutButtonSettings.setOnClickListener(onSettings());
        relativeLayoutButtonImport.setOnClickListener(onImport());
        relativeLayoutButtonBalance.setOnClickListener(onBalance());
        relativeLayoutButtonLogout.setOnClickListener(onLogout());
        /*try {
            if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_POSITION).equals("ช่างติดตั้งผลิตภัณฑ์")) {
                //setMenu();
            } else if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_POSITION).equals("พนักงานเก็บเงิน")) {
                //setMenuPaymet();
            } else {
                //setMenu();
            }
        } catch (Exception ex) {

        }*/
        /*float second = new Float("1234124.00350");
        BigDecimal first = new BigDecimal(String.valueOf(second));

        Log.e("currency text", first.abs() + "");*/
    }

    @Override
    public void initialize() {
        try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            if (!signPath.exists()) {
                customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
            }
        } catch (Exception e) {
            customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
        }
        //loadHomePage();

        //Log.e("timeout", Utils.getTimeout(session) + "");
        /*try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            if (!signPath.exists()) {
                //employeeSign();
            }
        } catch (Exception e) {
            //employeeSign();
        }

        try {
            boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(MainActivity.this);
            if (isNetworkAvailable) {
                getPresenter().getAddressNotSync();
            }
        } catch (Exception e) {

        }*/
    }

    /*private void loadHomePage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, JobInstallFragment.getInstance()).addToBackStack(null).commit();
    }*/

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        String title = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE);
        String firstname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME);
        String lastname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME);

        String empDetail = title + "" + firstname + " " + lastname;

        textViewEmpDetail.setText(empDetail);
    }

    private View.OnClickListener onSale() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonSale.startAnimation(new AnimateButton().animbutton());

            }
        };
    }

    private View.OnClickListener onInstall() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonInstall.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(MainActivity.this, JobActivity.class), Constance.REQUEST_JOB);
            }
        };
    }

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonPayment.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(MainActivity.this, PaymentActivity.class), Constance.REQUEST_PAYMENT);
                //startActivityForResult(new Intent(MainActivity.this, CustomerPaymentDetailActivity.class), Constance.REQUEST_PAYMENT);
            }
        };
    }

    private View.OnClickListener onDeposit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonDeposit.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonSettings.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(MainActivity.this, ConfigurationActivity.class), Constance.REQUEST_APP_SETTINGS);
            }
        };
    }

    private View.OnClickListener onImport() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonImport.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(MainActivity.this, WithdrawProductActivity.class), Constance.REQUEST_IMPORT_ITEM);
            }
        };
    }

    private View.OnClickListener onBalance() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonBalance.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent( MainActivity.this, ProductBalanceActivity.class), Constance.REQUEST_ITEM_BALANCE);
            }
        };
    }

    private View.OnClickListener onLogout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonLogout.startAnimation(new AnimateButton().animbutton());
                delDatabase();
            }
        };
    }

    /*private void setHeaderMenu() {
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
    }*/

    /*private void setMenuPaymet() {
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
    }*/

    /*private void setMenu() {
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
    }*/

    /*private void handleSelectedMenu(MenuItem menuItem) {
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
                if (currentFragment instanceof WithdrawProductFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, WithdrawProductFragment.getInstance(), "WithdrawProductFragment").addToBackStack(null).commit();
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
                if (currentFragment instanceof SignActivity) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, PaymentFragment.getInstance(), "PaymentFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_setting :
                if (currentFragment instanceof SignActivity) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, SignActivity.getInstance(), "SignActivity").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_backup :
                if (currentFragment instanceof BackupAndRestoreActivity) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, BackupAndRestoreActivity.getInstance(), "BackupAndRestoreActivity").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_logout :
                delDatabase();
                break;
            default: break;
        }
    }*/

    /*private void handleSelectedMenuPayment(MenuItem menuItem) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        switch (menuItem.getItemId()){
            case R.id.menu_payment:

                break;
            case R.id.menu_setting :
                if (currentFragment instanceof SignActivity) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, SignActivity.getInstance(), "SignActivity").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_backup :
                if (currentFragment instanceof BackupAndRestoreActivity) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, BackupAndRestoreActivity.getInstance(), "BackupAndRestoreActivity").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_logout :
                delDatabase();
                break;
            default: break;
        }
    }*/

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
                            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
                    /*FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

                    if (currentFragment instanceof BackupAndRestoreActivity) {
                        sweetAlertDialog.dismiss();
                    } else {
                        sweetAlertDialog.dismiss();
                        transaction.replace(R.id.container, BackupAndRestoreActivity.getInstance(), "BackupAndRestoreActivity").addToBackStack(null).commit();
                    }*/
                    sweetAlertDialog.dismiss();
                    startActivityForResult(new Intent(MainActivity.this, BackupAndRestoreActivity.class), Constance.REQUEST_BACKUP);
                }
            }).show();
    }

    /*private void employeeSign() {
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
                        if (currentFragment instanceof SignActivity) {
                            drawerLayout.closeDrawers();
                        } else {
                            transaction.replace(R.id.container, SignActivity.getInstance(), "SignActivity").addToBackStack(null).commit();
                        }
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                }).show();
    }*/

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
    }

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
    }*/
}
