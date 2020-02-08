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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;

public class LayoutBudget extends MainNavigation {

    Button layBudResInfoOkBtn;
    ContentValues layBudCV;
    Date latestDateD;
    DbHelper layBudHelper;
    DbManager layBudDbMgr;
    Double layBudAmtToSavGoal = 0.0,
            layBudSavGoalPercent = 0.0,
            layBudStillAvail = 0.0,
            layBudTotDebt = 0.0,
            layBudTotSav = 0.0,
            layBudTotSavGoal = 0.0,
            layBudTotInc = 0.0,
            layBudTotRes = 0.0,
            spendPercent2 = 0.0;
    float layBudAmtReservedF = 0,
            layBudAmtNotResF = 0,
            layBudAmtSavedF = 0,
            layBudAmtToSavGoalF = 0;
    General layBudGen;
    ImageButton layBudAdjDebtsBtn,
            layBudAdjExpBtn,
            layBudAdjIncBtn,
            layBudAdjSavBtn,
            layBudResInfoBtn;
    int layBudInt1 = 0;
    Intent layBudToAdjInc,
            layBudToAdjExp,
            layBudToAdjDebt,
            layBudToAdjSav;
    LinearLayout layBudDebtFreeLayout,
            layBudResInfoLayout;
    PieChart layBudPieChart,
            layBudPieChart2;
    public NumberFormat percentFormat = NumberFormat.getPercentInstance();
    SimpleDateFormat latestDateS;
    SQLiteDatabase layBudDb;
    String layBudRecommendation = null,
            latestDate = null,
            spendPercent = null;
    TextView layBudDebtFreeLabel,
            layBudDebtFreeTV,
            layBudOverWarnLabel,
            layBudOverWarnTV,
            layBudSavingsLabel,
            layBudSavingsLabel2,
            layBudSavingsLabel3,
            layBudSavingsTV,
            layBudSavingsTV2,
            layBudStatusTV,
            layBudStatusTV2,
            layBudTotIncTV,
            layBudTotResTV;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c5_layout_budget_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layBudGen = new General();
        layBudDbMgr = new DbManager(this);

        layBudDebtFreeLayout = findViewById(R.id.budMnDebtFreeLayout);
        layBudDebtFreeLabel = findViewById(R.id.budMnDebtFreeLabel);
        layBudDebtFreeTV = findViewById(R.id.budMnDebtFreeTV);
        layBudSavingsLabel = findViewById(R.id.budMnSavingsLabel);
        layBudSavingsLabel2 = findViewById(R.id.budMnSavingsLabel2);
        layBudSavingsTV = findViewById(R.id.budMnSavingsTV);
        layBudSavingsLabel3 = findViewById(R.id.budMnSavingsLabel3);
        layBudSavingsTV2 = findViewById(R.id.budMnSavingsTV2);
        layBudPieChart2 = findViewById(R.id.budMnPieChart2);
        layBudTotIncTV = findViewById(R.id.budMnTotIncTV);
        layBudTotResTV = findViewById(R.id.budMnTotExpTV);
        layBudResInfoBtn = findViewById(R.id.budMnResInfoBtn);
        layBudResInfoLayout = findViewById(R.id.budMnResInfoLayout);
        layBudResInfoLayout.setVisibility(View.GONE);
        layBudResInfoOkBtn = findViewById(R.id.budMnResInfoOkBtn);
        layBudPieChart = findViewById(R.id.budMnPieChart);
        layBudStatusTV = findViewById(R.id.budMnStatusTV);
        layBudStatusTV2 = findViewById(R.id.budMnStatusTV2);
        layBudOverWarnLabel = findViewById(R.id.budMnOverWarnLabel);
        layBudOverWarnLabel.setVisibility(View.GONE);
        layBudOverWarnTV = findViewById(R.id.budMnOverWarnTV);
        layBudOverWarnTV.setVisibility(View.GONE);
        layBudAdjIncBtn = findViewById(R.id.budMnAdjIncBtn);
        layBudAdjExpBtn = findViewById(R.id.budMnAdjExpBtn);
        layBudAdjDebtsBtn = findViewById(R.id.budMnAdjDebtsBtn);
        layBudAdjSavBtn = findViewById(R.id.budMnAdjSavBtn);

