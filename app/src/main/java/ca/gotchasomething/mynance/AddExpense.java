package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.gotchasomething.mynance.data.BudgetDb;

//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class AddExpense extends AppCompatActivity {

    BudgetDb addExpExpDb;
    Button addExpSaveBtn, addExpCancelBtn, addExpUpdateBtn;
    DbManager addExpDbMgr;
    Double expAmtFromEntry = 0.0, expAnnAmtFromEntry = 0.0, expAAnnAmtFromEntry = 0.0, expBAnnAmtFromEntry = 0.0, expFrqFromEntry = 0.0;
    EditText addExpCatET, addExpAmtET;
    General addExpGen;
    Intent addExpToList, addExpToWeeklyList;
    RadioButton addExpARB, addExpAnnlyRB, addExpBRB, addExpBiAnnlyRB, addExpBiMthlyRB, addExpBiWklyRB, addExpMthlyRB, addExpNoWklyRB, addExpWklyRB, addExpYesWklyRB;
    RadioGroup addExpFrqRG, addExpABRG, addExpWklyRG;
    String addExpABRB = null, addExpFrqRB = null, expPriorityFromEntry = null, expWeeklyFromEntry = null, addExpWklyLimRB = null, expNameFromEntry = null;
    TextView addExpWklyLabel;
    View addExpLine1, addExpLine2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_5_add_expense);

        addExpGen = new General();
        addExpDbMgr = new DbManager(this);

        addExpCatET = findViewById(R.id.addExpCatET);
        addExpAmtET = findViewById(R.id.addExpAmtET);
        addExpFrqRG = findViewById(R.id.addExpFrqRG);
        addExpABRG = findViewById(R.id.addExpABRG);
        addExpWklyLabel = findViewById(R.id.addExpWklyLabel);
        addExpWklyLabel.setVisibility(View.GONE);
        addExpWklyRG = findViewById(R.id.addExpWklyRG);
        addExpWklyRG.setVisibility(View.GONE);
        addExpLine2 = findViewById(R.id.addExpLine2);
        addExpLine2.setVisibility(View.GONE);
        addExpSaveBtn = findViewById(R.id.addExpSaveBtn);
        addExpUpdateBtn = findViewById(R.id.addExpUpdateBtn);
        addExpUpdateBtn.setVisibility(View.GONE);
        addExpCancelBtn = findViewById(R.id.addExpCancelBtn);
        addExpLine1 = findViewById(R.id.addExpLine1);

        addExpWklyRB = findViewById(R.id.addExpWklyRB);
        addExpBiWklyRB = findViewById(R.id.addExpBiWklyRB);
        addExpBiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
        addExpMthlyRB = findViewById(R.id.addExpMthlyRB);
        addExpBiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
        addExpAnnlyRB = findViewById(R.id.addExpAnnlyRB);

        addExpARB = findViewById(R.id.addExpARB);
        addExpBRB = findViewById(R.id.addExpBRB);

        addExpYesWklyRB = findViewById(R.id.addExpYesWklyRB);
        addExpNoWklyRB = findViewById(R.id.addExpNoWklyRB);

        if(addExpDbMgr.retrieveLatestDone().equals("income") || addExpDbMgr.retrieveLatestDone().equals("analysis") || addExpDbMgr.retrieveLastPageId() == 4) {
            addExpABRG.setVisibility(View.GONE);
            addExpLine1.setVisibility(View.GONE);
        }

        addExpCancelBtn.setOnClickListener(onClickAddExpCancelBtn);
        addExpSaveBtn.setOnClickListener(onClickAddExpSaveBtn);
        addExpFrqRG.setOnCheckedChangeListener(onCheckAddExpFrqRG);

        if(!addExpDbMgr.retrieveLatestDone().equals("income") && !addExpDbMgr.retrieveLatestDone().equals("analysis") && addExpDbMgr.retrieveLastPageId() != 4) {
            addExpABRG.setOnCheckedChangeListener(onCheckAddExpABRG);
        }
    }

    //handle radioGroups
    RadioGroup.OnCheckedChangeListener onCheckAddExpFrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpWklyRB:
                    addExpFrqRB = "52";
                    break;
                case R.id.addExpBiWklyRB:
                    addExpFrqRB = "26";
                    break;
                case R.id.addExpBiMthlyRB:
                    addExpFrqRB = "24";
                    break;
                case R.id.addExpMthlyRB:
                    addExpFrqRB = "12";
                    break;
                case R.id.addExpBiAnnlyRB:
                    addExpFrqRB = "2";
                    break;
                case R.id.addExpAnnlyRB:
                    addExpFrqRB = "1";
                    break;
                default:
                    addExpFrqRB = "1";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckAddExpABRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpARB:
                    addExpABRB = "A";
                    addExpWklyRG.setVisibility(View.GONE);
                    addExpLine2.setVisibility(View.GONE);
                    addExpWklyLimRB = "N";
                    break;
                case R.id.addExpBRB:
                    addExpABRB = "B";
                    addExpWklyRG.setVisibility(View.VISIBLE);
                    addExpLine2.setVisibility(View.VISIBLE);
                    addExpWklyRG.setOnCheckedChangeListener(onCheckAddExpWklyRG);
                    break;
                default:
                    addExpABRB = "B";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckAddExpWklyRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpYesWklyRB:
                    addExpWklyLimRB = "Y";
                    break;
                case R.id.addExpNoWklyRB:
                    addExpWklyLimRB = "N";
                    break;
                default:
                    addExpWklyLimRB = "Y";
            }
        }
    };

    View.OnClickListener onClickAddExpCancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addExpToList = new Intent(AddExpense.this, AddExpenseList.class);
            addExpToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addExpToList);
        }
    };

    View.OnClickListener onClickAddExpSaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expNameFromEntry = addExpGen.stringFromET(addExpCatET);
            expAmtFromEntry = addExpGen.dblFromET(addExpAmtET);
            expFrqFromEntry = Double.valueOf(addExpFrqRB);
            expAnnAmtFromEntry = expAmtFromEntry * expFrqFromEntry;

            if(addExpDbMgr.retrieveLatestDone().equals("income")) {
                expPriorityFromEntry = "A";
                expWeeklyFromEntry = "N";
            } else if(addExpDbMgr.retrieveLatestDone().equals("analysis") || addExpDbMgr.retrieveLastPageId() == 4) {
                expPriorityFromEntry = "B";
                expWeeklyFromEntry = "Y";
            } else  {
                expPriorityFromEntry = addExpABRB;
                expWeeklyFromEntry = addExpWklyLimRB;
            }

            /*if(expPriorityFromEntry.equals("A")) {
                expAAnnAmtFromEntry = expAnnAmtFromEntry;
            } else {
                expBAnnAmtFromEntry = expAnnAmtFromEntry;
            }*/

            if(!expNameFromEntry.equals("null")) {

                addExpExpDb = new BudgetDb(
                        expNameFromEntry,
                        expAmtFromEntry,
                        "Y",
                        "N",
                        expFrqFromEntry,
                        expAnnAmtFromEntry,
                        expPriorityFromEntry,
                        expWeeklyFromEntry,
                        0);

                addExpDbMgr.addExpense(addExpExpDb);

                if(addExpDbMgr.retrieveLastPageId() == 4) {
                    addExpToWeeklyList = new Intent(AddExpense.this, LayoutWeeklyLimitsList.class);
                    addExpToWeeklyList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(addExpToWeeklyList);
                } else {
                    addExpToList = new Intent(AddExpense.this, AddExpenseList.class);
                    addExpToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(addExpToList);
                }
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}
