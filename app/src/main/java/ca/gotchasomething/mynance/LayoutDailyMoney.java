package ca.gotchasomething.mynance;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyCC;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;
import ca.gotchasomething.mynance.tabFragments.DailyCreditCard;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyOut;
import ca.gotchasomething.mynance.tabFragments.DailyWeeklyLimits;

public class LayoutDailyMoney extends MainNavigation {

    Button moneyInButton, moneyOutButton;
    DbManager dbManager;
    Double newAccountBalance = 0.0, newAvailableBalance = 0.0;
    FragmentManager fm;
    FragmentTransaction transaction;
    FrameLayout container;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    String availableBalance2 = null, accountBalance2 = null;
    TabLayout tl;
    TextView totalAccountText, availableAccountText, budgetWarningText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_daily_money);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dbManager = new DbManager(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        budgetWarningText = findViewById(R.id.budgetWarningText);
        budgetWarningText.setVisibility(View.GONE);
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

        tl.addOnTabSelectedListener(onTabSelectedListener);

        switch(dbManager.retrieveCurrentPageId()) {
            case 1:
                replaceFragment(new DailyMoneyIn());
                tl.getTabAt(0).select();
                break;
            case 2:
                replaceFragment(new DailyMoneyOut());
                tl.getTabAt(1).select();
                break;
            case 3:
                replaceFragment(new DailyMoneyCC());
                tl.getTabAt(2).select();
                break;
            case 4:
                replaceFragment(new DailyCreditCard());
                tl.getTabAt(3).select();
                break;
            case 5:
                replaceFragment(new DailyWeeklyLimits());
                tl.getTabAt(4).select();
                break;
        }
    }

    public void dailyHeaderText() {

        if(dbManager.sumTotalExpenses() > dbManager.sumTotalIncome()) {
            budgetWarningText.setVisibility(View.VISIBLE);
        } else {
            budgetWarningText.setVisibility(View.GONE);
        }

        newAccountBalance = dbManager.retrieveCurrentAccountBalance();
        newAvailableBalance = dbManager.retrieveCurrentAvailableBalance();

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
