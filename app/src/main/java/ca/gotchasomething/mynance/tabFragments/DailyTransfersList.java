/*package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.MainActivity;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.TransfersDb;

public class DailyTransfersList extends Fragment {

    Button trn2AddMoreBtn, trn2DoneBtn;
    DbManager trn2DbMgr;
    Double trn2Amt = 0.0;
    General trn2Gen;
    Intent trn2ToMain, trn2ToAddMonIn;
    ListView trn2List;
    Trn2Adapter trn2Adapter;
    TextView trn2Title;
    View v;

    public DailyTransfersList() {
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

        trn2DbMgr = new DbManager(getContext());
        trn2Gen = new General();

        trn2Title = v.findViewById(R.id.frag4HeaderLabelTV);
        trn2Title.setText(getString(R.string.transfers));

        trn2AddMoreBtn = v.findViewById(R.id.frag4AddMoreBtn);
        trn2AddMoreBtn.setText(getString(R.string.record_transfers));
        trn2AddMoreBtn.setOnClickListener(onClickTrn2AddMoreBtn);

        trn2DoneBtn = v.findViewById(R.id.frag4DoneBtn);
        trn2DoneBtn.setOnClickListener(onClickTrn2DoneBtn);

        trn2List = v.findViewById(R.id.frag4ListView);
        trn2Adapter = new Trn2Adapter(getContext(), trn2DbMgr.getTransfers());
        trn2List.setAdapter(trn2Adapter);
    }

    View.OnClickListener onClickTrn2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToMain = new Intent(getContext(), MainActivity.class);
            trn2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToMain);
        }
    };

    View.OnClickListener onClickTrn2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToAddMonIn = new Intent(getContext(), DailyTransfers.class);
            trn2ToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToAddMonIn);
        }
    };

    public class Trn2Adapter extends ArrayAdapter<TransfersDb> {

        private Context context;
        private List<TransfersDb> transfers;

        private Trn2Adapter(
                Context context,
                List<TransfersDb> transfers) {

            super(context, -1, transfers);

            this.context = context;
            this.transfers = transfers;
        }

        public void updateTransfers(List<TransfersDb> transfers) {
            this.transfers = transfers;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transfers.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Trans2ViewHolder trn2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_8_5tv,
                        parent, false);

                trn2Hldr = new Trans2ViewHolder();
                trn2Hldr.trn2DateTV = convertView.findViewById(R.id.list8TV1);
                trn2Hldr.trn2AmtTV = convertView.findViewById(R.id.list8TV2);
                trn2Hldr.trn2FromNameTV = convertView.findViewById(R.id.list8TV4);
                trn2Hldr.trn2ToNameTV = convertView.findViewById(R.id.list8TV6);
                convertView.setTag(trn2Hldr);

            } else {
                trn2Hldr = (Trans2ViewHolder) convertView.getTag();
            }

            trn2Hldr.trn2DateTV.setText(transfers.get(position).getTransCreatedOn());
            trn2Amt = transfers.get(position).getTransAmt();
            trn2Gen.dblASCurrency(String.valueOf(trn2Amt), trn2Hldr.trn2AmtTV);
            trn2Hldr.trn2FromNameTV.setText(transfers.get(position).getTransFromAcct());
            trn2Hldr.trn2ToNameTV.setText(transfers.get(position).getTransToAcct());

            return convertView;
        }
    }

    private static class Trans2ViewHolder {
        public TextView trn2DateTV;
        public TextView trn2AmtTV;
        public TextView trn2FromNameTV;
        public EditText trn2ToNameTV;
    }
}*/
