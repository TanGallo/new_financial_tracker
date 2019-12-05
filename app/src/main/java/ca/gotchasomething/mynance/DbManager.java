package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.CurrentDb;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class DbManager extends AppCompatActivity {

    public Cursor cursor;
    public ContentValues CV, cvDebtToPay, cvToPay, newAccounts, newCurrent, newBudget, newIncome, newMoneyIn, newMoneyOut, newSetUp, newTransactions,
            updateAccounts, updateCurrent, updateBudget, updateIncome, updateMoneyIn, updateMoneyOut, updateMoneyOutPaid, updateSetUp, updateTransactions;
    public Date earliestDateInRange, latestDateInRange, newDate;
    public DbHelper dbHelper;
    public Double acctDebtToPayFromDb = 0.0, amtOfMissingEntries = 0.0, amtOfMissingEntriesE = 0.0, amtMissing = 0.0, amtToZero = 0.0, currentAccountBalance = 0.0, currentB = 0.0,
            currentAcctAmt = 0.0, currentDebtAmtOwing = 0.0, currentSavAmt = 0.0, annPaytFromDb = 0.0, expAmtFromDb = 0.0, expFrqFromDb = 0.0, expThisYear = 0.0,
            incAmtFromDb = 0.0, incAnnAmtFromDb = 0.0, incFrqFromDb = 0.0, moneyInA = 0.0, moneyInB = 0.0, moneyInOwing = 0.0, moneyOutA = 0.0,
            moneyOutB = 0.0, moneyOutOwing = 0.0, newABal = 0.0, newAcctBal = 0.0, newAvailBal2 = 0.0, newBBal = 0.0, newAmt = 0.0, newExpAnnAmt = 0.0,
            newIncAnnAmt = 0.0, newMoneyA = 0.0, newMoneyB = 0.0, newMoneyOwing = 0.0, newOwingBal = 0.0, incThisYear = 0.0, numberOfMissingEntries = 0.0,
            numberOfMissingEntriesE = 0.0, selectedTransactionSum = 0.0, spentThisWeekInCat = 0.0, totalAExpenses = 0.0, totalBExpenses = 0.0, totalCCPaymentDue = 0.0, totalDebt = 0.0, totalExpenses = 0.0, totalIncome = 0.0,
            totalSavings = 0.0, totalSavGoal = 0.0, currentA = 0.0, currentOwingA = 0.0, totalAPortion = 0.0, totalOwingPortion = 0.0, totalBPortion = 0.0,
            transFromThisYear = 0.0, transToThisYear = 0.0;
    public General general = new General();
    public int lastPageId = 0, earliestYear = 0, endIndex = 0, latestYear = 0, numberOfEntries = 0, numberOfEntriesE = 0, startIndex = 0, thisYear = 0;
    public List<Date> allDates;
    public Long expenseId;
    public NumberFormat percentFormat = NumberFormat.getPercentInstance();
    public SimpleDateFormat sdf;
    public SQLiteDatabase db;
    public String category = null, date = null, expPriorityFromDb = null, lastDate = null, moneyInDebtSav = null, moneyInIsSav = null, newMonth = null,
            newMonth2 = null,
            spendResStmt = null, spendPercent = null, startingString = null, subStringResult = null, latestDone = null, text = null, transDate = null;

    public DbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    //GET, ADD, UPDATE, & DELETE FORMULAS

    public List<TransactionsDb> getTransactions() {
        List<TransactionsDb> transactionsList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.TRANSACTIONS_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                TransactionsDb transactionsDb = new TransactionsDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTYPE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSISCC)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTCAT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSBDGTID)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTINA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTINOWING)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTINB)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTOUTA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTOUTOWING)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.TRANSAMTOUTB)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSTOACCTID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOACCTNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTODEBTSAV)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSFROMACCTID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMACCTNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMDEBTSAV)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTWEEKLY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCTOPAY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCPAID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                transactionsList.add(0, transactionsDb); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return transactionsList;
    }

    public void addTransactions(TransactionsDb transactionsDb) {
        newTransactions = new ContentValues();
        newTransactions.put(DbHelper.TRANSTYPE, transactionsDb.getTransType());
        newTransactions.put(DbHelper.TRANSISCC, transactionsDb.getTransIsCC());
        newTransactions.put(DbHelper.TRANSBDGTCAT, transactionsDb.getTransBdgtCat());
        newTransactions.put(DbHelper.TRANSBDGTID, transactionsDb.getTransBdgtId());
        newTransactions.put(DbHelper.TRANSAMT, transactionsDb.getTransAmt());
        newTransactions.put(DbHelper.TRANSAMTINA, transactionsDb.getTransAmtInA());
        newTransactions.put(DbHelper.TRANSAMTINOWING, transactionsDb.getTransAmtInOwing());
        newTransactions.put(DbHelper.TRANSAMTINB, transactionsDb.getTransAmtInB());
        newTransactions.put(DbHelper.TRANSAMTOUTA, transactionsDb.getTransAmtOutA());
        newTransactions.put(DbHelper.TRANSAMTOUTOWING, transactionsDb.getTransAmtOutOwing());
        newTransactions.put(DbHelper.TRANSAMTOUTB, transactionsDb.getTransAmtOutB());
        newTransactions.put(DbHelper.TRANSTOACCTID, transactionsDb.getTransToAcctId());
        newTransactions.put(DbHelper.TRANSTOACCTNAME, transactionsDb.getTransToAcctName());
        newTransactions.put(DbHelper.TRANSTODEBTSAV, transactionsDb.getTransToDebtSav());
        newTransactions.put(DbHelper.TRANSFROMACCTID, transactionsDb.getTransFromAcctId());
        newTransactions.put(DbHelper.TRANSFROMACCTNAME, transactionsDb.getTransFromAcctName());
        newTransactions.put(DbHelper.TRANSFROMDEBTSAV, transactionsDb.getTransFromDebtSav());
        newTransactions.put(DbHelper.TRANSBDGTPRIORITY, transactionsDb.getTransBdgtPriority());
        newTransactions.put(DbHelper.TRANSBDGTWEEKLY, transactionsDb.getTransBdgtWeekly());
        newTransactions.put(DbHelper.TRANSCCTOPAY, transactionsDb.getTransCCToPay());
        newTransactions.put(DbHelper.TRANSCCPAID, transactionsDb.getTransCCPaid());
        newTransactions.put(DbHelper.TRANSCREATEDON, transactionsDb.getTransCreatedOn());

        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.TRANSACTIONS_TABLE_NAME, null, newTransactions);
        db.close();
    }

    public void updateTransactions(TransactionsDb transactionsDb) {
        updateTransactions = new ContentValues();
        updateTransactions.put(DbHelper.TRANSTYPE, transactionsDb.getTransType());
        updateTransactions.put(DbHelper.TRANSISCC, transactionsDb.getTransIsCC());
        updateTransactions.put(DbHelper.TRANSBDGTCAT, transactionsDb.getTransBdgtCat());
        updateTransactions.put(DbHelper.TRANSBDGTID, transactionsDb.getTransBdgtId());
        updateTransactions.put(DbHelper.TRANSAMT, transactionsDb.getTransAmt());
        updateTransactions.put(DbHelper.TRANSAMTINA, transactionsDb.getTransAmtInA());
        updateTransactions.put(DbHelper.TRANSAMTINOWING, transactionsDb.getTransAmtInOwing());
        updateTransactions.put(DbHelper.TRANSAMTINB, transactionsDb.getTransAmtInB());
        updateTransactions.put(DbHelper.TRANSAMTOUTA, transactionsDb.getTransAmtOutA());
        updateTransactions.put(DbHelper.TRANSAMTOUTOWING, transactionsDb.getTransAmtOutOwing());
        updateTransactions.put(DbHelper.TRANSAMTOUTB, transactionsDb.getTransAmtOutB());
        updateTransactions.put(DbHelper.TRANSTOACCTID, transactionsDb.getTransToAcctId());
        updateTransactions.put(DbHelper.TRANSTOACCTNAME, transactionsDb.getTransToAcctName());
        updateTransactions.put(DbHelper.TRANSTODEBTSAV, transactionsDb.getTransToDebtSav());
        updateTransactions.put(DbHelper.TRANSFROMACCTID, transactionsDb.getTransFromAcctId());
        updateTransactions.put(DbHelper.TRANSFROMACCTNAME, transactionsDb.getTransFromAcctName());
        updateTransactions.put(DbHelper.TRANSFROMDEBTSAV, transactionsDb.getTransFromDebtSav());
        updateTransactions.put(DbHelper.TRANSBDGTPRIORITY, transactionsDb.getTransBdgtPriority());
        updateTransactions.put(DbHelper.TRANSBDGTWEEKLY, transactionsDb.getTransBdgtWeekly());
        updateTransactions.put(DbHelper.TRANSCCTOPAY, transactionsDb.getTransCCToPay());
        updateTransactions.put(DbHelper.TRANSCCPAID, transactionsDb.getTransCCPaid());
        updateTransactions.put(DbHelper.TRANSCREATEDON, transactionsDb.getTransCreatedOn());

        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(transactionsDb.getId())};
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, updateTransactions, DbHelper.ID + "=?", args);
        db.close();
    }

    public void deleteTransactions(TransactionsDb transactionsDb) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(transactionsDb.getId())};
        db.delete(DbHelper.TRANSACTIONS_TABLE_NAME, DbHelper.ID + "=?", args);
        db.close();
    }

    public List<AccountsDb> getAccounts() {
        List<AccountsDb> accountsList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                AccountsDb acctsDb = new AccountsDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.ACCTNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTBAL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.ACCTDEBTSAV)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTMAX)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTINTRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTPAYTSTO)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTANNPAYTSTO)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.ACCTENDDATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.ACCTDEBTTOPAY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                accountsList.add(acctsDb); //adds new items to end of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return accountsList;
    }

    public void addAccounts(AccountsDb acctsDb) {
        newAccounts = new ContentValues();
        newAccounts.put(DbHelper.ACCTNAME, acctsDb.getAcctName());
        newAccounts.put(DbHelper.ACCTBAL, acctsDb.getAcctBal());
        newAccounts.put(DbHelper.ACCTDEBTSAV, acctsDb.getAcctDebtSav());
        newAccounts.put(DbHelper.ACCTMAX, acctsDb.getAcctMax());
        newAccounts.put(DbHelper.ACCTINTRATE, acctsDb.getAcctIntRate());
        newAccounts.put(DbHelper.ACCTPAYTSTO, acctsDb.getAcctPaytsTo());
        newAccounts.put(DbHelper.ACCTANNPAYTSTO, acctsDb.getAcctAnnPaytsTo());
        newAccounts.put(DbHelper.ACCTENDDATE, acctsDb.getAcctEndDate());
        newAccounts.put(DbHelper.ACCTDEBTTOPAY, acctsDb.getAcctDebtToPay());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.ACCOUNTS_TABLE_NAME, null, newAccounts);
        db.close();
    }

    public void updateAccounts(AccountsDb acctsDb) {
        updateAccounts = new ContentValues();
        updateAccounts.put(DbHelper.ACCTNAME, acctsDb.getAcctName());
        updateAccounts.put(DbHelper.ACCTBAL, acctsDb.getAcctBal());
        updateAccounts.put(DbHelper.ACCTDEBTSAV, acctsDb.getAcctDebtSav());
        updateAccounts.put(DbHelper.ACCTMAX, acctsDb.getAcctMax());
        updateAccounts.put(DbHelper.ACCTINTRATE, acctsDb.getAcctIntRate());
        updateAccounts.put(DbHelper.ACCTPAYTSTO, acctsDb.getAcctPaytsTo());
        updateAccounts.put(DbHelper.ACCTANNPAYTSTO, acctsDb.getAcctAnnPaytsTo());
        updateAccounts.put(DbHelper.ACCTENDDATE, acctsDb.getAcctEndDate());
        updateAccounts.put(DbHelper.ACCTDEBTTOPAY, acctsDb.getAcctDebtToPay());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(acctsDb.getId())};
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, updateAccounts, DbHelper.ID + "=?", args);
        db.close();
    }

    public void deleteAccounts(AccountsDb acctsDb) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(acctsDb.getId())};
        db.delete(DbHelper.ACCOUNTS_TABLE_NAME, DbHelper.ID + "=?", args);
        db.close();
    }

    public List<SetUpDb> getSetUp() {
        List<SetUpDb> setUp = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SetUpDb setUps = new SetUpDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.LATESTDONE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BALANCEAMOUNT)),
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
        newSetUp.put(DbHelper.LATESTDONE, setUp.getLatestDone());
        newSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SET_UP_TABLE_NAME, null, newSetUp);
        db.close();
    }

    public void updateSetUp(SetUpDb setUp) {
        updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.LATESTDONE, setUp.getLatestDone());
        updateSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(setUp.getId())};
        db.update(DbHelper.SET_UP_TABLE_NAME, updateSetUp, DbHelper.ID + "=?", args);
        db.close();
    }

    public void deleteSetUp(SetUpDb setUp) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(setUp.getId())};
        db.delete(DbHelper.SET_UP_TABLE_NAME, DbHelper.ID + "=?", args);
        db.close();
    }

    public List<BudgetDb> getBudget() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.BUDGET_TABLE_NAME + " ORDER BY " + DbHelper.BDGTANNPAYT + " DESC", null);
        List<BudgetDb> budgetList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                BudgetDb budget = new BudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTEXPINC)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTFRQ)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTANNPAYT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTWEEKLY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                budgetList.add(budget); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return budgetList;
    }

    /*public List<BudgetDb> getExpenses() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.BUDGET_TABLE_NAME + " WHERE " + DbHelper.BDGTEXPINC + " = 'E' " + " ORDER BY " + DbHelper.BDGTANNPAYT + " DESC", null);
        List<BudgetDb> expenseList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                BudgetDb expenses = new BudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTEXPINC)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTFRQ)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTANNPAYT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTWEEKLY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                expenseList.add(expenses); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return expenseList;
    }*/

    public void addBudget(BudgetDb budget) {
        newBudget = new ContentValues();
        newBudget.put(DbHelper.BDGTCAT, budget.getBdgtCat());
        newBudget.put(DbHelper.BDGTPAYTAMT, budget.getBdgtPaytAmt());
        newBudget.put(DbHelper.BDGTEXPINC, budget.getBdgtExpInc());
        newBudget.put(DbHelper.BDGTPAYTFRQ, budget.getBdgtPaytFrq());
        newBudget.put(DbHelper.BDGTANNPAYT, budget.getBdgtAnnPayt());
        newBudget.put(DbHelper.BDGTPRIORITY, budget.getBdgtPriority());
        newBudget.put(DbHelper.BDGTWEEKLY, budget.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        db.insertOrThrow(DbHelper.BUDGET_TABLE_NAME, null, newBudget);
        db.close();
    }

    public void updateBudget(BudgetDb budget) {
        updateBudget = new ContentValues();
        updateBudget.put(DbHelper.BDGTCAT, budget.getBdgtCat());
        updateBudget.put(DbHelper.BDGTPAYTAMT, budget.getBdgtPaytAmt());
        updateBudget.put(DbHelper.BDGTEXPINC, budget.getBdgtExpInc());
        updateBudget.put(DbHelper.BDGTPAYTFRQ, budget.getBdgtPaytFrq());
        updateBudget.put(DbHelper.BDGTANNPAYT, budget.getBdgtAnnPayt());
        updateBudget.put(DbHelper.BDGTPRIORITY, budget.getBdgtPriority());
        updateBudget.put(DbHelper.BDGTWEEKLY, budget.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(budget.getId())};
        db.update(DbHelper.BUDGET_TABLE_NAME, updateBudget, DbHelper.ID + "=?", args);
        db.close();
    }

    public void deleteBudget(BudgetDb budget) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(budget.getId())};
        db.delete(DbHelper.BUDGET_TABLE_NAME, DbHelper.ID + "=?", args);
        db.close();
    }

    public List<CurrentDb> getCurrent() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        List<CurrentDb> currents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CurrentDb current = new CurrentDb(
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTOWINGA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTB)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.LASTPAGEID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.LASTDATE)),
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
        newCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        newCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        newCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        newCurrent.put(DbHelper.LASTPAGEID, current.getLastPageId());
        newCurrent.put(DbHelper.LASTDATE, current.getLastDate());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.CURRENT_TABLE_NAME, null, newCurrent);
        db.close();
    }

    public void updateCurrent(CurrentDb current) {
        updateCurrent = new ContentValues();
        updateCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        updateCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        updateCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        updateCurrent.put(DbHelper.LASTPAGEID, current.getLastPageId());
        updateCurrent.put(DbHelper.LASTDATE, current.getLastDate());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.update(DbHelper.CURRENT_TABLE_NAME, updateCurrent, DbHelper.ID + "=?", args);
        db.close();
    }

    public void deleteCurrent(CurrentDb current) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.delete(DbHelper.CURRENT_TABLE_NAME, DbHelper.ID + "=?", args);
        db.close();
    }


    //LIST RETRIEVAL FORMULAS

    public List<BudgetDb> getExpenses() {
        List<BudgetDb> expenses = new ArrayList<>();
        for (BudgetDb b : getBudget()) {
            if (b.getBdgtExpInc().equals("E")) {
                expenses.add(b);
            }
        }
        return expenses;
    }

    public List<BudgetDb> getIncomes() {
        List<BudgetDb> incomes = new ArrayList<>();
        for (BudgetDb b : getBudget()) {
            if (b.getBdgtExpInc().equals("I")) {
                incomes.add(b);
            }
        }
        return incomes;
    }

    public List<TransactionsDb> getMoneyOuts() {
        List<TransactionsDb> moneyOuts = new ArrayList<>();
        for (TransactionsDb t : getTransactions()) {
            if (t.getTransType().equals("out")) {
                moneyOuts.add(t);
            }
        }
        return moneyOuts;
    }

    public List<TransactionsDb> getMoneyIns() {
        List<TransactionsDb> moneyIns = new ArrayList<>();
        for (TransactionsDb t : getTransactions()) {
            if (t.getTransType().equals("in")) {
                moneyIns.add(t);
            }
        }
        return moneyIns;
    }

    public List<TransactionsDb> getSelectedTransactions(String str1, String str2, String str3, String str4) {
        //str1 = type of transaction ("in", "out", "transfer", "ccPayment")
        //str2 = fromMonth, fromYear, toMonth, or toYear
        //str3 = getString(R.string.year_to_date)
        //str4 = selectedFromYear or selectedToYear
        List<TransactionsDb> selectedTrans = new ArrayList<>();
        for (TransactionsDb t : getTransactions()) {
            if (t.getTransType().equals(str1)) {
                transDate = t.getTransCreatedOn();
                thisYear = Calendar.getInstance().get(Calendar.YEAR);
                if (str2.equals(str3)) { //IF TO MONTH SPIN SELECTION IS "YEAR TO DATE"
                    if (transDate.contains(String.valueOf(thisYear))) {
                        selectedTrans.add(t);
                    }
                } else {
                    startIndex = transDate.indexOf("-") + 1;
                    endIndex = transDate.length() - 5;
                    newMonth = transDate.substring(startIndex, endIndex);
                    try {
                        newMonth2 = newMonth.replace(".", "");
                    } catch (Exception e) {
                        newMonth2 = newMonth;
                    }
                    if (str2.contains(newMonth2) && transDate.contains(str4)) {
                        selectedTrans.add(t);
                    }
                }
            }
        }
        return selectedTrans;
    }

    public List<TransactionsDb> getTransfers() {
        List<TransactionsDb> transfers = new ArrayList<>();
        for (TransactionsDb t : getTransactions()) {
            if (t.getTransType().equals("transfer")) {
                transfers.add(t);
            }
        }

        return transfers;
    }

    public List<AccountsDb> getDebts() {
        List<AccountsDb> debts = new ArrayList<>();
        for (AccountsDb a : getAccounts()) {
            if (a.getAcctDebtSav().equals("D")) {
                debts.add(a);
            }
        }
        return debts;
    }

    public List<AccountsDb> getSavings() {
        List<AccountsDb> savings = new ArrayList<>();
        for (AccountsDb a : getAccounts()) {
            if (a.getAcctDebtSav().equals("S")) {
                savings.add(a);
            }
        }
        return savings;
    }

    public List<TransactionsDb> getCashTrans() {
        List<TransactionsDb> cashTrans = new ArrayList<>();
        for (TransactionsDb m2 : getMoneyOuts()) {
            if (m2.getTransType().equals("out") && !m2.getTransIsCC().equals("Y")) {
                cashTrans.add(m2);
            }
        }
        return cashTrans;
    }

    public List<TransactionsDb> getCCTrans() {
        List<TransactionsDb> ccTrans = new ArrayList<>();
        for (TransactionsDb m3 : getMoneyOuts()) {
            if (m3.getTransIsCC().equals("Y")) {
                ccTrans.add(m3);
            }
        }
        return ccTrans;
    }

    public List<TransactionsDb> getCCPayts() {
        List<TransactionsDb> ccPayts = new ArrayList<>();
        for (TransactionsDb m4 : getTransactions()) {
            if (m4.getTransType().equals("ccPayment")) {
                ccPayts.add(m4);
            }
        }
        return ccPayts;
    }

    public List<TransactionsDb> getCCTransStillToPay() {
        List<TransactionsDb> ccTransStillToPay = new ArrayList<>();
        for (TransactionsDb m4 : getMoneyOuts()) {
            if (m4.getTransIsCC().equals("Y") && m4.getTransCCPaid().equals("N")) {
                ccTransStillToPay.add(m4);
            }
        }
        return ccTransStillToPay;
    }

    public List<BudgetDb> getWeeklyLimits() {
        List<BudgetDb> weeklyLimits = new ArrayList<>();
        for (BudgetDb e : getBudget()) {
            if (e.getBdgtWeekly().equals("Y")) {
                weeklyLimits.add(e);
                expenseId = e.getId();
            }
        }
        return weeklyLimits;
    }

    public List<String> getYearsList() {
        List<String> yearsList = new ArrayList<>();

        try {
            List<String> allYears = new ArrayList<>(getTransactions().size());
            for (TransactionsDb m : getTransactions()) {
                allYears.add(m.getTransCreatedOn());
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

    public List<Date> getAllSelectedDatesList(String str1) {
        //str1 = type of Transaction ("in", "out", "transfer", or "ccPayment"
        List<String> allDatesList = new ArrayList<>();

        try {
            List<String> allTransactions = new ArrayList<>();
            for (TransactionsDb t : getTransactions()) {
                if (t.getTransType().equals(str1)) {
                    allTransactions.add(t.getTransCreatedOn());
                }
            }
            List<Date> allDates = new ArrayList<>();
            general.extractingDates(allTransactions, allDates);
            Collections.sort(allDates);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return allDates;
    }

    public Date getEarliestDateInRange(List<Date> list1) {
        //list1 = dbMgr.getAllSelectedDatesList();
        for (Date d : list1) {
            earliestDateInRange = list1.get(0);
        }
        return earliestDateInRange;
    }

    public Date getLatestDateInRange(List<Date> list1) {
        //list1 = dbMgr.getAllSelectedDatesList();
        for (Date d : list1) {
            latestDateInRange = list1.get(list1.size() -1);
        }
        return latestDateInRange;
    }


    public Integer getEarliestEntry(List<String> list1) {
        //list1 = dbMgr.getYearsList();
        List<Integer> years = new ArrayList<>(list1.size());
        for (String s : list1) {
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

    public Integer getLatestEntry(List<String> list2) {
        //list2 = dbMgr.getYearsList();
        List<Integer> years2 = new ArrayList<>(list2.size());
        for (String s : list2) {
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

    public List<TransactionsDb> getTransactionsInRange(List<TransactionsDb> db1, Date date1, Date date2) {
        //db1 = dbMgr.getMoneyIns, dbMgr.getMoneyOuts, dbMgr.getTransfers, or dbMgr.getCCPayts
        //date1 = getEarliestDateInRange
        //date2 = getLatestDateInRange
        List<TransactionsDb> selectedTransactions = new ArrayList<>();
        for(TransactionsDb t3 : db1) {
            date = t3.getTransCreatedOn();
            try {
                sdf = new SimpleDateFormat("dd-MMM-yyyy");
                newDate = sdf.parse(date);
                if (!newDate.before(date1) && !newDate.after(date2)) {
                    selectedTransactions.add(t3);
                }
            }catch(ParseException e) {
                e.printStackTrace();
            }

        }
        return selectedTransactions;
    }

    // DATA RETRIEVAL FORMULAS

    public Double checkOverWeekly(Long long1) {
        //long1 = budgetId for Category
        List<Double> spentThisWeekInCatList = new ArrayList<>();
        for(TransactionsDb t : getMoneyOuts()) {
                if (t.getTransBdgtId() == long1) {
                    if (general.thisWeek().contains(t.getTransCreatedOn())) {
                        spentThisWeekInCatList.add(t.getTransAmt());
                    }
                }
            }
        spentThisWeekInCat = 0.0;
        if (spentThisWeekInCatList.size() == 0) {
            spentThisWeekInCat = 0.0;
        } else {
            for (Double dbl : spentThisWeekInCatList) {
                spentThisWeekInCat += dbl;
            }
        }
        return spentThisWeekInCat;
    }

    public Double sumSelectedTransactions(List<TransactionsDb> list1) {
        //list1 = getTransactionsInRange();
        List<Double> selectedTransactionsList = new ArrayList<>();
        for (TransactionsDb t4 : list1) {
            selectedTransactionsList.add(t4.getTransAmt());
        }
        selectedTransactionSum = 0.0;
        if (selectedTransactionsList.size() == 0) {
            selectedTransactionSum = 0.0;
        } else {
                for (Double dbl : selectedTransactionsList) {
                    selectedTransactionSum += dbl;
                }
        }
        return selectedTransactionSum;
    }

    public String findMoneyInIsDebtSav(long long1) {
        //long1 = moneyInToAcctId
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                moneyInDebtSav = a.getAcctDebtSav();
            }
        }
        return moneyInDebtSav;
    }

    public Double retrieveCurrentAcctAmt(long long1) {
        //long1 = debtId or savId
        currentAcctAmt = 0.0;
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                currentAcctAmt = a.getAcctBal();
            }
        }
        return currentAcctAmt;
    }

    public Double retrieveCurrentAccountBalance() {
        currentAccountBalance = 0.0;
        for (AccountsDb c : getAccounts()) {
            if (c.getId() == 1) {
                currentAccountBalance = c.getAcctBal();
            }
        }
        return currentAccountBalance;
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

    public int retrieveLastPageId() {
        lastPageId = 0;
        for (CurrentDb c3 : getCurrent()) {
            if (c3.getId() == 1) {
                lastPageId = c3.getLastPageId();
            }
        }
        return lastPageId;
    }

    public String retrieveLastDate() {
        lastDate = null;
        for (CurrentDb c4 : getCurrent()) {
            if (c4.getId() == 1) {
                lastDate = c4.getLastDate();
            }
        }
        return lastDate;
    }

    public Double retrieveToPayTotal() {
        List<Double> toPayList = new ArrayList<>();
        for (TransactionsDb m5 : getMoneyOuts()) {
            if (m5.getTransCCToPay().equals("Y") && m5.getTransCCPaid().equals("N")) {
                toPayList.add(m5.getTransAmt());
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

    public Double retrieveAPortion() {
        List<Double> aPortion = new ArrayList<>();
        for (TransactionsDb m7 : getMoneyOuts()) {
            if (m7.getTransCCToPay().equals("Y") && m7.getTransCCPaid().equals("N")) {
                aPortion.add(m7.getTransAmtOutA());
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
        for (TransactionsDb m8 : getMoneyOuts()) {
            if (m8.getTransCCToPay().equals("Y") && m8.getTransCCPaid().equals("N")) {
                owingPortion.add(m8.getTransAmtOutOwing());
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
        for (TransactionsDb m9 : getMoneyOuts()) {
            if (m9.getTransCCToPay().equals("Y") && m9.getTransCCPaid().equals("N")) {
                bPortion.add(m9.getTransAmtOutB());
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

    public String retrieveLatestDone() {
        latestDone = null;
        for (SetUpDb s2 : getSetUp()) {
            if (s2.getId() == 1) {
                latestDone = s2.getLatestDone();
            }
        }
        return latestDone;
    }

    public Double sumTotalDebt() {
        List<Double> debtAmountList = new ArrayList<>(getDebts().size());
        for (AccountsDb d : getDebts()) {
            debtAmountList.add(d.getAcctBal());
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

    public Double sumTotalSavings() {
        List<Double> savingsAmountList = new ArrayList<>(getSavings().size());
        for (AccountsDb d : getSavings()) {
            savingsAmountList.add(d.getAcctBal());
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

    public Double sumTotalSavGoal() {
        List<Double> savingsGoalList = new ArrayList<>(getSavings().size());
        for (AccountsDb a : getSavings()) {
            savingsGoalList.add(a.getAcctMax());
        }
        totalSavGoal = 0.0;
        if (savingsGoalList.size() == 0) {
            totalSavGoal = 0.0;
        } else {
            for (Double dbl : savingsGoalList) {
                totalSavGoal += dbl;
            }
        }
        return totalSavGoal;
    }

    public Double sumTotalAExpenses() {
        List<Double> expensesAmountListA = new ArrayList<>();
        for (BudgetDb e : getExpenses()) {
            if (e.getBdgtPriority().equals("A")) {
                expensesAmountListA.add(e.getBdgtAnnPayt());
            }
        }
        for (AccountsDb d : getDebts()) {
            expensesAmountListA.add(d.getAcctAnnPaytsTo());
        }
        for (AccountsDb s : getSavings()) {
            expensesAmountListA.add(s.getAcctAnnPaytsTo());
        }
        totalAExpenses = 0.0;
        if (expensesAmountListA.size() == 0) {
            totalAExpenses = 0.0;
        } else {
            for (Double dbl : expensesAmountListA) {
                totalAExpenses += dbl;
            }
        }
        return totalAExpenses;
    }

    public Double sumTotalBExpenses() {
        List<Double> expensesAmountListB = new ArrayList<>();
        for (BudgetDb e : getExpenses()) {
            if (e.getBdgtPriority().equals("B")) {
                expensesAmountListB.add(e.getBdgtAnnPayt());
            }
        }
        totalBExpenses = 0.0;
        if (expensesAmountListB.size() == 0) {
            totalBExpenses = 0.0;
        } else {
            for (Double dbl : expensesAmountListB) {
                totalBExpenses += dbl;
            }
        }
        return totalBExpenses;
    }

    public Double sumTotalExpenses() {
        List<Double> expensesAmountList = new ArrayList<>(getExpenses().size());
        for (BudgetDb e : getExpenses()) {
            expensesAmountList.add(e.getBdgtAnnPayt());
        }
        for (AccountsDb d : getDebts()) {
            expensesAmountList.add(d.getAcctAnnPaytsTo());
        }
        for (AccountsDb s : getSavings()) {
            expensesAmountList.add(s.getAcctAnnPaytsTo());
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

    public Double sumTotalIncome() {
        List<Double> incomeList = new ArrayList<>(getIncomes().size());
        totalIncome = 0.0;
        for (BudgetDb i : getIncomes()) {
            incomeList.add(i.getBdgtAnnPayt());
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

    public Double transfersToAcctThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransToAcctId() == long1 && t.getTransFromAcctId() == 1 && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transToThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transToThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transToThisYear += dbl;
            }
        }

        return transToThisYear;
    }

    public Double transfersFromAcctThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransFromAcctId() == long1 && t.getTransToAcctId() == 1 && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transFromThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transFromThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transFromThisYear += dbl;
            }
        }

        return transFromThisYear;
    }

    public Double detAPortionInc(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = either the moneyInAmt from the selection or the new one from an entry
        //dbl2 = amtForA (moneyInAmt * retrieveAPercentage)
        //dbl3 = retrieveCurrentOwingA()

        if (dbl3 == 0.0) { //if nothing owing to A, then split moneyIn according to budget
            moneyInA = dbl2;
        } else { //if money owing to A
            if ((dbl1 - dbl2) >= dbl3) { //if B's portion will cover it, then take what you need and give the rest to B
                moneyInA = dbl2 + dbl3;
            } else { //if B's portion will cover only part, then take it all for A
                moneyInA = dbl1;
            }
        }
        return moneyInA;
    }

    public Double detOwingPortionInc(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = either the moneyInAmt from the selection or the new one from an entry
        //dbl2 = amtForA (moneyInAmt * retrieveAPercentage)
        //dbl3 = retrieveCurrentOwingA()

        if (dbl3 == 0.0) { //if nothing owing to A, then split moneyIn according to budget
            moneyInOwing = 0.0;
        } else { //if money owing to A
            if ((dbl1 - dbl2) >= dbl3) { //if B's portion will cover it, then take what you need and give the rest to B
                moneyInOwing = dbl3;
            } else { //if B's portion will cover only part, then take it all for A
                moneyInOwing = (dbl1 - dbl2);
            }
        }
        return moneyInOwing;
    }

    public Double detBPortionInc(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = either the moneyInAmt from the selection or the new one from an entry
        //dbl2 = amtForA (moneyInAmt * retrieveAPercentage)
        //dbl3 = retrieveCurrentOwingA()

        if (dbl3 == 0.0) { //if nothing owing to A, then split moneyIn according to budget
            moneyInB = (dbl1 - dbl2);
        } else { //if money owing to A
            if ((dbl1 - dbl2) >= dbl3) { //if B's portion will cover it, then take what you need and give the rest to B
                moneyInB = (dbl1 - dbl2) - dbl3;
            } else { //if B's portion will cover only part, then take it all for A
                moneyInB = 0.0;
            }
        }
        return moneyInB;
    }

    public Double detAPortionExp(Double dbl1, String str1, Double dbl2, Double dbl3) {
        //dbl1 = moneyOutAmt from source or entry or tag
        //str1 = priority from method
        //dbl2 = retrieveCurrentA()
        //dbl3 = retrieveCurrentB()

        if (str1.equals("A")) {
            if (dbl2 >= dbl1) { //if A can cover the purchase, it does
                moneyOutA = dbl1;
            } else if (dbl2 <= 0) { //if A has no money
                if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                    moneyOutA = 0.0;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by whole amount
                    moneyOutA = dbl1;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutA = amtMissing;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutA = dbl2;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by the rest
                    moneyOutA = dbl1;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutA = dbl1 - dbl3;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutA = 0.0;
            } else if (dbl3 == 0.0) { //if B has no money, A covers it but is owed for it
                moneyOutA = dbl1;
            } else { //if B can cover part of the purchase then A covers the rest and is owed for it
                amtMissing = dbl1 - dbl3;
                moneyOutA = amtMissing;
            }
        }
        return moneyOutA;
    }

    public Double detOwingPortionExp(Double dbl1, String str1, Double dbl2, Double dbl3) {
        //dbl1 = moneyOutAmt from source or entry or tag
        //str1 = priority from method
        //dbl2 = retrieveCurrentA()
        //dbl3 = retrieveCurrentB()

        if (str1.equals("A")) {
            if (dbl2 >= dbl1) { //if A can cover the purchase, it does
                moneyOutOwing = 0.0;
            } else if (dbl2 <= 0) { //if A has no money
                if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                    moneyOutOwing = 0.0;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by whole amount
                    moneyOutOwing = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutOwing = 0.0;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutOwing = 0.0;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by the rest
                    moneyOutOwing = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutOwing = 0.0;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutOwing = 0.0;
            } else if (dbl3 == 0.0) { //if B has no money, A covers it but is owed for it
                moneyOutOwing = dbl1;
            } else { //if B can cover part of the purchase then A covers the rest and is owed for it
                amtMissing = dbl1 - dbl3;
                moneyOutOwing = amtMissing;
            }
        }
        return moneyOutOwing;
    }

    public Double detBPortionExp(Double dbl1, String str1, Double dbl2, Double dbl3) {
        //dbl1 = moneyOutAmt from source or entry or tag
        //str1 = priority from method
        //dbl2 = retrieveCurrentA()
        //dbl3 = retrieveCurrentB()

        if (str1.equals("A")) {
            if (dbl2 >= dbl1) { //if A can cover the purchase, it does
                moneyOutB = 0.0;
            } else if (dbl2 <= 0) { //if A has no money
                if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                    moneyOutB = dbl1;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by whole amount
                    moneyOutB = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutB = dbl3;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutB = amtMissing;
                } else if (dbl3 == 0.0) { //if B has no money, A goes negative by the rest
                    moneyOutB = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutB = dbl3;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutB = dbl1;
            } else if (dbl3 == 0.0) { //if B has no money, A covers it but is owed for it
                moneyOutB = 0.0;
            } else { //if B can cover part of the purchase then A covers the rest and is owed for it
                amtMissing = dbl1 - dbl3;
                moneyOutB = dbl3;
            }
        }
        return moneyOutB;
    }

    public Double detNewAPortion(Double dbl1, Double dbl2) {
        //dbl1 = latestMoneyInA,
        //dbl2 = retrieveCurrentOwingA()
        amtToZero = -(dbl2);
        newMoneyA = dbl1 - amtToZero;

        return newMoneyA;
    }

    public Double detNewOwingPortion(Double dbl1, Double dbl2) {
        //dbl1 = latestMoneyInOwing,
        //dbl2 = retrieveCurrentOwingA()
        amtToZero = -(dbl2);
        newMoneyOwing = dbl1 + amtToZero;

        return newMoneyOwing;
    }

    public Double detNewBPortion(Double dbl1, Double dbl2) {
        //dbl1 = latestMoneyInB
        //dbl2 = retrieveCurrentOwingA()
        amtToZero = -(dbl2);
        newMoneyB = dbl1 + amtToZero;

        return newMoneyB;
    }

    public Double makeNewIncAnnAmt(Long long1, List<String> list1) {
        //long1 = incId
        //list1 = general.last365Days();
        List<Double> incEntriesThisYear = new ArrayList<>();
        for (TransactionsDb m : getMoneyIns()) {
            if (m.getTransBdgtId() == (long1) && list1.contains(m.getTransCreatedOn())) {
                incEntriesThisYear.add(m.getTransAmt());
            }
        }
        incThisYear = 0.0;
        numberOfEntries = 0;
        if (incEntriesThisYear.size() == 0) {
            incThisYear = 0.0;
            numberOfEntries = 0;
        } else {
            for (Double dbl : incEntriesThisYear) {
                incThisYear += dbl;
            }
            numberOfEntries = incEntriesThisYear.size();
        }

        numberOfMissingEntries = 0.0;
        amtOfMissingEntries = 0.0;
        for (BudgetDb i : getIncomes()) {
            if (i.getId() == long1) {
                incAmtFromDb = i.getBdgtPaytAmt();
                incFrqFromDb = i.getBdgtPaytFrq();
                incAnnAmtFromDb = i.getBdgtAnnPayt();
                numberOfMissingEntries = incFrqFromDb - numberOfEntries;
                if (incFrqFromDb == 1 && numberOfMissingEntries <= 0) {
                    if (incAmtFromDb >= incThisYear) {
                        newIncAnnAmt = incAmtFromDb;
                    } else {
                        newIncAnnAmt = incThisYear;
                    }
                } else if (numberOfMissingEntries <= 0) {
                    newIncAnnAmt = incThisYear;
                } else {
                    amtOfMissingEntries = incAmtFromDb * numberOfMissingEntries;
                    newIncAnnAmt = amtOfMissingEntries + incThisYear;
                }
            }
        }
        return newIncAnnAmt;
    }

    public Double makeNewExpAnnAmt(Long long1, List<String> list1) {
        //long1 = expenseId
        //list1 = general.lastNumOfDays(365);
        List<Double> expEntriesThisYear = new ArrayList<>();
        for (TransactionsDb m : getMoneyOuts()) {
            if (m.getTransBdgtId() == (long1) && list1.contains(m.getTransCreatedOn())) {
                expEntriesThisYear.add(m.getTransAmt());
            }
        }
        expThisYear = 0.0;
        numberOfEntriesE = 0;
        if (expEntriesThisYear.size() == 0) {
            expThisYear = 0.0;
            numberOfEntriesE = 0;
        } else {
            for (Double dbl : expEntriesThisYear) {
                expThisYear += dbl;
            }
            numberOfEntriesE = expEntriesThisYear.size();
        }

        numberOfMissingEntriesE = 0.0;
        amtOfMissingEntriesE = 0.0;
        for (BudgetDb e : getExpenses()) {
            if (e.getId() == long1) {
                expAmtFromDb = e.getBdgtPaytAmt();
                expPriorityFromDb = e.getBdgtPriority();
                expFrqFromDb = e.getBdgtPaytFrq();
                numberOfMissingEntriesE = expFrqFromDb - numberOfEntriesE;
                if (expFrqFromDb == 1 && numberOfMissingEntriesE <= 0) {
                    if (expAmtFromDb >= expThisYear) {
                        newExpAnnAmt = expAmtFromDb;
                    } else {
                        newExpAnnAmt = expThisYear;
                    }
                } else if (numberOfMissingEntriesE <= 0) {
                    newExpAnnAmt = expThisYear;
                } else {
                    amtOfMissingEntriesE = expAmtFromDb * numberOfMissingEntriesE;
                    newExpAnnAmt = amtOfMissingEntriesE + expThisYear;
                }
            }
        }
        return newExpAnnAmt;
    }


    //UPDATE TABLE OR DATA FORMULAS

    public void resetToPay() {
        db = dbHelper.getWritableDatabase();
        cvToPay = new ContentValues();
        cvToPay.put(DbHelper.TRANSCCTOPAY, "N");
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, cvToPay, DbHelper.TRANSCCTOPAY + "= 'Y' AND " + DbHelper.TRANSCCPAID
                + " = 'N'", null);
        db.close();
    }

    public void updatePaid() {
        db = dbHelper.getWritableDatabase();
        updateMoneyOutPaid = new ContentValues();
        updateMoneyOutPaid.put(DbHelper.TRANSCCPAID, "Y");
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, updateMoneyOutPaid, DbHelper.TRANSCCTOPAY + "= 'Y' AND " + DbHelper.TRANSCCPAID + " = 'N'", null);
        db.close();
    }

    public void resetDebtToPay() {
        db = dbHelper.getWritableDatabase();
        cvDebtToPay = new ContentValues();
        cvDebtToPay.put(DbHelper.ACCTDEBTTOPAY, 0.0);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, cvDebtToPay, DbHelper.ACCTDEBTTOPAY + "> 0", null);
        db.close();
    }

    public void updateTotAcctBalPlus(Double dbl1, Double dbl2) {
        //dbl1 = moneyIn or OutAmt from entry or selection or tag
        //dbl2 = retrieveCurrentAccountBalance()
        db = dbHelper.getWritableDatabase();

        newAcctBal = dbl2 + dbl1;

        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newAcctBal);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "= '1'", null);
        db.close();
    }

    public void updateTotAcctBalMinus(Double dbl1, Double dbl2) {
        //dbl1 = moneyIn or OutAmt from entry or selection or tag
        //dbl2 = retrieveCurrentAccountBalance()
        db = dbHelper.getWritableDatabase();

        newAcctBal = dbl2 - dbl1;

        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newAcctBal);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "= '1'", null);
        db.close();
    }

    public void updateRecReTransfer(Long long1, Double dbl1, Double dbl2) {
        //long1 = debtId or SavId
        //dbl1 = transfersToAcctThisYear(long1)
        //dbl2 = transfersFromAcctThisYear(long1)
        db = dbHelper.getWritableDatabase();
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                annPaytFromDb = a.getAcctAnnPaytsTo();
                if (dbl1 - dbl2 > annPaytFromDb) {
                    cursor = db.rawQuery("SELECT " + DbHelper.ACCTANNPAYTSTO + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ID + " = " + long1, null);
                    db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + dbl1 + " - " + dbl2 + " WHERE " + DbHelper.ID + " = " + long1);
                }
            }
        }
        db.close();
    }

    public void updateRecPlusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from entry or from tag
        //dbl2 = currAcctBal from whichever source
        //lg1 = acctId from whichever source
        db = dbHelper.getWritableDatabase();

        newAmt = dbl2 + dbl1;
        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }

    public void updateRecMinusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from entry or from tag
        //dbl2 = currAcctBal from whichever source
        //lg1 = acctId from whichever source
        db = dbHelper.getWritableDatabase();

        newAmt = dbl2 - dbl1;
        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }

    public void updateRecPt2(String str1, long lg1) {
        //str1 = endDate from calcEndDate method
        //lg1 = acctId from whichever source
        db = dbHelper.getWritableDatabase();

        CV = new ContentValues();
        CV.put(DbHelper.ACCTENDDATE, str1);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }

    public void adjustCurrentAandB(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = retrieveCurrentOwingA()
        //dbl2 = retrieveCurrentA()
        //dbl3 = retrieveCurrentB()

        //if amount owing to A is negative, then A actually owes B
        amtToZero = -(dbl1);
        newABal = dbl2 - amtToZero;
        newBBal = dbl3 + amtToZero;

        db = dbHelper.getWritableDatabase();

        CV = new ContentValues();
        CV.put(DbHelper.CURRENTA, newABal);
        CV.put(DbHelper.CURRENTOWINGA, 0.0);
        CV.put(DbHelper.CURRENTB, newBBal);
        db.update(DbHelper.CURRENT_TABLE_NAME, CV, DbHelper.ID + "= '1'", null);
        db.close();
    }

    public void updateAandBBalPlus(Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5, Double dbl6) {
        //db1 = currentMoneyIn or OutA,
        //dbl2 = currentMoneyIn or OutOwing,
        //dbl3 = currentMoneyIn or OutB
        //dbl4 = retrieveCurrentA()
        //dbl5 = retrieveCurrentOwingA()
        //dbl6 = retrieveCurrentB()
        db = dbHelper.getWritableDatabase();

        newABal = dbl4 + dbl1;
        newOwingBal = dbl5 - dbl2;
        newBBal = dbl6 + dbl3;

        CV = new ContentValues();

        CV.put(DbHelper.CURRENTA, newABal);
        CV.put(DbHelper.CURRENTOWINGA, newOwingBal);
        CV.put(DbHelper.CURRENTB, newBBal);
        db.update(DbHelper.CURRENT_TABLE_NAME, CV, DbHelper.ID + "= '1'", null);
        db.close();
    }

    public void updateAandBBalMinus(Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5, Double dbl6) {
        //db1 = currentMoneyIn or OutA
        //dbl2 = currentMoneyIn or OutOwing
        //dbl3 = currentMoneyIn or OutB
        //dbl4 = retrieveCurrentA()
        //dbl5 = retrieveCurrentOwingA()
        //dbl6 = retrieveCurrentB()
        db = dbHelper.getWritableDatabase();

        newABal = dbl4 - dbl1;
        newOwingBal = dbl5 + dbl2;
        newBBal = dbl6 - dbl3;

        CV = new ContentValues();

        CV.put(DbHelper.CURRENTA, newABal);
        CV.put(DbHelper.CURRENTOWINGA, newOwingBal);
        CV.put(DbHelper.CURRENTB, newBBal);
        db.update(DbHelper.CURRENT_TABLE_NAME, CV, DbHelper.ID + "= '1'", null);
        db.close();
    }

    public void mainHeaderText(TextView tv1, TextView tv2, TextView tv3, TextView tv4, Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5) {
        //tv1 = budgetWarningText
        //tv2 = totalAcctBalTV
        //tv3 = availBalTV
        //tv4 = availBalLabel
        //dbl1 = sumTotalExpenses()
        //dbl2 = sumTotalIncome()
        //dbl3 = retrieveCurrentAccountBalance()
        //dbl4 = retrieveCurrentB()
        //dbl5 = retrieveCurrentA()

        if (dbl1 > dbl2) {
            //totalExpenses = all expAnnAmt + all debtAnnPayt + all savAnnPayt
            tv1.setVisibility(View.VISIBLE);
        }

        if (dbl3 < 0 || dbl4 < 0 || dbl3 < dbl4) {
            newAvailBal2 = 0.0;
        } else {
            newAvailBal2 = dbl4;
        }

        general.dblASCurrency(String.valueOf(dbl5), tv2);
        general.dblASCurrency(String.valueOf(newAvailBal2), tv3);

        if (dbl4 > 0) {
            tv3.setTextColor(Color.parseColor("#5dbb63")); //light green
            tv4.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else {
            tv3.setTextColor(Color.parseColor("#83878b")); //gray
            tv4.setTextColor(Color.parseColor("#83878b")); //gray
        }
    }

    public void payCCHeaderText(TextView tv1, TextView tv2, TextView tv3, Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5) {
        //tv1 = totalAcctBalTV
        //tv2 = availBalTV
        //tv3 = availBalLabel
        //dbl1 = sumTotalExpenses()
        //dbl2 = sumTotalIncome()
        //dbl3 = retrieveCurrentAccountBalance()
        //dbl4 = retrieveCurrentB()
        //dbl5 = retrieveCurrentA()

        if (dbl3 < 0 || dbl4 < 0 || dbl3 < dbl4) {
            newAvailBal2 = 0.0;
        } else {
            newAvailBal2 = dbl4;
        }

        general.dblASCurrency(String.valueOf(dbl5), tv1);
        general.dblASCurrency(String.valueOf(newAvailBal2), tv2);

        if (dbl4 > 0) {
            tv2.setTextColor(Color.parseColor("#5dbb63")); //light green
            tv3.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else {
            tv2.setTextColor(Color.parseColor("#83878b")); //gray
            tv3.setTextColor(Color.parseColor("#83878b")); //gray
        }
    }

    public void spendResPara(TextView tv, TextView tv2, String str1, String str2, String str3, Double dbl1, int int1) {
        //tv = statement textView
        //tv2 = additional textView re: whether or not adjustments necessary
        //str1 = recommendation
        //str2 = ana_res_prt_1
        //str3 = ana_res_prt_2
        //dbl1 = (sumTotalAExpenses() / sumTotalIncome());

        percentFormat.setMinimumFractionDigits(1);
        percentFormat.setMaximumFractionDigits(1);
        spendPercent = percentFormat.format(dbl1);
        spendResStmt = str2 + " " + spendPercent + " " + str3;

        tv.setText(spendResStmt);
        tv.setTextColor(int1);
        tv2.setText(str1);
        tv2.setTextColor(int1);
    }

    public void updateAllDebtRecords() {
        db = dbHelper.getWritableDatabase();

        for (AccountsDb a : getDebts()) {
            acctDebtToPayFromDb = a.getAcctDebtToPay();
            if (acctDebtToPayFromDb > 0) {
                cursor = db.rawQuery("SELECT " + DbHelper.ACCTBAL + ", " + DbHelper.ACCTDEBTTOPAY + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ID + " = " + a.getId(), null);
                db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTBAL + " = " + DbHelper.ACCTBAL + " - " + DbHelper.ACCTDEBTTOPAY + " WHERE " + DbHelper.ID + " = " + a.getId());
            }
        }
        db.close();
    }
}
