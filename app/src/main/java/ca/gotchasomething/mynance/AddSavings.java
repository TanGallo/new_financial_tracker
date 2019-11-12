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

public class AddSavings extends AppCompatActivity {

    AccountsDb addSavAcctDb;
    Button addSavSavCancelBtn, addSavSavSaveBtn, addSavSavUpdateBtn;
    ContentValues addSavCV;
    DbHelper addSavHelper;
    DbManager addSavDbMgr;
    Double currentSavingsRate = 0.0, savAmtFromEntry = 0.0, savGoalFromEntry = 0.0, savPaytFromEntry = 0.0, savRateFromEntry = 0.0;
    EditText addSavSavAmtET, addSavSavGoalET, addSavSavNameET, addSavSavPaytET, addSavSavPercentET;
    General addSavGen;
    Intent addSavToList;
    long addSavSavId;
    //SavingsDb addSavSavDb;
    SQLiteDatabase addSavDb;
    String addSavSavDate2 = null, savNameFromEntry = null;
    TextView addSavSavDateResTV, addSavSavDateResLabel;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_4_add_savings);

        addSavDbMgr = new DbManager(this);
        addSavGen = new General();

        addSavSavNameET = findViewById(R.id.addSavNameET);
        addSavSavAmtET = findViewById(R.id.addSavAmtET);
        addSavSavGoalET = findViewById(R.id.addSavGoalET);
        addSavSavPaytET = findViewById(R.id.addSavPaytET);
        addSavSavPercentET = findViewById(R.id.addSavPercentET);
        addSavSavDateResLabel = findViewById(R.id.addSavDateResLabel);
        addSavSavDateResTV = findViewById(R.id.addSavDateResTV);
        addSavSavSaveBtn = findViewById(R.id.addSavSaveBtn);
        addSavSavUpdateBtn = findViewById(R.id.addSavUpdateBtn);
        addSavSavUpdateBtn.setVisibility(View.GONE);
        addSavSavCancelBtn = findViewById(R.id.addSavCancelBtn);
        
        addSavSavCancelBtn.setOnClickListener(onClickAddSavSavCancelBtn);
        addSavSavSaveBtn.setOnClickListener(onClickAddSavSavSaveBtn);

        addSavSavAmtET.addTextChangedListener(onChangeAddSavSavAmtET);
        addSavSavGoalET.addTextChangedListener(onChangeAddSavSavGoalET);
        addSavSavPaytET.addTextChangedListener(onChangeAddSavSavPaytET);
        addSavSavPercentET.addTextChangedListener(onChangeAddSavSavPercentET);

        addSavCV = new ContentValues();
        addSavCV.put(DbHelper.LASTPAGEID, 13);
        addSavHelper = new DbHelper(getApplicationContext());
        addSavDb = addSavHelper.getWritableDatabase();
        addSavDb.update(DbHelper.CURRENT_TABLE_NAME, addSavCV, DbHelper.ID + "= '1'", null);
        addSavDb.close();
    }

    TextWatcher onChangeAddSavSavAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addSavSavDateRes();
            addSavSavDateResTV.setText(addSavSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeAddSavSavGoalET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addSavSavDateRes();
            addSavSavDateResTV.setText(addSavSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeAddSavSavPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addSavSavDateRes();
            addSavSavDateResTV.setText(addSavSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeAddSavSavPercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addSavSavDateRes();
            addSavSavDateResTV.setText(addSavSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void addSavSavDateRes() {
        savNameFromEntry = addSavGen.stringFromET(addSavSavNameET);
        savAmtFromEntry = addSavGen.dblFromET(addSavSavAmtET);
        savGoalFromEntry = addSavGen.dblFromET(addSavSavGoalET);
        savRateFromEntry = addSavGen. percentFromET(addSavSavPercentET);
        savPaytFromEntry = addSavGen.dblFromET(addSavSavPaytET);

        addSavSavDate2 = addSavGen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (addSavSavDate2.equals(getString(R.string.goal_achieved))) {
            addSavSavDateResLabel.setVisibility(View.GONE);
            addSavSavDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (addSavSavDate2.equals(getString(R.string.too_far))) {
            addSavSavDateResLabel.setVisibility(View.GONE);
            addSavSavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            addSavSavDateResLabel.setVisibility(View.VISIBLE);
            addSavSavDateResTV.setTextColor(Color.parseColor("#303F9F"));
            addSavSavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
    }

    View.OnClickListener onClickAddSavSavCancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addSavToList = new Intent(AddSavings.this, AddSavingsList.class);
            addSavToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addSavToList);
        }
    };

    final View.OnClickListener onClickAddSavSavSaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addSavSavDateRes();

            if (savNameFromEntry != "null") {

                /*addSavSavDb = new SavingsDb(
                        savNameFromEntry,
                        savAmtFromEntry,
                        savGoalFromEntry,
                        savPaytFromEntry,
                        savRateFromEntry,
                        (savPaytFromEntry * 12.0),
                        addSavSavDate2,
                        0);

                addSavDbMgr.addSavings(addSavSavDb);

                addSavSavId = addSavDbMgr.findLatestSavId();*/

                addSavAcctDb = new AccountsDb(
                        savNameFromEntry,
                        savAmtFromEntry,
                        "S",
                        savGoalFromEntry,
                        savRateFromEntry,
                        savPaytFromEntry,
                        (savPaytFromEntry * 12.0),
                        addSavSavDate2,
                        0.0,
                        0);

                addSavDbMgr.addAccounts(addSavAcctDb);

                addSavToList = new Intent(AddSavings.this, AddSavingsList.class);
                addSavToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(addSavToList);

            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}
