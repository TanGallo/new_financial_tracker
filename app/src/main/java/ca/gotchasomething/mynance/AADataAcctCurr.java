/*package ca.gotchasomething.mynance;

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

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.CurrentDb;

public class AADataAcctCurr extends MainNavigation {

    AcctListAdapter acctListAdapter;
    CurrListAdapter currListAdapter;
    DbManager dataDbMgr;
    General dataGen;
    ListView acctList, currList;

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

        acctList = findViewById(R.id.listA);
        acctListAdapter = new AcctListAdapter(this, dataDbMgr.getAccounts());
        acctList.setAdapter(acctListAdapter);

        currList = findViewById(R.id.listB);
        currListAdapter = new CurrListAdapter(this, dataDbMgr.getCurrent());
        currList.setAdapter(currListAdapter);

    }

    public class AcctListAdapter extends ArrayAdapter<AccountsDb> {

        private Context context;
        private List<AccountsDb> accountsList;

        private AcctListAdapter(
                Context context,
                List<AccountsDb> accountsList) {

            super(context, -1, accountsList);

            this.context = context;
            this.accountsList = accountsList;
        }

        public void updateAccts(List<AccountsDb> accountsList) {
            this.accountsList = accountsList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return accountsList.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final AcctViewHolder acctHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_acct_list,
                        parent, false);

                acctHldr = new AcctViewHolder();
                acctHldr.acctName = convertView.findViewById(R.id.acctTV1);
                acctHldr.acctBal = convertView.findViewById(R.id.acctTV2);
                acctHldr.acctDS = convertView.findViewById(R.id.acctTV3);
                acctHldr.acctMax = convertView.findViewById(R.id.acctTV4);
                acctHldr.acctRate = convertView.findViewById(R.id.acctTV5);
                acctHldr.acctPaytsTo = convertView.findViewById(R.id.acctTV6);
                acctHldr.acctAnnPaytsTo = convertView.findViewById(R.id.acctTV7);
                acctHldr.acctEndDate = convertView.findViewById(R.id.acctTV8);
                acctHldr.acctDebtToPay = convertView.findViewById(R.id.acctTV9);
                acctHldr.acctId = convertView.findViewById(R.id.acctTV10);
                convertView.setTag(acctHldr);

            } else {
                acctHldr = (AcctViewHolder) convertView.getTag();
            }

            acctHldr.acctName.setText(accountsList.get(position).getAcctName());
            dataGen.dblASCurrency(String.valueOf(accountsList.get(position).getAcctBal()), acctHldr.acctBal);
            acctHldr.acctDS.setText(accountsList.get(position).getAcctDebtSav());
            dataGen.dblASCurrency(String.valueOf(accountsList.get(position).getAcctMax()), acctHldr.acctMax);
            acctHldr.acctRate.setText(String.valueOf(accountsList.get(position).getAcctIntRate()));
            dataGen.dblASCurrency(String.valueOf(accountsList.get(position).getAcctPaytsTo()), acctHldr.acctPaytsTo);
            dataGen.dblASCurrency(String.valueOf(accountsList.get(position).getAcctAnnPaytsTo()), acctHldr.acctAnnPaytsTo);
            acctHldr.acctEndDate.setText(accountsList.get(position).getAcctEndDate());
            dataGen.dblASCurrency(String.valueOf(accountsList.get(position).getAcctDebtToPay()), acctHldr.acctDebtToPay);
            acctHldr.acctId.setText(String.valueOf(accountsList.get(position).getId()));

            return convertView;
        }
    }

    private static class AcctViewHolder {
        private TextView acctName;
        private TextView acctBal;
        private TextView acctDS;
        private TextView acctMax;
        private TextView acctRate;
        private TextView acctPaytsTo;
        private TextView acctAnnPaytsTo;
        private TextView acctEndDate;
        private TextView acctDebtToPay;
        private TextView acctId;
    }

    public class CurrListAdapter extends ArrayAdapter<CurrentDb> {

        private Context context;
        private List<CurrentDb> currents;

        private CurrListAdapter(
                Context context,
                List<CurrentDb> currents) {

            super(context, -1, currents);

            this.context = context;
            this.currents = currents;
        }

        public void updateCurrent(List<CurrentDb> currents) {
            this.currents = currents;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return currents.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CurrViewHolder currHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_curr_list,
                        parent, false);

                currHldr = new CurrViewHolder();
                currHldr.currA = convertView.findViewById(R.id.currTV1);
                currHldr.currOwingA = convertView.findViewById(R.id.currTV2);
                currHldr.currB = convertView.findViewById(R.id.currTV3);
                currHldr.currLastPg = convertView.findViewById(R.id.currTV4);
                currHldr.currLastDate = convertView.findViewById(R.id.currTV5);
                currHldr.currId = convertView.findViewById(R.id.currTV6);
                convertView.setTag(currHldr);

            } else {
                currHldr = (CurrViewHolder) convertView.getTag();
            }

            dataGen.dblASCurrency(String.valueOf(currents.get(position).getCurrentA()), currHldr.currA);
            dataGen.dblASCurrency(String.valueOf(currents.get(position).getCurrentOwingA()), currHldr.currOwingA);
            dataGen.dblASCurrency(String.valueOf(currents.get(position).getCurrentB()), currHldr.currB);
            currHldr.currLastPg.setText(String.valueOf(currents.get(position).getLastPageId()));
            currHldr.currLastDate.setText(currents.get(position).getLastDate());
            currHldr.currId.setText(String.valueOf(currents.get(position).getId()));

            return convertView;
        }
    }

    private static class CurrViewHolder {
        private TextView currA;
        private TextView currOwingA;
        private TextView currB;
        private TextView currLastPg;
        private TextView currLastDate;
        private TextView currId;
    }
}*/