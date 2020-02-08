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

public class LayoutMoneyOutList extends MainNavigation {

    Button monOutLstAddMoreBtn,
            monOutLstDoneBtn,
            monOutLstResetBtn;
    DbManager monOutLstDbMgr;
    Double monOutLstMonOutAmt = 0.0;
    General monOutLstGen;
    Intent monOutLstToMain,
            monOutLstToAddMonOut;
    LinearLayout monOutLstSpinLayout;
    ListView monOutLstList;
    MonOutLstAdapter monOutLstAdapter;
    TextView monOutLstTitle,
            monOutLstTotalTV;

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

        monOutLstDbMgr = new DbManager(this);
        monOutLstGen = new General();

        monOutLstSpinLayout = findViewById(R.id.layout1SpinLayout);
        monOutLstSpinLayout.setVisibility(View.GONE);
        monOutLstResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monOutLstResetBtn.setVisibility(View.GONE);
        monOutLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monOutLstTitle.setText(getString(R.string.cash_debit_transactions));
        monOutLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monOutLstAddMoreBtn.setText(getString(R.string.record_cash_debit));
        monOutLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monOutLstDoneBtn.setOnClickListener(onClickMonOutLstDoneBtn);
        monOutLstTotalTV = findViewById(R.id.layout1TotalTV);
        monOutLstTotalTV.setVisibility(View.GONE);
        monOutLstList = findViewById(R.id.layout1ListView);

        monOutLstAddMoreBtn.setOnClickListener(onClickMonOutLstAddMoreBtn);

        monOutLstAdapter = new MonOutLstAdapter(this, monOutLstDbMgr.getCashTrans());
        monOutLstList.setAdapter(monOutLstAdapter);
    }

    View.OnClickListener onClickMonOutLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToMain = new Intent(LayoutMoneyOutList.this, MainActivity.class);
            monOutLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutLstToMain);
        }
    };

    View.OnClickListener onClickMonOutLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToAddMonOut = new Intent(LayoutMoneyOutList.this, LayoutMoneyOut.class);
            monOutLstToAddMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutLstToAddMonOut);
        }
    };

    public class MonOutLstAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> moneyOuts;

        private MonOutLstAdapter(
                Context context,
                List<TransactionsDb> moneyOuts) {

            super(context, -1, moneyOuts);

            this.context = context;
            this.moneyOuts = moneyOuts;
        }

        public void updateMoneyOuts(List<TransactionsDb> moneyOuts) {
            this.moneyOuts = moneyOuts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyOuts.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyOut2ViewHolder monOutLstHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monOutLstHldr = new MoneyOut2ViewHolder();
                monOutLstHldr.monOutLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monOutLstHldr.monOutLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                monOutLstHldr.monOutLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monOutLstHldr.monOutLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monOutLstHldr.monOutLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monOutLstHldr.monOutLstAcctLabel = convertView.findViewById(R.id.list5CCLabel);
                monOutLstHldr.monOutLstAcctLabel.setText(R.string.paid_from);
                monOutLstHldr.monOutLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monOutLstHldr.monOutLstAcctLabel2 = convertView.findViewById(R.id.list5CCLabel2);
                monOutLstHldr.monOutLstAcctLabel2.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctTV2 = convertView.findViewById(R.id.list5CCTV2);
                monOutLstHldr.monOutLstAcctTV2.setVisibility(View.GONE);
                monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);

                convertView.setTag(monOutLstHldr);

            } else {
                monOutLstHldr = (MoneyOut2ViewHolder) convertView.getTag();
            }

            monOutLstHldr.monOutLstDateTV.setText(moneyOuts.get(position).getTransCreatedOn());

            monOutLstMonOutAmt = moneyOuts.get(position).getTransAmt();
            monOutLstGen.dblASCurrency(String.valueOf(monOutLstMonOutAmt), monOutLstHldr.monOutLstAmtTV);

            monOutLstHldr.monOutLstCatTV.setText(moneyOuts.get(position).getTransBdgtCat());
            monOutLstHldr.monOutLstAcctTV.setText(moneyOuts.get(position).getTransFromAcctName());

            return convertView;
        }
    }

    private static class MoneyOut2ViewHolder {
        public TextView monOutLstCatTV;
        public TextView monOutLstAmtTV;
        public TextView monOutLstDateTV;
        public TextView monOutLstAcctLabel;
        public TextView monOutLstAcctTV;
        public TextView monOutLstAcctLabel2;
        public TextView monOutLstAcctTV2;
        public ImageButton monOutLstEditBtn;
        public ImageButton monOutLstDelBtn;
    }
}
