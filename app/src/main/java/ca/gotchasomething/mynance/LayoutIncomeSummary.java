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

import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.MoneyInDb;

public class LayoutIncomeSummary extends MainNavigation {

    ArrayAdapter incSumSpinAdapter;
    Button incSumCreateBtn;
    DbManager incSumDbMgr;
    Double incSumIncAnnAmt = 0.0;
    General incSumGen;
    ListView incSumListView;
    long incSumIncId;
    IncSumLstAdapter incSumLstAdapter;
    Spinner incSumChooseSpin;
    String incSumSpentDate = null, incSumYr = null;
    String[] incSumYears;
    TextView incSumChooseLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_4_choose_or_create_spinner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        incSumDbMgr = new DbManager(this);
        incSumGen = new General();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        incSumChooseLabel = findViewById(R.id.layout4ChooseLabel);
        incSumChooseLabel.setText(getString(R.string.choose_year));
        incSumCreateBtn = findViewById(R.id.layout4CreateBtn);

        incSumCreateBtn.setOnClickListener(onClickIncSumCreateBtn);

        incSumListView = findViewById(R.id.layout4ListView);
        incSumListView.setVisibility(View.GONE);

        incSumChooseSpin = findViewById(R.id.layout4ChooseSpin);
        List<String> yearsList2 = new ArrayList<>();
        for (int firstYear = incSumDbMgr.getEarliestEntry(); firstYear <= incSumDbMgr.getLatestEntry(); firstYear++) {
            yearsList2.add(String.valueOf(firstYear));
        }
        incSumYears = yearsList2.toArray(new String[0]);
        incSumSpinAdapter = new ArrayAdapter(this, R.layout.layout_1_spinner, R.id.spinnerText, incSumYears);
        incSumChooseSpin.setAdapter(incSumSpinAdapter);
        incSumChooseSpin.setOnItemSelectedListener(onYearSelected);

    }

    Spinner.OnItemSelectedListener onYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            incSumYr = String.valueOf(incSumChooseSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickIncSumCreateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incSumLstAdapter = new IncSumLstAdapter(getApplicationContext(), incSumDbMgr.getIncomes());
            incSumListView.setAdapter(incSumLstAdapter);
            incSumListView.setVisibility(View.VISIBLE);
        }
    };

    public class IncSumLstAdapter extends ArrayAdapter<IncomeBudgetDb> {

        private Context context;
        private List<IncomeBudgetDb> incomes;

        private IncSumLstAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncSum(List<IncomeBudgetDb> incomes) {
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

            final incSumViewHolder incSumHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_6_2tv,
                        parent, false);

                incSumHldr = new incSumViewHolder();
                incSumHldr.incSumCatTV = convertView.findViewById(R.id.list6CatTV);
                incSumHldr.incSumAmtTV = convertView.findViewById(R.id.list6AmtTV);
                convertView.setTag(incSumHldr);

            } else {
                incSumHldr = (incSumViewHolder) convertView.getTag();
            }

            incSumIncId = incomes.get(position).getId();

            //retrieve spending category
            incSumHldr.incSumCatTV.setText(incomes.get(position).getIncomeName());

            //retrieve total budgeted per category
            incSumIncAnnAmt = incomes.get(position).getIncomeAnnualAmount();
            incSumGen.dblASCurrency(String.valueOf(incSumIncAnnAmt), incSumHldr.incSumAmtTV);

            //retrieve amount spent in each category
            List<Double> spentPerCategory = new ArrayList<>();
            for (MoneyInDb m2 : incSumDbMgr.getMoneyIns()) {
                incSumSpentDate = m2.getMoneyInCreatedOn();
                if (String.valueOf(m2.getIncRefKeyMI()).equals(String.valueOf(incSumIncId)) && incSumSpentDate.contains(incSumYr)) {
                    spentPerCategory.add(m2.getMoneyInAmount());
                }
            }

            return convertView;
        }
    }

    private static class incSumViewHolder {
        public TextView incSumCatTV;
        public TextView incSumAmtTV;
    }
}
