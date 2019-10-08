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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class AddIncomeList extends AppCompatActivity {

    Button incLstAddMoreBtn, incLstUpdateBtn, incLstCancelBtn, incLstDoneBtn, incLstSaveBtn;
    ContentValues incLstCV;
    DbHelper incLstHelper;
    DbManager incLstDbMgr;
    Double incAnnAmtFromTag = 0.0, incAmtFromEntry = 0.0, incAmtFromTag = 0.0, incAnnAmtFromEntry = 0.0, incFrqFromEntry = 0.0, incFrqFromTag = 0.0;
    EditText incLstIncAmtET, incLstIncCatET;
    General incLstGen;
    IncomeBudgetDb incLstIncDB;
    IncLstLstAdapter incLstLstAdapter;
    Intent incLstRefresh, incLstToBud, incLstToMonIn, incLstToAnalysis, incLstToAddMore, incLstToSetUp;
    ListView incLstListView;
    long incIdFromTag;
    RadioButton incLstAnnlyRB, incLstBiAnnlyRB, incLstBiMthlyRB, incLstBiWklyRB, incLstMthlyRB, incLstWklyRB;
    RadioGroup incLstFrqRG;
    SQLiteDatabase incLstDB;
    String incLstAnnAmt2 = null, incLstIncFrqRB = null, incNameFromEntry = null, incNameFromTag = null;
    TextView incLstHeaderLabelTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        incLstDbMgr = new DbManager(this);
        incLstGen = new General();

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
            } else {
                if (incLstDbMgr.retrieveLastPageId() == 1) {
                    incLstToMonIn = new Intent(AddIncomeList.this, LayoutMoneyIn.class);
                    incLstToMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(incLstToMonIn);
                } else if (incLstDbMgr.retrieveLastPageId() == 2) {
                    incLstToBud = new Intent(AddIncomeList.this, LayoutBudget.class);
                    incLstToBud.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(incLstToBud);
                }
            }
        }
    };

    public class IncLstLstAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public IncLstLstAdapter(
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
                if(incLstDbMgr.retrieveLastPageId() == 1) {
                    incLstHldr.incLstDel.setVisibility(View.GONE);
                    incLstHldr.incLstEdit.setVisibility(View.GONE);
                }
                convertView.setTag(incLstHldr);

            } else {
                incLstHldr = (IncLstViewHolder) convertView.getTag();
            }

            incLstHldr.incLstIncName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            incLstAnnAmt2 = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
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
                    incLstBiAnnlyRB= findViewById(R.id.addIncBiAnnlyRB);
                    incLstAnnlyRB = findViewById(R.id.addIncAnnlyRB);

                    incLstIncDB = (IncomeBudgetDb) incLstHldr.incLstEdit.getTag();

                    incNameFromTag = incLstIncDB.getIncomeName();
                    incAmtFromTag = incLstIncDB.getIncomeAmount();
                    incFrqFromTag = incLstIncDB.getIncomeFrequency();
                    incAnnAmtFromTag = incLstIncDB.getIncomeAnnualAmount();
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
                            incLstRefresh = new Intent(AddIncomeList.this, AddIncomeList.class);
                            incLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(incLstRefresh);
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

                                incLstIncDB.setIncomeName(incNameFromEntry);
                                incLstIncDB.setIncomeAmount(incAmtFromEntry);
                                incLstIncDB.setIncomeFrequency(incFrqFromEntry);
                                if(incLstDbMgr.getMoneyIns().size() == 0) {
                                    incLstIncDB.setIncomeAnnualAmount(incAnnAmtFromEntry);
                                } else {
                                    incLstIncDB.setIncomeAnnualAmount(incLstDbMgr.makeNewIncAnnAmt(incIdFromTag, incLstGen.lastNumOfDays(365)));
                                }
                                incLstDbMgr.updateIncome(incLstIncDB);

                                incLstLstAdapter.updateIncomes(incLstDbMgr.getIncomes());
                                incLstLstAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                incLstRefresh = new Intent(AddIncomeList.this, AddIncomeList.class);
                                incLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                startActivity(incLstRefresh);

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

                    incLstIncDB = (IncomeBudgetDb) incLstHldr.incLstDel.getTag();

                    incLstDbMgr.deleteIncome(incLstIncDB);
                    incLstLstAdapter.updateIncomes(incLstDbMgr.getIncomes());
                    incLstLstAdapter.notifyDataSetChanged();

                    incLstRefresh = new Intent(AddIncomeList.this, AddIncomeList.class);
                    incLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(incLstRefresh);
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
