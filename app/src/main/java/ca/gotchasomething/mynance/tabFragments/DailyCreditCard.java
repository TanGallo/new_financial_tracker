package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class DailyCreditCard extends Fragment {

    boolean possibleA = true, possibleB = true;
    Button ccTransCancelButton, ccTransContinueButton;
    Calendar debtCal3;
    CCAdapter ccAdapter;
    CCPaymentsAdapter ccPaymentsAdapter;
    CheckBox ccPaidCheckbox;
    ContentValues currentValue, moneyOutValue, moneyOutValue2, updateMoneyOutToPay;
    Date debtEndD3;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5;
    DbManager dbManager;
    Double amountDue = 0.0, ccAmountD = 0.0, currentChargingDebtAmount = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0, debtPayments = 0.0,
            debtRate = 0.0, debtAmount = 0.0, newCurrentAccountBalance = 0.0, newCurrentAvailableBalance = 0.0, newDebtAmount = 0.0,
            numberOfYearsToPayDebt3 = 0.0;
    General general;
    int numberOfDaysToPayDebt3 = 0;
    Intent refresh;
    LinearLayout initialCCLayout;
    ListView ccListView, ccPaymentsList;
    long chargingDebtId;
    MoneyOutDb moneyOutDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat debtEndS3;
    SQLiteDatabase db, db2, db3, db4, db5;
    String amountDueS = null, ccAmountS = null, ccAmount2 = null, chargingDebtEnd = null;
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

        resetToPay();

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

    public void updateChargingDebtRecord() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        newDebtAmount = findCurrentChargingDebtAmount() - ccAmountD;
        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue, DbHelper.ID + "=" + chargingDebtId, null);

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
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.ID + "=" + chargingDebtId, null);
        db.close();
    }

    /*public String calcChargingDebtDate() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == chargingDebtId) {
                debtAmount3 = d2.getDebtAmount();
                currentDebtRate3 = d2.getDebtRate();
                currentDebtPayments3 = d2.getDebtPayments();
                currentDebtFrequency3 = d2.getDebtFrequency();
                currentDebtAnnualIncome = d2.getDebtAnnualIncome();
            }

            debtCal3 = Calendar.getInstance();
            numberOfYearsToPayDebt3 = -(Math.log(1 - (debtAmount3 * (currentDebtRate3 / 100) / ((currentDebtPayments3 * currentDebtFrequency3) - currentDebtAnnualIncome))) / (currentDebtFrequency3 * Math.log(1 + ((currentDebtRate3 / 100) / currentDebtFrequency3))));
            numberOfDaysToPayDebt3 = (int) Math.round(numberOfYearsToPayDebt3 * 365);

            if (debtAmount3 <= 0) {
                chargingDebtEnd = getString(R.string.debt_paid);

            } else if (numberOfDaysToPayDebt3 > Integer.MAX_VALUE || numberOfDaysToPayDebt3 <= 0) {
                chargingDebtEnd = getString(R.string.too_far);

            } else {
                debtCal3 = Calendar.getInstance();
                debtCal3.add(Calendar.DATE, numberOfDaysToPayDebt3);
                debtEndD3 = debtCal3.getTime();
                debtEndS3 = new SimpleDateFormat("dd-MMM-yyyy");
                chargingDebtEnd = debtEndS3.format(debtEndD3);
            }
        }
        return chargingDebtEnd;
    }*/


    public void finishTransaction() {
        updateCurrentAccountBalance();
        updateCurrentAvailableBalance();
        dbManager.updatePaid();
        ccAdapter.notifyDataSetChanged();

        updateChargingDebtRecord();

        /*dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        newDebtAmount = findCurrentChargingDebtAmount() - ccAmountD;
        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue, DbHelper.ID + "=" + chargingDebtId, null);
        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.DEBTEND, calcChargingDebtDate());
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.ID + "=" + chargingDebtId, null);*/

        backToCCLayout();
    }

    public void updateCurrentAvailableBalance() {
        newCurrentAvailableBalance = dbManager.retrieveCurrentAvailableBalance() - dbManager.retrieveToPayBTotal();

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        db3.close();
    }

    public void updateCurrentAccountBalance() {
        newCurrentAccountBalance = dbManager.retrieveCurrentAccountBalance() - dbManager.retrieveToPayTotal();

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance);
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();
    }

    CompoundButton.OnCheckedChangeListener onCheckCCPaid = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            finishTransaction();
        }
    };


    public void checkIfPaymentPossible() {
        possibleA = true;
        possibleB = true;
        if (dbManager.retrieveToPayBTotal() > dbManager.retrieveCurrentAvailableBalance()) {
            possibleB = false;
        } else if (dbManager.retrieveToPayTotal() > dbManager.retrieveCurrentAccountBalance()) {
            possibleA = false;
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
    }

    public void continueTransaction() {
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
    }

    public class CCAdapter extends ArrayAdapter<MoneyOutDb> {

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
                holder.ccCheck.setTag(holder); //NEW
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
                        moneyOutDb.setMoneyOutToPay(1);
                        dbManager.updateMoneyOut(moneyOutDb);
                        checkIfPaymentPossible();
                    } else if (!checkedState[position]) {
                        moneyOutDb.setMoneyOutToPay(0);
                        dbManager.updateMoneyOut(moneyOutDb);
                    }

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

                    ccPaymentsAdapter.notifyDataSetChanged();

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
                    }
                }
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
            List<Double> totalsDueList = new ArrayList<>();
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

            return convertView2;
        }
    }

    private static class CCPaymentsViewHolder {
        public TextView chargingDebt;
        public TextView paymentDue;
    }
}
