/*package ca.gotchasomething.mynance;

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

import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyIn;

public class DailyMoneyInListB extends AppCompatActivity {

    Button monInLstAddMoreBtn, monInLstDoneBtn;
    DbManager monInLstDbMgr;
    Double moneyInA = 0.0, moneyInB = 0.0, moneyInOwing = 0.0, monInLstAmtEntry = 0.0, monInLstMonInAmt = 0.0, monInLstMonInAmtDiff = 0.0, newMoneyA = 0.0,
            newMoneyOwing = 0.0, newMoneyB = 0.0;
    General monInLstGen;
    Intent monInLstToMain, monInLstToAddMonIn, monInLstRefresh;
    ListView monInLstList;
    long monInLstIncRefKeyMI;
    MoneyInDb monInLstMonInDb;
    MonInLstAdapter monInLstAdapter;
    TextView monInLstTitle;

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
            monInLstToMain = new Intent(DailyMoneyInListB.this, MainActivity.class);
            monInLstToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToMain);
        }
    };

    View.OnClickListener onClickMonInLstAddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInLstToAddMonIn = new Intent(DailyMoneyInListB.this, DailyMoneyIn.class);
            monInLstToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInLstToAddMonIn);
        }
    };

    public class MonInLstAdapter extends ArrayAdapter<MoneyInDb> {

        private Context context;
        private List<MoneyInDb> moneyIns;

        private MonInLstAdapter(
                Context context,
                List<MoneyInDb> moneyIns) {

            super(context, -1, moneyIns);

            this.context = context;
            this.moneyIns = moneyIns;
        }

        public void updateMoneyIns(List<MoneyInDb> moneyIns) {
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
                        R.layout.frag_list_4_3tv_edit_del,
                        parent, false);

                monInLstHldr = new MoneyIn2ViewHolder();
                monInLstHldr.monInLstCatTV = convertView.findViewById(R.id.list4CatTV);
                monInLstHldr.monInLstAmtTV = convertView.findViewById(R.id.list4AmtTV);
                monInLstHldr.monInLstDateTV = convertView.findViewById(R.id.list4DateTV);
                monInLstHldr.monInLstEditBtn = convertView.findViewById(R.id.list4EditBtn);
                monInLstHldr.monInLstDelBtn = convertView.findViewById(R.id.list4DelBtn);
                monInLstHldr.monInLstUpdateLayout = convertView.findViewById(R.id.list4UpdateLayout);
                monInLstHldr.monInLstUpdateLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstAmtET = convertView.findViewById(R.id.list4AmtET);
                monInLstHldr.monInLstUpdateBtn = convertView.findViewById(R.id.list4UpdateBtn);
                monInLstHldr.monInLstCancelBtn = convertView.findViewById(R.id.list4CancelBtn);
                monInLstHldr.monInLstWarnLayout = convertView.findViewById(R.id.list4WarnLayout);
                monInLstHldr.monInLstWarnLayout.setVisibility(View.GONE);
                monInLstHldr.monInLstWarnTV = convertView.findViewById(R.id.list4WarnTV);
                monInLstHldr.monInLstYesBtn = convertView.findViewById(R.id.list4YesBtn);
                monInLstHldr.monInLstNoBtn = convertView.findViewById(R.id.list4NoBtn);

                convertView.setTag(monInLstHldr);

            } else {
                monInLstHldr = (MoneyIn2ViewHolder) convertView.getTag();
            }

            monInLstHldr.monInLstCatTV.setText(moneyIns.get(position).getMoneyInCat());
            monInLstMonInAmt = moneyIns.get(position).getMoneyInAmount();
            monInLstGen.dblASCurrency(String.valueOf(monInLstMonInAmt), monInLstHldr.monInLstAmtTV);
            monInLstHldr.monInLstDateTV.setText(moneyIns.get(position).getMoneyInCreatedOn());
            monInLstHldr.monInLstEditBtn.setTag(moneyIns.get(position));
            monInLstHldr.monInLstDelBtn.setTag(moneyIns.get(position));

            monInLstIncRefKeyMI = moneyIns.get(position).getIncRefKeyMI();

            //click on pencil icon
            monInLstHldr.monInLstEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monInLstMonInDb = (MoneyInDb) monInLstHldr.monInLstEditBtn.getTag();
                    DailyMoneyInListB.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monInLstDbMgr = new DbManager(getContext());

                    monInLstHldr.monInLstUpdateLayout.setVisibility(View.VISIBLE);

                    monInLstHldr.monInLstCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            monInLstRefresh = new Intent(getContext(), DailyMoneyInListB.class);
                            monInLstRefresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(monInLstRefresh);
                        }
                    });

                    monInLstHldr.monInLstUpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            monInLstAmtEntry = monInLstGen.dblFromET(monInLstHldr.monInLstAmtET);
                            monInLstMonInAmtDiff = monInLstAmtEntry - moneyIns.get(position).getMoneyInAmount();

                            monInLstDbMgr.updateTotAcctBalPlus(monInLstMonInAmtDiff);

                            monInLstDbMgr.updateAvailBalMinus(moneyIns.get(position).getMoneyInA(), moneyIns.get(position).getMoneyInOwing(), moneyIns.get(position).getMoneyInB());
                            monInLstDbMgr.detAandBPortionsInc(monInLstAmtEntry);
                            monInLstDbMgr.updateAvailBalPlus(moneyInA, moneyInOwing, moneyInB);

                            if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                                monInLstDbMgr.adjustAandBPortions(moneyInA, moneyInOwing, moneyInB);
                                moneyInA = newMoneyA;
                                moneyInOwing = newMoneyOwing;
                                moneyInB = newMoneyB;
                            }

                            monInLstMonInDb.setMoneyInAmount(monInLstAmtEntry);
                            monInLstMonInDb.setMoneyInA(moneyInA);
                            monInLstMonInDb.setMoneyInOwing(moneyInOwing);
                            monInLstMonInDb.setMoneyInB(moneyInB);
                            monInLstDbMgr.updateMoneyIn(monInLstMonInDb);

                            monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                            notifyDataSetChanged();

                            monInLstDbMgr.updateIncAnnAmt(moneyIns.get(position).getIncRefKeyMI());

                            monInLstRefresh = new Intent(getContext(), DailyMoneyInListB.class);
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

                    monInLstMonInDb = (MoneyInDb) monInLstHldr.monInLstDelBtn.getTag();
                    monInLstIncRefKeyMI = moneyIns.get(position).getIncRefKeyMI();

                    monInLstDbMgr.updateTotAcctBalMinus(moneyIns.get(position).getMoneyInAmount());

                    monInLstDbMgr.updateAvailBalMinus(moneyIns.get(position).getMoneyInA(), moneyIns.get(position).getMoneyInOwing(), moneyIns.get(position).getMoneyInB());
                    if (monInLstDbMgr.retrieveCurrentOwingA() < 0) {
                        monInLstDbMgr.adjustAandBPortions(0.0, 0.0, 0.0);
                    }

                    monInLstDbMgr.deleteMoneyIn(monInLstMonInDb);
                    monInLstAdapter.updateMoneyIns(monInLstDbMgr.getMoneyIns());
                    notifyDataSetChanged();

                    monInLstDbMgr.updateIncAnnAmt(monInLstIncRefKeyMI);

                    monInLstRefresh = new Intent(getContext(), DailyMoneyInListB.class);
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
}*/
