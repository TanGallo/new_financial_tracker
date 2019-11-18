package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;

public class MainActivity extends MainNavigation {

    Intent intent;
    //PreferenceManager pfMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1_activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navView = findViewById(R.id.nav_view);
        menuConfig();
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //pfMgr = new PreferenceManager(this);

        //if (new PreferenceManager(this).checkPreferences()) { //loadHome();
        //}

        if (savedInstanceState == null) {
            //if (pfMgr.checkPreferences()) {

                //}

                //if (savedInstanceState == null) {
                intent = new Intent(MainActivity.this, LayoutDailyMoney.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
                navView.setCheckedItem(R.id.menu_home_page);
            }
        //}
    }
}