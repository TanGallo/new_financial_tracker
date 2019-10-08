/*package ca.gotchasomething.mynance;

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
import androidx.appcompat.app.AppCompatActivity;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class SetUp3AddDebts extends AppCompatActivity {

    AccountsDb debt1AcctDb;
    Button debt1CancelButton, debt1SaveButton, debt1UpdateButton;
    DbManager debt1DBMgr;
    DebtDb debt1DebtDb;
    Double debtAmtFromEntry = 0.0, debtLimitFromEntry = 0.0, debtRateFromEntry = 0.0, debtPaytFromEntry = 0.0, debtFrqFromEntry = 0.0, debtAnnPaytFromEntry = 0.0,
            debtToPayFromEntry = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0, debtLimit = 0.0, debtPayments = 0.0, debtRate = 0.0, debtToPay = 0.0,
            expAnnAmtCalc = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseAnnualAmount = 0.0, expenseFrequency = 0.0,
            incomeAmount = 0.0, incomeAnnualAmount = 0.0, incomeFrequency = 0.0;
    EditText debt1AmtET, debt1LimitET, debt1NameET, debt1PaytsET, debt1PercentET;
    ExpenseBudgetDb debt1ExpDB;
    General debt1Gen;
    IncomeBudgetDb debt1IncDB;
    Intent debt1ShwLst;
    LinearLayout debt1ToastLayout;
    long debt1DebtId, debt1ExpRefKeyD, debt1IncRefKeyD;
    RadioButton debt1BiWklyRadioButton, debt1MthlyRadioButton, debt1WklyRadioButton;
    RadioGroup debt1FrqRadioGroup;
    String debtNameFromEntry = null, debtEnd = null, debt1DebtEnd = null, debt1FrqRB = null, debtName = null, expenseName = null, expensePriority = null, expenseWeekly = null,
            incomeName = null;
    TextView debt1DateRes, debt1DateResLabel, debt1ToastTV;
    Toast debt1Toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_3_add_debt);

        debt1DBMgr = new DbManager(this);
        debt1Gen = new General();

        debt1NameET = findViewById(R.id.addDebtNameET);
        debt1LimitET = findViewById(R.id.addDebtLimitET);
        debt1AmtET = findViewById(R.id.addDebtAmtET);
        debt1PercentET = findViewById(R.id.addDebtRateET);
        debt1PaytsET = findViewById(R.id.addDebtPaytET);
        /*debt1FrqRadioGroup = findViewById(R.id.addDebtFrqRG);
        debt1WklyRadioButton = findViewById(R.id.addDebtWklyRB);
        debt1BiWklyRadioButton = findViewById(R.id.addDebtBiWklyRB);
        debt1MthlyRadioButton = findViewById(R.id.addDebtMthlyRB);*/
        /*debt1DateResLabel = findViewById(R.id.addDebtDateResLabel);
        debt1DateRes = findViewById(R.id.addDebtDateResTV);
        debt1SaveButton = findViewById(R.id.addDebtSaveBtn);
        debt1UpdateButton = findViewById(R.id.addDebtUpdateBtn);
        debt1UpdateButton.setVisibility(View.GONE);
        debt1CancelButton = findViewById(R.id.addDebtCancelBtn);

        //debt1FrqRadioGroup.setOnCheckedChangeListener(onCheckDebt1Frq);
        debt1CancelButton.setOnClickListener(onClickDebt1CancelButton);
        debt1SaveButton.setOnClickListener(onClickDebt1SaveButton);

        debt1AmtET.addTextChangedListener(onChangeDebt1AmtET);
        debt1PercentET.addTextChangedListener(onChangeDebt1PercentET);
        debt1PaytsET.addTextChangedListener(onChangeDebt1PaytsET);

        if(debt1DBMgr.getExpense().size() == 0) {
            debt1ExpDB = new ExpenseBudgetDb(
                    "Other",
                    0.0,
                    12.0,
                    "B",
                    "N",
                    0.0,
                    0.0,
                    0.0,
                    0);
            debt1DBMgr.addExpense(debt1ExpDB);
        }
    }

    TextWatcher onChangeDebt1AmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt1DebtEndRes();
            debt1DateRes.setText(debt1DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt1PercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt1DebtEndRes();
            debt1DateRes.setText(debt1DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt1PaytsET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt1DebtEndRes();
            debt1DateRes.setText(debt1DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /*RadioGroup.OnCheckedChangeListener onCheckDebt1Frq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.addDebtWklyRB:
                    debt1FrqRB = "52";
                    debt1DebtEndRes();
                    debt1DateRes.setText(debt1DebtEnd);
                    break;
                case R.id.addDebtBiWklyRB:
                    debt1FrqRB = "26";
                    debt1DebtEndRes();
                    debt1DateRes.setText(debt1DebtEnd);
                    break;
                case R.id.addDebtMthlyRB:
                    debt1FrqRB = "12";
                    debt1DebtEndRes();
                    debt1DateRes.setText(debt1DebtEnd);
                    break;
                default:
                    debt1FrqRB = "12";
            }
        }
    };*/

    /*public void debt1ShwLst() {
        debt1ShwLst = new Intent(SetUp3AddDebts.this, SetUp3AddDebtsList.class);
        debt1ShwLst.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(debt1ShwLst);
    }*/

    /*public void debt1DebtEndRes() {
        debt1Gen.debtDataFromEntries(
                debt1NameET,
                debt1LimitET,
                debt1AmtET,
                debt1PercentET,
                debt1PaytsET,
                0.0);
        debt1DebtEnd = debt1Gen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debt1DebtEnd.equals(getString(R.string.debt_paid))) {
            debt1DateRes.setTextColor(Color.parseColor("#03ac13"));
        } else if (debt1DebtEnd.equals(getString(R.string.too_far))) {
            debt1DateRes.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            debt1DateRes.setTextColor(Color.parseColor("#303F9F"));
            debt1DateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
        debt1Gen.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), debt1DateResLabel, debt1DateRes);
    }

    /*public void allDebtData() {
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
    }*/

    /*View.OnClickListener onClickDebt1CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debt1Gen.intentMethod(debt1ShwLst, SetUp3AddDebts.this, SetUp3AddDebtsList.class);
        }
    };

    final View.OnClickListener onClickDebt1SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            debt1DebtEndRes();

            if(!debtNameFromEntry.equals("null")) {

            //if (debtName != null) {

                /*incomeName = debtName;
                incomeAmount = 0.0;
                incomeFrequency = 1.0;
                incomeAnnualAmount = 0.0;*/

                /*debt1IncDB = new IncomeBudgetDb(
                        debtNameFromEntry,
                        0.0,
                        1.0,
                        0.0,
                        0);

                debt1DBMgr.addIncome(debt1IncDB);*/

                /*expenseName = debtName;
                expenseAmount = debtPayments;
                expenseFrequency = debtFrequency;
                expensePriority = "A";
                expenseWeekly = "N";*/
                //expenseAAnnualAmount = expenseAnnualAmount;
                //expenseBAnnualAmount = 0.0;

                /*expAnnAmtCalc = debtPaytFromEntry * debtFrqFromEntry;

                debt1ExpDB = new ExpenseBudgetDb(
                        debtNameFromEntry,
                        debtPaytFromEntry,
                        debtFrqFromEntry,
                        "A",
                        "N",
                        expAnnAmtCalc,
                        expAnnAmtCalc,
                        0.0,
                        0);

                debt1DBMgr.addExpense(debt1ExpDB);*/

                //debtEnd = debt1DebtEnd;

                /*debt1ExpRefKeyD = debt1DBMgr.findLatestExpenseId();
                debt1IncRefKeyD = debt1DBMgr.findLatestIncomeId();*/

                /*debt1DebtDb = new DebtDb(
                        debtNameFromEntry,
                        debtLimitFromEntry,
                        debtAmtFromEntry,
                        debtRateFromEntry,
                        debtPaytFromEntry,
                        (debtPaytFromEntry * 12.0),
                        debt1DebtEnd,
                        debtToPayFromEntry,
                        //debt1ExpRefKeyD,
                        //debt1IncRefKeyD,
                        0);

                debt1DBMgr.addDebt(debt1DebtDb);

                debt1DebtId = debt1DBMgr.findLatestDebtId();

                debt1AcctDb = new AccountsDb(
                        debtNameFromEntry,
                        debt1DebtId,
                        0,
                        0);

                debt1DBMgr.addAccounts(debt1AcctDb);

                debt1Gen.intentMethod(debt1ShwLst, SetUp3AddDebts.this, SetUp3AddDebtsList.class);
                //debt1ShwLst();

            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
