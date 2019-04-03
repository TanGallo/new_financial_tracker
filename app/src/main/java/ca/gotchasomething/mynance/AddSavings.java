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

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;

public class AddSavings extends LayoutSavings {

    Button cancelSavingsButton, saveSavingsButton, updateSavingsButton;
    DbManager dbManager;
    Double years2 = 0.0, expenseAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseAnnualAmount = 0.0, expenseFrequency = 0.0,
            incomeAmount = 0.0, incomeAnnualAmount = 0.0, incomeFrequency = 0.0, savingsAmount = 0.0, savingsAnnualIncome = 0.0, savingsFrequency = 0.0,
            savingsGoal = 0.0, savingsPayments = 0.0, savingsRate = 0.0;
    EditText savingsAmountEntry, savingsGoalAmountEntry, savingsNameEntry, savingsPaymentsEntry, savingsPercentEntry;
    ExpenseBudgetDb expenseBudgetDb;
    IncomeBudgetDb incomeBudgetDb;
    Intent backToSavingsLayout;
    LinearLayout toastLayout;
    long expRefKeyS, incRefKeyS;
    RadioButton savingsAnnuallyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton,
            savingsWeeklyRadioButton;
    RadioGroup savingsFrequencyRadioGroup;
    SavingsDb saving;
    String expenseName = null, expensePriority = null, expenseWeekly = null, incomeName = null, savingsDate = null, savingsDate2 = null,
            savingsFrequencyS = null, savingsName = null;
    TextView savingsDateResult, savingsDateResultLabel, savingsFrequencyLabel, tv;
    Toast toast;

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
        savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
        savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
        savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
        savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);
        savingsAnnuallyRadioButton.setChecked(true);
        savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
        savingsDateResult = findViewById(R.id.savingsDateResult);
        savingsDateResultLabel = findViewById(R.id.savingsDateResultLabel);
        saveSavingsButton = findViewById(R.id.saveSavingsButton);
        updateSavingsButton = findViewById(R.id.updateSavingsButton);
        updateSavingsButton.setVisibility(View.GONE);
        cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

        savingsFrequencyRadioGroup.setOnCheckedChangeListener(onCheckSavingsFrequency);
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

    RadioGroup.OnCheckedChangeListener onCheckSavingsFrequency = new RadioGroup.OnCheckedChangeListener() {
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
                default:
                    savingsFrequencyS = "1";
            }
        }
    };

    public void backToSavings() {
        backToSavingsLayout = new Intent(AddSavings.this, LayoutSavings.class);
        backToSavingsLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSavingsLayout);
    }

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
        if (savingsDate2.equals(getString(R.string.goal_achieved)) || savingsDate2.equals(getString(R.string.too_far))) {
            savingsDateResultLabel.setVisibility(View.GONE);
        } else {
            savingsDateResultLabel.setVisibility(View.VISIBLE);
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
        savingsAnnualIncome = 0.0;

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

    View.OnClickListener onClickCancelSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToSavings();
        }
    };

    final View.OnClickListener onClickSaveSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            savingsDateResult();
            savingsDate = savingsDate2;

            if (savingsName != "null") {

                incomeName = savingsName;
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

                expenseName = savingsName;
                expenseAmount = savingsPayments;
                expenseFrequency = savingsFrequency;
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

                expRefKeyS = dbManager.findLatestExpenseId();
                incRefKeyS = dbManager.findLatestIncomeId();

                saving = new SavingsDb(
                        savingsName,
                        savingsAmount,
                        savingsGoal,
                        savingsPayments,
                        savingsFrequency,
                        savingsRate,
                        savingsAnnualIncome,
                        savingsDate,
                        expRefKeyS,
                        incRefKeyS,
                        0);

                dbManager.addSavings(saving);

                toast = Toast.makeText(getBaseContext(), R.string.savings_saved,
                        Toast.LENGTH_LONG);
                toastLayout = (LinearLayout) toast.getView();
                tv = (TextView) toastLayout.getChildAt(0);
                tv.setTextSize(20);
                toast.show();

                savingsHeaderText();
                backToSavings();
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}
