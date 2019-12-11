package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

import ca.gotchasomething.mynance.data.BudgetDb;

public class AddExpenseList extends MainNavigation {

    BudgetDb expLstExpDB;
    Button expLstAddMoreButton, expLstDoneButton, expLstExpCancelBtn, expLstExpSaveBtn, expLstExpUpdateBtn;
    ContentValues expLstCV;
    DbHelper expLstHelper;
    DbManager expLstDbMgr;
    Double expAmtFromEntry = 0.0, expAmtFromTag = 0.0, expFrqFromTag = 0.0, expFrqFromEntry = 0.0, expLstNewExpAnnAmt = 0.0;
    EditText expLstExpAmtET, expLstExpCatET;
    General expLstGen;
    ExpLstLstAdapter expLstLstAdapter;
    Intent expLstToBud, expLstToCCPur, expLstToMonOut, expLstToWklyList, expLstRefresh, expLstToSetUp, expLstToAddMore, expLstToAnalysis;
    LinearLayout expLstSpinLayout;
    ListView expLstListView;
    long expIdFromTag;
    RadioButton expLstExpARB, expLstExpBRB, expLstExpAnnlyRB, expLstExpBiAnnlyRB, expLstExpBiMthlyRB, expLstExpBiWklyRB, expLstExpMthlyRB, expLstExpWklyRB,
            expLstExpNoWklyRB, expLstExpYesWklyRB;
    RadioGroup expLstExpABRG, expLstExpFrqRG, expLstExpWklyRG;
    SQLiteDatabase expLstDB;
    String expNameFromEntry = null, expLstExpPriorityRB = null, expPriorityFromEntry = null, expPriorityFromTag = null, expWeeklyFromEntry = null,
            expWeeklyFromTag = null, expLstAnnAmt2 = null, expLstExpFrqRB = null, expLstExpWeeklyRB = null, expNameFromTag = null;
    TextView expLstHeaderLabelTV, expLstExpWklyLabel, expLstTotalTV;
    View expLstExpLine1, expLstExpLine2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        expLstDbMgr = new DbManager(this);
        expLstGen = new General();

        expLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        expLstSpinLayout.setVisibility(View.GONE);
        expLstTotalTV = findViewById(R.id.layout1TotalTV);
        expLstTotalTV.setVisibility(View.GONE);

        expLstHeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        if(expLstDbMgr.retrieveLatestDone().equals("income")) {
            expLstHeaderLabelTV.setText(getString(R.string.bills));
        } else {
            expLstHeaderLabelTV.setText(getString(R.string.expenses));
        }

        expLstListView = findViewById(R.id.layout1ListView);
        expLstAddMoreButton = findViewById(R.id.layout1AddMoreBtn);
        if(expLstDbMgr.retrieveLatestDone().equals("income")) {
            expLstAddMoreButton.setText(getString(R.string.another_bill));
        } else {
            expLstAddMoreButton.setText(getString(R.string.another_expense));
        }

        expLstDoneButton = findViewById(R.id.layout1DoneBtn);

        expLstAddMoreButton.setOnClickListener(onClickExpLstAddMoreButton);
        expLstDoneButton.setOnClickListener(onClickExpLstDoneButton);

