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

import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class AddIncomeSetUp extends LayoutBudget {

    Button saveIncomeButton, updateIncomeButton, cancelIncomeButton, doneIncomeButton;
    DbManager dbManager;
    Double incomeAmount = 0.0, incomeAmountD = 0.0, incomeAnnualAmount = 0.0, incomeAnnualAmountD = 0.0, incomeFrequency = 0.0, incomeFrequencyD = 0.0;
    EditText incomeAmountET, incomeCategory;
    IncomeBudgetDb incomeBudgetDb;
    IncomeListAdapter incomeListAdapter;
    Intent backToIncomeSetUp, backToSetUp;
    ListView incomeListView;
    long id;
    RadioButton incomeAnnuallyRadioButton, incomeBiAnnuallyRadioButton, incomeBiMonthlyRadioButton, incomeBiWeeklyRadioButton,
            incomeMonthlyRadioButton, incomeWeeklyRadioButton;
    RadioGroup incomeFrequencyRadioGroup;
    String incomeFrequencyS = null, incomeName = null, incomeNameS = null, incomeAmountS = null;
    TextView incomeListLabel;
    View incomeLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_income_list);

        dbManager = new DbManager(this);

        incomeListLabel = findViewById(R.id.incomeListLabel);
        doneIncomeButton = findViewById(R.id.doneIncomeButton);
        incomeLine = findViewById(R.id.incomeLine);
        incomeCategory = findViewById(R.id.incomeCategory);
        incomeAmountET = findViewById(R.id.incomeAmount);
        incomeFrequencyRadioGroup = findViewById(R.id.incomeFrequencyRadioGroup);
        saveIncomeButton = findViewById(R.id.saveIncomeButton);
        updateIncomeButton = findViewById(R.id.updateIncomeButton);
        updateIncomeButton.setVisibility(View.GONE);
        cancelIncomeButton = findViewById(R.id.cancelIncomeButton);

        incomeListView = findViewById(R.id.incomeListView);

        incomeWeeklyRadioButton = findViewById(R.id.incomeWeeklyRadioButton);
        incomeBiWeeklyRadioButton = findViewById(R.id.incomeBiWeeklyRadioButton);
        incomeBiMonthlyRadioButton = findViewById(R.id.incomeBiMonthlyRadioButton);
        incomeMonthlyRadioButton = findViewById(R.id.incomeMonthlyRadioButton);
        incomeBiAnnuallyRadioButton = findViewById(R.id.incomeBiAnnuallyRadioButton);
        incomeAnnuallyRadioButton = findViewById(R.id.incomeAnnuallyRadioButton);

        cancelIncomeButton.setOnClickListener(onClickCancelIncomeButton);
        saveIncomeButton.setOnClickListener(onClickSaveIncomeButton);
        incomeFrequencyRadioGroup.setOnCheckedChangeListener(onCheckIncomeFrequency);
        doneIncomeButton.setOnClickListener(onClickDoneIncomeButton);

        incomeListAdapter = new IncomeListAdapter(this, dbManager.getIncomes());
        incomeListView.setAdapter(incomeListAdapter);

        incomeListView.setVisibility(View.VISIBLE);
        if (incomeListAdapter.getCount() == 0) {
            incomeListLabel.setVisibility(View.GONE);
            incomeListView.setVisibility(View.GONE);
            incomeListLabel.setVisibility(View.GONE);
            doneIncomeButton.setVisibility(View.GONE);
            incomeLine.setVisibility(View.GONE);
        } else {
            doneIncomeButton.setVisibility(View.VISIBLE);
            incomeListLabel.setVisibility(View.VISIBLE);
            incomeListView.setVisibility(View.VISIBLE);
            incomeLine.setVisibility(View.VISIBLE);
        }
        updateIncomeButton.setVisibility(View.GONE);
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
        backToIncomeSetUp = new Intent(AddIncomeSetUp.this, AddIncomeSetUp.class);
        backToIncomeSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToIncomeSetUp);
    }

    public void backToSetUp() {
        backToSetUp = new Intent(AddIncomeSetUp.this, LayoutSetUp.class);
        backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUp);
    }

    View.OnClickListener onClickDoneIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incomeDone = 1;

            setUpDb = new SetUpDb(incomeDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            backToSetUp();
        }
    };

    View.OnClickListener onClickCancelIncomeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToIncomeSetUp();
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
                incomeAdapter.updateIncomes(dbManager.getIncomes());
                incomeAdapter.notifyDataSetChanged();

                if (dbManager.incomeDoneCheck > 0) {
                    backToIncomeSetUp();
                }
            }
        }
    };

    public class IncomeListAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public IncomeListAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<IncomeBudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderIncomeList incomeListHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_budget_income,
                        parent, false);

                incomeListHolder = new ViewHolderIncomeList();
                incomeListHolder.incomeName = convertView.findViewById(R.id.budgetIncomeCategoryText);
                incomeListHolder.incomeAmount = convertView.findViewById(R.id.budgetIncomeAmountText);
                incomeListHolder.incomeDeleted = convertView.findViewById(R.id.deleteIncomeButton);
                incomeListHolder.incomeEdit = convertView.findViewById(R.id.editIncomeButton);
                convertView.setTag(incomeListHolder);

            } else {
                incomeListHolder = (ViewHolderIncomeList) convertView.getTag();
            }

            incomeListHolder.incomeName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            try {
                incomeAnnualAmountS = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
                if (!incomeAnnualAmountS.equals("")) {
                    incomeAnnualAmountD = Double.valueOf(incomeAnnualAmountS);
                } else {
                    incomeAnnualAmountD = 0.0;
                }
                incomeAnnualAmount2 = currencyFormat.format(incomeAnnualAmountD);
                incomeListHolder.incomeAmount.setText(incomeAnnualAmount2);
            } catch (NumberFormatException e2) {
                incomeListHolder.incomeAmount.setText(incomeAnnualAmount2);
            }

            incomeListHolder.incomeDeleted.setTag(incomes.get(position));
            incomeListHolder.incomeEdit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            incomeListHolder.incomeEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_income_list);
                    AddIncomeSetUp.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    incomeListLabel = findViewById(R.id.incomeListLabel);
                    incomeListLabel.setVisibility(View.GONE);
                    doneIncomeButton = findViewById(R.id.doneIncomeButton);
                    doneIncomeButton.setVisibility(View.GONE);
                    incomeLine = findViewById(R.id.incomeLine);
                    incomeLine.setVisibility(View.GONE);
                    incomeCategory = findViewById(R.id.incomeCategory);
                    incomeAmountET = findViewById(R.id.incomeAmount);
                    incomeFrequencyRadioGroup = findViewById(R.id.incomeFrequencyRadioGroup);
                    saveIncomeButton = findViewById(R.id.saveIncomeButton);
                    saveIncomeButton.setVisibility(View.GONE);
                    updateIncomeButton = findViewById(R.id.updateIncomeButton);
                    cancelIncomeButton = findViewById(R.id.cancelIncomeButton);

                    incomeWeeklyRadioButton = findViewById(R.id.incomeWeeklyRadioButton);
                    incomeBiWeeklyRadioButton = findViewById(R.id.incomeBiWeeklyRadioButton);
                    incomeBiMonthlyRadioButton = findViewById(R.id.incomeBiMonthlyRadioButton);
                    incomeMonthlyRadioButton = findViewById(R.id.incomeMonthlyRadioButton);
                    incomeBiAnnuallyRadioButton = findViewById(R.id.incomeBiAnnuallyRadioButton);
                    incomeAnnuallyRadioButton = findViewById(R.id.incomeAnnuallyRadioButton);

                    incomeBudgetDb = (IncomeBudgetDb) incomeListHolder.incomeEdit.getTag();
                    incomeId = String.valueOf(incomeBudgetDb.getId());

                    incomeCategory.setText(incomeBudgetDb.getIncomeName());

                    incomeAmountD = incomeBudgetDb.getIncomeAmount();
                    incomeAmountS = currencyFormat.format(incomeAmountD);
                    incomeAmountET.setText(incomeAmountS);

                    if (incomeBudgetDb.getIncomeFrequency() == 52) {
                        incomeWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "52";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 26) {
                        incomeBiWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "26";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 24) {
                        incomeBiMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "24";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 12) {
                        incomeMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "12";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 2) {
                        incomeBiAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "2";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 1) {
                        incomeAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "1";
                    }

                    //update db if changed
                    incomeFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                            }
                        }
                    });

                    cancelIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToIncomeSetUp();
                        }
                    });

                    updateIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (incomeCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryInc = incomeCategory.getText().toString();
                                amountEntry = general.extractingDouble(incomeAmountET);
                                frequencyEntry = Double.valueOf(incomeFrequencyS);
                                annualIncome = amountEntry * frequencyEntry;

                                incomeBudgetDb.setIncomeName(nameEntryInc);
                                incomeBudgetDb.setIncomeAmount(amountEntry);
                                incomeBudgetDb.setIncomeFrequency(frequencyEntry);
                                incomeBudgetDb.setIncomeAnnualAmount(annualIncome);

                                dbManager.updateIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                backToIncomeSetUp();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            incomeListHolder.incomeDeleted.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    incomeBudgetDb = (IncomeBudgetDb) incomeListHolder.incomeDeleted.getTag();

                    dbManager.deleteIncome(incomeBudgetDb);
                    incomeAdapter.updateIncomes(dbManager.getIncomes());
                    incomeAdapter.notifyDataSetChanged();

                    backToIncomeSetUp();
                }
            });

            return convertView;
        }
    }

    private static class ViewHolderIncomeList {
        private TextView incomeName;
        private TextView incomeAmount;
        private ImageButton incomeDeleted;
        private ImageButton incomeEdit;
    }
}
