package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
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

import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutMoneyInList extends MainNavigation {

    Button monInLstAddMoreBtn, monInLstDoneBtn, monInLstResetBtn;
    DbManager monInLstDbMgr;
    Double monInLstMonInAmt = 0.0;
    General monInLstGen;
    Intent monInLstToMain, monInLstToAddMonIn;
    LinearLayout monInLstSpinLayout;
    ListView monInLstList;
    MonInLstAdapter monInLstAdapter;
    TextView monInLstTitle, monInLstTotalTV;

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

        monInLstDbMgr = new DbManager(this);
        monInLstGen = new General();

        monInLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        monInLstSpinLayout.setVisibility(View.GONE);
        monInLstResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monInLstResetBtn.setVisibility(View.GONE);
        monInLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monInLstAddMoreBtn.setText(getString(R.string.record_deposits));
        monInLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monInLstTitle.setText(getString(R.string.deposits));
        monInLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monInLstDoneBtn.setOnClickListener(onClickMonInLstDoneBtn);
        monInLstTotalTV = findViewById(R.id.layout1TotalTV);
        monInLstTotalTV.setVisibility(View.GONE);
        monInLstList = findViewById(R.id.layout1ListView);

        monInLstAddMoreBtn.setOnClickListener(onClickMonInLstAddMoreBtn);

        monInLstAdapter = new MonInLstAdapter(this, monInLstDbMgr.getMoneyIns());
        monInLstList.setAdapter(monInLstAdapter);
    }

    View.OnClickListener onClickMonInLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstToMain = new Intent(LayoutMoneyInList.this, MainActivity.class);
            monInLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToMain);
        }
    };

    View.OnClickListener onClickMonInLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstToAddMonIn = new Intent(LayoutMoneyInList.this, LayoutMoneyIn.class);
            monInLstToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToAddMonIn);
        }
    };

    public class MonInLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> moneyIns;

        private MonInLstAdapter(
                Context context,
                List<TransactionsDb> moneyIns) {

            super(context, -1, moneyIns);

            this.context = context;
            this.moneyIns = moneyIns;
        }

        public void updateMoneyIns(List<TransactionsDb> moneyIns) {
            this.moneyIns = moneyIns;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIns.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyIn2ViewHolder monInLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monInLstHldr = new MoneyIn2ViewHolder();
                monInLstHldr.monInLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monInLstHldr.monInLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                monInLstHldr.monInLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monInLstHldr.monInLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monInLstHldr.monInLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monInLstHldr.monInLstAcctLabel = convertView.findViewById(R.id.list5CCLabel);
                monInLstHldr.monInLstAcctLabel.setText(R.string.deposited_to);
                monInLstHldr.monInLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monInLstHldr.monInLstAcctLabel2 = convertView.findViewById(R.id.list5CCLabel2);
                monInLstHldr.monInLstAcctLabel2.setVisibility(View.GONE);
                monInLstHldr.monInLstAcctTV2 = convertView.findViewById(R.id.list5CCTV2);
                monInLstHldr.monInLstAcctTV2.setVisibility(View.GONE);
                monInLstHldr.monInLstEditBtn.setVisibility(View.GONE);
                monInLstHldr.monInLstDelBtn.setVisibility(View.GONE);

                convertView.setTag(monInLstHldr);

            } else {
                monInLstHldr = (MoneyIn2ViewHolder) convertView.getTag();
            }

            monInLstHldr.monInLstDateTV.setText(moneyIns.get(position).getTransCreatedOn());

            monInLstMonInAmt = moneyIns.get(position).getTransAmt();
            monInLstGen.dblASCurrency(String.valueOf(monInLstMonInAmt), monInLstHldr.monInLstAmtTV);

            monInLstHldr.monInLstCatTV.setText(moneyIns.get(position).getTransBdgtCat());
            monInLstHldr.monInLstAcctTV.setText(moneyIns.get(position).getTransToAcctName());

            return convertView;
        }
    }

    private static class MoneyIn2ViewHolder {
        public TextView monInLstCatTV;
        public TextView monInLstAmtTV;
        public TextView monInLstDateTV;
        public TextView monInLstAcctLabel;
        public TextView monInLstAcctTV;
        public TextView monInLstAcctLabel2;
        public TextView monInLstAcctTV2;
        public ImageButton monInLstEditBtn;
        public ImageButton monInLstDelBtn;
    }
}
