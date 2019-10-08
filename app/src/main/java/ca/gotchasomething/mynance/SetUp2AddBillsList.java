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

import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class SetUp2AddBillsList extends AppCompatActivity {

    Bil2LstAdapter bil2LstAdapter;
    Button bil2AddMoreButton, bil2UpdateButton, bil2CancelButton, bil2DoneButton, bil2SaveButton;
    ContentValues bil2CV1;
    DbHelper bil2Helper1;
    DbManager bil2DBMgr;
    Double bil2Amt = 0.0, expAmtFromTag = 0.0, expAmtFromEntry = 0.0, expenseAmount = 0.0, expFrqFromTag = 0.0, expFrqFromEntry = 0.0,
            expenseFrequency = 0.0, expAnnAmtFromEntry = 0.0, expenseAnnualAmount = 0.0;
    EditText bil2AmtET, bil2CatET;
    ExpenseBudgetDb bil2ExpDB;
    General bil2Gen;
    Intent bil2Refresh, bil2ToSetUp, bil2ToAddMore, bil2ToAnalysis;
    ListView bil2ListView;
    NumberFormat bil2CurrFor = NumberFormat.getCurrencyInstance();
    RadioButton bil2AnnlyRadioButton, bil2BiAnnlyRadioButton, bil2BiMthlyRadioButton, bil2BiWklyRadioButton, bil2MthlyRadioButton, bil2WklyRadioButton;
    RadioGroup bil2FrqRadioGroup;
    SQLiteDatabase bil2DB1;
    String bil2AnnAmt2 = null, bil2Amt2 = null, bil2FrqRB = null, expNameFromTag = null, expNameFromEntry = null, expenseName = null, expensePriority = null, expenseWeekly = null;
    TextView bil2HeaderLabelTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        bil2DBMgr = new DbManager(this);
        bil2Gen = new General();

        bil2HeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        bil2HeaderLabelTV.setText(getString(R.string.bills));

        bil2ListView = findViewById(R.id.layout1ListView);
        bil2AddMoreButton = findViewById(R.id.layout1AddMoreBtn);
        bil2AddMoreButton.setText(getString(R.string.another_bill));
        bil2DoneButton = findViewById(R.id.layout1DoneBtn);

        bil2AddMoreButton.setOnClickListener(onClickBil2AddMoreButton);
        bil2DoneButton.setOnClickListener(onClickBil2DoneButton);

        bil2LstAdapter = new Bil2LstAdapter(this, bil2DBMgr.getExpense());
        bil2ListView.setAdapter(bil2LstAdapter);
    }

    /*public void bil2Refresh() {
        bil2Refresh = new Intent(SetUp2AddBillsList.this, SetUp2AddBillsList.class);
        bil2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil2Refresh);
    }

    public void bil2ToSetUp() {
        bil2ToSetUp = new Intent(SetUp2AddBillsList.this, LayoutSetUp.class);
        bil2ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil2ToSetUp);
    }

    public void bil2ToAddMore() {
        bil2ToAddMore = new Intent(SetUp2AddBillsList.this, SetUp2AddBills.class);
        bil2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil2ToAddMore);
    }

    public void bil2ToAnalysis() {
        bil2ToAnalysis = new Intent(SetUp2AddBillsList.this, SetUpAnalysis.class);
        bil2ToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(bil2ToAnalysis);
    }*/

    /*View.OnClickListener onClickBil2AddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bil2Gen.intentMethod(bil2ToAddMore, SetUp2AddBillsList.this, SetUp2AddBills.class);
            //bil2ToAddMore();
        }
    };

    View.OnClickListener onClickBil2DoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bil2DBMgr.retrieveLatestDone().equals("savings")) {
                bil2Gen.intentMethod(bil2ToAnalysis, SetUp2AddBillsList.this, SetUpAnalysis.class);
                //bil2ToAnalysis();
            } else {
                bil2CV1 = new ContentValues();
                bil2CV1.put(DbHelper.LATESTDONE, "bills");
                bil2Helper1 = new DbHelper(getApplicationContext());
                bil2DB1 = bil2Helper1.getWritableDatabase();
                bil2DB1.update(DbHelper.SET_UP_TABLE_NAME, bil2CV1, DbHelper.ID + "= '1'", null);
                bil2DB1.close();

                bil2Gen.intentMethod(bil2ToSetUp, SetUp2AddBillsList.this, LayoutSetUp.class);
                //bil2ToSetUp();
            }
        }
};

public class Bil2LstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

    public Context context;
    public List<ExpenseBudgetDb> expenses;

    public Bil2LstAdapter(
            Context context,
            List<ExpenseBudgetDb> expenses) {

        super(context, -1, expenses);

        this.context = context;
        this.expenses = expenses;
    }

    public void updateBills(List<ExpenseBudgetDb> expenses) {
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

        final bil2ViewHolder bil2Hldr;

        if (convertView == null) {
            convertView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.frag_list_2_2tv_edit_del,
                    parent, false);

            bil2Hldr = new bil2ViewHolder();
            bil2Hldr.bil2Name = convertView.findViewById(R.id.list2TV1);
            bil2Hldr.bil2Amt = convertView.findViewById(R.id.list2TV2);
            bil2Hldr.bil2Del = convertView.findViewById(R.id.list2DelBtn);
            bil2Hldr.bil2Edit = convertView.findViewById(R.id.list2EditBtn);
            convertView.setTag(bil2Hldr);

        } else {
            bil2Hldr = (bil2ViewHolder) convertView.getTag();
        }

        bil2Hldr.bil2Name.setText(expenses.get(position).getExpenseName());

        //retrieve incomeAnnualAmount and format as currency
        bil2AnnAmt2 = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
        bil2Gen.dblASCurrency(bil2AnnAmt2, bil2Hldr.bil2Amt);
            /*try {
                billsAnnualAmountS = String.valueOf((expenses.get(position).getExpenseAmount()) * (expenses.get(position).getExpenseFrequency()));
                if (!billsAnnualAmountS.equals("")) {
                    billsAnnualAmountD = Double.valueOf(billsAnnualAmountS);
                } else {
                    billsAnnualAmountD = 0.0;
                }
                billsAnnualAmount2 = bil2CurrFor.format(billsAnnualAmountD);
                bil2Hldr.billsAmount.setText(billsAnnualAmount2);
            } catch (NumberFormatException e2) {
                bil2Hldr.billsAmount.setText(billsAnnualAmount2);
            }*/

        /*bil2Hldr.bil2Del.setTag(expenses.get(position));
        bil2Hldr.bil2Edit.setTag(expenses.get(position));

        //click on pencil icon to edit a data record
        bil2Hldr.bil2Edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setContentView(R.layout.zz_form_2_add_bills);
                SetUp2AddBillsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                bil2CatET = findViewById(R.id.addBil1CatET);
                bil2AmtET = findViewById(R.id.addBil1AmtET);
                bil2FrqRadioGroup = findViewById(R.id.addBil1FrqRG);
                bil2SaveButton = findViewById(R.id.addBil1SaveBtn);
                bil2SaveButton.setVisibility(View.GONE);
                bil2UpdateButton = findViewById(R.id.addBil1UpdateBtn);
                bil2CancelButton = findViewById(R.id.addBil1CancelBtn);

                bil2WklyRadioButton = findViewById(R.id.addBil1WklyRB);
                bil2BiWklyRadioButton = findViewById(R.id.addBil1BiWklyRB);
                bil2BiMthlyRadioButton = findViewById(R.id.addBil1BiMthlyRB);
                bil2MthlyRadioButton = findViewById(R.id.addBil1MthlyRB);
                bil2BiAnnlyRadioButton = findViewById(R.id.addBil1BiAnnlyRB);
                bil2AnnlyRadioButton = findViewById(R.id.addBil1AnnlyRB);

                bil2ExpDB = (ExpenseBudgetDb) bil2Hldr.bil2Edit.getTag();
                bil2Gen.expDataFromTag(bil2ExpDB);

                bil2CatET.setText(expNameFromTag);

                //bil2Amt = bil2ExpDB.getExpenseAmount();
                bil2Gen.dblASCurrency(String.valueOf(expAmtFromTag), bil2AmtET);
                //bil2Amt2 = bil2CurrFor.format(bil2Amt);
                //bil2AmtET.setText(bil2Amt2);

                if (expFrqFromTag == 52) {
                    bil2WklyRadioButton.setChecked(true);
                    bil2FrqRB = "52";
                } else if (expFrqFromTag == 26) {
                    bil2BiWklyRadioButton.setChecked(true);
                    bil2FrqRB = "26";
                } else if (expFrqFromTag == 24) {
                    bil2BiMthlyRadioButton.setChecked(true);
                    bil2FrqRB = "24";
                } else if (expFrqFromTag == 12) {
                    bil2MthlyRadioButton.setChecked(true);
                    bil2FrqRB = "12";
                } else if (expFrqFromTag == 2) {
                    bil2BiAnnlyRadioButton.setChecked(true);
                    bil2FrqRB = "2";
                } else if (expFrqFromTag == 1) {
                    bil2AnnlyRadioButton.setChecked(true);
                    bil2FrqRB = "1";
                }

                //update db if changed
                bil2FrqRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId) {
                            case R.id.addBil1WklyRB:
                                bil2FrqRB = "52";
                                break;
                            case R.id.addBil1BiWklyRB:
                                bil2FrqRB = "26";
                                break;
                            case R.id.addBil1BiMthlyRB:
                                bil2FrqRB = "24";
                                break;
                            case R.id.addBil1MthlyRB:
                                bil2FrqRB = "12";
                                break;
                            case R.id.addBil1BiAnnlyRB:
                                bil2FrqRB = "2";
                                break;
                            case R.id.addBil1AnnlyRB:
                                bil2FrqRB = "1";
                                break;
                        }
                    }
                });

                bil2CancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bil2Gen.intentMethod(bil2Refresh, SetUp2AddBillsList.this, SetUp2AddBillsList.class);
                        //bil2Refresh();
                    }
                });

                bil2UpdateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bil2Gen.expenseDataFromEntries(bil2CatET, bil2AmtET, bil2FrqRB, "A", "N");

                        if (!expNameFromEntry.equals("null")) {

                            /*if (billsCategory.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            } else {
                                nameEntryExp = billsCategory.getText().toString();
                                amountEntry = general.extractingDouble(billsAmountET);
                                frequencyEntry = Double.valueOf(bil2FrqRB);
                                annualIncome = amountEntry * frequencyEntry;*/

                            /*bil2ExpDB.setExpenseName(expNameFromEntry);
                            bil2ExpDB.setExpenseAmount(expAmtFromEntry);
                            bil2ExpDB.setExpenseFrequency(expFrqFromEntry);
                            bil2ExpDB.setExpenseAnnualAmount(expAnnAmtFromEntry);

                            bil2DBMgr.updateExpense(bil2ExpDB);
                            bil2LstAdapter.updateBills(bil2DBMgr.getExpense());
                            bil2LstAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), R.string.changes_saved,
                                    Toast.LENGTH_LONG).show();

                            bil2Gen.intentMethod(bil2Refresh, SetUp2AddBillsList.this, SetUp2AddBillsList.class);
                            //bil2Refresh();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //click on trash can to delete data record
                bil2Hldr.bil2Del.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        bil2ExpDB = (ExpenseBudgetDb) bil2Hldr.bil2Del.getTag();

                        bil2DBMgr.deleteExpense(bil2ExpDB);
                        bil2LstAdapter.updateBills(bil2DBMgr.getExpense());
                        bil2LstAdapter.notifyDataSetChanged();

                        bil2Gen.intentMethod(bil2Refresh, SetUp2AddBillsList.this, SetUp2AddBillsList.class);
                        //bil2Refresh();
                    }

                });
            }
        });

        return convertView;
    }
}


private static class bil2ViewHolder {
    private TextView bil2Name;
    private TextView bil2Amt;
    private ImageButton bil2Del;
    private ImageButton bil2Edit;
}
}*/
