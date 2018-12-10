package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.CurrentDb;
//import ca.gotchasomething.mynance.data.CurrentDbManager;
import ca.gotchasomething.mynance.data.MoneyInDb;
//import ca.gotchasomething.mynance.data.MoneyInDbManager;
import ca.gotchasomething.mynance.data.SetUpDb;
//import ca.gotchasomething.mynance.data.SetUpDbManager;

public class LayoutSetUp extends MainNavigation {

    Button setUpDebtsButton, setUpSavingsButton, setUpBudgetButton;
    CheckBox setUpDebtsCheckbox, setUpSavingsCheckbox, setUpBudgetCheckbox, setUpAccountCheckbox, setUpTourCheckbox;
    CurrentDb currentDb;
    //CurrentDbManager currentDbManager;
    Date moneyInDate;
    DbManager dbManager;
    Double startingBalanceResult = 0.0, balanceAmount = 0.0, moneyInAmount = 0.0, currentAccountBalance = 0.0, currentAvailableBalance = 0.0;
    EditText setUpAccountAmount;
    int debtsDone, savingsDone, budgetDone, balanceDone, tourDone;
    Intent setUpDebts, setUpSavings, setUpBudget, toMainActivity;
    MoneyInDb moneyInDb;
    //MoneyInDbManager moneyInDbManager;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SetUpDb setUpDb;
    //SetUpDbManager setUpDbManager;
    SimpleDateFormat moneyInSDF;
    String startingBalanceS, startingBalance2, moneyInCat, moneyInCreatedOn;
    TextView setUpDebtsLabel, setUpSavingsLabel, setUpBudgetLabel, setUpAccountAmountLabel, setUpAccountAmountLabel2, setUpAccountAmountLabel3,
            setUpAccountAmountResult, almostDone, setUpTourLabel, setUpTourLabel2, setUpTourLabel3, setUpTourLabel4, setUpGotItLabel;
    Timestamp moneyInTimestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_up);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //menuConfig();
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dbManager = new DbManager(this);
        //setUpDbManager = new SetUpDbManager(this);
        //moneyInDbManager = new MoneyInDbManager(this);
        //currentDbManager = new CurrentDbManager(this);

        setUpDebtsButton = findViewById(R.id.setUpDebtsButton);
        setUpDebtsLabel = findViewById(R.id.setUpDebtsLabel);
        setUpDebtsLabel.setVisibility(View.GONE);
        setUpDebtsCheckbox = findViewById(R.id.setUpDebtsCheckbox);

        setUpSavingsButton = findViewById(R.id.setUpSavingsButton);
        setUpSavingsButton.setVisibility(View.GONE);
        setUpSavingsLabel = findViewById(R.id.setUpSavingsLabel);
        setUpSavingsCheckbox = findViewById(R.id.setUpSavingsCheckbox);

        setUpBudgetButton = findViewById(R.id.setUpBudgetButton);
        setUpBudgetButton.setVisibility(View.GONE);
        setUpBudgetLabel = findViewById(R.id.setUpBudgetLabel);
        setUpBudgetCheckbox = findViewById(R.id.setUpBudgetCheckbox);

        setUpAccountAmount = findViewById(R.id.setUpAccountAmount);
        setUpAccountAmount.setVisibility(View.GONE);
        setUpAccountAmountLabel = findViewById(R.id.setUpAccountAmountLabel);
        setUpAccountAmountLabel.setVisibility(View.GONE);
        setUpAccountAmountLabel2 = findViewById(R.id.setUpAccountAmountLabel2);
        setUpAccountAmountLabel2.setVisibility(View.GONE);
        setUpAccountAmountLabel3 = findViewById(R.id.setUpAccountAmountLabel3);
        setUpAccountCheckbox = findViewById(R.id.setUpAccountCheckbox);
        setUpAccountAmountResult = findViewById(R.id.setUpAccountAmountResult);
        setUpAccountAmountResult.setVisibility(View.GONE);

        almostDone = findViewById(R.id.almostDone);
        almostDone.setVisibility(View.GONE);
        setUpTourLabel = findViewById(R.id.setUpTourLabel);
        setUpTourLabel.setVisibility(View.GONE);
        setUpTourLabel2 = findViewById(R.id.setUpTourLabel2);
        setUpTourLabel2.setVisibility(View.GONE);
        setUpTourLabel3 = findViewById(R.id.setUpTourLabel3);
        setUpTourLabel3.setVisibility(View.GONE);
        setUpTourLabel4 = findViewById(R.id.setUpTourLabel4);
        setUpTourLabel4.setVisibility(View.GONE);
        setUpGotItLabel = findViewById(R.id.setUpGotItLabel);
        setUpGotItLabel.setVisibility(View.GONE);
        setUpTourCheckbox = findViewById(R.id.setUpTourCheckbox);
        setUpTourCheckbox.setVisibility(View.GONE);

        setUpDebtsButton.setOnClickListener(onClickSetUpDebtsButton);
        setUpSavingsButton.setOnClickListener(onClickSetUpSavingsButton);
        setUpBudgetButton.setOnClickListener(onClickSetUpBudgetButton);
        setUpAccountCheckbox.setOnCheckedChangeListener(onCheckBalanceDone);
        setUpTourCheckbox.setOnCheckedChangeListener(onCheckTourCheckbox);

        //dbManager.debtSetUpCheck();
        if (dbManager.debtSetUpCheck() > 0) {
            setUpDebtsLabel.setVisibility(View.VISIBLE);
            setUpDebtsButton.setVisibility(View.GONE);
            setUpDebtsCheckbox.setChecked(true);
            setUpSavingsButton.setVisibility(View.VISIBLE);
            setUpSavingsLabel.setVisibility(View.GONE);
        }

        /*setUpDbManager.debtSetUpCheck();
        if (setUpDbManager.debtSetUpCheck() > 0) {
            setUpDebtsLabel.setVisibility(View.VISIBLE);
            setUpDebtsButton.setVisibility(View.GONE);
            setUpDebtsCheckbox.setChecked(true);
            setUpSavingsButton.setVisibility(View.VISIBLE);
            setUpSavingsLabel.setVisibility(View.GONE);
        }*/

        //dbManager.savingsSetUpCheck();
        if (dbManager.savingsSetUpCheck() > 0) {
            setUpSavingsLabel.setVisibility(View.VISIBLE);
            setUpSavingsButton.setVisibility(View.GONE);
            setUpSavingsCheckbox.setChecked(true);
            setUpBudgetButton.setVisibility(View.VISIBLE);
            setUpBudgetLabel.setVisibility(View.GONE);
        }

        /*setUpDbManager.savingsSetUpCheck();
        if (setUpDbManager.savingsSetUpCheck() > 0) {
            setUpSavingsLabel.setVisibility(View.VISIBLE);
            setUpSavingsButton.setVisibility(View.GONE);
            setUpSavingsCheckbox.setChecked(true);
            setUpBudgetButton.setVisibility(View.VISIBLE);
            setUpBudgetLabel.setVisibility(View.GONE);
        }*/

        //dbManager.budgetSetUpCheck();
        if (dbManager.budgetSetUpCheck() > 0) {
            setUpBudgetLabel.setVisibility(View.VISIBLE);
            setUpBudgetButton.setVisibility(View.GONE);
            setUpBudgetCheckbox.setChecked(true);
            setUpAccountAmountLabel.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel2.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel3.setVisibility(View.GONE);
            setUpAccountAmount.setVisibility(View.VISIBLE);
        }

        /*setUpDbManager.budgetSetUpCheck();
        if (setUpDbManager.budgetSetUpCheck() > 0) {
            setUpBudgetLabel.setVisibility(View.VISIBLE);
            setUpBudgetButton.setVisibility(View.GONE);
            setUpBudgetCheckbox.setChecked(true);
            setUpAccountAmountLabel.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel2.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel3.setVisibility(View.GONE);
            setUpAccountAmount.setVisibility(View.VISIBLE);
        }*/

        //dbManager.balanceSetUpCheck();
        if (dbManager.balanceSetUpCheck() > 0) {
            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountAmountResult.setVisibility(View.VISIBLE);
            dbManager.retrieveStartingBalance();
            startingBalance2 = currencyFormat.format(startingBalanceResult);
            setUpAccountAmountResult.setText(startingBalance2);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }

        /*setUpDbManager.balanceSetUpCheck();
        if (setUpDbManager.balanceSetUpCheck() > 0) {
            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountAmountResult.setVisibility(View.VISIBLE);
            setUpDbManager.retrieveStartingBalance();
            startingBalance2 = currencyFormat.format(startingBalanceResult);
            setUpAccountAmountResult.setText(startingBalance2);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }*/

        //dbManager.tourSetUpCheck();
        if (dbManager.tourSetUpCheck() > 0) {
            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }

        /*setUpDbManager.tourSetUpCheck();
        if (setUpDbManager.tourSetUpCheck() > 0) {
            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }*/

    }

    CheckBox.OnCheckedChangeListener onCheckTourCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            debtsDone = 0;
            savingsDone = 0;
            budgetDone = 0;
            balanceDone = 0;
            balanceAmount = 0.0;
            tourDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);
            //setUpDbManager.addSetUp(setUpDb);

            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }
    };

    CheckBox.OnCheckedChangeListener onCheckBalanceDone = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            balanceDone = 1;

            try {
                startingBalanceS = setUpAccountAmount.getText().toString();
                if (startingBalanceS != null && !startingBalanceS.equals("")) {
                    balanceAmount = Double.valueOf(startingBalanceS);
                } else {
                    balanceAmount = 0.0;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);
            //setUpDbManager.addSetUp(setUpDb);

            currentAccountBalance = balanceAmount;
            currentAvailableBalance = balanceAmount * dbManager.retrieveBPercentage();

            currentDb = new CurrentDb(currentAccountBalance, currentAvailableBalance, 0);

            dbManager.addCurrent(currentDb);

            moneyInCat = "start";
            moneyInAmount = balanceAmount;
            moneyInDate = new Date();
            moneyInTimestamp = new Timestamp(moneyInDate.getTime());
            moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);

            moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInCreatedOn, 0);

            dbManager.addMoneyIn(moneyInDb);

            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountAmountResult.setVisibility(View.VISIBLE);
            dbManager.retrieveStartingBalance();
            //setUpDbManager.retrieveStartingBalance();
            startingBalance2 = currencyFormat.format(startingBalanceResult);
            setUpAccountAmountResult.setText(startingBalance2);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickSetUpBudgetButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpBudget = new Intent(LayoutSetUp.this, LayoutBudget.class);
            setUpBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpBudget);
        }
    };

    View.OnClickListener onClickSetUpSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpSavings = new Intent(LayoutSetUp.this, LayoutSavings.class);
            setUpSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpSavings);
        }
    };


    View.OnClickListener onClickSetUpDebtsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpDebts = new Intent(LayoutSetUp.this, LayoutDebt.class);
            setUpDebts.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpDebts);
        }
    };

}
