package ca.gotchasomething.mynance.listFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import ca.gotchasomething.mynance.R;

public class MoneyInOutListFragment extends Fragment {

    public MoneyInOutListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_daily_money_in_out, container, false);
    }
}
