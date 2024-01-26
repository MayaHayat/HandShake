package com.example.handshake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostNewDonation extends AppCompatActivity {

    Button createDonation;
    EditText nameDonation, infoDonation;
    RadioGroup radioGroup;
    Spinner chooseLocation;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_donation);

        // User for region choice
        chooseLocation = findViewById(R.id.chooseLocation);
        nameDonation = findViewById(R.id.donationName);
        infoDonation = findViewById(R.id.donationInfo);
        radioGroup = findViewById(R.id.catagoriesDonation);
        createDonation = findViewById(R.id.shareDonationButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.regions,
                android.R.layout.simple_spinner_item
        );
        // Set a drop down menu with all regions in Israel
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseLocation.setAdapter(adapter);

        // Selecting a region for donation post
        chooseLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedRegion = parentView.getItemAtPosition(position).toString();
                Toast.makeText(PostNewDonation.this, "Selected: " + selectedRegion, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }
}