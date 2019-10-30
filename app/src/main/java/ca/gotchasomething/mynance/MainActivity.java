package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;

public class MainActivity extends MainNavigation {

    General mainGen2;
    Intent intent;
    public String[] monthsList, monthsOnlyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1_activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            mainGen2 = new General();

        navView = findViewById(R.id.nav_view);
            menuConfig();
        navView.setNavigationItemSelectedListener(this);

            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        if(savedInstanceState == null) {
            intent = new Intent(MainActivity.this, LayoutDailyMoney.class);
            startActivity(intent);
            navView.setCheckedItem(R.id.menu_home_page);
        }
    }

    public void loadSlides(View view) {
        new PreferenceManager(this).clearPreferences();
        startActivity(new Intent(this, AdapterSlides.class));
        finish();
    }
}