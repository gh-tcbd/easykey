package me.easykey.easykey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu = this.generateMenu(menu);

        menu.setGroupVisible(R.id.room_group, true);
    }
    public void onSettingsClick(MenuItem item){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    private Menu generateMenu(Menu menu) {
        for(int i = 1; i <= 3; i++) {
            menu.add(R.id.room_group, Menu.NONE, i, "Testing"+i);
        }
        return menu;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences sharedPref = getSharedPreferences("me.easykey.settings", Context.MODE_PRIVATE);
        if (sharedPref.contains("APIKEY")) {
            menu.setGroupVisible(R.id.login_group, false);
            menu.setGroupVisible(R.id.logout_group, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_sign_in) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, 1);
            return true;
        } else if (id == R.id.action_log_out) {
            SharedPreferences sp = getSharedPreferences("me.easykey.settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.clear();
            e.commit();
            resetToggles();
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the settings action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences("me.easykey.settings", Context.MODE_PRIVATE);


        if (sharedPref.contains("APIKEY")) {
            String fName = sharedPref.getString("fName", "[Error]");
            String lName = sharedPref.getString("lName", "[Error]");
            String apiKey = sharedPref.getString("APIKEY", "[Error]");

            Toast.makeText(getApplicationContext(), fName + " " + lName + " logged in with api key: " + apiKey, Toast.LENGTH_LONG).show();

        }
        resetToggles();
        invalidateOptionsMenu();
    }

    private void resetToggles() {
        SharedPreferences sharedPref = getSharedPreferences("me.easykey.settings", Context.MODE_PRIVATE);
        Switch toggle_awake = (Switch) findViewById(R.id.toggle_sleep);
        Switch toggle_away = (Switch) findViewById(R.id.toggle_away);
        Switch toggle_lockdown = (Switch) findViewById(R.id.toggle_lockdown);
        toggle_awake.setChecked(sharedPref.getBoolean("sleep", false));
        toggle_away.setChecked(sharedPref.getBoolean("away", false));
        toggle_lockdown.setChecked(sharedPref.getBoolean("lockdown", false));
    }


}
