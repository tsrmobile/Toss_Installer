package th.co.thiensurat.toss_installer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.itembalance.ItemBalanceFragment;
import th.co.thiensurat.toss_installer.itemlist.ItemlistFragment;
import th.co.thiensurat.toss_installer.job.JobFragment;
import th.co.thiensurat.toss_installer.job.all.AllJobFragment;
import th.co.thiensurat.toss_installer.jobinstallation.JobInstallFragment;
import th.co.thiensurat.toss_installer.setting.SettingFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class MainActivity extends BaseMvpActivity<MainInterface.Presenter> implements MainInterface.View {

    private String empid;
    private File signPath;
    private ImageConfiguration imageConfiguration;

    private String itemIntent;
    private boolean clickBackAain;
    private MenuItem menuItemClicked;
    private CustomDialog customDialog;
    private ImageView imageViewProfile;
    private TextView textViewName, textViewTitle;

    @Override
    public MainInterface.Presenter createPresenter() {
        return MainPresenter.create();
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
                //customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
            }
        } catch (Exception e) {
            //customDialog.dialogWarning("ยังไม่มีลายเซ็นต์พนักงาน\nกรุณาเพิ่มลายเซ็นต์");
        }

    }

    private void loadHomePage() {
        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, JobFragment.getInstance()).addToBackStack(null).commit();*/
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
        imageViewProfile = (ImageView) header.findViewById(R.id.image_profile);

        String title = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE);
        String firstname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME);
        String lastname = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME);

        String fullname = title + "" + firstname + " " + lastname;
        textViewName.setText(fullname);

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
            case R.id.menu_job_all :
                /*if (currentFragment instanceof AllJobFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, AllJobFragment.getInstance(), "AllJobFragment").addToBackStack(null).commit();
                }*/
                break;
            case R.id.menu_assignment :
                if (currentFragment instanceof JobFragment) {
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

                break;
            case R.id.menu_setting :
                if (currentFragment instanceof SettingFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, SettingFragment.getInstance(), "SettingFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_logout :
                MyApplication.getInstance().getPrefManager().clear();
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
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
            case R.id.menu_logout :
                MyApplication.getInstance().getPrefManager().clear();
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
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
            Toast.makeText(MainActivity.this, "คลิกอีกครั้งเพื่อออก", Toast.LENGTH_LONG).show();

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
}
