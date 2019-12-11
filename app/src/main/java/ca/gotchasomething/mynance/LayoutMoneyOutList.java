package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutMoneyOutList extends MainNavigation {

    ArrayAdapter monOutLstFromMthSpinAdapter, monOutLstToMthSpinAdapter, monOutLstFromYrSpinAdapter, monOutLstToYrSpinAdapter;
    boolean monOutLstLeapYear;
    Button dialogCancelBtn, dialogNoContBtn, dialogUpdateBtn, dialogYesContBtn, monOutLstAddMoreBtn, monOutLstDoneBtn, monOutLstSpinOkBtn, monOutLstSpinResetBtn;
    Calendar monOutLstCal;
    ContentValues monOutLstCV;
    Date monOutLstEarliestDate, monOutLstLatestDate;
    DbHelper monOutLstHelper;
    DbManager monOutLstDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyOutA = 0.0, moneyOutB = 0.0, moneyOutOwing = 0.0, monOutLstAmtEntry = 0.0, monOutLstMoneyOutA = 0.0, monOutLstMoneyOutOwing = 0.0,
            monOutLstMoneyOutB = 0.0, monOutLstMonOutAmt = 0.0, monOutLstMonOutAmtDiff = 0.0, monOutLstTransAmtOutA = 0.0, monOutLstTransAmtOutOwing = 0.0, monOutLstTransAmtOutB = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0,
            savGoalFromDb = 0.0, savAmtFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    EditText dialogAmtET;
    General monOutLstGen;
    int monOutLstYear;
    Intent monOutLstToMain, monOutLstToAddMonOut, monOutLstRefresh;
    LinearLayout dialogWarnLayout, monOutLstSpinLayout;
    ListView monOutLstList;
    long monOutLstFromAcctId, monOutLstTransBdgtId;
    MonOutLstAdapter monOutLstAdapter;
    Spinner monOutLstToMthSpin, monOutLstFromMthSpin, monOutLstToYrSpin, monOutLstFromYrSpin;
    SQLiteDatabase monOutLstDb;
    String monOutLstBdgtPriority = null, monOutLstFromMonth = null, monOutLstFromYr = null, monOutLstLastDay = null, monOutLstToMonth = null, monOutLstToYr = null, monOutLstFromIsDebtSav = null, monOutLstSumTotalSelTrans = null;
    String[] monOutLstMonths, monOutLstOnlyMonths, monOutLstYears;
    TextView dialogWarnTV, monOutLstAndTV, monOutLstTitle, monOutLstTotalTV;
    TransactionsDb monOutLstMonOutDb;
    View dView;

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
        monOutLstSpinLayout.setVisibility(View.GONE);
        /*monOutLstToMthSpin = findViewById(R.id.layout1ToMthSpin);
        monOutLstFromMthSpin = findViewById(R.id.layout1FromMthSpin);
        monOutLstToYrSpin = findViewById(R.id.layout1ToYrSpin);
        monOutLstFromYrSpin = findViewById(R.id.layout1FromYrSpin);
        monOutLstAndTV = findViewById(R.id.layout1AndTV);
        monOutLstSpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        monOutLstSpinResetBtn = findViewById(R.id.layout1SpinResetBtn);*/
        monOutLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monOutLstTitle.setText(getString(R.string.cash_debit_transactions));
        monOutLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monOutLstAddMoreBtn.setText(getString(R.string.record_cash_debit));
        monOutLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monOutLstDoneBtn.setOnClickListener(onClickMonOutLstDoneBtn);
        monOutLstTotalTV = findViewById(R.id.layout1TotalTV);
        monOutLstTotalTV.setVisibility(View.GONE);
        monOutLstList = findViewById(R.id.layout1ListView);

        monOutLstAddMoreBtn.setOnClickListener(onClickMonOutLstAddMoreBtn);

        monOutLstAdapter = new MonOutLstAdapter(this, monOutLstDbMgr.getCashTrans());
        monOutLstList.setAdapter(monOutLstAdapter);

        /*if (monOutLstDbMgr.retrieveLastPageId() == 10) {
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
        }*/
    }

    public void monOutLstRefresh() {
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

    /*Spinner.OnItemSelectedListener monOutLstOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
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
    };*/

    /*public void monOutLstMainAcctMinus() {
        monOutLstDbMgr.updateTotAcctBalMinus(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentAccountBalance());

        monOutLstDbMgr.updateAandBBalPlus(monOutLstTransAmtOutA, monOutLstTransAmtOutOwing, monOutLstTransAmtOutB, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());

        monOutLstMoneyOutA = monOutLstDbMgr.detAPortionExp(monOutLstAmtEntry, monOutLstBdgtPriority, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
        monOutLstMoneyOutOwing = monOutLstDbMgr.detOwingPortionExp(monOutLstAmtEntry, monOutLstBdgtPriority, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
        monOutLstMoneyOutB = monOutLstDbMgr.detBPortionExp(monOutLstAmtEntry, monOutLstBdgtPriority, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());

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
    }

    public void monOutLstSavMinus() {
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

    public void monOutLstDebtPlus() {
        monOutLstDbMgr.updateRecPlusPt1(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId), monOutLstFromAcctId);
        for (AccountsDb a : monOutLstDbMgr.getDebts()) {
            if (a.getId() == monOutLstFromAcctId) {
                debtLimitFromDb = a.getAcctMax();
                debtAmtFromDb = a.getAcctBal();
                debtRateFromDb = a.getAcctIntRate();
                debtPaytFromDb = a.getAcctPaytsTo();
            }
        }
        monOutLstDbMgr.updateRecPt2(monOutLstGen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), monOutLstFromAcctId);
        moneyOutA = 0.0;
        moneyOutOwing = 0.0;
        moneyOutB = 0.0;
    }*/

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
                monOutLstHldr.monOutLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monOutLstHldr.monOutLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                monOutLstHldr.monOutLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monOutLstHldr.monOutLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monOutLstHldr.monOutLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monOutLstHldr.monOutLstAcctLabel = convertView.findViewById(R.id.list5CCLabel);
                monOutLstHldr.monOutLstAcctLabel.setText(R.string.paid_from);
                monOutLstHldr.monOutLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monOutLstHldr.monOutLstAcctLabel2 = convertView.findViewById(R.id.list5CCLabel2);
                monOutLstHldr.monOutLstAcctLabel2.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctTV2 = convertView.findViewById(R.id.list5CCTV2);
                monOutLstHldr.monOutLstAcctTV2.setVisibility(View.GONE);
                monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);

                convertView.setTag(monOutLstHldr);

            } else {
                monOutLstHldr = (MoneyOut2ViewHolder) convertView.getTag();
            }

            /*if (moneyOuts.get(position).getTransIsCC().equals("Y")) {
                monOutLstHldr.monOutLstCatTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDateTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctLabel.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);
            }*/

            monOutLstHldr.monOutLstDateTV.setText(moneyOuts.get(position).getTransCreatedOn());

            monOutLstMonOutAmt = moneyOuts.get(position).getTransAmt();
            monOutLstGen.dblASCurrency(String.valueOf(monOutLstMonOutAmt), monOutLstHldr.monOutLstAmtTV);

            monOutLstHldr.monOutLstCatTV.setText(moneyOuts.get(position).getTransBdgtCat());
            monOutLstHldr.monOutLstAcctTV.setText(moneyOuts.get(position).getTransFromAcctName());

            /*monOutLstHldr.monOutLstEditBtn.setTag(moneyOuts.get(position));
            monOutLstHldr.monOutLstDelBtn.setTag(moneyOuts.get(position));*/

            /*monOutLstTransBdgtId = moneyOuts.get(position).getTransBdgtId();
            monOutLstFromIsDebtSav = moneyOuts.get(position).getTransFromDebtSav();
            monOutLstTransAmtOutA = moneyOuts.get(position).getTransAmtOutA();
            monOutLstTransAmtOutOwing = moneyOuts.get(position).getTransAmtOutOwing();
            monOutLstTransAmtOutB = moneyOuts.get(position).getTransAmtOutB();
            monOutLstBdgtPriority = moneyOuts.get(position).getTransBdgtPriority();*/

            //click on pencil icon
            /*monOutLstHldr.monOutLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (TransactionsDb) monOutLstHldr.monOutLstEditBtn.getTag();
                    LayoutMoneyOutList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monOutLstDbMgr = new DbManager(getContext());

                    AlertDialog.Builder builder = new AlertDialog.Builder(LayoutMoneyOutList.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_update, null);
                    dialogAmtET = dView.findViewById(R.id.dialogAmtET);
                    dialogUpdateBtn = dView.findViewById(R.id.dialogUpdateBtn);
                    dialogCancelBtn = dView.findViewById(R.id.dialogCancelBtn);
                    dialogWarnLayout = dView.findViewById(R.id.dialogWarnLayout);
                    dialogWarnLayout.setVisibility(View.GONE);
                    dialogWarnTV = dView.findViewById(R.id.dialogWarnTV);
                    dialogNoContBtn = dView.findViewById(R.id.dialogNoContBtn);
                    dialogYesContBtn = dView.findViewById(R.id.dialogYesContBtn);

                    dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutLstRefresh();
                        }
                    });

                    dialogUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monOutLstAmtEntry = monOutLstGen.dblFromET(monOutLstHldr.monOutLstAmtET);
                            monOutLstMonOutAmtDiff = monOutLstAmtEntry - moneyOuts.get(position).getTransAmt();

                            if (!monOutLstFromIsDebtSav.equals("S") && !monOutLstFromIsDebtSav.equals("D")) { //FROM MAIN ACCT
                                if ((monOutLstBdgtPriority.equals("A") && monOutLstDbMgr.retrieveCurrentAccountBalance() < monOutLstMonOutAmtDiff)) { //IF A AND WILL GO NEGATIVE
                                    dialogWarnLayout.setVisibility(View.VISIBLE);
                                    dialogWarnTV.setText(getString(R.string.payment_not_possible_A));
                                    dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutLstRefresh();
                                        }
                                    });

                                    dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogWarnLayout.setVisibility(View.GONE);
                                            monOutLstMainAcctMinus();
                                        }
                                    });
                                } else if ((monOutLstBdgtPriority.equals("B") && monOutLstDbMgr.retrieveCurrentB() < monOutLstMonOutAmtDiff)) { //IF B AND WILL GO NEGATIVE
                                    dialogWarnLayout.setVisibility(View.VISIBLE);
                                    dialogWarnTV.setText(getString(R.string.payment_not_possible_B));
                                    dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutLstRefresh();
                                        }
                                    });

                                    dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogWarnLayout.setVisibility(View.GONE);
                                            monOutLstMainAcctMinus();
                                        }
                                    });
                                } else { //WILL NOT GO NEGATIVE
                                    dialogWarnLayout.setVisibility(View.GONE);
                                    monOutLstMainAcctMinus();
                                }
                            } else if (monOutLstFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                                if (monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId) - monOutLstMonOutAmtDiff < 0) { //WILL GO NEGATIVE
                                    dialogWarnLayout.setVisibility(View.VISIBLE);
                                    dialogWarnTV.setText(getString(R.string.not_enough_savings_warning));
                                    dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutLstRefresh();
                                        }
                                    });

                                    dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogWarnLayout.setVisibility(View.GONE);
                                            monOutLstSavMinus();
                                        }
                                    });
                                } else { //WILL NOT GO NEGATIVE
                                    monOutLstSavMinus();
                                }
                            } else if (monOutLstFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                if (monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId) + monOutLstMonOutAmtDiff > debtLimitFromDb) { //WILL GO OVER LIMIT
                                    dialogWarnLayout.setVisibility(View.VISIBLE);
                                    dialogWarnTV.setText(getString(R.string.not_enough_credit_warning));
                                    dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutLstRefresh();
                                        }
                                    });

                                    dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogWarnLayout.setVisibility(View.GONE);
                                            monOutLstDebtPlus();
                                        }
                                    });
                                } else { //WILL NOT GO OVER LIMIT
                                    monOutLstDebtPlus();
                                }
                            }

                            monOutLstMonOutDb.setTransAmt(monOutLstAmtEntry);
                            monOutLstMonOutDb.setTransAmtOutA(moneyOutA);
                            monOutLstMonOutDb.setTransAmtOutOwing(moneyOutOwing);
                            monOutLstMonOutDb.setTransAmtOutB(moneyOutB);
                            monOutLstDbMgr.updateTransactions(monOutLstMonOutDb);

                            monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                            notifyDataSetChanged();

                            monOutLstHelper = new DbHelper(getApplicationContext());
                            monOutLstDb = monOutLstHelper.getWritableDatabase();
                            monOutLstCV = new ContentValues();
                            monOutLstCV.put(DbHelper.BDGTANNPAYT, monOutLstDbMgr.makeNewExpAnnAmt(monOutLstTransBdgtId, monOutLstGen.lastNumOfDays(365)));
                            monOutLstDb.update(DbHelper.BUDGET_TABLE_NAME, monOutLstCV, DbHelper.ID + "=" + monOutLstTransBdgtId, null);
                            monOutLstDb.close();

                            monOutLstRefresh();
                        }
                    });
                    builder.setView(dView);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });*/

            //click on trash can icon
            /*monOutLstHldr.monOutLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (TransactionsDb) monOutLstHldr.monOutLstDelBtn.getTag();

                    if (!monOutLstFromIsDebtSav.equals("S") && !monOutLstFromIsDebtSav.equals("D")) { //FROM MAIN ACCT
                        monOutLstDbMgr.updateTotAcctBalPlus(moneyOuts.get(position).getTransAmt(), monOutLstDbMgr.retrieveCurrentAccountBalance());

                        monOutLstDbMgr.updateAandBBalPlus(monOutLstTransAmtOutA, monOutLstTransAmtOutOwing, monOutLstTransAmtOutB, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());
                        if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monOutLstDbMgr.adjustCurrentAandB(monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                        }
                    } else if (monOutLstFromIsDebtSav.equals("S")) { //FROM SAV ACCT
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
                    } else if (monOutLstFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                        monOutLstDbMgr.updateRecMinusPt1(moneyOuts.get(position).getTransAmt(), monOutLstDbMgr.retrieveCurrentAcctAmt(monOutLstFromAcctId), monOutLstFromAcctId);
                        for (AccountsDb a : monOutLstDbMgr.getDebts()) {
                            if (a.getId() == monOutLstFromAcctId) {
                                debtLimitFromDb = a.getAcctMax();
                                debtAmtFromDb = a.getAcctBal();
                                debtRateFromDb = a.getAcctIntRate();
                                debtPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monOutLstDbMgr.updateRecPt2(monOutLstGen.calcDebtDate(
                                debtAmtFromDb,
                                debtRateFromDb,
                                debtPaytFromDb,
                                getString(R.string.debt_paid),
                                getString(R.string.too_far)), monOutLstFromAcctId);
                        moneyOutA = 0.0;
                        moneyOutOwing = 0.0;
                        moneyOutB = 0.0;
                    }

                    monOutLstDbMgr.deleteTransactions(monOutLstMonOutDb);
                    monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                    notifyDataSetChanged();

                    monOutLstHelper = new DbHelper(getApplicationContext());
                    monOutLstDb = monOutLstHelper.getWritableDatabase();
                    monOutLstCV = new ContentValues();
                    monOutLstCV.put(DbHelper.BDGTANNPAYT, monOutLstDbMgr.makeNewExpAnnAmt(monOutLstTransBdgtId, monOutLstGen.lastNumOfDays(365)));
                    monOutLstDb.update(DbHelper.BUDGET_TABLE_NAME, monOutLstCV, DbHelper.ID + "=" + monOutLstTransBdgtId, null);
                    monOutLstDb.close();

                    monOutLstRefresh();
                }
            });*/
            return convertView;
        }
    }

    private static class MoneyOut2ViewHolder {
        public TextView monOutLstCatTV;
        public TextView monOutLstAmtTV;
        public TextView monOutLstDateTV;
        public TextView monOutLstAcctLabel;
        public TextView monOutLstAcctTV;
        public TextView monOutLstAcctLabel2;
        public TextView monOutLstAcctTV2;
        public ImageButton monOutLstEditBtn;
        public ImageButton monOutLstDelBtn;
    }
}
