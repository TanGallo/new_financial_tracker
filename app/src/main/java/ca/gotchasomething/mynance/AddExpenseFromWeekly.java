/*package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class AddExpenseFromWeekly extends AppCompatActivity {

    Button exp3SaveBtn, exp3CancelBtn, exp3UpdateBtn;
    DbManager exp3DbMgr;
    Double expAmtFromEntry = 0.0, expAnnAmtFromEntry = 0.0, expAAnnAmtFromEntry = 0.0, expBAnnAmtFromEntry = 0.0, expFrqFromEntry = 0.0;
    EditText exp3CatET, exp3AmtET;
    ExpenseBudgetDb exp3ExpDb;
    General exp3Gen;
    Intent exp3ToAddWkly;
    RadioButton exp3ARB, exp3AnnlyRB, exp3BRB, exp3BiAnnlyRB, exp3BiMthlyRB, exp3BiWklyRB, exp3MthlyRB, exp3NoWklyRB, exp3WklyRB, exp3YesWklyRB;
    RadioGroup exp3FrqRG, exp3ABRG, exp3WklyRG;
    String exp3ABRB = null, exp3FrqRB = null, expPriorityFromEntry = null, expWeeklyFromEntry = null, exp3WklyLimRB = null, expNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_5_add_expense);

        exp3Gen = new General();
        exp3DbMgr = new DbManager(this);

        exp3CatET = findViewById(R.id.addExpCatET);
        exp3AmtET = findViewById(R.id.addExpAmtET);
        exp3FrqRG = findViewById(R.id.addExpFrqRG);
        exp3ABRG = findViewById(R.id.addExpABRG);
        exp3ABRG.setVisibility(View.GONE);
        exp3WklyRG = findViewById(R.id.addExpWklyRG);
        exp3WklyRG.setVisibility(View.GONE);
        exp3SaveBtn = findViewById(R.id.addExpSaveBtn);
        exp3UpdateBtn = findViewById(R.id.addExpUpdateBtn);
        exp3UpdateBtn.setVisibility(View.GONE);
        exp3CancelBtn = findViewById(R.id.addExpCancelBtn);

        exp3WklyRB = findViewById(R.id.addExpWklyRB);
        exp3BiWklyRB = findViewById(R.id.addExpBiWklyRB);
        exp3BiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
        exp3MthlyRB = findViewById(R.id.addExpMthlyRB);
        exp3BiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
        exp3AnnlyRB = findViewById(R.id.addExpAnnlyRB);

        exp3CancelBtn.setOnClickListener(onClickExp3CancelBtn);
        exp3SaveBtn.setOnClickListener(onClickExp3SaveBtn);
        exp3FrqRG.setOnCheckedChangeListener(onCheckExp3FrqRG);
    }

    //handle radioGroups
    RadioGroup.OnCheckedChangeListener onCheckExp3FrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpWklyRB:
                    exp3FrqRB = "52";
                    break;
                case R.id.addExpBiWklyRB:
                    exp3FrqRB = "26";
                    break;
                case R.id.addExpBiMthlyRB:
                    exp3FrqRB = "24";
                    break;
                case R.id.addExpMthlyRB:
                    exp3FrqRB = "12";
                    break;
                case R.id.addExpBiAnnlyRB:
                    exp3FrqRB = "2";
                    break;
                case R.id.addExpAnnlyRB:
                    exp3FrqRB = "1";
                    break;
                default:
                    exp3FrqRB = "1";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp3ABRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpARB:
                    exp3ABRB = "A";
                    break;
                case R.id.addExpBRB:
                    exp3ABRB = "B";
                    break;
                default:
                    exp3ABRB = "B";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp3WklyRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpYesWklyRB:
                    exp3WklyLimRB = "Y";
                    break;
                case R.id.addExpNoWklyRB:
                    exp3WklyLimRB = "N";
                    break;
                default:
                    exp3WklyLimRB = "Y";
            }
        }
    };

    View.OnClickListener onClickExp3CancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exp3Gen.intentMethod(exp3ToAddWkly, AddExpenseFromWeekly.this, LayoutWeeklyLimitsList.class);
        }
    };

    View.OnClickListener onClickExp3SaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            exp3Gen.expenseDataFromEntries(exp3CatET, exp3AmtET, exp3FrqRB, "B", "Y");
            if(!expNameFromEntry.equals("null")) {

                exp3ExpDb = new ExpenseBudgetDb(
                        expNameFromEntry,
                        expAmtFromEntry,
                        expFrqFromEntry,
                        expPriorityFromEntry,
                        expWeeklyFromEntry,
                        expAnnAmtFromEntry,
                        expAAnnAmtFromEntry,
                        expBAnnAmtFromEntry,
                        0);

                exp3DbMgr.addExpense(exp3ExpDb);

                exp3Gen.intentMethod(exp3ToAddWkly, AddExpenseFromWeekly.this, LayoutWeeklyLimitsList.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
