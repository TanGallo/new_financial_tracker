package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutReports extends MainNavigation {
    
    ArrayAdapter repFromMthSpinAdapter, repFromYrSpinAdapter, repToMthSpinAdapter, repToYrSpinAdapter;
    boolean repLeapYear;
    Button repSpinOkBtn, repSpinResetBtn;
    Calendar repCal;
    Date repEarliestDate, repLatestDate, repTransDateD, repTransDateD2, repTransDateD3;
    DbManager repDbMgr;
    Double percentSpent = 0.0, totalAmtPerCat = 0.0, totalReceived = 0.0, totalReceivedPerCat = 0.0, totalSpent = 0.0, totalSpentPerCat = 0.0;
    General repGen;
    int repYear;
    Intent repRefresh;
    LinearLayout repSpinLayout;
    ListView repListView;
    long budgetId;
    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RepLstAdapter repAdapter;
    Spinner repToMthSpin, repFromMthSpin, repToYrSpin, repFromYrSpin;
    String repFromMonth = null, repToMonth = null, repFromYr = null, repToYr = null, repLastDay = null, repTransDate = null, repTransDate2 = null, repTransDate3 = null;
    String[] repMthList, repOnlyMthList, repYrList;
    TextView repAmtLabel, repAndTV, repCatLabel, repPercentLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_6_reports);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        repGen = new General();
        repDbMgr = new DbManager(getApplicationContext());

        repSpinLayout = findViewById(R.id.repSpinLayout);
        repToMthSpin = findViewById(R.id.repToMthSpin);
        repFromMthSpin = findViewById(R.id.repFromMthSpin);
        repToYrSpin = findViewById(R.id.repToYrSpin);
        repFromYrSpin = findViewById(R.id.repFromYrSpin);
        repAndTV = findViewById(R.id.repAndTV);
        repSpinOkBtn = findViewById(R.id.repSpinOkBtn);
        repSpinResetBtn = findViewById(R.id.repSpinResetBtn);
        repSpinResetBtn.setVisibility(View.GONE);
        repCatLabel = findViewById(R.id.repCatLabel);
        repCatLabel.setVisibility(View.GONE);
        repAmtLabel = findViewById(R.id.repAmtLabel);
        repAmtLabel.setVisibility(View.GONE);
        repPercentLabel = findViewById(R.id.repPercentLabel);
        repPercentLabel.setVisibility(View.GONE);
        repListView = findViewById(R.id.repListView);
        repListView.setVisibility(View.GONE);

        repMthList = new String[]{
                getString(R.string.jan),
                getString(R.string.feb),
                getString(R.string.mar),
                getString(R.string.apr),
                getString(R.string.may),
                getString(R.string.jun),
                getString(R.string.jul),
                getString(R.string.aug),
                getString(R.string.sep),
                getString(R.string.oct),
                getString(R.string.nov),
                getString(R.string.dec),
                getString(R.string.year_to_date)};
        repOnlyMthList = new String[]{
                getString(R.string.jan),
                getString(R.string.feb),
                getString(R.string.mar),
                getString(R.string.apr),
                getString(R.string.may),
                getString(R.string.jun),
                getString(R.string.jul),
                getString(R.string.aug),
                getString(R.string.sep),
                getString(R.string.oct),
                getString(R.string.nov),
                getString(R.string.dec)};
        repYrList = repGen.yearsList(repDbMgr.getEarliestEntry(repDbMgr.getYearsList()), repDbMgr.getLatestEntry(repDbMgr.getYearsList()));

        repFromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, repMthList);
        repFromMthSpin.setAdapter(repFromMthSpinAdapter);
        repFromMthSpin.setOnItemSelectedListener(repOnFromMonthSelected);

        repFromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, repYrList);
        repFromYrSpin.setAdapter(repFromYrSpinAdapter);
        repFromYrSpin.setOnItemSelectedListener(repOnFromYearSelected);

        repToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, repOnlyMthList);
        repToMthSpin.setAdapter(repToMthSpinAdapter);
        repToMthSpin.setOnItemSelectedListener(repOnToMonthSelected);

        repToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, repYrList);
        repToYrSpin.setAdapter(repToYrSpinAdapter);
        repToYrSpin.setOnItemSelectedListener(repOnToYearSelected);

        repSpinOkBtn.setOnClickListener(onClickSpinOkBtn);
        repSpinResetBtn.setOnClickListener(onClickSpinResetBtn);
    }

    public void repRefresh() {
        repRefresh = new Intent(this, LayoutReports.class);
        repRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(repRefresh);
    }

    Spinner.OnItemSelectedListener repOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                repFromMonth = getString(R.string.year_to_date);
                repFromYrSpin.setVisibility(View.GONE);
                repToMthSpin.setVisibility(View.GONE);
                repToYrSpin.setVisibility(View.GONE);
                repAndTV.setVisibility(View.GONE);
            } else {
                repFromMonth = String.valueOf(repFromMthSpin.getSelectedItem());
                repFromYrSpin.setVisibility(View.VISIBLE);
                repToMthSpin.setVisibility(View.VISIBLE);
                repToYrSpin.setVisibility(View.VISIBLE);
                repAndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener repOnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            repFromYr = String.valueOf(repFromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener repOnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            repToMonth = String.valueOf(repToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener repOnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            repToYr = String.valueOf(repToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repListView.setVisibility(View.VISIBLE);
            if (repFromMonth.equals(getString(R.string.year_to_date))) {
                repCal = Calendar.getInstance();
                repYear = repCal.get(Calendar.YEAR);
                repEarliestDate = repGen.dateFromString("1-" + getString(R.string.jan) + "-" + repYear);
                repLatestDate = repGen.dateFromString("31-" + getString(R.string.dec) + "-" + repYear);
            } else {
                repEarliestDate = repGen.dateFromString("1-" + repFromMonth + "-" + repFromYr);
                repLastDay = null;
                if (repToMonth.equals(getString(R.string.jan)) ||
                        repToMonth.equals(getString(R.string.mar)) ||
                        repToMonth.equals(getString(R.string.may)) ||
                        repToMonth.equals(getString(R.string.jul)) ||
                        repToMonth.equals(getString(R.string.aug)) ||
                        repToMonth.equals(getString(R.string.oct)) ||
                        repToMonth.equals(getString(R.string.dec))) {
                    repLastDay = "31";
                } else if (repToMonth.equals(getString(R.string.apr)) ||
                        repToMonth.equals(getString(R.string.jun)) ||
                        repToMonth.equals(getString(R.string.sep)) ||
                        repToMonth.equals(getString(R.string.nov))) {
                    repLastDay = "30";
                } else if (repToMonth.equals(getString(R.string.feb))) {
                    repLeapYear = repGen.checkForLeapYear(Integer.valueOf(repToYr));
                    if (repLeapYear) {
                        repLastDay = "29";
                    } else {
                        repLastDay = "28";
                    }
                }
                repLatestDate = repGen.dateFromString(repLastDay + "-" + repToMonth + "-" + repToYr);
            }
            if (repLatestDate.before(repEarliestDate)) {
                Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
            } else {
                try {
                    repAdapter = new RepLstAdapter(getApplicationContext(), repDbMgr.getBudget());
                        repListView.setAdapter(repAdapter);
                        repCatLabel.setVisibility(View.VISIBLE);
                        repAmtLabel.setVisibility(View.VISIBLE);
                        repPercentLabel.setVisibility(View.VISIBLE);
                        repSpinOkBtn.setVisibility(View.GONE);
                        repSpinResetBtn.setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repRefresh();
        }
    };

    public class RepLstAdapter extends ArrayAdapter<BudgetDb> {

        private Context context;
        private List<BudgetDb> budget;

        private RepLstAdapter(
                Context context,
                List<BudgetDb> budget) {

            super(context, -1, budget);

            this.context = context;
            this.budget = budget;
        }

        public void updateBudgetReport(List<BudgetDb> budget) {
            this.budget = budget;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return budget.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final RepViewHolder repHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.inc_exp_report,
                        parent, false);

                repHldr = new RepViewHolder();
                repHldr.repCatTV = convertView.findViewById(R.id.repCatTV);
                repHldr.repAmtTV = convertView.findViewById(R.id.repAmtTV);
                repHldr.repPercentTV = convertView.findViewById(R.id.repPercentTV);
                convertView.setTag(repHldr);

            } else {
                repHldr = (RepViewHolder) convertView.getTag();
            }

            //retrieve budget category
            repHldr.repCatTV.setText(budget.get(position).getBdgtCat());

            budgetId = budget.get(position).getId();

            //retrieve total spent or received in each category
            List<Double> totalAmtPerCatList = new ArrayList<>();
            for(TransactionsDb t3 : repDbMgr.getTransactions()) {
                repTransDate = t3.getTransCreatedOn();
                repTransDateD = repGen.dateFromString(repTransDate);
                if(t3.getTransBdgtId() == budgetId && !repTransDateD.before(repEarliestDate) && !repTransDateD.after(repLatestDate)) {
                    totalAmtPerCatList.add(t3.getTransAmt());
                }
            }
            totalAmtPerCat = 0.0;
            if (totalAmtPerCatList.size() == 0) {
                totalAmtPerCat = 0.0;
                //repHldr.repCatTV.setVisibility(View.GONE);
                //repHldr.repAmtTV.setVisibility(View.GONE);
                repHldr.repPercentTV.setVisibility(View.GONE);
            } else {
                for (Double dbl : totalAmtPerCatList) {
                    totalAmtPerCat += dbl;
                }
            }

            repGen.dblASCurrency(String.valueOf(totalAmtPerCat), repHldr.repAmtTV);

            //retrieve total spent during the period
            List<Double> totalSpentList = new ArrayList<>();
            for(TransactionsDb t2 : repDbMgr.getTransactions()) {
                repTransDate2 = t2.getTransCreatedOn();
                repTransDateD2 = repGen.dateFromString(repTransDate2);
                if(t2.getTransType().equals("out") && !repTransDateD2.before(repEarliestDate) && !repTransDateD2.after(repLatestDate)) {
                    totalSpentList.add(t2.getTransAmt());
                }
            }
            totalSpent = 0.0;
            if (totalSpentList.size() == 0) {
                totalSpent = 0.0;
            } else {
                for (Double dbl : totalSpentList) {
                    totalSpent += dbl;
                }
            }

            //retrieve total received during the period
            List<Double> totalReceivedList = new ArrayList<>();
            for(TransactionsDb t : repDbMgr.getTransactions()) {
                repTransDate3 = t.getTransCreatedOn();
                repTransDateD3 = repGen.dateFromString(repTransDate3);
                if(t.getTransType().equals("in") && !repTransDateD3.before(repEarliestDate) && !repTransDateD3.after(repLatestDate)) {
                    totalReceivedList.add(t.getTransAmt());
                }
            }
            totalReceived = 0.0;
            if (totalReceivedList.size() == 0) {
                totalReceived = 0.0;
            } else {
                for (Double dbl : totalReceivedList) {
                    totalReceived += dbl;
                }
            }

            //retrieve percent of total in each category
            percentFormat.setMinimumFractionDigits(2);
            if(budget.get(position).getBdgtExpInc().equals("E")) {
                repHldr.repPercentTV.setText(percentFormat.format(totalAmtPerCat / totalSpent));
            } else if(budget.get(position).getBdgtExpInc().equals("I")) {
                repHldr.repPercentTV.setText(percentFormat.format(totalAmtPerCat / totalReceived));
            }

            return convertView;
        }
    }

    private static class RepViewHolder {
        public TextView repCatTV;
        public TextView repAmtTV;
        public TextView repPercentTV;
    }
}
