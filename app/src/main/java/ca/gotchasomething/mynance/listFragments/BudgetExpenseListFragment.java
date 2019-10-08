package ca.gotchasomething.mynance.listFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ca.gotchasomething.mynance.R;

public class BudgetExpenseListFragment extends Fragment {

    public BudgetExpenseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.z_fragment_list_budget_expense, container, false);
    }
}
