package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutBudget extends MainNavigation {

    boolean foundDebtIdExp = false, foundDebtIdInc = false, foundSavingsIdExp = false, foundSavingsIdInc = false;
    Button budgetAddExpenseButton, budgetAddIncomeButton, budgetCancelExpenseButton, budgetCancelIncomeButton, budgetSetUpTimeButton,
            budgetSetUpHelpButton, budgetUpdateExpenseButton, budgetUpdateIncomeButton, cancelButton, doneBudgetSetUpButton, okButton, ok2Button;
    ContentValues debtValues, debtValues2, debtValues3, debtValues4, expValues, incValues, moneyInValues, moneyOutValues, moneyOutValues2, savingsValues, savingsValues2,
            savingsValues3, savingsValues4;
    DbHelper dbHelper, dbHelper2;
    DbManager dbManager;
    Double amountEntry = 0.0, annualAmount = 0.0, annualIncome = 0.0, balanceAmount = 0.0, budgetExpenseAmountD = 0.0, budgetIncomeAmountD = 0.0,
            savingsFrequency = 0.0, savingsPayments = 0.0, currentSavingsRate = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0,
            debtPayments = 0.0, debtRate = 0.0, expenseAnnualAmountD = 0.0, frequencyEntry = 0.0, incomeAnnualAmountD = 0.0, incomeAvailableD = 0.0,
            incomeAvailableN = 0.0, savingsAmount = 0.0, savingsAnnualIncome = 0.0, savingsGoal = 0.0, savingsRate = 0.0,
            totalExpensesD = 0.0, totalExpensesR = 0.0, totalIncomeD = 0.0, totalIncomeR = 0.0;
    EditText budgetExpenseAmount, budgetExpenseCategory, budgetIncomeAmount, budgetIncomeCategory;
    ExpenseBudgetDb expenseBudgetDb;
    ExpenseDbAdapter expenseAdapter;
    General general;
    FloatingActionButton budgetExpensePlusButton, budgetIncomePlusButton;
    IncomeBudgetDb incomeBudgetDb;
    IncomeDbAdapter incomeAdapter;
    int balanceDone = 0, budgetDone = 0, debtsDone = 0, savingsDone = 0, tourDone = 0;
    Intent backToBudget, backToSetUp, expensePlusButton, incomePlusButton;
    LinearLayout toastLayout;
    ListView budgetExpensesDetails, budgetIncomeDetails;
    long id;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RadioButton budgetExpenseARadioButton, budgetExpenseAnnuallyRadioButton, budgetExpenseBRadioButton, budgetExpenseBiAnnuallyRadioButton,
            budgetExpenseBiMonthlyRadioButton, budgetExpenseBiWeeklyRadioButton, budgetExpenseMonthlyRadioButton, budgetExpenseNoRadioButton,
            budgetExpenseWeeklyRadioButton, budgetExpenseYesRadioButton, budgetIncomeAnnuallyRadioButton, budgetIncomeBiAnnuallyRadioButton,
            budgetIncomeBiMonthlyRadioButton, budgetIncomeBiWeeklyRadioButton, budgetIncomeMonthlyRadioButton, budgetIncomeWeeklyRadioButton;
    RadioGroup budgetExpenseABRadioGroup, budgetExpenseFrequencyRadioGroup, budgetIncomeFrequencyRadioGroup, budgetExpenseReminderRadioGroup;
    SetUpDb setUpDb;
    SQLiteDatabase db, db2;
    String budgetExpenseAmountS = null, budgetIncomeAmountS = null, debtName = null, expDebtId = null, expSavingsId = null, expRefKeyD = null, expenseAnnualAmount2 = null,
            expenseAnnualAmountS = null, expenseFrequencyS = null, expenseId = null, expensePriorityS = null, expenseWeeklyS = null, incomeAnnualAmount2 = null,
            incomeAnnualAmountS = null, incRefKeyD = null, incDebtId = null, incSavingsId = null, incomeAvailable2 = null, incomeAvailableN2 = null,
            incomeFrequencyS = null, incomeId = null, nameEntryInc = null, nameEntryExp = null, priorityEntryExp = null, totalExpenses2 = null,
            totalExpensesS = null, totalIncome2 = null, totalIncomeS = null, weeklyEntry = null;
    TextView budgetExpensesTotalText, budgetIncomeTotalText, budgetOopsAmountText, budgetOopsText, budgetSetUpNoTime, budgetSetUpNoTime2,
            budgetSetUpNeedHelp, budgetSetUpNeedHelp2, deleteExpText, emptyBudgetText, headerLabel2, incomeAvailable, noSpendingReportText,
            weeklyGuidanceLabel, tv;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_budget);
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

        budgetIncomeTotalText = findViewById(R.id.budgetIncomeTotalText);
        budgetExpensesTotalText = findViewById(R.id.budgetExpensesTotalText);
        headerLabel2 = findViewById(R.id.headerLabel2);
        incomeAvailable = findViewById(R.id.incomeAvailable);
        budgetOopsText = findViewById(R.id.budgetOopsText);
        budgetOopsText.setVisibility(View.GONE);
        budgetOopsAmountText = findViewById(R.id.budgetOopsAmountText);
        budgetOopsAmountText.setVisibility(View.GONE);
        emptyBudgetText = findViewById(R.id.emptyBudgetText);
        budgetSetUpNoTime = findViewById(R.id.budgetSetUpNoTime);
        budgetSetUpNoTime.setOnClickListener(onClickNoTimeBudget);
        budgetSetUpNoTime2 = findViewById(R.id.budgetSetUpNoTime2);
        budgetSetUpNoTime2.setVisibility(View.GONE);
        budgetSetUpTimeButton = findViewById(R.id.budgetSetUpTimeButton);
        budgetSetUpTimeButton.setVisibility(View.GONE);
        budgetSetUpNeedHelp = findViewById(R.id.budgetSetUpNeedHelp);
        budgetSetUpNeedHelp.setOnClickListener(onClickNeedHelpBudget);
        budgetSetUpNeedHelp2 = findViewById(R.id.budgetSetUpNeedHelp2);
        budgetSetUpNeedHelp2.setVisibility(View.GONE);
        budgetSetUpHelpButton = findViewById(R.id.budgetSetUpHelpButton);
        budgetSetUpHelpButton.setVisibility(View.GONE);
        deleteExpText = findViewById(R.id.deleteExpText);
        deleteExpText.setVisibility(View.GONE);
        noSpendingReportText = findViewById(R.id.noSpendingReportText);
        noSpendingReportText.setVisibility(View.GONE);
        okButton = findViewById(R.id.okButton);
        okButton.setVisibility(View.GONE);
        ok2Button = findViewById(R.id.ok2Button);
        ok2Button.setVisibility(View.GONE);
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.GONE);
        budgetIncomePlusButton = findViewById(R.id.budgetIncomePlusButton);
        budgetExpensePlusButton = findViewById(R.id.budgetExpensePlusButton);
        budgetIncomeDetails = findViewById(R.id.budgetIncomeDetails);
        budgetExpensesDetails = findViewById(R.id.budgetExpensesDetails);

        budgetIncomePlusButton.setOnClickListener(onClickIncomePlusButton);
        budgetExpensePlusButton.setOnClickListener(onClickExpensePlusButton);

        doneBudgetSetUpButton = findViewById(R.id.doneBudgetSetUpButton);
        doneBudgetSetUpButton.setOnClickListener(onClickDoneBudgetSetUpButton);

        if (dbManager.budgetSetUpCheck() > 0) {
            doneBudgetSetUpButton.setVisibility(View.GONE);
            emptyBudgetText.setVisibility(View.GONE);
            budgetSetUpNoTime.setVisibility(View.GONE);
            budgetSetUpNoTime2.setVisibility(View.GONE);
            budgetSetUpTimeButton.setVisibility(View.GONE);
            budgetSetUpNeedHelp.setVisibility(View.GONE);
            budgetSetUpNeedHelp2.setVisibility(View.GONE);
            budgetSetUpHelpButton.setVisibility(View.GONE);
        }

        incomeAdapter = new IncomeDbAdapter(this, dbManager.getIncomes());
        budgetIncomeDetails.setAdapter(incomeAdapter);

        expenseAdapter = new ExpenseDbAdapter(this, dbManager.getExpense());
        budgetExpensesDetails.setAdapter(expenseAdapter);

        budgetHeaderText();

    }

    View.OnClickListener onClickDoneBudgetSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetDone = 1;

            setUpDb = new SetUpDb(debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            toast = Toast.makeText(getApplicationContext(), R.string.edit_budget_message, Toast.LENGTH_LONG);
            toastLayout = (LinearLayout) toast.getView();
            tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutBudget.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };

    View.OnClickListener onClickNoTimeBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetSetUpNoTime2.setVisibility(View.VISIBLE);
            budgetSetUpTimeButton.setVisibility(View.VISIBLE);
            budgetSetUpTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budgetSetUpNoTime2.setVisibility(View.GONE);
                    budgetSetUpTimeButton.setVisibility(View.GONE);
                }
            });
        }
    };

    View.OnClickListener onClickNeedHelpBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetSetUpNeedHelp2.setVisibility(View.VISIBLE);
            budgetSetUpHelpButton.setVisibility(View.VISIBLE);
            budgetSetUpHelpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budgetSetUpNeedHelp2.setVisibility(View.GONE);
                    budgetSetUpHelpButton.setVisibility(View.GONE);
                }
            });
        }
    };

    public void budgetHeaderText() {

        totalIncomeR = dbManager.sumTotalIncome();
        totalExpensesR = dbManager.sumTotalExpenses();

        try {
            totalIncomeS = String.valueOf(totalIncomeR);
            if (totalIncomeS != null && !totalIncomeS.equals("")) {
                totalIncomeD = Double.valueOf(totalIncomeS);
            } else {
                totalIncomeD = 0.0;
            }

            totalIncome2 = currencyFormat.format(totalIncomeD);
            budgetIncomeTotalText.setText(totalIncome2);

        } catch (NumberFormatException e) {
            budgetIncomeTotalText.setText(totalIncome2);
        }

        try {
            totalExpensesS = String.valueOf(totalExpensesR);
            if (totalExpensesS != null && !totalExpensesS.equals("")) {
                totalExpensesD = Double.valueOf(totalExpensesS);
            } else {
                totalExpensesD = 0.0;
            }

            totalExpenses2 = currencyFormat.format(totalExpensesD);
            budgetExpensesTotalText.setText(totalExpenses2);

        } catch (NumberFormatException e) {
            budgetExpensesTotalText.setText(totalExpenses2);
        }

        if (totalExpensesD > totalIncomeD) {

            incomeAvailableN = (totalExpensesR - totalIncomeR);
            incomeAvailableN2 = currencyFormat.format(incomeAvailableN);

            budgetOopsText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setText("-" + incomeAvailableN2);
            headerLabel2.setVisibility(View.GONE);
            incomeAvailable.setVisibility(View.GONE);
        }

        if (totalIncomeD >= totalExpensesD) {
            budgetOopsText.setVisibility(View.GONE);
            budgetOopsAmountText.setVisibility(View.GONE);
            headerLabel2.setVisibility(View.VISIBLE);
            incomeAvailable.setVisibility(View.VISIBLE);
        }

        incomeAvailableD = (totalIncomeR - totalExpensesR);
        incomeAvailable2 = currencyFormat.format(incomeAvailableD);
        incomeAvailable.setText(incomeAvailable2);

    }

    View.OnClickListener onClickIncomePlusButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incomePlusButton = new Intent(LayoutBudget.this, AddBudgetIncome.class);
            incomePlusButton.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(incomePlusButton);
        }
    };

    View.OnClickListener onClickExpensePlusButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expensePlusButton = new Intent(LayoutBudget.this, AddBudgetExpense.class);
            expensePlusButton.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(expensePlusButton);
        }
    };

    public void backToBudget() {
        backToBudget = new Intent(LayoutBudget.this, LayoutBudget.class);
        backToBudget.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToBudget);
    }

    public void findAllMatchingIncIds() {

        for (DebtDb d : dbManager.getDebts()) {
            if (String.valueOf(d.getIncRefKeyD()).equals(incomeId)) {
                incDebtId = String.valueOf(d.getId());
                expRefKeyD = String.valueOf(d.getExpRefKeyD());
                foundDebtIdInc = true;
            }
        }

        for (SavingsDb s : dbManager.getSavings()) {
            if (String.valueOf(s.getIncRefKeyS()).equals(incomeId)) {
                incSavingsId = String.valueOf(s.getId());
                foundSavingsIdInc = true;
            }
        }

        for (ExpenseBudgetDb e : dbManager.getExpense()) {
            if (String.valueOf(e.getId()).equals(expRefKeyD)) {
                expenseId = String.valueOf(e.getId());
            }
        }
    }

    public void findAllMatchingExpIds() {

        for (DebtDb d : dbManager.getDebts()) {
            if (String.valueOf(d.getExpRefKeyD()).equals(expenseId)) {
                expDebtId = String.valueOf(d.getId());
                incRefKeyD = String.valueOf(d.getIncRefKeyD());
                foundDebtIdExp = true;
            }
        }

        for (SavingsDb s : dbManager.getSavings()) {
            if (String.valueOf(s.getExpRefKeyS()).equals(expenseId)) {
                expSavingsId = String.valueOf(s.getId());
                foundSavingsIdExp = true;
            }
        }

        for (IncomeBudgetDb i : dbManager.getIncomes()) {
            if (String.valueOf(i.getId()).equals(incRefKeyD)) {
                incomeId = String.valueOf(i.getId());
            }
        }
    }

    public void allDebtDataExp() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (String.valueOf(d3.getExpRefKeyD()).equals(String.valueOf(expenseBudgetDb.getId()))) {
                debtAmount = d3.getDebtAmount();
                debtRate = d3.getDebtRate();
                debtPayments = d3.getDebtPayments();
                debtFrequency = d3.getDebtFrequency();
                debtAnnualIncome = d3.getDebtAnnualIncome();
            }
        }
    }

    public void allDebtDataInc() {
        for (DebtDb d4 : dbManager.getDebts()) {
            if (String.valueOf(d4.getIncRefKeyD()).equals(String.valueOf(incomeBudgetDb.getId()))) {
                debtAmount = d4.getDebtAmount();
                debtRate = d4.getDebtRate();
                debtPayments = d4.getDebtPayments();
                debtFrequency = d4.getDebtFrequency();
                debtAnnualIncome = d4.getDebtAnnualIncome();
            }
        }
    }

    public void allDataSavingsExp() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (String.valueOf(s2.getExpRefKeyS()).equals(expenseId)) {
                savingsAmount = s2.getSavingsAmount();
                savingsGoal = s2.getSavingsGoal();
                //savingsRate = s2.getSavingsRate();
                currentSavingsRate = s2.getSavingsRate();
                savingsRate = currentSavingsRate / 100;
                savingsPayments = s2.getSavingsPayments();
                savingsFrequency = s2.getSavingsFrequency();
                savingsAnnualIncome = s2.getSavingsAnnualIncome();
            }
        }
        /*if(savingsGoal == 0 || savingsGoal < savingsAmount) {
            savingsGoal = savingsAmount;
        }*/
        //if(savingsPayments == 0) {
            //savingsPayments = .01;
            //savingsFrequency = 1.0;
            //savingsAnnuallyRadioButton.setChecked(true);
        //}
    }

    public void allDataSavingsInc() {
        for (SavingsDb s3 : dbManager.getSavings()) {
            if (String.valueOf(s3.getIncRefKeyS()).equals(incomeId)) {
                savingsAmount = s3.getSavingsAmount();
                //savingsGoal = s3.getSavingsGoal();
                currentSavingsRate = s3.getSavingsRate();
                savingsRate = currentSavingsRate / 100;
                savingsRate = s3.getSavingsRate();
                savingsPayments = s3.getSavingsPayments();
                savingsFrequency = s3.getSavingsFrequency();
                savingsAnnualIncome = s3.getSavingsAnnualIncome();
            }
        }
        /*if(savingsGoal == 0 || savingsGoal < savingsAmount) {
            savingsGoal = savingsAmount;
        }*/
        //if(savingsPayments == 0) {
            //savingsPayments = .01;
            //savingsFrequency = 1.0;
            //savingsAnnuallyRadioButton.setChecked(true);
        //}
    }

    public class IncomeDbAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public IncomeDbAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<IncomeBudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderIncome incomeHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_income,
                        parent, false);

                incomeHolder = new ViewHolderIncome();
                incomeHolder.incomeName = convertView.findViewById(R.id.budgetIncomeCategoryText);
                incomeHolder.incomeAmount = convertView.findViewById(R.id.budgetIncomeAmountText);
                incomeHolder.incomeDeleted = convertView.findViewById(R.id.deleteIncomeButton);
                incomeHolder.incomeEdit = convertView.findViewById(R.id.editIncomeButton);
                convertView.setTag(incomeHolder);

            } else {
                incomeHolder = (ViewHolderIncome) convertView.getTag();
            }

            incomeHolder.incomeName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            try {
                incomeAnnualAmountS = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
                if (!incomeAnnualAmountS.equals("")) {
                    incomeAnnualAmountD = Double.valueOf(incomeAnnualAmountS);
                } else {
                    incomeAnnualAmountD = 0.0;
                }
                incomeAnnualAmount2 = currencyFormat.format(incomeAnnualAmountD);
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            } catch (NumberFormatException e2) {
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            }

            incomeHolder.incomeDeleted.setTag(incomes.get(position));
            incomeHolder.incomeEdit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            incomeHolder.incomeEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_budget_income);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetIncomeCategory = findViewById(R.id.budgetIncomeCategory);
                    budgetIncomeAmount = findViewById(R.id.budgetIncomeAmount);
                    budgetIncomeFrequencyRadioGroup = findViewById(R.id.budgetIncomeFrequencyRadioGroup);
                    budgetIncomeWeeklyRadioButton = findViewById(R.id.budgetIncomeWeeklyRadioButton);
                    budgetIncomeBiWeeklyRadioButton = findViewById(R.id.budgetIncomeBiWeeklyRadioButton);
                    budgetIncomeBiMonthlyRadioButton = findViewById(R.id.budgetIncomeBiMonthlyRadioButton);
                    budgetIncomeMonthlyRadioButton = findViewById(R.id.budgetIncomeMonthlyRadioButton);
                    budgetIncomeBiAnnuallyRadioButton = findViewById(R.id.budgetIncomeBiAnnuallyRadioButton);
                    budgetIncomeAnnuallyRadioButton = findViewById(R.id.budgetIncomeAnnuallyRadioButton);
                    budgetAddIncomeButton = findViewById(R.id.budgetAddIncomeButton);
                    budgetUpdateIncomeButton = findViewById(R.id.budgetUpdateIncomeButton);
                    budgetAddIncomeButton.setVisibility(View.GONE);
                    budgetCancelIncomeButton = findViewById(R.id.budgetCancelIncomeButton);

                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeEdit.getTag();

                    budgetIncomeCategory.setText(incomeBudgetDb.getIncomeName());

                    budgetIncomeAmountD = incomeBudgetDb.getIncomeAmount();
                    budgetIncomeAmountS = currencyFormat.format(budgetIncomeAmountD);
                    budgetIncomeAmount.setText(budgetIncomeAmountS);

                    if (incomeBudgetDb.getIncomeFrequency() == 52) {
                        budgetIncomeWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "52";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 26) {
                        budgetIncomeBiWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "26";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 24) {
                        budgetIncomeBiMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "24";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 12) {
                        budgetIncomeMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "12";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 2) {
                        budgetIncomeBiAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "2";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 1) {
                        budgetIncomeAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "1";
                    }

                    //update db if changed
                    budgetIncomeFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetIncomeWeeklyRadioButton:
                                    incomeFrequencyS = "52";
                                    break;
                                case R.id.budgetIncomeBiWeeklyRadioButton:
                                    incomeFrequencyS = "26";
                                    break;
                                case R.id.budgetIncomeBiMonthlyRadioButton:
                                    incomeFrequencyS = "24";
                                    break;
                                case R.id.budgetIncomeMonthlyRadioButton:
                                    incomeFrequencyS = "12";
                                    break;
                                case R.id.budgetIncomeBiAnnuallyRadioButton:
                                    incomeFrequencyS = "2";
                                    break;
                                case R.id.budgetIncomeAnnuallyRadioButton:
                                    incomeFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetCancelIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    budgetUpdateIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            incomeId = String.valueOf(incomes.get(position).getId());

                            findAllMatchingIncIds();

                            if (budgetIncomeCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryInc = budgetIncomeCategory.getText().toString();
                                amountEntry = general.extractingDouble(budgetIncomeAmount);
                                frequencyEntry = Double.valueOf(incomeFrequencyS);
                                annualIncome = amountEntry * frequencyEntry;

                                incomeBudgetDb.setIncomeName(nameEntryInc);
                                incomeBudgetDb.setIncomeAmount(amountEntry);
                                incomeBudgetDb.setIncomeFrequency(frequencyEntry);
                                incomeBudgetDb.setIncomeAnnualAmount(annualIncome);

                                dbHelper2 = new DbHelper(getContext());
                                db2 = dbHelper2.getWritableDatabase();

                                try {
                                    String[] args4 = new String[]{incDebtId};
                                    debtValues3 = new ContentValues();

                                    debtValues3.put(DbHelper.DEBTNAME, nameEntryInc);
                                    debtValues3.put(DbHelper.DEBTANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues3, DbHelper.ID + "=?", args4);

                                    allDebtDataInc();

                                    debtValues4 = new ContentValues();
                                    debtValues4.put(DbHelper.DEBTEND, general.calcDebtDate(
                                            debtAmount,
                                            debtRate,
                                            debtPayments,
                                            debtFrequency,
                                            debtAnnualIncome,
                                            getString(R.string.debt_paid),
                                            getString(R.string.too_far)));
                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues4, DbHelper.ID + "=?", args4);

                                } catch (CursorIndexOutOfBoundsException e4) {
                                    e4.printStackTrace();
                                }

                                try {
                                    String[] args5 = new String[]{incSavingsId};
                                    savingsValues3 = new ContentValues();

                                    savingsValues3.put(DbHelper.SAVINGSNAME, nameEntryInc);
                                    savingsValues3.put(DbHelper.SAVINGSANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues3, DbHelper.ID + "=?", args5);

                                    allDataSavingsInc();
                                    /*general.findSavingsYears(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome
                                    );*/

                                    savingsValues4 = new ContentValues();
                                    savingsValues4.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome,
                                            getString(R.string.goal_achieved),
                                            getString(R.string.too_far)));
                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues4, DbHelper.ID + "=?", args5);

                                } catch (CursorIndexOutOfBoundsException e5) {
                                    e5.printStackTrace();
                                }

                                try {
                                    String[] args6 = new String[]{expenseId};
                                    expValues = new ContentValues();

                                    expValues.put(DbHelper.EXPENSENAME, nameEntryInc);

                                    db2.update(DbHelper.EXPENSES_TABLE_NAME, expValues, DbHelper.ID + "=?", args6);

                                } catch (CursorIndexOutOfBoundsException e6) {
                                    e6.printStackTrace();
                                }

                                try {
                                    String[] args7 = new String[]{incomeId};
                                    moneyInValues = new ContentValues();

                                    moneyInValues.put(DbHelper.MONEYINCAT, nameEntryInc);

                                    db2.update(DbHelper.MONEY_IN_TABLE_NAME, moneyInValues, DbHelper.INCREFKEYMI + "=?", args7);

                                } catch (CursorIndexOutOfBoundsException e7) {
                                    e7.printStackTrace();
                                }
                                db2.close();

                                dbManager.updateIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                backToBudget();
                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            incomeHolder.incomeDeleted.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeDeleted.getTag();

                    incomeId = String.valueOf(incomes.get(position).getId());

                    noSpendingReportText.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            noSpendingReportText.setVisibility(View.GONE);
                            okButton.setVisibility(View.GONE);
                            cancelButton.setVisibility(View.GONE);

                            findAllMatchingIncIds();

                            if (foundDebtIdInc || foundSavingsIdInc) {
                                deleteExpText.setVisibility(View.VISIBLE);
                                ok2Button.setVisibility(View.VISIBLE);

                                ok2Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        backToBudget();
                                    }
                                });
                            } else {
                                deleteExpText.setVisibility(View.GONE);
                                ok2Button.setVisibility(View.GONE);

                                dbManager.deleteIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();

                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            return convertView;
        }
    }

    public class ExpenseDbAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> expenses;

        public ExpenseDbAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<ExpenseBudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderExpense expenseHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_expense,
                        parent, false);

                expenseHolder = new ViewHolderExpense();
                expenseHolder.expenseName = convertView.findViewById(R.id.budgetExpenseCategoryText);
                expenseHolder.expenseAmount = convertView.findViewById(R.id.budgetExpenseAmountText);
                expenseHolder.expenseDeleted = convertView.findViewById(R.id.deleteExpenseButton);
                expenseHolder.expenseEdit = convertView.findViewById(R.id.editExpenseButton);
                convertView.setTag(expenseHolder);

            } else {
                expenseHolder = (ViewHolderExpense) convertView.getTag();
            }

            expenseHolder.expenseName.setText(expenses.get(position).getExpenseName());

            //retrieve incomeAmount and format as currency
            try {
                expenseAnnualAmountS = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
                if (!expenseAnnualAmountS.equals("")) {
                    expenseAnnualAmountD = Double.valueOf(expenseAnnualAmountS);
                } else {
                    expenseAnnualAmountD = 0.0;
                }
                expenseAnnualAmount2 = currencyFormat.format(expenseAnnualAmountD);
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            } catch (NumberFormatException e8) {
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            }

            expenseHolder.expenseDeleted.setTag(expenses.get(position));
            expenseHolder.expenseEdit.setTag(expenses.get(position));

            //click on pencil icon to edit a data record
            expenseHolder.expenseEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_budget_expense);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetExpenseCategory = findViewById(R.id.budgetExpenseCategory);
                    budgetExpenseAmount = findViewById(R.id.budgetExpenseAmount);
                    budgetExpenseFrequencyRadioGroup = findViewById(R.id.budgetExpenseFrequencyRadioGroup);
                    weeklyGuidanceLabel = findViewById(R.id.weeklyGuidanceLabel);
                    budgetExpenseABRadioGroup = findViewById(R.id.budgetExpenseABRadioGroup);
                    budgetExpenseReminderRadioGroup = findViewById(R.id.budgetExpenseReminderRadioGroup);
                    budgetCancelExpenseButton = findViewById(R.id.budgetCancelExpenseButton);
                    budgetAddExpenseButton = findViewById(R.id.budgetAddExpenseButton);
                    budgetUpdateExpenseButton = findViewById(R.id.budgetUpdateExpenseButton);
                    budgetAddExpenseButton.setVisibility(View.GONE);
                    weeklyGuidanceLabel.setVisibility(View.GONE);
                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);

                    budgetExpenseWeeklyRadioButton = findViewById(R.id.budgetExpenseWeeklyRadioButton);
                    budgetExpenseBiWeeklyRadioButton = findViewById(R.id.budgetExpenseBiWeeklyRadioButton);
                    budgetExpenseBiMonthlyRadioButton = findViewById(R.id.budgetExpenseBiMonthlyRadioButton);
                    budgetExpenseMonthlyRadioButton = findViewById(R.id.budgetExpenseMonthlyRadioButton);
                    budgetExpenseBiAnnuallyRadioButton = findViewById(R.id.budgetExpenseBiAnnuallyRadioButton);
                    budgetExpenseAnnuallyRadioButton = findViewById(R.id.budgetExpenseAnnuallyRadioButton);

                    budgetExpenseARadioButton = findViewById(R.id.budgetExpenseARadioButton);
                    budgetExpenseBRadioButton = findViewById(R.id.budgetExpenseBRadioButton);

                    budgetExpenseYesRadioButton = findViewById(R.id.budgetExpenseYesRadioButton);
                    budgetExpenseNoRadioButton = findViewById(R.id.budgetExpenseNoRadioButton);

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseEdit.getTag();

                    budgetExpenseCategory.setText(expenseBudgetDb.getExpenseName());

                    findAllMatchingExpIds();

                    if (foundDebtIdExp) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.GONE);
                    } else if (foundSavingsIdExp) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    } else {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    }

                    budgetExpenseAmountD = expenseBudgetDb.getExpenseAmount();
                    budgetExpenseAmountS = currencyFormat.format(budgetExpenseAmountD);
                    budgetExpenseAmount.setText(budgetExpenseAmountS);

                    //set radio buttons from data
                    if (expenseBudgetDb.getExpenseFrequency() == 52) {
                        budgetExpenseWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "52";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 26) {
                        budgetExpenseBiWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "26";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 24) {
                        budgetExpenseBiMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "24";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 12) {
                        budgetExpenseMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "12";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 2) {
                        budgetExpenseBiAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "2";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 1) {
                        budgetExpenseAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "1";
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpensePriority())) {
                        case "A":
                            budgetExpenseARadioButton.setChecked(true);
                            expensePriorityS = "A";
                            budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                            weeklyGuidanceLabel.setVisibility(View.GONE);
                            break;
                        case "B":
                            budgetExpenseBRadioButton.setChecked(true);
                            expensePriorityS = "B";
                            budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                            weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                            break;
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpenseWeekly())) {
                        case "Y":
                            budgetExpenseYesRadioButton.setChecked(true);
                            expenseWeeklyS = "Y";
                            break;
                        case "N":
                            budgetExpenseNoRadioButton.setChecked(true);
                            expenseWeeklyS = "N";
                            break;
                    }


                    //update db if changed
                    budgetExpenseFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseWeeklyRadioButton:
                                    expenseFrequencyS = "52";
                                    break;
                                case R.id.budgetExpenseBiWeeklyRadioButton:
                                    expenseFrequencyS = "26";
                                    break;
                                case R.id.budgetExpenseBiMonthlyRadioButton:
                                    expenseFrequencyS = "24";
                                    break;
                                case R.id.budgetExpenseMonthlyRadioButton:
                                    expenseFrequencyS = "12";
                                    break;
                                case R.id.budgetExpenseBiAnnuallyRadioButton:
                                    expenseFrequencyS = "2";
                                    break;
                                case R.id.budgetExpenseAnnuallyRadioButton:
                                    expenseFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetExpenseABRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseARadioButton:
                                    expensePriorityS = "A";
                                    expenseWeeklyS = "N";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                                    weeklyGuidanceLabel.setVisibility(View.GONE);
                                    break;
                                case R.id.budgetExpenseBRadioButton:
                                    expensePriorityS = "B";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                                    weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                    });

                    budgetExpenseReminderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseYesRadioButton:
                                    expenseWeeklyS = "Y";
                                    break;
                                case R.id.budgetExpenseNoRadioButton:
                                    expenseWeeklyS = "N";
                                    break;
                            }
                        }

                    });

                    budgetCancelExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    budgetUpdateExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expenseId = String.valueOf(expenses.get(position).getId());

                            findAllMatchingExpIds();

                            if (budgetExpenseCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryExp = budgetExpenseCategory.getText().toString();
                                amountEntry = general.extractingDouble(budgetExpenseAmount);
                                frequencyEntry = Double.valueOf(expenseFrequencyS);
                                priorityEntryExp = String.valueOf(expensePriorityS);
                                weeklyEntry = String.valueOf(expenseWeeklyS);
                                annualAmount = amountEntry * frequencyEntry;

                                expenseBudgetDb.setExpenseName(nameEntryExp);
                                expenseBudgetDb.setExpenseAmount(amountEntry);
                                expenseBudgetDb.setExpenseFrequency(frequencyEntry);
                                expenseBudgetDb.setExpensePriority(priorityEntryExp);
                                expenseBudgetDb.setExpenseWeekly(weeklyEntry);
                                expenseBudgetDb.setExpenseAnnualAmount(annualAmount);

                                if (priorityEntryExp.equals("A")) {
                                    expenseBudgetDb.setExpenseAAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseBAnnualAmount(0.0);
                                } else if (priorityEntryExp.equals("B")) {
                                    expenseBudgetDb.setExpenseBAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseAAnnualAmount(0.0);
                                }

                                dbHelper = new DbHelper(getContext());
                                db = dbHelper.getWritableDatabase();

                                try {
                                    String[] args = new String[]{expDebtId};
                                    debtValues = new ContentValues();

                                    debtValues.put(DbHelper.DEBTNAME, nameEntryExp);
                                    debtValues.put(DbHelper.DEBTPAYMENTS, amountEntry);
                                    debtValues.put(DbHelper.DEBTFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues, DbHelper.ID + "=?", args);

                                    allDebtDataExp();

                                    debtValues2 = new ContentValues();
                                    debtValues2.put(DbHelper.DEBTEND, general.calcDebtDate(
                                            debtAmount,
                                            debtRate,
                                            debtPayments,
                                            debtFrequency,
                                            debtAnnualIncome,
                                            getString(R.string.debt_paid),
                                            getString(R.string.too_far)));
                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues2, DbHelper.ID + "=?", args);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
                                }

                                try {
                                    String[] args2 = new String[]{expSavingsId};
                                    savingsValues = new ContentValues();

                                    savingsValues.put(DbHelper.SAVINGSNAME, nameEntryExp);
                                    savingsValues.put(DbHelper.SAVINGSPAYMENTS, amountEntry);
                                    savingsValues.put(DbHelper.SAVINGSFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues, DbHelper.ID + "=?", args2);

                                    allDataSavingsExp();
                                    /*general.findSavingsYears(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome
                                    );*/

                                    savingsValues2 = new ContentValues();
                                    savingsValues2.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome,
                                            getString(R.string.goal_achieved),
                                            getString(R.string.too_far)));
                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues2, DbHelper.ID + "=?", args2);

                                } catch (CursorIndexOutOfBoundsException e11) {
                                    e11.printStackTrace();
                                }

                                try {
                                    String[] args8 = new String[]{incomeId};
                                    incValues = new ContentValues();

                                    incValues.put(DbHelper.INCOMENAME, nameEntryExp);

                                    db.update(DbHelper.INCOME_TABLE_NAME, incValues, DbHelper.ID + "=?", args8);

                                } catch (CursorIndexOutOfBoundsException e12) {
                                    e12.printStackTrace();
                                }


                                try {
                                    String[] args3 = new String[]{expenseId};
                                    String[] args4 = new String[]{expDebtId};
                                    moneyOutValues = new ContentValues();
                                    moneyOutValues2 = new ContentValues();

                                    moneyOutValues.put(DbHelper.MONEYOUTCAT, nameEntryExp);
                                    moneyOutValues.put(DbHelper.MONEYOUTPRIORITY, priorityEntryExp);
                                    moneyOutValues.put(DbHelper.MONEYOUTWEEKLY, weeklyEntry);
                                    moneyOutValues2.put(DbHelper.MONEYOUTDEBTCAT, nameEntryExp);

                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues, DbHelper.EXPREFKEYMO + "=?", args3);
                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues2, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args4);

                                } catch (CursorIndexOutOfBoundsException e13) {
                                    e13.printStackTrace();
                                }

                                db.close();

                                dbManager.updateExpense(expenseBudgetDb);
                                expenseAdapter.updateExpenses(dbManager.getExpense());
                                expenseAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                backToBudget();
                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            expenseHolder.expenseDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseDeleted.getTag();

                    expenseId = String.valueOf(expenses.get(position).getId());

                    noSpendingReportText.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            noSpendingReportText.setVisibility(View.GONE);
                            okButton.setVisibility(View.GONE);
                            cancelButton.setVisibility(View.GONE);

                            findAllMatchingExpIds();

                            if (foundDebtIdExp || foundSavingsIdExp) {
                                deleteExpText.setVisibility(View.VISIBLE);
                                ok2Button.setVisibility(View.VISIBLE);

                                ok2Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        backToBudget();
                                    }
                                });
                            } else {
                                deleteExpText.setVisibility(View.GONE);
                                ok2Button.setVisibility(View.GONE);

                                dbManager.deleteExpense(expenseBudgetDb);
                                expenseAdapter.updateExpenses(dbManager.getExpense());
                                budgetHeaderText();
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }

    private static class ViewHolderIncome {
        private TextView incomeName;
        private TextView incomeAmount;
        private ImageButton incomeDeleted;
        private ImageButton incomeEdit;
    }

    private static class ViewHolderExpense {
        private TextView expenseName;
        private TextView expenseAmount;
        private ImageButton expenseDeleted;
        private ImageButton expenseEdit;
    }
}