        expLstLstAdapter = new ExpLstLstAdapter(this, expLstDbMgr.getExpenses());
        expLstListView.setAdapter(expLstLstAdapter);
    }

    public void expLstRefresh() {
        expLstRefresh = new Intent(AddExpenseList.this, AddExpenseList.class);
        expLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(expLstRefresh);
    }

    public void expLstToMonOut() {
        expLstToMonOut = new Intent(AddExpenseList.this, LayoutMoneyOut.class);
        expLstToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(expLstToMonOut);
    }

    View.OnClickListener onClickExpLstAddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expLstToAddMore = new Intent(AddExpenseList.this, AddExpense.class);
            expLstToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(expLstToAddMore);
        }
    };

    View.OnClickListener onClickExpLstDoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (expLstDbMgr.retrieveLatestDone().equals("savings")) {
                expLstToAnalysis = new Intent(AddExpenseList.this, SetUpAnalysis.class);
                expLstToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToAnalysis);
            } else if(expLstDbMgr.retrieveLatestDone().equals("income")) {
                expLstCV = new ContentValues();
                expLstCV.put(DbHelper.LATESTDONE, "bills");
                expLstHelper = new DbHelper(getApplicationContext());
                expLstDB = expLstHelper.getWritableDatabase();
                expLstDB.update(DbHelper.SET_UP_TABLE_NAME, expLstCV, DbHelper.ID + "= '1'", null);
                expLstDB.close();

                expLstToSetUp = new Intent(AddExpenseList.this, LayoutSetUp.class);
                expLstToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToSetUp);
            } else if(expLstDbMgr.retrieveLatestDone().equals("analysis")) {
                expLstToWklyList = new Intent(AddExpenseList.this, LayoutWeeklyLimitsList.class);
                expLstToWklyList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToWklyList);
            } else if(expLstDbMgr.retrieveLastPageId() == 2) {
                expLstToBud = new Intent(AddExpenseList.this, LayoutBudget.class);
                expLstToBud.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToBud);
            } else if(expLstDbMgr.retrieveLastPageId() == 3) {
                expLstToMonOut();
            } else if(expLstDbMgr.retrieveLastPageId() == 7) {
                expLstToCCPur = new Intent(AddExpenseList.this, LayoutCCPur.class);
                expLstToCCPur.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToCCPur);
            } else if(expLstDbMgr.retrieveLastPageId() == 4) {
                expLstToWklyList = new Intent(AddExpenseList.this, LayoutWeeklyLimitsList.class);
                expLstToWklyList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(expLstToWklyList);
            }
        }
    };

    public class ExpLstLstAdapter extends ArrayAdapter<BudgetDb> {

        public Context context;
        public List<BudgetDb> expense;

        public ExpLstLstAdapter(
                Context context,
                List<BudgetDb> expense) {

            super(context, -1, expense);

            this.context = context;
            this.expense = expense;
        }

        public void updateExpenses(List<BudgetDb> expense) {
            this.expense = expense;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expense.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ExpLstViewHolder expLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                expLstHldr = new ExpLstViewHolder();
                expLstHldr.expLstExpName = convertView.findViewById(R.id.list2TV1);
                expLstHldr.expLstExpAmt = convertView.findViewById(R.id.list2TV2);
                expLstHldr.expLstDel = convertView.findViewById(R.id.list2DelBtn);
                expLstHldr.expLstEdit = convertView.findViewById(R.id.list2EditBtn);
                if(expLstDbMgr.retrieveLastPageId() == 3 || expLstDbMgr.retrieveLastPageId() == 7 || expLstDbMgr.retrieveLastPageId() ==4) {
                    //IF COMING FROM LAYOUTMONEYOUT, NO EDIT/DELETE
                    //IF COMING FROM LAYOUTCCPUR, NO EDIT/DELETE
                    //IF COMING FROM LAYOUTWEEKLYLIMITSLIST, NO EDIT/DELETE
                    //IF DURING SET UP, YES EDIT/DELETE
                    //IF COMING FROM LAYOUTBUDGET, YES EDIT/DELETE
                    expLstHldr.expLstDel.setVisibility(View.GONE);
                    expLstHldr.expLstEdit.setVisibility(View.GONE);
                }
                convertView.setTag(expLstHldr);

            } else {
                expLstHldr = (ExpLstViewHolder) convertView.getTag();
            }

            expLstHldr.expLstExpName.setText(expense.get(position).getBdgtCat());

            //retrieve incomeAnnualAmount and format as currency
            expLstAnnAmt2 = String.valueOf((expense.get(position).getBdgtAnnPayt()));
            expLstGen.dblASCurrency(expLstAnnAmt2, expLstHldr.expLstExpAmt);

            expLstHldr.expLstDel.setTag(expense.get(position));
            expLstHldr.expLstEdit.setTag(expense.get(position));

            //click on pencil icon to edit a data record
            expLstHldr.expLstEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_5_add_expense);
                    AddExpenseList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    expLstExpCatET = findViewById(R.id.addExpCatET);
                    expLstExpAmtET = findViewById(R.id.addExpAmtET);
                    expLstExpFrqRG = findViewById(R.id.addExpFrqRG);
                    expLstExpABRG = findViewById(R.id.addExpABRG);
                    expLstExpWklyLabel = findViewById(R.id.addExpWklyLabel);
                    expLstExpWklyLabel.setVisibility(View.GONE);
                    expLstExpWklyRG = findViewById(R.id.addExpWklyRG);
                    expLstExpWklyRG.setVisibility(View.GONE);
                    expLstExpCancelBtn = findViewById(R.id.addExpCancelBtn);
                    expLstExpSaveBtn = findViewById(R.id.addExpSaveBtn);
                    expLstExpSaveBtn.setVisibility(View.GONE);
                    expLstExpUpdateBtn = findViewById(R.id.addExpUpdateBtn);
                    expLstExpLine1 = findViewById(R.id.addExpLine1);
                    expLstExpLine2 = findViewById(R.id.addExpLine2);

                    if(expLstDbMgr.retrieveLatestDone().equals("income") || expLstDbMgr.retrieveLatestDone().equals("analysis") || expLstDbMgr.retrieveLastPageId() == 4) {
                        expLstExpABRG.setVisibility(View.GONE);
                        expLstExpWklyRG.setVisibility(View.GONE);
                        expLstExpLine1.setVisibility(View.GONE);
                        expLstExpLine2.setVisibility(View.GONE);
                        expLstExpWklyLabel.setVisibility(View.GONE);
                    }

                    expLstExpWklyRB = findViewById(R.id.addExpWklyRB);
                    expLstExpBiWklyRB = findViewById(R.id.addExpBiWklyRB);
                    expLstExpBiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
                    expLstExpMthlyRB = findViewById(R.id.addExpMthlyRB);
                    expLstExpBiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
                    expLstExpAnnlyRB = findViewById(R.id.addExpAnnlyRB);

                    expLstExpARB = findViewById(R.id.addExpARB);
                    expLstExpBRB = findViewById(R.id.addExpBRB);

                    expLstExpYesWklyRB = findViewById(R.id.addExpYesWklyRB);
                    expLstExpNoWklyRB = findViewById(R.id.addExpNoWklyRB);

                    expLstExpDB = (BudgetDb) expLstHldr.expLstEdit.getTag();
                    expNameFromTag = expLstExpDB.getBdgtCat();
                    expAmtFromTag = expLstExpDB.getBdgtPaytAmt();
                    expFrqFromTag = expLstExpDB.getBdgtPaytFrq();
                    expPriorityFromTag = expLstExpDB.getBdgtPriority();
                    expWeeklyFromTag = expLstExpDB.getBdgtWeekly();
                    expIdFromTag = expLstExpDB.getId();

                    expLstExpCatET.setText(expNameFromTag);
                    expLstGen.dblASCurrency(String.valueOf(expAmtFromTag), expLstExpAmtET);

                    //set radio buttons from data
                    if (expFrqFromTag == 52) {
                        expLstExpWklyRB.setChecked(true);
                        expLstExpFrqRB = "52";
                    } else if (expFrqFromTag == 26) {
                        expLstExpBiWklyRB.setChecked(true);
                        expLstExpFrqRB = "26";
                    } else if (expFrqFromTag == 24) {
                        expLstExpBiMthlyRB.setChecked(true);
                        expLstExpFrqRB = "24";
                    } else if (expFrqFromTag == 12) {
                        expLstExpMthlyRB.setChecked(true);
                        expLstExpFrqRB = "12";
                    } else if (expFrqFromTag == 2) {
                        expLstExpBiAnnlyRB.setChecked(true);
                        expLstExpFrqRB = "2";
                    } else if (expFrqFromTag == 1) {
                        expLstExpAnnlyRB.setChecked(true);
                        expLstExpFrqRB = "1";
                    }

                    switch (expPriorityFromTag) {
                        case "A":
                            expLstExpARB.setChecked(true);
                            expLstExpPriorityRB = "A";
                            expLstExpWklyRG.setVisibility(View.GONE);
                            expLstExpWklyLabel.setVisibility(View.GONE);
                            break;
                        case "B":
                            expLstExpBRB.setChecked(true);
                            expLstExpPriorityRB = "B";
                            expLstExpWklyRG.setVisibility(View.VISIBLE);
                            expLstExpWklyLabel.setVisibility(View.VISIBLE);
                            break;
                    }

                    switch (expWeeklyFromTag) {
                        case "Y":
                            expLstExpYesWklyRB.setChecked(true);
                            expLstExpWeeklyRB = "Y";
                            break;
                        case "N":
                            expLstExpNoWklyRB.setChecked(true);
                            expLstExpWeeklyRB = "N";
                            break;
                    }

                    //update db if changed
                    expLstExpFrqRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpWklyRB:
                                    expLstExpFrqRB = "52";
                                    break;
                                case R.id.addExpBiWklyRB:
                                    expLstExpFrqRB = "26";
                                    break;
                                case R.id.addExpBiMthlyRB:
                                    expLstExpFrqRB = "24";
                                    break;
                                case R.id.addExpMthlyRB:
                                    expLstExpFrqRB = "12";
                                    break;
                                case R.id.addExpBiAnnlyRB:
                                    expLstExpFrqRB = "2";
                                    break;
                                case R.id.addExpAnnlyRB:
                                    expLstExpFrqRB = "1";
                                    break;
                            }
                        }
                    });

                    expLstExpABRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpARB:
                                    expLstExpPriorityRB = "A";
                                    expLstExpWeeklyRB = "N";
                                    expLstExpWklyRG.setVisibility(View.GONE);
                                    expLstExpWklyLabel.setVisibility(View.GONE);
                                    break;
                                case R.id.addExpBRB:
                                    expLstExpPriorityRB = "B";
                                    expLstExpWklyRG.setVisibility(View.VISIBLE);
                                    expLstExpWklyLabel.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                    });

                    expLstExpWklyRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addExpYesWklyRB:
                                    expLstExpWeeklyRB = "Y";
                                    break;
                                case R.id.addExpNoWklyRB:
                                    expLstExpWeeklyRB = "N";
                                    break;
                            }
                        }

                    });

                    expLstExpCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            expLstRefresh();
                        }
                    });

                    expLstExpUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            expNameFromEntry = expLstGen.stringFromET(expLstExpCatET);
                            expAmtFromEntry = expLstGen.dblFromET(expLstExpAmtET);
                            expFrqFromEntry = Double.valueOf(expLstExpFrqRB);

                            if(expLstDbMgr.retrieveLatestDone().equals("income")) {
                                expPriorityFromEntry = "A";
                                expWeeklyFromEntry = "N";
                            } else if(expLstDbMgr.retrieveLatestDone().equals("analysis") || expLstDbMgr.retrieveLastPageId() == 4) {
                                expPriorityFromEntry = "B";
                                expWeeklyFromEntry = "Y";
                            } else {
                                expPriorityFromEntry = expLstExpPriorityRB;
                                expWeeklyFromEntry = expLstExpWeeklyRB;
                            }

                            if (!expNameFromEntry.equals("null")) {

                                expLstExpDB.setBdgtCat(expNameFromEntry);
                                expLstExpDB.setBdgtPaytAmt(expAmtFromEntry);
                                expLstExpDB.setBdgtPaytFrq(expFrqFromEntry);
                                expLstExpDB.setBdgtPriority(expPriorityFromEntry);
                                expLstExpDB.setBdgtWeekly(expWeeklyFromEntry);
                                expLstDbMgr.updateBudget(expLstExpDB);

                                if(expLstDbMgr.getMoneyOuts().size() != 0) {
                                    expLstNewExpAnnAmt = expLstDbMgr.makeNewExpAnnAmt(expIdFromTag, expLstGen.lastNumOfDays(365));
                                } else {
                                    expLstNewExpAnnAmt = expAmtFromEntry * expFrqFromEntry;
                                }
                                expLstExpDB.setBdgtAnnPayt(expLstNewExpAnnAmt);
                                expLstDbMgr.updateBudget(expLstExpDB);
                                
                                expLstLstAdapter.updateExpenses(expLstDbMgr.getExpenses());
                                expLstLstAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                expLstRefresh();

                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            expLstHldr.expLstDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    expLstExpDB = (BudgetDb) expLstHldr.expLstDel.getTag();

                    expLstDbMgr.deleteBudget(expLstExpDB);
                    expLstLstAdapter.updateExpenses(expLstDbMgr.getExpenses());
                    expLstLstAdapter.notifyDataSetChanged();

                    expLstRefresh();
                }
            });

            return convertView;
        }
    }

    private static class ExpLstViewHolder {
        private TextView expLstExpName;
        private TextView expLstExpAmt;
        private ImageButton expLstDel;
        private ImageButton expLstEdit;
    }
}
