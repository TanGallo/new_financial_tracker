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
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;

public class AddIncomeFromMoneyIn extends AppCompatActivity {

    Button inc3SaveButton, inc3CancelButton, inc3UpdateButton;
    DbManager inc3DbMgr;
    Double incAmtFromEntry = 0.0, incAnnAmtFromEntry = 0.0, incFrqFromEntry = 0.0;
    EditText inc3AmtET, inc3CatET;
    General inc3Gen;
    IncomeBudgetDb inc3IncDb;
    Intent inc3ToMonIn;
    RadioButton inc3AnnlyRadioButton, inc3BiAnnlyRadioButton, inc3BiMthlyRadioButton, inc3BiWklyRadioButton, inc3MthlyRadioButton, inc3WklyRadioButton;
    RadioGroup inc3FrqRadioGroup;
    String inc3FrqRB = null, incNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_1_add_income);

        inc3Gen = new General();
        inc3DbMgr = new DbManager(this);

        inc3CatET = findViewById(R.id.addIncCatET);
        inc3AmtET = findViewById(R.id.addIncAmtET);
        inc3FrqRadioGroup = findViewById(R.id.addIncFrqRG);
        inc3SaveButton = findViewById(R.id.addIncSaveBtn);
        inc3UpdateButton = findViewById(R.id.addIncUpdateBtn);
        inc3UpdateButton.setVisibility(View.GONE);
        inc3CancelButton = findViewById(R.id.addIncCancelBtn);

        inc3WklyRadioButton = findViewById(R.id.addIncWklyRB);
        inc3BiWklyRadioButton = findViewById(R.id.addIncBiWklyRB);
        inc3BiMthlyRadioButton = findViewById(R.id.addIncBiMthlyRB);
        inc3MthlyRadioButton = findViewById(R.id.addIncMthlyRB);
        inc3BiAnnlyRadioButton = findViewById(R.id.addIncBiAnnlyRB);
        inc3AnnlyRadioButton = findViewById(R.id.addIncAnnlyRB);

        inc3CancelButton.setOnClickListener(onClickInc3CancelButton);
        inc3SaveButton.setOnClickListener(onClickInc3SaveButton);
        inc3FrqRadioGroup.setOnCheckedChangeListener(onCheckInc3Frq);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckInc3Frq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addIncWklyRB:
                    inc3FrqRB = "52";
                    break;
                case R.id.addIncBiWklyRB:
                    inc3FrqRB = "26";
                    break;
                case R.id.addIncBiMthlyRB:
                    inc3FrqRB = "24";
                    break;
                case R.id.addIncMthlyRB:
                    inc3FrqRB = "12";
                    break;
                case R.id.addIncBiAnnlyRB:
                    inc3FrqRB = "2";
                    break;
                case R.id.addIncAnnlyRB:
                    inc3FrqRB = "1";
                    break;
                default:
                    inc3FrqRB = "1";
            }
        }
    };

    View.OnClickListener onClickInc3CancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inc3Gen.intentMethod(inc3ToMonIn, AddIncomeFromMoneyIn.this, DailyMoneyIn.class);
        }
    };

    View.OnClickListener onClickInc3SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            inc3Gen.incomeDataFromEntries(inc3CatET, inc3AmtET, inc3FrqRB);
            if(!incNameFromEntry.equals("null")) {

                inc3IncDb = new IncomeBudgetDb(
                        incNameFromEntry,
                        incAmtFromEntry,
                        incFrqFromEntry,
                        incAnnAmtFromEntry,
                        0);

                inc3DbMgr.addIncome(inc3IncDb);

                inc3Gen.intentMethod(inc3ToMonIn, AddIncomeFromMoneyIn.this, DailyMoneyIn.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
