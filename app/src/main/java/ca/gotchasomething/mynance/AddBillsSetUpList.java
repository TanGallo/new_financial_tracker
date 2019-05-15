package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class AddBillsSetUpList extends LayoutBudget {

    Button addMoreBillsButton, updateBillsButton, cancelBillsButton, doneBillsButton, saveBillsButton;
    DbManager dbManager;
    Double billsAmountD = 0.0, billsAnnualAmountD = 0.0;
    EditText billsAmountET, billsCategory;
    ExpenseBudgetDb expenseBudgetDb;
    BillsListAdapter billsListAdapter;
    Intent backToBillsSetUp, backToSetUp, backToSetUpBills;
    ListView billsListView;
    long id;
    RadioButton billsAnnuallyRadioButton, billsBiAnnuallyRadioButton, billsBiMonthlyRadioButton, billsBiWeeklyRadioButton,
            billsMonthlyRadioButton, billsWeeklyRadioButton;
    RadioGroup billsFrequencyRadioGroup;
    String billsFrequencyS = null, billsAnnualAmountS = null, billsAnnualAmount2 = null, billsAmountS = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_income_list);

        dbManager = new DbManager(this);

        billsListView = findViewById(R.id.billsListView);
        addMoreBillsButton = findViewById(R.id.addMoreBillsButton);
        doneBillsButton = findViewById(R.id.doneBillsButton);

        addMoreBillsButton.setOnClickListener(onClickAddMoreBillsButton);
        doneBillsButton.setOnClickListener(onClickDoneBillsButton);

        billsListAdapter = new BillsListAdapter(this, dbManager.getExpense());
        billsListView.setAdapter(billsListAdapter);
    }

    public void backToBillsSetUpList() {
        backToBillsSetUp = new Intent(AddBillsSetUpList.this, AddBillsSetUpList.class);
        backToBillsSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToBillsSetUp);
    }

    public void backToSetUp() {
        backToSetUp = new Intent(AddBillsSetUpList.this, LayoutSetUp.class);
        backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUp);
    }

    public void backToSetUpBills() {
        backToSetUpBills = new Intent(AddBillsSetUpList.this, AddBillsSetUp.class);
        backToSetUpBills.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUpBills);
    }

    View.OnClickListener onClickAddMoreBillsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToSetUpBills();
        }
    };

    View.OnClickListener onClickDoneBillsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            billsDone = 1;

            setUpDb = new SetUpDb(incomeDone, billsDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            backToSetUp();
        }
    };

    public class BillsListAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> expenses;

        public BillsListAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateBills(List<ExpenseBudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderBillsList billsListHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_expense,
                        parent, false);

                billsListHolder = new ViewHolderBillsList();
                billsListHolder.billsName = convertView.findViewById(R.id.budgetExpenseCategoryText);
                billsListHolder.billsAmount = convertView.findViewById(R.id.budgetExpenseAmountText);
                billsListHolder.billsDeleted = convertView.findViewById(R.id.deleteExpenseButton);
                billsListHolder.billsEdit = convertView.findViewById(R.id.editExpenseButton);
                convertView.setTag(billsListHolder);

            } else {
                billsListHolder = (ViewHolderBillsList) convertView.getTag();
            }

            billsListHolder.billsName.setText(expenses.get(position).getExpenseName());

            //retrieve incomeAnnualAmount and format as currency
            try {
                billsAnnualAmountS = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
                if (!billsAnnualAmountS.equals("")) {
                    billsAnnualAmountD = Double.valueOf(billsAnnualAmountS);
                } else {
                    billsAnnualAmountD = 0.0;
                }
                billsAnnualAmount2 = currencyFormat.format(billsAnnualAmountD);
                billsListHolder.billsAmount.setText(billsAnnualAmount2);
            } catch (NumberFormatException e2) {
                billsListHolder.billsAmount.setText(billsAnnualAmount2);
            }

            billsListHolder.billsDeleted.setTag(expenses.get(position));
            billsListHolder.billsEdit.setTag(expenses.get(position));

            //click on pencil icon to edit a data record
            billsListHolder.billsEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_bills);
                    AddBillsSetUpList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    billsCategory = findViewById(R.id.billsCategory);
                    billsAmountET = findViewById(R.id.billsAmount);
                    billsFrequencyRadioGroup = findViewById(R.id.billsFrequencyRadioGroup);
                    saveBillsButton = findViewById(R.id.saveBillsButton);
                    saveBillsButton.setVisibility(View.GONE);
                    updateBillsButton = findViewById(R.id.updateBillsButton);
                    cancelBillsButton = findViewById(R.id.cancelBillsButton);

                    billsWeeklyRadioButton = findViewById(R.id.billsWeeklyRadioButton);
                    billsBiWeeklyRadioButton = findViewById(R.id.billsBiWeeklyRadioButton);
                    billsBiMonthlyRadioButton = findViewById(R.id.billsBiMonthlyRadioButton);
                    billsMonthlyRadioButton = findViewById(R.id.billsMonthlyRadioButton);
                    billsBiAnnuallyRadioButton = findViewById(R.id.billsBiAnnuallyRadioButton);
                    billsAnnuallyRadioButton = findViewById(R.id.billsAnnuallyRadioButton);

                    expenseBudgetDb = (ExpenseBudgetDb) billsListHolder.billsEdit.getTag();

                    billsCategory.setText(expenseBudgetDb.getExpenseName());

                    billsAmountD = expenseBudgetDb.getExpenseAmount();
                    billsAmountS = currencyFormat.format(billsAmountD);
                    billsAmountET.setText(billsAmountS);

                    if (expenseBudgetDb.getExpenseFrequency() == 52) {
                        billsWeeklyRadioButton.setChecked(true);
                        billsFrequencyS = "52";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 26) {
                        billsBiWeeklyRadioButton.setChecked(true);
                        billsFrequencyS = "26";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 24) {
                        billsBiMonthlyRadioButton.setChecked(true);
                        billsFrequencyS = "24";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 12) {
                        billsMonthlyRadioButton.setChecked(true);
                        billsFrequencyS = "12";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 2) {
                        billsBiAnnuallyRadioButton.setChecked(true);
                        billsFrequencyS = "2";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 1) {
                        billsAnnuallyRadioButton.setChecked(true);
                        billsFrequencyS = "1";
                    }

                    //update db if changed
                    billsFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                            }
                        }
                    });

                    cancelBillsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBillsSetUpList();
                        }
                    });

                    updateBillsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (billsCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryExp = billsCategory.getText().toString();
                                amountEntry = general.extractingDouble(billsAmountET);
                                frequencyEntry = Double.valueOf(billsFrequencyS);
                                annualIncome = amountEntry * frequencyEntry;

                                expenseBudgetDb.setExpenseName(nameEntryInc);
                                expenseBudgetDb.setExpenseAmount(amountEntry);
                                expenseBudgetDb.setExpenseFrequency(frequencyEntry);
                                expenseBudgetDb.setExpenseAnnualAmount(annualIncome);

                                dbManager.updateExpense(expenseBudgetDb);
                                billsListAdapter.updateBills(dbManager.getExpense());
                                billsListAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                backToBillsSetUpList();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            billsListHolder.billsDeleted.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    expenseBudgetDb = (ExpenseBudgetDb) billsListHolder.billsDeleted.getTag();

                    dbManager.deleteExpense(expenseBudgetDb);
                    billsListAdapter.updateBills(dbManager.getExpense());
                    billsListAdapter.notifyDataSetChanged();

                    backToBillsSetUpList();
                }
            });

            return convertView;
        }
    }

    private static class ViewHolderBillsList {
        private TextView billsName;
        private TextView billsAmount;
        private ImageButton billsDeleted;
        private ImageButton billsEdit;
    }
}
