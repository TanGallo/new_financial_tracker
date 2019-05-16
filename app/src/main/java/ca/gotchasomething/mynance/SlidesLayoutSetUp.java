package ca.gotchasomething.mynance;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SlidesLayoutSetUp extends AppCompatActivity {

    //DbManager dbManager;
    Intent portrait, landscape;
    //String latestDone = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dbManager = new DbManager(this);

        int orientation = getResources().getConfiguration().orientation;

        //dbManager.retrieveLatestDone();

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            /*switch(latestDone) {
                case "null":
                    portrait = new Intent(this, SlidesSetUpIncomeP.class);
                    break;
                case "income":
                    portrait = new Intent(this, SlidesSetUpBillsP.class);
                    break;
                case "bills":
                    portrait = new Intent(this, SlidesSetUpDebtsP.class);
                    break;
                case "debts":
                    portrait = new Intent(this, SlidesSetUpSavingsP.class);
                    break;
                case "savings":
                    portrait = new Intent(this, className.class);
                    break;
                case "budget":
                    portrait = new Intent(this, className.class);
                    break;
                case "balance":
                    portrait = new Intent(this, className.class);
                    break;
            }*/
            //portrait = new Intent(this, SlidesSetUpIncomeP.class);
            portrait = new Intent(this, SlidesSetUpP.class);
            startActivity(portrait);
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            /*switch(latestDone) {
                case "null":
                    landscape = new Intent(this, SlidesSetUpIncomeL.class);
                    break;
                case "income":
                    landscape = new Intent(this, SlidesSetUpBillsL.class);
                    break;
                case "bills":
                    landscape = new Intent(this, SlidesSetUpDebtsL.class);
                    break;
                case "debts":
                    landscape = new Intent(this, SlidesSetUpSavingsL.class);
                    break;
                case "savings":
                    landscape = new Intent(this, className.class);
                    break;
                case "budget":
                    landscape = new Intent(this, className.class);
                    break;
                case "balance":
                    landscape = new Intent(this, className.class);
                    break;
            }*/
            //landscape = new Intent(this, SlidesSetUpIncomeL.class);
            landscape = new Intent(this, SlidesSetUpL.class);
            startActivity(landscape);
        }
    }
}

