package ca.gotchasomething.mynance;

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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutBudgetReport extends MainNavigation {

    ArrayAdapter year2SpinnerAdapter;
    Button budgetReportButton;
    DbManager dbManager;
    Double expenseAnnualAmount, percentSpent, totalBudgetSpent;
    General general;
    LinearLayout budgetReportHeaderLayout, budgetReportLayout;
    ListView budgetReportListView;
    long expenseId;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    BudgetReportAdapter budgetReportAdapter;
    Spinner year2Spinner;
    String spentDate = null, percentSpentP = null, expenseAnnualAmountS = null, year = null;
    String[] years;
    TextView budgetEmptySpinnersText, budgetEmptySpinnersText2, budgetReportCatLabel, budgetReportBudgetedAmountLabel, budgetReportPercentSpentLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_budget_report);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        general = new General();
        dbManager = new DbManager(getApplicationContext());

        budgetReportLayout = findViewById(R.id.budgetReportLayout);
        budgetReportHeaderLayout = findViewById(R.id.budgetReportHeaderLayout);
        budgetReportButton = findViewById(R.id.budgetReportButton);
        budgetEmptySpinnersText = findViewById(R.id.budgetEmptySpinnersText);
        budgetEmptySpinnersText2 = findViewById(R.id.budgetEmptySpinnersText2);
        budgetReportCatLabel = findViewById(R.id.budgetReportCatLabel);
        budgetReportCatLabel.setVisibility(View.GONE);
        budgetReportBudgetedAmountLabel = findViewById(R.id.budgetReportBudgetedAmountLabel);
        budgetReportBudgetedAmountLabel.setVisibility(View.GONE);
        budgetReportPercentSpentLabel = findViewById(R.id.budgetReportPercentSpentLabel);
        budgetReportPercentSpentLabel.setVisibility(View.GONE);

        budgetReportListView = findViewById(R.id.budgetReportListView);
        budgetReportListView.setVisibility(View.GONE);

        budgetReportButton.setOnClickListener(onClickShowBudgetReport);

        year2Spinner = findViewById(R.id.year2Spinner);
        List<String> yearsList2 = new ArrayList<>();
        for (int firstYear = dbManager.getEarliestEntry(); firstYear <= dbManager.getLatestEntry(); firstYear++) {
            yearsList2.add(String.valueOf(firstYear));
        }
        if (yearsList2.size() == 0) {
            budgetEmptySpinnersText.setVisibility(View.VISIBLE);
            budgetEmptySpinnersText2.setVisibility(View.VISIBLE);
            budgetReportHeaderLayout.setVisibility(View.GONE);
            budgetReportLayout.setVisibility(View.GONE);
            year2Spinner.setVisibility(View.GONE);
            budgetReportButton.setVisibility(View.GONE);
        } else {
            years = yearsList2.toArray(new String[yearsList2.size()]);
            budgetEmptySpinnersText.setVisibility(View.GONE);
            budgetEmptySpinnersText2.setVisibility(View.GONE);
            budgetReportHeaderLayout.setVisibility(View.VISIBLE);
            budgetReportLayout.setVisibility(View.VISIBLE);
            year2Spinner.setVisibility(View.VISIBLE);
            budgetReportButton.setVisibility(View.VISIBLE);

            year2SpinnerAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerText, years);
            year2Spinner.setAdapter(year2SpinnerAdapter);
            year2Spinner.setOnItemSelectedListener(onYear2Selected);
        }

        if (dbManager.getMoneyOuts().size() == 0) {
            budgetEmptySpinnersText.setVisibility(View.VISIBLE);
            budgetEmptySpinnersText2.setVisibility(View.VISIBLE);
            year2Spinner.setVisibility(View.GONE);
            budgetReportButton.setVisibility(View.GONE);
        } else {
            budgetEmptySpinnersText.setVisibility(View.GONE);
            budgetEmptySpinnersText2.setVisibility(View.GONE);
            year2Spinner.setVisibility(View.VISIBLE);
            budgetReportButton.setVisibility(View.VISIBLE);
        }
    }

    Spinner.OnItemSelectedListener onYear2Selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            year = String.valueOf(year2Spinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickShowBudgetReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            budgetEmptySpinnersText.setVisibility(View.GONE);
            budgetEmptySpinnersText2.setVisibility(View.GONE);
            budgetReportCatLabel.setVisibility(View.VISIBLE);
            budgetReportBudgetedAmountLabel.setVisibility(View.VISIBLE);
            budgetReportPercentSpentLabel.setVisibility(View.VISIBLE);

            budgetReportAdapter = new BudgetReportAdapter(getApplicationContext(), dbManager.getExpense());
            budgetReportListView.setAdapter(budgetReportAdapter);
            budgetReportListView.setVisibility(View.VISIBLE);
        }
    };

    public class BudgetReportAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private BudgetReportAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateBudgetReport(List<ExpenseBudgetDb> expenses) {
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

            final BudgetReportViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_budget_report,
                        parent, false);

                holder = new BudgetReportViewHolder();
                holder.budgetReportCat = convertView.findViewById(R.id.budgetReportCat);
                holder.budgetReportBudgetedAmount = convertView.findViewById(R.id.budgetReportBudgetedAmount);
                holder.budgetReportPercentSpent = convertView.findViewById(R.id.budgetReportPercentSpent);
                convertView.setTag(holder);

            } else {
                holder = (BudgetReportViewHolder) convertView.getTag();
            }

            expenseId = expenses.get(position).getId();

            //retrieve spending category
            holder.budgetReportCat.setText(expenses.get(position).getExpenseName());

            //retrieve total budgeted per category
            expenseAnnualAmount = expenses.get(position).getExpenseAnnualAmount();
            expenseAnnualAmountS = currencyFormat.format(expenseAnnualAmount);
            holder.budgetReportBudgetedAmount.setText(expenseAnnualAmountS);

            //retrieve amount spent in each category
            List<Double> spentPerCategory = new ArrayList<>();
            for (MoneyOutDb m : dbManager.getMoneyOuts()) {
                spentDate = m.getMoneyOutCreatedOn();
                if (String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(expenseId)) && spentDate.contains(year)) {
                    spentPerCategory.add(m.getMoneyOutAmount());
                }
            }

            totalBudgetSpent = 0.0;
            if (spentPerCategory.size() == 0) {
                totalBudgetSpent = 0.0;
            } else {
                for (Double dbl : spentPerCategory) {
                    totalBudgetSpent += dbl;
                }
            }
            percentSpent = totalBudgetSpent / expenseAnnualAmount;
            if(percentSpent.isInfinite() || percentSpent.isNaN()) {
                percentSpent = 0.0;
            }

            percentFormat.setMinimumFractionDigits(2);
            percentSpentP = percentFormat.format(percentSpent);
            holder.budgetReportPercentSpent.setText(percentSpentP);

            return convertView;
        }
    }

    private static class BudgetReportViewHolder {
        public TextView budgetReportCat;
        public TextView budgetReportBudgetedAmount;
        public TextView budgetReportPercentSpent;
    }
}
