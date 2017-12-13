package th.co.thiensurat.toss_installer;


import android.content.Intent;
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

import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.dashboard.DashboardFragment;
import th.co.thiensurat.toss_installer.itembalance.ItemBalanceFragment;
import th.co.thiensurat.toss_installer.itemlist.ItemlistFragment;
import th.co.thiensurat.toss_installer.job.JobFragment;
import th.co.thiensurat.toss_installer.job.all.AllJobFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class MainActivity extends BaseMvpActivity<MainInterface.Presenter> implements MainInterface.View {

    private String itemIntent;
    private TextView textViewName, textViewTitle;
    private ImageView imageViewProfile;
    private boolean clickBackAain;
    private MenuItem menuItemClicked;

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

    }

    @Override
    public void setupView() {
        setToolbar();
        setMenu();
    }

    @Override
    public void initialize() {
        loadHomePage();
    }

    private void loadHomePage() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //transaction.replace(R.id.container, DashboardFragment.getInstance()).addToBackStack(null).commit();
        transaction.replace(R.id.container, JobFragment.getInstance()).addToBackStack(null).commit();
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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
            /*case R.id.memu_home :
                if (currentFragment instanceof DashboardFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    setTitle(getResources().getString(R.string.app_name));
                    transaction.replace(R.id.container, DashboardFragment.getInstance(), "DashboardFragment").addToBackStack(null).commit();
                }
                break;*/
            case R.id.menu_job_all :
                if (currentFragment instanceof AllJobFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, AllJobFragment.getInstance(), "AllJobFragment").addToBackStack(null).commit();
                }
                break;
            case R.id.menu_assignment :
                if (currentFragment instanceof JobFragment) {
                    drawerLayout.closeDrawers();
                } else {
                    transaction.replace(R.id.container, JobFragment.getInstance(), "JobFragment").addToBackStack(null).commit();
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
            Toast.makeText(MainActivity.this, "คลิกอีกครั้งเพื่อออกจากแอพพลิเคชั่น", Toast.LENGTH_LONG).show();

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
