package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyCreditCard extends Fragment {

    boolean possibleA = true, possibleB = true;
    Button ccTransCancelButton, ccTransContinueButton;
    CCAdapter ccAdapter;
    CheckBox ccPaidCheckbox;
    ContentValues moneyOutValue, moneyOutValue2, currentValue;
    DbHelper dbHelper2, dbHelper3, dbHelper4, dbHelper5;
    DbManager dbManager;
    Double ccAmountD = 0.0, totalCCPaymentDue = 0.0, currentAccountBalance = 0.0, currentAvailableBalance = 0.0, totalCCPaymentBDue = 0.0,
            newCurrentAvailableBalance = 0.0, newCurrentAccountBalance = 0.0, currentAccountBalance2 = 0.0, totalCCPaymentDue2 = 0.0,
            currentAvailableBalance2 = 0.0, totalCCPaymentBDue2 = 0.0, totalCCPaymentDue3 = 0.0;
    Intent refresh;
    ListView ccListView;
    MoneyOutDb moneyOutDb;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase db2, db3, db4, db5;
    String ccAmountS = null, ccAmount2 = null, totalCCPaymentDueS = null;
    TextView checkBelowLabel, totalCCPaymentDueLabel, totalCCPaymentDueAmount, ccPaidLabel, noCCTransLabel, ccTransPaymentNotPossibleBText,
            ccTransPaymentNotPossibleAText, ccTransContinueAnywayText, ccHeaderLabel, ccPayLabel;
    View v, ccLine;

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

        dbManager = new DbManager(getContext());

        noCCTransLabel = v.findViewById(R.id.noCCTransLabel);
        noCCTransLabel.setVisibility(View.GONE);
        ccHeaderLabel = v.findViewById(R.id.ccHeaderLabel);
        ccPayLabel = v.findViewById(R.id.ccPayLabel);
        ccLine = v.findViewById(R.id.ccLine);
        ccTransPaymentNotPossibleAText = v.findViewById(R.id.ccTransPaymentNotPossibleAText);
        ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
        ccTransPaymentNotPossibleBText = v.findViewById(R.id.ccTransPaymentNotPossibleBText);
        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
        ccTransContinueAnywayText = v.findViewById(R.id.ccTransContinueAnywayText);
        ccTransContinueAnywayText.setVisibility(View.GONE);
        ccTransCancelButton = v.findViewById(R.id.ccTransCancelButton);
        ccTransCancelButton.setVisibility(View.GONE);
        ccTransContinueButton = v.findViewById(R.id.ccTransContinueButton);
        ccTransContinueButton.setVisibility(View.GONE);
        checkBelowLabel = v.findViewById(R.id.checkBelowLabel);
        totalCCPaymentDueLabel = v.findViewById(R.id.totalCCPaymentDueLabel);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount = v.findViewById(R.id.totalCCPaymentDueAmount);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel = v.findViewById(R.id.ccPaidLabel);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox = v.findViewById(R.id.ccPaidCheckbox);
        ccPaidCheckbox.setVisibility(View.GONE);

        ccListView = v.findViewById(R.id.ccListView);

        ccAdapter = new CCAdapter(getContext(), dbManager.getCCTransToPay());
        ccListView.setAdapter(ccAdapter);

        ccPaidCheckbox.setOnCheckedChangeListener(onCheckCCPaid);

        resetToPay();
        updateCCPaymentDue();

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 4);
        dbHelper5 = new DbHelper(getContext());
        db5 = dbHelper5.getWritableDatabase();
        db5.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db5.close();
    }

    public void cancelTransaction() {
        ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
        ccTransContinueAnywayText.setVisibility(View.GONE);
        ccTransCancelButton.setVisibility(View.GONE);
        ccTransContinueButton.setVisibility(View.GONE);
    }

    public void continueTransaction() {
        updateCurrentAccountBalance();
        updateCurrentAvailableBalance();
        dbManager.updatePaid();

        resetToPay();

        refresh = new Intent(getContext(), LayoutDailyMoney.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(refresh);
    }

    public void updateCurrentAvailableBalance() {
        currentAvailableBalance = dbManager.retrieveCurrentAvailableBalance();
        totalCCPaymentBDue = dbManager.retrieveToPayBTotal();
        newCurrentAvailableBalance = currentAvailableBalance - totalCCPaymentBDue;

        moneyOutValue = new ContentValues();
        moneyOutValue.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        db3.close();
    }

    public void updateCurrentAccountBalance() {
        currentAccountBalance = dbManager.retrieveCurrentAccountBalance();
        totalCCPaymentDue3 = dbManager.retrieveToPayTotal();
        newCurrentAccountBalance = currentAccountBalance - totalCCPaymentDue3;

        moneyOutValue2 = new ContentValues();
        moneyOutValue2.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance);
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        db4.close();
    }

    CompoundButton.OnCheckedChangeListener onCheckCCPaid = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            continueTransaction();
        }
    };


    public void checkIfPaymentPossible() {

        totalCCPaymentBDue2 = dbManager.retrieveToPayBTotal();
        currentAvailableBalance2 = dbManager.retrieveCurrentAvailableBalance();
        totalCCPaymentDue2 = dbManager.retrieveToPayTotal();
        currentAccountBalance2 = dbManager.retrieveCurrentAccountBalance();

        possibleA = true;
        possibleB = true;
        if (totalCCPaymentBDue2 > currentAvailableBalance2) {
            possibleB = false;
        } else if (totalCCPaymentDue2 > currentAccountBalance2) {
            possibleA = false;
        } else {
            ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
            ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
            ccTransContinueAnywayText.setVisibility(View.GONE);
            ccTransCancelButton.setVisibility(View.GONE);
            ccTransContinueButton.setVisibility(View.GONE);
        }
    }

    public void resetToPay() {
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getWritableDatabase();
        ContentValues updateMoneyOutToPay = new ContentValues();
        updateMoneyOutToPay.put(DbHelper.MONEYOUTTOPAY, 0);
        db2.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutToPay, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID
                + " = '0'", null);
        db2.close();

        if (ccAdapter.getCount() == 0) {
            noCCTransLabel.setVisibility(View.VISIBLE);
            checkBelowLabel.setVisibility(View.GONE);
            ccHeaderLabel.setVisibility(View.GONE);
            ccPayLabel.setVisibility(View.GONE);
            ccLine.setVisibility(View.GONE);
        } else {
            noCCTransLabel.setVisibility(View.GONE);
            checkBelowLabel.setVisibility(View.VISIBLE);
            ccHeaderLabel.setVisibility(View.VISIBLE);
            ccPayLabel.setVisibility(View.VISIBLE);
            ccLine.setVisibility(View.VISIBLE);
        }

        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox.setVisibility(View.GONE);
        ccTransPaymentNotPossibleAText.setVisibility(View.GONE);
        ccTransPaymentNotPossibleBText.setVisibility(View.GONE);
        ccTransContinueAnywayText.setVisibility(View.GONE);
        ccTransCancelButton.setVisibility(View.GONE);
        ccTransContinueButton.setVisibility(View.GONE);
    }

    public void updateCCPaymentDue() {

        totalCCPaymentDue = dbManager.retrieveToPayTotal();

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
                        dbManager.updateMoneyOut(moneyOutDb);
                    } else {
                        moneyOutDb.setMoneyOutToPay(0);
                        dbManager.updateMoneyOut(moneyOutDb);
                    }

                    updateCCPaymentDue();

                    if(!possibleB) {
                        ccPaidLabel.setVisibility(View.GONE);
                        ccPaidCheckbox.setVisibility(View.GONE);
                        ccTransPaymentNotPossibleBText.setVisibility(View.VISIBLE);
                        ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                        ccTransCancelButton.setVisibility(View.VISIBLE);
                        ccTransContinueButton.setVisibility(View.VISIBLE);

                        ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.ccCheck.setChecked(false);
                                cancelTransaction();
                            }
                        });

                        ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                continueTransaction();
                            }
                        });
                    } else if(!possibleA) {
                        ccPaidLabel.setVisibility(View.GONE);
                        ccPaidCheckbox.setVisibility(View.GONE);
                        ccTransPaymentNotPossibleAText.setVisibility(View.VISIBLE);
                        ccTransContinueAnywayText.setVisibility(View.VISIBLE);
                        ccTransCancelButton.setVisibility(View.VISIBLE);
                        ccTransContinueButton.setVisibility(View.VISIBLE);

                        ccTransCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.ccCheck.setChecked(false);
                                cancelTransaction();
                            }
                        });

                        ccTransContinueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                continueTransaction();
                            }
                        });
                    }
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
