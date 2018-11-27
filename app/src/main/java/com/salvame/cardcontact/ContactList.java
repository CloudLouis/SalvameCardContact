package com.salvame.cardcontact;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.NfcAdapter;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import com.salvame.cardcontact.db.Database;
import com.salvame.cardcontact.db.entity.ContactEntity;
import com.salvame.nfcreader.parser.NdefMessageParser;
import com.salvame.nfcreader.record.ParsedNdefRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ContactList extends AppCompatActivity implements View.OnClickListener {
    private ContactListAdapter mAdapter;
    private IntentIntegrator qrScan;
    private Database database;
    private ArrayList<ContactEntity> dataset;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView mRecyclerView;
        List<String> categories;
        RecyclerView.LayoutManager mLayoutManager;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().getContactEntity();
        database.close();

        dataset = new ArrayList<>();
        Collections.addAll(dataset, contacts);


        mRecyclerView = findViewById(R.id.contact_recycler);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContactListAdapter(this, dataset);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categories = new ArrayList<>();
        categories.add("name");
        categories.add("email");
        categories.add("phone_number");
        categories.add("whatsapp");
        categories.add("line");
        categories.add("company");
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        cat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner search_cat = findViewById(R.id.search_category);
        search_cat.setAdapter(cat_adapter);

        final Spinner sort_cat = findViewById(R.id.sort_option);
        sort_cat.setAdapter(cat_adapter);

        ImageButton search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText query_field = findViewById(R.id.search_field);
                String query = query_field.getText().toString();
                query = "%"+query+"%";
                String category = search_cat.getSelectedItem().toString();
                searchContact(category, query);
            }
        });

        ImageButton sort_button  = findViewById(R.id.sort_button);
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = sort_cat.getSelectedItem().toString();
                String direction = "ASC";
                sortContact(cat, direction);
            }
        });

        ImageButton sort_button2  = findViewById(R.id.sort_button2);
        sort_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = sort_cat.getSelectedItem().toString();
                String direction = "DESC";
                sortContact(cat, direction);
            }
        });

        qrScan = new IntentIntegrator(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
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

                    addContact(obj);
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

    public void addContact(JSONObject obj) throws JSONException {
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

        database.close();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("web_link")));
        startActivity(browserIntent);

    }

    public void updateContactList(){
        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().getContactEntity();

        dataset.clear();
        Collections.addAll(dataset, contacts);
        mAdapter.updateItems(dataset);
        database.close();
    }

    public void searchContact(String cat, String query){
        String queryString = "SELECT * FROM contacts WHERE "+cat+" LIKE '"+query+"';";
        SimpleSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery(queryString);
        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().queryDatabase(sqLiteQuery);

        dataset.clear();
        Collections.addAll(dataset, contacts);
        mAdapter.updateItems(dataset);
        database.close();
    }

    public void sortContact(String cat, String direction){
        String queryString = "SELECT * FROM contacts ORDER BY "+cat+" "+direction+";";
        SimpleSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery(queryString);
        database = Room.databaseBuilder(this, Database.class, "mainDB").allowMainThreadQueries().build();
        ContactEntity[] contacts = database.getContactDao().queryDatabase(sqLiteQuery);

        dataset.clear();
        Collections.addAll(dataset, contacts);
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
        if (id == R.id.action_search) {
            ConstraintLayout search_container = findViewById(R.id.search_container);
            search_container.setVisibility(View.VISIBLE);
            ConstraintLayout sort_container = findViewById(R.id.sort_container);
            sort_container.setVisibility(View.GONE);
        }
        if (id == R.id.action_sort) {
            updateContactList();
            ConstraintLayout search_container = findViewById(R.id.search_container);
            search_container.setVisibility(View.GONE);
            ConstraintLayout sort_container = findViewById(R.id.sort_container);
            sort_container.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    //NFC READER PART OF THE CODE
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        try {
            resolveIntent(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resolveIntent(Intent intent) throws JSONException {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            getMsg(msgs);
        }
    }

    private void getMsg(NdefMessage[] msgs) throws JSONException {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str);
        }

        final JSONObject obj = new JSONObject(builder.toString());
        AlertDialog.Builder Abuilder = new AlertDialog.Builder(this);
        Abuilder.setCancelable(true);
        Abuilder.setTitle("NFC Reader");
        Abuilder.setMessage("Confirm adding this contact?\n"+obj);
        Abuilder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            addContact(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Abuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ContactList.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = Abuilder.create();
        dialog.show();
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
}
