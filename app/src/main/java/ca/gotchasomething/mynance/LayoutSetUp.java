package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gotchasomething.mynance.data.CurrentDb;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutSetUp extends MainNavigation {

    Button setUpIncomeButton, setUpBudgetButton, setUpDebtsButton, setUpSavingsButton;
    CheckBox setUpIncomeCheckbox, setUpAccountCheckbox, setUpBudgetCheckbox, setUpDebtsCheckbox, setUpSavingsCheckbox, setUpTourCheckbox;
    CurrentDb currentDb;
    Date moneyInDate;
    DbManager dbManager;
    Double balanceAmount = 0.0, currentAccount = 0.0, currentB = 0.0, currentA = 0.0, currentOwingA = 0.0, moneyInAmount = 0.0,
            moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0;
    EditText setUpAccountAmount;
    int balanceDone = 0, budgetDone = 0, currentPageId = 0, debtsDone = 0, incomeDone = 0, savingsDone = 0, tourDone = 0;
    Intent setUpIncome, setUpBudget, setUpDebts, setUpSavings, toMainActivity;
    long incRefKeyMI;
    MoneyInDb moneyInDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SetUpDb setUpDb;
    SimpleDateFormat moneyInSDF;
    String startingBalance2 = null, startingBalanceS = null, moneyInCat = null, moneyInCreatedOn = null;
    TextView almostDone, setUpIncomeLabel, setUpAccountAmountLabel, setUpAccountAmountLabel2, setUpAccountAmountLabel3, setUpAccountAmountLabel4, setUpBudgetLabel,
            setUpDebtsLabel, setUpGotItLabel, setUpSavingsLabel, setUpTourLabel, setUpTourLabel2, setUpTourLabel3, setUpTourLabel4;
    Timestamp moneyInTimestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_up);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        dbManager = new DbManager(this);

        setUpIncomeButton = findViewById(R.id.setUpIncomeButton);
        setUpIncomeLabel = findViewById(R.id.setUpIncomeLabel);
        setUpIncomeLabel.setVisibility(View.GONE);
        setUpIncomeCheckbox = findViewById(R.id.setUpIncomeCheckbox);

        setUpDebtsButton = findViewById(R.id.setUpDebtsButton);
        setUpDebtsButton.setVisibility(View.GONE);
        setUpDebtsLabel = findViewById(R.id.setUpDebtsLabel);
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
        setUpAccountAmountLabel3.setVisibility(View.GONE);
        setUpAccountAmountLabel4 = findViewById(R.id.setUpAccountAmountLabel4);
        setUpAccountAmountLabel4.setVisibility(View.GONE);
        setUpAccountCheckbox = findViewById(R.id.setUpAccountCheckbox);
        setUpAccountCheckbox.setVisibility(View.GONE);

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

        setUpIncomeButton.setOnClickListener(onClickSetUpIncomeButton);
        setUpDebtsButton.setOnClickListener(onClickSetUpDebtsButton);
        setUpSavingsButton.setOnClickListener(onClickSetUpSavingsButton);
        setUpBudgetButton.setOnClickListener(onClickSetUpBudgetButton);
        setUpAccountCheckbox.setOnCheckedChangeListener(onCheckBalanceDone);
        setUpTourCheckbox.setOnCheckedChangeListener(onCheckTourCheckbox);

        if (dbManager.incomeSetUpCheck() > 0) {
            setUpIncomeLabel.setVisibility(View.VISIBLE);
            setUpIncomeButton.setVisibility(View.GONE);
            setUpIncomeCheckbox.setChecked(true);
            setUpDebtsButton.setVisibility(View.VISIBLE);
            setUpDebtsLabel.setVisibility(View.GONE);
        }

        if (dbManager.debtSetUpCheck() > 0) {
            setUpDebtsLabel.setVisibility(View.VISIBLE);
            setUpDebtsButton.setVisibility(View.GONE);
            setUpDebtsCheckbox.setChecked(true);
            setUpSavingsButton.setVisibility(View.VISIBLE);
            setUpSavingsLabel.setVisibility(View.GONE);
        }

        if (dbManager.savingsSetUpCheck() > 0) {
            setUpSavingsLabel.setVisibility(View.VISIBLE);
            setUpSavingsButton.setVisibility(View.GONE);
            setUpSavingsCheckbox.setChecked(true);
            setUpBudgetButton.setVisibility(View.VISIBLE);
            setUpBudgetLabel.setVisibility(View.GONE);
        }

        if (dbManager.budgetSetUpCheck() > 0) {
            setUpBudgetLabel.setVisibility(View.VISIBLE);
            setUpBudgetButton.setVisibility(View.GONE);
            setUpBudgetCheckbox.setChecked(true);
            setUpAccountAmount.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel2.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel4.setVisibility(View.VISIBLE);
            setUpAccountCheckbox.setVisibility(View.VISIBLE);
        }

        if (dbManager.balanceSetUpCheck() > 0) {
            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel4.setVisibility(View.GONE);
            setUpAccountAmount.setVisibility(View.GONE);
            setUpAccountCheckbox.setChecked(true);
            almostDone.setVisibility(View.VISIBLE);
            setUpTourLabel.setVisibility(View.VISIBLE);
            setUpTourLabel2.setVisibility(View.VISIBLE);
            setUpTourLabel3.setVisibility(View.VISIBLE);
            setUpTourLabel4.setVisibility(View.VISIBLE);
            setUpGotItLabel.setVisibility(View.VISIBLE);
            setUpTourCheckbox.setVisibility(View.VISIBLE);
        }

        if (dbManager.tourSetUpCheck() > 0) {
            toMainActivity = new Intent(LayoutSetUp.this, MainActivity.class);
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(toMainActivity);
        }
    }

    CheckBox.OnCheckedChangeListener onCheckTourCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            incomeDone = 0;
            debtsDone = 0;
            savingsDone = 0;
            budgetDone = 0;
            balanceDone = 0;
            balanceAmount = 0.0;
            tourDone = 1;

            setUpDb = new SetUpDb(incomeDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

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

            setUpDb = new SetUpDb(incomeDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            moneyInCat = getString(R.string.starting_balance);
            moneyInAmount = balanceAmount;
            if(dbManager.getIncomes().size() == 0) {
                moneyInA = moneyInAmount;
                moneyInB = 0.0;
            } else {
                moneyInA = moneyInAmount * dbManager.retrieveAPercentage();
                moneyInB = moneyInAmount * dbManager.retrieveBPercentage();
            }
            moneyInOwing = 0.0;
            moneyInDate = new Date();
            moneyInTimestamp = new Timestamp(moneyInDate.getTime());
            moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);
            incRefKeyMI = 1;

            moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInA, moneyInOwing, moneyInB, moneyInCreatedOn, incRefKeyMI, 0);

            dbManager.addMoneyIn(moneyInDb);

            currentAccount = balanceAmount;
            currentB = moneyInB;
            currentA = moneyInA;
            currentOwingA = 0.0;
            currentPageId = 1;

            currentDb = new CurrentDb(currentAccount, currentB, currentA, currentOwingA, currentPageId, 0);

            dbManager.addCurrent(currentDb);

            setUpAccountAmountLabel.setVisibility(View.GONE);
            setUpAccountAmountLabel2.setVisibility(View.GONE);
            setUpAccountAmountLabel3.setVisibility(View.VISIBLE);
            setUpAccountAmountLabel4.setVisibility(View.GONE);
            setUpAccountAmount.setVisibility(View.GONE);
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

    View.OnClickListener onClickSetUpIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpIncome = new Intent(LayoutSetUp.this, LayoutSetUpIncome.class);
            setUpIncome.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(setUpIncome);
        }
    };

}
