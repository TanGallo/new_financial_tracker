package ca.gotchasomething.mynance.tabFragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import ca.gotchasomething.mynance.DbHelper;
//import ca.gotchasomething.mynance.HeaderDailyMoney;
import ca.gotchasomething.mynance.LayoutDebt;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyCreditCard extends Fragment {

    boolean isSpinnerTouched;
    Button cancelCCButton, updateCCButton;
    CCAdapter ccAdapter, ccAdapter2;
    CheckBox ccPaidCheckbox;
    Cursor moneyOutCursor, moneyOutCursor2;
    DbHelper moneyOutHelper, moneyOutHelper2;
    Double ccAmountD, thisAmount, moneyOutAmountS;
    EditText ccCatEntry, ccAmountEntry;
    ImageButton thisCatButton;
    int thisIdL;
    Intent refreshHeader, editCC, backToDailyCreditCard, backToDailyCreditCard2;
    LinearLayout editCCLayout;
    ListView ccListView;
    long moneyOutId, thisId, thisId2;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutSpinnerAdapter moneyOutAdapter, moneyOutAdapter2;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    Spinner ccCatSpinner, ccCatSpinner2;
    SQLiteDatabase moneyOutDbDb, moneyOutDbDb2;
    String ccAmountS, ccAmount2, thisCat, moneyOutCatS, moneyOutPriorityS, selectedItemCategory;
    TextView checkBelowLabel, totalCCPaymentDueLabel, totalCCPaymentDueAmount, ccPaidLabel, thisCatText;
    View v, editCCLine;

    public DailyCreditCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_credit_card, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editCCLayout = v.findViewById(R.id.editCCLayout);
        editCCLayout.setVisibility(View.GONE);
        editCCLine = v.findViewById(R.id.editCCLine);
        editCCLine.setVisibility(View.GONE);
        thisCatText = v.findViewById(R.id.thisCatText);
        thisCatText.setVisibility(View.GONE);
        ccAmountEntry = v.findViewById(R.id.ccAmountEntry);
        ccAmountEntry.setVisibility(View.GONE);
        cancelCCButton = v.findViewById(R.id.cancelCCButton);
        cancelCCButton.setVisibility(View.GONE);
        updateCCButton = v.findViewById(R.id.updateCCButton);
        updateCCButton.setVisibility(View.GONE);
        checkBelowLabel = v.findViewById(R.id.checkBelowLabel);
        totalCCPaymentDueLabel = v.findViewById(R.id.totalCCPaymentDueLabel);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount = v.findViewById(R.id.totalCCPaymentDueAmount);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel = v.findViewById(R.id.ccPaidLabel);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox = v.findViewById(R.id.ccPaidCheckbox);
        ccPaidCheckbox.setVisibility(View.GONE);

        ccListView = v.findViewById(R.id.ccListView);

        moneyOutDbManager = new MoneyOutDbManager(getContext());
        ccAdapter = new CCAdapter(getContext(), moneyOutDbManager.getCCTrans());
        ccListView.setAdapter(ccAdapter);

    }

    public class CCAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private CCAdapter(
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

            final CCViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_credit_card,
                        parent, false);

                holder = new CCViewHolder();
                holder.ccCat = convertView.findViewById(R.id.ccCat);
                holder.ccAmount = convertView.findViewById(R.id.ccAmount);
                holder.ccCheck = convertView.findViewById(R.id.ccCheck);
                holder.ccDeleted = convertView.findViewById(R.id.deleteCCButton);
                holder.ccEdit = convertView.findViewById(R.id.editCCButton);
                convertView.setTag(holder);

            } else {
                holder = (CCViewHolder) convertView.getTag();
            }

            //retrieve ccCat
            holder.ccCat.setText(ccTrans.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccAmountS = (String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                if (ccAmountS != null && !ccAmountS.equals("")) {
                    ccAmountD = Double.valueOf(ccAmountS);
                } else {
                    ccAmountD = 0.0;
                }
                ccAmount2 = currencyFormat.format(ccAmountD);
                holder.ccAmount.setText(ccAmount2);
            } catch (NumberFormatException e) {
                holder.ccAmount.setText(ccAmount2);
            }

            holder.ccDeleted.setTag(ccTrans.get(position));
            holder.ccEdit.setTag(ccTrans.get(position));
            holder.ccCheck.setTag(ccTrans.get(position));

            //click on pencil icon
            holder.ccEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccEdit.getTag();

                    checkBelowLabel.setVisibility(View.GONE);
                    totalCCPaymentDueAmount.setVisibility(View.GONE);
                    ccPaidLabel.setVisibility(View.GONE);
                    ccPaidCheckbox.setVisibility(View.GONE);
                    editCCLayout.setVisibility(View.VISIBLE);
                    editCCLine.setVisibility(View.VISIBLE);
                    thisCatText.setVisibility(View.VISIBLE);
                    ccAmountEntry.setVisibility(View.VISIBLE);
                    cancelCCButton.setVisibility(View.VISIBLE);
                    updateCCButton.setVisibility(View.VISIBLE);

                    ccAmountEntry.setText(String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                    thisCatText.setText(String.valueOf(ccTrans.get(position).getMoneyOutCat()));

                }
            });

            cancelCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkBelowLabel.setVisibility(View.VISIBLE);
                    editCCLayout.setVisibility(View.GONE);
                    editCCLine.setVisibility(View.GONE);
                }
            });

            updateCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb.setMoneyOutAmount(Double.valueOf(ccAmountEntry.getText().toString()));

                    moneyOutDbManager.updateMoneyOut(moneyOutDb);
                    ccAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();

                    checkBelowLabel.setVisibility(View.VISIBLE);
                    editCCLayout.setVisibility(View.GONE);
                    editCCLine.setVisibility(View.GONE);
                }
            });

            //click on trash can icon
            holder.ccDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccDeleted.getTag();
                    moneyOutDbManager.deleteMoneyOut(moneyOutDb);

                    ccAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class CCViewHolder {
        public TextView ccCat;
        public TextView ccAmount;
        public CheckBox ccCheck;
        ImageButton ccDeleted;
        ImageButton ccEdit;
    }
}
