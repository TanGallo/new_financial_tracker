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

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class SetUpAddBills extends LayoutBudget {

    Button saveBillsButton, updateBillsButton, cancelBillsButton;
    DbManager dbManager;
    Double billsAmount = 0.0, billsAmountD = 0.0, billsAnnualAmount = 0.0, billsAAnnualAmount = 0.0, billsBAnnualAmount = 0.0, billsAnnualAmountD = 0.0,
            billsFrequency = 0.0, billsFrequencyD = 0.0;
    EditText billsAmountET, billsCategory;
    Intent backToBillsSetUp, showList;
    long id;
    RadioButton billsAnnuallyRadioButton, billsBiAnnuallyRadioButton, billsBiMonthlyRadioButton, billsBiWeeklyRadioButton,
            billsMonthlyRadioButton, billsWeeklyRadioButton;
    RadioGroup billsFrequencyRadioGroup;
    String billsFrequencyS = null, billsName = null, billsNameS = null, billsPriority = null, billsWeekly = null;
    TextView billsAmountLabel, billsCategoryLabel, billsFrequencyLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_add_bills);

        dbManager = new DbManager(this);

        billsCategoryLabel = findViewById(R.id.billsCategoryLabel);
        billsCategory = findViewById(R.id.billsCategory);
        billsAmountLabel = findViewById(R.id.billsAmountLabel);
        billsAmountET = findViewById(R.id.billsAmount);
        billsFrequencyLabel = findViewById(R.id.billsFrequencyLabel);
        billsFrequencyRadioGroup = findViewById(R.id.billsFrequencyRadioGroup);
        saveBillsButton = findViewById(R.id.saveBillsButton);
        updateBillsButton = findViewById(R.id.updateBillsButton);
        updateBillsButton.setVisibility(View.GONE);
        cancelBillsButton = findViewById(R.id.cancelBillsButton);

        billsWeeklyRadioButton = findViewById(R.id.billsWeeklyRadioButton);
        billsBiWeeklyRadioButton = findViewById(R.id.billsBiWeeklyRadioButton);
        billsBiMonthlyRadioButton = findViewById(R.id.billsBiMonthlyRadioButton);
        billsMonthlyRadioButton = findViewById(R.id.billsMonthlyRadioButton);
        billsBiAnnuallyRadioButton = findViewById(R.id.billsBiAnnuallyRadioButton);
        billsAnnuallyRadioButton = findViewById(R.id.billsAnnuallyRadioButton);

        cancelBillsButton.setOnClickListener(onClickCancelBillsButton);
        saveBillsButton.setOnClickListener(onClickSaveBillsButton);
        billsFrequencyRadioGroup.setOnCheckedChangeListener(onCheckBillsFrequency);
    }

    //handle radioGroup for incomeFrequency
    RadioGroup.OnCheckedChangeListener onCheckBillsFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.billsWeeklyRadioButton:
                    billsFrequencyS = "52";
                    break;
                case R.id.billsBiWeeklyRadioButton:
                    billsFrequencyS = "26";
                    break;
                case R.id.billsBiMonthlyRadioButton:
                    billsFrequencyS = "24";
                    break;
                case R.id.billsMonthlyRadioButton:
                    billsFrequencyS = "12";
                    break;
                case R.id.billsBiAnnuallyRadioButton:
                    billsFrequencyS = "2";
                    break;
                case R.id.billsAnnuallyRadioButton:
                    billsFrequencyS = "1";
                    break;
                default:
                    billsFrequencyS = "1";
            }
        }
    };

    public void backToBillsSetUp() {
        backToBillsSetUp = new Intent(SetUpAddBills.this, SetUpAddBills.class);
        backToBillsSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToBillsSetUp);
    }

    public void showList() {
        showList = new Intent(SetUpAddBills.this, SetUpAddBillsList.class);
        showList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(showList);
    }

    View.OnClickListener onClickCancelBillsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showList();
        }
    };

    View.OnClickListener onClickSaveBillsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (billsCategory.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                billsNameS = billsCategory.getText().toString();
                if (billsAmountET.getText().toString().equals("")) {
                    billsAmountD = 0.0;
                } else {
                    billsAmountD = Double.valueOf(billsAmountET.getText().toString());
                }
                try {
                    billsFrequencyD = Double.valueOf(billsFrequencyS);
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
                billsBAnnualAmount = 0.0;

                expenseBudgetDb = new ExpenseBudgetDb(
                        billsName,
                        billsAmount,
                        billsFrequency,
                        billsPriority,
                        billsWeekly,
                        billsAnnualAmount,
                        billsAAnnualAmount,
                        billsBAnnualAmount,
                        0);

                dbManager.addExpense(expenseBudgetDb);
                //expenseAdapter.updateExpenses(dbManager.getExpense());
                //expenseAdapter.notifyDataSetChanged();

                showList();
            }
        }
    };
}
