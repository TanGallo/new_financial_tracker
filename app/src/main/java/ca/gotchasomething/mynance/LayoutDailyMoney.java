package ca.gotchasomething.mynance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.data.CurrentDbManager;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyCC;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyInOut;
import ca.gotchasomething.mynance.tabFragments.DailyCreditCard;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyOut;
import ca.gotchasomething.mynance.tabFragments.DailyWeeklyLimits;

public class LayoutDailyMoney extends MainNavigation {

    Button moneyInButton, moneyOutButton;
    CurrentDbManager currentDbManager;
    Cursor currentCursor;
    DbHelper currentHelper;
    Double newAccountBalance, newAvailableBalance, currentAccountBalance, currentAvailableBalance;
    FragmentManager fm;
    FragmentTransaction transaction;
    FrameLayout container;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase currentDbDb;
    String availableBalance2, accountBalance2;
    TabLayout tl;
    TextView totalAccountText, availableAccountText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_daily_money);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        currentDbManager = new CurrentDbManager(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        totalAccountText = findViewById(R.id.totalAccountText);
        availableAccountText = findViewById(R.id.availableAccountText);

        dailyHeaderText();

        tl = findViewById(R.id.daily_tab_layout);
        container = findViewById(R.id.daily_fragment_container);

        tl.addTab(tl.newTab().setText("Money In"));
        tl.addTab(tl.newTab().setText("Money Out"));
        tl.addTab(tl.newTab().setText("Credit card"));
        tl.addTab(tl.newTab().setText("Pay cc"));
        tl.addTab(tl.newTab().setText("Weekly Limits"));

        replaceFragment(new DailyMoneyIn());

        tl.addOnTabSelectedListener(onTabSelectedListener);

    }

    /*public Double retrieveCurrentAccountBalance() {

        currentHelper = new DbHelper(this);
        currentDbDb = currentHelper.getReadableDatabase();
        currentCursor = currentDbDb.rawQuery("SELECT " + DbHelper.CURRENTACCOUNTBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        currentCursor.moveToFirst();
        currentAccountBalance = currentCursor.getDouble(0);
        currentCursor.close();

        if(currentAccountBalance.isNaN()) {
            currentAccountBalance = 0.0;
        }

        return currentAccountBalance;
    }*/

    /*public Double retrieveCurrentAvailableBalance() {

        currentHelper = new DbHelper(this);
        currentDbDb = currentHelper.getReadableDatabase();
        currentCursor = currentDbDb.rawQuery("SELECT " + DbHelper.CURRENTAVAILABLEBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        currentCursor.moveToFirst();
        currentAvailableBalance = currentCursor.getDouble(0);
        currentCursor.close();

        if(currentAvailableBalance.isNaN()) {
            currentAvailableBalance = 0.0;
        }

        return currentAvailableBalance;
    }*/

    public void dailyHeaderText() {

        newAccountBalance = currentDbManager.retrieveCurrentAccountBalance();
        newAvailableBalance = currentDbManager.retrieveCurrentAvailableBalance();
        //newAccountBalance = retrieveCurrentAccountBalance();
        //newAvailableBalance = retrieveCurrentAvailableBalance();

        if(newAccountBalance < newAvailableBalance || newAccountBalance == 0.0) {
            newAvailableBalance = 0.0;
        }

        accountBalance2 = currencyFormat.format(newAccountBalance);
        availableBalance2 = currencyFormat.format(newAvailableBalance);

        totalAccountText.setText(accountBalance2);
        availableAccountText.setText(availableBalance2);
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = (new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getPosition() == 0) {
                replaceFragment(new DailyMoneyIn());
            } else if (tab.getPosition() == 1) {
                replaceFragment(new DailyMoneyOut());
            } else if (tab.getPosition() == 2) {
                replaceFragment(new DailyMoneyCC());
            } else if (tab.getPosition() == 3) {
                replaceFragment(new DailyCreditCard());
            } else if (tab.getPosition() == 4) {
                replaceFragment(new DailyWeeklyLimits());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });

    public void replaceFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.daily_fragment_container, fragment);

        transaction.commit();
    }
}
