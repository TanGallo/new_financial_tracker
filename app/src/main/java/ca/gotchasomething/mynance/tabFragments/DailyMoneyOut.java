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
import android.widget.ListView;
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

    boolean possible = true, foundMatchingDebtId = false, foundMatchingSavingsId = false;
    Button moneyOutButton, cancelMoneyOutEntryButton, updateMoneyOutEntryButton;
    Calendar debtCal, savingsCal;
    ContentValues moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, moneyOutValue5, moneyOutValue6, moneyOutValue7, moneyOutValue8, currentValue;
    Cursor moneyOutCursor;
    Date moneyOutDate, debtEndD, savingsDateD;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper6, dbHelper7;
    DbManager dbManager;
    DebtDb debt;
    Double moneyOutAmount = 0.0, currentAccountBalance = 0.0, newCurrentAccountBalance3 = 0.0, currentAvailableBalance = 0.0, newCurrentAvailableBalance3 = 0.0,
            moneyOutD = 0.0, oldMoneyOutAmount = 0.0, newMoneyOutAmount = 0.0, moneyOutAmountD = 0.0, currentDebtAmount = 0.0, currentSavingsAmount = 0.0,
            newDebtAmount = 0.0, newSavingsAmount = 0.0, currentDebtRate = 0.0, currentDebtPayments = 0.0, currentDebtFrequency = 0.0,
            numberOfYearsToPayDebt = 0.0, currentSavingsRate = 0.0, currentSavingsPayments = 0.0, currentSavingsFrequency = 0.0, numberOfYearsToSavingsGoal = 0.0;
    EditText moneyOutAmountText, moneyOutAmountEditText;
    General general;
    int moneyOutToPay = 0, moneyOutPaid = 0, numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    Intent backToDaily, backToDaily2, backToDaily3;
    ListView moneyOutList;
    long moneyOutRefKeyMO, expRefKeyMO, debtId, savingsId;
    MoneyOutAdapter moneyOutAdapter;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter moneyOutSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyOutSDF, debtEndS, savingsDateS;
    Spinner moneyOutCatSpinner;
    SQLiteDatabase db, db2, db3, db4, db5, db6, db7;
    String moneyOutCatS = null, moneyOutCat = null, moneyOutPriority = null, moneyOutWeekly = null, moneyOutPriorityS = null, moneyOutWeeklyS = null,
            moneyOutCreatedOn = null, moneyOutCC = null, moneyOutS = null, moneyOut2 = null, moneyOutAmountS = null, debtEnd = null, savingsDate = null;
    TextView moneyOutCatText;
    Timestamp moneyOutTimestamp;
    View v, moneyOutLine;

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

        moneyOutButton.setOnClickListener(onClickMoneyOutButton);

        moneyOutAdapter = new MoneyOutAdapter(getContext(), dbManager.getCashTrans());
        moneyOutList.setAdapter(moneyOutAdapter);

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

    public void updateCurrentAvailableBalanceMoneyOut() {
        newCurrentAvailableBalance3 = dbManager.retrieveCurrentAvailableBalance() - moneyOutAmount;

        possible = true;
        if (newCurrentAvailableBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {
            moneyOutValue2 = new ContentValues();
            moneyOutValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance3);
            dbHelper4 = new DbHelper(getContext());
            db4 = dbHelper4.getWritableDatabase();
            db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
            db4.close();
        }
    }

    public void updateCurrentAccountBalanceMoneyOut() {
        newCurrentAccountBalance3 = dbManager.retrieveCurrentAccountBalance() - moneyOutAmount;

        possible = true;
        if (newCurrentAccountBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {
            moneyOutValue = new ContentValues();
            moneyOutValue.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance3);
            dbHelper3 = new DbHelper(getContext());
            db3 = dbHelper3.getWritableDatabase();
            db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
            db3.close();
        }
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

    public String calcDebtDate() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == findMatchingDebtId()) {
                currentDebtAmount = d2.getDebtAmount();
                currentDebtRate = d2.getDebtRate();
                currentDebtPayments = d2.getDebtPayments();
                currentDebtFrequency = d2.getDebtFrequency();
            }

            debtCal = Calendar.getInstance();
            numberOfYearsToPayDebt = -(Math.log(1 - (currentDebtAmount * (currentDebtRate / 100) / (currentDebtPayments * currentDebtFrequency))) / (currentDebtFrequency * Math.log(1 + ((currentDebtRate / 100) / currentDebtFrequency))));
            numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

            if (currentDebtAmount <= 0) {
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

    public String calcSavingsDate() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == findMatchingSavingsId()) {
                currentSavingsAmount = s2.getSavingsAmount();
                currentSavingsRate = s2.getSavingsRate();
                currentSavingsPayments = s2.getSavingsPayments();
                currentSavingsFrequency = s2.getSavingsFrequency();
            }

            savingsCal = Calendar.getInstance();
            numberOfYearsToSavingsGoal = -(Math.log(1 - (currentSavingsAmount * (currentSavingsRate / 100) / (currentSavingsPayments * currentSavingsFrequency))) / (currentSavingsFrequency * Math.log(1 + ((currentSavingsRate / 100) / currentSavingsFrequency))));
            numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

            if (currentSavingsAmount <= 0) {
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

            dbHelper = new DbHelper(getContext());
            db = dbHelper.getWritableDatabase();

            moneyOutCat = moneyOutCatS;
            moneyOutPriority = moneyOutPriorityS;
            moneyOutWeekly = moneyOutWeeklyS;
            moneyOutAmount = Double.valueOf(moneyOutAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "N";
            moneyOutToPay = 0;
            moneyOutPaid = 0;
            expRefKeyMO = moneyOutRefKeyMO;

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, expRefKeyMO, 0);

            dbManager.addMoneyOut(moneyOutDb);

            findMatchingDebtId();
            if (foundMatchingDebtId) {
                calcDebtDate();
                newDebtAmount = currentDebtAmount - moneyOutAmount;
                moneyOutValue3 = new ContentValues();
                moneyOutValue3.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                moneyOutValue3.put(DbHelper.DEBTEND, calcDebtDate());
                db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue3, DbHelper.ID + "=" + findMatchingDebtId(), null);
            }
            findMatchingSavingsId();
            if (foundMatchingSavingsId) {
                calcSavingsDate();
                newSavingsAmount = currentSavingsAmount + moneyOutAmount;
                moneyOutValue4 = new ContentValues();
                moneyOutValue4.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                moneyOutValue4.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                db.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue4, DbHelper.ID + "=" + findMatchingSavingsId(), null);
            }
            db.close();

            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyOutAmountText.setText("");
            moneyOutCatSpinner.setSelection(0, false);

            moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
            moneyOutAdapter.notifyDataSetChanged();

            if (moneyOutPriority.equals("B")) {
                updateCurrentAvailableBalanceMoneyOut();

                if (possible) {
                    updateCurrentAccountBalanceMoneyOut();
                }
            } else if (moneyOutPriority.equals("A")) {
                updateCurrentAccountBalanceMoneyOut();
            }

            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily2);
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

                    dbHelper6 = new DbHelper(getContext());
                    db6 = dbHelper6.getWritableDatabase();

                    moneyOutDb = (MoneyOutDb) holder.moneyOutEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    moneyOutCatText.setVisibility(View.VISIBLE);
                    moneyOutAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    moneyOutLine.setVisibility(View.VISIBLE);

                    moneyOutCatText.setText(moneyOutDb.getMoneyOutCat());

                    moneyOutAmountD = moneyOutDb.getMoneyOutAmount();
                    moneyOutAmountS = currencyFormat.format(moneyOutAmountD);
                    moneyOutAmountEditText.setText(moneyOutAmountS);

                    oldMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);

                    updateMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                moneyOutDb.setMoneyOutAmount(Double.valueOf(moneyOutAmountEditText.getText().toString()));
                                newMoneyOutAmount = Double.valueOf(moneyOutAmountEditText.getText().toString());
                            } catch (NumberFormatException e) {
                                moneyOutDb.setMoneyOutAmount(general.extractingDollars(moneyOutAmountEditText));
                                newMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);
                            }

                            moneyOutAmount = newMoneyOutAmount - oldMoneyOutAmount;

                            dbManager.updateMoneyOut(moneyOutDb);

                            findMatchingDebtId();
                            if (foundMatchingDebtId) {
                                calcDebtDate();
                                newDebtAmount = currentDebtAmount - moneyOutAmount;
                                moneyOutValue5 = new ContentValues();
                                moneyOutValue5.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                                moneyOutValue5.put(DbHelper.DEBTEND, calcDebtDate());
                                db6.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue5, DbHelper.ID + "=" + findMatchingDebtId(), null);
                            }
                            findMatchingSavingsId();
                            if (foundMatchingSavingsId) {
                                calcSavingsDate();
                                newSavingsAmount = currentSavingsAmount + moneyOutAmount;
                                moneyOutValue6 = new ContentValues();
                                moneyOutValue6.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                                moneyOutValue6.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                                db6.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue6, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                            }
                            db6.close();

                            moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
                            notifyDataSetChanged();

                            Toast.makeText(getContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            moneyOutCatText.setVisibility(View.GONE);
                            moneyOutAmountEditText.setVisibility(View.GONE);
                            cancelMoneyOutEntryButton.setVisibility(View.GONE);
                            updateMoneyOutEntryButton.setVisibility(View.GONE);
                            moneyOutLine.setVisibility(View.GONE);

                            if (moneyOutDb.getMoneyOutPriority().equals("B")) {
                                updateCurrentAvailableBalanceMoneyOut();

                                if (possible) {
                                    updateCurrentAccountBalanceMoneyOut();
                                }
                            } else if (moneyOutDb.getMoneyOutPriority().equals("A")) {
                                updateCurrentAccountBalanceMoneyOut();
                            }

                            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily2);
                        }
                    });

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {
                            moneyOutCatText.setVisibility(View.GONE);
                            moneyOutAmountEditText.setVisibility(View.GONE);
                            cancelMoneyOutEntryButton.setVisibility(View.GONE);
                            updateMoneyOutEntryButton.setVisibility(View.GONE);
                            moneyOutLine.setVisibility(View.GONE);

                            backToDaily3 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily3);
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

                    moneyOutAmount = -(Double.valueOf(cashTrans.get(position).getMoneyOutAmount()));

                    moneyOutDb = (MoneyOutDb) holder.moneyOutDelete.getTag();
                    dbManager.deleteMoneyOut(moneyOutDb);

                    findMatchingDebtId();
                    if (foundMatchingDebtId) {
                        calcDebtDate();
                        newDebtAmount = currentDebtAmount - moneyOutAmount;
                        moneyOutValue7 = new ContentValues();
                        moneyOutValue7.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                        moneyOutValue7.put(DbHelper.DEBTEND, calcDebtDate());
                        db7.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue7, DbHelper.ID + "=" + findMatchingDebtId(), null);
                    }
                    findMatchingSavingsId();
                    if (foundMatchingSavingsId) {
                        calcSavingsDate();
                        newSavingsAmount = currentSavingsAmount + moneyOutAmount;
                        moneyOutValue8 = new ContentValues();
                        moneyOutValue8.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                        moneyOutValue8.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                        db7.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue8, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                    }
                    db7.close();

                    moneyOutAdapter.updateCashTrans(dbManager.getCashTrans());
                    notifyDataSetChanged();

                    if (moneyOutDb.getMoneyOutPriority().equals("B")) {
                        updateCurrentAvailableBalanceMoneyOut();
                        updateCurrentAccountBalanceMoneyOut();

                    } else if (moneyOutDb.getMoneyOutPriority().equals("A")) {
                        updateCurrentAccountBalanceMoneyOut();
                    }

                    backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
                    backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(backToDaily);
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
