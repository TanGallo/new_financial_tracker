package ca.gotchasomething.mynance.tabFragments;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyOut extends Fragment {

    boolean foundMatchingDebtId = false, foundMatchingSavingsId = false, paymentAPossible = true, paymentBPossible = true, newTransaction = true;
    Button cancelMoneyOutEntryButton, emailNoButton, emailYesButton, enjoyNoButton, enjoyNotSureButton, enjoyYesButton, moneyOutButton, noMoneyOutButton,
            paymentNotPossibleContinueButton, rateNoButton, rateYesButton, updateMoneyOutEntryButton, yesMoneyOutButton;
    Calendar debtCal, savingsCal;
    ContentValues currentValue, moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, moneyOutValue5, moneyOutValue6, moneyOutValue7, moneyOutValue8,
            moneyOutValue9, moneyOutValue10, moneyOutValue11, moneyOutValue12, moneyOutValue13;
    Cursor cursor, moneyOutCursor;
    Date debtEndD, moneyOutDate, savingsDateD;
    DbHelper dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper7, helper, dbHelper8, dbHelper9, dbHelper10;
    DbManager dbManager;
    Double amountEntry = 0.0, currentDebtAmount = 0.0, debtFrequency = 0.0, debtRate = 0.0, debtPayments = 0.0, currentSavingsAmount = 0.0,
            savingsFrequency = 0.0, savingsPayments = 0.0, savingsRate = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, moneyOutAmount = 0.0,
            moneyOutAmountD = 0.0, moneyOutD = 0.0, moneyOutOwing2 = 0.0, newCurrentAccountBalance3 = 0.0, newCurrentAccountBalance = 0.0,
            amountToZero = 0.0, moneyOutA = 0.0, moneyOutA2 = 0.0, moneyOutB = 0.0, moneyOutB2 = 0.0, newABalance = 0.0, newBBalance = 0.0,
            newOwingA = 0.0, moneyOutAN = 0.0, moneyOutAmountN = 0.0, newDebtAmount = 0.0, newSavingsAmount = 0.0,
            amountMissing = 0.0, amountMissingB = 0.0, moneyOutOwing = 0.0, newOwingBalance = 0.0, moneyOutBN = 0.0, moneyOutOwingN = 0.0, oldMoneyOutB = 0.0,
            oldMoneyOutAmount = 0.0, savingsAmount = 0.0, savingsAnnualIncome = 0.0, savingsGoal = 0.0, rate = 0.0, years = 0.0;
    EditText moneyOutAmountEditText, moneyOutAmountText;
    General general;
    int moneyOutPaid = 0, moneyOutToPay = 0, numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    Intent backToDaily, email, goToRatings;
    LinearLayout updateMoneyOutLayout;
    ListView moneyOutList;
    long debtId, expRefKeyMO, id, moneyOutChargingDebtId, moneyOutRefKeyMO, savingsId;
    MoneyOutAdapter moneyOutAdapter;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter moneyOutSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RelativeLayout addMoneyOutLayout;
    SimpleDateFormat debtEndS, moneyOutSDF, savingsDateS;
    Spinner moneyOutCatSpinner;
    SQLiteDatabase db, db2, db3, db4, db5, db7, db8, db9, db10;
    String clicksES = null, clicksS = null, debtEnd = null, moneyOut2 = null, moneyOutAmountS = null, moneyOutCat = null, moneyOutCatS = null, moneyOutCC = null,
            moneyOutCreatedOn = null, moneyOutDebtCat = null, moneyOutPriority = null, moneyOutPriorityS = null, moneyOutS = null, moneyOutWeekly = null,
            moneyOutWeeklyS = null, savingsDate = null;
    TextView continueAnywayText, moneyOutCatText, newMoneyOutLabel, paymentNotPossibleAText, paymentNotPossibleBText, ratingsLabel, ratingsNoLabel, ratingsYesLabel,
            temp, tempE;
    Timestamp moneyOutTimestamp;
    View v;
    public static int clicked = 0;
    public static int clickedE = 0;
    public static final String SRPE = "shared ratings pref";
    public static final String CTE = String.valueOf(clickedE);
    public static final String SRP = "shared ratings pref";
    public static final String CT = String.valueOf(clicked);

    public DailyMoneyOut() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_out, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();
        dbManager = new DbManager(getContext());

        ratingsLabel = v.findViewById(R.id.ratingsLabel);
        ratingsLabel.setVisibility(View.GONE);
        enjoyNoButton = v.findViewById(R.id.enjoyNoButton);
        enjoyNoButton.setVisibility(View.GONE);
        enjoyNotSureButton = v.findViewById(R.id.enjoyNotSureButton);
        enjoyNotSureButton.setVisibility(View.GONE);
        enjoyYesButton = v.findViewById(R.id.enjoyYesButton);
        enjoyYesButton.setVisibility(View.GONE);
        ratingsYesLabel = v.findViewById(R.id.ratingsYesLabel);
        ratingsYesLabel.setVisibility(View.GONE);
        rateNoButton = v.findViewById(R.id.rateNoButton);
        rateNoButton.setVisibility(View.GONE);
        rateYesButton = v.findViewById(R.id.rateYesButton);
        rateYesButton.setVisibility(View.GONE);
        ratingsNoLabel = v.findViewById(R.id.ratingsNoLabel);
        ratingsNoLabel.setVisibility(View.GONE);
        emailNoButton = v.findViewById(R.id.emailNoButton);
        emailNoButton.setVisibility(View.GONE);
        emailYesButton = v.findViewById(R.id.emailYesButton);
        emailYesButton.setVisibility(View.GONE);
        temp = v.findViewById(R.id.temp);
        temp.setVisibility(View.GONE);
        temp.setText(String.valueOf(clicked));
        tempE = v.findViewById(R.id.tempE);
        tempE.setVisibility(View.GONE);
        tempE.setText(String.valueOf(clickedE));
        addMoneyOutLayout = v.findViewById(R.id.addMoneyOutLayout);
        updateMoneyOutLayout = v.findViewById(R.id.updateMoneyOutLayout);
        updateMoneyOutLayout.setVisibility(View.GONE);
        paymentNotPossibleAText = v.findViewById(R.id.paymentNotPossibleAText);
        paymentNotPossibleAText.setVisibility(View.GONE);
        paymentNotPossibleBText = v.findViewById(R.id.paymentNotPossibleBText);
        paymentNotPossibleBText.setVisibility(View.GONE);
        continueAnywayText = v.findViewById(R.id.continueAnywayText);
        continueAnywayText.setVisibility(View.GONE);
        noMoneyOutButton = v.findViewById(R.id.noMoneyOutButton);
        noMoneyOutButton.setVisibility(View.GONE);
        yesMoneyOutButton = v.findViewById(R.id.yesMoneyOutButton);
        yesMoneyOutButton.setVisibility(View.GONE);
        paymentNotPossibleContinueButton = v.findViewById(R.id.paymentNotPossibleContinueButton);
        paymentNotPossibleContinueButton.setVisibility(View.GONE);
        moneyOutAmountText = v.findViewById(R.id.moneyOutAmount);
        moneyOutButton = v.findViewById(R.id.moneyOutButton);
        moneyOutList = v.findViewById(R.id.moneyOutList);
        moneyOutCatText = v.findViewById(R.id.moneyOutCatText);
        moneyOutCatText.setVisibility(View.GONE);
        moneyOutAmountEditText = v.findViewById(R.id.moneyOutAmountEditText);
        moneyOutAmountEditText.setVisibility(View.GONE);
        cancelMoneyOutEntryButton = v.findViewById(R.id.cancelMoneyOutEntryButton);
        cancelMoneyOutEntryButton.setVisibility(View.GONE);
        updateMoneyOutEntryButton = v.findViewById(R.id.updateMoneyOutEntryButton);
        updateMoneyOutEntryButton.setVisibility(View.GONE);
        newMoneyOutLabel = v.findViewById(R.id.newMoneyOutLabel);
        newMoneyOutLabel.setVisibility(View.GONE);

        moneyOutButton.setOnClickListener(onClickMoneyOutButton);

        moneyOutAdapter = new MoneyOutAdapter(getContext(), dbManager.getCashTrans());
        moneyOutList.setAdapter(moneyOutAdapter);
        if (moneyOutAdapter.getCount() == 0) {
            newMoneyOutLabel.setVisibility(View.VISIBLE);
        } else {
            newMoneyOutLabel.setVisibility(View.GONE);
        }

        moneyOutCatSpinner = v.findViewById(R.id.moneyOutCatSpinner);
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getReadableDatabase();
        moneyOutCursor = db2.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSENAME + " ASC", null);
        moneyOutSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor);
        moneyOutCatSpinner.setAdapter(moneyOutSpinnerAdapter);

        moneyOutCatSpinner.setOnItemSelectedListener(moneyOutSpinnerSelection);

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 2);
        dbHelper5 = new DbHelper(getContext());
        db5 = dbHelper5.getWritableDatabase();
        db5.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db5.close();

        loadClicks();
        updateClicks();
        loadClicksE();
        updateClicksE();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(String.valueOf(clicked), temp.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    AdapterView.OnItemSelectedListener moneyOutSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyOutCatS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            moneyOutPriorityS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));
            moneyOutWeeklyS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSEWEEKLY));
            moneyOutRefKeyMO = moneyOutCursor.getLong(moneyOutCursor.getColumnIndexOrThrow(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    /*public void updateB() {
        newCurrentAvailableBalance3 = dbManager.retrieveCurrentB() - moneyOutAmount;
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTB, newCurrentAvailableBalance3);
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();
    }*/

     /*public void updateA() {
        newNeededForABalance = dbManager.retrieveCurrentOwingA() - moneyOutAmount;
        neededFromB = -newNeededForABalance;
        newCurrentAvailableBalance2 = dbManager.retrieveCurrentB() - neededFromB;

        dbHelper9 = new DbHelper(getContext());
        db9 = dbHelper9.getWritableDatabase();

        moneyOutValue3 = new ContentValues();
        if (newNeededForABalance > 0) {
            moneyOutValue3.put(DbHelper.CURRENTOWINGA, newNeededForABalance);
        } else {
            /*if (dbManager.retrieveCurrentAvailableBalance() < neededFromB) {
                moneyOutValue3.put(DbHelper.CURRENTAVAILABLEBALANCE, 0);
                moneyOutValue3.put(DbHelper.NEEDEDFORA, 0);*/
    //} else {
    //moneyOutValue3.put(DbHelper.CURRENTB, newCurrentAvailableBalance2);
    //moneyOutValue3.put(DbHelper.CURRENTOWINGA, 0);
    //}
        /*}

        db9.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue3, DbHelper.ID + "= '1'", null);
        db9.close();
    }*/

        /*public void updateAandB() {
        dbHelper10 = new DbHelper(getContext());
        db10 = dbHelper10.getWritableDatabase();

        newABalance = dbManager.retrieveCurrentA() - moneyOutA;
        newOwingBalance = dbManager.retrieveCurrentOwingA() + moneyOutOwing;
        newBBalance = dbManager.retrieveCurrentB() - moneyOutB;

        moneyOutValue13.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue13.put(DbHelper.CURRENTOWINGA, newOwingBalance);
        moneyOutValue13.put(DbHelper.CURRENTB, newBBalance);
        db10.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue13, DbHelper.ID + "= '1'", null);
        db10.close();
    }*/

         /*public void refundAccount() {
        dbHelper9 = new DbHelper(getContext());
        db9 = dbHelper9.getWritableDatabase();

        newCurrentAccountBalance = dbManager.retrieveCurrentAccountBalance() + moneyOutAmount;

        moneyOutValue3 = new ContentValues();
        moneyOutValue3.put(DbHelper.CURRENTACCOUNT, newCurrentAccountBalance);
        db9.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue3, DbHelper.ID + "= '1'", null);
        db9.close();
    }*/

    /*public void refundA() {
        newCurrentAccountBalance4 = dbManager.retrieveCurrentAccountBalance() + moneyOutAmount;
        newNeededForABalance = dbManager.retrieveCurrentOwingA() + moneyOutAmount;
        moneyOutValue12 = new ContentValues();
        moneyOutValue12.put(DbHelper.CURRENTACCOUNT, newCurrentAccountBalance4);
        moneyOutValue12.put(DbHelper.CURRENTA, newNeededForABalance);
        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue12, DbHelper.ID + "= '1'", null);
    }

    public void refundB() {
        newCurrentAvailableBalance = dbManager.retrieveCurrentB() + moneyOutAmount;
        moneyOutValue11 = new ContentValues();
        moneyOutValue11.put(DbHelper.CURRENTB, newCurrentAvailableBalance);
        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue11, DbHelper.ID + "= '1'", null);
    }*/

    public void loadClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        clicksS = sp.getString(CT, "");
    }

    public void updateClicks() {
        temp.setText(clicksS);

        if (!clicksS.equals("")) {
            clicked = Integer.valueOf(clicksS);
        } else {
            clicked = 0;
        }
    }

    public void loadClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        clicksES = spE.getString(CTE, "");
    }

    public void updateClicksE() {
        tempE.setText(clicksES);

        if (!clicksES.equals("")) {
            clickedE = Integer.valueOf(clicksES);
        } else {
            clickedE = 0;
        }
    }

    public void saveClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CT, temp.getText().toString());
        clicked = Integer.valueOf(temp.getText().toString());
        editor.apply();
    }

    public void saveClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editorE = spE.edit();
        editorE.putString(CTE, tempE.getText().toString());
        clickedE = Integer.valueOf(tempE.getText().toString());
        editorE.apply();
    }

    public boolean checkIfAPossible() {
        if (dbManager.retrieveCurrentAccountBalance() - moneyOutAmount < 0) {
            paymentAPossible = false;
        } else {
            paymentAPossible = true;
        }
        return paymentAPossible;
    }

    public boolean checkIfBPossible() {
        if (dbManager.retrieveCurrentB() - moneyOutAmount < 0) {
            paymentBPossible = false;
        } else {
            paymentBPossible = true;
        }
        return paymentBPossible;
    }

    public void cancelTransaction() {
        paymentNotPossibleAText.setVisibility(View.GONE);
        paymentNotPossibleBText.setVisibility(View.GONE);
        hideContinueButtons();
        hideUpdateLayout();
        backToLayoutDailyMoney();
    }

    public void backToLayoutDailyMoney() {
        backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
        backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDaily);
    }

    public void createMoneyOut() {
        moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutA, moneyOutOwing, moneyOutB, moneyOutCreatedOn,
                moneyOutCC, moneyOutDebtCat, moneyOutChargingDebtId, moneyOutToPay, moneyOutPaid, expRefKeyMO, 0);
        dbManager.addMoneyOut(moneyOutDb);

        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_LONG).show();
        moneyOutAmountText.setText("");
        moneyOutCatSpinner.setSelection(0, false);

        moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
        moneyOutAdapter.notifyDataSetChanged();

        updateAllBalances();
    }

    public void updateMoneyOut() {
        moneyOutDb.setMoneyOutAmount(amountEntry);
        moneyOutDb.setMoneyOutA(moneyOutA);
        moneyOutDb.setMoneyOutOwing(moneyOutOwing);
        moneyOutDb.setMoneyOutB(moneyOutB);
        dbManager.updateMoneyOut(moneyOutDb);
        moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
        moneyOutAdapter.notifyDataSetChanged();

        updateAllBalances();
    }

    public void determineAandBPortionsMoneyOut() {

        if(newTransaction) {
            moneyOutAmountN = moneyOutAmount;
        } else {
            moneyOutAmountN = amountEntry;
        }

        if (moneyOutPriority.equals("A")) {
            if (dbManager.retrieveCurrentA() >= moneyOutAmountN) { //if A can cover the purchase, it does
                moneyOutA = moneyOutAmountN;
                moneyOutOwing = 0.0;
                moneyOutB = 0.0;
            } else if (dbManager.retrieveCurrentA() <= 0) { //if A has no money
                if (dbManager.retrieveCurrentB() >= moneyOutAmountN) { //if B can cover the purchase, it does
                    moneyOutA = 0.0;
                    moneyOutOwing = 0.0;
                    moneyOutB = moneyOutAmountN;
                } else if (dbManager.retrieveCurrentB() == 0) { //if B has no money, A goes negative by whole amount
                    moneyOutA = moneyOutAmountN;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amountMissing = moneyOutAmountN - dbManager.retrieveCurrentB();
                    moneyOutA = amountMissing;
                    moneyOutOwing = 0.0;
                    moneyOutB = dbManager.retrieveCurrentB();
                }
            } else { //if A can cover part of the purchase
                amountMissing = moneyOutAmountN - dbManager.retrieveCurrentA();
                if (dbManager.retrieveCurrentB() >= amountMissing) { //if B can cover the rest, it does
                    moneyOutA = dbManager.retrieveCurrentA();
                    moneyOutOwing = 0.0;
                    moneyOutB = amountMissing;
                } else if (dbManager.retrieveCurrentB() == 0) { //if B has no money, A goes negative by the rest
                    moneyOutA = moneyOutAmountN;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutA = moneyOutAmountN - dbManager.retrieveCurrentB();
                    moneyOutOwing = 0.0;
                    moneyOutB = dbManager.retrieveCurrentB();
                }
            }
        } else if (moneyOutPriority.equals("B")) {
            if (dbManager.retrieveCurrentB() >= moneyOutAmountN) { //if B can cover the purchase, it does
                moneyOutA = 0.0;
                moneyOutOwing = 0.0;
                moneyOutB = moneyOutAmountN;
            } else if (dbManager.retrieveCurrentB() == 0) { //if B has no money, A covers it but is owed for it
                moneyOutA = moneyOutAmountN;
                moneyOutOwing = moneyOutAmountN;
                moneyOutB = 0.0;
            } else { //if B can cover part of the purchase then A covers the rest and is owed for it
                amountMissing = moneyOutAmountN - dbManager.retrieveCurrentB();
                moneyOutA = amountMissing;
                moneyOutOwing = amountMissing;
                moneyOutB = dbManager.retrieveCurrentB();
            }
        }
    }

    public long findMatchingDebtId() {
        foundMatchingDebtId = false;
        for (DebtDb d : dbManager.getDebts()) {
            try {
                if (d.getExpRefKeyD() == moneyOutRefKeyMO) {
                    debtId = d.getId();
                    foundMatchingDebtId = true;
                }
            } catch (Exception e) {
                foundMatchingDebtId = false;
            }
        }
        return debtId;
    }

    public void allDebtData() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == findMatchingDebtId()) {
                debtAmount = d2.getDebtAmount();
                debtRate = d2.getDebtRate();
                debtPayments = d2.getDebtPayments();
                debtFrequency = d2.getDebtFrequency();
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }
        }
    }

    public void updateDebtsRecord() {
        dbHelper7 = new DbHelper(getContext());
        db7 = dbHelper7.getWritableDatabase();

        allDebtData();

        newDebtAmount = debtAmount - moneyOutAmount;

        moneyOutValue7 = new ContentValues();
        moneyOutValue7.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue7, DbHelper.ID + "=" + findMatchingDebtId(), null);

        allDebtData();

        moneyOutValue8 = new ContentValues();
        moneyOutValue8.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue8, DbHelper.ID + "=" + findMatchingDebtId(), null);
        db7.close();
    }

    public long findMatchingSavingsId() {
        foundMatchingSavingsId = false;
        for (SavingsDb s : dbManager.getSavings()) {
            try {
                if (s.getExpRefKeyS() == moneyOutRefKeyMO) {
                    savingsId = s.getId();
                    foundMatchingSavingsId = true;
                }
            } catch (Exception e2) {
                foundMatchingSavingsId = false;
            }
        }
        return savingsId;
    }

    public void allSavingsData() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == findMatchingSavingsId()) {
                savingsAmount = s2.getSavingsAmount();
                savingsGoal = s2.getSavingsGoal();
                savingsRate = s2.getSavingsRate();
                savingsPayments = s2.getSavingsPayments();
                savingsFrequency = s2.getSavingsFrequency();
                savingsAnnualIncome = s2.getSavingsAnnualIncome();
            }
        }
    }

    public void updateSavingsRecord() {
        dbHelper8 = new DbHelper(getContext());
        db8 = dbHelper8.getWritableDatabase();

        allSavingsData();

        newSavingsAmount = savingsAmount + moneyOutAmount;
        moneyOutValue9 = new ContentValues();
        moneyOutValue9.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
        db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue9, DbHelper.ID + "=" + findMatchingSavingsId(), null);

        allSavingsData();

        moneyOutValue10 = new ContentValues();
        moneyOutValue10.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)));
        db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue10, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        db8.close();
    }

    public void updateAllBalances() {

        newCurrentAccountBalance3 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;
        newABalance = dbManager.retrieveCurrentA() - moneyOutA;
        newOwingBalance = dbManager.retrieveCurrentOwingA() + moneyOutOwing;
        newBBalance = dbManager.retrieveCurrentB() - moneyOutB;

        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTACCOUNT, newCurrentAccountBalance3);
        moneyOutValue.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue.put(DbHelper.CURRENTOWINGA, newOwingBalance);
        moneyOutValue.put(DbHelper.CURRENTB, newBBalance);
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        db3.close();

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            updateDebtsRecord();
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            updateSavingsRecord();
        }

        if (dbManager.retrieveCurrentOwingA() < 0) {
            adjustAandB();
        }

        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_LONG).show();
        moneyOutAmountText.setText("");
        moneyOutCatSpinner.setSelection(0, false);
    }

    public void adjustAandB() {
        amountToZero = -(dbManager.retrieveCurrentOwingA());
        newABalance = dbManager.retrieveCurrentA() - amountToZero;
        newOwingA = 0.0;
        newBBalance = dbManager.retrieveCurrentB() + amountToZero;
        moneyOutA2 = moneyOutA - amountToZero;
        moneyOutOwing2 = moneyOutOwing + amountToZero;
        moneyOutB2 = moneyOutB + amountToZero;
        //orig purchase 150 = 150A, 0Owing, 0B then bal = 300A, Owing-50, 0B
        //adj bal = 250A (300-50), 0Owing(-50+50), 50B (0 +50)
        //adj amt = 100A (150-50), 50Owing (0 + 50), 50B, (0+50)

        dbHelper9 = new DbHelper(getContext());
        db9 = dbHelper9.getWritableDatabase();

        moneyOutValue3 = new ContentValues();

        moneyOutValue3.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue3.put(DbHelper.CURRENTOWINGA, newOwingA);
        moneyOutValue3.put(DbHelper.CURRENTB, newBBalance);
        db9.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue3, DbHelper.ID + "= '1'", null);
        db9.close();
    }

    public void refundAandB() {
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();

        newABalance = dbManager.retrieveCurrentA() - moneyOutA;
        newOwingBalance = dbManager.retrieveCurrentOwingA() + moneyOutOwing;
        newBBalance = dbManager.retrieveCurrentB() - moneyOutB;

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue2.put(DbHelper.CURRENTOWINGA, newOwingBalance);
        moneyOutValue2.put(DbHelper.CURRENTB, newBBalance);
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();
    }

    public void askForRating() {

        ratingsLabel.setVisibility(View.VISIBLE);
        enjoyNoButton.setVisibility(View.VISIBLE);
        enjoyNotSureButton.setVisibility(View.VISIBLE);
        enjoyYesButton.setVisibility(View.VISIBLE);
        paymentNotPossibleBText.setVisibility(View.GONE);
        paymentNotPossibleAText.setVisibility(View.GONE);
        continueAnywayText.setVisibility(View.GONE);
        noMoneyOutButton.setVisibility(View.GONE);
        yesMoneyOutButton.setVisibility(View.GONE);
        updateMoneyOutLayout.setVisibility(View.GONE);
        addMoneyOutLayout.setVisibility(View.GONE);
        newMoneyOutLabel.setVisibility(View.GONE);
        moneyOutList.setVisibility(View.GONE);

        enjoyNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingsLabel.setVisibility(View.GONE);
                enjoyNoButton.setVisibility(View.GONE);
                enjoyNotSureButton.setVisibility(View.GONE);
                enjoyYesButton.setVisibility(View.GONE);

                ratingsNoLabel.setVisibility(View.VISIBLE);
                emailNoButton.setVisibility(View.VISIBLE);
                emailYesButton.setVisibility(View.VISIBLE);

                emailNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
                        ratingsNoLabel.setVisibility(View.GONE);
                        emailNoButton.setVisibility(View.GONE);
                        emailYesButton.setVisibility(View.GONE);
                        addMoneyOutLayout.setVisibility(View.VISIBLE);
                        moneyOutList.setVisibility(View.VISIBLE);
                        continueMoneyOut();
                    }
                });
                emailYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickedE++;
                        tempE.setText(String.valueOf(clickedE));

                        saveClicksE();
                        ratingsNoLabel.setVisibility(View.GONE);
                        emailNoButton.setVisibility(View.GONE);
                        emailYesButton.setVisibility(View.GONE);
                        addMoneyOutLayout.setVisibility(View.VISIBLE);
                        moneyOutList.setVisibility(View.VISIBLE);
                        continueMoneyOut();

                        email = new Intent(Intent.ACTION_SEND);
                        email.setType("message/rfc822");
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});

                        try {
                            startActivity(Intent.createChooser(email, getString(R.string.choose_email)));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), getString(R.string.email_warning), Toast.LENGTH_LONG).show();
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        } else {
                            email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        }
                    }
                });
            }
        });
        enjoyNotSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
                ratingsLabel.setVisibility(View.GONE);
                enjoyNoButton.setVisibility(View.GONE);
                enjoyNotSureButton.setVisibility(View.GONE);
                enjoyYesButton.setVisibility(View.GONE);
                addMoneyOutLayout.setVisibility(View.VISIBLE);
                moneyOutList.setVisibility(View.VISIBLE);
                continueMoneyOut();
            }
        });
        enjoyYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingsLabel.setVisibility(View.GONE);
                enjoyNoButton.setVisibility(View.GONE);
                enjoyNotSureButton.setVisibility(View.GONE);
                enjoyYesButton.setVisibility(View.GONE);

                ratingsYesLabel.setVisibility(View.VISIBLE);
                rateNoButton.setVisibility(View.VISIBLE);
                rateYesButton.setVisibility(View.VISIBLE);

                rateNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
                        ratingsYesLabel.setVisibility(View.GONE);
                        rateNoButton.setVisibility(View.GONE);
                        rateYesButton.setVisibility(View.GONE);
                        addMoneyOutLayout.setVisibility(View.VISIBLE);
                        moneyOutList.setVisibility(View.VISIBLE);
                        continueMoneyOut();
                    }
                });
                rateYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked++;
                        temp.setText(String.valueOf(clicked));

                        saveClicks();
                        ratingsYesLabel.setVisibility(View.GONE);
                        rateNoButton.setVisibility(View.GONE);
                        rateYesButton.setVisibility(View.GONE);
                        addMoneyOutLayout.setVisibility(View.VISIBLE);
                        moneyOutList.setVisibility(View.VISIBLE);
                        continueMoneyOut();

                        goToRatings = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_url)));
                        goToRatings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(goToRatings);
                    }
                });
            }
        });
    }

    public void showContinueButtons() {
        noMoneyOutButton.setVisibility(View.VISIBLE);
        continueAnywayText.setVisibility(View.VISIBLE);
        yesMoneyOutButton.setVisibility(View.VISIBLE);
    }

    public void hideContinueButtons() {
        noMoneyOutButton.setVisibility(View.GONE);
        continueAnywayText.setVisibility(View.GONE);
        yesMoneyOutButton.setVisibility(View.GONE);
    }

    public void hideUpdateLayout() {
        moneyOutCatText.setVisibility(View.GONE);
        moneyOutAmountEditText.setVisibility(View.GONE);
        cancelMoneyOutEntryButton.setVisibility(View.GONE);
        updateMoneyOutEntryButton.setVisibility(View.GONE);
    }

    public void continueMoneyOut() {
        moneyOutPriority = moneyOutPriorityS;
        moneyOutWeekly = moneyOutWeeklyS;
        moneyOutAmount = general.extractingDouble(moneyOutAmountText);
        moneyOutDate = new Date();
        moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
        moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
        moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
        moneyOutCC = "N";
        moneyOutDebtCat = "N/A";
        moneyOutChargingDebtId = 0;
        moneyOutToPay = 0;
        moneyOutPaid = 0;
        expRefKeyMO = moneyOutRefKeyMO;

        determineAandBPortionsMoneyOut();

        checkIfAPossible();
        checkIfBPossible();

        if (moneyOutPriority.equals("A")) {
            if (!paymentAPossible) {
                paymentNotPossibleAText.setVisibility(View.VISIBLE);
                showContinueButtons();

                noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelTransaction();
                    }
                });

                yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createMoneyOut();
                        backToLayoutDailyMoney();
                    }
                });
            } else {
                createMoneyOut();
                backToLayoutDailyMoney();
            }
        } else if (moneyOutPriority.equals("B")) {
            if (!paymentBPossible && !paymentAPossible) {
                paymentNotPossibleAText.setVisibility(View.VISIBLE);
                paymentNotPossibleBText.setVisibility(View.VISIBLE);
                showContinueButtons();

                noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelTransaction();
                    }
                });

                yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createMoneyOut();
                        backToLayoutDailyMoney();
                    }
                });
            } else if (!paymentBPossible && paymentAPossible) {
                paymentNotPossibleBText.setVisibility(View.VISIBLE);
                showContinueButtons();

                noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelTransaction();
                    }
                });

                yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createMoneyOut();
                        backToLayoutDailyMoney();
                    }
                });
            } else {
                createMoneyOut();
                backToLayoutDailyMoney();
            }
        }
    }

    View.OnClickListener onClickMoneyOutButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            newTransaction = true;

            helper = new DbHelper(getContext());
            db = helper.getReadableDatabase();
            cursor = db.rawQuery("SELECT max(_id)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
            cursor.moveToFirst();
            id = cursor.getLong(0);
            cursor.close();

            moneyOutCat = moneyOutCatS;
            if (moneyOutCat == null || moneyOutCat.equals("")) {
                Toast.makeText(getContext(), R.string.no_exp_warning, Toast.LENGTH_LONG).show();
            } else if (id != 0 && id % 10 == 0) {
                askForRating();
            } else {
                continueMoneyOut();
            }
        }
    };

    public class MoneyOutAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> cashTrans;

        private MoneyOutAdapter(
                Context context,
                List<MoneyOutDb> cashTrans) {

            super(context, -1, cashTrans);

            this.context = context;
            this.cashTrans = cashTrans;
        }

        public void updateCashTrans(List<MoneyOutDb> cashTrans) {
            this.cashTrans = cashTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return cashTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyOutViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new MoneyOutViewHolder();
                holder.moneyOutEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.moneyOutDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.moneyOutAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.moneyOutCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.moneyOutDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (MoneyOutViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.moneyOutDate.setText(cashTrans.get(position).getMoneyOutCreatedOn());

            //retrieve moneyOutCat
            holder.moneyOutCat.setText(cashTrans.get(position).getMoneyOutCat());

            //moneyOutAmount and format as currency
            try {
                moneyOutS = (String.valueOf(cashTrans.get(position).getMoneyOutAmount()));
                if (moneyOutS != null && !moneyOutS.equals("")) {
                    moneyOutD = Double.valueOf(moneyOutS);
                } else {
                    moneyOutD = 0.0;
                }
                moneyOut2 = currencyFormat.format(moneyOutD);
                holder.moneyOutAmount.setText(moneyOut2);
            } catch (NumberFormatException e) {
                holder.moneyOutAmount.setText(moneyOut2);
            }

            holder.moneyOutDelete.setTag(cashTrans.get(position));
            holder.moneyOutEdit.setTag(cashTrans.get(position));

            //click on pencil icon
            holder.moneyOutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.moneyOutEdit.getTag();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    addMoneyOutLayout.setVisibility(View.GONE);
                    updateMoneyOutLayout.setVisibility(View.VISIBLE);
                    moneyOutAmountText.setVisibility(View.GONE);
                    moneyOutCatSpinner.setVisibility(View.GONE);
                    moneyOutButton.setVisibility(View.GONE);
                    moneyOutCatText.setVisibility(View.VISIBLE);
                    moneyOutAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyOutEntryButton.setVisibility(View.VISIBLE);

                    moneyOutCatText.setText(moneyOutDb.getMoneyOutCat());

                    try {
                        moneyOutAmountD = moneyOutDb.getMoneyOutAmount();
                    } catch (NullPointerException e11) {
                        moneyOutAmountD = 0.0;
                    }
                    moneyOutAmountS = currencyFormat.format(moneyOutAmountD);
                    moneyOutAmountEditText.setText(moneyOutAmountS);

                    moneyOutRefKeyMO = moneyOutDb.getExpRefKeyMO();
                    moneyOutPriority = moneyOutDb.getMoneyOutPriority();

                    try {
                        moneyOutA = -(moneyOutDb.getMoneyOutA());
                    } catch (NullPointerException e2) {
                        moneyOutA = 0.0;
                    }
                    try {
                        moneyOutOwing = -(moneyOutDb.getMoneyOutOwing());
                    } catch (NullPointerException e3) {
                        moneyOutOwing = 0.0;
                    }
                    try {
                        moneyOutB = -(moneyOutDb.getMoneyOutB());
                    } catch (NullPointerException e4) {
                        moneyOutB = 0.0;
                    }

                    oldMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);

                    updateMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            newTransaction = false;

                            amountEntry = general.extractingDouble(moneyOutAmountEditText);
                            moneyOutAmount = amountEntry - oldMoneyOutAmount;

                            refundAandB();
                            determineAandBPortionsMoneyOut();
                            checkIfAPossible();
                            checkIfBPossible();

                            if (moneyOutDb.getMoneyOutPriority().equals("A")) {
                                if (!paymentAPossible) {
                                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                                    showContinueButtons();

                                    noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateMoneyOut();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                } else {
                                    updateMoneyOut();
                                    backToLayoutDailyMoney();
                                }
                            } else if (moneyOutDb.getMoneyOutPriority().equals("B")) {
                                if (!paymentBPossible && !paymentAPossible) {
                                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                                    showContinueButtons();

                                    noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateMoneyOut();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                } else if (!paymentBPossible && paymentAPossible) {
                                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                                    showContinueButtons();

                                    noMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    yesMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateMoneyOut();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                } else {
                                    updateMoneyOut();
                                    backToLayoutDailyMoney();
                                }
                            }
                        }
                    });

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideUpdateLayout();
                            cancelTransaction();
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyOutDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.moneyOutDelete.getTag();

                    try {
                        moneyOutAmount = -(cashTrans.get(position).getMoneyOutAmount());
                    } catch (NullPointerException e5) {
                        moneyOutAmount = 0.0;
                    }
                    try {
                        moneyOutA = -(cashTrans.get(position).getMoneyOutA());
                    } catch (NullPointerException e6) {
                        moneyOutA = 0.0;
                    }
                    try {
                        moneyOutOwing = -(cashTrans.get(position).getMoneyOutOwing());
                    } catch (NullPointerException e7) {
                        moneyOutOwing = 0.0;
                    }
                    try {
                        moneyOutB = -(cashTrans.get(position).getMoneyOutB());
                    } catch (NullPointerException e8) {
                        moneyOutB = 0.0;
                    }

                    moneyOutRefKeyMO = cashTrans.get(position).getExpRefKeyMO();
                    moneyOutPriority = cashTrans.get(position).getMoneyOutPriority();

                    updateAllBalances();

                    dbManager.deleteMoneyOut(moneyOutDb);
                    moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
                    notifyDataSetChanged();

                    backToLayoutDailyMoney();
                }
            });
            return convertView;
        }
    }

    private static class MoneyOutViewHolder {
        public TextView moneyOutDate;
        public TextView moneyOutCat;
        public TextView moneyOutAmount;
        ImageButton moneyOutEdit;
        ImageButton moneyOutDelete;
    }
}
