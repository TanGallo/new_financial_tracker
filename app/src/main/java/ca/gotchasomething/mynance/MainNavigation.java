package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    Boolean before = false;
    public DbManager dbManager;
    protected DrawerLayout drawer;
    General mainGen;
    Intent i, i2, i3, i4, i5, i6;
    Menu menu;
    NavigationView navigationView;
    String latestDone;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1_activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DbManager(this);
        mainGen = new General();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuConfig();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void menuConfig() {
        latestDone = dbManager.retrieveLatestDone();
        beforeSetUpOrAfter(latestDone);
        menu = navigationView.getMenu();
        if (before) {
            menu.findItem(R.id.menu_before).setVisible(true);
            menu.findItem(R.id.menu_home_page).setVisible(false);
            menu.findItem(R.id.menu_home_page).setEnabled(false);
            menu.findItem(R.id.menu_financial_summary).setVisible(false);
            menu.findItem(R.id.menu_financial_summary).setEnabled(false);
            /*menu.findItem(R.id.menu_debt_plan).setVisible(false);
            menu.findItem(R.id.menu_debt_plan).setEnabled(false);
            menu.findItem(R.id.menu_savings_plan).setVisible(false);
            menu.findItem(R.id.menu_savings_plan).setEnabled(false);*/
            menu.findItem(R.id.menu_view_edit_transactions).setVisible(false);
            menu.findItem(R.id.menu_view_edit_transactions).setEnabled(false);
            menu.findItem(R.id.menu_help).setVisible(false);
            menu.findItem(R.id.menu_help).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_before).setVisible(false);
            menu.findItem(R.id.menu_home_page).setVisible(true);
            menu.findItem(R.id.menu_home_page).setEnabled(true);
            menu.findItem(R.id.menu_financial_summary).setVisible(true);
            menu.findItem(R.id.menu_financial_summary).setEnabled(true);
            /*menu.findItem(R.id.menu_debt_plan).setVisible(true);
            menu.findItem(R.id.menu_debt_plan).setEnabled(true);
            menu.findItem(R.id.menu_savings_plan).setVisible(true);
            menu.findItem(R.id.menu_savings_plan).setEnabled(true);*/
            menu.findItem(R.id.menu_view_edit_transactions).setVisible(true);
            menu.findItem(R.id.menu_view_edit_transactions).setEnabled(true);
            menu.findItem(R.id.menu_help).setVisible(true);
            menu.findItem(R.id.menu_help).setEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean beforeSetUpOrAfter(String str1) {

        if(str1.equals("tour")) {
            before = false;
        } else {
            before = true;
        }

        return before;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_home_page:
                i = new Intent(MainNavigation.this, LayoutDailyMoney.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);
                break;
            case R.id.menu_financial_summary:
                i4 = new Intent(MainNavigation.this, LayoutBudget.class);
                i4.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i4);
                break;
            /*case R.id.menu_debt_plan:
                i2 = new Intent(MainNavigation.this, LayoutDebt.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i2);
                break;
            case R.id.menu_savings_plan:
                i3 = new Intent(MainNavigation.this, LayoutSavings.class);
                i3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i3);
                break;*/
            case R.id.menu_view_edit_transactions:
                i5 = new Intent(MainNavigation.this, LayoutSavings.class);
                i5.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i5);
                break;
            case R.id.menu_help:
                i6 = new Intent(MainNavigation.this, LayoutSavings.class);
                i6.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i6);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
