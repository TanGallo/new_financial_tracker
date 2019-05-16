package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class SetUpAddDebtsList extends LayoutDebt {

    Button addMoreDebtsButton, updateDebtButton, cancelDebtButton, doneDebtsButton, saveDebtButton;
    ContentValues cv17;
    DbHelper helper17;
    DbManager dbManager;
    DebtDb debtDb;
    Double debtsAmountD = 0.0, debtsAnnualAmountD = 0.0;
    EditText debtsAmountET, debtsCategory;
    DebtsListAdapter debtsListAdapter;
    Intent backToDebtsSetUp, backToSetUp, backToSetUpDebts;
    ListView debtsListView;
    long id;
    RadioButton debtsAnnuallyRadioButton, debtsBiAnnuallyRadioButton, debtsBiMonthlyRadioButton, debtsBiWeeklyRadioButton,
            debtsMonthlyRadioButton, debtsWeeklyRadioButton;
    RadioGroup debtsFrequencyRadioGroup;
    SQLiteDatabase db17;
    String debtsFrequencyS = null, debtsAnnualAmountS = null, debtsAnnualAmount2 = null, debtsAmountS = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_add_debts_list);

        dbManager = new DbManager(this);

        debtsListView = findViewById(R.id.debtsListView);
        addMoreDebtsButton = findViewById(R.id.addMoreDebtsButton);
        doneDebtsButton = findViewById(R.id.doneDebtsButton);

        addMoreDebtsButton.setOnClickListener(onClickAddMoreDebtsButton);
        doneDebtsButton.setOnClickListener(onClickDoneDebtsButton);

        debtsListAdapter = new DebtsListAdapter(this, dbManager.getDebts());
        debtsListView.setAdapter(debtsListAdapter);
    }

    public void backToDebtsSetUpList() {
        backToDebtsSetUp = new Intent(SetUpAddDebtsList.this, SetUpAddDebtsList.class);
        backToDebtsSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToDebtsSetUp);
    }

    public void backToSetUp() {
        backToSetUp = new Intent(SetUpAddDebtsList.this, LayoutSetUp.class);
        backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUp);
    }

    public void backToSetUpDebts() {
        backToSetUpDebts = new Intent(SetUpAddDebtsList.this, AddDebt.class);
        backToSetUpDebts.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUpDebts);
    }

    View.OnClickListener onClickAddMoreDebtsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToSetUpDebts();
        }
    };

    View.OnClickListener onClickDoneDebtsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cv17 = new ContentValues();
            cv17.put(DbHelper.LATESTDONE, "debts");
            helper17 = new DbHelper(getApplicationContext());
            db17 = helper17.getWritableDatabase();
            db17.update(DbHelper.SET_UP_TABLE_NAME, cv17, DbHelper.ID + "= '1'", null);
            db17.close();

            backToSetUp();
        }
    };

    public class DebtsListAdapter extends ArrayAdapter<DebtDb> {

        public Context context;
        public List<DebtDb> debts;

        public DebtsListAdapter(
                Context context,
                List<DebtDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<DebtDb> debts) {
            this.debts = debts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return debts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderDebtsList debtsListHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_debt,
                        parent, false);

                debtsListHolder = new ViewHolderDebtsList();
                debtsListHolder.debtsName = convertView.findViewById(R.id.debtListName);
                debtsListHolder.debtsAmount = convertView.findViewById(R.id.debtListAmount);
                debtsListHolder.debtsFreeDateLabel = convertView.findViewById(R.id.debtListFreeDateLabel);
                debtsListHolder.debtsFreeDate = convertView.findViewById(R.id.debtListFreeDate);
                debtsListHolder.debtsDeleted = convertView.findViewById(R.id.deleteDebtButton);
                debtsListHolder.debtsEdit = convertView.findViewById(R.id.editDebtButton);
                debtsListHolder.debtsOverLimit = convertView.findViewById(R.id.debtOverLimitWarning);
                convertView.setTag(debtsListHolder);

            } else {
                debtsListHolder = (ViewHolderDebtsList) convertView.getTag();
            }

            //retrieve debtName
            debtsListHolder.debtsName.setText(debts.get(position).getDebtName());

            //retrieve debtAmount and format as currency
            try {
                debtAmountS = (String.valueOf(debts.get(position).getDebtAmount()));
                if (debtAmountS != null && !debtAmountS.equals("")) {
                    debtAmountD = Double.valueOf(debtAmountS);
                } else {
                    debtAmountD = 0.0;
                }
                debtAmount2 = currencyFormat.format(debtAmountD);
                debtsListHolder.debtsAmount.setText(debtAmount2);
            } catch (NumberFormatException e5) {
                debtsListHolder.debtsAmount.setText(debtAmount2);
            }

            //retrieve debtEnd
            debtEnd = debts.get(position).getDebtEnd();
            if (debtEnd.contains("2")) {
                debtsListHolder.debtsFreeDateLabel.setVisibility(View.VISIBLE);
            } else {
                debtsListHolder.debtsFreeDateLabel.setVisibility(View.GONE);
            }
            debtsListHolder.debtsFreeDate.setText(debtEnd);
            if (debtEnd.equals(getString(R.string.debt_paid))) {
                debtsListHolder.debtsFreeDate.setTextColor(Color.parseColor("#03ac13"));
            } else if (debtEnd.equals(getString(R.string.too_far))) {
                debtsListHolder.debtsFreeDate.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                debtsListHolder.debtsFreeDate.setTextColor(Color.parseColor("#303F9F"));
                debtsListHolder.debtsFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            incRefKeyD = debts.get(position).getIncRefKeyD();

            for (IncomeBudgetDb i : dbManager.getIncomes()) {
                if (String.valueOf(i.getId()).equals(String.valueOf(incRefKeyD))) {
                    debtAnnualIncomeb = i.getIncomeAnnualAmount();
                }
            }

            if (debts.get(position).getDebtLimit() == 0) {
                debtsListHolder.debtsOverLimit.setVisibility(View.GONE);
            } else if (debts.get(position).getDebtAmount() > debts.get(position).getDebtLimit()) {
                debtsListHolder.debtsOverLimit.setVisibility(View.VISIBLE);
            } else {
                debtsListHolder.debtsOverLimit.setVisibility(View.GONE);
            }

            debtsListHolder.debtsDeleted.setTag(debts.get(position));
            debtsListHolder.debtsEdit.setTag(debts.get(position));

            //click on pencil icon to edit a data record
            debtsListHolder.debtsEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_debt);
                    SetUpAddDebtsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getApplicationContext());

                    debtNameEntry = findViewById(R.id.debtNameEntry);
                    debtLimitEntry = findViewById(R.id.debtLimitEntry);
                    debtAmountEntry = findViewById(R.id.debtAmountEntry);
                    debtPercentEntry = findViewById(R.id.debtPercentEntry);
                    debtPaymentsEntry = findViewById(R.id.debtPaymentsEntry);
                    debtDateResult = findViewById(R.id.debtDateResult);
                    debtDateResultLabel = findViewById(R.id.debtDateResultLabel);

                    debtFrequencyRadioGroup = findViewById(R.id.debtFrequencyRadioGroup);

                    debtWeeklyRadioButton = findViewById(R.id.debtWeeklyRadioButton);
                    debtBiWeeklyRadioButton = findViewById(R.id.debtBiWeeklyRadioButton);
                    debtMonthlyRadioButton = findViewById(R.id.debtMonthlyRadioButton);

                    saveDebtButton = findViewById(R.id.saveDebtButton);
                    saveDebtButton.setVisibility(View.GONE);
                    updateDebtButton = findViewById(R.id.updateDebtButton);
                    cancelDebtButton = findViewById(R.id.cancelDebtButton);

                    debtDb = (DebtDb) debtsListHolder.debtsEdit.getTag();

                    debtNameb = debtDb.getDebtName();
                    debtNameEntry.setText(debtNameb);

                    debtLimitb = debtDb.getDebtLimit();
                    debtLimitS2 = currencyFormat.format(debtLimitb);
                    debtLimitEntry.setText(debtLimitS2);

                    debtAmount = debtDb.getDebtAmount();
                    debtAmountS2 = currencyFormat.format(debtAmount);
                    debtAmountEntry.setText(debtAmountS2);
                    debtAmountEntry.addTextChangedListener(onChangeDebtAmount);

                    debtRate = debtDb.getDebtRate();
                    debtPercentD2 = debtRate / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    debtPercentS = percentFormat.format(debtPercentD2);
                    debtPercentEntry.setText(debtPercentS);
                    debtPercentEntry.addTextChangedListener(onChangeDebtPercent);

                    debtPayments = debtDb.getDebtPayments();
                    debtPaymentsS = currencyFormat.format(debtPayments);
                    debtPaymentsEntry.setText(debtPaymentsS);
                    debtPaymentsEntry.addTextChangedListener(onChangeDebtPayments);

                    debtFrequency = debtDb.getDebtFrequency();

                    debtAnnualIncome = debtDb.getDebtAnnualIncome();

                    debtEnd = debtDb.getDebtEnd();
                    debtDateResult.setText(debtEnd);
                    if (debtEnd.equals(getString(R.string.debt_paid))) {
                        debtDateResultLabel.setVisibility(View.GONE);
                        debtDateResult.setTextColor(Color.parseColor("#03ac13"));
                    } else if (debtEnd.equals(getString(R.string.too_far))) {
                        debtDateResultLabel.setVisibility(View.GONE);
                        debtDateResult.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        debtDateResultLabel.setVisibility(View.VISIBLE);
                        debtDateResult.setTextColor(Color.parseColor("#303F9F"));
                        debtDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    //set radio button selections from data
                    if (debtDb.getDebtFrequency() == 52) {
                        debtWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "52";
                    } else if (debtDb.getDebtFrequency() == 26) {
                        debtBiWeeklyRadioButton.setChecked(true);
                        debtFrequencyS = "26";
                    } else if (debtDb.getDebtFrequency() == 12) {
                        debtMonthlyRadioButton.setChecked(true);
                        debtFrequencyS = "12";
                    }

                    //update db if radio buttons changed
                    debtFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.debtWeeklyRadioButton:
                                    debtFrequencyS = "52";
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                                case R.id.debtBiWeeklyRadioButton:
                                    debtFrequencyS = "26";
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                                case R.id.debtMonthlyRadioButton:
                                    debtFrequencyS = "12";
                                    debtEndResult();
                                    debtDateResult.setText(debtEnd2);
                                    break;
                            }
                        }
                    });

                    updateDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyD = String.valueOf(debtDb.getExpRefKeyD());

                            debtEndResult();

                            //if (debtName != null) {

                            debtDb.setDebtName(debtName);
                            debtDb.setDebtLimit(debtLimit);
                            debtDb.setDebtAmount(debtAmount);
                            debtDb.setDebtRate(debtRate);
                            debtDb.setDebtPayments(debtPayments);
                            debtDb.setDebtFrequency(debtFrequency);
                            debtDb.setDebtEnd(debtEnd2);

                            dbHelper = new DbHelper(getContext());
                            expenseDb = dbHelper.getWritableDatabase();

                            String[] args = new String[]{expRefKeyD};
                            String[] args2 = new String[]{String.valueOf(debtDb.getId())};
                            String[] args3 = new String[]{String.valueOf(debtDb.getIncRefKeyD())};

                            values = new ContentValues();
                            values2 = new ContentValues();
                            values3 = new ContentValues();
                            values4 = new ContentValues();
                            values5 = new ContentValues();

                            values.put(DbHelper.EXPENSENAME, debtName);
                            values.put(DbHelper.EXPENSEAMOUNT, debtPayments);
                            values.put(DbHelper.EXPENSEFREQUENCY, debtFrequency);
                            expenseAnnualAmount = debtPayments * debtFrequency;
                            values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                            if (priorityData().equals("A")) {
                                values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                            } else if (priorityData().equals("B")) {
                                values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                            }
                            values2.put(DbHelper.INCOMENAME, debtName);
                            values3.put(DbHelper.MONEYOUTCAT, debtName);
                            values4.put(DbHelper.MONEYOUTDEBTCAT, debtName);
                            values5.put(DbHelper.MONEYINCAT, debtName);

                            try {
                                expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args3);
                                expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values4, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args2);
                                expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values5, DbHelper.INCREFKEYMI + "=?", args3);
                            } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                e.printStackTrace();
                            }

                            expenseDb.close();

                            dbManager.updateDebt(debtDb);
                            debtsListAdapter.updateDebts(dbManager.getDebts());
                            notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                            backToDebtsSetUpList();
                        } //else {
                        //Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                        //}
                        //}
                    });

                    cancelDebtButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToDebtsSetUpList();
                        }
                    });
                }
            });

            //click on trash can icon
            debtsListHolder.debtsDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    debtDb = (DebtDb) debtsListHolder.debtsDeleted.getTag();

                    dbHelper = new DbHelper(getContext());
                    expenseDb = dbHelper.getWritableDatabase();

                    try {
                        String[] args = new String[]{String.valueOf(debtDb.getExpRefKeyD())};
                        expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    try {
                        String[] args2 = new String[]{String.valueOf(debtDb.getIncRefKeyD())};
                        expenseDb.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    expenseDb.close();

                    dbManager.deleteDebt(debtDb);
                    debtsListAdapter.updateDebts(dbManager.getDebts());
                    notifyDataSetChanged();

                    backToDebtsSetUpList();
                }
            });

            return convertView;
        }
    }

    private static class ViewHolderDebtsList {
        private TextView debtsName;
        private TextView debtsAmount;
        private TextView debtsFreeDateLabel;
        private TextView debtsFreeDate;
        private ImageButton debtsDeleted;
        private ImageButton debtsEdit;
        private TextView debtsOverLimit;
    }
}
