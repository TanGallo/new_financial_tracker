package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.text.NumberFormat;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;

public class AddSavingsList extends MainNavigation {

    AccountsDb savLstSavDb;
    Button savLstAddMoreBtn, savLstUpdateBtn, savLstCancelBtn, savLstDoneBtn, savLstSaveBtn;
    ContentValues savLstCV;
    DbHelper savLstHelper;
    DbManager savLstDBMgr;
    Double savAmtFromEntry = 0.0, savAmtFromTag = 0.0, savRateFromEntry = 0.0, savRateFromTag = 0.0, savPaytFromEntry = 0.0, savPaytFromTag = 0.0,
            savGoalFromEntry = 0.0, savGoalFromTag = 0.0, savLstSavRate2 = 0.0, savLstGoalAmt2 = 0.0, savLstSavCurr = 0.0;
    EditText savLstSavAmtET, savLstSavGoalET, savLstSavPaytET, savLstSavPercentET, savLstSavNameET;
    SavLstListAdapter savLstLstAdapter;
    General savLstGen;
    Intent savLstToLayoutBudget, savLstToLayoutSavings, savLstToLayoutSavingsList, savLstRefresh, savLstToSetUp, savLstToAddMore, savLstToAnalysis;
    LinearLayout savLstSpinLayout;
    ListView savLstListView;
    Long savIdFromTag;
    NumberFormat savLstPercentFor = NumberFormat.getPercentInstance();
    SQLiteDatabase savLstDB;
    String savDateFromTag = null, savNameFromEntry = null, savNameFromTag = null, savLstSavDate = null, savLstSavDate2 = null, savLstSavRate3 = null;
    TextView savLstSavDateResLabel, savLstHeaderLabelTV, savLstSavDateResTV, savLstTotalTV;

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

        savLstDBMgr = new DbManager(this);
        savLstGen = new General();

        savLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        savLstSpinLayout.setVisibility(View.GONE);

        savLstHeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        savLstHeaderLabelTV.setText(getString(R.string.savings));
        savLstTotalTV = findViewById(R.id.layout1TotalTV);
        savLstTotalTV.setVisibility(View.GONE);

        savLstListView = findViewById(R.id.layout1ListView);
        savLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        savLstAddMoreBtn.setText(getString(R.string.another_savings));
        savLstDoneBtn = findViewById(R.id.layout1DoneBtn);

        savLstAddMoreBtn.setOnClickListener(onClickSavLstAddMoreButton);
        savLstDoneBtn.setOnClickListener(onClickSavLstDoneButton);

