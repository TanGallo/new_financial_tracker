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

    ArrayAdapter layVwEdFromMthSpinAdapter, layVwEdToMthSpinAdapter, layVwEdFromYrSpinAdapter, layVwEdToYrSpinAdapter;
    boolean layVwEdLeapYear;
    Button dialogCancelBtn, dialogUpdateBtn, dialogNoContBtn, dialogYesContBtn, layVwEdAddMoreBtn, layVwEdDoneBtn, layVwEdSpinOkBtn, layVwEdSpinResetBtn;
    Calendar layVwEdCal;
    Date layVwEdEarliestDate, layVwEdLatestDate;
    DbManager layVwEdDbMgr;
    Double debtLimitFromDb = 0.0, debtOwingFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, layVwEdTransAmt = 0.0, layVwEdAmtEntry = 0.0, layVwEdAmtDiff = 0.0,
            layVwEdPercentA = 0.0, layVwEdMoneyInA = 0.0, layVwEdMoneyInOwing = 0.0, layVwEdMoneyInB = 0.0, layVwEdMoneyOutA = 0.0, layVwEdMoneyOutOwing = 0.0,
            layVwEdMoneyOutB = 0.0, layVwEdTransAmtOutA = 0.0, layVwEdTransAmtOutOwing = 0.0, layVwEdTransAmtOutB = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0, moneyOutA = 0.0,
            layVwEdTransAmtInA = 0.0, layVwEdTransAmtInOwing = 0.0, layVwEdTransAmtInB = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, newAnnAmt = 0.0, newMoneyOutA = 0.0,
            newMoneyOutOwing = 0.0, newMoneyOutB = 0.0, newMoneyInA = 0.0, newMoneyInOwing = 0.0, newMoneyInB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0,
            savRateFromDb = 0.0;
    EditText dialogAmtET;
    General layVwEdGen;
    int layVwEdYear;
    Intent layVwEdRefresh;
    LayVwEdAdapter layVwEdAdapter;
    LinearLayout dialogWarnLayout, layVwEdSpinLayout;
    ListView layVwEdList;
    long delBdgtId, editBdgtId, layVwEdTransBdgtId, layVwEdToAcctId, layVwEdFromAcctId;
    Spinner layVwEdToMthSpin, layVwEdFromMthSpin, layVwEdToYrSpin, layVwEdFromYrSpin;
    String layVwEdPriority = null, layVwEdSumTotalSelTrans = null, layVwEdTransType = null, layVwEdLastDay = null, layVwEdFromMonth = null, layVwEdToMonth = null, layVwEdFromYr = null, layVwEdToYr = null,
            layVwEdToIsDebtSav = null, layVwEdFromIsDebtSav = null;
    String[] layVwEdMonths, layVwEdOnlyMonths, layVwEdYears;
    TextView dialogWarnTV, layVwEdAndTV, layVwEdTitle, layVwEdTotalTV;
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

    public void layVwEdMainAcctPlus(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = transAmtInA
        //dbl2 = transAmtInOwing
        //dbl3 = transAmtInB
        layVwEdDbMgr.updateTotAcctBalPlus(layVwEdAmtDiff, layVwEdDbMgr.retrieveCurrentAccountBalance());
        layVwEdDbMgr.updateAandBBalMinus(
                dbl1,
                dbl2,
                dbl3,
                layVwEdDbMgr.retrieveCurrentA(),
                layVwEdDbMgr.retrieveCurrentOwingA(),
                layVwEdDbMgr.retrieveCurrentB());

        layVwEdMoneyInA = layVwEdDbMgr.detAPortionInc(layVwEdAmtEntry, (layVwEdPercentA * layVwEdAmtEntry), layVwEdDbMgr.retrieveCurrentOwingA());
        layVwEdMoneyInOwing = layVwEdDbMgr.detOwingPortionInc(layVwEdAmtEntry, (layVwEdPercentA * layVwEdAmtEntry), layVwEdDbMgr.retrieveCurrentOwingA());
        layVwEdMoneyInB = layVwEdDbMgr.detBPortionInc(layVwEdAmtEntry, (layVwEdPercentA * layVwEdAmtEntry), layVwEdDbMgr.retrieveCurrentOwingA());

        layVwEdDbMgr.updateAandBBalPlus(layVwEdMoneyInA, layVwEdMoneyInOwing, layVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentB());

        if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
            layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
            newMoneyInA = layVwEdDbMgr.detNewAPortion(layVwEdMoneyInA, layVwEdDbMgr.retrieveCurrentOwingA());
            newMoneyInOwing = layVwEdDbMgr.detNewOwingPortion(layVwEdMoneyInOwing, layVwEdDbMgr.retrieveCurrentOwingA());
            newMoneyInB = layVwEdDbMgr.detNewBPortion(layVwEdMoneyInB, layVwEdDbMgr.retrieveCurrentOwingA());
            moneyInA = newMoneyInA;
            moneyInOwing = newMoneyInOwing;
            moneyInB = newMoneyInB;
        } else {
            moneyInA = layVwEdMoneyInA;
            moneyInOwing = layVwEdMoneyInOwing;
            moneyInB = layVwEdMoneyInB;
        }
    }

    public void layVwEdMainAcctMinus(Double dbl1, Double dbl2, Double dbl3, String str1) {
        //dbl1 = transAmtOutA
        //dbl2 = transAmtOutOwing
        //dbl3 = transAmtOutB
        //str1 = priority
        layVwEdDbMgr.updateTotAcctBalMinus(layVwEdAmtDiff, layVwEdDbMgr.retrieveCurrentAccountBalance());
        layVwEdDbMgr.updateAandBBalPlus(dbl1, dbl2, dbl3, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentB());

        if (!str1.equals("A") && !str1.equals("B")) {
            if (layVwEdPercentA > 0) {
                str1 = "A";
            } else {
                str1 = "B";
            }
        }

        layVwEdMoneyOutA = layVwEdDbMgr.detAPortionExp(layVwEdAmtEntry, str1, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
        layVwEdMoneyOutOwing = layVwEdDbMgr.detOwingPortionExp(layVwEdAmtEntry, str1, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
        layVwEdMoneyOutB = layVwEdDbMgr.detBPortionExp(layVwEdAmtEntry, str1, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());

        layVwEdDbMgr.updateAandBBalMinus(layVwEdMoneyOutA, layVwEdMoneyOutOwing, layVwEdMoneyOutB, layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentB());

        if (layVwEdDbMgr.retrieveCurrentOwingA() < 0) {
            layVwEdDbMgr.adjustCurrentAandB(layVwEdDbMgr.retrieveCurrentOwingA(), layVwEdDbMgr.retrieveCurrentA(), layVwEdDbMgr.retrieveCurrentB());
            newMoneyOutA = layVwEdDbMgr.detNewAPortion(layVwEdMoneyOutA, layVwEdDbMgr.retrieveCurrentOwingA());
            newMoneyOutOwing = layVwEdDbMgr.detNewOwingPortion(layVwEdMoneyOutOwing, layVwEdDbMgr.retrieveCurrentOwingA());
            newMoneyOutB = layVwEdDbMgr.detNewBPortion(layVwEdMoneyOutB, layVwEdDbMgr.retrieveCurrentOwingA());
            moneyOutA = newMoneyOutA;
            moneyOutOwing = newMoneyOutOwing;
            moneyOutB = newMoneyOutB;
        } else {
            moneyOutA = layVwEdMoneyOutA;
            moneyOutOwing = layVwEdMoneyOutOwing;
            moneyOutB = layVwEdMoneyOutB;
        }
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
        layVwEdTransDb.setTransAmtInA(moneyInA);
        layVwEdTransDb.setTransAmtInOwing(moneyInOwing);
        layVwEdTransDb.setTransAmtInB(moneyInB);
        layVwEdDbMgr.updateTransactions(layVwEdTransDb);

        layVwEdAdapter.updateTransactions(layVwEdDbMgr.getTransactions());
        layVwEdAdapter.notifyDataSetChanged();
    }

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
                            layVwEdAmtDiff = layVwEdAmtEntry - transactionsList.get(position).getTransAmt();
                            layVwEdPercentA = layVwEdDbMgr.sumTotalAExpenses() / layVwEdDbMgr.sumTotalIncome();
                            layVwEdPriority = transactionsList.get(position).getTransBdgtPriority();
                            layVwEdToIsDebtSav = transactionsList.get(position).getTransToDebtSav();
                            layVwEdFromIsDebtSav = transactionsList.get(position).getTransFromDebtSav();
                            layVwEdToAcctId = transactionsList.get(position).getTransToAcctId();
                            layVwEdFromAcctId = transactionsList.get(position).getTransFromAcctId();
                            editBdgtId = transactionsList.get(position).getTransBdgtId();

                            if (layVwEdToAcctId != 0) { //THERE IS A "TO" ACCT
                                for (AccountsDb a : layVwEdDbMgr.getAccounts()) {
                                    if (layVwEdToAcctId == a.getId()) {
                                        if (layVwEdToIsDebtSav.equals("S")) { //TO SAV ACCT
                                            moneyInA = 0.0;
                                            moneyInOwing = 0.0;
                                            moneyInB = 0.0;

                                            layVwEdSavPlus(layVwEdAmtDiff, layVwEdToAcctId);
                                        } else if (layVwEdToIsDebtSav.equals("D")) { //TO DEBT ACCT
                                            moneyInA = 0.0;
                                            moneyInOwing = 0.0;
                                            moneyInB = 0.0;

                                            layVwEdDebtMinus(layVwEdAmtDiff, layVwEdToAcctId);
                                        } else { //TO MAIN ACCT
                                            layVwEdMainAcctPlus(layVwEdTransAmtInA, layVwEdTransAmtInOwing, layVwEdTransAmtInB);
                                        }
                                    }
                                }

                                layVwEdFinishIn();
                            }

                            if (layVwEdFromAcctId != 0) { //THERE IS A "FROM" ACCT
                                for (AccountsDb a2 : layVwEdDbMgr.getAccounts()) {
                                    if (layVwEdFromAcctId == a2.getId()) {
                                        debtLimitFromDb = a2.getAcctMax();

                                        if (layVwEdFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                                            moneyOutA = 0.0;
                                            moneyOutOwing = 0.0;
                                            moneyOutB = 0.0;

                                            if (a2.getAcctBal() - layVwEdAmtDiff < 0) { //IF SAV WILL GO NEGATIVE
                                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                                dialogWarnTV.setText(getString(R.string.not_enough_savings_warning));

                                                dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layVwEdRefresh();
                                                    }
                                                });

                                                dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogWarnLayout.setVisibility(View.GONE);
                                                        layVwEdSavMinus(layVwEdAmtDiff, layVwEdFromAcctId);
                                                        layVwEdFinishOut();
                                                    }
                                                });
                                            } else { //IF SAV WILL NOT GO NEGATIVE
                                                layVwEdSavMinus(layVwEdAmtDiff, layVwEdFromAcctId);
                                                layVwEdFinishOut();
                                            }
                                        } else if (layVwEdFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                            moneyOutA = 0.0;
                                            moneyOutOwing = 0.0;
                                            moneyOutB = 0.0;

                                            if (a2.getAcctBal() + layVwEdAmtDiff > debtLimitFromDb) { //IF ACCT WILL BE OVER LIMIT
                                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                                dialogWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                                dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layVwEdRefresh();
                                                    }
                                                });

                                                dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogWarnLayout.setVisibility(View.GONE);
                                                        layVwEdDebtPlus(layVwEdAmtDiff, layVwEdFromAcctId);
                                                        layVwEdFinishOut();
                                                    }
                                                });
                                            } else { //ACCT WILL NOT BE OVER LIMIT
                                                layVwEdDebtPlus(layVwEdAmtDiff, layVwEdFromAcctId);
                                                layVwEdFinishOut();
                                            }
                                        } else { //FROM MAIN ACCT
                                            if (layVwEdPriority.equals("A") && (a2.getAcctBal() - layVwEdAmtDiff < 0)) { //IF A AND WILL GO NEGATIVE
                                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                                dialogWarnTV.setText(getString(R.string.payment_not_possible_A));

                                                dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layVwEdRefresh();
                                                    }
                                                });

                                                dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogWarnLayout.setVisibility(View.GONE);
                                                        layVwEdMainAcctMinus(layVwEdTransAmtOutA, layVwEdTransAmtOutOwing, layVwEdTransAmtOutB, layVwEdPriority);
                                                        layVwEdFinishOut();
                                                    }
                                                });
                                            } else if (layVwEdPriority.equals("B") && (layVwEdDbMgr.retrieveCurrentB() - layVwEdAmtDiff < 0)) { //IF B AND WILL GO NEGATIVE
                                                dialogWarnLayout.setVisibility(View.VISIBLE);
                                                dialogWarnTV.setText(getString(R.string.payment_not_possible_B));

                                                dialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layVwEdRefresh();
                                                    }
                                                });

                                                dialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogWarnLayout.setVisibility(View.GONE);
                                                        layVwEdMainAcctMinus(layVwEdTransAmtOutA, layVwEdTransAmtOutOwing, layVwEdTransAmtOutB, layVwEdPriority);
                                                        layVwEdFinishOut();
                                                    }
                                                });
                                            } else { //WILL NOT GO NEGATIVE
                                                layVwEdMainAcctMinus(layVwEdTransAmtOutA, layVwEdTransAmtOutOwing, layVwEdTransAmtOutB, layVwEdPriority);
                                                layVwEdFinishOut();
                                            }
                                        }
                                    }
                                }
                            }

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

                    if (layVwEdToAcctId != 0) { //THERE IS A "TO" ACCT
                        for (AccountsDb a : layVwEdDbMgr.getAccounts()) {
                            if (layVwEdToAcctId == a.getId()) {
                                if (layVwEdToIsDebtSav.equals("S")) {
                                    layVwEdSavMinus(layVwEdTransAmt, layVwEdToAcctId);
                                } else if (layVwEdToIsDebtSav.equals("D")) {
                                    layVwEdDebtPlus(layVwEdTransAmt, layVwEdToAcctId);
                                } else { //TO MAIN ACCT
                                    a.setAcctBal(a.getAcctBal() - layVwEdTransAmt);
                                    layVwEdDbMgr.updateAccounts(a);

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
                    }

                    if (layVwEdFromAcctId != 0) { //THERE IS A "FROM" ACCT
                        for (AccountsDb a2 : layVwEdDbMgr.getAccounts()) {
                            if (layVwEdFromAcctId == a2.getId()) {
                                if (layVwEdFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                                    layVwEdSavPlus(layVwEdTransAmt, layVwEdFromAcctId);
                                } else if (layVwEdFromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                    layVwEdDebtMinus(layVwEdTransAmt, layVwEdFromAcctId);
                                } else if (a2.getId() == 1) { //FROM MAIN ACCT
                                    a2.setAcctBal(a2.getAcctBal() + layVwEdTransAmt);
                                    layVwEdDbMgr.updateAccounts(a2);

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
