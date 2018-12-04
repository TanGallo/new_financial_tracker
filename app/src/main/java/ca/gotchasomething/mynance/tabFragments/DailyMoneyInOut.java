package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyInDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyInOut extends Fragment {

    boolean possible = true;
    Button moneyInButton, moneyOutButton, ccTransButton;
    ContentValues moneyInValue, moneyOutValue, moneyInValue2, moneyOutValue2;
    Cursor moneyInCursor, moneyOutCursor, moneyOutCursor2, currentCursor, currentCursor2, expenseCursor, incomeCursor;
    Date moneyInDate, moneyOutDate;
    DbHelper moneyInDbHelper, moneyOutDbHelper, moneyOutDbHelper2, currentHelper, currentHelper2, currentHelper3, currentHelper4, currentHelper5,
            currentHelper6, expenseHelper, incomeHelper;
    Double moneyInAmount, moneyOutAmount, newCurrentAccountBalance, currentAccountBalance, newCurrentAccountBalance2, newCurrentAccountBalance3,
            totalBudgetAExpenses, totalBudgetIncome, percentB, currentAvailableBalance, newCurrentAvailableBalance2, newCurrentAvailableBalance3;
    EditText moneyInAmountText, moneyOutAmountText, ccTransAmountText;
    int moneyOutToPay, moneyOutPaid;
    Intent backToDaily, backToDaily2;
    MoneyInDb moneyInDb;
    MoneyInDbManager moneyInDbManager;
    MoneyInSpinnerAdapter moneyInAdapter;
    MoneyOutDb moneyOutDb, moneyOutDb2;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutSpinnerAdapter moneyOutAdapter, moneyOutAdapter2;
    SimpleDateFormat moneyInSDF, moneyOutSDF;
    Spinner moneyInCatSpinner, moneyOutCatSpinner, ccTransCatSpinner;
    SQLiteDatabase moneyInDbDb, moneyOutDbDb, moneyOutDbDb2, currentDbDb, currentDbDb2, currentDbDb3, currentDbDb4, currentDbDb5, currentDbDb6,
            expenseDbDb, incomeDbDb;
    String moneyInCatS, moneyInCat, moneyInCreatedOn, moneyOutCatS, moneyOutCat, moneyOutPriority, moneyOutPriorityS, moneyOutCreatedOn,
            moneyOutCC, ccTransCatS, ccTransPriorityS;
    TextView totalAccountText, availableAccountText;
    Timestamp moneyInTimestamp, moneyOutTimestamp;
    View v;

    public DailyMoneyInOut() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_in_out, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalAccountText = v.findViewById(R.id.totalAccountText);
        availableAccountText = v.findViewById(R.id.availableAccountText);

        moneyInAmountText = v.findViewById(R.id.moneyInAmount);
        moneyInButton = v.findViewById(R.id.moneyInButton);
        moneyOutAmountText = v.findViewById(R.id.moneyOutAmount);
        moneyOutButton = v.findViewById(R.id.moneyOutButton);
        ccTransAmountText = v.findViewById(R.id.ccTransAmount);
        ccTransButton = v.findViewById(R.id.ccTransButton);

        moneyInButton.setOnClickListener(onClickMoneyInButton);
        moneyOutButton.setOnClickListener(onClickMoneyOutButton);
        ccTransButton.setOnClickListener(onClickCCTransButton);

        moneyInDbManager = new MoneyInDbManager(getContext());
        moneyOutDbManager = new MoneyOutDbManager(getContext());

        moneyInCatSpinner = v.findViewById(R.id.moneyInCatSpinner);
        moneyInDbHelper = new DbHelper(getContext());
        moneyInDbDb = moneyInDbHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT * FROM " +
                DbHelper.INCOME_TABLE_NAME +
                " ORDER BY " +
                DbHelper.INCOMENAME +
                " ASC", null);
        moneyInAdapter = new MoneyInSpinnerAdapter(getContext(), moneyInCursor);
        moneyInCatSpinner.setAdapter(moneyInAdapter);

        moneyOutCatSpinner = v.findViewById(R.id.moneyOutCatSpinner);
        moneyOutDbHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutDbHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        moneyOutAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor);
        moneyOutCatSpinner.setAdapter(moneyOutAdapter);

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        moneyOutDbHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutDbHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        moneyOutAdapter2 = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor2);
        ccTransCatSpinner.setAdapter(moneyOutAdapter2);

        moneyInCatSpinner.setOnItemSelectedListener(moneyInSpinnerSelection);
        moneyOutCatSpinner.setOnItemSelectedListener(moneyOutSpinnerSelection);
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

    public void updateCurrentAvailableBalanceMoneyIn() {
        newCurrentAvailableBalance2 = retrieveCurrentAvailableBalance() + (moneyInAmount * retrieveBPercentage());

        moneyInValue2 = new ContentValues();
        moneyInValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance2);
        currentHelper4 = new DbHelper(getContext());
        currentDbDb4 = currentHelper4.getWritableDatabase();
        currentDbDb4.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue2, DbHelper.ID + "= '1'", null);
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

    AdapterView.OnItemSelectedListener moneyOutSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyOutCatS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            moneyOutPriorityS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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

            updateCurrentAccountBalanceMoneyIn();
            updateCurrentAvailableBalanceMoneyIn();

            backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily);
        }
    };

    View.OnClickListener onClickMoneyOutButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = moneyOutCatS;
            moneyOutPriority = moneyOutPriorityS;
            moneyOutAmount = Double.valueOf(moneyOutAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "N";
            moneyOutToPay = 0;
            moneyOutPaid = 0;

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyOutAmountText.setText("");
            moneyOutCatSpinner.setSelection(0, false);

            if (moneyOutPriority.equals("B")) {
                updateCurrentAvailableBalanceMoneyOut();

                if (possible) {
                    updateCurrentAccountBalanceMoneyOut();
                }
            } else if (moneyOutPriority.equals("A")) {
                updateCurrentAccountBalanceMoneyOut();
            }

            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily2);
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

            moneyOutDb2 = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb2);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            ccTransAmountText.setText("");
            ccTransCatSpinner.setSelection(0, false);
        }
    };
}
