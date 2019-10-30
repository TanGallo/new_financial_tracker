package ca.gotchasomething.mynance;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.text.NumberFormat;
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;

public class AddDebtsList extends MainNavigation {

    AccountsDb debtLstDebtDb;
    Button debtLstAddMoreBtn, debtLstUpdateBtn, debtLstCancelBtn, debtLstDoneBtn, debtLstSaveBtn;
    ContentValues debtLstCV, debtLstCV2, debtLstCV3;
    DbHelper debtLstHelper, debtLstHelper2;
    DbManager debtLstDBMgr;
    Double debtAmtFromEntry = 0.0, debtAmtFromTag = 0.0, debtRateFromEntry = 0.0, debtRateFromTag = 0.0, debtPaytFromEntry = 0.0, debtPaytFromTag = 0.0,
            debtLimitFromEntry = 0.0, debtLimitFromTag = 0.0, debtLstDebtRate2 = 0.0;
    EditText debtLstLimitET, debtLstAmtET, debtLstPercentET, debtLstPaytsET, debtLstDebtNameET;
    DebtLstListAdapter debtLstLstAdapter;
    General debtLstGen;
    Intent debtLstRefresh, debtLstToLayoutBudget, debtLstToLayoutDebt, debtLstToDaiMonCCPur, debtLstToSetUp, debtLstToAddMore, debtLstToAnalysis;
    LinearLayout debtLstSpinLayout;
    ListView debtLstListView;
    Long debtIdFromTag;
    NumberFormat debtLstPercentFor = NumberFormat.getPercentInstance();
    SQLiteDatabase debtLstDB, debtLstDB2;
    String debtEndFromTag = null, debtNameFromTag = null, debtLstDebtAmt2 = null, debtLstDebtEnd = null, debtLstDebtRate3 = null;
    TextView debtLstDateRes, debtLstDateResLabel, debtLstHeaderLabelTV, debtLstTotalTV;

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

        debtLstDBMgr = new DbManager(this);
        debtLstGen = new General();

        debtLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        debtLstSpinLayout.setVisibility(View.GONE);

        debtLstHeaderLabelTV = findViewById(R.id.layout1HeaderLabelTV);
        debtLstHeaderLabelTV.setText(getString(R.string.credit));
        debtLstTotalTV = findViewById(R.id.layout1TotalTV);
        debtLstTotalTV.setVisibility(View.GONE);

        debtLstListView = findViewById(R.id.layout1ListView);
        debtLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        debtLstAddMoreBtn.setText(getString(R.string.another_credit));
        debtLstDoneBtn = findViewById(R.id.layout1DoneBtn);

        debtLstAddMoreBtn.setOnClickListener(onClickDebtLstAddMoreButton);
        debtLstDoneBtn.setOnClickListener(onClickDebtLstDoneButton);

