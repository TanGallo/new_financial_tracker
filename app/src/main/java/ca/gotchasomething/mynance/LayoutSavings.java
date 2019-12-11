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
import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;

public class LayoutSavings extends MainNavigation {

    AccountsDb laySavSavDb;
    Button laySavCancelBtn, laySavDoneBtn, laySavSaveBtn, laySavUpdateBtn;
    ContentValues laySavCV, laySavCV2, laySavCV3;
    DbHelper laySavHelper, laySavHelper3;
    DbManager laySavDbMgr;
    Double laySavSavRate = 0.0, laySavTotSav = 0.0, savAmtFromEntry = 0.0, savAmtFromTag = 0.0, savGoalFromEntry = 0.0, savGoalFromTag = 0.0,
            savPaytFromEntry = 0.0, savPaytFromTag = 0.0, savRateFromEntry = 0.0, savRateFromTag = 0.0;
    EditText laySavSavAmtET, laySavSavGoalET, laySavSavNameET, laySavSavPaytET, laySavSavPercentET;
    FloatingActionButton laySavAddMoreBtn;
    General laySavGen;
    Intent laySavToAnalysis, laySavToAddMore, laySavRefresh;
    ListView laySavListView;
    long id, savIdFromTag;
    NumberFormat laySavPerFor = NumberFormat.getPercentInstance();
    LaySavListAdapter laySavListAdapter;
    SQLiteDatabase laySavDb, laySavDb3;
    String laySavSavDate = null, laySavSavDate2 = null, laySavSavRateS = null, savDateFromTag = null, savNameFromEntry = null, savNameFromTag = null;
    TextView laySavDateHeaderLabel, laySavDateHeaderTV, laySavSavDateResLabel, laySavSavDateResTV, laySavTotAmtHeaderLabel, laySavTotAmtHeaderTV;

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

        laySavGen = new General();
        laySavDbMgr = new DbManager(this);

        laySavTotAmtHeaderLabel = findViewById(R.id.c4HeaderLabel);
        laySavTotAmtHeaderLabel.setText(getString(R.string.total_saved));
        laySavTotAmtHeaderTV = findViewById(R.id.c4HeaderTV);
        laySavDateHeaderLabel = findViewById(R.id.c4HeaderLabel2);
        laySavDateHeaderLabel.setVisibility(View.GONE);
        laySavDateHeaderTV = findViewById(R.id.c4HeaderTV2);
        laySavDateHeaderTV.setVisibility(View.GONE);
        laySavAddMoreBtn = findViewById(R.id.c4AddMoreBtn);
        laySavDoneBtn = findViewById(R.id.c4DoneBtn);
        if(laySavDbMgr.retrieveLatestDone().equals("savings")) {
            laySavDoneBtn.setVisibility(View.VISIBLE);
            laySavDoneBtn.setOnClickListener(onClickLaySavDoneBtn);
        } else {
            laySavDoneBtn.setVisibility(View.GONE);
        }

        laySavAddMoreBtn.setOnClickListener(onClickLaySavAddMoreBtn);

        laySavListView = findViewById(R.id.c4ListView);
        laySavListAdapter = new LaySavListAdapter(this, laySavDbMgr.getSavings());
        laySavListView.setAdapter(laySavListAdapter);

        laySavHeaderText();

