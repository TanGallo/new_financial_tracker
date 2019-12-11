package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

public class AddDebts extends AppCompatActivity {

    AccountsDb addDebtAcctdb;
    BudgetDb addDebtExpDb;
    Button addDebtSaveBtn, addDebtCancelBtn, addDebtUpdateBtn;
    ContentValues addDebtCV;
    DbHelper addDebtHelper;
    DbManager addDebtDbMgr;
    Double debtAmtFromEntry = 0.0, debtPaytFromEntry = 0.0, debtRateFromEntry = 0.0;
    EditText addDebtDebtNameET, addDebtDebtLimitET, addDebtDebtAmtET, addDebtDebtRateET, addDebtDebtPaytET;
    General addDebtGen;
    Intent addDebtToList;
    SQLiteDatabase addDebtDb;
    String addDebtDebtEnd2 = null;
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

        if (addDebtDbMgr.getBudget().size() == 0) {
            addDebtExpDb = new BudgetDb(
                    getString(R.string.other),
                    0.0,
                    "E",
                    12.0,
                    0.0,
                    "B",
                    "N",
                    0);
            addDebtDbMgr.addBudget(addDebtExpDb);
        }

        addDebtCV = new ContentValues();
        addDebtCV.put(DbHelper.LASTPAGEID, 11);
        addDebtHelper = new DbHelper(getApplicationContext());
        addDebtDb = addDebtHelper.getWritableDatabase();
        addDebtDb.update(DbHelper.CURRENT_TABLE_NAME, addDebtCV, DbHelper.ID + "= '1'", null);
        addDebtDb.close();
    }

    TextWatcher onChangeAddDebtDebtAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = addDebtGen.dblFromET(addDebtDebtAmtET);
            debtRateFromEntry = addDebtGen.percentFromET(addDebtDebtRateET);
            debtPaytFromEntry = addDebtGen.dblFromET(addDebtDebtPaytET);
            addDebtDebtEndRes();
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
            debtAmtFromEntry = addDebtGen.dblFromET(addDebtDebtAmtET);
            debtRateFromEntry = addDebtGen.percentFromET(addDebtDebtRateET);
            debtPaytFromEntry = addDebtGen.dblFromET(addDebtDebtPaytET);
            addDebtDebtEndRes();
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
            debtAmtFromEntry = addDebtGen.dblFromET(addDebtDebtAmtET);
            debtRateFromEntry = addDebtGen.percentFromET(addDebtDebtRateET);
            debtPaytFromEntry = addDebtGen.dblFromET(addDebtDebtPaytET);
            addDebtDebtEndRes();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void addDebtDebtEndRes() {
        addDebtDebtEnd2 = addDebtGen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (addDebtDebtEnd2.equals(getString(R.string.debt_paid))) {
            addDebtDebtDateResLabel.setVisibility(View.GONE);
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else if (addDebtDebtEnd2.equals(getString(R.string.too_far))) {
            addDebtDebtDateResLabel.setVisibility(View.GONE);
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#ffff4444")); //red
        } else {
            addDebtDebtDateResLabel.setVisibility(View.VISIBLE);
            addDebtDebtDateResTV.setTextColor(Color.parseColor("#303F9F"));
            addDebtDebtDateResLabel.setTextColor(Color.parseColor("#303F9F")); //primary dark
        }

        addDebtDebtDateResTV.setText(addDebtDebtEnd2);
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

                if (!addDebtGen.stringFromET(addDebtDebtNameET).equals("null")) {

                    addDebtAcctdb = new AccountsDb(
                            addDebtGen.stringFromET(addDebtDebtNameET),
                            debtAmtFromEntry,
                            "D",
                            addDebtGen.dblFromET(addDebtDebtLimitET),
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
