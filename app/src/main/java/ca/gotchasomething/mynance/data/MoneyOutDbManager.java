package ca.gotchasomething.mynance.data;
//ContentProvider

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;

public class MoneyOutDbManager {

    public Cursor cursorMoneyOut, cursorMoneyOut2, cursorMoneyOut3, cursorMoneyOut4, cursorMoneyOut8, cursorMoneyOut9, cursorMoneyOut11;
    private DbHelper dbHelperMoneyOut;
    public Double totalCCPaymentDue, totalCCPaymentBDue;
    General general = new General();
    SQLiteDatabase dbMoneyOut, dbMoneyOut2, dbMoneyOut3, dbMoneyOut4, dbMoneyOut5, dbMoneyOut6, dbMoneyOut7, dbMoneyOut8, dbMoneyOut9, dbMoneyOut10,
            dbMoneyOut11;

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
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursorMoneyOut.getDouble(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut.getString(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut.getInt(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut.getInt(cursorMoneyOut.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut.getLong(cursorMoneyOut.getColumnIndex(DbHelper.EXPREFKEYMO)),
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
        newMoneyOut.put(DbHelper.MONEYOUTWEEKLY, moneyOut.getMoneyOutWeekly());
        newMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        newMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        newMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());
        newMoneyOut.put(DbHelper.MONEYOUTTOPAY, moneyOut.getMoneyOutToPay());
        newMoneyOut.put(DbHelper.MONEYOUTPAID, moneyOut.getMoneyOutPaid());
        newMoneyOut.put(DbHelper.EXPREFKEYMO, moneyOut.getExpRefKeyMO());

        dbMoneyOut2 = dbHelperMoneyOut.getWritableDatabase();
        dbMoneyOut2.insert(DbHelper.MONEY_OUT_TABLE_NAME, null, newMoneyOut);
    }

    public void updateMoneyOut(MoneyOutDb moneyOut) {

        ContentValues updateMoneyOut = new ContentValues();
        updateMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        updateMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        updateMoneyOut.put(DbHelper.MONEYOUTWEEKLY, moneyOut.getMoneyOutWeekly());
        updateMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        updateMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        updateMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());
        updateMoneyOut.put(DbHelper.MONEYOUTTOPAY, moneyOut.getMoneyOutToPay());
        updateMoneyOut.put(DbHelper.MONEYOUTPAID, moneyOut.getMoneyOutPaid());
        updateMoneyOut.put(DbHelper.EXPREFKEYMO, moneyOut.getExpRefKeyMO());

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
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursorMoneyOut4.getDouble(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut4.getString(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut4.getInt(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut4.getInt(cursorMoneyOut4.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut4.getLong(cursorMoneyOut4.getColumnIndex(DbHelper.EXPREFKEYMO)),
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
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursorMoneyOut2.getDouble(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut2.getString(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut2.getInt(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut2.getInt(cursorMoneyOut2.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut2.getLong(cursorMoneyOut2.getColumnIndex(DbHelper.EXPREFKEYMO)),
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
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursorMoneyOut3.getDouble(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut3.getString(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut3.getInt(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut3.getInt(cursorMoneyOut3.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut3.getLong(cursorMoneyOut3.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursorMoneyOut3.getLong(cursorMoneyOut3.getColumnIndex(DbHelper.ID))
                );

                ccTransToPay.add(ccTransToPayList); //adds new items to end of list
                cursorMoneyOut3.moveToNext();
            }
        }
        cursorMoneyOut3.close();
        return ccTransToPay;
    }

    public Double retrieveToPayTotal() {
        dbMoneyOut8 = dbHelperMoneyOut.getReadableDatabase();
        cursorMoneyOut8 = dbMoneyOut8.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1' AND " + DbHelper.MONEYOUTPAID + " = '0'", null);
        try {
            cursorMoneyOut8.moveToFirst();
        } catch (Exception e) {
            totalCCPaymentDue = 0.0;
        }
        totalCCPaymentDue = cursorMoneyOut8.getDouble(0);
        cursorMoneyOut8.close();

        return totalCCPaymentDue;
    }

    public Double retrieveToPayBTotal() {
        dbMoneyOut9 = dbHelperMoneyOut.getReadableDatabase();
        cursorMoneyOut9 = dbMoneyOut9.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1' AND " + DbHelper.MONEYOUTPAID + " = '0' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        try {
            cursorMoneyOut9.moveToFirst();
        } catch (Exception e) {
            totalCCPaymentBDue = 0.0;
        }
        totalCCPaymentBDue = cursorMoneyOut9.getDouble(0);
        cursorMoneyOut9.close();

        return totalCCPaymentBDue;
    }

    public void updatePaid() {

        dbMoneyOut10 = dbHelperMoneyOut.getWritableDatabase();
        ContentValues updateMoneyOutPaid = new ContentValues();
        updateMoneyOutPaid.put(DbHelper.MONEYOUTPAID, 1);
        dbMoneyOut10.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutPaid, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID
                + " = '0'", null);
    }

    /*public void getSpentThisWeek() {

        dbMoneyOut11 = dbHelperMoneyOut.getReadableDatabase();
        cursorMoneyOut11 = dbMoneyOut11.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE "
                + DbHelper.MONEYOUTWEEKLY + " = 'Y' AND " + DbHelper.MONEYOUTCREATEDON + " IN " + general.validDates()
                + " GROUP BY " + DbHelper.MONEYOUTCAT, null);*/

        /*dbHelperMoneyOut.getWritableDatabase();

        List<Double> spentThisWeekList = new ArrayList<>();

        if(cursorMoneyOut11.moveToFirst()) {
            do {
                spentThisWeekList.add(cursorMoneyOut11.getDouble(0));
            } while(cursorMoneyOut11.moveToNext());
        }

        if (cursorMoneyOut11.moveToFirst()) {
            while (!cursorMoneyOut11.isAfterLast()) {

                MoneyOutDb spentThisWeek = new MoneyOutDb(
                        cursorMoneyOut11.getString(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursorMoneyOut11.getString(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursorMoneyOut11.getString(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursorMoneyOut11.getDouble(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursorMoneyOut11.getString(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursorMoneyOut11.getString(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursorMoneyOut11.getInt(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursorMoneyOut11.getInt(cursorMoneyOut11.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursorMoneyOut11.getLong(cursorMoneyOut11.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursorMoneyOut11.getLong(cursorMoneyOut11.getColumnIndex(DbHelper.ID))
                );

                spentThisWeekList.add(spentThisWeek);
                cursorMoneyOut11.moveToNext();
            }
        }
        cursorMoneyOut11.close();
        return spentThisWeekList;*/
    //}
}
