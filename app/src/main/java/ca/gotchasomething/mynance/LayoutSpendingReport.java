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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutSpendingReport extends MainNavigation {

    ArrayAdapter monthSpinnerAdapter, yearSpinnerAdapter;
    Button spendingReportButton;
    Date thisDate, thisMonth, thisYear;
    DbManager dbManager;
    Double totalSpent = 0.0;
    General general;
    ListView spendingListView;
    long moneyOutId;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SpendingReportAdapter spendingReportAdapter;
    Spinner monthSpinner, yearSpinner;
    String moneyOutCat = null, month = null, year = null, monthSelected = null, totalSpentS = null, moneyOutDate = null, thisMonthS = null, thisYearS = null, spentDate = null, startingStringYear = null;
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
        //introText = findViewById(R.id.introText);
        emptyListText = findViewById(R.id.emptyListText);
        emptyListText.setVisibility(View.GONE);
        emptySpinnersText = findViewById(R.id.emptySpinnersText);
        emptySpinnersText2 = findViewById(R.id.emptySpinnersText2);

        spendingListView = findViewById(R.id.spendingListView);
        /*spendingReportAdapter = new SpendingReportAdapter(getApplicationContext(), dbManager.getMoneyOuts());
        spendingListView.setAdapter(spendingReportAdapter);
        if (spendingReportAdapter.getCount() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
            spendingListView.setVisibility(View.GONE);
        } else {
            emptyListText.setVisibility(View.GONE);
            spendingListView.setVisibility(View.VISIBLE);
        }*/
        spendingListView.setVisibility(View.GONE);

        spendingReportButton.setOnClickListener(onClickShowReport);

        monthSpinner = findViewById(R.id.monthSpinner);
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthSpinnerAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerText, months);
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
            monthSpinner.setVisibility(View.GONE);
            yearSpinner.setVisibility(View.GONE);
            spendingReportButton.setVisibility(View.GONE);
        } else {
            years = yearsList.toArray(new String[yearsList.size()]);
            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.VISIBLE);
            yearSpinner.setVisibility(View.VISIBLE);
            spendingReportButton.setVisibility(View.VISIBLE);

            yearSpinnerAdapter = new ArrayAdapter(this, R.layout.spinner_layout, R.id.spinnerText, years);
            yearSpinner.setAdapter(yearSpinnerAdapter);
            yearSpinner.setOnItemSelectedListener(onYearSelected);
        }

        if (dbManager.getMoneyOuts().size() == 0) {
            emptySpinnersText.setVisibility(View.VISIBLE);
            emptySpinnersText2.setVisibility(View.VISIBLE);
            monthSpinner.setVisibility(View.GONE);
            yearSpinner.setVisibility(View.GONE);
            spendingReportButton.setVisibility(View.GONE);
            //introText.setVisibility(View.GONE);
            emptyListText.setVisibility(View.GONE);
        } else {
            emptySpinnersText.setVisibility(View.GONE);
            emptySpinnersText2.setVisibility(View.GONE);
            monthSpinner.setVisibility(View.VISIBLE);
            yearSpinner.setVisibility(View.VISIBLE);
            spendingReportButton.setVisibility(View.VISIBLE);
            //introText.setVisibility(View.VISIBLE);
            emptyListText.setVisibility(View.GONE);
        }
    }

    Spinner.OnItemSelectedListener onMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monthSelected = String.valueOf(monthSpinner.getSelectedItem());
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

            spendingReportAdapter = new SpendingReportAdapter(getApplicationContext(), getSpendingReport());
            spendingListView.setAdapter(spendingReportAdapter);
            if (spendingReportAdapter.getCount() == 0) {
                emptyListText.setVisibility(View.VISIBLE);
                spendingListView.setVisibility(View.GONE);
            } else {
                emptyListText.setVisibility(View.GONE);
                spendingListView.setVisibility(View.VISIBLE);
            }
        }
    };

    public List<MoneyOutDb> getSpendingReport() {
        List<MoneyOutDb> spendingReport = new ArrayList<>(dbManager.getMoneyOuts().size());
        for(MoneyOutDb m: dbManager.getMoneyOuts()) {
            spentDate = m.getMoneyOutCreatedOn();
            if(spentDate.contains(month) && spentDate.contains(year)) {
                spendingReport.add(m);
            }
        }

        return spendingReport;
    }

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
            moneyOutCat = spendingReport.get(position).getMoneyOutCat();
            holder.spendingReportCat.setText(moneyOutCat);

            //retrieve spent amount in each category
            List<Double> totalSpentList = new ArrayList<>();
            for (MoneyOutDb m2 : getSpendingReport()) {
                if (m2.getMoneyOutCat().equals(moneyOutCat)) {
                    totalSpentList.add(m2.getMoneyOutAmount());
                }
            }

            totalSpent = 0.0;
            if(totalSpentList.size() == 0) {
                totalSpent = 0.0;
            } else {
                for(Double dbl : totalSpentList) {
                    totalSpent += dbl;
                }
            }

            //totalSpent = spendingReport.get(position).getMoneyOutAmount();
            totalSpentS = currencyFormat.format(totalSpent);
            holder.spendingReportAmount.setText(totalSpentS);

            /*moneyOutDate = moneyOuts.get(position).getMoneyOutCreatedOn();
            try {
                thisDate = new SimpleDateFormat("dd-MMM-yyyy").parse(moneyOutDate);
            } catch(Exception e) {
                e.printStackTrace();
            }
            String startingStringYear = thisDate.toString();
            int startIndexY = startingStringYear.length() - 4;
            int endIndexY = startingStringYear.length();
            String subStringYear = startingStringYear.substring(startIndexY, endIndexY);
            int startIndexM = startingStringYear.length() - 8;
            int endIndexM = startingStringYear.length() - 6;
            String subStringMonth = startingStringYear.substring(startIndexM, endIndexM);

            List<MoneyOutDb> byMonthYear = new ArrayList<>(moneyOuts.size());
            for(MoneyOutDb m: moneyOuts) {
                if(m.getMoneyOutCreatedOn().contains(subStringMonth) && m.getMoneyOutCreatedOn().contains(subStringYear)) {
                    byMonthYear.add(m);
                    List<String> categories = new ArrayList<>(byMonthYear.size());
                    for(MoneyOutDb m2: byMonthYear) {
                        String category = m2.getMoneyOutCat();
                        Double totalSpent = m2.getMoneyOutAmount();
                        categories.add(category);
                        for(String s : categories) {
                            List<Double> totalSpentList = new ArrayList<>(categories.size());
                            for(Double d : totalSpentList) {
                                totalSpentList.add(totalSpent += d);
                            }
                        }
                    }
                }
            }*/

            //retrieve id of records
            //moneyOutId = moneyOuts.get(position).getId();


            /*List<Double> totalSpentList = new ArrayList<>();
            for (MoneyOutDb m2 : dbManager.getMoneyOuts()) {
                if (String.valueOf(m2.getExpRefKeyMO()).equals(moneyOutId)) {
                    totalSpentList.add(m2.getMoneyOutAmount());
                }
            }

            totalSpent = 0.0;
            if(totalSpentList.size() == 0) {
                totalSpent = 0.0;
            } else {
                for(Double dbl : totalSpentList) {
                    totalSpent += dbl;
                }
            }

            totalSpentS = currencyFormat.format(totalSpent);
            holder.spendingReportAmount.setText(totalSpentS);*/

            return convertView;
        }
    }

    private static class SpendingReportViewHolder {
        public TextView spendingReportCat;
        public TextView spendingReportAmount;
    }
}
