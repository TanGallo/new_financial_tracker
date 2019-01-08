package ca.gotchasomething.mynance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutSpendingReport extends MainNavigation {

    Button spendingReportButton;
    DbManager dbManager;
    Double totalSpent = 0.0;
    General general;
    ListView spendingListView;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SpendingReportAdapter spendingReportAdapter;
    Spinner monthSpinner, yearSpinner;
    String month = null, year = null, monthSelected = null, totalSpentS = null;
    String[] months, years;
    TextView introText, emptyListText, emptySpinnersText, emptySpinnersText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_spending_report);
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

        spendingReportButton = findViewById(R.id.spendingReportButton);
        introText = findViewById(R.id.introText);
        emptyListText = findViewById(R.id.emptyListText);
        emptyListText.setVisibility(View.GONE);
        emptySpinnersText = findViewById(R.id.emptySpinnersText);
        emptySpinnersText2 = findViewById(R.id.emptySpinnersText2);

        spendingListView = findViewById(R.id.spendingListView);
        spendingListView.setVisibility(View.GONE);

        monthSpinner = findViewById(R.id.monthSpinner);
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_layout, months);
        monthSpinner.setAdapter(monthSpinnerAdapter);
        monthSpinner.setOnItemSelectedListener(onMonthSelected);

        yearSpinner = findViewById(R.id.yearSpinner);
        if(dbManager.getYearsList().size() == 0) {
            emptySpinnersText.setVisibility(View.VISIBLE);
            emptySpinnersText2.setVisibility(View.VISIBLE);
            monthSpinner.setVisibility(View.GONE);
            yearSpinner.setVisibility(View.GONE);
            spendingReportButton.setVisibility(View.GONE);
        } else {
            years = dbManager.getYearsList().toArray(new String[dbManager.getYearsList().size()]);
            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.VISIBLE);
            yearSpinner.setVisibility(View.VISIBLE);
            spendingReportButton.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> yearSpinnerAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_layout, years);
        yearSpinner.setAdapter(yearSpinnerAdapter);
        yearSpinner.setOnItemSelectedListener(onYearSelected);


        spendingReportAdapter = new SpendingReportAdapter(getApplicationContext(), dbManager.getSpendingReport());
        spendingListView.setAdapter(spendingReportAdapter);
        if (spendingReportAdapter.getCount() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
        }

    }

    Spinner.OnItemSelectedListener onMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (monthSelected) {
                case "January":
                    month = "Jan";
                    break;
                case "February":
                    month = "Feb";
                    break;
                case "March":
                    month = "Mar";
                    break;
                case "April":
                    month = "Apr";
                    break;
                case "May":
                    month = "May";
                    break;
                case "June":
                    month = "Jun";
                    break;
                case "July":
                    month = "Jul";
                    break;
                case "August":
                    month = "Aug";
                    break;
                case "September":
                    month = "Sep";
                    break;
                case "October":
                    month = "Oct";
                    break;
                case "November":
                    month = "Nov";
                    break;
                case "December":
                    month = "Dec";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Spinner.OnItemSelectedListener onYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            year = String.valueOf(yearSpinner.getItemAtPosition(0));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public class SpendingReportAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> spendingReport;

        private SpendingReportAdapter(
                Context context,
                List<MoneyOutDb> spendingReport) {

            super(context, -1, spendingReport);

            this.context = context;
            this.spendingReport = spendingReport;
        }

        public void updateSpendingReport(List<MoneyOutDb> spendingReport) {
            this.spendingReport = spendingReport;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return spendingReport.size();
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

            //retrieve spending category
            holder.spendingReportCat.setText(spendingReport.get(position).getMoneyOutCat());

            //retrieve spent amount in each category
            List<Double> totalSpentList = new ArrayList<>();
            for (MoneyOutDb m : dbManager.getSpendingReport()) {
                totalSpentList.add(m.getMoneyOutAmount());
            }
            for (Double dbl : totalSpentList) {
                totalSpent += dbl;
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
}
