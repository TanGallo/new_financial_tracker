package ca.gotchasomething.mynance;

import android.content.Intent;
import android.graphics.Color;
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

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class AddDebt extends LayoutDebt {

    Button cancelDebtButton, saveDebtButton, updateDebtButton;
    DbManager dbManager;
    DebtDb debt;
    Double debtAmount = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0, debtLimit = 0.0, debtPayments = 0.0, debtRate = 0.0, debtToPay = 0.0, expenseAmount = 0.0,
            expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseAnnualAmount = 0.0, expenseFrequency = 0.0, incomeAmount = 0.0,
            incomeAnnualAmount = 0.0, incomeFrequency = 0.0;
    EditText debtAmountEntry, debtLimitEntry, debtNameEntry, debtPaymentsEntry, debtPercentEntry;
    ExpenseBudgetDb expenseBudgetDb;
    IncomeBudgetDb incomeBudgetDb;
    Intent backToDebtLayout, showList;
    LinearLayout toastLayout;
    long expRefKeyD, incRefKeyD;
    RadioButton debtBiWeeklyRadioButton, debtMonthlyRadioButton, debtWeeklyRadioButton;
    RadioGroup debtFrequencyRadioGroup;
    String debtEnd = null, debtEnd2 = null, debtFrequencyS = null, debtName = null, expenseName = null, expensePriority = null, expenseWeekly = null, incomeName = null;
    TextView debtDateResult, debtDateResultLabel, tvToast;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_debt);

        dbManager = new DbManager(this);

        debtNameEntry = findViewById(R.id.debtNameEntry);
        debtLimitEntry = findViewById(R.id.debtLimitEntry);
        debtAmountEntry = findViewById(R.id.debtAmountEntry);
        debtPercentEntry = findViewById(R.id.debtPercentEntry);
        debtPaymentsEntry = findViewById(R.id.debtPaymentsEntry);
        debtFrequencyRadioGroup = findViewById(R.id.debtFrequencyRadioGroup);
        debtWeeklyRadioButton = findViewById(R.id.debtWeeklyRadioButton);
        debtBiWeeklyRadioButton = findViewById(R.id.debtBiWeeklyRadioButton);
        debtMonthlyRadioButton = findViewById(R.id.debtMonthlyRadioButton);
        debtDateResultLabel = findViewById(R.id.debtDateResultLabel);
        debtDateResult = findViewById(R.id.debtDateResult);
        saveDebtButton = findViewById(R.id.saveDebtButton);
        updateDebtButton = findViewById(R.id.updateDebtButton);
        updateDebtButton.setVisibility(View.GONE);
        cancelDebtButton = findViewById(R.id.cancelDebtButton);

        debtFrequencyRadioGroup.setOnCheckedChangeListener(onCheckDebtFrequency);
        cancelDebtButton.setOnClickListener(onClickCancelDebtButton);
        saveDebtButton.setOnClickListener(onClickSaveDebtButton);

        debtAmountEntry.addTextChangedListener(onChangeDebtAmount);
        debtPercentEntry.addTextChangedListener(onChangeDebtPercent);
        debtPaymentsEntry.addTextChangedListener(onChangeDebtPayments);
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

    RadioGroup.OnCheckedChangeListener onCheckDebtFrequency = new RadioGroup.OnCheckedChangeListener() {
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
                default:
                    debtFrequencyS = "12";
            }
        }
    };

    public void backToDebt() {
        backToDebtLayout = new Intent(AddDebt.this, LayoutDebt.class);
        backToDebtLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDebtLayout);
    }

    public void showList() {
        showList = new Intent(AddDebt.this, SetUpAddDebtsList.class);
        showList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(showList);
    }

    public void debtEndResult() {
        allDebtData();
        debtEnd2 = general.calcDebtDate(debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debtEnd2.equals(getString(R.string.debt_paid))) {
            debtDateResult.setTextColor(Color.parseColor("#03ac13"));
        } else if (debtEnd2.equals(getString(R.string.too_far))) {
            debtDateResult.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            debtDateResult.setTextColor(Color.parseColor("#303F9F"));
            debtDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
        }
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
        debtAnnualIncome = 0.0;
        debtToPay = 0.0;
    }

    View.OnClickListener onClickCancelDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dbManager.retrieveLatestDone().equals("bills")) {
                showList();
            } else {
                backToDebt();
            }
        }
    };

    final View.OnClickListener onClickSaveDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            debtEndResult();

            if (debtName != null) {

                incomeName = debtName;
                incomeAmount = 0.0;
                incomeFrequency = 1.0;
                incomeAnnualAmount = 0.0;

                incomeBudgetDb = new IncomeBudgetDb(
                        incomeName,
                        incomeAmount,
                        incomeFrequency,
                        incomeAnnualAmount,
                        0);

                dbManager.addIncome(incomeBudgetDb);

                expenseName = debtName;
                expenseAmount = debtPayments;
                expenseFrequency = debtFrequency;
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

                debtEnd = debtEnd2;

                expRefKeyD = dbManager.findLatestExpenseId();
                incRefKeyD = dbManager.findLatestIncomeId();

                debt = new DebtDb(
                        debtName,
                        debtLimit,
                        debtAmount,
                        debtRate,
                        debtPayments,
                        debtFrequency,
                        debtAnnualIncome,
                        debtEnd,
                        debtToPay,
                        expRefKeyD,
                        incRefKeyD,
                        0);

                dbManager.addDebt(debt);

                if (dbManager.retrieveLatestDone().equals("bills")) {
                    showList();
                } else {
                    debtHeaderText();
                    backToDebt();
                }
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}
