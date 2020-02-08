package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutWeeklyLimits extends MainNavigation {

    Button wee2AddMoreBtn,
            wee2DoneBtn,
            wee2ResetBtn;
    ContentValues  wee2CV;
    DbHelper  wee2Helper;
    DbManager wee2DbMgr;
    Double wee2AmtLeft = 0.0,
            wee2SpentThisWeek = 0.0,
            wee2StartBal = 0.0;
    float wee2AmtSpentF,
            wee2AmtLeftF;
    General wee2Gen;
    Intent wee2ToMain,
            wee2ToAddMore;
    LinearLayout wee2SpinLayout;
    ListView wee2ListView;
    long wee2ExpId;
    SQLiteDatabase  wee2Db;
    TextView wee2HeaderLabel,
            wee2TotalTV;
    Wee2LstAdapter wee2LstAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        wee2DbMgr = new DbManager(this);
        wee2Gen = new General();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        wee2SpinLayout = findViewById(R.id.layout1SpinLayout);
        wee2SpinLayout.setVisibility(View.GONE);
        wee2ResetBtn = findViewById(R.id.layout1SpinResetBtn);
        wee2ResetBtn.setVisibility(View.GONE);
        wee2HeaderLabel = findViewById(R.id.layout1HeaderLabelTV);
        wee2HeaderLabel.setText(getString(R.string.weekly_limits));
        wee2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        wee2AddMoreBtn.setText(getString(R.string.add_edit_weekly_limits));
        wee2DoneBtn = findViewById(R.id.layout1DoneBtn);
        wee2DoneBtn.setText(getString(R.string.done));
        wee2TotalTV = findViewById(R.id.layout1TotalTV);
        wee2TotalTV.setVisibility(View.GONE);

        wee2AddMoreBtn.setOnClickListener(onclickWee2AddMoreBtn);
        wee2DoneBtn.setOnClickListener(onclickWee2DoneBtn);

        wee2ListView = findViewById(R.id.layout1ListView);
        wee2LstAdapter = new Wee2LstAdapter(this, wee2DbMgr.getWeeklyLimits());
        wee2ListView.setAdapter(wee2LstAdapter);

        wee2CV = new ContentValues();
        wee2CV.put(DbHelper.LASTPAGEID, 9);
        wee2Helper = new DbHelper(this);
        wee2Db =  wee2Helper.getWritableDatabase();
        wee2Db.update(DbHelper.CURRENT_TABLE_NAME,  wee2CV, DbHelper.ID + "= '1'", null);
        wee2Db.close();
    }

    View.OnClickListener onclickWee2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wee2ToAddMore = new Intent(LayoutWeeklyLimits.this, LayoutWeeklyLimitsList.class);
            wee2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(wee2ToAddMore);
        }
    };

    View.OnClickListener onclickWee2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wee2ToMain = new Intent(LayoutWeeklyLimits.this, MainActivity.class);
            wee2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(wee2ToMain);
        }
    };

    public class Wee2LstAdapter extends ArrayAdapter<BudgetDb> {

        public Context context;
        public List<BudgetDb> weeklyLimits;

        public Wee2LstAdapter(
                Context context,
                List<BudgetDb> weeklyLimits) {

            super(context, -1, weeklyLimits);

            this.context = context;
            this.weeklyLimits = weeklyLimits;
        }

        public void getWeeklyLimits(List<BudgetDb> weeklyLimits) {
            this.weeklyLimits = weeklyLimits;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return weeklyLimits.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Wee2ViewHolder wee2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_pie_charts,
                        parent, false);

                wee2Hldr = new Wee2ViewHolder();
                wee2Hldr.wee2PieChart = convertView.findViewById(R.id.pcListPieChart);
                wee2Hldr.wee2CatTV = convertView.findViewById(R.id.pcListCatTV);
                wee2Hldr.wee2StartBalTV = convertView.findViewById(R.id.pcListStartBalTV);
                wee2Hldr.wee2AmtLeftLabel = convertView.findViewById(R.id.pcListAmtLeftLabel);
                wee2Hldr.wee2AmtLeftLabel.setTextColor(Color.parseColor("#5dbb63")); //light green
                wee2Hldr.wee2AmtLeftTV = convertView.findViewById(R.id.pcListAmtLeftTV);
                wee2Hldr.wee2AmtLeftTV.setTextColor(Color.parseColor("#5dbb63")); //light green
                wee2Hldr.wee2SpentAmtTV = convertView.findViewById(R.id.pcListSpentAmtTV);
                wee2Hldr.wee2SpentAmtTV.setTextColor(Color.parseColor("#83878b"));
                wee2Hldr.wee2AmtSpentLabel = convertView.findViewById(R.id.pcListSpentAmtLabel); //grey
                wee2Hldr.wee2AmtSpentLabel.setTextColor(Color.parseColor("#83878b")); //grey
                wee2Hldr.wee2AmtLeftWarn = convertView.findViewById(R.id.pcListAmtLeftWarnTV);
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.GONE);
                convertView.setTag(wee2Hldr);

            } else {
                wee2Hldr = (Wee2ViewHolder) convertView.getTag();
            }

            //retrieve spendingCategory
            wee2Hldr.wee2CatTV.setText(weeklyLimits.get(position).getBdgtCat());

            //retrieve amount spent in this category during general.thisWeek();
            wee2ExpId = weeklyLimits.get(position).getId();

            List<Double> spentThisWeekList = new ArrayList<>();
            for(TransactionsDb m : wee2DbMgr.getMoneyOuts()) {
                if(m.getTransBdgtId() == wee2ExpId && wee2Gen.thisWeek().contains(m.getTransCreatedOn())) {
                    spentThisWeekList.add(m.getTransAmt());
                }
            }
            wee2SpentThisWeek = 0.0;
            if(spentThisWeekList.size() == 0) {
                wee2SpentThisWeek = 0.0;
            } else {
                for(Double dbl : spentThisWeekList) {
                    wee2SpentThisWeek += dbl;
                }
            }

            wee2Gen.dblASCurrency(String.valueOf(wee2SpentThisWeek), wee2Hldr.wee2SpentAmtTV);

            //retrieve amountLeft
            wee2StartBal = (weeklyLimits.get(position).getBdgtAnnPayt() / 52);
            wee2AmtLeft = wee2StartBal - wee2SpentThisWeek;
            wee2Gen.dblASCurrency(String.valueOf(wee2AmtLeft), wee2Hldr.wee2AmtLeftTV);

            wee2Gen.dblASCurrency(String.valueOf(wee2StartBal), wee2Hldr.wee2StartBalTV);

            if(wee2AmtLeft < 0) {
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.VISIBLE);
                wee2Hldr.wee2AmtLeftLabel.setVisibility(View.INVISIBLE);
                wee2Hldr.wee2AmtLeftTV.setVisibility(View.INVISIBLE);
                wee2Hldr.wee2PieChart.setVisibility(View.GONE);
            } else {
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.GONE);
                wee2Hldr.wee2AmtLeftLabel.setVisibility(View.VISIBLE);
                wee2Hldr.wee2AmtLeftTV.setVisibility(View.VISIBLE);
                wee2Hldr.wee2PieChart.setVisibility(View.VISIBLE);
            }

            wee2AmtLeftF = wee2Gen.convertDblToFloat(wee2AmtLeft) / wee2Gen.convertDblToFloat(wee2StartBal);
            wee2AmtSpentF = wee2Gen.convertDblToFloat(wee2SpentThisWeek) / wee2Gen.convertDblToFloat(wee2StartBal);

            wee2Gen.buildLimitsPieChart(
                    wee2AmtLeftF,
                    wee2AmtSpentF,
                    wee2Hldr.wee2PieChart,
                    Color.parseColor("#5dbb63"), //light green
                    Color.parseColor("#83878b")); //gray

            return convertView;
        }
    }

    private static class Wee2ViewHolder {
        private PieChart wee2PieChart;
        private TextView wee2CatTV;
        private TextView wee2StartBalTV;
        private TextView wee2AmtLeftTV;
        private TextView wee2AmtSpentLabel;
        private TextView wee2SpentAmtTV;
        private TextView wee2AmtLeftLabel;
        private TextView wee2AmtLeftWarn;
    }
}
