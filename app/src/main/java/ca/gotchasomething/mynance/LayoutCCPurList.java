package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutCCPurList extends MainNavigation {

    ArrayAdapter monCC2FromMthSpinAdapter, monCC2ToMthSpinAdapter, monCC2FromYrSpinAdapter, monCC2ToYrSpinAdapter;
    boolean monCC2LeapYear;
    Button monCC2AddMoreBtn, monCC2DoneBtn, monCC2SpinOkBtn, monCC2SpinResetBtn;
    Calendar monCC2Cal;
    Date monCC2EarliestDate, monCC2LatestDate;
    DbManager monCC2DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, monCC2AmtEntry = 0.0, monCC2MonOutAmt = 0.0,
            monCC2MonOutAmtDiff = 0.0;
    General monCC2Gen;
    int monCC2Year;
    Intent monCC2ToMain, monCC2ToAddCCTrans, monCC2Refresh;
    LinearLayout monCC2SpinLayout;
    ListView monCC2List;
    long monCC2ExpRefKeyMO, monCC2MonOutchargingDebtId;
    MonCC2Adapter monCC2Adapter;
    TextView monCC2AndTV, monCC2Title, monCC2TotalTV;
    TransactionsDb monCC2MonOutDb;
    Spinner monCC2ToMthSpin, monCC2FromMthSpin, monCC2ToYrSpin, monCC2FromYrSpin;
    String monCC2FromMonth = null, monCC2FromYr = null, monCC2ToMonth = null, monCC2ToYr = null, monCC2LastDay = null, monCC2SumTotalSelTrans = null;
    String[] monCC2Months, monCC2OnlyMonths, monCC2Years;

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

        monCC2DbMgr = new DbManager(this);
        monCC2Gen = new General();

        monCC2SpinLayout = findViewById(R.id.layout1SpinLayout);
        monCC2ToMthSpin = findViewById(R.id.layout1ToMthSpin);
        monCC2FromMthSpin = findViewById(R.id.layout1FromMthSpin);
        monCC2ToYrSpin = findViewById(R.id.layout1ToYrSpin);
        monCC2FromYrSpin = findViewById(R.id.layout1FromYrSpin);
        monCC2AndTV = findViewById(R.id.layout1AndTV);
        monCC2SpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        monCC2SpinResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monCC2Title = findViewById(R.id.layout1HeaderLabelTV);
        monCC2Title.setText(getString(R.string.credit_card_purchases_2));
        monCC2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monCC2DoneBtn = findViewById(R.id.layout1DoneBtn);
        monCC2DoneBtn.setOnClickListener(onClickMonCC2DoneBtn);
        monCC2TotalTV = findViewById(R.id.layout1TotalTV);
        monCC2TotalTV.setVisibility(View.GONE);
        monCC2List = findViewById(R.id.layout1ListView);

        monCC2Adapter = new MonCC2Adapter(this, monCC2DbMgr.getCCTrans());
        monCC2List.setAdapter(monCC2Adapter);
        
        if(monCC2DbMgr.retrieveLastPageId() == 10) {
            monCC2SpinLayout.setVisibility(View.VISIBLE);
            monCC2SpinResetBtn.setVisibility(View.GONE);
            monCC2Months = new String[]{
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
            monCC2OnlyMonths = new String[]{
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
            monCC2Years = monCC2Gen.yearsList(monCC2DbMgr.getEarliestEntry(monCC2DbMgr.getYearsList()), monCC2DbMgr.getLatestEntry(monCC2DbMgr.getYearsList()));

            monCC2FromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monCC2Months);
            monCC2FromMthSpin.setAdapter(monCC2FromMthSpinAdapter);
            monCC2FromMthSpin.setOnItemSelectedListener(monCC2OnFromMonthSelected);

            monCC2FromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monCC2Years);
            monCC2FromYrSpin.setAdapter(monCC2FromYrSpinAdapter);
            monCC2FromYrSpin.setOnItemSelectedListener(monCC2OnFromYearSelected);

            monCC2ToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monCC2OnlyMonths);
            monCC2ToMthSpin.setAdapter(monCC2ToMthSpinAdapter);
            monCC2ToMthSpin.setOnItemSelectedListener(monCC2OnToMonthSelected);

            monCC2ToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monCC2Years);
            monCC2ToYrSpin.setAdapter(monCC2ToYrSpinAdapter);
            monCC2ToYrSpin.setOnItemSelectedListener(monCC2OnToYearSelected);

            monCC2SpinOkBtn.setOnClickListener(onClickSpinOkBtn);
            monCC2SpinResetBtn.setOnClickListener(onClickSpinResetBtn);

            monCC2AddMoreBtn.setVisibility(View.GONE);
            monCC2DoneBtn.setVisibility(View.GONE);
        } else {
            monCC2SpinLayout.setVisibility(View.GONE);
            monCC2AddMoreBtn.setVisibility(View.VISIBLE);
            monCC2AddMoreBtn.setText(getString(R.string.record_credit_card));
            monCC2AddMoreBtn.setOnClickListener(onClickMonCC2AddMoreBtn);
            monCC2DoneBtn.setVisibility(View.VISIBLE);
        }
    }

    public void monCC2Refresh() {
        monCC2Refresh = new Intent(this, LayoutCCPurList.class);
        monCC2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monCC2Refresh);
    }

    View.OnClickListener onClickMonCC2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToMain = new Intent(LayoutCCPurList.this, MainActivity.class);
            monCC2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToMain);
        }
    };

    View.OnClickListener onClickMonCC2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToAddCCTrans = new Intent(LayoutCCPurList.this, LayoutCCPur.class);
            monCC2ToAddCCTrans.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToAddCCTrans);
        }
    };

    Spinner.OnItemSelectedListener monCC2OnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                monCC2FromMonth = getString(R.string.year_to_date);
                monCC2FromYrSpin.setVisibility(View.GONE);
                monCC2ToMthSpin.setVisibility(View.GONE);
                monCC2ToYrSpin.setVisibility(View.GONE);
                monCC2AndTV.setVisibility(View.GONE);
            } else {
                monCC2FromMonth = String.valueOf(monCC2FromMthSpin.getSelectedItem());
                monCC2FromYrSpin.setVisibility(View.VISIBLE);
                monCC2ToMthSpin.setVisibility(View.VISIBLE);
                monCC2ToYrSpin.setVisibility(View.VISIBLE);
                monCC2AndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monCC2OnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monCC2FromYr = String.valueOf(monCC2FromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monCC2OnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monCC2ToMonth = String.valueOf(monCC2ToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monCC2OnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monCC2ToYr = String.valueOf(monCC2ToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (monCC2FromMonth.equals(getString(R.string.year_to_date))) {
                monCC2Cal = Calendar.getInstance();
                monCC2Year = monCC2Cal.get(Calendar.YEAR);
                monCC2EarliestDate = monCC2Gen.dateFromString("1-" + getString(R.string.jan) + "-" + monCC2Year);
                monCC2LatestDate = monCC2Gen.dateFromString("31-" + getString(R.string.dec) + "-" + monCC2Year);
            } else {
                monCC2EarliestDate = monCC2Gen.dateFromString("1-" + monCC2FromMonth + "-" + monCC2FromYr);
                monCC2LastDay = null;
                if (monCC2ToMonth.equals(getString(R.string.jan)) ||
                        monCC2ToMonth.equals(getString(R.string.mar)) ||
                        monCC2ToMonth.equals(getString(R.string.may)) ||
                        monCC2ToMonth.equals(getString(R.string.jul)) ||
                        monCC2ToMonth.equals(getString(R.string.aug)) ||
                        monCC2ToMonth.equals(getString(R.string.oct)) ||
                        monCC2ToMonth.equals(getString(R.string.dec))) {
                    monCC2LastDay = "31";
                } else if (monCC2ToMonth.equals(getString(R.string.apr)) ||
                        monCC2ToMonth.equals(getString(R.string.jun)) ||
                        monCC2ToMonth.equals(getString(R.string.sep)) ||
                        monCC2ToMonth.equals(getString(R.string.nov))) {
                    monCC2LastDay = "30";
                } else if (monCC2ToMonth.equals(getString(R.string.feb))) {
                    monCC2LeapYear = monCC2Gen.checkForLeapYear(Integer.valueOf(monCC2ToYr));
                    if (monCC2LeapYear) {
                        monCC2LastDay = "29";
                    } else {
                        monCC2LastDay = "28";
                    }
                }
                monCC2LatestDate = monCC2Gen.dateFromString(monCC2LastDay + "-" + monCC2ToMonth + "-" + monCC2ToYr);
            }
            try {
                monCC2Adapter = new MonCC2Adapter(getApplicationContext(), monCC2DbMgr.getTransactionsInRange(monCC2DbMgr.getCCTrans(), monCC2EarliestDate, monCC2LatestDate));
                monCC2List.setAdapter(monCC2Adapter);
                monCC2SpinOkBtn.setVisibility(View.GONE);
                monCC2SpinResetBtn.setVisibility(View.VISIBLE);
                monCC2SumTotalSelTrans = String.valueOf(monCC2DbMgr.sumSelectedTransactions(monCC2DbMgr.getTransactionsInRange(monCC2DbMgr.getCCTrans(), monCC2EarliestDate, monCC2LatestDate)));
                if (monCC2LatestDate.before(monCC2EarliestDate)) {
                    monCC2TotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (monCC2List.getCount() == 0) {
                    monCC2TotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                } else {
                    monCC2TotalTV.setVisibility(View.VISIBLE);
                    monCC2Gen.dblASCurrency(monCC2SumTotalSelTrans, monCC2TotalTV);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2Refresh();
        }
    };

    public void monCC2Finish() {
        monCC2DbMgr.updateRecPlusPt1(monCC2MonOutAmtDiff, debtAmtFromDb, monCC2MonOutchargingDebtId);
        for (AccountsDb a : monCC2DbMgr.getDebts()) {
            if (String.valueOf(a.getId()).equals(monCC2MonOutchargingDebtId)) {
                debtAmtFromDb = a.getAcctBal();
                debtLimitFromDb = a.getAcctMax();
                debtRateFromDb = a.getAcctIntRate();
                debtPaytFromDb = a.getAcctPaytsTo();
            }
        }
        monCC2DbMgr.updateRecPt2(monCC2Gen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), monCC2MonOutchargingDebtId);

        monCC2MonOutDb.setTransAmt(monCC2AmtEntry);
        monCC2DbMgr.updateTransactions(monCC2MonOutDb);

        monCC2Adapter.updateCCTrans(monCC2DbMgr.getCCTrans());
        monCC2Adapter.notifyDataSetChanged();

        monCC2DbMgr.makeNewExpAnnAmt(monCC2ExpRefKeyMO, monCC2Gen.lastNumOfDays(365));

        monCC2Refresh();
    }

    public class MonCC2Adapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> ccTrans;

        private MonCC2Adapter(
                Context context,
                List<TransactionsDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<TransactionsDb> ccTrans) {
            this.ccTrans = ccTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyCC2ViewHolder monCC2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monCC2Hldr = new MoneyCC2ViewHolder();
                monCC2Hldr.monCC2CatTV = convertView.findViewById(R.id.list5CatTV);
                monCC2Hldr.monCC2AmtTV = convertView.findViewById(R.id.list5AmtTV);
                monCC2Hldr.monCC2DateTV = convertView.findViewById(R.id.list5DateTV);
                monCC2Hldr.monCC2CCTV = convertView.findViewById(R.id.list5CCTV);
                monCC2Hldr.monCC2EditBtn = convertView.findViewById(R.id.list5EditBtn);
                monCC2Hldr.monCC2DelBtn = convertView.findViewById(R.id.list5DelBtn);
                monCC2Hldr.monCC2UpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monCC2Hldr.monCC2UpdateLayout.setVisibility(View.GONE);
                monCC2Hldr.monCC2AmtET = convertView.findViewById(R.id.list5AmtET);
                monCC2Hldr.monCC2UpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monCC2Hldr.monCC2CancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monCC2Hldr.monCC2WarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monCC2Hldr.monCC2WarnLayout.setVisibility(View.GONE);
                monCC2Hldr.monCC2WarnTV = convertView.findViewById(R.id.list5WarnTV);
                monCC2Hldr.monCC2ContTV = convertView.findViewById(R.id.list5ContTV);
                monCC2Hldr.monCC2YesBtn = convertView.findViewById(R.id.list5YesBtn);
                monCC2Hldr.monCC2NoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monCC2Hldr);

            } else {
                monCC2Hldr = (MoneyCC2ViewHolder) convertView.getTag();
            }

            if(monCC2DbMgr.retrieveLastPageId() == 10) {
                if(ccTrans.get(position).getTransCCPaid().equals("Y")) {
                    monCC2Hldr.monCC2EditBtn.setVisibility(View.GONE);
                    monCC2Hldr.monCC2DelBtn.setVisibility(View.GONE);
                } else {
                    monCC2Hldr.monCC2EditBtn.setVisibility(View.VISIBLE);
                    monCC2Hldr.monCC2DelBtn.setVisibility(View.VISIBLE);
                }
            } else {
                monCC2Hldr.monCC2EditBtn.setVisibility(View.GONE);
                monCC2Hldr.monCC2DelBtn.setVisibility(View.GONE);
            }

            monCC2Hldr.monCC2CatTV.setText(ccTrans.get(position).getTransBdgtCat());
            monCC2MonOutAmt = ccTrans.get(position).getTransAmt();
            monCC2Gen.dblASCurrency(String.valueOf(monCC2MonOutAmt), monCC2Hldr.monCC2AmtTV);
            monCC2Hldr.monCC2DateTV.setText(ccTrans.get(position).getTransCreatedOn());
            monCC2Hldr.monCC2CCTV.setText(ccTrans.get(position).getTransFromAcctName());
            monCC2Hldr.monCC2EditBtn.setTag(ccTrans.get(position));
            monCC2Hldr.monCC2DelBtn.setTag(ccTrans.get(position));

            monCC2ExpRefKeyMO = ccTrans.get(position).getTransBdgtId();

            //click on pencil icon
            monCC2Hldr.monCC2EditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monCC2MonOutDb = (TransactionsDb) monCC2Hldr.monCC2EditBtn.getTag();
                    LayoutCCPurList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monCC2DbMgr = new DbManager(getContext());

                    monCC2Hldr.monCC2UpdateLayout.setVisibility(View.VISIBLE);

                    monCC2Hldr.monCC2CancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monCC2Refresh();
                        }
                    });

                    monCC2Hldr.monCC2UpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monCC2AmtEntry = monCC2Gen.dblFromET(monCC2Hldr.monCC2AmtET);
                            monCC2MonOutAmtDiff = monCC2AmtEntry - ccTrans.get(position).getTransAmt();
                            monCC2MonOutchargingDebtId = ccTrans.get(position).getTransFromAcctId();

                            for (AccountsDb a : monCC2DbMgr.getDebts()) {
                                if (String.valueOf(a.getId()).equals(monCC2MonOutchargingDebtId)) {
                                    debtAmtFromDb = a.getAcctBal();
                                    debtLimitFromDb = a.getAcctMax();
                                    debtRateFromDb = a.getAcctIntRate();
                                    debtPaytFromDb = a.getAcctPaytsTo();
                                }
                            }
                            if (debtAmtFromDb + monCC2MonOutAmtDiff > debtLimitFromDb) {
                                monCC2Hldr.monCC2WarnLayout.setVisibility(View.VISIBLE);
                                monCC2Hldr.monCC2WarnTV.setText(getString(R.string.not_enough_credit_warning));
                                monCC2Hldr.monCC2NoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC2Refresh();
                                    }
                                });

                                monCC2Hldr.monCC2YesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC2Hldr.monCC2WarnLayout.setVisibility(View.GONE);
                                        monCC2Finish();
                                    }
                                });
                            } else {
                                monCC2Finish();
                            }
                        }
                    });
                }
            });

            //click on trash can icon
            monCC2Hldr.monCC2DelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monCC2MonOutDb = (TransactionsDb) monCC2Hldr.monCC2DelBtn.getTag();
                    monCC2ExpRefKeyMO = ccTrans.get(position).getTransBdgtId();
                    monCC2MonOutchargingDebtId = ccTrans.get(position).getTransFromAcctId();

                    for (AccountsDb a : monCC2DbMgr.getDebts()) {
                        if (String.valueOf(a.getId()).equals(monCC2MonOutchargingDebtId)) {
                            debtAmtFromDb = a.getAcctBal();
                            debtLimitFromDb = a.getAcctMax();
                            debtRateFromDb = a.getAcctIntRate();
                            debtPaytFromDb = a.getAcctPaytsTo();
                        }
                    }
                    monCC2DbMgr.updateRecMinusPt1(ccTrans.get(position).getTransAmt(), debtAmtFromDb, monCC2MonOutchargingDebtId);
                    for (AccountsDb a : monCC2DbMgr.getDebts()) {
                        if (String.valueOf(a.getId()).equals(monCC2MonOutchargingDebtId)) {
                            debtAmtFromDb = a.getAcctBal();
                            debtLimitFromDb = a.getAcctMax();
                            debtRateFromDb = a.getAcctIntRate();
                            debtPaytFromDb = a.getAcctPaytsTo();
                        }
                    }
                    monCC2DbMgr.updateRecPt2(monCC2Gen.calcDebtDate(
                            debtAmtFromDb,
                            debtRateFromDb,
                            debtPaytFromDb,
                            getString(R.string.debt_paid),
                            getString(R.string.too_far)), monCC2MonOutchargingDebtId);

                    monCC2DbMgr.deleteTransactions(monCC2MonOutDb);
                    monCC2Adapter.updateCCTrans(monCC2DbMgr.getCCTrans());
                    notifyDataSetChanged();

                    monCC2DbMgr.makeNewExpAnnAmt(monCC2ExpRefKeyMO, monCC2Gen.lastNumOfDays(365));

                    monCC2Refresh();
                }
            });
            return convertView;
        }
    }

    private static class MoneyCC2ViewHolder {
        public TextView monCC2CatTV;
        public TextView monCC2AmtTV;
        public TextView monCC2DateTV;
        public TextView monCC2CCTV;
        public ImageButton monCC2EditBtn;
        public ImageButton monCC2DelBtn;
        public RelativeLayout monCC2UpdateLayout;
        public EditText monCC2AmtET;
        public Button monCC2UpdateBtn;
        public Button monCC2CancelBtn;
        public LinearLayout monCC2WarnLayout;
        public TextView monCC2WarnTV;
        public TextView monCC2ContTV;
        public Button monCC2YesBtn;
        public Button monCC2NoBtn;
    }
}
