package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
//import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;

public class DailyWeeklyLimits extends Fragment {

    Cursor moneyOutCursor, moneyOutCursor2;
    DbHelper moneyOutHelper, moneyOutHelper2;
    DbManager dbManager;
    Double annualLimit, weeklyLimitD, spentThisWeek, spentThisWeek2, amountLeft;
    //ExpenseBudgetDbManager expenseBudgetDbManager;
    FragmentManager fm;
    FragmentTransaction transaction;
    General general;
    ListView weeklyLimitListView;
    long expenseId;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase moneyOutDbDb, moneyOutDbDb2;
    String amountLeftS, spentThisWeekS;
    TextView amountLeftText;
    View v;
    WeeklyLimitsAdapter weeklyLimitsAdapter;

    public DailyWeeklyLimits() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_weekly_limits, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();

        weeklyLimitListView = v.findViewById(R.id.weeklyLimitListView);

        dbManager = new DbManager(getContext());
        weeklyLimitsAdapter = new WeeklyLimitsAdapter(getContext(), dbManager.getWeeklyLimits());
        weeklyLimitListView.setAdapter(weeklyLimitsAdapter);

    }

    /*public List<String> alsoValidDates() {
        List<String> alsoValidDates = new ArrayList<>();
        moneyOutHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT (moneyOutCreatedOn)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE "
                + DbHelper.EXPREFKEYMO + " = " + String.valueOf(expenseId), null);
        moneyOutHelper2.getWritableDatabase();
        moneyOutCursor2.moveToFirst();
        if (moneyOutCursor2.moveToFirst()) {
            do {
                alsoValidDates.add(moneyOutCursor2.getString(0));
            } while (moneyOutCursor2.moveToNext());
        }
        return alsoValidDates;
    }*/

    public Double getSpentThisWeek() {

        moneyOutHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        /*moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE "
                + DbHelper.EXPREFKEYMO + " = " + String.valueOf(expenseId), null);*/
        //String[] args = general.newDatesList();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT SUM(moneyOutAmount) FROM "
                + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE "
                + DbHelper.EXPREFKEYMO
                + " = "
                + String.valueOf(expenseId)
                + " AND "
                + DbHelper.MONEYOUTCREATEDON
                //+ " =?", args);
                + " IN "
                + general.thisWeek(), null);
        try {
            moneyOutCursor.moveToFirst();
        } catch(Exception e) {
            spentThisWeek = 0.0;
            moneyOutCursor.close();
        }
        spentThisWeek = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return spentThisWeek;
    }

    //list adapter
    public class WeeklyLimitsAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> weeklyLimits;

        public WeeklyLimitsAdapter(
                Context context,
                List<ExpenseBudgetDb> weeklyLimits) {

            super(context, -1, weeklyLimits);

            this.context = context;
            this.weeklyLimits = weeklyLimits;
        }

        public void getWeeklyLimits(List<ExpenseBudgetDb> weeklyLimits) {
            this.weeklyLimits = weeklyLimits;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return weeklyLimits.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final ViewHolderWeeklyLimits holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(v.getContext()).inflate(
                        R.layout.fragment_list_daily_weekly_limits,
                        parent, false);

                holder = new ViewHolderWeeklyLimits();
                //put visual elements here and list them in ViewHolder class below
                holder.spendingCategory = convertView.findViewById(R.id.spendingCategory);
                holder.amountLeftText = convertView.findViewById(R.id.amountLeftText);
                holder.spentAmount = convertView.findViewById(R.id.spentAmount);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolderWeeklyLimits) convertView.getTag();
            }

            //retrieve spendingCategory
            holder.spendingCategory.setText(weeklyLimits.get(position).getExpenseName());

            //retrieve amountLeft
            expenseId = weeklyLimits.get(position).getId();
            spentThisWeek2 = getSpentThisWeek();
            annualLimit = weeklyLimits.get(position).getExpenseBAnnualAmount();
            weeklyLimitD = annualLimit / 52;
            amountLeft = weeklyLimitD - spentThisWeek2;
            amountLeftS = currencyFormat.format(amountLeft);
            holder.amountLeftText.setText(String.valueOf(amountLeftS));

            //retrieve amount spent in this category during general.validDates();
            spentThisWeekS = currencyFormat.format(spentThisWeek2);
            holder.spentAmount.setText(spentThisWeekS);

            return convertView;
        }
    }

    private static class ViewHolderWeeklyLimits {
        private TextView spendingCategory;
        private TextView amountLeftText;
        private TextView spentAmount;
    }

    /*public void replaceFragment(Fragment fragment) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.daily_fragment_container, fragment);

        transaction.commit();
    }*/
}
