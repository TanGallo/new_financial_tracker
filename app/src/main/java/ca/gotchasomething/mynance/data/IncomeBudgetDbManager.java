package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class IncomeBudgetDbManager {

    private DbHelper dbHelperIncome;
    public Double totalIncome;
    SQLiteDatabase dbIncome, dbIncome2, dbIncome3, dbIncome4, dbIncome5;
    Cursor cursorIncome, cursorIncome5;

    public IncomeBudgetDbManager(Context context) {
        dbHelperIncome = DbHelper.getInstance(context);
    }

    public List<IncomeBudgetDb> getIncomes() {

        dbIncome = dbHelperIncome.getReadableDatabase();

        cursorIncome = dbIncome.rawQuery(
                "SELECT * FROM " + DbHelper.INCOME_TABLE_NAME + " ORDER BY " + DbHelper.INCOMEANNUALAMOUNT + " DESC",
                null);

        List<IncomeBudgetDb> incomes = new ArrayList<>();

        if (cursorIncome.moveToFirst()) {
            while (!cursorIncome.isAfterLast()) {

                IncomeBudgetDb income = new IncomeBudgetDb(
                        cursorIncome.getString(cursorIncome.getColumnIndex(DbHelper.INCOMENAME)),
                        cursorIncome.getDouble(cursorIncome.getColumnIndex(DbHelper.INCOMEAMOUNT)),
                        cursorIncome.getDouble(cursorIncome.getColumnIndex(DbHelper.INCOMEFREQUENCY)),
                        cursorIncome.getDouble(cursorIncome.getColumnIndex(DbHelper.INCOMEANNUALAMOUNT)),
                        cursorIncome.getLong(cursorIncome.getColumnIndex(DbHelper.ID))
                );

                incomes.add(income); //adds new items to bottom of list
                cursorIncome.moveToNext();
            }
        }
        cursorIncome.close();
        return incomes;
    }

    public void addIncome(IncomeBudgetDb income) {

        ContentValues newIncome = new ContentValues();
        newIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        newIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        newIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        newIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());

        dbIncome2 = dbHelperIncome.getWritableDatabase();
        dbIncome2.insert(DbHelper.INCOME_TABLE_NAME, null, newIncome);
    }

    public void updateIncome(IncomeBudgetDb income) {

        ContentValues updateIncome = new ContentValues();
        updateIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        updateIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        updateIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        updateIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());

        dbIncome3 = dbHelperIncome.getWritableDatabase();

        String[] args = new String[]{String.valueOf(income.getId())};

        dbIncome3.update(
                DbHelper.INCOME_TABLE_NAME,
                updateIncome,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteIncome(IncomeBudgetDb income) {
        dbIncome4 = dbHelperIncome.getWritableDatabase();

        String[] args = new String[]{String.valueOf(income.getId())};

        dbIncome4.delete(
                DbHelper.INCOME_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public Double sumTotalIncome() {
        dbIncome5 = dbHelperIncome.getReadableDatabase();
        cursorIncome5 = dbIncome5.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        cursorIncome5.moveToFirst();
        totalIncome = cursorIncome5.getDouble(0);
        cursorIncome5.close();

        return totalIncome;
    }
}
