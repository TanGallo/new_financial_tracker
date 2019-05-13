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
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutSavings extends MainNavigation {

    Button addNewSavingsButton, cancelDeleteSavingsButton, continueDeleteSavingsButton, cancelSavingsButton, doneSavingsSetUpButton, saveSavingsButton,
            savingsSetUpTimeButton, savingsSetUpHelpButton, updateSavingsButton;
    ContentValues values, values2, values3, values4;
    DbHelper dbHelper;
    DbManager dbManager;
    Double savingsAnnualIncomeb = 0.0, savingsAmount = 0.0, savingsAmountb = 0.0, balanceAmount = 0.0, expenseAnnualAmount = 0.0, savingsGoalb = 0.0,
            savingsAnnualIncome = 0.0, savingsFrequency = 0.0, savingsGoal = 0.0, savingsPaymentsb = 0.0, savingsPayments = 0.0,
            currentSavingsRate = 0.0, savingsRate = 0.0, savingsCurrentD = 0.0, savingsGoalD = 0.0, savingsPercentD2 = 0.0, totalSavings = 0.0,
            totalSavingsD = 0.0, years2 = 0.0;
    EditText savingsAmountEntry, savingsGoalAmountEntry, savingsNameEntry, savingsPaymentsEntry, savingsPercentEntry;
    FloatingActionButton addSavingsButton;
    General general;
    int balanceDone = 0, budgetDone = 0, debtsDone = 0, incomeDone = 0, savingsDone = 0, tourDone = 0;
    Intent addNewSavings, backToSavingsScreen, backToSetUp;
    LinearLayout toastLayout;
    ListView savingsListView;
    long id, incRefKeyS;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton savingsAnnuallyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton, savingsWeeklyRadioButton;
    RadioGroup savingsFrequencyRadioGroup;
    SavingsDbAdapter savingsAdapter;
    SavingsDb savingsDb;
    SetUpDb setUpDb;
    SQLiteDatabase expenseDb;
    String expRefKeyS = null, savingsName = null, savingsNameb = null, priority = null, savingsAmountS = null, savingsCurrent2 = null, savingsCurrentS = null,
            savingsDate = null, savingsDate2 = null, savingsFrequencyS = null, savingsGoal2 = null, savingsGoalS = null, savingsGoalS2 = null,
            savingsPaymentsS = null, savingsPercentS = null, totalSavings2 = null, totalSavingsS = null;
    TextView deleteSavingsWarningText, emptysavingsText, emptysavingsText2, emptySavingsText3, savingsDateResult, savingsDateResultLabel, savingsFrequencyLabel,
            savingsSetUpNoTime, savingsSetUpNoTime2, savingsSetUpNeedHelp, savingsSetUpNeedHelp2, totalSavedText, tv;
    Toast toast;

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

        menuConfig();

        general = new General();
        dbManager = new DbManager(this);

        totalSavedText = findViewById(R.id.totalSavedText);
        emptysavingsText = findViewById(R.id.emptySavingsText);
        emptysavingsText2 = findViewById(R.id.emptySavingsText2);
        emptySavingsText3 = findViewById(R.id.emptySavingsText3);
        savingsSetUpNoTime = findViewById(R.id.savingsSetUpNoTime);
        savingsSetUpNoTime.setOnClickListener(onClickNoTimeSavings);
        savingsSetUpNoTime2 = findViewById(R.id.savingsSetUpNoTime2);
        savingsSetUpNoTime2.setVisibility(View.GONE);
        savingsSetUpTimeButton = findViewById(R.id.savingsSetUpTimeButton);
        savingsSetUpTimeButton.setVisibility(View.GONE);
        savingsSetUpNeedHelp = findViewById(R.id.savingsSetUpNeedHelp);
        savingsSetUpNeedHelp.setOnClickListener(onClickNeedHelpSavings);
        savingsSetUpNeedHelp2 = findViewById(R.id.savingsSetUpNeedHelp2);
        savingsSetUpNeedHelp2.setVisibility(View.GONE);
        savingsSetUpHelpButton = findViewById(R.id.savingsSetUpHelpButton);
        savingsSetUpHelpButton.setVisibility(View.GONE);
        deleteSavingsWarningText = findViewById(R.id.deleteSavingsWarningText);
        deleteSavingsWarningText.setVisibility(View.GONE);
        cancelDeleteSavingsButton = findViewById(R.id.cancelDeleteSavingsButton);
        cancelDeleteSavingsButton.setVisibility(View.GONE);
        continueDeleteSavingsButton = findViewById(R.id.continueDeleteSavingsButton);
        continueDeleteSavingsButton.setVisibility(View.GONE);

        savingsListView = findViewById(R.id.savingsListView);
        addNewSavingsButton = findViewById(R.id.addNewSavingsButton);
        addNewSavingsButton.setOnClickListener(onClickAddNewSavingsButton);
        addSavingsButton = findViewById(R.id.addSavingsButton);
        addSavingsButton.setVisibility(View.GONE);

        doneSavingsSetUpButton = findViewById(R.id.doneSavingsSetUpButton);
        doneSavingsSetUpButton.setOnClickListener(onClickDoneSavingsSetUpButton);

        if (dbManager.savingsSetUpCheck() > 0) {
            addNewSavingsButton.setVisibility(View.GONE);
            addSavingsButton.setVisibility(View.VISIBLE);
            addSavingsButton.setOnClickListener(onClickAddSavingsButton);
            doneSavingsSetUpButton.setVisibility(View.GONE);
            emptySavingsText3.setVisibility(View.GONE);
            savingsSetUpNoTime.setVisibility(View.GONE);
            savingsSetUpNoTime2.setVisibility(View.GONE);
            savingsSetUpTimeButton.setVisibility(View.GONE);
            savingsSetUpNeedHelp.setVisibility(View.GONE);
            savingsSetUpNeedHelp2.setVisibility(View.GONE);
            savingsSetUpHelpButton.setVisibility(View.GONE);
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

            setUpDb = new SetUpDb(incomeDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            toast = Toast.makeText(getApplicationContext(), R.string.edit_savings_message, Toast.LENGTH_LONG);
            toastLayout = (LinearLayout) toast.getView();
            tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutSavings.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    View.OnClickListener onClickNoTimeSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savingsSetUpNoTime2.setVisibility(View.VISIBLE);
            savingsSetUpTimeButton.setVisibility(View.VISIBLE);
            savingsSetUpTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsSetUpNoTime2.setVisibility(View.GONE);
                    savingsSetUpTimeButton.setVisibility(View.GONE);
                }
            });
        }
    };

    View.OnClickListener onClickNeedHelpSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savingsSetUpNeedHelp2.setVisibility(View.VISIBLE);
            savingsSetUpHelpButton.setVisibility(View.VISIBLE);
            savingsSetUpHelpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsSetUpNeedHelp2.setVisibility(View.GONE);
                    savingsSetUpHelpButton.setVisibility(View.GONE);
                }
            });
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

    public String priorityData() {
        for (ExpenseBudgetDb e : dbManager.getExpense()) {
            if (String.valueOf(savingsDb.getExpRefKeyS()).equals(String.valueOf(e.getId()))) {
                priority = e.getExpensePriority();
            }
        }
        return priority;
    }

    TextWatcher onChangeSavingsAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savingsDateResult();
            savingsDateResult.setText(savingsDate2);
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
            savingsDateResult();
            savingsDateResult.setText(savingsDate2);
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
            savingsDateResult();
            savingsDateResult.setText(savingsDate2);
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
            savingsDateResult();
            savingsDateResult.setText(savingsDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void savingsDateResult() {
        allSavingsData();
        savingsDate2 = general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (savingsDate2.equals(getString(R.string.goal_achieved))) {
            savingsDateResultLabel.setVisibility(View.GONE);
            savingsDateResult.setTextColor(Color.parseColor("#03ac13"));
        } else if(savingsDate2.equals(getString(R.string.too_far))) {
            savingsDateResultLabel.setVisibility(View.GONE);
            savingsDateResult.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            savingsDateResultLabel.setVisibility(View.VISIBLE);
            savingsDateResult.setTextColor(Color.parseColor("#303F9F"));
            savingsDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
        }
    }

    public void allSavingsData() {
        if (savingsNameEntry.getText().toString().equals("")) {
            savingsName = "null";
        } else {
            savingsName = savingsNameEntry.getText().toString();
        }
        savingsAmount = general.extractingDouble(savingsAmountEntry);
        savingsGoal = general.extractingDouble(savingsGoalAmountEntry);
        currentSavingsRate = general.extractingPercent(savingsPercentEntry);
        savingsRate = currentSavingsRate / 100;
        savingsPayments = general.extractingDouble(savingsPaymentsEntry);
        savingsAnnualIncome = savingsAnnualIncomeb;

        if (savingsPayments == 0) {
            savingsFrequency = 1.0;
            savingsAnnuallyRadioButton.setChecked(true);
        } else {
            try {
                savingsFrequency = Double.valueOf(savingsFrequencyS);
            } catch (NullPointerException e) {
                savingsFrequency = 1.0;
            }
        }
    }

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
            if (savingsDate.equals(getString(R.string.goal_achieved))) {
                holder.savingsListDate.setTextColor(Color.parseColor("#03ac13"));
            } else if(savingsDate.equals(getString(R.string.too_far))) {
                holder.savingsListDate.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                holder.savingsListDate.setTextColor(Color.parseColor("#303F9F"));
                holder.savingsListDateLabel.setTextColor(Color.parseColor("#303F9F"));
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

            incRefKeyS = savings.get(position).getIncRefKeyS();

            /*for (IncomeBudgetDb i : dbManager.getIncomes()) {
                if (String.valueOf(incRefKeyS).equals(String.valueOf(i.getId()))) {
                    savingsAnnualIncomeb = i.getIncomeAnnualAmount();
                }
            }*/

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

                    savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);
                    savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
                    savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
                    savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
                    savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);

                    saveSavingsButton = findViewById(R.id.saveSavingsButton);
                    saveSavingsButton.setVisibility(View.GONE);
                    updateSavingsButton = findViewById(R.id.updateSavingsButton);
                    cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

                    savingsDb = (SavingsDb) holder.savingsEdit.getTag();

                    savingsNameb = savingsDb.getSavingsName();
                    savingsNameEntry.setText(savingsNameb);

                    savingsAmount = savingsDb.getSavingsAmount();
                    savingsAmountS = currencyFormat.format(savingsAmount);
                    savingsAmountEntry.setText(savingsAmountS);
                    savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);

                    savingsGoal = savingsDb.getSavingsGoal();
                    savingsGoalS2 = currencyFormat.format(savingsGoal);
                    savingsGoalAmountEntry.setText(savingsGoalS2);
                    savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);

                    savingsPayments = savingsDb.getSavingsPayments();
                    savingsPaymentsS = currencyFormat.format(savingsPayments);
                    savingsPaymentsEntry.setText(savingsPaymentsS);
                    savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);

                    savingsRate = savingsDb.getSavingsRate();
                    savingsPercentD2 = savingsRate / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    savingsPercentS = percentFormat.format(savingsRate);
                    savingsPercentEntry.setText(savingsPercentS);
                    savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);

                    savingsFrequency = savingsDb.getSavingsFrequency();

                    savingsAnnualIncomeb = savingsDb.getSavingsAnnualIncome();

                    savingsDate = savingsDb.getSavingsDate();
                    savingsDateResult.setText(savingsDate);
                    if (savingsDate2.equals(getString(R.string.goal_achieved))) {
                        savingsDateResultLabel.setVisibility(View.GONE);
                        savingsDateResult.setTextColor(Color.parseColor("#03ac13"));
                    } else if(savingsDate2.equals(getString(R.string.too_far))) {
                        savingsDateResultLabel.setVisibility(View.GONE);
                        savingsDateResult.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        savingsDateResultLabel.setVisibility(View.VISIBLE);
                        savingsDateResult.setTextColor(Color.parseColor("#303F9F"));
                        savingsDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
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

                    //update db if radio buttons changed
                    savingsFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.savingsWeeklyRadioButton:
                                    savingsFrequencyS = "52";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsBiWeeklyRadioButton:
                                    savingsFrequencyS = "26";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsMonthlyRadioButton:
                                    savingsFrequencyS = "12";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsAnnuallyRadioButton:
                                    savingsFrequencyS = "1";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                            }
                        }
                    });

                    updateSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyS = String.valueOf(savingsDb.getExpRefKeyS());

                            savingsDateResult();

                            if (savingsName != "null") {

                                savingsDb.setSavingsName(savingsName);
                                savingsDb.setSavingsAmount(savingsAmount);
                                savingsDb.setSavingsGoal(savingsGoal);
                                savingsDb.setSavingsPayments(savingsPayments);
                                savingsDb.setSavingsFrequency(savingsFrequency);
                                savingsDb.setSavingsRate(savingsRate);
                                savingsDb.setSavingsDate(savingsDate2);

                                dbHelper = new DbHelper(getContext());
                                expenseDb = dbHelper.getWritableDatabase();

                                String[] args = new String[]{expRefKeyS};
                                String[] args2 = new String[]{String.valueOf(savingsDb.getIncRefKeyS())};
                                values = new ContentValues();
                                values2 = new ContentValues();
                                values3 = new ContentValues();
                                values4 = new ContentValues();

                                values.put(DbHelper.EXPENSENAME, savingsName);
                                values.put(DbHelper.EXPENSEAMOUNT, savingsPayments);
                                values.put(DbHelper.EXPENSEFREQUENCY, savingsFrequency);
                                expenseAnnualAmount = savingsPayments * savingsFrequency;
                                values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                                if (priorityData().equals("A")) {
                                    values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                                } else if (priorityData().equals("B")) {
                                    values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                                }
                                values2.put(DbHelper.INCOMENAME, savingsName);
                                values3.put(DbHelper.MONEYOUTCAT, savingsName);
                                values4.put(DbHelper.MONEYINCAT, savingsName);

                                try {
                                    expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                    expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args2);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                    expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values4, DbHelper.INCREFKEYMI + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException| SQLException e) {
                                    e.printStackTrace();
                                }

                                expenseDb.close();

                                dbManager.updateSavings(savingsDb);
                                savingsAdapter.updateSavings(dbManager.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                backToSavings();
                                savingsHeaderText();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
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

    View.OnClickListener onClickAddNewSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewSavings = new Intent(LayoutSavings.this, AddSavings.class);
            addNewSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewSavings);
        }
    };

    View.OnClickListener onClickAddSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addNewSavings = new Intent(LayoutSavings.this, AddSavings.class);
            addNewSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addNewSavings);
        }
    };
}
