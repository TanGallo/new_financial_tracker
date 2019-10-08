/*package ca.gotchasomething.mynance.tabFragments;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.MainActivity;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyMoneyOutList extends Fragment {

    Button monOutLstAddMoreBtn, monOutLstDoneBtn;
    DbManager monOutLstDbMgr;
    Double moneyOutA = 0.0, moneyOutB = 0.0, moneyOutOwing = 0.0, monOutLstAmtEntry = 0.0, monOutLstMonOutAmt = 0.0, monOutLstMonOutAmtDiff = 0.0,
            newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0;
    General monOutLstGen;
    Intent monOutLstToMain, monOutLstToAddMonOut, monOutLstRefresh;
    ListView monOutLstList;
    long monOutLstExpRefKeyMO;
    MoneyOutDb monOutLstMonOutDb;
    MonOutLstAdapter monOutLstAdapter;
    TextView monOutLstTitle;
    View v;

    public DailyMoneyOutList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_2_list_add_done, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monOutLstDbMgr = new DbManager(getContext());
        monOutLstGen = new General();

        monOutLstTitle = v.findViewById(R.id.frag4HeaderLabelTV);
        monOutLstTitle.setText(getString(R.string.cash_debit_transactions));

        monOutLstAddMoreBtn = v.findViewById(R.id.frag4AddMoreBtn);
        monOutLstAddMoreBtn.setText(getString(R.string.record_cash_debit));
        monOutLstAddMoreBtn.setOnClickListener(onClickMonOutLstAddMoreBtn);

        monOutLstDoneBtn = v.findViewById(R.id.frag4DoneBtn);
        monOutLstDoneBtn.setOnClickListener(onClickMonOutLstDoneBtn);

        monOutLstList = v.findViewById(R.id.frag4ListView);
        monOutLstAdapter = new MonOutLstAdapter(getContext(), monOutLstDbMgr.getCashTrans());
        monOutLstList.setAdapter(monOutLstAdapter);
    }

    View.OnClickListener onClickMonOutLstDoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToMain = new Intent(getContext(), MainActivity.class);
            monOutLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monOutLstToMain);
        }
    };

    View.OnClickListener onClickMonOutLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monOutLstToAddMonOut = new Intent(getContext(), DailyMoneyOut.class);
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
                        R.layout.frag_list_4_3tv_edit_del,
                        parent, false);

                monOutLstHldr = new MoneyOut2ViewHolder();
                monOutLstHldr.monOutLstCatTV = convertView.findViewById(R.id.list4CatTV);
                monOutLstHldr.monOutLstAmtTV = convertView.findViewById(R.id.list4AmtTV);
                monOutLstHldr.monOutLstDateTV = convertView.findViewById(R.id.list4DateTV);
                monOutLstHldr.monOutLstEditBtn = convertView.findViewById(R.id.list4EditBtn);
                monOutLstHldr.monOutLstDelBtn = convertView.findViewById(R.id.list4DelBtn);
                monOutLstHldr.monOutLstUpdateLayout = convertView.findViewById(R.id.list4UpdateLayout);
                monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstAmtET = convertView.findViewById(R.id.list4AmtET);
                monOutLstHldr.monOutLstUpdateBtn = convertView.findViewById(R.id.list4UpdateBtn);
                monOutLstHldr.monOutLstCancelBtn = convertView.findViewById(R.id.list4CancelBtn);
                monOutLstHldr.monOutLstWarnLayout = convertView.findViewById(R.id.list4WarnLayout);
                monOutLstHldr.monOutLstWarnLayout.setVisibility(View.GONE);
                monOutLstHldr.monOutLstWarnTV = convertView.findViewById(R.id.list4WarnTV);
                monOutLstHldr.monOutLstYesBtn = convertView.findViewById(R.id.list4YesBtn);
                monOutLstHldr.monOutLstNoBtn = convertView.findViewById(R.id.list4NoBtn);

                convertView.setTag(monOutLstHldr);

            } else {
                monOutLstHldr = (MoneyOut2ViewHolder) convertView.getTag();
            }

            monOutLstHldr.monOutLstCatTV.setText(moneyOuts.get(position).getMoneyOutCat());
            monOutLstMonOutAmt = moneyOuts.get(position).getMoneyOutAmount();
            monOutLstGen.dblASCurrency(String.valueOf(monOutLstMonOutAmt), monOutLstHldr.monOutLstAmtTV);
            monOutLstHldr.monOutLstDateTV.setText(moneyOuts.get(position).getMoneyOutCreatedOn());
            monOutLstHldr.monOutLstEditBtn.setTag(moneyOuts.get(position));
            monOutLstHldr.monOutLstDelBtn.setTag(moneyOuts.get(position));

            monOutLstExpRefKeyMO = moneyOuts.get(position).getExpRefKeyMO();

            //click on pencil icon
            monOutLstHldr.monOutLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monOutLstMonOutDb = (MoneyOutDb) monOutLstHldr.monOutLstEditBtn.getTag();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monOutLstDbMgr = new DbManager(getContext());

                    monOutLstHldr.monOutLstUpdateLayout.setVisibility(View.VISIBLE);

                    monOutLstHldr.monOutLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monOutLstRefresh = new Intent(getContext(), DailyMoneyOutList.class);
                            monOutLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monOutLstRefresh);
                        }
                    });

                    monOutLstHldr.monOutLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monOutLstAmtEntry = monOutLstGen.dblFromET(monOutLstHldr.monOutLstAmtET);
                            monOutLstMonOutAmtDiff = monOutLstAmtEntry - moneyOuts.get(position).getMoneyOutAmount();

                            monOutLstDbMgr.updateTotAcctBalMinus(monOutLstMonOutAmtDiff);

                            monOutLstDbMgr.updateAvailBalPlus(moneyOuts.get(position).getMoneyOutA(), moneyOuts.get(position).getMoneyOutOwing(), moneyOuts.get(position).getMoneyOutB());
                            monOutLstDbMgr.detAandBPortionsExp(monOutLstAmtEntry, moneyOuts.get(position).getMoneyOutPriority());
                            monOutLstDbMgr.updateAvailBalMinus(moneyOutA, moneyOutOwing, moneyOutB);

                            if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                                monOutLstDbMgr.adjustAandBPortions(moneyOutA, moneyOutOwing, moneyOutB);
                                moneyOutA = newMoneyA;
                                moneyOutOwing = newMoneyOwing;
                                moneyOutB = newMoneyB;
                            }

                            monOutLstMonOutDb.setMoneyOutAmount(monOutLstAmtEntry);
                            monOutLstMonOutDb.setMoneyOutA(moneyOutA);
                            monOutLstMonOutDb.setMoneyOutOwing(moneyOutOwing);
                            monOutLstMonOutDb.setMoneyOutB(moneyOutB);
                            monOutLstDbMgr.updateMoneyOut(monOutLstMonOutDb);

                            monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                            notifyDataSetChanged();

                            monOutLstDbMgr.updateExpAnnAmt(moneyOuts.get(position).getExpRefKeyMO());

                            monOutLstRefresh = new Intent(getContext(), DailyMoneyOutList.class);
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

                    monOutLstDbMgr.updateTotAcctBalPlus(moneyOuts.get(position).getMoneyOutAmount());

                    monOutLstDbMgr.updateAvailBalPlus(moneyOuts.get(position).getMoneyOutA(), moneyOuts.get(position).getMoneyOutOwing(), moneyOuts.get(position).getMoneyOutB());
                    if (monOutLstDbMgr.retrieveCurrentOwingA() < 0) {
                        monOutLstDbMgr.adjustAandBPortions(0.0, 0.0, 0.0);
                    }

                    monOutLstDbMgr.deleteMoneyOut(monOutLstMonOutDb);
                    monOutLstAdapter.updateMoneyOuts(monOutLstDbMgr.getMoneyOuts());
                    notifyDataSetChanged();

                    monOutLstDbMgr.updateExpAnnAmt(monOutLstExpRefKeyMO);

                    monOutLstRefresh = new Intent(getContext(), DailyMoneyOutList.class);
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
        public Button monOutLstEditBtn;
        public Button monOutLstDelBtn;
        public RelativeLayout monOutLstUpdateLayout;
        public EditText monOutLstAmtET;
        public Button monOutLstUpdateBtn;
        public Button monOutLstCancelBtn;
        public LinearLayout monOutLstWarnLayout;
        public TextView monOutLstWarnTV;
        public Button monOutLstYesBtn;
        public Button monOutLstNoBtn;
    }
}*/
