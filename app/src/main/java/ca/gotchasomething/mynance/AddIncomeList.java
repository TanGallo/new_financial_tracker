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

public class AddIncomeList extends MainNavigation {

    BudgetDb incLstIncDB;
    Button incLstAddMoreBtn,
            incLstUpdateBtn,
            incLstCancelBtn,
            incLstDoneBtn,
            incLstResetBtn,
            incLstSaveBtn;
    ContentValues incLstCV;
    DbHelper incLstHelper;
    DbManager incLstDbMgr;
    Double incAnnAmtFromTag = 0.0,
            incAmtFromEntry = 0.0,
            incAmtFromTag = 0.0,
            incAnnAmtFromEntry = 0.0,
            incFrqFromEntry = 0.0,
            incFrqFromTag = 0.0;
    EditText incLstIncAmtET,
            incLstIncCatET;
    General incLstGen;
    IncLstLstAdapter incLstLstAdapter;
    Intent incLstRefresh,
            incLstToBud,
            incLstToMonIn,
            incLstToAnalysis,
            incLstToAddMore,
            incLstToSetUp;
    LinearLayout incLstSpinLayout;
    ListView incLstListView;
    long incIdFromTag;
    RadioButton incLstAnnlyRB,
            incLstBiAnnlyRB,
            incLstBiMthlyRB,
            incLstBiWklyRB,
            incLstMthlyRB,
            incLstWklyRB;
    RadioGroup incLstFrqRG;
    SQLiteDatabase incLstDB;
    String incLstAnnAmt2 = null,
            incLstIncFrqRB = null,
            incNameFromEntry = null,
            incNameFromTag = null;
    TextView incLstHeaderLabelTV,
            incLstTotalTV;

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

        incLstDbMgr = new DbManager(this);
        incLstGen = new General();

        incLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        incLstSpinLayout.setVisibility(View.GONE);
        incLstResetBtn = findViewById(R.id.layout1SpinResetBtn);
        incLstResetBtn.setVisibility(View.GONE);
        incLstTotalTV = findViewById(R.id.layout1TotalTV);
        incLstTotalTV.setVisibility(View.GONE);

        incLstHeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        incLstHeaderLabelTV.setText(getString(R.string.sources_of_income));

        incLstListView = findViewById(R.id.layout1ListView);
        incLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        incLstAddMoreBtn.setText(getString(R.string.another_income));
        incLstDoneBtn = findViewById(R.id.layout1DoneBtn);

        incLstAddMoreBtn.setOnClickListener(onClickIncLstAddMoreBtn);
        incLstDoneBtn.setOnClickListener(onClickIncLstDoneBtn);

