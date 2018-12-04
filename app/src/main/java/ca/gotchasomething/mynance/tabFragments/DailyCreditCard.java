package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;

public class DailyCreditCard extends Fragment {

    boolean possible = true;
    CCAdapter ccAdapter;
    CheckBox ccPaidCheckbox;
    ContentValues moneyOutValue, moneyOutValue2;
    Cursor moneyOutCursor, moneyOutCursor4, currentCursor, currentCursor2;
    DbHelper moneyOutHelper, moneyOutHelper2, moneyOutHelper3, moneyOutHelper4, currentHelper, currentHelper2, currentHelper3, currentHelper4;
    Double ccAmountD, totalCCPaymentDue, currentAccountBalance, currentAvailableBalance, totalCCPaymentBDue, newCurrentAvailableBalance, newCurrentAccountBalance;
    Intent refreshView;
    ListView ccListView;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase moneyOutDbDb, moneyOutDbDb2, moneyOutDbDb3, moneyOutDbDb4, currentDbDb, currentDbDb2, currentDbDb3, currentDbDb4;
    String ccAmountS, ccAmount2, totalCCPaymentDueS;
    TextView checkBelowLabel, totalCCPaymentDueLabel, totalCCPaymentDueAmount, ccPaidLabel, ccOopsText;
    View v;

    public DailyCreditCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_credit_card, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkBelowLabel = v.findViewById(R.id.checkBelowLabel);
        totalCCPaymentDueLabel = v.findViewById(R.id.totalCCPaymentDueLabel);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount = v.findViewById(R.id.totalCCPaymentDueAmount);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel = v.findViewById(R.id.ccPaidLabel);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox = v.findViewById(R.id.ccPaidCheckbox);
        ccPaidCheckbox.setVisibility(View.GONE);
        ccOopsText = v.findViewById(R.id.ccOopsText);
        ccOopsText.setVisibility(View.GONE);

        ccListView = v.findViewById(R.id.ccListView);

        moneyOutDbManager = new MoneyOutDbManager(getContext());
        ccAdapter = new CCAdapter(getContext(), moneyOutDbManager.getCCTransToPay());
        ccListView.setAdapter(ccAdapter);

        ccPaidCheckbox.setOnCheckedChangeListener(onCheckCCPaid);

