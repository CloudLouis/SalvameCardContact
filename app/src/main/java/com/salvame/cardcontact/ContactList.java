package com.salvame.cardcontact;

import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.NfcAdapter;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import com.salvame.cardcontact.db.Database;
import com.salvame.cardcontact.db.entity.ContactEntity;

import java.util.ArrayList;


public class ContactList extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ContactListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private IntentIntegrator qrScan;
    private Database database;
    private ArrayList<ContactEntity> dataset;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().getContactEntity();

        dataset = new ArrayList<>();
        for(int i = 0;i<contacts.length;i++){
            dataset.add(contacts[i]);
        }

        database.close();

        mRecyclerView = findViewById(R.id.contact_recycler);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContactListAdapter(this, dataset);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        qrScan = new IntentIntegrator(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //adding new values to database and reloading view
                    database = Room.databaseBuilder(this, Database.class, "mainDB").fallbackToDestructiveMigration().allowMainThreadQueries().build();
                    ContactEntity new_contact = new ContactEntity();
                    new_contact.setName(obj.getString("name"));
                    new_contact.setEmail(obj.getString("email"));
                    new_contact.setPhone_number(obj.getString("phone_number"));
                    new_contact.setCompany(obj.getString("company"));
                    new_contact.setLine(obj.getString("line"));
                    new_contact.setWhatsapp(obj.getString("whatsapp"));
                    new_contact.setWebpage(obj.getString("web_link"));
                    database.getContactDao().insert(new_contact);

                    updateContactList();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("web_link")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void openBrowser(View v){
        TextView link_view = findViewById(R.id.contact_webpage);
        String link = link_view.getText().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    public void deleteContact(View v){
        TextView id_view = findViewById(R.id.contact_id);
        int id = Integer.parseInt(id_view.getText().toString());
        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        database.getContactDao().delete(database.getContactDao().getContactEntityById(id));
        database.close();
        updateContactList();
    }

    public void updateContactList(){
        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().getContactEntity();

        dataset.clear();
        for (int i = 0; i < contacts.length; i++) {
            dataset.add(contacts[i]);
        }
        mAdapter.updateItems(dataset);
        database.close();
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
