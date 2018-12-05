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

public class SavingsDbManager {

    private DbHelper dbHelperSavings;
    SQLiteDatabase dbSavings, dbSavings2, dbSavings3, dbSavings4, dbSavings5;
    Cursor cursorSavings, cursorSavings5;
    public Double numberOfYearsToSavingsGoal = 0.0, totalSavings;
    Integer numberOfDaysToSavingsGoal = 0;
    Calendar savingsCal;
    Date savingsDateD;
    SimpleDateFormat savingsDateS;
    String savingsDate = null;

    public SavingsDbManager(Context context) {
        dbHelperSavings = DbHelper.getInstance(context);
    }

    public List<SavingsDb> getSavings() {

        dbSavings = dbHelperSavings.getReadableDatabase();

        cursorSavings = dbSavings.rawQuery(
                "SELECT * FROM " + DbHelper.SAVINGS_TABLE_NAME + " ORDER BY " + DbHelper.SAVINGSGOAL + " DESC",
                null);

        List<SavingsDb> savings = new ArrayList<>();

        if (cursorSavings.moveToFirst()) {
            while (!cursorSavings.isAfterLast()) {

                SavingsDb saving = new SavingsDb(
                        cursorSavings.getString(cursorSavings.getColumnIndex(DbHelper.SAVINGSNAME)),
                        cursorSavings.getDouble(cursorSavings.getColumnIndex(DbHelper.SAVINGSAMOUNT)),
                        cursorSavings.getDouble(cursorSavings.getColumnIndex(DbHelper.SAVINGSRATE)),
                        cursorSavings.getDouble(cursorSavings.getColumnIndex(DbHelper.SAVINGSPAYMENTS)),
                        cursorSavings.getDouble(cursorSavings.getColumnIndex(DbHelper.SAVINGSFREQUENCY)),
                        cursorSavings.getDouble(cursorSavings.getColumnIndex(DbHelper.SAVINGSGOAL)),
                        cursorSavings.getString(cursorSavings.getColumnIndex(DbHelper.SAVINGSDATE)),
                        cursorSavings.getLong(cursorSavings.getColumnIndex(DbHelper.EXPREFKEYS)),
                        cursorSavings.getLong(cursorSavings.getColumnIndex(DbHelper.ID))
                );

                savings.add(saving); //adds new items to end of list
                cursorSavings.moveToNext();
            }
        }
        cursorSavings.close();
        return savings;
    }

    public void addSavings(SavingsDb saving) {

        ContentValues newSavings = new ContentValues();
        newSavings.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        newSavings.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        newSavings.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        newSavings.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        newSavings.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        newSavings.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        newSavings.put(DbHelper.SAVINGSDATE, savingsEndDate(saving));
        newSavings.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());

        dbSavings2 = dbHelperSavings.getWritableDatabase();
        dbSavings2.insert(DbHelper.SAVINGS_TABLE_NAME, null, newSavings);
    }

    public void updateSavings(SavingsDb saving) {

        ContentValues updateSaving = new ContentValues();
        updateSaving.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        updateSaving.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        updateSaving.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        updateSaving.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        updateSaving.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        updateSaving.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        updateSaving.put(DbHelper.SAVINGSDATE, savingsEndDate(saving));
        updateSaving.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());

        dbSavings3 = dbHelperSavings.getWritableDatabase();

        String[] args = new String[]{String.valueOf(saving.getId())};

        dbSavings3.update(
                DbHelper.SAVINGS_TABLE_NAME,
                updateSaving,
                DbHelper.ID + "=?",
                args);
    }

    public String savingsEndDate(SavingsDb saving) {

        savingsCal = Calendar.getInstance();
        //years = -(Math.log(1 - (amount * (rate / 100) / (payments * frequency))) / (frequency * Math.log(1 + ((rate / 100) / frequency))))
        numberOfYearsToSavingsGoal = -(Math.log(1 - ((saving.getSavingsGoal() - saving.getSavingsAmount()) * (saving.getSavingsRate() / 100) /
                (saving.getSavingsPayments() * saving.getSavingsFrequency()))) / (saving.getSavingsFrequency() *
                Math.log(1 + ((saving.getSavingsRate() / 100) / saving.getSavingsFrequency()))));
        numberOfDaysToSavingsGoal = (int) Math.round(numberOfYearsToSavingsGoal * 365);

        if (saving.getSavingsGoal() - saving.getSavingsAmount() <= 0) {
            savingsDate = "Goal_achieved!";

        } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal <= 0) {
            savingsDate = "Too far in the future";

        } else {

            savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = savingsCal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = "Will be saved by " + savingsDateS.format(savingsDateD);
        }

        return savingsDate;
    }

    public void deleteSavings(SavingsDb saving) {
        dbSavings4 = dbHelperSavings.getWritableDatabase();

        String[] args = new String[]{String.valueOf(saving.getId())};

        dbSavings4.delete(
                DbHelper.SAVINGS_TABLE_NAME,
                DbHelper.ID + "=?",
                args);
    }

    public Double sumTotalSavings() {
        dbSavings5 = dbHelperSavings.getReadableDatabase();
        cursorSavings5 = dbSavings5.rawQuery("SELECT sum(savingsAmount)" + " FROM " + DbHelper.SAVINGS_TABLE_NAME, null);
        cursorSavings5.moveToFirst();
        totalSavings = cursorSavings5.getDouble(0);
        cursorSavings5.close();

        return totalSavings;
    }
}
