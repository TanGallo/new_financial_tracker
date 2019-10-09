package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class AddDebts extends AppCompatActivity {

    AccountsDb addDebtAcctdb;
    BudgetDb addDebtExpDb;
    Button addDebtSaveBtn, addDebtCancelBtn, addDebtUpdateBtn;
    DbManager addDebtDbMgr;
    //DebtDb addDebtDebtDb;
    Double debtAmtFromEntry = 0.0, debtLimitFromEntry = 0.0, debtPaytFromEntry = 0.0, debtRateFromEntry = 0.0;
    EditText addDebtDebtNameET, addDebtDebtLimitET, addDebtDebtAmtET, addDebtDebtRateET, addDebtDebtPaytET;
    General addDebtGen;
    Intent addDebtToList;
    long addDebtDebtId;
    String addDebtDebtEnd2 = null, debtNameFromEntry = null;
    TextView addDebtDebtDateResLabel, addDebtDebtDateResTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_3_add_debt);

        addDebtGen = new General();
        addDebtDbMgr = new DbManager(this);

        addDebtDebtNameET = findViewById(R.id.addDebtNameET);
        addDebtDebtLimitET = findViewById(R.id.addDebtLimitET);
        addDebtDebtAmtET = findViewById(R.id.addDebtAmtET);
        addDebtDebtRateET = findViewById(R.id.addDebtRateET);
        addDebtDebtPaytET = findViewById(R.id.addDebtPaytET);
        addDebtDebtDateResLabel = findViewById(R.id.addDebtDateResLabel);
        addDebtDebtDateResTV = findViewById(R.id.addDebtDateResTV);
        addDebtSaveBtn = findViewById(R.id.addDebtSaveBtn);
        addDebtCancelBtn = findViewById(R.id.addDebtCancelBtn);
        addDebtUpdateBtn = findViewById(R.id.addDebtUpdateBtn);
        addDebtUpdateBtn.setVisibility(View.GONE);

        addDebtCancelBtn.setOnClickListener(onClickAddDebtCancelBtn);
        addDebtSaveBtn.setOnClickListener(onClickAddDebtSaveBtn);

        addDebtDebtAmtET.addTextChangedListener(onChangeAddDebtDebtAmtET);
        addDebtDebtRateET.addTextChangedListener(onChangeAddDebtDebtRateET);
        addDebtDebtPaytET.addTextChangedListener(onChangeAddDebtDebtPaytET);

        if(addDebtDbMgr.getExpense().size() == 0) {
            addDebtExpDb = new BudgetDb(
                    getString(R.string.other),
                    0.0,
                    "Y",
                    "N",
                    12.0,
                    0.0,
                    "B",
                    "N",
                    0);
            addDebtDbMgr.addExpense(addDebtExpDb);
        }
    }

    TextWatcher onChangeAddDebtDebtAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addDebtDebtEndRes();
            addDebtDebtDateResTV.setText(addDebtDebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeAddDebtDebtRateET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addDebtDebtEndRes();
            addDebtDebtDateResTV.setText(addDebtDebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeAddDebtDebtPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addDebtDebtEndRes();
            addDebtDebtDateResTV.setText(addDebtDebtEnd2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void addDebtDebtEndRes() {
        debtNameFromEntry = addDebtGen.stringFromET(addDebtDebtNameET);
        debtLimitFromEntry = addDebtGen.dblFromET(addDebtDebtLimitET);
        debtAmtFromEntry = addDebtGen.dblFromET(addDebtDebtAmtET);
        debtRateFromEntry = addDebtGen.percentFromET(addDebtDebtRateET);
        debtPaytFromEntry = addDebtGen.dblFromET(addDebtDebtPaytET);

        addDebtDebtEnd2 = addDebtGen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        /*if (addDebtDebtEnd2.equals(getString(R.string.debt_paid))) {
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (addDebtDebtEnd2.equals(getString(R.string.too_far))) {
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#303F9F"));
            addDebtDebtDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }*/
        addDebtGen.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), addDebtDebtDateResLabel, addDebtDebtDateResTV);
    }

    View.OnClickListener onClickAddDebtCancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addDebtToList = new Intent(AddDebts.this, AddDebtsList.class);
            addDebtToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addDebtToList);
        }
    };

    View.OnClickListener onClickAddDebtSaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addDebtDebtEndRes();

            if (!debtNameFromEntry.equals("null")) {

                /*addDebtDebtDb = new DebtDb(
                        debtNameFromEntry,
                        debtLimitFromEntry,
                        debtAmtFromEntry,
                        debtRateFromEntry,
                        debtPaytFromEntry,
                        (debtPaytFromEntry * 12.0),
                        addDebtDebtEnd2,
                        0.0,
                        0);

                addDebtDbMgr.addDebt(addDebtDebtDb);

                addDebtDebtId = addDebtDbMgr.findLatestDebtId();*/

                addDebtAcctdb = new AccountsDb(
                        debtNameFromEntry,
                        -debtAmtFromEntry,
                        "Y",
                        "N",
                        debtLimitFromEntry,
                        debtRateFromEntry,
                        debtPaytFromEntry,
                        (debtPaytFromEntry * 12.0),
                        addDebtDebtEnd2,
                        0.0,
                        0);

                addDebtDbMgr.addAccounts(addDebtAcctdb);

                addDebtToList = new Intent(AddDebts.this, AddDebtsList.class);
                addDebtToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(addDebtToList);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}
