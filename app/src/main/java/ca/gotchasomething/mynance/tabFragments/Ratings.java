/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.R;

public class Ratings extends Fragment {

    Button emailNoButton, emailYesButton, enjoyNoButton, enjoyNotSureButton, enjoyYesButton, rateNoButton, rateYesButton;
            //yesMoneyOutButton, noMoneyOutButton;
    DailyMoneyOut rateDaiMonOut;
    //LayoutDailyMoney ratDaiMonLayout;
    //DbManager rateDbMgr;
    General rateGen;
    Intent email, goToRatings, rateToMonOut;
    //LinearLayout updateMoneyOutLayout;
    //ListView moneyOutList;
    //RelativeLayout addMoneyOutLayout;
    //String clicksES = null, clicksS = null;
    TextView ratingsLabel, ratingsNoLabel, ratingsYesLabel, temp, tempE;
    View v;
    public static int clicked = 0;
    public static int clickedE = 0;
    public static final String SRPE = "shared ratings pref";
    public static final String CTE = String.valueOf(clickedE);
    public static final String SRP = "shared ratings pref";
    public static final String CT = String.valueOf(clicked);

    public Ratings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.d1_frag_ratings, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rateGen = new General();
        //rateDbMgr = new DbManager(getContext());
        rateDaiMonOut = new DailyMoneyOut();
        //ratDaiMonLayout = new LayoutDailyMoney();

        ratingsLabel = v.findViewById(R.id.ratLabel);
        enjoyNoButton = v.findViewById(R.id.ratNoJoyBtn);
        enjoyNotSureButton = v.findViewById(R.id.ratNotSureJoyBtn);
        enjoyYesButton = v.findViewById(R.id.ratYesJoyBtn);

        ratingsYesLabel = v.findViewById(R.id.ratYesLabel);
        ratingsYesLabel.setVisibility(View.GONE);
        rateNoButton = v.findViewById(R.id.ratNoRateBtn);
        rateNoButton.setVisibility(View.GONE);
        rateYesButton = v.findViewById(R.id.ratYesRateBtn);
        rateYesButton.setVisibility(View.GONE);

        ratingsNoLabel = v.findViewById(R.id.ratNoLabel);
        ratingsNoLabel.setVisibility(View.GONE);
        emailNoButton = v.findViewById(R.id.ratNoEmailBtn);
        emailNoButton.setVisibility(View.GONE);
        emailYesButton = v.findViewById(R.id.ratYesEmailBtn);
        emailYesButton.setVisibility(View.GONE);

        enjoyNoButton.setOnClickListener(onClickNoJoyBtn);
        enjoyNotSureButton.setOnClickListener(onClickNotSureJoyBtn);
        enjoyYesButton.setOnClickListener(onClickYesJoyBtn);

        /*temp = v.findViewById(R.id.ratTemp);
        temp.setVisibility(View.GONE);
        temp.setText(String.valueOf(clicked));
        tempE = v.findViewById(R.id.ratTempE);
        tempE.setVisibility(View.GONE);
        tempE.setText(String.valueOf(clickedE));*/

        /*loadClicks();
        updateClicks();
        loadClicksE();
        updateClicksE();*/
    /*}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putString(String.valueOf(clicked), temp.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    /*public void loadClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        clicksS = sp.getString(CT, "");
    }

    public void updateClicks() {
        temp.setText(clicksS);

        if (!clicksS.equals("")) {
            clicked = Integer.valueOf(clicksS);
        } else {
            clicked = 0;
        }
    }

    public void loadClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        clicksES = spE.getString(CTE, "");
    }

    public void updateClicksE() {
        tempE.setText(clicksES);

        if (!clicksES.equals("")) {
            clickedE = Integer.valueOf(clicksES);
        } else {
            clickedE = 0;
        }
    }

    public void saveClicks() {
        SharedPreferences sp = getContext().getSharedPreferences(SRP, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CT, temp.getText().toString());
        clicked = Integer.valueOf(temp.getText().toString());
        editor.apply();
    }

    public void saveClicksE() {
        SharedPreferences spE = getContext().getSharedPreferences(SRPE, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editorE = spE.edit();
        editorE.putString(CTE, tempE.getText().toString());
        clickedE = Integer.valueOf(tempE.getText().toString());
        editorE.apply();
    }*/

    /*View.OnClickListener onClickNoJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ratingsLabel.setVisibility(View.GONE);
            enjoyNoButton.setVisibility(View.GONE);
            enjoyNotSureButton.setVisibility(View.GONE);
            enjoyYesButton.setVisibility(View.GONE);

            ratingsNoLabel.setVisibility(View.VISIBLE);
            emailNoButton.setVisibility(View.VISIBLE);
            emailYesButton.setVisibility(View.VISIBLE);

            emailNoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
                    ratingsNoLabel.setVisibility(View.GONE);
                    emailNoButton.setVisibility(View.GONE);
                    emailYesButton.setVisibility(View.GONE);
                    rateToMonOut = new Intent(getContext(), DailyMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);
                }
            });
            emailYesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedE++;
                    tempE.setText(String.valueOf(clickedE));

                    rateDaiMonOut.saveClicksE();
                    ratingsNoLabel.setVisibility(View.GONE);
                    emailNoButton.setVisibility(View.GONE);
                    emailYesButton.setVisibility(View.GONE);

                    email = new Intent(Intent.ACTION_SEND);
                    email.setType("message/rfc822");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});

                    try {
                        startActivity(Intent.createChooser(email, getString(R.string.choose_email)));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), getString(R.string.email_warning), Toast.LENGTH_LONG).show();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    } else {
                        email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    }
                }
            });
        }
    };

    View.OnClickListener onClickNotSureJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
            ratingsLabel.setVisibility(View.GONE);
            enjoyNoButton.setVisibility(View.GONE);
            enjoyNotSureButton.setVisibility(View.GONE);
            enjoyYesButton.setVisibility(View.GONE);
            rateToMonOut = new Intent(getContext(), DailyMoneyOut.class);
            rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(rateToMonOut);
        }
    };

    View.OnClickListener onClickYesJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ratingsLabel.setVisibility(View.GONE);
            enjoyNoButton.setVisibility(View.GONE);
            enjoyNotSureButton.setVisibility(View.GONE);
            enjoyYesButton.setVisibility(View.GONE);

            ratingsYesLabel.setVisibility(View.VISIBLE);
            rateNoButton.setVisibility(View.VISIBLE);
            rateYesButton.setVisibility(View.VISIBLE);

            rateNoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), R.string.ask_later, Toast.LENGTH_LONG).show();
                    ratingsYesLabel.setVisibility(View.GONE);
                    rateNoButton.setVisibility(View.GONE);
                    rateYesButton.setVisibility(View.GONE);
                    rateToMonOut = new Intent(getContext(), DailyMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);
                }
            });
            rateYesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked++;
                    temp.setText(String.valueOf(clicked));

                    rateDaiMonOut.saveClicks();
                    ratingsYesLabel.setVisibility(View.GONE);
                    rateNoButton.setVisibility(View.GONE);
                    rateYesButton.setVisibility(View.GONE);
                    //ratDaiMonLayout.daiMon1RepFrag(new DailyMoneyOut());

                    goToRatings = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_url)));
                    goToRatings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToRatings);
                }
            });
        }
    };
}*/
