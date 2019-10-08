package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
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

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
//import ca.gotchasomething.mynance.data.SavingsDb;

public class LayoutMoneyOutList extends AppCompatActivity {

    Button monOutLstAddMoreBtn, monOutLstDoneBtn;
    DbManager monOutLstDbMgr;
    Double moneyOutA = 0.0, moneyOutB = 0.0, moneyOutOwing = 0.0, monOutLstAmtEntry = 0.0, monOutLstMoneyOutA = 0.0, monOutLstMoneyOutOwing = 0.0,
            monOutLstMoneyOutB = 0.0, monOutLstMonOutAmt = 0.0, monOutLstMonOutAmtDiff = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0,
            savGoalFromDb = 0.0, savAmtFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0;
    General monOutLstGen;
    Intent monOutLstToMain, monOutLstToAddMonOut, monOutLstRefresh;
    ListView monOutLstList;
    long monOutLstFromAcctId, monOutLstExpRefKeyMO;
    MoneyOutDb monOutLstMonOutDb;
    MonOutLstAdapter monOutLstAdapter;
    String monOutLstIsSav = null;
    TextView monOutLstTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        monOutLstDbMgr = new DbManager(this);
        monOutLstGen = new General();

        monOutLstTitle = findViewById(R.id.layout1HeaderLabelTV);
        monOutLstTitle.setText(getString(R.string.cash_debit_transactions));

        monOutLstAddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        monOutLstAddMoreBtn.setText(getString(R.string.record_cash_debit));
        monOutLstAddMoreBtn.setOnClickListener(onClickMonOutLstAddMoreBtn);

        monOutLstDoneBtn = findViewById(R.id.layout1DoneBtn);
        monOutLstDoneBtn.setOnClickListener(onClickMonOutLstDoneBtn);

        monOutLstList = findViewById(R.id.layout1ListView);
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

    public class MonOutLstAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> moneyOuts;

        private MonOutLstAdapter(
                Context context,
                List<MoneyOutDb> moneyOuts) {

            super(context, -1, moneyOuts);

            this.context = context;
            this.moneyOuts = moneyOuts;
        }

