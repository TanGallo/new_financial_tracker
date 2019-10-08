/*package ca.gotchasomething.mynance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutSpendingReport extends MainNavigation {

    ArrayAdapter monthSpinnerAdapter, yearSpinnerAdapter;
    Button spendingReportButton;
    DbManager dbManager;
    Double totalSpent = 0.0, totalSpentAll = 0.0;
    General general;
    int startIndex, endIndex;
    LinearLayout debtHeaderLayout, spendingReportLayout;
    ListView spendingListView;
    long expenseId;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SpendingReportAdapter spendingReportAdapter;
    Spinner monthSpinner, yearSpinner;
    String month = null, spentDate = null, totalSpentS = null, totalSpentAllS = null, year = null, newMonth = null, newMonth2 = null;
    String[] months, years;
    TextView emptySpinnersText, emptySpinnersText2, totalSpentLabel, totalSpentText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_layout_spending_report);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        general = new General();
        dbManager = new DbManager(getApplicationContext());

        spendingReportLayout = findViewById(R.id.spendingReportLayout);
        debtHeaderLayout = findViewById(R.id.debtHeaderLayout);
        spendingReportButton = findViewById(R.id.spendingReportButton);
        emptySpinnersText = findViewById(R.id.emptySpinnersText);
        emptySpinnersText2 = findViewById(R.id.emptySpinnersText2);
        totalSpentLabel = findViewById(R.id.totalSpentLabel);
        totalSpentLabel.setVisibility(View.GONE);
        totalSpentText = findViewById(R.id.totalSpentText);
        totalSpentText.setVisibility(View.GONE);

        spendingListView = findViewById(R.id.spendingListView);
        spendingListView.setVisibility(View.GONE);

        spendingReportButton.setOnClickListener(onClickShowReport);

        monthSpinner = findViewById(R.id.monthSpinner);
        months = new String[]{getString(R.string.jan), getString(R.string.feb), getString(R.string.mar), getString(R.string.apr), getString(R.string.may), getString(R.string.jun), getString(R.string.jul), getString(R.string.aug), getString(R.string.sep), getString(R.string.oct), getString(R.string.nov), getString(R.string.dec), getString(R.string.year_to_date)};
        monthSpinnerAdapter = new ArrayAdapter(this, R.layout.layout_1_spinner, R.id.spinnerText, months);
        monthSpinner.setAdapter(monthSpinnerAdapter);
        monthSpinner.setOnItemSelectedListener(onMonthSelected);

        yearSpinner = findViewById(R.id.yearSpinner);
        List<String> yearsList = new ArrayList<>();
        for (int firstYear = dbManager.getEarliestEntry(); firstYear <= dbManager.getLatestEntry(); firstYear++) {
            yearsList.add(String.valueOf(firstYear));
        }
        if (yearsList.size() == 0) {
            emptySpinnersText.setVisibility(View.VISIBLE);
            emptySpinnersText2.setVisibility(View.VISIBLE);
            debtHeaderLayout.setVisibility(View.GONE);
            spendingReportLayout.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.GONE);
            yearSpinner.setVisibility(View.GONE);
            spendingReportButton.setVisibility(View.GONE);
        } else {
            years = yearsList.toArray(new String[yearsList.size()]);
            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            debtHeaderLayout.setVisibility(View.VISIBLE);
            spendingReportLayout.setVisibility(View.VISIBLE);
            monthSpinner.setVisibility(View.VISIBLE);
            yearSpinner.setVisibility(View.VISIBLE);
            spendingReportButton.setVisibility(View.VISIBLE);

            yearSpinnerAdapter = new ArrayAdapter(this, R.layout.layout_1_spinner, R.id.spinnerText, years);
            yearSpinner.setAdapter(yearSpinnerAdapter);
            yearSpinner.setOnItemSelectedListener(onYearSelected);
        }

        if (dbManager.getMoneyOuts().size() == 0) {
            emptySpinnersText.setVisibility(View.VISIBLE);
            emptySpinnersText2.setVisibility(View.VISIBLE);
            monthSpinner.setVisibility(View.GONE);
            yearSpinner.setVisibility(View.GONE);
            spendingReportButton.setVisibility(View.GONE);
            totalSpentLabel.setVisibility(View.GONE);
            totalSpentText.setVisibility(View.GONE);
            spendingReportLayout.setVisibility(View.GONE);
        } else {
            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.VISIBLE);
            yearSpinner.setVisibility(View.VISIBLE);
            spendingReportButton.setVisibility(View.VISIBLE);
            spendingReportLayout.setVisibility(View.VISIBLE);
        }
    }

    Spinner.OnItemSelectedListener onMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                month = getString(R.string.year_to_date1);
            } else {
                month = String.valueOf(monthSpinner.getSelectedItem());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener onYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            year = String.valueOf(yearSpinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickShowReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            totalSpentLabel.setVisibility(View.VISIBLE);
            totalSpentText.setVisibility(View.VISIBLE);

            spendingReportAdapter = new SpendingReportAdapter(getApplicationContext(), dbManager.getExpense());
            spendingListView.setAdapter(spendingReportAdapter);
            spendingListView.setVisibility(View.VISIBLE);

            //retrieve total spent for report
            List<Double> totalSpentList = new ArrayList<>();
            for (MoneyOutDb m : dbManager.getMoneyOuts()) {
                spentDate = m.getMoneyOutCreatedOn();
                if (month == getString(R.string.year_to_date1)) {
                    if (spentDate.contains(year)) {
                        totalSpentList.add(m.getMoneyOutAmount());
                    }
                } else {
                    startIndex = spentDate.indexOf("-") + 1;
                    endIndex = spentDate.length() - 5;
                    newMonth = spentDate.substring(startIndex, endIndex);
                    try {
                        newMonth2 = newMonth.replace(".", "");
                    } catch (Exception e) {
                        newMonth2 = newMonth;
                    }
                    if (month.contains(newMonth2) && spentDate.contains(year)) {
                        totalSpentList.add(m.getMoneyOutAmount());
                    }
                }
            }
            totalSpentAll = 0.0;
            if (totalSpentList.size() == 0) {
                totalSpentAll = 0.0;
            } else {
                for (Double dbl : totalSpentList) {
                    totalSpentAll += dbl;
                }
            }
                totalSpentAllS = currencyFormat.format(totalSpentAll);
                totalSpentText.setText(totalSpentAllS);
        }
    };

    public class SpendingReportAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private SpendingReportAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateSpendingReport(List<ExpenseBudgetDb> expenses) {
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

            final SpendingReportViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_spending_report,
                        parent, false);

                holder = new SpendingReportViewHolder();
                holder.spendingReportCat = convertView.findViewById(R.id.spendingReportCat);
                holder.spendingReportAmount = convertView.findViewById(R.id.spendingReportAmount);
                convertView.setTag(holder);

            } else {
                holder = (SpendingReportViewHolder) convertView.getTag();
            }

            expenseId = expenses.get(position).getId();

            //retrieve spending category
            holder.spendingReportCat.setText(expenses.get(position).getExpenseName());

            //retrieve amount spent in each category
            List<Double> spentPerCategory = new ArrayList<>();
            for (MoneyOutDb m : dbManager.getMoneyOuts()) {
                spentDate = m.getMoneyOutCreatedOn();
                if (month == getString(R.string.year_to_date1)) {
                    if (String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(expenseId)) && spentDate.contains(year)) {
                        spentPerCategory.add(m.getMoneyOutAmount());
                    }
                } else {
                    startIndex = spentDate.indexOf("-") + 1;
                    endIndex = spentDate.length() - 5;
                    newMonth = spentDate.substring(startIndex, endIndex);
                    try {
                        newMonth2 = newMonth.replace(".", "");
                    } catch(Exception e) {
                        newMonth2 = newMonth;
                    }
                    if (String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(expenseId)) && month.contains(newMonth2) && spentDate.contains(year)) {
                        spentPerCategory.add(m.getMoneyOutAmount());
                    }
                }
            }

            totalSpent = 0.0;
            if (spentPerCategory.size() == 0) {
                totalSpent = 0.0;
            } else {
                for (Double dbl : spentPerCategory) {
                    totalSpent += dbl;
                }
            }
            totalSpentS = currencyFormat.format(totalSpent);
            holder.spendingReportAmount.setText(totalSpentS);

            return convertView;
        }
    }

    private static class SpendingReportViewHolder {
        public TextView spendingReportCat;
        public TextView spendingReportAmount;
    }
}*/
