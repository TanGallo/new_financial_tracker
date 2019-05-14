package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
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

public class DbManager extends AppCompatActivity {

    public Cursor cursor;
    public ContentValues newCurrent, newDebt, newExpense, newIncome, newMoneyIn, newMoneyOut, newSavings, newSetUp, updateCurrent, updateDebt, updateExpense,
            updateIncome, updateMoneyIn, updateMoneyOut, updateMoneyOutPaid, updateSaving, updateSetUp, zero;
    public DbHelper dbHelper;
    public Double currentAccountBalance = 0.0, currentB = 0.0, percentA = 0.0, percentB = 0.0, startingBalanceResult = 0.0, totalBudgetAExpenses = 0.0,
            totalCCPaymentDue = 0.0, totalCCPaymentBDue = 0.0, totalDebt = 0.0, totalExpenses = 0.0, totalIncome = 0.0, totalSavings = 0.0, currentA = 0.0,
            currentOwingA = 0.0, totalAPortion = 0.0, totalOwingPortion = 0.0, totalBPortion = 0.0;
    public General general = new General();
    public int balanceDoneCheck = 0, billsDoneCheck = 0, budgetDoneCheck = 0, currentPageId = 0, debtCount = 0, debtsDoneCheck = 0, earliestYear = 0,
            endIndex = 0, incomeDoneCheck = 0, latestYear = 0, savingsDoneCheck = 0, startIndex = 0, tourDoneCheck = 0;
    public Long debtId, expenseId, incomeId;
    public SQLiteDatabase db;
    public String category = null, startingString = null, subStringResult = null;

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
                        cursor.getInt(cursor.getColumnIndex(DbHelper.INCOMEDONE)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.BILLSDONE)),
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
        newSetUp = new ContentValues();
        newSetUp.put(DbHelper.INCOMEDONE, setUp.getIncomeDone());
        newSetUp.put(DbHelper.BILLSDONE, setUp.getBillsDone());
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
        updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.INCOMEDONE, setUp.getIncomeDone());
        updateSetUp.put(DbHelper.BILLSDONE, setUp.getBillsDone());
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
            try {
                tourDoneList.add(s.getTourDone());
            } catch(Exception e) {
                e.printStackTrace();
            }
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

    public int incomeSetUpCheck() {
        List<Integer> incomeDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            incomeDoneList.add(s.getIncomeDone());
        }
        incomeDoneCheck = 0;
        if (incomeDoneList.size() == 0) {
            incomeDoneCheck = 0;
        } else {
            incomeDoneCheck = Collections.max(incomeDoneList);
        }
        return incomeDoneCheck;
    }

    public int billsSetUpCheck() {
        List<Integer> billsDoneList = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            billsDoneList.add(s.getBillsDone());
        }
        billsDoneCheck = 0;
        if (billsDoneList.size() == 0) {
            billsDoneCheck = 0;
        } else {
            billsDoneCheck = Collections.max(billsDoneList);
        }
        return billsDoneCheck;
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
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTLIMIT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTAMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTANNUALINCOME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTEND)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTTOPAY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYD)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.INCREFKEYD)),
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
        newDebt = new ContentValues();
        newDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        newDebt.put(DbHelper.DEBTLIMIT, debt.getDebtLimit());
        newDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        newDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        newDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        newDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        newDebt.put(DbHelper.DEBTANNUALINCOME, debt.getDebtAnnualIncome());
        newDebt.put(DbHelper.DEBTEND, debt.getDebtEnd());
        newDebt.put(DbHelper.DEBTTOPAY, debt.getDebtToPay());
        newDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());
        newDebt.put(DbHelper.INCREFKEYD, debt.getIncRefKeyD());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.DEBTS_TABLE_NAME, null, newDebt);
    }

    public void updateDebt(DebtDb debt) {
        updateDebt = new ContentValues();
        updateDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        updateDebt.put(DbHelper.DEBTLIMIT, debt.getDebtLimit());
        updateDebt.put(DbHelper.DEBTAMOUNT, debt.getDebtAmount());
        updateDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        updateDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        updateDebt.put(DbHelper.DEBTFREQUENCY, debt.getDebtFrequency());
        updateDebt.put(DbHelper.DEBTANNUALINCOME, debt.getDebtAnnualIncome());
        updateDebt.put(DbHelper.DEBTEND, debt.getDebtEnd());
        updateDebt.put(DbHelper.DEBTTOPAY, debt.getDebtToPay());
        updateDebt.put(DbHelper.EXPREFKEYD, debt.getExpRefKeyD());
        updateDebt.put(DbHelper.INCREFKEYD, debt.getIncRefKeyD());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.update(DbHelper.DEBTS_TABLE_NAME, updateDebt, DbHelper.ID + "=?", args);
    }

    public void deleteDebt(DebtDb debt) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.delete(DbHelper.DEBTS_TABLE_NAME, DbHelper.ID + "=?", args);
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

    public int getDebtCount() {
        debtCount = getDebts().size();
        return debtCount;
    }

    public Long findLatestDebtId() {
        List<Long> debtIds = new ArrayList<>(getDebts().size());
        for (DebtDb d2 : getDebts()) {
            debtIds.add(d2.getId());
        }
        debtId = null;
        if (debtIds.size() == 0) {
            debtId = null;
        } else {
            debtId = Collections.max(debtIds);
        }
        return debtId;
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
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSGOAL)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSFREQUENCY)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSANNUALINCOME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSDATE)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYS)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.INCREFKEYS)),
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
        newSavings = new ContentValues();
        newSavings.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        newSavings.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        newSavings.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        newSavings.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        newSavings.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        newSavings.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        newSavings.put(DbHelper.SAVINGSANNUALINCOME, saving.getSavingsAnnualIncome());
        newSavings.put(DbHelper.SAVINGSDATE, saving.getSavingsDate());
        newSavings.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());
        newSavings.put(DbHelper.INCREFKEYS, saving.getIncRefKeyS());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SAVINGS_TABLE_NAME, null, newSavings);
    }

    public void updateSavings(SavingsDb saving) {
        updateSaving = new ContentValues();
        updateSaving.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        updateSaving.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        updateSaving.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        updateSaving.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        updateSaving.put(DbHelper.SAVINGSFREQUENCY, saving.getSavingsFrequency());
        updateSaving.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        updateSaving.put(DbHelper.SAVINGSANNUALINCOME, saving.getSavingsAnnualIncome());
        updateSaving.put(DbHelper.SAVINGSDATE, saving.getSavingsDate());
        updateSaving.put(DbHelper.EXPREFKEYS, saving.getExpRefKeyS());
        updateSaving.put(DbHelper.INCREFKEYS, saving.getIncRefKeyS());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(saving.getId())};
        db.update(DbHelper.SAVINGS_TABLE_NAME, updateSaving, DbHelper.ID + "=?", args);
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
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSEANNUALAMOUNT + " DESC", null);
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
        newExpense = new ContentValues();
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
        updateExpense = new ContentValues();
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

    public Long findLatestExpenseId() {
        List<Long> expenseIds = new ArrayList<>(getExpense().size());
        for (ExpenseBudgetDb e : getExpense()) {
            expenseIds.add(e.getId());
        }
        expenseId = null;
        if (expenseIds.size() == 0) {
            expenseId = null;
        } else {
            expenseId = Collections.max(expenseIds);
        }
        return expenseId;
    }

    public List<ExpenseBudgetDb> getWeeklyLimits() {
        List<ExpenseBudgetDb> weeklyLimits = new ArrayList<>();
        for (ExpenseBudgetDb e : getExpense()) {
            if (e.getExpenseWeekly().equals("Y")) {
                weeklyLimits.add(e);
                expenseId = e.getId();
            }
        }
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
        newIncome = new ContentValues();
        newIncome.put(DbHelper.INCOMENAME, income.getIncomeName());
        newIncome.put(DbHelper.INCOMEAMOUNT, income.getIncomeAmount());
        newIncome.put(DbHelper.INCOMEFREQUENCY, income.getIncomeFrequency());
        newIncome.put(DbHelper.INCOMEANNUALAMOUNT, income.getIncomeAnnualAmount());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.INCOME_TABLE_NAME, null, newIncome);
    }

    public void updateIncome(IncomeBudgetDb income) {
        updateIncome = new ContentValues();
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

    public Long findLatestIncomeId() {
        List<Long> incomeIds = new ArrayList<>(getIncomes().size());
        for (IncomeBudgetDb i : getIncomes()) {
            incomeIds.add(i.getId());
        }
        incomeId = null;
        if (incomeIds.size() == 0) {
            incomeId = null;
        } else {
            incomeId = Collections.max(incomeIds);
        }
        return incomeId;
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
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYINA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYINOWING)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYINB)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.MONEYINCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.INCREFKEYMI)),
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
        newMoneyIn = new ContentValues();
        newMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        newMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        newMoneyIn.put(DbHelper.MONEYINA, moneyIn.getMoneyInA());
        newMoneyIn.put(DbHelper.MONEYINOWING, moneyIn.getMoneyInOwing());
        newMoneyIn.put(DbHelper.MONEYINB, moneyIn.getMoneyInB());
        newMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());
        newMoneyIn.put(DbHelper.INCREFKEYMI, moneyIn.getIncRefKeyMI());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_IN_TABLE_NAME, null, newMoneyIn);
    }

    public void updateMoneyIn(MoneyInDb moneyIn) {
        updateMoneyIn = new ContentValues();
        updateMoneyIn.put(DbHelper.MONEYINCAT, moneyIn.getMoneyInCat());
        updateMoneyIn.put(DbHelper.MONEYINAMOUNT, moneyIn.getMoneyInAmount());
        updateMoneyIn.put(DbHelper.MONEYINA, moneyIn.getMoneyInA());
        updateMoneyIn.put(DbHelper.MONEYINOWING, moneyIn.getMoneyInOwing());
        updateMoneyIn.put(DbHelper.MONEYINB, moneyIn.getMoneyInB());
        updateMoneyIn.put(DbHelper.MONEYINCREATEDON, moneyIn.getMoneyInCreatedOn());
        updateMoneyIn.put(DbHelper.INCREFKEYMI, moneyIn.getIncRefKeyMI());
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
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    MoneyOutDb moneyOut = new MoneyOutDb(
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCAT)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTPRIORITY)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTWEEKLY)),
                            cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTAMOUNT)),
                            cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTA)),
                            cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTOWING)),
                            cursor.getDouble(cursor.getColumnIndex(DbHelper.MONEYOUTB)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCREATEDON)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTCC)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.MONEYOUTDEBTCAT)),
                            cursor.getLong(cursor.getColumnIndex(DbHelper.MONEYOUTCHARGINGDEBTID)),
                            cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTTOPAY)),
                            cursor.getInt(cursor.getColumnIndex(DbHelper.MONEYOUTPAID)),
                            cursor.getLong(cursor.getColumnIndex(DbHelper.EXPREFKEYMO)),
                            cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                    );
                    moneyOuts.add(0, moneyOut); //adds new items to beginning of list
                    cursor.moveToNext();
                }
            }
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
        cursor.close();
        return moneyOuts;
    }

    public void addMoneyOut(MoneyOutDb moneyOut) {
        newMoneyOut = new ContentValues();
        newMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        newMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        newMoneyOut.put(DbHelper.MONEYOUTWEEKLY, moneyOut.getMoneyOutWeekly());
        newMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        newMoneyOut.put(DbHelper.MONEYOUTA, moneyOut.getMoneyOutA());
        newMoneyOut.put(DbHelper.MONEYOUTOWING, moneyOut.getMoneyOutOwing());
        newMoneyOut.put(DbHelper.MONEYOUTB, moneyOut.getMoneyOutB());
        newMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        newMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());
        newMoneyOut.put(DbHelper.MONEYOUTDEBTCAT, moneyOut.getMoneyOutDebtCat());
        newMoneyOut.put(DbHelper.MONEYOUTCHARGINGDEBTID, moneyOut.getMoneyOutChargingDebtId());
        newMoneyOut.put(DbHelper.MONEYOUTTOPAY, moneyOut.getMoneyOutToPay());
        newMoneyOut.put(DbHelper.MONEYOUTPAID, moneyOut.getMoneyOutPaid());
        newMoneyOut.put(DbHelper.EXPREFKEYMO, moneyOut.getExpRefKeyMO());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.MONEY_OUT_TABLE_NAME, null, newMoneyOut);
    }

    public void updateMoneyOut(MoneyOutDb moneyOut) {
        updateMoneyOut = new ContentValues();
        updateMoneyOut.put(DbHelper.MONEYOUTCAT, moneyOut.getMoneyOutCat());
        updateMoneyOut.put(DbHelper.MONEYOUTPRIORITY, moneyOut.getMoneyOutPriority());
        updateMoneyOut.put(DbHelper.MONEYOUTWEEKLY, moneyOut.getMoneyOutWeekly());
        updateMoneyOut.put(DbHelper.MONEYOUTAMOUNT, moneyOut.getMoneyOutAmount());
        updateMoneyOut.put(DbHelper.MONEYOUTA, moneyOut.getMoneyOutA());
        updateMoneyOut.put(DbHelper.MONEYOUTOWING, moneyOut.getMoneyOutOwing());
        updateMoneyOut.put(DbHelper.MONEYOUTB, moneyOut.getMoneyOutB());
        updateMoneyOut.put(DbHelper.MONEYOUTCREATEDON, moneyOut.getMoneyOutCreatedOn());
        updateMoneyOut.put(DbHelper.MONEYOUTCC, moneyOut.getMoneyOutCC());
        updateMoneyOut.put(DbHelper.MONEYOUTDEBTCAT, moneyOut.getMoneyOutDebtCat());
        updateMoneyOut.put(DbHelper.MONEYOUTCHARGINGDEBTID, moneyOut.getMoneyOutChargingDebtId());
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

    public List<String> getYearsList() {
        List<String> yearsList = new ArrayList<>();

        try {
            List<String> allYears = new ArrayList<>(getMoneyOuts().size());
            for (MoneyOutDb m : getMoneyOuts()) {
                allYears.add(m.getMoneyOutCreatedOn());
            }
            for(MoneyInDb m2 : getMoneyIns()) {
                allYears.add(m2.getMoneyInCreatedOn());
            }
            List<Date> allDates = new ArrayList<>(allYears.size());
            general.extractingDates(allYears, allDates);

            for (Date d : allDates) {
                startingString = d.toString();
                startIndex = startingString.length() - 4;
                endIndex = startingString.length();
                subStringResult = startingString.substring(startIndex, endIndex);
                yearsList.add(subStringResult);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return yearsList;
    }

    public Integer getEarliestEntry() {
        List<Integer> years = new ArrayList<>(getYearsList().size());
        for (String s : getYearsList()) {
            years.add(Integer.valueOf(s));
        }
        earliestYear = 0;
        if (years.size() == 0) {
            earliestYear = 0;
        } else {
            earliestYear = Collections.min(years);
        }
        return earliestYear;
    }

    public Integer getLatestEntry() {
        List<Integer> years2 = new ArrayList<>(getYearsList().size());
        for (String s : getYearsList()) {
            years2.add(Integer.valueOf(s));
        }
        latestYear = 0;
        if (years2.size() == 0) {
            latestYear = 0;
        } else {
            latestYear = Collections.max(years2);
        }
        return latestYear;
    }

    public List<CurrentDb> getCurrent() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        List<CurrentDb> currents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CurrentDb current = new CurrentDb(
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTACCOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTB)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTOWINGA)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.CURRENTPAGEID)),
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
        newCurrent = new ContentValues();
        newCurrent.put(DbHelper.CURRENTACCOUNT, current.getCurrentAccount());
        newCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        newCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        newCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        newCurrent.put(DbHelper.CURRENTPAGEID, current.getCurrentPageId());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.CURRENT_TABLE_NAME, null, newCurrent);
    }

    public void updateCurrent(CurrentDb current) {
        updateCurrent = new ContentValues();
        updateCurrent.put(DbHelper.CURRENTACCOUNT, current.getCurrentAccount());
        updateCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        updateCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        updateCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        updateCurrent.put(DbHelper.CURRENTPAGEID, current.getCurrentPageId());
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
        currentAccountBalance = 0.0;
        for (CurrentDb c : getCurrent()) {
            if (c.getId() == 1) {
                currentAccountBalance = c.getCurrentAccount();
            }
        }
        return currentAccountBalance;
    }

    public Double retrieveAPercentage() {
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
        percentA = totalBudgetAExpenses / sumTotalIncome();
        return percentA;
    }

    public Double retrieveBPercentage() {
        retrieveAPercentage();
        percentB = 1 - percentA;

        return percentB;
    }

    public Double retrieveCurrentA() {
        currentA = 0.0;
        for (CurrentDb c3 : getCurrent()) {
            if (c3.getId() == 1) {
                currentA = c3.getCurrentA();
            }
        }
        return currentA;
    }

    public Double retrieveCurrentB() {
        currentB = 0.0;
        for (CurrentDb c2 : getCurrent()) {
            if (c2.getId() == 1) {
                currentB = c2.getCurrentB();
            }
        }
        return currentB;
    }

    public Double retrieveCurrentOwingA() {
        currentOwingA = 0.0;
        for (CurrentDb c4 : getCurrent()) {
            if (c4.getId() == 1) {
                currentOwingA = c4.getCurrentOwingA();
            }
        }
        return currentOwingA;
    }

    public int retrieveCurrentPageId() {
        currentPageId = 0;
        for (CurrentDb c3 : getCurrent()) {
            if (c3.getId() == 1) {
                currentPageId = c3.getCurrentPageId();
            }
        }
        return currentPageId;
    }

    public List<MoneyOutDb> getCashTrans() {
        List<MoneyOutDb> cashTrans = new ArrayList<>();
        for (MoneyOutDb m2 : getMoneyOuts()) {
            if (m2.getMoneyOutChargingDebtId() == 0) {
                cashTrans.add(m2);
            }
        }
        return cashTrans;
    }

    public List<MoneyOutDb> getCCTrans() {
        List<MoneyOutDb> ccTrans = new ArrayList<>();
        for (MoneyOutDb m3 : getMoneyOuts()) {
            if (m3.getMoneyOutChargingDebtId() > 0) {
                ccTrans.add(m3);
            }
        }
        return ccTrans;
    }

    public List<MoneyOutDb> getCCTransToPay() {
        List<MoneyOutDb> ccTransToPay = new ArrayList<>();
        for (MoneyOutDb m4 : getMoneyOuts()) {
            if (m4.getMoneyOutCC().equals("Y") && m4.getMoneyOutPaid() == 0) {
                ccTransToPay.add(m4);
            }
        }
        return ccTransToPay;
    }

    public Double retrieveToPayTotal() {
        List<Double> toPayList = new ArrayList<>();
        for (MoneyOutDb m5 : getMoneyOuts()) {
            if (m5.getMoneyOutToPay() == 1 && m5.getMoneyOutPaid() == 0) {
                toPayList.add(m5.getMoneyOutAmount());
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
        for (MoneyOutDb m6 : getMoneyOuts()) {
            if (m6.getMoneyOutToPay() == 1 && m6.getMoneyOutPaid() == 0 && m6.getMoneyOutPriority().equals("B")) {
                toPayBList.add(m6.getMoneyOutAmount());
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

    public Double retrieveAPortion() {
        List<Double> aPortion = new ArrayList<>();
        for (MoneyOutDb m7 : getMoneyOuts()) {
            if(m7.getMoneyOutToPay() == 1 && m7.getMoneyOutPaid() == 0) {
                aPortion.add(m7.getMoneyOutA());
            }
        }
        totalAPortion = 0.0;
        if (aPortion.size() == 0) {
            totalAPortion = 0.0;
        } else {
            for (Double dbl : aPortion) {
                totalAPortion += dbl;
            }
        }
        return totalAPortion;
    }

    public Double retrieveOwingPortion() {
        List<Double> owingPortion = new ArrayList<>();
        for (MoneyOutDb m8 : getMoneyOuts()) {
            if(m8.getMoneyOutToPay() == 1 && m8.getMoneyOutPaid() == 0) {
                owingPortion.add(m8.getMoneyOutOwing());
            }
        }
        totalOwingPortion = 0.0;
        if (owingPortion.size() == 0) {
            totalOwingPortion = 0.0;
        } else {
            for (Double dbl : owingPortion) {
                totalOwingPortion += dbl;
            }
        }
        return totalOwingPortion;
    }

    public Double retrieveBPortion() {
        List<Double> bPortion = new ArrayList<>();
        for (MoneyOutDb m9 : getMoneyOuts()) {
            if(m9.getMoneyOutToPay() == 1 && m9.getMoneyOutPaid() == 0) {
                bPortion.add(m9.getMoneyOutB());
            }
        }
        totalBPortion = 0.0;
        if (bPortion.size() == 0) {
            totalBPortion = 0.0;
        } else {
            for (Double dbl : bPortion) {
                totalBPortion += dbl;
            }
        }
        return totalBPortion;
    }

    public void updatePaid() {
        db = dbHelper.getWritableDatabase();
        updateMoneyOutPaid = new ContentValues();
        updateMoneyOutPaid.put(DbHelper.MONEYOUTPAID, 1);
        db.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutPaid, DbHelper.MONEYOUTTOPAY + "= '1' AND " + DbHelper.MONEYOUTPAID + " = '0'", null);
    }
}
