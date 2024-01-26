package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PostNewDonation extends AppCompatActivity {

    Button createDonation;
    EditText nameDonation, infoDonation;
    RadioGroup radioGroup;
    Spinner chooseLocation;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // Strings to save the info of the donation:
    String getDonationName, getDonationInfo, getCatagory, getDonationLocation;

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


        createDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDonationName = nameDonation.getText().toString();
                getDonationInfo = infoDonation.getText().toString();
                getDonationLocation = chooseLocation.getSelectedItem().toString();
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                getCatagory = selectedRadioButton.getText().toString();
                saveUserDataToDatabase();

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

    private void saveUserDataToDatabase() {
        // Save information to Firebase Database
        HashMap<String, Object> donationMap = new HashMap<>();
        donationMap.put("Name", getDonationName);
        donationMap.put("Info", getDonationInfo);
        donationMap.put("Catagory", getCatagory);
        donationMap.put("Location", getDonationLocation);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();

        databaseReference.child("Donations").child(userID).setValue(donationMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostNewDonation.this, "Data added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostNewDonation.this, "Couldn't add data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}