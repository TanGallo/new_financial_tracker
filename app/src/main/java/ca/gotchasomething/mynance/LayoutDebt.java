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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import ca.gotchasomething.mynance.data.AccountsDb;

public class LayoutDebt extends MainNavigation {

    AccountsDb layDebtDebtDb;
    Button layDebtDebtCancelBtn, layDebtDoneBtn, layDebtDebtSaveBtn, layDebtDebtUpdateBtn;
    ContentValues layDebtCV, layDebtCV2, layDebtCV3;
    Date latestDateD;
    DbHelper layDebtHelper, layDebtHelper3;
    DbManager layDebtDbMgr;
    LayDebtListAdapter layDebtListAdapter;
    Double layDebtDebtRate = 0.0, layDebtTotDebt = 0.0, debtAmtFromEntry = 0.0, debtAmtFromTag = 0.0, debtLimitFromEntry = 0.0, debtLimitFromTag = 0.0,
            debtPaytFromEntry = 0.0, debtPaytFromTag = 0.0, debtRateFromEntry = 0.0, debtRateFromTag = 0.0;
    EditText layDebtDebtAmtET, layDebtDebtLimitET, layDebtDebtNameET, layDebtDebtPaytET, layDebtDebtRateET;
    FloatingActionButton layDebtAddDebtBtn;
    General layDebtGen;
    Intent layDebtToAddMore, layDebtRefresh, layDebtToAnalysis;
    ListView layDebtListView;
    long id, debtIdFromTag;
    NumberFormat layDebtPerFor = NumberFormat.getPercentInstance();
    SQLiteDatabase layDebtDb, layDebtDb3;
    SimpleDateFormat latestDateS;
    String layDebtDebtEnd = null, layDebtDebtEnd2 = null, layDebtDebtRateS = null, debtEndFromTag = null, debtNameFromTag = null, latestDate = null;
    TextView layDebtDateHeaderLabel, layDebtDateHeaderTV, layDebtDebtDateResLabel, layDebtDebtDateResTV, layDebtTotAmtHeaderLabel, layDebtTotAmtHeaderTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c4_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        layDebtGen = new General();
        layDebtDbMgr = new DbManager(this);

        layDebtTotAmtHeaderLabel = findViewById(R.id.c4HeaderLabel);
        layDebtTotAmtHeaderLabel.setText(getString(R.string.total_owing));
        layDebtTotAmtHeaderTV = findViewById(R.id.c4HeaderTV);
        layDebtDateHeaderLabel = findViewById(R.id.c4HeaderLabel2);
        layDebtDateHeaderLabel.setText(getString(R.string.debt_free_by));
        layDebtDateHeaderTV = findViewById(R.id.c4HeaderTV2);
        layDebtAddDebtBtn = findViewById(R.id.c4AddMoreBtn);
        layDebtDoneBtn = findViewById(R.id.c4DoneBtn);
        if(layDebtDbMgr.retrieveLatestDone().equals("savings")) {
            layDebtDoneBtn.setVisibility(View.VISIBLE);
            layDebtDoneBtn.setOnClickListener(onClickLayDebtDoneBtn);
        } else {
            layDebtDoneBtn.setVisibility(View.GONE);
        }

        layDebtAddDebtBtn.setOnClickListener(onClickLayDebtAddDebtBtn);

        layDebtListView = findViewById(R.id.c4ListView);
        layDebtListAdapter = new LayDebtListAdapter(this, layDebtDbMgr.getDebts());
        layDebtListView.setAdapter(layDebtListAdapter);

        layDebtHeaderText();

