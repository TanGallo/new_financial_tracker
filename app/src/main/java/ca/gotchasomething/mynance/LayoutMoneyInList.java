package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

//import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.TransactionsDb;
//import ca.gotchasomething.mynance.data.MoneyInDb;
//import ca.gotchasomething.mynance.data.SavingsDb;

public class LayoutMoneyInList extends AppCompatActivity {

    Button monInLstAddMoreBtn, monInLstDoneBtn;
    ContentValues monInLstCV;
    DbHelper monInLstHelper;
    DbManager monInLstDbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyInA = 0.0, moneyInB = 0.0, moneyInOwing = 0.0, monInLstAmtEntry = 0.0,
            monInLstMoneyInA = 0.0, monInLstMoneyInOwing = 0.0, monInLstMoneyInB = 0.0, monInLstMonInAmt = 0.0, monInLstMonInAmtDiff = 0.0, monInLstPercentA = 0.0,
            newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    General monInLstGen;
    Intent monInLstToMain, monInLstToAddMonIn, monInLstRefresh;
    ListView monInLstList;
    long monInLstIncRefKeyMI, monInToAcctId;
    MonInLstAdapter monInLstAdapter;
    SQLiteDatabase monInLstDb;
    String monInIsDebt = null, monInIsSav = null;
    TextView monInLstTitle;
    TransactionsDb monInLstMonInDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        monInLstDbMgr = new DbManager(this);
        monInLstGen = new General();

        monInLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monInLstTitle.setText(getString(R.string.deposits));

        monInLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monInLstAddMoreBtn.setText(getString(R.string.record_deposits));
        monInLstAddMoreBtn.setOnClickListener(onClickMonInLstAddMoreBtn);

        monInLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monInLstDoneBtn.setOnClickListener(onClickMonInLstDoneBtn);

