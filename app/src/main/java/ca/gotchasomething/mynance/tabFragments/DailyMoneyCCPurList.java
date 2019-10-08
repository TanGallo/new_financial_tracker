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
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class DailyMoneyCCPurList extends Fragment {

    Button monCC2AddMoreBtn, monCC2DoneBtn;
    DbManager monCC2DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, monCC2AmtEntry = 0.0, monCC2MonOutAmt = 0.0,
            monCC2MonOutAmtDiff = 0.0;
    General monCC2Gen;
    Intent monCC2ToMain, monCC2ToAddMonOut, monCC2Refresh;
    ListView monCC2List;
    long monCC2ExpRefKeyMO, monCC2MonOutchargingDebtId;
    MoneyOutDb monCC2MonOutDb;
    MonCC2Adapter monCC2Adapter;
    TextView monCC2Title;
    View v;

    public DailyMoneyCCPurList() {
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

        monCC2DbMgr = new DbManager(getContext());
        monCC2Gen = new General();

        monCC2Title = v.findViewById(R.id.frag4HeaderLabelTV);
        monCC2Title.setText(getString(R.string.credit_card_purchases_2));

        monCC2AddMoreBtn = v.findViewById(R.id.frag4AddMoreBtn);
        monCC2AddMoreBtn.setText(getString(R.string.record_credit_card));
        monCC2AddMoreBtn.setOnClickListener(onClickMonCC2AddMoreBtn);

        monCC2DoneBtn = v.findViewById(R.id.frag4DoneBtn);
        monCC2DoneBtn.setOnClickListener(onClickMonCC2DoneBtn);

        monCC2List = v.findViewById(R.id.frag4ListView);
        monCC2Adapter = new MonCC2Adapter(getContext(), monCC2DbMgr.getCCTrans());
        monCC2List.setAdapter(monCC2Adapter);
    }

    View.OnClickListener onClickMonCC2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToMain = new Intent(getContext(), MainActivity.class);
            monCC2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToMain);
        }
    };

    View.OnClickListener onClickMonCC2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC2ToAddMonOut = new Intent(getContext(), DailyMoneyCCPur.class);
            monCC2ToAddMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC2ToAddMonOut);
        }
    };

    public class MonCC2Adapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private MonCC2Adapter(
                Context context,
                List<MoneyOutDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<MoneyOutDb> ccTrans) {
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
                monCC2Hldr.monCC2CatTV = convertView.findViewById(R.id.list5CatTV);
                monCC2Hldr.monCC2AmtTV = convertView.findViewById(R.id.list5AmtTV);
                monCC2Hldr.monCC2DateTV = convertView.findViewById(R.id.list5DateTV);
                monCC2Hldr.monCC2CCTV = convertView.findViewById(R.id.list5CCTV);
                monCC2Hldr.monCC2EditBtn = convertView.findViewById(R.id.list5EditBtn);
                monCC2Hldr.monCC2DelBtn = convertView.findViewById(R.id.list5DelBtn);
                monCC2Hldr.monCC2UpdateLayout = convertView.findViewById(R.id.list5UpdateLayout);
                monCC2Hldr.monCC2UpdateLayout.setVisibility(View.GONE);
                monCC2Hldr.monCC2AmtET = convertView.findViewById(R.id.list5AmtET);
                monCC2Hldr.monCC2UpdateBtn = convertView.findViewById(R.id.list5UpdateBtn);
                monCC2Hldr.monCC2CancelBtn = convertView.findViewById(R.id.list5CancelBtn);
                monCC2Hldr.monCC2WarnLayout = convertView.findViewById(R.id.list5WarnLayout);
                monCC2Hldr.monCC2WarnLayout.setVisibility(View.GONE);
                monCC2Hldr.monCC2WarnTV = convertView.findViewById(R.id.list5WarnTV);
                monCC2Hldr.monCC2YesBtn = convertView.findViewById(R.id.list5YesBtn);
                monCC2Hldr.monCC2NoBtn = convertView.findViewById(R.id.list5NoBtn);

                convertView.setTag(monCC2Hldr);

            } else {
                monCC2Hldr = (MoneyCC2ViewHolder) convertView.getTag();
            }

            monCC2Hldr.monCC2CatTV.setText(ccTrans.get(position).getMoneyOutCat());
            monCC2MonOutAmt = ccTrans.get(position).getMoneyOutAmount();
            monCC2Gen.dblASCurrency(String.valueOf(monCC2MonOutAmt), monCC2Hldr.monCC2AmtTV);
            monCC2Hldr.monCC2DateTV.setText(ccTrans.get(position).getMoneyOutCreatedOn());
            monCC2Hldr.monCC2CCTV.setText(ccTrans.get(position).getMoneyOutDebtCat());
            monCC2Hldr.monCC2EditBtn.setTag(ccTrans.get(position));
            monCC2Hldr.monCC2DelBtn.setTag(ccTrans.get(position));

            monCC2ExpRefKeyMO = ccTrans.get(position).getExpRefKeyMO();

            //click on pencil icon
            monCC2Hldr.monCC2EditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monCC2MonOutDb = (MoneyOutDb) monCC2Hldr.monCC2EditBtn.getTag();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monCC2DbMgr = new DbManager(getContext());

                    monCC2Hldr.monCC2UpdateLayout.setVisibility(View.VISIBLE);

                    monCC2Hldr.monCC2CancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monCC2Refresh = new Intent(getContext(), DailyMoneyCCPurList.class);
                            monCC2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monCC2Refresh);
                        }
                    });

                    monCC2Hldr.monCC2UpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monCC2AmtEntry = monCC2Gen.dblFromET(monCC2Hldr.monCC2AmtET);
                            monCC2MonOutAmtDiff = monCC2AmtEntry - ccTrans.get(position).getMoneyOutAmount();
                            monCC2MonOutchargingDebtId = ccTrans.get(position).getMoneyOutChargingDebtId();

                            for (DebtDb d : monCC2DbMgr.getDebts()) {
                                if (String.valueOf(d.getId()).equals(monCC2MonOutchargingDebtId)) {
                                    debtAmtFromDb = d.getDebtOwing();
                                    debtLimitFromDb = d.getDebtLimit();
                                    debtRateFromDb = d.getDebtRate();
                                    debtPaytFromDb = d.getDebtPayments();
                                }
                            }
                            if (debtAmtFromDb + monCC2MonOutAmtDiff > debtLimitFromDb) {
                                monCC2Hldr.monCC2WarnLayout.setVisibility(View.VISIBLE);
                                monCC2Hldr.monCC2WarnTV.setText(getString(R.string.not_enough_credit_warning));
                                monCC2Hldr.monCC2NoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC2Refresh = new Intent(getContext(), DailyMoneyCCPurList.class);
                                        monCC2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        startActivity(monCC2Refresh);
                                    }
                                });

                                monCC2Hldr.monCC2YesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC2Hldr.monCC2WarnLayout.setVisibility(View.GONE);
                                    }
                                });
                            }

                            monCC2DbMgr.updateDebtRecPlusPt1(monCC2MonOutAmtDiff, debtAmtFromDb, monCC2MonOutchargingDebtId);
                            for (DebtDb d : monCC2DbMgr.getDebts()) {
                                if (String.valueOf(d.getId()).equals(monCC2MonOutchargingDebtId)) {
                                    debtAmtFromDb = d.getDebtOwing();
                                    debtLimitFromDb = d.getDebtLimit();
                                    debtRateFromDb = d.getDebtRate();
                                    debtPaytFromDb = d.getDebtPayments();
                                }
                            }
                            monCC2DbMgr.updateDebtRecPt2(monCC2Gen.calcDebtDate(
                                    debtAmtFromDb,
                                    debtRateFromDb,
                                    debtPaytFromDb,
                                    getString(R.string.debt_paid),
                                    getString(R.string.too_far)), monCC2MonOutchargingDebtId);

                            monCC2MonOutDb.setMoneyOutAmount(monCC2AmtEntry);
                            monCC2DbMgr.updateMoneyOut(monCC2MonOutDb);

                            monCC2Adapter.updateCCTrans(monCC2DbMgr.getCCTrans());
                            notifyDataSetChanged();

                            monCC2DbMgr.updateExpAnnAmt(ccTrans.get(position).getExpRefKeyMO());

                            monCC2Refresh = new Intent(getContext(), DailyMoneyCCPurList.class);
                            monCC2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monCC2Refresh);
                        }
                    });
                }
            });

            //click on trash can icon
            monCC2Hldr.monCC2DelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monCC2MonOutDb = (MoneyOutDb) monCC2Hldr.monCC2DelBtn.getTag();
                    monCC2ExpRefKeyMO = ccTrans.get(position).getExpRefKeyMO();
                    monCC2MonOutchargingDebtId = ccTrans.get(position).getMoneyOutChargingDebtId();

                    for (DebtDb d : monCC2DbMgr.getDebts()) {
                        if (String.valueOf(d.getId()).equals(monCC2MonOutchargingDebtId)) {
                            debtAmtFromDb = d.getDebtOwing();
                            debtLimitFromDb = d.getDebtLimit();
                            debtRateFromDb = d.getDebtRate();
                            debtPaytFromDb = d.getDebtPayments();
                        }
                    }
                    monCC2DbMgr.updateDebtRecMinusPt1(ccTrans.get(position).getMoneyOutAmount(), debtAmtFromDb, monCC2MonOutchargingDebtId);
                    for (DebtDb d : monCC2DbMgr.getDebts()) {
                        if (String.valueOf(d.getId()).equals(monCC2MonOutchargingDebtId)) {
                            debtAmtFromDb = d.getDebtOwing();
                            debtLimitFromDb = d.getDebtLimit();
                            debtRateFromDb = d.getDebtRate();
                            debtPaytFromDb = d.getDebtPayments();
                        }
                    }
                    monCC2DbMgr.updateDebtRecPt2(monCC2Gen.calcDebtDate(
                            debtAmtFromDb,
                            debtRateFromDb,
                            debtPaytFromDb,
                            getString(R.string.debt_paid),
                            getString(R.string.too_far)), monCC2MonOutchargingDebtId);

                    monCC2DbMgr.deleteMoneyOut(monCC2MonOutDb);
                    monCC2Adapter.updateCCTrans(monCC2DbMgr.getCCTrans());
                    notifyDataSetChanged();

                    monCC2DbMgr.updateExpAnnAmt(monCC2ExpRefKeyMO);

                    monCC2Refresh = new Intent(getContext(), DailyMoneyCCPurList.class);
                    monCC2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(monCC2Refresh);
                }
            });
            return convertView;
        }
    }

    private static class MoneyCC2ViewHolder {
        public TextView monCC2CatTV;
        public TextView monCC2AmtTV;
        public TextView monCC2DateTV;
        public TextView monCC2CCTV;
        public Button monCC2EditBtn;
        public Button monCC2DelBtn;
        public RelativeLayout monCC2UpdateLayout;
        public EditText monCC2AmtET;
        public Button monCC2UpdateBtn;
        public Button monCC2CancelBtn;
        public LinearLayout monCC2WarnLayout;
        public TextView monCC2WarnTV;
        public Button monCC2YesBtn;
        public Button monCC2NoBtn;
    }
}*/
