/*package ca.gotchasomething.mynance;

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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutCCPayList extends MainNavigation {

    ArrayAdapter ccPayLstFromMthSpinAdapter, ccPayLstToMthSpinAdapter, ccPayLstFromYrSpinAdapter, ccPayLstToYrSpinAdapter;
    boolean ccPayLstLeapYear;
    Button ccPayLstAddMoreBtn, ccPayLstDoneBtn, ccPayLstSpinOkBtn, ccPayLstSpinResetBtn;
    Calendar ccPayLstCal;
    Date ccPayLstEarliestDate, ccPayLstLatestDate;
    DbManager ccPayLstDbMgr;
    General ccPayLstGen;
    int ccPayLstYear;
    Intent ccPayLstToMain, ccPayLstRefresh;
    LinearLayout ccPayLstSpinLayout;
    ListView ccPayLstList;
    CCPayLstAdapter ccPayLstAdapter;
    Spinner ccPayLstToMthSpin, ccPayLstFromMthSpin, ccPayLstToYrSpin, ccPayLstFromYrSpin;
    String ccPayLstFromMonth = null, ccPayLstFromYr = null, ccPayLstToMonth = null, ccPayLstToYr = null, ccPayLstLastDay = null,ccPayLstSumTotalSelTrans = null;
    String[] ccPayLstMonths, ccPayLstOnlyMonths, ccPayLstYears;
    TextView ccPayLstAndTV, ccPayLstTitle, ccPayLstTotalTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        ccPayLstDbMgr = new DbManager(this);
        ccPayLstGen = new General();

        ccPayLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        ccPayLstToMthSpin = findViewById(R.id.layout1ToMthSpin);
        ccPayLstFromMthSpin = findViewById(R.id.layout1FromMthSpin);
        ccPayLstToYrSpin = findViewById(R.id.layout1ToYrSpin);
        ccPayLstFromYrSpin = findViewById(R.id.layout1FromYrSpin);
        ccPayLstAndTV = findViewById(R.id.layout1AndTV);
        ccPayLstSpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        ccPayLstSpinResetBtn = findViewById(R.id.layout1SpinResetBtn);
        ccPayLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        ccPayLstAddMoreBtn.setVisibility(View.GONE);
        ccPayLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        ccPayLstTitle.setText(getString(R.string.credit_card_payts));
        ccPayLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        ccPayLstDoneBtn.setVisibility(View.GONE);
        ccPayLstTotalTV = findViewById(R.id.layout1TotalTV);
        ccPayLstTotalTV.setVisibility(View.GONE);
        ccPayLstList = findViewById(R.id.layout1ListView);
        
        ccPayLstAdapter = new CCPayLstAdapter(this, ccPayLstDbMgr.getCCPayts());
        ccPayLstList.setAdapter(ccPayLstAdapter);

        if (ccPayLstDbMgr.retrieveLastPageId() == 10) {
            ccPayLstSpinLayout.setVisibility(View.VISIBLE);
            ccPayLstSpinResetBtn.setVisibility(View.GONE);
            ccPayLstMonths = new String[]{
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
            ccPayLstOnlyMonths = new String[]{
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
            ccPayLstYears = ccPayLstGen.yearsList(ccPayLstDbMgr.getEarliestEntry(ccPayLstDbMgr.getYearsList()), ccPayLstDbMgr.getLatestEntry(ccPayLstDbMgr.getYearsList()));

            ccPayLstFromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, ccPayLstMonths);
            ccPayLstFromMthSpin.setAdapter(ccPayLstFromMthSpinAdapter);
            ccPayLstFromMthSpin.setOnItemSelectedListener(ccPayLstOnFromMonthSelected);

            ccPayLstFromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, ccPayLstYears);
            ccPayLstFromYrSpin.setAdapter(ccPayLstFromYrSpinAdapter);
            ccPayLstFromYrSpin.setOnItemSelectedListener(ccPayLstOnFromYearSelected);

            ccPayLstToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, ccPayLstOnlyMonths);
            ccPayLstToMthSpin.setAdapter(ccPayLstToMthSpinAdapter);
            ccPayLstToMthSpin.setOnItemSelectedListener(ccPayLstOnToMonthSelected);

            ccPayLstToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, ccPayLstYears);
            ccPayLstToYrSpin.setAdapter(ccPayLstToYrSpinAdapter);
            ccPayLstToYrSpin.setOnItemSelectedListener(ccPayLstOnToYearSelected);

            ccPayLstSpinOkBtn.setOnClickListener(onClickSpinOkBtn);
            ccPayLstSpinResetBtn.setOnClickListener(onClickSpinResetBtn);
        } else {
            ccPayLstSpinLayout.setVisibility(View.GONE);
        }
    }

    public void ccPayLstRefresh() {
        ccPayLstRefresh = new Intent(this, LayoutCCPayList.class);
        ccPayLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(ccPayLstRefresh);
    }
    
    Spinner.OnItemSelectedListener ccPayLstOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
               ccPayLstFromMonth = getString(R.string.year_to_date);
               ccPayLstFromYrSpin.setVisibility(View.GONE);
               ccPayLstToMthSpin.setVisibility(View.GONE);
               ccPayLstToYrSpin.setVisibility(View.GONE);
               ccPayLstAndTV.setVisibility(View.GONE);
            } else {
               ccPayLstFromMonth = String.valueOf(ccPayLstFromMthSpin.getSelectedItem());
               ccPayLstFromYrSpin.setVisibility(View.VISIBLE);
               ccPayLstToMthSpin.setVisibility(View.VISIBLE);
               ccPayLstToYrSpin.setVisibility(View.VISIBLE);
               ccPayLstAndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener ccPayLstOnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           ccPayLstFromYr = String.valueOf(ccPayLstFromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener ccPayLstOnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           ccPayLstToMonth = String.valueOf(ccPayLstToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener ccPayLstOnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           ccPayLstToYr = String.valueOf(ccPayLstToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ccPayLstFromMonth.equals(getString(R.string.year_to_date))) {
               ccPayLstCal = Calendar.getInstance();
               ccPayLstYear =ccPayLstCal.get(Calendar.YEAR);
               ccPayLstEarliestDate =ccPayLstGen.dateFromString("1-" + getString(R.string.jan) + "-" +ccPayLstYear);
               ccPayLstLatestDate =ccPayLstGen.dateFromString("31-" + getString(R.string.dec) + "-" +ccPayLstYear);
            } else {
               ccPayLstEarliestDate =ccPayLstGen.dateFromString("1-" +ccPayLstFromMonth + "-" +ccPayLstFromYr);
               ccPayLstLastDay = null;
                if (ccPayLstToMonth.equals(getString(R.string.jan)) ||
                       ccPayLstToMonth.equals(getString(R.string.mar)) ||
                       ccPayLstToMonth.equals(getString(R.string.may)) ||
                       ccPayLstToMonth.equals(getString(R.string.jul)) ||
                       ccPayLstToMonth.equals(getString(R.string.aug)) ||
                       ccPayLstToMonth.equals(getString(R.string.oct)) ||
                       ccPayLstToMonth.equals(getString(R.string.dec))) {
                   ccPayLstLastDay = "31";
                } else if (ccPayLstToMonth.equals(getString(R.string.apr)) ||
                       ccPayLstToMonth.equals(getString(R.string.jun)) ||
                       ccPayLstToMonth.equals(getString(R.string.sep)) ||
                       ccPayLstToMonth.equals(getString(R.string.nov))) {
                   ccPayLstLastDay = "30";
                } else if (ccPayLstToMonth.equals(getString(R.string.feb))) {
                   ccPayLstLeapYear =ccPayLstGen.checkForLeapYear(Integer.valueOf(ccPayLstToYr));
                    if (ccPayLstLeapYear) {
                       ccPayLstLastDay = "29";
                    } else {
                       ccPayLstLastDay = "28";
                    }
                }
               ccPayLstLatestDate =ccPayLstGen.dateFromString(ccPayLstLastDay + "-" +ccPayLstToMonth + "-" +ccPayLstToYr);
            }
            try {
               ccPayLstAdapter = new CCPayLstAdapter(getApplicationContext(),ccPayLstDbMgr.getTransactionsInRange(ccPayLstDbMgr.getCCPayts(),ccPayLstEarliestDate,ccPayLstLatestDate));
               ccPayLstList.setAdapter(ccPayLstAdapter);
               ccPayLstSpinOkBtn.setVisibility(View.GONE);
               ccPayLstSpinResetBtn.setVisibility(View.VISIBLE);
                ccPayLstSumTotalSelTrans = String.valueOf(ccPayLstDbMgr.sumSelectedTransactions(ccPayLstDbMgr.getTransactionsInRange(ccPayLstDbMgr.getCCPayts(), ccPayLstEarliestDate, ccPayLstLatestDate)));
                if (ccPayLstLatestDate.before(ccPayLstEarliestDate)) {
                   ccPayLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (ccPayLstList.getCount() == 0) {
                    ccPayLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                } else {
                    ccPayLstTotalTV.setVisibility(View.VISIBLE);
                    ccPayLstGen.dblASCurrency(ccPayLstSumTotalSelTrans, ccPayLstTotalTV);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           ccPayLstRefresh();
        }
    };

    public class CCPayLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> ccPayts;

        private CCPayLstAdapter(
                Context context,
                List<TransactionsDb> ccPayts) {

            super(context, -1, ccPayts);

            this.context = context;
            this.ccPayts = ccPayts;
        }

        public void updateCCTrans(List<TransactionsDb> ccPayts) {
            this.ccPayts = ccPayts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccPayts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CCPayListViewHolder ccPayLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_9_cc_payt,
                        parent, false);

                ccPayLstHldr = new CCPayListViewHolder();
                ccPayLstHldr.ccPayLstDateTV = convertView.findViewById(R.id.list9DateTV);
                ccPayLstHldr.ccPayLstAmtTV = convertView.findViewById(R.id.list9AmtTV);
                ccPayLstHldr.ccPayLstCCTV = convertView.findViewById(R.id.list9CCTV);

                convertView.setTag(ccPayLstHldr);

            } else {
                ccPayLstHldr = (CCPayListViewHolder) convertView.getTag();
            }


            ccPayLstHldr.ccPayLstDateTV.setText(ccPayts.get(position).getTransCreatedOn());
            ccPayLstGen.dblASCurrency(String.valueOf(ccPayts.get(position).getTransAmt()), ccPayLstHldr.ccPayLstAmtTV);
            ccPayLstHldr.ccPayLstCCTV.setText(ccPayts.get(position).getTransToAcctName());

            return convertView;
        }
    }

    private static class CCPayListViewHolder {
        public TextView ccPayLstDateTV;
        public TextView ccPayLstAmtTV;
        public TextView ccPayLstCCTV;
    }
}*/
