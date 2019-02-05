package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;

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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class LayoutBudget extends MainNavigation {

    boolean foundDebtIdExp = false, foundDebtIdInc = false, foundSavingsIdExp = false, foundSavingsIdInc = false;
    Button budgetAddExpenseButton, budgetAddIncomeButton, budgetCancelExpenseButton, budgetCancelIncomeButton, budgetUpdateExpenseButton, budgetUpdateIncomeButton,
            doneBudgetSetUpButton, okButton;
    Calendar debtCal, debtCal2, savingsCal, savingsCal2;
    ContentValues debtValues, debtValues2, debtValues3, debtValues4, moneyInValues, moneyOutValues, savingsValues, savingsValues2, savingsValues3, savingsValues4;
    Date debtEndD, debtEndD2, savingsDateD, savingsDateD2;
    DbHelper dbHelper, dbHelper2;
    DbManager dbManager;
    Double amountEntry = 0.0, annualAmount = 0.0, annualIncome = 0.0, balanceAmount = 0.0, budgetExpenseAmountD = 0.0, budgetIncomeAmountD = 0.0,
            currentSavingsFrequency = 0.0, currentSavingsFrequency2 = 0.0, currentSavingsPayments = 0.0, currentSavingsPayments2 = 0.0, currentSavingsRate = 0.0,
            currentSavingsRate2 = 0.0, debtAmount2 = 0.0, debtAmount3 = 0.0, debtAnnualIncome2 = 0.0, debtAnnualIncome3 = 0.0, debtFrequency2 = 0.0,
            debtFrequency3 = 0.0, debtPayments2 = 0.0, debtPayments3 = 0.0, debtRate2 = 0.0, debtRate3 = 0.0, expenseAnnualAmountD = 0.0, frequencyEntry = 0.0,
            incomeAnnualAmountD = 0.0, incomeAvailableD = 0.0, incomeAvailableN = 0.0, numberOfYearsToPayDebt = 0.0, numberOfYearsToPayDebt2 = 0.0, rate = 0.0,
            rate2 = 0.0, savingsAmount = 0.0, savingsAmount2 = 0.0, savingsAnnualIncome = 0.0, savingsAnnualIncome2 = 0.0, savingsGoal = 0.0, savingsGoal2 = 0.0,
            savingsIntFrequency = 0.0, savingsIntFrequency2 = 0.0, totalExpensesD = 0.0, totalExpensesR = 0.0, totalIncomeD = 0.0, totalIncomeR = 0.0, years = 0.0,
            years2 = 0.0;
    EditText budgetExpenseAmount, budgetExpenseCategory, budgetIncomeAmount, budgetIncomeCategory;
    ExpenseBudgetDb expenseBudgetDb;
    ExpenseDbAdapter expenseAdapter;
    General general;
    FloatingActionButton budgetExpensePlusButton, budgetIncomePlusButton;
    IncomeBudgetDb incomeBudgetDb;
    IncomeDbAdapter incomeAdapter;
    int balanceDone = 0, budgetDone = 0, debtsDone = 0, numberOfDaysToPayDebt = 0, numberOfDaysToPayDebt2 = 0, numberOfDaysToSavingsGoal = 0,
            numberOfDaysToSavingsGoal2 = 0, savingsDone = 0, tourDone = 0;
    Intent backToBudget, backToSetUp, expensePlusButton, incomePlusButton;
    ListView budgetExpensesDetails, budgetIncomeDetails;
    long id;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    RadioButton budgetExpenseARadioButton, budgetExpenseAnnuallyRadioButton, budgetExpenseBRadioButton, budgetExpenseBiAnnuallyRadioButton,
            budgetExpenseBiMonthlyRadioButton, budgetExpenseBiWeeklyRadioButton, budgetExpenseMonthlyRadioButton, budgetExpenseNoRadioButton,
            budgetExpenseWeeklyRadioButton, budgetExpenseYesRadioButton, budgetIncomeAnnuallyRadioButton, budgetIncomeBiAnnuallyRadioButton,
            budgetIncomeBiMonthlyRadioButton, budgetIncomeBiWeeklyRadioButton, budgetIncomeMonthlyRadioButton, budgetIncomeWeeklyRadioButton;
    RadioGroup budgetExpenseABRadioGroup, budgetExpenseFrequencyRadioGroup, budgetIncomeFrequencyRadioGroup, budgetExpenseReminderRadioGroup;
    SetUpDb setUpDb;
    SimpleDateFormat debtEndS, debtEndS2, savingsDateS, savingsDateS2;
    SQLiteDatabase db, db2;
    String budgetExpenseAmountS = null, budgetIncomeAmountS = null, debtEnd = null, debtEnd2 = null, expenseAnnualAmount2 = null, expenseAnnualAmountS = null,
            expenseFrequencyS = null, expensePriorityS = null, expenseWeeklyS = null, incomeAnnualAmount2 = null, incomeAnnualAmountS = null, incomeAvailable2 = null,
            incomeAvailableN2 = null, incomeFrequencyS = null, incomeId = null, nameEntry = null, priorityEntry = null, savingsDate = null, savingsDate2 = null,
            totalExpenses2 = null, totalExpensesS = null, totalIncome2 = null, totalIncomeS = null, weeklyEntry = null;
    TextView emptyBudgetText, budgetExpensesTotalText, budgetIncomeTotalText, budgetOopsAmountText, budgetOopsText, deleteExpText, headerLabel2, incomeAvailable, weeklyGuidanceLabel;

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
        deleteExpText = findViewById(R.id.deleteExpText);
        deleteExpText.setVisibility(View.GONE);
        okButton = findViewById(R.id.okButton);
        okButton.setVisibility(View.GONE);
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

            Toast toast = Toast.makeText(getApplicationContext(), R.string.edit_budget_message, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutBudget.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

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

        } catch (NumberFormatException e2) {
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

    public long findMatchingDebtIdExp() {
        for (DebtDb d : dbManager.getDebts()) {
            try {
                if (d.getExpRefKeyD() == expenseBudgetDb.getId()) {
                    id = d.getId();
                    foundDebtIdExp = true;
                }
            } catch (Exception e) {
                foundDebtIdExp = false;
            }
        }
        return id;
    }

    public String updateDebtEndDateExp() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (String.valueOf(d3.getExpRefKeyD()).equals(String.valueOf(expenseBudgetDb.getId()))) {
                debtAmount2 = d3.getDebtAmount();
                debtRate2 = d3.getDebtRate();
                debtPayments2 = d3.getDebtPayments();
                debtFrequency2 = d3.getDebtFrequency();
                debtAnnualIncome2 = d3.getDebtAnnualIncome();
            }
        }

        debtCal = Calendar.getInstance();
        numberOfYearsToPayDebt = -(Math.log(1 - (debtAmount2 * (debtRate2 / 100) / (debtPayments2 * debtFrequency2) - debtAnnualIncome2)) / (debtFrequency2 *
                Math.log(1 + ((debtRate2 / 100) / debtFrequency2))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (debtAmount2 == 0) {
            debtEnd = getString(R.string.debt_paid);

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = getString(R.string.too_far);

        } else {
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    public long findMatchingDebtIdInc() {
        for (DebtDb d2 : dbManager.getDebts()) {
            try {
                if (d2.getIncRefKeyD() == incomeBudgetDb.getId()) {
                    id = d2.getId();
                    foundDebtIdInc = true;
                }
            } catch (Exception e) {
                foundDebtIdInc = false;
            }
        }
        return id;
    }

    public String updateDebtEndDateInc() {
        for (DebtDb d4 : dbManager.getDebts()) {
            if (String.valueOf(d4.getIncRefKeyD()).equals(String.valueOf(incomeBudgetDb.getId()))) {
                debtAmount3 = d4.getDebtAmount();
                debtRate3 = d4.getDebtRate();
                debtPayments3 = d4.getDebtPayments();
                debtFrequency3 = d4.getDebtFrequency();
                debtAnnualIncome3 = d4.getDebtAnnualIncome();
            }
        }

        debtCal2 = Calendar.getInstance();
        numberOfYearsToPayDebt2 = -(Math.log(1 - (debtAmount3 * (debtRate3 / 100) / ((debtPayments3 * debtFrequency3) - debtAnnualIncome3))) / (debtFrequency3 *
                Math.log(1 + ((debtRate3 / 100) / debtFrequency3))));
        numberOfDaysToPayDebt2 = (int) Math.round(numberOfYearsToPayDebt2 * 365);

        if (debtAmount3 == 0) {
            debtEnd2 = getString(R.string.debt_paid);

        } else if (numberOfDaysToPayDebt2 > Integer.MAX_VALUE || numberOfDaysToPayDebt2 <= 0) {
            debtEnd2 = getString(R.string.too_far);

        } else {
            debtCal2.add(Calendar.DATE, numberOfDaysToPayDebt2);
            debtEndD2 = debtCal2.getTime();
            debtEndS2 = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd2 = debtEndS2.format(debtEndD2);
        }

        return debtEnd2;
    }

    public long findMatchingSavingsIdExp() {
        for (SavingsDb s : dbManager.getSavings()) {
            try {
                if (s.getExpRefKeyS() == expenseBudgetDb.getId()) {
                    id = s.getId();
                    foundSavingsIdExp = true;
                }
            } catch (Exception e2) {
                foundSavingsIdExp = false;
            }
        }
        return id;
    }

    public Double findSavingsYearsExp() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == findMatchingSavingsIdExp()) {
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

    public String updateSavingsDateExp() {

        savingsCal = Calendar.getInstance();
        numberOfDaysToSavingsGoal = (int) Math.round(findSavingsYearsExp() * 365);

        if ((numberOfDaysToSavingsGoal) <= 0) {
            savingsDate = getString(R.string.goal_achieved);

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE) {
            savingsDate = getString(R.string.too_far);

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = savingsDateS.format(savingsDateD);
        }
        return savingsDate;
    }

    public long findMatchingSavingsIdInc() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            try {
                if (s2.getIncRefKeyS() == incomeBudgetDb.getId()) {
                    id = s2.getId();
                    foundSavingsIdInc = true;
                }
            } catch (Exception e2) {
                foundSavingsIdInc = false;
            }
        }
        return id;
    }

    public Double findSavingsYearsInc() {
        for (SavingsDb s3 : dbManager.getSavings()) {
            if (s3.getId() == findMatchingSavingsIdInc()) {
                savingsAmount2 = s3.getSavingsAmount();
                savingsGoal2 = s3.getSavingsGoal();
                currentSavingsRate2 = s3.getSavingsRate();
                currentSavingsPayments2 = s3.getSavingsPayments();
                currentSavingsFrequency2 = s3.getSavingsFrequency();
                savingsIntFrequency2 = s3.getSavingsIntFrequency();
                savingsAnnualIncome2 = s3.getSavingsAnnualIncome();
            }
        }
        if (savingsGoal2 < savingsAmount2) {
            savingsGoal2 = savingsAmount2;
        }
        rate2 = currentSavingsRate2 / 100;
        if (rate2 == 0) {
            rate2 = .01;
        }
        if (currentSavingsPayments2 == 0) {
            currentSavingsPayments2 = 0.01;
        }
        if (savingsAmount2 == 0 && currentSavingsPayments2 == 0.01) {
            years2 = 0.0;
        } else if (savingsGoal2.equals(savingsAmount2)) {
            years2 = 0.0;
        } else {
            years2 = 0.0;
            do {
                years2 = years2 + .00274;
            }
            while (savingsGoal2 >= (savingsAmount2 * (Math.pow((1 + rate2 / savingsIntFrequency2), savingsIntFrequency2 * years2))) + ((((currentSavingsPayments2 * currentSavingsFrequency2) - savingsAnnualIncome2) / 12) * (((Math.pow((1 + rate2 / savingsIntFrequency2), savingsIntFrequency2 * years2)) - 1) / (rate2 / savingsIntFrequency2)) * (1 + rate2 / savingsIntFrequency2)));
        }

        return years2;
    }

    public String updateSavingsDateInc() {

        savingsCal2 = Calendar.getInstance();
        numberOfDaysToSavingsGoal2 = (int) Math.round(findSavingsYearsInc() * 365);

        if ((numberOfDaysToSavingsGoal2) <= 0) {
            savingsDate2 = getString(R.string.goal_achieved);

        } else if (numberOfDaysToSavingsGoal2 > Integer.MAX_VALUE) {
            savingsDate2 = getString(R.string.too_far);

        } else {

            savingsCal2.add(Calendar.DATE, numberOfDaysToSavingsGoal2);
            savingsDateD2 = savingsCal2.getTime();
            savingsDateS2 = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate2 = savingsDateS2.format(savingsDateD2);
        }
        return savingsDate2;
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
            } catch (NumberFormatException e3) {
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

                            incomeId = String.valueOf(incomeBudgetDb.getId());

                            if (budgetIncomeCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntry = budgetIncomeCategory.getText().toString();
                                try {
                                    amountEntry = Double.valueOf(budgetIncomeAmount.getText().toString());
                                } catch (NumberFormatException e4) {
                                    amountEntry = general.extractingDollars(budgetIncomeAmount);
                                }
                                if (String.valueOf(amountEntry).equals("")) {
                                    amountEntry = 0.0;
                                }
                                frequencyEntry = Double.valueOf(incomeFrequencyS);
                                annualIncome = amountEntry * frequencyEntry;

                                incomeBudgetDb.setIncomeName(nameEntry);
                                incomeBudgetDb.setIncomeAmount(amountEntry);
                                incomeBudgetDb.setIncomeFrequency(frequencyEntry);
                                incomeBudgetDb.setIncomeAnnualAmount(annualIncome);

                                dbManager.updateIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                dbHelper2 = new DbHelper(getContext());
                                db2 = dbHelper2.getWritableDatabase();

                                try {
                                    String[] args3 = new String[]{String.valueOf(findMatchingDebtIdInc())};
                                    debtValues3 = new ContentValues();

                                    debtValues3.put(DbHelper.DEBTNAME, nameEntry);
                                    debtValues3.put(DbHelper.DEBTANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues3, DbHelper.ID + "=?", args3);

                                    debtValues4 = new ContentValues();
                                    debtValues4.put(DbHelper.DEBTEND, updateDebtEndDateInc());
                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues4, DbHelper.ID + "=?", args3);

                                } catch (CursorIndexOutOfBoundsException e8) {
                                    e8.printStackTrace();
                                }

                                try {
                                    String[] args4 = new String[]{String.valueOf(findMatchingSavingsIdInc())};
                                    savingsValues3 = new ContentValues();

                                    savingsValues3.put(DbHelper.SAVINGSNAME, nameEntry);
                                    savingsValues3.put(DbHelper.SAVINGSANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues3, DbHelper.ID + "=?", args4);

                                    savingsValues4 = new ContentValues();
                                    savingsValues4.put(DbHelper.SAVINGSDATE, updateSavingsDateInc());
                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues4, DbHelper.ID + "=?", args4);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
                                }

                                try {
                                    String[] args5 = new String[]{incomeId};
                                    moneyInValues = new ContentValues();

                                    moneyInValues.put(DbHelper.MONEYINCAT, nameEntry);

                                    db2.update(DbHelper.MONEY_IN_TABLE_NAME, moneyInValues, DbHelper.INCREFKEYMI + "=?", args5);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
                                }


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

                    findMatchingDebtIdInc();
                    findMatchingSavingsIdInc();
                    if (foundDebtIdInc || foundSavingsIdInc) {
                        deleteExpText.setVisibility(View.VISIBLE);
                        okButton.setVisibility(View.VISIBLE);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                backToBudget();
                            }
                        });
                    } else {
                        deleteExpText.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);

                        incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeDeleted.getTag();

                        dbManager.deleteIncome(incomeBudgetDb);
                        incomeAdapter.updateIncomes(dbManager.getIncomes());
                        incomeAdapter.notifyDataSetChanged();

                        budgetHeaderText();
                    }
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
            } catch (NumberFormatException e5) {
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

                    findMatchingDebtIdExp();
                    findMatchingSavingsIdExp();

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

                            if (budgetExpenseCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntry = budgetExpenseCategory.getText().toString();
                                try {
                                    amountEntry = Double.valueOf(budgetExpenseAmount.getText().toString());
                                } catch (NumberFormatException e6) {
                                    amountEntry = general.extractingDollars(budgetExpenseAmount);
                                }
                                if (String.valueOf(amountEntry).equals("")) {
                                    amountEntry = 0.0;
                                }
                                frequencyEntry = Double.valueOf(expenseFrequencyS);
                                priorityEntry = String.valueOf(expensePriorityS);
                                weeklyEntry = String.valueOf(expenseWeeklyS);
                                annualAmount = amountEntry * frequencyEntry;

                                expenseBudgetDb.setExpenseName(nameEntry);
                                expenseBudgetDb.setExpenseAmount(amountEntry);
                                expenseBudgetDb.setExpenseFrequency(frequencyEntry);
                                expenseBudgetDb.setExpensePriority(priorityEntry);
                                expenseBudgetDb.setExpenseWeekly(weeklyEntry);
                                expenseBudgetDb.setExpenseAnnualAmount(annualAmount);

                                if (priorityEntry.equals("A")) {
                                    expenseBudgetDb.setExpenseAAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseBAnnualAmount(0.0);
                                } else if (priorityEntry.equals("B")) {
                                    expenseBudgetDb.setExpenseBAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseAAnnualAmount(0.0);
                                }

                                dbHelper = new DbHelper(getContext());
                                db = dbHelper.getWritableDatabase();

                                try {
                                    String[] args = new String[]{String.valueOf(findMatchingDebtIdExp())};
                                    debtValues = new ContentValues();

                                    debtValues.put(DbHelper.DEBTNAME, nameEntry);
                                    debtValues.put(DbHelper.DEBTPAYMENTS, amountEntry);
                                    debtValues.put(DbHelper.DEBTFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues, DbHelper.ID + "=?", args);

                                    debtValues2 = new ContentValues();
                                    debtValues2.put(DbHelper.DEBTEND, updateDebtEndDateExp());
                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues2, DbHelper.ID + "=?", args);

                                } catch (CursorIndexOutOfBoundsException e8) {
                                    e8.printStackTrace();
                                }

                                try {
                                    String[] args2 = new String[]{String.valueOf(findMatchingSavingsIdExp())};
                                    savingsValues = new ContentValues();

                                    savingsValues.put(DbHelper.SAVINGSNAME, nameEntry);
                                    savingsValues.put(DbHelper.SAVINGSPAYMENTS, amountEntry);
                                    savingsValues.put(DbHelper.SAVINGSFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues, DbHelper.ID + "=?", args2);

                                    savingsValues2 = new ContentValues();
                                    savingsValues2.put(DbHelper.SAVINGSDATE, updateSavingsDateExp());
                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues2, DbHelper.ID + "=?", args2);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
                                }


                                try {
                                    String[] args3 = new String[]{String.valueOf(expenseBudgetDb.getId())};
                                    moneyOutValues = new ContentValues();

                                    moneyOutValues.put(DbHelper.MONEYOUTCAT, nameEntry);
                                    moneyOutValues.put(DbHelper.MONEYOUTPRIORITY, priorityEntry);
                                    moneyOutValues.put(DbHelper.MONEYOUTWEEKLY, weeklyEntry);

                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues, DbHelper.EXPREFKEYMO + "=?", args3);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
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

                    findMatchingDebtIdExp();
                    findMatchingSavingsIdExp();
                    if (foundDebtIdExp || foundSavingsIdExp) {
                        deleteExpText.setVisibility(View.VISIBLE);
                        okButton.setVisibility(View.VISIBLE);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                backToBudget();
                            }
                        });
                    } else {
                        deleteExpText.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);

                        expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseDeleted.getTag();

                        dbManager.deleteExpense(expenseBudgetDb);
                        expenseAdapter.updateExpenses(dbManager.getExpense());
                        budgetHeaderText();
                        notifyDataSetChanged();
                    }
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
