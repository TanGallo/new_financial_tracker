package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class LayoutViewEdit extends MainNavigation {

    ContentValues layVwEdCV;
    Cursor layVwEdCursor, layVwEdCursor2, layVwEdCursor3, layVwEdCursor4;
    DbHelper layVwEdHelper;
    DbManager layVwEdDbMgr;
    Double annPaytFromDb = 0.0, layVwEdNewExpAnnAmt = 0.0, layVwEdNewIncAnnAmt = 0.0, transfersToAcctThisYear = 0.0, transfersFromAcctThisYear = 0.0;
    General layVwEdGen;
    ImageButton layVwEdCCPayBtn, layVwEdCCPurBtn, layVwEdMonInBtn, layVwEdMonOutBtn, layVwEdTransferBtn, layVwEdWklyLimitsBtn;
    Intent layVwEdToCCPay, layVwEdToCCPur, layVwEdToFixBudget, layVwEdToMonIn, layVwEdToMonOut, layVwEdToTransfers, layVwEdToWklyLimits;
    LinearLayout layVwEdTransactionsLayout, layVwEdControlLayout;
    SQLiteDatabase layVwEdDb;
    TextView layVwEdAvailAcctTV, layVwEdAvailAmtLabel, layVwEdBudgWarnTV, layVwEdTotAcctTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c7_layout_view_edit_entries);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layVwEdDbMgr = new DbManager(this);
        layVwEdGen = new General();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        layVwEdMonInBtn = findViewById(R.id.viewEditMonInBtn);
        layVwEdMonOutBtn = findViewById(R.id.viewEditMonOutBtn);
        layVwEdCCPurBtn = findViewById(R.id.viewEditCCPurBtn);
        layVwEdTransferBtn = findViewById(R.id.viewEditTransferBtn);
        layVwEdCCPayBtn = findViewById(R.id.viewEditCCPayBtn);

        layVwEdMonInBtn.setOnClickListener(onClickLayVwEdMoneyInBtn);
        layVwEdMonOutBtn.setOnClickListener(onClickLayVwEdMoneyOutBtn);
        layVwEdTransferBtn.setOnClickListener(onClickLayVwEdTransferBtn);
        layVwEdCCPurBtn.setOnClickListener(onClickLayVwEdCCPurBtn);
        layVwEdCCPayBtn.setOnClickListener(onClickLayVwEdCCPayBtn);

        layVwEdCV = new ContentValues();
        layVwEdCV.put(DbHelper.LASTPAGEID, 10);
        layVwEdHelper = new DbHelper(getApplicationContext());
        layVwEdDb = layVwEdHelper.getWritableDatabase();
        layVwEdDb.update(DbHelper.CURRENT_TABLE_NAME, layVwEdCV, DbHelper.ID + "= '1'", null);
        layVwEdDb.close();
    }
    
    View.OnClickListener onClickLayVwEdMoneyInBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdToMonIn = new Intent(LayoutViewEdit.this, LayoutMoneyInList.class);
            layVwEdToMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layVwEdToMonIn);
        }
    };

    View.OnClickListener onClickLayVwEdMoneyOutBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdToMonOut = new Intent(LayoutViewEdit.this, LayoutMoneyOutList.class);
            layVwEdToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layVwEdToMonOut);
        }
    };

    View.OnClickListener onClickLayVwEdCCPurBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdToCCPur = new Intent(LayoutViewEdit.this, LayoutCCPurList.class);
            layVwEdToCCPur.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layVwEdToCCPur);
        }
    };

    View.OnClickListener onClickLayVwEdTransferBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdToTransfers = new Intent(LayoutViewEdit.this, LayoutTransfersList.class);
            layVwEdToTransfers.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layVwEdToTransfers);
        }
    };

    View.OnClickListener onClickLayVwEdCCPayBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layVwEdToCCPay = new Intent(LayoutViewEdit.this, LayoutCCPayList.class);
            layVwEdToCCPay.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layVwEdToCCPay);
        }
    };
}
