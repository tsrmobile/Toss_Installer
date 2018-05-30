package th.co.thiensurat.toss_installer;


import android.Manifest;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centerm.smartpos.aidl.idcard.AidlIdCard;
import com.centerm.smartpos.aidl.idcard.AidlIdCardListener;
import com.centerm.smartpos.aidl.idcard.IdInfoBean;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.deposit.channel.DepositChannelActivity;
import th.co.thiensurat.toss_installer.deposit.deposit.DepositActivity;
import th.co.thiensurat.toss_installer.firebase.MyJobService;
import th.co.thiensurat.toss_installer.installation.choice.ChoiceInstallActivity;
import th.co.thiensurat.toss_installer.productbalance.activity.ProductBalanceActivity;
import th.co.thiensurat.toss_installer.productwithdraw.WithdrawProductActivity;
import th.co.thiensurat.toss_installer.jobinstallation.JobActivity;
import th.co.thiensurat.toss_installer.payment.activity.PaymentActivity;
import th.co.thiensurat.toss_installer.setting.ConfigurationActivity;
import th.co.thiensurat.toss_installer.setting.backupandrestore.BackupAndRestoreActivity;
import th.co.thiensurat.toss_installer.systemnew.detail.NewDetailActivity;
import th.co.thiensurat.toss_installer.systemnew.list.ListProductFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.GPSTracker;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class MainActivity extends BaseMvpActivity<MainInterface.Presenter> implements MainInterface.View {

    public AidlDeviceManager manager = null;

    private String empid;
    private File signPath;
    private ImageConfiguration imageConfiguration;

    private long WAIT_TIME = 60 * 60 * 1000;

    private static Context context;
    private String itemIntent;
    private boolean clickBackAain;
    private TextView textViewTitle;
    private MenuItem menuItemClicked;
    private CustomDialog customDialog;

    private double latitude;
    private double longitude;
    private FirebaseJobDispatcher dispatcher;

    private FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private Bundle bundle = null;

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
    @BindView(R.id.button_delivery) RelativeLayout relativeLayoutButtonDelivery;
    @BindView(R.id.button_payment) RelativeLayout relativeLayoutButtonPayment;
    @BindView(R.id.button_deposit) RelativeLayout relativeLayoutButtonDeposit;
    @BindView(R.id.button_settings) RelativeLayout relativeLayoutButtonSettings;
    @BindView(R.id.button_import) RelativeLayout relativeLayoutButtonImport;
    @BindView(R.id.button_balance) RelativeLayout relativeLayoutButtonBalance;
    @BindView(R.id.button_logout) RelativeLayout relativeLayoutButtonLogout;

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
        relativeLayoutButtonDelivery.setOnClickListener(onDelivery());
        relativeLayoutButtonPayment.setOnClickListener(onPayment());
        relativeLayoutButtonDeposit.setOnClickListener(onDeposit());
        relativeLayoutButtonSettings.setOnClickListener(onSettings());
        relativeLayoutButtonImport.setOnClickListener(onImport());
        relativeLayoutButtonBalance.setOnClickListener(onBalance());
        relativeLayoutButtonLogout.setOnClickListener(onLogout());
    }

    @Override
    public void initialize() {
        /*empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
        try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            if (!signPath.exists()) {
                customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
            }
        } catch (Exception e) {
            customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
        }*/

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            if (!MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_ORDER_ID).isEmpty()) {
                Intent intent = new Intent(MainActivity.this, NewDetailActivity.class);
                intent.putExtra(Constance.KEY_ORDER_ID,
                        MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_ORDER_ID));
                startActivityForResult(intent, Constance.REQUEST_NEW_DETAIL);
            }
        } catch (Exception e) {
            Log.e("Bundle", e.getLocalizedMessage());
        }
    }

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
                startActivityForResult(new Intent(MainActivity.this, ChoiceInstallActivity.class), Constance.REQUEST_CHOICE_INSTALL);
            }
        };
    }

    private View.OnClickListener onDelivery() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutButtonDelivery.startAnimation(new AnimateButton().animbutton());
                MyApplication.getInstance().getPrefManager().setPreferrence("choice", "delivery");
                startActivityForResult(new Intent(MainActivity.this, Main2Activity.class), Constance.REQUEST_DELIVERY);
            }
        };
    }

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonPayment.startAnimation(new AnimateButton().animbutton());
                //startActivityForResult(new Intent(MainActivity.this, PaymentActivity.class), Constance.REQUEST_PAYMENT);
            }
        };
    }

    private View.OnClickListener onDeposit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonDeposit.startAnimation(new AnimateButton().animbutton());
                //startActivityForResult(new Intent(MainActivity.this, DepositChannelActivity.class), Constance.REQUEST_DEPOSTI_CHANNEL);
            }
        };
    }

    private View.OnClickListener onSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonSettings.startAnimation(new AnimateButton().animbutton());
                //startActivityForResult(new Intent(MainActivity.this, ConfigurationActivity.class), Constance.REQUEST_APP_SETTINGS);
            }
        };
    }

    private View.OnClickListener onImport() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonImport.startAnimation(new AnimateButton().animbutton());
                //startActivityForResult(new Intent(MainActivity.this, WithdrawProductActivity.class), Constance.REQUEST_IMPORT_ITEM);
            }
        };
    }

    private View.OnClickListener onBalance() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonBalance.startAnimation(new AnimateButton().animbutton());
                //startActivityForResult(new Intent(MainActivity.this, ProductBalanceActivity.class), Constance.REQUEST_ITEM_BALANCE);
            }
        };
    }

    private View.OnClickListener onLogout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonLogout.startAnimation(new AnimateButton().animbutton());
                //delDatabase();
                signout();
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (clickBackAain) {
                finish();
                return true;
            }
            this.clickBackAain = true;
            Toast.makeText(MainActivity.this, "กด BACK อีกครั้งเพื่อออกจากแอพ", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    clickBackAain = false;
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
                .showCancelButton(false)
                .setCancelText("ยกเลิก")
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
                    sweetAlertDialog.dismiss();
                }
            }).show();
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
    }

    @Override
    public void onSuccess() {

    }
}
