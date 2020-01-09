package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutMoneyOut extends MainNavigation {

    BudgetDb monOutExpDb;
    Button transDialogSaveBtn, transDialogCancelBtn, transDialogNoDefBtn, transDialogYesDefBtn, transDialogNoContBtn, transDialogYesContBtn;
    ContentValues monOutCV;
    Cursor monOutCur, monOutCur2;
    DbHelper monOutHelper, monOutHelper2, monOutHelper3;
    DbManager monOutDbMgr;
    Double moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, monOutMoneyOutA = 0.0, monOutMoneyOutOwing = 0.0, monOutMoneyOutB = 0.0,
            monOutMonOutAmt = 0.0, monOutMonOutOldAmt = 0.0, monOutMonOutNewAmt = 0.0, monOutWeeklyLimit = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0,
            savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    EditText transDialogAmtET;
    General monOutGen;
    int clicked2 = 0, clickedE2 = 0;
    Intent monOutToRatings, monOutToAddMore, monOutRefresh, monOutToList, monOutToFixBudget;
    LinearLayout monOutHeaderLayout, transDialogDefLayout, transDialogWarnLayout;
    ListView monOutList;
    long monOutExpId, monOutMaxId, monOutFromAcctId;
    MonOutLstAdapter monOutLstAdapter;
    SharedPreferences sp, spE;
    Spinner monOutSpin;
    SQLiteDatabase monOutDb, monOutDb2, monOutDb3;
    String clicked2S = null, clickedE2S = null, monOutAcctName = null, monOutExpName = null, monOutExpPriority = null, monOutExpWeekly = null, monOutFromIsDebtSav = null;
    TextView monOutAddMoreTV, monOutBudgWarnTV, monOutAvailAcctTV, monOutAvailAmtLabel, monOutDepToTV, monOutIncSourceTV, monOutTotAcctTV,
            transDialogCatTV, transDialogPayLabel, transDialogAmtTV, transDialogWarnTV;
    TransactionsDb monOutMonOutDb;
    TransferSpinnerAdapter monOutSpinAdapter;
    View dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        monOutGen = new General();
        monOutDbMgr = new DbManager(this);

        monOutBudgWarnTV = findViewById(R.id.mainBudgetWarnTV);
        monOutBudgWarnTV.setVisibility(View.GONE);
        monOutHeaderLayout = findViewById(R.id.mainHeaderLayout);
        monOutHeaderLayout.setVisibility(View.VISIBLE);
        monOutTotAcctTV = findViewById(R.id.mainTotalAmtTV);
        monOutAvailAcctTV = findViewById(R.id.mainAvailAmtTV);
        monOutAvailAmtLabel = findViewById(R.id.mainAvailAmtLabel);
        monOutDepToTV = findViewById(R.id.mainLabel1);
        monOutDepToTV.setText(getString(R.string.money_from));
        monOutIncSourceTV = findViewById(R.id.mainLabel2);
        monOutIncSourceTV.setText(getString(R.string.spent_on));
        monOutAddMoreTV = findViewById(R.id.mainAddMoreTV);
        monOutAddMoreTV.setVisibility(View.VISIBLE);

        monOutAddMoreTV.setOnClickListener(onClickAddMoreTV);

        monOutSpin = findViewById(R.id.mainSpin);

        monOutDbMgr.mainHeaderText(
                monOutBudgWarnTV,
                monOutTotAcctTV,
                monOutAvailAcctTV,
                monOutAvailAmtLabel,
                monOutDbMgr.sumTotalExpenses(),
                monOutDbMgr.sumTotalIncome(),
                monOutDbMgr.retrieveCurrentAccountBalance(),
                monOutDbMgr.retrieveCurrentB(),
                monOutDbMgr.retrieveCurrentA());
        monOutBudgWarnTV.setOnClickListener(onClickMonOutBudgWarnTV);

        monOutHelper3 = new DbHelper(this);
        monOutDb3 = monOutHelper3.getReadableDatabase();
        monOutCur2 = monOutDb3.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ACCTDEBTSAV + " != 'D' " + " ORDER BY " + DbHelper.ID + " ASC", null);
        monOutSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), monOutCur2);
        monOutSpin.setAdapter(monOutSpinAdapter);

        monOutSpin.setOnItemSelectedListener(monOutSpinSel);

        monOutList = findViewById(R.id.mainListView);
        monOutList.setVisibility(View.VISIBLE);
        monOutLstAdapter = new MonOutLstAdapter(this, monOutDbMgr.getExpenses());
        monOutList.setAdapter(monOutLstAdapter);

        monOutCV = new ContentValues();
        monOutCV.put(DbHelper.LASTPAGEID, 3);
        monOutHelper2 = new DbHelper(this);
        monOutDb2 = monOutHelper2.getWritableDatabase();
        monOutDb2.update(DbHelper.CURRENT_TABLE_NAME, monOutCV, DbHelper.ID + "= '1'", null);
        monOutDb2.close();
    }

    public void monOutRefresh() {
        monOutRefresh = new Intent(this, LayoutMoneyOut.class);
        monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monOutRefresh);
    }

    View.OnClickListener onClickAddMoreTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutToAddMore = new Intent(LayoutMoneyOut.this, AddExpense.class);
            monOutToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutToAddMore);
        }
    };

    View.OnClickListener onClickMonOutBudgWarnTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutToFixBudget = new Intent(LayoutMoneyOut.this, LayoutBudget.class);
            monOutToFixBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutToFixBudget);
        }
    };

    public void noRatingsYet() {
        sp = getSharedPreferences(LayoutRatings.SRP, Context.MODE_PRIVATE);
        clicked2S = sp.getString(LayoutRatings.CT, "");
        if (!clicked2S.equals("")) {
            clicked2 = Integer.valueOf(clicked2S);
        } else {
            clicked2 = 0;
        }

        spE = getSharedPreferences(LayoutRatings.SRPE, Context.MODE_PRIVATE);
        clickedE2S = spE.getString(LayoutRatings.CTE, "");
        if (!clickedE2S.equals("")) {
            clickedE2 = Integer.valueOf(clickedE2S);
        } else {
            clickedE2 = 0;
        }

        monOutHelper = new DbHelper(this);
        monOutDb = monOutHelper.getReadableDatabase();
        monOutCur = monOutDb.rawQuery("SELECT max(_id)" + " FROM " + DbHelper.TRANSACTIONS_TABLE_NAME, null);
        monOutCur.moveToFirst();
        monOutMaxId = monOutCur.getLong(0);
        monOutCur.close();

        if (monOutMaxId != 0 && monOutMaxId % 10 == 0) {
            if (clicked2 == 0 && clickedE2 == 0) {
                monOutToRatings = new Intent(LayoutMoneyOut.this, LayoutRatings.class);
                monOutToRatings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(monOutToRatings);
            } else {
                monOutToList = new Intent(LayoutMoneyOut.this, LayoutMoneyOutList.class);
                monOutToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(monOutToList);
            }
        } else {
            monOutToList = new Intent(LayoutMoneyOut.this, LayoutMoneyOutList.class);
            monOutToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutToList);
        }
    }

    AdapterView.OnItemSelectedListener monOutSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monOutAcctName = monOutCur2.getString(monOutCur2.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            monOutFromIsDebtSav = monOutCur2.getString(monOutCur2.getColumnIndexOrThrow(DbHelper.ACCTDEBTSAV));
            monOutFromAcctId = monOutCur2.getLong(monOutCur2.getColumnIndexOrThrow(DbHelper.ID));
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void monOutChangeDefault() {
        monOutExpDb.setBdgtPaytAmt(monOutMonOutAmt);
        monOutDbMgr.updateBudget(monOutExpDb);
    }

    public void monOutContMainAcctTrans() {
        monOutMoneyOutA = monOutDbMgr.detAPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());
        monOutMoneyOutOwing = monOutDbMgr.detOwingPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());
        monOutMoneyOutB = monOutDbMgr.detBPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());

        monOutDbMgr.updateTotAcctBalMinus(monOutMonOutAmt, monOutDbMgr.retrieveCurrentAccountBalance());
        monOutDbMgr.updateAandBBalMinus(
                monOutMoneyOutA,
                monOutMoneyOutOwing,
                monOutMoneyOutB,
                monOutDbMgr.retrieveCurrentA(),
                monOutDbMgr.retrieveCurrentOwingA(),
                monOutDbMgr.retrieveCurrentB());

        if (monOutDbMgr.retrieveCurrentOwingA() < 0) {
            monOutDbMgr.adjustCurrentAandB(monOutDbMgr.retrieveCurrentOwingA(), monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());
            newMoneyA = monOutDbMgr.detNewAPortion(monOutMoneyOutA, monOutDbMgr.retrieveCurrentOwingA());
            newMoneyOwing = monOutDbMgr.detNewOwingPortion(monOutMoneyOutOwing, monOutDbMgr.retrieveCurrentOwingA());
            newMoneyB = monOutDbMgr.detNewBPortion(monOutMoneyOutB, monOutDbMgr.retrieveCurrentOwingA());
            moneyOutA = newMoneyA;
            moneyOutOwing = newMoneyOwing;
            moneyOutB = newMoneyB;
        } else {
            moneyOutA = monOutMoneyOutA;
            moneyOutOwing = monOutMoneyOutOwing;
            moneyOutB = monOutMoneyOutB;
        }

        monOutAddMoneyOut();
    }

    public void monOutContSavAcctTrans() {
        monOutDbMgr.updateRecMinusPt1(monOutMonOutAmt, monOutDbMgr.retrieveCurrentAcctAmt(monOutFromAcctId), monOutFromAcctId);
        for (AccountsDb a : monOutDbMgr.getSavings()) {
            if (a.getId() == monOutFromAcctId) {
                savGoalFromDb = a.getAcctMax();
                savAmtFromDb = a.getAcctBal();
                savRateFromDb = a.getAcctIntRate();
                savPaytFromDb = a.getAcctPaytsTo();
            }
        }
        monOutDbMgr.updateRecPt2(monOutGen.calcSavingsDate(
                savGoalFromDb,
                savAmtFromDb,
                savRateFromDb,
                savPaytFromDb,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)), monOutFromAcctId);
        moneyOutA = 0.0;
        moneyOutOwing = 0.0;
        moneyOutB = 0.0;

        monOutAddMoneyOut();
    }

    public void monOutAddMoneyOut() {
        monOutMonOutDb = new TransactionsDb(
                "out",
                "N",
                monOutExpName,
                monOutExpId,
                monOutMonOutAmt,
                0.0,
                0.0,
                0.0,
                moneyOutA,
                moneyOutOwing,
                moneyOutB,
                0,
                "N/A",
                "N/A",
                monOutFromAcctId,
                monOutAcctName,
                monOutFromIsDebtSav,
                monOutExpPriority,
                monOutExpWeekly,
                "N/A",
                "N/A",
                monOutGen.createTimestamp(),
                0);
        monOutDbMgr.addTransactions(monOutMonOutDb);

        monOutExpDb.setBdgtAnnPayt(monOutDbMgr.makeNewExpAnnAmt(monOutExpId, monOutGen.lastNumOfDays(365)));
        monOutDbMgr.updateBudget(monOutExpDb);

        monOutLstAdapter.updateExpenses(monOutDbMgr.getExpenses());
        monOutLstAdapter.notifyDataSetChanged();

        noRatingsYet();
    }

    public class MonOutLstAdapter extends ArrayAdapter<BudgetDb> {

        private Context context;
        private List<BudgetDb> expenses;

        private MonOutLstAdapter(
                Context context,
                List<BudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<BudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MonOutViewHolder monOutHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                monOutHldr = new MonOutViewHolder();
                monOutHldr.monOutCatTV = convertView.findViewById(R.id.paytCatTV);
                convertView.setTag(monOutHldr);

            } else {
                monOutHldr = (MonOutViewHolder) convertView.getTag();
            }

            monOutHldr.monOutCatTV.setText(expenses.get(position).getBdgtCat());

            monOutHldr.monOutCatTV.setTag(expenses.get(position));

            monOutHldr.monOutCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monOutExpDb = (BudgetDb) monOutHldr.monOutCatTV.getTag();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutMoneyOut.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_transaction, null);
                    builder.setView(dView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    transDialogCatTV = dView.findViewById(R.id.transDialogCatTV);
                    transDialogCatTV.setText(expenses.get(position).getBdgtCat());
                    transDialogPayLabel = dView.findViewById(R.id.transDialogPayLabel);
                    transDialogPayLabel.setText(R.string.spent);
                    transDialogAmtTV = dView.findViewById(R.id.transDialogAmtTV);
                    monOutGen.dblASCurrency(String.valueOf(expenses.get(position).getBdgtPaytAmt()), transDialogAmtTV);
                    transDialogAmtET = dView.findViewById(R.id.transDialogAmtET);
                    transDialogSaveBtn = dView.findViewById(R.id.transDialogSaveBtn);
                    transDialogCancelBtn = dView.findViewById(R.id.transDialogCancelBtn);
                    transDialogDefLayout = dView.findViewById(R.id.transDialogDefLayout);
                    transDialogDefLayout.setVisibility(View.GONE);
                    transDialogNoDefBtn = dView.findViewById(R.id.transDialogNoDefBtn);
                    transDialogYesDefBtn = dView.findViewById(R.id.transDialogYesDefBtn);
                    transDialogWarnLayout = dView.findViewById(R.id.transDialogWarnLayout);
                    transDialogWarnLayout.setVisibility(View.GONE);
                    transDialogWarnTV = dView.findViewById(R.id.transDialogWarnTV);
                    transDialogNoContBtn = dView.findViewById(R.id.transDialogNoContBtn);
                    transDialogYesContBtn = dView.findViewById(R.id.transDialogYesContBtn);

                    transDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutRefresh();
                        }
                    });

                    transDialogSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monOutExpName = monOutExpDb.getBdgtCat();
                            monOutExpId = monOutExpDb.getId();
                            monOutWeeklyLimit = monOutExpDb.getBdgtAnnPayt() / 52;

                            monOutMonOutOldAmt = monOutExpDb.getBdgtPaytAmt();
                            monOutMonOutNewAmt = monOutGen.dblFromET(transDialogAmtET);

                            if (monOutMonOutNewAmt == 0) {
                                monOutMonOutAmt = monOutMonOutOldAmt;
                            } else {
                                monOutMonOutAmt = monOutMonOutNewAmt;
                            }

                            monOutExpPriority = monOutExpDb.getBdgtPriority();
                            monOutExpWeekly = monOutExpDb.getBdgtWeekly();

                            if (monOutExpWeekly.equals("Y") && (monOutDbMgr.checkOverWeekly(monOutExpId) + monOutMonOutAmt > monOutWeeklyLimit)) { //IF OVER WEEKLY LIMIT
                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                transDialogWarnTV.setText(getString(R.string.over_weekly));

                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        monOutRefresh();
                                    }
                                });

                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);

                                        if (monOutFromIsDebtSav.equals("S")) { //FROM SAV ACCT
                                            if (monOutDbMgr.retrieveCurrentAcctAmt(monOutFromAcctId) - monOutMonOutAmt < 0) { //WILL GO NEGATIVE
                                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                                transDialogWarnTV.setText(getString(R.string.not_enough_savings_warning));

                                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        monOutRefresh();
                                                    }
                                                });

                                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        if (monOutMonOutAmt == monOutMonOutNewAmt) { //IF ENTERED NEW AMT
                                                            transDialogDefLayout.setVisibility(View.VISIBLE);

                                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutContSavAcctTrans();
                                                                }
                                                            });

                                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutChangeDefault();
                                                                    monOutContSavAcctTrans();
                                                                }
                                                            });
                                                        } else {
                                                            monOutContSavAcctTrans();
                                                        }
                                                    }
                                                });
                                            } else { //WON'T GO NEGATIVE
                                                if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                                    transDialogDefLayout.setVisibility(View.VISIBLE);

                                                    transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            monOutContSavAcctTrans();
                                                        }
                                                    });

                                                    transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            monOutChangeDefault();
                                                            monOutContSavAcctTrans();
                                                        }
                                                    });
                                                } else {
                                                    monOutContSavAcctTrans();
                                                }
                                            }
                                        } else { //FROM MAIN ACCT
                                            if (monOutExpPriority.equals("A") && (monOutDbMgr.retrieveCurrentAccountBalance() - monOutMonOutAmt < 0)) { //PRIORITY IS A AND WILL GO NEGATIVE
                                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                                transDialogWarnTV.setText(getString(R.string.payment_not_possible_A));

                                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        monOutRefresh();
                                                    }
                                                });

                                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        if (monOutMonOutAmt == monOutMonOutNewAmt) { //ENTERED NEW AMT
                                                            transDialogDefLayout.setVisibility(View.VISIBLE);

                                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutContMainAcctTrans();
                                                                }
                                                            });

                                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutChangeDefault();
                                                                    monOutContMainAcctTrans();
                                                                }
                                                            });
                                                        } else { //DID NOT ENTER NEW AMT
                                                            monOutContMainAcctTrans();
                                                        }
                                                    }
                                                });
                                            } else if (monOutExpPriority.equals("B") && (monOutDbMgr.retrieveCurrentB() - monOutMonOutAmt < 0)) { //PRIORITY IS B AND WILL GO NEGATIVE
                                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                                transDialogWarnTV.setText(getString(R.string.payment_not_possible_B));

                                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        monOutRefresh();
                                                    }
                                                });

                                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        if (monOutMonOutAmt == monOutMonOutNewAmt) { //ENTERED NEW AMT
                                                            transDialogDefLayout.setVisibility(View.VISIBLE);

                                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutContMainAcctTrans();
                                                                }
                                                            });

                                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    monOutChangeDefault();
                                                                    monOutContMainAcctTrans();
                                                                }
                                                            });
                                                        } else { //DID NOT ENTER NEW AMT
                                                            monOutContMainAcctTrans();
                                                        }
                                                    }
                                                });
                                            } else { //WON'T GO NEGATIVE
                                                if (monOutMonOutAmt == monOutMonOutNewAmt) { //ENTERED NEW AMT
                                                    transDialogDefLayout.setVisibility(View.VISIBLE);

                                                    transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });

                                                    transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            monOutChangeDefault();
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });
                                                } else { //DID NOT ENTER NEW AMT
                                                    monOutContMainAcctTrans();
                                                }
                                            }
                                        }
                                    }
                                });
                            } else if (monOutFromIsDebtSav.equals("S")) { //NOT OVER WEEKLY LIMIT && FROM SAV ACCT
                                if (monOutDbMgr.retrieveCurrentAcctAmt(monOutFromAcctId) - monOutMonOutAmt < 0) { //WILL GO NEGATIVE
                                    transDialogWarnLayout.setVisibility(View.VISIBLE);
                                    transDialogWarnTV.setText(getString(R.string.not_enough_savings_warning));

                                    transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogWarnLayout.setVisibility(View.GONE);
                                            monOutRefresh();
                                        }
                                    });

                                    transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogWarnLayout.setVisibility(View.GONE);
                                            if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                                transDialogDefLayout.setVisibility(View.VISIBLE);

                                                transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        monOutContSavAcctTrans();
                                                    }
                                                });

                                                transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        monOutChangeDefault();
                                                        monOutContSavAcctTrans();
                                                    }
                                                });
                                            } else {
                                                monOutContSavAcctTrans();
                                            }
                                        }
                                    });
                                } else {  //WON'T GO NEGATIVE
                                    if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                        transDialogDefLayout.setVisibility(View.VISIBLE);

                                        transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transDialogDefLayout.setVisibility(View.GONE);
                                                monOutContSavAcctTrans();
                                            }
                                        });

                                        transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transDialogDefLayout.setVisibility(View.GONE);
                                                monOutChangeDefault();
                                                monOutContSavAcctTrans();
                                            }
                                        });
                                    } else {
                                        monOutContSavAcctTrans();
                                    }
                                }
                            } else if (monOutExpPriority.equals("A") && (monOutDbMgr.retrieveCurrentAccountBalance() - monOutMonOutAmt < 0)) { //PRIORITY IS A AND WILL GO NEGATIVE
                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                transDialogWarnTV.setText(getString(R.string.payment_not_possible_A));

                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        monOutRefresh();
                                    }
                                });

                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                            transDialogDefLayout.setVisibility(View.VISIBLE);

                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                    monOutContMainAcctTrans();
                                                }
                                            });

                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                    monOutChangeDefault();
                                                    monOutContMainAcctTrans();
                                                }
                                            });
                                        } else {
                                            monOutContMainAcctTrans();
                                        }
                                    }
                                });
                            } else if (monOutExpPriority.equals("B") && (monOutDbMgr.retrieveCurrentB() - monOutMonOutAmt < 0)) { //PRIORITY IS B AND WILL GO NEGATIVE
                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                transDialogWarnTV.setText(getString(R.string.payment_not_possible_B));

                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        monOutRefresh();
                                    }
                                });

                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                            transDialogDefLayout.setVisibility(View.VISIBLE);

                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                    monOutContMainAcctTrans();
                                                }
                                            });

                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                    monOutChangeDefault();
                                                    monOutContMainAcctTrans();
                                                }
                                            });
                                        } else {
                                            monOutContMainAcctTrans();
                                        }
                                    }
                                });
                            } else { //WON'T GO NEGATIVE
                                if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                    transDialogDefLayout.setVisibility(View.VISIBLE);

                                    transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogDefLayout.setVisibility(View.GONE);
                                            monOutContMainAcctTrans();
                                        }
                                    });

                                    transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogDefLayout.setVisibility(View.GONE);
                                            monOutChangeDefault();
                                            monOutContMainAcctTrans();
                                        }
                                    });
                                } else {
                                    monOutContMainAcctTrans();
                                }
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }

    private static class MonOutViewHolder {
        public TextView monOutCatTV;
    }
}
