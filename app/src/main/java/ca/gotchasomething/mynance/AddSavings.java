package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;

public class AddSavings extends LayoutSavings {

    Button cancelSavingsButton, saveSavingsButton, updateSavingsButton;
    Calendar savingsCal;
    Date savingsDateD;
    DbManager dbManager;
    Double amount = 0.0, expenseAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseAnnualAmount = 0.0, expenseFrequency = 0.0, frequency = 0.0,
            goal = 0.0, incomeAmount = 0.0, incomeAnnualAmount = 0.0, incomeFrequency = 0.0, intFrequency = 0.0, payments = 0.0, rate = 0.0, rate2 = 0.0,
            savingsAmount = 0.0, savingsAnnualIncome = 0.0, savingsFrequency = 0.0, savingsGoal = 0.0, savingsIntFrequency = 0.0, savingsPayments = 0.0,
            savingsRate = 0.0, years = 0.0;
    EditText savingsAmountEntry, savingsGoalAmountEntry, savingsNameEntry, savingsPaymentsEntry, savingsPercentEntry;
    ExpenseBudgetDb expenseBudgetDb;
    IncomeBudgetDb incomeBudgetDb;
    Integer numberOfDaysToSavingsGoal = 0;
    Intent backToSavingsLayout;
    long expRefKeyS, incRefKeyS;
    RadioButton savingsAnnuallyRadioButton, savingsBiWeeklyRadioButton, savingsIntAnnuallyRadioButton, savingsIntMonthlyRadioButton, savingsMonthlyRadioButton,
            savingsWeeklyRadioButton;
    RadioGroup savingsFrequencyRadioGroup, savingsIntFrequencyRadioGroup;
    SavingsDb saving;
    SimpleDateFormat savingsDateS;
    String expenseName = null, expensePriority = null, expenseWeekly = null, incomeName = null, savingsDate = null, savingsFrequencyS = null, savingsName = null,
            savingsIntFrequencyS = null;
    TextView savingsDateResult, savingsFrequencyLabel, savingsIntFrequencyLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_savings);

        dbManager = new DbManager(this);

        savingsNameEntry = findViewById(R.id.savingsNameEntry);
        savingsAmountEntry = findViewById(R.id.savingsAmountEntry);
        savingsPercentEntry = findViewById(R.id.savingsPercentEntry);
        savingsPaymentsEntry = findViewById(R.id.savingsPaymentsEntry);
        savingsFrequencyLabel = findViewById(R.id.savingsFrequencyLabel);
        savingsFrequencyLabel.setVisibility(View.VISIBLE);
        savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);
        savingsIntFrequencyLabel = findViewById(R.id.savingsIntFrequencyLabel);
        savingsIntFrequencyRadioGroup = findViewById(R.id.savingsIntFrequencyRadioGroup);
        savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
        savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
        savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
        savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);
        savingsAnnuallyRadioButton.setChecked(true);
        savingsIntMonthlyRadioButton = findViewById(R.id.savingsIntMonthlyRadioButton);
        savingsIntAnnuallyRadioButton = findViewById(R.id.savingsIntAnnuallyRadioButton);
        savingsIntMonthlyRadioButton.setChecked(true);
        savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
        savingsDateResult = findViewById(R.id.savingsDateResult);
        saveSavingsButton = findViewById(R.id.saveSavingsButton);
        updateSavingsButton = findViewById(R.id.updateSavingsButton);
        updateSavingsButton.setVisibility(View.GONE);
        cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

        savingsFrequencyRadioGroup.setOnCheckedChangeListener(onCheckSavingsFrequency);
        savingsIntFrequencyRadioGroup.setOnCheckedChangeListener(onCheckSavingsIntFrequency);
        cancelSavingsButton.setOnClickListener(onClickCancelSavingsButton);
        saveSavingsButton.setOnClickListener(onClickSaveSavingsButton);

        savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);
        savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);
        savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);
        savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);
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

    RadioGroup.OnCheckedChangeListener onCheckSavingsFrequency = new RadioGroup.OnCheckedChangeListener() {
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
                default:
                    savingsFrequencyS = "1";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckSavingsIntFrequency = new RadioGroup.OnCheckedChangeListener() {
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
                default:
                    savingsIntFrequencyS = "12";
            }
        }
    };

    public Double findNumberOfYears() {

        try {
            amount = Double.valueOf(savingsAmountEntry.getText().toString());
        } catch (Exception e) {
            amount = 0.0;
        }
        try {
            goal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
        } catch (Exception e2) {
            goal = 0.0;
        }
        if (goal < amount) {
            goal = amount;
        }
        try {
            rate2 = Double.valueOf(savingsPercentEntry.getText().toString());
            rate = rate2 / 100;
        } catch (Exception e3) {
            rate = 0.01;
        }
        if (rate <= 0) {
            rate = .01;
            savingsIntAnnuallyRadioButton.setChecked(true);
        }
        try {
            payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
        } catch (Exception e4) {
            payments = .01;
        }

        if (payments <= 0) {
            payments = .01;
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
            while (goal >= (amount * (Math.pow((1 + rate / intFrequency), intFrequency * years))) + (((payments * frequency) / 12) * (((Math.pow((1 + rate / intFrequency), intFrequency * years)) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency)));
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
            savingsDateS = new SimpleDateFormat(getString(R.string.simple_date_format));
            savingsDate = getString(R.string.goal_will) + " " + savingsDateS.format(savingsDateD);
        }

        return savingsDate;
    }

    public void backToSavings() {
        backToSavingsLayout = new Intent(AddSavings.this, LayoutSavings.class);
        backToSavingsLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSavingsLayout);
    }

    View.OnClickListener onClickCancelSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToSavings();
        }
    };

    final View.OnClickListener onClickSaveSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(savingsNameEntry.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                incomeName = savingsNameEntry.getText().toString();
                incomeAmount = 0.0;
                incomeFrequency = 1.0;
                incomeAnnualAmount = 0.0;

                incomeBudgetDb = new IncomeBudgetDb(
                        incomeName,
                        incomeAmount,
                        incomeFrequency,
                        incomeAnnualAmount,
                        0
                );

                dbManager.addIncome(incomeBudgetDb);

                expenseName = savingsNameEntry.getText().toString();
                if(savingsPaymentsEntry.getText().toString().equals("")) {
                    expenseAmount = 0.0;
                } else {
                    expenseAmount = Double.valueOf(savingsPaymentsEntry.getText().toString());
                }
                expenseFrequency = Double.valueOf(savingsFrequencyS);
                expensePriority = "A";
                expenseWeekly = "N";
                expenseAnnualAmount = expenseAmount * expenseFrequency;
                expenseAAnnualAmount = expenseAnnualAmount;
                expenseBAnnualAmount = 0.0;

                expenseBudgetDb = new ExpenseBudgetDb(
                        expenseName,
                        expenseAmount,
                        expenseFrequency,
                        expensePriority,
                        expenseWeekly,
                        expenseAnnualAmount,
                        expenseAAnnualAmount,
                        expenseBAnnualAmount,
                        0);

                dbManager.addExpense(expenseBudgetDb);

                savingsName = savingsNameEntry.getText().toString();
                if(savingsAmountEntry.getText().toString().equals("")) {
                    savingsAmount = 0.0;
                } else {
                    savingsAmount = Double.valueOf(savingsAmountEntry.getText().toString());
                }
                if(savingsGoalAmountEntry.getText().toString().equals("")) {
                    savingsGoal = 0.0;
                } else {
                    savingsGoal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
                }
                if(savingsPaymentsEntry.getText().toString().equals("")) {
                    savingsPayments = 0.0;
                } else {
                    savingsPayments = Double.valueOf(savingsPaymentsEntry.getText().toString());
                }
                savingsFrequency = Double.valueOf(savingsFrequencyS);
                if(savingsPercentEntry.getText().toString().equals("")) {
                    savingsRate = 0.0;
                } else {
                    savingsRate = Double.valueOf(savingsPercentEntry.getText().toString());
                }
                savingsIntFrequency = Double.valueOf(savingsIntFrequencyS);
                savingsAnnualIncome = 0.0;
                savingsDate = calcSavingsDate();
                expRefKeyS = dbManager.findLatestExpenseId();
                incRefKeyS = dbManager.findLatestIncomeId();

                saving = new SavingsDb(
                        savingsName,
                        savingsAmount,
                        savingsGoal,
                        savingsPayments,
                        savingsFrequency,
                        savingsRate,
                        savingsIntFrequency,
                        savingsAnnualIncome,
                        savingsDate,
                        expRefKeyS,
                        incRefKeyS,
                        0);

                dbManager.addSavings(saving);

                Toast toast = Toast.makeText(getBaseContext(), R.string.savings_saved,
                        Toast.LENGTH_LONG);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView tv = (TextView) toastLayout.getChildAt(0);
                tv.setTextSize(20);
                toast.show();

                savingsHeaderText();
                backToSavings();
            }
        }
    };

}
