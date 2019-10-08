/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.MainActivity;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyMoneyCCPay extends Fragment {

    boolean aPoss = true, bPoss = true;
    Button monCC3NoContBtn, monCC3YesContBtn;
    MonCC3PaytLstAdapter monCC3PaytLstAdapter;
    CheckBox monCC3Checkbox;
    ContentValues monCC3CV, monCC3CV2;
    Cursor monCC3Cur;
    DbHelper monCC3Helper, monCC3Helper2, monCC3Helper3;
    DbManager monCC3DbMgr;
    Double monCC3AmtDue = 0.0, monCC3AmtMissing = 0.0, monCC3CurrAcctBal = 0.0, monCC3TransAmt = 0.0, monCC3CurDebtToPay = 0.0, monCC3NewDebtToPay = 0.0, monOutAmtFromTag = 0.0, moneyOutA = 0.0,
            moneyOutOwing = 0.0, moneyOutB = 0.0;
    General monCC3Gen;
    Intent monCC3ToMain;
    LinearLayout monCC3PaytListLayout, monCC3WarnLayout;
    ListView monCC3TransListView, monCC3CCPaytListView;
    List<Double> monCC3TotDueList;
    long monCC3Id, monOutChargingDebtIdFromTag;
    MonCC3TransLstAdapter monCC3TransLstAdapter;
    MoneyOutDb monCC3MonOutDb;
    RelativeLayout monCC3ToPayHeaderLayout;
    SQLiteDatabase monCC3Db, monCC3Db2, monCC3Db3;
    String monOutPriorityFromTag = null;
    TextView monCC3checkBelowLabel, monCC3NoCCTransTV, monCC3WarnTV;
    View v;

    public DailyMoneyCCPay() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.d2_frag_pay_cc, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monCC3DbMgr = new DbManager(getContext());
        monCC3Gen = new General();

        monCC3NoCCTransTV = v.findViewById(R.id.payCCNoCCTransTV);
        monCC3NoCCTransTV.setVisibility(View.GONE);
        monCC3WarnTV = v.findViewById(R.id.payCCWarnTV);
        monCC3YesContBtn = v.findViewById(R.id.payCCYesContBtn);
        monCC3NoContBtn = v.findViewById(R.id.payCCNoContBtn);
        monCC3WarnLayout = v.findViewById(R.id.payCCWarnLayout);
        monCC3WarnLayout.setVisibility(View.GONE);
        monCC3checkBelowLabel = v.findViewById(R.id.payCCCheckBelowLabel);
        monCC3PaytListLayout = v.findViewById(R.id.payCCPaytListLayout);
        monCC3PaytListLayout.setVisibility(View.GONE);
        monCC3ToPayHeaderLayout = v.findViewById(R.id.payCCToPayHeaderLayout);
        monCC3Checkbox = v.findViewById(R.id.payCCCheckbox);

        monCC3TransListView = v.findViewById(R.id.payCCTransListView);
        monCC3TransLstAdapter = new MonCC3TransLstAdapter(getContext(), monCC3DbMgr.getCCTransToPay());
        monCC3TransListView.setAdapter(monCC3TransLstAdapter);

        monCC3CCPaytListView = v.findViewById(R.id.payCCPaytListView);
        monCC3PaytLstAdapter = new MonCC3PaytLstAdapter(getContext(), monCC3DbMgr.getDebts());
        monCC3CCPaytListView.setAdapter(monCC3PaytLstAdapter);

        if (monCC3TransLstAdapter.getCount() == 0) {
            monCC3NoCCTransTV.setVisibility(View.VISIBLE);
            monCC3WarnLayout.setVisibility(View.GONE);
            monCC3checkBelowLabel.setVisibility(View.GONE);
            monCC3PaytListLayout.setVisibility(View.GONE);
            monCC3ToPayHeaderLayout.setVisibility(View.GONE);
            monCC3TransListView.setVisibility(View.GONE);
        } else {
            if (monCC3DbMgr.retrieveToPayTotal() == 0) {
                monCC3NoCCTransTV.setVisibility(View.GONE);
                monCC3WarnLayout.setVisibility(View.GONE);
                monCC3checkBelowLabel.setVisibility(View.VISIBLE);
                monCC3PaytListLayout.setVisibility(View.GONE);
                monCC3ToPayHeaderLayout.setVisibility(View.VISIBLE);
                monCC3TransListView.setVisibility(View.VISIBLE);
            } else {
                monCC3NoCCTransTV.setVisibility(View.GONE);
                monCC3WarnLayout.setVisibility(View.GONE);
                monCC3checkBelowLabel.setVisibility(View.GONE);
                monCC3PaytListLayout.setVisibility(View.VISIBLE);
                monCC3ToPayHeaderLayout.setVisibility(View.VISIBLE);
                monCC3TransListView.setVisibility(View.VISIBLE);
            }
        }

        monCC3DbMgr.resetToPay();
        monCC3DbMgr.resetDebtToPay();

        monCC3Checkbox.setOnCheckedChangeListener(onCheckMonCC3Checkbox);
    }

    CompoundButton.OnCheckedChangeListener onCheckMonCC3Checkbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //monCC3CurrAcctBal = monCC3DbMgr.retrieveCurrentAccountBalance();
            monCC3DbMgr.updateTotAcctBalMinus(monCC3DbMgr.retrieveToPayTotal(), monCC3DbMgr.retrieveCurrentAccountBalance());
            monCC3DbMgr.updateAvailBalMinus(monCC3DbMgr.retrieveAPortion(), monCC3DbMgr.retrieveOwingPortion(), monCC3DbMgr.retrieveBPortion(), monCC3DbMgr.retrieveCurrentA(), monCC3DbMgr.retrieveCurrentOwingA(), monCC3DbMgr.retrieveCurrentB());
            if (monCC3DbMgr.retrieveCurrentOwingA() < 0) {
                monCC3DbMgr.adjustCurrentAandB(monCC3DbMgr.retrieveOwingPortion(), monCC3DbMgr.retrieveAPortion(), monCC3DbMgr.retrieveBPortion());
            }
            monCC3UpdateAllDebtRecords();
            monCC3DbMgr.updatePaid();
            monCC3TransLstAdapter.notifyDataSetChanged();
            monCC3ToMain = new Intent(getContext(), MainActivity.class);
            monCC3ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC3ToMain);
        }
    };

    public void monCC3PlusDebtToPay() {
        for (DebtDb d : monCC3DbMgr.getDebts()) {
            if (d.getId() == monOutChargingDebtIdFromTag) {
                monCC3CurDebtToPay = d.getDebtToPay();
            }
        }
        monCC3NewDebtToPay = monCC3CurDebtToPay + monOutAmtFromTag;

        monCC3Helper = new DbHelper(getContext());
        monCC3Db = monCC3Helper.getWritableDatabase();
        monCC3CV = new ContentValues();
        monCC3CV.put(DbHelper.DEBTTOPAY, monCC3NewDebtToPay);
        monCC3Db.update(DbHelper.DEBTS_TABLE_NAME, monCC3CV, DbHelper.ID + "=" + monOutChargingDebtIdFromTag, null);
        monCC3Db.close();
    }

    public void monCC3MinusDebtToPay() {
        for (DebtDb d2 : monCC3DbMgr.getDebts()) {
            if (d2.getId() == monOutChargingDebtIdFromTag) {
                monCC3CurDebtToPay = d2.getDebtToPay();
            }
        }
        monCC3NewDebtToPay = monCC3CurDebtToPay - monOutAmtFromTag;

        monCC3Helper2 = new DbHelper(getContext());
        monCC3Db2 = monCC3Helper2.getWritableDatabase();
        monCC3CV2 = new ContentValues();
        monCC3CV2.put(DbHelper.DEBTTOPAY, monCC3NewDebtToPay);
        monCC3Db2.update(DbHelper.DEBTS_TABLE_NAME, monCC3CV2, DbHelper.ID + "=" + monOutChargingDebtIdFromTag, null);
        monCC3Db2.close();

    }

    public void monCC3CheckToPayTotal() {
        if (monCC3DbMgr.retrieveToPayTotal() == 0) {
            monCC3checkBelowLabel.setVisibility(View.VISIBLE);
            monCC3WarnLayout.setVisibility(View.GONE);
            monCC3PaytListLayout.setVisibility(View.GONE);
        } else {
            monCC3checkBelowLabel.setVisibility(View.GONE);
            monCC3ToPayHeaderLayout.setVisibility(View.VISIBLE);
            monCC3PaytListLayout.setVisibility(View.VISIBLE);
            monCC3WarnLayout.setVisibility(View.GONE);
        }
    }

    public void monCC3UpdateAllDebtRecords() {
        monCC3Helper3 = new DbHelper(getContext());
        monCC3Db3 = monCC3Helper3.getReadableDatabase();
        monCC3Db3.beginTransaction();
        monCC3Cur = monCC3Db3.rawQuery("SELECT " + DbHelper.DEBTOWING + ", " + DbHelper.DEBTTOPAY + " FROM " + DbHelper.DEBTS_TABLE_NAME + " WHERE " + DbHelper.DEBTTOPAY + " > 0", null);
        monCC3Db3.execSQL("UPDATE " + DbHelper.DEBTS_TABLE_NAME + " SET " + DbHelper.DEBTOWING + " = " + DbHelper.DEBTOWING + " - " + DbHelper.DEBTTOPAY);
        monCC3Cur.close();
        monCC3Db3.setTransactionSuccessful();
        monCC3Db3.endTransaction();
    }

    public void monCC3DetAandBPortionsExp(Double dbl1, String str1) {
        //dbl1 = moneyOutAmt from source or entry or tag
        //str1 = priority from method

        if (str1.equals("A")) {
            if (monCC3DbMgr.retrieveCurrentA() >= dbl1) { //if A can cover the purchase, it does
                moneyOutA = dbl1;
                moneyOutOwing = 0.0;
                moneyOutB = 0.0;
            } else if (monCC3DbMgr.retrieveCurrentA() <= 0) { //if A has no money
                if (monCC3DbMgr.retrieveCurrentB() >= dbl1) { //if B can cover the purchase, it does
                    moneyOutA = 0.0;
                    moneyOutOwing = 0.0;
                    moneyOutB = dbl1;
                } else if (monCC3DbMgr.retrieveCurrentB() == 0) { //if B has no money, A goes negative by whole amount
                    moneyOutA = dbl1;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    monCC3AmtMissing = dbl1 - monCC3DbMgr.retrieveCurrentB();
                    moneyOutA = monCC3AmtMissing;
                    moneyOutOwing = 0.0;
                    moneyOutB = monCC3DbMgr.retrieveCurrentB();
                }
            } else { //if A can cover part of the purchase
                monCC3AmtMissing = dbl1 - monCC3DbMgr.retrieveCurrentA();
                if (monCC3DbMgr.retrieveCurrentB() >= monCC3AmtMissing) { //if B can cover the rest, it does
                    moneyOutA = monCC3DbMgr.retrieveCurrentA();
                    moneyOutOwing = 0.0;
                    moneyOutB = monCC3AmtMissing;
                } else if (monCC3DbMgr.retrieveCurrentB() == 0) { //if B has no money, A goes negative by the rest
                    moneyOutA = dbl1;
                    moneyOutOwing = 0.0;
                    moneyOutB = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutA = dbl1 - monCC3DbMgr.retrieveCurrentB();
                    moneyOutOwing = 0.0;
                    moneyOutB = monCC3DbMgr.retrieveCurrentB();
                }
            }
        } else if (str1.equals("B")) {
            if (monCC3DbMgr.retrieveCurrentB() >= dbl1) { //if B can cover the purchase, it does
                moneyOutA = 0.0;
                moneyOutOwing = 0.0;
                moneyOutB = dbl1;
            } else if (monCC3DbMgr.retrieveCurrentB() == 0) { //if B has no money, A covers it but is owed for it
                moneyOutA = dbl1;
                moneyOutOwing = dbl1;
                moneyOutB = 0.0;
            } else { //if B can cover part of the purchase then A covers the rest and is owed for it
                monCC3AmtMissing = dbl1 - monCC3DbMgr.retrieveCurrentB();
                moneyOutA = monCC3AmtMissing;
                moneyOutOwing = monCC3AmtMissing;
                moneyOutB = monCC3DbMgr.retrieveCurrentB();
            }
        }
    }

    public class MonCC3TransLstAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTransToPay;
        boolean[] checkedState;

        private MonCC3TransLstAdapter(
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

            final monCC3TransViewHolder monCC3TransHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_pay_cc,
                        parent, false);

                monCC3TransHldr = new monCC3TransViewHolder();
                monCC3TransHldr.monCC3LstAmtTV = convertView.findViewById(R.id.payCCLstAmtTV);
                monCC3TransHldr.monCC3LstCatTV = convertView.findViewById(R.id.payCCLstCatTV);
                monCC3TransHldr.monCC3LstChargingDebtCatTV = convertView.findViewById(R.id.payCCLstChargingDebtCatTV);
                monCC3TransHldr.monCC3LstCheckbox = convertView.findViewById(R.id.payCCLstCheckbox);
                convertView.setTag(monCC3TransHldr);

            } else {
                monCC3TransHldr = (monCC3TransViewHolder) convertView.getTag();
                monCC3TransHldr.monCC3LstCheckbox.setTag(monCC3TransHldr);
            }

            monCC3TransAmt = ccTransToPay.get(position).getMoneyOutAmount();
            monCC3Gen.dblASCurrency(String.valueOf(monCC3TransAmt), monCC3TransHldr.monCC3LstAmtTV);

            monCC3TransHldr.monCC3LstCatTV.setText(ccTransToPay.get(position).getMoneyOutCat());
            monCC3TransHldr.monCC3LstChargingDebtCatTV.setText(ccTransToPay.get(position).getMoneyOutDebtCat());

            monCC3TransHldr.monCC3LstCheckbox.setTag(ccTransToPay.get(position));
            monCC3TransHldr.monCC3LstCheckbox.setTag(R.id.payCCLstCheckbox, position);

            monCC3TransHldr.monCC3LstCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monCC3MonOutDb = (MoneyOutDb) monCC3TransHldr.monCC3LstCheckbox.getTag();
                    checkedState[position] = !checkedState[position];

                    monOutPriorityFromTag = monCC3MonOutDb.getMoneyOutPriority();
                    monOutAmtFromTag = monCC3MonOutDb.getMoneyOutAmount();
                    monOutChargingDebtIdFromTag = monCC3MonOutDb.getMoneyOutChargingDebtId();

                    if (checkedState[position]) {
                        if (monOutPriorityFromTag.equals("A")) {
                            if(monCC3DbMgr.retrieveCurrentAccountBalance() - monOutAmtFromTag < 0) { //A NOT POSSIBLE
                            //monCC3DbMgr.checkIfAPoss(monOutAmtFromTag);
                            //if (!aPoss) {
                                monCC3WarnLayout.setVisibility(View.VISIBLE);
                                monCC3WarnTV.setText(getString(R.string.payment_not_possible_A));
                                monCC3NoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC3WarnLayout.setVisibility(View.GONE);
                                        monCC3TransHldr.monCC3LstCheckbox.setChecked(false);
                                    }
                                });

                                monCC3YesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC3WarnLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }

                        if (monOutPriorityFromTag.equals("B")) {
                            if(monCC3DbMgr.retrieveCurrentB() - monOutAmtFromTag < 0) {
                            //monCC3DbMgr.checkIfBPoss(monOutAmtFromTag);
                            //if (!bPoss) {
                                monCC3WarnLayout.setVisibility(View.VISIBLE);
                                monCC3WarnTV.setText(getString(R.string.payment_not_possible_B));
                                monCC3NoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC3WarnLayout.setVisibility(View.GONE);
                                        monCC3TransHldr.monCC3LstCheckbox.setChecked(false);
                                    }
                                });

                                monCC3YesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC3WarnLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }

                        monCC3DetAandBPortionsExp(monOutAmtFromTag, monOutPriorityFromTag);

                        monCC3MonOutDb.setMoneyOutToPay(1);
                        monCC3MonOutDb.setMoneyOutA(moneyOutA);
                        monCC3MonOutDb.setMoneyOutOwing(moneyOutOwing);
                        monCC3MonOutDb.setMoneyOutB(moneyOutB);
                        monCC3DbMgr.updateMoneyOut(monCC3MonOutDb);

                        monCC3PlusDebtToPay();
                        monCC3CheckToPayTotal();
                        monCC3PaytLstAdapter.notifyDataSetChanged();

                    } else if (!checkedState[position]) {
                        monCC3MonOutDb.setMoneyOutToPay(0);
                        monCC3MonOutDb.setMoneyOutA(0.0);
                        monCC3MonOutDb.setMoneyOutOwing(0.0);
                        monCC3MonOutDb.setMoneyOutB(0.0);
                        monCC3DbMgr.updateMoneyOut(monCC3MonOutDb);
                        monCC3MinusDebtToPay();
                        monCC3CheckToPayTotal();
                        monCC3PaytLstAdapter.notifyDataSetChanged();
                    }
                }
            });

            monCC3TransHldr.monCC3LstCheckbox.setChecked(checkedState[position]);

            return convertView;
        }
    }

    private static class monCC3TransViewHolder {
        public TextView monCC3LstAmtTV;
        public TextView monCC3LstCatTV;
        public TextView monCC3LstChargingDebtCatTV;
        public CheckBox monCC3LstCheckbox;
    }

    public class MonCC3PaytLstAdapter extends ArrayAdapter<DebtDb> {

        private Context context;
        private List<DebtDb> debts;

        private MonCC3PaytLstAdapter(
                Context context,
                List<DebtDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<DebtDb> debts) {
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

            final MonCC3PaytViewHolder monCC3PaytHldr;

            if (convertView2 == null) {
                convertView2 = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_6_2tv,
                        parent, false);

                monCC3PaytHldr = new MonCC3PaytViewHolder();
                monCC3PaytHldr.monCC3PaytCatTV = convertView2.findViewById(R.id.list6CatTV);
                monCC3PaytHldr.monCC3PaytAmtTV = convertView2.findViewById(R.id.list6AmtTV);
                convertView2.setTag(monCC3PaytHldr);

            } else {
                monCC3PaytHldr = (MonCC3PaytViewHolder) convertView2.getTag();
            }

            monCC3Id = debts.get(position).getId();
            monCC3PaytHldr.monCC3PaytCatTV.setText(debts.get(position).getDebtName());

            //retrieve amount due in each category and format as currency
            monCC3TotDueList = new ArrayList<>();
            for (MoneyOutDb m2 : monCC3DbMgr.getMoneyOuts()) {
                if (String.valueOf(m2.getMoneyOutChargingDebtId()).equals(String.valueOf(monCC3Id)) && m2.getMoneyOutToPay() == 1 && m2.getMoneyOutPaid() == 0) {
                    monCC3TotDueList.add(m2.getMoneyOutAmount());
                }
            }
            monCC3AmtDue = 0.0;
            if (monCC3TotDueList.size() == 0) {
                monCC3AmtDue = 0.0;
            } else {
                for (Double dbl : monCC3TotDueList) {
                    monCC3AmtDue += dbl;
                }
            }
            monCC3Gen.dblASCurrency(String.valueOf(monCC3AmtDue), monCC3PaytHldr.monCC3PaytAmtTV);

            if (monCC3AmtDue == 0.0) {
                monCC3PaytHldr.monCC3PaytCatTV.setVisibility(View.GONE);
                monCC3PaytHldr.monCC3PaytAmtTV.setVisibility(View.GONE);
            } else {
                monCC3PaytHldr.monCC3PaytCatTV.setVisibility(View.VISIBLE);
                monCC3PaytHldr.monCC3PaytAmtTV.setVisibility(View.VISIBLE);
            }

            return convertView2;
        }
    }

    private static class MonCC3PaytViewHolder {
        public TextView monCC3PaytCatTV;
        public TextView monCC3PaytAmtTV;
    }
}*/
