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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyCreditCard extends Fragment {

    //AbstractMap.SimpleEntry<Long, Double> pair;
    boolean possibleA = true, possibleB = true, newTransaction = true;
    Button ccTransCancelButton, ccTransContinueButton;
    Calendar debtCal3;
    CCAdapter ccAdapter;
    CCPaymentsAdapter ccPaymentsAdapter;
    CheckBox ccPaidCheckbox;
    ContentValues currentValue, debtToPayValue, debtToPayValue2, debtToPayValue3, moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, updateMoneyOutToPay;
    Cursor cursor, cursor2;
    Date debtEndD3;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper6, dbHelper7, dbHelper8, dbHelper9;
    DbManager dbManager;
    Double amountDue = 0.0, ccAmountD = 0.0, currentChargingDebtAmount = 0.0, currentDebtToPay = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0, debtPayments = 0.0,
            debtRate = 0.0, debtAmount = 0.0, newCurrentAccountBalance = 0.0, amountToZero = 0.0, newChargingDebtAmount = 0.0,
            newDebtAmount = 0.0, newDebtToPay = 0.0, moneyOutOwing2 = 0.0, moneyOutB2 = 0.0, newOwingBalance = 0.0, newBBalance = 0.0,
            debtToPay = 0.0, newABalance = 0.0, moneyOutAmountN = 0.0, moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, amountMissing = 0.0;
    General general;
    //HashMap debtAmounts;
    int numberOfDaysToPayDebt3 = 0;
    Intent refresh;
    LinearLayout initialCCLayout;
    ListView ccListView, ccPaymentsList;
    List<Double> totalsDueList;
    long chargingDebtId, debtId, number, chargingDebtId1, moneyOutId;
    //Map<Long, Double> pair;
    //MainNavigation main;
    MoneyOutDb moneyOutDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    //Pair<Long, Double> pair;
    SimpleDateFormat debtEndS3;
    SQLiteDatabase db, db2, db3, db4, db5, db6, db7, db8, db9;
    String amountDue2 = null, id = null, moneyOutPriority = null, ccAmountS = null, ccAmount2 = null, amountDueS = null;
    TextView ccHeaderLabel, ccPaidLabel, ccPayLabel, ccTransContinueAnywayText, ccTransPaymentNotPossibleAText, ccTransPaymentNotPossibleBText, checkBelowLabel,
            noCCTransLabel, totalCCPaymentDueLabel;
    View v;

    public DailyCreditCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_credit_card, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbManager = new DbManager(getContext());
        general = new General();

        checkBelowLabel = v.findViewById(R.id.checkBelowLabel);
        initialCCLayout = v.findViewById(R.id.initalCCLayout);
        ccHeaderLabel = v.findViewById(R.id.ccHeaderLabel);
        ccPayLabel = v.findViewById(R.id.ccPayLabel);
        noCCTransLabel = v.findViewById(R.id.noCCTransLabel);
        noCCTransLabel.setVisibility(View.GONE);
        ccTransPaymentNotPossibleAText = v.findViewById(R.id.ccTransPaymentNotPossibleAText);
        ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
        ccTransPaymentNotPossibleBText = v.findViewById(R.id.ccTransPaymentNotPossibleBText);
        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
        ccTransContinueAnywayText = v.findViewById(R.id.ccTransContinueAnywayText);
        ccTransContinueAnywayText.setVisibility(View.GONE);
        ccTransCancelButton = v.findViewById(R.id.ccTransCancelButton);
        ccTransCancelButton.setVisibility(View.GONE);
        ccTransContinueButton = v.findViewById(R.id.ccTransContinueButton);
        ccTransContinueButton.setVisibility(View.GONE);
        totalCCPaymentDueLabel = v.findViewById(R.id.totalCCPaymentDueLabel);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        ccPaidLabel = v.findViewById(R.id.ccPaidLabel);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox = v.findViewById(R.id.ccPaidCheckbox);
        ccPaidCheckbox.setVisibility(View.GONE);

        ccListView = v.findViewById(R.id.ccListView);
        ccAdapter = new CCAdapter(getContext(), dbManager.getCCTransToPay());
        ccListView.setAdapter(ccAdapter);

        ccPaymentsList = v.findViewById(R.id.ccPaymentsList);
        ccPaymentsAdapter = new CCPaymentsAdapter(getContext(), dbManager.getDebts());
        ccPaymentsList.setAdapter(ccPaymentsAdapter);

        resetToPay();
        resetDebtToPay();

        if (ccAdapter.getCount() == 0) {
            noCCTransLabel.setVisibility(View.VISIBLE);
            checkBelowLabel.setVisibility(View.GONE);
            initialCCLayout.setVisibility(View.GONE);
            ccHeaderLabel.setVisibility(View.GONE);
            ccPayLabel.setVisibility(View.GONE);
            ccListView.setVisibility(View.GONE);
        } else {
            if (dbManager.retrieveToPayTotal() == 0) {
                checkBelowLabel.setVisibility(View.VISIBLE);
                totalCCPaymentDueLabel.setVisibility(View.GONE);
                ccPaymentsList.setVisibility(View.GONE);
                initialCCLayout.setVisibility(View.GONE);
                ccPaidLabel.setVisibility(View.GONE);
                ccPaidCheckbox.setVisibility(View.GONE);
            } else {
                checkBelowLabel.setVisibility(View.GONE);
                totalCCPaymentDueLabel.setVisibility(View.VISIBLE);
                ccPaymentsList.setVisibility(View.VISIBLE);
                initialCCLayout.setVisibility(View.VISIBLE);
                ccPaidLabel.setVisibility(View.VISIBLE);
                ccPaidCheckbox.setVisibility(View.VISIBLE);
            }
            noCCTransLabel.setVisibility(View.GONE);
            ccHeaderLabel.setVisibility(View.VISIBLE);
            ccPayLabel.setVisibility(View.VISIBLE);
            ccListView.setVisibility(View.VISIBLE);
        }

        ccPaidCheckbox.setOnCheckedChangeListener(onCheckCCPaid);

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 4);
        dbHelper5 = new DbHelper(getContext());
        db5 = dbHelper5.getWritableDatabase();
        db5.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db5.close();
    }

    public void backToCCLayout() {
        refresh = new Intent(getContext(), LayoutDailyMoney.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(refresh);
    }

    public Double findCurrentChargingDebtAmount() {
        for (DebtDb d : dbManager.getDebts()) {
            if (d.getId() == chargingDebtId) {
                currentChargingDebtAmount = d.getDebtAmount();
            }
        }
        return currentChargingDebtAmount;
    }

    public void allChargingDebtData() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == chargingDebtId) {
                debtAmount = d2.getDebtAmount();
                debtRate = d2.getDebtRate();
                debtPayments = d2.getDebtPayments();
                debtFrequency = d2.getDebtFrequency();
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }
        }
    }

    public void plusDebtToPay() {
        for (DebtDb d : dbManager.getDebts()) {
            if (d.getId() == chargingDebtId) {
                currentDebtToPay = d.getDebtToPay();
            }
        }
        newDebtToPay = currentDebtToPay + amountDue;

        dbHelper6 = new DbHelper(getContext());
        db6 = dbHelper6.getWritableDatabase();
        debtToPayValue = new ContentValues();
        debtToPayValue.put(DbHelper.DEBTTOPAY, newDebtToPay);
        db6.update(DbHelper.DEBTS_TABLE_NAME, debtToPayValue, DbHelper.ID + "=" + chargingDebtId, null);
        db6.close();
    }

    public void minusDebtToPay() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == chargingDebtId) {
                currentDebtToPay = d2.getDebtToPay();
            }
        }
        newDebtToPay = currentDebtToPay - amountDue;

        dbHelper7 = new DbHelper(getContext());
        db7 = dbHelper7.getWritableDatabase();
        debtToPayValue2 = new ContentValues();
        debtToPayValue2.put(DbHelper.DEBTTOPAY, newDebtToPay);
        db7.update(DbHelper.DEBTS_TABLE_NAME, debtToPayValue2, DbHelper.ID + "=" + chargingDebtId, null);
        db7.close();

    }

    public void resetDebtToPay() {
        dbHelper9 = new DbHelper(getContext());
        db9 = dbHelper9.getWritableDatabase();
        debtToPayValue3 = new ContentValues();
        debtToPayValue3.put(DbHelper.DEBTTOPAY, 0.0);
        db9.update(DbHelper.DEBTS_TABLE_NAME, debtToPayValue3, DbHelper.DEBTTOPAY + "> 0", null);
        db9.close();
    }

    /*public void determineNewDebtAmount() {
        for(DebtDb d : dbManager.getDebts()) {
            if(d.getDebtToPay() > 0) {
                debtId = d.getId();
                currentChargingDebtAmount = d.getDebtAmount();
                currentDebtToPay = d.getDebtToPay();
                newDebtAmount = currentChargingDebtAmount - currentDebtToPay;
            }
        }
    }*/

    /*public void updateDebtRecords() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.DEBTAMOUNT + ", " + DbHelper.DEBTTOPAY + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.DEBTTOPAY + " > 0", null);
            db.execSQL("UPDATE " + DbHelper.DEBTS_TABLE_NAME + " SET " + DbHelper.DEBTAMOUNT + " = " + DbHelper.DEBTAMOUNT + " - " + DbHelper.DEBTTOPAY);
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void updateDebtDates() {
        /*for (DebtDb d4 : dbManager.getDebts()) {
            if (d4.getDebtToPay() > 0) {
                debtAmount = d4.getDebtAmount();
                debtRate = d4.getDebtRate();
                debtPayments = d4.getDebtPayments();
                debtFrequency = d4.getDebtFrequency();
                debtAnnualIncome = d4.getDebtAnnualIncome();

                dbHelper8 = new DbHelper(getContext());
                db8 = dbHelper8.getWritableDatabase();
                moneyOutValue2 = new ContentValues();
                moneyOutValue2.put(DbHelper.DEBTEND, general.calcDebtDate(
                        debtAmount,
                        debtRate,
                        debtPayments,
                        debtFrequency,
                        debtAnnualIncome,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)));
                db8.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.DEBTTOPAY + "> 0", null);
                db8.close();
            }
        }*/
    /*}

    public void refundChargingDebtRecord() {

        dbHelper8 = new DbHelper(getContext());
        db8 = dbHelper8.getWritableDatabase();

        newDebtAmount = findCurrentChargingDebtAmount() + amountDue;
        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db8.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue, DbHelper.ID + "=" + chargingDebtId, null);

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
        db8.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.ID + "=" + chargingDebtId, null);
        db8.close();
    }*/

    /*public void assignAandBPortions() {

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
            //moneyOutAmountN = m.getMoneyOutAmount();
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
                /*dbHelper7 = new DbHelper(getContext());
                db7 = dbHelper7.getWritableDatabase();

                moneyOutValue4 = new ContentValues();
                moneyOutValue4.put(DbHelper.MONEYOUTA, moneyOutA);
                moneyOutValue4.put(DbHelper.MONEYOUTOWING, moneyOutOwing);
                moneyOutValue4.put(DbHelper.MONEYOUTB, moneyOutB);
                db7.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValue4, DbHelper.ID + "=" + moneyOutId, null);

                newABalance = dbManager.retrieveCurrentA() - moneyOutA;
                newOwingBalance = dbManager.retrieveCurrentOwingA() + moneyOutOwing;
                newBBalance = dbManager.retrieveCurrentB() - moneyOutB;

                moneyOutValue3 = new ContentValues();
                moneyOutValue3.put(DbHelper.CURRENTA, newABalance);
                moneyOutValue3.put(DbHelper.CURRENTOWINGA, newOwingBalance);
                moneyOutValue3.put(DbHelper.CURRENTB, newBBalance);
                db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue3, DbHelper.ID + "= '1'", null);
                db7.close();*/
    /*}

    public void finishTransaction() {
        updateAllBalances();
        updateDebtRecords();
        //updateDebtDates();
        //chargeChargingDebtRecord();
        dbManager.updatePaid();
        ccAdapter.notifyDataSetChanged();
        backToCCLayout();
    }*/

    /*public void updateCurrentB() {
        newCurrentAvailableBalance = dbManager.retrieveCurrentB() - dbManager.retrieveToPayBTotal();

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTB, newCurrentAvailableBalance);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        db3.close();
    }*/

    /*public void updateAllBalances() {
        newCurrentAccountBalance = dbManager.retrieveCurrentAccountBalance() - dbManager.retrieveToPayTotal();
        newABalance = dbManager.retrieveCurrentA() - dbManager.retrieveAPortion();
        newOwingBalance = dbManager.retrieveCurrentOwingA() + dbManager.retrieveOwingPortion();
        newBBalance = dbManager.retrieveCurrentB() - dbManager.retrieveBPortion();

        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTACCOUNT, newCurrentAccountBalance);
        moneyOutValue2.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue2.put(DbHelper.CURRENTOWINGA, newOwingBalance);
        moneyOutValue2.put(DbHelper.CURRENTB, newBBalance);
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();

        if (dbManager.retrieveCurrentOwingA() < 0) {
            adjustAandB();
        }
    }

    public void adjustAandB() {
        amountToZero = -(dbManager.retrieveCurrentOwingA());
        newABalance = dbManager.retrieveCurrentA() - amountToZero;
        newOwingBalance = 0.0;
        newBBalance = dbManager.retrieveCurrentB() + amountToZero;
        /*moneyOutA2 = moneyOutA - amountToZero;
        moneyOutOwing2 = moneyOutOwing + amountToZero;
        moneyOutB2 = moneyOutB + amountToZero;*/
        //orig purchase 150 = 150A, 0Owing, 0B then bal = 300A, Owing-50, 0B
        //adj bal = 250A (300-50), 0Owing(-50+50), 50B (0 +50)
        //adj amt = 100A (150-50), 50Owing (0 + 50), 50B, (0+50)

        /*dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();

        moneyOutValue4 = new ContentValues();
        moneyOutValue4.put(DbHelper.CURRENTA, newABalance);
        moneyOutValue4.put(DbHelper.CURRENTOWINGA, newOwingBalance);
        moneyOutValue4.put(DbHelper.CURRENTB, newBBalance);
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue4, DbHelper.ID + "= '1'", null);
        db3.close();
    }*/

    /*public void updateCurrentA() {
        newABalance = dbManager.retrieveCurrentA() - (dbManager.retrieveToPayTotal() - dbManager.retrieveToPayBTotal());

        dbHelper6 = new DbHelper(getContext());
        db6 = dbHelper6.getWritableDatabase();

        moneyOutValue3 = new ContentValues();*/
        /*if (newABalance > 0) {
            moneyOutValue3.put(DbHelper.CURRENTA, newABalance);
        } else {
            if (dbManager.retrieveCurrentAvailableBalance() < neededFromB) {
                moneyOutValue3.put(DbHelper.CURRENTAVAILABLEBALANCE, 0);
                moneyOutValue3.put(DbHelper.NEEDEDFORA, 0);
            } else {
            moneyOutValue3.put(DbHelper.CURRENTB, newCurrentAvailableBalance2);
            moneyOutValue3.put(DbHelper.CURRENTA, 0);*/

        /*db6.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue3, DbHelper.ID + "= '1'", null);
        db6.close();
    }*/

    /*CompoundButton.OnCheckedChangeListener onCheckCCPaid = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            finishTransaction();
        }
    };

    public void checkIfPaymentPossible() {
        possibleA = true;
        possibleB = true;
        if (dbManager.retrieveToPayBTotal() > dbManager.retrieveCurrentB()) {
            possibleB = false;
        } else if (dbManager.retrieveToPayTotal() > dbManager.retrieveCurrentAccountBalance()) {
            possibleA = false;
        }
    }

    public void checkToPayTotal() {
        if (dbManager.retrieveToPayTotal() == 0) {
            checkBelowLabel.setVisibility(View.VISIBLE);
            totalCCPaymentDueLabel.setVisibility(View.GONE);
            ccPaymentsList.setVisibility(View.GONE);
            initialCCLayout.setVisibility(View.GONE);
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
        } else {
            checkBelowLabel.setVisibility(View.GONE);
            totalCCPaymentDueLabel.setVisibility(View.VISIBLE);
            ccPaymentsList.setVisibility(View.VISIBLE);
            initialCCLayout.setVisibility(View.VISIBLE);
            ccPaidLabel.setVisibility(View.VISIBLE);
            ccPaidCheckbox.setVisibility(View.VISIBLE);

            ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
            ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
            ccTransContinueAnywayText.setVisibility(View.GONE);
            ccTransCancelButton.setVisibility(View.GONE);
            ccTransContinueButton.setVisibility(View.GONE);
        }
    }

    public void resetToPay() {
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getWritableDatabase();
        updateMoneyOutToPay = new ContentValues();
        updateMoneyOutToPay.put(DbHelper.MONEYOUTTOPAY, 0);
        db2.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutToPay, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID
                + " = '0'", null);
        db2.close();
    }*/

    /*public void continueTransaction() {
        if (dbManager.retrieveToPayTotal() == 0) {
            checkBelowLabel.setVisibility(View.VISIBLE);
            initialCCLayout.setVisibility(View.GONE);
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
        } else {
            checkBelowLabel.setVisibility(View.GONE);
            initialCCLayout.setVisibility(View.VISIBLE);
            ccPaidLabel.setVisibility(View.VISIBLE);
            ccPaidCheckbox.setVisibility(View.VISIBLE);
        }

        ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
        ccTransContinueAnywayText.setVisibility(View.GONE);
        ccTransCancelButton.setVisibility(View.GONE);
        ccTransContinueButton.setVisibility(View.GONE);
    }*/

    /*public class CCAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTransToPay;
        boolean[] checkedState;

        private CCAdapter(
                Context context,
                List<MoneyOutDb> ccTransToPay) {

            super(context, -1, ccTransToPay);

            this.context = context;
            this.ccTransToPay = ccTransToPay;
            checkedState = new boolean[ccTransToPay.size()];
        }

        public void updateCCTransToPay(List<MoneyOutDb> ccTransToPay) {
            this.ccTransToPay = ccTransToPay;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTransToPay.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CCViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_credit_card,
                        parent, false);

                holder = new CCViewHolder();
                holder.ccCat = convertView.findViewById(R.id.ccCat);
                holder.ccAmount = convertView.findViewById(R.id.ccAmount);
                holder.ccChargedOn = convertView.findViewById(R.id.ccChargedOn);
                holder.ccCheck = convertView.findViewById(R.id.ccCheck);
                convertView.setTag(holder);

            } else {
                holder = (CCViewHolder) convertView.getTag();
                holder.ccCheck.setTag(holder);
            }

            //retrieve ccAmount and format as currency
            try {
                ccAmountS = (String.valueOf(ccTransToPay.get(position).getMoneyOutAmount()));
                if (ccAmountS != null && !ccAmountS.equals("")) {
                    ccAmountD = Double.valueOf(ccAmountS);
                } else {
                    ccAmountD = 0.0;
                }
                ccAmount2 = currencyFormat.format(ccAmountD);
                holder.ccAmount.setText(ccAmount2);
            } catch (NumberFormatException e) {
                holder.ccAmount.setText(ccAmount2);
            }

            //retrieve ccCat
            holder.ccCat.setText(ccTransToPay.get(position).getMoneyOutCat());

            //retrieve chargingDebt
            holder.ccChargedOn.setText(ccTransToPay.get(position).getMoneyOutDebtCat());

            holder.ccCheck.setTag(ccTransToPay.get(position));
            holder.ccCheck.setTag(R.id.ccCheck, position);

            holder.ccCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moneyOutDb = (MoneyOutDb) holder.ccCheck.getTag();
                    checkedState[position] = !checkedState[position];

                    if (checkedState[position]) {
                        moneyOutAmountN = moneyOutDb.getMoneyOutAmount();
                        moneyOutPriority = moneyOutDb.getMoneyOutPriority();
                        chargingDebtId = moneyOutDb.getMoneyOutChargingDebtId();
                        amountDue = moneyOutDb.getMoneyOutAmount();
                        assignAandBPortions();
                        moneyOutDb.setMoneyOutToPay(1);
                        moneyOutDb.setMoneyOutA(moneyOutA);
                        moneyOutDb.setMoneyOutOwing(moneyOutOwing);
                        moneyOutDb.setMoneyOutB(moneyOutB);
                        dbManager.updateMoneyOut(moneyOutDb);
                        checkIfPaymentPossible();
                        if (!possibleB) {
                            checkBelowLabel.setVisibility(View.GONE);
                            initialCCLayout.setVisibility(View.GONE);
                            ccPaidLabel.setVisibility(View.GONE);
                            ccPaidCheckbox.setVisibility(View.GONE);
                            ccTransPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                            ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                            ccTransCancelButton.setVisibility(View.VISIBLE);
                            ccTransContinueButton.setVisibility(View.VISIBLE);

                            ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.ccCheck.setChecked(false);
                                    backToCCLayout();
                                }
                            });

                            ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    plusDebtToPay();
                                    //chargeChargingDebtRecord();
                                    //updateChargingDebtDate();
                                    checkToPayTotal();
                                    ccPaymentsAdapter.notifyDataSetChanged();
                                }
                            });
                        } else if (!possibleA) {
                            checkBelowLabel.setVisibility(View.GONE);
                            initialCCLayout.setVisibility(View.GONE);
                            ccPaidLabel.setVisibility(View.GONE);
                            ccPaidCheckbox.setVisibility(View.GONE);
                            ccTransPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                            ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                            ccTransCancelButton.setVisibility(View.VISIBLE);
                            ccTransContinueButton.setVisibility(View.VISIBLE);

                            ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.ccCheck.setChecked(false);
                                    backToCCLayout();
                                }
                            });

                            ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    plusDebtToPay();
                                    //chargeChargingDebtRecord();
                                    //updateChargingDebtDate();
                                    checkToPayTotal();
                                    ccPaymentsAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            plusDebtToPay();
                            //chargeChargingDebtRecord();
                            //updateChargingDebtDate();
                            checkToPayTotal();
                            ccPaymentsAdapter.notifyDataSetChanged();
                        }
                        //ccPaymentsAdapter.notifyDataSetChanged();
                    } else if (!checkedState[position]) {
                        chargingDebtId = moneyOutDb.getMoneyOutChargingDebtId();
                        amountDue = moneyOutDb.getMoneyOutAmount();
                        moneyOutDb.setMoneyOutToPay(0);
                        moneyOutDb.setMoneyOutA(0.0);
                        moneyOutDb.setMoneyOutOwing(0.0);
                        moneyOutDb.setMoneyOutB(0.0);
                        dbManager.updateMoneyOut(moneyOutDb);
                        minusDebtToPay();
                        //refundChargingDebtRecord();
                        //updateChargingDebtDate();
                        checkToPayTotal();
                        ccPaymentsAdapter.notifyDataSetChanged();
                        //ccPaymentsAdapter.notifyDataSetChanged();
                        /*ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
                        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
                        ccTransContinueAnywayText.setVisibility(View.GONE);
                        ccTransCancelButton.setVisibility(View.GONE);
                        ccTransContinueButton.setVisibility(View.GONE);*/
                    /*}

                    if (dbManager.retrieveToPayTotal() == 0) {
                        checkBelowLabel.setVisibility(View.VISIBLE);
                        totalCCPaymentDueLabel.setVisibility(View.GONE);
                        ccPaymentsList.setVisibility(View.GONE);
                        initialCCLayout.setVisibility(View.GONE);
                        ccPaidLabel.setVisibility(View.GONE);
                        ccPaidCheckbox.setVisibility(View.GONE);
                    } else {
                        checkBelowLabel.setVisibility(View.GONE);
                        totalCCPaymentDueLabel.setVisibility(View.VISIBLE);
                        ccPaymentsList.setVisibility(View.VISIBLE);
                        initialCCLayout.setVisibility(View.VISIBLE);
                        ccPaidLabel.setVisibility(View.VISIBLE);
                        ccPaidCheckbox.setVisibility(View.VISIBLE);
                    }*/

                    //ccPaymentsAdapter.notifyDataSetChanged();

                    /*if (!possibleB) {
                        checkBelowLabel.setVisibility(View.GONE);
                        initialCCLayout.setVisibility(View.GONE);
                        ccPaidLabel.setVisibility(View.GONE);
                        ccPaidCheckbox.setVisibility(View.GONE);
                        ccTransPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                        ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                        ccTransCancelButton.setVisibility(View.VISIBLE);
                        ccTransContinueButton.setVisibility(View.VISIBLE);

                        ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.ccCheck.setChecked(false);
                                backToCCLayout();
                            }
                        });

                        ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                continueTransaction();
                            }
                        });
                    } else if (!possibleA) {
                        checkBelowLabel.setVisibility(View.GONE);
                        initialCCLayout.setVisibility(View.GONE);
                        ccPaidLabel.setVisibility(View.GONE);
                        ccPaidCheckbox.setVisibility(View.GONE);
                        ccTransPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                        ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                        ccTransCancelButton.setVisibility(View.VISIBLE);
                        ccTransContinueButton.setVisibility(View.VISIBLE);

                        ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.ccCheck.setChecked(false);
                                backToCCLayout();
                            }
                        });

                        ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                continueTransaction();
                            }
                        });
                    }*/
                /*}
            });

            holder.ccCheck.setChecked(checkedState[position]);

            return convertView;
        }
    }

    private static class CCViewHolder {
        public TextView ccCat;
        public TextView ccAmount;
        public TextView ccChargedOn;
        public CheckBox ccCheck;
    }

    public class CCPaymentsAdapter extends ArrayAdapter<DebtDb> {

        private Context context;
        private List<DebtDb> debts;

        private CCPaymentsAdapter(
                Context context,
                List<DebtDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<DebtDb> debts) {
            this.debts = debts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return debts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView2, @NonNull ViewGroup parent) {

            final CCPaymentsViewHolder holder2;

            if (convertView2 == null) {
                convertView2 = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_cc_payments,
                        parent, false);

                holder2 = new CCPaymentsViewHolder();
                holder2.chargingDebt = convertView2.findViewById(R.id.chargingDebt);
                holder2.paymentDue = convertView2.findViewById(R.id.paymentDue);
                convertView2.setTag(holder2);

            } else {
                holder2 = (CCPaymentsViewHolder) convertView2.getTag();
            }

            //retrieve chargingDebtCat
            chargingDebtId = debts.get(position).getId();
            holder2.chargingDebt.setText(debts.get(position).getDebtName());

            //retrieve amount due in each category and format as currency
            totalsDueList = new ArrayList<>();
            for (MoneyOutDb m2 : dbManager.getMoneyOuts()) {
                if (String.valueOf(m2.getMoneyOutChargingDebtId()).equals(String.valueOf(chargingDebtId)) && m2.getMoneyOutToPay() == 1 && m2.getMoneyOutPaid() == 0) {
                    totalsDueList.add(m2.getMoneyOutAmount());
                }
            }
            amountDue = 0.0;
            if (totalsDueList.size() == 0) {
                amountDue = 0.0;
            } else {
                for (Double dbl : totalsDueList) {
                    amountDue += dbl;
                }
            }
            amountDueS = currencyFormat.format(amountDue);
            holder2.paymentDue.setText(amountDueS);

            if (amountDue == 0.0) {
                holder2.chargingDebt.setVisibility(View.GONE);
                holder2.paymentDue.setVisibility(View.GONE);
            } else {
                holder2.chargingDebt.setVisibility(View.VISIBLE);
                holder2.paymentDue.setVisibility(View.VISIBLE);
            }

            //pair = new AbstractMap.SimpleEntry<>(chargingDebtId, amountDue);

            //pair = new Pair<>(chargingDebtId, amountDue);

        debtAmounts = new HashMap<>();
        debtAmounts.put(chargingDebtId, amountDue);*/

            /*return convertView2;
        }
    }

    private static class CCPaymentsViewHolder {
        public TextView chargingDebt;
        public TextView paymentDue;
    }
}*/
