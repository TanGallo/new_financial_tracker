/*package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import ca.gotchasomething.mynance.tabFragments.DailyMoneyCCPay;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyCCPur;

public class LayoutCreditCardTransactions extends MainNavigation {

    boolean warning = false;
    DbManager layCCDbMgr;
    FragmentManager layCCFM;
    FragmentTransaction layCCTrans;
    FrameLayout layCCContainer;
    General layCCGen;
    Intent layCCToFixBudget;
    LinearLayout layCCHeaderLayout;
    TabLayout layCCTabLay;
    TabLayout.Tab tab;
    TextView layCCAvailAcctTV, layCCBudgWarnTV, layCCTotAcctTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main_credit_card);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layCCDbMgr = new DbManager(this);
        layCCGen = new General();

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        layCCBudgWarnTV = findViewById(R.id.mainCCBudgetWarnTV);
        layCCBudgWarnTV.setVisibility(View.GONE);
        layCCHeaderLayout = findViewById(R.id.mainCCHeaderLayout);
        layCCHeaderLayout.setVisibility(View.VISIBLE);
        layCCTotAcctTV = findViewById(R.id.mainCCTotalAmtTV);
        layCCAvailAcctTV = findViewById(R.id.mainCCAvailAmtTV);

        layCCDbMgr.mainHeaderText(layCCBudgWarnTV, layCCTotAcctTV, layCCAvailAcctTV, layCCDbMgr.sumTotalExpenses(), layCCDbMgr.sumTotalIncome(), layCCDbMgr.retrieveCurrentAccountBalance(), layCCDbMgr.retrieveCurrentB());
        layCCBudgWarnTV.setOnClickListener(onClickLayCCBudgWarnTV);

        layCCTabLay = findViewById(R.id.mainCCTabLayout);

        //layCCDbMgr.updateAllIncBudget();
        //layCCDbMgr.updateAllExpBudget();
        //layCCDbMgr.updateAllDebtBudget();
        //layCCDbMgr.updateAllSavBudget();

        layCCContainer = findViewById(R.id.mainCCFragContainer);

        layCCTabLay.addTab(layCCTabLay.newTab().setText(getString(R.string.purchases)));
        layCCTabLay.addTab(layCCTabLay.newTab().setText(getString(R.string.payments)));
        layCCRepFrag(new DailyMoneyCCPur());

        layCCTabLay.addOnTabSelectedListener(onMainCCTabSelectedListener);
    }

    View.OnClickListener onClickLayCCBudgWarnTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layCCToFixBudget = new Intent(LayoutCreditCardTransactions.this, LayoutBudget.class);
            layCCToFixBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layCCToFixBudget);
        }
    };

    TabLayout.OnTabSelectedListener onMainCCTabSelectedListener = (new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getPosition() == 0) {
                layCCRepFrag(new DailyMoneyCCPur());
            } else if (tab.getPosition() == 1) {
                layCCRepFrag(new DailyMoneyCCPay());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    });

    public void layCCRepFrag(Fragment fragment) {
        layCCFM = getSupportFragmentManager();
        layCCTrans = layCCFM.beginTransaction();
        layCCTrans.replace(R.id.mainCCFragContainer, fragment);
        layCCTrans.commit();
    }
}*/
