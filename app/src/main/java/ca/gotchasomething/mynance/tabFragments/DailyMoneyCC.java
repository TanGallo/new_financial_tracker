/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import ca.gotchasomething.mynance.spinners.MoneyOutCCSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyCC extends Fragment {

    boolean newTransaction = true, foundMatchingDebtId = false, foundMatchingSavingsId = false, paymentAPossible = true, paymentBPossible = true;
    Button cancelCCTransEntryButton, ccTransButton, noCCButton, updateCCTransEntryButton, yesCCButton;
    Calendar debtCal, debtCal3, savingsCal;
    CCTransAdapter ccTransAdapter;
    ContentValues currentValue, moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, moneyOutValue5, moneyOutValue6, moneyOutValue7, moneyOutValue8,
            moneyOutValue9, moneyOutValue10, moneyOutValue11, moneyOutValue12;
    Cursor cursor2, cursor6;
    Date debtEndD, debtEndD3, moneyOutDate, savingsDateD;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper5, dbHelper6, dbHelper7, dbHelper8;
    DbManager dbManager;
    Double amountEntry = 0.0, ccTransAmountD = 0.0, ccTransAmountD2 = 0.0, moneyOutA = 0.0, currentChargingDebtAmount = 0.0, currentChargingDebtLimit = 0.0,
            currentDebtAmount = 0.0, debtFrequency = 0.0, moneyOutOwing = 0.0, debtPayments = 0.0, currentDebtPayments3 = 0.0,
            debtRate = 0.0, moneyOutB = 0.0, currentSavingsAmount = 0.0, savingsFrequency = 0.0, savingsPayments = 0.0,
            savingsRate = 0.0, debtAmount = 0.0, moneyOutAmountN = 0.0, debtAnnualIncome = 0.0, moneyOutAmount = 0.0, moneyOutAmount1 = 0.0, newDebtAmount = 0.0, newDebtAmount2 = 0.0,
            newDebtAmount4 = 0.0, newSavingsAmount = 0.0, amountMissing = 0.0, numberOfYearsToPayDebt3 = 0.0, oldMoneyOutAmount = 0.0, rate = 0.0,
            savingsAmount = 0.0, savingsAnnualIncome = 0.0, savingsGoal = 0.0, years = 0.0;
    EditText ccTransAmountText, ccTransAmountEditText;
    General general;
    int moneyOutPaid = 0, moneyOutToPay = 0, numberOfDaysToPayDebt = 0, numberOfDaysToPayDebt3 = 0, numberOfDaysToSavingsGoal = 0;
    Intent refresh;
    LinearLayout updateCreditCardLayout;
    ListView ccTransList;
    long chargingDebtIdS, debtId, expRefKeyMO, moneyOutChargingDebtId, moneyOutRefKeyMO, savingsId;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter ccTransSpinnerAdapter;
    MoneyOutCCSpinnerAdapter ccTransSpinnerAdapter2;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RelativeLayout addCreditCardLayout;
    SimpleDateFormat debtEndS, debtEndS3, moneyOutSDF, savingsDateS;
    Spinner ccTransCatSpinner, ccTransDebtCatSpinner;
    SQLiteDatabase db, db2, db3, db5, db6, db7, db8;
    String ccTransAmount2 = null, ccTransAmountS = null, ccTransAmountS2 = null, ccTransCatS = null, ccTransDebtCatS = null, ccTransPriorityS = null,
            chargingDebtEnd = null, debtEnd = null, moneyOutCat = null, moneyOutCC = null, moneyOutCreatedOn = null, moneyOutDebtCat = null, moneyOutPriority = null,
            moneyOutWeekly = null, moneyOutWeeklyS = null, savingsDate = null;
    TextView ccContinueAnywayText, ccContinueWarningText, ccLimitWarningText, ccPaymentNotPossibleAText, ccPaymentNotPossibleBText, ccTransCatText,
            ccTransDebtCatLabel, newMoneyCCLabel, newMoneyCCLabel2;
    Timestamp moneyOutTimestamp;
    View v;

    public DailyMoneyCC() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_cc, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();

        addCreditCardLayout = v.findViewById(R.id.addCreditCardLayout);
        updateCreditCardLayout = v.findViewById(R.id.updateCreditCardLayout);
        updateCreditCardLayout.setVisibility(View.GONE);
        ccPaymentNotPossibleAText = v.findViewById(R.id.ccPaymentNotPossibleAText);
        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText = v.findViewById(R.id.ccPaymentNotPossibleBText);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccLimitWarningText = v.findViewById(R.id.ccLimitWarningText);
        ccLimitWarningText.setVisibility(View.GONE);
        ccContinueAnywayText = v.findViewById(R.id.ccContinueAnywayText);
        ccContinueAnywayText.setVisibility(View.GONE);
        ccContinueWarningText = v.findViewById(R.id.ccContinueWarningText);
        ccContinueWarningText.setVisibility(View.GONE);
        noCCButton = v.findViewById(R.id.noCCButton);
        noCCButton.setVisibility(View.GONE);
        yesCCButton = v.findViewById(R.id.yesCCButton);
        yesCCButton.setVisibility(View.GONE);
        ccTransAmountText = v.findViewById(R.id.ccTransAmount);
        ccTransButton = v.findViewById(R.id.ccTransButton);
        ccTransList = v.findViewById(R.id.ccTransList);
        ccTransCatText = v.findViewById(R.id.ccTransCatText);
        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText = v.findViewById(R.id.ccTransAmountEditText);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton = v.findViewById(R.id.cancelCCTransEntryButton);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton = v.findViewById(R.id.updateCCTransEntryButton);
        updateCCTransEntryButton.setVisibility(View.GONE);
        newMoneyCCLabel = v.findViewById(R.id.newMoneyCCLabel);
        newMoneyCCLabel.setVisibility(View.GONE);
        newMoneyCCLabel2 = v.findViewById(R.id.newMoneyCCLabel2);
        newMoneyCCLabel2.setVisibility(View.GONE);
        ccTransDebtCatLabel = v.findViewById(R.id.ccTransDebtCatLabel);

        ccTransButton.setOnClickListener(onClickCCTransButton);

        dbManager = new DbManager(getContext());
        ccTransAdapter = new CCTransAdapter(getContext(), dbManager.getCCTrans());
        ccTransList.setAdapter(ccTransAdapter);
        if (ccTransAdapter.getCount() == 0) {
            newMoneyCCLabel.setVisibility(View.VISIBLE);
            newMoneyCCLabel2.setVisibility(View.VISIBLE);
            updateCreditCardLayout.setVisibility(View.GONE);
        } else {
            newMoneyCCLabel.setVisibility(View.GONE);
            newMoneyCCLabel2.setVisibility(View.GONE);
        }

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getReadableDatabase();
        cursor2 = db2.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSENAME + " ASC", null);
        ccTransSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), cursor2);
        ccTransCatSpinner.setAdapter(ccTransSpinnerAdapter);

        ccTransCatSpinner.setOnItemSelectedListener(ccTransSpinnerSelection);

        ccTransDebtCatSpinner = v.findViewById(R.id.ccTransDebtCatSpinner);
        dbHelper6 = new DbHelper(getContext());
        db6 = dbHelper6.getReadableDatabase();
        cursor6 = db6.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTNAME + " ASC", null);
        ccTransSpinnerAdapter2 = new MoneyOutCCSpinnerAdapter(getContext(), cursor6);
        ccTransDebtCatSpinner.setAdapter(ccTransSpinnerAdapter2);

        ccTransDebtCatSpinner.setOnItemSelectedListener(ccDebtTransSpinnerSelection);

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 3);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db3.close();

    }

    public void backToMainLayout() {
        refresh = new Intent(getContext(), LayoutDailyMoney.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(refresh);
    }

    AdapterView.OnItemSelectedListener ccTransSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ccTransCatS = cursor2.getString(cursor2.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            ccTransPriorityS = cursor2.getString(cursor2.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));
            moneyOutWeeklyS = cursor2.getString(cursor2.getColumnIndexOrThrow(DbHelper.EXPENSEWEEKLY));
            moneyOutRefKeyMO = cursor2.getLong(cursor2.getColumnIndexOrThrow(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener ccDebtTransSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ccTransDebtCatS = cursor6.getString(cursor6.getColumnIndex(DbHelper.DEBTNAME));
            chargingDebtIdS = cursor6.getLong(cursor6.getColumnIndex(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void cancelTransaction() {

        updateCreditCardLayout.setVisibility(View.GONE);
        addCreditCardLayout.setVisibility(View.VISIBLE);

        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccLimitWarningText.setVisibility(View.GONE);
        hideWarnings();

        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton.setVisibility(View.GONE);

        ccTransAmountText.setVisibility(View.VISIBLE);
        ccTransCatSpinner.setVisibility(View.VISIBLE);
        ccTransDebtCatSpinner.setVisibility(View.VISIBLE);
        ccTransDebtCatLabel.setVisibility(View.VISIBLE);
        ccTransButton.setVisibility(View.VISIBLE);

        backToMainLayout();
    }

    public void createMoneyOutCC() {
        moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutA, moneyOutOwing, moneyOutB, moneyOutCreatedOn,
                moneyOutCC, moneyOutDebtCat, moneyOutChargingDebtId, moneyOutToPay, moneyOutPaid, expRefKeyMO, 0);
        dbManager.addMoneyOut(moneyOutDb);
    }

    public void updateMoneyOutCC() {
        dbManager.updateMoneyOut(moneyOutDb);
    }

    public void updateDebtsandSavings() {
        updateChargingDebtRecord();

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            updateDebtsRecord();
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            updateSavingsRecord();
        }

        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_LONG).show();
        ccTransAmountText.setText("");
        ccTransCatSpinner.setSelection(0, false);

        cancelTransaction();
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

    /*public Double findCurrentDebtAmount() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (d3.getExpRefKeyD() == moneyOutRefKeyMO) {
                currentDebtAmount = d3.getDebtAmount();
            }
        }
        return currentDebtAmount;
    }*/

    /*public void allDebtData() {
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

        newDebtAmount2 = debtAmount - moneyOutAmount;
        moneyOutValue9 = new ContentValues();
        moneyOutValue9.put(DbHelper.DEBTAMOUNT, newDebtAmount2);
        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue9, DbHelper.ID + "=" + findMatchingDebtId(), null);

        allDebtData();

        moneyOutValue10 = new ContentValues();
        moneyOutValue10.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue10, DbHelper.ID + "=" + findMatchingDebtId(), null);
        db7.close();
    }

    public Double findCurrentChargingDebtAmount() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (d3.getId() == moneyOutChargingDebtId) {
                currentChargingDebtAmount = d3.getDebtAmount();
            }
        }
        return currentChargingDebtAmount;
    }

    public Double findCurrentChargingDebtLimit() {
        for (DebtDb d4 : dbManager.getDebts()) {
            if (d4.getId() == moneyOutChargingDebtId) {
                currentChargingDebtLimit = d4.getDebtLimit();
            }
        }
        return currentChargingDebtLimit;
    }

    public void allChargingDebtData() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == moneyOutChargingDebtId) {
                debtAmount = d2.getDebtAmount();
                debtRate = d2.getDebtRate();
                debtPayments = d2.getDebtPayments();
                debtFrequency = d2.getDebtFrequency();
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }
        }
    }

    public void updateChargingDebtRecord() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        allChargingDebtData();

        newDebtAmount = debtAmount + moneyOutAmount;
        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue, DbHelper.ID + "=" + moneyOutChargingDebtId, null);

        allChargingDebtData();

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.ID + "=" + moneyOutChargingDebtId, null);
        db.close();
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

    /*public Double findCurrentSavingsAmount() {
        for (SavingsDb s3 : dbManager.getSavings()) {
            if (s3.getExpRefKeyS() == moneyOutRefKeyMO) {
                currentSavingsAmount = s3.getSavingsAmount();
            }
        }
        return currentSavingsAmount;
    }*/

    /*public void allSavingsData() {
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

        newSavingsAmount = savingsAmount+ moneyOutAmount;
        moneyOutValue3 = new ContentValues();
        moneyOutValue3.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
        db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue3, DbHelper.ID + "=" + findMatchingSavingsId(), null);

        allSavingsData();

        moneyOutValue4 = new ContentValues();
        moneyOutValue4.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)));
        db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue4, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        db8.close();
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

    /*public void determineAandBPortions() {

        if (newTransaction) {
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
    }*/

    /*View.OnClickListener onClickNoCCButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelTransaction();
        }
    };

    public void defineMoneyOut() {
        moneyOutCat = ccTransCatS;
        moneyOutPriority = ccTransPriorityS;
        moneyOutWeekly = moneyOutWeeklyS;
        moneyOutAmount = general.extractingDouble(ccTransAmountText);
        moneyOutA = 0.0;
        moneyOutB = 0.0;
        moneyOutOwing = 0.0;
        moneyOutDate = new Date();
        moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
        moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
        moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);

        findMatchingSavingsId();
        findMatchingDebtId();

        if (foundMatchingDebtId || foundMatchingSavingsId) {
            moneyOutCC = "N";
        } else {
            moneyOutCC = "Y";
        }
        moneyOutDebtCat = ccTransDebtCatS;
        moneyOutChargingDebtId = chargingDebtIdS;
        moneyOutToPay = 0;
        moneyOutPaid = 0;
        expRefKeyMO = moneyOutRefKeyMO;

        //determineAandBPortions();
    }

    public void checkAndContinue() {
        checkIfAPossible();
        checkIfBPossible();

        if (moneyOutPriority.equals("A")) {
            if (!paymentAPossible) {
                ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                showWarnings();

                noCCButton.setOnClickListener(onClickNoCCButton);

                yesCCButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createOrUpdate();
                        /*if (newTransaction) {
                            createMoneyOutCC();
                            updateDebtsandSavings();
                        } else {
                            updateMoneyOutCC();
                            updateDebtsandSavings();
                        }*/
                        /*ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                        ccTransAdapter.notifyDataSetChanged();
                        backToMainLayout();
                    }
                });
            } else {
                createOrUpdate();
                /*if (newTransaction) {
                    createMoneyOutCC();
                    updateDebtsandSavings();
                } else {
                    updateMoneyOutCC();
                    updateDebtsandSavings();
                }*/
                /*ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                ccTransAdapter.notifyDataSetChanged();
                backToMainLayout();
            }
        } else if (moneyOutPriority.equals("B")) {
            if (!paymentBPossible && !paymentAPossible) {
                ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                showWarnings();

                noCCButton.setOnClickListener(onClickNoCCButton);

                yesCCButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createOrUpdate();
                        /*if (newTransaction) {
                            createMoneyOutCC();
                            updateDebtsandSavings();
                        } else {
                            updateMoneyOutCC();
                            updateDebtsandSavings();
                        }*/
                        /*ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                        ccTransAdapter.notifyDataSetChanged();
                        backToMainLayout();
                    }
                });
            } else if (!paymentBPossible && paymentAPossible) {
                ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                showWarnings();

                noCCButton.setOnClickListener(onClickNoCCButton);

                yesCCButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createOrUpdate();
                        /*if (newTransaction) {
                            createMoneyOutCC();
                            updateDebtsandSavings();
                        } else {
                            updateMoneyOutCC();
                            updateDebtsandSavings();
                        }*/
                        /*ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                        ccTransAdapter.notifyDataSetChanged();
                        backToMainLayout();
                    }
                });
            } else {
                createOrUpdate();
                /*if (newTransaction) {
                    createMoneyOutCC();
                    updateDebtsandSavings();
                } else {
                    updateMoneyOutCC();
                    updateDebtsandSavings();
                }*/
                /*ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                ccTransAdapter.notifyDataSetChanged();
                backToMainLayout();
            }
        }
    }

    public void showWarnings() {
        ccContinueWarningText.setVisibility(View.VISIBLE);
        ccContinueAnywayText.setVisibility(View.VISIBLE);
        noCCButton.setVisibility(View.VISIBLE);
        yesCCButton.setVisibility(View.VISIBLE);
    }

    public void hideWarnings() {
        ccContinueWarningText.setVisibility(View.GONE);
        ccContinueAnywayText.setVisibility(View.GONE);
        noCCButton.setVisibility(View.GONE);
        yesCCButton.setVisibility(View.GONE);
    }

    public void createOrUpdate() {
        if (newTransaction) {
            createMoneyOutCC();
        } else {
            updateMoneyOutCC();
        }
        updateDebtsandSavings();
    }

    View.OnClickListener onClickCCTransButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (dbManager.getDebtCount() == 0) {
                Toast.makeText(getContext(), R.string.no_debt_item_warning, Toast.LENGTH_LONG).show();
            } else {
                defineMoneyOut();
                if (findCurrentChargingDebtAmount() + moneyOutAmount > findCurrentChargingDebtLimit()) {
                    ccLimitWarningText.setVisibility(View.VISIBLE);
                    ccContinueAnywayText.setVisibility(View.VISIBLE);
                    yesCCButton.setVisibility(View.VISIBLE);
                    noCCButton.setVisibility(View.VISIBLE);

                    noCCButton.setOnClickListener(onClickNoCCButton);

                    yesCCButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newTransaction = true;
                            checkAndContinue();
                        }
                    });
                } else {
                    newTransaction = true;
                    checkAndContinue();
                }
            }
        }
    };

    public class CCTransAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private CCTransAdapter(
                Context context,
                List<MoneyOutDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<MoneyOutDb> ccTrans) {
            this.ccTrans = ccTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CCTransViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_cc,
                        parent, false);

                holder = new CCTransViewHolder();
                holder.ccTransEdit = convertView.findViewById(R.id.editMoneyCCButton);
                if (ccTrans.get(position).getMoneyOutPaid() > 0) {
                    holder.ccTransEdit.setVisibility(View.INVISIBLE);
                }
                holder.ccTransDelete = convertView.findViewById(R.id.deleteMoneyCCButton);
                if (ccTrans.get(position).getMoneyOutPaid() > 0) {
                    holder.ccTransDelete.setVisibility(View.INVISIBLE);
                }
                holder.ccTransAmount = convertView.findViewById(R.id.moneyCCAmount);
                holder.ccTransCat = convertView.findViewById(R.id.moneyCCCat);
                holder.ccTransDate = convertView.findViewById(R.id.moneyCCDate);
                holder.ccDebtCat = convertView.findViewById(R.id.moneyCCDebtCat);
                convertView.setTag(holder);

            } else {
                holder = (CCTransViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.ccTransDate.setText(ccTrans.get(position).getMoneyOutCreatedOn());

            //retrieve ccCat
            holder.ccTransCat.setText(ccTrans.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccTransAmountS = (String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                if (ccTransAmountS != null && !ccTransAmountS.equals("")) {
                    ccTransAmountD = Double.valueOf(ccTransAmountS);
                } else {
                    ccTransAmountD = 0.0;
                }
                ccTransAmount2 = currencyFormat.format(ccTransAmountD);
                holder.ccTransAmount.setText(ccTransAmount2);
            } catch (NumberFormatException e) {
                holder.ccTransAmount.setText(ccTransAmount2);
            }

            //retrieve ccDebtCat
            holder.ccDebtCat.setText(ccTrans.get(position).getMoneyOutDebtCat());

            holder.ccTransDelete.setTag(ccTrans.get(position));
            holder.ccTransEdit.setTag(ccTrans.get(position));

            //click on pencil icon
            holder.ccTransEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccTransEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    addCreditCardLayout.setVisibility(View.GONE);
                    updateCreditCardLayout.setVisibility(View.VISIBLE);
                    ccTransAmountText.setVisibility(View.GONE);
                    ccTransCatSpinner.setVisibility(View.GONE);
                    ccTransDebtCatSpinner.setVisibility(View.GONE);
                    ccTransDebtCatLabel.setVisibility(View.GONE);
                    ccTransButton.setVisibility(View.GONE);
                    ccTransCatText.setVisibility(View.VISIBLE);
                    ccTransAmountEditText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    updateCCTransEntryButton.setVisibility(View.VISIBLE);

                    ccTransCatText.setText(moneyOutDb.getMoneyOutCat());

                    ccTransAmountD2 = moneyOutDb.getMoneyOutAmount();
                    ccTransAmountS2 = currencyFormat.format(ccTransAmountD2);
                    ccTransAmountEditText.setText(ccTransAmountS2);

                    oldMoneyOutAmount = general.extractingDollars(ccTransAmountEditText);
                    /*moneyOutA = -(moneyOutDb.getMoneyOutA());
                    moneyOutOwing = -(moneyOutDb.getMoneyOutOwing());
                    moneyOutB = -(moneyOutDb.getMoneyOutB());*/
                    /*moneyOutRefKeyMO = moneyOutDb.getExpRefKeyMO();
                    moneyOutPriority = moneyOutDb.getMoneyOutPriority();
                    moneyOutChargingDebtId = moneyOutDb.getMoneyOutChargingDebtId();
                    moneyOutPaid = moneyOutDb.getMoneyOutPaid();
                }
            });

            updateCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateCreditCardLayout.setVisibility(View.GONE);
                    addCreditCardLayout.setVisibility(View.VISIBLE);

                    amountEntry = general.extractingDouble(ccTransAmountEditText);
                    moneyOutAmount = amountEntry - oldMoneyOutAmount;

                    if (findCurrentChargingDebtAmount() + moneyOutAmount > findCurrentChargingDebtLimit()) {
                        ccLimitWarningText.setVisibility(View.VISIBLE);
                        ccContinueAnywayText.setVisibility(View.VISIBLE);
                        yesCCButton.setVisibility(View.VISIBLE);
                        noCCButton.setVisibility(View.VISIBLE);

                        noCCButton.setOnClickListener(onClickNoCCButton);

                        yesCCButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newTransaction = false;
                                //determineAandBPortions();
                                checkAndContinue();
                            }
                        });
                    } else {
                        newTransaction = false;
                        //determineAandBPortions();
                        checkAndContinue();
                    }
                }
            });

            cancelCCTransEntryButton.setOnClickListener(onClickNoCCButton);

            //click on trash can icon
            holder.ccTransDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccTransDelete.getTag();

                    moneyOutAmount = -(ccTrans.get(position).getMoneyOutAmount());
                    moneyOutRefKeyMO = ccTrans.get(position).getExpRefKeyMO();
                    moneyOutChargingDebtId = ccTrans.get(position).getMoneyOutChargingDebtId();

                    updateDebtsandSavings();

                    /*dbHelper5 = new DbHelper(getContext());
                    db5 = dbHelper5.getWritableDatabase();

                    newDebtAmount = findCurrentChargingDebtAmount() - moneyOutAmount;
                    moneyOutValue5 = new ContentValues();
                    moneyOutValue5.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                    db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue5, DbHelper.ID + "=" + moneyOutChargingDebtId, null);
                    moneyOutValue6 = new ContentValues();
                    moneyOutValue6.put(DbHelper.DEBTEND, calcChargingDebtDate());
                    db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue6, DbHelper.ID + "=" + moneyOutChargingDebtId, null);

                    findMatchingDebtId();
                    if (foundMatchingDebtId) {
                        newDebtAmount4 = findCurrentDebtAmount() + moneyOutAmount;
                        moneyOutValue11 = new ContentValues();
                        moneyOutValue11.put(DbHelper.DEBTAMOUNT, newDebtAmount4);
                        db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue11, DbHelper.ID + "=" + findMatchingDebtId(), null);
                        moneyOutValue12 = new ContentValues();
                        moneyOutValue12.put(DbHelper.DEBTEND, calcDebtDate());
                        db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue12, DbHelper.ID + "=" + findMatchingDebtId(), null);
                    }
                    findMatchingSavingsId();
                    if (foundMatchingSavingsId) {
                        newSavingsAmount = findCurrentSavingsAmount() - moneyOutAmount;
                        moneyOutValue7 = new ContentValues();
                        moneyOutValue7.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                        db5.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue7, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                        moneyOutValue8 = new ContentValues();
                        moneyOutValue8.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                        db5.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue8, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                    }
                    db5.close();*/

                    /*dbManager.deleteMoneyOut(moneyOutDb);
                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    notifyDataSetChanged();
                    backToMainLayout();
                    /*if (ccTransAdapter.getCount() == 0) {
                        newMoneyCCLabel.setVisibility(View.VISIBLE);
                        newMoneyCCLabel2.setVisibility(View.VISIBLE);
                        updateCreditCardLayout.setVisibility(View.GONE);
                    } else {
                        newMoneyCCLabel.setVisibility(View.GONE);
                        newMoneyCCLabel2.setVisibility(View.GONE);
                    }*/
                /*}
            });
            return convertView;
        }
    }

    private static class CCTransViewHolder {
        public TextView ccTransCat;
        public TextView ccTransAmount;
        public TextView ccTransDate;
        public TextView ccDebtCat;
        ImageButton ccTransDelete;
        ImageButton ccTransEdit;
    }
}
*/