        layDebtCV = new ContentValues();
        layDebtCV.put(DbHelper.LASTPAGEID, 5);
        layDebtHelper3 = new DbHelper(getApplicationContext());
        layDebtDb3 = layDebtHelper3.getWritableDatabase();
        layDebtDb3.update(DbHelper.CURRENT_TABLE_NAME, layDebtCV, DbHelper.ID + "= '1'", null);
        layDebtDb3.close();
    }

    public void layDebtHeaderText() {

        layDebtTotDebt = layDebtDbMgr.sumTotalDebt();
        layDebtGen.dblASCurrency(String.valueOf(layDebtTotDebt), layDebtTotAmtHeaderTV);

        if (layDebtListAdapter.getCount() == 0) {
            layDebtDateHeaderLabel.setVisibility(View.GONE);
            layDebtDateHeaderTV.setVisibility(View.GONE);
        } else {
            layDebtDateHeaderLabel.setVisibility(View.VISIBLE);
            layDebtDateHeaderTV.setVisibility(View.VISIBLE);
            layDebtDateHeaderTV.setText(layDebtLatestDate());
        }
    }

    public String layDebtLatestDate() {
        List<String> dates = new ArrayList<>();
        for (AccountsDb a : layDebtDbMgr.getDebts()) {
                dates.add(a.getAcctEndDate());
        }
        List<Date> dates2 = new ArrayList<>(dates.size());
        layDebtGen.extractingDates(dates, dates2);

        try {
            latestDateD = Collections.max(dates2);
        } catch (NoSuchElementException e) {
            layDebtDateHeaderLabel.setVisibility(View.GONE);
            layDebtDateHeaderTV.setVisibility(View.GONE);
        }
        try {
            latestDateS = new SimpleDateFormat("dd-MMM-yyyy");
            latestDate = latestDateS.format(latestDateD);
        } catch (Exception e2) {
            layDebtDateHeaderTV.setVisibility(View.GONE);
            //layDebtDateHeaderLabel.setVisibility(View.GONE);
        }
        return latestDate;
    }

    public void layDebtRefresh() {
        layDebtRefresh = new Intent(LayoutDebt.this, LayoutDebt.class);
        layDebtRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(layDebtRefresh);
    }

    TextWatcher onChangeLayDebtDebtAmt = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = layDebtGen.dblFromET(layDebtDebtAmtET);
            debtRateFromEntry = layDebtGen.percentFromET(layDebtDebtRateET);
            debtPaytFromEntry = layDebtGen.dblFromET(layDebtDebtPaytET);
            layDebtDebtEndResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeLayDebtDebtRate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = layDebtGen.dblFromET(layDebtDebtAmtET);
            debtRateFromEntry = layDebtGen.percentFromET(layDebtDebtRateET);
            debtPaytFromEntry = layDebtGen.dblFromET(layDebtDebtPaytET);
            layDebtDebtEndResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeLayDebtDebtPayt = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            debtAmtFromEntry = layDebtGen.dblFromET(layDebtDebtAmtET);
            debtRateFromEntry = layDebtGen.percentFromET(layDebtDebtRateET);
            debtPaytFromEntry = layDebtGen.dblFromET(layDebtDebtPaytET);
            layDebtDebtEndResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void layDebtDebtEndResult() {
        layDebtDebtEnd2 = layDebtGen.calcDebtDate(
                debtAmtFromEntry,
                debtRateFromEntry,
                debtPaytFromEntry,
                getString(R.string.debt_paid),
                getString(R.string.too_far));

        if (layDebtDebtEnd2.equals(getString(R.string.debt_paid))) {
            layDebtDebtDateResLabel.setVisibility(View.GONE);
            layDebtDebtDateResTV.setTextColor(Color.parseColor("#5dbb63")); //light green
        } else if (layDebtDebtEnd2.equals(getString(R.string.too_far))) {
            layDebtDebtDateResLabel.setVisibility(View.GONE);
            layDebtDebtDateResTV.setTextColor(Color.parseColor("#ffff4444")); //red
        } else {
            layDebtDebtDateResLabel.setVisibility(View.VISIBLE);
            layDebtDebtDateResTV.setTextColor(Color.parseColor("#303F9F")); //primary dark
            layDebtDebtDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }

        layDebtDebtDateResTV.setText(layDebtDebtEnd2);
    }

    public class LayDebtListAdapter extends ArrayAdapter<AccountsDb> {

        private Context context;
        private List<AccountsDb> debts;

        private LayDebtListAdapter(
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

            final LayDebtViewHolder layDebtHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                layDebtHldr = new LayDebtViewHolder();
                layDebtHldr.layDebttNameTV = convertView.findViewById(R.id.bigLstTV1);
                layDebtHldr.layDebtAmtTV = convertView.findViewById(R.id.bigLstTV2);
                layDebtHldr.layDebtFreeDateLabel = convertView.findViewById(R.id.bigLstLabel);
                layDebtHldr.layDebtFreeDateLabel.setText(getString(R.string.debt_will));
                layDebtHldr.layDebtFreeDateTV = convertView.findViewById(R.id.bigLstTV3);
                layDebtHldr.layDebtDelBtn = convertView.findViewById(R.id.bigLstDelBtn);
                layDebtHldr.layDebtEditBtn = convertView.findViewById(R.id.bigLstEditBtn);
                layDebtHldr.layDebtLabel2 = convertView.findViewById(R.id.bigLstLabel2);
                layDebtHldr.layDebtLabel2.setVisibility(View.GONE);
                layDebtHldr.layDebtOverLimitWarn = convertView.findViewById(R.id.bigLstTV4);
                layDebtHldr.layDebtOverLimitWarn.setText(getString(R.string.over_limit));
                layDebtHldr.layDebtOverLimitWarn.setTextColor(Color.parseColor("#ffff4444"));
                convertView.setTag(layDebtHldr);

            } else {
                layDebtHldr = (LayDebtViewHolder) convertView.getTag();
            }

            //retrieve debtName
            layDebtHldr.layDebttNameTV.setText(debts.get(position).getAcctName());

            layDebtGen.dblASCurrency(String.valueOf(debts.get(position).getAcctBal()), layDebtHldr.layDebtAmtTV);

            //retrieve debtEnd
            layDebtDebtEnd = debts.get(position).getAcctEndDate();

            if (layDebtDebtEnd.contains("2")) {
                layDebtHldr.layDebtFreeDateLabel.setVisibility(View.VISIBLE);
                layDebtHldr.layDebtFreeDateTV.setTextColor(Color.parseColor("#303F9F")); //primary dark
                layDebtHldr.layDebtFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            } else if (layDebtDebtEnd.equals(getString(R.string.debt_paid))) {
                layDebtHldr.layDebtFreeDateLabel.setVisibility(View.GONE);
                layDebtHldr.layDebtFreeDateTV.setTextColor(Color.parseColor("#5dbb63")); //light green
            } else if (layDebtDebtEnd.equals(getString(R.string.too_far))) {
                layDebtHldr.layDebtFreeDateLabel.setVisibility(View.GONE);
                layDebtHldr.layDebtFreeDateTV.setTextColor(Color.parseColor("#ffff4444")); //red
            } else {
                layDebtHldr.layDebtFreeDateLabel.setVisibility(View.VISIBLE);
                layDebtHldr.layDebtFreeDateTV.setTextColor(Color.parseColor("#303F9F")); //primary dark
                layDebtHldr.layDebtFreeDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            layDebtHldr.layDebtFreeDateTV.setText(layDebtDebtEnd);

            layDebtHldr.layDebtDelBtn.setTag(debts.get(position));
            layDebtHldr.layDebtEditBtn.setTag(debts.get(position));

            if (debts.get(position).getAcctMax() == 0) {
                layDebtHldr.layDebtOverLimitWarn.setVisibility(View.GONE);
            } else if (debts.get(position).getAcctBal() > debts.get(position).getAcctMax()) {
                layDebtHldr.layDebtOverLimitWarn.setVisibility(View.VISIBLE);
            } else {
                layDebtHldr.layDebtOverLimitWarn.setVisibility(View.GONE);
            }

            //click on pencil icon
            layDebtHldr.layDebtEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_3_add_debt);
                    LayoutDebt.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    layDebtDbMgr = new DbManager(getApplicationContext());

                    layDebtDebtNameET = findViewById(R.id.addDebtNameET);
                    layDebtDebtLimitET = findViewById(R.id.addDebtLimitET);
                    layDebtDebtAmtET = findViewById(R.id.addDebtAmtET);
                    layDebtDebtRateET = findViewById(R.id.addDebtRateET);
                    layDebtDebtPaytET = findViewById(R.id.addDebtPaytET);
                    layDebtDebtDateResTV = findViewById(R.id.addDebtDateResTV);
                    layDebtDebtDateResLabel = findViewById(R.id.addDebtDateResLabel);

                    layDebtDebtSaveBtn = findViewById(R.id.addDebtSaveBtn);
                    layDebtDebtSaveBtn.setVisibility(View.GONE);
                    layDebtDebtUpdateBtn = findViewById(R.id.addDebtUpdateBtn);
                    layDebtDebtCancelBtn = findViewById(R.id.addDebtCancelBtn);

                    layDebtDebtDb = (AccountsDb) layDebtHldr.layDebtEditBtn.getTag();

                    debtNameFromTag = layDebtDebtDb.getAcctName();
                    debtAmtFromTag = layDebtDebtDb.getAcctBal();
                    debtLimitFromTag = layDebtDebtDb.getAcctMax();
                    debtRateFromTag = layDebtDebtDb.getAcctIntRate();
                    debtPaytFromTag = layDebtDebtDb.getAcctPaytsTo();
                    debtEndFromTag = layDebtDebtDb.getAcctEndDate();
                    debtIdFromTag = layDebtDebtDb.getId();

                    layDebtDebtNameET.setText(debtNameFromTag);

                    if (debtEndFromTag.equals(getString(R.string.debt_paid))) {
                        layDebtDebtDateResLabel.setVisibility(View.GONE);
                        layDebtDebtDateResTV.setTextColor(Color.parseColor("#5dbb63")); //light green
                    } else if (debtEndFromTag.equals(getString(R.string.too_far))) {
                        layDebtDebtDateResLabel.setVisibility(View.GONE);
                        layDebtDebtDateResTV.setTextColor(Color.parseColor("#ffff4444")); //red
                    } else {
                        layDebtDebtDateResLabel.setVisibility(View.VISIBLE);
                        layDebtDebtDateResTV.setTextColor(Color.parseColor("#303F9F")); //primary dark
                        layDebtDebtDateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }
                    layDebtDebtDateResTV.setText(debtEndFromTag);

                    layDebtGen.dblASCurrency(String.valueOf(debtLimitFromTag), layDebtDebtLimitET);
                    layDebtGen.dblASCurrency(String.valueOf(debtAmtFromTag), layDebtDebtAmtET);
                    layDebtDebtAmtET.addTextChangedListener(onChangeLayDebtDebtAmt);

                    layDebtDebtRate = debtRateFromTag / 100;
                    layDebtPerFor.setMinimumFractionDigits(2);
                    layDebtDebtRateS = layDebtPerFor.format(layDebtDebtRate);
                    layDebtDebtRateET.setText(layDebtDebtRateS);
                    layDebtDebtRateET.addTextChangedListener(onChangeLayDebtDebtRate);

                    layDebtGen.dblASCurrency(String.valueOf(debtPaytFromTag), layDebtDebtPaytET);
                    layDebtDebtPaytET.addTextChangedListener(onChangeLayDebtDebtPayt);

                    layDebtDebtUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (layDebtGen.stringFromET(layDebtDebtNameET) != null) {

                                debtLimitFromEntry = layDebtGen.dblFromET(layDebtDebtLimitET);
                                debtAmtFromEntry = layDebtGen.dblFromET(layDebtDebtAmtET);
                                debtRateFromEntry = layDebtGen.percentFromET(layDebtDebtRateET);
                                debtPaytFromEntry = layDebtGen.dblFromET(layDebtDebtPaytET);

                                layDebtDebtDb.setAcctName(layDebtGen.stringFromET(layDebtDebtNameET));
                                layDebtDebtDb.setAcctMax(debtLimitFromEntry);
                                layDebtDebtDb.setAcctBal(debtAmtFromEntry);
                                layDebtDebtDb.setAcctIntRate(debtRateFromEntry);
                                layDebtDebtDb.setAcctPaytsTo(debtPaytFromEntry);
                                layDebtDebtEndResult();
                                layDebtDebtDb.setAcctEndDate(layDebtDebtEnd2);

                                layDebtDbMgr.updateAccounts(layDebtDebtDb);

                                if (layDebtDbMgr.getTransfers().size() != 0) {
                                    layDebtDbMgr.updateRecReTransfer(debtIdFromTag, layDebtDbMgr.transfersToAcctThisYear(debtIdFromTag, layDebtGen.lastNumOfDays(365)), layDebtDbMgr.transfersFromAcctThisYear(debtIdFromTag, layDebtGen.lastNumOfDays(365)));
                                } else {
                                    layDebtDebtDb.setAcctAnnPaytsTo(debtPaytFromEntry * 12.0);
                                    layDebtDbMgr.updateAccounts(layDebtDebtDb);
                                }

                                layDebtHelper = new DbHelper(getContext());
                                layDebtDb = layDebtHelper.getWritableDatabase();

                                String[] args2 = new String[]{String.valueOf(layDebtDebtDb.getId())};

                                layDebtCV2 = new ContentValues();
                                layDebtCV3 = new ContentValues();

                                layDebtCV2.put(DbHelper.TRANSFROMACCTNAME, layDebtGen.stringFromET(layDebtDebtNameET));
                                layDebtCV3.put(DbHelper.TRANSTOACCTNAME, layDebtGen.stringFromET(layDebtDebtNameET));

                                try {
                                    layDebtDb.update(DbHelper.TRANSACTIONS_TABLE_NAME, layDebtCV2, DbHelper.TRANSFROMACCTID + "=?", args2);
                                    layDebtDb.update(DbHelper.TRANSACTIONS_TABLE_NAME, layDebtCV3, DbHelper.TRANSTOACCTID + "=?", args2);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                layDebtDb.close();

                                layDebtListAdapter.updateDebts(layDebtDbMgr.getDebts());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();

                                layDebtRefresh();
                            } else {
                                Toast.makeText(getBaseContext(), getString(R.string.no_blanks_warning), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    layDebtDebtCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layDebtRefresh();
                        }
                    });
                }
            });

            //click on trash can icon
            layDebtHldr.layDebtDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layDebtDebtDb = (AccountsDb) layDebtHldr.layDebtDelBtn.getTag();

                    layDebtDbMgr.deleteAccounts(layDebtDebtDb);
                    layDebtListAdapter.updateDebts(layDebtDbMgr.getDebts());
                    notifyDataSetChanged();

                    layDebtRefresh();
                }
            });

            return convertView;
        }
    }

    private static class LayDebtViewHolder {
        public TextView layDebttNameTV;
        public TextView layDebtAmtTV;
        public TextView layDebtFreeDateLabel;
        public TextView layDebtFreeDateTV;
        public TextView layDebtLabel2;
        public TextView layDebtOverLimitWarn;
        public ImageButton layDebtDelBtn;
        public ImageButton layDebtEditBtn;
    }

    View.OnClickListener onClickLayDebtAddDebtBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layDebtToAddMore = new Intent(LayoutDebt.this, AddDebts.class);
            layDebtToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layDebtToAddMore);
        }
    };

    View.OnClickListener onClickLayDebtDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layDebtToAnalysis = new Intent(LayoutDebt.this, SetUpAnalysis.class);
            layDebtToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(layDebtToAnalysis);
        }
    };
}
