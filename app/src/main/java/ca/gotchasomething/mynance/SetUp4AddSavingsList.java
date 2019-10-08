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

import ca.gotchasomething.mynance.data.SavingsDb;

public class SetUp4AddSavingsList extends AppCompatActivity {

    Button sav2AddMoreButton, sav2UpdateButton, sav2CancelButton, sav2DoneButton, sav2SaveButton;
    ContentValues sav2CV1, sav2CV2, sav2CV3, sav2CV4, sav2CV5, sav2CV6;
    DbHelper sav2Helper1, sav2Helper2, sav2Helper3;
    DbManager sav2DBMgr;
    Double expAnnAmtCalc = 0.0, savAmtFromEntry = 0.0, savAmtFromTag = 0.0, savAnnIncFromEntry = 0.0, savAnnIncFromTag = 0.0,
            savGoalFromEntry = 0.0, savGoalFromTag = 0.0, savPaytFromEntry = 0.0, savPaytFromTag = 0.0,
            savRateFromEntry = 0.0, savRateFromTag = 0.0, sav2SavPercent2 = 0.0,
            savFrqFromEntry = 0.0, savFrqFromTag = 0.0, savingsGoal = 0.0, savingsAmount = 0.0, savingsRate = 0.0, savingsPayments = 0.0,
            savingsFrequency = 0.0,
            savingsAnnualIncome = 0.0;
    EditText sav2SavNameET, sav2SavAmtET, sav2SavGoalET, sav2SavPaytET, sav2SavPercentET, savingsAmountET, savingsCategory;
    General sav2Gen;
    Sav2LstAdapter sav2LstAdapter;
    Intent sav2Refresh, sav2ToSetUp, sav2ToAddMore;
    ListView sav2ListView;
    long id, sav2IncRefKeyS;
    NumberFormat sav2PercentFor = NumberFormat.getPercentInstance();
    RadioButton sav2AnnlyRadioButton, sav2BiAnnlyRadioButton, sav2BiMthlyRadioButton, sav2BiWklyRadioButton, sav2MthlyRadioButton, sav2WklyRadioButton,
            sav2SepYesRadioButton, sav2SepNoRadioButton;
    RadioGroup sav2FrqRadioGroup, sav2SepRadioGroup;
    SavingsDb sav2SavDB;
    SQLiteDatabase sav2DB1, sav2DB2, sav2DB3;
    String savDateFromTag = null, sav2FrqRB = null, expensePriority = null, savNameFromEntry = null, savNameFromTag = null, sav2SavPercent3 = null,
            savSepFromEntry = null, savSepFromTag = null, sav2SavGoal2 = null,
            sav2SavDate2 = null, sav2SavCurr2 = null, sav2SavDate = null, sav2SepRB = null, sav2ExpRefKeyS = null, savingsName = null,
            savingsSeparate = null;
    TextView sav2SavDateResTV, sav2SavDateResLabel, sav2SavFrqLabel, sav2HeaderLabelTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        sav2DBMgr = new DbManager(this);
        sav2Gen = new General();

        sav2HeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        sav2HeaderLabelTV.setText(getString(R.string.savings));

        sav2ListView = findViewById(R.id.layout1ListView);
        sav2AddMoreButton = findViewById(R.id.layout1AddMoreBtn);
        sav2AddMoreButton.setText(getString(R.string.another_savings));
        sav2DoneButton = findViewById(R.id.layout1DoneBtn);

        sav2AddMoreButton.setOnClickListener(onClickSav2AddMoreButton);
        sav2DoneButton.setOnClickListener(onClickSav2DoneButton);

