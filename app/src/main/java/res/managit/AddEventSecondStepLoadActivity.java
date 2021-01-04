package res.managit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import res.managit.add.event.AddEventFirstStepActivity;
import res.managit.add.event.adapter.ProductAdapter;
import res.managit.add.event.adapter.SupplierAdapter;
import res.managit.add.event.adapter.WorkerAdapter;
import res.managit.dbo.PublicDatabaseAcces;
import res.managit.dbo.WarehouseDb;
import res.managit.dbo.entity.Product;
import res.managit.dbo.entity.Supply;
import res.managit.dbo.entity.Worker;
import res.managit.settings.Settings;

public class AddEventSecondStepLoadActivity extends AppCompatActivity {

    private ListView listSuppliers;
    private ArrayList<Supply> suppliersArrayList;
    private static SupplierAdapter supplierAdapter;

    private ListView listProducts;
    private ArrayList<Product> productsArrayList;
    private static ProductAdapter productsAdapter;

    private Button buttonAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_second_load_step);

        WarehouseDb db = PublicDatabaseAcces.getDatabaseList().get(Settings.getActualSelectedDataBase());


        //initialize for suppliers
        listSuppliers = (ListView) findViewById(R.id.suppliersList);
        suppliersArrayList = new ArrayList<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Supply> supplyList = db.supplyDao().getAll();
            suppliersArrayList.addAll(supplyList);
        });
        supplierAdapter = new SupplierAdapter(suppliersArrayList, getApplicationContext());
        listSuppliers.setAdapter(supplierAdapter);

        //initialize for products
        listProducts = (ListView) findViewById(R.id.productList);
        productsArrayList = new ArrayList<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> productList = db.productDao().getAll();
            productsArrayList.addAll(productList);
        });
        productsAdapter = new ProductAdapter(productsArrayList, getApplicationContext());
        listProducts.setAdapter(productsAdapter);

        buttonAddEvent = findViewById(R.id.buttonSaveEvent);
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("#############################");
                System.out.println("Details of event");
                System.out.println("List of suppliers");
                SupplierAdapter.getSuppliesListChecked().forEach(System.out::println);
                System.out.println("List of products with quantity");
                ProductAdapter.getProductQuantityList().forEach(item->System.out.println(item.first+" "+item.second));
                System.out.println("Type: Load");
                System.out.println("List of workers");
                WorkerAdapter.getWorkerListChecked().forEach(System.out::println);
                System.out.println("#############################");
            }
        });
    }
}