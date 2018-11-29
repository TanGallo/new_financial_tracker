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

    private DbHelper dbHelperSetUp;
    SQLiteDatabase dbSetUp, dbSetUp2, dbSetUp3, dbSetUp4;
    Cursor cursorSetUp;

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
}