        laySavCV = new ContentValues();
        laySavCV.put(DbHelper.LASTPAGEID, 6);
        laySavHelper3 = new DbHelper(getApplicationContext());
        laySavDb3 = laySavHelper3.getWritableDatabase();
        laySavDb3.update(DbHelper.CURRENT_TABLE_NAME, laySavCV, DbHelper.ID + "= '1'", null);
        laySavDb3.close();
    }

    public void laySavHeaderText() {
        laySavTotSav = laySavDbMgr.sumTotalSavings();
        laySavGen.dblASCurrency(String.valueOf(laySavTotSav), laySavTotAmtHeaderTV);
    }

    public void laySavRefresh() {
        laySavRefresh = new Intent(this, LayoutSavings.class);
        laySavRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(laySavRefresh);
    }

    TextWatcher onChangeLaySavSavAmt = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
            savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
            savRateFromEntry = laySavGen.percentFromET(laySavSavPercentET);
            savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);
            laySavDateResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeLaySavSavGoal = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
            savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
            savRateFromEntry = laySavGen.percentFromET(laySavSavPercentET);
            savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);
            laySavDateResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeLaySavSavPayt = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
            savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
            savRateFromEntry = laySavGen.percentFromET(laySavSavPercentET);
            savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);
            laySavDateResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher onChangeLaySavSavRate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
            savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
            savRateFromEntry = laySavGen.percentFromET(laySavSavPercentET);
            savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);
            laySavDateResult();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void laySavDateResult() {
        laySavSavDate2 = laySavGen.calcSavingsDate(
                savGoalFromEntry,
                savAmtFromEntry,
                savRateFromEntry,
                savPaytFromEntry,
                getString(R.string.goal_achieved),
                getString(R.string.too_far));
        if (laySavSavDate2.equals(getString(R.string.goal_achieved))) {
            laySavSavDateResLabel.setVisibility(View.GONE);
            laySavSavDateResTV.setTextColor(Color.parseColor("#03ac13"));
        } else if (laySavSavDate2.equals(getString(R.string.too_far))) {
            laySavSavDateResLabel.setVisibility(View.GONE);
            laySavSavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
        } else {
            laySavSavDateResLabel.setVisibility(View.VISIBLE);
            laySavSavDateResTV.setTextColor(Color.parseColor("#303F9F"));
            laySavSavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
        }

        laySavSavDateResTV.setText(laySavSavDate2);
    }

    public class LaySavListAdapter extends ArrayAdapter<AccountsDb> {

        private Context context;
        private List<AccountsDb> savings;

        private LaySavListAdapter(
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

            final LaySavViewHolder laySavHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_7_3_lines,
                        parent, false);

                laySavHldr = new LaySavViewHolder();
                laySavHldr.laySavNameTV = convertView.findViewById(R.id.bigLstTV1);
                laySavHldr.laySavGoalTV = convertView.findViewById(R.id.bigLstTV2);
                laySavHldr.laySavDateLabel = convertView.findViewById(R.id.bigLstLabel);
                laySavHldr.laySavDateLabel.setText(getString(R.string.goal_will));
                laySavHldr.laySavDateTV = convertView.findViewById(R.id.bigLstTV3);
                laySavHldr.laySavCurrBalLabel = convertView.findViewById(R.id.bigLstLabel2);
                laySavHldr.laySavCurrBalLabel.setText(getString(R.string.current_balance));
                laySavHldr.laySavCurrBalTV = convertView.findViewById(R.id.bigLstTV4);
                laySavHldr.laySavDelBtn = convertView.findViewById(R.id.bigLstDelBtn);
                laySavHldr.laySavEditBtn = convertView.findViewById(R.id.bigLstEditBtn);
                convertView.setTag(laySavHldr);

            } else {
                laySavHldr = (LaySavViewHolder) convertView.getTag();
            }

            //retrieve savingsName
            laySavHldr.laySavNameTV.setText(savings.get(position).getAcctName());

            laySavGen.dblASCurrency(String.valueOf(savings.get(position).getAcctMax()), laySavHldr.laySavGoalTV);

            //retrieve savingsDate
            laySavSavDate = savings.get(position).getAcctEndDate();
            laySavHldr.laySavDateTV.setText(laySavSavDate);
            if (laySavSavDate.contains("2")) {
                laySavHldr.laySavDateLabel.setVisibility(View.VISIBLE);
            } else {
                laySavHldr.laySavDateLabel.setVisibility(View.GONE);
            }
            if (laySavSavDate.equals(getString(R.string.goal_achieved))) {
                laySavHldr.laySavDateTV.setTextColor(Color.parseColor("#03ac13"));
            } else if (laySavSavDate.equals(getString(R.string.too_far))) {
                laySavHldr.laySavDateTV.setTextColor(Color.parseColor("#ffff4444"));
            } else {
                laySavHldr.laySavDateTV.setTextColor(Color.parseColor("#303F9F"));
                laySavHldr.laySavDateLabel.setTextColor(Color.parseColor("#303F9F"));
            }

            //retrieve savingsAmount & format as currency
            laySavGen.dblASCurrency(String.valueOf(savings.get(position).getAcctBal()), laySavHldr.laySavCurrBalTV);

            laySavHldr.laySavDelBtn.setTag(savings.get(position));
            laySavHldr.laySavEditBtn.setTag(savings.get(position));

            //click on pencil icon
            laySavHldr.laySavEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.form_4_add_savings);
                    LayoutSavings.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    laySavDbMgr = new DbManager(getApplicationContext());

                    laySavSavNameET = findViewById(R.id.addSavNameET);
                    laySavSavAmtET = findViewById(R.id.addSavAmtET);
                    laySavSavGoalET = findViewById(R.id.addSavGoalET);
                    laySavSavPaytET = findViewById(R.id.addSavPaytET);
                    laySavSavPercentET = findViewById(R.id.addSavPercentET);
                    laySavSavDateResTV = findViewById(R.id.addSavDateResTV);
                    laySavSavDateResLabel = findViewById(R.id.addSavDateResLabel);

                    laySavSaveBtn = findViewById(R.id.addSavSaveBtn);
                    laySavSaveBtn.setVisibility(View.GONE);
                    laySavUpdateBtn = findViewById(R.id.addSavUpdateBtn);
                    laySavCancelBtn = findViewById(R.id.addSavCancelBtn);

                    laySavSavDb = (AccountsDb) laySavHldr.laySavEditBtn.getTag();

                    savNameFromTag = laySavSavDb.getAcctName();
                    savAmtFromTag = laySavSavDb.getAcctBal();
                    savGoalFromTag = laySavSavDb.getAcctMax();
                    savPaytFromTag = laySavSavDb.getAcctPaytsTo();
                    savRateFromTag = laySavSavDb.getAcctIntRate();
                    savDateFromTag = laySavSavDb.getAcctEndDate();
                    savIdFromTag = laySavSavDb.getId();

                    laySavSavNameET.setText(savNameFromTag);

                    laySavSavDate = savDateFromTag;
                    laySavSavDateResTV.setText(laySavSavDate);
                    if (laySavSavDate.equals(getString(R.string.goal_achieved))) {
                        laySavSavDateResLabel.setVisibility(View.GONE);
                        laySavSavDateResTV.setTextColor(Color.parseColor("#03ac13"));
                    } else if (laySavSavDate.equals(getString(R.string.too_far))) {
                        laySavSavDateResLabel.setVisibility(View.GONE);
                        laySavSavDateResTV.setTextColor(Color.parseColor("#ffff4444"));
                    } else {
                        laySavSavDateResLabel.setVisibility(View.VISIBLE);
                        laySavSavDateResTV.setTextColor(Color.parseColor("#303F9F"));
                        laySavSavDateResLabel.setTextColor(Color.parseColor("#303F9F"));
                    }

                    laySavGen.dblASCurrency(String.valueOf(savAmtFromTag), laySavSavAmtET);
                    laySavSavAmtET.addTextChangedListener(onChangeLaySavSavAmt);

                    laySavGen.dblASCurrency(String.valueOf(savGoalFromTag), laySavSavGoalET);
                    laySavSavGoalET.addTextChangedListener(onChangeLaySavSavGoal);

                    laySavGen.dblASCurrency(String.valueOf(savPaytFromTag), laySavSavPaytET);
                    laySavSavPaytET.addTextChangedListener(onChangeLaySavSavPayt);

                    laySavSavRate = savRateFromTag / 100;
                    laySavPerFor.setMinimumFractionDigits(2);
                    laySavSavRateS = laySavPerFor.format(laySavSavRate);
                    laySavSavPercentET.setText(laySavSavRateS);
                    laySavSavPercentET.addTextChangedListener(onChangeLaySavSavRate);

                    laySavUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (laySavGen.stringFromET(laySavSavNameET) != "null") {

                                savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
                                savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
                                savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);
                                savRateFromEntry = laySavGen.percentFromET(laySavSavPercentET);

                                laySavSavDb.setAcctName(laySavGen.stringFromET(laySavSavNameET));
                                laySavSavDb.setAcctBal(savAmtFromEntry);
                                laySavSavDb.setAcctMax(savGoalFromEntry);
                                laySavSavDb.setAcctPaytsTo(savPaytFromEntry);
                                laySavSavDb.setAcctIntRate(savRateFromEntry);
                                laySavDateResult();
                                laySavSavDb.setAcctEndDate(laySavSavDate2);

                                laySavDbMgr.updateAccounts(laySavSavDb);

                                if(laySavDbMgr.getTransfers().size() != 0) {
                                    laySavDbMgr.updateRecReTransfer(savIdFromTag, laySavDbMgr.transfersToAcctThisYear(savIdFromTag, laySavGen.lastNumOfDays(365)), laySavDbMgr.transfersFromAcctThisYear(savIdFromTag, laySavGen.lastNumOfDays(365)));
                                } else {
                                    laySavSavDb.setAcctAnnPaytsTo(savPaytFromEntry * 12.0);
                                    laySavDbMgr.updateAccounts(laySavSavDb);
                                }

                                laySavHelper = new DbHelper(getContext());
                                laySavDb = laySavHelper.getWritableDatabase();

                                String[] args = new String[]{String.valueOf(laySavSavDb.getId())};

                                laySavCV2 = new ContentValues();
                                laySavCV3 = new ContentValues();

                                laySavCV2.put(DbHelper.TRANSFROMACCTNAME, laySavGen.stringFromET(laySavSavNameET));
                                laySavCV3.put(DbHelper.TRANSTOACCTNAME, laySavGen.stringFromET(laySavSavNameET));

                                try {
                                    laySavDb.update(DbHelper.TRANSACTIONS_TABLE_NAME, laySavCV2, DbHelper.TRANSFROMACCTID + "=?", args);
                                    laySavDb.update(DbHelper.TRANSACTIONS_TABLE_NAME, laySavCV3, DbHelper.TRANSTOACCTID + "=?", args);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                laySavDb.close();

                                laySavListAdapter.updateSavings(laySavDbMgr.getSavings());
                                notifyDataSetChanged();

                                Toast.makeText(getBaseContext(), R.string.changes_saved, Toast.LENGTH_LONG).show();

                                laySavRefresh();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    laySavCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            laySavRefresh();
                        }
                    });
                }
            });

            //click on trash can icon
            laySavHldr.laySavDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    laySavSavDb = (AccountsDb) laySavHldr.laySavDelBtn.getTag();

                    laySavDbMgr.deleteAccounts(laySavSavDb);
                    laySavListAdapter.updateSavings(laySavDbMgr.getSavings());
                    notifyDataSetChanged();

                    laySavRefresh();
                }
            });

            return convertView;
        }
    }

    private static class LaySavViewHolder {
        public TextView laySavNameTV;
        public TextView laySavGoalTV;
        public TextView laySavDateTV;
        public TextView laySavDateLabel;
        public TextView laySavCurrBalLabel;
        public TextView laySavCurrBalTV;
        public ImageButton laySavDelBtn;
        public ImageButton laySavEditBtn;
    }

    View.OnClickListener onClickLaySavAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySavToAddMore = new Intent(LayoutSavings.this, AddSavings.class);
            laySavToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(laySavToAddMore);
        }
    };

    View.OnClickListener onClickLaySavDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySavToAnalysis = new Intent(LayoutSavings.this, SetUpAnalysis.class);
            laySavToAnalysis.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(laySavToAnalysis);
        }
    };
}
