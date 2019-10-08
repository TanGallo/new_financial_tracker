package ca.gotchasomething.mynance.spinners;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.R;

public class TransferSpinnerAdapter extends CursorAdapter {


    General transfersGen;
    TextView spinnerText, spinnerAmtTV;
    String spinName;
    Double spinAmt;

    public TransferSpinnerAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_1_spinner, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        transfersGen = new General();

        spinnerText = view.findViewById(R.id.spinnerText);
        spinnerAmtTV = view.findViewById(R.id.spinnerAmtTV);
        spinName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.ACCTNAME));
        spinnerText.setText(spinName);
        spinAmt = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.ACCTBAL));
        transfersGen.dblASCurrency(String.valueOf(spinAmt), spinnerAmtTV);
        //spinnerAmtTV.setText(String.valueOf(spinAmt));
    }


}
