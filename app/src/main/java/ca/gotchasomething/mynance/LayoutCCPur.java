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

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class LayoutCCPur extends MainNavigation {

    BudgetDb layCCPurExpDb;
    Button transDialogCancelBtn, transDialogNoContBtn, transDialogNoDefBtn, transDialogSaveBtn, transDialogYesContBtn, transDialogYesDefBtn;
    ContentValues layCCPurCV;
    Cursor layCCPurCur;
    DbHelper layCCPurHelper, layCCPurHelper2;
    DbManager layCCPurDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, layCCPurMonOutAmt = 0.0, layCCPurMonOutNewAmt = 0.0,
            layCCPurMonOutOldAmt = 0.0, layCCPurWeeklyLimit = 0.0;
    EditText transDialogAmtET;
    General layCCPurGen;
    Intent layCCPurToAddCC, layCCPurToAddMore, layCCPurToFixBudget, layCCPurToList, layCCPurRefresh;
    LayCCPurLstAdapter layCCPurListAdapter;
    LinearLayout layCCPurHeaderLayout, transDialogDefLayout, transDialogWarnLayout;
    ListView layCCPurList;
    long layCCPurChargingDebtIdFromSpin, layCCPurExpId;
    TransferSpinnerAdapter layCCPurSpinAdapter;
    Spinner layCCPurSpin;
    SQLiteDatabase layCCPurDb, layCCPurDb2;
    String layCCPurChargingDebtNameFromSpin = null, layCCPurExpName = null, layCCPurExpPriority = null, layCCPurExpWeekly = null;
    TabLayout layCCPurTabLay;
    TextView layCCPurAddMoreTV, layCCPurAvailAcctTV, layCCPurAvailAmtLabel, layCCPurBudgWarnTV, layCCPurLabel2, layCCPurNewCCTV, layCCPurTotAcctTV,
            transDialogAmtTV, transDialogCatTV, transDialogPayLabel, transDialogWarnTV;
    TransactionsDb layCCPurMonOutDb;
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

        layCCPurDbMgr = new DbManager(this);
        layCCPurGen = new General();

        layCCPurBudgWarnTV = findViewById(R.id.mainBudgetWarnTV);
        layCCPurBudgWarnTV.setVisibility(View.GONE);
        layCCPurHeaderLayout = findViewById(R.id.mainHeaderLayout);
        layCCPurHeaderLayout.setVisibility(View.VISIBLE);
        layCCPurTotAcctTV = findViewById(R.id.mainTotalAmtTV);
        layCCPurAvailAcctTV = findViewById(R.id.mainAvailAmtTV);
        layCCPurAvailAmtLabel = findViewById(R.id.mainAvailAmtLabel);
        layCCPurNewCCTV = findViewById(R.id.mainLabel1);
        layCCPurNewCCTV.setText(getString(R.string.new_cc));
        layCCPurLabel2 = findViewById(R.id.mainLabel2);
        layCCPurLabel2.setVisibility(View.GONE);
        layCCPurAddMoreTV = findViewById(R.id.mainAddMoreTV);
        layCCPurAddMoreTV.setVisibility(View.VISIBLE);

        layCCPurNewCCTV.setOnClickListener(onClickLayCCPurNewCCTV);
        layCCPurAddMoreTV.setOnClickListener(onClickLayCCPurAddMoreTV);

        layCCPurDbMgr.mainHeaderText(
                layCCPurBudgWarnTV,
                layCCPurTotAcctTV,
                layCCPurAvailAcctTV,
                layCCPurAvailAmtLabel,
                layCCPurDbMgr.sumTotalExpenses(),
                layCCPurDbMgr.sumTotalIncome(),
                layCCPurDbMgr.retrieveCurrentAccountBalance(),
                layCCPurDbMgr.retrieveCurrentB(),
                layCCPurDbMgr.retrieveCurrentA());
        layCCPurBudgWarnTV.setOnClickListener(onClickLayCCPurBudgWarnTV);

        layCCPurTabLay = findViewById(R.id.mainCCTabLayout);

        layCCPurSpin = findViewById(R.id.mainSpin);

        layCCPurHelper = new DbHelper(this);
        layCCPurDb = layCCPurHelper.getReadableDatabase();
        layCCPurCur = layCCPurDb.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ACCTDEBTSAV + " = 'D'" + " ORDER BY " + DbHelper.ID + " ASC", null);
        layCCPurSpinAdapter = new TransferSpinnerAdapter(getApplicationContext(), layCCPurCur);
        layCCPurSpin.setAdapter(layCCPurSpinAdapter);

        layCCPurSpin.setOnItemSelectedListener(layCCPurSpinSel);

        layCCPurList = findViewById(R.id.mainListView);
        layCCPurList.setVisibility(View.VISIBLE);
        layCCPurListAdapter = new LayCCPurLstAdapter(this, layCCPurDbMgr.getExpenses());
        layCCPurList.setAdapter(layCCPurListAdapter);

        layCCPurCV = new ContentValues();
        layCCPurCV.put(DbHelper.LASTPAGEID, 7);
        layCCPurHelper2 = new DbHelper(this);
        layCCPurDb2 = layCCPurHelper2.getWritableDatabase();
        layCCPurDb2.update(DbHelper.CURRENT_TABLE_NAME, layCCPurCV, DbHelper.ID + "= '1'", null);
        layCCPurDb2.close();
    }

    public void layCCPurRefresh() {
        layCCPurRefresh = new Intent(this, LayoutCCPur.class);
        layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPurRefresh);
    }

    View.OnClickListener onClickLayCCPurAddMoreTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layCCPurToAddMore = new Intent(LayoutCCPur.this, AddExpense.class);
            layCCPurToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layCCPurToAddMore);
        }
    };

    View.OnClickListener onClickLayCCPurNewCCTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layCCPurToAddCC = new Intent(LayoutCCPur.this, AddDebts.class);
            layCCPurToAddCC.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layCCPurToAddCC);
        }
    };

    View.OnClickListener onClickLayCCPurBudgWarnTV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layCCPurToFixBudget = new Intent(LayoutCCPur.this, LayoutBudget.class);
            layCCPurToFixBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layCCPurToFixBudget);
        }
    };

    AdapterView.OnItemSelectedListener layCCPurSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            layCCPurChargingDebtNameFromSpin = layCCPurCur.getString(layCCPurCur.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            layCCPurChargingDebtIdFromSpin = layCCPurCur.getLong(layCCPurCur.getColumnIndexOrThrow(DbHelper.ID));
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void layCCPurChangeDefault() {
        layCCPurExpDb.setBdgtPaytAmt(layCCPurMonOutAmt);
        layCCPurDbMgr.updateBudget(layCCPurExpDb);
        layCCPurContTransaction();
    }

    public void layCCPurContTransaction() {

        layCCPurDbMgr.updateRecPlusPt1(layCCPurMonOutAmt, debtAmtFromDb, layCCPurChargingDebtIdFromSpin);
        for (AccountsDb a : layCCPurDbMgr.getDebts()) {
            if (a.getId() == layCCPurChargingDebtIdFromSpin) {
                debtAmtFromDb = a.getAcctBal();
                debtLimitFromDb = a.getAcctMax();
                debtRateFromDb = a.getAcctIntRate();
                debtPaytFromDb = a.getAcctPaytsTo();
            }
        }
        layCCPurDbMgr.updateRecPt2(layCCPurGen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), layCCPurChargingDebtIdFromSpin);

        layCCPurMonOutDb = new TransactionsDb(
                "out",
                "Y",
                layCCPurExpName,
                layCCPurExpId,
                layCCPurMonOutAmt,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0,
                "N/A",
                "N/A",
                layCCPurChargingDebtIdFromSpin,
                layCCPurChargingDebtNameFromSpin,
                "D",
                layCCPurExpPriority,
                layCCPurExpWeekly,
                "N",
                "N",
                layCCPurGen.createTimestamp(),
                0);
        layCCPurDbMgr.addTransactions(layCCPurMonOutDb);

        layCCPurExpDb.setBdgtAnnPayt(layCCPurDbMgr.makeNewExpAnnAmt(layCCPurExpId, layCCPurGen.lastNumOfDays(365)));
        layCCPurDbMgr.updateBudget(layCCPurExpDb);

        layCCPurListAdapter.updateExpenses(layCCPurDbMgr.getExpenses());
        layCCPurListAdapter.notifyDataSetChanged();

        layCCPurToList = new Intent(LayoutCCPur.this, LayoutCCPurList.class);
        layCCPurToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPurToList);
    }

    public class LayCCPurLstAdapter extends ArrayAdapter<BudgetDb> {

        private Context context;
        private List<BudgetDb> expenses;

        private LayCCPurLstAdapter(
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

            final LayCCPurViewHolder layCCPurHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                layCCPurHldr = new LayCCPurViewHolder();
                layCCPurHldr.layCCPurPaytCatTV = convertView.findViewById(R.id.paytCatTV);
                convertView.setTag(layCCPurHldr);

            } else {
                layCCPurHldr = (LayCCPurViewHolder) convertView.getTag();
            }

            layCCPurHldr.layCCPurPaytCatTV.setText(expenses.get(position).getBdgtCat());

            layCCPurHldr.layCCPurPaytCatTV.setTag(expenses.get(position));

            layCCPurHldr.layCCPurPaytCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layCCPurExpDb = (BudgetDb) layCCPurHldr.layCCPurPaytCatTV.getTag();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(LayoutCCPur.this);
                    dView = getLayoutInflater().inflate(R.layout.dialog_transaction, null);
                    builder.setView(dView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    transDialogCatTV = dView.findViewById(R.id.transDialogCatTV);
                    transDialogCatTV.setText(expenses.get(position).getBdgtCat());
                    transDialogPayLabel = dView.findViewById(R.id.transDialogPayLabel);
                    transDialogPayLabel.setText(R.string.spent);
                    transDialogAmtTV = dView.findViewById(R.id.transDialogAmtTV);
                    layCCPurGen.dblASCurrency(String.valueOf(expenses.get(position).getBdgtPaytAmt()), transDialogAmtTV);
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
                            layCCPurRefresh();
                        }
                    });

                    transDialogSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            layCCPurExpName = layCCPurExpDb.getBdgtCat();
                            layCCPurExpId = layCCPurExpDb.getId();
                            layCCPurWeeklyLimit = layCCPurExpDb.getBdgtAnnPayt() / 52;

                            layCCPurMonOutOldAmt = expenses.get(position).getBdgtPaytAmt();
                            layCCPurMonOutNewAmt = layCCPurGen.dblFromET(transDialogAmtET);

                            if (layCCPurMonOutNewAmt == 0) {
                                layCCPurMonOutAmt = layCCPurMonOutOldAmt;
                            } else {
                                layCCPurMonOutAmt = layCCPurMonOutNewAmt;
                            }

                            layCCPurExpPriority = layCCPurExpDb.getBdgtPriority();
                            layCCPurExpWeekly = layCCPurExpDb.getBdgtWeekly();

                            for (AccountsDb a : layCCPurDbMgr.getDebts()) {
                                if (a.getId() == layCCPurChargingDebtIdFromSpin) {
                                    debtAmtFromDb = a.getAcctBal();
                                    debtLimitFromDb = a.getAcctMax();
                                    debtRateFromDb = a.getAcctIntRate();
                                    debtPaytFromDb = a.getAcctPaytsTo();
                                }
                            }

                            if (layCCPurExpWeekly.equals("Y") && (layCCPurDbMgr.checkOverWeekly(layCCPurExpId) + layCCPurMonOutAmt > layCCPurWeeklyLimit)) { //IF OVER WEEKLY LIMIT
                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                transDialogWarnTV.setText(getString(R.string.over_weekly));

                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        layCCPurRefresh();
                                    }
                                });

                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        if (debtAmtFromDb + layCCPurMonOutAmt > debtLimitFromDb) { //CARD IS OVER LIMIT
                                            //SHOW WARNING
                                            transDialogWarnLayout.setVisibility(View.VISIBLE);
                                            transDialogWarnTV.setText(getString(R.string.not_enough_credit_warning));
                                            //NO CONTINUE
                                            transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogWarnLayout.setVisibility(View.GONE);
                                                    layCCPurRefresh();
                                                }
                                            });
                                            //YES CONTINUE
                                            transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogWarnLayout.setVisibility(View.GONE);
                                                    if ((layCCPurExpPriority.equals("A") && (layCCPurDbMgr.retrieveCurrentAccountBalance() - layCCPurMonOutAmt < 0)) || (layCCPurExpPriority.equals("B") && (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0))) {  //PRIORITY IS A OR B AND WILL GO NEGATIVE
                                                        //SHOW WARNING
                                                        transDialogWarnLayout.setVisibility(View.VISIBLE);
                                                        transDialogWarnTV.setText(getString(R.string.cc_payment_not_possible));
                                                        //NO CONTINUE
                                                        transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                transDialogWarnLayout.setVisibility(View.GONE);
                                                                layCCPurRefresh();
                                                            }
                                                        });
                                                        //YES CONTINUE
                                                        transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                transDialogWarnLayout.setVisibility(View.GONE);
                                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                                    transDialogDefLayout.setVisibility(View.VISIBLE);
                                                                    //NO CHANGE DEFAULT
                                                                    transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                                            layCCPurContTransaction();
                                                                        }
                                                                    });
                                                                    //YES CHANGE DEFAULT
                                                                    transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                                            layCCPurChangeDefault();
                                                                        }
                                                                    });
                                                                } else { //MONEY IN = DEFAULT AMT
                                                                    layCCPurContTransaction();
                                                                }
                                                            }
                                                        });
                                                    } else { //WILL NOT GO NEGATIVE
                                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                            //SHOW OPTION TO CHANGE DEFAULT
                                                            transDialogDefLayout.setVisibility(View.VISIBLE);
                                                            //NO CHANGE DEFAULT
                                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    layCCPurContTransaction();
                                                                }
                                                            });
                                                            //YES CHANGE DEFAULT
                                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    layCCPurChangeDefault();
                                                                }
                                                            });
                                                        } else { //MONEY IN = DEFAULT AMT
                                                            layCCPurContTransaction();
                                                        }
                                                    }
                                                }
                                            });
                                        } else { //CARD NOT OVER LIMIT
                                            if ((layCCPurExpPriority.equals("A") && (layCCPurDbMgr.retrieveCurrentAccountBalance() - layCCPurMonOutAmt < 0)) || (layCCPurExpPriority.equals("B") && (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0))) { //PRIORITY IS A OR B AND WILL GO NEAGITVE
                                                //SHOW WARNING
                                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                                transDialogWarnTV.setText(getString(R.string.cc_payment_not_possible));
                                                //NO CONTINUE
                                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        layCCPurRefresh();
                                                    }
                                                });
                                                //YES CONTINUE
                                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogWarnLayout.setVisibility(View.GONE);
                                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                            //SHOW OPTION TO CHANGE DEFAULT
                                                            transDialogDefLayout.setVisibility(View.VISIBLE);
                                                            //NO CHANGE DEFAULT
                                                            transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    layCCPurContTransaction();
                                                                }
                                                            });
                                                            //YES CHANGE DEFAULT
                                                            transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    transDialogDefLayout.setVisibility(View.GONE);
                                                                    layCCPurChangeDefault();
                                                                }
                                                            });
                                                        } else { //MONEY IN = DEFAULT AMT
                                                            layCCPurContTransaction();
                                                        }
                                                    }
                                                });
                                            } else { //WILL NOT GO NEGATIVE
                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    transDialogDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            layCCPurContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            transDialogDefLayout.setVisibility(View.GONE);
                                                            layCCPurChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    layCCPurContTransaction();
                                                }
                                            }
                                        }
                                    }
                                });
                            } else if (debtAmtFromDb + layCCPurMonOutAmt > debtLimitFromDb) { // IF NOT OVER WEEKLY LIMIT && CARD IS OVER LIMIT
                                //SHOW WARNING
                                transDialogWarnLayout.setVisibility(View.VISIBLE);
                                transDialogWarnTV.setText(getString(R.string.not_enough_credit_warning));
                                //NO CONTINUE
                                transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        layCCPurRefresh();
                                    }
                                });
                                //YES CONTINUE
                                transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        transDialogWarnLayout.setVisibility(View.GONE);
                                        if ((layCCPurExpPriority.equals("A") && (layCCPurDbMgr.retrieveCurrentAccountBalance() - layCCPurMonOutAmt < 0)) || (layCCPurExpPriority.equals("B") && (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0))) {  //PRIORITY IS A OR B AND WILL GO NEGATIVE
                                            //SHOW WARNING
                                            transDialogWarnLayout.setVisibility(View.VISIBLE);
                                            transDialogWarnTV.setText(getString(R.string.cc_payment_not_possible));
                                            //NO CONTINUE
                                            transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogWarnLayout.setVisibility(View.GONE);
                                                    layCCPurRefresh();
                                                }
                                            });
                                            //YES CONTINUE
                                            transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    transDialogWarnLayout.setVisibility(View.GONE);
                                                    if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                        //SHOW OPTION TO CHANGE DEFAULT
                                                        transDialogDefLayout.setVisibility(View.VISIBLE);
                                                        //NO CHANGE DEFAULT
                                                        transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                transDialogDefLayout.setVisibility(View.GONE);
                                                                layCCPurContTransaction();
                                                            }
                                                        });
                                                        //YES CHANGE DEFAULT
                                                        transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                transDialogDefLayout.setVisibility(View.GONE);
                                                                layCCPurChangeDefault();
                                                            }
                                                        });
                                                    } else { //MONEY IN = DEFAULT AMT
                                                        layCCPurContTransaction();
                                                    }
                                                }
                                            });
                                        } else { //WILL NOT GO NEGATIVE
                                            if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                //SHOW OPTION TO CHANGE DEFAULT
                                                transDialogDefLayout.setVisibility(View.VISIBLE);
                                                //NO CHANGE DEFAULT
                                                transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        layCCPurContTransaction();
                                                    }
                                                });
                                                //YES CHANGE DEFAULT
                                                transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        layCCPurChangeDefault();
                                                    }
                                                });
                                            } else { //MONEY IN = DEFAULT AMT
                                                layCCPurContTransaction();
                                            }
                                        }
                                    }
                                });
                            } else { //CARD NOT OVER LIMIT
                                if ((layCCPurExpPriority.equals("A") && (layCCPurDbMgr.retrieveCurrentAccountBalance() < layCCPurMonOutAmt)) || (layCCPurExpPriority.equals("B") && (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0))) { //PRIORITY IS A OR B AND WILL GO NEGATIVE
                                    //SHOW WARNING
                                    transDialogWarnLayout.setVisibility(View.VISIBLE);
                                    transDialogWarnTV.setText(getString(R.string.cc_payment_not_possible));
                                    //NO CONTINUE
                                    transDialogNoContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogWarnLayout.setVisibility(View.GONE);
                                            layCCPurRefresh();
                                        }
                                    });
                                    //YES CONTINUE
                                    transDialogYesContBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            transDialogWarnLayout.setVisibility(View.GONE);
                                            if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                //SHOW OPTION TO CHANGE DEFAULT
                                                transDialogDefLayout.setVisibility(View.VISIBLE);
                                                //NO CHANGE DEFAULT
                                                transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        layCCPurContTransaction();
                                                    }
                                                });
                                                //YES CHANGE DEFAULT
                                                transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        transDialogDefLayout.setVisibility(View.GONE);
                                                        layCCPurChangeDefault();
                                                    }
                                                });
                                            } else { //MONEY IN = DEFAULT AMT
                                                layCCPurContTransaction();
                                            }
                                        }
                                    });
                                } else { //WILL NOT GO NEGATIVE
                                    if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                        //SHOW OPTION TO CHANGE DEFAULT
                                        transDialogDefLayout.setVisibility(View.VISIBLE);
                                        //NO CHANGE DEFAULT
                                        transDialogNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transDialogDefLayout.setVisibility(View.GONE);
                                                layCCPurContTransaction();
                                            }
                                        });
                                        //YES CHANGE DEFAULT
                                        transDialogYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transDialogDefLayout.setVisibility(View.GONE);
                                                layCCPurChangeDefault();
                                            }
                                        });
                                    } else { //MONEY IN = DEFAULT AMT
                                        layCCPurContTransaction();
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

    private static class LayCCPurViewHolder {
        public TextView layCCPurPaytCatTV;
    }
}
