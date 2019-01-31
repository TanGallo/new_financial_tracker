package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.List;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyWeeklyLimits extends Fragment {

    ContentValues currentValue;
    DbHelper dbHelper3;
    DbManager dbManager;
    Double amountLeft = 0.0, spentThisWeek = 0.0, startingBalance = 0.0;
    General general;
    ListView weeklyLimitListView;
    long expenseId;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase db3;
    String amountLeftS = null, spentAmountS = null;
    TextView amountLeftLabel, amountLeftWarning, noWeeklyLabel, noWeeklyLabel2;
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
        noWeeklyLabel = v.findViewById(R.id.noWeeklyLabel);
        noWeeklyLabel.setVisibility(View.GONE);
        noWeeklyLabel2 = v.findViewById(R.id.noWeeklyLabel2);
        noWeeklyLabel2.setVisibility(View.GONE);

        dbManager = new DbManager(getContext());
        weeklyLimitsAdapter = new WeeklyLimitsAdapter(getContext(), dbManager.getWeeklyLimits());
        weeklyLimitListView.setAdapter(weeklyLimitsAdapter);
        if(weeklyLimitsAdapter.getCount() == 0) {
            noWeeklyLabel.setVisibility(View.VISIBLE);
            noWeeklyLabel2.setVisibility(View.VISIBLE);
        } else {
            noWeeklyLabel.setVisibility(View.GONE);
            noWeeklyLabel2.setVisibility(View.GONE);
        }

        currentValue = new ContentValues();
        currentValue.put(DbHelper.CURRENTPAGEID, 5);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, currentValue, DbHelper.ID + "= '1'", null);
        db3.close();

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
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_weekly_limits,
                        parent, false);

                holder = new ViewHolderWeeklyLimits();
                //put visual elements here and list them in ViewHolder class below
                holder.spendingCategory = convertView.findViewById(R.id.spendingCategory);
                holder.amountLeftText = convertView.findViewById(R.id.amountLeftText);
                holder.spentAmount = convertView.findViewById(R.id.spentAmount);
                amountLeftLabel = convertView.findViewById(R.id.amountLeftLabel);
                amountLeftWarning = convertView.findViewById(R.id.amountLeftWarning);
                amountLeftWarning.setVisibility(View.GONE);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolderWeeklyLimits) convertView.getTag();
            }

            //retrieve spendingCategory
            holder.spendingCategory.setText(weeklyLimits.get(position).getExpenseName());

            //retrieve amount spent in this category during general.thisWeek();
            expenseId = weeklyLimits.get(position).getId();

            List<Double> spentThisWeekList = new ArrayList<>();
            for(MoneyOutDb m : dbManager.getMoneyOuts()) {
                if(String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(expenseId)) && general.thisWeek().contains(m.getMoneyOutCreatedOn())) {
                    spentThisWeekList.add(m.getMoneyOutAmount());
                }
            }
            spentThisWeek = 0.0;
            if(spentThisWeekList.size() == 0) {
                spentThisWeek = 0.0;
            } else {
                for(Double dbl : spentThisWeekList) {
                    spentThisWeek += dbl;
                }
            }

            //spentAmount = getSpentThisWeek();
            spentAmountS = currencyFormat.format(spentThisWeek);
            holder.spentAmount.setText(spentAmountS);

            //retrieve amountLeft
            startingBalance = weeklyLimits.get(position).getExpenseBAnnualAmount() / 52;
            amountLeft = startingBalance - spentThisWeek;
            amountLeftS = currencyFormat.format(amountLeft);
            holder.amountLeftText.setText(amountLeftS);

            if(amountLeft <= 0) {
                amountLeftWarning.setVisibility(View.VISIBLE);
                amountLeftLabel.setVisibility(View.GONE);
            } else {
                amountLeftWarning.setVisibility(View.GONE);
                amountLeftLabel.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    private static class ViewHolderWeeklyLimits {
        private TextView spendingCategory;
        private TextView amountLeftText;
        private TextView spentAmount;
    }
}
