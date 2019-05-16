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

import ca.gotchasomething.mynance.data.SavingsDb;

public class SetUpAddSavingsList extends LayoutSavings {

    Button addMoreSavingsButton, updateSavingsButton, cancelSavingsButton, doneSavingsButton, saveSavingsButton;
    ContentValues cv18;
    DbHelper helper18;
    DbManager dbManager;
    Double savingsAmountD = 0.0, savingsAnnualAmountD = 0.0;
    EditText savingsAmountET, savingsCategory;
    SavingsListAdapter savingsListAdapter;
    Intent backToSavingsSetUp, backToSetUp, backToSetUpSavings;
    ListView savingsListView;
    long id;
    RadioButton savingsAnnuallyRadioButton, savingsBiAnnuallyRadioButton, savingsBiMonthlyRadioButton, savingsBiWeeklyRadioButton,
            savingsMonthlyRadioButton, savingsWeeklyRadioButton;
    RadioGroup savingsFrequencyRadioGroup;
    SavingsDb savingsDb;
    SQLiteDatabase db18;
    String savingsFrequencyS = null, savingsAnnualAmountS = null, savingsAnnualAmount2 = null, savingsAmountS = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_add_savings_list);

        dbManager = new DbManager(this);

        savingsListView = findViewById(R.id.savingsListView);
        addMoreSavingsButton = findViewById(R.id.addMoreSavingsButton);
        doneSavingsButton = findViewById(R.id.doneSavingsButton);

        addMoreSavingsButton.setOnClickListener(onClickAddMoreSavingsButton);
        doneSavingsButton.setOnClickListener(onClickDoneSavingsButton);

        savingsListAdapter = new SavingsListAdapter(this, dbManager.getSavings());
        savingsListView.setAdapter(savingsListAdapter);
    }

    public void backToSavingsSetUpList() {
        backToSavingsSetUp = new Intent(SetUpAddSavingsList.this, SetUpAddSavingsList.class);
        backToSavingsSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSavingsSetUp);
    }

    public void backToSetUp() {
        backToSetUp = new Intent(SetUpAddSavingsList.this, LayoutSetUp.class);
        backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUp);
    }

    public void backToSetUpSavings() {
        backToSetUpSavings = new Intent(SetUpAddSavingsList.this, AddSavings.class);
        backToSetUpSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToSetUpSavings);
    }

    View.OnClickListener onClickAddMoreSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToSetUpSavings();
        }
    };

    View.OnClickListener onClickDoneSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cv18 = new ContentValues();
            cv18.put(DbHelper.LATESTDONE, "savings");
            helper18 = new DbHelper(getApplicationContext());
            db18 = helper18.getWritableDatabase();
            db18.update(DbHelper.SET_UP_TABLE_NAME, cv18, DbHelper.ID + "= '1'", null);
            db18.close();

            backToSetUp();
        }
    };

    public class SavingsListAdapter extends ArrayAdapter<SavingsDb> {

        public Context context;
        public List<SavingsDb> savings;

        public SavingsListAdapter(
                Context context,
                List<SavingsDb> savings) {

            super(context, -1, savings);

            this.context = context;
            this.savings = savings;
        }

        public void updateSavings(List<SavingsDb> savings) {
            this.savings = savings;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return savings.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderSavingsList savingsListHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.fragment_list_savings,
                        parent, false);

                savingsListHolder = new ViewHolderSavingsList();
                savingsListHolder.savingsName = convertView.findViewById(R.id.savingsListName);
                savingsListHolder.savingsGoalAmount = convertView.findViewById(R.id.savingsListGoalAmount);
                savingsListHolder.savingsDate = convertView.findViewById(R.id.savingsListDate);
                savingsListHolder.savingsDateLabel = convertView.findViewById(R.id.savingsListDateLabel);
                savingsListHolder.savingsCurrentLabel = convertView.findViewById(R.id.savingsListCurrentLabel);
                savingsListHolder.savingsCurrentAmount = convertView.findViewById(R.id.savingsListCurrentAmount);
                savingsListHolder.savingsDeleted = convertView.findViewById(R.id.deleteSavingsButton);
                savingsListHolder.savingsEdit = convertView.findViewById(R.id.editSavingsButton);
                convertView.setTag(savingsListHolder);

            } else {
                savingsListHolder = (ViewHolderSavingsList) convertView.getTag();
            }

            //retrieve savingsName
            savingsListHolder.savingsName.setText(savings.get(position).getSavingsName());

            //retrieve savingsGoal and format as currency
            try {
                savingsGoalS = (String.valueOf(savings.get(position).getSavingsGoal()));
                if (savingsGoalS != null && !savingsGoalS.equals("")) {
                    savingsGoalD = Double.valueOf(savingsGoalS);
                } else {
                    savingsGoalD = 0.0;
                }
                savingsGoal2 = currencyFormat.format(savingsGoalD);
                savingsListHolder.savingsGoalAmount.setText(savingsGoal2);
            } catch (NumberFormatException e6) {
                savingsListHolder.savingsGoalAmount.setText(savingsGoal2);
            }

            //retrieve savingsDate
            savingsDate = savings.get(position).getSavingsDate();
            savingsListHolder.savingsDate.setText(savingsDate);
            if (savingsDate.contains("2")) {
                savingsListHolder.savingsDateLabel.setVisibility(View.VISIBLE);
            } else {
                savingsListHolder.savingsDateLabel.setVisibility(View.GONE);
            }
            if (savingsDate.equals(getString(R.string.goal_achieved))) {
                savingsListHolder.savingsDate.setTextColor(Color.parseColor("#03ac13"));
            } else if(savingsDate.equals(getString(R.string.too_far))) {
                savingsListHolder.savingsDate.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                savingsListHolder.savingsDate.setTextColor(Color.parseColor("#303F9F"));
                savingsListHolder.savingsDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            //retrieve savingsAmount & format as currency
            try {
                savingsCurrentS = (String.valueOf(savings.get(position).getSavingsAmount()));
                if (savingsCurrentS != null && !savingsCurrentS.equals("")) {
                    savingsCurrentD = Double.valueOf(savingsCurrentS);
                } else {
                    savingsCurrentD = 0.0;
                }
                savingsCurrent2 = currencyFormat.format(savingsCurrentD);
                savingsListHolder.savingsCurrentAmount.setText(savingsCurrent2);
            } catch (NumberFormatException e7) {
                savingsListHolder.savingsCurrentAmount.setText(savingsCurrent2);
            }

            incRefKeyS = savings.get(position).getIncRefKeyS();

            savingsListHolder.savingsDeleted.setTag(savings.get(position));
            savingsListHolder.savingsEdit.setTag(savings.get(position));

            //click on pencil icon
            savingsListHolder.savingsEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.add_edit_savings);
                    SetUpAddSavingsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getApplicationContext());

                    savingsNameEntry = findViewById(R.id.savingsNameEntry);
                    savingsAmountEntry = findViewById(R.id.savingsAmountEntry);
                    savingsGoalAmountEntry = findViewById(R.id.savingsGoalAmountEntry);
                    savingsPaymentsEntry = findViewById(R.id.savingsPaymentsEntry);
                    savingsPercentEntry = findViewById(R.id.savingsPercentEntry);
                    savingsDateResult = findViewById(R.id.savingsDateResult);
                    savingsDateResultLabel = findViewById(R.id.savingsDateResultLabel);
                    savingsFrequencyLabel = findViewById(R.id.savingsFrequencyLabel);

                    savingsFrequencyRadioGroup = findViewById(R.id.savingsFrequencyRadioGroup);
                    savingsWeeklyRadioButton = findViewById(R.id.savingsWeeklyRadioButton);
                    savingsBiWeeklyRadioButton = findViewById(R.id.savingsBiWeeklyRadioButton);
                    savingsMonthlyRadioButton = findViewById(R.id.savingsMonthlyRadioButton);
                    savingsAnnuallyRadioButton = findViewById(R.id.savingsAnnuallyRadioButton);

                    saveSavingsButton = findViewById(R.id.saveSavingsButton);
                    saveSavingsButton.setVisibility(View.GONE);
                    updateSavingsButton = findViewById(R.id.updateSavingsButton);
                    cancelSavingsButton = findViewById(R.id.cancelSavingsButton);

                    savingsDb = (SavingsDb) savingsListHolder.savingsEdit.getTag();

                    savingsNameb = savingsDb.getSavingsName();
                    savingsNameEntry.setText(savingsNameb);

                    savingsAmount = savingsDb.getSavingsAmount();
                    savingsAmountS = currencyFormat.format(savingsAmount);
                    savingsAmountEntry.setText(savingsAmountS);
                    savingsAmountEntry.addTextChangedListener(onChangeSavingsAmount);

                    savingsGoal = savingsDb.getSavingsGoal();
                    savingsGoalS2 = currencyFormat.format(savingsGoal);
                    savingsGoalAmountEntry.setText(savingsGoalS2);
                    savingsGoalAmountEntry.addTextChangedListener(onChangeSavingsGoal);

                    savingsPayments = savingsDb.getSavingsPayments();
                    savingsPaymentsS = currencyFormat.format(savingsPayments);
                    savingsPaymentsEntry.setText(savingsPaymentsS);
                    savingsPaymentsEntry.addTextChangedListener(onChangeSavingsPayments);

                    savingsRate = savingsDb.getSavingsRate();
                    savingsPercentD2 = savingsRate / 100;
                    percentFormat.setMinimumFractionDigits(2);
                    savingsPercentS = percentFormat.format(savingsRate);
                    savingsPercentEntry.setText(savingsPercentS);
                    savingsPercentEntry.addTextChangedListener(onChangeSavingsPercent);

                    savingsFrequency = savingsDb.getSavingsFrequency();

                    savingsAnnualIncomeb = savingsDb.getSavingsAnnualIncome();

                    savingsDate = savingsDb.getSavingsDate();
                    savingsDateResult.setText(savingsDate);
                    if (savingsDate2.equals(getString(R.string.goal_achieved))) {
                        savingsDateResultLabel.setVisibility(View.GONE);
                        savingsDateResult.setTextColor(Color.parseColor("#03ac13"));
                    } else if(savingsDate2.equals(getString(R.string.too_far))) {
                        savingsDateResultLabel.setVisibility(View.GONE);
                        savingsDateResult.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        savingsDateResultLabel.setVisibility(View.VISIBLE);
                        savingsDateResult.setTextColor(Color.parseColor("#303F9F"));
                        savingsDateResultLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    //set radio button selections from data
                    if (savingsDb.getSavingsFrequency() == 52) {
                        savingsWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "52";
                    } else if (savingsDb.getSavingsFrequency() == 26) {
                        savingsBiWeeklyRadioButton.setChecked(true);
                        savingsFrequencyS = "26";
                    } else if (savingsDb.getSavingsFrequency() == 12) {
                        savingsMonthlyRadioButton.setChecked(true);
                        savingsFrequencyS = "12";
                    } else if (savingsDb.getSavingsFrequency() == 1) {
                        savingsAnnuallyRadioButton.setChecked(true);
                        savingsFrequencyS = "1";
                    }

                    //update db if radio buttons changed
                    savingsFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.savingsWeeklyRadioButton:
                                    savingsFrequencyS = "52";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsBiWeeklyRadioButton:
                                    savingsFrequencyS = "26";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsMonthlyRadioButton:
                                    savingsFrequencyS = "12";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                                case R.id.savingsAnnuallyRadioButton:
                                    savingsFrequencyS = "1";
                                    savingsDateResult();
                                    savingsDateResult.setText(savingsDate2);
                                    break;
                            }
                        }
                    });

                    updateSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expRefKeyS = String.valueOf(savingsDb.getExpRefKeyS());

                            savingsDateResult();

                                savingsDb.setSavingsName(savingsName);
                                savingsDb.setSavingsAmount(savingsAmount);
                                savingsDb.setSavingsGoal(savingsGoal);
                                savingsDb.setSavingsPayments(savingsPayments);
                                savingsDb.setSavingsFrequency(savingsFrequency);
                                savingsDb.setSavingsRate(savingsRate);
                                savingsDb.setSavingsDate(savingsDate2);

                                dbHelper = new DbHelper(getContext());
                                expenseDb = dbHelper.getWritableDatabase();

                                String[] args = new String[]{expRefKeyS};
                                String[] args2 = new String[]{String.valueOf(savingsDb.getIncRefKeyS())};
                                values = new ContentValues();
                                values2 = new ContentValues();
                                values3 = new ContentValues();
                                values4 = new ContentValues();

                                values.put(DbHelper.EXPENSENAME, savingsName);
                                values.put(DbHelper.EXPENSEAMOUNT, savingsPayments);
                                values.put(DbHelper.EXPENSEFREQUENCY, savingsFrequency);
                                expenseAnnualAmount = savingsPayments * savingsFrequency;
                                values.put(DbHelper.EXPENSEANNUALAMOUNT, expenseAnnualAmount);
                                if (priorityData().equals("A")) {
                                    values.put(DbHelper.EXPENSEAANNUALAMOUNT, expenseAnnualAmount);
                                } else if (priorityData().equals("B")) {
                                    values.put(DbHelper.EXPENSEBANNUALAMOUNT, expenseAnnualAmount);
                                }
                                values2.put(DbHelper.INCOMENAME, savingsName);
                                values3.put(DbHelper.MONEYOUTCAT, savingsName);
                                values4.put(DbHelper.MONEYINCAT, savingsName);

                                try {
                                    expenseDb.update(DbHelper.EXPENSES_TABLE_NAME, values, DbHelper.ID + "=?", args);
                                    expenseDb.update(DbHelper.INCOME_TABLE_NAME, values2, DbHelper.ID + "=?", args2);
                                    expenseDb.update(DbHelper.MONEY_OUT_TABLE_NAME, values3, DbHelper.EXPREFKEYMO + "=?", args);
                                    expenseDb.update(DbHelper.MONEY_IN_TABLE_NAME, values4, DbHelper.INCREFKEYMI + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException| SQLException e) {
                                    e.printStackTrace();
                                }

                                expenseDb.close();

                                dbManager.updateSavings(savingsDb);
                                savingsAdapter.updateSavings(dbManager.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                backToSavingsSetUpList();
                        }
                    });

                    cancelSavingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToSavingsSetUpList();
                        }
                    });
                }
            });

            //click on trash can icon
            savingsListHolder.savingsDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    savingsDb = (SavingsDb) savingsListHolder.savingsDeleted.getTag();

                            dbHelper = new DbHelper(getContext());
                            expenseDb = dbHelper.getWritableDatabase();

                            try {
                                String[] args = new String[]{String.valueOf(savingsDb.getExpRefKeyS())};
                                expenseDb.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            try {
                                String[] args2 = new String[]{String.valueOf(savingsDb.getIncRefKeyS())};
                                expenseDb.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            expenseDb.close();

                            dbManager.deleteSavings(savingsDb);
                            savingsAdapter.updateSavings(dbManager.getSavings());
                            notifyDataSetChanged();

                            backToSavingsSetUpList();
                        }
                    });

            return convertView;
        }
    }

    private static class ViewHolderSavingsList {
        public TextView savingsName;
        public TextView savingsGoalAmount;
        public TextView savingsDate;
        public TextView savingsDateLabel;
        public TextView savingsCurrentLabel;
        public TextView savingsCurrentAmount;
        ImageButton savingsDeleted;
        ImageButton savingsEdit;
    }
}
