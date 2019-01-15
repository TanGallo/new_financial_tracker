package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutSavings extends MainNavigation {

    Button saveSavingsButton, updateSavingsButton, cancelSavingsButton, doneSavingsSetUpButton;
    Calendar savingsCal;
    Cursor expenseCursor;
    Date savingsDateD;
    DbHelper dbHelper;
    DbManager dbManager;
    Double amount = 0.0, expenseAnnualAmount = 0.0, totalSavings = 0.0, totalSavingsD = 0.0, savingsGoalD = 0.0, savingsCurrentD = 0.0, rate = 0.0,
            a = 0.0, payments = 0.0, frequency = 0.0, balanceAmount = 0.0, goal = 0.0, savingsAmountD = 0.0, savingsPaymentsD = 0.0,
            savingsGoalD2 = 0.0, savingsPercentD = 0.0, savingsPercentD2 = 0.0, rate2 = 0.0, intFrequency = 0.0, years = 0.0;
    EditText savingsNameEntry, savingsAmountEntry, savingsPercentEntry, savingsPaymentsEntry, savingsGoalAmountEntry;
    FloatingActionButton addSavingsButton;
    General general;
    int debtsDone = 0, savingsDone = 0, budgetDone = 0, balanceDone = 0, tourDone = 0;
    Integer numberOfDaysToSavingsGoal = 0;
    Intent addNewSavings, backToSavingsScreen, backToSavingsScreen2, backToSetUp;
    ListView savingsListView;
    long id;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton savingsWeeklyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton, savingsAnnuallyRadioButton, savingsIntMonthlyRadioButton, savingsIntAnnuallyRadioButton;
    RadioGroup savingsFrequencyRadioGroup, savingsIntFrequencyRadioGroup;
    SavingsDbAdapter savingsAdapter;
    SavingsDb savingsDb;
    SetUpDb setUpDb;
    SimpleDateFormat savingsDateS;
    SQLiteDatabase expenseDb;
    String totalSavings2 = null, totalSavingsS = null, savingsGoalS = null, savingsGoal2 = null, savingsCurrentS = null, savingsCurrent2 = null,
            savingsDate = null, savingsFrequencyS = null, savingsAmountS = null, savingsPaymentsS = null, savingsGoalS2 = null, savingsPercentS = null, savingsIntFrequencyS = null;
    TextView totalSavedText, savingsListName, savingsListGoalAmount, savingsListDate, savingsListCurrentAmount, savingsDateResult, emptysavingsText,
            emptysavingsText2, savingsFrequencyLabel, savingsIntFrequencyLabel;

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

        savingsListView = findViewById(R.id.savingsListView);
        addSavingsButton = findViewById(R.id.addSavingsButton);
        addSavingsButton.setOnClickListener(onClickAddSavingsButton);

        doneSavingsSetUpButton = findViewById(R.id.doneSavingsSetUpButton);
        doneSavingsSetUpButton.setOnClickListener(onClickDoneSavingsSetUpButton);

        if (dbManager.savingsSetUpCheck() > 0) {
            doneSavingsSetUpButton.setVisibility(View.GONE);
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

            Toast toast = Toast.makeText(getApplicationContext(), "You can edit this list by clicking SAVINGS on the menu", Toast.LENGTH_LONG);
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
            payments = 0.0;
        } else {
            try {
                payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
            } catch (Exception e4) {
                payments = general.extractingDollars(savingsPaymentsEntry);
            }
        }
        if (payments <= 0) {
            payments = 0.1;
            savingsAnnuallyRadioButton.setChecked(true);
        }

        frequency = Double.valueOf(savingsFrequencyS);
        intFrequency = Double.valueOf(savingsIntFrequencyS);

        if (amount == 0 && payments == 0.1) {
            years = 0.0;
        } else if (goal <= amount) {
            years = 0.0;
        } else {
            years = .00274;
        }

        if(years == 0.0) {
            years = 0.0;
        } else {
            do {
                years++;
            }
            while (((amount * Math.pow((1 + rate / intFrequency), (intFrequency * years))) + (((payments * frequency) / 12) * ((Math.pow((1 + rate / intFrequency), (intFrequency * years)) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency))) <= goal);

        }
        return years;
    }

    public String calcSavingsDate() {

        savingsCal = Calendar.getInstance();

            numberOfDaysToSavingsGoal = (int) Math.round(findNumberOfYears() * 365);

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
            if(savingsPaymentsEntry.getText().toString().equals("0") || general.extractingDollars(savingsPaymentsEntry) == 0) {
                savingsFrequencyLabel.setVisibility(View.GONE);
                savingsFrequencyRadioGroup.setVisibility(View.GONE);
                savingsAnnuallyRadioButton.setChecked(true);
            } else {
                savingsFrequencyLabel.setVisibility(View.VISIBLE);
                savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
            }

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
            if(savingsPercentEntry.getText().toString().equals("0") || general.extractingPercents(savingsPercentEntry) == 0) {
                savingsIntFrequencyLabel.setVisibility(View.GONE);
                savingsIntFrequencyRadioGroup.setVisibility(View.GONE);
                savingsIntAnnuallyRadioButton.setChecked(true);
            } else {
                savingsIntFrequencyLabel.setVisibility(View.VISIBLE);
                savingsIntFrequencyRadioGroup.setVisibility(View.VISIBLE);
            }

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
            holder.savingsListDate.setText(savings.get(position).getSavingsDate());

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

                    savingsNameEntry.setText(savingsDb.getSavingsName());

                    savingsAmountD = savingsDb.getSavingsAmount();
                    savingsAmountS = currencyFormat.format(savingsAmountD);
                    savingsAmountEntry.setText(savingsAmountS);
                    savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);

                    savingsGoalD2 = savingsDb.getSavingsGoal();
                    savingsGoalS2 = currencyFormat.format(savingsGoalD2);
                    savingsGoalAmountEntry.setText(savingsGoalS2);
                    savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);

                    savingsPaymentsD = savingsDb.getSavingsPayments();
                    savingsPaymentsS = currencyFormat.format(savingsPaymentsD);
                    savingsPaymentsEntry.setText(savingsPaymentsS);
                    savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);

                    if(savingsDb.getSavingsPayments() == 0) {
                        savingsFrequencyLabel.setVisibility(View.GONE);
                        savingsFrequencyRadioGroup.setVisibility(View.GONE);
                    } else {
                        savingsFrequencyLabel.setVisibility(View.VISIBLE);
                        savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
                    }

                    savingsPercentD = savingsDb.getSavingsRate();
                    savingsPercentD2 = savingsPercentD / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    savingsPercentS = percentFormat.format(savingsPercentD2);
                    savingsPercentEntry.setText(savingsPercentS);
                    savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);

                    if(savingsDb.getSavingsRate() == 0) {
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

                    savingsDateResult.setText(savingsDb.getSavingsDate());

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

                            dbHelper = new DbHelper(getContext());
                            expenseDb = dbHelper.getWritableDatabase();

                            String[] args = new String[]{String.valueOf(savingsDb.getExpRefKeyS())};
                            ContentValues values = new ContentValues();

                            values.put(DbHelper.EXPENSENAME, savingsNameEntry.getText().toString());
                            try {
                                values.put(DbHelper.EXPENSEAMOUNT, Double.valueOf(savingsPaymentsEntry.getText().toString()));
                            } catch (NumberFormatException e8) {
                                values.put(DbHelper.EXPENSEAMOUNT, general.extractingDollars(savingsPaymentsEntry));
                            }
                            values.put(DbHelper.EXPENSEFREQUENCY, Double.valueOf(savingsFrequencyS));

                            try {
                                expenseAnnualAmount = Double.valueOf(savingsPaymentsEntry.getText().toString()) * Double.valueOf(savingsFrequencyS);
                            } catch (NumberFormatException e9) {
                                expenseAnnualAmount = general.extractingDollars(savingsPaymentsEntry) * Double.valueOf(savingsFrequencyS);
                            }

                            values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                            values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);

                            expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);

                            expenseDb.close();

                            savingsDb.setSavingsName(savingsNameEntry.getText().toString());

                            try {
                                savingsDb.setSavingsAmount(Double.valueOf(savingsAmountEntry.getText().toString()));
                            } catch (NumberFormatException e10) {
                                savingsDb.setSavingsAmount(general.extractingDollars(savingsAmountEntry));
                            }

                            try {
                                savingsDb.setSavingsGoal(Double.valueOf(savingsGoalAmountEntry.getText().toString()));
                            } catch (NumberFormatException e12) {
                                savingsDb.setSavingsGoal(general.extractingDollars(savingsGoalAmountEntry));
                            }

                            try {
                                savingsDb.setSavingsPayments(Double.valueOf(savingsPaymentsEntry.getText().toString()));
                            } catch (NumberFormatException e11) {
                                savingsDb.setSavingsPayments(general.extractingDollars(savingsPaymentsEntry));
                            }

                            if(savingsDb.getSavingsPayments() == 0) {
                                savingsFrequencyLabel.setVisibility(View.GONE);
                                savingsFrequencyRadioGroup.setVisibility(View.GONE);
                            } else {
                                savingsFrequencyLabel.setVisibility(View.VISIBLE);
                                savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
                            }

                            try {
                                savingsDb.setSavingsRate(Double.valueOf(savingsPercentEntry.getText().toString()));
                            } catch (NumberFormatException e13) {
                                savingsDb.setSavingsRate(general.extractingPercents(savingsPercentEntry));
                            }

                            if(savingsDb.getSavingsRate() == 0) {
                                savingsIntFrequencyLabel.setVisibility(View.GONE);
                                savingsIntFrequencyRadioGroup.setVisibility(View.GONE);
                            } else {
                                savingsIntFrequencyLabel.setVisibility(View.VISIBLE);
                                savingsIntFrequencyRadioGroup.setVisibility(View.VISIBLE);
                            }

                            savingsDb.setSavingsFrequency(Double.valueOf(savingsFrequencyS));
                            savingsDb.setSavingsIntFrequency(Double.valueOf(savingsIntFrequencyS));

                            savingsDb.setSavingsDate(calcSavingsDate());

                            dbManager.updateSavings(savingsDb);
                            savingsAdapter.updateSavings(dbManager.getSavings());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            backToSavingsScreen2 = new Intent(LayoutSavings.this, LayoutSavings.class);
                            backToSavingsScreen2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSavingsScreen2);

                            savingsHeaderText();
                        }
                    });

                    cancelSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToSavingsScreen = new Intent(LayoutSavings.this, LayoutSavings.class);
                            backToSavingsScreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToSavingsScreen);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.savingsDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    savingsDb = (SavingsDb) holder.savingsDeleted.getTag();
                    dbManager.deleteSavings(savingsDb);

                    dbHelper = new DbHelper(getContext());
                    expenseDb = dbHelper.getWritableDatabase();

                    try {
                        String[] args = new String[]{String.valueOf(savingsDb.getExpRefKeyS())};
                        expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    expenseDb.close();

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
                }
            });

            return convertView;
        }
    }

    private static class SavingsViewHolder {
        public TextView savingsListName;
        public TextView savingsListGoalAmount;
        public TextView savingsListDate;
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
