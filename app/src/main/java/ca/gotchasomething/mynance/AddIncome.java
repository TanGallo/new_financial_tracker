package ca.gotchasomething.mynance;

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

import ca.gotchasomething.mynance.data.BudgetDb;

public class AddIncome extends AppCompatActivity {

    BudgetDb addIncIncDb;
    Button addIncSaveBtn, addIncCancelBtn, addIncUpdateBtn;
    DbManager addIncDbMgr;
    Double incAmtFromEntry = 0.0, incAnnAmtFromEntry = 0.0, incFrqFromEntry = 0.0;
    EditText addIncAmtET, addIncCatET;
    General addIncGen;
    Intent addIncToList;
    RadioButton addIncAnnlyRB, addIncBiAnnlyRB, addIncBiMthlyRB, addIncBiWklyRB, addIncMthlyRB, addIncWklyRB;
    RadioGroup addIncFrqRG;
    String addIncFrqRB = null, incNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_1_add_income);

        addIncGen = new General();
        addIncDbMgr = new DbManager(this);

        addIncCatET = findViewById(R.id.addIncCatET);
        addIncAmtET = findViewById(R.id.addIncAmtET);
        addIncFrqRG = findViewById(R.id.addIncFrqRG);
        addIncSaveBtn = findViewById(R.id.addIncSaveBtn);
        addIncUpdateBtn = findViewById(R.id.addIncUpdateBtn);
        addIncUpdateBtn.setVisibility(View.GONE);
        addIncCancelBtn = findViewById(R.id.addIncCancelBtn);

        addIncWklyRB = findViewById(R.id.addIncWklyRB);
        addIncBiWklyRB = findViewById(R.id.addIncBiWklyRB);
        addIncBiMthlyRB = findViewById(R.id.addIncBiMthlyRB);
        addIncMthlyRB = findViewById(R.id.addIncMthlyRB);
        addIncBiAnnlyRB = findViewById(R.id.addIncBiAnnlyRB);
        addIncAnnlyRB = findViewById(R.id.addIncAnnlyRB);

        addIncCancelBtn.setOnClickListener(onClickAddIncCancelBtn);
        addIncSaveBtn.setOnClickListener(onClickAddIncSaveBtn);
        addIncFrqRG.setOnCheckedChangeListener(onCheckAddIncFrq);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckAddIncFrq = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addIncWklyRB:
                    addIncFrqRB = "52";
                    break;
                case R.id.addIncBiWklyRB:
                    addIncFrqRB = "26";
                    break;
                case R.id.addIncBiMthlyRB:
                    addIncFrqRB = "24";
                    break;
                case R.id.addIncMthlyRB:
                    addIncFrqRB = "12";
                    break;
                case R.id.addIncBiAnnlyRB:
                    addIncFrqRB = "2";
                    break;
                case R.id.addIncAnnlyRB:
                    addIncFrqRB = "1";
                    break;
                default:
                    addIncFrqRB = "1";
            }
        }
    };

    View.OnClickListener onClickAddIncCancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addIncToList = new Intent(AddIncome.this, AddIncomeList.class);
            addIncToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(addIncToList);
        }
    };

        View.OnClickListener onClickAddIncSaveBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incNameFromEntry = addIncGen.stringFromET(addIncCatET);
                incAmtFromEntry = addIncGen.dblFromET(addIncAmtET);
                try {
                    incFrqFromEntry = Double.valueOf(addIncFrqRB);
                } catch (NullPointerException e) {
                    incFrqFromEntry = 1.0;
                }
                incAnnAmtFromEntry = incAmtFromEntry * incFrqFromEntry;

                if (!incNameFromEntry.equals("null")) {

                    addIncIncDb = new BudgetDb(
                            incNameFromEntry,
                            incAmtFromEntry,
                            "I",
                            incFrqFromEntry,
                            incAnnAmtFromEntry,
                            "N/A",
                            "N",
                            0);

                    addIncDbMgr.addBudget(addIncIncDb);

                    addIncToList = new Intent(AddIncome.this, AddIncomeList.class);
                    addIncToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(addIncToList);
                } else {
                    Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                }
            }
        };
    }
