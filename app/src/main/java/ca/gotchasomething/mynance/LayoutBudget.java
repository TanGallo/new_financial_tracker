package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;

import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
//import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class LayoutBudget extends MainNavigation {

    boolean foundDebtIdInc = false, foundSavIdInc = false, foundDebtIdExp = false, foundSavIdExp = false, foundDebtIdCharging = false, foundExpIdE = false,
            foundExpIdI = false;
    BudgetDb layBudExpDb, layBudIncDb;
    Button layBudExpCancelBtn, layBudExpSaveBtn, layBudExpUpdateBtn, layBudIncCancelBtn, layBudIncUpdateBtn, layBudIncSaveBtn, layBudResInfoOkBtn, budgetCancelExpenseButton,
            budgetCancelIncomeButton, budgetSetUpTimeButton,
            budgetSetUpHelpButton, budgetUpdateExpenseButton, budgetUpdateIncomeButton, budRepWarnCancelBtn, doneBudgetSetUpButton,
            layBudDelWarnBtn, layBudRepWarnOkBtn;
    ContentValues layBudCV, layBudCV2, layBudCV3, layBudCV4, layBudCV5, layBudCV6, layBudCV7, layBudCV8, layBudCV9, layBudCV10, cv5, cv6, cv7, cv8, cv9, cv10, cv11, cv12, cv13, cv14, debtValues, debtValues2, debtValues3,
            debtValues4, expValues, incValues, moneyInValues, moneyOutValues, moneyOutValues2, moneyOutValues3, savingsValues, savingsValues2,
            savingsValues3, savingsValues4;
    DbHelper layBudHelper, layBudHelper2;
    DbManager layBudDbMgr;
    Double layBudStillAvail = 0.0, debtAmtFromDb = 0.0, debtAnnIncFromDb = 0.0, debtFrqFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0,
            expAmtFromEntry = 0.0, expAnnAmtFromTag = 0.0, expAnnAmtFromEntry = 0.0, expAAnnAmtFromEntry = 0.0, expBAnnAmtFromEntry = 0.0,
            expFrqFromEntry = 0.0, expFrqFromTag = 0.0, incAmtFromEntry = 0.0,
            incAnnAmtFromTag = 0.0, incAnnAmtFromEntry = 0.0, incFrqFromEntry = 0.0,
            savAmtFromDb = 0.0, savAnnIncFromDb = 0.0, savFrqFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0,
            savRateFromDb = 0.0, amountEntry = 0.0,
            annualAmount = 0.0, annualIncome = 0.0, balanceAmount = 0.0, budgetExpenseAmountD = 0.0, budgetIncomeAmountD = 0.0,
            savFrqFromEntry = 0.0, savPaytFromEntry = 0.0, currentSavingsRate = 0.0, debtAmount = 0.0, debtAnnualIncome = 0.0, debtFrequency = 0.0,
            debtPayments = 0.0, debtRate = 0.0, expenseAnnualAmountD = 0.0, frequencyEntry = 0.0, incomeAnnualAmountD = 0.0, incomeAvailableD = 0.0,
            incomeAvailableN = 0.0, savAmtFromEntry = 0.0, savAnnIncFromEntry = 0.0, savGoalFromEntry = 0.0, savRateFromEntry = 0.0,
            totalExpensesD = 0.0, layBudTotRes = 0.0, totalIncomeD = 0.0, layBudTotInc = 0.0, spendPercent2 = 0.0, years2 = 0.0;
    EditText layBudExpAmtET, layBudExpCatET, layBudIncAmtET, layBudIncCatET;
    //BudExpListAdapter layBudExpListAdapter;
    General layBudGen;
    FloatingActionButton layBudAddExpBtn, layBudAddIncBtn;
    ImageButton layBudAdjIncBtn, layBudAdjExpBtn, layBudAdjDebtsBtn, layBudAdjSavBtn, layBudResInfoBtn;
    //IncomeBudgetDb layBudIncDb;
    //BudIncListAdapter layBudIncListAdapter;
    int layBudInt1 = 0, balanceDone = 0, billsDone = 0, budgetDone = 0, debtsDone = 0, incomeDone = 0, savingsDone = 0, tourDone = 0;
    Intent layBudToAdjInc, layBudToAdjExp, layBudToAdjDebt, layBudToAdjSav;
    LinearLayout layBudResInfoLayout, layBudExpListLayout, layBudIncListLayout, layBudRepWarnLayout;
    ListView layBudExpListView, layBudIncListView;
    long debtIdCharging, debtIdInc, debtIdExp, expIdFromTag, expRefKeyDFromDb, expRefKeySFromDb, incRefKeyDFromDb, incRefKeySFromDb,
            incIdFromTag, savIdInc, savIdExp;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    public NumberFormat percentFormat = NumberFormat.getPercentInstance();
    RadioButton layBudExpARB, layBudExpAnnlyRB, layBudExpBRB, layBudExpBiAnnlyRB, layBudExpBiMthlyRB, layBudExpBiWklyRB, layBudExpMthlyRB, layBudExpNoWklyRB,
            layBudExpWklyRB, layBudExpYesWklyRB, layBudIncAnnlyRB, layBudIncBiAnnlyRB, layBudIncBiMthlyRB, layBudIncBiWklyRB, layBudIncMthlyRB, layBudIncWklyRB;
    RadioGroup layBudExpWklyRG, layBudExpFrqRG, layBudIncFrqRG, layBudExpABRG;
    RelativeLayout layBudAdjIncLayout, layBudAdjExpLayout, layBudAdjDebtsLayout, layBudAdjSavLayout;
    SetUpDb setUpDb;
    SQLiteDatabase layBudDb, layBudDb2;
    String layBudRecommendation = null, budExpIdE = null, budExpIdI = null, layBudExpPriorityRB = null, layBudIncFrqRB = null, budPriorityFromTag = null, budWeeklyFromTag = null,
            expNameFromEntry = null, expNameFromTag = null,
            expPriorityFromEntry = null, expPriorityFromTag = null, expWeeklyFromEntry = null, expWeeklyFromTag = null, layBudExpWeeklyRB = null, incFrqFromTag = null, layBudExpFrqRB = null, incNameFromEntry = null, incNameFromTag = null, budgetExpenseAmountS = null,
            budgetIncomeAmountS = null, debtId = null, expRefKeyS = null, expDebtId = null, expSavingsId = null, expRefKeyD = null,
            expenseAnnualAmount2 = null,
            expenseAnnualAmountS = null, expenseFrequencyS = null, expenseId = null, expensePriorityS = null, expenseWeeklyS = null,
            incomeId = null, incomeAnnualAmount2 = null,
            incomeAnnualAmountS = null, incRefKeyD = null, incRefKeyS = null, incDebtId = null, incSavingsId = null, incomeAvailable2 = null,
            incomeAvailableN2 = null,
            incomeFrequencyS = null, savingsId = null, nameEntryInc = null, nameEntryExp = null, priorityEntryExp = null, layBudSavDate2 = null,
            spendResStmt = null, spendPercent = null, totalExpenses2 = null,
            totalExpensesS = null, totalIncome2 = null, totalIncomeS = null, weeklyEntry = null;
    TextView layBudStatusTV, layBudStatusTV2, layBudExpWklyLabel, layBudIncFrqLabel, layBudTotResTV, layBudTotIncTV, layBudOverWarnTV, layBudOverWarnLabel, layBudResLabel, budgetSetUpNoTime2,
            budgetSetUpNeedHelp, budgetSetUpNeedHelp2, deleteExpText, emptyBudgetText, layBudHeaderLabel, layBudHeaderTV, layBudRepWarnTV,
            weeklyGuidanceLabel, tv;
    Toast toast;
    View layBudExpLine1, layBudExpLine2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c5_layout_budget_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layBudGen = new General();
        layBudDbMgr = new DbManager(this);

        layBudTotIncTV = findViewById(R.id.budMnTotIncTV);
        layBudTotResTV = findViewById(R.id.budMnTotExpTV);
        layBudResInfoBtn = findViewById(R.id.budMnResInfoBtn);
        layBudResInfoLayout = findViewById(R.id.budMnResInfoLayout);
        layBudResInfoLayout.setVisibility(View.GONE);
        layBudResInfoOkBtn = findViewById(R.id.budMnResInfoOkBtn);
        layBudStatusTV = findViewById(R.id.budMnStatusTV);
        layBudStatusTV2 = findViewById(R.id.budMnStatusTV2);
        layBudOverWarnLabel = findViewById(R.id.budMnOverWarnLabel);
        layBudOverWarnLabel.setVisibility(View.GONE);
        layBudOverWarnTV = findViewById(R.id.budMnOverWarnTV);
        layBudOverWarnTV.setVisibility(View.GONE);
        layBudAdjIncBtn = findViewById(R.id.budMnAdjIncBtn);
        layBudAdjExpBtn = findViewById(R.id.budMnAdjExpBtn);
        layBudAdjDebtsBtn = findViewById(R.id.budMnAdjDebtsBtn);
        layBudAdjSavBtn = findViewById(R.id.budMnAdjSavBtn);

        layBudResInfoBtn.setOnClickListener(onClickLayBudResInfoBtn);
        layBudAdjIncBtn.setOnClickListener(onClickLayBudAdjIncBtn);
        layBudAdjExpBtn.setOnClickListener(onClickLayBudAdjExpBtn);
        layBudAdjDebtsBtn.setOnClickListener(onClickLayBudAdjDebtsBtn);
        layBudAdjSavBtn.setOnClickListener(onClickLayBudAdjSavBtn);

        layBudHeaderText();

        layBudCV = new ContentValues();
        layBudCV.put(DbHelper.LASTPAGEID, 2);
        layBudHelper = new DbHelper(this);
        layBudDb = layBudHelper.getWritableDatabase();
        layBudDb.update(DbHelper.CURRENT_TABLE_NAME, layBudCV, DbHelper.ID + "= '1'", null);
        layBudDb.close();
    }

    public void layBudHeaderText() {

        layBudTotInc = layBudDbMgr.sumTotalIncome();
        layBudTotRes = layBudTotInc * (layBudDbMgr.sumTotalAExpenses() / layBudTotInc);
        layBudGen.dblASCurrency(String.valueOf(layBudTotInc), layBudTotIncTV);
        layBudGen.dblASCurrency(String.valueOf(layBudTotRes), layBudTotResTV);

        layBudStillAvail = layBudTotInc - layBudTotRes;

        spendPercent2 = (layBudDbMgr.sumTotalAExpenses() / layBudDbMgr.sumTotalIncome());
        if (spendPercent2 >= .910) {
            layBudInt1 = Color.parseColor("#ffff4444"); //red
            layBudRecommendation = getString(R.string.should_adj);
        } else if (spendPercent2 <= .909 && spendPercent2 >= .801) {
            layBudInt1 = Color.parseColor("#ffc30b"); //yellow
            layBudRecommendation = getString(R.string.may_adj);
        } else if(spendPercent2 <= .800) {
            layBudInt1 = Color.parseColor("#5dbb63"); //light green
            layBudRecommendation = getString(R.string.no_adj_nec);
        }
        layBudDbMgr.spendResPara(
                layBudStatusTV,
                layBudStatusTV2,
                layBudRecommendation,
                getString(R.string.ana_res_prt_1),
                getString(R.string.ana_res_part_2),
                spendPercent2,
                layBudInt1);
        if (layBudStillAvail < 0) {
            layBudStatusTV.setVisibility(View.GONE);
            layBudStatusTV2.setVisibility(View.GONE);
            layBudOverWarnLabel.setVisibility(View.VISIBLE);
            layBudOverWarnTV.setVisibility(View.VISIBLE);
            layBudGen.dblASCurrency(String.valueOf(layBudStillAvail), layBudOverWarnTV);
        } else {
            layBudOverWarnLabel.setVisibility(View.GONE);
            layBudOverWarnTV.setVisibility(View.GONE);
            layBudStatusTV.setVisibility(View.VISIBLE);
            layBudStatusTV2.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener onClickLayBudResInfoBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudResInfoLayout.setVisibility(View.VISIBLE);
            layBudResInfoOkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layBudResInfoLayout.setVisibility(View.GONE);
                }
            });

        }
    };

    View.OnClickListener onClickLayBudAdjIncBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjInc = new Intent(LayoutBudget.this, AddIncomeList.class);
            layBudToAdjInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjInc);
        }
    };

    View.OnClickListener onClickLayBudAdjExpBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjExp = new Intent(LayoutBudget.this, AddExpenseList.class);
            layBudToAdjExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjExp);
        }
    };

    View.OnClickListener onClickLayBudAdjDebtsBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjDebt = new Intent(LayoutBudget.this, AddDebtsList.class);
            layBudToAdjDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjDebt);
        }
    };

    View.OnClickListener onClickLayBudAdjSavBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layBudToAdjSav = new Intent(LayoutBudget.this, AddSavingsList.class);
            layBudToAdjSav.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layBudToAdjSav);
        }
    };

    /*View.OnClickListener onClickBudAddIncBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budGen.intentMethod(budToAddInc, LayoutBudget.this, AddIncomeFromBudget.class);

        }
    };*/

    /*View.OnClickListener onClickBudAddExpBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budGen.intentMethod(budToAddExp, LayoutBudget.this, AddExpenseFromBudget.class);

        }
    };*/

    /*public void budRefresh() {
        budGen.intentMethod(budRefresh, this, LayoutBudget.class);

    }*/

    /*public class BudIncListAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public BudIncListAdapter(
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

            final BudIncListViewHolder budIncListHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                budIncListHldr = new BudIncListViewHolder();
                budIncListHldr.budIncListNameTV = convertView.findViewById(R.id.list2TV1);
                budIncListHldr.budIncListAmtTV = convertView.findViewById(R.id.list2TV2);
                budIncListHldr.budIncListDelBtn = convertView.findViewById(R.id.list2DelBtn);
                budIncListHldr.budIncListEditBtn = convertView.findViewById(R.id.list2EditBtn);
                convertView.setTag(budIncListHldr);

            } else {
                budIncListHldr = (BudIncListViewHolder) convertView.getTag();
            }

            budIncListHldr.budIncListNameTV.setText(incomes.get(position).getIncomeName());
            budGen.dblASCurrency(String.valueOf(incomes.get(position).getIncomeAnnualAmount()), budIncListHldr.budIncListAmtTV);

            budIncListHldr.budIncListDelBtn.setTag(incomes.get(position));
            budIncListHldr.budIncListEditBtn.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            budIncListHldr.budIncListEditBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_1_add_income);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budIncCatET = findViewById(R.id.addIncCatET);
                    budIncAmtET = findViewById(R.id.addIncAmtET);
                    budIncFrqLabel = findViewById(R.id.addIncFrqLabel);
                    budIncFrqRG = findViewById(R.id.addIncFrqRG);
                    budIncWklyRB = findViewById(R.id.addIncWklyRB);
                    budIncBiWklyRB = findViewById(R.id.addIncBiWklyRB);
                    budIncBiMthlyRB = findViewById(R.id.addIncBiMthlyRB);
                    budIncMthlyRB = findViewById(R.id.addIncMthlyRB);
                    budIncBiAnnlyRB = findViewById(R.id.addIncBiAnnlyRB);
                    budIncAnnlyRB = findViewById(R.id.addIncAnnlyRB);
                    budIncSaveBtn = findViewById(R.id.addIncSaveBtn);
                    budIncUpdateBtn = findViewById(R.id.addIncUpdateBtn);
                    budIncSaveBtn.setVisibility(View.GONE);
                    budIncCancelBtn = findViewById(R.id.addIncCancelBtn);

                    budIncDb = (IncomeBudgetDb) budIncListHldr.budIncListEditBtn.getTag();
                    budGen.incDataFromTag(budIncDb);

                    budIncCatET.setText(incNameFromTag);
                    budGen.dblASCurrency(String.valueOf(incAnnAmtFromTag), budIncAmtET);


                    if (incFrqFromTag.equals("52")) {
                        budIncWklyRB.setChecked(true);
                        budIncFrqRB = "52";
                    } else if (incFrqFromTag.equals("26")) {
                        budIncBiWklyRB.setChecked(true);
                        budIncFrqRB = "26";
                    } else if (incFrqFromTag.equals("24")) {
                        budIncBiMthlyRB.setChecked(true);
                        budIncFrqRB = "24";
                    } else if (incFrqFromTag.equals("12")) {
                        budIncMthlyRB.setChecked(true);
                        budIncFrqRB = "12";
                    } else if (incFrqFromTag.equals("2")) {
                        budIncBiAnnlyRB.setChecked(true);
                        budIncFrqRB = "2";
                    } else if (incFrqFromTag.equals("1")) {
                        budIncAnnlyRB.setChecked(true);
                        budIncFrqRB = "1";
                    }

                    //update db if changed
                    budIncFrqRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addIncWklyRB:
                                    budIncFrqRB = "52";
                                    break;
                                case R.id.addIncBiWklyRB:
                                    budIncFrqRB = "26";
                                    break;
                                case R.id.addIncBiMthlyRB:
                                    budIncFrqRB = "24";
                                    break;
                                case R.id.addIncMthlyRB:
                                    budIncFrqRB = "12";
                                    break;
                                case R.id.addIncBiAnnlyRB:
                                    budIncFrqRB = "2";
                                    break;
                                case R.id.addIncAnnlyRB:
                                    budIncFrqRB = "1";
                                    break;
                            }
                        }
                    });

                    budIncCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            budRefresh();
                        }
                    });

                    budIncUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            budGen.incomeDataFromEntries(
                                    budIncCatET,
                                    budIncAmtET,
                                    budIncFrqRB
                            );

                            //budGen.findMatchId(String.valueOf(incIdFromTag));

                            if (incNameFromEntry != null) {

                                budIncDb.setIncomeName(incNameFromEntry);
                                budIncDb.setIncomeAmount(incAmtFromEntry);
                                budIncDb.setIncomeFrequency(incFrqFromEntry);
                                budIncDb.setIncomeAnnualAmount(incAnnAmtFromEntry);

                                budDbMgr.updateIncome(budIncDb);

                                budHelper = new DbHelper(getContext());
                                budDb = budHelper.getWritableDatabase();

                                budCV7 = new ContentValues();

                                String[] args7 = new String[]{String.valueOf(incIdFromTag)};
                                try {
                                    budCV7.put(DbHelper.MONEYINCAT, incNameFromEntry);
                                    budDb.update(DbHelper.MONEY_IN_TABLE_NAME, budCV7, DbHelper.INCREFKEYMI + "=?", args7);
                                } catch (CursorIndexOutOfBoundsException | SQLException e7) {
                                    e7.printStackTrace();
                                }

                                budDb.close();

                                budIncListAdapter.updateIncomes(budDbMgr.getIncomes());
                                budIncListAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                budRefresh();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            budIncListHldr.budIncListDelBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    budIncDb = (IncomeBudgetDb) budIncListHldr.budIncListDelBtn.getTag();

                    budRepWarnLayout.setVisibility(View.VISIBLE);

                    budRepWarnCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            budRefresh();
                        }
                    });

                    budRepWarnOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            budRepWarnLayout.setVisibility(View.GONE);

                        }
                    });

                    budDbMgr.deleteIncome(budIncDb);
                    budIncListAdapter.updateIncomes(budDbMgr.getIncomes());
                    budIncListAdapter.notifyDataSetChanged();

                    budHeaderText();
                }
            });
            return convertView;
        }
    }

    public class BudExpListAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> expenses;

        public BudExpListAdapter(
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

            final BudExpListViewHolder budExpListHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                budExpListHldr = new BudExpListViewHolder();
                budExpListHldr.budExpListNameTV = convertView.findViewById(R.id.list2TV1);
                budExpListHldr.budExpListAmtTV = convertView.findViewById(R.id.list2TV2);
                budExpListHldr.budExpListDelBtn = convertView.findViewById(R.id.list2DelBtn);
                budExpListHldr.budExpListEditBtn = convertView.findViewById(R.id.list2EditBtn);
                convertView.setTag(budExpListHldr);

            } else {
                budExpListHldr = (BudExpListViewHolder) convertView.getTag();
            }

            budExpListHldr.budExpListNameTV.setText(expenses.get(position).getExpenseName());
            budGen.dblASCurrency(String.valueOf(expenses.get(position).getExpenseAnnualAmount()), budExpListHldr.budExpListAmtTV);

            budExpListHldr.budExpListDelBtn.setTag(expenses.get(position));
            budExpListHldr.budExpListEditBtn.setTag(expenses.get(position));

            //click on pencil icon to edit a data record
            budExpListHldr.budExpListEditBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_5_add_expense);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budExpCatET = findViewById(R.id.addExpCatET);
                    budExpAmtET = findViewById(R.id.addExpAmtET);
                    budExpFrqRG = findViewById(R.id.addExpFrqRG);
                    budExpABRG = findViewById(R.id.addExpABRG);
                    budExpWklyLabel = findViewById(R.id.addExpWklyLabel);
                    budExpWklyLabel.setVisibility(View.GONE);
                    budExpWklyRG = findViewById(R.id.addExpWklyRG);
                    budExpWklyRG.setVisibility(View.GONE);
                    budExpCancelBtn = findViewById(R.id.addExpCancelBtn);
                    budExpSaveBtn = findViewById(R.id.addExpSaveBtn);
                    budExpSaveBtn.setVisibility(View.GONE);
                    budExpUpdateBtn = findViewById(R.id.addExpUpdateBtn);
                    budExpLine1 = findViewById(R.id.addExpLine1);
                    budExpLine2 = findViewById(R.id.addExpLine2);

                    budExpWklyRB = findViewById(R.id.addExpWklyRB);
                    budExpBiWklyRB = findViewById(R.id.addExpBiWklyRB);
                    budExpBiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
                    budExpMthlyRB = findViewById(R.id.addExpMthlyRB);
                    budExpBiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
                    budExpAnnlyRB = findViewById(R.id.addExpAnnlyRB);

                    budExpARB = findViewById(R.id.addExpARB);
                    budExpBRB = findViewById(R.id.addExpBRB);

                    budExpYesWklyRB = findViewById(R.id.addExpYesWklyRB);
                    budExpNoWklyRB = findViewById(R.id.addExpNoWklyRB);

                    budExpDb = (ExpenseBudgetDb) budExpListHldr.budExpListEditBtn.getTag();
                    budGen.expDataFromTag(budExpDb);

                    budExpCatET.setText(expNameFromTag);
                    budGen.dblASCurrency(String.valueOf(expAnnAmtFromTag), budExpAmtET);

                    //set radio buttons from data
                    if (expPriorityFromTag.equals("52")) {
                        budExpWklyRB.setChecked(true);
                        budExpFrqRB = "52";
                    } else if (expPriorityFromTag.equals("26")) {
                        budExpBiWklyRB.setChecked(true);
                        budExpFrqRB = "26";
                    } else if (expPriorityFromTag.equals("24")) {
                        budExpBiMthlyRB.setChecked(true);
                        budExpFrqRB = "24";
                    } else if (expPriorityFromTag.equals("12")) {
                        budExpMthlyRB.setChecked(true);
                        budExpFrqRB = "12";
                    } else if (expPriorityFromTag.equals("2")) {
                        budExpBiAnnlyRB.setChecked(true);
                        budExpFrqRB = "2";
                    } else if (expPriorityFromTag.equals("1")) {
                        budExpAnnlyRB.setChecked(true);
                        budExpFrqRB = "1";
                    }

                    switch (expPriorityFromTag) {
                        case "A":
                            budExpARB.setChecked(true);
                            budExpPriorityRB = "A";
                            budExpWklyRG.setVisibility(View.GONE);
                            budExpWklyLabel.setVisibility(View.GONE);
                            break;
                        case "B":
                            budExpBRB.setChecked(true);
                            budExpPriorityRB = "B";
                            budExpWklyRG.setVisibility(View.VISIBLE);
                            budExpWklyLabel.setVisibility(View.VISIBLE);
                            break;
                    }

                    switch (expWeeklyFromTag) {
                        case "Y":
                            budExpYesWklyRB.setChecked(true);
                            budExpWeeklyRB = "Y";
                            break;
                        case "N":
                            budExpNoWklyRB.setChecked(true);
                            budExpWeeklyRB = "N";
                            break;
                    }

                    //update db if changed
                    budExpFrqRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpWklyRB:
                                    budExpFrqRB = "52";
                                    break;
                                case R.id.addExpBiWklyRB:
                                    budExpFrqRB = "26";
                                    break;
                                case R.id.addExpBiMthlyRB:
                                    budExpFrqRB = "24";
                                    break;
                                case R.id.addExpMthlyRB:
                                    budExpFrqRB = "12";
                                    break;
                                case R.id.addExpBiAnnlyRB:
                                    budExpFrqRB = "2";
                                    break;
                                case R.id.addExpAnnlyRB:
                                    budExpFrqRB = "1";
                                    break;
                            }
                        }
                    });

                    budExpABRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpARB:
                                    budExpPriorityRB = "A";
                                    budExpWeeklyRB = "N";
                                    budExpWklyRG.setVisibility(View.GONE);
                                    budExpWklyLabel.setVisibility(View.GONE);
                                    break;
                                case R.id.addExpBRB:
                                    budExpPriorityRB = "B";
                                    budExpWklyRG.setVisibility(View.VISIBLE);
                                    budExpWklyLabel.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                    });

                    budExpWklyRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpYesWklyRB:
                                    budExpWeeklyRB = "Y";
                                    break;
                                case R.id.addExpNoWklyRB:
                                    budExpWeeklyRB = "N";
                                    break;
                            }
                        }

                    });

                    budExpCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            budRefresh();
                        }
                    });

                    budExpUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            budGen.expenseDataFromEntries(
                                    budExpCatET,
                                    budExpAmtET,
                                    budExpFrqRB,
                                    budExpPriorityRB,
                                    budExpWeeklyRB);

                            //budGen.findMatchId(String.valueOf(expIdFromTag));

                            if (expNameFromEntry != null) {

                                budExpDb.setExpenseName(expNameFromEntry);
                                budExpDb.setExpenseAmount(expAmtFromEntry);
                                budExpDb.setExpenseFrequency(expFrqFromEntry);
                                budExpDb.setExpensePriority(expPriorityFromEntry);
                                budExpDb.setExpenseWeekly(expWeeklyFromEntry);
                                budExpDb.setExpenseAnnualAmount(expAnnAmtFromEntry);
                                budExpDb.setExpenseAAnnualAmount(expAAnnAmtFromEntry);
                                budExpDb.setExpenseBAnnualAmount(expBAnnAmtFromEntry);

                                budDbMgr.updateExpense(budExpDb);

                                budHelper2 = new DbHelper(getContext());
                                budDb2 = budHelper2.getWritableDatabase();

                                budCV9 = new ContentValues();

                                String[] args4 = new String[]{String.valueOf(expIdFromTag)};

                                budCV9.put(DbHelper.MONEYOUTCAT, nameEntryExp);
                                budCV9.put(DbHelper.MONEYOUTPRIORITY, priorityEntryExp);
                                budCV9.put(DbHelper.MONEYOUTWEEKLY, weeklyEntry);

                                try {
                                    budDb2.update(DbHelper.MONEY_OUT_TABLE_NAME, budCV9, DbHelper.EXPREFKEYMO + "=?", args4);
                                } catch (CursorIndexOutOfBoundsException | SQLException e4) {
                                    e4.printStackTrace();
                                }

                                budDb2.close();

                                budDbMgr.updateExpense(budExpDb);
                                budExpListAdapter.updateExpenses(budDbMgr.getExpense());
                                budExpListAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                budRefresh();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            budExpListHldr.budExpListDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    budExpDb = (ExpenseBudgetDb) budExpListHldr.budExpListDelBtn.getTag();

                    budRepWarnLayout.setVisibility(View.VISIBLE);

                    budRepWarnCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            budRefresh();
                        }
                    });

                    budRepWarnOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            budRepWarnLayout.setVisibility(View.GONE);
                        }
                    });

                    budDbMgr.deleteExpense(budExpDb);
                    budExpListAdapter.updateExpenses(budDbMgr.getExpense());
                    budExpListAdapter.notifyDataSetChanged();

                    budHeaderText();
                }
            });
            return convertView;
        }
    }

    private static class BudIncListViewHolder {
        private TextView budIncListNameTV;
        private TextView budIncListAmtTV;
        private ImageButton budIncListDelBtn;
        private ImageButton budIncListEditBtn;
    }

    private static class BudExpListViewHolder {
        private TextView budExpListNameTV;
        private TextView budExpListAmtTV;
        private ImageButton budExpListDelBtn;
        private ImageButton budExpListEditBtn;
    }*/

    /*public void budSavDateRes() {
        budGen.savingsDataFromEntries(
                savingsNameEntry,
                savingsSeparateS,
                savingsAmountEntry,
                savingsGoalAmountEntry,
                savingsPercentEntry,
                savingsPaymentsEntry,
                savingsAnnualIncomeb,
                savingsAnnuallyRadioButton,
                savingsFrequencyS);
        budSavDate2 = budGen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                savFrqFromEntry,
                savAnnIncFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
    }*/

     /*public void findMatchingDebtId() {
        foundDebtId = false;
        for (DebtDb d : dbManager.getDebts()) {
            if (String.valueOf(d.getIncRefKeyD()).equals(incomeId) || String.valueOf(d.getExpRefKeyD()).equals(expenseId)) {
                foundDebtId = true;
                debtId = String.valueOf(d.getId());
                expRefKeyD = String.valueOf(d.getExpRefKeyD());
                incRefKeyD = String.valueOf(d.getIncRefKeyD());
            }
        }
    }

    public void findMatchingSavingsId() {
        foundSavingsId = false;
        for (SavingsDb s : dbManager.getSavings()) {
            if (String.valueOf(s.getIncRefKeyS()).equals(incomeId) || String.valueOf(s.getExpRefKeyS()).equals(expenseId)) {
                foundSavingsId = true;
                savingsId = String.valueOf(s.getId());
                expRefKeyS = String.valueOf(s.getExpRefKeyS());
                incRefKeyS = String.valueOf(s.getIncRefKeyS());
            }
        }
    }

    public void findAllMatchingIncIds() {

        foundDebtIdInc = false;
        foundSavingsIdInc = false;

        for (DebtDb d : dbManager.getDebts()) {
            if (String.valueOf(d.getIncRefKeyD()).equals(incomeId)) {
                incDebtId = String.valueOf(d.getId());
                expRefKeyD = String.valueOf(d.getExpRefKeyD());
                foundDebtIdInc = true;
                foundSavingsIdInc = false;
            }
        }

        for (SavingsDb s : dbManager.getSavings()) {
            if (String.valueOf(s.getIncRefKeyS()).equals(incomeId)) {
                incSavingsId = String.valueOf(s.getId());
                expRefKeyS = String.valueOf(s.getExpRefKeyS());
                foundSavingsIdInc = true;
                foundDebtIdInc = false;
            }
        }

        for (ExpenseBudgetDb e : dbManager.getExpense()) {
            if (String.valueOf(e.getId()).equals(expRefKeyD)  || String.valueOf(e.getId()).equals(expRefKeyS)) {
                expenseId = String.valueOf(e.getId());
            }
        }
    }

    public void findAllMatchingExpIds() {

        foundDebtIdExp = false;
        foundSavingsIdExp = false;

        for (DebtDb d : dbManager.getDebts()) {
            if (String.valueOf(d.getExpRefKeyD()).equals(expenseId)) {
                expDebtId = String.valueOf(d.getId());
                incRefKeyD = String.valueOf(d.getIncRefKeyD());
                foundDebtIdExp = true;
            }
        }

        for (SavingsDb s : dbManager.getSavings()) {
            if (String.valueOf(s.getExpRefKeyS()).equals(expenseId)) {
                expSavingsId = String.valueOf(s.getId());
                incRefKeyS = String.valueOf(s.getIncRefKeyS());
                foundSavingsIdExp = true;
            }
        }

        for (IncomeBudgetDb i : dbManager.getIncomes()) {
            if (String.valueOf(i.getId()).equals(incRefKeyD) || String.valueOf(i.getId()).equals(incRefKeyS)) {
                incomeId = String.valueOf(i.getId());
            }
        }
    }*/


    /*public class BudIncListAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public BudIncListAdapter(
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

            final ViewHolderIncome incomeHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                incomeHolder = new ViewHolderIncome();
                incomeHolder.incomeName = convertView.findViewById(R.id.list2TV1);
                incomeHolder.incomeAmount = convertView.findViewById(R.id.list2TV2);
                incomeHolder.incomeDeleted = convertView.findViewById(R.id.list2DelButton);
                incomeHolder.incomeEdit = convertView.findViewById(R.id.list2EditButton);
                convertView.setTag(incomeHolder);

            } else {
                incomeHolder = (ViewHolderIncome) convertView.getTag();
            }

            incomeHolder.incomeName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            try {
                incomeAnnualAmountS = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
                if (!incomeAnnualAmountS.equals("")) {
                    incomeAnnualAmountD = Double.valueOf(incomeAnnualAmountS);
                } else {
                    incomeAnnualAmountD = 0.0;
                }
                incomeAnnualAmount2 = currencyFormat.format(incomeAnnualAmountD);
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            } catch (NumberFormatException e2) {
                incomeHolder.incomeAmount.setText(incomeAnnualAmount2);
            }

            //incomeId = String.valueOf(incomes.get(position).getId());

            incomeHolder.incomeDeleted.setTag(incomes.get(position));
            incomeHolder.incomeEdit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            incomeHolder.incomeEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.z_add_edit_budget_income);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetIncomeCategory = findViewById(R.id.budgetIncomeCategory);
                    budgetIncomeAmount = findViewById(R.id.budgetIncomeAmount);
                    budgetIncomeFrequencyRadioGroup = findViewById(R.id.budgetIncomeFrequencyRadioGroup);
                    budgetIncomeWeeklyRadioButton = findViewById(R.id.budgetIncomeWeeklyRadioButton);
                    budgetIncomeBiWeeklyRadioButton = findViewById(R.id.budgetIncomeBiWeeklyRadioButton);
                    budgetIncomeBiMonthlyRadioButton = findViewById(R.id.budgetIncomeBiMonthlyRadioButton);
                    budgetIncomeMonthlyRadioButton = findViewById(R.id.budgetIncomeMonthlyRadioButton);
                    budgetIncomeBiAnnuallyRadioButton = findViewById(R.id.budgetIncomeBiAnnuallyRadioButton);
                    budgetIncomeAnnuallyRadioButton = findViewById(R.id.budgetIncomeAnnuallyRadioButton);
                    budgetAddIncomeButton = findViewById(R.id.budgetAddIncomeButton);
                    budgetUpdateIncomeButton = findViewById(R.id.budgetUpdateIncomeButton);
                    budgetAddIncomeButton.setVisibility(View.GONE);
                    budgetCancelIncomeButton = findViewById(R.id.budgetCancelIncomeButton);

                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeEdit.getTag();
                    incomeId = String.valueOf(incomeBudgetDb.getId());

                    budgetIncomeCategory.setText(incomeBudgetDb.getIncomeName());

                    budgetIncomeAmountD = incomeBudgetDb.getIncomeAmount();
                    budgetIncomeAmountS = currencyFormat.format(budgetIncomeAmountD);
                    budgetIncomeAmount.setText(budgetIncomeAmountS);

                    findMatchingDebtId();
                    findMatchingSavingsId();

                    if (foundDebtId) {
                        budgetIncomeWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetIncomeMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetIncomeAnnuallyRadioButton.setVisibility(View.GONE);
                    } else if (foundSavingsId) {
                        budgetIncomeWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetIncomeMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetIncomeAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    } else {
                        budgetIncomeWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeBiAnnuallyRadioButton.setVisibility(View.VISIBLE);
                        budgetIncomeAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    }

                    if (incomeBudgetDb.getIncomeFrequency() == 52) {
                        budgetIncomeWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "52";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 26) {
                        budgetIncomeBiWeeklyRadioButton.setChecked(true);
                        incomeFrequencyS = "26";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 24) {
                        budgetIncomeBiMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "24";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 12) {
                        budgetIncomeMonthlyRadioButton.setChecked(true);
                        incomeFrequencyS = "12";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 2) {
                        budgetIncomeBiAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "2";
                    } else if (incomeBudgetDb.getIncomeFrequency() == 1) {
                        budgetIncomeAnnuallyRadioButton.setChecked(true);
                        incomeFrequencyS = "1";
                    }

                    //update db if changed
                    budgetIncomeFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetIncomeWeeklyRadioButton:
                                    incomeFrequencyS = "52";
                                    break;
                                case R.id.budgetIncomeBiWeeklyRadioButton:
                                    incomeFrequencyS = "26";
                                    break;
                                case R.id.budgetIncomeBiMonthlyRadioButton:
                                    incomeFrequencyS = "24";
                                    break;
                                case R.id.budgetIncomeMonthlyRadioButton:
                                    incomeFrequencyS = "12";
                                    break;
                                case R.id.budgetIncomeBiAnnuallyRadioButton:
                                    incomeFrequencyS = "2";
                                    break;
                                case R.id.budgetIncomeAnnuallyRadioButton:
                                    incomeFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetCancelIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    budgetUpdateIncomeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //incomeId = String.valueOf(incomes.get(position).getId());

                            findMatchingDebtId();
                            findMatchingSavingsId();

                            if (budgetIncomeCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryInc = budgetIncomeCategory.getText().toString();
                                amountEntry = general.dblFromET(budgetIncomeAmount);
                                frequencyEntry = Double.valueOf(incomeFrequencyS);
                                annualIncome = amountEntry * frequencyEntry;

                                incomeBudgetDb.setIncomeName(nameEntryInc);
                                incomeBudgetDb.setIncomeAmount(amountEntry);
                                incomeBudgetDb.setIncomeFrequency(frequencyEntry);
                                incomeBudgetDb.setIncomeAnnualAmount(annualIncome);

                                for (ExpenseBudgetDb e : dbManager.getExpense()) {
                                    if (String.valueOf(e.getId()).equals(expRefKeyD) || String.valueOf(e.getId()).equals(expRefKeyS)) {
                                        expenseId = String.valueOf(e.getId());
                                    }
                                }

                                dbHelper2 = new DbHelper(getContext());
                                db2 = dbHelper2.getWritableDatabase();

                                String[] args4 = new String[]{debtId};
                                String[] args5 = new String[]{savingsId};
                                String[] args6 = new String[]{expenseId};
                                String[] args7 = new String[]{incomeId};

                                cv = new ContentValues();
                                try {
                                    cv.put(DbHelper.DEBTNAME, nameEntryInc);
                                    cv.put(DbHelper.DEBTANNUALINCOME, annualIncome);
                                    db2.update(DbHelper.DEBTS_TABLE_NAME, cv, DbHelper.ID + "=?", args4);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                allDebtData();

                                cv13 = new ContentValues();
                                try {
                                    cv13.put(DbHelper.DEBTEND, general.calcDebtDate(
                                            debtAmount,
                                            debtRate,
                                            debtPayments,
                                            debtFrequency,
                                            debtAnnualIncome,
                                            getString(R.string.debt_paid),
                                            getString(R.string.too_far)));

                                    db2.update(DbHelper.DEBTS_TABLE_NAME, cv13, DbHelper.ID + "=?", args4);
                                } catch (CursorIndexOutOfBoundsException | SQLException e2) {
                                    e2.printStackTrace();
                                }

                                cv2 = new ContentValues();
                                try {
                                    cv2.put(DbHelper.MONEYOUTDEBTCAT, nameEntryInc);
                                    db2.update(DbHelper.MONEY_OUT_TABLE_NAME, cv2, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args4);
                                } catch (CursorIndexOutOfBoundsException | SQLException e3) {
                                    e3.printStackTrace();
                                }

                                cv3 = new ContentValues();
                                try {
                                    cv3.put(DbHelper.SAVINGSNAME, nameEntryInc);
                                    cv3.put(DbHelper.SAVINGSANNUALINCOME, annualIncome);
                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, cv3, DbHelper.ID + "=?", args5);
                                } catch (CursorIndexOutOfBoundsException | SQLException e4) {
                                    e4.printStackTrace();
                                }

                                savingsDateResult();
                                cv14 = new ContentValues();
                                try {
                                    cv14.put(DbHelper.SAVINGSDATE, savingsDate2);
                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, cv14, DbHelper.ID + "=?", args5);
                                } catch (CursorIndexOutOfBoundsException | SQLException e5) {
                                    e5.printStackTrace();
                                }

                                //general.allSavingsDataFromDb(dbManager, savingsId);

                                allSavingsData();

                                cv14 = new ContentValues();
                                try {
                                    cv14.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome,
                                            getString(R.string.goal_achieved),
                                            getString(R.string.too_far)));

                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, cv14, DbHelper.ID + "=?", args5);
                                } catch (CursorIndexOutOfBoundsException | SQLException e5) {
                                    e5.printStackTrace();
                                }


                                cv4 = new ContentValues();
                                try {
                                    cv4.put(DbHelper.EXPENSENAME, nameEntryInc);
                                    db2.update(DbHelper.EXPENSES_TABLE_NAME, cv4, DbHelper.ID + "=?", args6);
                                } catch (CursorIndexOutOfBoundsException | SQLException e6) {
                                    e6.printStackTrace();
                                }

                                cv5 = new ContentValues();
                                try {
                                    cv5.put(DbHelper.MONEYINCAT, nameEntryInc);
                                    db2.update(DbHelper.MONEY_IN_TABLE_NAME, cv5, DbHelper.INCREFKEYMI + "=?", args7);
                                } catch (CursorIndexOutOfBoundsException | SQLException e7) {
                                    e7.printStackTrace();
                                }

                                cv6 = new ContentValues();
                                try {
                                    cv6.put(DbHelper.MONEYOUTCAT, nameEntryInc);
                                    db2.update(DbHelper.MONEY_OUT_TABLE_NAME, cv6, DbHelper.EXPREFKEYMO + "=?", args6);
                                } catch (CursorIndexOutOfBoundsException | SQLException e8) {
                                    e8.printStackTrace();
                                }

                                try {
                                    String[] args4 = new String[]{debtId};
                                    debtValues3 = new ContentValues();

                                    debtValues3.put(DbHelper.DEBTNAME, nameEntryInc);
                                    debtValues3.put(DbHelper.DEBTANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues3, DbHelper.ID + "=?", args4);

                                    allDebtData();

                                    debtValues4 = new ContentValues();
                                    debtValues4.put(DbHelper.DEBTEND, general.calcDebtDate(
                                            debtAmount,
                                            debtRate,
                                            debtPayments,
                                            debtFrequency,
                                            debtAnnualIncome,
                                            getString(R.string.debt_paid),
                                            getString(R.string.too_far)));
                                    db2.update(DbHelper.DEBTS_TABLE_NAME, debtValues4, DbHelper.ID + "=?", args4);

                                } catch (CursorIndexOutOfBoundsException e4) {
                                    e4.printStackTrace();
                                }

                                try {
                                    String[] args5 = new String[]{savingsId};
                                    savingsValues3 = new ContentValues();

                                    savingsValues3.put(DbHelper.SAVINGSNAME, nameEntryInc);
                                    savingsValues3.put(DbHelper.SAVINGSANNUALINCOME, annualIncome);

                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues3, DbHelper.ID + "=?", args5);

                                    allSavingsData();

                                    savingsDate2 = general.calcSavingsDate(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome,
                                            getString(R.string.goal_achieved),
                                            getString(R.string.too_far));

                                    savingsValues4 = new ContentValues();
                                    savingsValues4.put(DbHelper.SAVINGSDATE, savingsDate2);
                                    db2.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues4, DbHelper.ID + "=?", args5);

                                } catch (CursorIndexOutOfBoundsException e5) {
                                    e5.printStackTrace();
                                }

                                for (ExpenseBudgetDb e : dbManager.getExpense()) {
                                    if (String.valueOf(e.getId()).equals(incRefKeyD)  || String.valueOf(e.getId()).equals(incRefKeyS)) {
                                        expenseId = String.valueOf(e.getId());
                                    }
                                }

                                try {
                                    String[] args6 = new String[]{expenseId};
                                    expValues = new ContentValues();

                                    expValues.put(DbHelper.EXPENSENAME, nameEntryInc);

                                    db2.update(DbHelper.EXPENSES_TABLE_NAME, expValues, DbHelper.ID + "=?", args6);

                                } catch (CursorIndexOutOfBoundsException e6) {
                                    e6.printStackTrace();
                                }

                                try {
                                    String[] args8 = new String[]{expenseId};
                                    moneyOutValues3 = new ContentValues();

                                    moneyOutValues3.put(DbHelper.MONEYOUTCAT, nameEntryInc);

                                    db2.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues3, DbHelper.EXPREFKEYMO + "=?", args8);

                                } catch (CursorIndexOutOfBoundsException e7) {
                                    e7.printStackTrace();
                                }

                                try {
                                    String[] args7 = new String[]{incomeId};
                                    moneyInValues = new ContentValues();

                                    moneyInValues.put(DbHelper.MONEYINCAT, nameEntryInc);

                                    db2.update(DbHelper.MONEY_IN_TABLE_NAME, moneyInValues, DbHelper.INCREFKEYMI + "=?", args7);

                                } catch (CursorIndexOutOfBoundsException e8) {
                                    e8.printStackTrace();
                                }

                                db2.close();

                                dbManager.updateIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                backToBudget();
                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            incomeHolder.incomeDeleted.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    incomeBudgetDb = (IncomeBudgetDb) incomeHolder.incomeDeleted.getTag();

                    incomeId = String.valueOf(incomes.get(position).getId());

                    noSpendingReportText.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            noSpendingReportText.setVisibility(View.GONE);
                            okButton.setVisibility(View.GONE);
                            cancelButton.setVisibility(View.GONE);

                            findMatchingDebtId();
                            findMatchingSavingsId();

                            if (foundDebtId || foundSavingsId) {
                                deleteExpText.setVisibility(View.VISIBLE);
                                ok2Button.setVisibility(View.VISIBLE);

                                ok2Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        backToBudget();
                                    }
                                });
                            } else {
                                deleteExpText.setVisibility(View.GONE);
                                ok2Button.setVisibility(View.GONE);

                                dbManager.deleteIncome(incomeBudgetDb);
                                incomeAdapter.updateIncomes(dbManager.getIncomes());
                                incomeAdapter.notifyDataSetChanged();

                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            return convertView;
        }
    }*/

    /*public class BudExpListAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> expenses;

        public BudExpListAdapter(
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

            final ViewHolderExpense expenseHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                expenseHolder = new ViewHolderExpense();
                expenseHolder.expenseName = convertView.findViewById(R.id.list2TV1);
                expenseHolder.expenseAmount = convertView.findViewById(R.id.list2TV2);
                expenseHolder.expenseDeleted = convertView.findViewById(R.id.list2DelButton);
                expenseHolder.expenseEdit = convertView.findViewById(R.id.list2EditButton);
                convertView.setTag(expenseHolder);

            } else {
                expenseHolder = (ViewHolderExpense) convertView.getTag();
            }

            expenseHolder.expenseName.setText(expenses.get(position).getExpenseName());

            //retrieve incomeAmount and format as currency
            try {
                expenseAnnualAmountS = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
                if (!expenseAnnualAmountS.equals("")) {
                    expenseAnnualAmountD = Double.valueOf(expenseAnnualAmountS);
                } else {
                    expenseAnnualAmountD = 0.0;
                }
                expenseAnnualAmount2 = currencyFormat.format(expenseAnnualAmountD);
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            } catch (NumberFormatException e8) {
                expenseHolder.expenseAmount.setText(expenseAnnualAmount2);
            }

            //expenseId = String.valueOf(expenses.get(position).getId());

            expenseHolder.expenseDeleted.setTag(expenses.get(position));
            expenseHolder.expenseEdit.setTag(expenses.get(position));

            //click on pencil icon to edit a data record
            expenseHolder.expenseEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_5_add_expense);
                    LayoutBudget.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    budgetExpenseCategory = findViewById(R.id.budgetExpenseCategory);
                    budgetExpenseAmount = findViewById(R.id.budgetExpenseAmount);
                    budgetExpenseFrequencyRadioGroup = findViewById(R.id.budgetExpenseFrequencyRadioGroup);
                    weeklyGuidanceLabel = findViewById(R.id.weeklyGuidanceLabel);
                    budgetExpenseABRadioGroup = findViewById(R.id.budgetExpenseABRadioGroup);
                    budgetExpenseReminderRadioGroup = findViewById(R.id.budgetExpenseReminderRadioGroup);
                    budgetCancelExpenseButton = findViewById(R.id.budgetCancelExpenseButton);
                    budgetAddExpenseButton = findViewById(R.id.budgetAddExpenseButton);
                    budgetUpdateExpenseButton = findViewById(R.id.budgetUpdateExpenseButton);
                    budgetAddExpenseButton.setVisibility(View.GONE);
                    weeklyGuidanceLabel.setVisibility(View.GONE);
                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);

                    budgetExpenseWeeklyRadioButton = findViewById(R.id.budgetExpenseWeeklyRadioButton);
                    budgetExpenseBiWeeklyRadioButton = findViewById(R.id.budgetExpenseBiWeeklyRadioButton);
                    budgetExpenseBiMonthlyRadioButton = findViewById(R.id.budgetExpenseBiMonthlyRadioButton);
                    budgetExpenseMonthlyRadioButton = findViewById(R.id.budgetExpenseMonthlyRadioButton);
                    budgetExpenseBiAnnuallyRadioButton = findViewById(R.id.budgetExpenseBiAnnuallyRadioButton);
                    budgetExpenseAnnuallyRadioButton = findViewById(R.id.budgetExpenseAnnuallyRadioButton);

                    budgetExpenseARadioButton = findViewById(R.id.budgetExpenseARadioButton);
                    budgetExpenseBRadioButton = findViewById(R.id.budgetExpenseBRadioButton);

                    budgetExpenseYesRadioButton = findViewById(R.id.budgetExpenseYesRadioButton);
                    budgetExpenseNoRadioButton = findViewById(R.id.budgetExpenseNoRadioButton);

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseEdit.getTag();
                    expenseId = String.valueOf(expenseBudgetDb.getId());

                    budgetExpenseCategory.setText(expenseBudgetDb.getExpenseName());

                    budgetExpenseAmountD = expenseBudgetDb.getExpenseAmount();
                    budgetExpenseAmountS = currencyFormat.format(budgetExpenseAmountD);
                    budgetExpenseAmount.setText(budgetExpenseAmountS);

                    findMatchingDebtId();
                    findMatchingSavingsId();

                    if (foundDebtId) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.GONE);
                    } else if (foundSavingsId) {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.GONE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.GONE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    } else {
                        budgetExpenseWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiWeeklyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseMonthlyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseBiAnnuallyRadioButton.setVisibility(View.VISIBLE);
                        budgetExpenseAnnuallyRadioButton.setVisibility(View.VISIBLE);
                    }

                    //set radio buttons from data
                    if (expenseBudgetDb.getExpenseFrequency() == 52) {
                        budgetExpenseWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "52";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 26) {
                        budgetExpenseBiWeeklyRadioButton.setChecked(true);
                        expenseFrequencyS = "26";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 24) {
                        budgetExpenseBiMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "24";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 12) {
                        budgetExpenseMonthlyRadioButton.setChecked(true);
                        expenseFrequencyS = "12";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 2) {
                        budgetExpenseBiAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "2";
                    } else if (expenseBudgetDb.getExpenseFrequency() == 1) {
                        budgetExpenseAnnuallyRadioButton.setChecked(true);
                        expenseFrequencyS = "1";
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpensePriority())) {
                        case "A":
                            budgetExpenseARadioButton.setChecked(true);
                            expensePriorityS = "A";
                            budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                            weeklyGuidanceLabel.setVisibility(View.GONE);
                            break;
                        case "B":
                            budgetExpenseBRadioButton.setChecked(true);
                            expensePriorityS = "B";
                            budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                            weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                            break;
                    }

                    switch (String.valueOf(expenseBudgetDb.getExpenseWeekly())) {
                        case "Y":
                            budgetExpenseYesRadioButton.setChecked(true);
                            expenseWeeklyS = "Y";
                            break;
                        case "N":
                            budgetExpenseNoRadioButton.setChecked(true);
                            expenseWeeklyS = "N";
                            break;
                    }


                    //update db if changed
                    budgetExpenseFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseWeeklyRadioButton:
                                    expenseFrequencyS = "52";
                                    break;
                                case R.id.budgetExpenseBiWeeklyRadioButton:
                                    expenseFrequencyS = "26";
                                    break;
                                case R.id.budgetExpenseBiMonthlyRadioButton:
                                    expenseFrequencyS = "24";
                                    break;
                                case R.id.budgetExpenseMonthlyRadioButton:
                                    expenseFrequencyS = "12";
                                    break;
                                case R.id.budgetExpenseBiAnnuallyRadioButton:
                                    expenseFrequencyS = "2";
                                    break;
                                case R.id.budgetExpenseAnnuallyRadioButton:
                                    expenseFrequencyS = "1";
                                    break;
                            }
                        }
                    });

                    budgetExpenseABRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseARadioButton:
                                    expensePriorityS = "A";
                                    expenseWeeklyS = "N";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.GONE);
                                    weeklyGuidanceLabel.setVisibility(View.GONE);
                                    break;
                                case R.id.budgetExpenseBRadioButton:
                                    expensePriorityS = "B";
                                    budgetExpenseReminderRadioGroup.setVisibility(View.VISIBLE);
                                    weeklyGuidanceLabel.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                    });

                    budgetExpenseReminderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.budgetExpenseYesRadioButton:
                                    expenseWeeklyS = "Y";
                                    break;
                                case R.id.budgetExpenseNoRadioButton:
                                    expenseWeeklyS = "N";
                                    break;
                            }
                        }

                    });

                    budgetCancelExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    budgetUpdateExpenseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //expenseId = String.valueOf(expenses.get(position).getId());

                            findMatchingDebtId();
                            findMatchingSavingsId();

                            if (budgetExpenseCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryExp = budgetExpenseCategory.getText().toString();
                                amountEntry = general.dblFromET(budgetExpenseAmount);
                                frequencyEntry = Double.valueOf(expenseFrequencyS);
                                priorityEntryExp = String.valueOf(expensePriorityS);
                                weeklyEntry = String.valueOf(expenseWeeklyS);
                                annualAmount = amountEntry * frequencyEntry;

                                expenseBudgetDb.setExpenseName(nameEntryExp);
                                expenseBudgetDb.setExpenseAmount(amountEntry);
                                expenseBudgetDb.setExpenseFrequency(frequencyEntry);
                                expenseBudgetDb.setExpensePriority(priorityEntryExp);
                                expenseBudgetDb.setExpenseWeekly(weeklyEntry);
                                expenseBudgetDb.setExpenseAnnualAmount(annualAmount);

                                if (priorityEntryExp.equals("A")) {
                                    expenseBudgetDb.setExpenseAAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseBAnnualAmount(0.0);
                                } else if (priorityEntryExp.equals("B")) {
                                    expenseBudgetDb.setExpenseBAnnualAmount(annualAmount);
                                    expenseBudgetDb.setExpenseAAnnualAmount(0.0);
                                }

                                for (IncomeBudgetDb i : dbManager.getIncomes()) {
                                    if (String.valueOf(i.getId()).equals(incRefKeyD) || String.valueOf(i.getId()).equals(incRefKeyS)) {
                                        incomeId = String.valueOf(i.getId());
                                    }
                                }

                                dbHelper = new DbHelper(getContext());
                                db = dbHelper.getWritableDatabase();

                                String[] args = new String[]{debtId};
                                String[] args2 = new String[]{savingsId};
                                String[] args3 = new String[]{incomeId};
                                String[] args4 = new String[]{expenseId};

                                cv7 = new ContentValues();
                                cv8 = new ContentValues();
                                cv9 = new ContentValues();
                                cv10 = new ContentValues();
                                cv11 = new ContentValues();
                                cv12 = new ContentValues();

                                cv7.put(DbHelper.DEBTNAME, nameEntryExp);
                                cv7.put(DbHelper.DEBTPAYMENTS, amountEntry);
                                cv7.put(DbHelper.DEBTFREQUENCY, frequencyEntry);
                                cv8.put(DbHelper.SAVINGSNAME, nameEntryExp);
                                cv8.put(DbHelper.SAVINGSPAYMENTS, amountEntry);
                                cv8.put(DbHelper.SAVINGSFREQUENCY, frequencyEntry);
                                cv9.put(DbHelper.INCOMENAME, nameEntryExp);
                                cv10.put(DbHelper.MONEYOUTCAT, nameEntryExp);
                                cv10.put(DbHelper.MONEYOUTPRIORITY, priorityEntryExp);
                                cv10.put(DbHelper.MONEYOUTWEEKLY, weeklyEntry);
                                cv11.put(DbHelper.MONEYOUTDEBTCAT, nameEntryExp);
                                cv12.put(DbHelper.MONEYINCAT, nameEntryInc);

                                try {
                                    db.update(DbHelper.DEBTS_TABLE_NAME, cv7, DbHelper.ID + "=?", args);
                                    db.update(DbHelper.SAVINGS_TABLE_NAME, cv8, DbHelper.ID + "=?", args2);
                                    db.update(DbHelper.INCOME_TABLE_NAME, cv9, DbHelper.ID + "=?", args3);
                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, cv10, DbHelper.EXPREFKEYMO + "=?", args4);
                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, cv11, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args);
                                    db.update(DbHelper.MONEY_IN_TABLE_NAME, cv12, DbHelper.INCREFKEYMI + "=?", args3);
                                } catch (CursorIndexOutOfBoundsException | SQLException e4) {
                                    e4.printStackTrace();
                                }

                                allDebtData();

                                cv7.put(DbHelper.DEBTEND, general.calcDebtDate(
                                        debtAmount,
                                        debtRate,
                                        debtPayments,
                                        debtFrequency,
                                        debtAnnualIncome,
                                        getString(R.string.debt_paid),
                                        getString(R.string.too_far)));
                                try {
                                    db.update(DbHelper.DEBTS_TABLE_NAME, cv7, DbHelper.ID + "=?", args);
                                } catch (CursorIndexOutOfBoundsException | SQLException e5) {
                                    e5.printStackTrace();
                                }

                                //general.allSavingsDataFromDb(dbManager, savingsId);
                                allSavingsData();

                                cv8.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                                        savingsGoal,
                                        savingsAmount,
                                        savingsRate,
                                        savingsPayments,
                                        savingsFrequency,
                                        savingsAnnualIncome,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)));
                                try {
                                    db.update(DbHelper.SAVINGS_TABLE_NAME, cv8, DbHelper.ID + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException | SQLException e6) {
                                    e6.printStackTrace();
                                }

                                try {
                                    String[] args = new String[]{debtId};
                                    debtValues = new ContentValues();

                                    debtValues.put(DbHelper.DEBTNAME, nameEntryExp);
                                    debtValues.put(DbHelper.DEBTPAYMENTS, amountEntry);
                                    debtValues.put(DbHelper.DEBTFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues, DbHelper.ID + "=?", args);

                                    allDebtData();

                                    debtValues2 = new ContentValues();
                                    debtValues2.put(DbHelper.DEBTEND, general.calcDebtDate(
                                            debtAmount,
                                            debtRate,
                                            debtPayments,
                                            debtFrequency,
                                            debtAnnualIncome,
                                            getString(R.string.debt_paid),
                                            getString(R.string.too_far)));
                                    db.update(DbHelper.DEBTS_TABLE_NAME, debtValues2, DbHelper.ID + "=?", args);

                                } catch (CursorIndexOutOfBoundsException e10) {
                                    e10.printStackTrace();
                                }

                                try {
                                    String[] args2 = new String[]{savingsId};
                                    savingsValues = new ContentValues();

                                    savingsValues.put(DbHelper.SAVINGSNAME, nameEntryExp);
                                    savingsValues.put(DbHelper.SAVINGSPAYMENTS, amountEntry);
                                    savingsValues.put(DbHelper.SAVINGSFREQUENCY, frequencyEntry);

                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues, DbHelper.ID + "=?", args2);

                                    allSavingsData();

                                    savingsDate2 = general.calcSavingsDate(
                                            savingsGoal,
                                            savingsAmount,
                                            savingsRate,
                                            savingsPayments,
                                            savingsFrequency,
                                            savingsAnnualIncome,
                                            getString(R.string.goal_achieved),
                                            getString(R.string.too_far));

                                    savingsValues2 = new ContentValues();
                                    savingsValues2.put(DbHelper.SAVINGSDATE, savingsDate2);
                                    db.update(DbHelper.SAVINGS_TABLE_NAME, savingsValues2, DbHelper.ID + "=?", args2);

                                } catch (CursorIndexOutOfBoundsException e11) {
                                    e11.printStackTrace();
                                }

                                for (IncomeBudgetDb i : dbManager.getIncomes()) {
                                    if (String.valueOf(i.getId()).equals(expRefKeyD)  || String.valueOf(i.getId()).equals(expRefKeyS)) {
                                        incomeId = String.valueOf(i.getId());
                                    }
                                }

                                try {
                                    String[] args3 = new String[]{incomeId};
                                    incValues = new ContentValues();

                                    incValues.put(DbHelper.INCOMENAME, nameEntryExp);

                                    db.update(DbHelper.INCOME_TABLE_NAME, incValues, DbHelper.ID + "=?", args3);

                                } catch (CursorIndexOutOfBoundsException e12) {
                                    e12.printStackTrace();
                                }


                                try {
                                    String[] args4 = new String[]{expenseId};
                                    //String[] args4 = new String[]{debtId};
                                    moneyOutValues = new ContentValues();
                                    moneyOutValues2 = new ContentValues();

                                    moneyOutValues.put(DbHelper.MONEYOUTCAT, nameEntryExp);
                                    moneyOutValues.put(DbHelper.MONEYOUTPRIORITY, priorityEntryExp);
                                    moneyOutValues.put(DbHelper.MONEYOUTWEEKLY, weeklyEntry);
                                    moneyOutValues2.put(DbHelper.MONEYOUTDEBTCAT, nameEntryExp);

                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues, DbHelper.EXPREFKEYMO + "=?", args4);
                                    db.update(DbHelper.MONEY_OUT_TABLE_NAME, moneyOutValues2, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args4);

                                } catch (CursorIndexOutOfBoundsException e13) {
                                    e13.printStackTrace();
                                }

                                db.close();

                                dbManager.updateExpense(expenseBudgetDb);
                                expenseAdapter.updateExpenses(dbManager.getExpense());
                                expenseAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                backToBudget();
                                budgetHeaderText();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            expenseHolder.expenseDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expenseBudgetDb = (ExpenseBudgetDb) expenseHolder.expenseDeleted.getTag();

                    expenseId = String.valueOf(expenses.get(position).getId());

                    noSpendingReportText.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToBudget();
                        }
                    });

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            noSpendingReportText.setVisibility(View.GONE);
                            okButton.setVisibility(View.GONE);
                            cancelButton.setVisibility(View.GONE);

                            findMatchingDebtId();
                            findMatchingSavingsId();

                            if (foundDebtId || foundSavingsId) {
                                deleteExpText.setVisibility(View.VISIBLE);
                                ok2Button.setVisibility(View.VISIBLE);

                                ok2Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        backToBudget();
                                    }
                                });
                            } else {
                                deleteExpText.setVisibility(View.GONE);
                                ok2Button.setVisibility(View.GONE);

                                dbManager.deleteExpense(expenseBudgetDb);
                                expenseAdapter.updateExpenses(dbManager.getExpense());
                                budgetHeaderText();
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }*/

    /*private static class ViewHolderIncome {
        private TextView incomeName;
        private TextView incomeAmount;
        private ImageButton incomeDeleted;
        private ImageButton incomeEdit;
    }*/

    /*private static class ViewHolderExpense {
        private TextView expenseName;
        private TextView expenseAmount;
        private ImageButton expenseDeleted;
        private ImageButton expenseEdit;
    }*/

    /*public void budHeaderText() {

        budTotInc = budDbMgr.sumTotalIncome();
        budTotExp = budDbMgr.sumTotalExpenses();
        budGen.dblASCurrency(String.valueOf(budTotInc), budTotIncTV);
        budGen.dblASCurrency(String.valueOf(budTotExp), budTotExpTV);

        budStillAvail = budTotInc - budTotExp;

        if (budStillAvail < 0) {
            budOverWarnLabel.setVisibility(View.VISIBLE);
            budOverWarnTV.setVisibility(View.VISIBLE);
            budGen.dblASCurrency("-" + String.valueOf(budStillAvail), budOverWarnTV);
            budHeaderLabel.setVisibility(View.GONE);
            budHeaderTV.setVisibility(View.GONE);

        } else {
            budOverWarnLabel.setVisibility(View.GONE);
            budOverWarnTV.setVisibility(View.GONE);
            budHeaderLabel.setVisibility(View.VISIBLE);
            budHeaderTV.setVisibility(View.VISIBLE);
            budGen.dblASCurrency(String.valueOf(budStillAvail), budHeaderTV);
        }

        try {
            totalIncomeS = String.valueOf(totalIncomeR);
            if (totalIncomeS != null && !totalIncomeS.equals("")) {
                totalIncomeD = Double.valueOf(totalIncomeS);
            } else {
                totalIncomeD = 0.0;
            }

            totalIncome2 = currencyFormat.format(totalIncomeD);
            budgetIncomeTotalText.setText(totalIncome2);

        } catch (NumberFormatException e) {
            budgetIncomeTotalText.setText(totalIncome2);
        }

        try {
            totalExpensesS = String.valueOf(totalExpensesR);
            if (totalExpensesS != null && !totalExpensesS.equals("")) {
                totalExpensesD = Double.valueOf(totalExpensesS);
            } else {
                totalExpensesD = 0.0;
            }

            totalExpenses2 = currencyFormat.format(totalExpensesD);
            budgetExpensesTotalText.setText(totalExpenses2);

        } catch (NumberFormatException e) {
            budgetExpensesTotalText.setText(totalExpenses2);
        }

        if (budTotExp > budTotInc) {

            incomeAvailableN = (totalExpensesR - totalIncomeR);
            incomeAvailableN2 = currencyFormat.format(incomeAvailableN);

            budgetOopsText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setVisibility(View.VISIBLE);
            budgetOopsAmountText.setText("-" + incomeAvailableN2);
            headerLabel2.setVisibility(View.GONE);
            incomeAvailable.setVisibility(View.GONE);
        }

        if (totalIncomeD >= totalExpensesD) {
            budgetOopsText.setVisibility(View.GONE);
            budgetOopsAmountText.setVisibility(View.GONE);
            headerLabel2.setVisibility(View.VISIBLE);
            incomeAvailable.setVisibility(View.VISIBLE);
        }

        incomeAvailableD = (totalIncomeR - totalExpensesR);
        incomeAvailable2 = currencyFormat.format(incomeAvailableD);
        incomeAvailable.setText(incomeAvailable2);

    }*/

    /*View.OnClickListener onClickDoneBudgetSetUpButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetDone = 1;

            setUpDb = new SetUpDb(incomeDone, billsDone, debtsDone, savingsDone, budgetDone, balanceDone, balanceAmount, tourDone, 0);
            dbManager.addSetUp(setUpDb);

            toast = Toast.makeText(getApplicationContext(), R.string.edit_budget_message, Toast.LENGTH_LONG);
            toastLayout = (LinearLayout) toast.getView();
            tv = (TextView) toastLayout.getChildAt(0);
            tv.setTextSize(20);
            toast.show();

            backToSetUp = new Intent(LayoutBudget.this, LayoutSetUp.class);
            backToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToSetUp);

        }
    };*/

    /*View.OnClickListener onClickNoTimeBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetSetUpNoTime2.setVisibility(View.VISIBLE);
            budgetSetUpTimeButton.setVisibility(View.VISIBLE);
            budgetSetUpTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budgetSetUpNoTime2.setVisibility(View.GONE);
                    budgetSetUpTimeButton.setVisibility(View.GONE);
                }
            });
        }
    };*/

    /*View.OnClickListener onClickNeedHelpBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            budgetSetUpNeedHelp2.setVisibility(View.VISIBLE);
            budgetSetUpHelpButton.setVisibility(View.VISIBLE);
            budgetSetUpHelpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budgetSetUpNeedHelp2.setVisibility(View.GONE);
                    budgetSetUpHelpButton.setVisibility(View.GONE);
                }
            });
        }
    };*/

    /*public void allDebtData() {
        for (DebtDb d3 : dbManager.getDebts()) {
            if (String.valueOf(d3.getId()).equals(debtId)) {
                debtAmount = d3.getDebtAmount();
                debtRate = d3.getDebtRate();
                debtPayments = d3.getDebtPayments();
                debtFrequency = d3.getDebtFrequency();
                debtAnnualIncome = d3.getDebtAnnualIncome();
            }
        }
    }*/

    /*public void allDebtDataInc() {
        for (DebtDb d4 : dbManager.getDebts()) {
            if (String.valueOf(d4.getId()).equals(debtId)) {
                debtAmount = d4.getDebtAmount();
                debtRate = d4.getDebtRate();
                debtPayments = d4.getDebtPayments();
                debtFrequency = d4.getDebtFrequency();
                debtAnnualIncome = d4.getDebtAnnualIncome();
            }
        }
    }*/

    /*public void allSavingsData() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (String.valueOf(s2.getId()).equals(savingsId)) {
                savingsAmount = s2.getSavingsAmount();
                savingsGoal = s2.getSavingsGoal();
                savingsRate = s2.getSavingsRate();
                //savingsRate = currentSavingsRate / 100;
                savingsPayments = s2.getSavingsPayments();
                savingsFrequency = s2.getSavingsFrequency();
                savingsAnnualIncome = s2.getSavingsAnnualIncome();
            }
        }
    }*/

    /*public void allDataSavingsInc() {
        for (SavingsDb s3 : dbManager.getSavings()) {
            if (String.valueOf(s3.getId()).equals(incSavingsId)) {
                savingsAmount = s3.getSavingsAmount();
                currentSavingsRate = s3.getSavingsRate();
                savingsRate = currentSavingsRate / 100;
                savingsPayments = s3.getSavingsPayments();
                savingsFrequency = s3.getSavingsFrequency();
                savingsAnnualIncome = s3.getSavingsAnnualIncome();
            }
        }
    }*/

}
