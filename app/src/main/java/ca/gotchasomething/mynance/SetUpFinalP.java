package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.gotchasomething.mynance.data.CurrentDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
//import ca.gotchasomething.mynance.data.MoneyInDb;

public class SetUpFinalP extends AppCompatActivity {

    Button fin1EnterBtn;
    ContentValues fin1CV, fin1CV2, fin1CV3;
    CurrentDb fin1CurrDb;
    DbHelper fin1Helper;
    DbManager fin1DbMgr;
    Double fin1StartBal = 0.0, fin1MoneyInA, fin1MoneyInB;
    EditText fin1ET;
    General fin1Gen;
    Intent fin1ToSetUp;
    SQLiteDatabase fin1DB;
    TransactionsDb fin1MoneyInDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b8_slides_set_up_final_3);

        fin1DbMgr = new DbManager(this);
        fin1Gen = new General();

        fin1EnterBtn = findViewById(R.id.finalSlide3EnterButton);
        fin1ET = findViewById(R.id.finalSlide3ET);

        fin1EnterBtn.setOnClickListener(onClickfin1EnterButton);
    }

    View.OnClickListener onClickfin1EnterButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fin1StartBal = fin1Gen.dblFromET(fin1ET);

            if(fin1DbMgr.getIncomes().size() == 0) {
                fin1MoneyInA = fin1StartBal;
                fin1MoneyInB = 0.0;
            } else {
                fin1MoneyInA = fin1StartBal * (fin1DbMgr.sumTotalAExpenses() / fin1DbMgr.sumTotalIncome());
                fin1MoneyInB = fin1StartBal - fin1MoneyInA;
            }


            fin1Helper = new DbHelper(getApplicationContext());
            fin1DB = fin1Helper.getWritableDatabase();
            fin1CV = new ContentValues();
            fin1CV2 = new ContentValues();
            fin1CV3 = new ContentValues();
            fin1CV.put(DbHelper.LATESTDONE, "final");
            fin1CV.put(DbHelper.BALANCEAMOUNT, fin1StartBal);
            fin1CV2.put(DbHelper.ACCTBAL, fin1StartBal);
            fin1CV3.put(DbHelper.CURRENTB, fin1MoneyInB);
            fin1CV3.put(DbHelper.CURRENTA, fin1MoneyInA);
            fin1CV3.put(DbHelper.LASTDATE, fin1Gen.createTimestamp());
            fin1DB.update(DbHelper.SET_UP_TABLE_NAME, fin1CV, DbHelper.ID + "= '1'", null);
            fin1DB.update(DbHelper.ACCOUNTS_TABLE_NAME, fin1CV2, DbHelper.ID + "= '1'", null);
            fin1DB.update(DbHelper.CURRENT_TABLE_NAME, fin1CV3, DbHelper.ID + "= '1'", null);
            fin1DB.close();

            fin1MoneyInDb = new TransactionsDb(
                    "in",
                    "N/A",
                    getString(R.string.starting_balance),
                    0,
                    fin1StartBal,
                    fin1MoneyInA,
                    0.0,
                    fin1MoneyInB,
                    0.0,
                    0.0,
                    0.0,
                    1,
                    getString(R.string.main_account),
                    "N/A",
                    0,
                    "N/A",
                    "N/A",
                    "N/A",
                    "N/A",
                    "N/A",
                    "N/A",
                    fin1Gen.createTimestamp(),
                    0);

            fin1DbMgr.addTransactions(fin1MoneyInDb);

            fin1ToSetUp = new Intent(SetUpFinalP.this, LayoutSetUp.class);
            fin1ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(fin1ToSetUp);
        }
    };

}