package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;

public class MoneyOutDbManager {

    private DbHelper dbHelperMoneyOut;
    SQLiteDatabase dbMoneyOut, dbMoneyOut2, dbMoneyOut3, dbMoneyOut4, dbMoneyOut5, dbMoneyOut6, dbMoneyOut7;
    Cursor cursorMoneyOut, cursorMoneyOut2, cursorMoneyOut3, cursorMoneyOut4;

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
                        cursorMoneyOut.getInt(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut.getInt(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut.getLong(cursorMoneyOut.getColumnIndex(DbHelper.ID))
                );

                moneyOuts.add(0, moneyOut); //adds new items to beginning of list
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
        newMoneyOut.put(DbHelper.MONEYOUTTOPAY, moneyOut.getMoneyOutToPay());
        newMoneyOut.put(DbHelper.MONEYOUTPAID, moneyOut.getMoneyOutPaid());

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
        updateMoneyOut.put(DbHelper.MONEYOUTTOPAY, moneyOut.getMoneyOutToPay());
        updateMoneyOut.put(DbHelper.MONEYOUTPAID, moneyOut.getMoneyOutPaid());

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

    public List<MoneyOutDb> getCashTrans() {

        dbMoneyOut7 = dbHelperMoneyOut.getReadableDatabase();

        cursorMoneyOut4 = dbMoneyOut7.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'N'", null);

        List<MoneyOutDb> cashTrans = new ArrayList<>();

        if (cursorMoneyOut4.moveToFirst()) {
            while (!cursorMoneyOut4.isAfterLast()) {

                MoneyOutDb cashTransList = new MoneyOutDb(
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursorMoneyOut4.getDouble(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut4.getInt(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut4.getInt(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut4.getLong(cursorMoneyOut4.getColumnIndex(DbHelper.ID))
                );

                cashTrans.add(0, cashTransList); //adds new items to beginning of list
                cursorMoneyOut4.moveToNext();
            }
        }
        cursorMoneyOut4.close();
        return cashTrans;
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
                        cursorMoneyOut2.getInt(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut2.getInt(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut2.getLong(cursorMoneyOut2.getColumnIndex(DbHelper.ID))
                );

                ccTrans.add(0, ccTransList); //adds new items to beginning of list
                cursorMoneyOut2.moveToNext();
            }
        }
        cursorMoneyOut2.close();
        return ccTrans;
    }

    public List<MoneyOutDb> getCCTransToPay() {

        dbMoneyOut6 = dbHelperMoneyOut.getReadableDatabase();

        cursorMoneyOut3 = dbMoneyOut6.rawQuery(
                "SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'Y' AND "
                + DbHelper.MONEYOUTPAID + " = '0'", null);

        List<MoneyOutDb> ccTransToPay = new ArrayList<>();

        if (cursorMoneyOut3.moveToFirst()) {
            while (!cursorMoneyOut3.isAfterLast()) {

                MoneyOutDb ccTransToPayList = new MoneyOutDb(
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursorMoneyOut3.getDouble(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut3.getInt(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut3.getInt(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut3.getLong(cursorMoneyOut3.getColumnIndex(DbHelper.ID))
                );

                ccTransToPay.add(ccTransToPayList); //adds new items to end of list
                cursorMoneyOut3.moveToNext();
            }
        }
        cursorMoneyOut3.close();
        return ccTransToPay;
    }
}
