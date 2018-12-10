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

import ca.gotchasomething.mynance.data.CurrentDb;
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
    public Double startingBalanceResult, totalDebt, numberOfYearsToPayDebt, numberOfYearsToSavingsGoal, totalSavings, totalExpenses, totalIncome,
            totalCCPaymentDue, totalCCPaymentBDue, currentAccountBalance, currentAvailableBalance, totalBudgetAExpenses, percentB;
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
        tourDoneCheck = 0;
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
        startingBalanceResult = 0.0;
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
        balanceDoneCheck = 0;
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
        budgetDoneCheck = 0;
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
        savingsDoneCheck = 0;
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
        debtsDoneCheck = 0;
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
        totalDebt = 0.0;
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
        totalSavings = 0.0;
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
            expensesAmountList.add(e.getExpenseAnnualAmount());
        }
        totalExpenses = 0.0;
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
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.INCOME_TABLE_NAME + " ORDER BY " + DbHelper.INCOMEANNUALAMOUNT + " DESC", null);
        List<IncomeBudgetDb> incomes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                IncomeBudgetDb income = new IncomeBudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.INCOMENAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.INCOMEANNUALAMOUNT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                incomes.add(income); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return incomes;
    }

    public void addIncome(IncomeBudgetDb income) {
        ContentValues newIncome = new ContentValues();
        newIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        newIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        newIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        newIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.INCOME_TABLE_NAME, null, newIncome);
    }

    public void updateIncome(IncomeBudgetDb income) {
        ContentValues updateIncome = new ContentValues();
        updateIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        updateIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        updateIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        updateIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(income.getId())};
        db.update(DbHelper.INCOME_TABLE_NAME, updateIncome, DbHelper.ID + "=?", args);
    }

    public void deleteIncome(IncomeBudgetDb income) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(income.getId())};
        db.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public Double sumTotalIncome() {
        List<Double> incomeList = new ArrayList<>(getIncomes().size());
        totalIncome = 0.0;
        for (IncomeBudgetDb i : getIncomes()) {
            incomeList.add(i.getIncomeAnnualAmount());
        }
        totalIncome = 0.0;
        if (incomeList.size() == 0) {
            totalIncome = 0.0;
        } else {
            for (Double dbl : incomeList) {
                totalIncome += dbl;
            }
        }
        return totalIncome;
    }

    public List<MoneyInDb> getMoneyIns() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        List<MoneyInDb> moneyIns = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MoneyInDb moneyIn = new MoneyInDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYINCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYINAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYINCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                moneyIns.add(0, moneyIn); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return moneyIns;
    }

    public void addMoneyIn(MoneyInDb moneyIn) {
        ContentValues newMoneyIn = new ContentValues();
        newMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        newMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        newMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_IN_TABLE_NAME, null, newMoneyIn);
    }

    public void updateMoneyIn(MoneyInDb moneyIn) {
        ContentValues updateMoneyIn = new ContentValues();
        updateMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        updateMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        updateMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyIn.getId())};
        db.update(DbHelper.MONEY_IN_TABLE_NAME, updateMoneyIn, DbHelper.ID + "=?", args);
    }

    public void deleteMoneyIn(MoneyInDb moneyIn) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyIn.getId())};
        db.delete(DbHelper.MONEY_IN_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<MoneyOutDb> getMoneyOuts() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
        List<MoneyOutDb> moneyOuts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MoneyOutDb moneyOut = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                moneyOuts.add(0, moneyOut); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
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
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_OUT_TABLE_NAME, null, newMoneyOut);
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
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyOut.getId())};
        db.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOut, DbHelper.ID + "=?", args);
    }

    public void deleteMoneyOut(MoneyOutDb moneyOut) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyOut.getId())};
        db.delete(DbHelper.MONEY_OUT_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<CurrentDb> getCurrent() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        List<CurrentDb> currents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CurrentDb current = new CurrentDb(
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTACCOUNTBALANCE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTAVAILABLEBALANCE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                currents.add(0, current); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return currents;
    }

    public void addCurrent(CurrentDb current) {
        ContentValues newCurrent = new ContentValues();
        newCurrent.put(DbHelper.CURRENTACCOUNTBALANCE, current.getCurrentAccountBalance());
        newCurrent.put(DbHelper.CURRENTAVAILABLEBALANCE, current.getCurrentAvailableBalance());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.CURRENT_TABLE_NAME, null, newCurrent);
    }

    public void updateCurrent(CurrentDb current) {
        ContentValues updateCurrent = new ContentValues();
        updateCurrent.put(DbHelper.CURRENTACCOUNTBALANCE, current.getCurrentAccountBalance());
        updateCurrent.put(DbHelper.CURRENTAVAILABLEBALANCE, current.getCurrentAvailableBalance());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.update(DbHelper.CURRENT_TABLE_NAME, updateCurrent, DbHelper.ID + "=?", args);
    }

    public void deleteCurrent(CurrentDb current) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.delete(DbHelper.CURRENT_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public Double retrieveCurrentAccountBalance() {
        List<Double> currentList = new ArrayList<>(getCurrent().size());
        for (CurrentDb c : getCurrent()) {
            currentList.add(c.getCurrentAccountBalance());
        }
        currentAccountBalance = 0.0;
        if (currentList.size() == 0) {
            currentAccountBalance = 0.0;
        } else {
            for (Double dbl : currentList) {
                currentAccountBalance += dbl;
            }
        }
        return currentAccountBalance;
    }

    public Double retrieveBPercentage() {
        List<Double> expenseList = new ArrayList<>(getExpense().size());
        for (ExpenseBudgetDb e : getExpense()) {
            if (e.getExpensePriority().equals("A")) {
                expenseList.add(e.getExpenseAAnnualAmount());
            }
        }
        totalBudgetAExpenses = 0.0;
        if (expenseList.size() == 0) {
            totalBudgetAExpenses = 0.0;
        } else {
            for (Double dbl : expenseList) {
                totalBudgetAExpenses += dbl;
            }
        }
        percentB = 1 - (totalBudgetAExpenses / sumTotalIncome());
        return percentB;
    }

    public Double retrieveCurrentAvailableBalance() {
        List<Double> currentList = new ArrayList<>(getCurrent().size());
        for (CurrentDb c : getCurrent()) {
            currentList.add(c.getCurrentAvailableBalance());
        }
        currentAvailableBalance = 0.0;
        if (currentList.size() == 0) {
            currentAvailableBalance = 0.0;
        } else {
            for (Double dbl : currentList) {
                currentAvailableBalance += dbl;
            }
        }
        return currentAvailableBalance;
    }

    public List<MoneyOutDb> getCashTrans() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'N'", null);
        List<MoneyOutDb> cashTrans = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MoneyOutDb cashTransList = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                cashTrans.add(0, cashTransList); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cashTrans;
    }

    public List<MoneyOutDb> getCCTrans() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'Y'", null);
        List<MoneyOutDb> ccTrans = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MoneyOutDb ccTransList = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                ccTrans.add(0, ccTransList); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return ccTrans;
    }

    public List<MoneyOutDb> getCCTransToPay() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " + DbHelper.MONEYOUTCC + " = 'Y' AND " + DbHelper.MONEYOUTPAID + " = '0'", null);
        List<MoneyOutDb> ccTransToPay = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MoneyOutDb ccTransToPayList = new MoneyOutDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTPAID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYMO)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                ccTransToPay.add(ccTransToPayList); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return ccTransToPay;
    }

    public Double retrieveToPayTotal() {
        List<Double> toPayList = new ArrayList<>();
        for (MoneyOutDb m : getMoneyOuts()) {
            if (m.getMoneyOutToPay() == 1 && m.getMoneyOutPaid() == 0) {
                toPayList.add(m.getMoneyOutAmount());
            }
        }
        totalCCPaymentDue = 0.0;
        if (toPayList.size() == 0) {
            totalCCPaymentDue = 0.0;
        } else {
            for (Double dbl : toPayList) {
                totalCCPaymentDue += dbl;
            }
        }
        return totalCCPaymentDue;
    }

    public Double retrieveToPayBTotal() {
        List<Double> toPayBList = new ArrayList<>();
        for (MoneyOutDb m : getMoneyOuts()) {
            if (m.getMoneyOutToPay() == 1 && m.getMoneyOutPaid() == 0 && m.getMoneyOutPriority().equals("B")) {
                toPayBList.add(m.getMoneyOutAmount());
            }
        }
        totalCCPaymentBDue = 0.0;
        if (toPayBList.size() == 0) {
            totalCCPaymentBDue = 0.0;
        } else {
            for (Double dbl : toPayBList) {
                totalCCPaymentBDue += dbl;
            }
        }
        return totalCCPaymentBDue;
    }

    public void updatePaid() {
        db = dbHelper.getWritableDatabase();
        ContentValues updateMoneyOutPaid = new ContentValues();
        updateMoneyOutPaid.put(DbHelper.MONEYOUTPAID, 1);
        db.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutPaid, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID + " = '0'", null);
    }
}
