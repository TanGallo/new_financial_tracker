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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import ca.gotchasomething.mynance.LayoutEntriesIncome;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyInDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyIn extends Fragment {

    Button moneyInButton, cancelMoneyInEntryButton, updateMoneyInEntryButton;
    ContentValues moneyInValue, moneyInValue2;
    Cursor moneyInCursor, currentCursor, currentCursor2, expenseCursor, incomeCursor;
    Date moneyInDate;
    DbHelper moneyInDbHelper, currentHelper, currentHelper2, currentHelper4, currentHelper6, expenseHelper, incomeHelper;
    Double moneyInAmount, moneyInD, newCurrentAccountBalance, currentAccountBalance, totalBudgetAExpenses, totalBudgetIncome, percentB,
            currentAvailableBalance, newCurrentAvailableBalance2, newMoneyInAmount, oldMoneyInAmount;
    EditText moneyInAmountText, moneyInAmountEditText;
    Intent backToDaily, backToDaily2, backToDaily3, backToDaily4;
    ListView moneyInList;
    MoneyInAdapter moneyInAdapter;
    MoneyInDb moneyInDb;
    MoneyInDbManager moneyInDbManager;
    MoneyInSpinnerAdapter moneyInSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyInSDF;
    Spinner moneyInCatSpinner;
    SQLiteDatabase moneyInDbDb, currentDbDb, currentDbDb2, currentDbDb4, currentDbDb6, expenseDbDb, incomeDbDb;
    String moneyInCatS, moneyInCat, moneyInCreatedOn, moneyInS, moneyIn2;
    TextView moneyInCatText;
    Timestamp moneyInTimestamp;
    View v, moneyInLine;

    public DailyMoneyIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_in, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moneyInAmountText = v.findViewById(R.id.moneyInAmount);
        moneyInButton = v.findViewById(R.id.moneyInButton);
        moneyInList = v.findViewById(R.id.moneyInList);
        moneyInCatText = v.findViewById(R.id.moneyInCatText);
        moneyInCatText.setVisibility(View.GONE);
        moneyInAmountEditText = v.findViewById(R.id.moneyInAmountEditText);
        moneyInAmountEditText.setVisibility(View.GONE);
        cancelMoneyInEntryButton = v.findViewById(R.id.cancelMoneyInEntryButton);
        cancelMoneyInEntryButton.setVisibility(View.GONE);
        updateMoneyInEntryButton = v.findViewById(R.id.updateMoneyInEntryButton);
        updateMoneyInEntryButton.setVisibility(View.GONE);
        moneyInLine = v.findViewById(R.id.moneyInLine);
        moneyInLine.setVisibility(View.GONE);

        moneyInButton.setOnClickListener(onClickMoneyInButton);

        moneyInDbManager = new MoneyInDbManager(getContext());
        moneyInAdapter = new MoneyInAdapter(getContext(), moneyInDbManager.getMoneyIns());
        moneyInList.setAdapter(moneyInAdapter);

        moneyInCatSpinner = v.findViewById(R.id.moneyInCatSpinner);
        moneyInDbHelper = new DbHelper(getContext());
        moneyInDbDb = moneyInDbHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT * FROM " +
                DbHelper.INCOME_TABLE_NAME +
                " ORDER BY " +
                DbHelper.INCOMENAME +
                " ASC", null);
        moneyInSpinnerAdapter = new MoneyInSpinnerAdapter(getContext(), moneyInCursor);
        moneyInCatSpinner.setAdapter(moneyInSpinnerAdapter);

        moneyInCatSpinner.setOnItemSelectedListener(moneyInSpinnerSelection);

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

    public void updateCurrentAvailableBalanceMoneyIn() {
        newCurrentAvailableBalance2 = retrieveCurrentAvailableBalance() + (moneyInAmount * retrieveBPercentage());

        moneyInValue2 = new ContentValues();
        moneyInValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance2);
        currentHelper4 = new DbHelper(getContext());
        currentDbDb4 = currentHelper4.getWritableDatabase();
        currentDbDb4.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue2, DbHelper.ID + "= '1'", null);
    }

    public void updateCurrentAccountBalanceMoneyIn() {
        newCurrentAccountBalance = retrieveCurrentAccountBalance() + moneyInAmount;

        moneyInValue = new ContentValues();
        moneyInValue.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance);
        currentHelper6 = new DbHelper(getContext());
        currentDbDb6 = currentHelper6.getWritableDatabase();
        currentDbDb6.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue, DbHelper.ID + "= '1'", null);
    }

    AdapterView.OnItemSelectedListener moneyInSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyInCatS = moneyInCursor.getString(moneyInCursor.getColumnIndexOrThrow(DbHelper.INCOMENAME));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onClickMoneyInButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyInCat = moneyInCatS;
            moneyInAmount = Double.valueOf(moneyInAmountText.getText().toString());
            moneyInDate = new Date();
            moneyInTimestamp = new Timestamp(moneyInDate.getTime());
            moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);

            moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInCreatedOn, 0);

            moneyInDbManager.addMoneyIn(moneyInDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyInAmountText.setText("");
            moneyInCatSpinner.setSelection(0, false);

            moneyInAdapter.updateMoneyIn(moneyInDbManager.getMoneyIns());
            moneyInAdapter.notifyDataSetChanged();

            updateCurrentAccountBalanceMoneyIn();
            updateCurrentAvailableBalanceMoneyIn();

            backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily);
        }
    };

    public class MoneyInAdapter extends ArrayAdapter<MoneyInDb> {

        private Context context;
        private List<MoneyInDb> moneyIn;

        private MoneyInAdapter(
                Context context,
                List<MoneyInDb> moneyIn) {

            super(context, -1, moneyIn);

            this.context = context;
            this.moneyIn = moneyIn;
        }

        public void updateMoneyIn(List<MoneyInDb> moneyIn) {
            this.moneyIn = moneyIn;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIn.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new MoneyInViewHolder();
                holder.moneyInEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.moneyInDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.moneyInAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.moneyInCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.moneyInDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (MoneyInViewHolder) convertView.getTag();
            }

            //retrieve moneyInCreatedOn
            holder.moneyInDate.setText(moneyIn.get(position).getMoneyInCreatedOn());

            //retrieve moneyInCat
            holder.moneyInCat.setText(moneyIn.get(position).getMoneyInCat());

            //moneyInAmount and format as currency
            try {
                moneyInS = (String.valueOf(moneyIn.get(position).getMoneyInAmount()));
                if (moneyInS != null && !moneyInS.equals("")) {
                    moneyInD = Double.valueOf(moneyInS);
                } else {
                    moneyInD = 0.0;
                }
                moneyIn2 = currencyFormat.format(moneyInD);
                holder.moneyInAmount.setText(moneyIn2);
            } catch (NumberFormatException e) {
                holder.moneyInAmount.setText(moneyIn2);
            }

            holder.moneyInDelete.setTag(moneyIn.get(position));
            holder.moneyInEdit.setTag(moneyIn.get(position));

            //click on pencil icon
            holder.moneyInEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInDb = (MoneyInDb) holder.moneyInEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    moneyInDbManager = new MoneyInDbManager(getContext());

                    moneyInCatText.setVisibility(View.VISIBLE);
                    moneyInAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyInEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyInEntryButton.setVisibility(View.VISIBLE);
                    moneyInLine.setVisibility(View.VISIBLE);

                    moneyInCatText.setText(moneyInDb.getMoneyInCat());
                    moneyInAmountEditText.setText(String.valueOf(moneyInDb.getMoneyInAmount()));
                    oldMoneyInAmount = Double.valueOf(moneyInAmountEditText.getText().toString());

                    updateMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            moneyInDb.setMoneyInAmount(Double.valueOf(moneyInAmountEditText.getText().toString()));
                            newMoneyInAmount = Double.valueOf(moneyInAmountEditText.getText().toString());
                            moneyInAmount = newMoneyInAmount - oldMoneyInAmount;

                            moneyInDbManager.updateMoneyIn(moneyInDb);
                            moneyInAdapter.updateMoneyIn(moneyInDbManager.getMoneyIns());
                            notifyDataSetChanged();

                            Toast.makeText(getContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            updateCurrentAccountBalanceMoneyIn();
                            updateCurrentAvailableBalanceMoneyIn();

                            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily2);
                        }
                    });

                    cancelMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            backToDaily3 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily3);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyInDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInAmount = -(Double.valueOf(moneyIn.get(position).getMoneyInAmount()));

                    moneyInDb = (MoneyInDb) holder.moneyInDelete.getTag();
                    moneyInDbManager.deleteMoneyIn(moneyInDb);
                    moneyInAdapter.updateMoneyIn(moneyInDbManager.getMoneyIns());
                    notifyDataSetChanged();

                    updateCurrentAvailableBalanceMoneyIn();
                    updateCurrentAccountBalanceMoneyIn();

                    backToDaily4 = new Intent(getContext(), LayoutDailyMoney.class);
                    backToDaily4.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(backToDaily4);
                }
            });

            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView moneyInDate;
        public TextView moneyInCat;
        public TextView moneyInAmount;
        ImageButton moneyInEdit;
        ImageButton moneyInDelete;
    }
}
