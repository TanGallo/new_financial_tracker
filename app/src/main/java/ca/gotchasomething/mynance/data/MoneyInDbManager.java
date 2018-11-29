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

public class MoneyInDbManager {

    private DbHelper dbHelperMoneyIn;
    SQLiteDatabase dbMoneyIn, dbMoneyIn2, dbMoneyIn3, dbMoneyIn4;
    Cursor cursorMoneyIn;

    public MoneyInDbManager(Context context) {
        dbHelperMoneyIn = DbHelper.getInstance(context);
    }

    public List<MoneyInDb> getMoneyIns() {

        dbMoneyIn = dbHelperMoneyIn.getReadableDatabase();

        cursorMoneyIn = dbMoneyIn.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);

        List<MoneyInDb> moneyIns = new ArrayList<>();

        if (cursorMoneyIn.moveToFirst()) {
            while (!cursorMoneyIn.isAfterLast()) {

                MoneyInDb moneyIn = new MoneyInDb(
                        cursorMoneyIn.getString(cursorMoneyIn.getColumnIndex(DbHelper.MONEYINCAT)),
                        cursorMoneyIn.getDouble(cursorMoneyIn.getColumnIndex(DbHelper.MONEYINAMOUNT)),
                        cursorMoneyIn.getString(cursorMoneyIn.getColumnIndex(DbHelper.MONEYINCREATEDON)),
                        cursorMoneyIn.getLong(cursorMoneyIn.getColumnIndex(DbHelper.ID))
                );

                moneyIns.add(0, moneyIn); //adds new items to beginning of list
                cursorMoneyIn.moveToNext();
            }
        }
        cursorMoneyIn.close();
        return moneyIns;
    }

    public void addMoneyIn(MoneyInDb moneyIn) {

        ContentValues newMoneyIn = new ContentValues();
        newMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        newMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        newMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());

        dbMoneyIn2 = dbHelperMoneyIn.getWritableDatabase();
        dbMoneyIn2.insert(DbHelper.MONEY_IN_TABLE_NAME, null, newMoneyIn);
    }

    public void updateMoneyIn(MoneyInDb moneyIn) {

        ContentValues updateMoneyIn = new ContentValues();
        updateMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        updateMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        updateMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());

        dbMoneyIn3 = dbHelperMoneyIn.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyIn.getId())};

        dbMoneyIn3.update(
                DbHelper.MONEY_IN_TABLE_NAME,
                updateMoneyIn,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteMoneyIn(MoneyInDb moneyIn) {
        dbMoneyIn4 = dbHelperMoneyIn.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyIn.getId())};

        dbMoneyIn4.delete(
                DbHelper.MONEY_IN_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }
}