        layBudResInfoBtn.setOnClickListener(onClickLayBudResInfoBtn);
        layBudAdjIncBtn.setOnClickListener(onClickLayBudAdjIncBtn);
        layBudAdjExpBtn.setOnClickListener(onClickLayBudAdjExpBtn);
        layBudAdjDebtsBtn.setOnClickListener(onClickLayBudAdjDebtsBtn);
        layBudAdjSavBtn.setOnClickListener(onClickLayBudAdjSavBtn);

        layBudHeaderText();

        layBudCV = new ContentValues();
        layBudCV.put(DbHelper.LASTPAGEID, 2);
        layBudHelper = new DbHelper(this);
        layBudDb = layBudHelper.getWritableDatabase();
        layBudDb.update(DbHelper.CURRENT_TABLE_NAME, layBudCV, DbHelper.ID + "= '1'", null);
        layBudDb.close();
    }

    public void layBudHeaderText() {

        layBudTotSav = layBudDbMgr.sumTotalSavings();
        layBudTotSavGoal = layBudDbMgr.sumTotalSavGoal();
        layBudAmtToSavGoal = layBudTotSavGoal - layBudTotSav;
        layBudGen.dblASCurrency(String.valueOf(layBudTotSav), layBudSavingsTV);

        if(layBudTotSav == 0) {
            layBudSavingsTV2.setVisibility(View.GONE);
            layBudSavingsLabel3.setVisibility(View.GONE);
            layBudPieChart2.setVisibility(View.GONE);
        } else {
            layBudSavGoalPercent = layBudTotSav / layBudDbMgr.sumTotalSavGoal();
            percentFormat.setMinimumFractionDigits(0);
            percentFormat.setMaximumFractionDigits(0);
            spendPercent = percentFormat.format(layBudSavGoalPercent);
            layBudSavingsTV2.setVisibility(View.VISIBLE);
            layBudSavingsTV2.setText(spendPercent);
            layBudSavingsLabel3.setVisibility(View.VISIBLE);
            layBudPieChart2.setVisibility(View.VISIBLE);

            layBudAmtSavedF = layBudGen.convertDblToFloat(layBudTotSav) / layBudGen.convertDblToFloat(layBudTotSavGoal);
            layBudAmtToSavGoalF = layBudGen.convertDblToFloat(layBudAmtToSavGoal) / layBudGen.convertDblToFloat(layBudTotSavGoal);

            layBudGen.buildLimitsPieChart(
                    layBudAmtSavedF,
                    layBudAmtToSavGoalF,
                    layBudPieChart2,
                    Color.parseColor("#5dbb63"), //light green
                    Color.parseColor("#83878b")); //grey
        }

        layBudTotDebt = layBudDbMgr.sumTotalDebt();
        layBudGen.dblASCurrency(String.valueOf(layBudTotDebt), layBudDebtFreeTV);

        if (layBudDbMgr.getDebts().size() == 0) {
            layBudDebtFreeTV.setVisibility(View.GONE);
            layBudDebtFreeLabel.setText(getString(R.string.no_debts));
            layBudDebtFreeLabel.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else {
            layBudDebtFreeTV.setVisibility(View.VISIBLE);
            layBudDebtFreeTV.setText(layBudLatestDate());
        }

        layBudTotInc = layBudDbMgr.sumTotalIncome();
        layBudTotRes = layBudTotInc * (layBudDbMgr.sumTotalAExpenses() / layBudTotInc);
        layBudGen.dblASCurrency(String.valueOf(layBudTotInc), layBudTotIncTV);
        layBudGen.dblASCurrency(String.valueOf(layBudTotRes), layBudTotResTV);

        layBudStillAvail = layBudTotInc - layBudTotRes;
        if (layBudStillAvail < 0) {
            layBudStatusTV.setVisibility(View.GONE);
            layBudStatusTV2.setVisibility(View.GONE);
            layBudOverWarnLabel.setVisibility(View.VISIBLE);
            layBudOverWarnTV.setVisibility(View.VISIBLE);
            layBudPieChart.setVisibility(View.GONE);
            layBudGen.dblASCurrency(String.valueOf(layBudStillAvail), layBudOverWarnTV);
        } else {
            layBudOverWarnLabel.setVisibility(View.GONE);
            layBudOverWarnTV.setVisibility(View.GONE);
            layBudStatusTV.setVisibility(View.VISIBLE);
            layBudStatusTV2.setVisibility(View.VISIBLE);
            layBudPieChart.setVisibility(View.VISIBLE);

            spendPercent2 = (layBudDbMgr.sumTotalAExpenses() / layBudDbMgr.sumTotalIncome());
            if (spendPercent2 >= .910) {
                layBudInt1 = Color.parseColor("#ffff4444"); //red
                layBudRecommendation = getString(R.string.should_adj);
            } else if (spendPercent2 <= .909 && spendPercent2 >= .801) {
                layBudInt1 = Color.parseColor("#ffc30b"); //yellow
                layBudRecommendation = getString(R.string.may_adj);
            } else if(spendPercent2 <= .800) {
                layBudInt1 = Color.parseColor("#5dbb63"); //light green
                layBudRecommendation = getString(R.string.no_adj_nec);
            }
            layBudDbMgr.spendResPara(
                    layBudStatusTV,
                    layBudStatusTV2,
                    layBudRecommendation,
                    getString(R.string.ana_res_prt_1),
                    getString(R.string.ana_res_part_2),
                    spendPercent2,
                    layBudInt1);

            layBudAmtReservedF = layBudGen.convertDblToFloat(layBudTotRes) / layBudGen.convertDblToFloat(layBudTotInc);
            layBudAmtNotResF = layBudGen.convertDblToFloat(layBudStillAvail) / layBudGen.convertDblToFloat(layBudTotInc);

            layBudGen.buildLimitsPieChart(
                    layBudAmtReservedF,
                    layBudAmtNotResF,
                    layBudPieChart,
                    layBudInt1,
                    Color.parseColor("#83878b")); //grey
        }
    }

    public String layBudLatestDate() {
        List<String> dates = new ArrayList<>();
        for (AccountsDb a : layBudDbMgr.getDebts()) {
            dates.add(a.getAcctEndDate());
        }
        List<Date> dates2 = new ArrayList<>(dates.size());
        layBudGen.extractingDates(dates, dates2);

        try {
            latestDateD = Collections.max(dates2);
        } catch (Exception e) {
            layBudDebtFreeLayout.setVisibility(View.GONE);
        }
        try {
            latestDateS = new SimpleDateFormat("dd-MMM-yyyy");
            latestDate = latestDateS.format(latestDateD);
        } catch (Exception e2) {
            layBudDebtFreeLayout.setVisibility(View.GONE);
        }
        return latestDate;
    }

    View.OnClickListener onClickLayBudResInfoBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudResInfoLayout.setVisibility(View.VISIBLE);
            layBudResInfoOkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layBudResInfoLayout.setVisibility(View.GONE);
                }
            });

        }
    };

    View.OnClickListener onClickLayBudAdjIncBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjInc = new Intent(LayoutBudget.this, AddIncomeList.class);
            layBudToAdjInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjInc);
        }
    };

    View.OnClickListener onClickLayBudAdjExpBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjExp = new Intent(LayoutBudget.this, AddExpenseList.class);
            layBudToAdjExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjExp);
        }
    };

    View.OnClickListener onClickLayBudAdjDebtsBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjDebt = new Intent(LayoutBudget.this, LayoutDebt.class);
            layBudToAdjDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjDebt);
        }
    };

    View.OnClickListener onClickLayBudAdjSavBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjSav = new Intent(LayoutBudget.this, LayoutSavings.class);
            layBudToAdjSav.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjSav);
        }
    };

}
