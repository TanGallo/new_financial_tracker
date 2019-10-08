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

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.spinners.MoneyOutCCSpinnerAdapter;

public class LayoutCCPur extends MainNavigation {

    ContentValues layCCPurCV;
    Cursor layCCPurCur;
    DbHelper layCCPurHelper, layCCPurHelper2;
    DbManager layCCPurDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, layCCPurMonOutAmt = 0.0, layCCPurMonOutNewAmt = 0.0,
            layCCPurMonOutOldAmt = 0.0;
    ExpenseBudgetDb layCCPurExpDb;
    General layCCPurGen;
    Intent layCCPurToAddCC, layCCPurToAddMore, layCCPurToFixBudget, layCCPurToList, layCCPurRefresh;
    LayCCPurLstAdapter layCCPurListAdapter;
    LinearLayout layCCPurHeaderLayout;
    ListView layCCPurList;
    long layCCPurChargingDebtIdFromSpin, layCCPurExpId, layCCPurExpRefKeyMO;
    MoneyOutCCSpinnerAdapter layCCPurSpinAdapter;
    MoneyOutDb layCCPurMonOutDb;
    Spinner layCCPurSpin;
    SQLiteDatabase layCCPurDb, layCCPurDb2;
    String layCCPurChargingDebtNameFromSpin = null, layCCPurExpName = null, layCCPurExpPriority = null, layCCPurExpWeekly = null;
    TabLayout layCCPurTabLay;
    TextView layCCPurAddMoreTV, layCCPurAvailAcctTV, layCCPurAvailAmtLabel, layCCPurBudgWarnTV, layCCPurLabel2, layCCPurNewCCTV, layCCPurTotAcctTV;


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

        //layCCPurDbMgr.mainHeaderText(layCCPurBudgWarnTV, layCCPurTotAcctTV, layCCPurAvailAcctTV, layCCPurDbMgr.sumTotalExpenses(), layCCPurDbMgr.sumTotalIncome(), layCCPurDbMgr.retrieveCurrentAccountBalance(), layCCPurDbMgr.retrieveCurrentB());
        //layCCPurBudgWarnTV.setOnClickListener(onClickLayCCPurBudgWarnTV);