        sav2LstAdapter = new Sav2LstAdapter(this, sav2DBMgr.getSavings());
        sav2ListView.setAdapter(sav2LstAdapter);
    }

    /*public void sav2Refresh() {
        sav2Refresh = new Intent(SetUp4AddSavingsList.this, SetUp4AddSavingsList.class);
        sav2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(sav2Refresh);
    }*/

    /*public void sav2ToSetUp() {
        sav2ToSetUp = new Intent(SetUp4AddSavingsList.this, LayoutSetUp.class);
        sav2ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(sav2ToSetUp);
    }*/

    /*public void sav2ToAddMore() {
        sav2ToAddMore = new Intent(SetUp4AddSavingsList.this, SetUp4AddSavings.class);
        sav2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(sav2ToAddMore);
    }*/

    /*View.OnClickListener onClickSav2AddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sav2Gen.intentMethod(sav2ToAddMore, SetUp4AddSavingsList.this, SetUp4AddSavings.class);
            //sav2ToAddMore();
        }
    };

    View.OnClickListener onClickSav2DoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sav2CV1 = new ContentValues();
            sav2CV1.put(DbHelper.LATESTDONE, "savings");
            sav2Helper1 = new DbHelper(getApplicationContext());
            sav2DB1 = sav2Helper1.getWritableDatabase();
            sav2DB1.update(DbHelper.SET_UP_TABLE_NAME, sav2CV1, DbHelper.ID + "= '1'", null);
            sav2DB1.close();

            sav2Gen.intentMethod(sav2ToSetUp, SetUp4AddSavingsList.this, LayoutSetUp.class);
            //sav2ToSetUp();
        }
    };

    TextWatcher onChangeSav2SavAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav2SavDateRes();
            sav2SavDateResTV.setText(sav2SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav2SavGoalET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav2SavDateRes();
            sav2SavDateResTV.setText(sav2SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav2SavPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav2SavDateRes();
            sav2SavDateResTV.setText(sav2SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSav2SavPercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sav2SavDateRes();
            sav2SavDateResTV.setText(sav2SavDate);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void sav2SavDateRes() {
        sav2Gen.savingsDataFromEntries(
                sav2SavNameET,
                //sav2SepRB,
                sav2SavAmtET,
                sav2SavGoalET,
                sav2SavPercentET,
                sav2SavPaytET);
        sav2SavDate = sav2Gen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (sav2SavDate.equals(getString(R.string.goal_achieved))) {
            sav2SavDateResLabel.setVisibility(View.GONE);
            sav2SavDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (sav2SavDate.equals(getString(R.string.too_far))) {
            sav2SavDateResLabel.setVisibility(View.GONE);
            sav2SavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            sav2SavDateResLabel.setVisibility(View.VISIBLE);
            sav2SavDateResTV.setTextColor(Color.parseColor("#303F9F"));
            sav2SavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
    }

    /*public String sav2PriorityData() {
        for (ExpenseBudgetDb e : sav2DBMgr.getExpense()) {
            if (String.valueOf(sav2ExpRefKeyS).equals(String.valueOf(e.getId()))) {
                expensePriority = e.getExpensePriority();
            }
        }
        return expensePriority;
    }*/

    /*public class Sav2LstAdapter extends ArrayAdapter<SavingsDb> {

        public Context context;
        public List<SavingsDb> savings;

        public Sav2LstAdapter(
                Context context,
                List<SavingsDb> savings) {

            super(context, -1, savings);

            this.context = context;
            this.savings = savings;
        }

        public void updateSavings(List<SavingsDb> savings) {
            this.savings = savings;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return savings.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Sav2ViewHolder sav2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                sav2Hldr = new Sav2ViewHolder();
                sav2Hldr.sav2NameTV = convertView.findViewById(R.id.bigLstTV1);
                sav2Hldr.sav2GoalAmtTV = convertView.findViewById(R.id.bigLstTV2);
                sav2Hldr.sav2DateLabelTV = convertView.findViewById(R.id.bigLstLabel);
                sav2Hldr.sav2DateLabelTV.setText(getString(R.string.goal_will));
                sav2Hldr.sav2DateTV = convertView.findViewById(R.id.bigLstTV3);
                sav2Hldr.sav2CurrLabelTV = convertView.findViewById(R.id.bigLstLabel2);
                sav2Hldr.sav2CurrLabelTV.setText(getString(R.string.current_balance));
                sav2Hldr.sav2CurrAmtTV = convertView.findViewById(R.id.bigLstTV4);
                sav2Hldr.sav2Del = convertView.findViewById(R.id.bigLstDelBtn);
                sav2Hldr.sav2Edit = convertView.findViewById(R.id.bigLstEditBtn);
                convertView.setTag(sav2Hldr);

            } else {
                sav2Hldr = (Sav2ViewHolder) convertView.getTag();
            }

            //retrieve savingsName
            sav2Hldr.sav2NameTV.setText(savings.get(position).getSavingsName());

            //retrieve savingsGoal and format as currency
            sav2SavGoal2 = (String.valueOf(savings.get(position).getSavingsGoal()));
            sav2Gen.dblASCurrency(sav2SavGoal2, sav2Hldr.sav2GoalAmtTV);
            /*try {
                savingsGoalS = (String.valueOf(savings.get(position).getSavingsGoal()));
                if (savingsGoalS != null && !savingsGoalS.equals("")) {
                    savingsGoalD = Double.valueOf(savingsGoalS);
                } else {
                    savingsGoalD = 0.0;
                }
                savingsGoal2 = currencyFormat.format(savingsGoalD);
                sav2Hldr.savingsGoalAmount.setText(savingsGoal2);
            } catch (NumberFormatException e6) {
                sav2Hldr.savingsGoalAmount.setText(savingsGoal2);
            }*/

            //retrieve savingsDate
            /*sav2SavDate2 = savings.get(position).getSavingsDate();
            sav2Hldr.sav2DateTV.setText(sav2SavDate2);
            if (sav2SavDate2.contains("2")) {
                sav2Hldr.sav2DateLabelTV.setVisibility(View.VISIBLE);
            } else {
                sav2Hldr.sav2DateLabelTV.setVisibility(View.GONE);
            }
            if (sav2SavDate2.equals(getString(R.string.goal_achieved))) {
                sav2Hldr.sav2DateTV.setTextColor(Color.parseColor("#03ac13"));
            } else if (sav2SavDate2.equals(getString(R.string.too_far))) {
                sav2Hldr.sav2DateTV.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                sav2Hldr.sav2DateTV.setTextColor(Color.parseColor("#303F9F"));
                sav2Hldr.sav2DateLabelTV.setTextColor(Color.parseColor("#303F9F"));
            }

            //retrieve savingsAmount & format as currency
            sav2SavCurr2 = (String.valueOf(savings.get(position).getSavingsAmount()));
            sav2Gen.dblASCurrency(sav2SavCurr2, sav2Hldr.sav2CurrAmtTV);
            /*try {
                savingsCurrentS = (String.valueOf(savings.get(position).getSavingsAmount()));
                if (savingsCurrentS != null && !savingsCurrentS.equals("")) {
                    savingsCurrentD = Double.valueOf(savingsCurrentS);
                } else {
                    savingsCurrentD = 0.0;
                }
                savingsCurrent2 = currencyFormat.format(savingsCurrentD);
                sav2Hldr.savingsCurrentAmount.setText(savingsCurrent2);
            } catch (NumberFormatException e7) {
                sav2Hldr.savingsCurrentAmount.setText(savingsCurrent2);
            }*/

            //sav2IncRefKeyS = savings.get(position).getIncRefKeyS();

            /*sav2Hldr.sav2Del.setTag(savings.get(position));
            sav2Hldr.sav2Edit.setTag(savings.get(position));

            //click on pencil icon
            sav2Hldr.sav2Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_4_add_savings);
                    SetUp4AddSavingsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    sav2DBMgr = new DbManager(getApplicationContext());

                    sav2SavNameET = findViewById(R.id.addSavNameET);
                    sav2SavAmtET = findViewById(R.id.addSavAmtET);
                    sav2SavGoalET = findViewById(R.id.addSavGoalET);
                    sav2SavPaytET = findViewById(R.id.addSavPaytET);
                    sav2SavPercentET = findViewById(R.id.addSavPercentET);
                    sav2SavDateResTV = findViewById(R.id.addSavDateResTV);
                    sav2SavDateResLabel = findViewById(R.id.addSavDateResLabel);
                    //sav2SavFrqLabel = findViewById(R.id.addSavFrqLabel);

                    //sav2SepRadioGroup = findViewById(R.id.addSavSepRG);
                    //sav2SepYesRadioButton = findViewById(R.id.addSavSepYesRB);
                    //sav2SepNoRadioButton = findViewById(R.id.addSavSepNoRB);
                    /*sav2FrqRadioGroup = findViewById(R.id.addSavFrqRG);
                    sav2WklyRadioButton = findViewById(R.id.addSavWklyRB);
                    sav2BiWklyRadioButton = findViewById(R.id.addSavBiWklyRB);
                    sav2MthlyRadioButton = findViewById(R.id.addSavMthlyRB);
                    sav2AnnlyRadioButton = findViewById(R.id.addSavAnnlyRB);*/

                    /*sav2SaveButton = findViewById(R.id.addSavSaveBtn);
                    sav2SaveButton.setVisibility(View.GONE);
                    sav2UpdateButton = findViewById(R.id.addSavUpdateBtn);
                    sav2CancelButton = findViewById(R.id.addSavCancelBtn);

                    sav2SavDB = (SavingsDb) sav2Hldr.sav2Edit.getTag();

                    sav2Gen.savDataFromTag(sav2SavDB);

                    //sav2SavName = sav2SavDB.getSavingsName();
                    sav2SavNameET.setText(savNameFromTag);

                    sav2Gen.dblASCurrency(String.valueOf(savAmtFromTag), sav2SavAmtET);
                    sav2SavAmtET.addTextChangedListener(onChangeSav2SavAmtET);

                    sav2Gen.dblASCurrency(String.valueOf(savGoalFromTag), sav2SavGoalET);
                    sav2SavGoalET.addTextChangedListener(onChangeSav2SavGoalET);

                    sav2Gen.dblASCurrency(String.valueOf(savPaytFromTag), sav2SavPaytET);
                    sav2SavPaytET.addTextChangedListener(onChangeSav2SavPaytET);

                    sav2SavPercent2 = savRateFromTag / 100;
                    sav2PercentFor.setMinimumFractionDigits(2);
                    sav2SavPercent3 = sav2PercentFor.format(sav2SavPercent2);
                    sav2SavPercentET.setText(sav2SavPercent3);
                    sav2SavPercentET.addTextChangedListener(onChangeSav2SavPercentET);

                    sav2SavDateResTV.setText(savDateFromTag);
                    if (savDateFromTag.equals(getString(R.string.goal_achieved))) {
                        sav2SavDateResLabel.setVisibility(View.GONE);
                        sav2SavDateResTV.setTextColor(Color.parseColor("#03ac13"));
                    } else if (savDateFromTag.equals(getString(R.string.too_far))) {
                        sav2SavDateResLabel.setVisibility(View.GONE);
                        sav2SavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        sav2SavDateResLabel.setVisibility(View.VISIBLE);
                        sav2SavDateResTV.setTextColor(Color.parseColor("#303F9F"));
                        sav2SavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    //set radio button selections from data
                    /*if (savFrqFromTag == 52) {
                        sav2WklyRadioButton.setChecked(true);
                        sav2FrqRB = "52";
                    } else if (savFrqFromTag == 26) {
                        sav2BiWklyRadioButton.setChecked(true);
                        sav2FrqRB = "26";
                    } else if (savFrqFromTag == 12) {
                        sav2MthlyRadioButton.setChecked(true);
                        sav2FrqRB = "12";
                    } else if (savFrqFromTag == 1) {
                        sav2AnnlyRadioButton.setChecked(true);
                        sav2FrqRB = "1";
                    }*/

                    /*if (savSepFromTag.equals("Y")) {
                        sav2SepYesRadioButton.setChecked(true);
                        sav2SepRB = "Y";
                    } else if (savSepFromTag.equals("N")) {
                        sav2SepNoRadioButton.setChecked(true);
                        sav2SepRB = "N";
                    }*/

                    //update db if radio buttons changed
                    /*sav2FrqRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.addSavWklyRB:
                                    sav2FrqRB = "52";
                                    sav2SavDateRes();
                                    sav2SavDateResTV.setText(sav2SavDate);
                                    break;
                                case R.id.addSavBiWklyRB:
                                    sav2FrqRB = "26";
                                    sav2SavDateRes();
                                    sav2SavDateResTV.setText(sav2SavDate);
                                    break;
                                case R.id.addSavMthlyRB:
                                    sav2FrqRB = "12";
                                    sav2SavDateRes();
                                    sav2SavDateResTV.setText(sav2SavDate);
                                    break;
                                case R.id.addSavAnnlyRB:
                                    sav2FrqRB = "1";
                                    sav2SavDateRes();
                                    sav2SavDateResTV.setText(sav2SavDate);
                                    break;
                            }
                        }
                    });*/

                    /*sav2SepRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.addSavSepYesRB:
                                    sav2SepRB = "Y";
                                    break;
                                case R.id.addSavSepNoRB:
                                    sav2SepRB = "N";
                                    break;
                            }
                        }
                    });*/

                    /*sav2UpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //sav2ExpRefKeyS = String.valueOf(sav2SavDB.getExpRefKeyS());

                            sav2SavDateRes();

                            if (!savNameFromEntry.equals("null")) {

                                sav2SavDB.setSavingsName(savNameFromEntry);
                                sav2SavDB.setSavingsAmount(savAmtFromEntry);
                                sav2SavDB.setSavingsGoal(savGoalFromEntry);
                                sav2SavDB.setSavingsPayments(savPaytFromEntry);
                                sav2SavDB.setSavingsRate(savRateFromEntry);
                                sav2SavDB.setSavingsAnnualPayments(savPaytFromEntry * 12.0);
                                sav2SavDB.setSavingsDate(sav2SavDate);

                                sav2Helper2 = new DbHelper(getContext());
                                sav2DB2 = sav2Helper2.getWritableDatabase();

                                //String[] args = new String[]{sav2ExpRefKeyS};
                               // String[] args2 = new String[]{String.valueOf(sav2SavDB.getIncRefKeyS())};
                                String[] args3 = new String[]{String.valueOf(sav2SavDB.getId())};

                                //sav2CV2 = new ContentValues();
                                //sav2CV3 = new ContentValues();
                                //sav2CV4 = new ContentValues();
                                //sav2CV5 = new ContentValues();
                                sav2CV6 = new ContentValues();

                                /*sav2CV2.put(DbHelper.EXPENSENAME, savNameFromEntry);
                                sav2CV2.put(DbHelper.EXPENSEAMOUNT, savPaytFromEntry);
                                sav2CV2.put(DbHelper.EXPENSEFREQUENCY, savFrqFromEntry);
                                expAnnAmtCalc = savPaytFromEntry * savFrqFromEntry;
                                sav2CV2.put(DbHelper.EXPENSEANNUALAMOUNT, expAnnAmtCalc);
                                if (sav2PriorityData().equals("A")) {
                                    sav2CV2.put(DbHelper.EXPENSEAANNUALAMOUNT, expAnnAmtCalc);
                                } else if (sav2PriorityData().equals("B")) {
                                    sav2CV2.put(DbHelper.EXPENSEBANNUALAMOUNT, expAnnAmtCalc);
                                }*/
                                //sav2CV3.put(DbHelper.INCOMENAME, savNameFromEntry);
                                //sav2CV4.put(DbHelper.MONEYOUTCAT, savNameFromEntry);
                                //sav2CV5.put(DbHelper.MONEYINCAT, savNameFromEntry);
                                /*sav2CV6.put(DbHelper.ACCTNAME, savNameFromEntry);

                                try {
                                    //sav2DB2.update(DbHelper.EXPENSES_TABLE_NAME, sav2CV2, DbHelper.ID + "=?", args);
                                    //sav2DB2.update(DbHelper.INCOME_TABLE_NAME, sav2CV3, DbHelper.ID + "=?", args2);
                                    //sav2DB2.update(DbHelper.MONEY_OUT_TABLE_NAME, sav2CV4, DbHelper.EXPREFKEYMO + "=?", args);
                                    //sav2DB2.update(DbHelper.MONEY_IN_TABLE_NAME, sav2CV5, DbHelper.INCREFKEYMI + "=?", args2);
                                    sav2DB2.update(DbHelper.ACCOUNTS_TABLE_NAME, sav2CV6, DbHelper.SAVID + "=?", args3);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                sav2DB2.close();

                                sav2DBMgr.updateSavings(sav2SavDB);
                                sav2LstAdapter.updateSavings(sav2DBMgr.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                sav2Gen.intentMethod(sav2Refresh, SetUp4AddSavingsList.this, SetUp4AddSavingsList.class);
                                //sav2Refresh();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    sav2CancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sav2Gen.intentMethod(sav2Refresh, SetUp4AddSavingsList.this, SetUp4AddSavingsList.class);
                            //sav2Refresh();
                        }
                    });

                    //click on trash can icon
                    sav2Hldr.sav2Del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            sav2SavDB = (SavingsDb) sav2Hldr.sav2Del.getTag();

                            sav2Helper3 = new DbHelper(getContext());
                            sav2DB3 = sav2Helper3.getWritableDatabase();

                            /*try {
                                String[] args = new String[]{String.valueOf(sav2SavDB.getExpRefKeyS())};
                                sav2DB3.delete(DbHelper.EXPENSES_TABLE_NAME, DbHelper.ID + "=?", args);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            try {
                                String[] args2 = new String[]{String.valueOf(sav2SavDB.getIncRefKeyS())};
                                sav2DB3.delete(DbHelper.INCOME_TABLE_NAME, DbHelper.ID + "=?", args2);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }*/
                            /*try {
                                String[] args3 = new String[]{String.valueOf(sav2SavDB.getId())};
                                sav2DB3.delete(DbHelper.ACCOUNTS_TABLE_NAME, DbHelper.SAVID + "=?", args3);
                            } catch (CursorIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            sav2DB3.close();

                            sav2DBMgr.deleteSavings(sav2SavDB);
                            sav2LstAdapter.updateSavings(sav2DBMgr.getSavings());
                            notifyDataSetChanged();

                            sav2Gen.intentMethod(sav2Refresh, SetUp4AddSavingsList.this, SetUp4AddSavingsList.class);
                            //sav2Refresh();

                        }
                    });
                }
            });

            return convertView;

        }
    }

    private static class Sav2ViewHolder {
        public TextView sav2NameTV;
        public TextView sav2GoalAmtTV;
        public TextView sav2GoalLabel;
        public TextView sav2DateTV;
        public TextView sav2DateLabelTV;
        public TextView sav2CurrLabelTV;
        public TextView sav2CurrAmtTV;
        ImageButton sav2Del;
        ImageButton sav2Edit;
    }
}*/

