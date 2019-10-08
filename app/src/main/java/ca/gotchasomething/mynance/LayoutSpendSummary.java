package ca.gotchasomething.mynance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutSpendSummary extends MainNavigation {

    ArrayAdapter spndSumMthSpinAdapter, spndSumYrSpinAdapter;
    Button spndSumBtn;
    DbManager spndSumDbMgr;
    Double spndSumTotSpent = 0.0, spndSumTotSpentAll = 0.0;
    General spndSumGen;
    int spndSumStartIndex, spndSumEndIndex;
    ListView spndSumListView;
    long spndSumExpId;
    SpndSumLstAdapter spndSumLstAdapter;
    Spinner spndSumMthSpin, spndSumYrSpin;
    String spndSumMth = null, spndSumSpentDate = null, spndSumYr = null, spndSumNewMth = null, spndSumNewMth2 = null;
    String[] spndSumMths, spndSumYrs;
    TextView spndSumHeaderLabel, spndSumTotSpentLabel, spndSumTotSpentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_5_choose_or_create_2spinner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        spndSumGen = new General();
        spndSumDbMgr = new DbManager(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spndSumHeaderLabel = findViewById(R.id.layout5HeaderLabel);
        spndSumHeaderLabel.setText(getString(R.string.choose_month_year));
        spndSumBtn = findViewById(R.id.layout5Btn);
        spndSumTotSpentLabel = findViewById(R.id.layout5LstLabel);
        spndSumTotSpentLabel.setVisibility(View.GONE);
        spndSumTotSpentTV = findViewById(R.id.layout5LstTV);
        spndSumTotSpentTV.setVisibility(View.GONE);

        spndSumBtn.setOnClickListener(onClickSpndSumBtn);

        spndSumListView = findViewById(R.id.layout5ListView);
        spndSumListView.setVisibility(View.GONE);

        spndSumMthSpin = findViewById(R.id.layout5Spin1);
        spndSumMths = new String[]{getString(R.string.jan), getString(R.string.feb), getString(R.string.mar), getString(R.string.apr), getString(R.string.may), getString(R.string.jun), getString(R.string.jul), getString(R.string.aug), getString(R.string.sep), getString(R.string.oct), getString(R.string.nov), getString(R.string.dec), getString(R.string.year_to_date)};
        spndSumMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_1_spinner, R.id.spinnerText, spndSumMths);
        spndSumMthSpin.setAdapter(spndSumMthSpinAdapter);
        spndSumMthSpin.setOnItemSelectedListener(onMonthSelected);

        spndSumYrSpin = findViewById(R.id.layout5Spin2);
        List<String> yearsList = new ArrayList<>();
        for (int firstYear = spndSumDbMgr.getEarliestEntry(); firstYear <= spndSumDbMgr.getLatestEntry(); firstYear++) {
            yearsList.add(String.valueOf(firstYear));
        }
        spndSumYrs = yearsList.toArray(new String[yearsList.size()]);
        spndSumTotSpentLabel.setVisibility(View.VISIBLE);
        spndSumTotSpentTV.setVisibility(View.VISIBLE);
        spndSumListView.setVisibility(View.VISIBLE);

        spndSumYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_1_spinner, R.id.spinnerText, spndSumYrs);
        spndSumYrSpin.setAdapter(spndSumYrSpinAdapter);
        spndSumYrSpin.setOnItemSelectedListener(onYearSelected);
    }

    Spinner.OnItemSelectedListener onMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                spndSumMth = getString(R.string.year_to_date1);
            } else {
                spndSumMth = String.valueOf(spndSumMthSpin.getSelectedItem());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener onYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spndSumYr = String.valueOf(spndSumYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpndSumBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            spndSumTotSpentLabel.setVisibility(View.VISIBLE);
            spndSumTotSpentTV.setVisibility(View.VISIBLE);

            spndSumLstAdapter = new SpndSumLstAdapter(LayoutSpendSummary.this, spndSumDbMgr.getExpense());
            spndSumListView.setAdapter(spndSumLstAdapter);
            spndSumListView.setVisibility(View.VISIBLE);

            //retrieve total spent for report
            List<Double> totalSpentList = new ArrayList<>();
            for (MoneyOutDb m : spndSumDbMgr.getMoneyOuts()) {
                spndSumSpentDate = m.getMoneyOutCreatedOn();
                if (spndSumMth == getString(R.string.year_to_date1)) {
                    if (spndSumSpentDate.contains(spndSumYr)) {
                        totalSpentList.add(m.getMoneyOutAmount());
                    }
                } else {
                    spndSumStartIndex = spndSumSpentDate.indexOf("-") + 1;
                    spndSumEndIndex = spndSumSpentDate.length() - 5;
                    spndSumNewMth = spndSumSpentDate.substring(spndSumStartIndex, spndSumEndIndex);
                    try {
                        spndSumNewMth2 = spndSumNewMth.replace(".", "");
                    } catch (Exception e) {
                        spndSumNewMth2 = spndSumNewMth;
                    }
                    if (spndSumMth.contains(spndSumNewMth2) && spndSumSpentDate.contains(spndSumYr)) {
                        totalSpentList.add(m.getMoneyOutAmount());
                    }
                }
            }
            spndSumTotSpentAll = 0.0;
            if (totalSpentList.size() == 0) {
                spndSumTotSpentAll = 0.0;
            } else {
                for (Double dbl : totalSpentList) {
                    spndSumTotSpentAll += dbl;
                }
            }
            spndSumGen.dblASCurrency(String.valueOf(spndSumTotSpentAll), spndSumTotSpentTV);
        }
    };

    public class SpndSumLstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private SpndSumLstAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateSpendSum(List<ExpenseBudgetDb> expenses) {
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

            final SpndSumViewHolder spndSumHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_6_2tv,
                        parent, false);

                spndSumHldr = new SpndSumViewHolder();
                spndSumHldr.spndSumCatTV = convertView.findViewById(R.id.list6CatTV);
                spndSumHldr.spndSumAmtTV = convertView.findViewById(R.id.list6AmtTV);
                convertView.setTag(spndSumHldr);

            } else {
                spndSumHldr = (SpndSumViewHolder) convertView.getTag();
            }

            spndSumExpId = expenses.get(position).getId();

            //retrieve spending category
            spndSumHldr.spndSumCatTV.setText(expenses.get(position).getExpenseName());

            //retrieve amount spent in each category
            List<Double> spentPerCategory = new ArrayList<>();
            for (MoneyOutDb m : spndSumDbMgr.getMoneyOuts()) {
                spndSumSpentDate = m.getMoneyOutCreatedOn();
                if (spndSumMth == getString(R.string.year_to_date1)) {
                    if (String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(spndSumExpId)) && spndSumSpentDate.contains(spndSumYr)) {
                        spentPerCategory.add(m.getMoneyOutAmount());
                    }
                } else {
                    spndSumStartIndex = spndSumSpentDate.indexOf("-") + 1;
                    spndSumEndIndex = spndSumSpentDate.length() - 5;
                    spndSumNewMth = spndSumSpentDate.substring(spndSumStartIndex, spndSumEndIndex);
                    try {
                        spndSumNewMth2 = spndSumNewMth.replace(".", "");
                    } catch (Exception e) {
                        spndSumNewMth2 = spndSumNewMth;
                    }
                    if (String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(spndSumExpId)) && spndSumMth.contains(spndSumNewMth2) && spndSumSpentDate.contains(spndSumYr)) {
                        spentPerCategory.add(m.getMoneyOutAmount());
                    }
                }
            }

            spndSumTotSpent = 0.0;
            if (spentPerCategory.size() == 0) {
                spndSumTotSpent = 0.0;
            } else {
                for (Double dbl : spentPerCategory) {
                    spndSumTotSpent += dbl;
                }
            }
            spndSumGen.dblASCurrency(String.valueOf(spndSumTotSpent), spndSumHldr.spndSumAmtTV);

            return convertView;
        }
    }

    private static class SpndSumViewHolder {
        public TextView spndSumCatTV;
        public TextView spndSumAmtTV;
    }
}
