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
import ca.gotchasomething.mynance.tabFragments.DailyMoneyCCPur;

public class AddDebtsFromDailyMoneyCC extends AppCompatActivity {

    AccountsDb debt3Acctdb;
    Button debt3SaveBtn, debt3CancelBtn, debt3UpdateBtn;
    DbManager debt3DbMgr;
    DebtDb debt3DebtDb;
    Double debtAmtFromEntry = 0.0, debtAnnIncFromEntry = 0.0, debtFrqFromEntry = 0.0, debtLimitFromEntry = 0.0, debtPaytFromEntry = 0.0,
            debtRateFromEntry = 0.0, debt3ExpAnnAmt = 0.0;
    EditText debt3DebtNameET, debt3DebtLimitET, debt3DebtAmtET, debt3DebtRateET, debt3DebtPaytET;
    ExpenseBudgetDb debt3ExpDb;
    General debt3Gen;
    IncomeBudgetDb debt3IncDb;
    Intent debt3ToDaiMonCCPur;
    long debt3DebtId;
    RadioButton debt3BiWklyRB, debt3MthlyRB, debt3WklyRB;
    RadioGroup debt3FrqRG;
    String debt3DebtEnd2 = null, debt3DebtFrqRB = null, debtNameFromEntry = null;
    TextView debt3DebtDateResLabel, debt3DebtDateResTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_3_add_debt);

        debt3Gen = new General();
        debt3DbMgr = new DbManager(this);

        debt3DebtNameET = findViewById(R.id.addDebtNameET);
        debt3DebtLimitET = findViewById(R.id.addDebtLimitET);
        debt3DebtAmtET = findViewById(R.id.addDebtAmtET);
        debt3DebtRateET = findViewById(R.id.addDebtRateET);
        debt3DebtPaytET = findViewById(R.id.addDebtPaytET);
        debt3DebtDateResLabel = findViewById(R.id.addDebtDateResLabel);
        debt3DebtDateResTV = findViewById(R.id.addDebtDateResTV);
        debt3SaveBtn = findViewById(R.id.addDebtSaveBtn);
        debt3CancelBtn = findViewById(R.id.addDebtCancelBtn);
        debt3UpdateBtn = findViewById(R.id.addDebtUpdateBtn);
        debt3UpdateBtn.setVisibility(View.GONE);
        /*debt3FrqRG = findViewById(R.id.addDebtFrqRG);
        debt3WklyRB = findViewById(R.id.addDebtWklyRB);
        debt3BiWklyRB = findViewById(R.id.addDebtBiWklyRB);
        debt3MthlyRB = findViewById(R.id.addDebtMthlyRB);*/

        /*debt3CancelBtn.setOnClickListener(onClickDebt3CancelBtn);
        debt3SaveBtn.setOnClickListener(onClickDebt3SaveBtn);
        //debt3FrqRG.setOnCheckedChangeListener(onCheckDebt3FrqRG);

        debt3DebtAmtET.addTextChangedListener(onChangeDebt3DebtAmtET);
        debt3DebtRateET.addTextChangedListener(onChangeDebt3DebtRateET);
        debt3DebtPaytET.addTextChangedListener(onChangeDebt3DebtPaytET);
    }

    TextWatcher onChangeDebt3DebtAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt3DebtEndRes();
            debt3DebtDateResTV.setText(debt3DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt3DebtRateET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt3DebtEndRes();
            debt3DebtDateResTV.setText(debt3DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt3DebtPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt3DebtEndRes();
            debt3DebtDateResTV.setText(debt3DebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //handle radioGroup for incomeFrequency
    /*RadioGroup.OnCheckedChangeListener onCheckDebt3FrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addDebtWklyRB:
                    debt3DebtFrqRB = "52";
                    break;
                case R.id.addDebtBiWklyRB:
                    debt3DebtFrqRB = "26";
                    break;
                case R.id.addDebtMthlyRB:
                    debt3DebtFrqRB = "12";
                    break;
                default:
                    debt3DebtFrqRB = "12";
            }
        }
    };*/

    /*public void debt3DebtEndRes() {
        debt3Gen.debtDataFromEntries(debt3DebtNameET, debt3DebtLimitET, debt3DebtAmtET, debt3DebtRateET, debt3DebtPaytET,0.0);
        debt3DebtEnd2 = debt3Gen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debt3DebtEnd2.equals(getString(R.string.debt_paid))) {
            debt3DebtDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (debt3DebtEnd2.equals(getString(R.string.too_far))) {
            debt3DebtDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            debt3DebtDateResTV.setTextColor(Color.parseColor("#303F9F"));
            debt3DebtDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
        debt3Gen.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), debt3DebtDateResLabel, debt3DebtDateResTV);
    }

    View.OnClickListener onClickDebt3CancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debt3Gen.intentMethod(debt3ToDaiMonCCPur, AddDebtsFromDailyMoneyCC.this, DailyMoneyCCPur.class);
        }
    };

    View.OnClickListener onClickDebt3SaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            debt3DebtEndRes();
            if (!debtNameFromEntry.equals("null")) {

                /*debt3IncDb = new IncomeBudgetDb(
                        debtNameFromEntry,
                        0.0,
                        1.0,
                        0.0,
                        0);

                debt3DbMgr.addIncome(debt3IncDb);*/

                /*debt3ExpAnnAmt = debtPaytFromEntry * debtFrqFromEntry;

                debt3ExpDb = new ExpenseBudgetDb(
                        debtNameFromEntry,
                        debtPaytFromEntry,
                        debtFrqFromEntry,
                        "A",
                        "N",
                        debt3ExpAnnAmt,
                        debt3ExpAnnAmt,
                        0.0,
                        0);

                debt3DbMgr.addExpense(debt3ExpDb);*/

                /*debt3DebtDb = new DebtDb(
                        debtNameFromEntry,
                        debtLimitFromEntry,
                        debtAmtFromEntry,
                        debtRateFromEntry,
                        debtPaytFromEntry,
                        (debtPaytFromEntry * 12.0),
                        debt3DebtEnd2,
                        0.0,
                        0);

                debt3DbMgr.addDebt(debt3DebtDb);

                debt3DebtId = debt3DbMgr.findLatestDebtId();

                debt3Acctdb = new AccountsDb(
                        debtNameFromEntry,
                        debt3DebtId,
                        0,
                        0);

                debt3DbMgr.addAccounts(debt3Acctdb);

                debt3Gen.intentMethod(debt3ToDaiMonCCPur, AddDebtsFromDailyMoneyCC.this, DailyMoneyCCPur.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
