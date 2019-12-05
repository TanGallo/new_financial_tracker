package ca.gotchasomething.mynance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.TransactionsDb;

public class AADataBdgtTrans extends MainNavigation {

    BdgtListAdapter bdgtListAdapter;
    DbManager dataDbMgr;
    General dataGen;
    ListView acctList, bdgtList, currList, transList;
    TransListAdapter transListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_data);
                toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        dataDbMgr = new DbManager(this);
        dataGen = new General();

        bdgtList = findViewById(R.id.listA);
        bdgtListAdapter = new BdgtListAdapter(this, dataDbMgr.getBudget());
        bdgtList.setAdapter(bdgtListAdapter);

        transList = findViewById(R.id.listB);
        transListAdapter = new TransListAdapter(this, dataDbMgr.getTransactions());
        transList.setAdapter(transListAdapter);

    }

    public class BdgtListAdapter extends ArrayAdapter<BudgetDb> {

        private Context context;
        private List<BudgetDb> budgetList;

        private BdgtListAdapter(
                Context context,
                List<BudgetDb> budgetList) {

            super(context, -1, budgetList);

            this.context = context;
            this.budgetList = budgetList;
        }

        public void updateBdgt(List<BudgetDb> budgetList) {
            this.budgetList = budgetList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return budgetList.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final BdgtViewHolder bdgtHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_bdgt_list,
                        parent, false);

                bdgtHldr = new BdgtViewHolder();
                bdgtHldr.bdgtCat = convertView.findViewById(R.id.bdgtTV1);
                bdgtHldr.bdgtPaytsTo = convertView.findViewById(R.id.bdgtTV2);
                bdgtHldr.bdgtEI = convertView.findViewById(R.id.bdgtTV3);
                bdgtHldr.bdgtFrq = convertView.findViewById(R.id.bdgtTV4);
                bdgtHldr.bdgtAnnPaytsTo = convertView.findViewById(R.id.bdgtTV5);
                bdgtHldr.bdgtPriority = convertView.findViewById(R.id.bdgtTV6);
                bdgtHldr.bdgtWeekly = convertView.findViewById(R.id.bdgtTV7);
                bdgtHldr.bdgtId = convertView.findViewById(R.id.bdgtTV8);
                convertView.setTag(bdgtHldr);

            } else {
                bdgtHldr = (BdgtViewHolder) convertView.getTag();
            }

            bdgtHldr.bdgtCat.setText(budgetList.get(position).getBdgtCat());
            dataGen.dblASCurrency(String.valueOf(budgetList.get(position).getBdgtPaytAmt()), bdgtHldr.bdgtPaytsTo);
            bdgtHldr.bdgtEI.setText(budgetList.get(position).getBdgtExpInc());
            bdgtHldr.bdgtFrq.setText(String.valueOf(budgetList.get(position).getBdgtPaytFrq()));
            dataGen.dblASCurrency(String.valueOf(budgetList.get(position).getBdgtAnnPayt()), bdgtHldr.bdgtAnnPaytsTo);
            bdgtHldr.bdgtPriority.setText(budgetList.get(position).getBdgtPriority());
            bdgtHldr.bdgtWeekly.setText(budgetList.get(position).getBdgtWeekly());
            bdgtHldr.bdgtId.setText(String.valueOf(budgetList.get(position).getId()));

            return convertView;
        }
    }

    private static class BdgtViewHolder {
        private TextView bdgtCat;
        private TextView bdgtPaytsTo;
        private TextView bdgtEI;
        private TextView bdgtFrq;
        private TextView bdgtAnnPaytsTo;
        private TextView bdgtPriority;
        private TextView bdgtWeekly;
        private TextView bdgtId;
    }

    public class TransListAdapter extends ArrayAdapter<TransactionsDb> {

        private Context context;
        private List<TransactionsDb> transactionsList;

        private TransListAdapter(
                Context context,
                List<TransactionsDb> transactionsList) {

            super(context, -1, transactionsList);

            this.context = context;
            this.transactionsList = transactionsList;
        }

        public void updateTrans(List<TransactionsDb> transactionsList) {
            this.transactionsList = transactionsList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transactionsList.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final TransViewHolder transHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_acct_list,
                        parent, false);

                transHldr = new TransViewHolder();
                transHldr.transType = convertView.findViewById(R.id.transTV1);
                transHldr.transIsCC = convertView.findViewById(R.id.transTV2);
                transHldr.transBdgtCat = convertView.findViewById(R.id.transTV3);
                transHldr.transBdgtId = convertView.findViewById(R.id.transTV4);
                transHldr.transAmt = convertView.findViewById(R.id.transTV5);
                transHldr.transInA = convertView.findViewById(R.id.transTV6);
                transHldr.transInOwing = convertView.findViewById(R.id.transTV7);
                transHldr.transInB = convertView.findViewById(R.id.transTV8);
                transHldr.transOutA = convertView.findViewById(R.id.transTV9);
                transHldr.transOutOwing = convertView.findViewById(R.id.transTV10);
                transHldr.transOutB = convertView.findViewById(R.id.transTV11);
                transHldr.transToId = convertView.findViewById(R.id.transTV12);
                transHldr.transToName = convertView.findViewById(R.id.transTV13);
                transHldr.transToDS = convertView.findViewById(R.id.transTV14);
                transHldr.transFromId = convertView.findViewById(R.id.transTV15);
                transHldr.transFromName = convertView.findViewById(R.id.transTV16);
                transHldr.transFromDS = convertView.findViewById(R.id.transTV17);
                transHldr.transBdgtPriority = convertView.findViewById(R.id.transTV18);
                transHldr.transBdgtWkly = convertView.findViewById(R.id.transTV19);
                transHldr.transCCToPay = convertView.findViewById(R.id.transTV20);
                transHldr.transCCPaid = convertView.findViewById(R.id.transTV21);
                transHldr.transDate = convertView.findViewById(R.id.transTV22);
                transHldr.transId = convertView.findViewById(R.id.transTV23);
                convertView.setTag(transHldr);

            } else {
                transHldr = (TransViewHolder) convertView.getTag();
            }

            try {
                transHldr.transType.setText(transactionsList.get(position).getTransType());
                transHldr.transIsCC.setText(transactionsList.get(position).getTransIsCC());
                transHldr.transBdgtCat.setText(transactionsList.get(position).getTransBdgtCat());
                transHldr.transBdgtId.setText(String.valueOf(transactionsList.get(position).getTransBdgtId()));
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmt()), transHldr.transAmt);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtInA()), transHldr.transInA);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtInOwing()), transHldr.transInOwing);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtInB()), transHldr.transInB);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtOutA()), transHldr.transOutA);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtOutOwing()), transHldr.transOutOwing);
                dataGen.dblASCurrency(String.valueOf(transactionsList.get(position).getTransAmtOutB()), transHldr.transOutB);
                transHldr.transToId.setText(String.valueOf(transactionsList.get(position).getTransToAcctId()));
                transHldr.transToName.setText(transactionsList.get(position).getTransToAcctName());
                transHldr.transToDS.setText(transactionsList.get(position).getTransToDebtSav());
                transHldr.transFromId.setText(String.valueOf(transactionsList.get(position).getTransFromAcctId()));
                transHldr.transFromName.setText(transactionsList.get(position).getTransFromAcctName());
                transHldr.transFromDS.setText(transactionsList.get(position).getTransFromDebtSav());
                transHldr.transBdgtPriority.setText(transactionsList.get(position).getTransBdgtPriority());
                transHldr.transBdgtWkly.setText(transactionsList.get(position).getTransBdgtWeekly());
                transHldr.transCCToPay.setText(transactionsList.get(position).getTransCCToPay());
                transHldr.transCCPaid.setText(transactionsList.get(position).getTransCCPaid());
                transHldr.transDate.setText(transactionsList.get(position).getTransCreatedOn());
                transHldr.transId.setText(String.valueOf(transactionsList.get(position).getId()));
            } catch(NullPointerException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

    private static class TransViewHolder {
        private TextView transType;
        private TextView transIsCC;
        private TextView transBdgtCat;
        private TextView transBdgtId;
        private TextView transAmt;
        private TextView transInA;
        private TextView transInOwing;
        private TextView transInB;
        private TextView transOutA;
        private TextView transOutOwing;
        private TextView transOutB;
        private TextView transToId;
        private TextView transToName;
        private TextView transToDS;
        private TextView transFromId;
        private TextView transFromName;
        private TextView transFromDS;
        private TextView transBdgtPriority;
        private TextView transBdgtWkly;
        private TextView transCCToPay;
        private TextView transCCPaid;
        private TextView transDate;
        private TextView transId;
    }
}