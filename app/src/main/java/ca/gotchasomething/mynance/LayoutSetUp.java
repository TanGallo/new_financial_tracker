package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import ca.gotchasomething.mynance.data.CurrentDb;

public class LayoutSetUp extends MainNavigation {

    Button laySetIncBtn, laySetBillsBtn, laySetWeeklyBtn, laySetDebtsBtn, laySetSavingsBtn, laySetAnalysisBtn, laySetFinalBtn;
    CheckBox laySetIncomeCheckbox, laySetBillsCheckbox, laySetWeeklyCheckbox, laySetDebtsCheckbox, laySetSavingsCheckbox, laySetAnalysisCheckbox,
            laySetTourCheckbox, laySetFinalCheckbox;
    ContentValues laySetCV;
    CurrentDb laySetCurDb;
    DbHelper laySetHelper;
    DbManager laySetDbMgr;
    General laySetGen;
    Intent laySetToSlides, laySetToMain;
    SQLiteDatabase laySetDb;
    String latestDone = null;
    TextView laySetIncomeLabel, laySetBillsLabel, laySetWeeklyLabel, laySetDebtsLabel, laySetTourLabel, laySetTourLabel2, laySetSavingsLabel,
            laySetAnalysisLabel, laySetFinalLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c2_layout_set_up);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        laySetDbMgr = new DbManager(this);
        laySetGen = new General();

        laySetIncBtn = findViewById(R.id.setUpIncomeButton);
        laySetIncomeLabel = findViewById(R.id.setUpIncomeLabel);
        laySetIncomeLabel.setVisibility(View.GONE);
        laySetIncomeCheckbox = findViewById(R.id.setUpIncomeCheckbox);

        laySetBillsBtn = findViewById(R.id.setUpBillsButton);
        laySetBillsBtn.setVisibility(View.GONE);
        laySetBillsLabel = findViewById(R.id.setUpBillsLabel);
        laySetBillsCheckbox = findViewById(R.id.setUpBillsCheckbox);

        laySetDebtsBtn = findViewById(R.id.setUpDebtsButton);
        laySetDebtsBtn.setVisibility(View.GONE);
        laySetDebtsLabel = findViewById(R.id.setUpDebtsLabel);
        laySetDebtsCheckbox = findViewById(R.id.setUpDebtsCheckbox);

        laySetSavingsBtn = findViewById(R.id.setUpSavingsButton);
        laySetSavingsBtn.setVisibility(View.GONE);
        laySetSavingsLabel = findViewById(R.id.setUpSavingsLabel);
        laySetSavingsCheckbox = findViewById(R.id.setUpSavingsCheckbox);

        laySetAnalysisBtn = findViewById(R.id.setUpAnalysisButton);
        laySetAnalysisBtn.setVisibility(View.GONE);
        laySetAnalysisLabel = findViewById(R.id.setUpAnalysisLabel);
        laySetAnalysisCheckbox = findViewById(R.id.setUpAnalysisCheckbox);

        laySetWeeklyBtn = findViewById(R.id.setUpWeeklyButton);
        laySetWeeklyBtn.setVisibility(View.GONE);
        laySetWeeklyLabel = findViewById(R.id.setUpWeeklyLabel);
        laySetWeeklyCheckbox = findViewById(R.id.setUpWeeklyCheckbox);

        laySetFinalBtn = findViewById(R.id.setUpFinalButton);
        laySetFinalBtn.setVisibility(View.GONE);
        laySetFinalLabel = findViewById(R.id.setUpFinalLabel);
        laySetFinalCheckbox = findViewById(R.id.setUpFinalCheckbox);

        laySetTourLabel = findViewById(R.id.setUpTourLabel);
        laySetTourLabel.setVisibility(View.GONE);
        laySetTourLabel2 = findViewById(R.id.setUpTourLabel2);
        laySetTourLabel2.setVisibility(View.GONE);
        laySetTourCheckbox = findViewById(R.id.setUpTourCheckbox);
        laySetTourCheckbox.setVisibility(View.GONE);

        laySetIncBtn.setOnClickListener(onClickLaySetIncButton);
        laySetBillsBtn.setOnClickListener(onClickLaySetBillsButton);
        laySetDebtsBtn.setOnClickListener(onClickLaySetDebtsButton);
        laySetSavingsBtn.setOnClickListener(onClickLaySetSavingsButton);
        laySetAnalysisBtn.setOnClickListener(onClickLaySetAnalysisButton);
        laySetWeeklyBtn.setOnClickListener(onClickLaySetWeeklyButton);
        laySetFinalBtn.setOnClickListener(onClickLaySetFinalButton);
        laySetTourCheckbox.setOnCheckedChangeListener(onCheckLaySetTourCheckbox);

        if(laySetDbMgr.getCurrent().size() == 0) {
            laySetCurDb = new CurrentDb(
                    0.0,
                    0.0,
                    0.0,
                    0,
                    laySetGen.createTimestamp(),
                    0);
            laySetDbMgr.addCurrent(laySetCurDb);
        }

        latestDone = laySetDbMgr.retrieveLatestDone();
        switch (latestDone) {
            case "income":
                laySetAfterIncome();
                laySetBillsBtn.setVisibility(View.VISIBLE);
                laySetBillsLabel.setVisibility(View.GONE);
                break;
            case "bills":
                laySetAfterIncome();
                laySetAfterBills();
                laySetDebtsBtn.setVisibility(View.VISIBLE);
                laySetDebtsLabel.setVisibility(View.GONE);
                break;
            case "debts":
                laySetAfterIncome();
                laySetAfterBills();
                laySetAfterDebts();
                laySetSavingsBtn.setVisibility(View.VISIBLE);
                laySetSavingsLabel.setVisibility(View.GONE);
                break;
            case "savings":
                laySetAfterIncome();
                laySetAfterBills();
                laySetAfterDebts();
                laySetAfterSavings();
                laySetAnalysisBtn.setVisibility(View.VISIBLE);
                laySetAnalysisLabel.setVisibility(View.GONE);
                break;
            case "analysis":
                laySetAfterIncome();
                laySetAfterBills();
                laySetAfterDebts();
                laySetAfterSavings();
                laySetAfterAnalysis();
                laySetWeeklyBtn.setVisibility(View.VISIBLE);
                laySetWeeklyLabel.setVisibility(View.GONE);
                break;
            case"weekly":
                laySetAfterIncome();
                laySetAfterBills();
                laySetAfterDebts();
                laySetAfterSavings();
                laySetAfterAnalysis();
                laySetAfterWeekly();
                laySetFinalBtn.setVisibility(View.VISIBLE);
                laySetFinalLabel.setVisibility(View.GONE);
                break;
            case"final":
                laySetAfterIncome();
                laySetAfterBills();
                laySetAfterDebts();
                laySetAfterSavings();
                laySetAfterAnalysis();
                laySetAfterWeekly();
                laySetAfterFinal();
                break;
            case "tour":
                laySetToMain = new Intent(LayoutSetUp.this, MainActivity.class);
                laySetToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(laySetToMain);
        }
    }

    public void laySetAfterIncome() {
        laySetIncBtn.setVisibility(View.GONE);
        laySetIncomeLabel.setVisibility(View.VISIBLE);
        laySetIncomeCheckbox.setChecked(true);
    }

    public void laySetAfterBills() {
        laySetBillsBtn.setVisibility(View.GONE);
        laySetBillsLabel.setVisibility(View.VISIBLE);
        laySetBillsCheckbox.setChecked(true);
    }

    public void laySetAfterDebts() {
        laySetDebtsBtn.setVisibility(View.GONE);
        laySetDebtsLabel.setVisibility(View.VISIBLE);
        laySetDebtsCheckbox.setChecked(true);
    }

    public void laySetAfterSavings() {
        laySetSavingsBtn.setVisibility(View.GONE);
        laySetSavingsLabel.setVisibility(View.VISIBLE);
        laySetSavingsCheckbox.setChecked(true);
    }

    public void laySetAfterAnalysis() {
        laySetAnalysisBtn.setVisibility(View.GONE);
        laySetAnalysisLabel.setVisibility(View.VISIBLE);
        laySetAnalysisCheckbox.setChecked(true);
    }

    public void laySetAfterWeekly() {
        laySetWeeklyLabel.setVisibility(View.VISIBLE);
        laySetWeeklyBtn.setVisibility(View.GONE);
        laySetWeeklyCheckbox.setChecked(true);
    }

    public void laySetAfterFinal() {
        laySetFinalLabel.setVisibility(View.VISIBLE);
        laySetFinalBtn.setVisibility(View.GONE);
        laySetFinalCheckbox.setChecked(true);
        laySetTourLabel.setVisibility(View.VISIBLE);
        laySetTourLabel2.setVisibility(View.VISIBLE);
        laySetTourCheckbox.setVisibility(View.VISIBLE);
    }

    CheckBox.OnCheckedChangeListener onCheckLaySetTourCheckbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            laySetHelper = new DbHelper(getApplicationContext());
            laySetDb = laySetHelper.getWritableDatabase();
            laySetCV = new ContentValues();
            laySetCV.put(DbHelper.LATESTDONE, "tour");
            laySetDb.update(DbHelper.SET_UP_TABLE_NAME, laySetCV, DbHelper.ID + "= '1'", null);
            laySetDb.close();

            laySetToMain = new Intent(LayoutSetUp.this, MainActivity.class);
            laySetToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(laySetToMain);
        }
    };

    public void laySetGoToSlides() {
        laySetToSlides = new Intent(LayoutSetUp.this, SlidesLayoutSetUp.class);
        laySetToSlides.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(laySetToSlides);
    }


    View.OnClickListener onClickLaySetFinalButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetWeeklyButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetAnalysisButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetSavingsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetDebtsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetBillsButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };

    View.OnClickListener onClickLaySetIncButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySetGoToSlides();
        }
    };
}
