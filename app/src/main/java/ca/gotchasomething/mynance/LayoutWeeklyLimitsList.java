package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ca.gotchasomething.mynance.data.BudgetDb;

//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;

public class LayoutWeeklyLimitsList extends AppCompatActivity {

    BudgetDb wee1ExpDb;
    Button wee1DoneBtn;
    ContentValues wee1CV, wee1CV2;
    DbHelper wee1Helper, wee1Helper2;
    DbManager wee1DbMgr;
    Double wee1WeeklyAmt;
    General wee1Gen;
    Intent wee1ToSetUp, wee1ToMain, wee1ToAddMoreExp, wee1ToWeekly;
    ListView wee1ListView;
    SQLiteDatabase wee1DB, wee1DB2;
    TextView wee1LayoutHeaderLabel;
    Wee1LstAdapter wee1LstAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_3_choose_or_create);

        wee1DbMgr = new DbManager(this);
        wee1Gen = new General();

        wee1LayoutHeaderLabel = findViewById(R.id.chooseLayoutHeaderLabel);
        wee1LayoutHeaderLabel.setText(getString(R.string.add_watch_or_create));
        wee1LayoutHeaderLabel.setOnClickListener(onClickWee1LayoutHeaderLabel);
        wee1ListView = findViewById(R.id.chooseLayoutListView);
        wee1DoneBtn = findViewById(R.id.chooseLayoutDoneBtn);

        wee1DoneBtn.setOnClickListener(onClickWee1DoneButton);

        wee1LstAdapter = new Wee1LstAdapter(this, wee1DbMgr.getExpense());
        wee1ListView.setAdapter(wee1LstAdapter);

        wee1CV2 = new ContentValues();
        wee1CV2.put(DbHelper.LASTPAGEID, 4);
        wee1Helper2 = new DbHelper(this);
        wee1DB2 = wee1Helper2.getWritableDatabase();
        wee1DB2.update(DbHelper.CURRENT_TABLE_NAME, wee1CV2, DbHelper.ID + "= '1'", null);
        wee1DB2.close();
    }

    View.OnClickListener onClickWee1LayoutHeaderLabel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wee1ToAddMoreExp = new Intent(LayoutWeeklyLimitsList.this, AddExpense.class);
            wee1ToAddMoreExp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(wee1ToAddMoreExp);
        }
    };

    View.OnClickListener onClickWee1DoneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (wee1DbMgr.retrieveLatestDone().equals("analysis")) {
                wee1CV = new ContentValues();
                wee1CV.put(DbHelper.LATESTDONE, "weekly");
                wee1Helper = new DbHelper(getApplicationContext());
                wee1DB = wee1Helper.getWritableDatabase();
                wee1DB.update(DbHelper.SET_UP_TABLE_NAME, wee1CV, DbHelper.ID + "= '1'", null);
                wee1DB.close();

                wee1ToSetUp = new Intent(LayoutWeeklyLimitsList.this, LayoutSetUp.class);
                wee1ToSetUp.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(wee1ToSetUp);
            } else if(wee1DbMgr.retrieveLastPageId() == 4) {
                wee1ToWeekly = new Intent(LayoutWeeklyLimitsList.this, LayoutWeeklyLimits.class);
                wee1ToWeekly.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(wee1ToWeekly);

            } else {
                    wee1ToMain = new Intent(LayoutWeeklyLimitsList.this, MainActivity.class);
                    wee1ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(wee1ToMain);
                }
            }
    };

    public class Wee1LstAdapter extends ArrayAdapter<BudgetDb> {

        public Context context;
        public List<BudgetDb> expenses;
        boolean[] checkedState;

        public Wee1LstAdapter(
                Context context,
                List<BudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
            checkedState = new boolean[expenses.size()];
        }

        public void updateExpenses(List<BudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Wee1ViewHolder wee1Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.frag_list_1_toggle_buttons,
                        parent, false);

                wee1Hldr = new Wee1ViewHolder();
                wee1Hldr.wee1WeeklyCatTV = convertView.findViewById(R.id.toggleCatTV);
                wee1Hldr.wee1WeeklyAmtTV = convertView.findViewById(R.id.toggleAmtTV);
                wee1Hldr.wee1AddWatchSwitch = convertView.findViewById(R.id.toggleSwitch);
                convertView.setTag(wee1Hldr);

            } else {
                wee1Hldr = (Wee1ViewHolder) convertView.getTag();
                wee1Hldr.wee1AddWatchSwitch.setTag(wee1Hldr);
            }

            //retrieve category
            wee1Hldr.wee1WeeklyCatTV.setText(expenses.get(position).getBdgtCat());

            wee1WeeklyAmt = expenses.get(position).getBdgtAnnPayt() / 52;

            wee1Gen.dblASCurrency(String.valueOf(wee1WeeklyAmt), wee1Hldr.wee1WeeklyAmtTV);

            if (expenses.get(position).getBdgtWeekly().equals("Y")) {
                checkedState[position] = true;
            } else {
                checkedState[position] = false;
            }

            wee1Hldr.wee1AddWatchSwitch.setTag(expenses.get(position));
            wee1Hldr.wee1AddWatchSwitch.setTag(R.id.toggleSwitch, position);

            wee1Hldr.wee1AddWatchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    wee1DbMgr = new DbManager(getApplicationContext());
                    wee1ExpDb = (BudgetDb) wee1Hldr.wee1AddWatchSwitch.getTag();
                    checkedState[position] = !checkedState[position];

                    if(isChecked) {
                        checkedState[position] = true;
                        wee1ExpDb.setBdgtWeekly("Y");

                        wee1DbMgr.updateExpense(wee1ExpDb);
                        wee1LstAdapter.updateExpenses(wee1DbMgr.getExpense());
                        notifyDataSetChanged();
                    } else if(!isChecked) {
                        checkedState[position] = false;
                        wee1ExpDb.setBdgtWeekly("N");

                        wee1DbMgr.updateExpense(wee1ExpDb);
                        wee1LstAdapter.updateExpenses(wee1DbMgr.getExpense());
                        notifyDataSetChanged();
                    }
                }
            });

            wee1Hldr.wee1AddWatchSwitch.setChecked(checkedState[position]);

            return convertView;
        }
    }

    private static class Wee1ViewHolder {
        public TextView wee1WeeklyCatTV;
        public TextView wee1WeeklyAmtTV;
        public Switch wee1AddWatchSwitch;
    }
}

