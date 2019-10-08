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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class LayoutCCPay extends MainNavigation {

    Button layCCPayNoBtn, layCCPayYesBtn;
    CheckBox layCCPayCheckbox;
    ContentValues layCCPayCV, layCCPayCV2;
    DbHelper layCCPayHelper, layCCPayHelper2;
    DbManager layCCPayDbMgr;
    Double debtToPayFromTag = 0.0, layCCPayAmtDue = 0.0, layCCPayAmtFromTag = 0.0, layCCPayNewDebtToPay = 0.0, layCCPayToPayTotA = 0.0, layCCPayToPayTotB = 0.0,
            moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0;
    General layCCPayGen;
    Intent layCCPayRefresh, layCCPayToMain;
    LayCCPayPaytLstAdapter layCCPayPaytLstAdapter;
    LayCCPayTransLstAdapter layCCPayTransLstAdapter;
    LinearLayout layCCPayPaytListLayout, layCCPayWarnLayout;
    ListView layCCPayCCPaytListView, layCCPayTransListView;
    long layCCPayChargingDebtIdFromTag, layCCPayId;
    MoneyOutDb layCCPayMonOutDb;
    RelativeLayout layCCPayToPayHeaderLayout;
    SQLiteDatabase layCCPayDb, layCCPayDb2;
    String layCCPayPriorityFromTag = null;
    TextView layCCPaycheckBelowLabel, layCCPayNoCCTransTV, layCCPayWarnTV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main_pay_cc);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layCCPayDbMgr = new DbManager(this);
        layCCPayGen = new General();

        layCCPayNoCCTransTV = findViewById(R.id.mainPayCCHeaderTV);
        layCCPayNoCCTransTV.setVisibility(View.GONE);
        layCCPayWarnTV = findViewById(R.id.mainPayCCWarnTV);
        layCCPayYesBtn = findViewById(R.id.mainPayCCYesBtn);
        layCCPayNoBtn = findViewById(R.id.mainPayCCNoBtn);
        layCCPayWarnLayout = findViewById(R.id.mainPayCCWarnLayout);
        layCCPayWarnLayout.setVisibility(View.GONE);
        layCCPaycheckBelowLabel = findViewById(R.id.mainPayCCCheckBelowLabel);
        layCCPayPaytListLayout = findViewById(R.id.mainPayCCPaytListLayout);
        layCCPayPaytListLayout.setVisibility(View.GONE);
        layCCPayToPayHeaderLayout = findViewById(R.id.mainPayCCToPayHeaderLayout);
        layCCPayCheckbox = findViewById(R.id.mainPayCCCheckbox);

        layCCPayTransListView = findViewById(R.id.mainPayCCTransListView);
        layCCPayTransLstAdapter = new LayCCPayTransLstAdapter(this, layCCPayDbMgr.getCCTransToPay());
        layCCPayTransListView.setAdapter(layCCPayTransLstAdapter);

        layCCPayCCPaytListView = findViewById(R.id.mainPayCCPaytListView);
        layCCPayPaytLstAdapter = new LayCCPayPaytLstAdapter(this, layCCPayDbMgr.getDebts());
        layCCPayCCPaytListView.setAdapter(layCCPayPaytLstAdapter);

        layCCPayDbMgr.resetToPay();
        layCCPayDbMgr.resetDebtToPay();

        if (layCCPayTransLstAdapter.getCount() == 0) {
            layCCPayNoCCTransTV.setVisibility(View.VISIBLE);
            layCCPayWarnLayout.setVisibility(View.GONE);
            layCCPaycheckBelowLabel.setVisibility(View.GONE);
            layCCPayPaytListLayout.setVisibility(View.GONE);
            layCCPayToPayHeaderLayout.setVisibility(View.GONE);
            layCCPayTransListView.setVisibility(View.GONE);
        } else {
            layCCPayCheckToPayTotal();
            /*if (layCCPayDbMgr.retrieveToPayTotal() == 0) {
                layCCPayNoCCTransTV.setVisibility(View.GONE);
                layCCPayWarnLayout.setVisibility(View.GONE);
                layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
                layCCPayPaytListLayout.setVisibility(View.GONE);
                layCCPayToPayHeaderLayout.setVisibility(View.VISIBLE);
                layCCPayTransListView.setVisibility(View.VISIBLE);
            } else {
                layCCPayNoCCTransTV.setVisibility(View.GONE);
                layCCPayWarnLayout.setVisibility(View.GONE);
                layCCPaycheckBelowLabel.setVisibility(View.GONE);
                layCCPayPaytListLayout.setVisibility(View.VISIBLE);
                layCCPayToPayHeaderLayout.setVisibility(View.VISIBLE);
                layCCPayTransListView.setVisibility(View.VISIBLE);
            }*/
        }

        layCCPayCheckbox.setOnCheckedChangeListener(onCheckLayCCPayCheckbox);
    }

    public void layCCPayToMain() {
        layCCPayToMain = new Intent(LayoutCCPay.this, MainActivity.class);
        layCCPayToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPayToMain);
    }

    public void layCCPayRefresh() {
        layCCPayRefresh = new Intent(LayoutCCPay.this, LayoutCCPay.class);
        layCCPayRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPayRefresh);
    }

    CompoundButton.OnCheckedChangeListener onCheckLayCCPayCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            layCCPayDbMgr.updateTotAcctBalMinus(layCCPayDbMgr.retrieveToPayTotal(), layCCPayDbMgr.retrieveCurrentAccountBalance());
            layCCPayDbMgr.updateAvailBalMinus(layCCPayDbMgr.retrieveAPortion(), layCCPayDbMgr.retrieveOwingPortion(), layCCPayDbMgr.retrieveBPortion(), layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentOwingA(), layCCPayDbMgr.retrieveCurrentB());
            if (layCCPayDbMgr.retrieveCurrentOwingA() < 0) {
                layCCPayDbMgr.adjustCurrentAandB(layCCPayDbMgr.retrieveOwingPortion(), layCCPayDbMgr.retrieveAPortion(), layCCPayDbMgr.retrieveBPortion());
            }

            layCCPayDbMgr.updateAllDebtRecords();
            layCCPayDbMgr.updatePaid();
            layCCPayTransLstAdapter.notifyDataSetChanged();

            layCCPayToMain();
        }
    };

    public void layCCPayPlusDebtToPay() {
        for (AccountsDb a : layCCPayDbMgr.getDebts()) {
            if (a.getId() == layCCPayChargingDebtIdFromTag) {
                debtToPayFromTag = a.getDebtToPay();
            }
        }
        layCCPayNewDebtToPay = debtToPayFromTag + layCCPayAmtFromTag;

        layCCPayHelper = new DbHelper(this);
        layCCPayDb = layCCPayHelper.getWritableDatabase();
        layCCPayCV = new ContentValues();
        layCCPayCV.put(DbHelper.DEBTTOPAY, layCCPayNewDebtToPay);
        layCCPayDb.update(DbHelper.ACCOUNTS_TABLE_NAME, layCCPayCV, DbHelper.ID + "=" + layCCPayChargingDebtIdFromTag, null);
        layCCPayDb.close();
    }

    public void layCCPayMinusDebtToPay() {
        for (AccountsDb a2 : layCCPayDbMgr.getDebts()) {
            if (a2.getId() == layCCPayChargingDebtIdFromTag) {
                debtToPayFromTag = a2.getDebtToPay();
            }
        }
        layCCPayNewDebtToPay = debtToPayFromTag - layCCPayAmtFromTag;

        layCCPayHelper2 = new DbHelper(this);
        layCCPayDb2 = layCCPayHelper2.getWritableDatabase();
        layCCPayCV2 = new ContentValues();
        layCCPayCV2.put(DbHelper.DEBTTOPAY, layCCPayNewDebtToPay);
        layCCPayDb2.update(DbHelper.ACCOUNTS_TABLE_NAME, layCCPayCV2, DbHelper.ID + "=" + layCCPayChargingDebtIdFromTag, null);
        layCCPayDb2.close();

    }

    public void layCCPayCheckToPayTotal() {
        if (layCCPayDbMgr.retrieveToPayTotal() == 0) {
            layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
            layCCPayToPayHeaderLayout.setVisibility(View.GONE);
            layCCPayWarnLayout.setVisibility(View.GONE);
            layCCPayPaytListLayout.setVisibility(View.GONE);
        } else {
            layCCPaycheckBelowLabel.setVisibility(View.GONE);
            layCCPayToPayHeaderLayout.setVisibility(View.VISIBLE);
            layCCPayPaytListLayout.setVisibility(View.VISIBLE);
            layCCPayWarnLayout.setVisibility(View.GONE);
        }
    }

    public void layCCPayDetAandBPortionsExp() {
        moneyOutA = layCCPayDbMgr.detAPortionExp(layCCPayAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB());
        moneyOutOwing = layCCPayDbMgr.detOwingPortionExp(layCCPayAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB());
        moneyOutB = layCCPayDbMgr.detBPortionExp(layCCPayAmtFromTag, layCCPayPriorityFromTag, layCCPayDbMgr.retrieveCurrentA(), layCCPayDbMgr.retrieveCurrentB());
    }

    public void layCCPayAddToPayList() {
        layCCPayMonOutDb.setMoneyOutToPay(1);
        layCCPayMonOutDb.setMoneyOutA(moneyOutA);
        layCCPayMonOutDb.setMoneyOutOwing(moneyOutOwing);
        layCCPayMonOutDb.setMoneyOutB(moneyOutB);
        layCCPayDbMgr.updateMoneyOut(layCCPayMonOutDb);
    }

    public void layCCPayRemoveFromPayList() {
        layCCPayMonOutDb.setMoneyOutToPay(0);
        layCCPayMonOutDb.setMoneyOutA(0.0);
        layCCPayMonOutDb.setMoneyOutOwing(0.0);
        layCCPayMonOutDb.setMoneyOutB(0.0);
        layCCPayDbMgr.updateMoneyOut(layCCPayMonOutDb);
    }

    public class LayCCPayTransLstAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTransToPay;
        boolean[] checkedState;

        private LayCCPayTransLstAdapter(
                Context context,
                List<MoneyOutDb> ccTransToPay) {

            super(context, -1, ccTransToPay);

            this.context = context;
            this.ccTransToPay = ccTransToPay;
            checkedState = new boolean[ccTransToPay.size()];
        }

        public void updateCCTransToPay(List<MoneyOutDb> ccTransToPay) {
            this.ccTransToPay = ccTransToPay;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTransToPay.size();
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

            layCCPayGen.dblASCurrency(String.valueOf(ccTransToPay.get(position).getMoneyOutAmount()), layCCPayTransHldr.layCCPayLstAmtTV);

            layCCPayTransHldr.layCCPayLstCatTV.setText(ccTransToPay.get(position).getMoneyOutCat());
            layCCPayTransHldr.layCCPayLstChargingDebtCatTV.setText(ccTransToPay.get(position).getMoneyOutDebtCat());

            layCCPayTransHldr.layCCPayLstCheckbox.setTag(ccTransToPay.get(position));
            layCCPayTransHldr.layCCPayLstCheckbox.setTag(R.id.payCCLstCheckbox, position);

            layCCPayTransHldr.layCCPayLstCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layCCPayMonOutDb = (MoneyOutDb) layCCPayTransHldr.layCCPayLstCheckbox.getTag();
                    checkedState[position] = !checkedState[position];

                    layCCPayPriorityFromTag = layCCPayMonOutDb.getMoneyOutPriority();
                    layCCPayAmtFromTag = layCCPayMonOutDb.getMoneyOutAmount();
                    layCCPayChargingDebtIdFromTag = layCCPayMonOutDb.getMoneyOutChargingDebtId();

                    if (checkedState[position]) {
                        layCCPayDetAandBPortionsExp();
                        layCCPayAddToPayList();
                        layCCPayPlusDebtToPay();
                        layCCPayToPayTotA = layCCPayDbMgr.retrieveAPortion();
                        layCCPayToPayTotB = layCCPayDbMgr.retrieveToPayTotal() - layCCPayToPayTotA;

                        if (layCCPayPriorityFromTag.equals("A")) {
                            if (layCCPayDbMgr.retrieveCurrentAccountBalance() < layCCPayToPayTotA) { //A NOT POSSIBLE
                                layCCPaycheckBelowLabel.setVisibility(View.GONE);
                                layCCPayWarnLayout.setVisibility(View.VISIBLE);
                                layCCPayWarnTV.setText(getString(R.string.payment_not_possible_A));
                                layCCPayNoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPayWarnLayout.setVisibility(View.GONE);
                                        layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
                                        layCCPayRemoveFromPayList();
                                        layCCPayMinusDebtToPay();
                                        layCCPayTransHldr.layCCPayLstCheckbox.setChecked(false);
                                    }
                                });

                                layCCPayYesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPayWarnLayout.setVisibility(View.GONE);
                                        //layCCPayPlusDebtToPay();
                                        layCCPayCheckToPayTotal();
                                        layCCPayPaytLstAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                //layCCPayPlusDebtToPay();
                                layCCPayCheckToPayTotal();
                                layCCPayPaytLstAdapter.notifyDataSetChanged();
                            }
                        } else if (layCCPayPriorityFromTag.equals("B")) {
                            if (layCCPayDbMgr.retrieveCurrentB() < layCCPayToPayTotB) { //B NOT POSSIBLE
                                layCCPaycheckBelowLabel.setVisibility(View.GONE);
                                layCCPayWarnLayout.setVisibility(View.VISIBLE);
                                layCCPayWarnTV.setText(getString(R.string.payment_not_possible_B));
                                layCCPayNoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPayWarnLayout.setVisibility(View.GONE);
                                        layCCPaycheckBelowLabel.setVisibility(View.VISIBLE);
                                        layCCPayRemoveFromPayList();
                                        layCCPayMinusDebtToPay();
                                        layCCPayTransHldr.layCCPayLstCheckbox.setChecked(false);
                                    }
                                });

                                layCCPayYesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPayWarnLayout.setVisibility(View.GONE);
                                        //layCCPayPlusDebtToPay();
                                        layCCPayCheckToPayTotal();
                                        layCCPayPaytLstAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                //layCCPayPlusDebtToPay();
                                layCCPayCheckToPayTotal();
                                layCCPayPaytLstAdapter.notifyDataSetChanged();
                            }
                        }

                        //layCCPayCheckToPayTotal();
                        //layCCPayPaytLstAdapter.notifyDataSetChanged();

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
            for (MoneyOutDb m2 : layCCPayDbMgr.getMoneyOuts()) {
                if (m2.getMoneyOutChargingDebtId() == layCCPayId && m2.getMoneyOutToPay() == 1 && m2.getMoneyOutPaid() == 0) {
                    layCCPayTotDueList.add(m2.getMoneyOutAmount());
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