        incLstLstAdapter = new IncLstLstAdapter(this, incLstDbMgr.getIncomes());
        incLstListView.setAdapter(incLstLstAdapter);
    }

    public void incLstRefresh() {
        incLstRefresh = new Intent(AddIncomeList.this, AddIncomeList.class);
        incLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(incLstRefresh);
    }

    View.OnClickListener onClickIncLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incLstToAddMore = new Intent(AddIncomeList.this, AddIncome.class);
            incLstToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(incLstToAddMore);
        }
    };

    View.OnClickListener onClickIncLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (incLstDbMgr.retrieveLatestDone().equals("savings")) {
                incLstToAnalysis = new Intent(AddIncomeList.this, SetUpAnalysis.class);
                incLstToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(incLstToAnalysis);
            } else if (incLstDbMgr.retrieveLatestDone().equals("start")) {
                incLstCV = new ContentValues();
                incLstCV.put(DbHelper.LATESTDONE, "income");
                incLstHelper = new DbHelper(getApplicationContext());
                incLstDB = incLstHelper.getWritableDatabase();
                incLstDB.update(DbHelper.SET_UP_TABLE_NAME, incLstCV, DbHelper.ID + "= '1'", null);
                incLstDB.close();

                incLstToSetUp = new Intent(AddIncomeList.this, LayoutSetUp.class);
                incLstToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(incLstToSetUp);
            } else if (incLstDbMgr.retrieveLastPageId() == 2) {
                incLstToBud = new Intent(AddIncomeList.this, LayoutBudget.class);
                incLstToBud.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(incLstToBud);
            } else if (incLstDbMgr.retrieveLastPageId() == 1) {
                incLstToMonIn = new Intent(AddIncomeList.this, LayoutMoneyIn.class);
                incLstToMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(incLstToMonIn);
            }
        }
    };

    public class IncLstLstAdapter extends ArrayAdapter<BudgetDb> {

        public Context context;
        public List<BudgetDb> incomes;

        public IncLstLstAdapter(
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

            final IncLstViewHolder incLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                incLstHldr = new IncLstViewHolder();
                incLstHldr.incLstIncName = convertView.findViewById(R.id.list2TV1);
                incLstHldr.incLstIncAmt = convertView.findViewById(R.id.list2TV2);
                incLstHldr.incLstDel = convertView.findViewById(R.id.list2DelBtn);
                incLstHldr.incLstEdit = convertView.findViewById(R.id.list2EditBtn);
                if (incLstDbMgr.retrieveLastPageId() == 1) {
                    //IF COMING FROM LAYOUTMONEYIN, NO EDIT/DELETE
                    //IF DURING SET UP, YES EDIT/DELETE
                    //IF COMING FROM LAYOUTBUDGET, YES EDIT/DELETE
                    incLstHldr.incLstDel.setVisibility(View.GONE);
                    incLstHldr.incLstEdit.setVisibility(View.GONE);
                }
                convertView.setTag(incLstHldr);

            } else {
                incLstHldr = (IncLstViewHolder) convertView.getTag();
            }

            incLstHldr.incLstIncName.setText(incomes.get(position).getBdgtCat());

            //retrieve incomeAnnualAmount and format as currency
            incLstAnnAmt2 = String.valueOf(incomes.get(position).getBdgtAnnPayt());
            incLstGen.dblASCurrency(incLstAnnAmt2, incLstHldr.incLstIncAmt);

            incLstHldr.incLstDel.setTag(incomes.get(position));
            incLstHldr.incLstEdit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            incLstHldr.incLstEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_1_add_income);
                    AddIncomeList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    incLstIncCatET = findViewById(R.id.addIncCatET);
                    incLstIncAmtET = findViewById(R.id.addIncAmtET);
                    incLstFrqRG = findViewById(R.id.addIncFrqRG);
                    incLstSaveBtn = findViewById(R.id.addIncSaveBtn);
                    incLstSaveBtn.setVisibility(View.GONE);
                    incLstUpdateBtn = findViewById(R.id.addIncUpdateBtn);
                    incLstCancelBtn = findViewById(R.id.addIncCancelBtn);

                    incLstWklyRB = findViewById(R.id.addIncWklyRB);
                    incLstBiWklyRB = findViewById(R.id.addIncBiWklyRB);
                    incLstBiMthlyRB = findViewById(R.id.addIncBiMthlyRB);
                    incLstMthlyRB = findViewById(R.id.addIncMthlyRB);
                    incLstBiAnnlyRB = findViewById(R.id.addIncBiAnnlyRB);
                    incLstAnnlyRB = findViewById(R.id.addIncAnnlyRB);

                    incLstIncDB = (BudgetDb) incLstHldr.incLstEdit.getTag();

                    incNameFromTag = incLstIncDB.getBdgtCat();
                    incAmtFromTag = incLstIncDB.getBdgtPaytAmt();
                    incFrqFromTag = incLstIncDB.getBdgtPaytFrq();
                    incAnnAmtFromTag = incLstIncDB.getBdgtAnnPayt();
                    incIdFromTag = incLstIncDB.getId();

                    incLstIncCatET.setText(incNameFromTag);

                    incLstGen.dblASCurrency(String.valueOf(incAmtFromTag), incLstIncAmtET);

                    if (incFrqFromTag == 52) {
                        incLstWklyRB.setChecked(true);
                        incLstIncFrqRB = "52";
                    } else if (incFrqFromTag == 26) {
                        incLstBiWklyRB.setChecked(true);
                        incLstIncFrqRB = "26";
                    } else if (incFrqFromTag == 24) {
                        incLstBiMthlyRB.setChecked(true);
                        incLstIncFrqRB = "24";
                    } else if (incFrqFromTag == 12) {
                        incLstMthlyRB.setChecked(true);
                        incLstIncFrqRB = "12";
                    } else if (incFrqFromTag == 2) {
                        incLstBiAnnlyRB.setChecked(true);
                        incLstIncFrqRB = "2";
                    } else if (incFrqFromTag == 1) {
                        incLstAnnlyRB.setChecked(true);
                        incLstIncFrqRB = "1";
                    }

                    //update db if changed
                    incLstFrqRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addIncWklyRB:
                                    incLstIncFrqRB = "52";
                                    break;
                                case R.id.addIncBiWklyRB:
                                    incLstIncFrqRB = "26";
                                    break;
                                case R.id.addIncBiMthlyRB:
                                    incLstIncFrqRB = "24";
                                    break;
                                case R.id.addIncMthlyRB:
                                    incLstIncFrqRB = "12";
                                    break;
                                case R.id.addIncBiAnnlyRB:
                                    incLstIncFrqRB = "2";
                                    break;
                                case R.id.addIncAnnlyRB:
                                    incLstIncFrqRB = "1";
                                    break;
                            }
                        }
                    });

                    incLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            incLstRefresh();
                        }
                    });

                    incLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            incNameFromEntry = incLstGen.stringFromET(incLstIncCatET);
                            incAmtFromEntry = incLstGen.dblFromET(incLstIncAmtET);
                            try {
                                incFrqFromEntry = Double.valueOf(incLstIncFrqRB);
                            } catch (NullPointerException e) {
                                incFrqFromEntry = 1.0;
                            }
                            incAnnAmtFromEntry = incAmtFromEntry * incFrqFromEntry;

                            if (!incNameFromEntry.equals("null")) {

                                incLstIncDB.setBdgtCat(incNameFromEntry);
                                incLstIncDB.setBdgtPaytAmt(incAmtFromEntry);
                                incLstIncDB.setBdgtPaytFrq(incFrqFromEntry);
                                incLstDbMgr.updateBudget(incLstIncDB);

                                if (incLstDbMgr.getMoneyIns().size() == 0) {
                                    incLstIncDB.setBdgtAnnPayt(incAnnAmtFromEntry);
                                } else {
                                    incLstIncDB.setBdgtAnnPayt(incLstDbMgr.makeNewIncAnnAmt(incIdFromTag, incLstGen.lastNumOfDays(365)));
                                }
                                incLstDbMgr.updateBudget(incLstIncDB);

                                incLstLstAdapter.updateIncomes(incLstDbMgr.getIncomes());
                                incLstLstAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                incLstRefresh();

                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            incLstHldr.incLstDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    incLstIncDB = (BudgetDb) incLstHldr.incLstDel.getTag();

                    incLstDbMgr.deleteBudget(incLstIncDB);
                    incLstLstAdapter.updateIncomes(incLstDbMgr.getIncomes());
                    incLstLstAdapter.notifyDataSetChanged();

                    incLstRefresh();
                }
            });

            return convertView;
        }
    }

    private static class IncLstViewHolder {
        private TextView incLstIncName;
        private TextView incLstIncAmt;
        private ImageButton incLstDel;
        private ImageButton incLstEdit;
    }
}