        layCCPurHelper = new DbHelper(this);
        layCCPurDb = layCCPurHelper.getReadableDatabase();
        layCCPurCur = layCCPurDb.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " WHERE " + DbHelper.ISDEBT + " = 'Y'" + " ORDER BY " + DbHelper.ACCTNAME + " ASC", null);
        layCCPurSpinAdapter = new MoneyOutCCSpinnerAdapter(getApplicationContext(), layCCPurCur);
        layCCPurSpin.setAdapter(layCCPurSpinAdapter);

        layCCPurSpin.setOnItemSelectedListener(layCCPurSpinSel);

        layCCPurList = findViewById(R.id.mainListView);
        layCCPurList.setVisibility(View.VISIBLE);
        layCCPurListAdapter = new LayCCPurLstAdapter(this, layCCPurDbMgr.getExpense());
        layCCPurList.setAdapter(layCCPurListAdapter);

        layCCPurCV = new ContentValues();
        layCCPurCV.put(DbHelper.LASTPAGEID, 7);
        layCCPurHelper2 = new DbHelper(this);
        layCCPurDb2 = layCCPurHelper2.getWritableDatabase();
        layCCPurDb2.update(DbHelper.CURRENT_TABLE_NAME, layCCPurCV, DbHelper.ID + "= '1'", null);
        layCCPurDb2.close();
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
        layCCPurExpDb.setExpenseAmount(layCCPurMonOutAmt);
        layCCPurDbMgr.updateExpense(layCCPurExpDb);
        layCCPurContTransaction();
    }

    public void layCCPurContTransaction() {

        layCCPurDbMgr.updateDebtRecPlusPt1(layCCPurMonOutAmt, debtAmtFromDb, layCCPurChargingDebtIdFromSpin);
        for (AccountsDb a : layCCPurDbMgr.getDebts()) {
            if (String.valueOf(a.getId()).equals(String.valueOf(layCCPurChargingDebtIdFromSpin))) {
                debtAmtFromDb = a.getAcctBal();
                debtLimitFromDb = a.getAcctMax();
                debtRateFromDb = a.getIntRate();
                debtPaytFromDb = a.getPaytsTo();
            }
        }
        layCCPurDbMgr.updateDebtRecPt2(layCCPurGen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), layCCPurChargingDebtIdFromSpin);

        layCCPurMonOutDb = new MoneyOutDb(
                layCCPurExpName,
                layCCPurExpPriority,
                layCCPurExpWeekly,
                layCCPurMonOutAmt,
                0.0,
                0.0,
                0.0,
                layCCPurChargingDebtIdFromSpin,
                layCCPurChargingDebtNameFromSpin,
                layCCPurGen.createTimestamp(),
                "Y",
                layCCPurChargingDebtNameFromSpin,
                layCCPurChargingDebtIdFromSpin,
                0,
                0,
                layCCPurExpRefKeyMO,
                0);
        layCCPurDbMgr.addMoneyOut(layCCPurMonOutDb);

        layCCPurDbMgr.makeNewExpAnnAmt(layCCPurExpRefKeyMO, layCCPurGen.lastNumOfDays(365));
        layCCPurDbMgr.updateExpense(layCCPurExpDb);

        layCCPurListAdapter.updateExpenses(layCCPurDbMgr.getExpense());
        layCCPurListAdapter.notifyDataSetChanged();

        layCCPurToList = new Intent(LayoutCCPur.this, LayoutCCPurList.class);
        layCCPurToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layCCPurToList);
    }

    public class LayCCPurLstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private LayCCPurLstAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<ExpenseBudgetDb> expenses) {
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
                layCCPurHldr.layCCPurCatLayout = convertView.findViewById(R.id.paytCatLayout);
                layCCPurHldr.layCCPurCatLayout.setVisibility(View.GONE);
                layCCPurHldr.layCCPurPaytPayLabel = convertView.findViewById(R.id.paytPayLabel);
                layCCPurHldr.layCCPurPaytPayLabel.setText(getString(R.string.spent));
                layCCPurHldr.layCCPurPaytAmtTV = convertView.findViewById(R.id.paytAmtTV);
                layCCPurHldr.layCCPurPaytAmtET = convertView.findViewById(R.id.paytAmtET);
                layCCPurHldr.layCCPurPaytSaveBtn = convertView.findViewById(R.id.paytSaveBtn);
                layCCPurHldr.layCCPurPaytSaveBtn.setVisibility(View.GONE);
                layCCPurHldr.layCCPurPaytWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                layCCPurHldr.layCCPurPaytWarnTV = convertView.findViewById(R.id.paytWarnTV);
                layCCPurHldr.layCCPurPaytYesContBtn = convertView.findViewById(R.id.paytYesContBtn);
                layCCPurHldr.layCCPurPaytNoContBtn = convertView.findViewById(R.id.paytNoContBtn);
                layCCPurHldr.layCCPurPaytDefLayout = convertView.findViewById(R.id.paytDefLayout);
                layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                layCCPurHldr.layCCPurPaytYesDefBtn = convertView.findViewById(R.id.paytYesDefBtn);
                layCCPurHldr.layCCPurPaytNoDefBtn = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(layCCPurHldr);

            } else {
                layCCPurHldr = (LayCCPurViewHolder) convertView.getTag();
            }

            layCCPurHldr.layCCPurPaytCatTV.setText(expenses.get(position).getExpenseName());
            layCCPurGen.dblASCurrency(String.valueOf(expenses.get(position).getExpenseAmount()), layCCPurHldr.layCCPurPaytAmtTV);

            layCCPurExpRefKeyMO = expenses.get(position).getId();

            layCCPurHldr.layCCPurPaytCatTV.setTag(expenses.get(position));
            layCCPurHldr.layCCPurPaytSaveBtn.setTag(expenses.get(position));

            layCCPurHldr.layCCPurPaytCatTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layCCPurHldr.layCCPurCatLayout.setVisibility(View.VISIBLE);
                    layCCPurHldr.layCCPurPaytSaveBtn.setVisibility(View.VISIBLE);

                    layCCPurHldr.layCCPurPaytSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layCCPurExpDb = (ExpenseBudgetDb) layCCPurHldr.layCCPurPaytSaveBtn.getTag();

                            layCCPurExpName = layCCPurExpDb.getExpenseName();
                            layCCPurExpId = layCCPurExpDb.getId();

                            layCCPurMonOutOldAmt = expenses.get(position).getExpenseAmount();
                            layCCPurMonOutNewAmt = layCCPurGen.dblFromET(layCCPurHldr.layCCPurPaytAmtET);

                            if (layCCPurMonOutNewAmt == 0) {
                                layCCPurMonOutAmt = layCCPurMonOutOldAmt;
                            } else {
                                layCCPurMonOutAmt = layCCPurMonOutNewAmt;
                            }

                            layCCPurExpPriority = layCCPurExpDb.getExpensePriority();
                            layCCPurExpWeekly = layCCPurExpDb.getExpenseWeekly();

                            for (AccountsDb a : layCCPurDbMgr.getDebts()) {
                                if (String.valueOf(a.getId()).equals(String.valueOf(layCCPurChargingDebtIdFromSpin))) {
                                    debtAmtFromDb = a.getAcctBal();
                                    debtLimitFromDb = a.getAcctMax();
                                    debtRateFromDb = a.getIntRate();
                                    debtPaytFromDb = a.getPaytsTo();
                                }
                            }

                            if (debtAmtFromDb + layCCPurMonOutAmt > debtLimitFromDb) { //CARD IS OVER LIMIT
                                //SHOW WARNING
                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.VISIBLE);
                                layCCPurHldr.layCCPurPaytWarnTV.setText(getString(R.string.not_enough_credit_warning));
                                //NO CONTINUE
                                layCCPurHldr.layCCPurPaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                        layCCPurRefresh = new Intent(getContext(), LayoutCCPur.class);
                                        layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        startActivity(layCCPurRefresh);
                                    }
                                });
                                //YES CONTINUE
                                layCCPurHldr.layCCPurPaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                        if (layCCPurExpPriority.equals("A")) {  //PRIORITY IS A
                                            if (layCCPurDbMgr.retrieveCurrentAccountBalance() - layCCPurMonOutAmt < 0) { //A NOT POSSIBLE
                                                //SHOW WARNING
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.VISIBLE);
                                                layCCPurHldr.layCCPurPaytWarnTV.setText(getString(R.string.payment_not_possible_A));
                                                //NO CONTINUE
                                                layCCPurHldr.layCCPurPaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                        layCCPurRefresh = new Intent(getContext(), LayoutCCPur.class);
                                                        layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                        startActivity(layCCPurRefresh);
                                                    }
                                                });
                                                //YES CONTINUE
                                                layCCPurHldr.layCCPurPaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                            //SHOW OPTION TO CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                            //NO CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                                    layCCPurContTransaction();
                                                                }
                                                            });
                                                            //YES CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                                    layCCPurChangeDefault();
                                                                }
                                                            });
                                                        } else { //MONEY IN = DEFAULT AMT
                                                            layCCPurContTransaction();
                                                        }
                                                    }
                                                });
                                            } else { //A POSSIBLE
                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    layCCPurContTransaction();
                                                }
                                            }
                                        } else if (layCCPurExpPriority.equals("B")) { //PRIORITY IS B
                                            if (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0) { //B NOT POSSIBLE
                                                //SHOW WARNING
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.VISIBLE);
                                                layCCPurHldr.layCCPurPaytWarnTV.setText(getString(R.string.payment_not_possible_B));
                                                //NO CONTINUE
                                                layCCPurHldr.layCCPurPaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                        layCCPurRefresh = new Intent(getContext(), LayoutCCPur.class);
                                                        layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                        startActivity(layCCPurRefresh);
                                                    }
                                                });
                                                //YES CONTINUE
                                                layCCPurHldr.layCCPurPaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                            //SHOW OPTION TO CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                            //NO CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                                    layCCPurContTransaction();
                                                                }
                                                            });
                                                            //YES CHANGE DEFAULT
                                                            layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                                    layCCPurChangeDefault();
                                                                }
                                                            });
                                                        } else { //MONEY IN = DEFAULT AMT
                                                            layCCPurContTransaction();
                                                        }
                                                    }
                                                });
                                            } else { //B POSSIBLE
                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
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
                            } else { //CARD NOT OVER LIMIT
                                if (layCCPurExpPriority.equals("A")) { //PRIORITY IS A
                                    if (layCCPurDbMgr.retrieveCurrentAccountBalance() - layCCPurMonOutAmt < 0) { //A NOT POSSIBLE
                                        //SHOW WARNING
                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.VISIBLE);
                                        layCCPurHldr.layCCPurPaytWarnTV.setText(getString(R.string.payment_not_possible_A));
                                        //NO CONTINUE
                                        layCCPurHldr.layCCPurPaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                layCCPurRefresh = new Intent(getContext(), LayoutCCPur.class);
                                                layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                startActivity(layCCPurRefresh);
                                            }
                                        });
                                        //YES CONTINUE
                                        layCCPurHldr.layCCPurPaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    layCCPurContTransaction();
                                                }
                                            }
                                        });
                                    } else { //A POSSIBLE
                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                    layCCPurContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                    layCCPurChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            layCCPurContTransaction();
                                        }
                                    }
                                } else if (layCCPurExpPriority.equals("B")) { //PRIORITY IS B
                                    if (layCCPurDbMgr.retrieveCurrentB() - layCCPurMonOutAmt < 0) { //B NOT POSSIBLE
                                        //SHOW WARNING
                                        layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.VISIBLE);
                                        layCCPurHldr.layCCPurPaytWarnTV.setText(getString(R.string.payment_not_possible_B));
                                        //NO CONTINUE
                                        layCCPurHldr.layCCPurPaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                layCCPurRefresh = new Intent(getContext(), LayoutCCPur.class);
                                                layCCPurRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                startActivity(layCCPurRefresh);
                                            }
                                        });
                                        //YES CONTINUE
                                        layCCPurHldr.layCCPurPaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                layCCPurHldr.layCCPurPaytWarnLayout.setVisibility(View.GONE);
                                                if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                            layCCPurChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    layCCPurContTransaction();
                                                }
                                            }
                                        });
                                    } else { //B POSSIBLE
                                        if (layCCPurMonOutAmt == layCCPurMonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                    layCCPurContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            layCCPurHldr.layCCPurPaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layCCPurHldr.layCCPurPaytDefLayout.setVisibility(View.GONE);
                                                    layCCPurChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            layCCPurContTransaction();
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

    private static class LayCCPurViewHolder {
        public TextView layCCPurPaytCatTV;
        public LinearLayout layCCPurCatLayout;
        public TextView layCCPurPaytPayLabel;
        public TextView layCCPurPaytAmtTV;
        public EditText layCCPurPaytAmtET;
        public ImageButton layCCPurPaytSaveBtn;
        public LinearLayout layCCPurPaytWarnLayout;
        public TextView layCCPurPaytWarnTV;
        public Button layCCPurPaytYesContBtn;
        public Button layCCPurPaytNoContBtn;
        public LinearLayout layCCPurPaytDefLayout;
        public Button layCCPurPaytYesDefBtn;
        public Button layCCPurPaytNoDefBtn;
    }
}