        public void updateMoneyOuts(List<MoneyOutDb> moneyOuts) {
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
                monOutLstHldr.monOutLstCatTV = convertView.findViewById(R.id.list5CatTV);
                monOutLstHldr.monOutLstAmtTV = convertView.findViewById(R.id.list5AmtTV);
                monOutLstHldr.monOutLstDateTV = convertView.findViewById(R.id.list5DateTV);
                monOutLstHldr.monOutLstAcctLabelTV = convertView.findViewById(R.id.list5CCLabel);
                monOutLstHldr.monOutLstAcctLabelTV.setText(R.string.paid_from);
                monOutLstHldr.monOutLstAcctTV = convertView.findViewById(R.id.list5CCTV);
                monOutLstHldr.monOutLstEditBtn = convertView.findViewById(R.id.list5EditBtn);
                monOutLstHldr.monOutLstDelBtn = convertView.findViewById(R.id.list5DelBtn);
                if(monOutLstDbMgr.retrieveLastPageId() == 10) {
                    monOutLstHldr.monOutLstEditBtn.setVisibility(View.VISIBLE);
                    monOutLstHldr.monOutLstDelBtn.setVisibility(View.VISIBLE);
                } else {
                    monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                    monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);
                }
                monOutLstHldr.monOutLstUpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtET = convertView.findViewById(R.id.list5AmtET);
                monOutLstHldr.monOutLstUpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monOutLstHldr.monOutLstCancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monOutLstHldr.monOutLstWarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monOutLstHldr.monOutLstWarnLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstWarnTV = convertView.findViewById(R.id.list5WarnTV);
                monOutLstHldr.monOutLstYesBtn = convertView.findViewById(R.id.list5YesBtn);
                monOutLstHldr.monOutLstNoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monOutLstHldr);

            } else {
                monOutLstHldr = (MoneyOut2ViewHolder) convertView.getTag();
            }

            if(moneyOuts.get(position).getMoneyOutCC().equals("Y")) {
                monOutLstHldr.monOutLstCatTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDateTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctLabelTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAcctTV.setVisibility(View.GONE);
                monOutLstHldr.monOutLstEditBtn.setVisibility(View.GONE);
                monOutLstHldr.monOutLstDelBtn.setVisibility(View.GONE);
            }

            monOutLstHldr.monOutLstCatTV.setText(moneyOuts.get(position).getMoneyOutCat());
            monOutLstMonOutAmt = moneyOuts.get(position).getMoneyOutAmount();
            monOutLstGen.dblASCurrency(String.valueOf(monOutLstMonOutAmt), monOutLstHldr.monOutLstAmtTV);
            monOutLstHldr.monOutLstDateTV.setText(moneyOuts.get(position).getMoneyOutCreatedOn());
            monOutLstHldr.monOutLstAcctTV.setText(moneyOuts.get(position).getMoneyOutPayFromName());

            monOutLstHldr.monOutLstEditBtn.setTag(moneyOuts.get(position));
            monOutLstHldr.monOutLstDelBtn.setTag(moneyOuts.get(position));

            monOutLstExpRefKeyMO = moneyOuts.get(position).getExpRefKeyMO();
            monOutLstIsSav = monOutLstDbMgr.findMoneyInIsSav(moneyOuts.get(position).getMoneyOutPayFromId());

            //click on pencil icon
            monOutLstHldr.monOutLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (MoneyOutDb) monOutLstHldr.monOutLstEditBtn.getTag();
                    LayoutMoneyOutList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monOutLstDbMgr = new DbManager(getContext());

                    monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.VISIBLE);

                    monOutLstHldr.monOutLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutLstRefresh = new Intent(getContext(), LayoutMoneyOutList.class);
                            monOutLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monOutLstRefresh);
                        }
                    });

                    monOutLstHldr.monOutLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monOutLstAmtEntry = monOutLstGen.dblFromET(monOutLstHldr.monOutLstAmtET);
                            monOutLstMonOutAmtDiff = monOutLstAmtEntry - moneyOuts.get(position).getMoneyOutAmount();

                            if(monOutLstIsSav.equals("N")) {
                                monOutLstDbMgr.updateTotAcctBalMinus(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentAccountBalance());

                                monOutLstDbMgr.updateAvailBalPlus(moneyOuts.get(position).getMoneyOutA(), moneyOuts.get(position).getMoneyOutOwing(), moneyOuts.get(position).getMoneyOutB(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());

                                monOutLstMoneyOutA = monOutLstDbMgr.detAPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getMoneyOutPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                monOutLstMoneyOutOwing = monOutLstDbMgr.detOwingPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getMoneyOutPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                monOutLstMoneyOutB = monOutLstDbMgr.detBPortionExp(monOutLstAmtEntry, moneyOuts.get(position).getMoneyOutPriority(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());

                                monOutLstDbMgr.updateAvailBalMinus(monOutLstMoneyOutA, monOutLstMoneyOutOwing, monOutLstMoneyOutB, monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());

                                if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                                    monOutLstDbMgr.adjustCurrentAandB(monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                                    newMoneyA = monOutLstDbMgr.detNewAPortion(monOutLstMoneyOutA, monOutLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyOwing = monOutLstDbMgr.detNewOwingPortion(monOutLstMoneyOutOwing, monOutLstDbMgr.retrieveCurrentOwingA());
                                    newMoneyB = monOutLstDbMgr.detNewBPortion(monOutLstMoneyOutB, monOutLstDbMgr.retrieveCurrentOwingA());
                                    moneyOutA = newMoneyA;
                                    moneyOutOwing = newMoneyOwing;
                                    moneyOutB = newMoneyB;
                                } else {
                                    moneyOutA = monOutLstMoneyOutA;
                                    moneyOutOwing = monOutLstMoneyOutOwing;
                                    moneyOutB = monOutLstMoneyOutB;
                                }
                            } else {
                                monOutLstDbMgr.updateSavRecMinusPt1(monOutLstMonOutAmtDiff, monOutLstDbMgr.retrieveCurrentSavAmt(monOutLstFromAcctId), monOutLstFromAcctId);
                                for (AccountsDb a : monOutLstDbMgr.getSavings()) {
                                    if (a.getId() == monOutLstFromAcctId) {
                                        savGoalFromDb = a.getAcctMax();
                                        savAmtFromDb = a.getAcctBal();
                                        savRateFromDb = a.getIntRate();
                                        savPaytFromDb = a.getPaytsTo();
                                    }
                                }
                                monOutLstDbMgr.updateSavRecPt2(monOutLstGen.calcSavingsDate(
                                        savGoalFromDb,
                                        savAmtFromDb,
                                        savRateFromDb,
                                        savPaytFromDb,
                                        getString(R.string.goal_achieved),
                                        getString(R.string.too_far)), monOutLstFromAcctId);
                                moneyOutA = 0.0;
                                moneyOutOwing = 0.0;
                                moneyOutB = 0.0;
                            }

                            monOutLstMonOutDb.setMoneyOutAmount(monOutLstAmtEntry);
                            monOutLstMonOutDb.setMoneyOutA(moneyOutA);
                            monOutLstMonOutDb.setMoneyOutOwing(moneyOutOwing);
                            monOutLstMonOutDb.setMoneyOutB(moneyOutB);
                            monOutLstDbMgr.updateMoneyOut(monOutLstMonOutDb);

                            monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                            notifyDataSetChanged();

                            monOutLstDbMgr.makeNewExpAnnAmt(monOutLstExpRefKeyMO, monOutLstGen.lastNumOfDays(365));

                            monOutLstRefresh = new Intent(getContext(), LayoutMoneyOutList.class);
                            monOutLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monOutLstRefresh);
                        }
                    });
                }
            });

            //click on trash can icon
            monOutLstHldr.monOutLstDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (MoneyOutDb) monOutLstHldr.monOutLstDelBtn.getTag();
                    monOutLstExpRefKeyMO = moneyOuts.get(position).getExpRefKeyMO();

                    if(monOutLstIsSav.equals("N")) {
                        monOutLstDbMgr.updateTotAcctBalPlus(moneyOuts.get(position).getMoneyOutAmount(), monOutLstDbMgr.retrieveCurrentAccountBalance());

                        monOutLstDbMgr.updateAvailBalPlus(moneyOuts.get(position).getMoneyOutA(), moneyOuts.get(position).getMoneyOutOwing(), moneyOuts.get(position).getMoneyOutB(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentB());
                        if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                            monOutLstDbMgr.adjustCurrentAandB(monOutLstDbMgr.retrieveCurrentOwingA(), monOutLstDbMgr.retrieveCurrentA(), monOutLstDbMgr.retrieveCurrentB());
                        }
                    } else {
                        monOutLstDbMgr.updateSavRecPlusPt1(moneyOuts.get(position).getMoneyOutAmount(), monOutLstDbMgr.retrieveCurrentSavAmt(monOutLstFromAcctId), monOutLstFromAcctId);
                        for (AccountsDb a : monOutLstDbMgr.getSavings()) {
                            if (a.getId() == monOutLstFromAcctId) {
                                savGoalFromDb = a.getAcctMax();
                                savAmtFromDb = a.getAcctBal();
                                savRateFromDb = a.getIntRate();
                                savPaytFromDb = a.getPaytsTo();
                            }
                        }
                        monOutLstDbMgr.updateSavRecPt2(monOutLstGen.calcSavingsDate(
                                savGoalFromDb,
                                savAmtFromDb,
                                savRateFromDb,
                                savPaytFromDb,
                                getString(R.string.goal_achieved),
                                getString(R.string.too_far)), monOutLstFromAcctId);
                    }

                    monOutLstDbMgr.deleteMoneyOut(monOutLstMonOutDb);
                    monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                    notifyDataSetChanged();

                    monOutLstDbMgr.makeNewExpAnnAmt(monOutLstExpRefKeyMO, monOutLstGen.lastNumOfDays(365));

                    monOutLstRefresh = new Intent(getContext(), LayoutMoneyOutList.class);
                    monOutLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(monOutLstRefresh);
                }
            });
            return convertView;
        }
    }

    private static class MoneyOut2ViewHolder {
        public TextView monOutLstCatTV;
        public TextView monOutLstAmtTV;
        public TextView monOutLstDateTV;
        public TextView monOutLstAcctLabelTV;
        public TextView monOutLstAcctTV;
        public ImageButton monOutLstEditBtn;
        public ImageButton monOutLstDelBtn;
        public RelativeLayout monOutLstUpdateLayout;
        public EditText monOutLstAmtET;
        public Button monOutLstUpdateBtn;
        public Button monOutLstCancelBtn;
        public LinearLayout monOutLstWarnLayout;
        public TextView monOutLstWarnTV;
        public Button monOutLstYesBtn;
        public Button monOutLstNoBtn;
    }
}
