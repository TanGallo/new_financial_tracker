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

public class LayoutMoneyOutList extends MainNavigation {

    ArrayAdapter monOutLstFromMthSpinAdapter, monOutLstToMthSpinAdapter, monOutLstFromYrSpinAdapter, monOutLstToYrSpinAdapter;
    boolean monOutLstLeapYear;
    Button monOutLstAddMoreBtn, monOutLstDoneBtn, monOutLstSpinOkBtn, monOutLstSpinResetBtn;
    Calendar monOutLstCal;
    Date monOutLstEarliestDate, monOutLstLatestDate;
    DbManager monOutLstDbMgr;
    Double moneyOutA = 0.0, moneyOutB = 0.0, moneyOutOwing = 0.0, monOutLstAmtEntry = 0.0, monOutLstMoneyOutA = 0.0, monOutLstMoneyOutOwing = 0.0,
            monOutLstMoneyOutB = 0.0, monOutLstMonOutAmt = 0.0, monOutLstMonOutAmtDiff = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0,
            savGoalFromDb = 0.0, savAmtFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    General monOutLstGen;
    int monOutLstYear;
    Intent monOutLstToMain, monOutLstToAddMonOut, monOutLstRefresh;
    LinearLayout monOutLstSpinLayout;
    ListView monOutLstList;
    long monOutLstFromAcctId, monOutLstExpRefKeyMO;
    MonOutLstAdapter monOutLstAdapter;
    Spinner monOutLstToMthSpin, monOutLstFromMthSpin, monOutLstToYrSpin, monOutLstFromYrSpin;
    String monOutLstFromMonth = null, monOutLstFromYr = null, monOutLstLastDay = null, monOutLstToMonth = null, monOutLstToYr = null, monOutLstIsDebtSav = null, monOutLstSumTotalSelTrans = null;
    String[] monOutLstMonths, monOutLstOnlyMonths, monOutLstYears;
    TextView monOutLstAndTV, monOutLstTitle, monOutLstTotalTV;
    TransactionsDb monOutLstMonOutDb;

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

        monOutLstDbMgr = new DbManager(this);
        monOutLstGen = new General();

        monOutLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        monOutLstToMthSpin = findViewById(R.id.layout1ToMthSpin);
        monOutLstFromMthSpin = findViewById(R.id.layout1FromMthSpin);
        monOutLstToYrSpin = findViewById(R.id.layout1ToYrSpin);
        monOutLstFromYrSpin = findViewById(R.id.layout1FromYrSpin);
        monOutLstAndTV = findViewById(R.id.layout1AndTV);
        monOutLstSpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        monOutLstSpinResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monOutLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monOutLstTitle.setText(getString(R.string.cash_debit_transactions));
        monOutLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monOutLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monOutLstDoneBtn.setOnClickListener(onClickMonOutLstDoneBtn);
        monOutLstTotalTV = findViewById(R.id.layout1TotalTV);
        monOutLstTotalTV.setVisibility(View.GONE);
        monOutLstList = findViewById(R.id.layout1ListView);

        monOutLstAdapter = new MonOutLstAdapter(this, monOutLstDbMgr.getCashTrans());
        monOutLstList.setAdapter(monOutLstAdapter);

        if (monOutLstDbMgr.retrieveLastPageId() == 10) {
            monOutLstSpinLayout.setVisibility(View.VISIBLE);
            monOutLstSpinResetBtn.setVisibility(View.GONE);
            monOutLstMonths = new String[]{
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
            monOutLstOnlyMonths = new String[]{
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
            monOutLstYears = monOutLstGen.yearsList(monOutLstDbMgr.getEarliestEntry(monOutLstDbMgr.getYearsList()), monOutLstDbMgr.getLatestEntry(monOutLstDbMgr.getYearsList()));

            monOutLstFromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monOutLstMonths);
            monOutLstFromMthSpin.setAdapter(monOutLstFromMthSpinAdapter);
            monOutLstFromMthSpin.setOnItemSelectedListener(monOutLstOnFromMonthSelected);

            monOutLstFromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monOutLstYears);
            monOutLstFromYrSpin.setAdapter(monOutLstFromYrSpinAdapter);
            monOutLstFromYrSpin.setOnItemSelectedListener(monOutLstOnFromYearSelected);

            monOutLstToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monOutLstOnlyMonths);
            monOutLstToMthSpin.setAdapter(monOutLstToMthSpinAdapter);
            monOutLstToMthSpin.setOnItemSelectedListener(monOutLstOnToMonthSelected);

            monOutLstToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monOutLstYears);
            monOutLstToYrSpin.setAdapter(monOutLstToYrSpinAdapter);
            monOutLstToYrSpin.setOnItemSelectedListener(monOutLstOnToYearSelected);

            monOutLstSpinOkBtn.setOnClickListener(onClickSpinOkBtn);
            monOutLstSpinResetBtn.setOnClickListener(onClickSpinResetBtn);

            monOutLstAddMoreBtn.setVisibility(View.GONE);
            monOutLstDoneBtn.setVisibility(View.GONE);
        } else {
            monOutLstSpinLayout.setVisibility(View.GONE);
            monOutLstAddMoreBtn.setVisibility(View.VISIBLE);
            monOutLstAddMoreBtn.setText(getString(R.string.record_cash_debit));
            monOutLstAddMoreBtn.setOnClickListener(onClickMonOutLstAddMoreBtn);
            monOutLstDoneBtn.setVisibility(View.VISIBLE);
        }
    }

    public void monOutLstRefresh () {
        monOutLstRefresh = new Intent(this, LayoutMoneyOutList.class);
        monOutLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monOutLstRefresh);
    }

    View.OnClickListener onClickMonOutLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToMain = new Intent(LayoutMoneyOutList.this, MainActivity.class);
            monOutLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutLstToMain);
        }
    };

    View.OnClickListener onClickMonOutLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToAddMonOut = new Intent(LayoutMoneyOutList.this, LayoutMoneyOut.class);
            monOutLstToAddMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutLstToAddMonOut);
        }
    };

    Spinner.OnItemSelectedListener monOutLstOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                monOutLstFromMonth = getString(R.string.year_to_date);
                monOutLstFromYrSpin.setVisibility(View.GONE);
                monOutLstToMthSpin.setVisibility(View.GONE);
                monOutLstToYrSpin.setVisibility(View.GONE);
                monOutLstAndTV.setVisibility(View.GONE);
            } else {
                monOutLstFromMonth = String.valueOf(monOutLstFromMthSpin.getSelectedItem());
                monOutLstFromYrSpin.setVisibility(View.VISIBLE);
                monOutLstToMthSpin.setVisibility(View.VISIBLE);
                monOutLstToYrSpin.setVisibility(View.VISIBLE);
                monOutLstAndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monOutLstOnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monOutLstFromYr = String.valueOf(monOutLstFromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monOutLstOnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monOutLstToMonth = String.valueOf(monOutLstToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monOutLstOnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monOutLstToYr = String.valueOf(monOutLstToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (monOutLstFromMonth.equals(getString(R.string.year_to_date))) {
                monOutLstCal = Calendar.getInstance();
                monOutLstYear = monOutLstCal.get(Calendar.YEAR);
                monOutLstEarliestDate = monOutLstGen.dateFromString("1-" + getString(R.string.jan) + "-" + monOutLstYear);
                monOutLstLatestDate = monOutLstGen.dateFromString("31-" + getString(R.string.dec) + "-" + monOutLstYear);
            } else {
                monOutLstEarliestDate = monOutLstGen.dateFromString("1-" + monOutLstFromMonth + "-" + monOutLstFromYr);
                monOutLstLastDay = null;
                if (monOutLstToMonth.equals(getString(R.string.jan)) ||
                        monOutLstToMonth.equals(getString(R.string.mar)) ||
                        monOutLstToMonth.equals(getString(R.string.may)) ||
                        monOutLstToMonth.equals(getString(R.string.jul)) ||
                        monOutLstToMonth.equals(getString(R.string.aug)) ||
                        monOutLstToMonth.equals(getString(R.string.oct)) ||
                        monOutLstToMonth.equals(getString(R.string.dec))) {
                    monOutLstLastDay = "31";
                } else if (monOutLstToMonth.equals(getString(R.string.apr)) ||
                        monOutLstToMonth.equals(getString(R.string.jun)) ||
                        monOutLstToMonth.equals(getString(R.string.sep)) ||
                        monOutLstToMonth.equals(getString(R.string.nov))) {
                    monOutLstLastDay = "30";
                } else if (monOutLstToMonth.equals(getString(R.string.feb))) {
                    monOutLstLeapYear = monOutLstGen.checkForLeapYear(Integer.valueOf(monOutLstToYr));
                    if (monOutLstLeapYear) {
                        monOutLstLastDay = "29";
                    } else {
                        monOutLstLastDay = "28";
                    }
                }
                monOutLstLatestDate = monOutLstGen.dateFromString(monOutLstLastDay + "-" + monOutLstToMonth + "-" + monOutLstToYr);
            }
            try {
                monOutLstAdapter = new MonOutLstAdapter(getApplicationContext(), monOutLstDbMgr.getTransactionsInRange(monOutLstDbMgr.getCashTrans(), monOutLstEarliestDate, monOutLstLatestDate));
                monOutLstList.setAdapter(monOutLstAdapter);
                monOutLstSpinOkBtn.setVisibility(View.GONE);
                monOutLstSpinResetBtn.setVisibility(View.VISIBLE);
                monOutLstSumTotalSelTrans = String.valueOf(monOutLstDbMgr.sumSelectedTransactions(monOutLstDbMgr.getTransactionsInRange(monOutLstDbMgr.getCashTrans(), monOutLstEarliestDate, monOutLstLatestDate)));
                if (monOutLstLatestDate.before(monOutLstEarliestDate)) {
                    monOutLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (monOutLstList.getCount() == 0) {
                    monOutLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                } else {
                    monOutLstTotalTV.setVisibility(View.VISIBLE);
                    monOutLstGen.dblASCurrency(monOutLstSumTotalSelTrans, monOutLstTotalTV);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstRefresh();
        }
    };

    public class MonOutLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> moneyOuts;

        private MonOutLstAdapter(
                Context context,
                List<TransactionsDb> moneyOuts) {

            super(context, -1, moneyOuts);

            this.context = context;
            this.moneyOuts = moneyOuts;
        }

        public void updateMoneyOuts(List<TransactionsDb> moneyOuts) {
            this.moneyOuts = moneyOuts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyOuts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyOut2ViewHolder monOutLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monOutLstHldr = new MoneyOut2ViewHolder();
                monOutLstHldr.monOutLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monOutLstHldr.monOutLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monOutLstHldr.monOutLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monOutLstHldr.monOutLstAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                monOutLstHldr.monOutLstAcctLabelTV.setText(R.string.paid_from);
                monOutLstHldr.monOutLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monOutLstHldr.monOutLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monOutLstHldr.monOutLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                if (monOutLstDbMgr.retrieveLastPageId() == 10) {
                    monOutLstHldr.monOutLstEditBtn.setVisibility(View.VISIBLE);
                    monOutLstHldr.monOutLstDelBtn.setVisibility(View.VISIBLE);
                } else {
                    monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                    monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);
                }
                monOutLstHldr.monOutLstUpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtET = convertView.findViewById(R.id.list5AmtET);
                monOutLstHldr.monOutLstUpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monOutLstHldr.monOutLstCancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monOutLstHldr.monOutLstWarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monOutLstHldr.monOutLstWarnLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstWarnTV = convertView.findViewById(R.id.list5WarnTV);
                monOutLstHldr.monOutLstYesBtn = convertView.findViewById(R.id.list5YesBtn);
                monOutLstHldr.monOutLstNoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monOutLstHldr);

            } else {
                monOutLstHldr = (MoneyOut2ViewHolder) convertView.getTag();
            }

            if (moneyOuts.get(position).getTransIsCC().equals("Y")) {
                monOutLstHldr.monOutLstCatTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDateTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctLabelTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);
            }

            monOutLstHldr.monOutLstCatTV.setText(moneyOuts.get(position).getTransBdgtCat());
            monOutLstMonOutAmt = moneyOuts.get(position).getTransAmt();
            monOutLstGen.dblASCurrency(String.valueOf(monOutLstMonOutAmt), monOutLstHldr.monOutLstAmtTV);
            monOutLstHldr.monOutLstDateTV.setText(moneyOuts.get(position).getTransCreatedOn());
            monOutLstHldr.monOutLstAcctTV.setText(moneyOuts.get(position).getTransFromAcctName());

            monOutLstHldr.monOutLstEditBtn.setTag(moneyOuts.get(position));
            monOutLstHldr.monOutLstDelBtn.setTag(moneyOuts.get(position));

            monOutLstExpRefKeyMO = moneyOuts.get(position).getTransBdgtId();
            monOutLstIsDebtSav = moneyOuts.get(position).getTransFromDebtSav();
            //monOutLstIsDebtSav = monOutLstDbMgr.findMoneyInIsDebtSav(moneyOuts.get(position).getTransFromAcctId());

            //click on pencil icon
            monOutLstHldr.monOutLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (TransactionsDb) monOutLstHldr.monOutLstEditBtn.getTag();
                    LayoutMoneyOutList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monOutLstDbMgr = new DbManager(getContext());

                    monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.VISIBLE);

                    monOutLstHldr.monOutLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutLstRefresh();
                        }
                    });

                    monOutLstHldr.monOutLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monOutLstAmtEntry = monOutLstGen.dblFromET(monOutLstHldr.monOutLstAmtET);
                            monOutLstMonOutAmtDiff = monOutLstAmtEntry - moneyOuts.get(position).getTransAmt();

                            if (!monOutLstIsDebtSav.equals("S") && !monOutLstIsDebtSav.equals("D")) {
                                monOutLstDbMgr.updateTotAcctBalMinus(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentAccountBalance());

                                monOutLstDbMgr.updateAandBBalPlus(moneyOuts.get(position).getTransAmtOutA(), moneyOuts.get(position).getTransAmtOutOwing(), moneyOuts.get(position).getTransAmtOutB(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());

                                monOutLstMoneyOutA = monOutLstDbMgr.detAPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getTransBdgtPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                monOutLstMoneyOutOwing = monOutLstDbMgr.detOwingPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getTransBdgtPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                monOutLstMoneyOutB = monOutLstDbMgr.detBPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getTransBdgtPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());

                                monOutLstDbMgr.updateAandBBalMinus(monOutLstMoneyOutA, monOutLstMoneyOutOwing, monOutLstMoneyOutB, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());

                                if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                                    monOutLstDbMgr.adjustCurrentAandB(monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                    newMoneyA = monOutLstDbMgr.detNewAPortion(monOutLstMoneyOutA, monOutLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyOwing = monOutLstDbMgr.detNewOwingPortion(monOutLstMoneyOutOwing, monOutLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyB = monOutLstDbMgr.detNewBPortion(monOutLstMoneyOutB, monOutLstDbMgr.retrieveCurrentOwingA());
                                    moneyOutA = newMoneyA;
                                    moneyOutOwing = newMoneyOwing;
                                    moneyOutB = newMoneyB;
                                } else {
                                    moneyOutA = monOutLstMoneyOutA;
                                    moneyOutOwing = monOutLstMoneyOutOwing;
                                    moneyOutB = monOutLstMoneyOutB;
                                }
                            } else if(monOutLstIsDebtSav.equals("S")) {
                                monOutLstDbMgr.updateRecMinusPt1(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId), monOutLstFromAcctId);
                                for (AccountsDb a : monOutLstDbMgr.getSavings()) {
                                    if (a.getId() == monOutLstFromAcctId) {
                                        savGoalFromDb = a.getAcctMax();
                                        savAmtFromDb = a.getAcctBal();
                                        savRateFromDb = a.getAcctIntRate();
                                        savPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monOutLstDbMgr.updateRecPt2(monOutLstGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), monOutLstFromAcctId);
                                moneyOutA = 0.0;
                                moneyOutOwing = 0.0;
                                moneyOutB = 0.0;
                            }

                            monOutLstMonOutDb.setTransAmt(monOutLstAmtEntry);
                            monOutLstMonOutDb.setTransAmtOutA(moneyOutA);
                            monOutLstMonOutDb.setTransAmtOutOwing(moneyOutOwing);
                            monOutLstMonOutDb.setTransAmtOutB(moneyOutB);
                            monOutLstDbMgr.updateTransactions(monOutLstMonOutDb);

                            monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                            notifyDataSetChanged();

                            monOutLstDbMgr.makeNewExpAnnAmt(monOutLstExpRefKeyMO, monOutLstGen.lastNumOfDays(365));

                            monOutLstRefresh();
                        }
                    });
                }
            });

            //click on trash can icon
            monOutLstHldr.monOutLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (TransactionsDb) monOutLstHldr.monOutLstDelBtn.getTag();
                    monOutLstExpRefKeyMO = moneyOuts.get(position).getTransBdgtId();

                    if (!monOutLstIsDebtSav.equals("S") && !monOutLstIsDebtSav.equals("D")) {
                        monOutLstDbMgr.updateTotAcctBalPlus(moneyOuts.get(position).getTransAmt(), monOutLstDbMgr.retrieveCurrentAccountBalance());

                        monOutLstDbMgr.updateAandBBalPlus(moneyOuts.get(position).getTransAmtOutA(), moneyOuts.get(position).getTransAmtOutOwing(), moneyOuts.get(position).getTransAmtOutB(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());
                        if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monOutLstDbMgr.adjustCurrentAandB(monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                        }
                    } else if(monOutLstIsDebtSav.equals("S")) {
                        monOutLstDbMgr.updateRecPlusPt1(moneyOuts.get(position).getTransAmt(), monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId), monOutLstFromAcctId);
                        for (AccountsDb a : monOutLstDbMgr.getSavings()) {
                            if (a.getId() == monOutLstFromAcctId) {
                                savGoalFromDb = a.getAcctMax();
                                savAmtFromDb = a.getAcctBal();
                                savRateFromDb = a.getAcctIntRate();
                                savPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monOutLstDbMgr.updateRecPt2(monOutLstGen.calcSavingsDate(
                                savGoalFromDb,
                                savAmtFromDb,
                                savRateFromDb,
                                savPaytFromDb,
                                getString(R.string.goal_achieved),
                                getString(R.string.too_far)), monOutLstFromAcctId);
                    }

                    monOutLstDbMgr.deleteTransactions(monOutLstMonOutDb);
                    monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                    notifyDataSetChanged();

                    monOutLstDbMgr.makeNewExpAnnAmt(monOutLstExpRefKeyMO, monOutLstGen.lastNumOfDays(365));

                    monOutLstRefresh();
                }
            });
            return convertView;
        }
    }

    private static class MoneyOut2ViewHolder {
        public TextView monOutLstCatTV;
        public TextView monOutLstAmtTV;
        public TextView monOutLstDateTV;
        public TextView monOutLstAcctLabelTV;
        public TextView monOutLstAcctTV;
        public ImageButton monOutLstEditBtn;
        public ImageButton monOutLstDelBtn;
        public RelativeLayout monOutLstUpdateLayout;
        public EditText monOutLstAmtET;
        public Button monOutLstUpdateBtn;
        public Button monOutLstCancelBtn;
        public LinearLayout monOutLstWarnLayout;
        public TextView monOutLstWarnTV;
        public Button monOutLstYesBtn;
        public Button monOutLstNoBtn;
    }
}
