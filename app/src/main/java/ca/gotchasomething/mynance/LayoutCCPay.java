package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutCCPay extends MainNavigation {

    Button warnDialogNoContBtn,
            warnDialogYesContBtn;
    CheckBox layCCPayCheckbox;
    ContentValues layCCPayCV,
            layCCPayCV2;
    Cursor layCCPayCur3;
    DbHelper layCCPayHelper,
            layCCPayHelper2,
            layCCPayHelper3;
    DbManager layCCPayDbMgr;
    Double debtAmtFromDb = 0.0,
            debtLimitFromDb = 0.0,
            debtPaytFromDb = 0.0,
            debtRateFromDb = 0.0,
            layCCPayFromAcctBal = 0.0,
            layCCPayFromAcctMax = 0.0,
            debtToPayToChargingCCFromTag = 0.0,
            layCCPayAmtDue = 0.0,
            layCCPayPurAmtFromTag = 0.0,
            layCCPayAmtToPay = 0.0,
            layCCPayNewDebtToPay = 0.0,
            layCCPayToPayTotA = 0.0,
            layCCPayToPayTotB = 0.0,
            savAmtFromDb = 0.0,
            savGoalFromDb = 0.0,
            savPaytFromDb = 0.0,
            savRateFromDb = 0.0;
    General layCCPayGen;
    Intent layCCPayRefresh,
            layCCPayToMain;
    LayCCPayPaytLstAdapter layCCPayPaytLstAdapter;
    LayCCPayTransLstAdapter layCCPayTransLstAdapter;
    LinearLayout layCCPayPaytListLayout,
            warnDialogWarnLayout;
    ListView layCCPayCCPaytListView,
            layCCPayTransListView;
    long layCCPayFromSpinId,
            layCCPayToAcctId,
            layCCPayChargingDebtIdFromTag,
            layCCPayId;
    RelativeLayout layCCPayToPayHeaderLayout;
    Spinner layCCPayPayFromSpin;
    SQLiteDatabase layCCPayDb,
            layCCPayDb2,
            layCCPayDb3;
    String layCCPayFromIsDebtSav = null,
            layCCPayFromSpinName = null,
            layCCPayPriorityFromTag = null,
            layCCPayToAcctName = null;
    TextView layCCPaycheckBelowLabel,
            layCCPayNoCCTransTV,
            layCCPayPayFromLabel,
            warnDialogWarnTV;
    TransactionsDb layCCPayMonOutDb,
            layCCPayTransDb;
    TransferSpinnerAdapter layCCPayFromSpinAdapter;
    View dView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main_pay_cc);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layCCPayDbMgr = new DbManager(this);
        layCCPayGen = new General();

        layCCPayNoCCTransTV = findViewById(R.id.mainPayCCHeaderTV);
        layCCPayNoCCTransTV.setVisibility(View.GONE);
        layCCPayPayFromLabel = findViewById(R.id.mainPayPayFromLabel);
        layCCPayPayFromSpin = findViewById(R.id.mainPayFromSpinner);
        layCCPaycheckBelowLabel = findViewById(R.id.mainPayCCCheckBelowLabel);
        layCCPayPaytListLayout = findViewById(R.id.mainPayCCPaytListLayout);
        layCCPayPaytListLayout.setVisibility(View.GONE);
        layCCPayToPayHeaderLayout = findViewById(R.id.mainPayCCToPayHeaderLayout);
        layCCPayCheckbox = findViewById(R.id.mainPayCCCheckbox);

        layCCPayHelper3 = new DbHelper(this);
        layCCPayDb3 = layCCPayHelper3.getReadableDatabase();
        layCCPayCur3 = layCCPayDb3.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ID + " ASC", null);
        layCCPayFromSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), layCCPayCur3);
        layCCPayPayFromSpin.setAdapter(layCCPayFromSpinAdapter);

        layCCPayPayFromSpin.setOnItemSelectedListener(layCCPayFromSpinSel);

        layCCPayTransListView = findViewById(R.id.mainPayCCTransListView);
        layCCPayTransLstAdapter = new LayCCPayTransLstAdapter(this, layCCPayDbMgr.getCCTransStillToPay());
        layCCPayTransListView.setAdapter(layCCPayTransLstAdapter);

        layCCPayCCPaytListView = findViewById(R.id.mainPayCCPaytListView);
        layCCPayPaytLstAdapter = new LayCCPayPaytLstAdapter(this, layCCPayDbMgr.getDebts());
        layCCPayCCPaytListView.setAdapter(layCCPayPaytLstAdapter);

        layCCPayDbMgr.resetToPay();
        layCCPayDbMgr.resetDebtToPay();

        if (layCCPayTransLstAdapter.getCount() == 0) {
            layCCPayNoCCTransTV.setVisibility(View.VISIBLE);
            layCCPaycheckBelowLabel.setVisibility(View.GONE);
            layCCPayPaytListLayout.setVisibility(View.GONE);
            layCCPayToPayHeaderLayout.setVisibility(View.GONE);
            layCCPayTransListView.setVisibility(View.GONE);
        } else {
            layCCPayCheckToPayTotal();
        }

        layCCPayCheckbox.setOnCheckedChangeListener(onCheckLayCCPayCheckbox);
    }

    public void layCCPayRefresh() {
        layCCPayRefresh = new Intent(LayoutCCPay.this, LayoutCCPay.class);
        layCCPayRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPayRefresh);
    }

    public void layCCPayToMain() {
        layCCPayToMain = new Intent(LayoutCCPay.this, MainActivity.class);
        layCCPayToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPayToMain);
    }

    AdapterView.OnItemSelectedListener layCCPayFromSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            layCCPayFromSpinName = layCCPayCur3.getString(layCCPayCur3.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            layCCPayFromSpinId = layCCPayCur3.getLong(layCCPayCur3.getColumnIndexOrThrow(DbHelper.ID));
            layCCPayFromIsDebtSav = layCCPayCur3.getString(layCCPayCur3.getColumnIndexOrThrow(DbHelper.ACCTDEBTSAV));
            layCCPayFromAcctBal = layCCPayCur3.getDouble(layCCPayCur3.getColumnIndexOrThrow(DbHelper.ACCTBAL));
            layCCPayFromAcctMax = layCCPayCur3.getDouble(layCCPayCur3.getColumnIndexOrThrow(DbHelper.ACCTMAX));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    CompoundButton.OnCheckedChangeListener onCheckLayCCPayCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //pop up = are you sure?
            final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutCCPay.this);
            View v = getLayoutInflater().inflate(R.layout.dialog_cc_warn, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.show();

            Button dialogYesBtn = v.findViewById(R.id.dialogYesBtn);
            Button dialogNoBtn = v.findViewById(R.id.dialogNoBtn);
            dialogNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layCCPayRefresh();
                }
            });
            dialogYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (AccountsDb a : layCCPayDbMgr.getAccounts()) {
                        if (a.getAcctDebtToPay() > 0) {
                            layCCPayAmtToPay = a.getAcctDebtToPay();
                            layCCPayToAcctId = a.getId();
                            layCCPayToAcctName = a.getAcctName();
                            if (layCCPayFromIsDebtSav.equals("D")) {//IF PAID FROM A DEBT ACCT
                                layCCPayDbMgr.updateRecPlusPt1(layCCPayAmtToPay, layCCPayFromAcctBal, layCCPayFromSpinId);
                                for (AccountsDb d : layCCPayDbMgr.getDebts()) {
                                    if (d.getId() == layCCPayFromSpinId) {
                                        debtAmtFromDb = d.getAcctBal();
                                        debtLimitFromDb = d.getAcctMax();
                                        debtRateFromDb = d.getAcctIntRate();
                                        debtPaytFromDb = d.getAcctPaytsTo();
                                    }
                                }
                                layCCPayDbMgr.updateRecPt2(layCCPayGen.calcDebtDate(
                                        debtAmtFromDb,
                                        debtRateFromDb,
                                        debtPaytFromDb,
                                        getString(R.string.debt_paid),
                                        getString(R.string.too_far)), layCCPayFromSpinId);
                            } else if (layCCPayFromIsDebtSav.equals("S")) {//IF PAID FROM A SAVINGS ACCT
                                layCCPayDbMgr.updateRecMinusPt1(layCCPayAmtToPay, layCCPayFromAcctBal, layCCPayFromSpinId);
                                for (AccountsDb s : layCCPayDbMgr.getSavings()) {
                                    if (s.getId() == layCCPayFromSpinId) {
                                        savAmtFromDb = s.getAcctBal();
                                        savGoalFromDb = s.getAcctMax();
                                        savRateFromDb = s.getAcctIntRate();
                                        savPaytFromDb = s.getAcctPaytsTo();
                                    }
                                }
                                layCCPayDbMgr.updateRecPt2(layCCPayGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), layCCPayFromSpinId);
                            } else { //IF PAID FROM MAIN ACCT
                                layCCPayDbMgr.updateTotAcctBalMinus(layCCPayAmtToPay, layCCPayDbMgr.retrieveCurrentAccountBalance());
                                layCCPayDbMgr.updateAandBBalMinus(
                                        layCCPayDbMgr.retrieveAPortion(),
                                        layCCPayDbMgr.retrieveOwingPortion(),
                                        layCCPayDbMgr.retrieveBPortion(),
                                        layCCPayDbMgr.retrieveCurrentA(),
                                        layCCPayDbMgr.retrieveCurrentOwingA(),
                                        layCCPayDbMgr.retrieveCurrentB());
                                if (layCCPayDbMgr.retrieveCurrentOwingA() < 0) {
                                    layCCPayDbMgr.adjustCurrentAandB(layCCPayDbMgr.retrieveOwingPortion(), layCCPayDbMgr.retrieveAPortion(), layCCPayDbMgr.retrieveBPortion());
                                }
                            }
                            layCCPayTransDb = new TransactionsDb(
                                    "ccPayment",
                                    "N/A",
                                    "N/A",
                                    0,
                                    layCCPayAmtToPay,
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    layCCPayToAcctId,
                                    layCCPayToAcctName,
                                    "D",
                                    layCCPayFromSpinId,
                                    layCCPayFromSpinName,
                                    layCCPayFromIsDebtSav,
                                    "N/A",
                                    "N/A",
                                    "N/A",
                                    "N/A",
                                    layCCPayGen.createTimestamp(),
                                    0);
                            layCCPayDbMgr.addTransactions(layCCPayTransDb);
                        }
                    }

                    layCCPayDbMgr.updateAllDebtRecords();
                    layCCPayDbMgr.updatePaid();
                    layCCPayTransLstAdapter.notifyDataSetChanged();

                    layCCPayToMain();
                }
            });
        }
    };

    public void layCCPayPlusDebtToPay() {
        for (AccountsDb a : layCCPayDbMgr.getDebts()) {
            if (a.getId() == layCCPayChargingDebtIdFromTag) {
                debtToPayToChargingCCFromTag = a.getAcctDebtToPay();
            }
        }
        layCCPayNewDebtToPay = debtToPayToChargingCCFromTag + layCCPayPurAmtFromTag;

        layCCPayHelper = new DbHelper(this);
        layCCPayDb = layCCPayHelper.getWritableDatabase();
        layCCPayCV = new ContentValues();
        layCCPayCV.put(DbHelper.ACCTDEBTTOPAY, layCCPayNewDebtToPay);
        layCCPayDb.update(DbHelper.ACCOUNTS_TABLE_NAME, layCCPayCV, DbHelper.ID + "=" + layCCPayChargingDebtIdFromTag, null);
        layCCPayDb.close();
    }

    public void layCCPayMinusDebtToPay() {
        for (AccountsDb a2 : layCCPayDbMgr.getDebts()) {
            if (a2.getId() == layCCPayChargingDebtIdFromTag) {
                debtToPayToChargingCCFromTag = a2.getAcctDebtToPay();
            }
        }
        layCCPayNewDebtToPay = debtToPayToChargingCCFromTag - layCCPayPurAmtFromTag;

        layCCPayHelper2 = new DbHelper(this);
        layCCPayDb2 = layCCPayHelper2.getWritableDatabase();
        layCCPayCV2 = new ContentValues();
        layCCPayCV2.put(DbHelper.ACCTDEBTTOPAY, layCCPayNewDebtToPay);
        layCCPayDb2.update(DbHelper.ACCOUNTS_TABLE_NAME, layCCPayCV2, DbHelper.ID + "=" + layCCPayChargingDebtIdFromTag, null);
        layCCPayDb2.close();

    }

    public void layCCPayCheckToPayTotal() {
        if (layCCPayDbMgr.retrieveToPayTotal() == 0) {
            layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
            layCCPayToPayHeaderLayout.setVisibility(View.GONE);
            layCCPayPaytListLayout.setVisibility(View.GONE);
        } else {
            layCCPaycheckBelowLabel.setVisibility(View.GONE);
            layCCPayToPayHeaderLayout.setVisibility(View.VISIBLE);
            layCCPayPaytListLayout.setVisibility(View.VISIBLE);
        }
    }

    public void layCCPayAddToPayList() {
        layCCPayMonOutDb.setTransCCToPay("Y");
        layCCPayMonOutDb.setTransAmtOutA(layCCPayDbMgr.detAPortionExp(layCCPayPurAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB()));
        layCCPayMonOutDb.setTransAmtOutOwing(layCCPayDbMgr.detOwingPortionExp(layCCPayPurAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB()));
        layCCPayMonOutDb.setTransAmtOutB(layCCPayDbMgr.detBPortionExp(layCCPayPurAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB()));
        layCCPayDbMgr.updateTransactions(layCCPayMonOutDb);
    }

    public void layCCPayRemoveFromPayList() {
        layCCPayMonOutDb.setTransCCToPay("N");
        layCCPayMonOutDb.setTransAmtOutA(0.0);
        layCCPayMonOutDb.setTransAmtOutOwing(0.0);
        layCCPayMonOutDb.setTransAmtOutB(0.0);
        layCCPayDbMgr.updateTransactions(layCCPayMonOutDb);
    }

    public class LayCCPayTransLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> ccTransStillToPay;
        boolean[] checkedState;

        private LayCCPayTransLstAdapter(
                Context context,
                List<TransactionsDb> ccTransStillToPay) {

            super(context, -1, ccTransStillToPay);

            this.context = context;
            this.ccTransStillToPay = ccTransStillToPay;
            checkedState = new boolean[ccTransStillToPay.size()];
        }

        public void updateCCTransToPay(List<TransactionsDb> ccTransStillToPay) {
            this.ccTransStillToPay = ccTransStillToPay;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTransStillToPay.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final LayCCPayTransViewHolder layCCPayTransHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_pay_cc,
                        parent, false);

                layCCPayTransHldr = new LayCCPayTransViewHolder();
                layCCPayTransHldr.layCCPayLstAmtTV = convertView.findViewById(R.id.payCCLstAmtTV);
                layCCPayTransHldr.layCCPayLstCatTV = convertView.findViewById(R.id.payCCLstCatTV);
                layCCPayTransHldr.layCCPayLstChargingDebtCatTV = convertView.findViewById(R.id.payCCLstChargingDebtCatTV);
                layCCPayTransHldr.layCCPayLstCheckbox = convertView.findViewById(R.id.payCCLstCheckbox);
                convertView.setTag(layCCPayTransHldr);

            } else {
                layCCPayTransHldr = (LayCCPayTransViewHolder) convertView.getTag();
                layCCPayTransHldr.layCCPayLstCheckbox.setTag(layCCPayTransHldr);
            }

            layCCPayGen.dblASCurrency(String.valueOf(ccTransStillToPay.get(position).getTransAmt()), layCCPayTransHldr.layCCPayLstAmtTV);

            layCCPayTransHldr.layCCPayLstCatTV.setText(ccTransStillToPay.get(position).getTransBdgtCat());
            layCCPayTransHldr.layCCPayLstChargingDebtCatTV.setText(ccTransStillToPay.get(position).getTransFromAcctName());

            layCCPayTransHldr.layCCPayLstCheckbox.setTag(ccTransStillToPay.get(position));

            layCCPayTransHldr.layCCPayLstCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layCCPayMonOutDb = (TransactionsDb) layCCPayTransHldr.layCCPayLstCheckbox.getTag();
                    checkedState[position] = !checkedState[position];

                    layCCPayPriorityFromTag = layCCPayMonOutDb.getTransBdgtPriority();
                    layCCPayPurAmtFromTag = layCCPayMonOutDb.getTransAmt();
                    layCCPayChargingDebtIdFromTag = layCCPayMonOutDb.getTransFromAcctId();

                    if (checkedState[position]) {
                        layCCPayAddToPayList();
                        layCCPayPlusDebtToPay();
                        layCCPayToPayTotA = layCCPayDbMgr.retrieveAPortion();
                        layCCPayToPayTotB = layCCPayDbMgr.retrieveToPayTotal() - layCCPayToPayTotA;

                        if ((layCCPayFromIsDebtSav.equals("D") && (layCCPayFromAcctBal + layCCPayPurAmtFromTag > layCCPayFromAcctMax)) || //IF FROM DEBT & WILL GO OVER LIMIT
                                (layCCPayFromIsDebtSav.equals("S") && (layCCPayFromAcctBal - layCCPayPurAmtFromTag < 0)) || //IF FROM SAV & WILL GO NEGATIVE
                                (layCCPayFromSpinId == 1 && (layCCPayPriorityFromTag.equals("A") && (layCCPayDbMgr.retrieveCurrentAccountBalance() < layCCPayToPayTotA))) || //FROM MAIN ACCT & PRIORITY A & WILL GO NEGATIVE
                                (layCCPayFromSpinId == 1 && (layCCPayPriorityFromTag.equals("B") && (layCCPayDbMgr.retrieveCurrentB() < layCCPayToPayTotB)))) { //FORM MAIN ACCT & PRIORITY B & DON'T HAVE THE MONEY TO SPEND
                            layCCPaycheckBelowLabel.setVisibility(View.GONE);

                            final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutCCPay.this);
                            dView = getLayoutInflater().inflate(R.layout.dialog_warn, null);
                            builder.setView(dView);
                            final AlertDialog dialog = builder.create();
                            dialog.show();

                            warnDialogWarnLayout = dView.findViewById(R.id.warnDialogWarnLayout);
                            warnDialogWarnTV = dView.findViewById(R.id.warnDialogWarnTV);
                            warnDialogNoContBtn = dView.findViewById(R.id.warnDialogNoContBtn);
                            warnDialogYesContBtn = dView.findViewById(R.id.warnDialogYesContBtn);

                            if (layCCPayFromIsDebtSav.equals("D")) {
                                warnDialogWarnTV.setText(R.string.not_enough_credit_warning);
                            } else if (layCCPayFromIsDebtSav.equals("S")) {
                                warnDialogWarnTV.setText(R.string.not_enough_savings_warning);
                            } else if (layCCPayPriorityFromTag.equals("A")) {
                                warnDialogWarnTV.setText(R.string.payment_not_possible_A);
                            } else if (layCCPayPriorityFromTag.equals("B")) {
                                warnDialogWarnTV.setText(R.string.payment_not_possible_B);
                            }

                            warnDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (layCCPayDbMgr.retrieveToPayTotal() == 0) {
                                        layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
                                    } else {
                                        layCCPaycheckBelowLabel.setVisibility(View.GONE);
                                    }
                                    layCCPayRemoveFromPayList();
                                    layCCPayMinusDebtToPay();
                                    layCCPayTransHldr.layCCPayLstCheckbox.setChecked(false);
                                    checkedState[position] = false;
                                    dialog.dismiss();
                                    layCCPayPaytLstAdapter.notifyDataSetChanged();
                                }
                            });

                            warnDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    layCCPayCheckToPayTotal();
                                    layCCPayPaytLstAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            layCCPayCheckToPayTotal();
                            layCCPayPaytLstAdapter.notifyDataSetChanged();
                        }
                    } else if (!checkedState[position]) {
                        layCCPayRemoveFromPayList();
                        layCCPayMinusDebtToPay();
                        layCCPayCheckToPayTotal();
                        layCCPayPaytLstAdapter.notifyDataSetChanged();
                    }
                }
            });

            layCCPayTransHldr.layCCPayLstCheckbox.setChecked(checkedState[position]);

            return convertView;
        }
    }

    private static class LayCCPayTransViewHolder {
        public TextView layCCPayLstAmtTV;
        public TextView layCCPayLstCatTV;
        public TextView layCCPayLstChargingDebtCatTV;
        public CheckBox layCCPayLstCheckbox;
    }

    public class LayCCPayPaytLstAdapter extends ArrayAdapter<AccountsDb> {

        private Context context;
        private List<AccountsDb> debts;

        private LayCCPayPaytLstAdapter(
                Context context,
                List<AccountsDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<AccountsDb> debts) {
            this.debts = debts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return debts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView2, @NonNull ViewGroup parent) {

            final LayCCPayPaytViewHolder layCCPayPaytHldr;

            if (convertView2 == null) {
                convertView2 = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_6_2tv,
                        parent, false);

                layCCPayPaytHldr = new LayCCPayPaytViewHolder();
                layCCPayPaytHldr.layCCPayPaytCatTV = convertView2.findViewById(R.id.list6CatTV);
                layCCPayPaytHldr.layCCPayPaytAmtTV = convertView2.findViewById(R.id.list6AmtTV);
                convertView2.setTag(layCCPayPaytHldr);

            } else {
                layCCPayPaytHldr = (LayCCPayPaytViewHolder) convertView2.getTag();
            }

            layCCPayId = debts.get(position).getId();
            layCCPayPaytHldr.layCCPayPaytCatTV.setText(debts.get(position).getAcctName());

            //retrieve amount due in each category and format as currency
            List<Double> layCCPayTotDueList = new ArrayList<>();
            for (TransactionsDb m2 : layCCPayDbMgr.getMoneyOuts()) {
                if (m2.getTransFromAcctId() == layCCPayId && m2.getTransCCToPay().equals("Y") && m2.getTransCCPaid().equals("N")) {
                    layCCPayTotDueList.add(m2.getTransAmt());
                }
            }
            layCCPayAmtDue = 0.0;
            if (layCCPayTotDueList.size() == 0) {
                layCCPayAmtDue = 0.0;
            } else {
                for (Double dbl : layCCPayTotDueList) {
                    layCCPayAmtDue += dbl;
                }
            }
            layCCPayGen.dblASCurrency(String.valueOf(layCCPayAmtDue), layCCPayPaytHldr.layCCPayPaytAmtTV);

            if (layCCPayAmtDue == 0.0) {
                layCCPayPaytHldr.layCCPayPaytCatTV.setVisibility(View.GONE);
                layCCPayPaytHldr.layCCPayPaytAmtTV.setVisibility(View.GONE);
            } else {
                layCCPayPaytHldr.layCCPayPaytCatTV.setVisibility(View.VISIBLE);
                layCCPayPaytHldr.layCCPayPaytAmtTV.setVisibility(View.VISIBLE);
            }

            return convertView2;
        }
    }

    private static class LayCCPayPaytViewHolder {
        public TextView layCCPayPaytCatTV;
        public TextView layCCPayPaytAmtTV;
    }


}