        resetToPay();
        updateCCPaymentDue();
    }

    public void updateCurrentAvailableBalance() {
        newCurrentAvailableBalance = retrieveCurrentAvailableBalance() - retrieveToPayBTotal();

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance);
        currentHelper3 = new DbHelper(getContext());
        currentDbDb3 = currentHelper3.getWritableDatabase();
        currentDbDb3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
    }

    public void updateCurrentAccountBalance() {
        newCurrentAccountBalance = retrieveCurrentAccountBalance() - retrieveToPayTotal();

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance);
        currentHelper4 = new DbHelper(getContext());
        currentDbDb4 = currentHelper4.getWritableDatabase();
        currentDbDb4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
    }

    public void updatePaid() {

        moneyOutHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutHelper2.getWritableDatabase();
        ContentValues updateMoneyOutPaid = new ContentValues();
        updateMoneyOutPaid.put(DbHelper.MONEYOUTPAID, 1);
        moneyOutDbDb2.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutPaid, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID
                + " = '0'", null);
    }

    CompoundButton.OnCheckedChangeListener onCheckCCPaid = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            updateCurrentAccountBalance();
            updateCurrentAvailableBalance();
            updatePaid();

            resetToPay();

            refreshView = new Intent(getContext(), LayoutDailyMoney.class);
            startActivity(refreshView);
        }
    };

    public Double retrieveCurrentAccountBalance() {
        currentHelper = new DbHelper(getContext());
        currentDbDb = currentHelper.getReadableDatabase();
        currentCursor = currentDbDb.rawQuery("SELECT " + DbHelper.CURRENTACCOUNTBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME + " WHERE "
                + DbHelper.ID + " = '1'", null);
        currentCursor.moveToFirst();
        currentAccountBalance = currentCursor.getDouble(0);
        currentCursor.close();

        if (currentAccountBalance.isNaN()) {
            currentAccountBalance = 0.0;
        }

        return currentAccountBalance;
    }

    public Double retrieveCurrentAvailableBalance() {
        currentHelper2 = new DbHelper(getContext());
        currentDbDb2 = currentHelper2.getReadableDatabase();
        currentCursor2 = currentDbDb2.rawQuery("SELECT " + DbHelper.CURRENTAVAILABLEBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME + " WHERE "
                + DbHelper.ID + " = '1'", null);
        currentCursor2.moveToFirst();
        currentAvailableBalance = currentCursor2.getDouble(0);
        currentCursor2.close();

        if (currentAvailableBalance.isNaN()) {
            currentAvailableBalance = 0.0;
        }

        return currentAvailableBalance;
    }

    public void checkIfPaymentPossible() {

        retrieveToPayBTotal();
        retrieveCurrentAvailableBalance();
        retrieveToPayTotal();
        retrieveCurrentAccountBalance();

        if (totalCCPaymentBDue > currentAvailableBalance) {
            possible = false;
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
            ccOopsText.setVisibility(View.VISIBLE);
        } else if (totalCCPaymentDue > currentAccountBalance) {
            possible = false;
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
            ccOopsText.setVisibility(View.VISIBLE);
        } else {
            ccOopsText.setVisibility(View.GONE);
        }
    }

    public void resetToPay() {
        moneyOutHelper3 = new DbHelper(getContext());
        moneyOutDbDb3 = moneyOutHelper3.getWritableDatabase();
        ContentValues updateMoneyOutToPay = new ContentValues();
        updateMoneyOutToPay.put(DbHelper.MONEYOUTTOPAY, 0);
        moneyOutDbDb3.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutToPay, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID
                + " = '0'", null);

        checkBelowLabel.setVisibility(View.VISIBLE);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox.setVisibility(View.GONE);
        ccOopsText.setVisibility(View.GONE);
    }

    public Double retrieveToPayTotal() {
        moneyOutHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1' AND " + DbHelper.MONEYOUTPAID + " = '0'", null);
        try {
            moneyOutCursor.moveToFirst();
        } catch (Exception e) {
            totalCCPaymentDue = 0.0;
        }
        totalCCPaymentDue = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return totalCCPaymentDue;
    }

    public Double retrieveToPayBTotal() {
        moneyOutHelper4 = new DbHelper(getContext());
        moneyOutDbDb4 = moneyOutHelper4.getReadableDatabase();
        moneyOutCursor4 = moneyOutDbDb4.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1' AND " + DbHelper.MONEYOUTPAID + " = '0' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        try {
            moneyOutCursor4.moveToFirst();
        } catch (Exception e) {
            totalCCPaymentBDue = 0.0;
        }
        totalCCPaymentBDue = moneyOutCursor4.getDouble(0);
        moneyOutCursor4.close();

        return totalCCPaymentBDue;
    }

    public void updateCCPaymentDue() {

        if (retrieveToPayTotal().equals(0.0)) {
            resetToPay();
        } else {
            checkBelowLabel.setVisibility(View.GONE);
            totalCCPaymentDueLabel.setVisibility(View.VISIBLE);
            totalCCPaymentDueAmount.setVisibility(View.VISIBLE);
            ccPaidLabel.setVisibility(View.VISIBLE);
            ccPaidCheckbox.setVisibility(View.VISIBLE);

            totalCCPaymentDueS = currencyFormat.format(totalCCPaymentDue);
            totalCCPaymentDueAmount.setText(totalCCPaymentDueS);
        }

        checkIfPaymentPossible();
    }

    public class CCAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTransToPay;

        private CCAdapter(
                Context context,
                List<MoneyOutDb> ccTransToPay) {

            super(context, -1, ccTransToPay);

            this.context = context;
            this.ccTransToPay = ccTransToPay;
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

            final CCViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_credit_card,
                        parent, false);

                holder = new CCViewHolder();
                holder.ccCat = convertView.findViewById(R.id.ccCat);
                holder.ccAmount = convertView.findViewById(R.id.ccAmount);
                holder.ccCheck = convertView.findViewById(R.id.ccCheck);
                convertView.setTag(holder);

            } else {
                holder = (CCViewHolder) convertView.getTag();
            }

            holder.ccCheck.setTag(ccTransToPay.get(position));

            //retrieve ccCat
            holder.ccCat.setText(ccTransToPay.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccAmountS = (String.valueOf(ccTransToPay.get(position).getMoneyOutAmount()));
                if (ccAmountS != null && !ccAmountS.equals("")) {
                    ccAmountD = Double.valueOf(ccAmountS);
                } else {
                    ccAmountD = 0.0;
                }
                ccAmount2 = currencyFormat.format(ccAmountD);
                holder.ccAmount.setText(ccAmount2);
            } catch (NumberFormatException e) {
                holder.ccAmount.setText(ccAmount2);
            }

            holder.ccCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    moneyOutDb = (MoneyOutDb) holder.ccCheck.getTag();

                    if (isChecked) {
                        moneyOutDb.setMoneyOutToPay(1);
                        moneyOutDbManager.updateMoneyOut(moneyOutDb);
                    } else {
                        moneyOutDb.setMoneyOutToPay(0);
                        moneyOutDbManager.updateMoneyOut(moneyOutDb);
                    }

                    updateCCPaymentDue();
                }
            });

            return convertView;
        }
    }

    private static class CCViewHolder {
        public TextView ccCat;
        public TextView ccAmount;
        public CheckBox ccCheck;
    }
}
