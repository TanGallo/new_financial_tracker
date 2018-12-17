package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
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
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyCC extends Fragment {

    boolean paymentAPossible = true, paymentBPossible = true, possible = true, foundMatchingDebtId = false, foundMatchingSavingsId = false;
    Button ccTransButton, cancelCCTransEntryButton, updateCCTransEntryButton, ccPaymentNotPossibleContinueButton;
    Calendar debtCal, savingsCal;
    CCTransAdapter ccTransAdapter;
    ContentValues currentValue, moneyOutValue, moneyOutValue2, moneyOutValue3, moneyOutValue4, moneyOutValue5, moneyOutValue6, moneyOutValue7, moneyOutValue8,
            moneyOutValue9, moneyOutValue10, moneyOutValue11, moneyOutValue12;
    Cursor cursor2;
    Date moneyOutDate, debtEndD, savingsDateD;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5;
    DbManager dbManager;
    Double moneyOutAmount = 0.0, ccTransAmountD = 0.0, oldMoneyOutAmount = 0.0, newMoneyOutAmount = 0.0, ccTransAmountD2 = 0.0, currentDebtAmount = 0.0,
            debtAmount = 0.0, currentDebtRate = 0.0, currentDebtPayments = 0.0, currentDebtFrequency = 0.0, numberOfYearsToPayDebt = 0.0,
            currentSavingsAmount = 0.0, savingsAmount = 0.0, currentSavingsRate = 0.0, currentSavingsPayments = 0.0, currentSavingsFrequency = 0.0,
            numberOfYearsToSavingsGoal = 0.0, newDebtAmount = 0.0, newSavingsAmount = 0.0;
    EditText ccTransAmountText, ccTransAmountEditText;
    General general;
    int moneyOutToPay = 0, moneyOutPaid = 0, numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    ListView ccTransList;
    long moneyOutRefKeyMO, expRefKeyMO, debtId, savingsId;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter ccTransSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyOutSDF, debtEndS, savingsDateS;
    Spinner ccTransCatSpinner;
    SQLiteDatabase db, db2, db3, db4, db5;
    String moneyOutCat = null, moneyOutPriority = null, moneyOutWeekly = null, moneyOutCreatedOn = null, moneyOutCC = null, ccTransCatS = null,
            ccTransPriorityS = null, moneyOutWeeklyS = null, ccTransAmountS = null, ccTransAmount2 = null, ccTransAmountS2 = null, debtEnd = null, savingsDate = null;
    TextView ccTransCatText, ccPaymentNotPossibleAText, ccPaymentNotPossibleBText, ccContinueAnywayText, ccContinueWarningText;
    Timestamp moneyOutTimestamp;
    View v, ccTransLine;

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

        ccPaymentNotPossibleAText = v.findViewById(R.id.ccPaymentNotPossibleAText);
        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText = v.findViewById(R.id.ccPaymentNotPossibleBText);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccContinueAnywayText = v.findViewById(R.id.ccContinueAnywayText);
        ccContinueAnywayText.setVisibility(View.GONE);
        ccContinueWarningText = v.findViewById(R.id.ccContinueWarningText);
        ccContinueWarningText.setVisibility(View.GONE);
        ccPaymentNotPossibleContinueButton = v.findViewById(R.id.ccPaymentNotPossibleContinueButton);
        ccPaymentNotPossibleContinueButton.setVisibility(View.GONE);
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
        ccTransLine = v.findViewById(R.id.ccTransLine);
        ccTransLine.setVisibility(View.GONE);

        ccTransButton.setOnClickListener(onClickCCTransButton);

        dbManager = new DbManager(getContext());
        ccTransAdapter = new CCTransAdapter(getContext(), dbManager.getCCTrans());
        ccTransList.setAdapter(ccTransAdapter);

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getReadableDatabase();
        cursor2 = db2.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSENAME + " ASC", null);
        ccTransSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), cursor2);
        ccTransCatSpinner.setAdapter(ccTransSpinnerAdapter);

        ccTransCatSpinner.setOnItemSelectedListener(ccTransSpinnerSelection);

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 3);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db3.close();

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

    public void cancelTransaction() {
        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccPaymentNotPossibleContinueButton.setVisibility(View.GONE);
        ccContinueAnywayText.setVisibility(View.GONE);
        ccContinueWarningText.setVisibility(View.GONE);

        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton.setVisibility(View.GONE);
        ccTransLine.setVisibility(View.GONE);
    }

    public void continueTransaction() {
        moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutCreatedOn,
                moneyOutCC, moneyOutToPay, moneyOutPaid, expRefKeyMO,  0);

        dbManager.addMoneyOut(moneyOutDb);

        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
            moneyOutValue = new ContentValues();
            moneyOutValue.put(DbHelper.DEBTAMOUNT, newDebtAmount);
            db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue, DbHelper.ID + "=" + findMatchingDebtId(), null);
            moneyOutValue2 = new ContentValues();
            moneyOutValue2.put(DbHelper.DEBTEND, calcDebtDate());
            db.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue2, DbHelper.ID + "=" + findMatchingDebtId(), null);
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
            moneyOutValue3 = new ContentValues();
            moneyOutValue3.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
            db.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue3, DbHelper.ID + "=" + findMatchingSavingsId(), null);
            moneyOutValue4 = new ContentValues();
            moneyOutValue4.put(DbHelper.SAVINGSDATE, calcSavingsDate());
            db.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue4, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        }
        db.close();

        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
        ccTransAmountText.setText("");
        ccTransCatSpinner.setSelection(0, false);

        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccPaymentNotPossibleContinueButton.setVisibility(View.GONE);
        ccContinueAnywayText.setVisibility(View.GONE);
        ccContinueWarningText.setVisibility(View.GONE);

        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton.setVisibility(View.GONE);
        ccTransLine.setVisibility(View.GONE);
    }

    public void continueUpdate() {
        dbManager.updateMoneyOut(moneyOutDb);

        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();

        findMatchingDebtId();
        if (foundMatchingDebtId) {
            newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
            moneyOutValue5 = new ContentValues();
            moneyOutValue5.put(DbHelper.DEBTAMOUNT, newDebtAmount);
            db4.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue5, DbHelper.ID + "=" + findMatchingDebtId(), null);
            moneyOutValue6 = new ContentValues();
            moneyOutValue6.put(DbHelper.DEBTEND, calcDebtDate());
            db4.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue6, DbHelper.ID + "=" + findMatchingDebtId(), null);
        }
        findMatchingSavingsId();
        if (foundMatchingSavingsId) {
            newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
            moneyOutValue7 = new ContentValues();
            moneyOutValue7.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
            db4.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue7, DbHelper.ID + "=" + findMatchingSavingsId(), null);
            moneyOutValue8 = new ContentValues();
            moneyOutValue8.put(DbHelper.SAVINGSDATE, calcSavingsDate());
            db4.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue8, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        }
        db4.close();

        Toast.makeText(getContext(), "Your changes have been saved",
                Toast.LENGTH_LONG).show();

        ccPaymentNotPossibleAText.setVisibility(View.GONE);
        ccPaymentNotPossibleBText.setVisibility(View.GONE);
        ccPaymentNotPossibleContinueButton.setVisibility(View.GONE);
        ccContinueAnywayText.setVisibility(View.GONE);
        ccContinueWarningText.setVisibility(View.GONE);

        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton.setVisibility(View.GONE);
        ccTransLine.setVisibility(View.GONE);
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

    public Double findCurrentDebtAmount() {
        for(DebtDb d3 : dbManager.getDebts()) {
            if(d3.getExpRefKeyD() == moneyOutRefKeyMO) {
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
        for(SavingsDb s3 : dbManager.getSavings()) {
            if(s3.getExpRefKeyS() == moneyOutRefKeyMO) {
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

    View.OnClickListener onClickCCTransButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = ccTransCatS;
            moneyOutPriority = ccTransPriorityS;
            moneyOutWeekly = moneyOutWeeklyS;
            moneyOutAmount = Double.valueOf(ccTransAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "Y";
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
                    continueTransaction();
                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    ccTransAdapter.notifyDataSetChanged();
                } else if (!paymentAPossible) {
                    ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                    ccContinueWarningText.setVisibility(View.VISIBLE);
                    ccContinueAnywayText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            continueTransaction();
                            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                            ccTransAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else if (moneyOutPriority.equals("B")) {
                if (paymentBPossible && paymentAPossible) {
                    continueTransaction();
                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    ccTransAdapter.notifyDataSetChanged();
                } else if (!paymentBPossible && paymentAPossible) {
                    ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                    ccContinueWarningText.setVisibility(View.VISIBLE);
                    ccContinueAnywayText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            continueTransaction();
                            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                            ccTransAdapter.notifyDataSetChanged();
                        }
                    });
                } else if (!paymentBPossible && !paymentAPossible) {
                    ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                    ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                    ccContinueWarningText.setVisibility(View.VISIBLE);
                    ccContinueAnywayText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                    cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelTransaction();
                        }
                    });

                    ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            continueTransaction();
                            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                            ccTransAdapter.notifyDataSetChanged();
                        }
                    });
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
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new CCTransViewHolder();
                holder.ccTransEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.ccTransDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.ccTransAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.ccTransCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.ccTransDate = convertView.findViewById(R.id.moneyInOutDate);
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

            holder.ccTransDelete.setTag(ccTrans.get(position));
            holder.ccTransEdit.setTag(ccTrans.get(position));

            //click on pencil icon
            holder.ccTransEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccTransEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    ccTransCatText.setVisibility(View.VISIBLE);
                    ccTransAmountEditText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    updateCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccTransLine.setVisibility(View.VISIBLE);

                    ccTransCatText.setText(moneyOutDb.getMoneyOutCat());

                    ccTransAmountD2 = moneyOutDb.getMoneyOutAmount();
                    ccTransAmountS2 = currencyFormat.format(ccTransAmountD2);
                    ccTransAmountEditText.setText(ccTransAmountS2);

                    oldMoneyOutAmount = general.extractingDollars(ccTransAmountEditText);
                    moneyOutRefKeyMO = moneyOutDb.getExpRefKeyMO();
                    moneyOutPriority = moneyOutDb.getMoneyOutPriority();
                }
            });

            updateCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        moneyOutDb.setMoneyOutAmount(Double.valueOf(ccTransAmountEditText.getText().toString()));
                        newMoneyOutAmount = Double.valueOf(ccTransAmountEditText.getText().toString());
                    } catch(NumberFormatException e) {
                        moneyOutDb.setMoneyOutAmount(general.extractingDollars(ccTransAmountEditText));
                        newMoneyOutAmount = general.extractingDollars(ccTransAmountEditText);
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

                    if (moneyOutPriority.equals("A")) {
                        if (paymentAPossible) {
                            continueUpdate();
                            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                            notifyDataSetChanged();
                        } else if (!paymentAPossible) {
                            ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                            ccContinueWarningText.setVisibility(View.VISIBLE);
                            ccContinueAnywayText.setVisibility(View.VISIBLE);
                            cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                            ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelTransaction();
                                }
                            });

                            ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    continueUpdate();
                                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                                    ccTransAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else if (moneyOutPriority.equals("B")) {
                        if (paymentBPossible && paymentAPossible) {
                            continueUpdate();
                            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                            ccTransAdapter.notifyDataSetChanged();
                        } else if (!paymentBPossible && paymentAPossible) {
                            ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                            ccContinueWarningText.setVisibility(View.VISIBLE);
                            ccContinueAnywayText.setVisibility(View.VISIBLE);
                            cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                            ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelTransaction();
                                }
                            });

                            ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    continueUpdate();
                                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                                    ccTransAdapter.notifyDataSetChanged();
                                }
                            });
                        } else if (!paymentBPossible && !paymentAPossible) {
                            ccPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                            ccPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                            ccContinueWarningText.setVisibility(View.VISIBLE);
                            ccContinueAnywayText.setVisibility(View.VISIBLE);
                            cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                            ccPaymentNotPossibleContinueButton.setVisibility(View.VISIBLE);

                            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelTransaction();
                                }
                            });

                            ccPaymentNotPossibleContinueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    continueUpdate();
                                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                                    ccTransAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            });

            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelTransaction();
                }
            });

            //click on trash can icon
            holder.ccTransDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dbHelper5 = new DbHelper(getContext());
                    db5 = dbHelper5.getWritableDatabase();

                    moneyOutDb = (MoneyOutDb) holder.ccTransDelete.getTag();

                    moneyOutAmount = -(ccTrans.get(position).getMoneyOutAmount());
                    moneyOutRefKeyMO = ccTrans.get(position).getExpRefKeyMO();

                    dbManager.deleteMoneyOut(moneyOutDb);

                    findMatchingDebtId();
                    if (foundMatchingDebtId) {
                        newDebtAmount = findCurrentDebtAmount() - moneyOutAmount;
                        moneyOutValue9 = new ContentValues();
                        moneyOutValue9.put(DbHelper.DEBTAMOUNT, newDebtAmount);
                        db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue9, DbHelper.ID + "=" + findMatchingDebtId(), null);
                        moneyOutValue10 = new ContentValues();
                        moneyOutValue10.put(DbHelper.DEBTEND, calcDebtDate());
                        db5.update(DbHelper.DEBTS_TABLE_NAME, moneyOutValue10, DbHelper.ID + "=" + findMatchingDebtId(), null);
                    }
                    findMatchingSavingsId();
                    if (foundMatchingSavingsId) {
                        newSavingsAmount = findCurrentSavingsAmount() + moneyOutAmount;
                        moneyOutValue11 = new ContentValues();
                        moneyOutValue11.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
                        db5.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue11, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                        moneyOutValue12 = new ContentValues();
                        moneyOutValue12.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                        db5.update(DbHelper.SAVINGS_TABLE_NAME, moneyOutValue12, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                    }
                    db5.close();

                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class CCTransViewHolder {
        public TextView ccTransCat;
        public TextView ccTransAmount;
        public TextView ccTransDate;
        ImageButton ccTransDelete;
        ImageButton ccTransEdit;
    }
}
