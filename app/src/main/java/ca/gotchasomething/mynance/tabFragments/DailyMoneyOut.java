package ca.gotchasomething.mynance.tabFragments;

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
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyOut extends Fragment {

    boolean paymentAPossible = true, paymentBPossible = true, foundMatchingDebtId = false, foundMatchingSavingsId = false;
    Button moneyOutButton, cancelMoneyOutEntryButton, updateMoneyOutEntryButton, paymentNotPossibleContinueButton;
    Calendar debtCal, savingsCal;
    ContentValues moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, moneyOutValue5, moneyOutValue6, moneyOutValue7, moneyOutValue8,
            moneyOutValue9, moneyOutValue10, moneyOutValue11, moneyOutValue12, moneyOutValue13, moneyOutValue14, moneyOutValue15, moneyOutValue17,
            moneyOutValue18, currentValue;
    Cursor moneyOutCursor;
    Date moneyOutDate, debtEndD, savingsDateD;
    DbHelper dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper7, dbHelper8;
    DbManager dbManager;
    Double moneyOutAmount = 0.0, newCurrentAccountBalance2 = 0.0, newCurrentAccountBalance3 = 0.0, newCurrentAvailableBalance3 = 0.0,
            moneyOutD = 0.0, oldMoneyOutAmount = 0.0, newMoneyOutAmount = 0.0, moneyOutAmountD = 0.0, currentDebtAmount = 0.0, debtAmount = 0.0,
            currentSavingsAmount = 0.0, newDebtAmount = 0.0, newSavingsAmount = 0.0, currentDebtRate = 0.0, currentDebtPayments = 0.0,
            currentDebtFrequency = 0.0, numberOfYearsToPayDebt = 0.0, currentSavingsRate = 0.0, currentSavingsPayments = 0.0,
            currentSavingsFrequency = 0.0, numberOfYearsToSavingsGoal = 0.0, savingsAmount = 0.0, newCurrentAvailableBalance = 0.0,
            newCurrentAccountBalance4 = 0.0;
    EditText moneyOutAmountText, moneyOutAmountEditText;
    General general;
    int moneyOutToPay = 0, moneyOutPaid = 0, numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    Intent backToDaily;
    LinearLayout updateMoneyOutLayout;
    ListView moneyOutList;
    long moneyOutRefKeyMO, expRefKeyMO, debtId, savingsId, moneyOutChargingDebtId;
    MoneyOutAdapter moneyOutAdapter;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter moneyOutSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RelativeLayout addMoneyOutLayout;
    SimpleDateFormat moneyOutSDF, debtEndS, savingsDateS;
    Spinner moneyOutCatSpinner;
    SQLiteDatabase db2, db3, db4, db5, db6, db7, db8;
    String moneyOutCatS = null, moneyOutCat = null, moneyOutPriority = null, moneyOutWeekly = null, moneyOutPriorityS = null, moneyOutWeeklyS = null,
            moneyOutCreatedOn = null, moneyOutCC = null, moneyOutS = null, moneyOut2 = null, moneyOutAmountS = null, debtEnd = null,
            savingsDate = null, moneyOutDebtCat = null;
    TextView moneyOutCatText, paymentNotPossibleAText, paymentNotPossibleBText, continueAnywayText, newMoneyOutLabel;
    Timestamp moneyOutTimestamp;
    View v, moneyOutLine, moneyOutLine2;

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

        addMoneyOutLayout = v.findViewById(R.id.addMoneyOutLayout);
        updateMoneyOutLayout = v.findViewById(R.id.updateMoneyOutLayout);
        updateMoneyOutLayout.setVisibility(View.GONE);
        paymentNotPossibleAText = v.findViewById(R.id.paymentNotPossibleAText);
        paymentNotPossibleAText.setVisibility(View.GONE);
        paymentNotPossibleBText = v.findViewById(R.id.paymentNotPossibleBText);
        paymentNotPossibleBText.setVisibility(View.GONE);
        continueAnywayText = v.findViewById(R.id.continueAnywayText);
        continueAnywayText.setVisibility(View.GONE);
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
        moneyOutLine = v.findViewById(R.id.moneyOutLine);
        moneyOutLine.setVisibility(View.GONE);
        moneyOutLine2 = v.findViewById(R.id.moneyOutLine2);
        newMoneyOutLabel = v.findViewById(R.id.newMoneyOutLabel);
        newMoneyOutLabel.setVisibility(View.GONE);

        moneyOutButton.setOnClickListener(onClickMoneyOutButton);

        moneyOutAdapter = new MoneyOutAdapter(getContext(), dbManager.getCashTrans());
        moneyOutList.setAdapter(moneyOutAdapter);
        if(moneyOutAdapter.getCount() == 0) {
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

    }

    public void cancelTransaction() {
        paymentNotPossibleAText.setVisibility(View.GONE);
        paymentNotPossibleBText.setVisibility(View.GONE);
        cancelMoneyOutEntryButton.setVisibility(View.GONE);
        paymentNotPossibleContinueButton.setVisibility(View.GONE);

        moneyOutCatText.setVisibility(View.GONE);
        moneyOutAmountEditText.setVisibility(View.GONE);
        cancelMoneyOutEntryButton.setVisibility(View.GONE);
        updateMoneyOutEntryButton.setVisibility(View.GONE);
        moneyOutLine.setVisibility(View.GONE);

        backToLayoutDailyMoney();
    }

    public void backToLayoutDailyMoney() {
        backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
        backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDaily);
    }

    public void makePaymentB() {
        newCurrentAvailableBalance3 = dbManager.retrieveCurrentAvailableBalance() - moneyOutAmount;
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance3);
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();
    }

    public void makePaymentA() {
        moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutCreatedOn,
                moneyOutCC, moneyOutDebtCat, moneyOutChargingDebtId, moneyOutToPay, moneyOutPaid, expRefKeyMO, 0);
        dbManager.addMoneyOut(moneyOutDb);

        newCurrentAccountBalance3 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance3);
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
            moneyOutValue3 = new ContentValues();
            moneyOutValue3.put(DbHelper.DEBTAMOUNT, newDebtAmount);
            db3.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue3, DbHelper.ID + "=" + findMatchingDebtId(), null);
            moneyOutValue4 = new ContentValues();
            moneyOutValue4.put(DbHelper.DEBTEND, calcDebtDate());
            db3.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue4, DbHelper.ID + "=" + findMatchingDebtId(), null);
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
            moneyOutValue5 = new ContentValues();
            moneyOutValue5.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
            db3.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue5, DbHelper.ID + "=" + findMatchingSavingsId(), null);
            moneyOutValue6 = new ContentValues();
            moneyOutValue6.put(DbHelper.SAVINGSDATE, calcSavingsDate());
            db3.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue6, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        }
        db3.close();

        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
        moneyOutAmountText.setText("");
        moneyOutCatSpinner.setSelection(0, false);
    }

    public void updateA() {
        dbManager.updateMoneyOut(moneyOutDb);

        newCurrentAccountBalance2 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;
        dbHelper8 = new DbHelper(getContext());
        db8 = dbHelper8.getWritableDatabase();
        moneyOutValue15 = new ContentValues();
        moneyOutValue15.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance2);
        db8.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue15, DbHelper.ID + "= '1'", null);

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
            moneyOutValue7 = new ContentValues();
            moneyOutValue7.put(DbHelper.DEBTAMOUNT, newDebtAmount);
            db8.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue7, DbHelper.ID + "=" + findMatchingDebtId(), null);
            moneyOutValue8 = new ContentValues();
            moneyOutValue8.put(DbHelper.DEBTEND, calcDebtDate());
            db8.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue8, DbHelper.ID + "=" + findMatchingDebtId(), null);
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
            moneyOutValue9 = new ContentValues();
            moneyOutValue9.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
            db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue9, DbHelper.ID + "=" + findMatchingSavingsId(), null);
            moneyOutValue10 = new ContentValues();
            moneyOutValue10.put(DbHelper.SAVINGSDATE, calcSavingsDate());
            db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue10, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        }
        db8.close();

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

    public Double findCurrentDebtAmount() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (d3.getExpRefKeyD() == moneyOutRefKeyMO) {
                currentDebtAmount = d3.getDebtAmount();
            }
        }
        return currentDebtAmount;
    }

    public String calcDebtDate() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == findMatchingDebtId()) {
                debtAmount = d2.getDebtAmount();
                currentDebtRate = d2.getDebtRate();
                currentDebtPayments = d2.getDebtPayments();
                currentDebtFrequency = d2.getDebtFrequency();
            }

            debtCal = Calendar.getInstance();
            numberOfYearsToPayDebt = -(Math.log(1 - (debtAmount * (currentDebtRate / 100) / (currentDebtPayments * currentDebtFrequency))) / (currentDebtFrequency * Math.log(1 + ((currentDebtRate / 100) / currentDebtFrequency))));
            numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

            if (debtAmount <= 0) {
                debtEnd = getString(R.string.debt_paid);

            } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
                debtEnd = getString(R.string.too_far);

            } else {
                debtCal = Calendar.getInstance();
                debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
                debtEndD = debtCal.getTime();
                debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
                debtEnd = getString(R.string.debt_will) + " " + debtEndS.format(debtEndD);
            }
        }

        return debtEnd;
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

    public Double findCurrentSavingsAmount() {
        for (SavingsDb s3 : dbManager.getSavings()) {
            if (s3.getExpRefKeyS() == moneyOutRefKeyMO) {
                currentSavingsAmount = s3.getSavingsAmount();
            }
        }
        return currentSavingsAmount;
    }

    public String calcSavingsDate() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == findMatchingSavingsId()) {
                savingsAmount = s2.getSavingsAmount();
                currentSavingsRate = s2.getSavingsRate();
                currentSavingsPayments = s2.getSavingsPayments();
                currentSavingsFrequency = s2.getSavingsFrequency();
            }

            savingsCal = Calendar.getInstance();
            numberOfYearsToSavingsGoal = -(Math.log(1 - (savingsAmount * (currentSavingsRate / 100) / (currentSavingsPayments * currentSavingsFrequency))) / (currentSavingsFrequency * Math.log(1 + ((currentSavingsRate / 100) / currentSavingsFrequency))));
            numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

            if (savingsAmount <= 0) {
                savingsDate = getString(R.string.goal_achieved);

            } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal <= 0) {

                Toast.makeText(getContext(), R.string.too_far, Toast.LENGTH_LONG).show();
                savingsDate = getString(R.string.too_far);

            } else {

                savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
                savingsDateD = savingsCal.getTime();
                savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
                savingsDate = getString(R.string.goal_will) + " " + savingsDateS.format(savingsDateD);
            }
        }

        return savingsDate;
    }

    View.OnClickListener onClickMoneyOutButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = moneyOutCatS;
            moneyOutPriority = moneyOutPriorityS;
            moneyOutWeekly = moneyOutWeeklyS;
            moneyOutAmount = Double.valueOf(moneyOutAmountText.getText().toString());
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

            paymentAPossible = true;
            paymentBPossible = true;

            if (dbManager.retrieveCurrentAccountBalance() - moneyOutAmount < 0) {
                paymentAPossible = false;
            }

            if (dbManager.retrieveCurrentAvailableBalance() - moneyOutAmount < 0) {
                paymentBPossible = false;
            }

            if (moneyOutPriority.equals("A")) {
                if (paymentAPossible) {
                    makePaymentA();
                    backToLayoutDailyMoney();
                } else if (!paymentAPossible) {
                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makePaymentA();
                            backToLayoutDailyMoney();
                        }
                    });
                }
            } else if (moneyOutPriority.equals("B")) {
                if (paymentBPossible && paymentAPossible) {
                    makePaymentA();
                    makePaymentB();
                    backToLayoutDailyMoney();
                } else if (!paymentBPossible && paymentAPossible) {
                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makePaymentA();
                            makePaymentB();
                            backToLayoutDailyMoney();
                        }
                    });
                } else if (!paymentBPossible && !paymentAPossible) {
                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makePaymentA();
                            makePaymentB();
                            backToLayoutDailyMoney();
                        }
                    });

                }
            }
            moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
            moneyOutAdapter.notifyDataSetChanged();

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
                    moneyOutLine2.setVisibility(View.GONE);
                    moneyOutCatText.setVisibility(View.VISIBLE);
                    moneyOutAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    moneyOutLine.setVisibility(View.VISIBLE);

                    moneyOutCatText.setText(moneyOutDb.getMoneyOutCat());

                    moneyOutAmountD = moneyOutDb.getMoneyOutAmount();
                    moneyOutAmountS = currencyFormat.format(moneyOutAmountD);
                    moneyOutAmountEditText.setText(moneyOutAmountS);
                    moneyOutRefKeyMO = moneyOutDb.getExpRefKeyMO();

                    oldMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);

                    updateMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //updateMoneyOutLayout.setVisibility(View.GONE);

                            try {
                                moneyOutDb.setMoneyOutAmount(Double.valueOf(moneyOutAmountEditText.getText().toString()));
                                newMoneyOutAmount = Double.valueOf(moneyOutAmountEditText.getText().toString());
                            } catch (NumberFormatException e) {
                                moneyOutDb.setMoneyOutAmount(general.extractingDollars(moneyOutAmountEditText));
                                newMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);
                            }

                            moneyOutAmount = newMoneyOutAmount - oldMoneyOutAmount;

                            paymentAPossible = true;
                            paymentBPossible = true;

                            if (dbManager.retrieveCurrentAccountBalance() - moneyOutAmount < 0) {
                                paymentAPossible = false;
                            }

                            if (dbManager.retrieveCurrentAvailableBalance() - moneyOutAmount < 0) {
                                paymentBPossible = false;
                            }

                            if (moneyOutDb.getMoneyOutPriority().equals("A")) {
                                if (paymentAPossible) {
                                    updateA();
                                    backToLayoutDailyMoney();
                                } else if (!paymentAPossible) {
                                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateA();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                }
                            } else if (moneyOutDb.getMoneyOutPriority().equals("B")) {
                                if (paymentBPossible && paymentAPossible) {
                                    updateA();
                                    makePaymentB();
                                    backToLayoutDailyMoney();
                                } else if (!paymentBPossible && paymentAPossible) {
                                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateA();
                                            makePaymentB();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                } else if (!paymentBPossible && !paymentAPossible) {
                                    paymentNotPossibleAText.setVisibility(View.VISIBLE);
                                    paymentNotPossibleBText.setVisibility(View.VISIBLE);
                                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                                    paymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelTransaction();
                                        }
                                    });

                                    paymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateA();
                                            makePaymentB();
                                            backToLayoutDailyMoney();
                                        }
                                    });
                                }
                            }

                            moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
                            notifyDataSetChanged();
                        }
                    });

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {
                            updateMoneyOutLayout.setVisibility(View.GONE);
                            //addMoneyOutLayout.setVisibility(View.GONE);
                            cancelTransaction();
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyOutDelete.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    dbHelper7 = new DbHelper(getContext());
                    db7 = dbHelper7.getWritableDatabase();

                    moneyOutDb = (MoneyOutDb) holder.moneyOutDelete.getTag();

                    moneyOutAmount = -(cashTrans.get(position).getMoneyOutAmount());
                    moneyOutRefKeyMO = cashTrans.get(position).getExpRefKeyMO();
                    moneyOutPriority = cashTrans.get(position).getMoneyOutPriority();

                    dbManager.deleteMoneyOut(moneyOutDb);

                    if (moneyOutPriority.equals("B")) {
                        newCurrentAvailableBalance = dbManager.retrieveCurrentAvailableBalance() - moneyOutAmount;
                        moneyOutValue17 = new ContentValues();
                        moneyOutValue17.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance);
                        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue17, DbHelper.ID + "= '1'", null);

                        newCurrentAccountBalance4 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;
                        moneyOutValue18 = new ContentValues();
                        moneyOutValue18.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance4);
                        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue18, DbHelper.ID + "= '1'", null);
                    } else if (moneyOutPriority.equals("A")) {
                        newCurrentAccountBalance4 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;
                        moneyOutValue18 = new ContentValues();
                        moneyOutValue18.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance4);
                        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue18, DbHelper.ID + "= '1'", null);
                    }

                    findMatchingDebtId();
                    if (foundMatchingDebtId) {
                        newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
                        moneyOutValue11 = new ContentValues();
                        moneyOutValue11.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue11, DbHelper.ID + "=" + findMatchingDebtId(), null);
                        moneyOutValue12 = new ContentValues();
                        moneyOutValue12.put(DbHelper.DEBTEND, calcDebtDate());
                        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue12, DbHelper.ID + "=" + findMatchingDebtId(), null);
                    }
                    findMatchingSavingsId();
                    if (foundMatchingSavingsId) {
                        newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
                        moneyOutValue13 = new ContentValues();
                        moneyOutValue13.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                        db7.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue13, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                        moneyOutValue14 = new ContentValues();
                        moneyOutValue14.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                        db7.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue14, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                    }
                    db7.close();

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
