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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.CurrentDb;
//import ca.gotchasomething.mynance.data.DebtDb;
//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
//import ca.gotchasomething.mynance.data.IncomeBudgetDb;
//import ca.gotchasomething.mynance.data.MoneyInDb;
//import ca.gotchasomething.mynance.data.MoneyOutDb;
//import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.SetUpDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
//import ca.gotchasomething.mynance.data.TransfersDb;

public class DbManager extends AppCompatActivity {

    public Cursor cursor;
    public ContentValues CV, cvDebtToPay, cvToPay, newAccounts, newCurrent, newDebt, newExpense, newIncome, newMoneyIn, newMoneyOut, newSavings, newSetUp,
            newTransfers, updateAccounts, updateCurrent, updateDebt, updateExpense,
            updateIncome, updateMoneyIn, updateMoneyOut, updateMoneyOutPaid, updateSaving, updateSetUp, updateTransfers;
    public DbHelper dbHelper;
    public Double amtOfMissingEntries = 0.0, amtOfMissingEntriesE = 0.0, amtMissing = 0.0, amtToZero = 0.0, currentAccountBalance = 0.0, currentB = 0.0,
            currentDebtAmtOwing = 0.0, currentSavAmt = 0.0, annPaytFromDb = 0.0, expAmtFromDb = 0.0, expFrqFromDb = 0.0, expThisYear = 0.0, incAmtFromDb = 0.0,
            incAnnAmtFromDb = 0.0, incFrqFromDb = 0.0, moneyInA = 0.0, moneyInB = 0.0, moneyInOwing = 0.0, moneyOutA = 0.0, moneyOutB = 0.0, moneyOutOwing = 0.0, newABal = 0.0, newAcctBal = 0.0,
            newAvailBal2 = 0.0, newBBal = 0.0, newAmt = 0.0, newExpAnnAmt = 0.0, newIncAnnAmt = 0.0, newMoneyA = 0.0, newMoneyB = 0.0, newMoneyOwing = 0.0,
            newOwingBal = 0.0, newSavAmt = 0.0, incThisYear = 0.0, numberOfMissingEntries = 0.0, numberOfMissingEntriesE = 0.0, savAnnPaytFromDb = 0.0, totalAExpenses = 0.0,
            totalCCPaymentDue = 0.0, totalDebt = 0.0, totalExpenses = 0.0, totalIncome = 0.0, totalSavings = 0.0, currentA = 0.0, currentOwingA = 0.0, totalAPortion = 0.0,
            totalOwingPortion = 0.0, totalBPortion = 0.0, transThisYear = 0.0;
    public General general = new General();
    public int int1 = 0, lastPageId = 0, earliestYear = 0, endIndex = 0, latestYear = 0, numberOfEntries = 0, numberOfEntriesE = 0, startIndex = 0;
    public Long acctId, debtId, expenseId, moneyInDebtId, moneyInSavId, savId;
    public NumberFormat percentFormat = NumberFormat.getPercentInstance();
    public SQLiteDatabase db;
    public String category = null, expPriorityFromDb = null, lastDate = null, moneyInIsDebt = null, moneyInIsSav = null,
            spendResStmt = null, spendPercent = null, startingString = null, subStringResult = null, latestDone = null, text = null;

    public DbManager(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }


    //GET, ADD, UPDATE, & DELETE FORMULAS

