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

    boolean initialized = false;
    Button moneyInButton, moneyOutButton;
    Cursor setUpCursor2, moneyInCursor, moneyOutCursor, moneyOutCursor2, expenseCursor, incomeCursor, moneyInCursor2, moneyInCursor3, moneyOutCursor3,
            moneyOutCursor4, moneyOutCursor5, moneyOutCursor6;
    DbHelper setUpHelper2, moneyInHelper, moneyOutHelper, moneyOutHelper2, expenseHelper, incomeHelper, moneyInDbHelper2, moneyInDbHelper3,
            moneyOutDbHelper3, moneyOutDbHelper4, moneyOutDbHelper5, moneyOutDbHelper6;
    Double startingBalance, startingBalanceB, startingBalanceResult, newAccountBalance, income, incomeB, spent, spentB, spentOnB, incomeTotal,
            totalAExpenses, totalIncome, percentB, spentFromAccountTotal, newAvailableBalance, currentAccountBalance, currentAvailableBalance,
            availableStartingBalance, moneyInAmount2, moneyOutAmount2, moneyInBAmount, moneyOutAmountB, thisMoneyInAmount;
    FrameLayout container;
    int startIndex, endIndex, startIndex2, endIndex2;
    long thisId, thisId2, thisId3;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase setUpDbDb2, moneyInDbDb, moneyOutDbDb, moneyOutDbDb2, expenseDbDb, incomeDbDb, moneyInDbDb2, moneyInDbDb3, moneyOutDbDb3, moneyOutDbDb4,
            moneyOutDbDb5, moneyOutDbDb6;
    String accountNumber, accountNumberResult, availableNumber, availableNumberResult, availableBalance2, accountBalance2, startingBalanceS, availableBalanceS, newAccountBalanceS, newAvailableBalanceS;
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

        //totalAccountText.setText(String.valueOf(retrieveStartingBalance()));
        //Double availableStartingBalance = retrieveStartingBalance() * retrieveStartingBalance();
        //availableAccountText.setText(String.valueOf(availableStartingBalance));

        //currentAccountBalance = Double.valueOf(totalAccountText.getText().toString());
        //currentAvailableBalance = Double.valueOf(availableAccountText.getText().toString());

        if(!initialized) {
            firstTime();
            //totalAccountText.setText(String.valueOf(retrieveStartingBalance()));
            //availableStartingBalance = retrieveStartingBalance() * retrieveBPercentage();
            //availableAccountText.setText(String.valueOf(availableStartingBalance));
        } else {
            accountNumber = totalAccountText.getText().toString();
            startIndex = accountNumber.indexOf("$") + 1;
            endIndex = accountNumber.length();
            accountNumberResult = accountNumber.substring(startIndex, endIndex);
            currentAccountBalance = Double.parseDouble(accountNumberResult);

            availableNumber = availableAccountText.getText().toString();
            startIndex2 = availableNumber.indexOf("$") + 1;
            endIndex2 = availableNumber.length();
            availableNumberResult = availableNumber.substring(startIndex, endIndex);
            currentAvailableBalance = Double.parseDouble(availableNumberResult);
            //currentAccountBalance = Double.valueOf(totalAccountText.getText().toString());
            //currentAvailableBalance = Double.valueOf(availableAccountText.getText().toString());
        }
            //dailyHeaderText();
        //}

        tl = findViewById(R.id.daily_tab_layout);
        container = findViewById(R.id.daily_fragment_container);

        tl.addTab(tl.newTab().setText("Daily Journal"));
        tl.addTab(tl.newTab().setText("Credit Card"));
        tl.addTab(tl.newTab().setText("Weekly Limits"));

        replaceFragment(new DailyMoneyInOut());

        tl.addOnTabSelectedListener(onTabSelectedListener);

    }

    /*public void updateMoneyIn() {
        totalAccountText = findViewById(R.id.totalAccountText);
        totalAccountText.setText(String.valueOf(thisMoneyInAmount));
    }*/

    public Double retrieveStartingBalance() {
        setUpHelper2 = new DbHelper(this);
        setUpDbDb2 = setUpHelper2.getReadableDatabase();
        setUpCursor2 = setUpDbDb2.rawQuery("SELECT max(balanceAmount)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor2.moveToFirst();
        startingBalanceResult = setUpCursor2.getDouble(0);
        setUpCursor2.close();

        return startingBalanceResult;
    }

    public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(this);
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseCursor.moveToFirst();
        totalAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(this);
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        percentB = 1 - (totalAExpenses / totalIncome);

        return percentB;

    }

    public boolean firstTime() {

        //retrieveStartingBalance();
        //retrieveBPercentage();

        startingBalanceS = currencyFormat.format(retrieveStartingBalance());
        totalAccountText.setText(startingBalanceS);
        availableStartingBalance = retrieveStartingBalance() * retrieveBPercentage();
        availableBalanceS = currencyFormat.format(availableStartingBalance);
        availableAccountText.setText(availableBalanceS);

        initialized = true;

        currentAccountBalance = startingBalanceResult;
        currentAvailableBalance = startingBalanceResult * percentB;

        return initialized;

    }

    /*public Double retrieveMoneyInAmount() {
        //moneyInDbHelper2 = new DbHelper(getApplicationContext());
        //moneyInDbDb2 = moneyInDbHelper2.getReadableDatabase();
        //moneyInCursor2 = moneyInDbDb2.rawQuery("SELECT max(id) FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        //moneyInCursor2.moveToFirst();
        //thisId = moneyInCursor2.getLong(0);
        //moneyInCursor2.close();

        moneyInDbHelper3 = new DbHelper(getApplicationContext());
        moneyInDbDb3 = moneyInDbHelper3.getReadableDatabase();
        moneyInCursor3 = moneyInDbDb3.rawQuery("SELECT " + DbHelper.MONEYINAMOUNT + " FROM " + DbHelper.MONEY_IN_TABLE_NAME + " WHERE " +
                DbHelper.ID + " = (SELECT max(id) FROM " + DbHelper.MONEY_IN_TABLE_NAME + ")", null);
        //moneyInCursor3 = moneyInDbDb3.rawQuery("SELECT " + DbHelper.MONEYINAMOUNT + " FROM " + DbHelper.MONEY_IN_TABLE_NAME + " WHERE "
                //+ String.valueOf(DbHelper.ID) + " = '" + String.valueOf(thisId) + "'", null);
        try {
            moneyInCursor3.moveToFirst();
        } catch(NullPointerException e3) {
            moneyInAmount2 = 0.0;
        }
        moneyInAmount2 = moneyInCursor3.getDouble(0);
        moneyInCursor3.close();

        return moneyInAmount2;

    }*/

    /*public void retrieveMoneyInAmount() {
        moneyInDbHelper2 = new DbHelper(getApplicationContext());
        moneyInDbDb2 = moneyInDbHelper2.getReadableDatabase();
        moneyInCursor2 = moneyInDbDb2.rawQuery("SELECT max(id) FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        moneyInCursor2.moveToFirst();
        thisId = moneyInCursor2.getLong(0);
        moneyInCursor2.close();

        moneyInDbHelper3 = new DbHelper(getBaseContext());
        moneyInDbDb3 = moneyInDbHelper3.getReadableDatabase();
        moneyInCursor3 = moneyInDbDb3.rawQuery("SELECT " + DbHelper.MONEYINAMOUNT + " FROM " + DbHelper.MONEY_IN_TABLE_NAME + " WHERE " +
                DbHelper.ID + " = (SELECT max(id) FROM " + DbHelper.MONEY_IN_TABLE_NAME + ")", null);
        moneyInCursor3 = moneyInDbDb3.rawQuery("SELECT " + DbHelper.MONEYINAMOUNT + " FROM " + DbHelper.MONEY_IN_TABLE_NAME + " WHERE "
                + String.valueOf(DbHelper.ID) + " = '" + String.valueOf(thisId) + "'", null);
        try {
            moneyInCursor3.moveToFirst();
        } catch(NullPointerException e3) {
            moneyInAmount2 = 0.0;
        }
        moneyInAmount2 = moneyInCursor3.getDouble(0);
        moneyInCursor3.close();

        newAccountBalance = currentAccountBalance + moneyInAmount2;
        newAccountBalanceS = currencyFormat.format(newAccountBalance);
        totalAccountText.setText(newAccountBalanceS);

    }*/

    /*public Double retrieveMoneyOutAmount() {
        moneyOutDbHelper3 = new DbHelper(getApplicationContext());
        moneyOutDbDb3 = moneyOutDbHelper3.getReadableDatabase();
        moneyOutCursor3 = moneyOutDbDb3.rawQuery("SELECT max(id) FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
        moneyOutCursor3.moveToFirst();
        thisId2 = moneyOutCursor3.getLong(0);
        moneyOutCursor3.close();

        moneyOutDbHelper4 = new DbHelper(getApplicationContext());
        moneyOutDbDb4 = moneyOutDbHelper4.getReadableDatabase();
        moneyOutCursor4 = moneyOutDbDb4.rawQuery("SELECT " + DbHelper.MONEYOUTAMOUNT + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE "
                + DbHelper.ID + " = '" + String.valueOf(thisId2) + "'" + " AND " + DbHelper.MONEYOUTCC + " = 'N'", null);
        try {
            moneyOutCursor4.moveToFirst();
        } catch(NullPointerException e) {
            moneyOutAmount2 = 0.0;
        }
        moneyOutAmount2 = moneyOutCursor4.getDouble(0);
        moneyOutCursor4.close();

        return moneyOutAmount2;

    }*/

    /*public Double moneyInB() {
        moneyInBAmount = retrieveMoneyInAmount() * retrieveBPercentage();

        return moneyInBAmount;
    }*/

    /*public Double retrieveMoneyOutBAmount() {
        moneyOutDbHelper5 = new DbHelper(getApplicationContext());
        moneyOutDbDb5 = moneyOutDbHelper5.getReadableDatabase();
        moneyOutCursor5 = moneyOutDbDb5.rawQuery("SELECT max(id) FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
        moneyOutCursor5.moveToFirst();
        thisId3 = moneyOutCursor5.getLong(0);
        moneyOutCursor5.close();

        moneyOutDbHelper6 = new DbHelper(getApplicationContext());
        moneyOutDbDb6 = moneyOutDbHelper6.getReadableDatabase();
        moneyOutCursor6 = moneyOutDbDb6.rawQuery("SELECT " + DbHelper.MONEYOUTAMOUNT + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE "
                + DbHelper.ID + " = '" + String.valueOf(thisId3) + "'" + " AND " + DbHelper.MONEYOUTPRIORITY + " = 'B' " +
                " AND " + DbHelper.MONEYOUTCC + " = 'N'", null);
        try {
            moneyOutCursor6.moveToFirst();
        } catch(NullPointerException e2) {
            moneyOutAmountB = 0.0;
        }
        moneyOutAmountB = moneyOutCursor6.getDouble(0);
        moneyOutCursor6.close();

        return moneyOutAmountB;

    }*/

    /*public void dailyHeaderText() {

        //currentAccountBalance = Double.valueOf(totalAccountText.getText().toString());
        //currentAvailableBalance = Double.valueOf(availableAccountText.getText().toString());

        newAccountBalance = currentAccountBalance + retrieveMoneyInAmount() - retrieveMoneyOutAmount();
        //newAccountBalance = moneyInAmount2;
        newAccountBalanceS = currencyFormat.format(newAccountBalance);
        totalAccountText.setText(newAccountBalanceS);

        newAvailableBalance = currentAvailableBalance + moneyInB() - retrieveMoneyOutBAmount();
        newAvailableBalanceS = currencyFormat.format(newAvailableBalance);
        availableAccountText.setText(newAvailableBalanceS);

    }*/

    /*public Double retrieveIncomeTotal() {
        moneyInHelper = new DbHelper(this);
        moneyInDbDb = moneyInHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT sum(moneyInAmount)" + " FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        moneyInCursor.moveToFirst();
        incomeTotal = moneyInCursor.getDouble(0);
        moneyInCursor.close();

        return incomeTotal;
    }

    public Double retrieveSpentFromAccountTotal() {
        moneyOutHelper = new DbHelper(this);
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC
                 + " = 'N'", null);
        moneyOutCursor.moveToFirst();
        spentFromAccountTotal = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return spentFromAccountTotal;
    }

    public Double retrieveBSpent() {
        moneyOutHelper2 = new DbHelper(this);
        moneyOutDbDb2 = moneyOutHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " +
                DbHelper.MONEYOUTCC + " = 'N' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        moneyOutCursor2.moveToFirst();
        spentOnB = moneyOutCursor2.getDouble(0);
        moneyOutCursor2.close();

        return spentOnB;
    }

    public void dailyHeaderText() {

        startingBalance = retrieveStartingBalance(); //initial balance in account at set up
        income = retrieveIncomeTotal(); //total ever into bank account
        spent = retrieveSpentFromAccountTotal(); //total spent from bank account, not on credit card
        newAccountBalance = startingBalance + income - spent;

        startingBalanceB = retrieveBPercentage() * startingBalance; //percentage of initial balance in account allocated to B
        incomeB = retrieveBPercentage() * income; //percentage of total income ever into bank account allocated to B
        spentB = retrieveBSpent(); //total spent from bank account, not on credit card, in categories marked as B
        newAvailableBalance = startingBalanceB + incomeB - spentB;
        if(newAvailableBalance.isNaN() || newAvailableBalance < 0 || newAccountBalance == 0) {
            newAvailableBalance = 0.0;
        }

        accountBalance2 = currencyFormat.format(newAccountBalance);
        availableBalance2 = currencyFormat.format(newAvailableBalance);

        totalAccountText.setText(accountBalance2);
        availableAccountText.setText(availableBalance2);

    }*/

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
