/*package ca.gotchasomething.mynance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddDebtsFromDebtLayout extends AppCompatActivity {

    AccountsDb debt5Acctdb;
    Button debt5SaveBtn, debt5CancelBtn, debt5UpdateBtn;
    DbManager debt5DbMgr;
    DebtDb debt5DebtDb;
    Double debtAmtFromEntry = 0.0, debtAnnIncFromEntry = 0.0, debtFrqFromEntry = 0.0, debtLimitFromEntry = 0.0, debtPaytFromEntry = 0.0,
            debtRateFromEntry = 0.0, debt5ExpAnnAmt = 0.0;
    EditText debt5DebtNameET, debt5DebtLimitET, debt5DebtAmtET, debt5DebtRateET, debt5DebtPaytET;
    ExpenseBudgetDb debt5ExpDb;
    General debt5Gen;
    IncomeBudgetDb debt5IncDb;
    Intent debt5ToDebtLayout;
    long debt5DebtId;
    RadioButton debt5BiWklyRB, debt5MthlyRB, debt5WklyRB;
    RadioGroup debt5FrqRG;
    String debt5DebtEnd2 = null, debt5DebtFrqRB = null, debtNameFromEntry = null;
    TextView debt5DebtDateResLabel, debt5DebtDateResTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_3_add_debt);

        debt5Gen = new General();
        debt5DbMgr = new DbManager(this);

        debt5DebtNameET = findViewById(R.id.addDebtNameET);
        debt5DebtLimitET = findViewById(R.id.addDebtLimitET);
        debt5DebtAmtET = findViewById(R.id.addDebtAmtET);
        debt5DebtRateET = findViewById(R.id.addDebtRateET);
        debt5DebtPaytET = findViewById(R.id.addDebtPaytET);
        debt5DebtDateResLabel = findViewById(R.id.addDebtDateResLabel);
        debt5DebtDateResTV = findViewById(R.id.addDebtDateResTV);
        debt5SaveBtn = findViewById(R.id.addDebtSaveBtn);
        debt5CancelBtn = findViewById(R.id.addDebtCancelBtn);
        debt5UpdateBtn = findViewById(R.id.addDebtUpdateBtn);
        debt5UpdateBtn.setVisibility(View.GONE);
        /*debt5FrqRG = findViewById(R.id.addDebtFrqRG);
        debt5WklyRB = findViewById(R.id.addDebtWklyRB);
        debt5BiWklyRB = findViewById(R.id.addDebtBiWklyRB);
        debt5MthlyRB = findViewById(R.id.addDebtMthlyRB);*/

        /*debt5CancelBtn.setOnClickListener(onClickDebt5CancelBtn);
        debt5SaveBtn.setOnClickListener(onClickDebt5SaveBtn);
        //debt5FrqRG.setOnCheckedChangeListener(onCheckDebt5FrqRG);

        debt5DebtAmtET.addTextChangedListener(onChangeDebt5DebtAmtET);
        debt5DebtRateET.addTextChangedListener(onChangeDebt5DebtRateET);
        debt5DebtPaytET.addTextChangedListener(onChangeDebt5DebtPaytET);
    }

    TextWatcher onChangeDebt5DebtAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt5DebtEndRes();
            debt5DebtDateResTV.setText(debt5DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt5DebtRateET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt5DebtEndRes();
            debt5DebtDateResTV.setText(debt5DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt5DebtPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt5DebtEndRes();
            debt5DebtDateResTV.setText(debt5DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //handle radioGroup for incomeFrequency
    /*RadioGroup.OnCheckedChangeListener onCheckDebt5FrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addDebtWklyRB:
                    debt5DebtFrqRB = "52";
                    break;
                case R.id.addDebtBiWklyRB:
                    debt5DebtFrqRB = "26";
                    break;
                case R.id.addDebtMthlyRB:
                    debt5DebtFrqRB = "12";
                    break;
                default:
                    debt5DebtFrqRB = "12";
            }
        }
    };*/

    /*public void debt5DebtEndRes() {
        debt5Gen.debtDataFromEntries(debt5DebtNameET, debt5DebtLimitET, debt5DebtAmtET, debt5DebtRateET, debt5DebtPaytET, 0.0);
        debt5DebtEnd2 = debt5Gen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debt5DebtEnd2.equals(getString(R.string.debt_paid))) {
            debt5DebtDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (debt5DebtEnd2.equals(getString(R.string.too_far))) {
            debt5DebtDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            debt5DebtDateResTV.setTextColor(Color.parseColor("#303F9F"));
            debt5DebtDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
        debt5Gen.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), debt5DebtDateResLabel, debt5DebtDateResTV);
    }

    View.OnClickListener onClickDebt5CancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debt5Gen.intentMethod(debt5ToDebtLayout, AddDebtsFromDebtLayout.this, LayoutDebt.class);
        }
    };

    View.OnClickListener onClickDebt5SaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            debt5DebtEndRes();
            if (!debtNameFromEntry.equals("null")) {

                /*debt5IncDb = new IncomeBudgetDb(
                        debtNameFromEntry,
                        0.0,
                        1.0,
                        0.0,
                        0);

                debt5DbMgr.addIncome(debt5IncDb);*/

                /*debt5ExpAnnAmt = debtPaytFromEntry * debtFrqFromEntry;

                debt5ExpDb = new ExpenseBudgetDb(
                        debtNameFromEntry,
                        debtPaytFromEntry,
                        debtFrqFromEntry,
                        "A",
                        "N",
                        debt5ExpAnnAmt,
                        debt5ExpAnnAmt,
                        0.0,
                        0);

                debt5DbMgr.addExpense(debt5ExpDb);*/

                /*debt5DebtDb = new DebtDb(
                        debtNameFromEntry,
                        debtLimitFromEntry,
                        debtAmtFromEntry,
                        debtRateFromEntry,
                        debtPaytFromEntry,
                        (debtPaytFromEntry * 12.0),
                        debt5DebtEnd2,
                        0.0,
                        //debt5DbMgr.findLatestExpenseId(),
                        //debt5DbMgr.findLatestIncomeId(),
                        0);

                debt5DbMgr.addDebt(debt5DebtDb);

                debt5DebtId = debt5DbMgr.findLatestDebtId();

                debt5Acctdb = new AccountsDb(
                        debtNameFromEntry,
                        debt5DebtId,
                        0,
                        0);

                debt5DbMgr.addAccounts(debt5Acctdb);

                debt5Gen.intentMethod(debt5ToDebtLayout, AddDebtsFromDebtLayout.this, LayoutDebt.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
