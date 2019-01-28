package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class AddDebt extends LayoutDebt {

    Button saveDebtButton, updateDebtButton, cancelDebtButton;
    Calendar debtCal;
    Cursor expenseCursor2;
    Date debtEndD;
    DbHelper expenseDbHelper2;
    DbManager dbManager;
    DebtDb debt;
    Double debtAmount = 0.0, debtRate = 0.0, debtPayments = 0.0, debtFrequency = 0.0, expenseAmount = 0.0, expenseFrequency = 0.0, expenseAnnualAmount = 0.0,
            expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, amount = 0.0, rate = 0.0, frequency = 0.0, payments = 0.0, numberOfYearsToPayDebt = 0.0,
            incomeAmount = 0.0, incomeFrequency = 0.0, incomeAnnualAmount = 0.0, debtLimit = 0.0, debtAnnualIncome = 0.0;
    EditText debtNameEntry, debtLimitEntry, debtAmountEntry, debtPercentEntry, debtPaymentsEntry;
    ExpenseBudgetDb expenseBudgetDb;
    IncomeBudgetDb incomeBudgetDb;
    Integer numberOfDaysToPayDebt = 0;
    Intent backToDebtLayout, backToDebtLayout2;
    long expRefKeyD, incRefKeyD, idResult;
    RadioButton debtWeeklyRadioButton, debtBiWeeklyRadioButton, debtMonthlyRadioButton;
    RadioGroup debtFrequencyRadioGroup;
    SimpleDateFormat debtEndS;
    SQLiteDatabase db, expenseDb2;
    String debtName = null, debtEnd = null, debtFrequencyS = null, expenseName = null, expensePriority = null, expenseWeekly = null, debtEndString = null,
            incomeName = null;
    TextView debtDateResult;

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

    RadioGroup.OnCheckedChangeListener onCheckDebtFrequency = new RadioGroup.OnCheckedChangeListener() {
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
    };

    public String calcDebtDate() {

        try {
            amount = Double.valueOf(debtAmountEntry.getText().toString());
        } catch(Exception e) {
            amount = 0.0;
        }
        try {
            rate = Double.valueOf(debtPercentEntry.getText().toString());
        } catch(Exception e2) {
            rate = 0.0;
        }
        try {
            payments = Double.valueOf(debtPaymentsEntry.getText().toString());
        } catch(Exception e3) {
            payments = 0.0;
        }
        try {
            frequency = Double.valueOf(debtFrequencyS);
        } catch(Exception e4) {
            frequency = 0.0;
        }

        debtCal = Calendar.getInstance();
        numberOfYearsToPayDebt = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (amount <= 0) {
            debtEnd = getString(R.string.debt_paid);

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = getString(R.string.too_far);

        } else {
            debtCal = Calendar.getInstance();
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = getString(R.string.debt_will) + " " + debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    View.OnClickListener onClickCancelDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            backToDebtLayout2 = new Intent(AddDebt.this, LayoutDebt.class);
            backToDebtLayout2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDebtLayout2);
        }
    };

    final View.OnClickListener onClickSaveDebtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            incomeName = debtNameEntry.getText().toString();
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

            expenseName = debtNameEntry.getText().toString();
            expenseAmount = Double.valueOf(debtPaymentsEntry.getText().toString());
            expenseFrequency = Double.valueOf(debtFrequencyS);
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

            debtName = debtNameEntry.getText().toString();
            debtLimit = Double.valueOf(debtLimitEntry.getText().toString());
            debtAmount = Double.valueOf(debtAmountEntry.getText().toString());
            debtRate = Double.valueOf(debtPercentEntry.getText().toString());
            debtPayments = Double.valueOf(debtPaymentsEntry.getText().toString());
            debtFrequency = Double.valueOf(debtFrequencyS);
            debtAnnualIncome = 0.0;
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
                    expRefKeyD,
                    incRefKeyD,
                    0);

            dbManager.addDebt(debt);

            Toast toast = Toast.makeText(getBaseContext(), "This debt has been saved to your BUDGET",
                    Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            debtHeaderText();

            backToDebtLayout = new Intent(AddDebt.this, LayoutDebt.class);
            backToDebtLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDebtLayout);
        }
    };

}
