package ca.gotchasomething.mynance;

/*public class AddExpenseFromMoneyOut extends AppCompatActivity {

    Button exp1SaveBtn, exp1CancelBtn, exp1UpdateBtn;
    DbManager exp1DbMgr;
    Double expAmtFromEntry = 0.0, expAnnAmtFromEntry = 0.0, expAAnnAmtFromEntry = 0.0, expBAnnAmtFromEntry = 0.0, expFrqFromEntry = 0.0;
    EditText exp1CatET, exp1AmtET;
    ExpenseBudgetDb exp1ExpDb;
    General exp1Gen;
    Intent exp1ToMonOut;
    RadioButton exp1ARB, exp1AnnlyRB, exp1BRB, exp1BiAnnlyRB, exp1BiMthlyRB, exp1BiWklyRB, exp1MthlyRB, exp1NoWklyRB, exp1WklyRB, exp1YesWklyRB;
    RadioGroup exp1FrqRG, exp1ABRG, exp1WklyRG;
    String exp1ABRB = null, exp1FrqRB = null, expPriorityFromEntry = null, expWeeklyFromEntry = null, exp1WklyLimRB = null, expNameFromEntry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_5_add_expense);

        exp1Gen = new General();
        exp1DbMgr = new DbManager(this);

        exp1CatET = findViewById(R.id.addExpCatET);
        exp1AmtET = findViewById(R.id.addExpAmtET);
        exp1FrqRG = findViewById(R.id.addExpFrqRG);
        exp1ABRG = findViewById(R.id.addExpABRG);
        exp1WklyRG = findViewById(R.id.addExpWklyRG);
        exp1SaveBtn = findViewById(R.id.addExpSaveBtn);
        exp1UpdateBtn = findViewById(R.id.addExpUpdateBtn);
        exp1UpdateBtn.setVisibility(View.GONE);
        exp1CancelBtn = findViewById(R.id.addExpCancelBtn);

        exp1WklyRB = findViewById(R.id.addExpWklyRB);
        exp1BiWklyRB = findViewById(R.id.addExpBiWklyRB);
        exp1BiMthlyRB = findViewById(R.id.addExpBiMthlyRB);
        exp1MthlyRB = findViewById(R.id.addExpMthlyRB);
        exp1BiAnnlyRB = findViewById(R.id.addExpBiAnnlyRB);
        exp1AnnlyRB = findViewById(R.id.addExpAnnlyRB);

        exp1ARB = findViewById(R.id.addExpARB);
        exp1BRB = findViewById(R.id.addExpBRB);

        exp1YesWklyRB = findViewById(R.id.addExpYesWklyRB);
        exp1NoWklyRB = findViewById(R.id.addExpNoWklyRB);

        exp1CancelBtn.setOnClickListener(onClickExp1CancelBtn);
        exp1SaveBtn.setOnClickListener(onClickExp1SaveBtn);
        exp1FrqRG.setOnCheckedChangeListener(onCheckExp1FrqRG);
        exp1ABRG.setOnCheckedChangeListener(onCheckExp1ABRG);
        exp1WklyRG.setOnCheckedChangeListener(onCheckExp1WklyRG);
    }

    //handle radioGroups
    RadioGroup.OnCheckedChangeListener onCheckExp1FrqRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpWklyRB:
                    exp1FrqRB = "52";
                    break;
                case R.id.addExpBiWklyRB:
                    exp1FrqRB = "26";
                    break;
                case R.id.addExpBiMthlyRB:
                    exp1FrqRB = "24";
                    break;
                case R.id.addExpMthlyRB:
                    exp1FrqRB = "12";
                    break;
                case R.id.addExpBiAnnlyRB:
                    exp1FrqRB = "2";
                    break;
                case R.id.addExpAnnlyRB:
                    exp1FrqRB = "1";
                    break;
                default:
                    exp1FrqRB = "1";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp1ABRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpARB:
                    exp1ABRB = "A";
                    break;
                case R.id.addExpBRB:
                    exp1ABRB = "B";
                    break;
                default:
                    exp1ABRB = "B";
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckExp1WklyRG = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.addExpYesWklyRB:
                    exp1WklyLimRB = "Y";
                    break;
                case R.id.addExpNoWklyRB:
                    exp1WklyLimRB = "N";
                    break;
                default:
                    exp1WklyLimRB = "Y";
            }
        }
    };

    View.OnClickListener onClickExp1CancelBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exp1Gen.intentMethod(exp1ToMonOut, AddExpenseFromMoneyOut.this, DailyMoneyOut.class);
        }
    };

    View.OnClickListener onClickExp1SaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            exp1Gen.expenseDataFromEntries(exp1CatET, exp1AmtET, exp1FrqRB, exp1ABRB, exp1WklyLimRB);
            if(!expNameFromEntry.equals("null")) {

                exp1ExpDb = new ExpenseBudgetDb(
                        expNameFromEntry,
                        expAmtFromEntry,
                        expFrqFromEntry,
                        expPriorityFromEntry,
                        expWeeklyFromEntry,
                        expAnnAmtFromEntry,
                        expAAnnAmtFromEntry,
                        expBAnnAmtFromEntry,
                        0);

                exp1DbMgr.addExpense(exp1ExpDb);

                exp1Gen.intentMethod(exp1ToMonOut, AddExpenseFromMoneyOut.this, DailyMoneyOut.class);
            } else {
                Toast.makeText(getBaseContext(), R.string.no_blanks_warning, Toast.LENGTH_LONG).show();
            }
        }
    };
}*/
