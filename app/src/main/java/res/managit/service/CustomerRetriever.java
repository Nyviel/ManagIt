package res.managit.service;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import res.managit.R;
import res.managit.dbo.WarehouseDb;
import res.managit.dbo.entity.Contact;
import res.managit.dbo.entity.Customer;

public class CustomerRetriever extends AsyncTask<Void, Void, CustomerRetriever.Data> {
    WarehouseDb db;
    View view;
    long id;

    protected class Data {
        protected Customer customer;
        protected Contact contact;

        public Data(Customer customer, Contact contact) {
            this.customer = customer;
            this.contact = contact;
        }
    }

    public CustomerRetriever(View view, WarehouseDb db, long id) {
        this.db = db;
        this.view = view;
        this.id = id;
    }

    @Override
    protected Data doInBackground(Void... voids) {
        Customer customer = db.customerDao().getCustomerById(id);
        Contact contact = db.contactDao().getById(customer.getContact_Id());
        Data data = new Data(customer, contact);
        return data;
    }

    @Override
    protected void onPostExecute(Data result) {
        TextView name = view.findViewById(R.id.name);
        TextView street = view.findViewById(R.id.street);
        TextView city = view.findViewById(R.id.city);
        TextView country = view.findViewById(R.id.country);
        TextView phone = view.findViewById(R.id.phone);

        name.setText("Name: " + result.customer.getName());
        street.setText("Street: " + result.contact.getStreetNumber() + " " + result.contact.getStreetName() + " st");
        city.setText("City: " + result.contact.getPostalCode() + " " + result.contact.getCity());
        country.setText("Country: " + result.contact.getCountry());
        phone.setText("Phone: " + result.contact.getPhoneNumber());
    }
}