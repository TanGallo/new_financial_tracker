package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

import ca.gotchasomething.mynance.data.TransactionsDb;

public class LayoutTransfersList extends MainNavigation {

    Button trn2AddMoreBtn,
            trn2DoneBtn,
            trn2ResetBtn;
    DbManager trn2DbMgr;
    Double trn2Amt = 0.0;
    General trn2Gen;
    Intent trn2ToMain,
            trn2ToAddMonIn;
    LinearLayout trn2SpinLayout;
    ListView trn2List;
    Trn2Adapter trn2Adapter;
    TextView trn2Title,
            trn2TotalTV;

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

        trn2DbMgr = new DbManager(this);
        trn2Gen = new General();

        trn2SpinLayout = findViewById(R.id.layout1SpinLayout);
        trn2SpinLayout.setVisibility(View.GONE);
        trn2ResetBtn = findViewById(R.id.layout1SpinResetBtn);
        trn2ResetBtn.setVisibility(View.GONE);
        trn2Title = findViewById(R.id.layout1HeaderLabelTV);
        trn2Title.setText(getString(R.string.transfers));
        trn2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        trn2AddMoreBtn.setText(getString(R.string.record_transfers));
        trn2DoneBtn = findViewById(R.id.layout1DoneBtn);
        trn2DoneBtn.setOnClickListener(onClickTrn2DoneBtn);
        trn2TotalTV = findViewById(R.id.layout1TotalTV);
        trn2TotalTV.setVisibility(View.GONE);
        trn2List = findViewById(R.id.layout1ListView);

        trn2AddMoreBtn.setOnClickListener(onClickTrn2AddMoreBtn);

        trn2Adapter = new Trn2Adapter(this, trn2DbMgr.getTransfers());
        trn2List.setAdapter(trn2Adapter);
    }

    View.OnClickListener onClickTrn2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToMain = new Intent(LayoutTransfersList.this, MainActivity.class);
            trn2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToMain);
        }
    };

    View.OnClickListener onClickTrn2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToAddMonIn = new Intent(LayoutTransfersList.this, LayoutTransfers.class);
            trn2ToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToAddMonIn);
        }
    };

    public class Trn2Adapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> transfers;

        private Trn2Adapter(
                Context context,
                List<TransactionsDb> transfers) {

            super(context, -1, transfers);

            this.context = context;
            this.transfers = transfers;
        }

        public void updateTransfers(List<TransactionsDb> transfers) {
            this.transfers = transfers;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transfers.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Trans2ViewHolder trn2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_8_5tv,
                        parent, false);

                trn2Hldr = new Trans2ViewHolder();
                trn2Hldr.trn2DateTV = convertView.findViewById(R.id.list8TV1);
                trn2Hldr.trn2AmtTV = convertView.findViewById(R.id.list8TV2);
                trn2Hldr.trn2FromNameTV = convertView.findViewById(R.id.list8TV4);
                trn2Hldr.trn2ToNameTV = convertView.findViewById(R.id.list8TV6);
                convertView.setTag(trn2Hldr);

            } else {
                trn2Hldr = (Trans2ViewHolder) convertView.getTag();
            }

            trn2Hldr.trn2DateTV.setText(transfers.get(position).getTransCreatedOn());
            trn2Amt = transfers.get(position).getTransAmt();
            trn2Gen.dblASCurrency(String.valueOf(trn2Amt), trn2Hldr.trn2AmtTV);
            trn2Hldr.trn2FromNameTV.setText(transfers.get(position).getTransFromAcctName());
            trn2Hldr.trn2ToNameTV.setText(transfers.get(position).getTransToAcctName());

            return convertView;
        }
    }

    private static class Trans2ViewHolder {

        public TextView trn2DateTV;
        public TextView trn2AmtTV;
        public TextView trn2FromNameTV;
        public TextView trn2ToNameTV;
    }

};
