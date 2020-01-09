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

public class LayoutCCPurList extends MainNavigation {

    Button monCC2AddMoreBtn, monCC2DoneBtn, monCC2ResetBtn;
    DbManager monCC2DbMgr;
    Double monCC2MonOutAmt = 0.0;
    General monCC2Gen;
    Intent monCC2ToMain, monCC2ToAddCCTrans, monCC2Refresh;
    LinearLayout monCC2SpinLayout;
    ListView monCC2List;
    MonCC2Adapter monCC2Adapter;
    TextView monCC2Title, monCC2TotalTV;

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

        monCC2DbMgr = new DbManager(this);
        monCC2Gen = new General();

        monCC2SpinLayout = findViewById(R.id.layout1SpinLayout);
        monCC2SpinLayout.setVisibility(View.GONE);
        monCC2ResetBtn = findViewById(R.id.layout1SpinResetBtn);
        monCC2ResetBtn.setVisibility(View.GONE);
        monCC2Title = findViewById(R.id.layout1HeaderLabelTV);
        monCC2Title.setText(getString(R.string.credit_card_purchases_2));
        monCC2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monCC2AddMoreBtn.setText(getString(R.string.record_credit_card));
        monCC2DoneBtn = findViewById(R.id.layout1DoneBtn);
        monCC2DoneBtn.setOnClickListener(onClickMonCC2DoneBtn);
        monCC2TotalTV = findViewById(R.id.layout1TotalTV);
        monCC2TotalTV.setVisibility(View.GONE);
        monCC2List = findViewById(R.id.layout1ListView);

        monCC2AddMoreBtn.setOnClickListener(onClickMonCC2AddMoreBtn);

        monCC2Adapter = new MonCC2Adapter(this, monCC2DbMgr.getCCTrans());
        monCC2List.setAdapter(monCC2Adapter);
    }

    View.OnClickListener onClickMonCC2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToMain = new Intent(LayoutCCPurList.this, MainActivity.class);
            monCC2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToMain);
        }
    };

    View.OnClickListener onClickMonCC2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToAddCCTrans = new Intent(LayoutCCPurList.this, LayoutCCPur.class);
            monCC2ToAddCCTrans.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToAddCCTrans);
        }
    };

    public class MonCC2Adapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> ccTrans;

        private MonCC2Adapter(
                Context context,
                List<TransactionsDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<TransactionsDb> ccTrans) {
            this.ccTrans = ccTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyCC2ViewHolder monCC2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_5_cc_3tv_edit_del,
                        parent, false);

                monCC2Hldr = new MoneyCC2ViewHolder();
                monCC2Hldr.monCC2EditBtn = convertView.findViewById(R.id.list5EditBtn);
                monCC2Hldr.monCC2DelBtn = convertView.findViewById(R.id.list5DelBtn);
                monCC2Hldr.monCC2DateTV = convertView.findViewById(R.id.list5DateTV);
                monCC2Hldr.monCC2AmtTV = convertView.findViewById(R.id.list5AmtTV);
                monCC2Hldr.monCC2CatTV = convertView.findViewById(R.id.list5CatTV);
                monCC2Hldr.monCC2AcctLabel = convertView.findViewById(R.id.list5CCLabel);
                monCC2Hldr.monCC2AcctTV = convertView.findViewById(R.id.list5CCTV);
                monCC2Hldr.monCC2AcctLabel2 = convertView.findViewById(R.id.list5CCLabel2);
                monCC2Hldr.monCC2AcctLabel2.setVisibility(View.GONE);
                monCC2Hldr.monCC2AcctTV2 = convertView.findViewById(R.id.list5CCTV2);
                monCC2Hldr.monCC2AcctTV2.setVisibility(View.GONE);

                convertView.setTag(monCC2Hldr);

            } else {
                monCC2Hldr = (MoneyCC2ViewHolder) convertView.getTag();
            }

                monCC2Hldr.monCC2EditBtn.setVisibility(View.GONE);
                monCC2Hldr.monCC2DelBtn.setVisibility(View.GONE);

            monCC2Hldr.monCC2DateTV.setText(ccTrans.get(position).getTransCreatedOn());
            
            monCC2MonOutAmt = ccTrans.get(position).getTransAmt();
            monCC2Gen.dblASCurrency(String.valueOf(monCC2MonOutAmt), monCC2Hldr.monCC2AmtTV);
            
            monCC2Hldr.monCC2CatTV.setText(ccTrans.get(position).getTransBdgtCat());
            monCC2Hldr.monCC2AcctTV.setText(ccTrans.get(position).getTransFromAcctName());

            return convertView;
        }
    }

    private static class MoneyCC2ViewHolder {
        public TextView monCC2CatTV;
        public TextView monCC2AmtTV;
        public TextView monCC2DateTV;
        public ImageButton monCC2EditBtn;
        public ImageButton monCC2DelBtn;
        public TextView monCC2AcctLabel;
        public TextView monCC2AcctTV;
        public TextView monCC2AcctLabel2;
        public TextView monCC2AcctTV2;
    }
}
