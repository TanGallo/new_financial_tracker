package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyInDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyCC extends Fragment {

    boolean possible = true;
    Button ccTransButton, cancelCCTransEntryButton, updateCCTransEntryButton;
    CCTransAdapter ccTransAdapter;
    ContentValues moneyOutValue, moneyOutValue2;
    Cursor moneyOutCursor2, currentCursor, currentCursor2, expenseCursor, incomeCursor;
    Date moneyOutDate;
    DbHelper moneyOutDbHelper2, currentHelper, currentHelper2, currentHelper3, currentHelper5, expenseHelper, incomeHelper;
    Double moneyOutAmount, currentAccountBalance, newCurrentAccountBalance3, totalBudgetAExpenses, totalBudgetIncome, percentB,
            currentAvailableBalance, newCurrentAvailableBalance3, ccTransAmountD, oldMoneyOutAmount, newMoneyOutAmount;
    EditText ccTransAmountText, ccTransAmountEditText;
    int moneyOutToPay, moneyOutPaid;
    Intent backToDaily, backToDaily2;
    ListView ccTransList;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutSpinnerAdapter ccTransSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyOutSDF;
    Spinner ccTransCatSpinner;
    SQLiteDatabase moneyOutDbDb2, currentDbDb, currentDbDb2, currentDbDb3, currentDbDb5, expenseDbDb, incomeDbDb;
    String moneyOutCat, moneyOutPriority, moneyOutCreatedOn, moneyOutCC, ccTransCatS, ccTransPriorityS, ccTransAmountS, ccTransAmount2;
    TextView ccTransCatText;
    Timestamp moneyOutTimestamp;
    View v, ccTransLine;

    public DailyMoneyCC() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_cc, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ccTransAmountText = v.findViewById(R.id.ccTransAmount);
        ccTransButton = v.findViewById(R.id.ccTransButton);
        ccTransList = v.findViewById(R.id.ccTransList);
        ccTransCatText = v.findViewById(R.id.ccTransCatText);
        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText = v.findViewById(R.id.ccTransAmountEditText);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton = v.findViewById(R.id.cancelCCTransEntryButton);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton = v.findViewById(R.id.updateCCTransEntryButton);
        updateCCTransEntryButton.setVisibility(View.GONE);
        ccTransLine = v.findViewById(R.id.ccTransLine);
        ccTransLine.setVisibility(View.GONE);

        ccTransButton.setOnClickListener(onClickCCTransButton);

        moneyOutDbManager = new MoneyOutDbManager(getContext());
        ccTransAdapter = new CCTransAdapter(getContext(), moneyOutDbManager.getCCTrans());
        ccTransList.setAdapter(ccTransAdapter);

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        moneyOutDbHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutDbHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        ccTransSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor2);
        ccTransCatSpinner.setAdapter(ccTransSpinnerAdapter);

        ccTransCatSpinner.setOnItemSelectedListener(ccTransSpinnerSelection);

    }

    public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(getContext());
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        try {
            expenseCursor.moveToFirst();
        } catch (Exception e) {
            totalBudgetAExpenses = 0.0;
        }
        totalBudgetAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(getContext());
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalBudgetIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        percentB = 1 - (totalBudgetAExpenses / totalBudgetIncome);

        return percentB;

    }

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

    public void updateCurrentAvailableBalanceMoneyOut() {
        newCurrentAvailableBalance3 = retrieveCurrentAvailableBalance() - moneyOutAmount;

        if (newCurrentAvailableBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {

            moneyOutValue2 = new ContentValues();
            moneyOutValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance3);
            currentHelper5 = new DbHelper(getContext());
            currentDbDb5 = currentHelper5.getWritableDatabase();
            currentDbDb5.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        }
    }

    public void updateCurrentAccountBalanceMoneyOut() {
        newCurrentAccountBalance3 = retrieveCurrentAccountBalance() - moneyOutAmount;

        if (newCurrentAccountBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {

            moneyOutValue = new ContentValues();
            moneyOutValue.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance3);
            currentHelper3 = new DbHelper(getContext());
            currentDbDb3 = currentHelper3.getWritableDatabase();
            currentDbDb3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        }
    }

    AdapterView.OnItemSelectedListener ccTransSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ccTransCatS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            ccTransPriorityS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onClickCCTransButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = ccTransCatS;
            moneyOutPriority = ccTransPriorityS;
            moneyOutAmount = Double.valueOf(ccTransAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "Y";
            moneyOutToPay = 0;
            moneyOutPaid = 0;

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            ccTransAmountText.setText("");
            ccTransCatSpinner.setSelection(0, false);

            ccTransAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
            ccTransAdapter.notifyDataSetChanged();
        }
    };

    public class CCTransAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private CCTransAdapter(
                Context context,
                List<MoneyOutDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<MoneyOutDb> ccTrans) {
            this.ccTrans = ccTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CCTransViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new CCTransViewHolder();
                holder.ccTransEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.ccTransDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.ccTransAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.ccTransCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.ccTransDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (CCTransViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.ccTransDate.setText(ccTrans.get(position).getMoneyOutCreatedOn());

            //retrieve ccCat
            holder.ccTransCat.setText(ccTrans.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccTransAmountS = (String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                if (ccTransAmountS != null && !ccTransAmountS.equals("")) {
                    ccTransAmountD = Double.valueOf(ccTransAmountS);
                } else {
                    ccTransAmountD = 0.0;
                }
                ccTransAmount2 = currencyFormat.format(ccTransAmountD);
                holder.ccTransAmount.setText(ccTransAmount2);
            } catch (NumberFormatException e) {
                holder.ccTransAmount.setText(ccTransAmount2);
            }

            holder.ccTransDelete.setTag(ccTrans.get(position));
            holder.ccTransEdit.setTag(ccTrans.get(position));

            //click on pencil icon
            holder.ccTransEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccTransEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    moneyOutDbManager = new MoneyOutDbManager(getContext());

                    ccTransCatText.setVisibility(View.VISIBLE);
                    ccTransAmountEditText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    updateCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccTransLine.setVisibility(View.VISIBLE);

                    ccTransCatText.setText(moneyOutDb.getMoneyOutCat());
                    ccTransAmountEditText.setText(String.valueOf(moneyOutDb.getMoneyOutAmount()));
                    oldMoneyOutAmount = Double.valueOf(ccTransAmountEditText.getText().toString());
                }
            });

            updateCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb.setMoneyOutAmount(Double.valueOf(ccTransAmountEditText.getText().toString()));
                    newMoneyOutAmount = Double.valueOf(ccTransAmountEditText.getText().toString());
                    moneyOutAmount = newMoneyOutAmount - oldMoneyOutAmount;

                    moneyOutDbManager.updateMoneyOut(moneyOutDb);
                    ccTransAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();

                    Toast.makeText(getContext(), "Your changes have been saved",
                            Toast.LENGTH_LONG).show();

                    ccTransCatText.setVisibility(View.GONE);
                    ccTransAmountEditText.setVisibility(View.GONE);
                    cancelCCTransEntryButton.setVisibility(View.GONE);
                    updateCCTransEntryButton.setVisibility(View.GONE);
                    ccTransLine.setVisibility(View.GONE);
                }
            });

            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ccTransCatText.setVisibility(View.GONE);
                    ccTransAmountEditText.setVisibility(View.GONE);
                    cancelCCTransEntryButton.setVisibility(View.GONE);
                    updateCCTransEntryButton.setVisibility(View.GONE);
                    ccTransLine.setVisibility(View.GONE);
                }
            });

            //click on trash can icon
            holder.ccTransDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moneyOutDb = (MoneyOutDb) holder.ccTransDelete.getTag();
                    moneyOutDbManager.deleteMoneyOut(moneyOutDb);
                    ccTransAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class CCTransViewHolder {
        public TextView ccTransCat;
        public TextView ccTransAmount;
        public TextView ccTransDate;
        ImageButton ccTransDelete;
        ImageButton ccTransEdit;
    }
}
