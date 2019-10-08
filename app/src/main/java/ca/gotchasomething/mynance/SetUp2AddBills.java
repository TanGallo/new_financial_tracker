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

public class SetUp2AddBills extends AppCompatActivity {

    Button bil1SaveButton, bil1UpdateButton, bil1CancelButton;
    DbManager bil1DBMgr;
    Double expAmtFromEntry = 0.0,
            expFrqFromEntry = 0.0,
            expAnnAmtFromEntry = 0.0,
            expAAnnAmtFromEntry = 0.0,
            expBAnnAmtFromEntry = 0.0, expenseAmount = 0.0, billsAmountD = 0.0, expenseAnnualAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0,
            billsAnnualAmountD = 0.0, expenseFrequency = 0.0, billsFrequencyD = 0.0;
    EditText bil1AmtET, bil1CatET;
    ExpenseBudgetDb bil1ExpDB;
    General bil1Gen;
    Intent backToBillsSetUp, bil1ShwLst, bil1ToWeeklyList;
    //long id;
    RadioButton bil1AnnlyRadioButton, bil1BiAnnlyRadioButton, bil1BiMthlyRadioButton, bil1BiWklyRadioButton, bil1MthlyRadioButton, bil1WklyRadioButton;
    RadioGroup bil1FrqRadioGroup;
    String bil1FrqRB = null, expNameFromEntry = null, expPriorityFromEntry = null,
            expWeeklyFromEntry = null, expenseName = null, billsNameS = null, expensePriority = null, expenseWeekly = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_2_add_bills);

        bil1DBMgr = new DbManager(this);
        bil1Gen = new General();

        bil1CatET = findViewById(R.id.addBil1CatET);
        bil1AmtET = findViewById(R.id.addBil1AmtET);
        bil1FrqRadioGroup = findViewById(R.id.addBil1FrqRG);
        bil1SaveButton = findViewById(R.id.addBil1SaveBtn);
        bil1UpdateButton = findViewById(R.id.addBil1UpdateBtn);
        bil1UpdateButton.setVisibility(View.GONE);
        bil1CancelButton = findViewById(R.id.addBil1CancelBtn);

        bil1WklyRadioButton = findViewById(R.id.addBil1WklyRB);
        bil1BiWklyRadioButton = findViewById(R.id.addBil1BiWklyRB);
        bil1BiMthlyRadioButton = findViewById(R.id.addBil1BiMthlyRB);
        bil1MthlyRadioButton = findViewById(R.id.addBil1MthlyRB);
        bil1BiAnnlyRadioButton = findViewById(R.id.addBil1AnnlyRB);
        bil1AnnlyRadioButton = findViewById(R.id.addBil1AnnlyRB);

        bil1CancelButton.setOnClickListener(onClickBil1CancelButton);
        bil1SaveButton.setOnClickListener(onClickBil1SaveButton);
        bil1FrqRadioGroup.setOnCheckedChangeListener(onCheckBil1Frq);
    }

    //handle radioGroup for billFrequency
    RadioGroup.OnCheckedChangeListener onCheckBil1Frq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addBil1WklyRB:
                    bil1FrqRB = "52";
                    break;
                case R.id.addBil1BiWklyRB:
                    bil1FrqRB = "26";
                    break;
                case R.id.addBil1BiMthlyRB:
                    bil1FrqRB = "24";
                    break;
                case R.id.addBil1MthlyRB:
                    bil1FrqRB = "12";
                    break;
                case R.id.addBil1BiAnnlyRB:
                    bil1FrqRB = "2";
                    break;
                case R.id.addBil1AnnlyRB:
                    bil1FrqRB = "1";
                    break;
                default:
                    bil1FrqRB = "1";
            }
        }
    };

    /*public void bil1ShwLst() {
        bil1ShwLst = new Intent(SetUp2AddBills.this, SetUp2AddBillsList.class);
        bil1ShwLst.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil1ShwLst);
    }

    public void bil1ToWeeklyList() {
        bil1ToWeeklyList = new Intent(SetUp2AddBills.this, LayoutWeeklyLimitsList.class);
        bil1ToWeeklyList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil1ToWeeklyList);
    }*/

    /*View.OnClickListener onClickBil1CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bil1DBMgr.retrieveLatestDone().equals("analysis")) {
                bil1Gen.intentMethod(bil1ToWeeklyList, SetUp2AddBills.this, LayoutWeeklyLimitsList.class);
                //bil1ToWeeklyList();
            } else {
                bil1Gen.intentMethod(bil1ShwLst, SetUp2AddBills.this, SetUp2AddBillsList.class);
                //bil1ShwLst();
            }
        }
    };

    View.OnClickListener onClickBil1SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bil1DBMgr.retrieveLatestDone().equals("analysis")) {
                bil1Gen.expenseDataFromEntries(bil1CatET, bil1AmtET, bil1FrqRB, "B", "Y");
            } else {
                bil1Gen.expenseDataFromEntries(bil1CatET, bil1AmtET, bil1FrqRB, "A", "N");
            }

            if(!expNameFromEntry.equals("null")) {

            /*if (bil1CatET.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                billsNameS = bil1CatET.getText().toString();
                if (bil1AmtET.getText().toString().equals("")) {
                    billsAmountD = 0.0;
                } else {
                    billsAmountD = Double.valueOf(bil1AmtET.getText().toString());
                }
                try {
                    billsFrequencyD = Double.valueOf(bil1FrqRB);
                } catch (NullPointerException e) {
                    billsFrequencyD = 1.0;
                }
                billsAnnualAmountD = billsAmountD * billsFrequencyD;

                billsName = billsNameS;
                billsAmount = billsAmountD;
                billsFrequency = billsFrequencyD;
                billsPriority = "A";
                billsWeekly = "N";
                billsAnnualAmount = billsAnnualAmountD;
                billsAAnnualAmount = billsAnnualAmount;
                billsBAnnualAmount = 0.0;*/

                /*bil1ExpDB = new ExpenseBudgetDb(
                        expNameFromEntry,
                        expAmtFromEntry,
                        expFrqFromEntry,
                        expPriorityFromEntry,
                        expWeeklyFromEntry,
                        expAnnAmtFromEntry,
                        expAAnnAmtFromEntry,
                        expBAnnAmtFromEntry,
                        0);

                bil1DBMgr.addExpense(bil1ExpDB);

                if(bil1DBMgr.retrieveLatestDone().equals("analysis")) {
                    bil1Gen.intentMethod(bil1ToWeeklyList, SetUp2AddBills.this, LayoutWeeklyLimitsList.class);
                    //bil1ToWeeklyList();
                } else {
                    bil1Gen.intentMethod(bil1ShwLst, SetUp2AddBills.this, SetUp2AddBillsList.class);
                    //bil1ShwLst();
                }
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
