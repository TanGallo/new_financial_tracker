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
import ca.gotchasomething.mynance.data.MoneyInDb;

public class SetUpFinalL extends AppCompatActivity {

    Button fin2EnterBtn;
    ContentValues fin2CV;
    CurrentDb fin2CurrDb;
    DbHelper fin2Helper;
    DbManager fin2DbMgr;
    Double fin2StartBal = 0.0, fin2MoneyInA, fin2MoneyInB;
    EditText fin2ET;
    General fin2Gen;
    Intent fin2ToSetUp;
    MoneyInDb fin2MoneyInDb;
    SQLiteDatabase fin2DB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b8_slides_set_up_final_3_land);

        fin2DbMgr = new DbManager(this);
        fin2Gen = new General();

        fin2EnterBtn = findViewById(R.id.finalSlide3EnterButton);
        fin2ET = findViewById(R.id.finalSlide3ET);

        fin2EnterBtn.setOnClickListener(onClickfin2EnterButton);
    }

    View.OnClickListener onClickfin2EnterButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fin2StartBal = fin2Gen.dblFromET(fin2ET);

            fin2Helper = new DbHelper(getApplicationContext());
            fin2DB = fin2Helper.getWritableDatabase();
            fin2CV = new ContentValues();
            fin2CV.put(DbHelper.LATESTDONE, "final");
            fin2CV.put(DbHelper.BALANCEAMOUNT, fin2StartBal);
            fin2DB.update(DbHelper.SET_UP_TABLE_NAME, fin2CV, DbHelper.ID + "= '1'", null);
            fin2DB.close();

            if(fin2DbMgr.getIncomes().size() == 0) {
                fin2MoneyInA = fin2StartBal;
                fin2MoneyInB = 0.0;
            } else {
                //fin2MoneyInA = fin2StartBal * fin2DbMgr.retrieveAPercentage();
                //fin2MoneyInB = fin2StartBal * fin2DbMgr.retrieveBPercentage();
                fin2MoneyInA = fin2StartBal * (fin2DbMgr.sumTotalAExpenses() / fin2DbMgr.sumTotalIncome());
                fin2MoneyInB = fin2StartBal - fin2MoneyInA;
            }

            fin2MoneyInDb = new MoneyInDb(
                    getString(R.string.starting_balance),
                    fin2StartBal,
                    fin2MoneyInA,
                    0.0,
                    fin2MoneyInB,
                    1,
                    getString(R.string.main_account),
                    fin2Gen.createTimestamp(),
                    1,
                    0);

            fin2DbMgr.addMoneyIn(fin2MoneyInDb);

            fin2CurrDb = new CurrentDb(
                    fin2StartBal,
                    fin2MoneyInB,
                    fin2MoneyInA,
                    0.0,
                    0,
                    0);

            fin2DbMgr.addCurrent(fin2CurrDb);

            fin2ToSetUp = new Intent(SetUpFinalL.this, LayoutSetUp.class);
            fin2ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(fin2ToSetUp);
        }
    };

}