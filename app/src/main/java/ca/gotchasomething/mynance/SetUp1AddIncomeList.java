/*package ca.gotchasomething.mynance;

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

import java.text.NumberFormat;
import java.util.List;

import ca.gotchasomething.mynance.data.IncomeBudgetDb;

public class SetUp1AddIncomeList extends AppCompatActivity {

    Button inc2AddMoreButton, inc2UpdateButton, inc2CancelButton, inc2DoneButton, inc2SaveButton;
    ContentValues inc2CV1;
    DbHelper inc2Helper1;
    DbManager inc2DbMgr;
    Double inc2IncAmt = 0.0, incAmtFromEntry = 0.0, incAmtFromTag = 0.0, incomeAmount = 0.0, inc2AnnAmt = 0.0, incAnnAmtFromTag = 0.0,
            incAnnAmtFromEntry = 0.0, incomeAnnualAmount = 0.0,
            incFrqFromEntry = 0.0, incFrqFromTag = 0.0, incomeFrequency = 0.0, incomeFrequencyD = 0.0;
    EditText inc2IncAmtET, inc2IncCatET;
    General inc2Gen;
    IncomeBudgetDb inc2IncDB;
    Inc2LstAdapter inc2LstAdapter;
    Intent inc2Refresh, inc2ToSetUp, showList, inc2ToAddMore, inc2ToAnalysis;
    ListView inc2ListView;
    long inc2IncId, incIdFromTag;
    NumberFormat inc2CurrFor = NumberFormat.getCurrencyInstance();
    RadioButton inc2AnnlyRadioButton, inc2BiAnnlyRadioButton, inc2BiMthlyRadioButton, inc2BiWklyRadioButton, inc2MthlyRadioButton, inc2WklyRadioButton;
    RadioGroup inc2FrqRadioGroup;
    SQLiteDatabase inc2DB1;
    String inc2AnnAmt2 = null, inc2IncId2 = null, inc2IncAmt2 = null, inc2IncFrqRB = null, incNameFromEntry = null, incNameFromTag = null,
            incomeName = null, incomeNameS = null, incomeAmountS = null,
            latestDone = null;
    TextView inc2HeaderLabelTV, incomeAmountLabel, incomeCategoryLabel, incomeFrequencyLabel;
    View incomeLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        inc2DbMgr = new DbManager(this);
        inc2Gen = new General();

        inc2HeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        inc2HeaderLabelTV.setText(getString(R.string.sources_of_income));

        inc2ListView = findViewById(R.id.layout1ListView);
        inc2AddMoreButton = findViewById(R.id.layout1AddMoreBtn);
        inc2AddMoreButton.setText(getString(R.string.another_income));
        inc2DoneButton = findViewById(R.id.layout1DoneBtn);

        inc2AddMoreButton.setOnClickListener(onClickInc2AddMoreButton);
        inc2DoneButton.setOnClickListener(onClickInc2DoneButton);

        inc2LstAdapter = new Inc2LstAdapter(this, inc2DbMgr.getIncomes());
        inc2ListView.setAdapter(inc2LstAdapter);
    }

    /*public void inc2Refresh() {
        inc2Refresh = new Intent(SetUp1AddIncomeList.this, SetUp1AddIncomeList.class);
        inc2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(inc2Refresh);
    }

    public void inc2ToSetUp() {
        inc2ToSetUp = new Intent(SetUp1AddIncomeList.this, LayoutSetUp.class);
        inc2ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(inc2ToSetUp);
    }

    public void inc2ToAddMore() {
        inc2ToAddMore = new Intent(SetUp1AddIncomeList.this, SetUp1AddIncome.class);
        inc2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(inc2ToAddMore);
    }

    public void inc2ToAnalysis() {
        inc2ToAnalysis = new Intent(SetUp1AddIncomeList.this, SetUpAnalysis.class);
        inc2ToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(inc2ToAnalysis);
    }*/

    /*View.OnClickListener onClickInc2AddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inc2Gen.intentMethod(inc2ToAddMore, SetUp1AddIncomeList.this, SetUp1AddIncome.class);
            //inc2ToAddMore();
        }
    };

    View.OnClickListener onClickInc2DoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inc2DbMgr.retrieveLatestDone().equals("savings")) {
                inc2Gen.intentMethod(inc2ToAnalysis, SetUp1AddIncomeList.this, SetUpAnalysis.class);
                //inc2ToAnalysis();
            } else {
                inc2CV1 = new ContentValues();
                inc2CV1.put(DbHelper.LATESTDONE, "income");
                inc2Helper1 = new DbHelper(getApplicationContext());
                inc2DB1 = inc2Helper1.getWritableDatabase();
                inc2DB1.update(DbHelper.SET_UP_TABLE_NAME, inc2CV1, DbHelper.ID + "= '1'", null);
                inc2DB1.close();

                inc2Gen.intentMethod(inc2ToSetUp, SetUp1AddIncomeList.this, LayoutSetUp.class);
                //inc2ToSetUp();
            }
        }
    };

    public class Inc2LstAdapter extends ArrayAdapter<IncomeBudgetDb> {

        public Context context;
        public List<IncomeBudgetDb> incomes;

        public Inc2LstAdapter(
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

            final Inc2ViewHolder inc2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_2_2tv_edit_del,
                        parent, false);

                inc2Hldr = new Inc2ViewHolder();
                inc2Hldr.inc2IncName = convertView.findViewById(R.id.list2TV1);
                inc2Hldr.inc2IncAmt = convertView.findViewById(R.id.list2TV2);
                inc2Hldr.inc2Del = convertView.findViewById(R.id.list2DelBtn);
                inc2Hldr.inc2Edit = convertView.findViewById(R.id.list2EditBtn);
                convertView.setTag(inc2Hldr);

            } else {
                inc2Hldr = (Inc2ViewHolder) convertView.getTag();
            }

            inc2Hldr.inc2IncName.setText(incomes.get(position).getIncomeName());

            //retrieve incomeAnnualAmount and format as currency
            inc2AnnAmt2 = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
            inc2Gen.dblASCurrency(inc2AnnAmt2, inc2Hldr.inc2IncAmt);
            /*try {
                inc2AnnAmt2 = String.valueOf((incomes.get(position).getIncomeAmount()) * (incomes.get(position).getIncomeFrequency()));
                if (!inc2AnnAmt2.equals("")) {
                    inc2AnnAmt = Double.valueOf(inc2AnnAmt2);
                } else {
                    inc2AnnAmt = 0.0;
                }
                inc2AnnAmt3 = inc2CurrFor.format(inc2AnnAmt);
                inc2Hldr.inc2IncAmt.setText(inc2AnnAmt3);
            } catch (NumberFormatException e2) {
                inc2Hldr.inc2IncAmt.setText(inc2AnnAmt3);
            }*/

            /*inc2Hldr.inc2Del.setTag(incomes.get(position));
            inc2Hldr.inc2Edit.setTag(incomes.get(position));

            //click on pencil icon to edit a data record
            inc2Hldr.inc2Edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_1_add_income);
                    SetUp1AddIncomeList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    inc2IncCatET = findViewById(R.id.addIncCatET);
                    inc2IncAmtET = findViewById(R.id.addIncAmtET);
                    inc2FrqRadioGroup = findViewById(R.id.addIncFrqRG);
                    inc2SaveButton = findViewById(R.id.addIncSaveBtn);
                    inc2SaveButton.setVisibility(View.GONE);
                    inc2UpdateButton = findViewById(R.id.addIncUpdateBtn);
                    inc2CancelButton = findViewById(R.id.addIncCancelBtn);

                    inc2WklyRadioButton = findViewById(R.id.addIncWklyRB);
                    inc2BiWklyRadioButton = findViewById(R.id.addIncBiWklyRB);
                    inc2BiMthlyRadioButton = findViewById(R.id.addIncBiMthlyRB);
                    inc2MthlyRadioButton = findViewById(R.id.addIncMthlyRB);
                    inc2BiAnnlyRadioButton = findViewById(R.id.addIncBiAnnlyRB);
                    inc2AnnlyRadioButton = findViewById(R.id.addIncAnnlyRB);

                    inc2IncDB = (IncomeBudgetDb) inc2Hldr.inc2Edit.getTag();
                    inc2Gen.incDataFromTag(inc2IncDB);

                    //inc2IncId2 = String.valueOf(incIdFromTag);

                    inc2IncCatET.setText(incNameFromTag);

                    //inc2IncAmt = inc2IncDB.getIncomeAmount();
                    inc2Gen.dblASCurrency(String.valueOf(incAmtFromTag), inc2IncAmtET);
                    //inc2IncAmt2 = inc2CurrFor.format(inc2IncAmt);
                    //inc2IncAmtET.setText(inc2IncAmt2);

                    if (incFrqFromTag == 52) {
                        inc2WklyRadioButton.setChecked(true);
                        inc2IncFrqRB = "52";
                    } else if (incFrqFromTag == 26) {
                        inc2BiWklyRadioButton.setChecked(true);
                        inc2IncFrqRB = "26";
                    } else if (incFrqFromTag == 24) {
                        inc2BiMthlyRadioButton.setChecked(true);
                        inc2IncFrqRB = "24";
                    } else if (incFrqFromTag == 12) {
                        inc2MthlyRadioButton.setChecked(true);
                        inc2IncFrqRB = "12";
                    } else if (incFrqFromTag == 2) {
                        inc2BiAnnlyRadioButton.setChecked(true);
                        inc2IncFrqRB = "2";
                    } else if (incFrqFromTag == 1) {
                        inc2AnnlyRadioButton.setChecked(true);
                        inc2IncFrqRB = "1";
                    }

                    //update db if changed
                    inc2FrqRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            switch (checkedId) {
                                case R.id.addIncWklyRB:
                                    inc2IncFrqRB = "52";
                                    break;
                                case R.id.addIncBiWklyRB:
                                    inc2IncFrqRB = "26";
                                    break;
                                case R.id.addIncBiMthlyRB:
                                    inc2IncFrqRB = "24";
                                    break;
                                case R.id.addIncMthlyRB:
                                    inc2IncFrqRB = "12";
                                    break;
                                case R.id.addIncBiAnnlyRB:
                                    inc2IncFrqRB = "2";
                                    break;
                                case R.id.addIncAnnlyRB:
                                    inc2IncFrqRB = "1";
                                    break;
                            }
                        }
                    });

                    inc2CancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inc2Gen.intentMethod(inc2Refresh, SetUp1AddIncomeList.this, SetUp1AddIncomeList.class);
                            //inc2Refresh();
                        }
                    });

                    inc2UpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            inc2Gen.incomeDataFromEntries(inc2IncCatET, inc2IncAmtET, inc2IncFrqRB);

                            if (!incNameFromEntry.equals("null")) {

                                //if (incomeCategory.getText().toString().equals("")) {
                                //Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                                //} else {
                                //nameEntryInc = incomeCategory.getText().toString();
                                //amountEntry = general.extractingDouble(incomeAmountET);
                                //frequencyEntry = Double.valueOf(inc2IncFrqRB);
                                //annualIncome = amountEntry * frequencyEntry;

                                inc2IncDB.setIncomeName(incNameFromEntry);
                                inc2IncDB.setIncomeAmount(incAmtFromEntry);
                                inc2IncDB.setIncomeFrequency(incFrqFromEntry);
                                inc2IncDB.setIncomeAnnualAmount(incAnnAmtFromEntry);

                                inc2DbMgr.updateIncome(inc2IncDB);
                                inc2LstAdapter.updateIncomes(inc2DbMgr.getIncomes());
                                inc2LstAdapter.notifyDataSetChanged();
                                Toast.makeText(getBaseContext(), R.string.changes_saved,
                                        Toast.LENGTH_LONG).show();

                                inc2Gen.intentMethod(inc2Refresh, SetUp1AddIncomeList.this, SetUp1AddIncomeList.class);
                                //inc2Refresh();

                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            //click on trash can to delete data record
            inc2Hldr.inc2Del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    inc2IncDB = (IncomeBudgetDb) inc2Hldr.inc2Del.getTag();

                    inc2DbMgr.deleteIncome(inc2IncDB);
                    inc2LstAdapter.updateIncomes(inc2DbMgr.getIncomes());
                    inc2LstAdapter.notifyDataSetChanged();

                    inc2Gen.intentMethod(inc2Refresh, SetUp1AddIncomeList.this, SetUp1AddIncomeList.class);
                    //inc2Refresh();
                }
            });

            return convertView;
        }
    }

    private static class Inc2ViewHolder {
        private TextView inc2IncName;
        private TextView inc2IncAmt;
        private ImageButton inc2Del;
        private ImageButton inc2Edit;
    }
}*/
