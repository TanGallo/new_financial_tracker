package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutMoneyIn extends MainNavigation {

    BudgetDb monInIncDb;
    Button transDialogCancelBtn, transDialogNoContBtn, transDialogNoDefBtn, transDialogSaveBtn, transDialogYesContBtn, transDialogYesDefBtn;
    ContentValues monInCV;
    Cursor monInCur;
    DbHelper monInHelper, monInHelper2;
    DbManager monInDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtRateFromDb = 0.0, debtPaytFromDb = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0,
            monInMoneyInA = 0.0, monInMoneyInB = 0.0, monInMoneyInOwing = 0.0, monInMonInAmt = 0.0, monInMonInOldAmt = 0.0, monInMonInNewAmt = 0.0,
            monInPercentA = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0,
            savRateFromDb = 0.0;
    EditText transDialogAmtET;
    General monInGen;
    Intent monInToAddInc, monInRefresh, monInToList, monInToFixBudget;
    LinearLayout transDialogDefLayout, transDialogWarnLayout;
    ListView monInList;
    long monInIncId, monInTransBdgtId, monInToAcctId;
    MonInAdapter monInAdapter;
    Spinner monInSpin;
    SQLiteDatabase monInDb, monInDb2;
    String monInAcctName = null, monInToIsDebtSav = null, monInIncName = null;
    TextView transDialogAmtTV, transDialogCatTV, transDialogPayLabel, monInAddMoreTV, monInAvailAcctTV, monInAvailAmtLabel, monInBudgWarnTV, monInDepToTV, monInIncSourceTV, monInTotAcctTV;
    TransactionsDb monInMoneyInDb;
    TransferSpinnerAdapter monInSpinAdapter;
    View dView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        monInDbMgr = new DbManager(this);
        monInGen = new General();

        monInBudgWarnTV = findViewById(R.id.mainBudgetWarnTV);
        monInBudgWarnTV.setVisibility(View.GONE);
        monInTotAcctTV = findViewById(R.id.mainTotalAmtTV);
        monInAvailAmtLabel = findViewById(R.id.mainAvailAmtLabel);
        monInAvailAcctTV = findViewById(R.id.mainAvailAmtTV);
        monInDepToTV = findViewById(R.id.mainLabel1);
        monInDepToTV.setText(getString(R.string.deposit_to));
        monInIncSourceTV = findViewById(R.id.mainLabel2);
        monInIncSourceTV.setText(getString(R.string.money_from));
        monInAddMoreTV = findViewById(R.id.mainAddMoreTV);

        monInAddMoreTV.setOnClickListener(onClickAddMoreTV);

        monInSpin = findViewById(R.id.mainSpin);

        monInDbMgr.mainHeaderText(
                monInBudgWarnTV,
                monInTotAcctTV,
                monInAvailAcctTV,
                monInAvailAmtLabel,
                monInDbMgr.sumTotalExpenses(),
                monInDbMgr.sumTotalIncome(),
                monInDbMgr.retrieveCurrentAccountBalance(),
                monInDbMgr.retrieveCurrentB(),
                monInDbMgr.retrieveCurrentA());
        monInBudgWarnTV.setOnClickListener(onClickMonInBudgWarnTV);

        monInHelper2 = new DbHelper(this);
        monInDb2 = monInHelper2.getReadableDatabase();
        monInCur = monInDb2.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ID + " ASC", null);
        monInSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), monInCur);
        monInSpin.setAdapter(monInSpinAdapter);

        monInSpin.setOnItemSelectedListener(monInSpinSel);

        monInList = findViewById(R.id.mainListView);
        monInAdapter = new MonInAdapter(this, monInDbMgr.getIncomes());
        monInList.setAdapter(monInAdapter);

        monInCV = new ContentValues();
        monInCV.put(DbHelper.LASTPAGEID, 1);
        monInHelper = new DbHelper(this);
        monInDb = monInHelper.getWritableDatabase();
        monInDb.update(DbHelper.CURRENT_TABLE_NAME, monInCV, DbHelper.ID + "= '1'", null);
        monInDb.close();
    }

    public void monInRefresh() {
        monInRefresh = new Intent(LayoutMoneyIn.this, LayoutMoneyIn.class);
        monInRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monInRefresh);
    }

    View.OnClickListener onClickAddMoreTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInToAddInc = new Intent(LayoutMoneyIn.this, AddIncome.class);
            monInToAddInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInToAddInc);
        }
    };

    View.OnClickListener onClickMonInBudgWarnTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInToFixBudget = new Intent(LayoutMoneyIn.this, LayoutBudget.class);
            monInToFixBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInToFixBudget);
        }
    };

    AdapterView.OnItemSelectedListener monInSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monInAcctName = monInCur.getString(monInCur.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            monInToIsDebtSav = monInCur.getString(monInCur.getColumnIndexOrThrow(DbHelper.ACCTDEBTSAV));
            monInToAcctId = monInCur.getLong(monInCur.getColumnIndexOrThrow(DbHelper.ID));
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void monInContinueTransaction() {
        if (!monInToIsDebtSav.equals("D") && !monInToIsDebtSav.equals("S")) {  //TO MAIN ACCT
            monInPercentA = monInDbMgr.sumTotalAExpenses() / monInDbMgr.sumTotalIncome();

            monInMoneyInA = monInDbMgr.detAPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());
            monInMoneyInOwing = monInDbMgr.detOwingPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());
            monInMoneyInB = monInDbMgr.detBPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());

            monInDbMgr.updateTotAcctBalPlus(monInMonInAmt, monInDbMgr.retrieveCurrentAccountBalance());
            monInDbMgr.updateAandBBalPlus(monInMoneyInA, monInMoneyInOwing, monInMoneyInB, monInDbMgr.retrieveCurrentA(), monInDbMgr.retrieveCurrentOwingA(), monInDbMgr.retrieveCurrentB());

            if (monInDbMgr.retrieveCurrentOwingA() < 0) {
                monInDbMgr.adjustCurrentAandB(monInDbMgr.retrieveCurrentOwingA(), monInDbMgr.retrieveCurrentA(), monInDbMgr.retrieveCurrentB());

                newMoneyA = monInDbMgr.detNewAPortion(monInMoneyInA, monInDbMgr.retrieveCurrentOwingA());
                newMoneyOwing = monInDbMgr.detNewOwingPortion(monInMoneyInOwing, monInDbMgr.retrieveCurrentOwingA());
                newMoneyB = monInDbMgr.detNewBPortion(monInMoneyInB, monInDbMgr.retrieveCurrentOwingA());

                moneyInA = newMoneyA;
                moneyInOwing = newMoneyOwing;
                moneyInB = newMoneyB;
            } else {
                moneyInA = monInMoneyInA;
                moneyInOwing = monInMoneyInOwing;
                moneyInB = monInMoneyInB;
            }
        } else if (monInToIsDebtSav.equals("D")) { //TO DEBT ACFT
            monInDbMgr.updateRecMinusPt1(monInMonInAmt, monInDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
            for (AccountsDb a : monInDbMgr.getDebts()) {
                if (a.getId() == monInToAcctId) {
                    debtAmtFromDb = a.getAcctBal();
                    debtLimitFromDb = a.getAcctMax();
                    debtRateFromDb = a.getAcctIntRate();
                    debtPaytFromDb = a.getAcctPaytsTo();
                }
            }
            monInDbMgr.updateRecPt2(monInGen.calcDebtDate(
                    debtAmtFromDb,
                    debtRateFromDb,
                    debtPaytFromDb,
                    getString(R.string.debt_paid),
                    getString(R.string.too_far)), monInToAcctId);
            moneyInA = 0.0;
            moneyInOwing = 0.0;
            moneyInB = 0.0;
        } else if (monInToIsDebtSav.equals("S")) { //TO SAV ACCT
            monInDbMgr.updateRecPlusPt1(monInMonInAmt, monInDbMgr.retrieveCurrentAcctAmt(monInToAcctId), monInToAcctId);
            for (AccountsDb a : monInDbMgr.getSavings()) {
                if (a.getId() == monInToAcctId) {
                    savGoalFromDb = a.getAcctMax();
                    savAmtFromDb = a.getAcctBal();
                    savRateFromDb = a.getAcctIntRate();
                    savPaytFromDb = a.getAcctPaytsTo();
                }
            }
            monInDbMgr.updateRecPt2(monInGen.calcSavingsDate(
                    savGoalFromDb,
                    savAmtFromDb,
                    savRateFromDb,
                    savPaytFromDb,
                    getString(R.string.goal_achieved),
                    getString(R.string.too_far)), monInToAcctId);
            moneyInA = 0.0;
            moneyInOwing = 0.0;
            moneyInB = 0.0;
        }

        monInMoneyInDb = new TransactionsDb(
                "in",
                "N/A",
                monInIncName,
                monInIncId,
                monInMonInAmt,
                moneyInA,
                moneyInOwing,
                moneyInB,
                0.0,
                0.0,
                0.0,
                monInToAcctId,
                monInAcctName,
                monInToIsDebtSav,
                0,
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                monInGen.createTimestamp(),
                0);
        monInDbMgr.addTransactions(monInMoneyInDb);

        monInIncDb.setBdgtAnnPayt(monInDbMgr.makeNewIncAnnAmt(monInIncId, monInGen.lastNumOfDays(365)));
        monInDbMgr.updateBudget(monInIncDb);

        monInAdapter.updateIncomes(monInDbMgr.getIncomes());
        monInAdapter.notifyDataSetChanged();

        monInToList = new Intent(LayoutMoneyIn.this, LayoutMoneyInList.class);
        monInToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monInToList);
    }

    public class MonInAdapter extends ArrayAdapter<BudgetDb> {

        private Context context;
        private List<BudgetDb> incomes;

        private MonInAdapter(
                Context context,
                List<BudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<BudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder monInHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                monInHldr = new MoneyInViewHolder();
                monInHldr.monInCatTV = convertView.findViewById(R.id.paytCatTV);
                convertView.setTag(monInHldr);

            } else {
                monInHldr = (MoneyInViewHolder) convertView.getTag();
            }

            monInHldr.monInCatTV.setText(incomes.get(position).getBdgtCat());

            monInHldr.monInCatTV.setTag(incomes.get(position));

            monInHldr.monInCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monInIncDb = (BudgetDb) monInHldr.monInCatTV.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(LayoutMoneyIn.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_transaction, null);
                    transDialogCatTV = dView.findViewById(R.id.transDialogCatTV);
                    transDialogCatTV.setText(incomes.get(position).getBdgtCat());
                    transDialogPayLabel = dView.findViewById(R.id.transDialogPayLabel);
                    transDialogPayLabel.setText(R.string.deposit);
                    transDialogAmtTV = dView.findViewById(R.id.transDialogAmtTV);
                    monInGen.dblASCurrency(String.valueOf(incomes.get(position).getBdgtPaytAmt()), transDialogAmtTV);
                    transDialogAmtET = dView.findViewById(R.id.transDialogAmtET);
                    transDialogSaveBtn = dView.findViewById(R.id.transDialogSaveBtn);
                    transDialogCancelBtn = dView.findViewById(R.id.transDialogCancelBtn);
                    transDialogDefLayout = dView.findViewById(R.id.transDialogDefLayout);
                    transDialogDefLayout.setVisibility(View.GONE);
                    transDialogNoDefBtn = dView.findViewById(R.id.transDialogNoDefBtn);
                    transDialogYesDefBtn = dView.findViewById(R.id.transDialogYesDefBtn);
                    transDialogWarnLayout = dView.findViewById(R.id.transDialogWarnLayout);
                    transDialogWarnLayout.setVisibility(View.GONE);

                    transDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInRefresh();
                        }
                    });

                    transDialogSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monInIncName = monInIncDb.getBdgtCat();
                            monInIncId = monInIncDb.getId();

                            monInMonInOldAmt = monInIncDb.getBdgtPaytAmt();
                            monInMonInNewAmt = monInGen.dblFromET(transDialogAmtET);

                            if (monInMonInNewAmt == 0) {
                                monInMonInAmt = monInMonInOldAmt;
                            } else {
                                monInMonInAmt = monInMonInNewAmt;
                            }

                            if (monInMonInAmt == monInMonInNewAmt) { //IF ENTERED ANOTHER AMT
                                transDialogDefLayout.setVisibility(View.VISIBLE);

                                transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogDefLayout.setVisibility(View.GONE);
                                        monInContinueTransaction();
                                    }
                                });

                                transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogDefLayout.setVisibility(View.GONE);
                                        monInIncDb.setBdgtPaytAmt(monInMonInAmt);
                                        monInDbMgr.updateBudget(monInIncDb);
                                        monInContinueTransaction();
                                    }
                                });
                            } else { //IF USING DEFAULT
                                monInContinueTransaction();
                            }
                        }
                    });
                    builder.setView(dView);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView monInCatTV;
    }
}
