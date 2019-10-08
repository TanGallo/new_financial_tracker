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
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;

public class SetUp4AddSavings extends AppCompatActivity {

    AccountsDb sav1AcctDb;
    Button sav1CancelButton, sav1SaveButton, sav1UpdateButton;
    DbManager sav1DBMgr;
    Double expAnnAmtCalc = 0.0, years2 = 0.0, expenseAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseAnnualAmount = 0.0, expenseFrequency = 0.0,
            incomeAmount = 0.0, incomeAnnualAmount = 0.0, incomeFrequency = 0.0, savAmtFromEntry = 0.0, savingsAmount = 0.0, savAnnIncFromEntry = 0.0,
            savingsAnnualIncome = 0.0, savFrqFromEntry = 0.0, savingsFrequency = 0.0,
            savGoalFromEntry = 0.0, savingsGoal = 0.0, savPaytFromEntry = 0.0, savingsPayments = 0.0, savRateFromEntry = 0.0, savingsRate = 0.0;
    EditText sav1SavAmtET, sav1SavGoalET, sav1SavNameET, sav1SavPaytET, sav1SavPercentET;
    ExpenseBudgetDb sav1ExpDB;
    General sav1Gen;
    IncomeBudgetDb sav1IncDB;
    Intent backToSavingsLayout, sav1ShwLst;
    LinearLayout toastLayout;
    long sav1ExpRefKeyS, sav1IncRefKeyS, sav1SavId;
    RadioButton sav1AnnlyRadioButton, sav1BiWklyRadioButton, sav1MthlyRadioButton, sav1WklyRadioButton, sav1SepYesRadioButton, sav1SepNoRadioButton;
    RadioGroup sav1FrqRadioGroup, sav1SepRadioGroup;
    SavingsDb sav1SavDB;
    String expenseName = null, expensePriority = null, expenseWeekly = null, incomeName = null, savingsDate = null, sav1SavDate = null,
            sav1FrqRB = null, savNameFromEntry = null, savingsName = null, savSepFromEntry = null, savingsSeparate = null, sav1SepRB = null,
            savingsSeparate2 = null;
    TextView sav1DateResTV, sav1DateResLabel, savingsFrequencyLabel, tv;
    //Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_4_add_savings);

        sav1DBMgr = new DbManager(this);
        sav1Gen = new General();

        sav1SavNameET = findViewById(R.id.addSavNameET);
        //sav1SepRadioGroup = findViewById(R.id.addSavSepRG);
        //sav1SepYesRadioButton = findViewById(R.id.addSavSepYesRB);
        //sav1SepNoRadioButton = findViewById(R.id.addSavSepNoRB);
        sav1SavAmtET = findViewById(R.id.addSavAmtET);
        sav1SavPercentET = findViewById(R.id.addSavPercentET);
        sav1SavPaytET = findViewById(R.id.addSavPaytET);
        /*sav1FrqRadioGroup = findViewById(R.id.addSavFrqRG);
        sav1WklyRadioButton = findViewById(R.id.addSavWklyRB);
        sav1BiWklyRadioButton = findViewById(R.id.addSavBiWklyRB);
        sav1MthlyRadioButton = findViewById(R.id.addSavMthlyRB);
        sav1AnnlyRadioButton = findViewById(R.id.addSavAnnlyRB);
        sav1AnnlyRadioButton.setChecked(true);*/
        /*sav1SavGoalET = findViewById(R.id.addSavGoalET);
        sav1DateResTV = findViewById(R.id.addSavDateResTV);
        sav1DateResLabel = findViewById(R.id.addSavDateResLabel);
        sav1SaveButton = findViewById(R.id.addSavSaveBtn);
        sav1UpdateButton = findViewById(R.id.addSavUpdateBtn);
        sav1UpdateButton.setVisibility(View.GONE);
        sav1CancelButton = findViewById(R.id.addSavCancelBtn);

        //sav1SepRadioGroup.setOnCheckedChangeListener(onCheckSav1SepRadioGroup);
        //sav1FrqRadioGroup.setOnCheckedChangeListener(onCheckSav1FrqRadioGroup);
        sav1CancelButton.setOnClickListener(onClickCancelSav1CancelButton);
        sav1SaveButton.setOnClickListener(onClickSav1SaveButton);

        sav1SavAmtET.addTextChangedListener(onChangeSav1SavAmtET);
        sav1SavGoalET.addTextChangedListener(onChangeSav1SavGoalET);
        sav1SavPaytET.addTextChangedListener(onChangeSav1SavPaytET);
        sav1SavPercentET.addTextChangedListener(onChangeSav1SavPercentET);
    }

    TextWatcher onChangeSav1SavAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav1SavDateRes();
            sav1DateResTV.setText(sav1SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav1SavGoalET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav1SavDateRes();
            sav1DateResTV.setText(sav1SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav1SavPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav1SavDateRes();
            sav1DateResTV.setText(sav1SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav1SavPercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav1SavDateRes();
            sav1DateResTV.setText(sav1SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /*RadioGroup.OnCheckedChangeListener onCheckSav1FrqRadioGroup = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.addSavWklyRB:
                    sav1FrqRB = "52";
                    sav1SavDateRes();
                    sav1DateResTV.setText(sav1SavDate);
                    break;
                case R.id.addSavBiWklyRB:
                    sav1FrqRB = "26";
                    sav1SavDateRes();
                    sav1DateResTV.setText(sav1SavDate);
                    break;
                case R.id.addSavMthlyRB:
                    sav1FrqRB = "12";
                    sav1SavDateRes();
                    sav1DateResTV.setText(sav1SavDate);
                    break;
                case R.id.addSavAnnlyRB:
                    sav1FrqRB = "1";
                    sav1SavDateRes();
                    sav1DateResTV.setText(sav1SavDate);
                    break;
                default:
                    sav1FrqRB = "1";
            }
        }
    };*/

