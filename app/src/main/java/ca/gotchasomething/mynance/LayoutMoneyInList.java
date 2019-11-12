package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutMoneyInList extends MainNavigation {

    ArrayAdapter monInLstToMthSpinAdapter, monInLstFromMthSpinAdapter, monInLstToYrSpinAdapter, monInLstFromYrSpinAdapter;
    boolean monInLstLeapYear;
    Button monInLstAddMoreBtn, monInLstDoneBtn, monInLstSpinOkBtn, monInLstSpinResetBtn;
    Calendar monInLstCal;
    ContentValues monInLstCV;
    Date monInLstEarliestDate, monInLstLatestDate, newDate;
    DbHelper monInLstHelper;
    DbManager monInLstDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyInA = 0.0, moneyInB = 0.0, moneyInOwing = 0.0, monInLstAmtEntry = 0.0,
            monInLstMoneyInA = 0.0, monInLstMoneyInOwing = 0.0, monInLstMoneyInB = 0.0, monInLstMonInAmt = 0.0, monInLstMonInAmtDiff = 0.0, monInLstPercentA = 0.0,
            newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    General monInLstGen;
    int monInLstYear;
    Intent monInLstToMain, monInLstToAddMonIn, monInLstRefresh;
    LinearLayout monInLstSpinLayout;
    List<String> monInLstYrsList;
    ListView monInLstList;
    long monInLstIncRefKeyMI, monInToAcctId;
    MonInLstAdapter monInLstAdapter;
    NavigationView monInLstNavView;
    Spinner monInLstToMthSpin, monInLstToYrSpin, monInLstFromMthSpin, monInLstFromYrSpin;
    SQLiteDatabase monInLstDb;
    String monInLstLastDay = null, monInLstSumTotalSelTrans = null, monInIsDebtSav = null, monInLstToMonth = null, monInLstFromMonth = null, monInLstToYr = null, monInLstFromYr = null;
    String[] monInLstMonths, monInLstOnlyMonths, monInLstYears;
    TextView monInLstAndTV, monInLstTitle, monInLstTotalTV;
    TransactionsDb monInLstMonInDb;

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

        monInLstDbMgr = new DbManager(this);
        monInLstGen = new General();

        monInLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        monInLstToMthSpin = findViewById(R.id.layout1ToMthSpin);
        monInLstFromMthSpin = findViewById(R.id.layout1FromMthSpin);
        monInLstToYrSpin = findViewById(R.id.layout1ToYrSpin);
        monInLstFromYrSpin = findViewById(R.id.layout1FromYrSpin);
        monInLstAndTV = findViewById(R.id.layout1AndTV);
        monInLstSpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        monInLstSpinResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monInLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monInLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monInLstTitle.setText(getString(R.string.deposits));
        monInLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monInLstDoneBtn.setOnClickListener(onClickMonInLstDoneBtn);
        monInLstTotalTV = findViewById(R.id.layout1TotalTV);
        monInLstTotalTV.setVisibility(View.GONE);
        monInLstList = findViewById(R.id.layout1ListView);

        monInLstAdapter = new MonInLstAdapter(this, monInLstDbMgr.getMoneyIns());
        monInLstList.setAdapter(monInLstAdapter);

        if (monInLstDbMgr.retrieveLastPageId() == 10) {
            monInLstSpinLayout.setVisibility(View.VISIBLE);
            monInLstSpinResetBtn.setVisibility(View.GONE);
            monInLstMonths = new String[]{
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
            monInLstOnlyMonths = new String[]{
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
            monInLstYears = monInLstGen.yearsList(monInLstDbMgr.getEarliestEntry(monInLstDbMgr.getYearsList()), monInLstDbMgr.getLatestEntry(monInLstDbMgr.getYearsList()));

            monInLstFromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monInLstMonths);
            monInLstFromMthSpin.setAdapter(monInLstFromMthSpinAdapter);
            monInLstFromMthSpin.setOnItemSelectedListener(monInLstOnFromMonthSelected);

            monInLstFromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monInLstYears);
            monInLstFromYrSpin.setAdapter(monInLstFromYrSpinAdapter);
            monInLstFromYrSpin.setOnItemSelectedListener(monInLstOnFromYearSelected);

            monInLstToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monInLstOnlyMonths);
            monInLstToMthSpin.setAdapter(monInLstToMthSpinAdapter);
            monInLstToMthSpin.setOnItemSelectedListener(monInLstOnToMonthSelected);

            monInLstToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, monInLstYears);
            monInLstToYrSpin.setAdapter(monInLstToYrSpinAdapter);
            monInLstToYrSpin.setOnItemSelectedListener(monInLstOnToYearSelected);

            monInLstSpinOkBtn.setOnClickListener(onClickSpinOkBtn);
            monInLstSpinResetBtn.setOnClickListener(onClickSpinResetBtn);

            monInLstAddMoreBtn.setVisibility(View.GONE);
            monInLstDoneBtn.setVisibility(View.GONE);
        } else {
            monInLstSpinLayout.setVisibility(View.GONE);
            monInLstAddMoreBtn.setVisibility(View.VISIBLE);
            monInLstAddMoreBtn.setText(getString(R.string.record_deposits));
            monInLstAddMoreBtn.setOnClickListener(onClickMonInLstAddMoreBtn);
            monInLstDoneBtn.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener onClickMonInLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstToMain = new Intent(LayoutMoneyInList.this, MainActivity.class);
            monInLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToMain);
        }
    };

    View.OnClickListener onClickMonInLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstToAddMonIn = new Intent(LayoutMoneyInList.this, LayoutMoneyIn.class);
            monInLstToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToAddMonIn);
        }
    };

    public void monInLstRefresh() {
        monInLstRefresh = new Intent(this, LayoutMoneyInList.class);
        monInLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monInLstRefresh);
    }

    Spinner.OnItemSelectedListener monInLstOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                monInLstFromMonth = getString(R.string.year_to_date);
                monInLstFromYrSpin.setVisibility(View.GONE);
                monInLstToMthSpin.setVisibility(View.GONE);
                monInLstToYrSpin.setVisibility(View.GONE);
                monInLstAndTV.setVisibility(View.GONE);
            } else {
                monInLstFromMonth = String.valueOf(monInLstFromMthSpin.getSelectedItem());
                monInLstFromYrSpin.setVisibility(View.VISIBLE);
                monInLstToMthSpin.setVisibility(View.VISIBLE);
                monInLstToYrSpin.setVisibility(View.VISIBLE);
                monInLstAndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monInLstOnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monInLstFromYr = String.valueOf(monInLstFromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monInLstOnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monInLstToMonth = String.valueOf(monInLstToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener monInLstOnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monInLstToYr = String.valueOf(monInLstToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (monInLstFromMonth.equals(getString(R.string.year_to_date))) {
                monInLstCal = Calendar.getInstance();
                monInLstYear = monInLstCal.get(Calendar.YEAR);
                monInLstEarliestDate = monInLstGen.dateFromString("1-" + getString(R.string.jan) + "-" + monInLstYear);
                monInLstLatestDate = monInLstGen.dateFromString("31-" + getString(R.string.dec) + "-" + monInLstYear);
            } else {
                monInLstEarliestDate = monInLstGen.dateFromString("1-" + monInLstFromMonth + "-" + monInLstFromYr);
                monInLstLastDay = null;
                if (monInLstToMonth.equals(getString(R.string.jan)) ||
                        monInLstToMonth.equals(getString(R.string.mar)) ||
                        monInLstToMonth.equals(getString(R.string.may)) ||
                        monInLstToMonth.equals(getString(R.string.jul)) ||
                        monInLstToMonth.equals(getString(R.string.aug)) ||
                        monInLstToMonth.equals(getString(R.string.oct)) ||
                        monInLstToMonth.equals(getString(R.string.dec))) {
                    monInLstLastDay = "31";
                } else if (monInLstToMonth.equals(getString(R.string.apr)) ||
                        monInLstToMonth.equals(getString(R.string.jun)) ||
                        monInLstToMonth.equals(getString(R.string.sep)) ||
                        monInLstToMonth.equals(getString(R.string.nov))) {
                    monInLstLastDay = "30";
                } else if (monInLstToMonth.equals(getString(R.string.feb))) {
                    monInLstLeapYear = monInLstGen.checkForLeapYear(Integer.valueOf(monInLstToYr));
                    if (monInLstLeapYear) {
                        monInLstLastDay = "29";
                    } else {
                        monInLstLastDay = "28";
                    }
                }
                monInLstLatestDate = monInLstGen.dateFromString(monInLstLastDay + "-" + monInLstToMonth + "-" + monInLstToYr);
            }
            try {
                monInLstAdapter = new MonInLstAdapter(getApplicationContext(), monInLstDbMgr.getTransactionsInRange(monInLstDbMgr.getMoneyIns(), monInLstEarliestDate, monInLstLatestDate));
                monInLstList.setAdapter(monInLstAdapter);
                monInLstSpinOkBtn.setVisibility(View.GONE);
                monInLstSpinResetBtn.setVisibility(View.VISIBLE);
                monInLstSumTotalSelTrans = String.valueOf(monInLstDbMgr.sumSelectedTransactions(monInLstDbMgr.getTransactionsInRange(monInLstDbMgr.getMoneyIns(), monInLstEarliestDate, monInLstLatestDate)));
                if (monInLstLatestDate.before(monInLstEarliestDate)) {
                    monInLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (monInLstList.getCount() == 0) {
                    monInLstTotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                } else {
                    monInLstTotalTV.setVisibility(View.VISIBLE);
                    monInLstGen.dblASCurrency(monInLstSumTotalSelTrans, monInLstTotalTV);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstRefresh();
        }
    };

    public class MonInLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> moneyIns;

        private MonInLstAdapter(
                Context context,
                List<TransactionsDb> moneyIns) {

            super(context, -1, moneyIns);

            this.context = context;
            this.moneyIns = moneyIns;
        }

        public void updateMoneyIns(List<TransactionsDb> moneyIns) {
            this.moneyIns = moneyIns;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIns.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyIn2ViewHolder monInLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monInLstHldr = new MoneyIn2ViewHolder();
                monInLstHldr.monInLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monInLstHldr.monInLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monInLstHldr.monInLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monInLstHldr.monInLstAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                monInLstHldr.monInLstAcctLabelTV.setText(R.string.deposited_to);
                monInLstHldr.monInLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monInLstHldr.monInLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monInLstHldr.monInLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                if (monInLstDbMgr.retrieveLastPageId() == 10) {
                    monInLstHldr.monInLstEditBtn.setVisibility(View.VISIBLE);
                    monInLstHldr.monInLstDelBtn.setVisibility(View.VISIBLE);
                } else {
                    monInLstHldr.monInLstEditBtn.setVisibility(View.GONE);
                    monInLstHldr.monInLstDelBtn.setVisibility(View.GONE);
                }
                monInLstHldr.monInLstUpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monInLstHldr.monInLstUpdateLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstAmtET = convertView.findViewById(R.id.list5AmtET);
                monInLstHldr.monInLstUpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monInLstHldr.monInLstCancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monInLstHldr.monInLstWarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monInLstHldr.monInLstWarnLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstWarnTV = convertView.findViewById(R.id.list5WarnTV);
                monInLstHldr.monInLstYesBtn = convertView.findViewById(R.id.list5YesBtn);
                monInLstHldr.monInLstNoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monInLstHldr);

            } else {
                monInLstHldr = (MoneyIn2ViewHolder) convertView.getTag();
            }

            monInLstHldr.monInLstCatTV.setText(moneyIns.get(position).getTransBdgtCat());
            monInLstMonInAmt = moneyIns.get(position).getTransAmt();
            monInLstGen.dblASCurrency(String.valueOf(monInLstMonInAmt), monInLstHldr.monInLstAmtTV);
            monInLstHldr.monInLstDateTV.setText(moneyIns.get(position).getTransCreatedOn());
            monInLstHldr.monInLstAcctTV.setText(moneyIns.get(position).getTransToAcctName());

            monInLstHldr.monInLstEditBtn.setTag(moneyIns.get(position));
            monInLstHldr.monInLstDelBtn.setTag(moneyIns.get(position));

            monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();
            monInIsDebtSav = monInLstDbMgr.findMoneyInIsDebtSav(moneyIns.get(position).getTransToAcctId());

            //click on pencil icon
            monInLstHldr.monInLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLstHldr.monInLstEditBtn.getTag();
                    LayoutMoneyInList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monInLstDbMgr = new DbManager(getContext());

                    monInLstHldr.monInLstUpdateLayout.setVisibility(View.VISIBLE);

                    monInLstHldr.monInLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInLstRefresh();
                        }
                    });

                    monInLstHldr.monInLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monInLstAmtEntry = monInLstGen.dblFromET(monInLstHldr.monInLstAmtET);
                            monInLstMonInAmtDiff = monInLstAmtEntry - moneyIns.get(position).getTransAmt();

                            if (!monInIsDebtSav.equals("D") && !monInIsDebtSav.equals("S")) {
                                monInLstDbMgr.updateTotAcctBalPlus(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAccountBalance());
                                monInLstDbMgr.updateAandBBalMinus(
                                        moneyIns.get(position).getTransAmtInA(),
                                        moneyIns.get(position).getTransAmtInOwing(),
                                        moneyIns.get(position).getTransAmtInB(),
                                        monInLstDbMgr.retrieveCurrentA(),
                                        monInLstDbMgr.retrieveCurrentOwingA(),
                                        monInLstDbMgr.retrieveCurrentB());

                                monInLstPercentA = monInLstDbMgr.sumTotalAExpenses() / monInLstDbMgr.sumTotalIncome();
                                monInLstMoneyInA = monInLstDbMgr.detAPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInOwing = monInLstDbMgr.detOwingPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInB = monInLstDbMgr.detBPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());

                                monInLstDbMgr.updateAandBBalPlus(monInLstMoneyInA, monInLstMoneyInOwing, monInLstMoneyInB, monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentB());

                                if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                                    monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                                    newMoneyA = monInLstDbMgr.detNewAPortion(monInLstMoneyInA, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyOwing = monInLstDbMgr.detNewOwingPortion(monInLstMoneyInOwing, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyB = monInLstDbMgr.detNewBPortion(monInLstMoneyInB, monInLstDbMgr.retrieveCurrentOwingA());
                                    moneyInA = newMoneyA;
                                    moneyInOwing = newMoneyOwing;
                                    moneyInB = newMoneyB;
                                } else {
                                    moneyInA = monInLstMoneyInA;
                                    moneyInOwing = monInLstMoneyInOwing;
                                    moneyInB = monInLstMoneyInB;
                                }
                            } else if (monInIsDebtSav.equals("D")) {
                                monInLstDbMgr.updateRecMinusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getDebts()) {
                                    if (a.getId() == monInToAcctId) {
                                        debtAmtFromDb = a.getAcctBal();
                                        debtLimitFromDb = a.getAcctMax();
                                        debtRateFromDb = a.getAcctIntRate();
                                        debtPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                        debtAmtFromDb,
                                        debtRateFromDb,
                                        debtPaytFromDb,
                                        getString(R.string.debt_paid),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            } else if (monInIsDebtSav.equals("S")) {
                                monInLstDbMgr.updateRecPlusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getSavings()) {
                                    if (a.getId() == monInToAcctId) {
                                        savGoalFromDb = a.getAcctMax();
                                        savAmtFromDb = a.getAcctBal();
                                        savRateFromDb = a.getAcctIntRate();
                                        savPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            }

                            monInLstMonInDb.setTransAmt(monInLstAmtEntry);
                            monInLstMonInDb.setTransAmtInA(moneyInA);
                            monInLstMonInDb.setTransAmtInOwing(moneyInOwing);
                            monInLstMonInDb.setTransAmtInB(moneyInB);
                            monInLstDbMgr.updateTransactions(monInLstMonInDb);

                            monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                            notifyDataSetChanged();

                            monInLstHelper = new DbHelper(getContext());
                            monInLstDb = monInLstHelper.getWritableDatabase();
                            monInLstCV = new ContentValues();
                            monInLstCV.put(DbHelper.BDGTANNPAYT, monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365)));
                            monInLstDb.update(DbHelper.BUDGET_TABLE_NAME, monInLstCV, DbHelper.ID + "=" + monInLstIncRefKeyMI, null);
                            monInLstDb.close();

                            monInLstRefresh();
                        }
                    });
                }
            });

            //click on trash can icon
            monInLstHldr.monInLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLstHldr.monInLstDelBtn.getTag();
                    monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();

                    if (!monInIsDebtSav.equals("D") && !monInIsDebtSav.equals("S")) {
                        monInLstDbMgr.updateTotAcctBalMinus(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAccountBalance());

                        monInLstDbMgr.updateAandBBalMinus(
                                moneyIns.get(position).getTransAmtInA(),
                                moneyIns.get(position).getTransAmtInOwing(),
                                moneyIns.get(position).getTransAmtInB(),
                                monInLstDbMgr.retrieveCurrentA(),
                                monInLstDbMgr.retrieveCurrentOwingA(),
                                monInLstDbMgr.retrieveCurrentB());
                        if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                        }
                    } else if (monInIsDebtSav.equals("D")) {
                        monInLstDbMgr.updateRecPlusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getDebts()) {
                            if (a.getId() == monInToAcctId) {
                                debtAmtFromDb = a.getAcctBal();
                                debtLimitFromDb = a.getAcctMax();
                                debtRateFromDb = a.getAcctIntRate();
                                debtPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                debtAmtFromDb,
                                debtRateFromDb,
                                debtPaytFromDb,
                                getString(R.string.debt_paid),
                                getString(R.string.too_far)), monInToAcctId);
                    } else if (monInIsDebtSav.equals("S")) {
                        monInLstDbMgr.updateRecMinusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getSavings()) {
                            if (a.getId() == monInToAcctId) {
                                savGoalFromDb = a.getAcctMax();
                                savAmtFromDb = a.getAcctBal();
                                savRateFromDb = a.getAcctIntRate();
                                savPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                savGoalFromDb,
                                savAmtFromDb,
                                savRateFromDb,
                                savPaytFromDb,
                                getString(R.string.goal_achieved),
                                getString(R.string.too_far)), monInToAcctId);
                    }

                    monInLstDbMgr.deleteTransactions(monInLstMonInDb);
                    monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                    notifyDataSetChanged();

                    monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365));

                    monInLstRefresh();
                }
            });
            return convertView;
        }
    }

    private static class MoneyIn2ViewHolder {
        public TextView monInLstCatTV;
        public TextView monInLstAmtTV;
        public TextView monInLstDateTV;
        public TextView monInLstAcctLabelTV;
        public TextView monInLstAcctTV;
        public ImageButton monInLstEditBtn;
        public ImageButton monInLstDelBtn;
        public RelativeLayout monInLstUpdateLayout;
        public EditText monInLstAmtET;
        public Button monInLstUpdateBtn;
        public Button monInLstCancelBtn;
        public LinearLayout monInLstWarnLayout;
        public TextView monInLstWarnTV;
        public Button monInLstYesBtn;
        public Button monInLstNoBtn;
    }

    public class MonInLst2Adapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> moneyIns;

        private MonInLst2Adapter(
                Context context,
                List<TransactionsDb> moneyIns) {

            super(context, -1, moneyIns);

            this.context = context;
            this.moneyIns = moneyIns;
        }

        public void updateMoneyIns(List<TransactionsDb> moneyIns) {
            this.moneyIns = moneyIns;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIns.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInList2ViewHolder monInLst2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monInLst2Hldr = new MoneyInList2ViewHolder();
                monInLst2Hldr.monInLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monInLst2Hldr.monInLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monInLst2Hldr.monInLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monInLst2Hldr.monInLstAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                monInLst2Hldr.monInLstAcctLabelTV.setText(R.string.deposited_to);
                monInLst2Hldr.monInLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monInLst2Hldr.monInLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monInLst2Hldr.monInLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                if (monInLstDbMgr.retrieveLastPageId() == 10) {
                    monInLst2Hldr.monInLstEditBtn.setVisibility(View.VISIBLE);
                    monInLst2Hldr.monInLstDelBtn.setVisibility(View.VISIBLE);
                } else {
                    monInLst2Hldr.monInLstEditBtn.setVisibility(View.GONE);
                    monInLst2Hldr.monInLstDelBtn.setVisibility(View.GONE);
                }
                monInLst2Hldr.monInLstUpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monInLst2Hldr.monInLstUpdateLayout.setVisibility(View.GONE);
                monInLst2Hldr.monInLstAmtET = convertView.findViewById(R.id.list5AmtET);
                monInLst2Hldr.monInLstUpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monInLst2Hldr.monInLstCancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monInLst2Hldr.monInLstWarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monInLst2Hldr.monInLstWarnLayout.setVisibility(View.GONE);
                monInLst2Hldr.monInLstWarnTV = convertView.findViewById(R.id.list5WarnTV);
                monInLst2Hldr.monInLstYesBtn = convertView.findViewById(R.id.list5YesBtn);
                monInLst2Hldr.monInLstNoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monInLst2Hldr);

            } else {
                monInLst2Hldr = (MoneyInList2ViewHolder) convertView.getTag();
            }

            monInLst2Hldr.monInLstCatTV.setText(moneyIns.get(position).getTransBdgtCat());
            monInLstMonInAmt = moneyIns.get(position).getTransAmt();
            monInLstGen.dblASCurrency(String.valueOf(monInLstMonInAmt), monInLst2Hldr.monInLstAmtTV);
            monInLst2Hldr.monInLstDateTV.setText(moneyIns.get(position).getTransCreatedOn());
            monInLst2Hldr.monInLstAcctTV.setText(moneyIns.get(position).getTransToAcctName());

            monInLst2Hldr.monInLstEditBtn.setTag(moneyIns.get(position));
            monInLst2Hldr.monInLstDelBtn.setTag(moneyIns.get(position));

            monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();
            monInIsDebtSav = monInLstDbMgr.findMoneyInIsDebtSav(moneyIns.get(position).getTransToAcctId());

            //click on pencil icon
            monInLst2Hldr.monInLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLst2Hldr.monInLstEditBtn.getTag();
                    LayoutMoneyInList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monInLstDbMgr = new DbManager(getContext());

                    monInLst2Hldr.monInLstUpdateLayout.setVisibility(View.VISIBLE);

                    monInLst2Hldr.monInLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInLstRefresh();
                        }
                    });

                    monInLst2Hldr.monInLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monInLstAmtEntry = monInLstGen.dblFromET(monInLst2Hldr.monInLstAmtET);
                            monInLstMonInAmtDiff = monInLstAmtEntry - moneyIns.get(position).getTransAmt();

                            if (!monInIsDebtSav.equals("D") && !monInIsDebtSav.equals("S")) {
                                monInLstDbMgr.updateTotAcctBalPlus(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAccountBalance());
                                monInLstDbMgr.updateAandBBalMinus(
                                        moneyIns.get(position).getTransAmtInA(),
                                        moneyIns.get(position).getTransAmtInOwing(),
                                        moneyIns.get(position).getTransAmtInB(),
                                        monInLstDbMgr.retrieveCurrentA(),
                                        monInLstDbMgr.retrieveCurrentOwingA(),
                                        monInLstDbMgr.retrieveCurrentB());

                                monInLstPercentA = monInLstDbMgr.sumTotalAExpenses() / monInLstDbMgr.sumTotalIncome();
                                monInLstMoneyInA = monInLstDbMgr.detAPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInOwing = monInLstDbMgr.detOwingPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInB = monInLstDbMgr.detBPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());

                                monInLstDbMgr.updateAandBBalPlus(monInLstMoneyInA, monInLstMoneyInOwing, monInLstMoneyInB, monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentB());

                                if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                                    monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                                    newMoneyA = monInLstDbMgr.detNewAPortion(monInLstMoneyInA, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyOwing = monInLstDbMgr.detNewOwingPortion(monInLstMoneyInOwing, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyB = monInLstDbMgr.detNewBPortion(monInLstMoneyInB, monInLstDbMgr.retrieveCurrentOwingA());
                                    moneyInA = newMoneyA;
                                    moneyInOwing = newMoneyOwing;
                                    moneyInB = newMoneyB;
                                } else {
                                    moneyInA = monInLstMoneyInA;
                                    moneyInOwing = monInLstMoneyInOwing;
                                    moneyInB = monInLstMoneyInB;
                                }
                            } else if (monInIsDebtSav.equals("D")) {
                                monInLstDbMgr.updateRecMinusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getDebts()) {
                                    if (a.getId() == monInToAcctId) {
                                        debtAmtFromDb = a.getAcctBal();
                                        debtLimitFromDb = a.getAcctMax();
                                        debtRateFromDb = a.getAcctIntRate();
                                        debtPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                        debtAmtFromDb,
                                        debtRateFromDb,
                                        debtPaytFromDb,
                                        getString(R.string.debt_paid),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            } else if (monInIsDebtSav.equals("S")) {
                                monInLstDbMgr.updateRecPlusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getSavings()) {
                                    if (a.getId() == monInToAcctId) {
                                        savGoalFromDb = a.getAcctMax();
                                        savAmtFromDb = a.getAcctBal();
                                        savRateFromDb = a.getAcctIntRate();
                                        savPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            }

                            monInLstMonInDb.setTransAmt(monInLstAmtEntry);
                            monInLstMonInDb.setTransAmtInA(moneyInA);
                            monInLstMonInDb.setTransAmtInOwing(moneyInOwing);
                            monInLstMonInDb.setTransAmtInB(moneyInB);
                            monInLstDbMgr.updateTransactions(monInLstMonInDb);

                            monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                            notifyDataSetChanged();

                            monInLstHelper = new DbHelper(getContext());
                            monInLstDb = monInLstHelper.getWritableDatabase();
                            monInLstCV = new ContentValues();
                            monInLstCV.put(DbHelper.BDGTANNPAYT, monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365)));
                            monInLstDb.update(DbHelper.BUDGET_TABLE_NAME, monInLstCV, DbHelper.ID + "=" + monInLstIncRefKeyMI, null);
                            monInLstDb.close();

                            monInLstRefresh();
                        }
                    });
                }
            });

            //click on trash can icon
            monInLst2Hldr.monInLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLst2Hldr.monInLstDelBtn.getTag();
                    monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();

                    if (!monInIsDebtSav.equals("D") && !monInIsDebtSav.equals("S")) {
                        monInLstDbMgr.updateTotAcctBalMinus(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAccountBalance());

                        monInLstDbMgr.updateAandBBalMinus(
                                moneyIns.get(position).getTransAmtInA(),
                                moneyIns.get(position).getTransAmtInOwing(),
                                moneyIns.get(position).getTransAmtInB(),
                                monInLstDbMgr.retrieveCurrentA(),
                                monInLstDbMgr.retrieveCurrentOwingA(),
                                monInLstDbMgr.retrieveCurrentB());
                        if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                        }
                    } else if (monInIsDebtSav.equals("D")) {
                        monInLstDbMgr.updateRecPlusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getDebts()) {
                            if (a.getId() == monInToAcctId) {
                                debtAmtFromDb = a.getAcctBal();
                                debtLimitFromDb = a.getAcctMax();
                                debtRateFromDb = a.getAcctIntRate();
                                debtPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                debtAmtFromDb,
                                debtRateFromDb,
                                debtPaytFromDb,
                                getString(R.string.debt_paid),
                                getString(R.string.too_far)), monInToAcctId);
                    } else if (monInIsDebtSav.equals("S")) {
                        monInLstDbMgr.updateRecMinusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getSavings()) {
                            if (a.getId() == monInToAcctId) {
                                savGoalFromDb = a.getAcctMax();
                                savAmtFromDb = a.getAcctBal();
                                savRateFromDb = a.getAcctIntRate();
                                savPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                savGoalFromDb,
                                savAmtFromDb,
                                savRateFromDb,
                                savPaytFromDb,
                                getString(R.string.goal_achieved),
                                getString(R.string.too_far)), monInToAcctId);
                    }

                    monInLstDbMgr.deleteTransactions(monInLstMonInDb);
                    monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                    notifyDataSetChanged();

                    monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365));

                    monInLstRefresh();
                }
            });
            return convertView;
        }
    }

    private static class MoneyInList2ViewHolder {
        public TextView monInLstCatTV;
        public TextView monInLstAmtTV;
        public TextView monInLstDateTV;
        public TextView monInLstAcctLabelTV;
        public TextView monInLstAcctTV;
        public ImageButton monInLstEditBtn;
        public ImageButton monInLstDelBtn;
        public RelativeLayout monInLstUpdateLayout;
        public EditText monInLstAmtET;
        public Button monInLstUpdateBtn;
        public Button monInLstCancelBtn;
        public LinearLayout monInLstWarnLayout;
        public TextView monInLstWarnTV;
        public Button monInLstYesBtn;
        public Button monInLstNoBtn;
    }
}
