/*package ca.gotchasomething.mynance;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SlidesLayoutSetUpDebts extends AppCompatActivity {

    Intent portrait, landscape;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            portrait = new Intent(this, SlidesSetUpDebtsP.class);
            startActivity(portrait);
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            landscape = new Intent(this, SlidesSetUpDebtsL.class);
            startActivity(landscape);
        }
    }
}*/

