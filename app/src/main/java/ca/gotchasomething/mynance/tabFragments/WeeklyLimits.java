/*package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.LayoutWeeklyLimitsList;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.MainActivity;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;

public class WeeklyLimits extends Fragment {

    Button wee2AddMoreBtn, wee2DoneBtn;
    DbManager wee2DbMgr;
    Double wee2AmtLeft = 0.0, wee2SpentThisWeek = 0.0, wee2StartBal = 0.0;
    General wee2Gen;
    Intent wee2ToMain, wee2ToAddMore;
    ListView wee2ListView;
    long wee2ExpId;
    TextView wee2HeaderLabel;
    View v;
    Wee2LstAdapter wee2LstAdapter;

    public WeeklyLimits() {
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

        wee2DbMgr = new DbManager(getContext());
        wee2Gen = new General();

        wee2HeaderLabel = v.findViewById(R.id.frag4HeaderLabelTV);
        wee2HeaderLabel.setText(getString(R.string.weekly_limits));
        wee2AddMoreBtn = v.findViewById(R.id.frag4AddMoreBtn);
        wee2AddMoreBtn.setText(getString(R.string.add_edit_weekly_limits));
        wee2DoneBtn = v.findViewById(R.id.frag4DoneBtn);
        wee2DoneBtn.setText(getString(R.string.done));

        wee2AddMoreBtn.setOnClickListener(onclickWee2AddMoreBtn);
        wee2DoneBtn.setOnClickListener(onclickWee2DoneBtn);

        wee2ListView = v.findViewById(R.id.frag4ListView);
        wee2LstAdapter = new Wee2LstAdapter(getContext(), wee2DbMgr.getWeeklyLimits());
        wee2ListView.setAdapter(wee2LstAdapter);
    }

    View.OnClickListener onclickWee2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wee2ToAddMore = new Intent(getContext(), LayoutWeeklyLimitsList.class);
            wee2ToAddMore.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(wee2ToAddMore);
        }
    };

    View.OnClickListener onclickWee2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wee2ToMain = new Intent(getContext(), MainActivity.class);
            wee2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(wee2ToMain);
        }
    };

    public class Wee2LstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        public Context context;
        public List<ExpenseBudgetDb> weeklyLimits;

        public Wee2LstAdapter(
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

            final Wee2ViewHolder wee2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_weekly_limits,
                        parent, false);

                wee2Hldr = new Wee2ViewHolder();
                wee2Hldr.wee2CatTV = convertView.findViewById(R.id.wklyLmtsCatTV);
                wee2Hldr.wee2AmtLeftTV = convertView.findViewById(R.id.wklyLmtsAmtLeftTV);
                wee2Hldr.wee2SpentAmtTV = convertView.findViewById(R.id.wklyLmtsSpentAmtTV);
                wee2Hldr.wee2AmtLeftLabel = convertView.findViewById(R.id.wklyLmtsAmtLeftLabel);
                wee2Hldr.wee2AmtLeftWarn = convertView.findViewById(R.id.wklyLmtsAmtLeftWarn);
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.GONE);
                convertView.setTag(wee2Hldr);

            } else {
                wee2Hldr = (Wee2ViewHolder) convertView.getTag();
            }

            //retrieve spendingCategory
            wee2Hldr.wee2CatTV.setText(weeklyLimits.get(position).getExpenseName());

            //retrieve amount spent in this category during general.thisWeek();
            wee2ExpId = weeklyLimits.get(position).getId();

            List<Double> spentThisWeekList = new ArrayList<>();
            for(MoneyOutDb m : wee2DbMgr.getMoneyOuts()) {
                if(String.valueOf(m.getExpRefKeyMO()).equals(String.valueOf(wee2ExpId)) && wee2Gen.thisWeek().contains(m.getMoneyOutCreatedOn())) {
                    spentThisWeekList.add(m.getMoneyOutAmount());
                }
            }
            wee2SpentThisWeek = 0.0;
            if(spentThisWeekList.size() == 0) {
                wee2SpentThisWeek = 0.0;
            } else {
                for(Double dbl : spentThisWeekList) {
                    wee2SpentThisWeek += dbl;
                }
            }

            wee2Gen.dblASCurrency(String.valueOf(wee2SpentThisWeek), wee2Hldr.wee2SpentAmtTV);

            //retrieve amountLeft
            wee2StartBal = weeklyLimits.get(position).getExpenseBAnnualAmount() / 52;
            wee2AmtLeft = wee2StartBal - wee2SpentThisWeek;
            wee2Gen.dblASCurrency(String.valueOf(wee2AmtLeft), wee2Hldr.wee2AmtLeftTV);

            if(wee2AmtLeft <= 0) {
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.VISIBLE);
                wee2Hldr.wee2AmtLeftLabel.setVisibility(View.GONE);
            } else {
                wee2Hldr.wee2AmtLeftWarn.setVisibility(View.GONE);
                wee2Hldr.wee2AmtLeftLabel.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    private static class Wee2ViewHolder {
        private TextView wee2CatTV;
        private TextView wee2AmtLeftTV;
        private TextView wee2SpentAmtTV;
        private TextView wee2AmtLeftLabel;
        private TextView wee2AmtLeftWarn;
    }
}*/
