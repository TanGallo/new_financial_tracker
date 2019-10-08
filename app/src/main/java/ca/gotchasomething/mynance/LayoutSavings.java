package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

//import ca.gotchasomething.mynance.data.SavingsDb;

public class LayoutSavings extends MainNavigation {

    AccountsDb laySavSavDb;
    Button laySavCancelBtn, laySavSaveBtn, laySavUpdateBtn;
    ContentValues laySavCV, laySavCV2;
    DbHelper laySavHelper, laySavHelper2, laySavHelper3;
    DbManager laySavDbMgr;
    Double currentSavingsRate = 0.0, laySavSavRate = 0.0, laySavTotSav = 0.0, laySavTransToSavThisYr = 0.0, laySavTransFromSavThisYr = 0.0,
            savAmtFromEntry = 0.0, savAmtFromTag = 0.0, savGoalFromEntry = 0.0, savGoalFromTag = 0.0,
            savPaytFromEntry = 0.0, savPaytFromTag = 0.0, savRateFromEntry = 0.0, savRateFromTag = 0.0;
    EditText laySavSavAmtET, laySavSavGoalET, laySavSavNameET, laySavSavPaytET, laySavSavPercentET, laySavSavRateET;
    FloatingActionButton laySavAddMoreBtn;
    General laySavGen;
    Intent laySavToAddMore, laySavRefresh;
    ListView laySavListView;
    long id, savIdFromTag;
    NumberFormat laySavPerFor = NumberFormat.getPercentInstance();
    LaySavListAdapter laySavListAdapter;
    SQLiteDatabase laySavDb, laySavDb2, laySavDb3;
    String laySavSavDate = null, laySavSavDate2 = null, laySavSavRateS = null, savDateFromTag = null, savNameFromEntry = null, savNameFromTag = null;
    TextView laySavDateHeaderLabel, laySavDateHeaderTV, laySavSavDateResLabel, laySavSavDateResTV, laySavTotAmtHeaderLabel, laySavTotAmtHeaderTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c4_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            laySavDateResult();
            laySavSavDateResTV.setText(laySavSavDate2);
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
            laySavDateResult();
            laySavSavDateResTV.setText(laySavSavDate2);
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
            laySavDateResult();
            laySavSavDateResTV.setText(laySavSavDate2);
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
            laySavDateResult();
            laySavSavDateResTV.setText(laySavSavDate2);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void laySavDateResult() {
        savNameFromEntry = laySavGen.stringFromET(laySavSavNameET);
        savAmtFromEntry = laySavGen.dblFromET(laySavSavAmtET);
        savGoalFromEntry = laySavGen.dblFromET(laySavSavGoalET);
        savRateFromEntry = laySavGen.percentFromET(laySavSavRateET);
        savPaytFromEntry = laySavGen.dblFromET(laySavSavPaytET);

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
            laySavSavDate = savings.get(position).getEndDate();
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
                    savPaytFromTag = laySavSavDb.getPaytsTo();
                    savRateFromTag = laySavSavDb.getIntRate();
                    savDateFromTag = laySavSavDb.getEndDate();
                    savIdFromTag = laySavSavDb.getId();

                    laySavSavNameET.setText(savNameFromTag);

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

                    laySavUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            laySavDateResult();

                            if (savNameFromEntry != "null") {

                                laySavSavDb.setAcctName(savNameFromEntry);
                                laySavSavDb.setAcctBal(savAmtFromEntry);
                                laySavSavDb.setAcctMax(savGoalFromEntry);
                                laySavSavDb.setPaytsTo(savPaytFromEntry);
                                laySavSavDb.setIntRate(savRateFromEntry);
                                laySavSavDb.setEndDate(laySavSavDate2);

                                laySavDbMgr.updateAccounts(laySavSavDb);

                                if(laySavDbMgr.getTransfers().size() != 0) {
                                    //laySavTransToSavThisYr = laySavDbMgr.transfersToSavThisYear(savIdFromTag);
                                    //laySavTransFromSavThisYr = laySavDbMgr.transfersFromSavThisYear(savIdFromTag);
                                    laySavDbMgr.updateSavRecReTransfer(savIdFromTag, laySavDbMgr.transfersToSavThisYear(savIdFromTag, laySavGen.lastNumOfDays(365)), laySavDbMgr.transfersFromSavThisYear(savIdFromTag, laySavGen.lastNumOfDays(365)));
                                } else {
                                    laySavSavDb.setAnnPaytsTo(savPaytFromEntry * 12.0);
                                    laySavDbMgr.updateAccounts(laySavSavDb);
                                }

                                /*laySavHelper = new DbHelper(getContext());
                                laySavDb = laySavHelper.getWritableDatabase();

                                String[] args = new String[]{String.valueOf(laySavSavDb.getId())};

                                laySavCV2 = new ContentValues();

                                laySavCV2.put(DbHelper.ACCTNAME, savNameFromEntry);

                                try {
                                    laySavDb.update(DbHelper.ACCOUNTS_TABLE_NAME, laySavCV2, DbHelper.SAVID + "=?", args);
                                } catch (CursorIndexOutOfBoundsException | SQLException e) {
                                    e.printStackTrace();
                                }

                                laySavDb.close();*/

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

                    /*laySavHelper2 = new DbHelper(getContext());
                    laySavDb2 = laySavHelper2.getWritableDatabase();

                    try {
                        String[] args3 = new String[]{String.valueOf(laySavSavDb.getId())};
                        laySavDb2.delete(DbHelper.ACCOUNTS_TABLE_NAME, DbHelper.SAVID + "=?", args3);
                    } catch (CursorIndexOutOfBoundsException e4) {
                        e4.printStackTrace();
                    }
                    laySavDb2.close();*/

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
        ImageButton laySavDelBtn;
        ImageButton laySavEditBtn;
    }

    View.OnClickListener onClickLaySavAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            laySavToAddMore = new Intent(LayoutSavings.this, AddSavings.class);
            laySavToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(laySavToAddMore);
        }
    };
}
