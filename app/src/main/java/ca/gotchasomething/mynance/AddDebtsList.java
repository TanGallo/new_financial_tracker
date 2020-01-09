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

public class AddDebtsList extends MainNavigation {

    Button debtLstAddMoreBtn, debtLstDoneBtn, debtLstResetBtn;
    ContentValues debtLstCV;
    DbHelper debtLstHelper;
    DbManager debtLstDBMgr;
    DebtLstListAdapter debtLstLstAdapter;
    General debtLstGen;
    Intent debtLstToLayoutDebt, debtLstToDaiMonCCPur, debtLstToSetUp, debtLstToAddMore;
    LinearLayout debtLstSpinLayout;
    ListView debtLstListView;
    SQLiteDatabase debtLstDB;
    String debtLstDebtAmt2 = null, debtLstDebtEnd = null;
    TextView debtLstHeaderLabelTV, debtLstTotalTV;

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
        debtLstResetBtn = findViewById(R.id.layout1SpinResetBtn);
        debtLstResetBtn.setVisibility(View.GONE);

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
            if (debtLstDBMgr.retrieveLatestDone().equals("bills")) {
                debtLstCV = new ContentValues();
                debtLstCV.put(DbHelper.LATESTDONE, "debts");
                debtLstHelper = new DbHelper(getApplicationContext());
                debtLstDB = debtLstHelper.getWritableDatabase();
                debtLstDB.update(DbHelper.SET_UP_TABLE_NAME, debtLstCV, DbHelper.ID + "= '1'", null);
                debtLstDB.close();

                debtLstToSetUp = new Intent(AddDebtsList.this, LayoutSetUp.class);
                debtLstToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToSetUp);
            } else if (debtLstDBMgr.retrieveLastPageId() == 11) {
                debtLstToLayoutDebt = new Intent(AddDebtsList.this, LayoutDebt.class);
                debtLstToLayoutDebt.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToLayoutDebt);
            } else if (debtLstDBMgr.retrieveLastPageId() == 7) {
                debtLstToDaiMonCCPur = new Intent(AddDebtsList.this, LayoutCCPur.class);
                debtLstToDaiMonCCPur.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(debtLstToDaiMonCCPur);
            }
        }
    };

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
                debtLstHldr.debtLstDel.setVisibility(View.GONE);
                debtLstHldr.debtLstEdit.setVisibility(View.GONE);
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
