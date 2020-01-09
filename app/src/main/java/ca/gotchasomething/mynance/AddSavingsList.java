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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

import ca.gotchasomething.mynance.data.AccountsDb;

public class AddSavingsList extends MainNavigation {

    Button savLstAddMoreBtn, savLstDoneBtn, savLstResetBtn;
    ContentValues savLstCV;
    DbHelper savLstHelper;
    DbManager savLstDBMgr;
    Double savLstGoalAmt2 = 0.0, savLstSavCurr = 0.0;
    SavLstListAdapter savLstLstAdapter;
    General savLstGen;
    Intent savLstToLayoutSavings, savLstToSetUp, savLstToAddMore, savLstToAnalysis;
    LinearLayout savLstSpinLayout;
    ListView savLstListView;
    SQLiteDatabase savLstDB;
    String savLstSavDate = null;
    TextView savLstHeaderLabelTV, savLstTotalTV;

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
        savLstResetBtn = findViewById(R.id.layout1SpinResetBtn);
        savLstResetBtn.setVisibility(View.GONE);

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
                savLstHldr.savLstDel.setVisibility(View.GONE);
                savLstHldr.savLstEdit.setVisibility(View.GONE);
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
