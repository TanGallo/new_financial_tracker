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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
//import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
//import ca.gotchasomething.mynance.data.SavingsDb;

public class LayoutMoneyOut extends MainNavigation {

    BudgetDb monOutExpDb;
    ContentValues monOutCV;
    Cursor monOutCur, monOutCur2;
    DbHelper monOutHelper, monOutHelper2, monOutHelper3;
    DbManager monOutDbMgr;
    Double moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, monOutMoneyOutA = 0.0, monOutMoneyOutOwing = 0.0, monOutMoneyOutB = 0.0,
            monOutMonOutAmt = 0.0, monOutMonOutOldAmt = 0.0, monOutMonOutNewAmt = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0,
            savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    General monOutGen;
    int clicked2 = 0, clickedE2 = 0;
    Intent monOutToRatings, monOutToAddMore, monOutRefresh, monOutToList, monOutToFixBudget;
    LinearLayout monOutHeaderLayout;
    ListView monOutList;
    long monOutExpId, monOutMaxId, monOutExpRefKeyMO, monOutSavId, monOutFromAcctId;
    MonOutLstAdapter monOutLstAdapter;
    SharedPreferences sp, spE;
    Spinner monOutSpin;
    SQLiteDatabase monOutDb, monOutDb2, monOutDb3;
    String clicked2S = null, clickedE2S = null, monOutAcctName = null, monOutExpName = null, monOutExpPriority = null, monOutExpWeekly = null, monOutFromIsSav = null;
    TextView monOutAddMoreTV, monOutBudgWarnTV, monOutAvailAcctTV, monOutAvailAmtLabel, monOutDepToTV, monOutIncSourceTV, monOutTotAcctTV;
    TransactionsDb monOutMonOutDb;
    TransferSpinnerAdapter monOutSpinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        monOutCur2 = monOutDb3.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ACCTISDEBT + " = 'N' " + " ORDER BY " + DbHelper.ACCTNAME + " ASC", null);
        monOutSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), monOutCur2);
        monOutSpin.setAdapter(monOutSpinAdapter);

        monOutSpin.setOnItemSelectedListener(monOutSpinSel);

        monOutList = findViewById(R.id.mainListView);
        monOutList.setVisibility(View.VISIBLE);
        monOutLstAdapter = new MonOutLstAdapter(this, monOutDbMgr.getExpense());
        monOutList.setAdapter(monOutLstAdapter);

        monOutCV = new ContentValues();
        monOutCV.put(DbHelper.LASTPAGEID, 3);
        monOutHelper2 = new DbHelper(this);
        monOutDb2 = monOutHelper2.getWritableDatabase();
        monOutDb2.update(DbHelper.CURRENT_TABLE_NAME, monOutCV, DbHelper.ID + "= '1'", null);
        monOutDb2.close();
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
            monOutFromIsSav = monOutCur2.getString(monOutCur2.getColumnIndexOrThrow(DbHelper.ACCTISSAV));
            monOutFromAcctId = monOutCur2.getLong(monOutCur2.getColumnIndexOrThrow(DbHelper.ID));
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void monOutChangeDefault() {
        monOutExpDb.setBdgtPaytAmt(monOutMonOutAmt);
        monOutDbMgr.updateExpense(monOutExpDb);
    }

    public void monOutContMainAcctTrans() {
        monOutMoneyOutA = monOutDbMgr.detAPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());
        monOutMoneyOutOwing = monOutDbMgr.detOwingPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());
        monOutMoneyOutB = monOutDbMgr.detBPortionExp(monOutMonOutAmt, monOutExpPriority, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentB());

        monOutDbMgr.updateTotAcctBalMinus(monOutMonOutAmt, monOutDbMgr.retrieveCurrentAccountBalance());
        monOutDbMgr.updateAvailBalMinus(monOutMoneyOutA, monOutMoneyOutOwing, monOutMoneyOutB, monOutDbMgr.retrieveCurrentA(), monOutDbMgr.retrieveCurrentOwingA(), monOutDbMgr.retrieveCurrentB());

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
        monOutDbMgr.updateRecMinusPt1(monOutMonOutAmt, monOutDbMgr.retrieveCurrentSavAmt(monOutFromAcctId), monOutFromAcctId);
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
                "N/A",
                monOutFromAcctId,
                monOutAcctName,
                "N",
                monOutFromIsSav,
                monOutExpPriority,
                monOutExpWeekly,
                "N/A",
                "N/A",
                monOutGen.createTimestamp(),
                0);
        monOutDbMgr.addMoneyOut(monOutMonOutDb);

        monOutExpDb.setBdgtAnnPayt(monOutDbMgr.makeNewExpAnnAmt(monOutExpId, monOutGen.lastNumOfDays(365)));
        monOutDbMgr.updateExpense(monOutExpDb);

        monOutLstAdapter.updateExpenses(monOutDbMgr.getExpense());
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
                monOutHldr.monOutCatLayout = convertView.findViewById(R.id.paytCatLayout);
                monOutHldr.monOutCatLayout.setVisibility(View.GONE);
                monOutHldr.monOutDepLabel = convertView.findViewById(R.id.paytPayLabel);
                monOutHldr.monOutDepLabel.setText(getString(R.string.spent));
                monOutHldr.monOutAmtTV = convertView.findViewById(R.id.paytAmtTV);
                monOutHldr.monOutNewAmtET = convertView.findViewById(R.id.paytAmtET);
                monOutHldr.monOutSaveButton = convertView.findViewById(R.id.paytSaveBtn);
                monOutHldr.monOutSaveButton.setVisibility(View.GONE);
                monOutHldr.monOutWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                monOutHldr.monOutWarnTV = convertView.findViewById(R.id.paytWarnTV);
                monOutHldr.monOutYesContButton = convertView.findViewById(R.id.paytYesContBtn);
                monOutHldr.monOutNoContButton = convertView.findViewById(R.id.paytNoContBtn);
                monOutHldr.monOutDefLayout = convertView.findViewById(R.id.paytDefLayout);
                monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                monOutHldr.monOutYesDefButton = convertView.findViewById(R.id.paytYesDefBtn);
                monOutHldr.monOutNoDefButton = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(monOutHldr);

            } else {
                monOutHldr = (MonOutViewHolder) convertView.getTag();
            }

            monOutHldr.monOutCatTV.setText(expenses.get(position).getBdgtCat());
            monOutGen.dblASCurrency(String.valueOf(expenses.get(position).getBdgtPaytAmt()), monOutHldr.monOutAmtTV);

            monOutExpRefKeyMO = expenses.get(position).getId();

            monOutHldr.monOutCatTV.setTag(expenses.get(position));
            monOutHldr.monOutSaveButton.setTag(expenses.get(position));

            monOutHldr.monOutCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monOutHldr.monOutCatLayout.setVisibility(View.VISIBLE);
                    monOutHldr.monOutSaveButton.setVisibility(View.VISIBLE);

                    monOutHldr.monOutSaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutExpDb = (BudgetDb) monOutHldr.monOutSaveButton.getTag();

                            monOutExpName = monOutExpDb.getBdgtCat();
                            monOutExpId = monOutExpDb.getId();

                            monOutMonOutOldAmt = monOutExpDb.getBdgtPaytAmt();
                            monOutMonOutNewAmt = monOutGen.dblFromET(monOutHldr.monOutNewAmtET);

                            if (monOutMonOutNewAmt == 0) {
                                monOutMonOutAmt = monOutMonOutOldAmt;
                            } else {
                                monOutMonOutAmt = monOutMonOutNewAmt;
                            }

                            monOutExpPriority = monOutExpDb.getBdgtPriority();
                            monOutExpWeekly = monOutExpDb.getBdgtWeekly();

                            if (monOutSavId > 0) {
                                if (monOutDbMgr.retrieveCurrentSavAmt(monOutSavId) - monOutMonOutAmt < 0) {
                                    monOutHldr.monOutWarnLayout.setVisibility(View.VISIBLE);
                                    monOutHldr.monOutWarnTV.setText(getString(R.string.not_enough_savings_warning));

                                    monOutHldr.monOutNoContButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                            monOutRefresh = new Intent(getContext(), LayoutMoneyOut.class);
                                            monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                            startActivity(monOutRefresh);
                                        }
                                    });

                                    monOutHldr.monOutYesContButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                            if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                                monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                                monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                        monOutContSavAcctTrans();
                                                    }
                                                });

                                                monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                        monOutChangeDefault();
                                                        monOutContSavAcctTrans();
                                                    }
                                                });
                                            } else {
                                                monOutContSavAcctTrans();
                                            }
                                        }
                                    });
                                } else {
                                    if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                        monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                        monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                monOutContSavAcctTrans();
                                            }
                                        });

                                        monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                monOutChangeDefault();
                                                monOutContSavAcctTrans();
                                            }
                                        });
                                    } else {
                                        monOutContSavAcctTrans();
                                    }
                                }
                            } else {
                                if (monOutExpPriority.equals("A")) {
                                    if (monOutDbMgr.retrieveCurrentAccountBalance() - monOutMonOutAmt < 0) { //A NOT POSSIBLE
                                        monOutHldr.monOutWarnLayout.setVisibility(View.VISIBLE);
                                        monOutHldr.monOutWarnTV.setText(getString(R.string.payment_not_possible_A));

                                        monOutHldr.monOutNoContButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                                monOutRefresh = new Intent(getContext(), LayoutMoneyOut.class);
                                                monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                startActivity(monOutRefresh);
                                            }
                                        });

                                        monOutHldr.monOutYesContButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                                if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                                    monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });

                                                    monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                            monOutChangeDefault();
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });
                                                } else {
                                                    monOutContMainAcctTrans();
                                                }
                                            }
                                        });
                                    } else {
                                        if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                            monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                            monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                    monOutContMainAcctTrans();
                                                }
                                            });

                                            monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                    monOutChangeDefault();
                                                    monOutContMainAcctTrans();
                                                }
                                            });
                                        } else {
                                            monOutContMainAcctTrans();
                                        }
                                    }
                                } else if (monOutExpPriority.equals("B")) {
                                    if (monOutDbMgr.retrieveCurrentB() - monOutMonOutAmt < 0) { //B NOT POSSIBLE
                                        monOutHldr.monOutWarnLayout.setVisibility(View.VISIBLE);
                                        monOutHldr.monOutWarnTV.setText(getString(R.string.payment_not_possible_B));

                                        monOutHldr.monOutNoContButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                                monOutRefresh = new Intent(getContext(), LayoutMoneyOut.class);
                                                monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                startActivity(monOutRefresh);
                                            }
                                        });

                                        monOutHldr.monOutYesContButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                                if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                                    monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });

                                                    monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                            monOutChangeDefault();
                                                            monOutContMainAcctTrans();
                                                        }
                                                    });
                                                } else {
                                                    monOutContMainAcctTrans();
                                                }
                                            }
                                        });
                                    } else {
                                        if (monOutMonOutAmt == monOutMonOutNewAmt) {
                                            monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                                            monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                    monOutContMainAcctTrans();
                                                }
                                            });

                                            monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                                    monOutChangeDefault();
                                                    monOutContMainAcctTrans();
                                                }
                                            });
                                        } else {
                                            monOutContMainAcctTrans();
                                        }
                                    }
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
        public TextView monOutDepLabel;
        public LinearLayout monOutCatLayout;
        public TextView monOutAmtTV;
        public EditText monOutNewAmtET;
        public ImageButton monOutSaveButton;
        public LinearLayout monOutWarnLayout;
        public TextView monOutWarnTV;
        public Button monOutYesContButton;
        public Button monOutNoContButton;
        public LinearLayout monOutDefLayout;
        public Button monOutYesDefButton;
        public Button monOutNoDefButton;
    }
}
