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
    public static final String ACCTDEBTSAV = "acctDebtSav";
    public static final String ACCTMAX = "acctMax";
    public static final String ACCTINTRATE = "acctIntRate";
    public static final String ACCTPAYTSTO = "acctPaytsTo";
    public static final String ACCTANNPAYTSTO = "acctAnnPaytsTo";
    public static final String ACCTENDDATE = "acctEndDate";
    public static final String ACCTDEBTTOPAY = "acctDebtToPay";

    public static final String BUDGET_TABLE_NAME = "budget";
    public static final String BDGTCAT = "bdgtCat";
    public static final String BDGTPAYTAMT = "bdgtPaytAmt";
    public static final String BDGTEXPINC = "bdgtExpInc";
    public static final String BDGTPAYTFRQ = "bdgtPaytFrq";
    public static final String BDGTANNPAYT = "bdgtAnnPayt";
    public static final String BDGTPRIORITY = "bdgtPriority";
    public static final String BDGTWEEKLY = "bdgtWeekly";

    public static final String CURRENT_TABLE_NAME = "current";
    public static final String CURRENTA = "currentA";
    public static final String CURRENTOWINGA = "currentOwingA";
    public static final String CURRENTB = "currentB";
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
    public static final String TRANSTODEBTSAV = "transToDebtSav";
    public static final String TRANSFROMACCTID = "transFromAcctId";
    public static final String TRANSFROMACCTNAME = "transFromAcctName";
    public static final String TRANSFROMDEBTSAV = "transFromDebtSav";
    public static final String TRANSBDGTPRIORITY = "transBdgtPriority";
    public static final String TRANSBDGTWEEKLY = "transBdgtWeekly";
    public static final String TRANSCCTOPAY = "transCCToPay";
    public static final String TRANSCCPAID = "transCCPaid";
    public static final String TRANSCREATEDON = "transCreatedOn";

    public static final String SET_UP_TABLE_NAME = "setUp";
    public static final String LATESTDONE = "latestDone";
    public static final String BALANCEAMOUNT = "balanceAmount";

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
            " acctDebtSav TEXT," +
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
            " bdgtExpInc TEXT," +
            " bdgtPaytFrq REAL," +
            " bdgtAnnPayt REAL," +
            " bdgtPriority TEXT," +
            " bdgtWeekly TEXT)";

    private static final String createCurrentQuery = "CREATE TABLE " + CURRENT_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " currentA REAL," +
            " currentOwingA REAL," +
            " currentB REAL," +
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
            " transToDebtSav TEXT," +
            " transFromAcctId INTEGER," +
            " transFromAcctName TEXT," +
            " transFromDebtSav TEXT," +
            " transBdgtPriority TEXT," +
            " transBdgtWeekly TEXT," +
            " transCCToPay TEXT," +
            " transCCPaid TEXT," +
            " transCreatedOn TEXT)";

    private static final String createSetUpQuery = "CREATE TABLE " + SET_UP_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY," +
            " latestDone TEXT," +
            " balanceAmount REAL)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createAccountsQuery);
        db.execSQL(createBudgetQuery);
        db.execSQL(createCurrentQuery);
        db.execSQL(createTransactionsQuery);
        db.execSQL(createSetUpQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
