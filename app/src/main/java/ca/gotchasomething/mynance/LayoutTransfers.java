package ca.gotchasomething.mynance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.spinners.TransferDebtSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.TransferSavingsSpinnerAdapter;

public class LayoutTransfers extends MainNavigation {

    //boolean[] checkedState;
    Button transfersButton;
    CheckBox fromDebtCheckbox, toDebtCheckbox, fromSavingsCheckbox, toSavingsCheckbox;
    ContentValues content, content2, content3, content4, content5, content6, content7, content8;
    Cursor cursor, cursor2, cursor3, cursor4;
    DbHelper dbHelper, dbHelper2, dbHelper3, dbHelper4, dbHelper5, dbHelper6, dbHelper7, dbHelper8;
    Double transferAmount = 0.0, fromDebtCurrentBalance = 0.0, fromDebtCurrentBalanceS = 0.0, newFromDebtAmount = 0.0, toDebtCurrentBalance = 0.0, toDebtCurrentBalanceS = 0.0,
            newToDebtAmount = 0.0, fromSavingsCurrentBalance = 0.0, fromSavingsCurrentBalanceS = 0.0, newFromSavingsAmount = 0.0, toSavingsCurrentBalance = 0.0,
            toSavingsCurrentBalanceS = 0.0, newToSavingsAmount = 0.0, debtAmount = 0.0, debtRate = 0.0, debtPayments = 0.0, debtFrequency = 0.0, debtAnnualIncome = 0.0,
            savingsAmount = 0.0, savingsGoal = 0.0, savingsRate = 0.0, savingsPayments = 0.0, savingsFrequency = 0.0, savingsAnnualIncome = 0.0;
    EditText transferAmountText;
    General general;
    int position;
    Intent backToTransferscreen;
    Long fromDebtId, fromDebtIdS, toDebtId, toDebtIdS, fromSavingsId, fromSavingsIdS, toSavingsId, toSavingsIdS;
    Spinner fromDebtSpinner, toDebtSpinner, fromSavingsSpinner, toSavingsSpinner;
    SQLiteDatabase db, db2, db3, db4, db5, db6, db7, db8;
    TransferDebtSpinnerAdapter fromDebtSpinnerAdapter, toDebtSpinnerAdapter;
    TransferSavingsSpinnerAdapter fromSavingsSpinnerAdapter, toSavingsSpinnerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transfers);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        general = new General();
        dbManager = new DbManager(this);

        transfersButton = findViewById(R.id.transfersButton);
        fromDebtCheckbox = findViewById(R.id.fromDebtCheckbox);
        fromSavingsCheckbox = findViewById(R.id.fromSavingsCheckbox);
        toDebtCheckbox = findViewById(R.id.toDebtCheckbox);
        toSavingsCheckbox = findViewById(R.id.toSavingsCheckbox);
        transferAmountText = findViewById(R.id.transferAmountText);
        fromDebtSpinner = findViewById(R.id.fromDebtSpinner);
        fromDebtSpinner.setVisibility(View.GONE);
        fromSavingsSpinner = findViewById(R.id.fromSavingsSpinner);
        fromSavingsSpinner.setVisibility(View.GONE);
        toDebtSpinner = findViewById(R.id.toDebtSpinner);
        toDebtSpinner.setVisibility(View.GONE);
        toSavingsSpinner = findViewById(R.id.toSavingsSpinner);
        toSavingsSpinner.setVisibility(View.GONE);

        fromDebtCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //checkedState[position] = !checkedState[position];

                if (isChecked) {
                    fromDebtSpinner.setVisibility(View.VISIBLE);
                } else if (!isChecked) {
                    fromDebtSpinner.setVisibility(View.GONE);
                }

                /*if (checkedState[position]) {
                    fromDebtSpinner.setVisibility(View.VISIBLE);
                } else if (!checkedState[position]) {
                    fromDebtSpinner.setVisibility(View.GONE);
                }*/
            }
        });
        //fromDebtCheckbox.setChecked(checkedState[position]);

        fromSavingsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //checkedState[position] = !checkedState[position];

                if (isChecked) {
                    fromSavingsSpinner.setVisibility(View.VISIBLE);
                } else if (!isChecked) {
                    fromSavingsSpinner.setVisibility(View.GONE);
                }

                /*if (checkedState[position]) {
                    fromSavingsSpinner.setVisibility(View.VISIBLE);
                } else if (!checkedState[position]) {
                    fromSavingsSpinner.setVisibility(View.GONE);
                }*/
            }
        });
        //fromSavingsCheckbox.setChecked(checkedState[position]);

        toDebtCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //checkedState[position] = !checkedState[position];

                if (isChecked) {
                    toDebtSpinner.setVisibility(View.VISIBLE);
                } else if (!isChecked) {
                    toDebtSpinner.setVisibility(View.GONE);
                }

                /*if (checkedState[position]) {
                    toDebtSpinner.setVisibility(View.VISIBLE);
                } else if (!checkedState[position]) {
                    toDebtSpinner.setVisibility(View.GONE);
                }*/
            }
        });
        //toDebtCheckbox.setChecked(checkedState[position]);

        toSavingsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //checkedState[position] = !checkedState[position];

                if (isChecked) {
                    toSavingsSpinner.setVisibility(View.VISIBLE);
                } else if (!isChecked) {
                    toSavingsSpinner.setVisibility(View.GONE);
                }

                /*if (checkedState[position]) {
                    toSavingsSpinner.setVisibility(View.VISIBLE);
                } else if (!checkedState[position]) {
                    toSavingsSpinner.setVisibility(View.GONE);
                }*/
            }
        });
        //toSavingsCheckbox.setChecked(checkedState[position]);

        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTNAME + " ASC", null);
        fromDebtSpinnerAdapter = new TransferDebtSpinnerAdapter(getApplicationContext(), cursor);
        fromDebtSpinner.setAdapter(fromDebtSpinnerAdapter);

        fromDebtSpinner.setOnItemSelectedListener(fromDebtSpinnerSelection);

        dbHelper2 = new DbHelper(getApplicationContext());
        db2 = dbHelper2.getReadableDatabase();
        cursor2 = db2.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTNAME + " ASC", null);
        toDebtSpinnerAdapter = new TransferDebtSpinnerAdapter(getApplicationContext(), cursor2);
        toDebtSpinner.setAdapter(toDebtSpinnerAdapter);

        toDebtSpinner.setOnItemSelectedListener(toDebtSpinnerSelection);

        dbHelper3 = new DbHelper(getApplicationContext());
        db3 = dbHelper3.getReadableDatabase();
        cursor3 = db3.rawQuery("SELECT * FROM " + DbHelper.SAVINGS_TABLE_NAME + " ORDER BY " + DbHelper.SAVINGSNAME + " ASC", null);
        fromSavingsSpinnerAdapter = new TransferSavingsSpinnerAdapter(getApplicationContext(), cursor3);
        fromSavingsSpinner.setAdapter(fromSavingsSpinnerAdapter);

        fromSavingsSpinner.setOnItemSelectedListener(fromSavingsSpinnerSelection);

        dbHelper4 = new DbHelper(getApplicationContext());
        db4 = dbHelper4.getReadableDatabase();
        cursor4 = db4.rawQuery("SELECT * FROM " + DbHelper.SAVINGS_TABLE_NAME + " ORDER BY " + DbHelper.SAVINGSNAME + " ASC", null);
        toSavingsSpinnerAdapter = new TransferSavingsSpinnerAdapter(getApplicationContext(), cursor4);
        toSavingsSpinner.setAdapter(toSavingsSpinnerAdapter);

        toSavingsSpinner.setOnItemSelectedListener(toSavingsSpinnerSelection);

        transfersButton.setOnClickListener(onClickTransfersButton);

    }

    public void backToTransfers() {
        backToTransferscreen = new Intent(LayoutTransfers.this, LayoutTransfers.class);
        backToTransferscreen.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(backToTransferscreen);
    }

    AdapterView.OnItemSelectedListener fromDebtSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            fromDebtIdS = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID));
            fromDebtCurrentBalanceS = cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.DEBTAMOUNT));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener fromSavingsSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            fromSavingsIdS = cursor3.getLong(cursor3.getColumnIndexOrThrow(DbHelper.ID));
            fromSavingsCurrentBalanceS = cursor3.getDouble(cursor3.getColumnIndexOrThrow(DbHelper.SAVINGSAMOUNT));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener toDebtSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            toDebtIdS = cursor2.getLong(cursor2.getColumnIndexOrThrow(DbHelper.ID));
            toDebtCurrentBalanceS = cursor2.getDouble(cursor2.getColumnIndexOrThrow(DbHelper.DEBTAMOUNT));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener toSavingsSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            toSavingsIdS = cursor4.getLong(cursor4.getColumnIndexOrThrow(DbHelper.ID));
            toSavingsCurrentBalanceS = cursor4.getDouble(cursor4.getColumnIndexOrThrow(DbHelper.SAVINGSAMOUNT));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void allFromDebtData() {
        for (DebtDb d : dbManager.getDebts()) {
            if (d.getId() == fromDebtId) {
                debtAmount = d.getDebtAmount();
                debtRate = d.getDebtRate();
                debtPayments = d.getDebtPayments();
                debtFrequency = d.getDebtFrequency();
                debtAnnualIncome = d.getDebtAnnualIncome();
            }
        }
    }

    public void allToDebtData() {
        for (DebtDb d2 : dbManager.getDebts()) {
            if (d2.getId() == toDebtId) {
                debtAmount = d2.getDebtAmount();
                debtRate = d2.getDebtRate();
                debtPayments = d2.getDebtPayments();
                debtFrequency = d2.getDebtFrequency();
                debtAnnualIncome = d2.getDebtAnnualIncome();
            }
        }
    }

    public void allFromSavingsData() {
        for (SavingsDb s : dbManager.getSavings()) {
            if (s.getId() == fromSavingsId) {
                savingsAmount = s.getSavingsAmount();
                savingsGoal = s.getSavingsGoal();
                savingsRate = s.getSavingsRate();
                savingsPayments = s.getSavingsPayments();
                savingsFrequency = s.getSavingsFrequency();
                savingsAnnualIncome = s.getSavingsAnnualIncome();
            }
        }
    }

    public void allToSavingsData() {
        for (SavingsDb s2 : dbManager.getSavings()) {
            if (s2.getId() == toSavingsId) {
                savingsAmount = s2.getSavingsAmount();
                savingsGoal = s2.getSavingsGoal();
                savingsRate = s2.getSavingsRate();
                savingsPayments = s2.getSavingsPayments();
                savingsFrequency = s2.getSavingsFrequency();
                savingsAnnualIncome = s2.getSavingsAnnualIncome();
            }
        }
    }

    public void updateFromDebtRecord() {
        dbHelper5 = new DbHelper(getApplicationContext());
        db5 = dbHelper5.getWritableDatabase();
        content = new ContentValues();
        newFromDebtAmount = fromDebtCurrentBalance + transferAmount;
        content.put(DbHelper.DEBTAMOUNT, newFromDebtAmount);
        db5.update(DbHelper.DEBTS_TABLE_NAME, content, DbHelper.ID + "=" + fromDebtId, null);

        allFromDebtData();

        content5 = new ContentValues();
        content5.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db5.update(DbHelper.DEBTS_TABLE_NAME, content5, DbHelper.ID + "=" + fromDebtId, null);
        db5.close();
    }

    public void updateToDebtRecord() {
        dbHelper6 = new DbHelper(getApplicationContext());
        db6 = dbHelper6.getWritableDatabase();
        content2 = new ContentValues();
        newToDebtAmount = toDebtCurrentBalance - transferAmount;
        content2.put(DbHelper.DEBTAMOUNT, newToDebtAmount);
        db6.update(DbHelper.DEBTS_TABLE_NAME, content2, DbHelper.ID + "=" + toDebtId, null);

        allToDebtData();

        content6 = new ContentValues();
        content6.put(DbHelper.DEBTEND, general.calcDebtDate(
                debtAmount,
                debtRate,
                debtPayments,
                debtFrequency,
                debtAnnualIncome,
                getString(R.string.debt_paid),
                getString(R.string.too_far)));
        db6.update(DbHelper.DEBTS_TABLE_NAME, content6, DbHelper.ID + "=" + toDebtId, null);
        db6.close();
    }

    public void updateFromSavingsRecord() {
        dbHelper7 = new DbHelper(getApplicationContext());
        db7 = dbHelper7.getWritableDatabase();
        content3 = new ContentValues();
        newFromSavingsAmount = fromSavingsCurrentBalance - transferAmount;
        content3.put(DbHelper.SAVINGSAMOUNT, newFromSavingsAmount);
        db7.update(DbHelper.SAVINGS_TABLE_NAME, content3, DbHelper.ID + "=" + fromSavingsId, null);

        allFromSavingsData();

        content7 = new ContentValues();
        content7.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)));
        db7.update(DbHelper.SAVINGS_TABLE_NAME, content7, DbHelper.ID + "=" + fromSavingsId, null);
        db7.close();
    }

    public void updateToSavingsRecord() {
        dbHelper8 = new DbHelper(getApplicationContext());
        db8 = dbHelper8.getWritableDatabase();
        content4 = new ContentValues();
        newToSavingsAmount = toSavingsCurrentBalance + transferAmount;
        content4.put(DbHelper.SAVINGSAMOUNT, newToSavingsAmount);
        db8.update(DbHelper.SAVINGS_TABLE_NAME, content4, DbHelper.ID + "=" + toSavingsId, null);

        allToSavingsData();

        content8 = new ContentValues();
        content8.put(DbHelper.SAVINGSDATE, general.calcSavingsDate(
                savingsGoal,
                savingsAmount,
                savingsRate,
                savingsPayments,
                savingsFrequency,
                savingsAnnualIncome,
                getString(R.string.goal_achieved),
                getString(R.string.too_far)));
        db8.update(DbHelper.SAVINGS_TABLE_NAME, content8, DbHelper.ID + "=" + toSavingsId, null);
        db8.close();
    }

    /*public void determineFromAndTo() {
        try {
            fromDebtId = fromDebtIdS;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            toDebtId = toDebtIdS;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            fromSavingsId = fromSavingsIdS;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            toSavingsId = toSavingsIdS;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }*/

    View.OnClickListener onClickTransfersButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            transferAmount = general.extractingDouble(transferAmountText);
            if (transferAmount == 0 || transferAmount == null) {
                Toast.makeText(getApplicationContext(), R.string.no_amount_warning, Toast.LENGTH_LONG).show();
            } else {

                try {
                    fromDebtId = fromDebtIdS;
                } catch (NullPointerException e) {
                    fromDebtId = Long.valueOf(0);
                    //e.printStackTrace();
                }
                try {
                    toDebtId = toDebtIdS;
                } catch (NullPointerException e) {
                    toDebtId = Long.valueOf(0);
                    //e.printStackTrace();
                }
                try {
                    fromSavingsId = fromSavingsIdS;
                } catch (NullPointerException e) {
                    fromSavingsId = Long.valueOf(0);
                    //e.printStackTrace();
                }
                try {
                    toSavingsId = toSavingsIdS;
                } catch (NullPointerException e) {
                    toSavingsId = Long.valueOf(0);
                    //e.printStackTrace();
                }

                if (fromDebtId == null && fromSavingsId == null) {
                    Toast.makeText(getApplicationContext(), R.string.no_from_item_warning, Toast.LENGTH_LONG).show();
                } else if (fromDebtId != null) {
                    fromDebtCurrentBalance = fromDebtCurrentBalanceS;
                    updateFromDebtRecord();
                } else if (fromSavingsId != null) {
                    fromSavingsCurrentBalance = fromSavingsCurrentBalanceS;
                    updateFromSavingsRecord();
                }

                if (toDebtId == null && toSavingsId == null) {
                    Toast.makeText(getApplicationContext(), R.string.no_to_item_warning, Toast.LENGTH_LONG).show();
                } else if (toDebtId != null) {
                    toDebtCurrentBalance = toDebtCurrentBalanceS;
                    updateToDebtRecord();
                } else if (toSavingsId != null) {
                    toSavingsCurrentBalance = toSavingsCurrentBalanceS;
                    updateToSavingsRecord();
                }

                Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_LONG).show();
                backToTransfers();
            }
        }
    };
}
