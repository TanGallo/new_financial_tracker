package ca.gotchasomething.mynance.data;
//ContentProvider
/*
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

public class DebtDbManager {

    public boolean foundNoDebtId;
    Calendar cal;
    Date debtEndD;
    private DbHelper dbHelperDebt;
    public Double totalDebt;
    ExpenseBudgetDb expenseBudgetDb;
    SQLiteDatabase dbDebt, dbDebt2, dbDebt3, dbDebt4, dbDebt5, dbDebt6;
    Cursor cursorDebt, cursorDebt5, cursorDebt6;
    Double numberOfYearsToPayDebt = 0.0;
    Integer numberOfDaysToPayDebt = 0;
    public long id;

    SimpleDateFormat debtEndS;
    String debtEnd = null;

    public DebtDbManager(Context context) {
        dbHelperDebt = DbHelper.getInstance(context);
    }

    public List<DebtDb> getDebts() {

        dbDebt = dbHelperDebt.getReadableDatabase();

        cursorDebt = dbDebt.rawQuery(
                "SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTRATE + " DESC",
                null);

        List<DebtDb> debts = new ArrayList<>();

        if (cursorDebt.moveToFirst()) {
            while (!cursorDebt.isAfterLast()) {

                DebtDb debt = new DebtDb(
                        cursorDebt.getString(cursorDebt.getColumnIndex(DbHelper.DEBTNAME)),
                        cursorDebt.getDouble(cursorDebt.getColumnIndex(DbHelper.DEBTAMOUNT)),
                        cursorDebt.getDouble(cursorDebt.getColumnIndex(DbHelper.DEBTRATE)),
                        cursorDebt.getDouble(cursorDebt.getColumnIndex(DbHelper.DEBTPAYMENTS)),
                        cursorDebt.getDouble(cursorDebt.getColumnIndex(DbHelper.DEBTFREQUENCY)),
                        cursorDebt.getString(cursorDebt.getColumnIndex(DbHelper.DEBTEND)),
                        cursorDebt.getLong(cursorDebt.getColumnIndex(DbHelper.EXPREFKEYD)),
                        cursorDebt.getLong(cursorDebt.getColumnIndex(DbHelper.ID))
                );

                debts.add(debt); //adds new items to end of list
                cursorDebt.moveToNext();
            }
        }
        cursorDebt.close();
        return debts;
    }

    public void addDebt(DebtDb debt) {

        ContentValues newDebt = new ContentValues();
        newDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        newDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        newDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        newDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        newDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        newDebt.put(DbHelper.DEBTEND, debtEndDate(debt));
        newDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());

        dbDebt2 = dbHelperDebt.getWritableDatabase();
        dbDebt2.insert(DbHelper.DEBTS_TABLE_NAME, null, newDebt);
    }

    public void updateDebt(DebtDb debt) {

        ContentValues updateDebt = new ContentValues();
        updateDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        updateDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        updateDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        updateDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        updateDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        updateDebt.put(DbHelper.DEBTEND, debtEndDate(debt));
        updateDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());

        dbDebt3 = dbHelperDebt.getWritableDatabase();

        String[] args = new String[]{String.valueOf(debt.getId())};

        dbDebt3.update(
                DbHelper.DEBTS_TABLE_NAME,
                updateDebt,
                DbHelper.ID + "=?",
                args);
    }

    public String debtEndDate(DebtDb debt) {

        cal = Calendar.getInstance();
        //years = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))))
        numberOfYearsToPayDebt = -(Math.log(1 - (debt.getDebtAmount() * (debt.getDebtRate() / 100) /
                (debt.getDebtPayments() * debt.getDebtFrequency()))) / (debt.getDebtFrequency() *
                Math.log(1 + ((debt.getDebtRate() / 100) / debt.getDebtFrequency()))));
        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);

        if (debt.getDebtAmount() <= 0) {
            debtEnd = "Debt paid!";

        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt <= 0) {
            debtEnd = "Too far in the future";

        } else {
            cal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = cal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = "Will be paid by " + debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    public void deleteDebt(DebtDb debt) {
        dbDebt4 = dbHelperDebt.getWritableDatabase();

        String[] args = new String[]{String.valueOf(debt.getId())};

        dbDebt4.delete(
                DbHelper.DEBTS_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public Double sumTotalDebt() {
        dbDebt5 = dbHelperDebt.getReadableDatabase();
        cursorDebt5 = dbDebt5.rawQuery("SELECT sum(debtAmount)" + " FROM " + DbHelper.DEBTS_TABLE_NAME, null);
        cursorDebt5.moveToFirst();
        totalDebt = cursorDebt5.getDouble(0);
        cursorDebt5.close();

        return totalDebt;
    }
}
*/