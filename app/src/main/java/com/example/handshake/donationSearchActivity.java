package com.example.handshake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class donationSearchActivity extends AppCompatActivity {

    Spinner filterLocation, filterType;
    Button filterDonations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_search);

        filterLocation = findViewById(R.id.donationLocationFilter);
        filterType = findViewById(R.id.donationTypeFiler);
        filterDonations = findViewById(R.id.filterDonations);  // Corrected the button name

        filterDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(donationSearchActivity.this, viewNewDonationsActivity.class));
            }
        });





        // DROP DOWN MENU FOR LOCATION
        ArrayAdapter<CharSequence> adaptertype = ArrayAdapter.createFromResource(
                this,
                R.array.type,
                android.R.layout.simple_spinner_item
        );
        // Set a drop down menu with all donation types
        adaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterType.setAdapter(adaptertype);

        // Selecting a type for donation filter
        filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getItemAtPosition(position).toString();
                Toast.makeText(donationSearchActivity.this, "Selected: " + selectedType, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // DROP DOWN MENU FOR LOCATION
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.regions,
                android.R.layout.simple_spinner_item
        );
        // Set a drop down menu with all regions in Israel
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterLocation.setAdapter(adapter);

        // Selecting a region for donation post
        filterLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedRegion = parentView.getItemAtPosition(position).toString();
                Toast.makeText(donationSearchActivity.this, "Selected: " + selectedRegion, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });



    }


}