        debtLstLstAdapter = new DebtLstListAdapter(this, debtLstDBMgr.getDebts());
        debtLstListView.setAdapter(debtLstLstAdapter);
    }

    View.OnClickListener onClickDebtLstAddMoreButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtLstToAddMore = new Intent(AddDebtsList.this, AddDebts.class);
            debtLstToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(debtLstToAddMore);
        }
    };

    View.OnClickListener onClickDebtLstDoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (debtLstDBMgr.retrieveLatestDone().equals("savings")) {
                debtLstToAnalysis = new Intent(AddDebtsList.this, SetUpAnalysis.class);
                debtLstToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToAnalysis);
            } else if (debtLstDBMgr.retrieveLatestDone().equals("bills")) {
                debtLstCV = new ContentValues();
                debtLstCV.put(DbHelper.LATESTDONE, "debts");
                debtLstHelper = new DbHelper(getApplicationContext());
                debtLstDB = debtLstHelper.getWritableDatabase();
                debtLstDB.update(DbHelper.SET_UP_TABLE_NAME, debtLstCV, DbHelper.ID + "= '1'", null);
                debtLstDB.close();

                debtLstToSetUp = new Intent(AddDebtsList.this, LayoutSetUp.class);
                debtLstToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToSetUp);
            } else if (debtLstDBMgr.retrieveLastPageId() == 7) {
                debtLstToDaiMonCCPur = new Intent(AddDebtsList.this, LayoutCCPur.class);
                debtLstToDaiMonCCPur.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToDaiMonCCPur);
            } else if (debtLstDBMgr.retrieveLastPageId() == 5 || debtLstDBMgr.retrieveLastPageId() == 11) {
                debtLstToLayoutDebt = new Intent(AddDebtsList.this, LayoutDebt.class);
                debtLstToLayoutDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToLayoutDebt);
            } else if (debtLstDBMgr.retrieveLastPageId() == 2) {
                debtLstToLayoutBudget = new Intent(AddDebtsList.this, LayoutBudget.class);
                debtLstToLayoutBudget.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToLayoutBudget);
            }
        }
    };

    TextWatcher onChangeDebtLstAmtET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = debtLstGen.dblFromET(debtLstAmtET);
            debtRateFromEntry = debtLstGen.percentFromET(debtLstPercentET);
            debtPaytFromEntry = debtLstGen.dblFromET(debtLstPaytsET);
            debtLstDebtEndRes();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtLstPercentET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = debtLstGen.dblFromET(debtLstAmtET);
            debtRateFromEntry = debtLstGen.percentFromET(debtLstPercentET);
            debtPaytFromEntry = debtLstGen.dblFromET(debtLstPaytsET);
            debtLstDebtEndRes();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeDebtLstPaytsET = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = debtLstGen.dblFromET(debtLstAmtET);
            debtRateFromEntry = debtLstGen.percentFromET(debtLstPercentET);
            debtPaytFromEntry = debtLstGen.dblFromET(debtLstPaytsET);
            debtLstDebtEndRes();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void debtLstDebtEndRes() {

        debtLstDebtEnd = debtLstGen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));
        if (debtLstDebtEnd.equals(getString(R.string.debt_paid))) {
            debtLstDateResLabel.setVisibility(View.GONE);
            debtLstDateRes.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else if (debtLstDebtEnd.equals(getString(R.string.too_far))) {
            debtLstDateResLabel.setVisibility(View.GONE);
            debtLstDateRes.setTextColor(Color.parseColor("#ffff4444")); //red
        } else {
            debtLstDateResLabel.setVisibility(View.VISIBLE);
            debtLstDateRes.setTextColor(Color.parseColor("#303F9F")); //primary dark
            debtLstDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }

        debtLstDateRes.setText(debtLstDebtEnd);
    }

    public class DebtLstListAdapter extends ArrayAdapter<AccountsDb> {

        public Context context;
        public List<AccountsDb> debts;

        public DebtLstListAdapter(
                Context context,
                List<AccountsDb> debts) {

            super(context, -1, debts);

            this.context = context;
            this.debts = debts;
        }

        public void updateDebts(List<AccountsDb> debts) {
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

            final DebtLstViewHolder debtLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                debtLstHldr = new DebtLstViewHolder();
                debtLstHldr.debtLstNameTV = convertView.findViewById(R.id.bigLstTV1);
                debtLstHldr.debtLstAmtTV = convertView.findViewById(R.id.bigLstTV2);
                debtLstHldr.debtLstFreeDateLabel = convertView.findViewById(R.id.bigLstLabel);
                debtLstHldr.debtLstFreeDateLabel.setText(getString(R.string.debt_will));
                debtLstHldr.debtLstFreeDateTV = convertView.findViewById(R.id.bigLstTV3);
                debtLstHldr.debtLstDel = convertView.findViewById(R.id.bigLstDelBtn);
                debtLstHldr.debtLstEdit = convertView.findViewById(R.id.bigLstEditBtn);
                if(debtLstDBMgr.retrieveLastPageId() == 7 || debtLstDBMgr.retrieveLastPageId() == 11) {
                    debtLstHldr.debtLstDel.setVisibility(View.GONE);
                    debtLstHldr.debtLstEdit.setVisibility(View.GONE);
                }
                debtLstHldr.debtLstLabel2 = convertView.findViewById(R.id.bigLstLabel2);
                debtLstHldr.debtLstLabel2.setVisibility(View.GONE);
                debtLstHldr.debtLstOverLimit = convertView.findViewById(R.id.bigLstTV4);
                debtLstHldr.debtLstOverLimit.setText(getString(R.string.over_limit));
                debtLstHldr.debtLstOverLimit.setTextColor(Color.parseColor("#ffff4444"));
                convertView.setTag(debtLstHldr);

            } else {
                debtLstHldr = (DebtLstViewHolder) convertView.getTag();
            }

            //retrieve debtName
            debtLstHldr.debtLstNameTV.setText(debts.get(position).getAcctName());

            //retrieve debtAmount and format as currency
            debtLstDebtAmt2 = (String.valueOf(debts.get(position).getAcctBal()));
            debtLstGen.dblASCurrency(debtLstDebtAmt2, debtLstHldr.debtLstAmtTV);

            //retrieve debtEnd
            debtLstDebtEnd = debts.get(position).getAcctEndDate();

            if (debtLstDebtEnd.contains("2")) {
                debtLstHldr.debtLstFreeDateLabel.setVisibility(View.VISIBLE);
                debtLstHldr.debtLstFreeDateTV.setTextColor(Color.parseColor("#303F9F")); //primary dark
                debtLstHldr.debtLstFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            } else if (debtLstDebtEnd.equals(getString(R.string.debt_paid))) {
                debtLstHldr.debtLstFreeDateLabel.setVisibility(View.GONE);
                debtLstHldr.debtLstFreeDateTV.setTextColor(Color.parseColor("#5dbb63")); //light green
            } else if (debtLstDebtEnd.equals(getString(R.string.too_far))) {
                debtLstHldr.debtLstFreeDateLabel.setVisibility(View.GONE);
                debtLstHldr.debtLstFreeDateTV.setTextColor(Color.parseColor("#ffff4444")); //red
            } else {
                debtLstHldr.debtLstFreeDateLabel.setVisibility(View.VISIBLE);
                debtLstHldr.debtLstFreeDateTV.setTextColor(Color.parseColor("#303F9F"));
                debtLstHldr.debtLstFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            debtLstHldr.debtLstFreeDateTV.setText(debtLstDebtEnd);

            if (debts.get(position).getAcctMax() == 0) {
                debtLstHldr.debtLstOverLimit.setVisibility(View.GONE);
            } else if (debts.get(position).getAcctBal() > debts.get(position).getAcctMax()) {
                debtLstHldr.debtLstOverLimit.setVisibility(View.VISIBLE);
            } else {
                debtLstHldr.debtLstOverLimit.setVisibility(View.GONE);
            }

            debtLstHldr.debtLstDel.setTag(debts.get(position));
            debtLstHldr.debtLstEdit.setTag(debts.get(position));

            //click on pencil icon to edit a data record
            debtLstHldr.debtLstEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_3_add_debt);
                    AddDebtsList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    debtLstDBMgr = new DbManager(getApplicationContext());

                    debtLstDebtNameET = findViewById(R.id.addDebtNameET);
                    debtLstLimitET = findViewById(R.id.addDebtLimitET);
                    debtLstAmtET = findViewById(R.id.addDebtAmtET);
                    debtLstPercentET = findViewById(R.id.addDebtRateET);
                    debtLstPaytsET = findViewById(R.id.addDebtPaytET);
                    debtLstDateRes = findViewById(R.id.addDebtDateResTV);
                    debtLstDateResLabel = findViewById(R.id.addDebtDateResLabel);

                    debtLstSaveBtn = findViewById(R.id.addDebtSaveBtn);
                    debtLstSaveBtn.setVisibility(View.GONE);
                    debtLstUpdateBtn = findViewById(R.id.addDebtUpdateBtn);
                    debtLstCancelBtn = findViewById(R.id.addDebtCancelBtn);

                    debtLstDebtDb = (AccountsDb) debtLstHldr.debtLstEdit.getTag();

                    debtNameFromTag = debtLstDebtDb.getAcctName();
                    debtAmtFromTag = debtLstDebtDb.getAcctBal();
                    debtLimitFromTag = debtLstDebtDb.getAcctMax();
                    debtRateFromTag = debtLstDebtDb.getAcctIntRate();
                    debtPaytFromTag = debtLstDebtDb.getAcctPaytsTo();
                    debtEndFromTag = debtLstDebtDb.getAcctEndDate();
                    debtIdFromTag = debtLstDebtDb.getId();

                    debtLstDebtNameET.setText(debtNameFromTag);
                    if (debtEndFromTag.equals(getString(R.string.debt_paid))) {
                        debtLstDateResLabel.setVisibility(View.GONE);
                        debtLstDateRes.setTextColor(Color.parseColor("#5dbb63")); //light green
                    } else if (debtEndFromTag.equals(getString(R.string.too_far))) {
                        debtLstDateResLabel.setVisibility(View.GONE);
                        debtLstDateRes.setTextColor(Color.parseColor("#ffff4444")); //red
                    } else {
                        debtLstDateResLabel.setVisibility(View.VISIBLE);
                        debtLstDateRes.setTextColor(Color.parseColor("#303F9F")); //pri,ary dark
                        debtLstDateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    debtLstGen.dblASCurrency(String.valueOf(debtLimitFromTag), debtLstLimitET);
                    
                    debtLstGen.dblASCurrency(String.valueOf(debtAmtFromTag), debtLstAmtET);
                    debtLstAmtET.addTextChangedListener(onChangeDebtLstAmtET);

                    debtLstDebtRate2 = debtRateFromTag / 100;
                    debtLstPercentFor.setMinimumFractionDigits(2);
                    debtLstDebtRate3 = debtLstPercentFor.format(debtLstDebtRate2);
                    debtLstPercentET.setText(debtLstDebtRate3);
                    debtLstPercentET.addTextChangedListener(onChangeDebtLstPercentET);

                    debtLstGen.dblASCurrency(String.valueOf(debtPaytFromTag), debtLstPaytsET);
                    debtLstPaytsET.addTextChangedListener(onChangeDebtLstPaytsET);

                    debtLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!debtLstGen.stringFromET(debtLstDebtNameET).equals("null")) {

                                debtLimitFromEntry = debtLstGen.dblFromET(debtLstLimitET);
                                debtAmtFromEntry = debtLstGen.dblFromET(debtLstAmtET);
                                debtRateFromEntry = debtLstGen.percentFromET(debtLstPercentET);
                                debtPaytFromEntry = debtLstGen.dblFromET(debtLstPaytsET);
                                
                                debtLstDebtDb.setAcctName(debtLstGen.stringFromET(debtLstDebtNameET));
                                debtLstDebtDb.setAcctMax(debtLimitFromEntry);
                                debtLstDebtDb.setAcctBal(debtAmtFromEntry);
                                debtLstDebtDb.setAcctIntRate(debtRateFromEntry);
                                debtLstDebtDb.setAcctPaytsTo(debtPaytFromEntry);
                                debtLstDebtEndRes();
                                debtLstDebtDb.setAcctEndDate(debtLstDebtEnd);

                                debtLstDBMgr.updateAccounts(debtLstDebtDb);

                                if(debtLstDBMgr.getTransfers().size() != 0) {
                                    debtLstDBMgr.updateRecReTransfer(debtIdFromTag, debtLstDBMgr.transfersToAcctThisYear(debtIdFromTag, debtLstGen.lastNumOfDays(365)), debtLstDBMgr.transfersFromAcctThisYear(debtIdFromTag, debtLstGen.lastNumOfDays(365)));
                                } else {
                                    debtLstDebtDb.setAcctAnnPaytsTo(debtPaytFromEntry * 12.0);
                                    debtLstDBMgr.updateAccounts(debtLstDebtDb);
                                }

                                debtLstHelper2 = new DbHelper(getContext());
                                debtLstDB2 = debtLstHelper2.getWritableDatabase();

                                String[] args2 = new String[]{String.valueOf(debtLstDebtDb.getId())};

                                debtLstCV2 = new ContentValues();
                                debtLstCV3 = new ContentValues();

                                debtLstCV2.put(DbHelper.TRANSFROMACCTNAME, debtLstGen.stringFromET(debtLstDebtNameET));
                                debtLstCV3.put(DbHelper.TRANSTOACCTNAME, debtLstGen.stringFromET(debtLstDebtNameET));

                                try {
                                    debtLstDB2.update(DbHelper.TRANSACTIONS_TABLE_NAME, debtLstCV2, DbHelper.TRANSFROMACCTID + "=?", args2);
                                    debtLstDB2.update(DbHelper.TRANSACTIONS_TABLE_NAME, debtLstCV3, DbHelper.TRANSTOACCTID + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                debtLstDB2.close();

                                debtLstLstAdapter.updateDebts(debtLstDBMgr.getDebts());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                debtLstRefresh = new Intent(AddDebtsList.this, AddDebtsList.class);
                                debtLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                startActivity(debtLstRefresh);
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    debtLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            debtLstRefresh = new Intent(AddDebtsList.this, AddDebtsList.class);
                            debtLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(debtLstRefresh);
                        }
                    });
                }
            });

            //click on trash can icon
            debtLstHldr.debtLstDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    debtLstDebtDb = (AccountsDb) debtLstHldr.debtLstDel.getTag();

                    debtLstDBMgr.deleteAccounts(debtLstDebtDb);
                    debtLstLstAdapter.updateDebts(debtLstDBMgr.getDebts());
                    notifyDataSetChanged();

                    debtLstRefresh = new Intent(AddDebtsList.this, AddDebtsList.class);
                    debtLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(debtLstRefresh);
                }
            });

            return convertView;
        }
    }

    private static class DebtLstViewHolder {
        private TextView debtLstNameTV;
        private TextView debtLstAmtTV;
        private TextView debtLstFreeDateLabel;
        private TextView debtLstFreeDateTV;
        private ImageButton debtLstDel;
        private ImageButton debtLstEdit;
        private TextView debtLstLabel2;
        private TextView debtLstOverLimit;
    }
}