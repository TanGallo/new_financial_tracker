package ca.gotchasomething.mynance.spinners;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.R;

public class TransferDebtSpinnerAdapter extends CursorAdapter {


    TextView spinnerText;
    String debtName;

    public TransferDebtSpinnerAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_1_spinner, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        spinnerText = view.findViewById(R.id.spinnerText);
        debtName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.DEBTNAME));
        spinnerText.setText(debtName);
    }


}