    public List<TransactionsDb> getTransfers() {
        List<TransactionsDb> transfersList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.TRANSACTIONS_TABLE_NAME + " WHERE " + DbHelper.TRANSTYPE + " = 'transfer'", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                TransactionsDb transfersDb = new TransactionsDb(
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
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISDEBT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISSAV)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSFROMACCTID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMACCTNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISDEBT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISSAV)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTWEEKLY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCTOPAY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCPAID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                transfersList.add(0, transfersDb); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return transfersList;
    }

    public void addTransfers(TransactionsDb transfersDb) {
        newTransfers = new ContentValues();
        newTransfers.put(DbHelper.TRANSTYPE, transfersDb.getTransType());
        newTransfers.put(DbHelper.TRANSISCC, transfersDb.getTransIsCC());
        newTransfers.put(DbHelper.TRANSBDGTCAT, transfersDb.getTransBdgtCat());
        newTransfers.put(DbHelper.TRANSBDGTID, transfersDb.getTransBdgtId());
        newTransfers.put(DbHelper.TRANSAMT, transfersDb.getTransAmt());
        newTransfers.put(DbHelper.TRANSAMTINA, transfersDb.getTransAmtInA());
        newTransfers.put(DbHelper.TRANSAMTINOWING, transfersDb.getTransAmtInOwing());
        newTransfers.put(DbHelper.TRANSAMTINB, transfersDb.getTransAmtInB());
        newTransfers.put(DbHelper.TRANSAMTOUTA, transfersDb.getTransAmtOutA());
        newTransfers.put(DbHelper.TRANSAMTOUTOWING, transfersDb.getTransAmtOutOwing());
        newTransfers.put(DbHelper.TRANSAMTOUTB, transfersDb.getTransAmtOutB());
        newTransfers.put(DbHelper.TRANSTOACCTID, transfersDb.getTransToAcctId());
        newTransfers.put(DbHelper.TRANSTOACCTNAME, transfersDb.getTransToAcctName());
        newTransfers.put(DbHelper.TRANSTOISDEBT, transfersDb.getTransToIsDebt());
        newTransfers.put(DbHelper.TRANSTOISSAV, transfersDb.getTransToIsSav());
        newTransfers.put(DbHelper.TRANSFROMACCTID, transfersDb.getTransFromAcctId());
        newTransfers.put(DbHelper.TRANSFROMACCTNAME, transfersDb.getTransFromAcctName());
        newTransfers.put(DbHelper.TRANSFROMISDEBT, transfersDb.getTransFromIsDebt());
        newTransfers.put(DbHelper.TRANSFROMISSAV, transfersDb.getTransFromIsSav());
        newTransfers.put(DbHelper.TRANSBDGTPRIORITY, transfersDb.getTransBdgtPriority());
        newTransfers.put(DbHelper.TRANSBDGTWEEKLY, transfersDb.getTransBdgtWeekly());
        newTransfers.put(DbHelper.TRANSCCTOPAY, transfersDb.getTransCCToPay());
        newTransfers.put(DbHelper.TRANSCCPAID, transfersDb.getTransCCPaid());
        newTransfers.put(DbHelper.TRANSCREATEDON, transfersDb.getTransCreatedOn());

        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.TRANSACTIONS_TABLE_NAME, null, newTransfers);
    }

    public void updateTransfers(TransactionsDb transfersDb) {
        updateTransfers = new ContentValues();
        updateTransfers.put(DbHelper.TRANSTYPE, transfersDb.getTransType());
        updateTransfers.put(DbHelper.TRANSISCC, transfersDb.getTransIsCC());
        updateTransfers.put(DbHelper.TRANSBDGTCAT, transfersDb.getTransBdgtCat());
        updateTransfers.put(DbHelper.TRANSBDGTID, transfersDb.getTransBdgtId());
        updateTransfers.put(DbHelper.TRANSAMT, transfersDb.getTransAmt());
        updateTransfers.put(DbHelper.TRANSAMTINA, transfersDb.getTransAmtInA());
        updateTransfers.put(DbHelper.TRANSAMTINOWING, transfersDb.getTransAmtInOwing());
        updateTransfers.put(DbHelper.TRANSAMTINB, transfersDb.getTransAmtInB());
        updateTransfers.put(DbHelper.TRANSAMTOUTA, transfersDb.getTransAmtOutA());
        updateTransfers.put(DbHelper.TRANSAMTOUTOWING, transfersDb.getTransAmtOutOwing());
        updateTransfers.put(DbHelper.TRANSAMTOUTB, transfersDb.getTransAmtOutB());
        updateTransfers.put(DbHelper.TRANSTOACCTID, transfersDb.getTransToAcctId());
        updateTransfers.put(DbHelper.TRANSTOACCTNAME, transfersDb.getTransToAcctName());
        updateTransfers.put(DbHelper.TRANSTOISDEBT, transfersDb.getTransToIsDebt());
        updateTransfers.put(DbHelper.TRANSTOISSAV, transfersDb.getTransToIsSav());
        updateTransfers.put(DbHelper.TRANSFROMACCTID, transfersDb.getTransFromAcctId());
        updateTransfers.put(DbHelper.TRANSFROMACCTNAME, transfersDb.getTransFromAcctName());
        updateTransfers.put(DbHelper.TRANSFROMISDEBT, transfersDb.getTransFromIsDebt());
        updateTransfers.put(DbHelper.TRANSFROMISSAV, transfersDb.getTransFromIsSav());
        updateTransfers.put(DbHelper.TRANSBDGTPRIORITY, transfersDb.getTransBdgtPriority());
        updateTransfers.put(DbHelper.TRANSBDGTWEEKLY, transfersDb.getTransBdgtWeekly());
        updateTransfers.put(DbHelper.TRANSCCTOPAY, transfersDb.getTransCCToPay());
        updateTransfers.put(DbHelper.TRANSCCPAID, transfersDb.getTransCCPaid());
        updateTransfers.put(DbHelper.TRANSCREATEDON, transfersDb.getTransCreatedOn());

        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(transfersDb.getId())};
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, updateTransfers, DbHelper.ID + "=?", args);
    }

    public void deleteTransfers(TransactionsDb transfersDb) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(transfersDb.getId())};
        db.delete(DbHelper.TRANSACTIONS_TABLE_NAME, DbHelper.ID + "=?", args);
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
                        cursor.getString(cursor.getColumnIndex(DbHelper.ACCTISDEBT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.ACCTISSAV)),
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
        newAccounts.put(DbHelper.ACCTISDEBT, acctsDb.getAcctIsDebt());
        newAccounts.put(DbHelper.ACCTISSAV, acctsDb.getAcctIsSav());
        newAccounts.put(DbHelper.ACCTMAX, acctsDb.getAcctMax());
        newAccounts.put(DbHelper.ACCTINTRATE, acctsDb.getAcctIntRate());
        newAccounts.put(DbHelper.ACCTPAYTSTO, acctsDb.getAcctPaytsTo());
        newAccounts.put(DbHelper.ACCTANNPAYTSTO, acctsDb.getAcctAnnPaytsTo());
        newAccounts.put(DbHelper.ACCTENDDATE, acctsDb.getAcctEndDate());
        newAccounts.put(DbHelper.ACCTDEBTTOPAY, acctsDb.getAcctDebtToPay());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.ACCOUNTS_TABLE_NAME, null, newAccounts);
    }

    public void updateAccounts(AccountsDb acctsDb) {
        updateAccounts = new ContentValues();
        updateAccounts.put(DbHelper.ACCTNAME, acctsDb.getAcctName());
        updateAccounts.put(DbHelper.ACCTBAL, acctsDb.getAcctBal());
        updateAccounts.put(DbHelper.ACCTISDEBT, acctsDb.getAcctIsDebt());
        updateAccounts.put(DbHelper.ACCTISSAV, acctsDb.getAcctIsSav());
        updateAccounts.put(DbHelper.ACCTMAX, acctsDb.getAcctMax());
        updateAccounts.put(DbHelper.ACCTINTRATE, acctsDb.getAcctIntRate());
        updateAccounts.put(DbHelper.ACCTPAYTSTO, acctsDb.getAcctPaytsTo());
        updateAccounts.put(DbHelper.ACCTANNPAYTSTO, acctsDb.getAcctAnnPaytsTo());
        updateAccounts.put(DbHelper.ACCTENDDATE, acctsDb.getAcctEndDate());
        updateAccounts.put(DbHelper.ACCTDEBTTOPAY, acctsDb.getAcctDebtToPay());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(acctsDb.getId())};
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, updateAccounts, DbHelper.ID + "=?", args);
    }

    public void deleteAccounts(AccountsDb acctsDb) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(acctsDb.getId())};
        db.delete(DbHelper.ACCOUNTS_TABLE_NAME, DbHelper.ID + "=?", args);
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
    }

    public void updateSetUp(SetUpDb setUp) {
        updateSetUp = new ContentValues();
        updateSetUp.put(DbHelper.LATESTDONE, setUp.getLatestDone());
        updateSetUp.put(DbHelper.BALANCEAMOUNT, setUp.getBalanceAmount());
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

    /*public List<DebtDb> getDebts() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTRATE + " DESC", null);
        List<DebtDb> debts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                DebtDb debt = new DebtDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTNAME)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTLIMIT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTOWING)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTPAYMENTS)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTANNUALPAYMENTS)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.DEBTEND)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.DEBTTOPAY)),
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
        newDebt.put(DbHelper.DEBTOWING, debt.getDebtOwing());
        newDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        newDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        newDebt.put(DbHelper.DEBTANNUALPAYMENTS, debt.getDebtAnnualPayments());
        newDebt.put(DbHelper.DEBTEND, debt.getDebtEnd());
        newDebt.put(DbHelper.DEBTTOPAY, debt.getDebtToPay());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.DEBTS_TABLE_NAME, null, newDebt);
    }

    public void updateDebt(DebtDb debt) {
        updateDebt = new ContentValues();
        updateDebt.put(DbHelper.DEBTNAME, debt.getDebtName());
        updateDebt.put(DbHelper.DEBTLIMIT, debt.getDebtLimit());
        updateDebt.put(DbHelper.DEBTOWING, debt.getDebtOwing());
        updateDebt.put(DbHelper.DEBTRATE, debt.getDebtRate());
        updateDebt.put(DbHelper.DEBTPAYMENTS, debt.getDebtPayments());
        updateDebt.put(DbHelper.DEBTANNUALPAYMENTS, debt.getDebtAnnualPayments());
        updateDebt.put(DbHelper.DEBTEND, debt.getDebtEnd());
        updateDebt.put(DbHelper.DEBTTOPAY, debt.getDebtToPay());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.update(DbHelper.DEBTS_TABLE_NAME, updateDebt, DbHelper.ID + "=?", args);
    }

    public void deleteDebt(DebtDb debt) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(debt.getId())};
        db.delete(DbHelper.DEBTS_TABLE_NAME, DbHelper.ID + "=?", args);
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
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSRATE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.SAVINGSANNUALPAYMENTS)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.SAVINGSDATE)),
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
        newSavings.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        newSavings.put(DbHelper.SAVINGSANNUALPAYMENTS, saving.getSavingsAnnualPayments());
        newSavings.put(DbHelper.SAVINGSDATE, saving.getSavingsDate());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.SAVINGS_TABLE_NAME, null, newSavings);
    }

    public void updateSavings(SavingsDb saving) {
        updateSaving = new ContentValues();
        updateSaving.put(DbHelper.SAVINGSNAME, saving.getSavingsName());
        updateSaving.put(DbHelper.SAVINGSAMOUNT, saving.getSavingsAmount());
        updateSaving.put(DbHelper.SAVINGSGOAL, saving.getSavingsGoal());
        updateSaving.put(DbHelper.SAVINGSPAYMENTS, saving.getSavingsPayments());
        updateSaving.put(DbHelper.SAVINGSRATE, saving.getSavingsRate());
        updateSaving.put(DbHelper.SAVINGSANNUALPAYMENTS, saving.getSavingsAnnualPayments());
        updateSaving.put(DbHelper.SAVINGSDATE, saving.getSavingsDate());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(saving.getId())};
        db.update(DbHelper.SAVINGS_TABLE_NAME, updateSaving, DbHelper.ID + "=?", args);
    }

    public void deleteSavings(SavingsDb saving) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(saving.getId())};
        db.delete(DbHelper.SAVINGS_TABLE_NAME, DbHelper.ID + "=?", args);
    }*/

    public List<BudgetDb> getExpense() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.BUDGET_TABLE_NAME + " WHERE " + DbHelper.BDGTISEXP + " = 'Y' " + " ORDER BY " + DbHelper.BDGTANNPAYT + " DESC", null);
        List<BudgetDb> expenses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                BudgetDb expense = new BudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTISEXP)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTISINC)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTFRQ)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTANNPAYT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTWEEKLY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                expenses.add(expense); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return expenses;
    }

    public void addExpense(BudgetDb expense) {
        newExpense = new ContentValues();
        newExpense.put(DbHelper.BDGTCAT, expense.getBdgtCat());
        newExpense.put(DbHelper.BDGTPAYTAMT, expense.getBdgtPaytAmt());
        newExpense.put(DbHelper.BDGTISEXP, expense.getBdgtIsExp());
        newExpense.put(DbHelper.BDGTISINC, expense.getBdgtIsInc());
        newExpense.put(DbHelper.BDGTPAYTFRQ, expense.getBdgtPaytFrq());
        newExpense.put(DbHelper.BDGTANNPAYT, expense.getBdgtAnnPayt());
        newExpense.put(DbHelper.BDGTPRIORITY, expense.getBdgtPriority());
        newExpense.put(DbHelper.BDGTWEEKLY, expense.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        db.insertOrThrow(DbHelper.BUDGET_TABLE_NAME, null, newExpense);
    }

    public void updateExpense(BudgetDb expense) {
        updateExpense = new ContentValues();
        updateExpense.put(DbHelper.BDGTCAT, expense.getBdgtCat());
        updateExpense.put(DbHelper.BDGTPAYTAMT, expense.getBdgtPaytAmt());
        updateExpense.put(DbHelper.BDGTISEXP, expense.getBdgtIsExp());
        updateExpense.put(DbHelper.BDGTISINC, expense.getBdgtIsInc());
        updateExpense.put(DbHelper.BDGTPAYTFRQ, expense.getBdgtPaytFrq());
        updateExpense.put(DbHelper.BDGTANNPAYT, expense.getBdgtAnnPayt());
        updateExpense.put(DbHelper.BDGTPRIORITY, expense.getBdgtPriority());
        updateExpense.put(DbHelper.BDGTWEEKLY, expense.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(expense.getId())};
        db.update(DbHelper.BUDGET_TABLE_NAME, updateExpense, DbHelper.ID + "=?", args);
    }

    public void deleteExpense(BudgetDb expense) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(expense.getId())};
        db.delete(DbHelper.BUDGET_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<BudgetDb> getIncomes() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.BUDGET_TABLE_NAME + " WHERE " + DbHelper.BDGTISINC + " = 'Y' " + " ORDER BY " + DbHelper.BDGTANNPAYT + " DESC", null);
        List<BudgetDb> incomes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                BudgetDb income = new BudgetDb(
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTCAT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTISEXP)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTISINC)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTPAYTFRQ)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.BDGTANNPAYT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.BDGTWEEKLY)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                incomes.add(income); //adds new items to bottom of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return incomes;
    }

    public void addIncome(BudgetDb income) {
        newIncome = new ContentValues();
        newIncome.put(DbHelper.BDGTCAT, income.getBdgtCat());
        newIncome.put(DbHelper.BDGTPAYTAMT, income.getBdgtPaytAmt());
        newIncome.put(DbHelper.BDGTISEXP, income.getBdgtIsExp());
        newIncome.put(DbHelper.BDGTISINC, income.getBdgtIsInc());
        newIncome.put(DbHelper.BDGTPAYTFRQ, income.getBdgtPaytFrq());
        newIncome.put(DbHelper.BDGTANNPAYT, income.getBdgtAnnPayt());
        newIncome.put(DbHelper.BDGTPRIORITY, income.getBdgtPriority());
        newIncome.put(DbHelper.BDGTWEEKLY, income.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.BUDGET_TABLE_NAME, null, newIncome);
    }

    public void updateIncome(BudgetDb income) {
        updateIncome = new ContentValues();
        updateIncome.put(DbHelper.BDGTCAT, income.getBdgtCat());
        updateIncome.put(DbHelper.BDGTPAYTAMT, income.getBdgtPaytAmt());
        updateIncome.put(DbHelper.BDGTISEXP, income.getBdgtIsExp());
        updateIncome.put(DbHelper.BDGTISINC, income.getBdgtIsInc());
        updateIncome.put(DbHelper.BDGTPAYTFRQ, income.getBdgtPaytFrq());
        updateIncome.put(DbHelper.BDGTANNPAYT, income.getBdgtAnnPayt());
        updateIncome.put(DbHelper.BDGTPRIORITY, income.getBdgtPriority());
        updateIncome.put(DbHelper.BDGTWEEKLY, income.getBdgtWeekly());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(income.getId())};
        db.update(DbHelper.BUDGET_TABLE_NAME, updateIncome, DbHelper.ID + "=?", args);
    }

    public void deleteIncome(BudgetDb income) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(income.getId())};
        db.delete(DbHelper.BUDGET_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<TransactionsDb> getMoneyIns() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.TRANSACTIONS_TABLE_NAME + " WHERE " + DbHelper.TRANSTYPE + " = 'in'", null);
        List<TransactionsDb> moneyIns = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                TransactionsDb moneyIn = new TransactionsDb(
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
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISDEBT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISSAV)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSFROMACCTID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMACCTNAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISDEBT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISSAV)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTPRIORITY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTWEEKLY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCTOPAY)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCPAID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCREATEDON)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.ID))
                );
                moneyIns.add(0, moneyIn); //adds new items to beginning of list
                cursor.moveToNext();
            }
        }
        cursor.close();
        return moneyIns;
    }

    public void addMoneyIn(TransactionsDb moneyIn) {
        newMoneyIn = new ContentValues();
        newMoneyIn.put(DbHelper.TRANSTYPE, moneyIn.getTransType());
        newMoneyIn.put(DbHelper.TRANSISCC, moneyIn.getTransIsCC());
        newMoneyIn.put(DbHelper.TRANSBDGTCAT, moneyIn.getTransBdgtCat());
        newMoneyIn.put(DbHelper.TRANSBDGTID, moneyIn.getTransBdgtId());
        newMoneyIn.put(DbHelper.TRANSAMT, moneyIn.getTransAmt());
        newMoneyIn.put(DbHelper.TRANSAMTINA, moneyIn.getTransAmtInA());
        newMoneyIn.put(DbHelper.TRANSAMTINOWING, moneyIn.getTransAmtInOwing());
        newMoneyIn.put(DbHelper.TRANSAMTINB, moneyIn.getTransAmtInB());
        newMoneyIn.put(DbHelper.TRANSAMTOUTA, moneyIn.getTransAmtOutA());
        newMoneyIn.put(DbHelper.TRANSAMTOUTOWING, moneyIn.getTransAmtOutOwing());
        newMoneyIn.put(DbHelper.TRANSAMTOUTB, moneyIn.getTransAmtOutB());
        newMoneyIn.put(DbHelper.TRANSTOACCTID, moneyIn.getTransToAcctId());
        newMoneyIn.put(DbHelper.TRANSTOACCTNAME, moneyIn.getTransToAcctName());
        newMoneyIn.put(DbHelper.TRANSTOISDEBT, moneyIn.getTransToIsDebt());
        newMoneyIn.put(DbHelper.TRANSTOISSAV, moneyIn.getTransToIsSav());
        newMoneyIn.put(DbHelper.TRANSFROMACCTID, moneyIn.getTransFromAcctId());
        newMoneyIn.put(DbHelper.TRANSFROMACCTNAME, moneyIn.getTransFromAcctName());
        newMoneyIn.put(DbHelper.TRANSFROMISDEBT, moneyIn.getTransFromIsDebt());
        newMoneyIn.put(DbHelper.TRANSFROMISSAV, moneyIn.getTransFromIsSav());
        newMoneyIn.put(DbHelper.TRANSBDGTPRIORITY, moneyIn.getTransBdgtPriority());
        newMoneyIn.put(DbHelper.TRANSBDGTWEEKLY, moneyIn.getTransBdgtWeekly());
        newMoneyIn.put(DbHelper.TRANSCCTOPAY, moneyIn.getTransCCToPay());
        newMoneyIn.put(DbHelper.TRANSCCPAID, moneyIn.getTransCCPaid());
        newMoneyIn.put(DbHelper.TRANSCREATEDON, moneyIn.getTransCreatedOn());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.TRANSACTIONS_TABLE_NAME, null, newMoneyIn);
    }

    public void updateMoneyIn(TransactionsDb moneyIn) {
        updateMoneyIn = new ContentValues();
        updateMoneyIn.put(DbHelper.TRANSTYPE, moneyIn.getTransType());
        updateMoneyIn.put(DbHelper.TRANSISCC, moneyIn.getTransIsCC());
        updateMoneyIn.put(DbHelper.TRANSBDGTCAT, moneyIn.getTransBdgtCat());
        updateMoneyIn.put(DbHelper.TRANSBDGTID, moneyIn.getTransBdgtId());
        updateMoneyIn.put(DbHelper.TRANSAMT, moneyIn.getTransAmt());
        updateMoneyIn.put(DbHelper.TRANSAMTINA, moneyIn.getTransAmtInA());
        updateMoneyIn.put(DbHelper.TRANSAMTINOWING, moneyIn.getTransAmtInOwing());
        updateMoneyIn.put(DbHelper.TRANSAMTINB, moneyIn.getTransAmtInB());
        updateMoneyIn.put(DbHelper.TRANSAMTOUTA, moneyIn.getTransAmtOutA());
        updateMoneyIn.put(DbHelper.TRANSAMTOUTOWING, moneyIn.getTransAmtOutOwing());
        updateMoneyIn.put(DbHelper.TRANSAMTOUTB, moneyIn.getTransAmtOutB());
        updateMoneyIn.put(DbHelper.TRANSTOACCTID, moneyIn.getTransToAcctId());
        updateMoneyIn.put(DbHelper.TRANSTOACCTNAME, moneyIn.getTransToAcctName());
        updateMoneyIn.put(DbHelper.TRANSTOISDEBT, moneyIn.getTransToIsDebt());
        updateMoneyIn.put(DbHelper.TRANSTOISSAV, moneyIn.getTransToIsSav());
        updateMoneyIn.put(DbHelper.TRANSFROMACCTID, moneyIn.getTransFromAcctId());
        updateMoneyIn.put(DbHelper.TRANSFROMACCTNAME, moneyIn.getTransFromAcctName());
        updateMoneyIn.put(DbHelper.TRANSFROMISDEBT, moneyIn.getTransFromIsDebt());
        updateMoneyIn.put(DbHelper.TRANSFROMISSAV, moneyIn.getTransFromIsSav());
        updateMoneyIn.put(DbHelper.TRANSBDGTPRIORITY, moneyIn.getTransBdgtPriority());
        updateMoneyIn.put(DbHelper.TRANSBDGTWEEKLY, moneyIn.getTransBdgtWeekly());
        updateMoneyIn.put(DbHelper.TRANSCCTOPAY, moneyIn.getTransCCToPay());
        updateMoneyIn.put(DbHelper.TRANSCCPAID, moneyIn.getTransCCPaid());
        updateMoneyIn.put(DbHelper.TRANSCREATEDON, moneyIn.getTransCreatedOn());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyIn.getId())};
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, updateMoneyIn, DbHelper.ID + "=?", args);
    }

    public void deleteMoneyIn(TransactionsDb moneyIn) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyIn.getId())};
        db.delete(DbHelper.TRANSACTIONS_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<TransactionsDb> getMoneyOuts() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.TRANSACTIONS_TABLE_NAME + " WHERE " + DbHelper.TRANSTYPE + " = 'out'", null);
        List<TransactionsDb> moneyOuts = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    TransactionsDb moneyOut = new TransactionsDb(
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
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISDEBT)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSTOISSAV)),
                            cursor.getLong(cursor.getColumnIndex(DbHelper.TRANSFROMACCTID)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMACCTNAME)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISDEBT)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSFROMISSAV)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTPRIORITY)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSBDGTWEEKLY)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCTOPAY)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCCPAID)),
                            cursor.getString(cursor.getColumnIndex(DbHelper.TRANSCREATEDON)),
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

    public void addMoneyOut(TransactionsDb moneyOut) {
        newMoneyOut = new ContentValues();
        newMoneyOut.put(DbHelper.TRANSTYPE, moneyOut.getTransType());
        newMoneyOut.put(DbHelper.TRANSISCC, moneyOut.getTransIsCC());
        newMoneyOut.put(DbHelper.TRANSBDGTCAT, moneyOut.getTransBdgtCat());
        newMoneyOut.put(DbHelper.TRANSBDGTID, moneyOut.getTransBdgtId());
        newMoneyOut.put(DbHelper.TRANSAMT, moneyOut.getTransAmt());
        newMoneyOut.put(DbHelper.TRANSAMTINA, moneyOut.getTransAmtInA());
        newMoneyOut.put(DbHelper.TRANSAMTINOWING, moneyOut.getTransAmtInOwing());
        newMoneyOut.put(DbHelper.TRANSAMTINB, moneyOut.getTransAmtInB());
        newMoneyOut.put(DbHelper.TRANSAMTOUTA, moneyOut.getTransAmtOutA());
        newMoneyOut.put(DbHelper.TRANSAMTOUTOWING, moneyOut.getTransAmtOutOwing());
        newMoneyOut.put(DbHelper.TRANSAMTOUTB, moneyOut.getTransAmtOutB());
        newMoneyOut.put(DbHelper.TRANSTOACCTID, moneyOut.getTransToAcctId());
        newMoneyOut.put(DbHelper.TRANSTOACCTNAME, moneyOut.getTransToAcctName());
        newMoneyOut.put(DbHelper.TRANSTOISDEBT, moneyOut.getTransToIsDebt());
        newMoneyOut.put(DbHelper.TRANSTOISSAV, moneyOut.getTransToIsSav());
        newMoneyOut.put(DbHelper.TRANSFROMACCTID, moneyOut.getTransFromAcctId());
        newMoneyOut.put(DbHelper.TRANSFROMACCTNAME, moneyOut.getTransFromAcctName());
        newMoneyOut.put(DbHelper.TRANSFROMISDEBT, moneyOut.getTransFromIsDebt());
        newMoneyOut.put(DbHelper.TRANSFROMISSAV, moneyOut.getTransFromIsSav());
        newMoneyOut.put(DbHelper.TRANSBDGTPRIORITY, moneyOut.getTransBdgtPriority());
        newMoneyOut.put(DbHelper.TRANSBDGTWEEKLY, moneyOut.getTransBdgtWeekly());
        newMoneyOut.put(DbHelper.TRANSCCTOPAY, moneyOut.getTransCCToPay());
        newMoneyOut.put(DbHelper.TRANSCCPAID, moneyOut.getTransCCPaid());
        newMoneyOut.put(DbHelper.TRANSCREATEDON, moneyOut.getTransCreatedOn());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.TRANSACTIONS_TABLE_NAME, null, newMoneyOut);
    }

    public void updateMoneyOut(TransactionsDb moneyOut) {
        updateMoneyOut = new ContentValues();
        updateMoneyOut.put(DbHelper.TRANSTYPE, moneyOut.getTransType());
        updateMoneyOut.put(DbHelper.TRANSISCC, moneyOut.getTransIsCC());
        updateMoneyOut.put(DbHelper.TRANSBDGTCAT, moneyOut.getTransBdgtCat());
        updateMoneyOut.put(DbHelper.TRANSBDGTID, moneyOut.getTransBdgtId());
        updateMoneyOut.put(DbHelper.TRANSAMT, moneyOut.getTransAmt());
        updateMoneyOut.put(DbHelper.TRANSAMTINA, moneyOut.getTransAmtInA());
        updateMoneyOut.put(DbHelper.TRANSAMTINOWING, moneyOut.getTransAmtInOwing());
        updateMoneyOut.put(DbHelper.TRANSAMTINB, moneyOut.getTransAmtInB());
        updateMoneyOut.put(DbHelper.TRANSAMTOUTA, moneyOut.getTransAmtOutA());
        updateMoneyOut.put(DbHelper.TRANSAMTOUTOWING, moneyOut.getTransAmtOutOwing());
        updateMoneyOut.put(DbHelper.TRANSAMTOUTB, moneyOut.getTransAmtOutB());
        updateMoneyOut.put(DbHelper.TRANSTOACCTID, moneyOut.getTransToAcctId());
        updateMoneyOut.put(DbHelper.TRANSTOACCTNAME, moneyOut.getTransToAcctName());
        updateMoneyOut.put(DbHelper.TRANSTOISDEBT, moneyOut.getTransToIsDebt());
        updateMoneyOut.put(DbHelper.TRANSTOISSAV, moneyOut.getTransToIsSav());
        updateMoneyOut.put(DbHelper.TRANSFROMACCTID, moneyOut.getTransFromAcctId());
        updateMoneyOut.put(DbHelper.TRANSFROMACCTNAME, moneyOut.getTransFromAcctName());
        updateMoneyOut.put(DbHelper.TRANSFROMISDEBT, moneyOut.getTransFromIsDebt());
        updateMoneyOut.put(DbHelper.TRANSFROMISSAV, moneyOut.getTransFromIsSav());
        updateMoneyOut.put(DbHelper.TRANSBDGTPRIORITY, moneyOut.getTransBdgtPriority());
        updateMoneyOut.put(DbHelper.TRANSBDGTWEEKLY, moneyOut.getTransBdgtWeekly());
        updateMoneyOut.put(DbHelper.TRANSCCTOPAY, moneyOut.getTransCCToPay());
        updateMoneyOut.put(DbHelper.TRANSCCPAID, moneyOut.getTransCCPaid());
        updateMoneyOut.put(DbHelper.TRANSCREATEDON, moneyOut.getTransCreatedOn());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyOut.getId())};
        db.update(DbHelper.TRANSACTIONS_TABLE_NAME, updateMoneyOut, DbHelper.ID + "=?", args);
    }

    public void deleteMoneyOut(TransactionsDb moneyOut) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(moneyOut.getId())};
        db.delete(DbHelper.TRANSACTIONS_TABLE_NAME, DbHelper.ID + "=?", args);
    }

    public List<CurrentDb> getCurrent() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.CURRENT_TABLE_NAME, null);
        List<CurrentDb> currents = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CurrentDb current = new CurrentDb(
                        //cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTACCOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTB)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTA)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CURRENTOWINGA)),
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
        //newCurrent.put(DbHelper.CURRENTACCOUNT, current.getCurrentAccount());
        newCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        newCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        newCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        newCurrent.put(DbHelper.LASTPAGEID, current.getLastPageId());
        newCurrent.put(DbHelper.LASTDATE, current.getLastDate());
        db = dbHelper.getWritableDatabase();
        db.insert(DbHelper.CURRENT_TABLE_NAME, null, newCurrent);
    }

    public void updateCurrent(CurrentDb current) {
        updateCurrent = new ContentValues();
        //updateCurrent.put(DbHelper.CURRENTACCOUNT, current.getCurrentAccount());
        updateCurrent.put(DbHelper.CURRENTB, current.getCurrentB());
        updateCurrent.put(DbHelper.CURRENTA, current.getCurrentA());
        updateCurrent.put(DbHelper.CURRENTOWINGA, current.getCurrentOwingA());
        updateCurrent.put(DbHelper.LASTPAGEID, current.getLastPageId());
        updateCurrent.put(DbHelper.LASTDATE, current.getLastDate());
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.update(DbHelper.CURRENT_TABLE_NAME, updateCurrent, DbHelper.ID + "=?", args);
    }

    public void deleteCurrent(CurrentDb current) {
        db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(current.getId())};
        db.delete(DbHelper.CURRENT_TABLE_NAME, DbHelper.ID + "=?", args);
    }


    //LIST RETRIEVAL FORMULAS

    public List<AccountsDb> getDebts() {
        List<AccountsDb> debts = new ArrayList<>();
        for (AccountsDb a : getAccounts()) {
            if (a.getAcctIsDebt().equals("Y")) {
                debts.add(a);
            }
        }
        return debts;
    }

    public List<AccountsDb> getSavings() {
        List<AccountsDb> savings = new ArrayList<>();
        for (AccountsDb a : getAccounts()) {
            if (a.getAcctIsSav().equals("Y")) {
                savings.add(a);
            }
        }
        return savings;
    }

    public List<TransactionsDb> getCashTrans() {
        List<TransactionsDb> cashTrans = new ArrayList<>();
        for (TransactionsDb m2 : getMoneyOuts()) {
            if (m2.getTransType().equals("out") && m2.getTransIsCC().equals("N")) {
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

    public List<TransactionsDb> getCCTransToPay() {
        List<TransactionsDb> ccTransToPay = new ArrayList<>();
        for (TransactionsDb m4 : getMoneyOuts()) {
            if (m4.getTransIsCC().equals("Y") && m4.getTransCCPaid().equals("N")) {
                ccTransToPay.add(m4);
            }
        }
        return ccTransToPay;
    }

    public List<BudgetDb> getWeeklyLimits() {
        List<BudgetDb> weeklyLimits = new ArrayList<>();
        for (BudgetDb e : getExpense()) {
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
            List<String> allYears = new ArrayList<>(getMoneyOuts().size());
            for (TransactionsDb m : getMoneyOuts()) {
                allYears.add(m.getTransCreatedOn());
            }
            for (TransactionsDb m2 : getMoneyIns()) {
                allYears.add(m2.getTransCreatedOn());
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


    // DATA RETRIEVAL FORMULAS

    public String findMoneyInIsDebt(long long1) {
        //long1 = moneyInToAcctId
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                moneyInIsDebt = a.getAcctIsDebt();
            }
        }
        return moneyInIsDebt;
    }

    public String findMoneyInIsSav(long long1) {
        //long1 = moneyInToAcctId
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                moneyInIsSav = a.getAcctIsSav();
            }
        }
        return moneyInIsSav;
    }

    /*public long findMoneyInDebtId(long long1) {
        //long1 = moneyInAcctId
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                moneyInDebtId = a.getDebtId();
            }
        }
        return moneyInDebtId;
    }*/

    /*public long findMoneyInSavId(long long1) {
        //long1 = moneyInAcctId
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                moneyInSavId = a.getSavId();
            }
        }
        return moneyInSavId;
    }*/

    public Double retrieveCurrentDebtAmtOwing(long long1) {
        //long1 = debtId
        currentDebtAmtOwing = 0.0;
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                currentDebtAmtOwing = a.getAcctBal();
            }
        }
        return currentDebtAmtOwing;
    }

    public Double retrieveCurrentSavAmt(long long1) {
        //long1 = savId
        currentSavAmt = 0.0;
        for (AccountsDb a : getAccounts()) {
            if (a.getId() == long1) {
                currentSavAmt = a.getAcctBal();
            }
        }
        return currentSavAmt;
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
        totalDebt = 0.0;
        for (AccountsDb d : getDebts()) {
            if (d.getAcctIsDebt().equals("Y")) {
                debtAmountList.add(d.getAcctBal());
            }
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

    /*public Long findLatestDebtId() {
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
    }*/

    public Double sumTotalSavings() {
        List<Double> savingsAmountList = new ArrayList<>(getSavings().size());
        totalSavings = 0.0;
        for (AccountsDb d : getSavings()) {
            if (d.getAcctIsSav().equals("Y")) {
                savingsAmountList.add(d.getAcctBal());
            }
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

    /*public Long findLatestSavId() {
        List<Long> savIds = new ArrayList<>(getSavings().size());
        for (SavingsDb s : getSavings()) {
            savIds.add(s.getId());
        }
        savId = null;
        if (savIds.size() == 0) {
            savId = null;
        } else {
            savId = Collections.max(savIds);
        }
        return savId;
    }*/

    public Double sumTotalAExpenses() {
        List<Double> expensesAmountListA = new ArrayList<>();
        for (BudgetDb e : getExpense()) {
            if (e.getBdgtPriority().equals("A")) {
                expensesAmountListA.add(e.getBdgtAnnPayt());
            }
        }
        for (AccountsDb d : getDebts()) {
            if (d.getAcctIsDebt().equals("Y") || d.getAcctIsSav().equals("Y")) {
                expensesAmountListA.add(d.getAcctAnnPaytsTo());
            }
        }
        /*for (AccountsDb s : getSavings()) {
            expensesAmountListA.add(s.getSavingsAnnualPayments());
        }*/
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

    public Double sumTotalExpenses() {
        List<Double> expensesAmountList = new ArrayList<>(getExpense().size());
        for (BudgetDb e : getExpense()) {
            expensesAmountList.add(e.getBdgtAnnPayt());
        }
        for (AccountsDb d : getDebts()) {
            if (d.getAcctIsDebt().equals("Y") || d.getAcctIsSav().equals("Y")) {
                expensesAmountList.add(d.getAcctAnnPaytsTo());
            }
        }
        /*for (SavingsDb s : getSavings()) {
            expensesAmountList.add(s.getSavingsAnnualPayments());
        }*/
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
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
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
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
    }


    /*public Double transfersToDebtThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransToAcctId() == long1 && t.getTransFromAcctId() == 1 && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
    }*/

    /*public Double transfersFromDebtThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransFromAcctId() == long1 && t.getTransToAcctId() == 1 && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
    }*/

    /*public Double transfersToSavThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransToSavid() == long1 && t.getTransFromAcct().equals(getString(R.string.main_account)) && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
    }*/

    /*public Double transfersFromSavThisYear(long long1, List<String> list1) {
        //long1 = debt or sav Id
        //list1 = general.last365Days();
        List<Double> transfersThisYear = new ArrayList<>();
        for (TransactionsDb t : getTransfers()) {
            if (t.getTransFromSavid() == long1 && t.getTransToAcct().equals(getString(R.string.main_account)) && list1.contains(t.getTransCreatedOn())) {
                transfersThisYear.add(t.getTransAmt());
            }
        }
        transThisYear = 0.0;
        if (transfersThisYear.size() == 0) {
            transThisYear = 0.0;
        } else {
            for (Double dbl : transfersThisYear) {
                transThisYear += dbl;
            }
        }

        return transThisYear;
    }*/

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
                } else if (dbl3 == 0) { //if B has no money, A goes negative by whole amount
                    moneyOutA = dbl1;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutA = amtMissing;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutA = dbl2;
                } else if (dbl3 == 0) { //if B has no money, A goes negative by the rest
                    moneyOutA = dbl1;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutA = dbl1 - dbl3;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutA = 0.0;
            } else if (dbl3 == 0) { //if B has no money, A covers it but is owed for it
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
                } else if (dbl3 == 0) { //if B has no money, A goes negative by whole amount
                    moneyOutOwing = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutOwing = 0.0;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutOwing = 0.0;
                } else if (dbl3 == 0) { //if B has no money, A goes negative by the rest
                    moneyOutOwing = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutOwing = 0.0;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutOwing = 0.0;
            } else if (dbl3 == 0) { //if B has no money, A covers it but is owed for it
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
                } else if (dbl3 == 0) { //if B has no money, A goes negative by whole amount
                    moneyOutB = 0.0;
                } else { //if B can cover part of the purchase, it pays what it can and A goes negative for rest
                    amtMissing = dbl1 - dbl3;
                    moneyOutB = dbl3;
                }
            } else { //if A can cover part of the purchase
                amtMissing = dbl1 - dbl2;
                if (dbl3 >= amtMissing) { //if B can cover the rest, it does
                    moneyOutB = amtMissing;
                } else if (dbl3 == 0) { //if B has no money, A goes negative by the rest
                    moneyOutB = 0.0;
                } else { //if B can cover part of the rest, it pays what it can and A goes negative for rest
                    moneyOutB = dbl3;
                }
            }
        } else if (str1.equals("B")) {
            if (dbl3 >= dbl1) { //if B can cover the purchase, it does
                moneyOutB = dbl1;
            } else if (dbl3 == 0) { //if B has no money, A covers it but is owed for it
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
        for (BudgetDb e : getExpense()) {
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

    /*public void updateAllAcctBal(Double dbl1, Double dbl2, Double dbl3) {
        //dbl1 = retrieveCurrentAcctBal()
        //dbl2 = retrieveCurrentDebtAmtOwing
        //dbl3 = retrieveCurrentSavAmt
        for(AccountsDb a : getAccounts()) {
            acctId = a.getId();
            if(a.getId() == 1) {
                newAcctBal = dbl1;
            } else if(a.getDebtId() != 0) {
                newAcctBal = -dbl2;
            } else if(a.getSavId() != 0) {
                newAcctBal = dbl3;
            }
        }

        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME, null);
        db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTBAL + " = " + newAcctBal + " WHERE " + DbHelper.ID + " = " + acctId);
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
    }*/

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
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.ACCTANNPAYTSTO + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ID + " = " + long1, null);
        annPaytFromDb = cursor.getDouble(0);
        if (dbl1 - dbl2 > annPaytFromDb) {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + dbl1 + " - " + dbl2);
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    /*public void updateDebtRecReTransfer(Long long1, Double dbl1, Double dbl2) {
        //long1 = debtId or SavId
        //dbl1 = transfersToAcctThisYear(long1)
        //dbl2 = transfersFromAcctThisYear(long1)
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.ACCTANNPAYTSTO + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ID + " = " + long1, null);
        debtAnnPaytFromDb = cursor.getDouble(0);
        if(dbl1 - dbl2 <= debtAnnPaytFromDb) {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + DbHelper.ACCTANNPAYTSTO);
        } else {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + dbl1 + " - " + dbl2);
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();

    }*/

    /*public void updateSavRecReTransfer(Long long1, Double dbl1, Double dbl2) {
        //long1 = debtId
        //dbl1 = transfersToSavThisYear(long1)
        //dbl2 = transfersFromSavThisYear(long1)
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.ACCTANNPAYTSTO + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ID + " = " + long1, null);
        debtAnnPaytFromDb = cursor.getDouble(0);
        if(dbl1 - dbl2 <= debtAnnPaytFromDb) {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + DbHelper.ACCTANNPAYTSTO);
        } else {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + dbl1 + " - " + dbl2);
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();

        for (SavingsDb s : getSavings()) {
            if (s.getId() == long1) {
                savAnnPaytFromDb = s.getSavingsAnnualPayments();
                if (dbl1 - dbl2 <= savAnnPaytFromDb) {
                    s.setSavingsAnnualPayments(savAnnPaytFromDb);
                } else {
                    s.setSavingsAnnualPayments(dbl1 - dbl2);
                }
                updateSavings(s);
            }
        }
    }*/

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

    /*public void updateSavRecPlusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from whichever source
        //dbl2 = currSavAmt from whichever source
        //lg1 = savId from whichever source
        db = dbHelper.getWritableDatabase();

        newSavAmt = dbl2 + dbl1;
        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newSavAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

    /*public void updateDebtRecPlusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from entry or from tag
        //dbl2 = currDebtAmt from whichever source
        //lg1 = debtId from whichever source
        db = dbHelper.getWritableDatabase();

        newDebtAmt = dbl2 + dbl1;
        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newDebtAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

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

    /*public void updateSavRecMinusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from whichever source
        //dbl2 = currSavAmt from whichever source
        //lg1 = savId from whichever source
        db = dbHelper.getWritableDatabase();

        newSavAmt = dbl2 - dbl1;
        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newSavAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

    /*public void updateDebtRecMinusPt1(Double dbl1, Double dbl2, long lg1) {
        //dbl1 = moneyInAmt from entry or from tag
        //dbl2 = currDebtAmt from whichever source
        //lg1 = debtId from whichever source
        db = dbHelper.getWritableDatabase();

        newDebtAmt = dbl2 - dbl1;

        CV = new ContentValues();
        CV.put(DbHelper.ACCTBAL, newDebtAmt);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

    public void updateRecPt2(String str1, long lg1) {
        //str1 = endDate from calcEndDate method
        //lg1 = acctId from whichever source
        db = dbHelper.getWritableDatabase();

        CV = new ContentValues();
        CV.put(DbHelper.ACCTENDDATE, str1);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }

    /*public void updateSavRecPt2(String str1, long lg1) {
        //str1 = calcSavDate from method
        //lg1 = savId from whichever source
        db = dbHelper.getWritableDatabase();

        CV = new ContentValues();
        CV.put(DbHelper.ENDDATE, str1);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

    /*public void updateDebtRecPt2(String str1, long lg1) {
        //str1 = debtEnd from calcDebtEnd method
        //lg1 = debtId from whichever source
        db = dbHelper.getWritableDatabase();

        CV = new ContentValues();
        CV.put(DbHelper.ENDDATE, str1);
        db.update(DbHelper.ACCOUNTS_TABLE_NAME, CV, DbHelper.ID + "=" + lg1, null);
        db.close();
    }*/

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

    public void updateAvailBalPlus(Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5, Double dbl6) {
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

    public void updateAvailBalMinus(Double dbl1, Double dbl2, Double dbl3, Double dbl4, Double dbl5, Double dbl6) {
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
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.ACCTBAL + ", " + DbHelper.ACCTDEBTTOPAY + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ACCTDEBTTOPAY + " > 0", null);
        db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTBAL + " = " + DbHelper.ACCTBAL + " - " + DbHelper.ACCTDEBTTOPAY);
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /*public void updateAllDebtBudget(Double dbl1, Double dbl2) {
        //dbl1 = transfersToDebtThisYear(long1)
        //dbl2 = transfersFromDebtThisYear(long1)
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        cursor = db.rawQuery("SELECT " + DbHelper.ACCTANNPAYTSTO + " FROM " + DbHelper.ACCOUNTS_TABLE_NAME , null);
        annPaytFromDb = cursor.getDouble(0);
        if(dbl1 - dbl2 <= annPaytFromDb) {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + DbHelper.ACCTANNPAYTSTO);
        } else {
            db.execSQL("UPDATE " + DbHelper.ACCOUNTS_TABLE_NAME + " SET " + DbHelper.ACCTANNPAYTSTO + " = " + dbl1 + " - " + dbl2);
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();

        List<Long> allDebtIds = new ArrayList<>();
        for (DebtDb d : getDebts()) {
            allDebtIds.add(d.getId());
        }
        for (long l : allDebtIds) {
            updateDebtRecReTransfer(l, dbl1, dbl2);
        }
    }

    public void updateAllSavBudget(Double dbl1, Double dbl2) {
        //dbl1 = transfersToSavThisYear(long1)
        //dbl2 = transfersFromSavThisYear(long1)
        List<Long> allSavIds = new ArrayList<>();
        for (SavingsDb s : getSavings()) {
            allSavIds.add(s.getId());
        }
        for (long l : allSavIds) {
            updateSavRecReTransfer(l, dbl1, dbl2);
        }
    }*/

     /*public void updateAllIncBudget() {
        List<Long> allIncIds = new ArrayList<>();
        for(IncomeBudgetDb i : getIncomes()) {
            allIncIds.add(i.getId());
        }
        for(long l : allIncIds) {
            updateIncAnnAmt(l);
        }
    }*/

    /*public void updateAllExpBudget() {
        List<Long> allExpIds = new ArrayList<>();
        for(ExpenseBudgetDb e : getExpense()) {
            allExpIds.add(e.getId());
        }
        for(long l : allExpIds) {
            updateExpAnnAmt(l);
        }
    }*/

    /*public Double retrieveAPercentage() {
        List<Double> aList = new ArrayList<>(getExpense().size());
        for (ExpenseBudgetDb e : getExpense()) {
            if (e.getExpensePriority().equals("A")) {
                aList.add(e.getExpenseAAnnualAmount());
            }
        }
        for(DebtDb d : getDebts()) {
            aList.add(d.getDebtAnnualPayments());
        }
        for(SavingsDb s : getSavings()) {
            aList.add(s.getSavingsAnnualPayments());
        }
        totalAExpenses = 0.0;
        if (aList.size() == 0) {
            totalAExpenses = 0.0;
        } else {
            for (Double dbl : aList) {
                totalAExpenses += dbl;
            }
        }
        percentA = totalAExpenses / sumTotalIncome();

        return percentA;
    }*/

    /*public Double retrieveBPercentage() {
        retrieveAPercentage();
        percentB = 1 - percentA;

        return percentB;
    }*/

    /*public Double retrieveToPayBTotal() {
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
    }*/

    /*public Long findLatestSetUpId() {
        List<Long> setUpIds = new ArrayList<>(getSetUp().size());
        for (SetUpDb s : getSetUp()) {
            setUpIds.add(s.getId());
        }
        setUpId = null;
        if (setUpIds.size() == 0) {
            setUpId = null;
        } else {
            setUpId = Collections.max(setUpIds);
        }
        return setUpId;
    }*/

    /*public Double retrieveStartingBalance() {
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
    }*/

    /*public int getDebtCount() {
        debtCount = getDebts().size();
        return debtCount;
    }*/

     /*public Long findLatestExpenseId() {
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
    }*/

     /*public Long findLatestIncomeId() {
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
    }*/

      /*public boolean checkIfAPoss(Double dbl1) {
        //dbl1 = moneyOutAmt
        aPoss = true;
        if (retrieveCurrentAccountBalance() - dbl1 < 0) {
            aPoss = false;
        } else {
            aPoss = true;
        }

        return aPoss;
    }*/

    /*public boolean checkIfBPoss(Double dbl1) {
        //dbl1 = moneyOutAmt
        bPoss = true;

        if (retrieveCurrentB() - dbl1 < 0) {
            bPoss = false;
        } else {
            bPoss = true;
        }

        return bPoss;
    }*/
}
