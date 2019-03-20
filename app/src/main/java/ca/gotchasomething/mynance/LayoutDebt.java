package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutDebt extends MainNavigation {

    Button cancelDebtButton, cancelDeleteDebtButton, continueDeleteDebtButton, doneDebtsSetUpButton, saveDebtButton, updateDebtButton;
    Calendar debtCal;
    ContentValues values, values2, values3, values4, values5;
    Date debtEndD, latestDateD;
    DbHelper dbHelper;
    DbManager dbManager;
    DebtDb debtDb;
    DebtDbAdapter debtAdapter;
    Double amount = 0.0, debtAmountEntryD = 0.0, annualIncome = 0.0, balanceAmount = 0.0, debtAmountD = 0.0, debtPercentD2 = 0.0, expenseAnnualAmount = 0.0,
            frequency = 0.0, debtFrequencyEntryD = 0.0, debtLimitEntryD = 0.0, numberOfYearsToPayDebt = 0.0, payments = 0.0, debtPaymentsEntryD = 0.0, rate = 0.0, debtRateEntryD = 0.0,
            totalDebt = 0.0, totalDebtD = 0.0;
    EditText debtAmountEntry, debtLimitEntry, debtNameEntry, debtPaymentsEntry, debtPercentEntry;
    FloatingActionButton addDebtButton;
    General general;
    int balanceDone = 0, budgetDone = 0, debtsDone = 0, savingsDone = 0, tourDone = 0;
    Integer numberOfDaysToPayDebt = 0;
    Intent addNewDebt, backToDebtScreen, backToSetUp;
    ListView debtListView;
    long id;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton debtBiWeeklyRadioButton, debtMonthlyRadioButton, debtWeeklyRadioButton;
    RadioGroup debtFrequencyRadioGroup;
    SQLiteDatabase expenseDb;
    SetUpDb setUpDb;
    SimpleDateFormat debtEndS, latestDateS;
    String debtAmount2 = null, debtAmountS = null, debtAmountS2 = null, debtEnd = null, debtFrequencyS = null, debtLimitS2 = null, debtPaymentsS = null,
            debtPercentS = null, expRefKeyD = null, latestDate = null, debtNameEntryD = null, priority = null, totalDebt2 = null, totalDebtS = null;
    TextView debtDateResult, debtDateResultLabel, debtsSetUpNoTime, debtsSetUpNoTime2, debtsSetUpNeedHelp, debtsSetUpNeedHelp2, deleteDebtWarningText,
            emptyDebtsText, emptyDebtsText2, emptyDebtsText3, totalDebtOwing, totalDebtPaidByDate, totalDebtPaidLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_debt);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        general = new General();
        dbManager = new DbManager(this);

        totalDebtOwing = findViewById(R.id.totalDebtOwing);
        totalDebtPaidLabel = findViewById(R.id.totalDebtPaidLabel);
        totalDebtPaidByDate = findViewById(R.id.totalDebtPaidByDate);
        emptyDebtsText = findViewById(R.id.emptyDebtsText);
        emptyDebtsText2 = findViewById(R.id.emptyDebtsText2);
        emptyDebtsText3 = findViewById(R.id.emptyDebtsText3);
        debtsSetUpNoTime = findViewById(R.id.debtsSetUpNoTime);
        debtsSetUpNoTime.setOnClickListener(onClickNoTime);
        debtsSetUpNoTime2 = findViewById(R.id.debtsSetUpNoTime2);
        debtsSetUpNoTime2.setVisibility(View.GONE);
        debtsSetUpNeedHelp = findViewById(R.id.debtsSetUpNeedHelp);
        debtsSetUpNeedHelp.setOnClickListener(onClickNeedHelp);
        debtsSetUpNeedHelp2 = findViewById(R.id.debtsSetUpNeedHelp2);
        debtsSetUpNeedHelp2.setVisibility(View.GONE);
        deleteDebtWarningText = findViewById(R.id.deleteDebtWarningText);
        deleteDebtWarningText.setVisibility(View.GONE);
        cancelDeleteDebtButton = findViewById(R.id.cancelDeleteDebtButton);
        cancelDeleteDebtButton.setVisibility(View.GONE);
        continueDeleteDebtButton = findViewById(R.id.continueDeleteDebtButton);
        continueDeleteDebtButton.setVisibility(View.GONE);

        debtListView = findViewById(R.id.debtListView);
        addDebtButton = findViewById(R.id.addDebtButton);
        addDebtButton.setOnClickListener(onClickAddDebtButton);

        doneDebtsSetUpButton = findViewById(R.id.doneDebtsSetUpButton);
        doneDebtsSetUpButton.setOnClickListener(onClickDoneDebtsSetUpButton);

        if (dbManager.debtSetUpCheck() > 0) {
            doneDebtsSetUpButton.setVisibility(View.GONE);
            emptyDebtsText3.setVisibility(View.GONE);
            debtsSetUpNoTime.setVisibility(View.GONE);
            debtsSetUpNoTime2.setVisibility(View.GONE);
            debtsSetUpNeedHelp.setVisibility(View.GONE);
            debtsSetUpNeedHelp2.setVisibility(View.GONE);
        }

        debtAdapter = new DebtDbAdapter(this, dbManager.getDebts());
        debtListView.setAdapter(debtAdapter);

        if (debtAdapter.getCount() == 0) {
            totalDebtPaidLabel.setVisibility(View.GONE);
            emptyDebtsText.setVisibility(View.VISIBLE);
            emptyDebtsText2.setVisibility(View.VISIBLE);
        } else {
            totalDebtPaidLabel.setVisibility(View.VISIBLE);
            emptyDebtsText.setVisibility(View.GONE);
            emptyDebtsText2.setVisibility(View.GONE);
        }

        debtHeaderText();
    }

    View.OnClickListener onClickDoneDebtsSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtsDone = 1;
            savingsDone = 0;
            budgetDone = 0;
            balanceDone = 0;
            balanceAmount = 0.0;
            tourDone = 0;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            Toast toast = Toast.makeText(getApplicationContext(), R.string.edit_debts_message, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutDebt.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    View.OnClickListener onClickNoTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtsSetUpNoTime2.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickNeedHelp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtsSetUpNeedHelp2.setVisibility(View.VISIBLE);
        }
    };

    public void debtHeaderText() {

        totalDebt = dbManager.sumTotalDebt();

        try {
            totalDebtS = String.valueOf(totalDebt);
            if (totalDebtS != null && !totalDebtS.equals("")) {
                totalDebtD = Double.valueOf(totalDebtS);
            } else {
                totalDebtD = 0.0;
            }

            totalDebt2 = currencyFormat.format(totalDebtD);
            totalDebtOwing.setText(totalDebt2);

        } catch (NumberFormatException e) {
            totalDebtOwing.setText(totalDebt2);
        }

        if (debtAdapter.getCount() == 0) {
            totalDebtPaidLabel.setVisibility(View.GONE);
            totalDebtPaidByDate.setVisibility(View.GONE);
        } else {
            totalDebtPaidLabel.setVisibility(View.VISIBLE);
            totalDebtPaidByDate.setVisibility(View.VISIBLE);
            totalDebtPaidByDate.setText(latestDate());
        }
    }

    public String latestDate() {
        List<String> dates = new ArrayList<>();
        for (DebtDb d : dbManager.getDebts()) {
            dates.add(d.getDebtEnd());
        }
        List<Date> dates2 = new ArrayList<>(dates.size());
        general.extractingDates(dates, dates2);
        try {
            latestDateD = Collections.max(dates2);
        } catch (NoSuchElementException e) {
            totalDebtPaidLabel.setVisibility(View.GONE);
            totalDebtPaidByDate.setVisibility(View.GONE);
        }
        try {
            latestDateS = new SimpleDateFormat("dd-MMM-yyyy");
            latestDate = latestDateS.format(latestDateD);
        } catch (Exception e2) {
            totalDebtPaidByDate.setVisibility(View.GONE);
        }
        return latestDate;
    }

    public void backToDebt() {
        backToDebtScreen = new Intent(LayoutDebt.this, LayoutDebt.class);
        backToDebtScreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDebtScreen);
    }

    public String priorityData() {
        for (ExpenseBudgetDb e : dbManager.getExpense()) {
            if (String.valueOf(debtDb.getExpRefKeyD()).equals(String.valueOf(e.getId()))) {
                priority = e.getExpensePriority();
            }
        }
        return priority;
    }

    public Double incomeData() {
        for (IncomeBudgetDb i : dbManager.getIncomes()) {
            if (String.valueOf(i.getId()).equals(String.valueOf(debtDb.getIncRefKeyD()))) {
                annualIncome = i.getIncomeAnnualAmount();
            }
        }
        return annualIncome;
    }

    public String calcDebtDate() {

        if (debtAmountEntry.getText().toString().equals("")) {
            amount = 0.0;
        } else {
            try {
                amount = Double.valueOf(debtAmountEntry.getText().toString());
            } catch (NumberFormatException e2) {
                amount = general.extractingDollars(debtAmountEntry);
            }
        }

        if (debtPercentEntry.getText().toString().equals("")) {
            rate = 0.0;
        } else {
            try {
                rate = Double.valueOf(debtPercentEntry.getText().toString());
            } catch (NumberFormatException e3) {
                rate = general.extractingPercents(debtPercentEntry);
            }
        }

        if (debtPaymentsEntry.getText().toString().equals("")) {
            payments = 0.0;
        } else {
            try {
                payments = Double.valueOf(debtPaymentsEntry.getText().toString());
            } catch (NumberFormatException e4) {
                payments = general.extractingDollars(debtPaymentsEntry);
            }
        }

        frequency = Double.valueOf(debtFrequencyS);

        debtCal = Calendar.getInstance();
        numberOfYearsToPayDebt = -(Math.log(1 - (amount * (rate / 100) / ((payments * frequency) - incomeData()))) / (frequency * Math.log(1 + ((rate / 100) / frequency))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (amount <= 0) {
            debtEnd = getString(R.string.debt_paid);
            debtDateResultLabel.setVisibility(View.GONE);

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = getString(R.string.too_far);
            debtDateResultLabel.setVisibility(View.GONE);

        } else {
            debtCal = Calendar.getInstance();
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = debtEndS.format(debtEndD);
            debtDateResultLabel.setVisibility(View.VISIBLE);
        }

        return debtEnd;
    }

    TextWatcher onChangeDebtAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtPercent = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtPayments = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtDateResult.setText(calcDebtDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public class DebtDbAdapter extends ArrayAdapter<DebtDb> {

        private Context context;
        private List<DebtDb> debts;

        private DebtDbAdapter(
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
                            View convertView, @NonNull ViewGroup parent) {

            final DebtViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_debt,
                        parent, false);

                holder = new DebtViewHolder();
                holder.debtListName = convertView.findViewById(R.id.debtListName);
                holder.debtListAmount = convertView.findViewById(R.id.debtListAmount);
                holder.debtListFreeDateLabel = convertView.findViewById(R.id.debtListFreeDateLabel);
                holder.debtListFreeDate = convertView.findViewById(R.id.debtListFreeDate);
                holder.debtDeleted = convertView.findViewById(R.id.deleteDebtButton);
                holder.debtEdit = convertView.findViewById(R.id.editDebtButton);
                convertView.setTag(holder);

            } else {
                holder = (DebtViewHolder) convertView.getTag();
            }

            //retrieve debtName
            holder.debtListName.setText(debts.get(position).getDebtName());

            //retrieve debtAmount and format as currency
            try {
                debtAmountS = (String.valueOf(debts.get(position).getDebtAmount()));
                if (debtAmountS != null && !debtAmountS.equals("")) {
                    debtAmountD = Double.valueOf(debtAmountS);
                } else {
                    debtAmountD = 0.0;
                }
                debtAmount2 = currencyFormat.format(debtAmountD);
                holder.debtListAmount.setText(debtAmount2);
            } catch (NumberFormatException e5) {
                holder.debtListAmount.setText(debtAmount2);
            }

            //retrieve debtEnd
            debtEnd = debts.get(position).getDebtEnd();
            if (debtEnd.contains("2")) {
                holder.debtListFreeDateLabel.setVisibility(View.VISIBLE);
            } else {
                holder.debtListFreeDateLabel.setVisibility(View.GONE);
            }
            holder.debtListFreeDate.setText(debtEnd);

            holder.debtDeleted.setTag(debts.get(position));
            holder.debtEdit.setTag(debts.get(position));

            //click on pencil icon
            holder.debtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_debt);
                    LayoutDebt.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getApplicationContext());

                    debtNameEntry = findViewById(R.id.debtNameEntry);
                    debtLimitEntry = findViewById(R.id.debtLimitEntry);
                    debtAmountEntry = findViewById(R.id.debtAmountEntry);
                    debtPercentEntry = findViewById(R.id.debtPercentEntry);
                    debtPaymentsEntry = findViewById(R.id.debtPaymentsEntry);
                    debtDateResult = findViewById(R.id.debtDateResult);
                    debtDateResultLabel = findViewById(R.id.debtDateResultLabel);

                    debtFrequencyRadioGroup = findViewById(R.id.debtFrequencyRadioGroup);

                    debtWeeklyRadioButton = findViewById(R.id.debtWeeklyRadioButton);
                    debtBiWeeklyRadioButton = findViewById(R.id.debtBiWeeklyRadioButton);
                    debtMonthlyRadioButton = findViewById(R.id.debtMonthlyRadioButton);

                    saveDebtButton = findViewById(R.id.saveDebtButton);
                    saveDebtButton.setVisibility(View.GONE);
                    updateDebtButton = findViewById(R.id.updateDebtButton);
                    cancelDebtButton = findViewById(R.id.cancelDebtButton);

                    debtDb = (DebtDb) holder.debtEdit.getTag();

                    debtNameEntryD = debtDb.getDebtName();
                    debtNameEntry.setText(debtNameEntryD);

                    debtLimitEntryD = debtDb.getDebtLimit();
                    debtLimitS2 = currencyFormat.format(debtLimitEntryD);
                    debtLimitEntry.setText(debtLimitS2);

                    debtAmountEntryD = debtDb.getDebtAmount();
                    debtAmountS2 = currencyFormat.format(debtAmountEntryD);
                    debtAmountEntry.setText(debtAmountS2);
                    debtAmountEntry.addTextChangedListener(onChangeDebtAmount);

                    debtRateEntryD = debtDb.getDebtRate();
                    debtPercentD2 = debtRateEntryD / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    debtPercentS = percentFormat.format(debtPercentD2);
                    debtPercentEntry.setText(debtPercentS);
                    debtPercentEntry.addTextChangedListener(onChangeDebtPercent);

                    debtPaymentsEntryD = debtDb.getDebtPayments();
                    debtPaymentsS = currencyFormat.format(debtPaymentsEntryD);
                    debtPaymentsEntry.setText(debtPaymentsS);
                    debtPaymentsEntry.addTextChangedListener(onChangeDebtPayments);

                    //set radio button selections from data
                    if (debtDb.getDebtFrequency() == 52) {
                        debtWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "52";
                    } else if (debtDb.getDebtFrequency() == 26) {
                        debtBiWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "26";
                    } else if (debtDb.getDebtFrequency() == 12) {
                        debtMonthlyRadioButton.setChecked(true);
                        debtFrequencyS = "12";
                    }

                    //update db if radio buttons changed
                    debtFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.debtWeeklyRadioButton:
                                    debtFrequencyS = "52";
                                    debtDateResult.setText(calcDebtDate());
                                    break;
                                case R.id.debtBiWeeklyRadioButton:
                                    debtFrequencyS = "26";
                                    debtDateResult.setText(calcDebtDate());
                                    break;
                                case R.id.debtMonthlyRadioButton:
                                    debtFrequencyS = "12";
                                    debtDateResult.setText(calcDebtDate());
                                    break;
                            }
                        }
                    });

                    debtDateResult.setText(calcDebtDate());

                    updateDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyD = String.valueOf(debtDb.getExpRefKeyD());

                            if (debtNameEntry.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                debtNameEntryD = debtNameEntry.getText().toString();
                                try {
                                    debtLimitEntryD = Double.valueOf(debtLimitEntry.getText().toString());
                                } catch (NumberFormatException e11) {
                                    debtLimitEntryD = general.extractingDollars(debtLimitEntry);
                                }
                                if (debtLimitEntry.getText().toString().equals("")) {
                                    Toast.makeText(getBaseContext(), R.string.no_limit_warning, Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        debtAmountEntryD = Double.valueOf(debtAmountEntry.getText().toString());
                                    } catch (NumberFormatException e8) {
                                        debtAmountEntryD = general.extractingDollars(debtAmountEntry);
                                    }
                                    if (debtAmountEntry.getText().toString().equals("")) {
                                        debtAmountEntryD = 0.0;
                                    }
                                    try {
                                        debtRateEntryD = (Double.valueOf(debtPercentEntry.getText().toString()));
                                    } catch (NumberFormatException e10) {
                                        debtRateEntryD = general.extractingPercents(debtPercentEntry);
                                    }
                                    if (debtPercentEntry.getText().toString().equals("")) {
                                        debtRateEntryD = 0.0;
                                    }
                                    try {
                                        debtPaymentsEntryD = (Double.valueOf(debtPaymentsEntry.getText().toString()));
                                    } catch (NumberFormatException e9) {
                                        debtPaymentsEntryD = (general.extractingDollars(debtPaymentsEntry));
                                    }
                                    if (debtPaymentsEntry.getText().toString().equals("")) {
                                        debtPaymentsEntryD = 0.0;
                                    }
                                    debtFrequencyEntryD = Double.valueOf(debtFrequencyS);

                                    debtDb.setDebtName(debtNameEntryD);
                                    debtDb.setDebtLimit(debtLimitEntryD);
                                    debtDb.setDebtAmount(debtAmountEntryD);
                                    debtDb.setDebtRate(debtRateEntryD);
                                    debtDb.setDebtPayments(debtPaymentsEntryD);
                                    debtDb.setDebtFrequency(debtFrequencyEntryD);
                                    debtDb.setDebtEnd(calcDebtDate());

                                    dbHelper = new DbHelper(getContext());
                                    expenseDb = dbHelper.getWritableDatabase();

                                    String[] args = new String[]{expRefKeyD};
                                    String[] args2 = new String[]{String.valueOf(debtDb.getId())};
                                    String[] args3 = new String[]{String.valueOf(debtDb.getIncRefKeyD())};
                                    values = new ContentValues();
                                    values2 = new ContentValues();
                                    values3 = new ContentValues();
                                    values4 = new ContentValues();
                                    values5 = new ContentValues();
                                    values.put(DbHelper.EXPENSENAME, debtNameEntryD);
                                    values2.put(DbHelper.INCOMENAME, debtNameEntryD);
                                    values3.put(DbHelper.MONEYOUTCAT, debtNameEntryD);
                                    values4.put(DbHelper.MONEYOUTDEBTCAT, debtNameEntryD);
                                    values5.put(DbHelper.MONEYINCAT, debtNameEntryD);

                                    values.put(DbHelper.EXPENSEAMOUNT, debtPaymentsEntryD);
                                    values.put(DbHelper.EXPENSEFREQUENCY, debtFrequencyEntryD);
                                    expenseAnnualAmount = debtPaymentsEntryD * debtFrequencyEntryD;
                                    values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                                    if (priorityData().equals("A")) {
                                        values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                                    } else if (priorityData().equals("B")) {
                                        values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                                    }

                                    expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                    expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args3);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values4, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args2);
                                    expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values5, DbHelper.INCREFKEYMI + "=?", args3);

                                    expenseDb.close();

                                    dbManager.updateDebt(debtDb);
                                    debtAdapter.updateDebts(dbManager.getDebts());
                                    notifyDataSetChanged();

                                    Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                    backToDebt();
                                    debtHeaderText();
                                }
                            }
                        }
                    });

                    cancelDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToDebt();
                        }
                    });
                }
            });

            //click on trash can icon
            holder.debtDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    debtDb = (DebtDb) holder.debtDeleted.getTag();

                    deleteDebtWarningText.setVisibility(View.VISIBLE);
                    cancelDeleteDebtButton.setVisibility(View.VISIBLE);
                    continueDeleteDebtButton.setVisibility(View.VISIBLE);
                    debtListView.setVisibility(View.GONE);

                    cancelDeleteDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToDebt();
                        }
                    });

                    continueDeleteDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleteDebtWarningText.setVisibility(View.GONE);
                            cancelDeleteDebtButton.setVisibility(View.GONE);
                            continueDeleteDebtButton.setVisibility(View.GONE);

                            dbHelper = new DbHelper(getContext());
                            expenseDb = dbHelper.getWritableDatabase();


                            try {
                                String[] args = new String[]{String.valueOf(debtDb.getExpRefKeyD())};
                                expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            try {
                                String[] args2 = new String[]{String.valueOf(debtDb.getIncRefKeyD())};
                                expenseDb.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            expenseDb.close();

                            dbManager.deleteDebt(debtDb);
                            debtAdapter.updateDebts(dbManager.getDebts());
                            notifyDataSetChanged();
                            if (debtAdapter.getCount() == 0) {
                                totalDebtPaidLabel.setVisibility(View.GONE);
                                emptyDebtsText.setVisibility(View.VISIBLE);
                                emptyDebtsText2.setVisibility(View.VISIBLE);
                            } else {
                                totalDebtPaidLabel.setVisibility(View.VISIBLE);
                                emptyDebtsText.setVisibility(View.GONE);
                                emptyDebtsText2.setVisibility(View.GONE);
                            }

                            debtHeaderText();
                            backToDebt();
                        }
                    });
                }
            });

            return convertView;
        }
    }

    private static class DebtViewHolder {
        public TextView debtListName;
        public TextView debtListAmount;
        public TextView debtListFreeDateLabel;
        public TextView debtListFreeDate;
        ImageButton debtDeleted;
        ImageButton debtEdit;
    }

    View.OnClickListener onClickAddDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewDebt = new Intent(LayoutDebt.this, AddDebt.class);
            addNewDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewDebt);
        }
    };
}
