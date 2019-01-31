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
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;

public class DailyMoneyIn extends Fragment {

    Boolean foundMatchingDebtId = false, foundMatchingSavingsId = false;
    Button cancelMoneyInEntryButton, moneyInButton, noMoneyInButton, updateMoneyInEntryButton, yesMoneyInButton;
    Calendar debtCal, savingsCal;
    ContentValues currentValue, moneyInValue, moneyInValue2, moneyInValue3, moneyInValue4, moneyInValue5, moneyInValue6, moneyInValue7, moneyInValue8, moneyInValue9,
            moneyInValue10;
    Cursor cursor2;
    Date debtEndD, moneyInDate, savingsDateD;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper6, dbHelper7, dbHelper8;
    DbManager dbManager;
    Double amountEntry = 0.0, currentDebtAmount = 0.0, currentDebtFrequency = 0.0, currentDebtPayments = 0.0, currentDebtRate = 0.0, currentSavingsAmount = 0.0,
            currentSavingsFrequency = 0.0, currentSavingsPayments = 0.0, currentSavingsRate = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, debtLimit = 0.0,
            moneyInAmount = 0.0, moneyInAmountD = 0.0, moneyInD = 0.0, newAccountBalance = 0.0, newAvailableBalance = 0.0, newDebtAmount = 0.0, newDebtAmount2 = 0.0,
            newSavingsAmount = 0.0, newSavingsAmount2 = 0.0, numberOfYearsToPayDebt = 0.0, oldMoneyInAmount = 0.0, percentB = 0.0, rate = 0.0, savingsAmount = 0.0,
            savingsAnnualIncome = 0.0, savingsGoal = 0.0, savingsIntFrequency = 0.0, years = 0.0;
    EditText moneyInAmountText, moneyInAmountEditText;
    General general;
    int numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    Intent backToDaily;
    LinearLayout updateMoneyInLayout;
    ListView moneyInList;
    long debtId, incRefKeyMI, moneyInRefKeyMI, savingsId;
    MoneyInAdapter moneyInAdapter;
    MoneyInDb moneyInDb;
    MoneyInSpinnerAdapter moneyInSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RelativeLayout addMoneyInLayout;
    SimpleDateFormat debtEndS, moneyInSDF, savingsDateS;
    Spinner moneyInCatSpinner;
    SQLiteDatabase db, db2, db3, db4, db5, db6, db7, db8;
    String debtEnd = null, moneyInAmountS = null, moneyInCatS = null, moneyInCat = null, moneyInCreatedOn = null, moneyIn2 = null, moneyInS = null, savingsDate = null;
    TextView debtContinueAnywayText, debtNotPossibleText, moneyInCatText, savingsNotPossibleText;
    Timestamp moneyInTimestamp;
    View moneyInLine, moneyInLine2, v;

    public DailyMoneyIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_in, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();
        dbManager = new DbManager(getContext());

        debtNotPossibleText = v.findViewById(R.id.debtNotPossibleText);
        debtNotPossibleText.setVisibility(View.GONE);
        savingsNotPossibleText = v.findViewById(R.id.savingsNotPossibleText);
        savingsNotPossibleText.setVisibility(View.GONE);
        debtContinueAnywayText = v.findViewById(R.id.debtContinueAnywayText);
        debtContinueAnywayText.setVisibility(View.GONE);
        noMoneyInButton = v.findViewById(R.id.noMoneyInButton);
        noMoneyInButton.setVisibility(View.GONE);
        yesMoneyInButton = v.findViewById(R.id.yesMoneyInButton);
        yesMoneyInButton.setVisibility(View.GONE);
        updateMoneyInLayout = v.findViewById(R.id.updateMoneyInLayout);
        updateMoneyInLayout.setVisibility(View.GONE);
        addMoneyInLayout = v.findViewById(R.id.addMoneyInLayout);
        moneyInAmountText = v.findViewById(R.id.moneyInAmount);
        moneyInButton = v.findViewById(R.id.moneyInButton);
        moneyInList = v.findViewById(R.id.moneyInList);
        moneyInCatText = v.findViewById(R.id.moneyInCatText);
        moneyInCatText.setVisibility(View.GONE);
        moneyInAmountEditText = v.findViewById(R.id.moneyInAmountEditText);
        moneyInAmountEditText.setVisibility(View.GONE);
        cancelMoneyInEntryButton = v.findViewById(R.id.cancelMoneyInEntryButton);
        cancelMoneyInEntryButton.setVisibility(View.GONE);
        updateMoneyInEntryButton = v.findViewById(R.id.updateMoneyInEntryButton);
        updateMoneyInEntryButton.setVisibility(View.GONE);
        moneyInLine = v.findViewById(R.id.moneyInLine);
        moneyInLine.setVisibility(View.GONE);
        moneyInLine2 = v.findViewById(R.id.moneyInLine2);

