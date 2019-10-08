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

public class AddIncomeFromBudget extends AppCompatActivity {

    Button inc4SaveButton, inc4CancelButton, inc4UpdateButton;
    DbManager inc4DbMgr;
    Double incAmtFromEntry = 0.0, incAnnAmtFromEntry = 0.0, incFrqFromEntry = 0.0;
    EditText inc4AmtET, inc4CatET;
    General inc4Gen;
    IncomeBudgetDb inc4IncDb;
    Intent inc4Budget;
    RadioButton inc4AnnlyRadioButton, inc4BiAnnlyRadioButton, inc4BiMthlyRadioButton, inc4BiWklyRadioButton, inc4MthlyRadioButton, inc4WklyRadioButton;
    RadioGroup inc4FrqRadioGroup;
    String inc4FrqRB = null, incNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_1_add_income);

        inc4Gen = new General();
        inc4DbMgr = new DbManager(this);

        inc4CatET = findViewById(R.id.addIncCatET);
        inc4AmtET = findViewById(R.id.addIncAmtET);
        inc4FrqRadioGroup = findViewById(R.id.addIncFrqRG);
        inc4SaveButton = findViewById(R.id.addIncSaveBtn);
        inc4UpdateButton = findViewById(R.id.addIncUpdateBtn);
        inc4UpdateButton.setVisibility(View.GONE);
        inc4CancelButton = findViewById(R.id.addIncCancelBtn);

        inc4WklyRadioButton = findViewById(R.id.addIncWklyRB);
        inc4BiWklyRadioButton = findViewById(R.id.addIncBiWklyRB);
        inc4BiMthlyRadioButton = findViewById(R.id.addIncBiMthlyRB);
        inc4MthlyRadioButton = findViewById(R.id.addIncMthlyRB);
        inc4BiAnnlyRadioButton = findViewById(R.id.addIncBiAnnlyRB);
        inc4AnnlyRadioButton = findViewById(R.id.addIncAnnlyRB);

        inc4CancelButton.setOnClickListener(onClickInc4CancelButton);
        inc4SaveButton.setOnClickListener(onClickInc4SaveButton);
        inc4FrqRadioGroup.setOnCheckedChangeListener(onCheckInc4Frq);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckInc4Frq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addIncWklyRB:
                    inc4FrqRB = "52";
                    break;
                case R.id.addIncBiWklyRB:
                    inc4FrqRB = "26";
                    break;
                case R.id.addIncBiMthlyRB:
                    inc4FrqRB = "24";
                    break;
                case R.id.addIncMthlyRB:
                    inc4FrqRB = "12";
                    break;
                case R.id.addIncBiAnnlyRB:
                    inc4FrqRB = "2";
                    break;
                case R.id.addIncAnnlyRB:
                    inc4FrqRB = "1";
                    break;
                default:
                    inc4FrqRB = "1";
            }
        }
    };

    View.OnClickListener onClickInc4CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inc4Gen.intentMethod(inc4Budget, AddIncomeFromBudget.this, LayoutBudget.class);
        }
    };

    View.OnClickListener onClickInc4SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            inc4Gen.incomeDataFromEntries(inc4CatET, inc4AmtET, inc4FrqRB);
            if(!incNameFromEntry.equals("null")) {

                inc4IncDb = new IncomeBudgetDb(
                        incNameFromEntry,
                        incAmtFromEntry,
                        incFrqFromEntry,
                        incAnnAmtFromEntry,
                        0);

                inc4DbMgr.addIncome(inc4IncDb);

                inc4Gen.intentMethod(inc4Budget, AddIncomeFromBudget.this, AddIncomeList.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
