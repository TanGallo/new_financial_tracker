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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.CurrentDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;

public class DailyCreditCard extends Fragment {

    boolean possible = true;
    CCAdapter ccAdapter;
    CheckBox ccPaidCheckbox;
    ContentValues moneyOutValue, moneyOutValue2;
    CurrentDbManager currentDbManager;
    DbHelper moneyOutHelper3, currentHelper3, currentHelper4;
    Double ccAmountD, totalCCPaymentDue, currentAccountBalance, currentAvailableBalance, totalCCPaymentBDue, newCurrentAvailableBalance,
            newCurrentAccountBalance, currentAccountBalance2, totalCCPaymentDue2, currentAvailableBalance2, totalCCPaymentBDue2, totalCCPaymentDue3;
    FragmentManager fm;
    FragmentTransaction transaction;
    Intent refreshView;
    ListView ccListView;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase moneyOutDbDb3, currentDbDb3, currentDbDb4;
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

        currentDbManager = new CurrentDbManager(getContext());

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
        currentAvailableBalance = currentDbManager.retrieveCurrentAvailableBalance();
        totalCCPaymentBDue = moneyOutDbManager.retrieveToPayBTotal();
        newCurrentAvailableBalance = currentAvailableBalance - totalCCPaymentBDue;

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance);
        currentHelper3 = new DbHelper(getContext());
        currentDbDb3 = currentHelper3.getWritableDatabase();
        currentDbDb3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
    }

    public void updateCurrentAccountBalance() {
        currentAccountBalance = currentDbManager.retrieveCurrentAccountBalance();
        totalCCPaymentDue3 = moneyOutDbManager.retrieveToPayTotal();
        newCurrentAccountBalance = currentAccountBalance - totalCCPaymentDue3;

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance);
        currentHelper4 = new DbHelper(getContext());
        currentDbDb4 = currentHelper4.getWritableDatabase();
        currentDbDb4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
    }

    CompoundButton.OnCheckedChangeListener onCheckCCPaid = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            updateCurrentAccountBalance();
            updateCurrentAvailableBalance();
            moneyOutDbManager.updatePaid();

            resetToPay();

            replaceFragment(new DailyCreditCard());

            /*refreshView = new Intent(getContext(), LayoutDailyMoney.class);
            startActivity(refreshView);*/
        }
    };


    public void checkIfPaymentPossible() {

        totalCCPaymentBDue2 = moneyOutDbManager.retrieveToPayBTotal();
        currentAvailableBalance2 = currentDbManager.retrieveCurrentAvailableBalance();
        totalCCPaymentDue2 = moneyOutDbManager.retrieveToPayTotal();
        currentAccountBalance2 = currentDbManager.retrieveCurrentAccountBalance();

        if (totalCCPaymentBDue2 > currentAvailableBalance2) {
            possible = false;
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
            ccOopsText.setVisibility(View.VISIBLE);
        } else if (totalCCPaymentDue2 > currentAccountBalance2) {
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

    public void updateCCPaymentDue() {

        totalCCPaymentDue = moneyOutDbManager.retrieveToPayTotal();

        if (totalCCPaymentDue.equals(0.0)) {
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

    public void replaceFragment(Fragment fragment) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.daily_fragment_container, fragment);

        transaction.commit();
    }
}