        moneyInButton.setOnClickListener(onClickMoneyInButton);

        moneyInAdapter = new MoneyInAdapter(getContext(), dbManager.getMoneyIns());
        moneyInList.setAdapter(moneyInAdapter);

        moneyInCatSpinner = v.findViewById(R.id.moneyInCatSpinner);
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getReadableDatabase();
        cursor2 = db2.rawQuery("SELECT * FROM " + DbHelper.INCOME_TABLE_NAME + " ORDER BY " + DbHelper.INCOMENAME + " ASC", null);
        moneyInSpinnerAdapter = new MoneyInSpinnerAdapter(getContext(), cursor2);
        moneyInCatSpinner.setAdapter(moneyInSpinnerAdapter);

        moneyInCatSpinner.setOnItemSelectedListener(moneyInSpinnerSelection);

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 1);
        dbHelper5 = new DbHelper(getContext());
        db5 = dbHelper5.getWritableDatabase();
        db5.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db5.close();

    }

    public void backToDaily() {
        backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
        backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDaily);
    }

    public long findMatchingDebtId() {
        foundMatchingDebtId = false;
        for (DebtDb d : dbManager.getDebts()) {
            try {
                if (d.getIncRefKeyD() == moneyInRefKeyMI) {
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
            if (d3.getIncRefKeyD() == moneyInRefKeyMI) {
                currentDebtAmount = d3.getDebtAmount();
                debtLimit = d3.getDebtLimit();
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
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }

            debtCal = Calendar.getInstance();
            numberOfYearsToPayDebt = -(Math.log(1 - (debtAmount * (currentDebtRate / 100) / ((currentDebtPayments * currentDebtFrequency) - debtAnnualIncome))) / (currentDebtFrequency * Math.log(1 + ((currentDebtRate / 100) / currentDebtFrequency))));
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

    public void updateDebtsRecord() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        newDebtAmount = findCurrentDebtAmount() + moneyInAmount;
        moneyInValue3 = new ContentValues();
        moneyInValue3.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue3, DbHelper.ID + "=" + findMatchingDebtId(), null);
        moneyInValue4 = new ContentValues();
        moneyInValue4.put(DbHelper.DEBTEND, calcDebtDate());
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue4, DbHelper.ID + "=" + findMatchingDebtId(), null);
    }

    public long findMatchingSavingsId() {
        foundMatchingSavingsId = false;
        for (SavingsDb s : dbManager.getSavings()) {
            try {
                if (s.getIncRefKeyS() == moneyInRefKeyMI) {
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
            if (s3.getIncRefKeyS() == moneyInRefKeyMI) {
                currentSavingsAmount = s3.getSavingsAmount();
            }
        }
        return currentSavingsAmount;
    }

    public Double findSavingsYears() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == findMatchingSavingsId()) {
                savingsAmount = s2.getSavingsAmount();
                savingsGoal = s2.getSavingsGoal();
                currentSavingsRate = s2.getSavingsRate();
                currentSavingsPayments = s2.getSavingsPayments();
                currentSavingsFrequency = s2.getSavingsFrequency();
                savingsIntFrequency = s2.getSavingsIntFrequency();
                savingsAnnualIncome = s2.getSavingsAnnualIncome();
            }
        }
        if (savingsGoal < savingsAmount) {
            savingsGoal = savingsAmount;
        }
        rate = currentSavingsRate / 100;
        if (rate == 0) {
            rate = .01;
        }
        if (currentSavingsPayments == 0) {
            currentSavingsPayments = 0.01;
        }
        if (savingsAmount == 0 && currentSavingsPayments == 0.01) {
            years = 0.0;
        } else if (savingsGoal.equals(savingsAmount)) {
            years = 0.0;
        } else {
            years = 0.0;
            do {
                years = years + .00274;
            }
            while (savingsGoal >= (savingsAmount * (Math.pow((1 + rate / savingsIntFrequency), savingsIntFrequency * years))) + ((((currentSavingsPayments * currentSavingsFrequency) - savingsAnnualIncome) / 12) * (((Math.pow((1 + rate / savingsIntFrequency), savingsIntFrequency * years)) - 1) / (rate / savingsIntFrequency)) * (1 + rate / savingsIntFrequency)));
        }

        return years;
    }

    public String calcSavingsDate() {

        savingsCal = Calendar.getInstance();
        numberOfDaysToSavingsGoal = (int) Math.round(findSavingsYears() * 365);

        if ((numberOfDaysToSavingsGoal) <= 0) {
            savingsDate = getString(R.string.goal_achieved);

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE) {
            savingsDate = getString(R.string.too_far);

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = getString(R.string.goal_will) + " " + savingsDateS.format(savingsDateD);
        }
        return savingsDate;
    }

    public void updateSavingsRecord() {
        dbHelper6 = new DbHelper(getContext());
        db6 = dbHelper6.getWritableDatabase();
        newSavingsAmount = findCurrentSavingsAmount() - moneyInAmount;
        moneyInValue5 = new ContentValues();
        moneyInValue5.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
        db6.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue5, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        moneyInValue6 = new ContentValues();
        moneyInValue6.put(DbHelper.SAVINGSDATE, calcSavingsDate());
        db6.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue6, DbHelper.ID + "=" + findMatchingSavingsId(), null);
    }


    public void updateCurrentAvailableBalanceMoneyIn() {
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        percentB = dbManager.retrieveBPercentage();
        if (dbManager.retrieveCurrentAccountBalance() < moneyInAmount) {
            newAvailableBalance = dbManager.retrieveCurrentAvailableBalance() + (dbManager.retrieveCurrentAccountBalance() * percentB);
        } else {
            newAvailableBalance = dbManager.retrieveCurrentAvailableBalance() + (moneyInAmount * percentB);
        }

        moneyInValue2 = new ContentValues();
        moneyInValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newAvailableBalance);
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue2, DbHelper.ID + "= '1'", null);
        db3.close();
    }

    public void updateCurrentAccountBalanceMoneyIn() {
        newAccountBalance = dbManager.retrieveCurrentAccountBalance() + moneyInAmount;
        moneyInValue = new ContentValues();
        moneyInValue.put(DbHelper.CURRENTACCOUNTBALANCE, newAccountBalance);
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue, DbHelper.ID + "= '1'", null);
        db4.close();

    }

    AdapterView.OnItemSelectedListener moneyInSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyInCatS = cursor2.getString(cursor2.getColumnIndexOrThrow(DbHelper.INCOMENAME));
            moneyInRefKeyMI = cursor2.getLong(cursor2.getColumnIndexOrThrow(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickMoneyInButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyInCat = moneyInCatS;
            if(moneyInAmountText.getText().toString().equals("")) {
                moneyInAmount = 0.0;
            }
            moneyInAmount = Double.valueOf(moneyInAmountText.getText().toString());
            moneyInDate = new Date();
            moneyInTimestamp = new Timestamp(moneyInDate.getTime());
            moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);
            incRefKeyMI = moneyInRefKeyMI;

            findMatchingDebtId();
            if (foundMatchingDebtId) {
                if (findCurrentDebtAmount() + moneyInAmount > debtLimit) {
                    debtNotPossibleText.setVisibility(View.VISIBLE);
                    debtContinueAnywayText.setVisibility(View.VISIBLE);
                    noMoneyInButton.setVisibility(View.VISIBLE);
                    yesMoneyInButton.setVisibility(View.VISIBLE);

                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            debtNotPossibleText.setVisibility(View.GONE);
                            savingsNotPossibleText.setVisibility(View.GONE);
                            debtContinueAnywayText.setVisibility(View.GONE);
                            noMoneyInButton.setVisibility(View.GONE);
                            yesMoneyInButton.setVisibility(View.GONE);

                            backToDaily();
                        }
                    });
                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDebtsRecord();
                        }
                    });
                } else {
                    updateDebtsRecord();
                }
            } else {
                debtNotPossibleText.setVisibility(View.GONE);
                savingsNotPossibleText.setVisibility(View.GONE);
                debtContinueAnywayText.setVisibility(View.GONE);
                noMoneyInButton.setVisibility(View.GONE);
                yesMoneyInButton.setVisibility(View.GONE);
            }

            findMatchingSavingsId();
            if (foundMatchingSavingsId) {
                if (findCurrentSavingsAmount() - moneyInAmount <= 0) {
                    savingsNotPossibleText.setVisibility(View.VISIBLE);
                    debtContinueAnywayText.setVisibility(View.VISIBLE);
                    noMoneyInButton.setVisibility(View.VISIBLE);
                    yesMoneyInButton.setVisibility(View.VISIBLE);

                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            debtNotPossibleText.setVisibility(View.GONE);
                            savingsNotPossibleText.setVisibility(View.GONE);
                            debtContinueAnywayText.setVisibility(View.GONE);
                            noMoneyInButton.setVisibility(View.GONE);
                            yesMoneyInButton.setVisibility(View.GONE);

                            backToDaily();
                        }
                    });
                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateSavingsRecord();
                        }
                    });
                } else {
                    updateSavingsRecord();
                }
            } else {
                debtNotPossibleText.setVisibility(View.GONE);
                savingsNotPossibleText.setVisibility(View.GONE);
                debtContinueAnywayText.setVisibility(View.GONE);
                noMoneyInButton.setVisibility(View.GONE);
                yesMoneyInButton.setVisibility(View.GONE);
            }

            moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInCreatedOn, incRefKeyMI, 0);

            dbManager.addMoneyIn(moneyInDb);
            Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_LONG).show();
            moneyInAmountText.setText("");
            moneyInCatSpinner.setSelection(0, false);

            moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
            moneyInAdapter.notifyDataSetChanged();

            updateCurrentAccountBalanceMoneyIn();
            updateCurrentAvailableBalanceMoneyIn();

            backToDaily();
        }
    };

    public class MoneyInAdapter extends ArrayAdapter<MoneyInDb> {

        private Context context;
        private List<MoneyInDb> moneyIn;

        private MoneyInAdapter(
                Context context,
                List<MoneyInDb> moneyIn) {

            super(context, -1, moneyIn);

            this.context = context;
            this.moneyIn = moneyIn;
        }

        public void updateMoneyIn(List<MoneyInDb> moneyIn) {
            this.moneyIn = moneyIn;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIn.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new MoneyInViewHolder();
                holder.moneyInEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.moneyInDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.moneyInAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.moneyInCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.moneyInDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (MoneyInViewHolder) convertView.getTag();
            }

            //retrieve moneyInCreatedOn
            holder.moneyInDate.setText(moneyIn.get(position).getMoneyInCreatedOn());

            //retrieve moneyInCat
            holder.moneyInCat.setText(moneyIn.get(position).getMoneyInCat());

            //moneyInAmount and format as currency
            try {
                moneyInS = (String.valueOf(moneyIn.get(position).getMoneyInAmount()));
                if (moneyInS != null && !moneyInS.equals("")) {
                    moneyInD = Double.valueOf(moneyInS);
                } else {
                    moneyInD = 0.0;
                }
                moneyIn2 = currencyFormat.format(moneyInD);
                holder.moneyInAmount.setText(moneyIn2);
            } catch (NumberFormatException e) {
                holder.moneyInAmount.setText(moneyIn2);
            }

            holder.moneyInDelete.setTag(moneyIn.get(position));
            holder.moneyInEdit.setTag(moneyIn.get(position));

            //click on pencil icon
            holder.moneyInEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInDb = (MoneyInDb) holder.moneyInEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    updateMoneyInLayout.setVisibility(View.VISIBLE);
                    addMoneyInLayout.setVisibility(View.GONE);
                    moneyInAmountText.setVisibility(View.GONE);
                    moneyInCatSpinner.setVisibility(View.GONE);
                    moneyInButton.setVisibility(View.GONE);
                    moneyInLine2.setVisibility(View.GONE);
                    moneyInCatText.setVisibility(View.VISIBLE);
                    moneyInAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyInEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyInEntryButton.setVisibility(View.VISIBLE);
                    moneyInLine.setVisibility(View.VISIBLE);

                    moneyInCatText.setText(moneyInDb.getMoneyInCat());

                    moneyInAmountD = moneyInDb.getMoneyInAmount();
                    moneyInAmountS = currencyFormat.format(moneyInAmountD);
                    moneyInAmountEditText.setText(moneyInAmountS);

                    oldMoneyInAmount = general.extractingDollars(moneyInAmountEditText);

                    updateMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                amountEntry = Double.valueOf(moneyInAmountEditText.getText().toString());
                            } catch (NumberFormatException e) {
                                amountEntry = general.extractingDollars(moneyInAmountEditText);
                            }
                            if(moneyInAmountEditText.getText().toString().equals("")) {
                                amountEntry = 0.0;
                            }

                            moneyInAmount = amountEntry - oldMoneyInAmount;

                            findMatchingDebtId();
                            if (foundMatchingDebtId) {
                                if (findCurrentDebtAmount() + moneyInAmount > debtLimit) {
                                    debtNotPossibleText.setVisibility(View.VISIBLE);
                                    debtContinueAnywayText.setVisibility(View.VISIBLE);
                                    noMoneyInButton.setVisibility(View.VISIBLE);
                                    yesMoneyInButton.setVisibility(View.VISIBLE);

                                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            debtNotPossibleText.setVisibility(View.GONE);
                                            savingsNotPossibleText.setVisibility(View.GONE);
                                            debtContinueAnywayText.setVisibility(View.GONE);
                                            noMoneyInButton.setVisibility(View.GONE);
                                            yesMoneyInButton.setVisibility(View.GONE);

                                            backToDaily();
                                        }
                                    });
                                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateDebtsRecord();
                                        }
                                    });
                                } else {
                                    updateDebtsRecord();
                                }
                            } else {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                debtContinueAnywayText.setVisibility(View.GONE);
                                noMoneyInButton.setVisibility(View.GONE);
                                yesMoneyInButton.setVisibility(View.GONE);
                            }

                            findMatchingSavingsId();
                            if (foundMatchingSavingsId) {
                                if (findCurrentSavingsAmount() - moneyInAmount <= 0) {
                                    savingsNotPossibleText.setVisibility(View.VISIBLE);
                                    debtContinueAnywayText.setVisibility(View.VISIBLE);
                                    noMoneyInButton.setVisibility(View.VISIBLE);
                                    yesMoneyInButton.setVisibility(View.VISIBLE);

                                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            debtNotPossibleText.setVisibility(View.GONE);
                                            savingsNotPossibleText.setVisibility(View.GONE);
                                            debtContinueAnywayText.setVisibility(View.GONE);
                                            noMoneyInButton.setVisibility(View.GONE);
                                            yesMoneyInButton.setVisibility(View.GONE);

                                            backToDaily();
                                        }
                                    });
                                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateSavingsRecord();
                                        }
                                    });
                                } else {
                                    updateSavingsRecord();
                                }
                            } else {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                debtContinueAnywayText.setVisibility(View.GONE);
                                noMoneyInButton.setVisibility(View.GONE);
                                yesMoneyInButton.setVisibility(View.GONE);
                            }

                            moneyInDb.setMoneyInAmount(amountEntry);
                            dbManager.updateMoneyIn(moneyInDb);
                            moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
                            notifyDataSetChanged();

                            Toast.makeText(getContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                            updateMoneyInLayout.setVisibility(View.GONE);
                            addMoneyInLayout.setVisibility(View.GONE);
                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            updateCurrentAccountBalanceMoneyIn();
                            updateCurrentAvailableBalanceMoneyIn();

                            backToDaily();
                        }
                    });

                    cancelMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addMoneyInLayout.setVisibility(View.GONE);
                            updateMoneyInLayout.setVisibility(View.GONE);
                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            backToDaily();
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyInDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInDb = (MoneyInDb) holder.moneyInDelete.getTag();
                    moneyInAmount = -(moneyIn.get(position).getMoneyInAmount());

                    if (moneyInDb.getId() == 1) {
                        Toast.makeText(getContext(), R.string.cannot_delete_warning, Toast.LENGTH_LONG).show();
                    } else {
                        findMatchingDebtId();
                        if (foundMatchingDebtId) {
                            dbHelper7 = new DbHelper(getContext());
                            db7 = dbHelper7.getWritableDatabase();
                            newDebtAmount2 = findCurrentDebtAmount() + moneyInAmount;
                            moneyInValue7 = new ContentValues();
                            moneyInValue7.put(DbHelper.DEBTAMOUNT, newDebtAmount2);
                            db7.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue7, DbHelper.ID + "=" + findMatchingDebtId(), null);
                            moneyInValue8 = new ContentValues();
                            moneyInValue8.put(DbHelper.DEBTEND, calcDebtDate());
                            db7.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue8, DbHelper.ID + "=" + findMatchingDebtId(), null);
                        }

                        findMatchingSavingsId();
                        if (foundMatchingSavingsId) {
                            dbHelper8 = new DbHelper(getContext());
                            db8 = dbHelper8.getWritableDatabase();
                            newSavingsAmount2 = findCurrentSavingsAmount() - moneyInAmount;
                            moneyInValue9 = new ContentValues();
                            moneyInValue9.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount2);
                            db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue9, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                            moneyInValue10 = new ContentValues();
                            moneyInValue10.put(DbHelper.SAVINGSDATE, calcSavingsDate());
                            db8.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue10, DbHelper.ID + "=" + findMatchingSavingsId(), null);
                        }

                        dbManager.deleteMoneyIn(moneyInDb);
                        moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
                        notifyDataSetChanged();

                        updateCurrentAccountBalanceMoneyIn();
                        updateCurrentAvailableBalanceMoneyIn();

                        backToDaily();
                    }
                }
            });
            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView moneyInDate;
        public TextView moneyInCat;
        public TextView moneyInAmount;
        ImageButton moneyInEdit;
        ImageButton moneyInDelete;
    }
}
