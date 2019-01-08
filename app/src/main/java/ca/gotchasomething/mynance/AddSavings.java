package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import ca.gotchasomething.mynance.data.SavingsDb;

public class AddSavings extends LayoutSavings {

    Button saveSavingsButton, updateSavingsButton, cancelSavingsButton;
    Calendar savingsCal;
    Date savingsDateD;
    DbManager dbManager;
    Double savingsAmount = 0.0, savingsRate = 0.0, savingsPayments = 0.0, savingsFrequency = 0.0, savingsGoal = 0.0, expenseAmount = 0.0, expenseFrequency = 0.0,
            expenseAnnualAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, frequency = 0.0, amount = 0.0, goal = 0.0, rate = 0.0, payments = 0.0,
            numberOfYearsToSavingsGoal = 0.0, numberOfInterestPeriods = 0.0, base = 0.0, exponent = 0.0, rate2 = 0.0, number = 0.0, getNumberOfInterestPeriods = 0.0,
            savingsIntFrequency = 0.0, intFrequency = 0.0, amountToGo = 0.0, rateMultiplier = 0.0, annualContributions = 0.0;
    EditText savingsNameEntry, savingsAmountEntry, savingsPercentEntry, savingsPaymentsEntry, savingsGoalAmountEntry;
    ExpenseBudgetDb expenseBudgetDb;
    Integer numberOfDaysToSavingsGoal = 0;
    Intent backToSavingsLayout, backToSavingsLayout2;
    long expRefKeyS;
    RadioButton savingsWeeklyRadioButton, savingsBiWeeklyRadioButton, savingsMonthlyRadioButton, savingsAnnuallyRadioButton,
            savingsIntMonthlyRadioButton, savingsIntAnnuallyRadioButton;
    RadioGroup savingsFrequencyRadioGroup, savingsIntFrequencyRadioGroup;
    SavingsDb saving;
    SimpleDateFormat savingsDateS;
    String savingsName = null, savingsDate = null, savingsFrequencyS = "1", expenseName = null, expensePriority = null, expenseWeekly = null, savingsIntFrequencyS = "12";
    TextView savingsDateResult, savingsFrequencyLabel;

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
            if (savingsPaymentsEntry.getText().toString().equals("0")) {
                savingsFrequencyLabel.setVisibility(View.GONE);
                savingsFrequencyRadioGroup.setVisibility(View.GONE);
                savingsFrequencyS = "1";
                //intFrequency = 12.0;
            } else {
                savingsFrequencyLabel.setVisibility(View.VISIBLE);
                savingsFrequencyRadioGroup.setVisibility(View.VISIBLE);
                //intFrequency = 1.0;
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
            }
        }
    };

    public String calcSavingsDate() {

        try {
            amount = Double.valueOf(savingsAmountEntry.getText().toString());
        } catch (Exception e) {
            amount = 0.0;
        }
        try {
            goal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
        } catch (Exception e2) {
            goal = 0.00;
        }
        try {
            rate2 = Double.valueOf(savingsPercentEntry.getText().toString());
            rate = rate2 / 100;
        } catch (Exception e3) {
            rate = 0.01;
        }
        if (rate == 0) {
            rate = .01;
        }
        try {
            payments = Double.valueOf(savingsPaymentsEntry.getText().toString());
        } catch (Exception e4) {
            payments = .01;
        }
        if (payments == 0) {
            payments = 0.1;
            savingsAnnuallyRadioButton.setChecked(true);
            frequency = 1.0;
        }
        frequency = Double.valueOf(savingsFrequencyS);
        intFrequency = Double.valueOf(savingsIntFrequencyS);

        savingsCal = Calendar.getInstance();

        //goal = compoundIntAndCap + contributionsTotal
        //goal = (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) + (((payments * frequency) / 12) * (((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency))
        //Double annualPayments = payments * frequency;
        //Double base = 1 + rate / intFrequency;
        //goal = (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) + ((annualPayments / 12) * (((base^(intFrequency * numberOfYearsToSavingsGoal)) - 1) / (rate / intFrequency)) * (base));
        //goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) = ((annualPayments / 12) * (((base^(intFrequency * numberOfYearsToSavingsGoal)) - 1) / (rate / intFrequency)) * (base));
        //goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) / base = (annualPayments / 12) * (((base^(intFrequency * numberOfYearsToSavingsGoal)) - 1) / (rate / intFrequency));
        //goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) / base / (annualPayments / 12) = (((base^(intFrequency * numberOfYearsToSavingsGoal)) - 1) / (rate / intFrequency));
        //goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) / base / (annualPayments / 12) * (rate / intFrequency) = ((base^(intFrequency * numberOfYearsToSavingsGoal)) - 1);
        //1 + (goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) / base / (annualPayments / 12) * (rate / intFrequency)) = (base^(intFrequency * numberOfYearsToSavingsGoal));
        //base^(intFrequency * numberOfYearsToSavingsGoal) = 1 + (goal - (amount * (base^(intFrequency * numberOfYearsToSavingsGoal))) / base / (annualPayments / 12) * (rate / intFrequency));


        //goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) = (((payments * frequency) / 12) * (((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency))
        //goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) / (1 + rate / intFrequency) = ((payments * frequency) / 12) * (((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1) / (rate / intFrequency))
        //goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) / (1 + rate / intFrequency) / ((payments * frequency) / 12) = (((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1) / (rate / intFrequency))
        //goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency) = ((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1)
        //1 + (goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency)) = (1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal)
        //(1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) = 1 + (goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)) / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency))
        //Double base = Math.log(1 + rate / intFrequency);
        //Double exponent = 1 + (goal - (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal2)) / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency)));
        //numberOfYearsToSavingsGoal = (Math.pow(base, exponent)) / intFrequency

        //5^x = 3
        //log5^5^x = log5^3
        //x=log5^3

        Double base = (1 + .1 / 12);
        Double exponent = 120.0;
        Double answer = (1000 * (Math.pow(base, exponent))) + (((100 * 12) / 12) * (((Math.pow(base, exponent)) - 1) / (.1 / 12)) * (1 + .1 / 12));

        //Double base = 1 + rate / intFrequency;
        //Double exponent = intFrequency * numberOfYearsToSavingsGoal;
        //Double answer = (amount * number) + (((payments * frequency) / 12) * ((number - 1) / (rate / intFrequency)) * (1 + rate / intFrequency));

        /*Double base = 1 + .1 / 12;
        Double exponent = 12.0 * 10;
        Double number = Math.pow(base, exponent);
        Double answer = (1000 * number) + (((100 * 12) / 12) * ((number - 1) / (.1 / 12)) * (1 + .1/12));*/ //23,362.24

        //compoundIntAndCap = (amount * (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal))
        //compoundIntAndCap / amount = (1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal)
        //log(1 + rate / intFrequency)^(1 + rate / intFrequency) ^(intFrequency * numberOfYearsToSavingsGoal) = log(1 + rate / intFrequency)^compoundIntAndCap / amount
        //intFrequency * numberOfYearsToSavingsGoal = log(1 + rate / intFrequency)^compoundIntAndCap / amount
        //numberOfYearsToSavingsGoal = (log(1 + rate / intFrequency)^compoundIntAndCap / amount) / intFrequency

        //contributionsTotal = (((payments * frequency) / 12) * (((1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency))
        //(1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) = 1 + (contributionsTotal / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency))
        //log(1 + rate / intFrequency)^(1 + rate / intFrequency)^(intFrequency * numberOfYearsToSavingsGoal) = log(1 + rate / intFrequency)^(1 + (contributionsTotal / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency)))
        //intFrequency * numberOfYearsToSavingsGoal = log(1 + rate / intFrequency)^(1 + (contributionsTotal / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency)))
        //numberOfYearsToSavingsGoal = (log(1 + rate / intFrequency)^(1 + (contributionsTotal / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency)))) / intFrequency

        /*Double base = Math.log(1 + rate / intFrequency);
        Double exponent1 = goal / amount;
        Double number1 = Math.pow(base, exponent1);
        Double exponent2 = 1 + (goal / (1 + rate / intFrequency) / ((payments * frequency) / 12) * (rate / intFrequency));
        Double number2 = Math.pow(base, exponent2);
        numberOfYearsToSavingsGoal = (number1 / intFrequency) + (number2 / intFrequency);*/

        /*Double base = Math.log(1 + .1 / 12);
        Double exponent1 = 23362.24 / 1000;
        Double number1 = Math.pow(base, exponent1);
        Double exponent2 = 1 + (23362.24 / (1 + .1 / 12) / ((100 * 12) / 12) * (.1 / 12));
        Double number2 = Math.pow(base, exponent2);
        numberOfYearsToSavingsGoal = (number1 / 12) + (number2 / 12);*/

        /*if(payments == 0.1) {
            numberOfInterestPeriods = -(Math.log10(1 - ((amountToGo + (amountToGo * (-rateMultiplier))) / (annualContributions))) / (intFrequency * Math.log10(1 + ((-rate) / intFrequency))));
        } else {
            numberOfInterestPeriods = -(Math.log(1 - ((amountToGo + (amountToGo * (-rateMultiplier))) + (annualContributions + (annualContributions * (-rateMultiplier))) / (annualContributions))) / (intFrequency * Math.log(1 + ((-rate) / intFrequency))));
        }*/

        //numberOfYearsToSavingsGoal = -(Math.log(1 - (amount * rate / (annualContributions))) / (frequency * Math.log(1 + (rate / frequency))));
        //numberOfYearsToSavingsGoal = -(Math.log10(1 - ((amount + (amount * (-rate))) / (annualContributions))) / (intFrequency * Math.log10(1 + ((-rate) / intFrequency))));
        //numberOfYearsToSavingsGoal = -(Math.log(1 - (10000 * .10 / (1200))) / (frequency * Math.log(1 + (rate / frequency))));
        //CLOSEST//numberOfInterestPeriods = -(Math.log10(1 - ((amountToGo + (amountToGo * (-rateMultiplier))) / (annualContributions))) / (intFrequency * Math.log10(1 + ((-rate) / intFrequency))));
        //numberOfYearsToSavingsGoal = numberOfInterestPeriods / intFrequency;
        //numberOfYearsToSavingsGoal = -(Math.log(1 - (5000 * .10 / (1200))) / (frequency * Math.log(1 + (rate / intFrequency))));

        /*Double rateMultiplier = 1 + rate;
        Double annualContributions = payments * frequency;
        numberOfYearsToSavingsGoal = (goal - amount) / (amount + (frequency * payments) + (intFrequency * ((amount + annualContributions) * (rateMultiplier))));*/
        //exponent = Math.log(intFrequency * rateMultiplier);
        //numberOfYearsToSavingsGoal = Math.pow(base, exponent);
        //numberOfYearsToSavingsGoal = Math.log10(amount / (frequency * payments) + (intFrequency * ((amount + annualContributions) * (rateMultiplier))));

        //Double amountToGo = goal - amount;
        //Double annualContributions = payments * frequency;
        //exponent = Math.log(rateMultiplier);
        //base = amount + (amount * rateMultiplier) + annualContributions + (annualContributions * rateMultiplier);
        //numberOfYearsToSavingsGoal = Math.pow(base, exponent);

        //futureValue = amount + (payment * ((((1 + rate) toPowOfnumberOfIntPeriods) - 1) / rate))
        //goal = amount + (payment * ((((1 + rate) toPowOfnumberOfIntPeriods) - 1) / rate))
        //(goal - amount) = (payment * ((((1 + rate) toPowOfnumberOfIntPeriods) - 1) / rate))

        //NPER calculation2 = -Log10(Payment/(payment+(capital*rate))/Log10(1+rate)
        //NPER calculation1 = Log10(Payment/(payment+capital+rate))/Log10((1+rate)
        //numberOfInterestPeriods = -Math.log10((payments * frequency) / ((payments * frequency) + ((payments * frequency) * rate) + (amount) + ((amount) * rate))) / Math.log10(1 + rate); //calc2
        //numberOfYearsToSavingsGoal = -(Math.log10(1 - ((goal - amount) * rate / (payments * frequency))) / (frequency * Math.log10(1 + (rate / frequency)))); //orig,but log10
        //numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * (-rate) + (payments * frequency) + ((payments * frequency) * (-rate)))) / (Math.log(1 + (-rate)))) / intFrequency; //orig, but neg rate
        //payments of $100 monthly, rate of 10%, goal of 10k, amount of $5K
        //num = -Math.log10((1200 / (1200 + (5000 * .1)) / Math.log10(1.1)
        //num = -Math.log10((1200 / (1200 + 500) / Math.log10(1.1)
        //num = -Math.log10(1200 / 1700) / Math.log10(1.1)
        //num = -.70588 / .04139
        //num = 17.0544 / intFrequency 12 = 1.421 1=17.0544
        //numberOfInterestPeriods = -Math.log10((payments * frequency) / ((payments * frequency) + ((goal - amount) + rate))) / Math.log10(1 + rate); //calc1
        //numberOfYearsToSavingsGoal = numberOfInterestPeriods / intFrequency;
        //payments of $0.10, rate of 30%, goal of 1M, amount of $125K
        //num = -Math.log10((0.1 / (0 + (875000 * .3)) / Math.log10(1.3)
        //num = -Math.log10((0.1 / (0 + (262500)) / Math.log10(1.3)
        //num = -Math.log10(0) / Math.log10(1.3)
        //num = --6.419129307687688 / .1139433523068367
        //num = 56.3361457955 / intFrequency 12 = 4.69  1=56

        //COMPOUND INTEREST WITH PAYMENTS: goal = payments * (((1 + rate)to the power of (numberOfYearsToSavingsGoal * frequency)) - 1) / rate;
        //rate * goal = payments * (((1 + rate)to the power of (numberOfYearsToSavingsGoal * frequency)) - 1)
        //(rate * goal) / payments = ((1 + rate)to the power of (numberOfYearsToSavingsGoal * frequency)) - 1
        //1 + ((rate * goal) / payments) = (1 + rate)--(numberOfYearsToSavingsGoal * frequency)
        //(1 + rate)--(numberOfYearsToSavingsGoal * frequency) = 1 + ((rate * goal) / payments)
        //log(1 + rate) --(1 + rate)--(numberOfYearsToSavingsGoal * frequency) = log(1 + rate) --1 + ((rate * goal) / payments)
        //numberOfYearsToSavingsGoal * frequency = log(1 + rate) -- 1 + ((rate * goal) / payments)
        //numberOfYearsToSavingsGoal = (log(1 + rate) -- 1 + ((rate * goal) / payments)) / frequency
        //5--x = 3
        //log5 --5--x = log5--3
        //x=log5--3

        //goal = amount(1 + rate) to the power of numberOfInterestPeriods (in years)
        //goal / amount = (1 + rate) to the power of numberOfInterestPeriods
        //log(1 + rate) (1 + rate)x = log(1 + rate) (goal / amount)
        //x = log(1 + rate)to the power of (goal / amount)
        //numberOfInterestPeriods = Math.log(1 + rate)to the power of (goal / amount)
        //numberOfInterestPeriods = Math.pow(Math.log(1 + rate), goal / amount)
        //amount = payments * frequency * compound interest
        //base = Math.log(1 + (rate / 100));
        //exponent = -(goal / amount);
        //exponent = -(goal / ((payments * frequency) * ((Math.log(1 + (-(rate / 100)))))));
        //numberOfYearsToSavingsGoal = Math.pow(base, exponent);

        //goal = amount(1 + (rate / 100) / frequency)to the power of frequency x years
        //goal / amount = (1 + (rate / 100) / frequency)to the power of frequency x years
        //frequency x years = log(1 + (rate / 100) / frequency) to the power of (goal / amount)
        //base =
        //exponent = goal / amount

        //ORIGINAL: numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * rate / (payments * frequency))) / (frequency * Math.log(1 + (rate / frequency))));
        //VERSION 2: numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * (-rate) / (payments * frequency))) / (frequency * Math.log(1 + ((-rate) / frequency))));
        //VERSION 3: numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * (-rate) / (payments * frequency))) / (frequency * Math.log(1 + ((-rate) / frequency))));
        //numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * (-rate) / (payments * frequency))) / (Math.log(1 + (-rate))));
        /*base = Math.log(1 + rate);
        exponent = 1 + ((rate * (goal-amount)) / payments);
        number = Math.pow(base, exponent);
        numberOfYearsToSavingsGoal = number / frequency;*/

        //numberOfYearsToSavingsGoal = (Math.log(1 + rate) -- 1 + ((rate * goal) / payments)) / frequency;
        //numberOfYearsToSavingsGoal = -(Math.log(1 - ((goal - amount) * (-rate) / (payments * frequency))) / (frequency * Math.log(1 + ((-rate) / frequency))));
        /*numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

        if ((goal - amount) <= 0) {
            savingsDate = getString(R.string.goal_achieved);

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal <= 0) {

            //Toast.makeText(getApplicationContext(), R.string.too_far, Toast.LENGTH_LONG).show();
            savingsDate = getString(R.string.too_far);

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = getString(R.string.goal_will) + " " + savingsDateS.format(savingsDateD);
        }*/
        savingsDate = String.valueOf(answer);

        return savingsDate;
    }

    View.OnClickListener onClickCancelSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            backToSavingsLayout2 = new Intent(AddSavings.this, LayoutSavings.class);
            backToSavingsLayout2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSavingsLayout2);
        }
    };

    final View.OnClickListener onClickSaveSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expenseName = savingsNameEntry.getText().toString();
            expenseAmount = Double.valueOf(savingsPaymentsEntry.getText().toString());
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
            savingsAmount = Double.valueOf(savingsAmountEntry.getText().toString());
            savingsGoal = Double.valueOf(savingsGoalAmountEntry.getText().toString());
            savingsPayments = Double.valueOf(savingsPaymentsEntry.getText().toString());
            savingsFrequency = Double.valueOf(savingsFrequencyS);
            savingsRate = Double.valueOf(savingsPercentEntry.getText().toString());
            if(savingsPayments == 0.0 || savingsPayments == 0.1) {
                savingsFrequency = 1.0;
            }
            savingsIntFrequency = Double.valueOf(savingsIntFrequencyS);
            expRefKeyS = dbManager.findLatestExpenseId();

            saving = new SavingsDb(
                    savingsName,
                    savingsAmount,
                    savingsGoal,
                    savingsPayments,
                    savingsFrequency,
                    savingsRate,
                    savingsIntFrequency,
                    savingsDate,
                    expRefKeyS,
                    0);

            dbManager.addSavings(saving);

            Toast toast = Toast.makeText(getBaseContext(), "This savings info has been saved to your BUDGET",
                    Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            savingsHeaderText();

            backToSavingsLayout = new Intent(AddSavings.this, LayoutSavings.class);
            backToSavingsLayout.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSavingsLayout);
        }
    };

}