        savLstLstAdapter = new SavLstListAdapter(this, savLstDBMgr.getSavings());
        savLstListView.setAdapter(savLstLstAdapter);
    }

    View.OnClickListener onClickSavLstAddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savLstToAddMore = new Intent(AddSavingsList.this, AddSavings.class);
            savLstToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(savLstToAddMore);
        }
    };

    View.OnClickListener onClickSavLstDoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (savLstDBMgr.retrieveLatestDone().equals("savings")) {
                savLstToAnalysis = new Intent(AddSavingsList.this, SetUpAnalysis.class);
                savLstToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(savLstToAnalysis);
            } else if (savLstDBMgr.retrieveLatestDone().equals("debts")) {
                savLstCV = new ContentValues();
                savLstCV.put(DbHelper.LATESTDONE, "savings");
                savLstHelper = new DbHelper(getApplicationContext());
                savLstDB = savLstHelper.getWritableDatabase();
                savLstDB.update(DbHelper.SET_UP_TABLE_NAME, savLstCV, DbHelper.ID + "= '1'", null);
                savLstDB.close();

                savLstToSetUp = new Intent(AddSavingsList.this, LayoutSetUp.class);
                savLstToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(savLstToSetUp);
            } else if (savLstDBMgr.retrieveLastPageId() == 13) {
                savLstToLayoutSavings = new Intent(AddSavingsList.this, LayoutSavings.class);
                savLstToLayoutSavings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(savLstToLayoutSavings);
            }
        }
    };

    /*TextWatcher onChangeSavLstSavAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savLstSavDateRes();
            savLstSavDateResTV.setText(savLstSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavLstSavGoalET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savLstSavDateRes();
            savLstSavDateResTV.setText(savLstSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavLstSavPaytET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savLstSavDateRes();
            savLstSavDateResTV.setText(savLstSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeSavLstSavPercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savLstSavDateRes();
            savLstSavDateResTV.setText(savLstSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };*/

    /*public void savLstSavDateRes() {
        savNameFromEntry = savLstGen.stringFromET(savLstSavNameET);
        savAmtFromEntry = savLstGen.dblFromET(savLstSavAmtET);
        savGoalFromEntry = savLstGen.dblFromET(savLstSavGoalET);
        savRateFromEntry = savLstGen.percentFromET(savLstSavPercentET);
        savPaytFromEntry = savLstGen.dblFromET(savLstSavPaytET);

        savLstSavDate2 = savLstGen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (savLstSavDate2.equals(getString(R.string.goal_achieved))) {
            savLstSavDateResLabel.setVisibility(View.GONE);
            savLstSavDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (savLstSavDate2.equals(getString(R.string.too_far))) {
            savLstSavDateResLabel.setVisibility(View.GONE);
            savLstSavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            savLstSavDateResLabel.setVisibility(View.VISIBLE);
            savLstSavDateResTV.setTextColor(Color.parseColor("#303F9F"));
            savLstSavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }
    }*/

    public class SavLstListAdapter extends ArrayAdapter<AccountsDb> {

        public Context context;
        public List<AccountsDb> savings;

        public SavLstListAdapter(
                Context context,
                List<AccountsDb> savings) {

            super(context, -1, savings);

            this.context = context;
            this.savings = savings;
        }

        public void updateSavings(List<AccountsDb> savings) {
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

            final SavLstViewHolder savLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                savLstHldr = new SavLstViewHolder();
                savLstHldr.savLstNameTV = convertView.findViewById(R.id.bigLstTV1);
                savLstHldr.savLstGoalAmtTV = convertView.findViewById(R.id.bigLstTV2);
                savLstHldr.savLstDateLabelTV = convertView.findViewById(R.id.bigLstLabel);
                savLstHldr.savLstDateLabelTV.setText(getString(R.string.goal_will));
                savLstHldr.savLstDateTV = convertView.findViewById(R.id.bigLstTV3);
                savLstHldr.savLstCurrLabelTV = convertView.findViewById(R.id.bigLstLabel2);
                savLstHldr.savLstCurrLabelTV.setText(getString(R.string.current_balance));
                savLstHldr.savLstCurrAmtTV = convertView.findViewById(R.id.bigLstTV4);
                savLstHldr.savLstDel = convertView.findViewById(R.id.bigLstDelBtn);
                savLstHldr.savLstEdit = convertView.findViewById(R.id.bigLstEditBtn);
                //if (savLstDBMgr.retrieveLastPageId() == 13) {
                    savLstHldr.savLstDel.setVisibility(View.GONE);
                    savLstHldr.savLstEdit.setVisibility(View.GONE);
                //}
                convertView.setTag(savLstHldr);

            } else {
                savLstHldr = (SavLstViewHolder) convertView.getTag();
            }

            //retrieve debtName
            savLstHldr.savLstNameTV.setText(savings.get(position).getAcctName());

            //retrieve debtAmount and format as currency
            savLstGoalAmt2 = (savings.get(position).getAcctMax());
            savLstGen.dblASCurrency(String.valueOf(savLstGoalAmt2), savLstHldr.savLstGoalAmtTV);

            //retrieve savingsDate
            savLstSavDate = savings.get(position).getAcctEndDate();
            savLstHldr.savLstDateTV.setText(savLstSavDate);
            if (savLstSavDate.contains("2")) {
                savLstHldr.savLstDateLabelTV.setVisibility(View.VISIBLE);
            } else {
                savLstHldr.savLstDateLabelTV.setVisibility(View.GONE);
            }
            if (savLstSavDate.equals(getString(R.string.goal_achieved))) {
                savLstHldr.savLstDateTV.setTextColor(Color.parseColor("#03ac13"));
            } else if (savLstSavDate.equals(getString(R.string.too_far))) {
                savLstHldr.savLstDateTV.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                savLstHldr.savLstDateTV.setTextColor(Color.parseColor("#303F9F"));
                savLstHldr.savLstDateLabelTV.setTextColor(Color.parseColor("#303F9F"));
            }

            //retrieve savingsAmount & format as currency
            savLstSavCurr = (savings.get(position).getAcctBal());
            savLstGen.dblASCurrency(String.valueOf(savLstSavCurr), savLstHldr.savLstCurrAmtTV);

            /*savLstHldr.savLstDel.setTag(savings.get(position));
            savLstHldr.savLstEdit.setTag(savings.get(position));*/

            //click on pencil icon to edit a data record
            /*savLstHldr.savLstEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_4_add_savings);
                    AddSavingsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    savLstDBMgr = new DbManager(getApplicationContext());

                    savLstSavNameET = findViewById(R.id.addSavNameET);
                    savLstSavAmtET = findViewById(R.id.addSavAmtET);
                    savLstSavGoalET = findViewById(R.id.addSavGoalET);
                    savLstSavPaytET = findViewById(R.id.addSavPaytET);
                    savLstSavPercentET = findViewById(R.id.addSavPercentET);
                    savLstSavDateResTV = findViewById(R.id.addSavDateResTV);
                    savLstSavDateResLabel = findViewById(R.id.addSavDateResLabel);

                    savLstSaveBtn = findViewById(R.id.addSavSaveBtn);
                    savLstSaveBtn.setVisibility(View.GONE);
                    savLstUpdateBtn = findViewById(R.id.addSavUpdateBtn);
                    savLstCancelBtn = findViewById(R.id.addSavCancelBtn);

                    savLstSavDb = (AccountsDb) savLstHldr.savLstEdit.getTag();

                    savNameFromTag = savLstSavDb.getAcctName();
                    savAmtFromTag = savLstSavDb.getAcctBal();
                    savGoalFromTag = savLstSavDb.getAcctMax();
                    savPaytFromTag = savLstSavDb.getAcctPaytsTo();
                    savRateFromTag = savLstSavDb.getAcctIntRate();
                    savDateFromTag = savLstSavDb.getAcctEndDate();
                    savIdFromTag = savLstSavDb.getId();

                    savLstSavNameET.setText(savNameFromTag);
                    savLstGen.dblASCurrency(String.valueOf(savAmtFromTag), savLstSavAmtET);
                    savLstSavAmtET.addTextChangedListener(onChangeSavLstSavAmtET);
                    savLstGen.dblASCurrency(String.valueOf(savGoalFromTag), savLstSavGoalET);
                    savLstSavGoalET.addTextChangedListener(onChangeSavLstSavGoalET);
                    savLstGen.dblASCurrency(String.valueOf(savPaytFromTag), savLstSavPaytET);
                    savLstSavPaytET.addTextChangedListener(onChangeSavLstSavPaytET);
                    savLstSavRate2 = savRateFromTag / 100;
                    savLstPercentFor.setMinimumFractionDigits(2);
                    savLstSavRate3 = savLstPercentFor.format(savLstSavRate2);
                    savLstSavPercentET.setText(savLstSavRate3);
                    savLstSavPercentET.addTextChangedListener(onChangeSavLstSavPercentET);

                    savLstSavDateResTV.setText(savDateFromTag);
                    if (savDateFromTag.equals(getString(R.string.goal_achieved))) {
                        savLstSavDateResLabel.setVisibility(View.GONE);
                        savLstSavDateResTV.setTextColor(Color.parseColor("#03ac13"));
                    } else if (savDateFromTag.equals(getString(R.string.too_far))) {
                        savLstSavDateResLabel.setVisibility(View.GONE);
                        savLstSavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        savLstSavDateResLabel.setVisibility(View.VISIBLE);
                        savLstSavDateResTV.setTextColor(Color.parseColor("#303F9F"));
                        savLstSavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    savLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            savLstSavDateRes();

                            if (!savNameFromEntry.equals("null")) {

                                savLstSavDb.setAcctName(savNameFromEntry);
                                savLstSavDb.setAcctBal(savAmtFromEntry);
                                savLstSavDb.setAcctMax(savGoalFromEntry);
                                savLstSavDb.setAcctPaytsTo(savPaytFromEntry);
                                savLstSavDb.setAcctIntRate(savRateFromEntry);
                                savLstSavDb.setAcctEndDate(savLstSavDate2);

                                savLstDBMgr.updateAccounts(savLstSavDb);

                                if (savLstDBMgr.getTransfers().size() != 0) {
                                    //savLstTransToSavThisYr = savLstDBMgr.transfersToSavThisYear(savIdFromTag);
                                    //savLstTransFromSavThisYr = savLstDBMgr.transfersFromSavThisYear(savIdFromTag);
                                    savLstDBMgr.updateRecReTransfer(savIdFromTag, savLstDBMgr.transfersToAcctThisYear(savIdFromTag, savLstGen.lastNumOfDays(365)), savLstDBMgr.transfersFromAcctThisYear(savIdFromTag, savLstGen.lastNumOfDays(365)));
                                } else {
                                    savLstSavDb.setAcctAnnPaytsTo(savPaytFromEntry * 12.0);
                                    savLstDBMgr.updateAccounts(savLstSavDb);
                                }

                                savLstLstAdapter.updateSavings(savLstDBMgr.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                savLstRefresh = new Intent(AddSavingsList.this, AddSavingsList.class);
                                savLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                startActivity(savLstRefresh);
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    savLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            savLstRefresh = new Intent(AddSavingsList.this, AddSavingsList.class);
                            savLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(savLstRefresh);
                        }
                    });
                }
            });*/

            //click on trash can icon
            /*savLstHldr.savLstDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    savLstSavDb = (AccountsDb) savLstHldr.savLstDel.getTag();

                    savLstDBMgr.deleteAccounts(savLstSavDb);
                    savLstLstAdapter.updateSavings(savLstDBMgr.getSavings());
                    notifyDataSetChanged();

                    savLstRefresh = new Intent(AddSavingsList.this, AddSavingsList.class);
                    savLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(savLstRefresh);
                }
            });*/

            return convertView;
        }
    }

    private static class SavLstViewHolder {
        private TextView savLstNameTV;
        private TextView savLstGoalAmtTV;
        private TextView savLstDateLabelTV;
        private TextView savLstDateTV;
        private ImageButton savLstDel;
        private ImageButton savLstEdit;
        private TextView savLstCurrLabelTV;
        private TextView savLstCurrAmtTV;
    }
}
