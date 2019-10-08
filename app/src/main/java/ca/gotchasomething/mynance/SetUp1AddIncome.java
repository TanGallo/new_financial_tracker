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

import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class SetUp1AddIncome extends AppCompatActivity {

    Button inc1SaveButton, inc1CancelButton, inc1UpdateButton;
    DbManager inc1DbMgr;
    Double incAmtFromEntry = 0.0, incomeAmount = 0.0, incAnnAmtFromEntry = 0.0, incomeAnnualAmount = 0.0, incFrqFromEntry = 0.0, incomeFrequency = 0.0;
    EditText inc1AmtET, inc1CatET;
    General inc1Gen;
    IncomeBudgetDb inc1IncDb;
    Intent inc1ShwLst;
    RadioButton inc1AnnlyRadioButton, inc1BiAnnlyRadioButton, inc1BiMthlyRadioButton, inc1BiWklyRadioButton, inc1MthlyRadioButton, inc1WklyRadioButton;
    RadioGroup inc1FrqRadioGroup;
    String inc1FrqRB = null, incNameFromEntry = null, incomeName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_1_add_income);

        inc1Gen = new General();
        inc1DbMgr = new DbManager(this);

        if(inc1DbMgr.getAccounts().size() == 0) {
            inc1DbMgr.mainAccount();
        }

        inc1CatET = findViewById(R.id.addIncCatET);
        inc1AmtET = findViewById(R.id.addIncAmtET);
        inc1FrqRadioGroup = findViewById(R.id.addIncFrqRG);
        inc1SaveButton = findViewById(R.id.addIncSaveBtn);
        inc1UpdateButton = findViewById(R.id.addIncUpdateBtn);
        inc1UpdateButton.setVisibility(View.GONE);
        inc1CancelButton = findViewById(R.id.addIncCancelBtn);

        inc1WklyRadioButton = findViewById(R.id.addIncWklyRB);
        inc1BiWklyRadioButton = findViewById(R.id.addIncBiWklyRB);
        inc1BiMthlyRadioButton = findViewById(R.id.addIncBiMthlyRB);
        inc1MthlyRadioButton = findViewById(R.id.addIncMthlyRB);
        inc1BiAnnlyRadioButton = findViewById(R.id.addIncBiAnnlyRB);
        inc1AnnlyRadioButton = findViewById(R.id.addIncAnnlyRB);

        inc1CancelButton.setOnClickListener(onClickInc1CancelButton);
        inc1SaveButton.setOnClickListener(onClickInc1SaveButton);
        inc1FrqRadioGroup.setOnCheckedChangeListener(onCheckInc1Frq);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckInc1Frq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addIncWklyRB:
                    inc1FrqRB = "52";
                    break;
                case R.id.addIncBiWklyRB:
                    inc1FrqRB = "26";
                    break;
                case R.id.addIncBiMthlyRB:
                    inc1FrqRB = "24";
                    break;
                case R.id.addIncMthlyRB:
                    inc1FrqRB = "12";
                    break;
                case R.id.addIncBiAnnlyRB:
                    inc1FrqRB = "2";
                    break;
                case R.id.addIncAnnlyRB:
                    inc1FrqRB = "1";
                    break;
                default:
                    inc1FrqRB = "1";
            }
        }
    };

    /*public void inc1ShwLst() {
        inc1ShwLst = new Intent(SetUp1AddIncome.this, SetUp1AddIncomeList.class);
        inc1ShwLst.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(inc1ShwLst);
    }*/

    /*View.OnClickListener onClickInc1CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inc1Gen.intentMethod(inc1ShwLst, SetUp1AddIncome.this, SetUp1AddIncomeList.class);
            //inc1ShwLst();
        }
    };

    View.OnClickListener onClickInc1SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            inc1Gen.incomeDataFromEntries(inc1CatET, inc1AmtET, inc1FrqRB);
            if(!incNameFromEntry.equals("null")) {

            /*inc1IncName2 = inc1Gen.stringFromET(inc1CatET);
            if(!inc1IncName2.equals("null")) {
                inc1Amt2 = inc1Gen.dblFromET(inc1AmtET);
                try {
                    inc1Frq2 = Double.valueOf(inc1FrqRB);
                } catch (NullPointerException e) {
                    inc1Frq2 = 1.0;
                }
                inc1AnnAmt2 = inc1Amt2 * inc1Frq2;

                inc1IncName = inc1IncName2;
                inc1Amt = inc1Amt2;
                inc1Frq = inc1Frq2;
                inc1AnnAmt = inc1AnnAmt2;*/

                /*inc1IncDb = new IncomeBudgetDb(
                        incNameFromEntry,
                        incAmtFromEntry,
                        incFrqFromEntry,
                        incAnnAmtFromEntry,
                        0);

                inc1DbMgr.addIncome(inc1IncDb);

                inc1Gen.intentMethod(inc1ShwLst, SetUp1AddIncome.this, SetUp1AddIncomeList.class);
                //inc1ShwLst();
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
