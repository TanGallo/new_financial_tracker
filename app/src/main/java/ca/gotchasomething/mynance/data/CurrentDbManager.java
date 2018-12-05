package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class CurrentDbManager {

    private DbHelper dbHelperCurrent;
    public Double currentAccountBalance, percentB, totalBudgetAExpenses, totalBudgetIncome, currentAvailableBalance;
    SQLiteDatabase dbCurrent, dbCurrent2, dbCurrent3, dbCurrent4, dbCurrent5, dbCurrent6, dbExpense, dbIncome;
    Cursor cursorCurrent, cursorCurrent2, cursorCurrent6, cursorExpense, cursorIncome;

    public CurrentDbManager(Context context) {
        dbHelperCurrent = DbHelper.getInstance(context);
    }

    public List<CurrentDb> getCurrent() {

        dbCurrent = dbHelperCurrent.getReadableDatabase();

        cursorCurrent = dbCurrent.rawQuery(
                "SELECT * FROM " + DbHelper.CURRENT_TABLE_NAME, null);

        List<CurrentDb> currents = new ArrayList<>();

        if (cursorCurrent.moveToFirst()) {
            while (!cursorCurrent.isAfterLast()) {

                CurrentDb current = new CurrentDb(
                        cursorCurrent.getDouble(cursorCurrent.getColumnIndex(DbHelper.CURRENTACCOUNTBALANCE)),
                        cursorCurrent.getDouble(cursorCurrent.getColumnIndex(DbHelper.CURRENTAVAILABLEBALANCE)),
                        cursorCurrent.getLong(cursorCurrent.getColumnIndex(DbHelper.ID))
                );

                currents.add(0, current); //adds new items to beginning of list
                cursorCurrent.moveToNext();
            }
        }
        cursorCurrent.close();
        return currents;
    }

    public void addCurrent(CurrentDb current) {

        ContentValues newCurrent = new ContentValues();
        newCurrent.put(DbHelper.CURRENTACCOUNTBALANCE, current.getCurrentAccountBalance());
        newCurrent.put(DbHelper.CURRENTAVAILABLEBALANCE, current.getCurrentAvailableBalance());

        dbCurrent2 = dbHelperCurrent.getWritableDatabase();
        dbCurrent2.insert(DbHelper.CURRENT_TABLE_NAME, null, newCurrent);
    }

    public void updateCurrent(CurrentDb current) {

        ContentValues updateCurrent = new ContentValues();
        updateCurrent.put(DbHelper.CURRENTACCOUNTBALANCE, current.getCurrentAccountBalance());
        updateCurrent.put(DbHelper.CURRENTAVAILABLEBALANCE, current.getCurrentAvailableBalance());

        dbCurrent3 = dbHelperCurrent.getWritableDatabase();

        String[] args = new String[]{String.valueOf(current.getId())};

        dbCurrent3.update(
                DbHelper.CURRENT_TABLE_NAME,
                updateCurrent,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteCurrent(CurrentDb current) {
        dbCurrent4 = dbHelperCurrent.getWritableDatabase();

        String[] args = new String[]{String.valueOf(current.getId())};

        dbCurrent4.delete(
                DbHelper.CURRENT_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public Double retrieveCurrentAccountBalance() {

        dbCurrent5 = dbHelperCurrent.getReadableDatabase();
        cursorCurrent2 = dbCurrent5.rawQuery("SELECT " + DbHelper.CURRENTACCOUNTBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        cursorCurrent2.moveToFirst();
        currentAccountBalance = cursorCurrent2.getDouble(0);
        cursorCurrent2.close();

        if(currentAccountBalance.isNaN()) {
            currentAccountBalance = 0.0;
        }

        return currentAccountBalance;
    }

    public Double retrieveBPercentage() {

        dbExpense = dbHelperCurrent.getReadableDatabase();
        cursorExpense = dbExpense.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        try {
            cursorExpense.moveToFirst();
        } catch(Exception e) {
            totalBudgetAExpenses = 0.0;
        }
        totalBudgetAExpenses = cursorExpense.getDouble(0);
        cursorExpense.close();

        dbIncome = dbHelperCurrent.getReadableDatabase();
        cursorIncome = dbIncome.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        cursorIncome.moveToFirst();
        totalBudgetIncome = cursorIncome.getDouble(0);
        cursorIncome.close();

        percentB = 1 - (totalBudgetAExpenses / totalBudgetIncome);

        return percentB;
    }

    public Double retrieveCurrentAvailableBalance() {

        dbCurrent6 = dbHelperCurrent.getReadableDatabase();
        cursorCurrent6 = dbCurrent6.rawQuery("SELECT " + DbHelper.CURRENTAVAILABLEBALANCE + " FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        cursorCurrent6.moveToFirst();
        currentAvailableBalance = cursorCurrent6.getDouble(0);
        cursorCurrent6.close();

        if(currentAvailableBalance.isNaN()) {
            currentAvailableBalance = 0.0;
        }

        return currentAvailableBalance;
    }
}
