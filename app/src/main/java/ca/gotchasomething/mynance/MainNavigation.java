package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    Boolean before = false;
    Cursor setUpCursor;
    DbHelper setUpHelper;
    public DbManager dbManager;
    protected DrawerLayout drawer;
    int tourDoneYes;
    Intent i, i2, i4, i5, i6, i7, i8, i9;
    Menu menu;
    NavigationView navigationView;
    SQLiteDatabase setUpDbDb;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuConfig();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    public void menuConfig() {
        beforeSetUpOrAfter();
        menu = navigationView.getMenu();
        if (before) {
            menu.findItem(R.id.menu_before).setVisible(true);
            menu.findItem(R.id.menu_daily_money).setVisible(false);
            menu.findItem(R.id.menu_budget).setVisible(false);
            menu.findItem(R.id.menu_debt).setVisible(false);
            menu.findItem(R.id.menu_savings).setVisible(false);
            menu.findItem(R.id.menu_spending_report).setVisible(false);
            menu.findItem(R.id.menu_budget_report_exp).setVisible(false);
            menu.findItem(R.id.menu_budget_report_inc).setVisible(false);
            menu.findItem(R.id.menu_help).setVisible(false);
            menu.findItem(R.id.menu_daily_money).setEnabled(false);
            menu.findItem(R.id.menu_budget).setEnabled(false);
            menu.findItem(R.id.menu_debt).setEnabled(false);
            menu.findItem(R.id.menu_savings).setEnabled(false);
            menu.findItem(R.id.menu_spending_report).setEnabled(false);
            menu.findItem(R.id.menu_budget_report_exp).setEnabled(false);
            menu.findItem(R.id.menu_budget_report_inc).setEnabled(false);
            menu.findItem(R.id.menu_help).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_before).setVisible(false);
            menu.findItem(R.id.menu_daily_money).setVisible(true);
            menu.findItem(R.id.menu_budget).setVisible(true);
            menu.findItem(R.id.menu_debt).setVisible(true);
            menu.findItem(R.id.menu_savings).setVisible(true);
            menu.findItem(R.id.menu_spending_report).setVisible(true);
            menu.findItem(R.id.menu_budget_report_exp).setVisible(true);
            menu.findItem(R.id.menu_budget_report_inc).setVisible(true);
            menu.findItem(R.id.menu_help).setVisible(true);
            menu.findItem(R.id.menu_daily_money).setEnabled(true);
            menu.findItem(R.id.menu_budget).setEnabled(true);
            menu.findItem(R.id.menu_debt).setEnabled(true);
            menu.findItem(R.id.menu_savings).setEnabled(true);
            menu.findItem(R.id.menu_spending_report).setEnabled(true);
            menu.findItem(R.id.menu_budget_report_exp).setEnabled(true);
            menu.findItem(R.id.menu_budget_report_inc).setEnabled(true);
            menu.findItem(R.id.menu_help).setEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean beforeSetUpOrAfter() {
        setUpHelper = new DbHelper(getApplicationContext());
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery(" SELECT max(tourDone) FROM " + DbHelper.SET_UP_TABLE_NAME + "", null);
        setUpCursor.moveToFirst();
        tourDoneYes = setUpCursor.getInt(0);
        setUpCursor.close();

        if (tourDoneYes <= 0) {
            before = true;
        } else {
            before = false;
        }

        return before;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_daily_money:
                i = new Intent(MainNavigation.this, LayoutDailyMoney.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);
                break;
            case R.id.menu_budget:
                i2 = new Intent(MainNavigation.this, LayoutBudget.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i2);
                break;
            case R.id.menu_debt:
                i4 = new Intent(MainNavigation.this, LayoutDebt.class);
                i4.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i4);
                break;
            case R.id.menu_savings:
                i5 = new Intent(MainNavigation.this, LayoutSavings.class);
                i5.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i5);
                break;
            case R.id.menu_spending_report:
                i7 = new Intent(MainNavigation.this, LayoutSpendingReport.class);
                i7.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i7);
                break;
            case R.id.menu_budget_report_exp:
                i8 = new Intent(MainNavigation.this, LayoutBudgetReport.class);
                i8.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i8);
                break;
            case R.id.menu_budget_report_inc:
                i9 = new Intent(MainNavigation.this, LayoutBudgetReport2.class);
                i9.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i9);
                break;
            case R.id.menu_help:
                i6 = new Intent(MainNavigation.this, LayoutHelp.class);
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
