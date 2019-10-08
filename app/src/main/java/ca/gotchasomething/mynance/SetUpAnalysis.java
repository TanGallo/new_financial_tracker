package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class SetUpAnalysis extends AppCompatActivity {

    Button ana1DoneAdjBtn;
    ContentValues ana1CV;
    DbHelper ana1Helper;
    DbManager ana1DBMgr;
    Double ana1SpendPercent2 = 0.0;
    General ana1Gen;
    ImageButton ana1AdjExpBtn, ana1AdjIncBtn, ana1AdjDebtsBtn, ana1AdjSavBtn;
    Intent ana1ToExp, ana1ToInc, ana1ToSetUp;
    LinearLayout ana1AdjLayout;
    NumberFormat ana1PerFor = NumberFormat.getPercentInstance();
    RelativeLayout ana1AdjIncLayout, ana1AdjExpLayout, ana1AdjDebtsLayout, ana1AdjSavLayout;
    String ana1SpendPercent = null, ana1SpendResStmt = null;
    SQLiteDatabase ana1DB;
    TextView ana1AnaResTV, ana1ResLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b6a_layout_analysis);

        ana1DBMgr = new DbManager(this);
        ana1Gen = new General();

        ana1AnaResTV = findViewById(R.id.ana1AnaResTV);
        ana1ResLabel = findViewById(R.id.ana1ResLabel);
        ana1AdjLayout = findViewById(R.id.ana1AdjLayout);
        ana1AdjIncBtn = findViewById(R.id.ana1AdjIncBtn);
        ana1AdjExpBtn = findViewById(R.id.ana1AdjExpBtn);
        ana1AdjDebtsBtn = findViewById(R.id.ana1AdjDebtsBtn);
        ana1AdjSavBtn = findViewById(R.id.ana1AdjSavBtn);
        ana1DoneAdjBtn = findViewById(R.id.ana1DoneAdjBtn);

        ana1AdjIncBtn.setOnClickListener(onClickAna1AdjIncBtn);
        ana1AdjExpBtn.setOnClickListener(onClickAna1AdjExpBtn);
        ana1AdjDebtsBtn.setOnClickListener(onClickAna1AdjDebtsBtn);
        ana1AdjSavBtn.setOnClickListener(onClickAna1AdjSavBtn);
        ana1DoneAdjBtn.setOnClickListener(onClickAna1DoneAdjBtn);

        ana1SpendResPara(ana1AnaResTV, ana1ResLabel);
    }

    public void ana1SpendResPara(TextView tv, TextView tv2) {
        //tv = statement textView
        //tv2 = additional textView re: whether or not adjustments necessary

        //ana1SpendPercent2 = ana1DBMgr.retrieveAPercentage();
        ana1SpendPercent2 = (ana1DBMgr.sumTotalAExpenses() / ana1DBMgr.sumTotalIncome());
        if(ana1SpendPercent2 == 0) {
            ana1SpendPercent = "0.0%";
        } else {
            ana1PerFor.setMinimumFractionDigits(1);
            ana1PerFor.setMaximumFractionDigits(1);
            ana1SpendPercent = ana1PerFor.format(ana1SpendPercent2);
        }

        ana1SpendResStmt = getString(R.string.ana_res_prt_1) + " " + ana1SpendPercent + " " + getString(R.string.ana_res_part_2);
        tv.setText(ana1SpendResStmt);

        if (ana1SpendPercent2 >= .910) {
            tv.setTextColor(Color.parseColor("#ffff4444"));
            tv2.setText(getString(R.string.should_adj));
            tv2.setTextColor(Color.parseColor("#ffff4444"));
            ana1AdjLayout.setVisibility(View.VISIBLE);
            /*ana1AdjIncLayout.setOnClickListener(onClickAna1AdjIncLayout);
            ana1AdjExpLayout.setOnClickListener(onClickAna1AdjExpLayout);
            ana1AdjDebtsLayout.setOnClickListener(onClickAna1AdjDebtsLayout);
            ana1AdjSavLayout.setOnClickListener(onClickAna1AdjSavLayout);*/
        } else if (ana1SpendPercent2 <= .909 && ana1SpendPercent2 >= .801) {
            tv.setTextColor(Color.parseColor("#ffff4444"));
            tv2.setText(getString(R.string.may_adj));
            tv2.setTextColor(Color.parseColor("#ffff4444"));
            ana1AdjLayout.setVisibility(View.VISIBLE);
            /*ana1AdjIncLayout.setOnClickListener(onClickAna1AdjIncLayout);
            ana1AdjExpLayout.setOnClickListener(onClickAna1AdjExpLayout);
            ana1AdjDebtsLayout.setOnClickListener(onClickAna1AdjDebtsLayout);
            ana1AdjSavLayout.setOnClickListener(onClickAna1AdjSavLayout);*/
        } else {
            tv.setTextColor(Color.parseColor("#03ac13"));
            tv2.setText(getString(R.string.no_adj_nec));
            tv2.setTextColor(Color.parseColor("#03ac13"));
            ana1AdjLayout.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickAna1AdjIncBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1ToExp = new Intent(SetUpAnalysis.this, AddIncomeList.class);
            ana1ToExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToExp);
        }
    };

    View.OnClickListener onClickAna1AdjExpBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1ToInc = new Intent(SetUpAnalysis.this, AddExpenseList.class);
            ana1ToInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToInc);
        }
    };

    View.OnClickListener onClickAna1AdjDebtsBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1ToExp = new Intent(SetUpAnalysis.this, AddDebtsList.class);
            ana1ToExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToExp);
        }
    };

    View.OnClickListener onClickAna1AdjSavBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1ToInc = new Intent(SetUpAnalysis.this, AddSavingsList.class);
            ana1ToInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToInc);
        }
    };

    View.OnClickListener onClickAna1DoneAdjBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1CV = new ContentValues();
            ana1CV.put(DbHelper.LATESTDONE, "analysis");
            ana1Helper = new DbHelper(getApplicationContext());
            ana1DB = ana1Helper.getWritableDatabase();
            ana1DB.update(DbHelper.SET_UP_TABLE_NAME, ana1CV, DbHelper.ID + "= '1'", null);
            ana1DB.close();

            ana1ToSetUp = new Intent(SetUpAnalysis.this, LayoutSetUp.class);
            ana1ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToSetUp);
        }
    };

}