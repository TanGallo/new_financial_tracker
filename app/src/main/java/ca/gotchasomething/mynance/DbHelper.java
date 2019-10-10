package ca.gotchasomething.mynance;
//DbHelper

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Mynance.db";
    private static final int DATABASE_VERSION = 1;
    public String databasePath = "";

    public static final String ID = "_id";

    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String ACCTNAME = "acctName";
    public static final String ACCTBAL = "acctBal";
    public static final String ACCTISDEBT = "acctIsDebt";
    public static final String ACCTISSAV = "acctIsSav";
    public static final String ACCTMAX = "acctMax";
    public static final String ACCTINTRATE = "acctIntRate";
    public static final String ACCTPAYTSTO = "acctPaytsTo";
    public static final String ACCTANNPAYTSTO = "acctAnnPaytsTo";
    public static final String ACCTENDDATE = "acctEndDate";
    public static final String ACCTDEBTTOPAY = "acctDebtToPay";

    public static final String BUDGET_TABLE_NAME = "budget";
    public static final String BDGTCAT = "bdgtCat";
    public static final String BDGTPAYTAMT = "bdgtPaytAmt";
    public static final String BDGTISEXP = "bdgtIsExp";
    public static final String BDGTISINC = "bdgtIsInc";
    public static final String BDGTPAYTFRQ = "bdgtPaytFrq";
    public static final String BDGTANNPAYT = "bdgtAnnPayt";
    public static final String BDGTPRIORITY = "bdgtPriority";
    public static final String BDGTWEEKLY = "bdgtWeekly";

    public static final String CURRENT_TABLE_NAME = "current";
    //public static final String CURRENTACCOUNT = "currentAccount";
    public static final String CURRENTB = "currentB";
    public static final String CURRENTA = "currentA";
    public static final String CURRENTOWINGA = "currentOwingA";
    public static final String LASTPAGEID = "lastPageId";
    public static final String LASTDATE = "lastDate";

    public static final String TRANSACTIONS_TABLE_NAME = "transactions";
    public static final String TRANSTYPE = "transType";
    public static final String TRANSISCC = "transIsCC";
    public static final String TRANSBDGTCAT = "transBdgtCat";
    public static final String TRANSBDGTID = "transBdgtId";
    public static final String TRANSAMT = "transAmt";
    public static final String TRANSAMTINA = "transAmtInA";
    public static final String TRANSAMTINOWING = "transAmtInOwing";
    public static final String TRANSAMTINB = "transAmtInB";
    public static final String TRANSAMTOUTA = "transAmtOutA";
    public static final String TRANSAMTOUTOWING = "transAmtOutOwing";
    public static final String TRANSAMTOUTB = "transAmtOutB";
    public static final String TRANSTOACCTID = "transToAcctId";
    public static final String TRANSTOACCTNAME = "transToAcctName";
    public static final String TRANSTOISDEBT = "transToIsDebt";
    public static final String TRANSTOISSAV = "transToIsSav";
    public static final String TRANSFROMACCTID = "transFromAcctId";
    public static final String TRANSFROMACCTNAME = "transFromAcctName";
    public static final String TRANSFROMISDEBT = "transFromIsDebt";
    public static final String TRANSFROMISSAV = "transFromIsSav";
    public static final String TRANSBDGTPRIORITY = "transBdgtPriority";
    public static final String TRANSBDGTWEEKLY = "transBdgtWeekly";
    public static final String TRANSCCTOPAY = "transCCToPay";
    public static final String TRANSCCPAID = "transCCPaid";
    public static final String TRANSCREATEDON = "transCreatedOn";

    public static final String SET_UP_TABLE_NAME = "setUp";
    public static final String LATESTDONE = "latestDone";
    public static final String BALANCEAMOUNT = "balanceAmount";

    /*public static final String INCOME_TABLE_NAME = "incomes";
    public static final String INCOMENAME = "incomeName";
    public static final String INCOMEAMOUNT = "incomeAmount";
    public static final String INCOMEFREQUENCY = "incomeFrequency";
    public static final String INCOMEANNUALAMOUNT = "incomeAnnualAmount";

    public static final String EXPENSES_TABLE_NAME = "expenses";
    public static final String EXPENSENAME = "expenseName";
    public static final String EXPENSEAMOUNT = "expenseAmount";
    public static final String EXPENSEFREQUENCY = "expenseFrequency";
    public static final String EXPENSEPRIORITY = "expensePriority";
    public static final String EXPENSEWEEKLY = "expenseWeekly";
    public static final String EXPENSEANNUALAMOUNT = "expenseAnnualAmount";*/

    /*public static final String DEBTS_TABLE_NAME = "debts";
    public static final String DEBTNAME = "debtName";
    public static final String DEBTLIMIT = "debtLimit";
    public static final String DEBTOWING = "debtOwing";
    public static final String DEBTRATE = "debtRate";
    public static final String DEBTPAYMENTS = "debtPayments";
    public static final String DEBTANNUALPAYMENTS = "debtAnnualPayments";
    public static final String DEBTEND = "debtEnd";
    public static final String DEBTTOPAY = "debtToPay";

    public static final String SAVINGS_TABLE_NAME = "savings";
    public static final String SAVINGSNAME = "savingsName";
    public static final String SAVINGSAMOUNT = "savingsAmount";
    public static final String SAVINGSGOAL = "savingsGoal";
    public static final String SAVINGSPAYMENTS = "savingsPayments";
    public static final String SAVINGSRATE = "savingsRate";
    public static final String SAVINGSANNUALPAYMENTS = "savingsAnnualPayments";
    public static final String SAVINGSDATE = "savingsDate";*/

    /*public static final String MONEY_IN_TABLE_NAME = "moneyIn";
    public static final String MONEYINCAT = "moneyInCat";
    public static final String MONEYINAMOUNT = "moneyInAmount";
    public static final String MONEYINA = "moneyInA";
    public static final String MONEYINOWING = "moneyInOwing";
    public static final String MONEYINB = "moneyInB";
    public static final String MONEYINTOACCT = "moneyInToAcct";
    public static final String MONEYINTONAME = "moneyInToName";
    public static final String MONEYINCREATEDON = "moneyInCreatedOn";
    public static final String INCREFKEYMI = "incRefKeyMI";

    public static final String MONEY_OUT_TABLE_NAME = "moneyOut";
    public static final String MONEYOUTCAT = "moneyOutCat";
    public static final String MONEYOUTPRIORITY = "moneyOutPriority";
    public static final String MONEYOUTWEEKLY = "moneyOutWeekly";
    public static final String MONEYOUTAMOUNT = "moneyOutAmount";
    public static final String MONEYOUTA = "moneyOutA";
    public static final String MONEYOUTOWING = "moneyOutOwing";
    public static final String MONEYOUTB = "moneyOutB";
    public static final String MONEYOUTPAYFROMID = "moneyOutPayFromId";
    public static final String MONEYOUTPAYFROMNAME = "moneyOutPayFromName";
    public static final String MONEYOUTCREATEDON = "moneyOutCreatedOn";
    public static final String MONEYOUTCC = "moneyOutCC";
    public static final String MONEYOUTDEBTCAT = "moneyOutDebtCat";
    public static final String MONEYOUTCHARGINGDEBTID = "moneyOutChargingDebtId";
    public static final String MONEYOUTTOPAY = "moneyOutToPay";
    public static final String MONEYOUTPAID = "moneyOutPaid";
    public static final String EXPREFKEYMO = "expRefKeyMO";*/

    /*public static final String TRANSFERS_TABLE_NAME = "transfers";
    public static final String TRANSFROMACCT = "transFromAcct";
    public static final String TRANSTOACCT = "transToAcct";
    public static final String TRANSFROMDEBTID = "transFromDebtId";
    public static final String TRANSFROMSAVID = "transFromSavId";
    public static final String TRANSTODEBTID = "transToDebtId";
    public static final String TRANSTOSAVID = "transToSavId";
    public static final String TRANSAMT = "transAmt";
    public static final String TRANSAMTOUTA = "transAmtOutA";
    public static final String TRANSAMTOUTOWING = "transAmtOutOwing";
    public static final String TRANSAMTOUTB = "transAmtOutB";
    public static final String TRANSAMTINA = "transAmtInA";
    public static final String TRANSAMTINOWING = "transAmtInOwing";
    public static final String TRANSAMTINB = "transAmtInB";
    public static final String TRANSCREATEDON = "transCreatedOn";*/

    //singleton pattern
    public static DbHelper instance = null;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    } //end of singleton pattern

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String createAccountsQuery = "CREATE TABLE " + ACCOUNTS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " acctName TEXT," +
            " acctBal REAL," +
            " acctIsDebt TEXT," +
            " acctIsSav TEXT," +
            " acctMax REAL," +
            " acctIntRate REAL," +
            " acctPaytsTo REAL," +
            " acctAnnPaytsTo REAL," +
            " acctEndDate TEXT," +
            " acctDebtToPay REAL)";

    private static final String createBudgetQuery = "CREATE TABLE " + BUDGET_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " bdgtCat TEXT," +
            " bdgtPaytAmt REAL," +
            " bdgtIsExp TEXT," +
            " bdgtIsInc TEXT," +
            " bdgtPaytFrq REAL," +
            " bdgtAnnPayt REAL," +
            " bdgtPriority TEXT," +
            " bdgtWeekly TEXT)";

    private static final String createCurrentQuery = "CREATE TABLE " + CURRENT_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            //" currentAccount REAL," +
            " currentB REAL," +
            " currentA REAL," +
            " currentOwingA REAL," +
            " lastPageId INTEGER," +
            " lastDate TEXT)";

    private static final String createTransactionsQuery = "CREATE TABLE " + TRANSACTIONS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " transType TEXT," +
            " transIsCC TEXT," +
            " transBdgtCat TEXT," +
            " transBdgtId INTEGER," +
            " transAmt REAL," +
            " transAmtInA REAL," +
            " transAmtInOwing REAL," +
            " transAmtInB REAL," +
            " transAmtOutA REAL," +
            " transAmtOutOwing REAL," +
            " transAmtOutB REAL," +
            " transToAcctId INTEGER," +
            " transToAcctName TEXT," +
            " transToIsDebt TEXT," +
            " transToIsSav TEXT," +
            " transFromAcctId INTEGER," +
            " transFromAcctName TEXT," +
            " transFromIsDebt TEXT," +
            " transFromIsSav TEXT," +
            " transBdgtPriority TEXT," +
            " transBdgtWeekly TEXT," +
            " transCCToPay TEXT," +
            " transCCPaid TEXT," +
            " transCreatedOn TEXT)";

    private static final String createSetUpQuery = "CREATE TABLE " + SET_UP_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " latestDone TEXT," +
            " balanceAmount REAL)";

    /*private static final String createExpensesQuery = "CREATE TABLE " + EXPENSES_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " expenseName TEXT," +
            " expenseAmount REAL," +
            " expenseFrequency REAL," +
            " expensePriority TEXT," +
            " expenseWeekly TEXT," +
            " expenseAnnualAmount REAL NOT NULL)";

    private static final String createIncomesQuery = "CREATE TABLE " + INCOME_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " incomeName TEXT," +
            " incomeAmount REAL," +
            " incomeFrequency REAL," +
            " incomeAnnualAmount REAL NOT NULL)";*/

    /*private static final String createDebtsQuery = "CREATE TABLE " + DEBTS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " debtName TEXT," +
            " debtLimit REAL," +
            " debtOwing REAL," +
            " debtRate REAL," +
            " debtPayments REAL," +
            " debtAnnualPayments REAL," +
            " debtEnd TEXT," +
            " debtToPay REAL)";

    private static final String createSavingsQuery = "CREATE TABLE " + SAVINGS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " savingsName TEXT," +
            " savingsAmount REAL," +
            " savingsGoal REAL," +
            " savingsPayments REAL," +
            " savingsRate REAL," +
            " savingsAnnualPayments REAL," +
            " savingsDate TEXT)";*/

    /*private static final String createMoneyInQuery = "CREATE TABLE " + MONEY_IN_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " moneyInCat TEXT," +
            " moneyInAmount REAL," +
            " moneyInA REAL," +
            " moneyInOwing REAL," +
            " moneyInB REAL," +
            " moneyInToAcct INTEGER," +
            " moneyInToName TEXT," +
            " moneyInCreatedOn TEXT," +
            " incRefKeyMI INTEGER)";

    private static final String createMoneyOutQuery = "CREATE TABLE " + MONEY_OUT_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " moneyOutCat TEXT," +
            " moneyOutPriority TEXT," +
            " moneyOutWeekly TEXT," +
            " moneyOutAmount REAL," +
            " moneyOutA REAL," +
            " moneyOutOwing REAL," +
            " moneyOutB REAL," +
            " moneyOutPayFromId INTEGER," +
            " moneyOutPayFromName TEXT," +
            " moneyOutCreatedOn TEXT," +
            " moneyOutCC TEXT," +
            " moneyOutDebtCat TEXT," +
            " moneyOutChargingDebtId INTEGER," +
            " moneyOutToPay INTEGER," +
            " moneyOutPaid INTEGER," +
            " expRefKeyMO INTEGER)";*/

    /*private static final String createTransfersQuery = "CREATE TABLE " + TRANSFERS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " transFromAcct TEXT," +
            " transToAcct TEXT," +
            " transFromDebtId INTEGER," +
            " transFromSavId INTEGER," +
            " transToDebtId INTEGER," +
            " transToSavId INTEGER," +
            " transAmt REAL," +
            " transAmtOutA REAL," +
            " transAmtOutOwing REAL," +
            " transAmtOutB REAL," +
            " transAmtInA REAL," +
            " transAmtInOwing REAL," +
            " transAmtInB REAL," +
            " transCreatedOn TEXT)";*/

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createAccountsQuery);
        db.execSQL(createBudgetQuery);
        db.execSQL(createCurrentQuery);
        db.execSQL(createTransactionsQuery);
        db.execSQL(createSetUpQuery);
        //db.execSQL(createIncomesQuery);
        //db.execSQL(createDebtsQuery);
        //db.execSQL(createSavingsQuery);
        //db.execSQL(createMoneyOutQuery);
        //db.execSQL(createTransfersQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
