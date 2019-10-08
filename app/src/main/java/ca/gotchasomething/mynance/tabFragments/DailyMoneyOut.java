/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ca.gotchasomething.mynance.AddExpense;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyMoneyOut extends Fragment {

    boolean aPoss = false, bPoss = false;
    Button monOutCreateBtn;
    ContentValues monOutCV;
    Cursor monOutCur;
    DbHelper monOutHelper, monOutHelper2;
    DbManager monOutDbMgr;
    Double moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, monOutMonOutAmt = 0.0, monOutMonOutOldAmt = 0.0, monOutMonOutNewAmt = 0.0,
            newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0;
    General monOutGen;
    ExpenseBudgetDb monOutExpDb;
    Intent monOutToAddMore, monOutRefresh, monOutToRatings, monOutToList;
    LayoutDailyMoney monOutLayDaiMon;
    ListView monOutList;
    long monOutMaxId, monOutExpRefKeyMO;
    MoneyOutDb monOutMonOutDb;
    MonOutLstAdapter monOutLstAdapter;
    SQLiteDatabase monOutDb, monOutDb2;
    String clicksS, clicksES, expCatFromTag = null, expPriorityFromTag = null, expWeeklyFromTag = null;
    TextView monOutBudgWarnTV, monOutAvailAcctTV, monOutTotAcctTV, temp, tempE;
    View v;
    public static int clicked = 0;
    public static int clickedE = 0;
    public static final String SRPE = "shared ratings pref";
    public static final String CTE = String.valueOf(clickedE);
    public static final String SRP = "shared ratings pref";
    public static final String CT = String.valueOf(clicked);

    public DailyMoneyOut() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_1_choose_or_create, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monOutGen = new General();
        monOutDbMgr = new DbManager(getContext());

        temp = v.findViewById(R.id.temp);
        temp.setVisibility(View.GONE);
        temp.setText(String.valueOf(clicked));
        tempE = v.findViewById(R.id.tempE);
        tempE.setVisibility(View.GONE);
        tempE.setText(String.valueOf(clickedE));
        monOutCreateBtn = v.findViewById(R.id.frag1CreateBtn);
        monOutCreateBtn.setOnClickListener(onClickMonIn1CreateBtn);
        monOutAvailAcctTV = v.findViewById(R.id.dailyMoneyAvailAmtTV);
        monOutBudgWarnTV = v.findViewById(R.id.dailyMoneyBudgetWarnTV);
        monOutTotAcctTV = v.findViewById(R.id.dailyMoneyTotalAmtTV);

        monOutList = v.findViewById(R.id.frag1ListView);
        monOutLstAdapter = new MonOutLstAdapter(getContext(), monOutDbMgr.getExpense());
        monOutList.setAdapter(monOutLstAdapter);

        loadClicks();
        updateClicks();
        loadClicksE();
        updateClicksE();

        monOutCV = new ContentValues();
        monOutCV.put(DbHelper.LASTPAGEID, 3);
        monOutHelper2 = new DbHelper(getContext());
        monOutDb2 = monOutHelper2.getWritableDatabase();
        monOutDb2.update(DbHelper.CURRENT_TABLE_NAME, monOutCV, DbHelper.ID + "= '1'", null);
        monOutDb2.close();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(String.valueOf(clicked), temp.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void loadClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        clicksS = sp.getString(CT, "");
    }

    public void updateClicks() {
        temp.setText(clicksS);

        if (!clicksS.equals("")) {
            clicked = Integer.valueOf(clicksS);
        } else {
            clicked = 0;
        }
    }

    public void loadClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        clicksES = spE.getString(CTE, "");
    }

    public void updateClicksE() {
        tempE.setText(clicksES);

        if (!clicksES.equals("")) {
            clickedE = Integer.valueOf(clicksES);
        } else {
            clickedE = 0;
        }
    }

    public void saveClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CT, temp.getText().toString());
        clicked = Integer.valueOf(temp.getText().toString());
        editor.apply();
    }

    public void saveClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editorE = spE.edit();
        editorE.putString(CTE, tempE.getText().toString());
        clickedE = Integer.valueOf(tempE.getText().toString());
        editorE.apply();
    }

    View.OnClickListener onClickMonIn1CreateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutToAddMore = new Intent(getContext(), AddExpense.class);
            monOutToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutToAddMore);
        }
    };

    public class MonOutLstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private MonOutLstAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<ExpenseBudgetDb> expenses) {
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

            final MonOutViewHolder monOutHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                monOutHldr = new MonOutViewHolder();
                monOutHldr.monOutDepLabel = convertView.findViewById(R.id.paytPayLabel);
                monOutHldr.monOutDepLabel.setText(getString(R.string.spent));
                monOutHldr.monOutFromLabel = convertView.findViewById(R.id.paytToLabel);
                monOutHldr.monOutFromLabel.setText(getString(R.string.on2));
                monOutHldr.monOutCatTV = convertView.findViewById(R.id.paytCatTV);
                monOutHldr.monOutAmtTV = convertView.findViewById(R.id.paytAmtTV);
                monOutHldr.monOutNewAmtET = convertView.findViewById(R.id.paytAmtET);
                monOutHldr.monOutSaveButton = convertView.findViewById(R.id.paytSaveBtn);
                monOutHldr.monOutWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                monOutHldr.monOutWarnTV = convertView.findViewById(R.id.paytWarnTV);
                monOutHldr.monOutYesContButton = convertView.findViewById(R.id.paytYesContBtn);
                monOutHldr.monOutNoContButton = convertView.findViewById(R.id.paytNoContBtn);
                monOutHldr.monOutDefLayout = convertView.findViewById(R.id.paytDefLayout);
                monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                monOutHldr.monOutYesDefButton = convertView.findViewById(R.id.paytYesDefBtn);
                monOutHldr.monOutNoDefButton = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(monOutHldr);

            } else {
                monOutHldr = (MonOutViewHolder) convertView.getTag();
            }

            monOutHldr.monOutCatTV.setText(expenses.get(position).getExpenseName());
            monOutHldr.monOutAmtTV.setText(String.valueOf(expenses.get(position).getExpenseAmount()));

            monOutExpRefKeyMO = expenses.get(position).getId();

            monOutHldr.monOutSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutExpDb = (ExpenseBudgetDb) monOutHldr.monOutSaveButton.getTag();

                    monOutHelper = new DbHelper(getContext());
                    monOutDb = monOutHelper.getReadableDatabase();
                    monOutCur = monOutDb.rawQuery("SELECT max(_id)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
                    monOutCur.moveToFirst();
                    monOutMaxId = monOutCur.getLong(0);
                    monOutCur.close();

                    if (monOutMaxId != 0 && monOutMaxId % 10 == 0) {
                        monOutToRatings = new Intent(getContext(), Ratings.class);
                        monOutToRatings.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(monOutToRatings);
                    } else {
                        monOutMonOutOldAmt = expenses.get(position).getExpenseAmount();
                        monOutMonOutNewAmt = monOutGen.dblFromET(monOutHldr.monOutNewAmtET);

                        if (monOutMonOutNewAmt == 0) {
                            monOutMonOutAmt = monOutMonOutOldAmt;
                        } else {
                            monOutMonOutAmt = monOutMonOutNewAmt;
                        }

                        expPriorityFromTag = monOutExpDb.getExpensePriority();
                        expWeeklyFromTag = monOutExpDb.getExpenseWeekly();

                        monOutDbMgr.checkIfPoss(monOutMonOutAmt);

                        if (expPriorityFromTag.equals("A")) {
                            if (!aPoss) {
                                monOutHldr.monOutWarnLayout.setVisibility(View.VISIBLE);
                                monOutHldr.monOutWarnTV.setText(getString(R.string.payment_not_possible_A));
                                monOutHldr.monOutNoContButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                        monOutRefresh = new Intent(getContext(), DailyMoneyOut.class);
                                        monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        startActivity(monOutRefresh);
                                    }
                                });
                                monOutHldr.monOutYesContButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        } else {
                            if (expPriorityFromTag.equals("B")) {
                                if (!bPoss) {
                                    monOutHldr.monOutWarnLayout.setVisibility(View.VISIBLE);
                                    monOutHldr.monOutWarnTV.setText(getString(R.string.payment_not_possible_B));
                                    monOutHldr.monOutNoContButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                            monOutRefresh = new Intent(getContext(), DailyMoneyOut.class);
                                            monOutRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                            startActivity(monOutRefresh);
                                        }
                                    });
                                    monOutHldr.monOutYesContButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monOutHldr.monOutWarnLayout.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        }

                        monOutDbMgr.detAandBPortionsExp(monOutMonOutAmt, expPriorityFromTag);

                        monOutDbMgr.updateTotAcctBalMinus(monOutMonOutAmt);

                        monOutDbMgr.updateAvailBalMinus(moneyOutA, moneyOutOwing, moneyOutB);

                        if (monOutDbMgr.retrieveCurrentOwingA() < 0) {
                            monOutDbMgr.adjustAandBPortions(moneyOutA, moneyOutOwing, moneyOutB);
                            moneyOutA = newMoneyA;
                            moneyOutOwing = newMoneyOwing;
                            moneyOutB = newMoneyB;
                        }

                        if (monOutMonOutAmt == monOutMonOutNewAmt) {
                            monOutHldr.monOutDefLayout.setVisibility(View.VISIBLE);

                            monOutHldr.monOutNoDefButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                }
                            });

                            monOutHldr.monOutYesDefButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monOutHldr.monOutDefLayout.setVisibility(View.GONE);
                                    monOutExpDb.setExpenseAmount(monOutMonOutAmt);
                                    monOutDbMgr.updateExpense(monOutExpDb);
                                    monOutLstAdapter.updateExpenses(monOutDbMgr.getExpense());
                                    notifyDataSetChanged();
                                }
                            });
                        }

                        monOutMonOutDb = new MoneyOutDb(
                                expCatFromTag,
                                expPriorityFromTag,
                                expWeeklyFromTag,
                                monOutMonOutAmt,
                                moneyOutA,
                                moneyOutOwing,
                                moneyOutB,
                                monOutGen.createTimestamp(),
                                "N",
                                "N/A",
                                0,
                                0,
                                0,
                                monOutExpRefKeyMO,
                                0);
                        monOutDbMgr.addMoneyOut(monOutMonOutDb);

                        monOutDbMgr.updateExpAnnAmt(expenses.get(position).getId());

                        monOutLayDaiMon = new LayoutDailyMoney();
                        monOutLayDaiMon.layDaiMonHeaderText();
                        //monOutDbMgr.dailyMoneyHeaderText(monOutBudgWarnTV, monOutTotAcctTV, monOutAvailAcctTV);

                        monOutToList = new Intent(getContext(), DailyMoneyOutList.class);
                        monOutToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(monOutToList);
                    }
                }
            });
            return convertView;
        }
    }
    private static class MonOutViewHolder {
        public TextView monOutDepLabel;
        public TextView monOutFromLabel;
        public TextView monOutCatTV;
        public TextView monOutAmtTV;
        public EditText monOutNewAmtET;
        public Button monOutSaveButton;
        public LinearLayout monOutWarnLayout;
        public TextView monOutWarnTV;
        public Button monOutYesContButton;
        public Button monOutNoContButton;
        public LinearLayout monOutDefLayout;
        public Button monOutYesDefButton;
        public Button monOutNoDefButton;
    }
}*/
