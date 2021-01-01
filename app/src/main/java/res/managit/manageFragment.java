package res.managit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import res.managit.dbo.PublicDatabaseAcces;
import res.managit.service.DatabaseRetriever;

public class manageFragment extends Fragment {
    public manageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setButtonListeners(view);
        new DatabaseRetriever(view, PublicDatabaseAcces.currentDatabase).execute();
    }

    public void setButtonListeners(@NonNull View view) {
        Button products = view.findViewById(R.id.products);
        products.setOnClickListener((event) -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new productsFragment()).commit();
        });

        Button workers = view.findViewById(R.id.workers);
        workers.setOnClickListener((event) -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new workersFragment()).commit();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }
}