    /*RadioGroup.OnCheckedChangeListener onCheckSav1SepRadioGroup = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.addSavSepYesRB:
                    sav1SepRB = "Y";
                    break;
                case R.id.addSavSepNoRB:
                    sav1SepRB = "N";
                    break;
                default:
                    sav1SepRB = "Y";
            }
        }
    };*/

    /*public void sav1ShwLst() {
        sav1ShwLst = new Intent(SetUp4AddSavings.this, SetUp4AddSavingsList.class);
        sav1ShwLst.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(sav1ShwLst);
    }*/

    /*public void sav1SavDateRes() {
        sav1Gen.savingsDataFromEntries(
                sav1SavNameET,
                //sav1SepRB,
                sav1SavAmtET,
                sav1SavGoalET,
                sav1SavPercentET,
                sav1SavPaytET);
        sav1SavDate = sav1Gen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (sav1SavDate.equals(getString(R.string.goal_achieved))) {
            sav1DateResLabel.setVisibility(View.GONE);
            sav1DateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (sav1SavDate.equals(getString(R.string.too_far))) {
            sav1DateResLabel.setVisibility(View.GONE);
            sav1DateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            sav1DateResLabel.setVisibility(View.VISIBLE);
            sav1DateResTV.setTextColor(Color.parseColor("#303F9F"));
            sav1DateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
    }

    /*public void allSavingsData() {
        if (savingsNameEntry.getText().toString().equals("")) {
            savingsName = "null";
        } else {
            savingsName = savingsNameEntry.getText().toString();
        }
        savingsSeparate2 = savingsSeparateS;
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
    }*/

    /*View.OnClickListener onClickCancelSav1CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sav1Gen.intentMethod(sav1ShwLst, SetUp4AddSavings.this, SetUp4AddSavingsList.class);
            //sav1ShwLst();
        }
    };

    final View.OnClickListener onClickSav1SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            sav1SavDateRes();
            //savingsDate = sav1SavDate;
            //savingsSeparate = savingsSeparate2;

            if (!savNameFromEntry.equals("null")) {

                /*incomeName = savingsName;
                incomeAmount = 0.0;
                incomeFrequency = 1.0;
                incomeAnnualAmount = 0.0;*/

                /*sav1IncDB = new IncomeBudgetDb(
                        savNameFromEntry,
                        0.0,
                        1.0,
                        0.0,
                        0
                );

                sav1DBMgr.addIncome(sav1IncDB);*/

                /*expenseName = savingsName;
                expenseAmount = savingsPayments;
                expenseFrequency = savingsFrequency;
                expensePriority = "A";
                expenseWeekly = "N";*/
                /*expenseAAnnualAmount = expenseAnnualAmount;
                expenseBAnnualAmount = 0.0;*/

                /*expAnnAmtCalc = savPaytFromEntry * savFrqFromEntry;

                sav1ExpDB = new ExpenseBudgetDb(
                        savNameFromEntry,
                        savPaytFromEntry,
                        savFrqFromEntry,
                        "A",
                        "N",
                        expAnnAmtCalc,
                        expAnnAmtCalc,
                        0.0,
                        0);

                sav1DBMgr.addExpense(sav1ExpDB);*/

                //sav1ExpRefKeyS = sav1DBMgr.findLatestExpenseId();
                //sav1IncRefKeyS = sav1DBMgr.findLatestIncomeId();

                /*sav1SavDB = new SavingsDb(
                        savNameFromEntry,
                        //savSepFromEntry,
                        savAmtFromEntry,
                        savGoalFromEntry,
                        savPaytFromEntry,
                        savRateFromEntry,
                        (savPaytFromEntry * 12.0),
                        sav1SavDate,
                        //sav1ExpRefKeyS,
                        //sav1IncRefKeyS,
                        0);

                sav1DBMgr.addSavings(sav1SavDB);

                sav1SavId = sav1DBMgr.findLatestSavId();

                sav1AcctDb = new AccountsDb(
                        savNameFromEntry,
                        0,
                        sav1SavId,
                        0);

                sav1DBMgr.addAccounts(sav1AcctDb);

                sav1Gen.intentMethod(sav1ShwLst, SetUp4AddSavings.this, SetUp4AddSavingsList.class);
                //sav1ShwLst();

            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
