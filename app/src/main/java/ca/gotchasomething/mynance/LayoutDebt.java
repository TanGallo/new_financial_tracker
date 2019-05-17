package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutDebt extends MainNavigation {

    Button cancelDebtButton, cancelDeleteDebtButton, continueDeleteDebtButton, saveDebtButton, updateDebtButton;
    ContentValues values, values2, values3, values4, values5;
    Date latestDateD;
    DbHelper dbHelper;
    DbManager dbManager;
    DebtDb debtDb;
    DebtDbAdapter debtAdapter;
    Double debtAmount = 0.0, debtAmountb = 0.0, debtAnnualIncome = 0.0, debtAnnualIncomeb = 0.0, balanceAmount = 0.0, debtAmountD = 0.0, debtPercentD2 = 0.0,
            expenseAnnualAmount = 0.0, debtFrequency = 0.0, debtFrequencyb = 0.0, debtLimit = 0.0, debtLimitb = 0.0, debtPayments = 0.0, debtPaymentsb = 0.0,
            debtRate = 0.0, debtRateb = 0.0, totalDebt = 0.0, totalDebtD = 0.0;
    EditText debtAmountEntry, debtLimitEntry, debtNameEntry, debtPaymentsEntry, debtPercentEntry;
    FloatingActionButton addDebtButton;
    General general;
    int balanceDone = 0, billsDone = 0, budgetDone = 0, debtsDone = 0, incomeDone = 0, savingsDone = 0, tourDone = 0;
    Intent addNewDebt, backToDebtScreen, backToSetUp;
    LinearLayout toastLayout;
    ListView debtListView;
    long id, incRefKeyD;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton debtBiWeeklyRadioButton, debtMonthlyRadioButton, debtWeeklyRadioButton;
    RadioGroup debtFrequencyRadioGroup;
    SQLiteDatabase expenseDb;
    SetUpDb setUpDb;
    SimpleDateFormat latestDateS;
    String debtAmount2 = null, debtAmountS = null, debtAmountS2 = null, debtEnd = null, debtEnd2 = null, debtFrequencyS = null, debtLimitS2 = null, debtPaymentsS = null,
            debtPercentS = null, expRefKeyD = null, latestDate = null, debtName = null, debtNameb = null, priority = null, totalDebt2 = null, totalDebtS = null,
            weekly = null;
    TextView debtDateResult, debtDateResultLabel, debtsSetUpNoTime, debtsSetUpNoTime2, debtsSetUpNeedHelp, debtsSetUpNeedHelp2, deleteDebtWarningText,
            emptyDebtsText, emptyDebtsText2, emptyDebtsText3, totalDebtOwing, totalDebtPaidByDate, totalDebtPaidLabel, tv;
    Toast toast;

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

        menuConfig();

        general = new General();
        dbManager = new DbManager(this);

        totalDebtOwing = findViewById(R.id.totalDebtOwing);
        totalDebtPaidLabel = findViewById(R.id.totalDebtPaidLabel);
        totalDebtPaidByDate = findViewById(R.id.totalDebtPaidByDate);
        emptyDebtsText = findViewById(R.id.emptyDebtsText);
        emptyDebtsText2 = findViewById(R.id.emptyDebtsText2);
        deleteDebtWarningText = findViewById(R.id.deleteDebtWarningText);
        deleteDebtWarningText.setVisibility(View.GONE);
        cancelDeleteDebtButton = findViewById(R.id.cancelDeleteDebtButton);
        cancelDeleteDebtButton.setVisibility(View.GONE);
        continueDeleteDebtButton = findViewById(R.id.continueDeleteDebtButton);
        continueDeleteDebtButton.setVisibility(View.GONE);

        debtListView = findViewById(R.id.debtListView);
        addDebtButton = findViewById(R.id.addDebtButton);
        addDebtButton.setOnClickListener(onClickAddDebtButton);

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
            if (String.valueOf(expRefKeyD).equals(String.valueOf(e.getId()))) {
                priority = e.getExpensePriority();
            }
        }
        return priority;
    }

    TextWatcher onChangeDebtAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtEndResult();
            debtDateResult.setText(debtEnd2);
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
            debtEndResult();
            debtDateResult.setText(debtEnd2);
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
            debtEndResult();
            debtDateResult.setText(debtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void debtEndResult() {
        allDebtData();
        debtEnd2 = general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        general.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), debtDateResultLabel, debtDateResult);
    }

    public void allDebtData() {
        if (debtNameEntry.getText().toString().equals("")) {
            debtName = "null";
        } else {
            debtName = debtNameEntry.getText().toString();
        }
        debtLimit = general.extractingDouble(debtLimitEntry);
        debtAmount = general.extractingDouble(debtAmountEntry);
        debtRate = general.extractingPercent(debtPercentEntry);
        debtPayments = general.extractingDouble(debtPaymentsEntry);
        try {
            debtFrequency = Double.valueOf(debtFrequencyS);
        } catch (NullPointerException e) {
            debtFrequency = 12.0;
        }
        debtAnnualIncome = debtAnnualIncomeb;

    }

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
                holder.debtOverLimit = convertView.findViewById(R.id.debtOverLimitWarning);
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
            if (debtEnd.equals(getString(R.string.debt_paid))) {
                holder.debtListFreeDate.setTextColor(Color.parseColor("#03ac13"));
            } else if (debtEnd.equals(getString(R.string.too_far))) {
                holder.debtListFreeDate.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                holder.debtListFreeDate.setTextColor(Color.parseColor("#303F9F"));
                holder.debtListFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            incRefKeyD = debts.get(position).getIncRefKeyD();

            for (IncomeBudgetDb i : dbManager.getIncomes()) {
                if (String.valueOf(i.getId()).equals(String.valueOf(incRefKeyD))) {
                    debtAnnualIncomeb = i.getIncomeAnnualAmount();
                }
            }

            holder.debtDeleted.setTag(debts.get(position));
            holder.debtEdit.setTag(debts.get(position));

            if (debts.get(position).getDebtLimit() == 0) {
                holder.debtOverLimit.setVisibility(View.GONE);
            } else if (debts.get(position).getDebtAmount() > debts.get(position).getDebtLimit()) {
                holder.debtOverLimit.setVisibility(View.VISIBLE);
            } else {
                holder.debtOverLimit.setVisibility(View.GONE);
            }

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

                    debtNameb = debtDb.getDebtName();
                    debtNameEntry.setText(debtNameb);

                    debtLimitb = debtDb.getDebtLimit();
                    debtLimitS2 = currencyFormat.format(debtLimitb);
                    debtLimitEntry.setText(debtLimitS2);

                    debtAmount = debtDb.getDebtAmount();
                    debtAmountS2 = currencyFormat.format(debtAmount);
                    debtAmountEntry.setText(debtAmountS2);
                    debtAmountEntry.addTextChangedListener(onChangeDebtAmount);

                    debtRate = debtDb.getDebtRate();
                    debtPercentD2 = debtRate / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    debtPercentS = percentFormat.format(debtPercentD2);
                    debtPercentEntry.setText(debtPercentS);
                    debtPercentEntry.addTextChangedListener(onChangeDebtPercent);

                    debtPayments = debtDb.getDebtPayments();
                    debtPaymentsS = currencyFormat.format(debtPayments);
                    debtPaymentsEntry.setText(debtPaymentsS);
                    debtPaymentsEntry.addTextChangedListener(onChangeDebtPayments);

                    debtFrequency = debtDb.getDebtFrequency();

                    debtAnnualIncome = debtDb.getDebtAnnualIncome();

                    debtEnd = debtDb.getDebtEnd();
                    debtDateResult.setText(debtEnd);
                    if (debtEnd.equals(getString(R.string.debt_paid))) {
                        debtDateResultLabel.setVisibility(View.GONE);
                        debtDateResult.setTextColor(Color.parseColor("#03ac13"));
                    } else if (debtEnd.equals(getString(R.string.too_far))) {
                        debtDateResultLabel.setVisibility(View.GONE);
                        debtDateResult.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        debtDateResultLabel.setVisibility(View.VISIBLE);
                        debtDateResult.setTextColor(Color.parseColor("#303F9F"));
                        debtDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

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
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                                case R.id.debtBiWeeklyRadioButton:
                                    debtFrequencyS = "26";
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                                case R.id.debtMonthlyRadioButton:
                                    debtFrequencyS = "12";
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                            }
                        }
                    });

                    updateDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyD = String.valueOf(debtDb.getExpRefKeyD());

                            debtEndResult();

                            if (debtName != null) {

                                debtDb.setDebtName(debtName);
                                debtDb.setDebtLimit(debtLimit);
                                debtDb.setDebtAmount(debtAmount);
                                debtDb.setDebtRate(debtRate);
                                debtDb.setDebtPayments(debtPayments);
                                debtDb.setDebtFrequency(debtFrequency);
                                debtDb.setDebtEnd(debtEnd2);

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

                                values.put(DbHelper.EXPENSENAME, debtName);
                                values.put(DbHelper.EXPENSEAMOUNT, debtPayments);
                                values.put(DbHelper.EXPENSEFREQUENCY, debtFrequency);
                                expenseAnnualAmount = debtPayments * debtFrequency;
                                values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                                if (priorityData().equals("A")) {
                                    values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                                } else if (priorityData().equals("B")) {
                                    values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                                }
                                values2.put(DbHelper.INCOMENAME, debtName);
                                values3.put(DbHelper.MONEYOUTCAT, debtName);
                                values4.put(DbHelper.MONEYOUTDEBTCAT, debtName);
                                values5.put(DbHelper.MONEYINCAT, debtName);

                                try {
                                    expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                    expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args3);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values4, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args2);
                                    expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values5, DbHelper.INCREFKEYMI + "=?", args3);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                expenseDb.close();

                                dbManager.updateDebt(debtDb);
                                debtAdapter.updateDebts(dbManager.getDebts());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                backToDebt();
                                debtHeaderText();
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
        public TextView debtOverLimit;
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
