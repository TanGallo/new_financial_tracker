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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    Boolean foundMatchingDebtId = false, foundMatchingSavingsId = false, newTransaction = true;
    Button cancelMoneyInEntryButton, moneyInButton, noMoneyInButton, updateMoneyInEntryButton, yesMoneyInButton;
    ContentValues currentValue, moneyInValue, moneyInValue2, moneyInValue3, moneyInValue4, moneyInValue5, moneyInValue6, moneyInValue7;
    Cursor cursor2;
    Date moneyInDate;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper6, dbHelper7;
    DbManager dbManager;
    Double newOwingA = 0.0, amountEntry = 0.0, moneyInD = 0.0, debtFrequency = 0.0, debtPayments = 0.0, debtRate = 0.0, savingsFrequency = 0.0,
            savingsPayments = 0.0, savingsRate = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, debtLimit = 0.0, moneyInAmount = 0.0,
            moneyInAmountD = 0.0, amountForB = 0.0, newAccountBalance = 0.0, newABalance = 0.0, newDebtAmount = 0.0, newSavingsAmount = 0.0,
            moneyInA2 = 0.0, oldMoneyInAmount = 0.0, amountForA = 0.0, amountToZero = 0.0, newBBalance = 0.0, savingsAmount = 0.0, savingsAnnualIncome = 0.0,
            savingsGoal = 0.0, moneyInOwing2 = 0.0, moneyInB2 = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0, moneyInAmountN = 0.0;
    EditText moneyInAmountText, moneyInAmountEditText;
    General general;
    Intent backToDaily;
    LinearLayout updateMoneyInLayout;
    ListView moneyInList;
    long debtId, incRefKeyMI, moneyInRefKeyMI, savingsId;
    MoneyInAdapter moneyInAdapter;
    MoneyInDb moneyInDb;
    MoneyInSpinnerAdapter moneyInSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RelativeLayout addMoneyInLayout;
    SimpleDateFormat moneyInSDF;
    Spinner moneyInCatSpinner;
    SQLiteDatabase db, db2, db3, db4, db5, db6, db7;
    String moneyInAmountS = null, moneyInCatS = null, moneyInCat = null, moneyInCreatedOn = null, moneyIn2 = null, moneyInS = null;
    TextView debtContinueAnywayText, debtNotPossibleText, moneyInCatText, savingsNotPossibleText;
    Timestamp moneyInTimestamp;
    View v;

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

    public void allDebtData() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == findMatchingDebtId()) {
                debtAmount = d2.getDebtAmount();
                debtLimit = d2.getDebtLimit();
                debtRate = d2.getDebtRate();
                debtPayments = d2.getDebtPayments();
                debtFrequency = d2.getDebtFrequency();
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }
        }
    }

    public void updateDebtsRecord() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        newDebtAmount = debtAmount + moneyInAmount;
        moneyInValue3 = new ContentValues();
        moneyInValue3.put(DbHelper.DEBTAMOUNT, newDebtAmount);
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue3, DbHelper.ID + "=" + findMatchingDebtId(), null);

        allDebtData();

        moneyInValue4 = new ContentValues();
        moneyInValue4.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db.update(DbHelper.DEBTS_TABLE_NAME, moneyInValue4, DbHelper.ID + "=" + findMatchingDebtId(), null);
        db.close();
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
        dbHelper6 = new DbHelper(getContext());
        db6 = dbHelper6.getWritableDatabase();

        newSavingsAmount = savingsAmount - moneyInAmount;
        moneyInValue5 = new ContentValues();
        moneyInValue5.put(DbHelper.SAVINGSAMOUNT, newSavingsAmount);
        db6.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue5, DbHelper.ID + "=" + findMatchingSavingsId(), null);

        allSavingsData();

        moneyInValue6 = new ContentValues();
        moneyInValue6.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)));
        db6.update(DbHelper.SAVINGS_TABLE_NAME, moneyInValue6, DbHelper.ID + "=" + findMatchingSavingsId(), null);
        db6.close();
    }

    public void updateAccountBalance() {
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();

        newAccountBalance = dbManager.retrieveCurrentAccountBalance() + moneyInAmount;

        moneyInValue = new ContentValues();
        moneyInValue.put(DbHelper.CURRENTACCOUNT, newAccountBalance);
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue, DbHelper.ID + "= '1'", null);
        db4.close();
    }

    public void determineAandBPortionsMoneyIn() {
        if (newTransaction) {
            moneyInAmountN = moneyInAmount;
        } else {
            moneyInAmountN = amountEntry;
        }
        amountForA = moneyInAmountN * dbManager.retrieveAPercentage();
        amountForB = moneyInAmountN * dbManager.retrieveBPercentage();

        if (dbManager.retrieveCurrentOwingA() == 0.0) { //if nothing owing to A, then split moneyIn according to budget
            moneyInA = amountForA;
            moneyInOwing = 0.0;
            moneyInB = amountForB;
        } else if (dbManager.retrieveCurrentOwingA() > 0) { //if money owing to A
            if (amountForB >= dbManager.retrieveCurrentOwingA()) { //if B's portion will cover it, then take what you need and give the rest to B
                moneyInA = amountForA + dbManager.retrieveCurrentOwingA();
                moneyInOwing = dbManager.retrieveCurrentOwingA();
                moneyInB = amountForB - dbManager.retrieveCurrentOwingA();
            } else { //if B's portion will cover only part, then take it all for A
                moneyInA = moneyInAmountN;
                moneyInOwing = amountForB;
                moneyInB = 0.0;
            }
        }
    }

    public void updateAandB() {
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();

        newABalance = dbManager.retrieveCurrentA() + moneyInA;
        newOwingA = dbManager.retrieveCurrentOwingA() - moneyInOwing;
        newBBalance = dbManager.retrieveCurrentB() + moneyInB;

        moneyInValue2 = new ContentValues();

        moneyInValue2.put(DbHelper.CURRENTA, newABalance);
        moneyInValue2.put(DbHelper.CURRENTOWINGA, newOwingA);
        moneyInValue2.put(DbHelper.CURRENTB, newBBalance);
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue2, DbHelper.ID + "= '1'", null);
        db3.close();
    }

    public void adjustAandB() {
        //if(dbManager.retrieveCurrentOwingA() < 0) { //if amount owing to A is negative, then A actually owes B
        amountToZero = -(dbManager.retrieveCurrentOwingA());
        newABalance = dbManager.retrieveCurrentA() - amountToZero;
        newOwingA = 0.0;
        newBBalance = dbManager.retrieveCurrentB() + amountToZero;
        moneyInA2 = moneyInA - amountToZero;
        moneyInOwing2 = moneyInOwing + amountToZero;
        moneyInB2 = moneyInB + amountToZero;
        //orig deposit 100 = 90A, 0Owing, 10B then bal of Owing = -50
        //adj bal = 40A (90-50), 0Owing (-50+50), 60B (10+50)
        //adj amt = 40A (90-50), 50Owing (0 + 50), 60B (10 + 50)

        dbHelper7 = new DbHelper(getContext());
        db7 = dbHelper7.getWritableDatabase();

        moneyInValue7 = new ContentValues();

        moneyInValue7.put(DbHelper.CURRENTA, newABalance);
        moneyInValue7.put(DbHelper.CURRENTOWINGA, newOwingA);
        moneyInValue7.put(DbHelper.CURRENTB, newBBalance);
        db7.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue7, DbHelper.ID + "= '1'", null);
        db7.close();
    }

    public void showContinueButtons() {
        debtContinueAnywayText.setVisibility(View.VISIBLE);
        noMoneyInButton.setVisibility(View.VISIBLE);
        yesMoneyInButton.setVisibility(View.VISIBLE);
    }

    public void hideContinueButtons() {
        debtContinueAnywayText.setVisibility(View.GONE);
        noMoneyInButton.setVisibility(View.GONE);
        yesMoneyInButton.setVisibility(View.GONE);
    }

    public void createMoneyIn() {
        moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInA, moneyInOwing, moneyInB, moneyInCreatedOn, incRefKeyMI, 0);

        dbManager.addMoneyIn(moneyInDb);
        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_LONG).show();
        moneyInAmountText.setText("");
        moneyInCatSpinner.setSelection(0, false);

        moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
        moneyInAdapter.notifyDataSetChanged();

        updateAccountBalance();
        updateAandB();
        if (dbManager.retrieveCurrentOwingA() < 0) {
            adjustAandB();
            moneyInA = moneyInA2;
            moneyInOwing = moneyInOwing2;
            moneyInB = moneyInB2;
        }
    }

    View.OnClickListener onClickMoneyInButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            newTransaction = true;

            moneyInCat = moneyInCatS;
            if (moneyInCat == null || moneyInCat.equals("")) {
                Toast.makeText(getContext(), R.string.no_inc_warning, Toast.LENGTH_LONG).show();
            } else {
                moneyInAmount = general.extractingDouble(moneyInAmountText);
                moneyInDate = new Date();
                moneyInTimestamp = new Timestamp(moneyInDate.getTime());
                moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
                moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);
                incRefKeyMI = moneyInRefKeyMI;

                determineAandBPortionsMoneyIn();

                findMatchingDebtId();
                findMatchingSavingsId();
                if (!foundMatchingDebtId && !foundMatchingSavingsId) {
                    debtNotPossibleText.setVisibility(View.GONE);
                    savingsNotPossibleText.setVisibility(View.GONE);
                    createMoneyIn();
                    hideContinueButtons();
                    backToDaily();
                } else if (foundMatchingDebtId) {
                    allDebtData();
                    if (debtAmount + moneyInAmount > debtLimit) {
                        debtNotPossibleText.setVisibility(View.VISIBLE);
                        showContinueButtons();
                        noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                hideContinueButtons();
                                backToDaily();
                            }
                        });
                        yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                updateDebtsRecord();
                                createMoneyIn();
                                hideContinueButtons();
                                backToDaily();
                            }
                        });
                    } else {
                        debtNotPossibleText.setVisibility(View.GONE);
                        savingsNotPossibleText.setVisibility(View.GONE);
                        updateDebtsRecord();
                        createMoneyIn();
                        hideContinueButtons();
                        backToDaily();
                    }
                } else if (foundMatchingSavingsId) {
                    allSavingsData();
                    if (savingsAmount - moneyInAmount < 0) {
                        savingsNotPossibleText.setVisibility(View.VISIBLE);
                        showContinueButtons();
                        noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                hideContinueButtons();
                                backToDaily();
                            }
                        });
                        yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                updateSavingsRecord();
                                createMoneyIn();
                                hideContinueButtons();
                                backToDaily();
                            }
                        });
                    } else {
                        debtNotPossibleText.setVisibility(View.GONE);
                        savingsNotPossibleText.setVisibility(View.GONE);
                        updateSavingsRecord();
                        createMoneyIn();
                        hideContinueButtons();
                        backToDaily();
                    }
                }
            }
        }
    };

    public void finishTransaction() {
        updateAccountBalance();

        moneyInDb.setMoneyInAmount(amountEntry);
        moneyInDb.setMoneyInA(moneyInA);
        moneyInDb.setMoneyInOwing(moneyInOwing);
        moneyInDb.setMoneyInB(moneyInB);
        dbManager.updateMoneyIn(moneyInDb);
        moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
        moneyInAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

        updateMoneyInLayout.setVisibility(View.GONE);
        addMoneyInLayout.setVisibility(View.GONE);
        moneyInCatText.setVisibility(View.GONE);
        moneyInAmountEditText.setVisibility(View.GONE);
        cancelMoneyInEntryButton.setVisibility(View.GONE);
        updateMoneyInEntryButton.setVisibility(View.GONE);

        backToDaily();
    }

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
                if (String.valueOf(moneyIn.get(position).getId()).equals("1")) {
                    holder.moneyInDelete.setVisibility(View.INVISIBLE);
                }
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
                    moneyInCatText.setVisibility(View.VISIBLE);
                    moneyInAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyInEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyInEntryButton.setVisibility(View.VISIBLE);

                    moneyInCatText.setText(moneyInDb.getMoneyInCat());

                    try {
                        moneyInAmountD = moneyInDb.getMoneyInAmount();
                    } catch (NullPointerException e) {
                        moneyInAmountD = 0.0;
                    }
                    moneyInAmountS = currencyFormat.format(moneyInAmountD);
                    moneyInAmountEditText.setText(moneyInAmountS);

                    oldMoneyInAmount = general.extractingDollars(moneyInAmountEditText);

                    try {
                        moneyInA = -(moneyIn.get(position).getMoneyInA());
                    } catch (NullPointerException e2) {
                        moneyInA = 0.0;
                    }
                    try {
                        moneyInOwing = -(moneyIn.get(position).getMoneyInOwing());
                    } catch (NullPointerException e3) {
                        moneyInOwing = 0.0;
                    }
                    try {
                        moneyInB = -(moneyIn.get(position).getMoneyInB());
                    } catch (NullPointerException e4) {
                        moneyInB = 0.0;
                    }

                    moneyInRefKeyMI = moneyIn.get(position).getIncRefKeyMI();

                    updateMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            newTransaction = false;

                            amountEntry = general.extractingDouble(moneyInAmountEditText);
                            moneyInAmount = amountEntry - oldMoneyInAmount;

                            updateAandB();
                            determineAandBPortionsMoneyIn();
                            updateAandB();
                            if (dbManager.retrieveCurrentOwingA() < 0) {
                                adjustAandB();
                                moneyInA = moneyInA2;
                                moneyInOwing = moneyInOwing2;
                                moneyInB = moneyInB2;
                            }

                            findMatchingDebtId();
                            findMatchingSavingsId();
                            if (!foundMatchingDebtId && !foundMatchingSavingsId) {
                                debtNotPossibleText.setVisibility(View.GONE);
                                savingsNotPossibleText.setVisibility(View.GONE);
                                hideContinueButtons();
                                finishTransaction();
                            } else if (foundMatchingDebtId) {
                                allDebtData();
                                if (debtAmount + moneyInAmount > debtLimit) {
                                    debtNotPossibleText.setVisibility(View.VISIBLE);
                                    showContinueButtons();

                                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            debtNotPossibleText.setVisibility(View.GONE);
                                            savingsNotPossibleText.setVisibility(View.GONE);
                                            hideContinueButtons();
                                            backToDaily();
                                        }
                                    });
                                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateDebtsRecord();
                                            finishTransaction();
                                        }
                                    });
                                } else {
                                    updateDebtsRecord();
                                    hideContinueButtons();
                                    finishTransaction();
                                }
                            } else if (foundMatchingSavingsId) {
                                allSavingsData();
                                if (savingsAmount - moneyInAmount < 0) {
                                    savingsNotPossibleText.setVisibility(View.VISIBLE);
                                    showContinueButtons();

                                    noMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            debtNotPossibleText.setVisibility(View.GONE);
                                            savingsNotPossibleText.setVisibility(View.GONE);
                                            hideContinueButtons();
                                            backToDaily();
                                        }
                                    });
                                    yesMoneyInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateSavingsRecord();
                                            finishTransaction();
                                        }
                                    });
                                } else {
                                    updateSavingsRecord();
                                    hideContinueButtons();
                                    finishTransaction();
                                }
                            }
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
                    moneyInRefKeyMI = moneyIn.get(position).getIncRefKeyMI();
                    try{
                        moneyInAmount = -(moneyIn.get(position).getMoneyInAmount());
                    } catch(NullPointerException e5) {
                        moneyInAmount = 0.0;
                    }
                    try {
                        moneyInA = -(moneyIn.get(position).getMoneyInA());
                    } catch(NullPointerException e6) {
                        moneyInA = 0.0;
                    }
                    try {
                        moneyInOwing = -(moneyIn.get(position).getMoneyInOwing());
                    } catch(NullPointerException e7) {
                        moneyInOwing = 0.0;
                    }
                    try {
                        moneyInB = -(moneyIn.get(position).getMoneyInB());
                    } catch(NullPointerException e8) {
                        moneyInB = 0.0;
                    }

                    updateAccountBalance();
                    updateAandB();
                    if (dbManager.retrieveCurrentOwingA() < 0) {
                        adjustAandB();
                    }

                    findMatchingDebtId();
                    if (foundMatchingDebtId) {
                        allDebtData();
                        updateDebtsRecord();
                    }

                    findMatchingSavingsId();
                    if (foundMatchingSavingsId) {
                        allSavingsData();
                        updateSavingsRecord();
                    }

                    dbManager.deleteMoneyIn(moneyInDb);
                    moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
                    notifyDataSetChanged();

                    backToDaily();
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
