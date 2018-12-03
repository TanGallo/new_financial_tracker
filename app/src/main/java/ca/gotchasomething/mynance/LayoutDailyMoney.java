package ca.gotchasomething.mynance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.text.NumberFormat;

import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.SetUpDbManager;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyInOut;
import ca.gotchasomething.mynance.tabFragments.DailyCreditCard;
import ca.gotchasomething.mynance.tabFragments.DailyWeeklyLimits;

public class LayoutDailyMoney extends MainNavigation {

    Button moneyInButton, moneyOutButton;
    Cursor moneyInCursor, moneyOutCursor, moneyOutCursor2, expenseCursor, incomeCursor, currentCursor;
    DbHelper moneyInHelper, moneyOutHelper, moneyOutHelper2, expenseHelper, incomeHelper, currentHelper;
    Double newAccountBalance, totalSpentOnB, moneyInTotal, totalBudgetAExpenses, totalBudgetIncome, percentB,
            moneyOutTotal, newAvailableBalance, totalSpentOnA, percentA, currentAccountBalance, currentAvailableBalance;
    FrameLayout container;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase moneyInDbDb, moneyOutDbDb, moneyOutDbDb2, expenseDbDb, incomeDbDb, currentDbDb;
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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        totalAccountText = findViewById(R.id.totalAccountText);
        availableAccountText = findViewById(R.id.availableAccountText);

        dailyHeaderText();

        tl = findViewById(R.id.daily_tab_layout);
        container = findViewById(R.id.daily_fragment_container);

        tl.addTab(tl.newTab().setText("Daily Journal"));
        tl.addTab(tl.newTab().setText("Credit Card"));
        tl.addTab(tl.newTab().setText("Weekly Limits"));

        replaceFragment(new DailyMoneyInOut());

        tl.addOnTabSelectedListener(onTabSelectedListener);

    }

    public Double retrieveCurrentAccountBalance() {

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
    }

    public Double retrieveCurrentAvailableBalance() {

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
    }

    /*public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(this);
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        try {
            expenseCursor.moveToFirst();
        } catch(Exception e) {
            totalBudgetAExpenses = 0.0;
        }
        totalBudgetAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(this);
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalBudgetIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        percentB = 1 - (totalBudgetAExpenses / totalBudgetIncome);

        return percentB;

    }*/

    /*public Double retrieveMoneyInTotal() {
        moneyInHelper = new DbHelper(this);
        moneyInDbDb = moneyInHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT sum(moneyInAmount)" + " FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        moneyInCursor.moveToFirst();
        moneyInTotal = moneyInCursor.getDouble(0);
        moneyInCursor.close();

        return moneyInTotal;
    }*/

    /*public Double retrieveMoneyOutTotal() {
        moneyOutHelper = new DbHelper(this);
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME +
                " WHERE " + DbHelper.MONEYOUTCC + " = 'N'", null);
        try {
            moneyOutCursor.moveToFirst();
        } catch(Exception e2) {
            moneyOutTotal = 0.0;
        }
        moneyOutTotal = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return moneyOutTotal;
    }*/

    /*public Double retrieveMoneyOutBTotal() {
        moneyOutHelper2 = new DbHelper(this);
        moneyOutDbDb2 = moneyOutHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " +
                DbHelper.MONEYOUTCC + " = 'N' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        try {
            moneyOutCursor2.moveToFirst();
        } catch(Exception e3) {
            totalSpentOnB = 0.0;
        }
        totalSpentOnB = moneyOutCursor2.getDouble(0);
        moneyOutCursor2.close();

        return totalSpentOnB;
    }*/

    public void dailyHeaderText() {

        newAccountBalance = retrieveCurrentAccountBalance();
        newAvailableBalance = retrieveCurrentAvailableBalance();

        if(newAccountBalance < newAvailableBalance || newAccountBalance == 0.0) {
            newAvailableBalance = 0.0;
        }

        accountBalance2 = currencyFormat.format(newAccountBalance);
        availableBalance2 = currencyFormat.format(newAvailableBalance);

        totalAccountText.setText(accountBalance2);
        availableAccountText.setText(availableBalance2);

        /*(retrieveMoneyInTotal() * retrieveBPercentage()) - retrieveMoneyOutBTotal(); //total ever into acc't attributed to B - total ever spent from acc't on B
        if(newAvailableBalance.isNaN() || newAvailableBalance < 0 || newAccountBalance == 0) {
            newAvailableBalance = 0.0;
        }

        totalSpentOnA = retrieveMoneyOutTotal() - retrieveMoneyOutBTotal();
        percentA = 1 - retrieveBPercentage();

        if(totalSpentOnA > (retrieveMoneyInTotal() * percentA)) {
            newAvailableBalance = (retrieveMoneyInTotal() - retrieveMoneyOutTotal()) - totalSpentOnA; //what it should be minus what was needed to pad A
        }*/

    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = (new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getPosition() == 0) {
                replaceFragment(new DailyMoneyInOut());
            } else if (tab.getPosition() == 1) {
                replaceFragment(new DailyCreditCard());
            } else if (tab.getPosition() == 2) {
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.daily_fragment_container, fragment);

        transaction.commit();
    }
}
