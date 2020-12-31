package res.managit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import res.managit.dbo.PublicDatabaseAcces;
import res.managit.service.ProductListRetriever;
import res.managit.service.ProductRetriever;

public class productsFragment extends Fragment {
    public productsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ProductListRetriever(requireContext(), view, PublicDatabaseAcces.currentDatabase).execute();

        ListView productsList = view.findViewById(R.id.productsList);
        productsList.setOnItemClickListener((adapterView, view1, i, l) -> {
            View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.products_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.showAsDropDown(popupView, 0, 0);

            String name = getProductName((String)adapterView.getAdapter().getItem(i));
            new ProductRetriever(popupView, PublicDatabaseAcces.currentDatabase, name).execute();
        });
    }

    private String getProductName(String text) {
        String name = text;
        int index = name.lastIndexOf(']') + 2;
        name = name.substring(index);
        index = name.indexOf('(') - 1;
        name = name.substring(0, index);
        return name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }
}