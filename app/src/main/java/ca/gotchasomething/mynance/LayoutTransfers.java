package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutTransfers extends MainNavigation {

    Button transferDialogCancelBtn,
            transferDialogEnterBtn,
            transferDialogInfoOkBtn,
            warnDialogNoContBtn,
            warnDialogYesContBtn,
            trn1Btn,
            trn1DoneBtn;
    ContentValues trn1CV;
    Cursor trn1Cur,
            trn1Cur2;
    DbHelper trn1Helper,
            trn1Helper2,
            trn1Helper3;
    DbManager trn1DbMgr;
    Double debtAmtFromDb = 0.0,
            debtLimitFromDb = 0.0,
            debtPaytFromDb = 0.0,
            debtRateFromDb = 0.0,
            savAmtFromDb = 0.0,
            savGoalFromDb = 0.0,
            savPaytFromDb = 0.0,
            savRateFromDb = 0.0,
            trn1TrnAmt = 0.0,
            trn1AmtForA = 0.0,
            trn1AmtForB = 0.0,
            trn1AmtMissing = 0.0,
            trn1FromAcctBal = 0.0,
            trn1FromAcctMax = 0.0,
            trn1MoneyInA = 0.0,
            trn1MoneyInOwing = 0.0,
            trn1MoneyInB = 0.0,
            trn1MoneyOutA = 0.0,
            trn1MoneyOutAEntry = 0.0,
            trn1MoneyOutOwing = 0.0,
            trn1MoneyOutB = 0.0,
            trn1ToAcctBal = 0.0;
    EditText transferDialogCustomResAmtET,
            trn1TrnAmtET;
    General trn1Gen;
    ImageButton transferDialogInfoBtn;
    Intent trn1Refresh,
            trn1ToList,
            trn1ToMain;
    LinearLayout transferDialogCustomLayout,
            warnDialogWarnLayout;
    long trn1DebtId,
            trn1SavId,
            trn1FromSpinId,
            trn1ToSpinId;
    RadioButton transferDialogAllResRB,
            transferDialogNoneResRB,
            transferDialogCustomRB,
            transferDialogAsUsualRB;
    RadioGroup transferDialogResRG;
    Spinner trn1FromSpin,
            trn1ToSpin;
    SQLiteDatabase trn1Db,
            trn1Db2,
            trn1Db3;
    String trn1FromIsDebtSav = null,
            trn1FromSpinName = null,
            trn1ToIsDebtSav = null,
            trn1ToSpinName = null;
    TextView transferDialogCustomAvailAmtTV,
            transferDialogInfoTV,
            transferDialogResTV,
            warnDialogWarnTV;
    TransactionsDb trn1TransDb;
    TransferSpinnerAdapter trn1FromSpinAdapter,
            trn1ToSpinAdapter;
    View dView,
            dView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main_transfers);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        trn1Gen = new General();
        trn1DbMgr = new DbManager(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        trn1FromSpin = findViewById(R.id.transferFromSpin);
        trn1ToSpin = findViewById(R.id.transferToSpin);
        trn1TrnAmtET = findViewById(R.id.transferAmtET);
        trn1Btn = findViewById(R.id.transferBtn);
        trn1DoneBtn = findViewById(R.id.transfersDoneBtn);

        trn1Btn.setOnClickListener(onClickTransferBtn);
        trn1DoneBtn.setOnClickListener(onClickTrn1DoneBtn);

        trn1Helper = new DbHelper(this);
        trn1Db = trn1Helper.getReadableDatabase();
        trn1Cur = trn1Db.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ID + " ASC", null);
        trn1FromSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), trn1Cur);
        trn1FromSpin.setAdapter(trn1FromSpinAdapter);

        trn1FromSpin.setOnItemSelectedListener(trn1FromSpinSel);

        trn1Helper2 = new DbHelper(this);
        trn1Db2 = trn1Helper2.getReadableDatabase();
        trn1Cur2 = trn1Db2.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ID + " ASC", null);
        trn1ToSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), trn1Cur2);
        trn1ToSpin.setAdapter(trn1ToSpinAdapter);

        trn1ToSpin.setOnItemSelectedListener(trn1ToSpinSel);

        trn1CV = new ContentValues();
        trn1CV.put(DbHelper.LASTPAGEID, 8);
        trn1Helper3 = new DbHelper(this);
        trn1Db3 = trn1Helper3.getWritableDatabase();
        trn1Db3.update(DbHelper.CURRENT_TABLE_NAME, trn1CV, DbHelper.ID + "= '1'", null);
        trn1Db3.close();

    }

    public void trn1Refresh() {
        trn1Refresh = new Intent(LayoutTransfers.this, LayoutTransfers.class);
        trn1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(trn1Refresh);
    }

    public void trn1ToList() {
        trn1ToList = new Intent(LayoutTransfers.this, LayoutTransfersList.class);
        trn1ToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(trn1ToList);
    }

    AdapterView.OnItemSelectedListener trn1FromSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn1FromSpinName = trn1Cur.getString(trn1Cur.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            trn1FromSpinId = trn1Cur.getLong(trn1Cur.getColumnIndexOrThrow(DbHelper.ID));
            trn1FromIsDebtSav = trn1Cur.getString(trn1Cur.getColumnIndexOrThrow(DbHelper.ACCTDEBTSAV));
            trn1FromAcctMax = trn1Cur.getDouble(trn1Cur.getColumnIndexOrThrow(DbHelper.ACCTMAX));
            trn1FromAcctBal = trn1Cur.getDouble(trn1Cur.getColumnIndexOrThrow(DbHelper.ACCTBAL));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener trn1ToSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn1ToSpinName = trn1Cur2.getString(trn1Cur2.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            trn1ToSpinId = trn1Cur2.getLong(trn1Cur2.getColumnIndexOrThrow(DbHelper.ID));
            trn1ToIsDebtSav = trn1Cur2.getString(trn1Cur2.getColumnIndexOrThrow(DbHelper.ACCTDEBTSAV));
            trn1ToAcctBal = trn1Cur2.getDouble(trn1Cur2.getColumnIndexOrThrow(DbHelper.ACCTBAL));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickTrn1DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn1ToMain = new Intent(LayoutTransfers.this, MainActivity.class);
            trn1ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn1ToMain);
        }
    };

    //UPDATE THE FROM ACCOUNTS

    public void trn1DebtPlus() {
        trn1DbMgr.updateRecPlusPt1(trn1TrnAmt, trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId), trn1FromSpinId);
        for (AccountsDb d : trn1DbMgr.getDebts()) {
            if (d.getId() == trn1FromSpinId) {
                debtAmtFromDb = d.getAcctBal();
                debtLimitFromDb = d.getAcctMax();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }
        trn1DbMgr.updateRecPt2(trn1Gen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), trn1FromSpinId);
    }

    public void trn1SavMinus() {
        trn1DbMgr.updateRecMinusPt1(trn1TrnAmt, trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId), trn1FromSpinId);
        for (AccountsDb s : trn1DbMgr.getSavings()) {
            if (s.getId() == trn1FromSpinId) {
                savAmtFromDb = s.getAcctBal();
                savGoalFromDb = s.getAcctMax();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }
        trn1DbMgr.updateRecPt2(trn1Gen.calcSavingsDate(
                savGoalFromDb,
                savAmtFromDb,
                savRateFromDb,
                savPaytFromDb,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)), trn1FromSpinId);
    }

    public void trn1MainMinus() {
        trn1DbMgr.updateTotAcctBalMinus(trn1TrnAmt, trn1DbMgr.retrieveCurrentAccountBalance());

        trn1DbMgr.updateAandBBalMinus(
                trn1MoneyOutA,
                trn1MoneyOutOwing,
                trn1MoneyOutB,
                trn1DbMgr.retrieveCurrentA(),
                trn1DbMgr.retrieveCurrentOwingA(),
                trn1DbMgr.retrieveCurrentB());
        if (trn1DbMgr.retrieveCurrentOwingA() < 0) {
            trn1DbMgr.adjustCurrentAandB(trn1DbMgr.retrieveCurrentOwingA(), trn1DbMgr.retrieveCurrentA(), trn1DbMgr.retrieveCurrentB());
        }
    }

    //UPDATE THE TO ACCOUNTS

    public void trn1DebtMinus() {
        trn1DbMgr.updateRecMinusPt1(trn1TrnAmt, trn1DbMgr.retrieveCurrentAcctAmt(trn1ToSpinId), trn1ToSpinId);

        for (AccountsDb d : trn1DbMgr.getDebts()) {
            if (d.getId() == trn1ToSpinId) {
                debtAmtFromDb = d.getAcctBal();
                debtLimitFromDb = d.getAcctMax();
                debtRateFromDb = d.getAcctIntRate();
                debtPaytFromDb = d.getAcctPaytsTo();
            }
        }

        trn1DbMgr.updateRecPt2(trn1Gen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), trn1ToSpinId);
    }

    public void trn1SavPlus() {
        trn1DbMgr.updateRecPlusPt1(trn1TrnAmt, trn1DbMgr.retrieveCurrentAcctAmt(trn1ToSpinId), trn1ToSpinId);

        for (AccountsDb s : trn1DbMgr.getSavings()) {
            if (s.getId() == trn1ToSpinId) {
                savAmtFromDb = s.getAcctBal();
                savGoalFromDb = s.getAcctMax();
                savRateFromDb = s.getAcctIntRate();
                savPaytFromDb = s.getAcctPaytsTo();
            }
        }

        trn1DbMgr.updateRecPt2(trn1Gen.calcSavingsDate(
                savGoalFromDb,
                savAmtFromDb,
                savRateFromDb,
                savPaytFromDb,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)), trn1ToSpinId);
    }

    public void trn1MainPlus() {
        trn1DbMgr.updateTotAcctBalPlus(trn1TrnAmt, trn1DbMgr.retrieveCurrentAccountBalance());

        trn1DbMgr.updateAandBBalPlus(
                trn1MoneyInA,
                trn1MoneyInOwing,
                trn1MoneyInB,
                trn1DbMgr.retrieveCurrentA(),
                trn1DbMgr.retrieveCurrentOwingA(),
                trn1DbMgr.retrieveCurrentB());
        if (trn1DbMgr.retrieveCurrentOwingA() < 0) {
            trn1DbMgr.adjustCurrentAandB(trn1DbMgr.retrieveCurrentOwingA(), trn1DbMgr.retrieveCurrentA(), trn1DbMgr.retrieveCurrentB());
        }
    }

    TextWatcher onChangeCustomResET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            trn1MoneyOutAEntry = trn1Gen.dblFromET(transferDialogCustomResAmtET);
            trn1Gen.dblASCurrency(String.valueOf((trn1TrnAmt - trn1MoneyOutAEntry)), transferDialogCustomAvailAmtTV);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void addTransAndFinish() {
        trn1TransDb = new TransactionsDb(
                "transfer",
                "N/A",
                "N/A",
                0,
                trn1TrnAmt,
                trn1MoneyInA,
                trn1MoneyInOwing,
                trn1MoneyInB,
                trn1MoneyOutA,
                trn1MoneyOutOwing,
                trn1MoneyOutB,
                trn1ToSpinId,
                trn1ToSpinName,
                trn1ToIsDebtSav,
                trn1FromSpinId,
                trn1FromSpinName,
                trn1FromIsDebtSav,
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                trn1Gen.createTimestamp(),
                0);
        trn1DbMgr.addTransactions(trn1TransDb);

        try {
            for (AccountsDb a : trn1DbMgr.getDebts()) {
                trn1DebtId = a.getId();
                trn1DbMgr.updateRecReTransfer(
                        trn1DebtId,
                        trn1DbMgr.transfersToAcctThisYear(trn1DebtId, trn1Gen.lastNumOfDays(365)),
                        trn1DbMgr.transfersFromAcctThisYear(trn1DebtId, trn1Gen.lastNumOfDays(365)));
            }
            for (AccountsDb a : trn1DbMgr.getSavings()) {
                trn1SavId = a.getId();
                trn1DbMgr.updateRecReTransfer(
                        trn1SavId,
                        trn1DbMgr.transfersToAcctThisYear(trn1SavId, trn1Gen.lastNumOfDays(365)),
                        trn1DbMgr.transfersFromAcctThisYear(trn1SavId, trn1Gen.lastNumOfDays(365)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(LayoutTransfers.this, R.string.transfer_complete, Toast.LENGTH_LONG).show();
        trn1ToList();
    }

    public void allFromRes() {
        trn1MoneyOutA = trn1TrnAmt;
        trn1MoneyOutOwing = 0.0;
        trn1MoneyOutB = 0.0;
    }

    public void allFromAvail() {
        trn1MoneyOutA = 0.0;
        trn1MoneyOutOwing = 0.0;
        trn1MoneyOutB = trn1TrnAmt;
    }

    public void fromUsual() {
        trn1MoneyOutA = trn1TrnAmt * (trn1DbMgr.sumTotalAExpenses() / trn1DbMgr.sumTotalIncome());
        trn1MoneyOutOwing = 0.0;
        trn1MoneyOutB = trn1TrnAmt - trn1MoneyOutA;
    }

    public void fromCustom() {
        trn1MoneyOutA = trn1MoneyOutAEntry;
        trn1MoneyOutOwing = 0.0;
        trn1MoneyOutB = trn1TrnAmt - trn1MoneyOutA;
    }

    public void allToRes() {
        trn1MoneyInA = trn1TrnAmt;
        trn1MoneyInOwing = 0.0;
        trn1MoneyInB = 0.0;
    }

    public void allToAvail() {
        trn1MoneyInA = 0.0;
        trn1MoneyInOwing = 0.0;
        trn1MoneyInB = trn1TrnAmt;
    }

    public void toUsual() {
        trn1MoneyInA = trn1TrnAmt * (trn1DbMgr.sumTotalAExpenses() / trn1DbMgr.sumTotalIncome());
        trn1MoneyInOwing = 0.0;
        trn1MoneyInB = trn1TrnAmt - trn1MoneyInA;
    }

    public void toCustom() {
        trn1MoneyInA = trn1MoneyOutAEntry;
        trn1MoneyInOwing = 0.0;
        trn1MoneyInB = trn1TrnAmt - trn1MoneyInA;
    }

    View.OnClickListener onClickTransferBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            trn1TrnAmt = trn1Gen.dblFromET(trn1TrnAmtET);

            if (trn1FromSpinId == trn1ToSpinId) { //FROM & TO ACCT THE SAME
                Toast.makeText(LayoutTransfers.this, R.string.same_acct, Toast.LENGTH_LONG).show();
            } else if (trn1FromSpinId == 1 || trn1ToSpinId == 1) { //IF FROM MAIN ACCT OR TO MAIN ACCT
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(LayoutTransfers.this);
                dView2 = getLayoutInflater().inflate(R.layout.dialog_transfer, null);
                builder2.setView(dView2);
                final AlertDialog dialog2 = builder2.create();
                dialog2.show();

                transferDialogResTV = dView2.findViewById(R.id.transferDialogResTV);
                transferDialogInfoBtn = dView2.findViewById(R.id.transferDialogInfoBtn);
                transferDialogInfoTV = dView2.findViewById(R.id.transferDialogInfoTV);
                transferDialogInfoTV.setVisibility(View.GONE);
                transferDialogInfoOkBtn = dView2.findViewById(R.id.transferDialogInfoOkBtn);
                transferDialogInfoOkBtn.setVisibility(View.GONE);
                transferDialogResRG = dView2.findViewById(R.id.transferDialogResRG);
                transferDialogAllResRB = dView2.findViewById(R.id.transferDialogAllResRB);
                transferDialogNoneResRB = dView2.findViewById(R.id.transferDialogNoneResRB);
                transferDialogAsUsualRB = dView2.findViewById(R.id.transferDialogAsUsualRB);
                transferDialogCustomRB = dView2.findViewById(R.id.transferDialogCustomRB);
                transferDialogCustomLayout = dView2.findViewById(R.id.transferDialogCustomLayout);
                transferDialogCustomLayout.setVisibility(View.GONE);
                transferDialogCustomResAmtET = dView2.findViewById(R.id.transferDialogCustomResAmtET);
                transferDialogCustomAvailAmtTV = dView2.findViewById(R.id.transferDialogCustomAvailAmtTV);
                transferDialogEnterBtn = dView2.findViewById(R.id.transferDialogEnterBtn);
                transferDialogCancelBtn = dView2.findViewById(R.id.transferDialogCancelBtn);

                if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                    transferDialogResTV.setText(R.string.choose_split);
                } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                    transferDialogResTV.setText(R.string.choose_split_inc);
                }

                transferDialogInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transferDialogInfoTV.setVisibility(View.VISIBLE);
                        transferDialogInfoOkBtn.setVisibility(View.VISIBLE);
                        transferDialogInfoOkBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                transferDialogInfoTV.setVisibility(View.GONE);
                                transferDialogInfoOkBtn.setVisibility(View.GONE);
                            }
                        });
                    }
                });

                transferDialogResRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.transferDialogAllResRB) {
                            transferDialogInfoTV.setVisibility(View.GONE);
                            transferDialogInfoOkBtn.setVisibility(View.GONE);
                            transferDialogNoneResRB.setVisibility(View.GONE);
                            transferDialogAsUsualRB.setVisibility(View.GONE);
                            transferDialogCustomRB.setVisibility(View.GONE);
                            transferDialogCustomLayout.setVisibility(View.GONE);
                        } else if (checkedId == R.id.transferDialogNoneResRB) {
                            transferDialogInfoTV.setVisibility(View.GONE);
                            transferDialogInfoOkBtn.setVisibility(View.GONE);
                            transferDialogAllResRB.setVisibility(View.GONE);
                            transferDialogAsUsualRB.setVisibility(View.GONE);
                            transferDialogCustomRB.setVisibility(View.GONE);
                            transferDialogCustomLayout.setVisibility(View.GONE);
                        } else if (checkedId == R.id.transferDialogAsUsualRB) {
                            transferDialogInfoTV.setVisibility(View.GONE);
                            transferDialogInfoOkBtn.setVisibility(View.GONE);
                            transferDialogAllResRB.setVisibility(View.GONE);
                            transferDialogNoneResRB.setVisibility(View.GONE);
                            transferDialogCustomRB.setVisibility(View.GONE);
                            transferDialogCustomLayout.setVisibility(View.GONE);
                        } else if (checkedId == R.id.transferDialogCustomRB) {
                            transferDialogInfoTV.setVisibility(View.GONE);
                            transferDialogInfoOkBtn.setVisibility(View.GONE);
                            transferDialogAllResRB.setVisibility(View.GONE);
                            transferDialogNoneResRB.setVisibility(View.GONE);
                            transferDialogAsUsualRB.setVisibility(View.GONE);
                            transferDialogCustomLayout.setVisibility(View.VISIBLE);
                            transferDialogCustomResAmtET.addTextChangedListener(onChangeCustomResET);
                        }
                    }
                });

                transferDialogEnterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((!trn1FromIsDebtSav.equals("D") && trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId) < trn1TrnAmt) || (trn1FromIsDebtSav.equals("D") && (trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId) + trn1TrnAmt > trn1FromAcctMax))) { //FROM ACCT WILL GO NEGATIVE OR OVER LIMIT
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutTransfers.this);
                            dView = getLayoutInflater().inflate(R.layout.dialog_warn, null);
                            builder.setView(dView);
                            final AlertDialog dialog = builder.create();
                            dialog.show();

                            warnDialogWarnLayout = dView.findViewById(R.id.warnDialogWarnLayout);
                            warnDialogWarnTV = dView.findViewById(R.id.warnDialogWarnTV);
                            warnDialogNoContBtn = dView.findViewById(R.id.warnDialogNoContBtn);
                            warnDialogYesContBtn = dView.findViewById(R.id.warnDialogYesContBtn);

                            if (trn1FromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                warnDialogWarnTV.setText(R.string.not_enough_credit_warning);
                            } else if (trn1FromIsDebtSav.equals("S")) { //FROM SAVINGS ACCT
                                warnDialogWarnTV.setText(R.string.not_enough_savings_warning);
                            } else if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                warnDialogWarnTV.setText(R.string.transfer_not_possible_A);
                            }

                            warnDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    trn1Refresh();
                                }
                            });

                            warnDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (transferDialogAllResRB.isChecked()) {
                                        if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                            trn1MoneyInA = 0.0;
                                            trn1MoneyInOwing = 0.0;
                                            trn1MoneyInB = 0.0;
                                            if (trn1DbMgr.retrieveCurrentA() < trn1TrnAmt) { //A CAN'T COVER WHOLE AMT
                                                trn1AmtMissing = trn1TrnAmt - trn1DbMgr.retrieveCurrentA();
                                                trn1MoneyOutOwing = 0.0;
                                                if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY
                                                    if (trn1DbMgr.retrieveCurrentB() >= trn1AmtMissing) { //B CAN COVER THE MISSING AMT
                                                        trn1MoneyOutA = trn1TrnAmt - trn1AmtMissing;
                                                        trn1MoneyOutB = trn1AmtMissing;
                                                    } else { //B CAN'T COVER MISSING AMT: B GIVES WHAT IT CAN AND A GOES NEGATIVE BY THE REST
                                                        trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                                        trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                                    }
                                                } else { //B HAS NO MONEY: A GOES NEGATIVE BY WHOLE AMT
                                                    trn1MoneyOutA = trn1TrnAmt;
                                                    trn1MoneyOutB = 0.0;
                                                }
                                            } else { //A CAN COVER WHOLE AMT
                                                allFromRes();
                                            }
                                            trn1MainMinus();
                                        } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                            trn1MoneyOutA = 0.0;
                                            trn1MoneyOutOwing = 0.0;
                                            trn1MoneyOutB = 0.0;
                                            allToRes();
                                            trn1MainPlus();
                                        }
                                    } else if (transferDialogNoneResRB.isChecked()) {
                                        if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                            trn1MoneyInA = 0.0;
                                            trn1MoneyInOwing = 0.0;
                                            trn1MoneyInB = 0.0;
                                            if (trn1DbMgr.retrieveCurrentB() < trn1TrnAmt) { //B IS LESS THAN AMT: A COVERS MISSING AMT, BUT IS OWED FOR IT
                                                trn1AmtMissing = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                                trn1MoneyOutA = trn1AmtMissing;
                                                trn1MoneyOutOwing = trn1AmtMissing;
                                                trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                            } else {
                                                allFromAvail();
                                            }
                                            trn1MainMinus();
                                        } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                            trn1MoneyOutA = 0.0;
                                            trn1MoneyOutOwing = 0.0;
                                            trn1MoneyOutB = 0.0;
                                            allToAvail();
                                            trn1MainPlus();
                                        }
                                    } else if (transferDialogAsUsualRB.isChecked()) {
                                        if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                            trn1MoneyInA = 0.0;
                                            trn1MoneyInOwing = 0.0;
                                            trn1MoneyInB = 0.0;
                                            trn1AmtForA = trn1TrnAmt * (trn1DbMgr.sumTotalAExpenses() / trn1DbMgr.sumTotalIncome());
                                            trn1AmtForB = trn1TrnAmt - trn1AmtForA;
                                            if (trn1DbMgr.retrieveCurrentB() < trn1AmtForB) { //B CAN'T COVER ITS PORTION
                                                if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY, GIVES WHAT IT CAN AND A GOES NEGATIVE AND IS OWED
                                                    trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                                    trn1MoneyOutOwing = trn1AmtForB - trn1DbMgr.retrieveCurrentB();
                                                    trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                                } else { //B HAS NO MONEY, A PAYS, BUT IS OWED FOR B'S PORTION
                                                    trn1MoneyOutA = trn1TrnAmt;
                                                    trn1MoneyOutOwing = trn1AmtForB;
                                                    trn1MoneyOutB = 0.0;
                                                }
                                            } else { //B CAN COVER ITS OWN PORTION
                                                fromUsual();
                                            }
                                            trn1MainMinus();
                                        } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                            trn1MoneyOutA = 0.0;
                                            trn1MoneyOutOwing = 0.0;
                                            trn1MoneyOutB = 0.0;
                                            toUsual();
                                            trn1MainPlus();
                                        }
                                    } else if (transferDialogCustomRB.isChecked()) {
                                        if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                            trn1MoneyInA = 0.0;
                                            trn1MoneyInOwing = 0.0;
                                            trn1MoneyInB = 0.0;
                                            trn1AmtForA = trn1MoneyOutAEntry;
                                            trn1AmtForB = trn1TrnAmt - trn1AmtForA;
                                            if (trn1DbMgr.retrieveCurrentB() < trn1AmtForB) { //B CAN'T COVER ITS PORTION
                                                if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY, GIVES WHAT IT CAN AND A GOES NEGATIVE AND IS OWED
                                                    trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                                    trn1MoneyOutOwing = trn1AmtForB - trn1DbMgr.retrieveCurrentB();
                                                    trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                                } else { //B HAS NO MONEY, A PAYS, BUT IS OWED FOR B'S PORTION
                                                    trn1MoneyOutA = trn1TrnAmt;
                                                    trn1MoneyOutOwing = trn1AmtForB;
                                                    trn1MoneyOutB = 0.0;
                                                }
                                            } else { //B CAN COVER ITS OWN PORTION
                                                fromCustom();
                                            }
                                            trn1MainMinus();
                                        } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                            trn1MoneyOutA = 0.0;
                                            trn1MoneyOutOwing = 0.0;
                                            trn1MoneyOutB = 0.0;
                                            toCustom();
                                            trn1MainPlus();
                                        }
                                    }

                                    if (trn1FromIsDebtSav.equals("D")) {
                                        trn1DebtPlus();
                                    } else if (trn1FromIsDebtSav.equals("S")) {
                                        trn1SavMinus();
                                    }

                                    if (trn1ToIsDebtSav.equals("D")) {
                                        trn1DebtMinus();
                                    } else if (trn1ToIsDebtSav.equals("S")) {
                                        trn1SavPlus();
                                    }

                                    addTransAndFinish();
                                }
                            });
                        } else { //FROM ACCT WILL NOT GO NEGATIVE OR OVER LIMIT
                            if (transferDialogAllResRB.isChecked()) {
                                if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                    trn1MoneyInA = 0.0;
                                    trn1MoneyInOwing = 0.0;
                                    trn1MoneyInB = 0.0;
                                    if (trn1DbMgr.retrieveCurrentA() < trn1TrnAmt) { //A CAN'T COVER WHOLE AMT
                                        trn1AmtMissing = trn1TrnAmt - trn1DbMgr.retrieveCurrentA();
                                        trn1MoneyOutOwing = 0.0;
                                        if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY
                                            if (trn1DbMgr.retrieveCurrentB() >= trn1AmtMissing) { //B CAN COVER THE MISSING AMT
                                                trn1MoneyOutA = trn1TrnAmt - trn1AmtMissing;
                                                trn1MoneyOutB = trn1AmtMissing;
                                            } else { //B CAN'T COVER MISSING AMT: B GIVES WHAT IT CAN AND A GOES NEGATIVE BY THE REST
                                                trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                                trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                            }
                                        } else { //B HAS NO MONEY: A GOES NEGATIVE BY WHOLE AMT
                                            trn1MoneyOutA = trn1TrnAmt;
                                            trn1MoneyOutB = 0.0;
                                        }
                                    } else { //A CAN COVER WHOLE AMT
                                        allFromRes();
                                    }
                                    trn1MainMinus();
                                } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                    trn1MoneyOutA = 0.0;
                                    trn1MoneyOutOwing = 0.0;
                                    trn1MoneyOutB = 0.0;
                                    allToRes();
                                    trn1MainPlus();
                                }
                            } else if (transferDialogNoneResRB.isChecked()) {
                                if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                    trn1MoneyInA = 0.0;
                                    trn1MoneyInOwing = 0.0;
                                    trn1MoneyInB = 0.0;
                                    if (trn1DbMgr.retrieveCurrentB() < trn1TrnAmt) { //B IS LESS THAN AMT: A COVERS MISSING AMT, BUT IS OWED FOR IT
                                        trn1AmtMissing = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                        trn1MoneyOutA = trn1AmtMissing;
                                        trn1MoneyOutOwing = trn1AmtMissing;
                                        trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                    } else {
                                        allFromAvail();
                                    }
                                    trn1MainMinus();
                                } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                    trn1MoneyOutA = 0.0;
                                    trn1MoneyOutOwing = 0.0;
                                    trn1MoneyOutB = 0.0;
                                    allToAvail();
                                    trn1MainPlus();
                                }
                            } else if (transferDialogAsUsualRB.isChecked()) {
                                if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                    trn1MoneyInA = 0.0;
                                    trn1MoneyInOwing = 0.0;
                                    trn1MoneyInB = 0.0;
                                    trn1AmtForA = trn1TrnAmt * (trn1DbMgr.sumTotalAExpenses() / trn1DbMgr.sumTotalIncome());
                                    trn1AmtForB = trn1TrnAmt - trn1AmtForA;
                                    if (trn1DbMgr.retrieveCurrentB() < trn1AmtForB) { //B CAN'T COVER ITS PORTION
                                        if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY, GIVES WHAT IT CAN AND A GOES NEGATIVE AND IS OWED
                                            trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                            trn1MoneyOutOwing = trn1AmtForB - trn1DbMgr.retrieveCurrentB();
                                            trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                        } else { //B HAS NO MONEY, A PAYS, BUT IS OWED FOR B'S PORTION
                                            trn1MoneyOutA = trn1TrnAmt;
                                            trn1MoneyOutOwing = trn1AmtForB;
                                            trn1MoneyOutB = 0.0;
                                        }
                                    } else { //B CAN COVER ITS OWN PORTION
                                        fromUsual();
                                    }
                                    trn1MainMinus();
                                } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                    trn1MoneyOutA = 0.0;
                                    trn1MoneyOutOwing = 0.0;
                                    trn1MoneyOutB = 0.0;
                                    toUsual();
                                    trn1MainPlus();
                                }
                            } else if (transferDialogCustomRB.isChecked()) {
                                if (trn1FromSpinId == 1) { //FROM MAIN ACCT
                                    trn1MoneyInA = 0.0;
                                    trn1MoneyInOwing = 0.0;
                                    trn1MoneyInB = 0.0;
                                    trn1AmtForA = trn1MoneyOutAEntry;
                                    trn1AmtForB = trn1TrnAmt - trn1AmtForA;
                                    if (trn1DbMgr.retrieveCurrentB() < trn1AmtForB) { //B CAN'T COVER ITS PORTION
                                        if (trn1DbMgr.retrieveCurrentB() > 0) { //B HAS MONEY, GIVES WHAT IT CAN AND A GOES NEGATIVE AND IS OWED
                                            trn1MoneyOutA = trn1TrnAmt - trn1DbMgr.retrieveCurrentB();
                                            trn1MoneyOutOwing = trn1AmtForB - trn1DbMgr.retrieveCurrentB();
                                            trn1MoneyOutB = trn1DbMgr.retrieveCurrentB();
                                        } else { //B HAS NO MONEY, A PAYS, BUT IS OWED FOR B'S PORTION
                                            trn1MoneyOutA = trn1TrnAmt;
                                            trn1MoneyOutOwing = trn1AmtForB;
                                            trn1MoneyOutB = 0.0;
                                        }
                                    } else { //B CAN COVER ITS OWN PORTION
                                        fromCustom();
                                    }
                                    trn1MainMinus();
                                } else if (trn1ToSpinId == 1) { //TO MAIN ACCT
                                    trn1MoneyOutA = 0.0;
                                    trn1MoneyOutOwing = 0.0;
                                    trn1MoneyOutB = 0.0;
                                    toCustom();
                                    trn1MainPlus();
                                }
                            }

                            if (trn1FromIsDebtSav.equals("D")) {
                                trn1DebtPlus();
                            } else if (trn1FromIsDebtSav.equals("S")) {
                                trn1SavMinus();
                            }

                            if (trn1ToIsDebtSav.equals("D")) {
                                trn1DebtMinus();
                            } else if (trn1ToIsDebtSav.equals("S")) {
                                trn1SavPlus();
                            }

                            addTransAndFinish();
                        }
                    }
                });

                transferDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trn1Refresh();
                    }
                });
            } else {//NOT FROM MAIN ACCT AND NOT TO MAIN ACCT
                if ((trn1FromIsDebtSav.equals("D") && (trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId) + trn1TrnAmt > trn1FromAcctMax)) || (trn1FromIsDebtSav.equals("S") && (trn1DbMgr.retrieveCurrentAcctAmt(trn1FromSpinId) < trn1TrnAmt))) { //WILL GO OVER LIMIT OR NEGATIVE
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutTransfers.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_warn, null);
                    builder.setView(dView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    warnDialogWarnLayout = dView.findViewById(R.id.warnDialogWarnLayout);
                    warnDialogWarnTV = dView.findViewById(R.id.warnDialogWarnTV);
                    warnDialogNoContBtn = dView.findViewById(R.id.warnDialogNoContBtn);
                    warnDialogYesContBtn = dView.findViewById(R.id.warnDialogYesContBtn);

                    if (trn1FromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                        warnDialogWarnTV.setText(R.string.not_enough_credit_warning);
                    } else if (trn1FromIsDebtSav.equals("S")) { //FROM SAVINGS ACCT
                        warnDialogWarnTV.setText(R.string.not_enough_savings_warning);
                    }

                    warnDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1Refresh();
                        }
                    });

                    warnDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1MoneyInA = 0.0;
                            trn1MoneyInOwing = 0.0;
                            trn1MoneyInB = 0.0;
                            trn1MoneyOutA = 0.0;
                            trn1MoneyOutOwing = 0.0;
                            trn1MoneyOutB = 0.0;
                            if (trn1FromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                                trn1DebtPlus();
                                if (trn1ToIsDebtSav.equals("D")) {
                                    trn1DebtMinus();
                                } else if (trn1ToIsDebtSav.equals("S")) {
                                    trn1SavPlus();
                                }
                            } else if (trn1FromIsDebtSav.equals("S")) { //FROM SAVINGS ACCT
                                trn1SavMinus();
                                if (trn1ToIsDebtSav.equals("D")) {
                                    trn1DebtMinus();
                                } else if (trn1ToIsDebtSav.equals("S")) {
                                    trn1SavPlus();
                                }
                            }

                            addTransAndFinish();
                        }
                    });
                } else { //WILL NOT GO OVER LIMIT OR NEGATIVE
                    trn1MoneyInA = 0.0;
                    trn1MoneyInOwing = 0.0;
                    trn1MoneyInB = 0.0;
                    trn1MoneyOutA = 0.0;
                    trn1MoneyOutOwing = 0.0;
                    trn1MoneyOutB = 0.0;
                    if (trn1FromIsDebtSav.equals("D")) { //FROM DEBT ACCT
                        trn1DebtPlus();
                        if (trn1ToIsDebtSav.equals("D")) {
                            trn1DebtMinus();
                        } else if (trn1ToIsDebtSav.equals("S")) {
                            trn1SavPlus();
                        }
                    } else if (trn1FromIsDebtSav.equals("S")) { //FROM SAVINGS ACCT
                        trn1SavMinus();
                        if (trn1ToIsDebtSav.equals("D")) {
                            trn1DebtMinus();
                        } else if (trn1ToIsDebtSav.equals("S")) {
                            trn1SavPlus();
                        }
                    }
                    addTransAndFinish();
                }
            }
        }
    };
}