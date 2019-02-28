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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutSavings extends MainNavigation {

    Button cancelDeleteSavingsButton, continueDeleteSavingsButton, cancelSavingsButton, doneSavingsSetUpButton, saveSavingsButton, updateSavingsButton;
    Calendar savingsCal;
    ContentValues values, values2, values3, values4;
    Date savingsDateD;
    DbHelper dbHelper;
    DbManager dbManager;
    Double amount = 0.0, savingsAmountEntryD = 0.0, annualIncome = 0.0, balanceAmount = 0.0, expenseAnnualAmount = 0.0, frequency = 0.0, savingsFrequencyEntryD = 0.0, goal = 0.0,
            savingsGoalEntryD = 0.0, intFrequency = 0.0, savingsIntFrequencyEntryD = 0.0, payments = 0.0, savingsPaymentsEntryD = 0.0, rate = 0.0, rate2 = 0.0, savingsRateEntryD = 0.0,
            savingsCurrentD = 0.0, savingsGoalD = 0.0, savingsPercentD2 = 0.0, totalSavings = 0.0, totalSavingsD = 0.0, years = 0.0;
    EditText savingsAmountEntry, savingsGoalAmountEntry, savingsNameEntry, savingsPaymentsEntry, savingsPercentEntry;
    FloatingActionButton addSavingsButton;
    General general;
    int balanceDone = 0, budgetDone = 0, debtsDone = 0, savingsDone = 0, tourDone = 0;
    Integer numberOfDaysToSavingsGoal = 0;
    Intent addNewSavings, backToSavingsScreen, backToSetUp;
    ListView savingsListView;
    long id;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton savingsAnnuallyRadioButton, savingsBiWeeklyRadioButton, savingsIntAnnuallyRadioButton, savingsIntMonthlyRadioButton, savingsMonthlyRadioButton,
            savingsWeeklyRadioButton;
    RadioGroup savingsFrequencyRadioGroup, savingsIntFrequencyRadioGroup;
    SavingsDbAdapter savingsAdapter;
    SavingsDb savingsDb;
    SetUpDb setUpDb;
    SimpleDateFormat savingsDateS;
    SQLiteDatabase expenseDb;
    String expRefKeyS = null, savingsNameEntryS = null, priority = null, savingsAmountS = null, savingsCurrent2 = null, savingsCurrentS = null, savingsDate = null,
            savingsFrequencyS = null, savingsGoal2 = null, savingsGoalS = null, savingsGoalS2 = null, savingsIntFrequencyS = null, savingsPaymentsS = null,
            savingsPercentS = null, totalSavings2 = null, totalSavingsS = null;
    TextView deleteSavingsWarningText, emptysavingsText, emptysavingsText2, emptySavingsText3, savingsDateResult, savingsDateResultLabel, savingsFrequencyLabel, savingsIntFrequencyLabel, totalSavedText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_savings);
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

        totalSavedText = findViewById(R.id.totalSavedText);
        emptysavingsText = findViewById(R.id.emptySavingsText);
        emptysavingsText2 = findViewById(R.id.emptySavingsText2);
        emptySavingsText3 = findViewById(R.id.emptySavingsText3);
        deleteSavingsWarningText = findViewById(R.id.deleteSavingsWarningText);
        deleteSavingsWarningText.setVisibility(View.GONE);
        cancelDeleteSavingsButton = findViewById(R.id.cancelDeleteSavingsButton);
        cancelDeleteSavingsButton.setVisibility(View.GONE);
        continueDeleteSavingsButton = findViewById(R.id.continueDeleteSavingsButton);
        continueDeleteSavingsButton.setVisibility(View.GONE);

        savingsListView = findViewById(R.id.savingsListView);
        addSavingsButton = findViewById(R.id.addSavingsButton);
        addSavingsButton.setOnClickListener(onClickAddSavingsButton);

        doneSavingsSetUpButton = findViewById(R.id.doneSavingsSetUpButton);
        doneSavingsSetUpButton.setOnClickListener(onClickDoneSavingsSetUpButton);

        if (dbManager.savingsSetUpCheck() > 0) {
            doneSavingsSetUpButton.setVisibility(View.GONE);
            emptySavingsText3.setVisibility(View.GONE);
        }

        savingsAdapter = new SavingsDbAdapter(this, dbManager.getSavings());
        savingsListView.setAdapter(savingsAdapter);

        if (savingsAdapter.getCount() == 0) {
            emptysavingsText.setVisibility(View.VISIBLE);
            emptysavingsText2.setVisibility(View.VISIBLE);
        } else {
            emptysavingsText.setVisibility(View.GONE);
            emptysavingsText2.setVisibility(View.GONE);
        }

        savingsHeaderText();
    }

    View.OnClickListener onClickDoneSavingsSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savingsDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            Toast toast = Toast.makeText(getApplicationContext(), R.string.edit_savings_message, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutSavings.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    public void savingsHeaderText() {

        totalSavings = dbManager.sumTotalSavings();

        try {
            totalSavingsS = String.valueOf(totalSavings);
            if (totalSavingsS != null && !totalSavingsS.equals("")) {
                totalSavingsD = Double.valueOf(totalSavingsS);
            } else {
                totalSavingsD = 0.0;
            }

            totalSavings2 = currencyFormat.format(totalSavingsD);
            totalSavedText.setText(totalSavings2);

        } catch (NumberFormatException e) {
            totalSavedText.setText(totalSavings2);
        }
    }

    public void backToSavings() {
        backToSavingsScreen = new Intent(LayoutSavings.this, LayoutSavings.class);
        backToSavingsScreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSavingsScreen);
    }

    public Double incomeData() {
        for (IncomeBudgetDb i : dbManager.getIncomes()) {
            if (String.valueOf(savingsDb.getIncRefKeyS()).equals(String.valueOf(i.getId()))) {
                annualIncome = i.getIncomeAnnualAmount();
            }
        }
        return annualIncome;
    }

    public String priorityData() {
        for (ExpenseBudgetDb e : dbManager.getExpense()) {
            if (String.valueOf(savingsDb.getExpRefKeyS()).equals(String.valueOf(e.getId()))) {
                priority = e.getExpensePriority();
            }
        }
        return priority;
    }

    public Double findNumberOfYears() {

        if (savingsAmountEntry.getText().toString().equals("")) {
            amount = 0.0;
        } else {
            try {
                amount = Double.valueOf(savingsAmountEntry.getText().toString());
            } catch (Exception e) {
                amount = general.extractingDollars(savingsAmountEntry);
            }
        }
        if (savingsGoalAmountEntry.getText().toString().equals("")) {
            goal = amount;
        } else {
            try {
                goal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
            } catch (Exception e2) {
                goal = general.extractingDollars(savingsGoalAmountEntry);
            }
        }
        if (goal < amount) {
            goal = amount;
        }
        if (savingsPercentEntry.getText().toString().equals("")) {
            rate = 0.01;
        } else {
            try {
                rate2 = Double.valueOf(savingsPercentEntry.getText().toString());
                rate = rate2 / 100;
            } catch (Exception e3) {
                rate2 = general.extractingPercents(savingsPercentEntry);
                rate = rate2 / 100;
            }
        }
        if (rate <= 0) {
            rate = .01;
            savingsIntAnnuallyRadioButton.setChecked(true);
        }
        if (savingsPaymentsEntry.getText().toString().equals("")) {
            payments = 0.01;
        } else {
            try {
                payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
            } catch (Exception e4) {
                payments = general.extractingDollars(savingsPaymentsEntry);
            }
        }
        if (payments <= 0) {
            payments = 0.01;
            savingsAnnuallyRadioButton.setChecked(true);
        }

        frequency = Double.valueOf(savingsFrequencyS);
        intFrequency = Double.valueOf(savingsIntFrequencyS);

        if (amount == 0 && payments == 0.01) {
            years = 0.0;
        } else if (goal.equals(amount)) {
            years = 0.0;
        } else {
            years = 0.0;
            do {
                years = years + .00274;
            }
            while (goal >= (amount * (Math.pow((1 + rate / intFrequency), intFrequency * years))) + ((((payments * frequency) - incomeData()) / 12) * (((Math.pow((1 + rate / intFrequency), intFrequency * years)) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency)));
        }
        return years;
    }

    public String calcSavingsDate() {

        savingsCal = Calendar.getInstance();
        numberOfDaysToSavingsGoal = (int) Math.round(findNumberOfYears() * 365);

        if ((numberOfDaysToSavingsGoal) <= 0) {
            savingsDate = getString(R.string.goal_achieved);
            savingsDateResultLabel.setVisibility(View.GONE);

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE) {
            savingsDate = getString(R.string.too_far);
            savingsDateResultLabel.setVisibility(View.GONE);

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = savingsDateS.format(savingsDateD);
            savingsDateResultLabel.setVisibility(View.VISIBLE);
        }

        return savingsDate;
    }

    TextWatcher onChangeSavingsAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsGoal = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsPayments = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavingsPercent = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult.setText(calcSavingsDate());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public class SavingsDbAdapter extends ArrayAdapter<SavingsDb> {

        private Context context;
        private List<SavingsDb> savings;

        private SavingsDbAdapter(
                Context context,
                List<SavingsDb> savings) {

            super(context, -1, savings);

            this.context = context;
            this.savings = savings;
        }

        public void updateSavings(List<SavingsDb> savings) {
            this.savings = savings;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return savings.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final SavingsViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_savings,
                        parent, false);

                holder = new SavingsViewHolder();
                holder.savingsListName = convertView.findViewById(R.id.savingsListName);
                holder.savingsListGoalAmount = convertView.findViewById(R.id.savingsListGoalAmount);
                holder.savingsListDate = convertView.findViewById(R.id.savingsListDate);
                holder.savingsListDateLabel = convertView.findViewById(R.id.savingsListDateLabel);
                holder.savingsListCurrentLabel = convertView.findViewById(R.id.savingsListCurrentLabel);
                holder.savingsListCurrentAmount = convertView.findViewById(R.id.savingsListCurrentAmount);
                holder.savingsDeleted = convertView.findViewById(R.id.deleteSavingsButton);
                holder.savingsEdit = convertView.findViewById(R.id.editSavingsButton);
                convertView.setTag(holder);

            } else {
                holder = (SavingsViewHolder) convertView.getTag();
            }

            //retrieve savingsName
            holder.savingsListName.setText(savings.get(position).getSavingsName());

            //retrieve savingsGoal and format as currency
            try {
                savingsGoalS = (String.valueOf(savings.get(position).getSavingsGoal()));
                if (savingsGoalS != null && !savingsGoalS.equals("")) {
                    savingsGoalD = Double.valueOf(savingsGoalS);
                } else {
                    savingsGoalD = 0.0;
                }
                savingsGoal2 = currencyFormat.format(savingsGoalD);
                holder.savingsListGoalAmount.setText(savingsGoal2);
            } catch (NumberFormatException e6) {
                holder.savingsListGoalAmount.setText(savingsGoal2);
            }

            //retrieve savingsDate
            savingsDate = savings.get(position).getSavingsDate();
            holder.savingsListDate.setText(savingsDate);
            if (savingsDate.contains("2")) {
                holder.savingsListDateLabel.setVisibility(View.VISIBLE);
            } else {
                holder.savingsListDateLabel.setVisibility(View.GONE);
            }

            //retrieve savingsAmount & format as currency
            try {
                savingsCurrentS = (String.valueOf(savings.get(position).getSavingsAmount()));
                if (savingsCurrentS != null && !savingsCurrentS.equals("")) {
                    savingsCurrentD = Double.valueOf(savingsCurrentS);
                } else {
                    savingsCurrentD = 0.0;
                }
                savingsCurrent2 = currencyFormat.format(savingsCurrentD);
                holder.savingsListCurrentAmount.setText(savingsCurrent2);
            } catch (NumberFormatException e7) {
                holder.savingsListCurrentAmount.setText(savingsCurrent2);
            }

            holder.savingsDeleted.setTag(savings.get(position));
            holder.savingsEdit.setTag(savings.get(position));

            //click on pencil icon
            holder.savingsEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_savings);
                    LayoutSavings.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getApplicationContext());

                    savingsNameEntry = findViewById(R.id.savingsNameEntry);
                    savingsAmountEntry = findViewById(R.id.savingsAmountEntry);
                    savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
                    savingsPaymentsEntry = findViewById(R.id.savingsPaymentsEntry);
                    savingsPercentEntry = findViewById(R.id.savingsPercentEntry);
                    savingsDateResult = findViewById(R.id.savingsDateResult);
                    savingsDateResultLabel = findViewById(R.id.savingsDateResultLabel);
                    savingsFrequencyLabel = findViewById(R.id.savingsFrequencyLabel);
                    savingsIntFrequencyLabel = findViewById(R.id.savingsIntFrequencyLabel);

                    savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);
                    savingsIntFrequencyRadioGroup = findViewById(R.id.savingsIntFrequencyRadioGroup);
                    savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
                    savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
                    savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
                    savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);
                    savingsIntMonthlyRadioButton = findViewById(R.id.savingsIntMonthlyRadioButton);
                    savingsIntAnnuallyRadioButton = findViewById(R.id.savingsIntAnnuallyRadioButton);

                    saveSavingsButton = findViewById(R.id.saveSavingsButton);
                    saveSavingsButton.setVisibility(View.GONE);
                    updateSavingsButton = findViewById(R.id.updateSavingsButton);
                    cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

                    savingsDb = (SavingsDb) holder.savingsEdit.getTag();

                    savingsNameEntryS = savingsDb.getSavingsName();
                    savingsAmountEntryD = savingsDb.getSavingsAmount();
                    savingsGoalEntryD = savingsDb.getSavingsGoal();
                    savingsPaymentsEntryD = savingsDb.getSavingsPayments();
                    savingsRateEntryD = savingsDb.getSavingsRate();

                    savingsNameEntry.setText(savingsNameEntryS);
                    savingsAmountS = currencyFormat.format(savingsAmountEntryD);
                    savingsAmountEntry.setText(savingsAmountS);
                    savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);

                    savingsGoalS2 = currencyFormat.format(savingsGoalEntryD);
                    savingsGoalAmountEntry.setText(savingsGoalS2);
                    savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);

                    savingsPaymentsS = currencyFormat.format(savingsPaymentsEntryD);
                    savingsPaymentsEntry.setText(savingsPaymentsS);
                    savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);

                    if (savingsPaymentsEntryD == 0) {
                        savingsFrequencyLabel.setVisibility(View.GONE);
                        savingsFrequencyRadioGroup.setVisibility(View.GONE);
                    } else {
                        savingsFrequencyLabel.setVisibility(View.VISIBLE);
                        savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
                    }

                    savingsPercentD2 = savingsRateEntryD / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    savingsPercentS = percentFormat.format(savingsPercentD2);
                    savingsPercentEntry.setText(savingsPercentS);
                    savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);

                    if (savingsRateEntryD == 0) {
                        savingsIntFrequencyLabel.setVisibility(View.GONE);
                        savingsIntFrequencyRadioGroup.setVisibility(View.GONE);
                    } else {
                        savingsIntFrequencyLabel.setVisibility(View.VISIBLE);
                        savingsIntFrequencyRadioGroup.setVisibility(View.VISIBLE);
                    }

                    //set radio button selections from data
                    if (savingsDb.getSavingsFrequency() == 52) {
                        savingsWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "52";
                    } else if (savingsDb.getSavingsFrequency() == 26) {
                        savingsBiWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "26";
                    } else if (savingsDb.getSavingsFrequency() == 12) {
                        savingsMonthlyRadioButton.setChecked(true);
                        savingsFrequencyS = "12";
                    } else if (savingsDb.getSavingsFrequency() == 1) {
                        savingsAnnuallyRadioButton.setChecked(true);
                        savingsFrequencyS = "1";
                    }

                    if (savingsDb.getSavingsIntFrequency() == 12) {
                        savingsIntMonthlyRadioButton.setChecked(true);
                        savingsIntFrequencyS = "12";
                    } else if (savingsDb.getSavingsIntFrequency() == 1) {
                        savingsIntAnnuallyRadioButton.setChecked(true);
                        savingsIntFrequencyS = "1";
                    }

                    savingsDateResult.setText(calcSavingsDate());

                    //update db if radio buttons changed
                    savingsFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.savingsWeeklyRadioButton:
                                    savingsFrequencyS = "52";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                                case R.id.savingsBiWeeklyRadioButton:
                                    savingsFrequencyS = "26";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                                case R.id.savingsMonthlyRadioButton:
                                    savingsFrequencyS = "12";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                                case R.id.savingsAnnuallyRadioButton:
                                    savingsFrequencyS = "1";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                            }
                        }
                    });

                    savingsIntFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.savingsIntMonthlyRadioButton:
                                    savingsIntFrequencyS = "12";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                                case R.id.savingsIntAnnuallyRadioButton:
                                    savingsIntFrequencyS = "1";
                                    savingsDateResult.setText(calcSavingsDate());
                                    break;
                            }
                        }
                    });

                    updateSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyS = String.valueOf(savingsDb.getExpRefKeyS());

                            if (savingsNameEntry.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                savingsNameEntryS = savingsNameEntry.getText().toString();
                                try {
                                    savingsAmountEntryD = Double.valueOf(savingsAmountEntry.getText().toString());
                                } catch (NumberFormatException e10) {
                                    savingsAmountEntryD = general.extractingDollars(savingsAmountEntry);
                                }
                                if (savingsAmountEntry.getText().toString().equals("")) {
                                    savingsAmountEntryD = 0.0;
                                }
                                try {
                                    savingsGoalEntryD = Double.valueOf(savingsGoalAmountEntry.getText().toString());
                                } catch (NumberFormatException e12) {
                                    savingsGoalEntryD = general.extractingDollars(savingsGoalAmountEntry);
                                }
                                if (savingsGoalAmountEntry.getText().toString().equals("")) {
                                    savingsGoalEntryD = 0.0;
                                }
                                try {
                                    savingsPaymentsEntryD = Double.valueOf(savingsPaymentsEntry.getText().toString());
                                } catch (NumberFormatException e11) {
                                    savingsPaymentsEntryD = general.extractingDollars(savingsPaymentsEntry);
                                }
                                if (savingsPaymentsEntry.getText().toString().equals("")) {
                                    savingsPaymentsEntryD = 0.0;
                                }
                                if (savingsPaymentsEntryD == 0) {
                                    savingsFrequencyLabel.setVisibility(View.GONE);
                                    savingsFrequencyRadioGroup.setVisibility(View.GONE);
                                } else {
                                    savingsFrequencyLabel.setVisibility(View.VISIBLE);
                                    savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
                                }
                                savingsFrequencyEntryD = Double.valueOf(savingsFrequencyS);
                                try {
                                    savingsRateEntryD = Double.valueOf(savingsPercentEntry.getText().toString());
                                } catch (NumberFormatException e13) {
                                    savingsRateEntryD = general.extractingPercents(savingsPercentEntry);
                                }
                                if (savingsPercentEntry.getText().toString().equals("")) {
                                    savingsRateEntryD = 0.0;
                                }
                                if (savingsRateEntryD == 0) {
                                    savingsIntFrequencyLabel.setVisibility(View.GONE);
                                    savingsIntFrequencyRadioGroup.setVisibility(View.GONE);
                                } else {
                                    savingsIntFrequencyLabel.setVisibility(View.VISIBLE);
                                    savingsIntFrequencyRadioGroup.setVisibility(View.VISIBLE);
                                }
                                savingsIntFrequencyEntryD = Double.valueOf(savingsIntFrequencyS);

                                savingsDb.setSavingsName(savingsNameEntryS);
                                savingsDb.setSavingsAmount(savingsAmountEntryD);
                                savingsDb.setSavingsGoal(savingsGoalEntryD);
                                savingsDb.setSavingsPayments(savingsPaymentsEntryD);
                                savingsDb.setSavingsFrequency(savingsFrequencyEntryD);
                                savingsDb.setSavingsRate(savingsRateEntryD);
                                savingsDb.setSavingsIntFrequency(savingsIntFrequencyEntryD);
                                savingsDb.setSavingsDate(calcSavingsDate());

                                dbHelper = new DbHelper(getContext());
                                expenseDb = dbHelper.getWritableDatabase();

                                String[] args = new String[]{expRefKeyS};
                                String[] args2 = new String[]{String.valueOf(savingsDb.getIncRefKeyS())};
                                values = new ContentValues();
                                values2 = new ContentValues();
                                values3 = new ContentValues();
                                values4 = new ContentValues();

                                values.put(DbHelper.EXPENSENAME, savingsNameEntryS);
                                values2.put(DbHelper.INCOMENAME, savingsNameEntryS);
                                values3.put(DbHelper.MONEYOUTCAT, savingsNameEntryS);
                                values4.put(DbHelper.MONEYINCAT, savingsNameEntryS);

                                values.put(DbHelper.EXPENSEAMOUNT, savingsPaymentsEntryD);
                                values.put(DbHelper.EXPENSEFREQUENCY, savingsFrequencyEntryD);
                                expenseAnnualAmount = savingsPaymentsEntryD * savingsFrequencyEntryD;
                                values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                                if (priorityData().equals("A")) {
                                    values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                                } else if (priorityData().equals("B")) {
                                    values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                                }

                                expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args2);
                                expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values4, DbHelper.INCREFKEYMI + "=?", args2);

                                expenseDb.close();

                                dbManager.updateSavings(savingsDb);
                                savingsAdapter.updateSavings(dbManager.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                backToSavings();
                                savingsHeaderText();
                            }
                        }
                    });

                    cancelSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToSavings();
                        }
                    });
                }
            });

            //click on trash can icon
            holder.savingsDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    savingsDb = (SavingsDb) holder.savingsDeleted.getTag();

                    deleteSavingsWarningText.setVisibility(View.VISIBLE);
                    cancelDeleteSavingsButton.setVisibility(View.VISIBLE);
                    continueDeleteSavingsButton.setVisibility(View.VISIBLE);
                    savingsListView.setVisibility(View.GONE);

                    cancelDeleteSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToSavings();
                        }
                    });

                    continueDeleteSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleteSavingsWarningText.setVisibility(View.GONE);
                            cancelDeleteSavingsButton.setVisibility(View.GONE);
                            continueDeleteSavingsButton.setVisibility(View.GONE);

                            dbHelper = new DbHelper(getContext());
                            expenseDb = dbHelper.getWritableDatabase();

                            try {
                                String[] args = new String[]{String.valueOf(savingsDb.getExpRefKeyS())};
                                expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            try {
                                String[] args2 = new String[]{String.valueOf(savingsDb.getIncRefKeyS())};
                                expenseDb.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            expenseDb.close();

                            dbManager.deleteSavings(savingsDb);
                            savingsAdapter.updateSavings(dbManager.getSavings());
                            notifyDataSetChanged();
                            if (savingsAdapter.getCount() == 0) {
                                emptysavingsText.setVisibility(View.VISIBLE);
                                emptysavingsText2.setVisibility(View.VISIBLE);
                            } else {
                                emptysavingsText.setVisibility(View.GONE);
                                emptysavingsText2.setVisibility(View.GONE);
                            }

                            savingsHeaderText();
                            backToSavings();
                        }
                    });
                }
            });


            return convertView;
        }
    }

    private static class SavingsViewHolder {
        public TextView savingsListName;
        public TextView savingsListGoalAmount;
        public TextView savingsListDate;
        public TextView savingsListDateLabel;
        public TextView savingsListCurrentLabel;
        public TextView savingsListCurrentAmount;
        ImageButton savingsDeleted;
        ImageButton savingsEdit;
    }

    View.OnClickListener onClickAddSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewSavings = new Intent(LayoutSavings.this, AddSavings.class);
            addNewSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewSavings);
        }
    };
}
