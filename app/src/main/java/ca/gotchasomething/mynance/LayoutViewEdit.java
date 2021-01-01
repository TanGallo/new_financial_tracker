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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutViewEdit extends MainNavigation {

    ArrayAdapter layVwEdFromMthSpinAdapter,
            layVwEdToMthSpinAdapter,
            layVwEdFromYrSpinAdapter,
            layVwEdToYrSpinAdapter;
    boolean layVwEdLeapYear = false;
    Button dialogCancelBtn,
            dialogUpdateBtn,
            dialogNoContBtn,
            dialogYesContBtn,
            layVwEdAddMoreBtn,
            layVwEdDoneBtn,
            layVwEdSpinOkBtn,
            layVwEdSpinResetBtn;
    Calendar layVwEdCal;
    Date layVwEdEarliestDate,
            layVwEdLatestDate;
    DbManager layVwEdDbMgr;
    Double aInPercent = 0.0,
            owingInPercent = 0.0,
            bInPercent = 0.0,
            aOutPercent = 0.0,
            owingOutPercent = 0.0,
            bOutPercent = 0.0,
            aInDiff = 0.0,
            owingInDiff = 0.0,
            bInDiff = 0.0,
            aOutDiff = 0.0,
            owingOutDiff = 0.0,
            bOutDiff = 0.0,
            debtLimitFromDb = 0.0,
            debtOwingFromDb = 0.0,
            debtPaytFromDb = 0.0,
            debtRateFromDb = 0.0,
            layVwEdTransAmt = 0.0,
            layVwEdAmtEntry = 0.0,
            layVwEdAmtDiff = 0.0,
            layVwEdFinMoneyInA = 0.0,
            layVwEdFinMoneyInOwing = 0.0,
            layVwEdFinMoneyInB = 0.0,
            layVwEdPercentA = 0.0,
            layVwEdMoneyInA = 0.0,
            layVwEdMoneyInOwing = 0.0,
            layVwEdMoneyInB = 0.0,
            layVwEdMoneyOutA = 0.0,
            layVwEdMoneyOutOwing = 0.0,
            layVwEdMoneyOutB = 0.0,
            layVwEdTransAmtOutA = 0.0,
            layVwEdTransAmtOutOwing = 0.0,
            layVwEdTransAmtOutB = 0.0,
            layVwEdTransAmtInA = 0.0,
            layVwEdTransAmtInOwing = 0.0,
            layVwEdTransAmtInB = 0.0,
            moneyOutA = 0.0,
            moneyOutOwing = 0.0,
            moneyOutB = 0.0,
            newAnnAmt = 0.0,
            newLayVwEdMoneyInA = 0.0,
            newLayVwEdMoneyInB = 0.0,
            newMoneyA = 0.0,
            newMoneyOwing = 0.0,
            newMoneyB = 0.0,
            newMoneyOutA = 0.0,
            newMoneyOutOwing = 0.0,
            newMoneyOutB = 0.0,
            newNewLayVwEdMoneyInB = 0.0,
            savAmtFromDb = 0.0,
            savGoalFromDb = 0.0,
            savPaytFromDb = 0.0,
            savRateFromDb = 0.0;
    EditText dialogAmtET;
    General layVwEdGen;
    int layVwEdYear;
    Intent layVwEdRefresh;
    LayVwEdAdapter layVwEdAdapter;
    LinearLayout dialogWarnLayout,
            layVwEdSpinLayout;
    ListView layVwEdList;
    long delBdgtId,
            editBdgtId,
            layVwEdTransBdgtId,
            layVwEdToAcctId,
            layVwEdFromAcctId;
    Spinner layVwEdToMthSpin,
            layVwEdFromMthSpin,
            layVwEdToYrSpin,
            layVwEdFromYrSpin;
    String layVwEdPriority = null,
            layVwEdSumTotalSelTrans = null,
            layVwEdTransType = null,
            layVwEdLastDay = null,
            layVwEdFromMonth = null,
            layVwEdToMonth = null,
            layVwEdFromYr = null,
            layVwEdToYr = null,
            layVwEdToIsDebtSav = null,
            layVwEdFromIsDebtSav = null;
    String[] layVwEdMonths,
            layVwEdOnlyMonths,
            layVwEdYears;
    TextView dialogWarnTV,
            layVwEdAndTV,
            layVwEdTitle,
            layVwEdTotalTV;
    TransactionsDb layVwEdTransDb;
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

        layVwEdDbMgr = new DbManager(this);
        layVwEdGen = new General();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        layVwEdSpinLayout = findViewById(R.id.layout1SpinLayout);
        layVwEdToMthSpin = findViewById(R.id.layout1ToMthSpin);
        layVwEdFromMthSpin = findViewById(R.id.layout1FromMthSpin);
        layVwEdToYrSpin = findViewById(R.id.layout1ToYrSpin);
        layVwEdFromYrSpin = findViewById(R.id.layout1FromYrSpin);
        layVwEdAndTV = findViewById(R.id.layout1AndTV);
        layVwEdSpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        layVwEdSpinResetBtn = findViewById(R.id.layout1SpinResetBtn);
        layVwEdSpinResetBtn.setVisibility(View.GONE);
        layVwEdAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        layVwEdAddMoreBtn.setVisibility(View.GONE);
        layVwEdTitle = findViewById(R.id.layout1HeaderLabelTV);
        layVwEdTitle.setText(getString(R.string.transactions));
        layVwEdDoneBtn = findViewById(R.id.layout1DoneBtn);
        layVwEdDoneBtn.setVisibility(View.GONE);
        layVwEdTotalTV = findViewById(R.id.layout1TotalTV);
        layVwEdTotalTV.setVisibility(View.GONE);
        layVwEdList = findViewById(R.id.layout1ListView);

        layVwEdAdapter = new LayVwEdAdapter(this, layVwEdDbMgr.getTransactions());
        layVwEdList.setAdapter(layVwEdAdapter);

        layVwEdSpinLayout.setVisibility(View.VISIBLE);
        layVwEdSpinResetBtn.setVisibility(View.GONE);
        layVwEdMonths = new String[]{
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
        layVwEdOnlyMonths = new String[]{
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
        layVwEdYears = layVwEdGen.yearsList(layVwEdDbMgr.getEarliestEntry(layVwEdDbMgr.getYearsList()), layVwEdDbMgr.getLatestEntry(layVwEdDbMgr.getYearsList()));

        layVwEdFromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, layVwEdMonths);
        layVwEdFromMthSpin.setAdapter(layVwEdFromMthSpinAdapter);
        layVwEdFromMthSpin.setOnItemSelectedListener(layVwEdOnFromMonthSelected);

        layVwEdFromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, layVwEdYears);
        layVwEdFromYrSpin.setAdapter(layVwEdFromYrSpinAdapter);
        layVwEdFromYrSpin.setOnItemSelectedListener(layVwEdOnFromYearSelected);

        layVwEdToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, layVwEdOnlyMonths);
        layVwEdToMthSpin.setAdapter(layVwEdToMthSpinAdapter);
        layVwEdToMthSpin.setOnItemSelectedListener(layVwEdOnToMonthSelected);

        layVwEdToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, layVwEdYears);
        layVwEdToYrSpin.setAdapter(layVwEdToYrSpinAdapter);
        layVwEdToYrSpin.setOnItemSelectedListener(layVwEdOnToYearSelected);

        layVwEdSpinOkBtn.setOnClickListener(onClickSpinOkBtn);
        layVwEdSpinResetBtn.setOnClickListener(onClickSpinResetBtn);
    }

    public void layVwEdRefresh() {
        layVwEdRefresh = new Intent(this, LayoutViewEdit.class);
        layVwEdRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layVwEdRefresh);
    }

    Spinner.OnItemSelectedListener layVwEdOnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                layVwEdFromMonth = getString(R.string.year_to_date);
                layVwEdFromYrSpin.setVisibility(View.GONE);
                layVwEdToMthSpin.setVisibility(View.GONE);
                layVwEdToYrSpin.setVisibility(View.GONE);
                layVwEdAndTV.setVisibility(View.GONE);
            } else {
                layVwEdFromMonth = String.valueOf(layVwEdFromMthSpin.getSelectedItem());
                layVwEdFromYrSpin.setVisibility(View.VISIBLE);
                layVwEdToMthSpin.setVisibility(View.VISIBLE);
                layVwEdToYrSpin.setVisibility(View.VISIBLE);
                layVwEdAndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener layVwEdOnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            layVwEdFromYr = String.valueOf(layVwEdFromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener layVwEdOnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            layVwEdToMonth = String.valueOf(layVwEdToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener layVwEdOnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            layVwEdToYr = String.valueOf(layVwEdToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdSpinLayout.setVisibility(View.GONE);
            if (layVwEdFromMonth.equals(getString(R.string.year_to_date))) {
                layVwEdCal = Calendar.getInstance();
                layVwEdYear = layVwEdCal.get(Calendar.YEAR);
                layVwEdEarliestDate = layVwEdGen.dateFromString("1-" + getString(R.string.jan) + "-" + layVwEdYear);
                layVwEdLatestDate = layVwEdGen.dateFromString("31-" + getString(R.string.dec) + "-" + layVwEdYear);
            } else {
                layVwEdEarliestDate = layVwEdGen.dateFromString("1-" + layVwEdFromMonth + "-" + layVwEdFromYr);
                layVwEdLastDay = null;
                if (layVwEdToMonth.equals(getString(R.string.jan)) ||
                        layVwEdToMonth.equals(getString(R.string.mar)) ||
                        layVwEdToMonth.equals(getString(R.string.may)) ||
                        layVwEdToMonth.equals(getString(R.string.jul)) ||
                        layVwEdToMonth.equals(getString(R.string.aug)) ||
                        layVwEdToMonth.equals(getString(R.string.oct)) ||
                        layVwEdToMonth.equals(getString(R.string.dec))) {
                    layVwEdLastDay = "31";
                } else if (layVwEdToMonth.equals(getString(R.string.apr)) ||
                        layVwEdToMonth.equals(getString(R.string.jun)) ||
                        layVwEdToMonth.equals(getString(R.string.sep)) ||
                        layVwEdToMonth.equals(getString(R.string.nov))) {
                    layVwEdLastDay = "30";
                } else if (layVwEdToMonth.equals(getString(R.string.feb))) {
                    layVwEdLeapYear = layVwEdGen.checkForLeapYear(Integer.valueOf(layVwEdToYr));
                    if (layVwEdLeapYear) {
                        layVwEdLastDay = "29";
                    } else {
                        layVwEdLastDay = "28";
                    }
                }
                layVwEdLatestDate = layVwEdGen.dateFromString(layVwEdLastDay + "-" + layVwEdToMonth + "-" + layVwEdToYr);
            }
            try {
                layVwEdAdapter = new LayVwEdAdapter(getApplicationContext(), layVwEdDbMgr.getTransactionsInRange(layVwEdDbMgr.getTransactions(), layVwEdEarliestDate, layVwEdLatestDate));
                layVwEdList.setAdapter(layVwEdAdapter);
                layVwEdSpinOkBtn.setVisibility(View.GONE);
                layVwEdSpinResetBtn.setVisibility(View.VISIBLE);
                layVwEdSumTotalSelTrans = String.valueOf(layVwEdDbMgr.sumSelectedTransactions(layVwEdDbMgr.getTransactionsInRange(layVwEdDbMgr.getTransactions(), layVwEdEarliestDate, layVwEdLatestDate)));
                if (layVwEdLatestDate.before(layVwEdEarliestDate)) {
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (layVwEdList.getCount() == 0) {
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdRefresh();
        }
    };

    public void layVwEdDebtPlus(Double dbl1, long longId) {
        for (AccountsDb d : layVwEdDbMgr.getDebts()) {
            if (d.getId() == longId) {
                d.setAcctBal(d.getAcctBal() + dbl1);
                layVwEdDbMgr.updateAccounts(d);

                debtOwingFromDb = d.getAcctBal();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }
        layVwEdDbMgr.updateRecPt2(
                layVwEdGen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), longId);
    }

    public void layVwEdSavPlus(Double dbl4, long longId) {
        for (AccountsDb s : layVwEdDbMgr.getSavings()) {
            if (s.getId() == longId) {
                s.setAcctBal(s.getAcctBal() + dbl4);
                layVwEdDbMgr.updateAccounts(s);

                savGoalFromDb = s.getAcctMax();
                savAmtFromDb = s.getAcctBal();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }
        layVwEdDbMgr.updateRecPt2(
                layVwEdGen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), longId);
    }

    public void layVwEdDebtMinus(Double dbl3, long longId) {
        for (AccountsDb d : layVwEdDbMgr.getDebts()) {
            if (d.getId() == longId) {
                d.setAcctBal(d.getAcctBal() - dbl3);
                layVwEdDbMgr.updateAccounts(d);

                debtOwingFromDb = d.getAcctBal();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }
        layVwEdDbMgr.updateRecPt2(
                layVwEdGen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), longId);
    }

    public void layVwEdSavMinus(Double dbl2, long longId) {
        for (AccountsDb s : layVwEdDbMgr.getSavings()) {
            if (s.getId() == longId) {
                s.setAcctBal(s.getAcctBal() - dbl2);
                layVwEdDbMgr.updateAccounts(s);

                savGoalFromDb = s.getAcctMax();
                savAmtFromDb = s.getAcctBal();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }
        layVwEdDbMgr.updateRecPt2(
                layVwEdGen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), longId);
    }

    public void layVwEdFinishOut() {
        layVwEdTransDb.setTransAmt(layVwEdAmtEntry);
        layVwEdTransDb.setTransAmtOutA(moneyOutA);
        layVwEdTransDb.setTransAmtOutOwing(moneyOutOwing);
        layVwEdTransDb.setTransAmtOutB(moneyOutB);
        layVwEdDbMgr.updateTransactions(layVwEdTransDb);

        layVwEdAdapter.updateTransactions(layVwEdDbMgr.getTransactions());
        layVwEdAdapter.notifyDataSetChanged();
    }

    public void layVwEdFinishIn() {
        layVwEdTransDb.setTransAmt(layVwEdAmtEntry);
        layVwEdTransDb.setTransAmtInA(layVwEdFinMoneyInA);
        layVwEdTransDb.setTransAmtInOwing(layVwEdFinMoneyInOwing);
        layVwEdTransDb.setTransAmtInB(layVwEdFinMoneyInB);
        layVwEdDbMgr.updateTransactions(layVwEdTransDb);

        layVwEdAdapter.updateTransactions(layVwEdDbMgr.getTransactions());
        layVwEdAdapter.notifyDataSetChanged();
    }

    public void finishTheEdit() {
        dialogWarnLayout.setVisibility(View.GONE);

        for (AccountsDb a2 : layVwEdDbMgr.getAccounts()) {
            if (layVwEdFromAcctId == a2.getId()) {
                if (layVwEdFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                    moneyOutA = 0.0;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;

                    layVwEdSavMinus(layVwEdAmtDiff, layVwEdFromAcctId);
                    layVwEdFinishOut();
                } else if (layVwEdFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                    moneyOutA = 0.0;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;

                    layVwEdDebtPlus(layVwEdAmtDiff, layVwEdFromAcctId);
                    layVwEdFinishOut();
                } else if (layVwEdFromAcctId == 1) { //FROM MAIN ACCT
                    if (layVwEdTransType.equals("transfer")) { //TRANS IS A TRANSFER
                        layVwEdDbMgr.updateTotAcctBalMinus(layVwEdAmtDiff, layVwEdDbMgr.retrieveCurrentAccountBalance());
                        layVwEdDbMgr.updateAandBBalMinus(aOutDiff, owingOutDiff, bOutDiff, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentB());
                        if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
                            layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                            newMoneyOutA = layVwEdDbMgr.detNewAPortion(aOutDiff, layVwEdDbMgr.retrieveCurrentOwingA());
                            newMoneyOutOwing = layVwEdDbMgr.detNewOwingPortion(owingOutDiff, layVwEdDbMgr.retrieveCurrentOwingA());
                            newMoneyOutB = layVwEdDbMgr.detNewBPortion(bOutDiff, layVwEdDbMgr.retrieveCurrentOwingA());
                            moneyOutA = newMoneyOutA + layVwEdTransAmtOutA;
                            moneyOutOwing = newMoneyOutOwing + layVwEdTransAmtOutOwing;
                            moneyOutB = newMoneyOutB + layVwEdTransAmtOutB;
                        } else {
                            moneyOutA = aOutDiff + layVwEdTransAmtOutA;
                            moneyOutOwing = owingOutDiff + layVwEdTransAmtOutOwing;
                            moneyOutB = bOutDiff + layVwEdTransAmtOutB;
                        }
                        layVwEdFinishOut();
                    } else {
                        layVwEdDbMgr.updateTotAcctBalMinus(layVwEdAmtDiff, layVwEdDbMgr.retrieveCurrentAccountBalance());

                        layVwEdMoneyOutA = layVwEdDbMgr.detAPortionExp(layVwEdAmtDiff, layVwEdPriority, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                        layVwEdMoneyOutOwing = layVwEdDbMgr.detOwingPortionExp(layVwEdAmtDiff, layVwEdPriority, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                        layVwEdMoneyOutB = layVwEdDbMgr.detBPortionExp(layVwEdAmtDiff, layVwEdPriority, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());

                        layVwEdDbMgr.updateAandBBalMinus(layVwEdMoneyOutA, layVwEdMoneyOutOwing, layVwEdMoneyOutB, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentB());

                        if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
                            layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                            newMoneyOutA = layVwEdDbMgr.detNewAPortion(layVwEdMoneyOutA, layVwEdDbMgr.retrieveCurrentOwingA());
                            newMoneyOutOwing = layVwEdDbMgr.detNewOwingPortion(layVwEdMoneyOutOwing, layVwEdDbMgr.retrieveCurrentOwingA());
                            newMoneyOutB = layVwEdDbMgr.detNewBPortion(layVwEdMoneyOutB, layVwEdDbMgr.retrieveCurrentOwingA());
                            moneyOutA = newMoneyOutA + layVwEdTransAmtOutA;
                            moneyOutOwing = newMoneyOutOwing + layVwEdTransAmtOutOwing;
                            moneyOutB = newMoneyOutB + layVwEdTransAmtOutB;
                        } else {
                            moneyOutA = layVwEdMoneyOutA + layVwEdTransAmtOutA;
                            moneyOutOwing = layVwEdMoneyOutOwing + layVwEdTransAmtOutOwing;
                            moneyOutB = layVwEdMoneyOutB + layVwEdTransAmtOutB;
                        }
                        layVwEdFinishOut();
                    }
                }
            }
        }

        for (AccountsDb a : layVwEdDbMgr.getAccounts()) {
            if (layVwEdToAcctId == a.getId()) {
                if (layVwEdToIsDebtSav.equals("S")) { //TO SAV ACCT
                    layVwEdFinMoneyInA = 0.0;
                    layVwEdFinMoneyInOwing = 0.0;
                    layVwEdFinMoneyInB = 0.0;

                    layVwEdSavPlus(layVwEdAmtDiff, layVwEdToAcctId);
                } else if (layVwEdToIsDebtSav.equals("D")) { //TO DEBT ACCT
                    layVwEdFinMoneyInA = 0.0;
                    layVwEdFinMoneyInOwing = 0.0;
                    layVwEdFinMoneyInB = 0.0;

                    layVwEdDebtMinus(layVwEdAmtDiff, layVwEdToAcctId);
                } else if (layVwEdToAcctId == 1) { //TO MAIN ACCT
                    //PUT ALL MONEY INTO MAIN ACCT
                    layVwEdDbMgr.updateTotAcctBalPlus(layVwEdAmtDiff, layVwEdDbMgr.retrieveCurrentAccountBalance());
                    //A PORTION GOES INTO CURRENT A
                    layVwEdDbMgr.updateCurrentAPlus(aInDiff, layVwEdDbMgr.retrieveCurrentA());
                    //IF CURRENT A NOW NEGATIVE, DETERMINE NEW A AND B PORTIONS AND ADJUST CURRENT A AS APPROPRIATE
                    if (layVwEdDbMgr.retrieveCurrentA() < 0) {
                        layVwEdDbMgr.updateCurrentAPlus(layVwEdDbMgr.depToMainAcctDetAPortion1(layVwEdDbMgr.retrieveCurrentA(), bInDiff), layVwEdDbMgr.retrieveCurrentA());
                        newLayVwEdMoneyInA = aInDiff + layVwEdDbMgr.depToMainAcctDetAPortion1(layVwEdDbMgr.retrieveCurrentA(), bInDiff);
                        newLayVwEdMoneyInB = layVwEdDbMgr.depToMainAcctDetBPortion1(layVwEdDbMgr.retrieveCurrentA(), bInDiff);
                    } else {
                        newLayVwEdMoneyInA = aInDiff;
                        newLayVwEdMoneyInB = bInDiff;
                    }
                    //IF B STILL HAS MONEY AND IF AMT OWING TO A, DETERMINE NEW OWING AND B PORTIONS
                    if (newLayVwEdMoneyInB != 0 && layVwEdDbMgr.retrieveCurrentOwingA() > 0) {
                        layVwEdMoneyInOwing = layVwEdDbMgr.depToMainAcctDetOwingPortion1(newLayVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentOwingA());
                        newNewLayVwEdMoneyInB = layVwEdDbMgr.depToMainAcctDetBPortion2(newLayVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentOwingA());
                    } else {
                        layVwEdMoneyInOwing = 0.0;
                        newNewLayVwEdMoneyInB = newLayVwEdMoneyInB;
                    }
                    //ADJUST CURRENT A, OWING, AND B FOR FINAL TIME
                    if (layVwEdMoneyInOwing != 0) {
                        layVwEdDbMgr.updateCurrentAPlus(layVwEdMoneyInOwing, layVwEdDbMgr.retrieveCurrentA());
                        layVwEdDbMgr.updateCurrentOwingMinus(layVwEdMoneyInOwing, layVwEdDbMgr.retrieveCurrentOwingA());
                    }
                    if (newNewLayVwEdMoneyInB != 0) {
                        layVwEdDbMgr.updateCurrentBPlus(newNewLayVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentB());
                    }
                    //IF OWING A IS NEGATIVE, THEN A ACTUALLY OWES TO B
                    if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
                        layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());

                        newMoneyA = layVwEdDbMgr.detNewAPortion(newLayVwEdMoneyInA, layVwEdDbMgr.retrieveCurrentOwingA());
                        newMoneyOwing = layVwEdDbMgr.detNewOwingPortion(layVwEdMoneyInOwing, layVwEdDbMgr.retrieveCurrentOwingA());
                        newMoneyB = layVwEdDbMgr.detNewBPortion(newNewLayVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentOwingA());

                        layVwEdFinMoneyInA = newMoneyA + layVwEdTransAmtInA;
                        layVwEdFinMoneyInOwing = newMoneyOwing + layVwEdTransAmtInOwing;
                        layVwEdFinMoneyInB = newMoneyB + layVwEdTransAmtInB;
                    } else {
                        layVwEdFinMoneyInA = newLayVwEdMoneyInA + layVwEdTransAmtInA;
                        layVwEdFinMoneyInOwing = layVwEdMoneyInOwing + layVwEdTransAmtInOwing;
                        layVwEdFinMoneyInB = newNewLayVwEdMoneyInB + layVwEdTransAmtInB;
                    }
                }
            }
        }
        layVwEdFinishIn();

        for (BudgetDb b : layVwEdDbMgr.getBudget()) {
            if (editBdgtId == b.getId()) {
                if (b.getBdgtExpInc().equals("I")) {
                    if (layVwEdDbMgr.getMoneyIns().size() != 0) {
                        newAnnAmt = layVwEdDbMgr.makeNewIncAnnAmt(editBdgtId, layVwEdGen.lastNumOfDays(365));
                        b.setBdgtAnnPayt(newAnnAmt);
                    } else {
                        b.setBdgtAnnPayt(b.getBdgtPaytAmt() * b.getBdgtPaytFrq());
                    }
                } else if (b.getBdgtExpInc().equals("E")) {
                    if (layVwEdDbMgr.getMoneyOuts().size() != 0) {
                        newAnnAmt = layVwEdDbMgr.makeNewExpAnnAmt(editBdgtId, layVwEdGen.lastNumOfDays(365));
                        b.setBdgtAnnPayt(newAnnAmt);
                    } else {
                        b.setBdgtAnnPayt(b.getBdgtPaytAmt() * b.getBdgtPaytFrq());
                    }
                }
                layVwEdDbMgr.updateBudget(b);
            }
        }
        layVwEdRefresh();
    }

    View.OnClickListener onClickNoContBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdRefresh();
        }
    };

    View.OnClickListener onClickYesContBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishTheEdit();
        }
    };

    public class LayVwEdAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> transactionsList;

        private LayVwEdAdapter(
                Context context,
                List<TransactionsDb> transactionsList) {

            super(context, -1, transactionsList);

            this.context = context;
            this.transactionsList = transactionsList;
        }

        public void updateTransactions(List<TransactionsDb> transactionsList) {
            this.transactionsList = transactionsList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transactionsList.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final LayVwEdViewHolder layVwEdHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                layVwEdHldr = new LayVwEdViewHolder();
                layVwEdHldr.layVwEdEditBtn = convertView.findViewById(R.id.list5EditBtn);
                layVwEdHldr.layVwEdDelBtn = convertView.findViewById(R.id.list5DelBtn);
                layVwEdHldr.layVwEdDateTV = convertView.findViewById(R.id.list5DateTV);
                layVwEdHldr.layVwEdAmtTV = convertView.findViewById(R.id.list5AmtTV);
                layVwEdHldr.layVwEdCatTV = convertView.findViewById(R.id.list5CatTV);
                layVwEdHldr.layVwEdAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                layVwEdHldr.layVwEdAcctTV = convertView.findViewById(R.id.list5CCTV);
                layVwEdHldr.layVwEdAcctLabelTV2 = convertView.findViewById(R.id.list5CCLabel2);
                layVwEdHldr.layVwEdAcctTV2 = convertView.findViewById(R.id.list5CCTV2);

                convertView.setTag(layVwEdHldr);

            } else {
                layVwEdHldr = (LayVwEdViewHolder) convertView.getTag();
            }

            layVwEdHldr.layVwEdDateTV.setText(transactionsList.get(position).getTransCreatedOn());

            layVwEdTransAmt = transactionsList.get(position).getTransAmt();
            layVwEdGen.dblASCurrency(String.valueOf(layVwEdTransAmt), layVwEdHldr.layVwEdAmtTV);

            if (transactionsList.get(position).getTransType().equals("in")) { //IF TRANSACTION IS A DEPOSIT
                layVwEdHldr.layVwEdEditBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdDelBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdCatTV.setText(transactionsList.get(position).getTransBdgtCat());
                layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.deposited_to);
                layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransToAcctName());
                layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.GONE);
                layVwEdHldr.layVwEdAcctTV2.setVisibility(View.GONE);

            } else if (transactionsList.get(position).getTransType().equals("out")) { //IF TRANSACTION IS A PURCHASE OF SOME KIND
                layVwEdHldr.layVwEdEditBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdDelBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdCatTV.setText(transactionsList.get(position).getTransBdgtCat());
                if (transactionsList.get(position).getTransIsCC().equals("Y")) { //IF PURCHASE IS ON A CC
                    if (transactionsList.get(position).getTransAmt() < 0) { //IF IS A CREDIT INSTEAD OF A PURCHASE
                        layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.credited_to);
                        layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransFromAcctName());
                        layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.GONE);
                        layVwEdHldr.layVwEdAcctTV2.setVisibility(View.GONE);
                    } else { //IF IS A PURCHASE
                        layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.on);
                        layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransFromAcctName());
                        layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.GONE);
                        layVwEdHldr.layVwEdAcctTV2.setVisibility(View.GONE);
                    }
                } else { //IF PURCHASE IS A CASH OR DEBIT TRANSACTION
                    layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.paid_from);
                    layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransFromAcctName());
                    layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.GONE);
                    layVwEdHldr.layVwEdAcctTV2.setVisibility(View.GONE);
                }

            } else if (transactionsList.get(position).getTransType().equals("transfer")) { //IF TRANSACTION IS A TRANSFER OF FUNDS
                layVwEdHldr.layVwEdEditBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdDelBtn.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdCatTV.setText(R.string.transfer);
                layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.from);
                layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransFromAcctName());
                layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdAcctLabelTV2.setText(R.string.to);
                layVwEdHldr.layVwEdAcctTV2.setVisibility(View.VISIBLE);
                layVwEdHldr.layVwEdAcctTV2.setText(transactionsList.get(position).getTransToAcctName());

            } else if (transactionsList.get(position).getTransType().equals("ccPayment")) { //IF TRANSACTION IS A CC PAYT
                layVwEdHldr.layVwEdEditBtn.setVisibility(View.GONE);
                layVwEdHldr.layVwEdDelBtn.setVisibility(View.GONE);
                layVwEdHldr.layVwEdCatTV.setText(R.string.credit_card_payt);
                layVwEdHldr.layVwEdAcctLabelTV.setText(R.string.paid_from);
                layVwEdHldr.layVwEdAcctTV.setText(transactionsList.get(position).getTransFromAcctName());
                layVwEdHldr.layVwEdAcctLabelTV2.setVisibility(View.GONE);
                layVwEdHldr.layVwEdAcctTV2.setVisibility(View.GONE);

            }

            layVwEdHldr.layVwEdEditBtn.setTag(transactionsList.get(position));
            layVwEdHldr.layVwEdDelBtn.setTag(transactionsList.get(position));

            layVwEdTransBdgtId = transactionsList.get(position).getTransBdgtId();
            layVwEdToIsDebtSav = transactionsList.get(position).getTransToDebtSav();//"D" or "S" or "N/A"
            layVwEdFromIsDebtSav = transactionsList.get(position).getTransFromDebtSav();//"D" or "S" or "N/A"
            layVwEdToAcctId = transactionsList.get(position).getTransToAcctId();
            layVwEdFromAcctId = transactionsList.get(position).getTransFromAcctId();
            layVwEdTransType = transactionsList.get(position).getTransType();
            layVwEdPriority = transactionsList.get(position).getTransBdgtPriority();
            layVwEdTransAmtOutA = transactionsList.get(position).getTransAmtOutA();
            layVwEdTransAmtOutOwing = transactionsList.get(position).getTransAmtOutOwing();
            layVwEdTransAmtOutB = transactionsList.get(position).getTransAmtOutB();
            layVwEdTransAmtInA = transactionsList.get(position).getTransAmtInA();
            layVwEdTransAmtInOwing = transactionsList.get(position).getTransAmtInOwing();
            layVwEdTransAmtInB = transactionsList.get(position).getTransAmtInB();

            //click on pencil icon
            layVwEdHldr.layVwEdEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layVwEdTransDb = (TransactionsDb) layVwEdHldr.layVwEdEditBtn.getTag();
                    LayoutViewEdit.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    layVwEdDbMgr = new DbManager(getContext());

                    layVwEdPriority = transactionsList.get(position).getTransBdgtPriority();
                    layVwEdToIsDebtSav = transactionsList.get(position).getTransToDebtSav();
                    layVwEdFromIsDebtSav = transactionsList.get(position).getTransFromDebtSav();
                    layVwEdToAcctId = transactionsList.get(position).getTransToAcctId();
                    layVwEdFromAcctId = transactionsList.get(position).getTransFromAcctId();
                    layVwEdTransAmt = transactionsList.get(position).getTransAmt();
                    layVwEdTransAmtInA = transactionsList.get(position).getTransAmtInA();
                    layVwEdTransAmtInOwing = transactionsList.get(position).getTransAmtInOwing();
                    layVwEdTransAmtInB = transactionsList.get(position).getTransAmtInB();
                    layVwEdTransAmtOutA = transactionsList.get(position).getTransAmtOutA();
                    layVwEdTransAmtOutOwing = transactionsList.get(position).getTransAmtOutOwing();
                    layVwEdTransAmtOutB = transactionsList.get(position).getTransAmtOutB();
                    aInPercent = layVwEdTransAmtInA / layVwEdTransAmt;
                    owingInPercent = layVwEdTransAmtInOwing / layVwEdTransAmt;
                    bInPercent = layVwEdTransAmtInB / layVwEdTransAmt;
                    aOutPercent = layVwEdTransAmtOutA / layVwEdTransAmt;
                    owingOutPercent = layVwEdTransAmtOutOwing / layVwEdTransAmt;
                    bOutPercent = layVwEdTransAmtOutB / layVwEdTransAmt;
                    layVwEdTransType = transactionsList.get(position).getTransType();
                    editBdgtId = transactionsList.get(position).getTransBdgtId();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutViewEdit.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_update, null);
                    builder.setView(dView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

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
                            layVwEdRefresh();
                        }
                    });
                    dialogUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            layVwEdAmtEntry = layVwEdGen.dblFromET(dialogAmtET);
                            layVwEdAmtDiff = layVwEdAmtEntry - layVwEdTransAmt;
                            aInDiff = layVwEdAmtDiff * aInPercent;
                            owingInDiff = layVwEdAmtDiff * owingInPercent;
                            bInDiff = layVwEdAmtDiff * bInPercent;
                            aOutDiff = layVwEdAmtDiff * aOutPercent;
                            owingOutDiff = layVwEdAmtDiff * owingOutPercent;
                            bOutDiff = layVwEdAmtDiff * bOutPercent;

                            for (AccountsDb a3 : layVwEdDbMgr.getAccounts()) {
                                if (layVwEdFromAcctId == a3.getId()) {
                                    if (a3.getAcctDebtSav().equals("D")) {
                                        debtLimitFromDb = a3.getAcctMax();
                                    }
                                }
                            }

                            if (layVwEdFromIsDebtSav.equals("S") && (layVwEdDbMgr.retrieveCurrentAcctAmt(layVwEdFromAcctId) - layVwEdAmtDiff < 0)) { //IF FROM SAV AND WILL GO NEGATIVE
                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                dialogWarnTV.setText(getString(R.string.not_enough_savings_warning));
                                dialogNoContBtn.setOnClickListener(onClickNoContBtn);
                                dialogYesContBtn.setOnClickListener(onClickYesContBtn);
                            } else if (layVwEdFromIsDebtSav.equals("D") && (layVwEdDbMgr.retrieveCurrentAcctAmt(layVwEdFromAcctId) + layVwEdAmtDiff > debtLimitFromDb)) { //IF FROM DEBT AND WILL GO OVER LIMIT
                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                dialogWarnTV.setText(getString(R.string.not_enough_credit_warning));
                                dialogNoContBtn.setOnClickListener(onClickNoContBtn);
                                dialogYesContBtn.setOnClickListener(onClickYesContBtn);
                            } else if (layVwEdPriority.equals("A") && (layVwEdDbMgr.retrieveCurrentAccountBalance() - layVwEdAmtDiff < 0)) { //IF FROM MAIN AND WILL GO NEGATIVE
                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                dialogWarnTV.setText(getString(R.string.payment_not_possible_A));
                                dialogNoContBtn.setOnClickListener(onClickNoContBtn);
                                dialogYesContBtn.setOnClickListener(onClickYesContBtn);
                            } else if (layVwEdPriority.equals("B") && (layVwEdDbMgr.retrieveCurrentB() - layVwEdAmtDiff < 0)) { //IF FROM MAIN AND WILL GO NEGATIVE
                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                dialogWarnTV.setText(getString(R.string.payment_not_possible_B));
                                dialogNoContBtn.setOnClickListener(onClickNoContBtn);
                                dialogYesContBtn.setOnClickListener(onClickYesContBtn);
                            } else if (layVwEdFromAcctId == 1 && layVwEdTransType.equals("transfer") && (layVwEdDbMgr.retrieveCurrentAccountBalance() - layVwEdAmtDiff < 0)) { //IF FROM MAIN AND WILL GO NEGATIVE
                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                dialogWarnTV.setText(getString(R.string.transfer_not_possible_A));
                                dialogNoContBtn.setOnClickListener(onClickNoContBtn);
                                dialogYesContBtn.setOnClickListener(onClickYesContBtn);
                            } else { //WILL NOT GO NEGATIVE OR OVER LIMIT
                                finishTheEdit();
                            }
                        }
                    });
                }
            });

            //click on trash can icon
            layVwEdHldr.layVwEdDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layVwEdTransDb = (TransactionsDb) layVwEdHldr.layVwEdDelBtn.getTag();

                    layVwEdTransAmt = transactionsList.get(position).getTransAmt();
                    layVwEdTransAmtInA = transactionsList.get(position).getTransAmtInA();
                    layVwEdTransAmtInOwing = transactionsList.get(position).getTransAmtInOwing();
                    layVwEdTransAmtInB = transactionsList.get(position).getTransAmtInB();
                    layVwEdTransAmtOutA = transactionsList.get(position).getTransAmtOutA();
                    layVwEdTransAmtOutOwing = transactionsList.get(position).getTransAmtOutOwing();
                    layVwEdTransAmtOutB = transactionsList.get(position).getTransAmtOutB();
                    layVwEdToIsDebtSav = transactionsList.get(position).getTransToDebtSav();
                    layVwEdFromIsDebtSav = transactionsList.get(position).getTransFromDebtSav();
                    layVwEdToAcctId = transactionsList.get(position).getTransToAcctId();
                    layVwEdFromAcctId = transactionsList.get(position).getTransFromAcctId();
                    delBdgtId = transactionsList.get(position).getTransBdgtId();

                    for (AccountsDb a : layVwEdDbMgr.getAccounts()) {
                        if (layVwEdToAcctId == a.getId()) {
                            if (layVwEdToIsDebtSav.equals("S")) { //TO SAV ACCT
                                layVwEdSavMinus(layVwEdTransAmt, layVwEdToAcctId);
                            } else if (layVwEdToIsDebtSav.equals("D")) { //TO DEBT ACCT
                                layVwEdDebtPlus(layVwEdTransAmt, layVwEdToAcctId);
                            } else if (layVwEdToAcctId == 1) { //TO MAIN ACCT
                                layVwEdDbMgr.updateTotAcctBalMinus(layVwEdTransAmt, layVwEdDbMgr.retrieveCurrentAccountBalance());

                                layVwEdDbMgr.updateAandBBalMinus(
                                        layVwEdTransAmtInA,
                                        layVwEdTransAmtInOwing,
                                        layVwEdTransAmtInB,
                                        layVwEdDbMgr.retrieveCurrentA(),
                                        layVwEdDbMgr.retrieveCurrentOwingA(),
                                        layVwEdDbMgr.retrieveCurrentB());
                                if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
                                    layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                                }
                            }
                        }
                    }

                    for (AccountsDb a2 : layVwEdDbMgr.getAccounts()) {
                        if (layVwEdFromAcctId == a2.getId()) {
                            if (layVwEdFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                                layVwEdSavPlus(layVwEdTransAmt, layVwEdFromAcctId);
                            } else if (layVwEdFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                layVwEdDebtMinus(layVwEdTransAmt, layVwEdFromAcctId);
                            } else if (layVwEdFromAcctId == 1) { //FROM MAIN ACCT
                                layVwEdDbMgr.updateTotAcctBalPlus(layVwEdTransAmt, layVwEdDbMgr.retrieveCurrentAccountBalance());

                                layVwEdDbMgr.updateAandBBalPlus(
                                        layVwEdTransAmtOutA,
                                        layVwEdTransAmtOutOwing,
                                        layVwEdTransAmtOutB,
                                        layVwEdDbMgr.retrieveCurrentA(),
                                        layVwEdDbMgr.retrieveCurrentOwingA(),
                                        layVwEdDbMgr.retrieveCurrentB());
                                if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
                                    layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
                                }
                            }
                        }
                    }

                    layVwEdDbMgr.deleteTransactions(layVwEdTransDb);
                    layVwEdAdapter.updateTransactions(layVwEdDbMgr.getTransactions());
                    notifyDataSetChanged();

                    for (BudgetDb b : layVwEdDbMgr.getBudget()) {
                        if (delBdgtId == b.getId()) {
                            if (b.getBdgtExpInc().equals("I")) {
                                if (layVwEdDbMgr.getMoneyIns().size() != 0) {
                                    newAnnAmt = layVwEdDbMgr.makeNewIncAnnAmt(delBdgtId, layVwEdGen.lastNumOfDays(365));
                                    b.setBdgtAnnPayt(newAnnAmt);
                                } else {
                                    b.setBdgtAnnPayt(b.getBdgtPaytAmt() * b.getBdgtPaytFrq());
                                }
                            } else if (b.getBdgtExpInc().equals("E")) {
                                if (layVwEdDbMgr.getMoneyOuts().size() != 0) {
                                    newAnnAmt = layVwEdDbMgr.makeNewExpAnnAmt(delBdgtId, layVwEdGen.lastNumOfDays(365));
                                    b.setBdgtAnnPayt(newAnnAmt);
                                } else {
                                    b.setBdgtAnnPayt(b.getBdgtPaytAmt() * b.getBdgtPaytFrq());
                                }
                            }
                            layVwEdDbMgr.updateBudget(b);
                        }
                    }

                    layVwEdRefresh();
                }
            });
            return convertView;
        }
    }

    private static class LayVwEdViewHolder {
        public TextView layVwEdCatTV;
        public TextView layVwEdAmtTV;
        public TextView layVwEdDateTV;
        public TextView layVwEdAcctLabelTV;
        public TextView layVwEdAcctTV;
        public TextView layVwEdAcctLabelTV2;
        public TextView layVwEdAcctTV2;
        public ImageButton layVwEdEditBtn;
        public ImageButton layVwEdDelBtn;
    }
}
