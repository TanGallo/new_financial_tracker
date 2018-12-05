package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class ExpenseBudgetDbManager {

    private DbHelper dbHelperExpense;
    public Double totalExpenses;
    SQLiteDatabase dbExpense, dbExpense2, dbExpense3, dbExpense4, dbExpense5, dbExpense6;
    Cursor cursorExpense, cursorExpense2, cursorExpense6;

    public ExpenseBudgetDbManager(Context context) {
        dbHelperExpense = DbHelper.getInstance(context);
    }

    public List<ExpenseBudgetDb> getExpense() {

        dbExpense = dbHelperExpense.getReadableDatabase();

        cursorExpense = dbExpense.rawQuery(
                "SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSEAANNUALAMOUNT + " DESC",
                null);

        List<ExpenseBudgetDb> expenses = new ArrayList<>();

        if (cursorExpense.moveToFirst()) {
            while (!cursorExpense.isAfterLast()) {

                ExpenseBudgetDb expense = new ExpenseBudgetDb(
                        cursorExpense.getString(cursorExpense.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursorExpense.getDouble(cursorExpense.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursorExpense.getDouble(cursorExpense.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursorExpense.getString(cursorExpense.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursorExpense.getString(cursorExpense.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursorExpense.getDouble(cursorExpense.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursorExpense.getDouble(cursorExpense.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursorExpense.getDouble(cursorExpense.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursorExpense.getLong(cursorExpense.getColumnIndex(DbHelper.ID))
                );

                expenses.add(expense); //adds new items to bottom of list
                cursorExpense.moveToNext();
            }
        }
        cursorExpense.close();
        return expenses;
    }

    public void addExpense(ExpenseBudgetDb expense) {

        ContentValues newExpense = new ContentValues();
        newExpense.put(DbHelper.EXPENSENAME, expense.getExpenseName());
        newExpense.put(DbHelper.EXPENSEAMOUNT, expense.getExpenseAmount());
        newExpense.put(DbHelper.EXPENSEFREQUENCY, expense.getExpenseFrequency());
        newExpense.put(DbHelper.EXPENSEPRIORITY, expense.getExpensePriority());
        newExpense.put(DbHelper.EXPENSEWEEKLY, expense.getExpenseWeekly());
        newExpense.put(DbHelper.EXPENSEANNUALAMOUNT, expense.getExpenseAnnualAmount());
        newExpense.put(DbHelper.EXPENSEAANNUALAMOUNT, expense.getExpenseAAnnualAmount());
        newExpense.put(DbHelper.EXPENSEBANNUALAMOUNT, expense.getExpenseBAnnualAmount());

        dbExpense2 = dbHelperExpense.getWritableDatabase();
        dbExpense2.insertOrThrow(DbHelper.EXPENSES_TABLE_NAME, null, newExpense);

    }

    public void updateExpense(ExpenseBudgetDb expense) {

        ContentValues updateExpense = new ContentValues();
        updateExpense.put(DbHelper.EXPENSENAME, expense.getExpenseName());
        updateExpense.put(DbHelper.EXPENSEAMOUNT, expense.getExpenseAmount());
        updateExpense.put(DbHelper.EXPENSEFREQUENCY, expense.getExpenseFrequency());
        updateExpense.put(DbHelper.EXPENSEPRIORITY, expense.getExpensePriority());
        updateExpense.put(DbHelper.EXPENSEWEEKLY, expense.getExpenseWeekly());
        updateExpense.put(DbHelper.EXPENSEANNUALAMOUNT, expense.getExpenseAnnualAmount());
        updateExpense.put(DbHelper.EXPENSEAANNUALAMOUNT, expense.getExpenseAAnnualAmount());
        updateExpense.put(DbHelper.EXPENSEBANNUALAMOUNT, expense.getExpenseBAnnualAmount());

        dbExpense3 = dbHelperExpense.getWritableDatabase();

        String[] args = new String[]{String.valueOf(expense.getId())};

        dbExpense3.update(
                DbHelper.EXPENSES_TABLE_NAME,
                updateExpense,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteExpense(ExpenseBudgetDb expense) {
        dbExpense4 = dbHelperExpense.getWritableDatabase();

        String[] args = new String[]{String.valueOf(expense.getId())};

        dbExpense4.delete(
                DbHelper.EXPENSES_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public List<ExpenseBudgetDb> getWeeklyLimits() {

        dbExpense5 = dbHelperExpense.getReadableDatabase();

        cursorExpense2 = dbExpense5.rawQuery(
                "SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.EXPENSEWEEKLY +
                " = 'Y'",
                null);

        List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();

        if (cursorExpense2.moveToFirst()) {
            while (!cursorExpense2.isAfterLast()) {

                ExpenseBudgetDb weekly = new ExpenseBudgetDb(
                        cursorExpense2.getString(cursorExpense2.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursorExpense2.getDouble(cursorExpense2.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursorExpense2.getDouble(cursorExpense2.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursorExpense2.getString(cursorExpense2.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursorExpense2.getString(cursorExpense2.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursorExpense2.getDouble(cursorExpense2.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursorExpense2.getDouble(cursorExpense2.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursorExpense2.getDouble(cursorExpense2.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursorExpense2.getLong(cursorExpense2.getColumnIndex(DbHelper.ID))
                );

                weeklyLimits.add(weekly); //adds new items to bottom of list
                cursorExpense2.moveToNext();
            }
        }
        cursorExpense2.close();
        return weeklyLimits;
    }

    public Double sumTotalExpenses() {
        dbExpense6 = dbHelperExpense.getReadableDatabase();
        cursorExpense6 = dbExpense6.rawQuery("SELECT sum(expenseAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        cursorExpense6.moveToFirst();
        totalExpenses = cursorExpense6.getDouble(0);
        cursorExpense6.close();

        return totalExpenses;
    }
}
