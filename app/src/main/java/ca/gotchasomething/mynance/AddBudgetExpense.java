package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class AddBudgetExpense extends LayoutBudget {

    Button budgetAddExpenseButton, budgetCancelExpenseButton, budgetUpdateExpenseButton;
    DbManager dbManager;
    Double expenseAmount = 0.0, expenseAnnualAmount = 0.0, expenseAAnnualAmount = 0.0, expenseBAnnualAmount = 0.0, expenseFrequency = 0.0;
    EditText budgetExpenseAmount, budgetExpenseCategory;
    ExpenseBudgetDb expenseBudgetDb;
    Intent backToBudget;
    long id = 0;
    RadioButton budgetExpenseARadioButton, budgetExpenseBRadioButton, budgetExpenseBiWeeklyRadioButton, budgetExpenseBiAnnuallyRadioButton,
            budgetExpenseBiMonthlyRadioButton, budgetExpenseMonthlyRadioButton, budgetExpenseNoRadioButton, budgetExpenseWeeklyRadioButton, budgetExpenseYesRadioButton;
    RadioGroup budgetExpenseABRadioGroup, budgetExpenseFrequencyRadioGroup, budgetExpenseReminderRadioGroup;
    String expenseFrequencyS = null, expenseName = null, expensePriority = null, expensePriorityS = null, expenseWeekly = null, expenseWeeklyS = null;
    TextView weeklyGuidanceLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_budget_expense);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        budgetExpenseCategory = findViewById(R.id.budgetExpenseCategory);
        budgetExpenseAmount = findViewById(R.id.budgetExpenseAmount);
        budgetExpenseFrequencyRadioGroup = findViewById(R.id.budgetExpenseFrequencyRadioGroup);
        budgetExpenseABRadioGroup = findViewById(R.id.budgetExpenseABRadioGroup);
        budgetExpenseReminderRadioGroup = findViewById(R.id.budgetExpenseReminderRadioGroup);
        budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
        budgetCancelExpenseButton = findViewById(R.id.budgetCancelExpenseButton);
        budgetAddExpenseButton = findViewById(R.id.budgetAddExpenseButton);
        budgetUpdateExpenseButton = findViewById(R.id.budgetUpdateExpenseButton);
        budgetUpdateExpenseButton.setVisibility(View.GONE);
        weeklyGuidanceLabel = findViewById(R.id.weeklyGuidanceLabel);
        weeklyGuidanceLabel.setVisibility(View.GONE);

        budgetExpenseWeeklyRadioButton = findViewById(R.id.budgetExpenseWeeklyRadioButton);
        budgetExpenseBiWeeklyRadioButton = findViewById(R.id.budgetExpenseBiWeeklyRadioButton);
        budgetExpenseBiMonthlyRadioButton = findViewById(R.id.budgetExpenseBiMonthlyRadioButton);
        budgetExpenseMonthlyRadioButton = findViewById(R.id.budgetExpenseMonthlyRadioButton);
        budgetExpenseBiAnnuallyRadioButton = findViewById(R.id.budgetExpenseBiAnnuallyRadioButton);

        budgetExpenseARadioButton = findViewById(R.id.budgetExpenseARadioButton);
        budgetExpenseBRadioButton = findViewById(R.id.budgetExpenseBRadioButton);

        budgetExpenseYesRadioButton = findViewById(R.id.budgetExpenseYesRadioButton);
        budgetExpenseNoRadioButton = findViewById(R.id.budgetExpenseNoRadioButton);

        budgetCancelExpenseButton.setOnClickListener(onClickCancelExpenseButton);
        budgetAddExpenseButton.setOnClickListener(onClickAddExpenseButton);
        budgetExpenseFrequencyRadioGroup.setOnCheckedChangeListener(onCheckExpenseFrequency);
        budgetExpenseABRadioGroup.setOnCheckedChangeListener(onCheckExpenseAB);
        budgetExpenseReminderRadioGroup.setOnCheckedChangeListener(onCheckExpenseReminder);

        dbManager = new DbManager(this);
    }

    //handle radioGroup for expenseFrequency
    RadioGroup.OnCheckedChangeListener onCheckExpenseFrequency = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.budgetExpenseWeeklyRadioButton:
                    expenseFrequencyS = "52";
                    break;
                case R.id.budgetExpenseBiWeeklyRadioButton:
                    expenseFrequencyS = "26";
                    break;
                case R.id.budgetExpenseBiMonthlyRadioButton:
                    expenseFrequencyS = "24";
                    break;
                case R.id.budgetExpenseMonthlyRadioButton:
                    expenseFrequencyS = "12";
                    break;
                case R.id.budgetExpenseBiAnnuallyRadioButton:
                    expenseFrequencyS = "2";
                    break;
                case R.id.budgetExpenseAnnuallyRadioButton:
                    expenseFrequencyS = "1";
                    break;
                default:
                    expenseFrequencyS = "1";
            }
        }
    };


    //handle radioGroup for expense: needs vs expenses: optional
    RadioGroup.OnCheckedChangeListener onCheckExpenseAB = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.budgetExpenseARadioButton:
                    expensePriorityS = "A";
                    expenseWeeklyS = "N";
                    break;
                case R.id.budgetExpenseBRadioButton:
                    expensePriorityS = "B";
                    budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                    weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                    break;
                default:
                    expensePriorityS = "A";
            }

        }
    };


    //handle radioGroup for weekly reminders on optional expenses
    RadioGroup.OnCheckedChangeListener onCheckExpenseReminder = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.budgetExpenseYesRadioButton:
                    expenseWeeklyS = "Y";
                    break;
                case R.id.budgetExpenseNoRadioButton:
                    expenseWeeklyS = "N";
                    break;
                default:
                    expenseWeeklyS = "N";
            }
        }
    };

    public void backToBudget() {
        backToBudget = new Intent(AddBudgetExpense.this, LayoutBudget.class);
        backToBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToBudget);
    }


    View.OnClickListener onClickCancelExpenseButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToBudget();
        }
    };

    View.OnClickListener onClickAddExpenseButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (budgetExpenseCategory.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            } else {
                expenseName = budgetExpenseCategory.getText().toString();
                if ((budgetExpenseAmount.getText().toString().equals(""))) {
                    expenseAmount = 0.0;
                } else {
                    expenseAmount = Double.valueOf(budgetExpenseAmount.getText().toString());
                }
                expenseFrequency = Double.valueOf(expenseFrequencyS);
                expensePriority = String.valueOf(expensePriorityS);
                expenseWeekly = String.valueOf(expenseWeeklyS);

                expenseAnnualAmount = expenseAmount * expenseFrequency;

                if (expensePriorityS.equals("A")) {
                    expenseAAnnualAmount = expenseAnnualAmount;
                    expenseBAnnualAmount = 0.0;
                } else if (expensePriorityS.equals("B")) {
                    expenseBAnnualAmount = expenseAnnualAmount;
                    expenseAAnnualAmount = 0.0;
                }

                expenseBudgetDb = new ExpenseBudgetDb(
                        expenseName,
                        expenseAmount,
                        expenseFrequency,
                        expensePriority,
                        expenseWeekly,
                        expenseAnnualAmount,
                        expenseAAnnualAmount,
                        expenseBAnnualAmount,
                        0);

                dbManager.addExpense(expenseBudgetDb);
                expenseAdapter.updateExpenses(dbManager.getExpense());
                expenseAdapter.notifyDataSetChanged();

                backToBudget();
            }
        }
    };
}
