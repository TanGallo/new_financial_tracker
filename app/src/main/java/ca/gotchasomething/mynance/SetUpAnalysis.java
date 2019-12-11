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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.github.mikephil.charting.charts.PieChart;

public class SetUpAnalysis extends MainNavigation {

    Button ana1DoneAdjBtn;
    ContentValues ana1CV;
    DbHelper ana1Helper;
    DbManager ana1DBMgr;
    Double ana1Reserved = 0.0, ana1ToSpend = 0.0;
    General ana1Gen;
    ImageButton ana1AdjExpBtn, ana1AdjIncBtn, ana1AdjDebtsBtn, ana1AdjSavBtn;
    int ana1Colour;
    Intent ana1ToExp, ana1ToInc, ana1ToSetUp;
    LinearLayout ana1AdjLayout;
    PieChart ana1PieChart;
    String ana1Recommendation = null;
    SQLiteDatabase ana1DB;
    TextView ana1AnaResTV, ana1ResLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b1a_layout_analysis);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        ana1DBMgr = new DbManager(this);
        ana1Gen = new General();

        ana1PieChart = findViewById(R.id.ana1PieChart);
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

        ana1Reserved = (ana1DBMgr.sumTotalAExpenses() / ana1DBMgr.sumTotalIncome());
        ana1ToSpend = 1 - ana1Reserved;

        if (ana1Reserved >= .910) {
            ana1Colour = Color.parseColor("#ffff4444"); //red
            ana1Recommendation = getString(R.string.should_adj);
            ana1AdjLayout.setVisibility(View.VISIBLE);
        } else if (ana1Reserved <= .909 && ana1Reserved >= .801) {
            ana1Colour = Color.parseColor("#ffc30b"); //yellow
            ana1Recommendation = getString(R.string.may_adj);
            ana1AdjLayout.setVisibility(View.VISIBLE);
        } else if(ana1Reserved <= .800) {
            ana1Colour = Color.parseColor("#5dbb63"); //light green
            ana1Recommendation = getString(R.string.no_adj_nec);
            ana1AdjLayout.setVisibility(View.GONE);
        }

        ana1Gen.buildLimitsPieChart(
                ana1Gen.convertDblToFloat(ana1Reserved),
                ana1Gen.convertDblToFloat(ana1ToSpend),
                ana1PieChart,
                ana1Colour,
                Color.parseColor("#83878b")); //gray

        ana1DBMgr.spendResPara(
                ana1AnaResTV,
                ana1ResLabel,
                ana1Recommendation,
                getString(R.string.ana_res_prt_1),
                getString(R.string.ana_res_part_2),
                ana1Reserved,
                ana1Colour);

        if (ana1Reserved > 1) {
            ana1PieChart.setVisibility(View.GONE);
        } else {
            ana1PieChart.setVisibility(View.VISIBLE);
        }
    }

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
            ana1ToExp = new Intent(SetUpAnalysis.this, LayoutDebt.class);
            ana1ToExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(ana1ToExp);
        }
    };

    View.OnClickListener onClickAna1AdjSavBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ana1ToInc = new Intent(SetUpAnalysis.this, LayoutSavings.class);
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