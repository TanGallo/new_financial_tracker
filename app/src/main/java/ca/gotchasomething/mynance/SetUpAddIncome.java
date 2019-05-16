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

import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class SetUpAddIncome extends LayoutBudget {

    Button saveIncomeButton, updateIncomeButton, cancelIncomeButton;
    DbManager dbManager;
    Double incomeAmount = 0.0, incomeAmountD = 0.0, incomeAnnualAmount = 0.0, incomeAnnualAmountD = 0.0, incomeFrequency = 0.0, incomeFrequencyD = 0.0;
    EditText incomeAmountET, incomeCategory;
    Intent backToIncomeSetUp, showList;
    long id;
    RadioButton incomeAnnuallyRadioButton, incomeBiAnnuallyRadioButton, incomeBiMonthlyRadioButton, incomeBiWeeklyRadioButton,
            incomeMonthlyRadioButton, incomeWeeklyRadioButton;
    RadioGroup incomeFrequencyRadioGroup;
    String incomeFrequencyS = null, incomeName = null, incomeNameS = null;
    TextView incomeAmountLabel, incomeCategoryLabel, incomeFrequencyLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_add_income);

        dbManager = new DbManager(this);

        incomeCategoryLabel = findViewById(R.id.incomeCategoryLabel);
        incomeCategory = findViewById(R.id.incomeCategory);
        incomeAmountLabel = findViewById(R.id.incomeAmountLabel);
        incomeAmountET = findViewById(R.id.incomeAmount);
        incomeFrequencyLabel = findViewById(R.id.incomeFrequencyLabel);
        incomeFrequencyRadioGroup = findViewById(R.id.incomeFrequencyRadioGroup);
        saveIncomeButton = findViewById(R.id.saveIncomeButton);
        updateIncomeButton = findViewById(R.id.updateIncomeButton);
        updateIncomeButton.setVisibility(View.GONE);
        cancelIncomeButton = findViewById(R.id.cancelIncomeButton);

        incomeWeeklyRadioButton = findViewById(R.id.incomeWeeklyRadioButton);
        incomeBiWeeklyRadioButton = findViewById(R.id.incomeBiWeeklyRadioButton);
        incomeBiMonthlyRadioButton = findViewById(R.id.incomeBiMonthlyRadioButton);
        incomeMonthlyRadioButton = findViewById(R.id.incomeMonthlyRadioButton);
        incomeBiAnnuallyRadioButton = findViewById(R.id.incomeBiAnnuallyRadioButton);
        incomeAnnuallyRadioButton = findViewById(R.id.incomeAnnuallyRadioButton);

        cancelIncomeButton.setOnClickListener(onClickCancelIncomeButton);
        saveIncomeButton.setOnClickListener(onClickSaveIncomeButton);
        incomeFrequencyRadioGroup.setOnCheckedChangeListener(onCheckIncomeFrequency);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckIncomeFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.incomeWeeklyRadioButton:
                    incomeFrequencyS = "52";
                    break;
                case R.id.incomeBiWeeklyRadioButton:
                    incomeFrequencyS = "26";
                    break;
                case R.id.incomeBiMonthlyRadioButton:
                    incomeFrequencyS = "24";
                    break;
                case R.id.incomeMonthlyRadioButton:
                    incomeFrequencyS = "12";
                    break;
                case R.id.incomeBiAnnuallyRadioButton:
                    incomeFrequencyS = "2";
                    break;
                case R.id.incomeAnnuallyRadioButton:
                    incomeFrequencyS = "1";
                    break;
                default:
                    incomeFrequencyS = "1";
            }
        }
    };

    public void backToIncomeSetUp() {
        backToIncomeSetUp = new Intent(SetUpAddIncome.this, SetUpAddIncome.class);
        backToIncomeSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToIncomeSetUp);
    }

    public void showList() {
        showList = new Intent(SetUpAddIncome.this, SetUpAddIncomeList.class);
        showList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(showList);
    }

    View.OnClickListener onClickCancelIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showList();
        }
    };

    View.OnClickListener onClickSaveIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (incomeCategory.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                incomeNameS = incomeCategory.getText().toString();
                if (incomeAmountET.getText().toString().equals("")) {
                    incomeAmountD = 0.0;
                } else {
                    incomeAmountD = Double.valueOf(incomeAmountET.getText().toString());
                }
                try {
                    incomeFrequencyD = Double.valueOf(incomeFrequencyS);
                } catch (NullPointerException e) {
                    incomeFrequencyD = 1.0;
                }
                incomeAnnualAmountD = incomeAmountD * incomeFrequencyD;

                incomeName = incomeNameS;
                incomeAmount = incomeAmountD;
                incomeFrequency = incomeFrequencyD;
                incomeAnnualAmount = incomeAnnualAmountD;

                incomeBudgetDb = new IncomeBudgetDb(
                        incomeName,
                        incomeAmount,
                        incomeFrequency,
                        incomeAnnualAmount,
                        0);

                dbManager.addIncome(incomeBudgetDb);
                //incomeAdapter.updateIncomes(dbManager.getIncomes());
                //incomeAdapter.notifyDataSetChanged();

                showList();
            }
        }
    };
}
