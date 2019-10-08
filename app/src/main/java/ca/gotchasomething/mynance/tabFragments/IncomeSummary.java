/*package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.MoneyInDb;

public class IncomeSummary extends Fragment {

    ArrayAdapter incSumSpinAdapter;
    Button incSumCreateBtn;
    DbManager incSumDbMgr;
    Double incSumIncAnnAmt = 0.0;
    General incSumGen;
    ListView incSumListView;
    long incSumIncId;
    IncSumLstAdapter incSumLstAdapter;
    Spinner incSumChooseSpin;
    String incSumSpentDate = null, incSumYr = null;
    String[] incSumYears;
    TextView incSumChooseLabel;
    View v;

    public IncomeSummary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_3_choose_create_spinner, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incSumDbMgr = new DbManager(getContext());
        incSumGen = new General();

        incSumChooseLabel = v.findViewById(R.id.frag3ChooseLabel);
        incSumChooseLabel.setText(getString(R.string.choose_year));
        incSumCreateBtn = v.findViewById(R.id.frag3CreateBtn);

        incSumCreateBtn.setOnClickListener(onClickIncSumCreateBtn);

        incSumListView = v.findViewById(R.id.frag3ListView);
        incSumListView.setVisibility(View.GONE);

        incSumChooseSpin = v.findViewById(R.id.frag3ChooseSpin);
        List<String> yearsList2 = new ArrayList<>();
        for (int firstYear = incSumDbMgr.getEarliestEntry(); firstYear <= incSumDbMgr.getLatestEntry(); firstYear++) {
            yearsList2.add(String.valueOf(firstYear));
        }
        incSumYears = yearsList2.toArray(new String[0]);
        incSumSpinAdapter = new ArrayAdapter(getContext(), R.layout.layout_1_spinner, R.id.spinnerText, incSumYears);
        incSumChooseSpin.setAdapter(incSumSpinAdapter);
        incSumChooseSpin.setOnItemSelectedListener(onYearSelected);

    }

    Spinner.OnItemSelectedListener onYearSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            incSumYr = String.valueOf(incSumChooseSpin.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickIncSumCreateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incSumLstAdapter = new IncSumLstAdapter(getContext(), incSumDbMgr.getIncomes());
            incSumListView.setAdapter(incSumLstAdapter);
            incSumListView.setVisibility(View.VISIBLE);
        }
    };

    public class IncSumLstAdapter extends ArrayAdapter<IncomeBudgetDb> {

        private Context context;
        private List<IncomeBudgetDb> incomes;

        private IncSumLstAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncSum(List<IncomeBudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final incSumViewHolder incSumHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_6_2tv,
                        parent, false);

                incSumHldr = new incSumViewHolder();
                incSumHldr.incSumCatTV = convertView.findViewById(R.id.list6CatTV);
                incSumHldr.incSumAmtTV = convertView.findViewById(R.id.list6AmtTV);
                convertView.setTag(incSumHldr);

            } else {
                incSumHldr = (incSumViewHolder) convertView.getTag();
            }

            incSumIncId = incomes.get(position).getId();

            //retrieve spending category
            incSumHldr.incSumCatTV.setText(incomes.get(position).getIncomeName());

            //retrieve total budgeted per category
            incSumIncAnnAmt = incomes.get(position).getIncomeAnnualAmount();
            incSumGen.dblASCurrency(String.valueOf(incSumIncAnnAmt), incSumHldr.incSumAmtTV);

            //retrieve amount spent in each category
            List<Double> spentPerCategory = new ArrayList<>();
            for (MoneyInDb m2 : incSumDbMgr.getMoneyIns()) {
                incSumSpentDate = m2.getMoneyInCreatedOn();
                if (String.valueOf(m2.getIncRefKeyMI()).equals(String.valueOf(incSumIncId)) && incSumSpentDate.contains(incSumYr)) {
                    spentPerCategory.add(m2.getMoneyInAmount());
                }
            }

            return convertView;
        }
    }

    private static class incSumViewHolder {
        public TextView incSumCatTV;
        public TextView incSumAmtTV;
    }
}*/
