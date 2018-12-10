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
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
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
        /*List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();
        for (ExpenseBudgetDb e : getExpense()) {
            if(e.getExpenseWeekly() == "Y") {
                weeklyLimits.add(e);
                String weeklyCat = e.getExpenseName();
                Double weeklyAnnual = e.getExpenseBAnnualAmount();
                Double weeklyWeekly = weeklyAnnual / 52;
                Double spentThisWeek = ;
                Double leftThisWeek = weeklyWeekly - spentThisWeek;
                //amount left
                //amount already spent this week
            }
        }*/
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " WHERE " + DbHelper.EXPENSEWEEKLY + " = 'Y'", null);
        List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();
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
        List<Double> expensesAmountList = new ArrayList<>(getExpense().size());
        totalExpenses = 0.0;
        for (ExpenseBudgetDb e : getExpense()) {
            expensesAmountList.add(e.getExpenseAmount());
        }
        if (expensesAmountList.size() == 0) {
            totalExpenses = 0.0;
        } else {
            for (Double dbl : expensesAmountList) {
                totalExpenses += dbl;
            }
        }
        return totalExpenses;
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
}