        monInLstList = findViewById(R.id.layout1ListView);
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
                monInLstHldr.monInLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monInLstHldr.monInLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monInLstHldr.monInLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monInLstHldr.monInLstAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                monInLstHldr.monInLstAcctLabelTV.setText(R.string.deposited_to);
                monInLstHldr.monInLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monInLstHldr.monInLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monInLstHldr.monInLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                if(monInLstDbMgr.retrieveLastPageId() == 10) {
                    monInLstHldr.monInLstEditBtn.setVisibility(View.VISIBLE);
                    monInLstHldr.monInLstDelBtn.setVisibility(View.VISIBLE);
                } else {
                    monInLstHldr.monInLstEditBtn.setVisibility(View.GONE);
                    monInLstHldr.monInLstDelBtn.setVisibility(View.GONE);
                }
                monInLstHldr.monInLstUpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monInLstHldr.monInLstUpdateLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstAmtET = convertView.findViewById(R.id.list5AmtET);
                monInLstHldr.monInLstUpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monInLstHldr.monInLstCancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monInLstHldr.monInLstWarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monInLstHldr.monInLstWarnLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstWarnTV = convertView.findViewById(R.id.list5WarnTV);
                monInLstHldr.monInLstYesBtn = convertView.findViewById(R.id.list5YesBtn);
                monInLstHldr.monInLstNoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monInLstHldr);

            } else {
                monInLstHldr = (MoneyIn2ViewHolder) convertView.getTag();
            }

            monInLstHldr.monInLstCatTV.setText(moneyIns.get(position).getTransBdgtCat());
            monInLstMonInAmt = moneyIns.get(position).getTransAmt();
            monInLstGen.dblASCurrency(String.valueOf(monInLstMonInAmt), monInLstHldr.monInLstAmtTV);
            monInLstHldr.monInLstDateTV.setText(moneyIns.get(position).getTransCreatedOn());
            monInLstHldr.monInLstAcctTV.setText(moneyIns.get(position).getTransToAcctName());

            monInLstHldr.monInLstEditBtn.setTag(moneyIns.get(position));
            monInLstHldr.monInLstDelBtn.setTag(moneyIns.get(position));

            monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();
            monInIsDebt = monInLstDbMgr.findMoneyInIsDebt(moneyIns.get(position).getTransToAcctId());
            monInIsSav = monInLstDbMgr.findMoneyInIsSav(moneyIns.get(position).getTransToAcctId());
            //monInLstSavId = monInLstDbMgr.findMoneyInSavId(moneyIns.get(position).getMoneyInToAcct());

            //click on pencil icon
            monInLstHldr.monInLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLstHldr.monInLstEditBtn.getTag();
                    LayoutMoneyInList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monInLstDbMgr = new DbManager(getContext());

                    monInLstHldr.monInLstUpdateLayout.setVisibility(View.VISIBLE);

                    monInLstHldr.monInLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInLstRefresh = new Intent(getContext(), LayoutMoneyInList.class);
                            monInLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monInLstRefresh);
                        }
                    });

                    monInLstHldr.monInLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monInLstAmtEntry = monInLstGen.dblFromET(monInLstHldr.monInLstAmtET);
                            monInLstMonInAmtDiff = monInLstAmtEntry - moneyIns.get(position).getTransAmt();

                            if (monInIsDebt.equals("N") && monInIsSav.equals("N")) {
                                monInLstDbMgr.updateTotAcctBalPlus(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentAccountBalance());
                                monInLstDbMgr.updateAvailBalMinus(moneyIns.get(position).getTransAmtInA(), moneyIns.get(position).getTransAmtInOwing(), moneyIns.get(position).getTransAmtInB(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentB());

                                monInLstPercentA = monInLstDbMgr.sumTotalAExpenses() / monInLstDbMgr.sumTotalIncome();
                                monInLstMoneyInA = monInLstDbMgr.detAPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInOwing = monInLstDbMgr.detOwingPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());
                                monInLstMoneyInB = monInLstDbMgr.detBPortionInc(monInLstAmtEntry, (monInLstPercentA * monInLstAmtEntry), monInLstDbMgr.retrieveCurrentOwingA());

                                monInLstDbMgr.updateAvailBalPlus(monInLstMoneyInA, monInLstMoneyInOwing, monInLstMoneyInB, monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentB());

                                if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                                    monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                                    newMoneyA = monInLstDbMgr.detNewAPortion(monInLstMoneyInA, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyOwing = monInLstDbMgr.detNewOwingPortion(monInLstMoneyInOwing, monInLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyB = monInLstDbMgr.detNewBPortion(monInLstMoneyInB, monInLstDbMgr.retrieveCurrentOwingA());
                                    moneyInA = newMoneyA;
                                    moneyInOwing = newMoneyOwing;
                                    moneyInB = newMoneyB;
                                } else {
                                    moneyInA = monInLstMoneyInA;
                                    moneyInOwing = monInLstMoneyInOwing;
                                    moneyInB = monInLstMoneyInB;
                                }
                            } else if(monInIsDebt.equals("Y")) {
                                monInLstDbMgr.updateRecMinusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentDebtAmtOwing(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getDebts()) {
                                    if (a.getId() == monInToAcctId) {
                                        debtAmtFromDb = a.getAcctBal();
                                        debtLimitFromDb = a.getAcctMax();
                                        debtRateFromDb = a.getAcctIntRate();
                                        debtPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                        debtAmtFromDb,
                                        debtRateFromDb,
                                        debtPaytFromDb,
                                        getString(R.string.debt_paid),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            } else if(monInIsSav.equals("Y")) {
                                monInLstDbMgr.updateRecPlusPt1(monInLstMonInAmtDiff, monInLstDbMgr.retrieveCurrentSavAmt(monInToAcctId), monInToAcctId);
                                for (AccountsDb a : monInLstDbMgr.getSavings()) {
                                    if (a.getId() == monInToAcctId) {
                                        savGoalFromDb = a.getAcctMax();
                                        savAmtFromDb = a.getAcctBal();
                                        savRateFromDb = a.getAcctIntRate();
                                        savPaytFromDb = a.getAcctPaytsTo();
                                    }
                                }
                                monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), monInToAcctId);
                                moneyInA = 0.0;
                                moneyInOwing = 0.0;
                                moneyInB = 0.0;
                            }

                            monInLstMonInDb.setTransAmt(monInLstAmtEntry);
                            monInLstMonInDb.setTransAmtInA(moneyInA);
                            monInLstMonInDb.setTransAmtInOwing(moneyInOwing);
                            monInLstMonInDb.setTransAmtInB(moneyInB);
                            monInLstDbMgr.updateMoneyIn(monInLstMonInDb);

                            monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                            notifyDataSetChanged();

                            monInLstHelper = new DbHelper(getContext());
                            monInLstDb = monInLstHelper.getWritableDatabase();
                            monInLstCV = new ContentValues();
                            monInLstCV.put(DbHelper.BDGTANNPAYT, monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365)));
                            monInLstDb.update(DbHelper.BUDGET_TABLE_NAME, monInLstCV, DbHelper.ID + "=" + monInLstIncRefKeyMI, null);
                            monInLstDb.close();

                            monInLstRefresh = new Intent(getContext(), LayoutMoneyInList.class);
                            monInLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monInLstRefresh);
                        }
                    });
                }
            });

            //click on trash can icon
            monInLstHldr.monInLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (TransactionsDb) monInLstHldr.monInLstDelBtn.getTag();
                    monInLstIncRefKeyMI = moneyIns.get(position).getTransBdgtId();

                    if(monInIsDebt.equals("N") && monInIsSav.equals("N")) {
                        monInLstDbMgr.updateTotAcctBalMinus(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentAccountBalance());

                        monInLstDbMgr.updateAvailBalMinus(moneyIns.get(position).getTransAmtInA(), moneyIns.get(position).getTransAmtInOwing(), moneyIns.get(position).getTransAmtInB(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentB());
                        if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monInLstDbMgr.adjustCurrentAandB(monInLstDbMgr.retrieveCurrentOwingA(), monInLstDbMgr.retrieveCurrentA(), monInLstDbMgr.retrieveCurrentB());
                        }
                    } else if(monInIsDebt.equals("Y")) {
                        monInLstDbMgr.updateRecPlusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentDebtAmtOwing(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getDebts()) {
                            if (a.getId() == monInToAcctId) {
                                debtAmtFromDb = a.getAcctBal();
                                debtLimitFromDb = a.getAcctMax();
                                debtRateFromDb = a.getAcctIntRate();
                                debtPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcDebtDate(
                                debtAmtFromDb,
                                debtRateFromDb,
                                debtPaytFromDb,
                                getString(R.string.debt_paid),
                                getString(R.string.too_far)), monInToAcctId);
                    } else if(monInIsSav.equals("Y")) {
                        monInLstDbMgr.updateRecMinusPt1(moneyIns.get(position).getTransAmt(), monInLstDbMgr.retrieveCurrentSavAmt(monInToAcctId), monInToAcctId);
                        for (AccountsDb a : monInLstDbMgr.getSavings()) {
                            if (a.getId() == monInToAcctId) {
                                savGoalFromDb = a.getAcctMax();
                                savAmtFromDb = a.getAcctBal();
                                savRateFromDb = a.getAcctIntRate();
                                savPaytFromDb = a.getAcctPaytsTo();
                            }
                        }
                        monInLstDbMgr.updateRecPt2(monInLstGen.calcSavingsDate(
                                savGoalFromDb,
                                savAmtFromDb,
                                savRateFromDb,
                                savPaytFromDb,
                                getString(R.string.goal_achieved),
                                getString(R.string.too_far)), monInToAcctId);
                    }

                    monInLstDbMgr.deleteMoneyIn(monInLstMonInDb);
                    monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                    notifyDataSetChanged();

                    monInLstDbMgr.makeNewIncAnnAmt(monInLstIncRefKeyMI, monInLstGen.lastNumOfDays(365));

                    monInLstRefresh = new Intent(getContext(), LayoutMoneyInList.class);
                    monInLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(monInLstRefresh);
                }
            });
            return convertView;
        }
    }

    private static class MoneyIn2ViewHolder {
        public TextView monInLstCatTV;
        public TextView monInLstAmtTV;
        public TextView monInLstDateTV;
        public TextView monInLstAcctLabelTV;
        public TextView monInLstAcctTV;
        public ImageButton monInLstEditBtn;
        public ImageButton monInLstDelBtn;
        public RelativeLayout monInLstUpdateLayout;
        public EditText monInLstAmtET;
        public Button monInLstUpdateBtn;
        public Button monInLstCancelBtn;
        public LinearLayout monInLstWarnLayout;
        public TextView monInLstWarnTV;
        public Button monInLstYesBtn;
        public Button monInLstNoBtn;
    }
}
