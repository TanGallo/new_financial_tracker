/*package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import ca.gotchasomething.mynance.data.DebtDb;

public class SetUp3AddDebtsList extends AppCompatActivity {

    Button debt2AddMoreButton, debt2UpdateButton, debt2CancelButton, debt2DoneButton, debt2SaveButton;
    ContentValues debt2CV1, debt2CV2, debt2CV3, debt2CV4, debt2CV5, debt2CV6, debt2CV7;
    DbHelper debt2Helper1, debt2Helper2, debt2Helper3;
    DbManager debt2DBMgr;
    DebtDb debt2DebtDb;
    Double debtAmtFromEntry = 0.0, debtAmtFromTag = 0.0, debtRateFromEntry = 0.0, debtRateFromTag = 0.0, debtPaytFromEntry = 0.0,
            debtPaytFromTag = 0.0,
            debtFrqFromEntry = 0.0, debtFrqFromTag = 0.0,
            debtAnnIncFromEntry = 0.0, debtAnnIncFromTag = 0.0, debtAmount = 0.0, debtRate = 0.0, debtPayments = 0.0, debtFrequency = 0.0,
            debtAnnualIncome = 0.0, debt2DebtAmt = 0.0, debtLimit = 0.0, debtLimitFromEntry = 0.0, debtLimitFromTag = 0.0,
            debt2AnnIncB = 0.0, debtsAnnualAmountD = 0.0, debt2DebtFrq = 0.0, debt2DebtLimit = 0.0, debt2DebtPayts = 0.0, debt2DebtRate = 0.0,
            debt2DebtRate2 = 0.0, expenseAnnualAmount = 0.0, expAnnAmtCalc = 0.0;
    EditText debt2LimitET, debt2AmtET, debt2PercentET, debt2PaytsET, debt2DebtAmtET, debt2DebtNameET;
    Debt2ListAdapter debt2LstAdapter;
    General debt2Gen;
    Intent debt2Refresh, debt2ToSetUp, debt2ToAddMore;
    ListView debt2ListView;
    NumberFormat debt2CurrFor = NumberFormat.getCurrencyInstance();
    NumberFormat debt2PercentFor = NumberFormat.getPercentInstance();
    long debt2IncRefKeyD;
    RadioButton debt2AnnlyRadioButton, debt2BiAnnlyRadioButton, debt2BiMthlyRadioButton, debt2BiWklyRadioButton, debt2MthlyRadioButton,
            debt2WklyRadioButton;
    RadioGroup debt2FrqRadioGroup;
    SQLiteDatabase debt2DB1, debt2DB2, debt2DB3;
    String debtEndFromTag = null, debtName = null, debtNameFromEntry = null, debtNameFromTag = null, debt2ExpRefKeyD = null, debt2DebtName = null, debt2DebtFrqRB = null, debt2DebtAnnAmt2 = null, debt2DebtAmt3 = null,
            debtsAnnualAmount2 = null, debt2DebtAmt2 = null, debt2DebtEnd = null, debt2DebtLimit2 = null, debt2DebtPayts2 = null, debt2DebtRate3 = null,
            expensePriority = null;
    TextView debt2DateRes, debt2DateResLabel, debt2HeaderLabelTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        debt2DBMgr = new DbManager(this);
        debt2Gen = new General();

        debt2HeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        debt2HeaderLabelTV.setText(getString(R.string.credit));

        debt2ListView = findViewById(R.id.layout1ListView);
        debt2AddMoreButton = findViewById(R.id.layout1AddMoreBtn);
        debt2AddMoreButton.setText(getString(R.string.another_credit));
        debt2DoneButton = findViewById(R.id.layout1DoneBtn);

        debt2AddMoreButton.setOnClickListener(onClickDebt2AddMoreButton);
        debt2DoneButton.setOnClickListener(onClickDebt2DoneButton);

        debt2LstAdapter = new Debt2ListAdapter(this, debt2DBMgr.getDebts());
        debt2ListView.setAdapter(debt2LstAdapter);
    }

    /*public void debt2Refresh() {
        debt2Refresh = new Intent(SetUp3AddDebtsList.this, SetUp3AddDebtsList.class);
        debt2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(debt2Refresh);
    }

    public void debt2ToSetUp() {
        debt2ToSetUp = new Intent(SetUp3AddDebtsList.this, LayoutSetUp.class);
        debt2ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(debt2ToSetUp);
    }

    public void debt2ToAddMore() {
        debt2ToAddMore = new Intent(SetUp3AddDebtsList.this, SetUp3AddDebts.class);
        debt2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(debt2ToAddMore);
    }*/

    /*View.OnClickListener onClickDebt2AddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debt2Gen.intentMethod(debt2ToAddMore, SetUp3AddDebtsList.this, SetUp3AddDebts.class);
            //debt2ToAddMore();
        }
    };

    View.OnClickListener onClickDebt2DoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debt2CV1 = new ContentValues();
            debt2CV1.put(DbHelper.LATESTDONE, "debts");
            debt2Helper1 = new DbHelper(getApplicationContext());
            debt2DB1 = debt2Helper1.getWritableDatabase();
            debt2DB1.update(DbHelper.SET_UP_TABLE_NAME, debt2CV1, DbHelper.ID + "= '1'", null);
            debt2DB1.close();

            debt2Gen.intentMethod(debt2ToSetUp, SetUp3AddDebtsList.this, LayoutSetUp.class);
            //debt2ToSetUp();
        }
    };

    TextWatcher onChangeDebt2AmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt2DebtEndRes();
            debt2DateRes.setText(debt2DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt2PercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt2DebtEndRes();
            debt2DateRes.setText(debt2DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebt2PaytsET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debt2DebtEndRes();
            debt2DateRes.setText(debt2DebtEnd);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void debt2DebtEndRes() {
        debt2Gen.debtDataFromEntries(
                debt2DebtNameET,
                debt2LimitET,
                debt2AmtET,
                debt2PercentET,
                debt2PaytsET,
                0.0);
        debt2DebtEnd = debt2Gen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debt2DebtEnd.equals(getString(R.string.debt_paid))) {
            debt2DateRes.setTextColor(Color.parseColor("#03ac13"));
        } else if (debt2DebtEnd.equals(getString(R.string.too_far))) {
            debt2DateRes.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            debt2DateRes.setTextColor(Color.parseColor("#303F9F"));
            debt2DateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
        debt2Gen.whatToShowDebt(getString(R.string.debt_paid), getString(R.string.too_far), debt2DateResLabel, debt2DateRes);
    }

    /*public String debt2PriorityData() {
        for (ExpenseBudgetDb e : debt2DBMgr.getExpense()) {
            if (String.valueOf(debt2ExpRefKeyD).equals(String.valueOf(e.getId()))) {
                expensePriority = e.getExpensePriority();
            }
        }
        return expensePriority;
    }*/

    /*public class Debt2ListAdapter extends ArrayAdapter<DebtDb> {

        public Context context;
        public List<DebtDb> debts;

        public Debt2ListAdapter(
                Context context,
                List<DebtDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<DebtDb> debts) {
            this.debts = debts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return debts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Debt2ViewHolder debt2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                debt2Hldr = new Debt2ViewHolder();
                debt2Hldr.debt2NameTV = convertView.findViewById(R.id.bigLstTV1);
                debt2Hldr.debt2AmtTV = convertView.findViewById(R.id.bigLstTV2);
                debt2Hldr.debt2FreeDateLabel = convertView.findViewById(R.id.bigLstLabel);
                debt2Hldr.debt2FreeDateLabel.setText(getString(R.string.debt_will));
                debt2Hldr.debt2FreeDateTV = convertView.findViewById(R.id.bigLstTV3);
                debt2Hldr.debt2Del = convertView.findViewById(R.id.bigLstDelBtn);
                debt2Hldr.debt2Edit = convertView.findViewById(R.id.bigLstEditBtn);
                debt2Hldr.debt2Label2 = convertView.findViewById(R.id.bigLstLabel2);
                debt2Hldr.debt2Label2.setVisibility(View.GONE);
                debt2Hldr.debt2OverLimit = convertView.findViewById(R.id.bigLstTV4);
                debt2Hldr.debt2OverLimit.setText(getString(R.string.over_limit));
                debt2Hldr.debt2OverLimit.setTextColor(Color.parseColor("#ffff4444"));
                convertView.setTag(debt2Hldr);

            } else {
                debt2Hldr = (Debt2ViewHolder) convertView.getTag();
            }

            //retrieve debtName
            debt2Hldr.debt2NameTV.setText(debts.get(position).getDebtName());

            //retrieve debtAmount and format as currency
            debt2DebtAmt2 = (String.valueOf(debts.get(position).getDebtOwing()));
            debt2Gen.dblASCurrency(debt2DebtAmt2, debt2Hldr.debt2AmtTV);
            /*try {
                debt2DebtAmt2 = (String.valueOf(debts.get(position).getDebtAmount()));
                if (debt2DebtAmt2 != null && !debt2DebtAmt2.equals("")) {
                    debt2DebtAmt = Double.valueOf(debt2DebtAmt2);
                } else {
                    debt2DebtAmt = 0.0;
                }
                debt2DebtAmt2 = debt2CurrFor.format(debt2DebtAmt);
                debt2Hldr.debt2AmtTV.setText(debt2DebtAmt2);
            } catch (NumberFormatException e5) {
                debt2Hldr.debt2AmtTV.setText(debt2DebtAmt2);
            }*/

            //retrieve debtEnd
            /*debt2DebtEnd = debts.get(position).getDebtEnd();
            if (debt2DebtEnd.contains("2")) {
                debt2Hldr.debt2FreeDateLabel.setVisibility(View.VISIBLE);
            } else {
                debt2Hldr.debt2FreeDateLabel.setVisibility(View.GONE);
            }
            debt2Hldr.debt2FreeDateTV.setText(debt2DebtEnd);
            if (debt2DebtEnd.equals(getString(R.string.debt_paid))) {
                debt2Hldr.debt2FreeDateTV.setTextColor(Color.parseColor("#03ac13"));
            } else if (debt2DebtEnd.equals(getString(R.string.too_far))) {
                debt2Hldr.debt2FreeDateTV.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                debt2Hldr.debt2FreeDateTV.setTextColor(Color.parseColor("#303F9F"));
                debt2Hldr.debt2FreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            /*debt2IncRefKeyD = debts.get(position).getIncRefKeyD();

            for (IncomeBudgetDb i : debt2DBMgr.getIncomes()) {
                if (String.valueOf(i.getId()).equals(String.valueOf(debt2IncRefKeyD))) {
                    debt2AnnIncB = i.getIncomeAnnualAmount();
                }
            }*/

            /*if (debts.get(position).getDebtLimit() == 0) {
                debt2Hldr.debt2OverLimit.setVisibility(View.GONE);
            } else if (debts.get(position).getDebtOwing() > debts.get(position).getDebtLimit()) {
                debt2Hldr.debt2OverLimit.setVisibility(View.VISIBLE);
            } else {
                debt2Hldr.debt2OverLimit.setVisibility(View.GONE);
            }

            debt2Hldr.debt2Del.setTag(debts.get(position));
            debt2Hldr.debt2Edit.setTag(debts.get(position));

            //click on pencil icon to edit a data record
            debt2Hldr.debt2Edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_3_add_debt);
                    SetUp3AddDebtsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    debt2DBMgr = new DbManager(getApplicationContext());

                    debt2DebtNameET = findViewById(R.id.addDebtNameET);
                    debt2LimitET = findViewById(R.id.addDebtLimitET);
                    debt2AmtET = findViewById(R.id.addDebtAmtET);
                    debt2PercentET = findViewById(R.id.addDebtRateET);
                    debt2PaytsET = findViewById(R.id.addDebtPaytET);
                    debt2DateRes = findViewById(R.id.addDebtDateResTV);
                    debt2DateResLabel = findViewById(R.id.addDebtDateResLabel);

                    /*debt2FrqRadioGroup = findViewById(R.id.addDebtFrqRG);

                    debt2WklyRadioButton = findViewById(R.id.addDebtWklyRB);
                    debt2BiWklyRadioButton = findViewById(R.id.addDebtBiWklyRB);
                    debt2MthlyRadioButton = findViewById(R.id.addDebtMthlyRB);*/

                    /*debt2SaveButton = findViewById(R.id.addDebtSaveBtn);
                    debt2SaveButton.setVisibility(View.GONE);
                    debt2UpdateButton = findViewById(R.id.addDebtUpdateBtn);
                    debt2CancelButton = findViewById(R.id.addDebtCancelBtn);

                    debt2DebtDb = (DebtDb) debt2Hldr.debt2Edit.getTag();
                    debt2Gen.debtDataFromTag(debt2DebtDb);

                    //debt2DebtName = debt2DebtDb.getDebtName();
                    debt2DebtNameET.setText(debtNameFromTag);

                    //debt2DebtLimit = debt2DebtDb.getDebtLimit();
                    debt2Gen.dblASCurrency(String.valueOf(debtLimitFromTag), debt2LimitET);
                    //debt2DebtLimit2 = debt2CurrFor.format(debt2DebtLimit);
                    //debt2LimitET.setText(debt2DebtLimit2);

                    //debt2DebtAmt = debt2DebtDb.getDebtAmount();
                    debt2Gen.dblASCurrency(String.valueOf(debtAmtFromTag), debt2AmtET);
                    //debt2DebtAmt3 = debt2CurrFor.format(debt2DebtAmt);
                    //debt2AmtET.setText(debt2DebtAmt3);
                    debt2AmtET.addTextChangedListener(onChangeDebt2AmtET);

                    //debt2DebtRate = debt2DebtDb.getDebtRate();
                    debt2DebtRate2 = debtRateFromTag / 100;
                    debt2PercentFor.setMinimumFractionDigits(2);
                    debt2DebtRate3 = debt2PercentFor.format(debt2DebtRate2);
                    debt2PercentET.setText(debt2DebtRate3);
                    debt2PercentET.addTextChangedListener(onChangeDebt2PercentET);

                    //debt2DebtPayts = debt2DebtDb.getDebtPayments();
                    debt2Gen.dblASCurrency(String.valueOf(debtPaytFromTag), debt2PaytsET);
                    //debt2DebtPayts2 = debt2CurrFor.format(debt2DebtPayts);
                    //debt2PaytsET.setText(debt2DebtPayts2);
                    debt2PaytsET.addTextChangedListener(onChangeDebt2PaytsET);

                    //debt2DebtFrq = debt2DebtDb.getDebtFrequency();

                    //debt2DebtAnnInc = debt2DebtDb.getDebtAnnualIncome();

                    //debt2DebtEnd = debt2DebtDb.getDebtEnd();
                    debt2DateRes.setText(debtEndFromTag);
                    if (debtEndFromTag.equals(getString(R.string.debt_paid))) {
                        debt2DateResLabel.setVisibility(View.GONE);
                        debt2DateRes.setTextColor(Color.parseColor("#03ac13"));
                    } else if (debtEndFromTag.equals(getString(R.string.too_far))) {
                        debt2DateResLabel.setVisibility(View.GONE);
                        debt2DateRes.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        debt2DateResLabel.setVisibility(View.VISIBLE);
                        debt2DateRes.setTextColor(Color.parseColor("#303F9F"));
                        debt2DateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    //set radio button selections from data
                    /*if (debtFrqFromTag == 52) {
                        debt2WklyRadioButton.setChecked(true);
                        debt2DebtFrqRB = "52";
                    } else if (debtFrqFromTag == 26) {
                        debt2BiWklyRadioButton.setChecked(true);
                        debt2DebtFrqRB = "26";
                    } else if (debtFrqFromTag == 12) {
                        debt2MthlyRadioButton.setChecked(true);
                        debt2DebtFrqRB = "12";
                    }*/

                    //update db if radio buttons changed
                    /*debt2FrqRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.addDebtWklyRB:
                                    debt2DebtFrqRB = "52";
                                    debt2DebtEndRes();
                                    debt2DateRes.setText(debt2DebtEnd);
                                    break;
                                case R.id.addDebtBiWklyRB:
                                    debt2DebtFrqRB = "26";
                                    debt2DebtEndRes();
                                    debt2DateRes.setText(debt2DebtEnd);
                                    break;
                                case R.id.addDebtMthlyRB:
                                    debt2DebtFrqRB = "12";
                                    debt2DebtEndRes();
                                    debt2DateRes.setText(debt2DebtEnd);
                                    break;
                            }
                        }
                    });*/

                    /*debt2UpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //debt2ExpRefKeyD = String.valueOf(debt2DebtDb.getExpRefKeyD());

                            debt2DebtEndRes();

                            if (!debtNameFromEntry.equals("null")) {

                                //if (debtName != null) {

                                debt2DebtDb.setDebtName(debtNameFromEntry);
                                debt2DebtDb.setDebtLimit(debtLimitFromEntry);
                                debt2DebtDb.setDebtOwing(debtAmtFromEntry);
                                debt2DebtDb.setDebtRate(debtRateFromEntry);
                                debt2DebtDb.setDebtPayments(debtPaytFromEntry);
                                //debt2DebtDb.setDebtFrequency(12.0);
                                debt2DebtDb.setDebtAnnualPayments(debtPaytFromEntry * 12.0);
                                debt2DebtDb.setDebtEnd(debt2DebtEnd);

                                debt2Helper2 = new DbHelper(getContext());
                                debt2DB2 = debt2Helper2.getWritableDatabase();

                                //String[] args = new String[]{debt2ExpRefKeyD};
                                String[] args2 = new String[]{String.valueOf(debt2DebtDb.getId())};
                                //String[] args3 = new String[]{String.valueOf(debt2DebtDb.getIncRefKeyD())};

                                //debt2CV2 = new ContentValues();
                                //debt2CV3 = new ContentValues();
                                //debt2CV4 = new ContentValues();
                                debt2CV5 = new ContentValues();
                                //debt2CV6 = new ContentValues();
                                debt2CV7 = new ContentValues();

                                /*debt2CV2.put(DbHelper.EXPENSENAME, debtNameFromEntry);
                                debt2CV2.put(DbHelper.EXPENSEAMOUNT, debtPaytFromEntry);
                                debt2CV2.put(DbHelper.EXPENSEFREQUENCY, debtFrqFromEntry);
                                expAnnAmtCalc = debtPaytFromEntry * debtFrqFromEntry;
                                debt2CV2.put(DbHelper.EXPENSEANNUALAMOUNT, expAnnAmtCalc);
                                if (debt2PriorityData().equals("A")) {
                                    debt2CV2.put(DbHelper.EXPENSEAANNUALAMOUNT, expAnnAmtCalc);
                                } else if (debt2PriorityData().equals("B")) {
                                    debt2CV2.put(DbHelper.EXPENSEBANNUALAMOUNT, expAnnAmtCalc);
                                }*/
                                //debt2CV3.put(DbHelper.INCOMENAME, debtNameFromEntry);
                                //debt2CV4.put(DbHelper.MONEYOUTCAT, debtNameFromEntry);
                                /*debt2CV5.put(DbHelper.MONEYOUTDEBTCAT, debtNameFromEntry);
                                //debt2CV6.put(DbHelper.MONEYINCAT, debtNameFromEntry);
                                debt2CV7.put(DbHelper.ACCTNAME, debtNameFromEntry);

                                try {
                                    //debt2DB2.update(DbHelper.EXPENSES_TABLE_NAME, debt2CV2, DbHelper.ID + "=?", args);
                                    //debt2DB2.update(DbHelper.INCOME_TABLE_NAME, debt2CV3, DbHelper.ID + "=?", args3);
                                    //debt2DB2.update(DbHelper.MONEY_OUT_TABLE_NAME, debt2CV4, DbHelper.EXPREFKEYMO + "=?", args);
                                    debt2DB2.update(DbHelper.MONEY_OUT_TABLE_NAME, debt2CV5, DbHelper.MONEYOUTCHARGINGDEBTID + "=?", args2);
                                    //debt2DB2.update(DbHelper.MONEY_IN_TABLE_NAME, debt2CV6, DbHelper.INCREFKEYMI + "=?", args3);
                                    debt2DB2.update(DbHelper.ACCOUNTS_TABLE_NAME, debt2CV7, DbHelper.DEBTID + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                debt2DB2.close();

                                debt2DBMgr.updateDebt(debt2DebtDb);
                                debt2LstAdapter.updateDebts(debt2DBMgr.getDebts());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                debt2Gen.intentMethod(debt2Refresh, SetUp3AddDebtsList.this, SetUp3AddDebtsList.class);
                                //debt2Refresh();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    debt2CancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            debt2Gen.intentMethod(debt2Refresh, SetUp3AddDebtsList.this, SetUp3AddDebtsList.class);
                            //debt2Refresh();
                        }
                    });
                }
            });

            //click on trash can icon
            debt2Hldr.debt2Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    debt2DebtDb = (DebtDb) debt2Hldr.debt2Del.getTag();

                    debt2Helper3 = new DbHelper(getContext());
                    debt2DB3 = debt2Helper3.getWritableDatabase();

                    /*try {
                        String[] args = new String[]{String.valueOf(debt2DebtDb.getExpRefKeyD())};
                        debt2DB3.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    try {
                        String[] args2 = new String[]{String.valueOf(debt2DebtDb.getIncRefKeyD())};
                        debt2DB3.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }*/

                    /*try {
                        String[] args3 = new String[]{String.valueOf(debt2DebtDb.getId())};
                        debt2DB3.delete(DbHelper.ACCOUNTS_TABLE_NAME, DbHelper.DEBTID + "=?", args3);
                    } catch (CursorIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    debt2DB3.close();

                    debt2DBMgr.deleteDebt(debt2DebtDb);
                    debt2LstAdapter.updateDebts(debt2DBMgr.getDebts());
                    notifyDataSetChanged();

                    debt2Gen.intentMethod(debt2Refresh, SetUp3AddDebtsList.this, SetUp3AddDebtsList.class);
                    //debt2Refresh();
                }
            });

            return convertView;
        }
    }

    private static class Debt2ViewHolder {
        private TextView debt2NameTV;
        private TextView debt2AmtTV;
        private TextView debt2FreeDateLabel;
        private TextView debt2FreeDateTV;
        private ImageButton debt2Del;
        private ImageButton debt2Edit;
        private TextView debt2Label2;
        private TextView debt2OverLimit;
    }
}*/
