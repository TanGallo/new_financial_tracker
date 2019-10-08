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

public class AddExpenseFromBudget extends AppCompatActivity {

    Button exp2SaveBtn, exp2CancelBtn, exp2UpdateBtn;
    DbManager exp2DbMgr;
    Double expAmtFromEntry = 0.0, expAnnAmtFromEntry = 0.0, expAAnnAmtFromEntry = 0.0, expBAnnAmtFromEntry = 0.0, expFrqFromEntry = 0.0;
    EditText exp2CatET, exp2AmtET;
    ExpenseBudgetDb exp2ExpDb;
    General exp2Gen;
    Intent exp2ToBudget;
    RadioButton exp2ARB, exp2AnnlyRB, exp2BRB, exp2BiAnnlyRB, exp2BiMthlyRB, exp2BiWklyRB, exp2MthlyRB, exp2NoWklyRB, exp2WklyRB, exp2YesWklyRB;
    RadioGroup exp2FrqRG, exp2ABRG, exp2WklyRG;
    String exp2ABRB = null, exp2FrqRB = null, expPriorityFromEntry = null, expWeeklyFromEntry = null, exp2WklyLimRB = null, expNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_5_add_expense);

        exp2Gen = new General();
        exp2DbMgr = new DbManager(this);

        exp2CatET = findViewById(R.id.addExpCatET);
        exp2AmtET = findViewById(R.id.addExpAmtET);
        exp2FrqRG = findViewById(R.id.addExpFrqRG);
        exp2ABRG = findViewById(R.id.addExpABRG);
        exp2WklyRG = findViewById(R.id.addExpWklyRG);
        exp2SaveBtn = findViewById(R.id.addExpSaveBtn);
        exp2UpdateBtn = findViewById(R.id.addExpUpdateBtn);
        exp2UpdateBtn.setVisibility(View.GONE);
        exp2CancelBtn = findViewById(R.id.addExpCancelBtn);

        exp2WklyRB = findViewById(R.id.addExpWklyRB);
        exp2BiWklyRB = findViewById(R.id.addExpBiWklyRB);
        exp2BiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
        exp2MthlyRB = findViewById(R.id.addExpMthlyRB);
        exp2BiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
        exp2AnnlyRB = findViewById(R.id.addExpAnnlyRB);

        exp2ARB = findViewById(R.id.addExpARB);
        exp2BRB = findViewById(R.id.addExpBRB);

        exp2YesWklyRB = findViewById(R.id.addExpYesWklyRB);
        exp2NoWklyRB = findViewById(R.id.addExpNoWklyRB);

        exp2CancelBtn.setOnClickListener(onClickExp2CancelBtn);
        exp2SaveBtn.setOnClickListener(onClickExp2SaveBtn);
        exp2FrqRG.setOnCheckedChangeListener(onCheckExp2FrqRG);
        exp2ABRG.setOnCheckedChangeListener(onCheckExp2ABRG);
        exp2WklyRG.setOnCheckedChangeListener(onCheckExp2WklyRG);
    }

    //handle radioGroups
    RadioGroup.OnCheckedChangeListener onCheckExp2FrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpWklyRB:
                    exp2FrqRB = "52";
                    break;
                case R.id.addExpBiWklyRB:
                    exp2FrqRB = "26";
                    break;
                case R.id.addExpBiMthlyRB:
                    exp2FrqRB = "24";
                    break;
                case R.id.addExpMthlyRB:
                    exp2FrqRB = "12";
                    break;
                case R.id.addExpBiAnnlyRB:
                    exp2FrqRB = "2";
                    break;
                case R.id.addExpAnnlyRB:
                    exp2FrqRB = "1";
                    break;
                default:
                    exp2FrqRB = "1";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp2ABRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpARB:
                    exp2ABRB = "A";
                    break;
                case R.id.addExpBRB:
                    exp2ABRB = "B";
                    break;
                default:
                    exp2ABRB = "B";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp2WklyRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpYesWklyRB:
                    exp2WklyLimRB = "Y";
                    break;
                case R.id.addExpNoWklyRB:
                    exp2WklyLimRB = "N";
                    break;
                default:
                    exp2WklyLimRB = "Y";
            }
        }
    };

    View.OnClickListener onClickExp2CancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exp2Gen.intentMethod(exp2ToBudget, AddExpenseFromBudget.this, LayoutBudget.class);
        }
    };

    View.OnClickListener onClickExp2SaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            exp2Gen.expenseDataFromEntries(exp2CatET, exp2AmtET, exp2FrqRB, exp2ABRB, exp2WklyLimRB);
            if(!expNameFromEntry.equals("null")) {

                exp2ExpDb = new ExpenseBudgetDb(
                        expNameFromEntry,
                        expAmtFromEntry,
                        expFrqFromEntry,
                        expPriorityFromEntry,
                        expWeeklyFromEntry,
                        expAnnAmtFromEntry,
                        expAAnnAmtFromEntry,
                        expBAnnAmtFromEntry,
                        0);

                exp2DbMgr.addExpense(exp2ExpDb);

                exp2Gen.intentMethod(exp2ToBudget, AddExpenseFromBudget.this, AddExpenseList.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
