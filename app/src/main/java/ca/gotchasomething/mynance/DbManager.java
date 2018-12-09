package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class DbManager {

    public Calendar cal;
    public Cursor cursor;
    public Date debtEndD, savingsDateD;
    public DbHelper dbHelper;
    public Double startingBalanceResult, totalDebt, numberOfYearsToPayDebt, numberOfYearsToSavingsGoal, totalSavings, totalExpenses;
    public int tourDoneCheck, balanceDoneCheck, budgetDoneCheck, savingsDoneCheck, debtsDoneCheck, numberOfDaysToPayDebt, numberOfDaysToSavingsGoal;
    public SimpleDateFormat debtEndS, savingsDateS;
    public SQLiteDatabase db;
    public String debtEnd, savingsDate;

    public DbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public List<SetUpDb> getSetUp() {
        List<SetUpDb> setUp = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SetUpDb setUps = new SetUpDb(
                        cursor.getInt(cursor.getColumnIndex(DbHelper.DEBTSDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.SAVINGSDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.BUDGETDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.BALANCEDONE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BALANCEAMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.TOURDONE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                setUp.add(setUps); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
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
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SET_UP_TABLE_NAME, null, newSetUp);
    }

    public void updateSetUp(SetUpDb setUp) {
        ContentValues updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.DEBTSDONE, setUp.getDebtsDone());
        updateSetUp.put(DbHelper.SAVINGSDONE, setUp.getSavingsDone());
        updateSetUp.put(DbHelper.BUDGETDONE, setUp.getBudgetDone());
        updateSetUp.put(DbHelper.BALANCEDONE, setUp.getBalanceDone());
        updateSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        updateSetUp.put(DbHelper.TOURDONE, setUp.getTourDone());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(setUp.getId())};
        db.update(
                DbHelper.SET_UP_TABLE_NAME, updateSetUp, DbHelper.ID + "=?", args);
    }

    public void deleteSetUp(SetUpDb setUp) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(setUp.getId())};
        db.delete(DbHelper.SET_UP_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public int tourSetUpCheck() {
        List<Integer> tourDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            tourDoneList.add(s.getTourDone());
        }
        if (tourDoneList.size() == 0) {
            tourDoneCheck = 0;
        } else {
            tourDoneCheck = Collections.max(tourDoneList);
        }
        return tourDoneCheck;
    }

    public Double retrieveStartingBalance() {
        List<Double> startingBalanceList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            startingBalanceList.add(s.getBalanceAmount());
        }
        if (startingBalanceList.size() == 0) {
            startingBalanceResult = 0.0;
        } else {
            startingBalanceResult = Collections.max(startingBalanceList);
        }
        return startingBalanceResult;
    }

    public int balanceSetUpCheck() {
        List<Integer> balanceDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            balanceDoneList.add(s.getBalanceDone());
        }
        if (balanceDoneList.size() == 0) {
            balanceDoneCheck = 0;
        } else {
            balanceDoneCheck = Collections.max(balanceDoneList);
        }
        return balanceDoneCheck;
    }

    public int budgetSetUpCheck() {
        List<Integer> budgetDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            budgetDoneList.add(s.getBudgetDone());
        }
        if (budgetDoneList.size() == 0) {
            budgetDoneCheck = 0;
        } else {
            budgetDoneCheck = Collections.max(budgetDoneList);
        }
        return budgetDoneCheck;
    }

    public int savingsSetUpCheck() {
        List<Integer> savingsDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            savingsDoneList.add(s.getSavingsDone());
        }
        if (savingsDoneList.size() == 0) {
            savingsDoneCheck = 0;
        } else {
            savingsDoneCheck = Collections.max(savingsDoneList);
        }
        return savingsDoneCheck;
    }

    public int debtSetUpCheck() {
        List<Integer> debtsDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            debtsDoneList.add(s.getDebtsDone());
        }
        if (debtsDoneList.size() == 0) {
            debtsDoneCheck = 0;
        } else {
            debtsDoneCheck = Collections.max(debtsDoneList);
        }
        return debtsDoneCheck;
    }

    public List<DebtDb> getDebts() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTRATE + " DESC", null);
        List<DebtDb> debts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                DebtDb debt = new DebtDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTEND)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYD)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                debts.add(debt); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
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
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.DEBTS_TABLE_NAME, null, newDebt);
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
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.update(DbHelper.DEBTS_TABLE_NAME, updateDebt, DbHelper.ID + "=?", args);
    }

    public void deleteDebt(DebtDb debt) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.delete(DbHelper.DEBTS_TABLE_NAME, DbHelper.ID + "=?", args);
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

    public Double sumTotalDebt() {
        List<Double> debtAmountList = new ArrayList<>(getDebts().size());
        totalDebt = 0.0;
        for (DebtDb d : getDebts()) {
            debtAmountList.add(d.getDebtAmount());
        }
        if (debtAmountList.size() == 0) {
            totalDebt = 0.0;
        } else {
            for (Double dbl : debtAmountList) {
                totalDebt += dbl;
            }
        }
        return totalDebt;
    }

    public List<SavingsDb> getSavings() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.SAVINGS_TABLE_NAME + " ORDER BY " + DbHelper.SAVINGSGOAL + " DESC", null);
        List<SavingsDb> savings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SavingsDb saving = new SavingsDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSGOAL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSDATE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYS)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                savings.add(saving); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
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
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SAVINGS_TABLE_NAME, null, newSavings);
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
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(saving.getId())};
        db.update(DbHelper.SAVINGS_TABLE_NAME, updateSaving, DbHelper.ID + "=?", args);
    }

    public String savingsEndDate(SavingsDb saving) {
        cal = Calendar.getInstance();
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
            cal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
            savingsDateD = cal.getTime();
            savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
            savingsDate = "Will be saved by " + savingsDateS.format(savingsDateD);
        }
        return savingsDate;
    }

    public void deleteSavings(SavingsDb saving) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(saving.getId())};
        db.delete(DbHelper.SAVINGS_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public Double sumTotalSavings() {
        List<Double> savingsAmountList = new ArrayList<>(getSavings().size());
        totalSavings = 0.0;
        for (SavingsDb d : getSavings()) {
            savingsAmountList.add(d.getSavingsAmount());
        }
        if (savingsAmountList.size() == 0) {
            totalSavings = 0.0;
        } else {
            for (Double dbl : savingsAmountList) {
                totalSavings += dbl;
            }
        }
        return totalSavings;
    }

    public List<ExpenseBudgetDb> getExpense() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSEAANNUALAMOUNT + " DESC", null);
        List<ExpenseBudgetDb> expenses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExpenseBudgetDb expense = new ExpenseBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                expenses.add(expense); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
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
        db = dbHelper.getWritableDatabase();
        db.insertOrThrow(DbHelper.EXPENSES_TABLE_NAME, null, newExpense);
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
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(expense.getId())};
        db.update(DbHelper.EXPENSES_TABLE_NAME, updateExpense, DbHelper.ID + "=?", args);
    }

    public void deleteExpense(ExpenseBudgetDb expense) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(expense.getId())};
        db.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<ExpenseBudgetDb> getWeeklyLimits() {
        List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>(getExpense().size());
        for (ExpenseBudgetDb e : getExpense()) {
            weeklyLimits.add(e.getExpenseBAnnualAmount());
        }

        db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery(
                "SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.EXPENSEWEEKLY +
                        " = 'Y'",
                null);

        //List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                ExpenseBudgetDb weekly = new ExpenseBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEFREQUENCY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.EXPENSEWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEAANNUALAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.EXPENSEBANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );

                weeklyLimits.add(weekly); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return weeklyLimits;
    }

    public Double sumTotalExpenses() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT sum(expenseAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        cursor.moveToFirst();
        totalExpenses = cursor.getDouble(0);
        cursor.close();

        return totalExpenses;
    }
}
