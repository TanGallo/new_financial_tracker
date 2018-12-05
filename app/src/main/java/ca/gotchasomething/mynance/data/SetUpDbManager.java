package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class SetUpDbManager {

    private DbHelper dbHelperSetUp, dbHelperSetUp5, dbHelperSetUp6;
    public Double startingBalanceResult;
    public int tourDoneCheck, balanceDoneCheck, budgetDoneCheck, savingsDoneCheck, debtsDoneCheck;
    SQLiteDatabase dbSetUp, dbSetUp2, dbSetUp3, dbSetUp4, dbSetUp5, dbSetUp6, dbSetUp7, dbSetUp8, dbSetUp9, dbSetUp10;
    Cursor cursorSetUp, cursorSetUp5, cursorSetUp6, cursorSetUp7, cursorSetUp8, cursorSetUp9, cursorSetUp10;

    public SetUpDbManager(Context context) {
        dbHelperSetUp = DbHelper.getInstance(context);
    }

    public List<SetUpDb> getSetUp() {

        dbSetUp = dbHelperSetUp.getReadableDatabase();

        cursorSetUp = dbSetUp.rawQuery(
                "SELECT * FROM " + DbHelper.SET_UP_TABLE_NAME, null);

        List<SetUpDb> setUp = new ArrayList<>();

        if (cursorSetUp.moveToFirst()) {
            while (!cursorSetUp.isAfterLast()) {

                SetUpDb setUps = new SetUpDb(
                        cursorSetUp.getInt(cursorSetUp.getColumnIndex(DbHelper.DEBTSDONE)),
                        cursorSetUp.getInt(cursorSetUp.getColumnIndex(DbHelper.SAVINGSDONE)),
                        cursorSetUp.getInt(cursorSetUp.getColumnIndex(DbHelper.BUDGETDONE)),
                        cursorSetUp.getInt(cursorSetUp.getColumnIndex(DbHelper.BALANCEDONE)),
                        cursorSetUp.getDouble(cursorSetUp.getColumnIndex(DbHelper.BALANCEAMOUNT)),
                        cursorSetUp.getInt(cursorSetUp.getColumnIndex(DbHelper.TOURDONE)),
                        cursorSetUp.getLong(cursorSetUp.getColumnIndex(DbHelper.ID))
                );

                setUp.add(setUps); //adds new items to end of list
                cursorSetUp.moveToNext();
            }
        }
        cursorSetUp.close();
        return setUp;
    }

    public void addSetUp(SetUpDb setUp) {

        ContentValues newSetUp = new ContentValues();
        newSetUp.put(DbHelper.DEBTSDONE, setUp.getDebtsDone());
        newSetUp.put(DbHelper.SAVINGSDONE, setUp.getSavingsDone());
        newSetUp.put(DbHelper.BUDGETDONE, setUp.getBudgetDone());
        newSetUp.put(DbHelper.BALANCEDONE, setUp.getBalanceDone());
        newSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        newSetUp.put(DbHelper.TOURDONE, setUp.getTourDone());

        dbSetUp2 = dbHelperSetUp.getWritableDatabase();
        dbSetUp2.insert(DbHelper.SET_UP_TABLE_NAME, null, newSetUp);
    }

    public void updateSetUp(SetUpDb setUp) {

        ContentValues updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.DEBTSDONE, setUp.getDebtsDone());
        updateSetUp.put(DbHelper.SAVINGSDONE, setUp.getSavingsDone());
        updateSetUp.put(DbHelper.BUDGETDONE, setUp.getBudgetDone());
        updateSetUp.put(DbHelper.BALANCEDONE, setUp.getBalanceDone());
        updateSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        updateSetUp.put(DbHelper.TOURDONE, setUp.getTourDone());

        dbSetUp3 = dbHelperSetUp.getWritableDatabase();

        String[] args = new String[]{String.valueOf(setUp.getId())};

        dbSetUp3.update(
                DbHelper.SET_UP_TABLE_NAME,
                updateSetUp,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteSetUp(SetUpDb setUp) {
        dbSetUp4 = dbHelperSetUp.getWritableDatabase();

        String[] args = new String[]{String.valueOf(setUp.getId())};

        dbSetUp4.delete(
                DbHelper.SET_UP_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public int tourSetUpCheck() {
        dbSetUp5 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp5 = dbSetUp5.rawQuery("SELECT max(tourDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp5.moveToFirst();
        tourDoneCheck = cursorSetUp5.getInt(0);
        cursorSetUp5.close();

        return tourDoneCheck;
    }

    public Double retrieveStartingBalance() {
        dbSetUp6 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp6 = dbSetUp6.rawQuery("SELECT max(balanceAmount)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp6.moveToFirst();
        startingBalanceResult = cursorSetUp6.getDouble(0);
        cursorSetUp6.close();

        return startingBalanceResult;
    }

    public int balanceSetUpCheck() {
        dbSetUp7 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp7 = dbSetUp7.rawQuery("SELECT max(balanceDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp7.moveToFirst();
        balanceDoneCheck = cursorSetUp7.getInt(0);
        cursorSetUp7.close();

        return balanceDoneCheck;
    }

    public int budgetSetUpCheck() {
        dbSetUp8 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp8 = dbSetUp8.rawQuery("SELECT max(budgetDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp8.moveToFirst();
        budgetDoneCheck = cursorSetUp8.getInt(0);
        cursorSetUp8.close();

        return budgetDoneCheck;
    }

    public int savingsSetUpCheck() {
        dbSetUp9 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp9 = dbSetUp9.rawQuery("SELECT max(savingsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp9.moveToFirst();
        savingsDoneCheck = cursorSetUp9.getInt(0);
        cursorSetUp9.close();

        return savingsDoneCheck;
    }

    public int debtSetUpCheck() {
        dbSetUp10 = dbHelperSetUp.getReadableDatabase();
        cursorSetUp10 = dbSetUp10.rawQuery("SELECT max(debtsDone)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        cursorSetUp10.moveToFirst();
        debtsDoneCheck = cursorSetUp10.getInt(0);
        cursorSetUp10.close();

        return debtsDoneCheck;
    }
}
