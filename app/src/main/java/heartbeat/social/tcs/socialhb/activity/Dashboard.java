package heartbeat.social.tcs.socialhb.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Notifications;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Profile;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Settings;
import heartbeat.social.tcs.socialhb.adapter.ModuleAdapter;
import heartbeat.social.tcs.socialhb.bean.ModuleItem;
import heartbeat.social.tcs.socialhb.network.NetworkManager;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;
import heartbeat.social.tcs.socialhb.utility.Utils;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private NetworkManager networkManager;

    private ProgressBar prgBar1;
    private List<ModuleItem> modules;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        prgBar1           = (ProgressBar) findViewById(R.id.prgBar1);

        final RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycleView1);

        setSupportActionBar(toolbar1);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        prgBar1.setVisibility(View.VISIBLE);

        networkManager = new NetworkManager(this);

        networkManager.makeGetModuleWebserviceCall(this, new NetworkManager.VolleyCallback() {
            @Override
            public void onSuccess(List<ModuleItem> result) {

                if (!(result == null)) {
                    prgBar1.setVisibility(View.GONE);
                    modules = result;

                    recyclerView1.setVisibility(View.VISIBLE);
                    recyclerView1.setHasFixedSize(true);
                    mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    mStaggeredLayoutManager.setSpanCount(2);
                    recyclerView1.setLayoutManager(mStaggeredLayoutManager);

                    ModuleAdapter moduleAdapter = new ModuleAdapter(modules, getApplicationContext());

                    recyclerView1.setAdapter(moduleAdapter);
                }
            }
        });




        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (null != navigationView) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();

                    int id = menuItem.getItemId();


                    switch (id) {


                        case R.id.navigation_item_share:
                            Utils.shareAppProcess(Dashboard.this);
                            return true;


                        case R.id.navigation_item_logout:
                            logoutProcess();
                            return true;


                        case R.id.navigation_item_settings:
                            Intent setting_intent = new Intent(getApplicationContext(), Settings.class);
                            startActivity(setting_intent);
                            return true;

                        case R.id.navigation_item_home:
                            return true;

                        case R.id.navigation_item_notification:
                            Intent intent1 = new Intent(getApplicationContext(), Notifications.class);
                            startActivity(intent1);
                            return true;


                        default:
                            return true;

                    }


                }
            });
        }


        //OnClick Listener on Profile View
        assert navigationView != null;
        View headerLayout = navigationView.getHeaderView(0);
        RelativeLayout drawer_layout_relative = (RelativeLayout) headerLayout.findViewById(R.id.drawer_layout_relative);
        drawer_layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent1);
            }
        });

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());
        TextView employeeName = (TextView) headerLayout.findViewById(R.id.emp_name);
        TextView employeeEmail = (TextView) headerLayout.findViewById(R.id.emp_email);
        String name = profileDBHelper.getEmpFirstName() + " " + profileDBHelper.getEmpLastName();
        employeeName.setText(name);
        String email = profileDBHelper.getEmpEmail();
        employeeEmail.setText(email);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.miCompose:
                Toast.makeText(getApplicationContext(), "Compose Clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.miProfile:
                if (mStaggeredLayoutManager.getSpanCount() == 1) {
                    item.setIcon(R.drawable.ic_view_list_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(2);
                } else {
                    item.setIcon(R.drawable.ic_grid_on_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(1);
                }
        }

        return super.onOptionsItemSelected(item);
    }


    private void logoutProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();


        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over


                String token_value = Utils.getToken(Dashboard.this);
                if (!(token_value == null)) {
                    Log.e("token value", "Token : " + token_value);
                    //Updating UserId for fire base token
                    networkManager.updateIDForFirebaseToken("dashboard");
                }

                logoutCommonTasks();
                progressDialog.dismiss();


            }


        }, SPLASH_TIME_OUT);


    }

    private void logoutCommonTasks() {
        //Deleting User Data from SQLite
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.deleteUserTable();

        //Deleting User Profile Data from SQLite
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());
        profileDBHelper.deleteUserProfileTable();

        //Deleting Fire base Token
        Utils.deleteToken(getApplicationContext());


        //Deleting Session
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(false);

        //Redirecting to Login Activity
        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent1);
        finish();
    }


}
