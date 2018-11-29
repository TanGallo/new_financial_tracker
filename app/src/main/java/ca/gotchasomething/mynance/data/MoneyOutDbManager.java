package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;

public class MoneyOutDbManager {

    private DbHelper dbHelperMoneyOut;
    SQLiteDatabase dbMoneyOut, dbMoneyOut2, dbMoneyOut3, dbMoneyOut4, dbMoneyOut5;
    Cursor cursorMoneyOut, cursorMoneyOut2;

    public MoneyOutDbManager(Context context) {
        dbHelperMoneyOut = DbHelper.getInstance(context);
    }

    public List<MoneyOutDb> getMoneyOuts() {

        dbMoneyOut = dbHelperMoneyOut.getReadableDatabase();

        cursorMoneyOut = dbMoneyOut.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);

        List<MoneyOutDb> moneyOuts = new ArrayList<>();

        if (cursorMoneyOut.moveToFirst()) {
            while (!cursorMoneyOut.isAfterLast()) {

                MoneyOutDb moneyOut = new MoneyOutDb(
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursorMoneyOut.getDouble(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut.getLong(cursorMoneyOut.getColumnIndex(DbHelper.ID))
                );

                moneyOuts.add(moneyOut); //adds new items to end of list
                cursorMoneyOut.moveToNext();
            }
        }
        cursorMoneyOut.close();
        return moneyOuts;
    }

    public void addMoneyOut(MoneyOutDb moneyOut) {

        ContentValues newMoneyOut = new ContentValues();
        newMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        newMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        newMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        newMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        newMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());

        dbMoneyOut2 = dbHelperMoneyOut.getWritableDatabase();
        dbMoneyOut2.insert(DbHelper.MONEY_OUT_TABLE_NAME, null, newMoneyOut);
    }

    public void updateMoneyOut(MoneyOutDb moneyOut) {

        ContentValues updateMoneyOut = new ContentValues();
        updateMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        updateMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        updateMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        updateMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        updateMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());

        dbMoneyOut3 = dbHelperMoneyOut.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyOut.getId())};

        dbMoneyOut3.update(
                DbHelper.MONEY_OUT_TABLE_NAME,
                updateMoneyOut,
                DbHelper.ID + "=?",
                args);
    }

    public void deleteMoneyOut(MoneyOutDb moneyOut) {
        dbMoneyOut4 = dbHelperMoneyOut.getWritableDatabase();

        String[] args = new String[]{String.valueOf(moneyOut.getId())};

        dbMoneyOut4.delete(
                DbHelper.MONEY_OUT_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public List<MoneyOutDb> getCCTrans() {

        dbMoneyOut5 = dbHelperMoneyOut.getReadableDatabase();

        cursorMoneyOut2 = dbMoneyOut5.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'Y'", null);

        List<MoneyOutDb> ccTrans = new ArrayList<>();

        if (cursorMoneyOut2.moveToFirst()) {
            while (!cursorMoneyOut2.isAfterLast()) {

                MoneyOutDb ccTransList = new MoneyOutDb(
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursorMoneyOut2.getDouble(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut2.getLong(cursorMoneyOut2.getColumnIndex(DbHelper.ID))
                );

                ccTrans.add(ccTransList); //adds new items to end of list
                cursorMoneyOut2.moveToNext();
            }
        }
        cursorMoneyOut2.close();
        return ccTrans;
    }
}
