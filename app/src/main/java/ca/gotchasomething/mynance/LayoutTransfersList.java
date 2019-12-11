package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class LayoutTransfersList extends MainNavigation {

    ArrayAdapter trn2FromMthSpinAdapter, trn2ToMthSpinAdapter, trn2FromYrSpinAdapter, trn2ToYrSpinAdapter;
    boolean trn2LeapYear;
    Button trn2AddMoreBtn, trn2DoneBtn, trn2SpinOkBtn, trn2SpinResetBtn, updateDialogCancelBtn, updateDialogNoContBtn, updateDialogUpdateBtn, updateDialogYesContBtn;
    Calendar trn2Cal;
    Date trn2EarliestDate, trn2LatestDate;
    DbManager trn2DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtOwingFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0,
            moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0, trn2Amt = 0.0,
            trn2TrnAmtDiff = 0.0, trn2AmtEntry = 0.0, trn2TrnAmtInA = 0.0, trn2TrnAmtInOwing = 0.0, trn2TrnAmtInB = 0.0, trn2TrnAmtOutA = 0.0, trn2TrnAmtOutB = 0.0,
            trn2TrnAmtOutOwing = 0.0, trn2FromAcctMax = 0.0, trn2MoneyInA = 0.0, trn2MoneyInAPercent = 0.0, trn2MoneyInB = 0.0, trn2MoneyInOwing = 0.0,
            trn2MoneyOutAPercent = 0.0, trn2NewMoneyInA = 0.0, trn2NewMoneyInB = 0.0, trn2NewMoneyInOwing = 0.0, trn2NewMoneyOutA = 0.0,
            trn2NewMoneyOutOwing = 0.0, trn2NewMoneyOutB = 0.0, trn2MoneyOutA = 0.0, trn2MoneyOutOwing = 0.0, trn2MoneyOutB = 0.0;
    General trn2Gen;
    EditText updateDialogAmtET;
    int trn2Year;
    Intent trn2ToMain, trn2ToAddMonIn, trn2Refresh;
    LinearLayout trn2SpinLayout, updateDialogWarnLayout;
    ListView trn2List;
    long trn2FromId, trn2ToId;
    Spinner trn2ToMthSpin, trn2FromMthSpin, trn2ToYrSpin, trn2FromYrSpin;
    String trn2FromDebtSav = null, trn2FromMonth = null, trn2FromYr = null, trn2ToMonth = null, trn2ToYr = null,
            trn2MoneyOutPriority = null, trn2ToDebtSav = null, trn2LastDay = null, trn2SumTotalSelTrans = null;
    String[] trn2Months, trn2OnlyMonths, trn2Years;
    TransactionsDb trn2TransDb;
    Trn2Adapter trn2Adapter;
    TextView trn2AndTV, trn2Title, trn2TotalTV, updateDialogWarnTV;
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

        trn2DbMgr = new DbManager(this);
        trn2Gen = new General();

        trn2SpinLayout = findViewById(R.id.layout1SpinLayout);
        trn2SpinLayout.setVisibility(View.GONE);
        /*trn2ToMthSpin = findViewById(R.id.layout1ToMthSpin);
        trn2FromMthSpin = findViewById(R.id.layout1FromMthSpin);
        trn2ToYrSpin = findViewById(R.id.layout1ToYrSpin);
        trn2FromYrSpin = findViewById(R.id.layout1FromYrSpin);
        trn2AndTV = findViewById(R.id.layout1AndTV);
        trn2SpinOkBtn = findViewById(R.id.layout1SpinOkBtn);
        trn2SpinResetBtn = findViewById(R.id.layout1SpinResetBtn);*/
        trn2Title = findViewById(R.id.layout1HeaderLabelTV);
        trn2Title.setText(getString(R.string.transfers));
        trn2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        trn2AddMoreBtn.setText(getString(R.string.record_transfers));
        trn2DoneBtn = findViewById(R.id.layout1DoneBtn);
        trn2DoneBtn.setOnClickListener(onClickTrn2DoneBtn);
        trn2TotalTV = findViewById(R.id.layout1TotalTV);
        trn2TotalTV.setVisibility(View.GONE);
        trn2List = findViewById(R.id.layout1ListView);

        trn2AddMoreBtn.setOnClickListener(onClickTrn2AddMoreBtn);

        trn2Adapter = new Trn2Adapter(this, trn2DbMgr.getTransfers());
        trn2List.setAdapter(trn2Adapter);

        /*if (trn2DbMgr.retrieveLastPageId() == 10) {
            trn2SpinLayout.setVisibility(View.VISIBLE);
            trn2SpinResetBtn.setVisibility(View.GONE);
            trn2Months = new String[]{
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
            trn2OnlyMonths = new String[]{
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
            trn2Years = trn2Gen.yearsList(trn2DbMgr.getEarliestEntry(trn2DbMgr.getYearsList()), trn2DbMgr.getLatestEntry(trn2DbMgr.getYearsList()));

            trn2FromMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, trn2Months);
            trn2FromMthSpin.setAdapter(trn2FromMthSpinAdapter);
            trn2FromMthSpin.setOnItemSelectedListener(trn2OnFromMonthSelected);

            trn2FromYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, trn2Years);
            trn2FromYrSpin.setAdapter(trn2FromYrSpinAdapter);
            trn2FromYrSpin.setOnItemSelectedListener(trn2OnFromYearSelected);

            trn2ToMthSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, trn2OnlyMonths);
            trn2ToMthSpin.setAdapter(trn2ToMthSpinAdapter);
            trn2ToMthSpin.setOnItemSelectedListener(trn2OnToMonthSelected);

            trn2ToYrSpinAdapter = new ArrayAdapter(this, R.layout.layout_5_spinner, R.id.spinner5Text, trn2Years);
            trn2ToYrSpin.setAdapter(trn2ToYrSpinAdapter);
            trn2ToYrSpin.setOnItemSelectedListener(trn2OnToYearSelected);

            trn2SpinOkBtn.setOnClickListener(onClickSpinOkBtn);
            trn2SpinResetBtn.setOnClickListener(onClickSpinResetBtn);

            trn2AddMoreBtn.setVisibility(View.GONE);
            trn2DoneBtn.setVisibility(View.GONE);
        } else {
            trn2SpinLayout.setVisibility(View.GONE);
            trn2AddMoreBtn.setVisibility(View.VISIBLE);
            trn2AddMoreBtn.setText(getString(R.string.record_transfers));
            trn2AddMoreBtn.setOnClickListener(onClickTrn2AddMoreBtn);
            trn2DoneBtn.setVisibility(View.VISIBLE);
        }*/
    }

    public void trn2Refresh() {
        trn2Refresh = new Intent(LayoutTransfersList.this, LayoutTransfersList.class);
        trn2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(trn2Refresh);
    }

    View.OnClickListener onClickTrn2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToMain = new Intent(LayoutTransfersList.this, MainActivity.class);
            trn2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToMain);
        }
    };

    View.OnClickListener onClickTrn2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToAddMonIn = new Intent(LayoutTransfersList.this, LayoutTransfers.class);
            trn2ToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToAddMonIn);
        }
    };

    /*Spinner.OnItemSelectedListener trn2OnFromMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 12) {
                trn2FromMonth = getString(R.string.year_to_date);
                trn2FromYrSpin.setVisibility(View.GONE);
                trn2ToMthSpin.setVisibility(View.GONE);
                trn2ToYrSpin.setVisibility(View.GONE);
                trn2AndTV.setVisibility(View.GONE);
            } else {
                trn2FromMonth = String.valueOf(trn2FromMthSpin.getSelectedItem());
                trn2FromYrSpin.setVisibility(View.VISIBLE);
                trn2ToMthSpin.setVisibility(View.VISIBLE);
                trn2ToYrSpin.setVisibility(View.VISIBLE);
                trn2AndTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener trn2OnFromYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn2FromYr = String.valueOf(trn2FromYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener trn2OnToMonthSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn2ToMonth = String.valueOf(trn2ToMthSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    Spinner.OnItemSelectedListener trn2OnToYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn2ToYr = String.valueOf(trn2ToYrSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickSpinOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (trn2FromMonth.equals(getString(R.string.year_to_date))) {
                trn2Cal = Calendar.getInstance();
                trn2Year = trn2Cal.get(Calendar.YEAR);
                trn2EarliestDate = trn2Gen.dateFromString("1-" + getString(R.string.jan) + "-" + trn2Year);
                trn2LatestDate = trn2Gen.dateFromString("31-" + getString(R.string.dec) + "-" + trn2Year);
            } else {
                trn2EarliestDate = trn2Gen.dateFromString("1-" + trn2FromMonth + "-" + trn2FromYr);
                trn2LastDay = null;
                if (trn2ToMonth.equals(getString(R.string.jan)) ||
                        trn2ToMonth.equals(getString(R.string.mar)) ||
                        trn2ToMonth.equals(getString(R.string.may)) ||
                        trn2ToMonth.equals(getString(R.string.jul)) ||
                        trn2ToMonth.equals(getString(R.string.aug)) ||
                        trn2ToMonth.equals(getString(R.string.oct)) ||
                        trn2ToMonth.equals(getString(R.string.dec))) {
                    trn2LastDay = "31";
                } else if (trn2ToMonth.equals(getString(R.string.apr)) ||
                        trn2ToMonth.equals(getString(R.string.jun)) ||
                        trn2ToMonth.equals(getString(R.string.sep)) ||
                        trn2ToMonth.equals(getString(R.string.nov))) {
                    trn2LastDay = "30";
                } else if (trn2ToMonth.equals(getString(R.string.feb))) {
                    trn2LeapYear = trn2Gen.checkForLeapYear(Integer.valueOf(trn2ToYr));
                    if (trn2LeapYear) {
                        trn2LastDay = "29";
                    } else {
                        trn2LastDay = "28";
                    }
                }
                trn2LatestDate = trn2Gen.dateFromString(trn2LastDay + "-" + trn2ToMonth + "-" + trn2ToYr);
            }
            try {
                trn2Adapter = new Trn2Adapter(getApplicationContext(), trn2DbMgr.getTransactionsInRange(trn2DbMgr.getTransfers(), trn2EarliestDate, trn2LatestDate));
                trn2List.setAdapter(trn2Adapter);
                trn2SpinOkBtn.setVisibility(View.GONE);
                trn2SpinResetBtn.setVisibility(View.VISIBLE);
                trn2SumTotalSelTrans = String.valueOf(trn2DbMgr.sumSelectedTransactions(trn2DbMgr.getTransactionsInRange(trn2DbMgr.getTransfers(), trn2EarliestDate, trn2LatestDate)));
                if (trn2LatestDate.before(trn2EarliestDate)) {
                    trn2TotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.date_confusion, Toast.LENGTH_LONG).show();
                } else if (trn2List.getCount() == 0) {
                    trn2TotalTV.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.no_entries, Toast.LENGTH_LONG).show();
                } else {
                    trn2TotalTV.setVisibility(View.VISIBLE);
                    trn2Gen.dblASCurrency(trn2SumTotalSelTrans, trn2TotalTV);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onClickSpinResetBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2Refresh();
        }
    };*/

    /*public void revAndAdjTransIn() {
        trn2DbMgr.updateTotAcctBalPlus(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentAccountBalance());
        trn2DbMgr.updateAandBBalMinus(trn2TrnAmtInA, trn2TrnAmtInOwing, trn2TrnAmtInB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        trn2MoneyInA = trn2MoneyInAPercent * trn2AmtEntry;
        trn2MoneyInOwing = trn2DbMgr.detOwingPortionInc(trn2AmtEntry, trn2MoneyInA, trn2DbMgr.retrieveCurrentOwingA());
        trn2MoneyInB = trn2AmtEntry - trn2MoneyInA;

        trn2DbMgr.updateAandBBalPlus(trn2MoneyInA, trn2MoneyInOwing, trn2MoneyInB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
            trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());

            trn2NewMoneyInA = trn2DbMgr.detNewAPortion(trn2MoneyInA, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyInOwing = trn2DbMgr.detNewOwingPortion(trn2MoneyInOwing, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyInB = trn2DbMgr.detNewBPortion(trn2MoneyInB, trn2DbMgr.retrieveCurrentOwingA());

            moneyInA = trn2NewMoneyInA;
            moneyInOwing = trn2NewMoneyInOwing;
            moneyInB = trn2NewMoneyInB;
        } else {
            moneyInA = trn2MoneyInA;
            moneyInOwing = trn2MoneyInOwing;
            moneyInB = trn2MoneyInB;
        }

        trn2TransDb.setTransAmtInA(moneyInA);
        trn2TransDb.setTransAmtInOwing(moneyInOwing);
        trn2TransDb.setTransAmtInB(moneyInB);
        trn2DbMgr.updateTransactions(trn2TransDb);
    }*/

    /*public void revAndAdjTransOut() {
        trn2DbMgr.updateTotAcctBalMinus(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentAccountBalance());
        trn2DbMgr.updateAandBBalPlus(
                trn2TrnAmtOutA,
                trn2TrnAmtOutOwing,
                trn2TrnAmtOutB,
                trn2DbMgr.retrieveCurrentA(),
                trn2DbMgr.retrieveCurrentOwingA(),
                trn2DbMgr.retrieveCurrentB());

        if (trn2MoneyOutAPercent > 0) {
            trn2MoneyOutPriority = "A";
        } else {
            trn2MoneyOutPriority = "B";
        }

        trn2MoneyOutA = trn2MoneyOutAPercent * trn2AmtEntry;
        trn2MoneyOutOwing = trn2DbMgr.detOwingPortionExp(trn2AmtEntry, trn2MoneyOutPriority, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
        trn2MoneyOutB = trn2AmtEntry - trn2MoneyOutA;

        trn2DbMgr.updateAandBBalMinus(trn2MoneyOutA, trn2MoneyOutOwing, trn2MoneyOutB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
            trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());

            trn2NewMoneyOutA = trn2DbMgr.detNewAPortion(trn2MoneyOutA, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyOutOwing = trn2DbMgr.detNewOwingPortion(trn2MoneyOutOwing, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyOutB = trn2DbMgr.detNewBPortion(trn2MoneyOutB, trn2DbMgr.retrieveCurrentOwingA());

            moneyOutA = trn2NewMoneyOutA;
            moneyOutOwing = trn2NewMoneyOutOwing;
            moneyOutB = trn2NewMoneyOutB;
        } else {
            moneyOutA = trn2MoneyOutA;
            moneyOutOwing = trn2MoneyOutOwing;
            moneyOutB = trn2MoneyOutB;
        }

        trn2TransDb.setTransAmtOutA(moneyOutA);
        trn2TransDb.setTransAmtOutOwing(moneyOutOwing);
        trn2TransDb.setTransAmtOutB(moneyOutB);
        trn2DbMgr.updateTransactions(trn2TransDb);
    }*/

    /*public void trn2DebtPlus(Double dbl1) {
        trn2DbMgr.updateRecPlusPt1(dbl1, trn2DbMgr.retrieveCurrentAcctAmt(trn2FromId), trn2FromId);
        for (AccountsDb d : trn2DbMgr.getDebts()) {
            if (d.getId() == trn2FromId) {
                debtOwingFromDb = d.getAcctBal();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }
        trn2DbMgr.updateRecPt2(
                trn2Gen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn2FromId);

    }

    public void trn2SavPlus(Double dbl4) {
        trn2DbMgr.updateRecPlusPt1(dbl4, trn2DbMgr.retrieveCurrentAcctAmt(trn2ToId), trn2ToId);
        for (AccountsDb s : trn2DbMgr.getSavings()) {
            if (s.getId() == trn2ToId) {
                savGoalFromDb = s.getAcctMax();
                savAmtFromDb = s.getAcctBal();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }
        trn2DbMgr.updateRecPt2(
                trn2Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn2ToId);
    }

    public void trn2DebtMinus(Double dbl3) {
        trn2DbMgr.updateRecMinusPt1(dbl3, trn2DbMgr.retrieveCurrentAcctAmt(trn2ToId), trn2ToId);
        for (AccountsDb d : trn2DbMgr.getDebts()) {
            if (d.getId() == trn2ToId) {
                debtOwingFromDb = d.getAcctBal();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }
        trn2DbMgr.updateRecPt2(
                trn2Gen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn2ToId);
    }

    public void trn2SavMinus(Double dbl2) {
        trn2DbMgr.updateRecMinusPt1(dbl2, trn2DbMgr.retrieveCurrentAcctAmt(trn2FromId), trn2FromId);
        for (AccountsDb s : trn2DbMgr.getSavings()) {
            if (s.getId() == trn2FromId) {
                savGoalFromDb = s.getAcctMax();
                savAmtFromDb = s.getAcctBal();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }
        trn2DbMgr.updateRecPt2(
                trn2Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn2FromId);
    }*/

    public class Trn2Adapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> transfers;

        private Trn2Adapter(
                Context context,
                List<TransactionsDb> transfers) {

            super(context, -1, transfers);

            this.context = context;
            this.transfers = transfers;
        }

        public void updateTransfers(List<TransactionsDb> transfers) {
            this.transfers = transfers;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transfers.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Trans2ViewHolder trn2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_8_5tv,
                        parent, false);

                trn2Hldr = new Trans2ViewHolder();
                trn2Hldr.trn2DateTV = convertView.findViewById(R.id.list8TV1);
                trn2Hldr.trn2AmtTV = convertView.findViewById(R.id.list8TV2);
                trn2Hldr.trn2FromNameTV = convertView.findViewById(R.id.list8TV4);
                trn2Hldr.trn2ToNameTV = convertView.findViewById(R.id.list8TV6);
                convertView.setTag(trn2Hldr);

            } else {
                trn2Hldr = (Trans2ViewHolder) convertView.getTag();
            }

            trn2Hldr.trn2DateTV.setText(transfers.get(position).getTransCreatedOn());
            trn2Amt = transfers.get(position).getTransAmt();
            trn2Gen.dblASCurrency(String.valueOf(trn2Amt), trn2Hldr.trn2AmtTV);
            trn2Hldr.trn2FromNameTV.setText(transfers.get(position).getTransFromAcctName());
            trn2Hldr.trn2ToNameTV.setText(transfers.get(position).getTransToAcctName());

            /*trn2TrnAmtOutA = transfers.get(position).getTransAmtOutA();
            trn2TrnAmtOutOwing = transfers.get(position).getTransAmtOutOwing();
            trn2TrnAmtOutB = transfers.get(position).getTransAmtOutB();
            trn2TrnAmtInA = transfers.get(position).getTransAmtInA();
            trn2TrnAmtInOwing = transfers.get(position).getTransAmtInOwing();
            trn2TrnAmtInB = transfers.get(position).getTransAmtInB();

            trn2MoneyOutAPercent = transfers.get(position).getTransAmtOutA() / transfers.get(position).getTransAmt();
            trn2MoneyInAPercent = transfers.get(position).getTransAmtInA() / transfers.get(position).getTransAmt();
            trn2FromId = transfers.get(position).getTransFromAcctId();
            trn2ToId = transfers.get(position).getTransToAcctId();
            trn2FromDebtSav = transfers.get(position).getTransFromDebtSav();
            trn2ToDebtSav = transfers.get(position).getTransToDebtSav();

            if (trn2FromDebtSav.equals("D")) {
                for (AccountsDb a : trn2DbMgr.getDebts()) {
                    if (a.getId() == trn2FromId) {
                        trn2FromAcctMax = a.getAcctMax();
                    }
                }
            }*/

            /*trn2Hldr.trn2EditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    trn2TransDb = (TransactionsDb) trn2Hldr.trn2EditBtn.getTag();
                    LayoutTransfersList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    trn2DbMgr = new DbManager(getContext());

                    AlertDialog.Builder builder = new AlertDialog.Builder(LayoutTransfersList.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_update, null);
                    updateDialogAmtET = dView.findViewById(R.id.dialogAmtET);
                    updateDialogUpdateBtn = dView.findViewById(R.id.dialogUpdateBtn);
                    updateDialogCancelBtn = dView.findViewById(R.id.dialogCancelBtn);
                    updateDialogWarnLayout = dView.findViewById(R.id.dialogWarnLayout);
                    updateDialogWarnLayout.setVisibility(View.GONE);
                    updateDialogWarnTV = dView.findViewById(R.id.dialogWarnTV);
                    updateDialogNoContBtn = dView.findViewById(R.id.dialogNoContBtn);
                    updateDialogYesContBtn = dView.findViewById(R.id.dialogYesContBtn);

                    updateDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn2Refresh();
                        }
                    });

                    updateDialogUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn2AmtEntry = trn2Gen.dblFromET(updateDialogAmtET);
                            trn2TrnAmtDiff = trn2AmtEntry - transfers.get(position).getTransAmt();

                            if (trn2FromId == 1 && (trn2DbMgr.retrieveCurrentAccountBalance() - trn2TrnAmtDiff < 0)) { //FROM MAIN ACCT & WILL GO NEGATIVE
                                updateDialogWarnLayout.setVisibility(View.VISIBLE);
                                updateDialogWarnTV.setText(getString(R.string.payment_not_possible_A));

                                updateDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        trn2Refresh();
                                    }
                                });

                                updateDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialogWarnLayout.setVisibility(View.GONE);
                                        revAndAdjTransOut();
                                        if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                            trn2DebtMinus(trn2TrnAmtDiff);
                                        } else if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                            trn2SavPlus(trn2TrnAmtDiff);
                                        }
                                    }
                                });
                            } else if (trn2FromId == 1) { //FROM MAIN ACCT & WON'T GO NEGATIVE
                                revAndAdjTransOut();
                                if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                    trn2DebtMinus(trn2TrnAmtDiff);
                                } else if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                    trn2SavPlus(trn2TrnAmtDiff);
                                }
                            } else if (trn2FromDebtSav.equals("D") & (trn2DbMgr.retrieveCurrentAcctAmt(trn2FromId) + trn2TrnAmtDiff > trn2FromAcctMax)) { //FROM DEBT ACCT && WILL GO OVER LIMIT
                                for (AccountsDb d : trn2DbMgr.getDebts()) {
                                    if (d.getId() == trn2FromId) {
                                        debtLimitFromDb = d.getAcctMax();
                                        debtAmtFromDb = d.getAcctBal();
                                    }
                                }
                                if (debtAmtFromDb + trn2TrnAmtDiff > debtLimitFromDb) { //DEBT WILL GO OVER LIMIT
                                    updateDialogWarnLayout.setVisibility(View.VISIBLE);
                                    updateDialogWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                    updateDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Refresh();
                                        }
                                    });

                                    updateDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateDialogWarnLayout.setVisibility(View.GONE);
                                            trn2DebtPlus(trn2TrnAmtDiff);
                                            if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                                trn2SavPlus(trn2TrnAmtDiff);
                                            } else if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                                trn2DebtMinus(trn2TrnAmtDiff);
                                            } else { //TO MAIN ACCT
                                                revAndAdjTransIn();
                                            }
                                        }
                                    });
                                } else { //DEBT WON'T GO OVER LIMIT
                                    trn2DebtPlus(trn2TrnAmtDiff);
                                    if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                        trn2SavPlus(trn2TrnAmtDiff);
                                    } else if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                        trn2DebtMinus(trn2TrnAmtDiff);
                                    } else { //TO MAIN ACCT
                                        revAndAdjTransIn();
                                    }
                                }
                            } else if (trn2FromDebtSav.equals("S")) { //FROM SAVINGS ACCT
                                if (trn2DbMgr.retrieveCurrentAcctAmt(trn2FromId) - trn2TrnAmtDiff < 0) { //ACCT WILL GO NEGATIVE
                                    updateDialogWarnLayout.setVisibility(View.VISIBLE);
                                    updateDialogWarnTV.setText(getString(R.string.not_enough_savings_warning));

                                    updateDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Refresh();
                                        }
                                    });

                                    updateDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateDialogWarnLayout.setVisibility(View.GONE);
                                            trn2SavMinus(trn2TrnAmtDiff);
                                            if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                                trn2SavPlus(trn2TrnAmtDiff);
                                            } else if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                                trn2DebtMinus(trn2TrnAmtDiff);
                                            } else { //TO MAIN ACCT
                                                revAndAdjTransIn();
                                            }
                                        }
                                    });
                                } else { //ACCT WON'T GO OVER NEGATIVE
                                    trn2SavMinus(trn2TrnAmtDiff);
                                    if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                                        trn2SavPlus(trn2TrnAmtDiff);
                                    } else if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                                        trn2DebtMinus(trn2TrnAmtDiff);
                                    } else { //TO MAIN ACCT
                                        revAndAdjTransIn();
                                    }
                                }
                            }

                            trn2Adapter.updateTransfers(trn2DbMgr.getTransfers());
                            notifyDataSetChanged();

                            trn2Refresh();
                        }
                    });
                    builder.setView(dView);
                    AlertDialog updateDialog = builder.create();
                    updateDialog.show();
                }
            });*/

            /*trn2Hldr.trn2DelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trn2TransDb = (TransactionsDb) trn2Hldr.trn2DelBtn.getTag();

                    if (!trn2FromDebtSav.equals("D") && !trn2FromDebtSav.equals("S")) { //FROM MAIN ACCT
                        trn2DbMgr.updateTotAcctBalPlus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                        trn2DbMgr.updateAandBBalPlus(
                                transfers.get(position).getTransAmtOutA(),
                                transfers.get(position).getTransAmtOutOwing(),
                                transfers.get(position).getTransAmtOutB(),
                                trn2DbMgr.retrieveCurrentA(),
                                trn2DbMgr.retrieveCurrentOwingA(),
                                trn2DbMgr.retrieveCurrentB());

                        if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                            trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                        }

                        if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                            trn2DebtPlus(transfers.get(position).getTransAmt());
                        } else if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                            trn2SavMinus(transfers.get(position).getTransAmt());
                        }
                    } else if (trn2FromDebtSav.equals("D")) { //FROM DEBT ACCT
                        trn2DebtMinus(transfers.get(position).getTransAmt());

                        if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                            trn2DebtPlus(transfers.get(position).getTransAmt());
                        } else if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                            trn2SavMinus(transfers.get(position).getTransAmt());
                        } else { //TO MAIN ACCT
                            trn2DbMgr.updateTotAcctBalMinus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                            trn2DbMgr.updateAandBBalMinus(
                                    transfers.get(position).getTransAmtOutA(),
                                    transfers.get(position).getTransAmtOutOwing(),
                                    transfers.get(position).getTransAmtOutB(),
                                    trn2DbMgr.retrieveCurrentA(),
                                    trn2DbMgr.retrieveCurrentOwingA(),
                                    trn2DbMgr.retrieveCurrentB());

                            if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                                trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                            }
                        }
                    } else if (trn2FromDebtSav.equals("S")) { //FROM SAVINGS ACCT
                        trn2SavPlus(transfers.get(position).getTransAmt());

                        if (trn2ToDebtSav.equals("D")) { //TO DEBT ACCT
                            trn2DebtPlus(transfers.get(position).getTransAmt());
                        } else if (trn2ToDebtSav.equals("S")) { //TO SAVINGS ACCT
                            trn2SavMinus(transfers.get(position).getTransAmt());
                        } else { //TO MAIN ACCT
                            trn2DbMgr.updateTotAcctBalMinus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                            trn2DbMgr.updateAandBBalMinus(
                                    transfers.get(position).getTransAmtOutA(),
                                    transfers.get(position).getTransAmtOutOwing(),
                                    transfers.get(position).getTransAmtOutB(),
                                    trn2DbMgr.retrieveCurrentA(),
                                    trn2DbMgr.retrieveCurrentOwingA(),
                                    trn2DbMgr.retrieveCurrentB());

                            if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                                trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                            }
                        }
                    }

                    trn2DbMgr.deleteTransactions(trn2TransDb);

                    trn2Adapter.updateTransfers(trn2DbMgr.getTransfers());
                    notifyDataSetChanged();

                    trn2Refresh();
                }
            });*/

            return convertView;
        }
    }

    private static class Trans2ViewHolder {

        public TextView trn2DateTV;
        public TextView trn2AmtTV;
        public TextView trn2FromNameTV;
        public TextView trn2ToNameTV;
    }

};
