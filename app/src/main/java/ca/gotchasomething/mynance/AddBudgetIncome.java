package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class AddBudgetIncome extends LayoutBudget {

    Button budgetAddIncomeButton, budgetUpdateIncomeButton;
    DbManager dbManager;
    Double incomeAmount = 0.0, incomeAnnualAmount = 0.0, incomeFrequency = 0.0;
    EditText budgetIncomeAmount, budgetIncomeCategory;
    IncomeBudgetDb incomeBudgetDb;
    Intent backToBudget;
    long id;
    RadioButton budgetIncomeAnnuallyRadioButton, budgetIncomeBiAnnuallyRadioButton, budgetIncomeBiMonthlyRadioButton, budgetIncomeBiWeeklyRadioButton,
            budgetIncomeMonthlyRadioButton, budgetIncomeWeeklyRadioButton;
    RadioGroup budgetIncomeFrequencyRadioGroup;
    String incomeFrequencyS = null, incomeName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_budget_income);

        budgetIncomeCategory = findViewById(R.id.budgetIncomeCategory);
        budgetIncomeAmount = findViewById(R.id.budgetIncomeAmount);
        budgetIncomeFrequencyRadioGroup = findViewById(R.id.budgetIncomeFrequencyRadioGroup);
        budgetAddIncomeButton = findViewById(R.id.budgetAddIncomeButton);
        budgetUpdateIncomeButton = findViewById(R.id.budgetUpdateIncomeButton);
        budgetUpdateIncomeButton.setVisibility(View.GONE);
        budgetCancelIncomeButton = findViewById(R.id.budgetCancelIncomeButton);

        budgetIncomeWeeklyRadioButton = findViewById(R.id.budgetIncomeWeeklyRadioButton);
        budgetIncomeBiWeeklyRadioButton = findViewById(R.id.budgetIncomeBiWeeklyRadioButton);
        budgetIncomeBiMonthlyRadioButton = findViewById(R.id.budgetIncomeBiMonthlyRadioButton);
        budgetIncomeMonthlyRadioButton = findViewById(R.id.budgetIncomeMonthlyRadioButton);
        budgetIncomeBiAnnuallyRadioButton = findViewById(R.id.budgetIncomeBiAnnuallyRadioButton);
        budgetIncomeAnnuallyRadioButton = findViewById(R.id.budgetIncomeAnnuallyRadioButton);

        budgetCancelIncomeButton.setOnClickListener(onClickCancelIncomeButton);
        budgetAddIncomeButton.setOnClickListener(onClickAddIncomeButton);
        budgetIncomeFrequencyRadioGroup.setOnCheckedChangeListener(onCheckIncomeFrequency);

        dbManager = new DbManager(this);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckIncomeFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.budgetIncomeWeeklyRadioButton:
                    incomeFrequencyS = "52";
                    break;
                case R.id.budgetIncomeBiWeeklyRadioButton:
                    incomeFrequencyS = "26";
                    break;
                case R.id.budgetIncomeBiMonthlyRadioButton:
                    incomeFrequencyS = "24";
                    break;
                case R.id.budgetIncomeMonthlyRadioButton:
                    incomeFrequencyS = "12";
                    break;
                case R.id.budgetIncomeBiAnnuallyRadioButton:
                    incomeFrequencyS = "2";
                    break;
                case R.id.budgetIncomeAnnuallyRadioButton:
                    incomeFrequencyS = "1";
                    break;
                default:
                    incomeFrequencyS = "1";
            }
        }
    };

    public void backToBudget() {
        backToBudget = new Intent(AddBudgetIncome.this, LayoutBudget.class);
        backToBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToBudget);
    }

    View.OnClickListener onClickCancelIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToBudget();
        }
    };

    View.OnClickListener onClickAddIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(budgetIncomeCategory.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                incomeName = budgetIncomeCategory.getText().toString();
                if(budgetIncomeAmount.getText().toString().equals("")) {
                    incomeAmount = 0.0;
                } else {
                    incomeAmount = Double.valueOf(budgetIncomeAmount.getText().toString());
                }
                incomeFrequency = Double.valueOf(incomeFrequencyS);
                incomeAnnualAmount = incomeAmount * incomeFrequency;

                incomeBudgetDb = new IncomeBudgetDb(
                        incomeName,
                        incomeAmount,
                        incomeFrequency,
                        incomeAnnualAmount,
                        0);

                dbManager.addIncome(incomeBudgetDb);
                incomeAdapter.updateIncomes(dbManager.getIncomes());
                incomeAdapter.notifyDataSetChanged();

                backToBudget();
            }
        }
    };
}
