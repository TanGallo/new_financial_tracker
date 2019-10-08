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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

//import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.MoneyInDb;
//import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutMoneyIn extends MainNavigation {

    ContentValues monInCV;
    Cursor monInCur;
    DbHelper monInHelper, monInHelper2;
    DbManager monInDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtRateFromDb = 0.0, debtPaytFromDb = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0,
            monInMoneyInA = 0.0, monInMoneyInB = 0.0, monInMoneyInOwing = 0.0, monInMonInAmt = 0.0, monInMonInOldAmt = 0.0, monInMonInNewAmt = 0.0,
            monInPercentA = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0,
            savRateFromDb = 0.0;
    General monInGen;
    IncomeBudgetDb monInIncDb;
    Intent monInToAddInc, monInToList, monInToFixBudget;
    ListView monInList;
    long monInIncId, monInIncRefKeyMI, monInToAcctId;
    MoneyInDb monInMoneyInDb;
    MonInAdapter monInAdapter;
    Spinner monInSpin;
    SQLiteDatabase monInDb, monInDb2;
    String monInAcctName = null, monInIsDebt = null, monInIncName = null, monInIsSav = null;
    TextView monInAddMoreTV, monInAvailAcctTV, monInAvailAmtLabel, monInBudgWarnTV, monInDepToTV, monInIncSourceTV, monInTotAcctTV;
    TransferSpinnerAdapter monInSpinAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3_layout_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        monInCur = monInDb2.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ACCTNAME + " ASC", null);
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
            monInIsDebt = monInCur.getString(monInCur.getColumnIndexOrThrow(DbHelper.ISDEBT));
            monInIsSav = monInCur.getString(monInCur.getColumnIndexOrThrow(DbHelper.ISSAV));
            monInToAcctId = monInCur.getLong(monInCur.getColumnIndexOrThrow(DbHelper.ID));
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void monInContinueTransaction() {
        if (monInIsDebt.equals("N") && monInIsSav.equals("N")) {
            monInPercentA = monInDbMgr.sumTotalAExpenses() / monInDbMgr.sumTotalIncome();

            monInMoneyInA = monInDbMgr.detAPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());
            monInMoneyInOwing = monInDbMgr.detOwingPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());
            monInMoneyInB = monInDbMgr.detBPortionInc(monInMonInAmt, (monInPercentA * monInMonInAmt), monInDbMgr.retrieveCurrentOwingA());

            monInDbMgr.updateTotAcctBalPlus(monInMonInAmt, monInDbMgr.retrieveCurrentAccountBalance());
            monInDbMgr.updateAvailBalPlus(monInMoneyInA, monInMoneyInOwing, monInMoneyInB, monInDbMgr.retrieveCurrentA(), monInDbMgr.retrieveCurrentOwingA(), monInDbMgr.retrieveCurrentB());

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
        } else if (monInIsDebt.equals("Y")) {
            monInDbMgr.updateDebtRecMinusPt1(monInMonInAmt, monInDbMgr.retrieveCurrentDebtAmtOwing(monInToAcctId), monInToAcctId);
            for (AccountsDb a : monInDbMgr.getDebts()) {
                if (a.getId() == monInToAcctId) {
                    debtAmtFromDb = a.getAcctBal();
                    debtLimitFromDb = a.getAcctMax();
                    debtRateFromDb = a.getIntRate();
                    debtPaytFromDb = a.getPaytsTo();
                }
            }
            monInDbMgr.updateDebtRecPt2(monInGen.calcDebtDate(
                    debtAmtFromDb,
                    debtRateFromDb,
                    debtPaytFromDb,
                    getString(R.string.debt_paid),
                    getString(R.string.too_far)), monInToAcctId);
            moneyInA = 0.0;
            moneyInOwing = 0.0;
            moneyInB = 0.0;
        } else if (monInIsSav.equals("Y")) {
            monInDbMgr.updateSavRecPlusPt1(monInMonInAmt, monInDbMgr.retrieveCurrentSavAmt(monInToAcctId), monInToAcctId);
            for (AccountsDb a : monInDbMgr.getSavings()) {
                if (a.getId() == monInToAcctId) {
                    savGoalFromDb = a.getAcctMax();
                    savAmtFromDb = a.getAcctBal();
                    savRateFromDb = a.getIntRate();
                    savPaytFromDb = a.getPaytsTo();
                }
            }
            monInDbMgr.updateSavRecPt2(monInGen.calcSavingsDate(
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

        monInMoneyInDb = new MoneyInDb(
                monInIncName,
                monInMonInAmt,
                moneyInA,
                moneyInOwing,
                moneyInB,
                monInToAcctId,
                monInAcctName,
                monInGen.createTimestamp(),
                monInIncId,
                0);
        monInDbMgr.addMoneyIn(monInMoneyInDb);

        monInIncDb.setIncomeAnnualAmount(monInDbMgr.makeNewIncAnnAmt(monInIncId, monInGen.lastNumOfDays(365)));
        monInDbMgr.updateIncome(monInIncDb);

        monInAdapter.updateIncomes(monInDbMgr.getIncomes());
        monInAdapter.notifyDataSetChanged();

        monInToList = new Intent(LayoutMoneyIn.this, LayoutMoneyInList.class);
        monInToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monInToList);
    }

    public class MonInAdapter extends ArrayAdapter<IncomeBudgetDb> {

        private Context context;
        private List<IncomeBudgetDb> incomes;

        private MonInAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<IncomeBudgetDb> incomes) {
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
                monInHldr.monInCatLayout = convertView.findViewById(R.id.paytCatLayout);
                monInHldr.monInCatLayout.setVisibility(View.GONE);
                monInHldr.monInDepLabel = convertView.findViewById(R.id.paytPayLabel);
                monInHldr.monInDepLabel.setText(getString(R.string.deposit));
                monInHldr.monInAmtTV = convertView.findViewById(R.id.paytAmtTV);
                monInHldr.monInNewAmtET = convertView.findViewById(R.id.paytAmtET);
                monInHldr.monInSaveButton = convertView.findViewById(R.id.paytSaveBtn);
                monInHldr.monInSaveButton.setVisibility(View.GONE);
                monInHldr.monInWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                monInHldr.monInWarnLayout.setVisibility(View.GONE);
                monInHldr.monInWarnTV = convertView.findViewById(R.id.paytWarnTV);
                monInHldr.monInYesContButton = convertView.findViewById(R.id.paytYesContBtn);
                monInHldr.monInNoContButton = convertView.findViewById(R.id.paytNoContBtn);
                monInHldr.monInDefLayout = convertView.findViewById(R.id.paytDefLayout);
                monInHldr.monInDefLayout.setVisibility(View.GONE);
                monInHldr.monInYesDefButton = convertView.findViewById(R.id.paytYesDefBtn);
                monInHldr.monInNoDefButton = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(monInHldr);

            } else {
                monInHldr = (MoneyInViewHolder) convertView.getTag();
            }

            monInHldr.monInCatTV.setText(incomes.get(position).getIncomeName());
            monInGen.dblASCurrency(String.valueOf(incomes.get(position).getIncomeAmount()), monInHldr.monInAmtTV);

            monInIncRefKeyMI = incomes.get(position).getId();

            monInHldr.monInCatTV.setTag(incomes.get(position));
            monInHldr.monInSaveButton.setTag(incomes.get(position));

            monInHldr.monInCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monInHldr.monInCatLayout.setVisibility(View.VISIBLE);
                    monInHldr.monInSaveButton.setVisibility(View.VISIBLE);

                    monInHldr.monInSaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInIncDb = (IncomeBudgetDb) monInHldr.monInSaveButton.getTag();

                            monInIncName = monInIncDb.getIncomeName();
                            monInIncId = monInIncDb.getId();

                            monInMonInOldAmt = monInIncDb.getIncomeAmount();
                            monInMonInNewAmt = monInGen.dblFromET(monInHldr.monInNewAmtET);

                            if (monInMonInNewAmt == 0) {
                                monInMonInAmt = monInMonInOldAmt;
                            } else {
                                monInMonInAmt = monInMonInNewAmt;
                            }

                            if (monInMonInAmt == monInMonInNewAmt) {
                                monInHldr.monInDefLayout.setVisibility(View.VISIBLE);

                                monInHldr.monInNoDefButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monInHldr.monInDefLayout.setVisibility(View.GONE);
                                        monInContinueTransaction();
                                    }
                                });

                                monInHldr.monInYesDefButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monInHldr.monInDefLayout.setVisibility(View.GONE);
                                        monInIncDb.setIncomeAmount(monInMonInAmt);
                                        monInDbMgr.updateIncome(monInIncDb);
                                        monInContinueTransaction();
                                    }
                                });
                            } else {
                                monInContinueTransaction();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView monInDepLabel;
        public TextView monInCatTV;
        public LinearLayout monInCatLayout;
        public TextView monInAmtTV;
        public EditText monInNewAmtET;
        public ImageButton monInSaveButton;
        public LinearLayout monInWarnLayout;
        public TextView monInWarnTV;
        public Button monInYesContButton;
        public Button monInNoContButton;
        public LinearLayout monInDefLayout;
        public Button monInYesDefButton;
        public Button monInNoDefButton;
    }
}
