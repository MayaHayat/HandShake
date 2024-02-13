package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewNewDonationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    adapterForDonationSearch myAdapter;
    ArrayList<Donation> dList;

    Spinner filterLocation, filterType;
    Button filterDonations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_new_donations);

        recyclerView = findViewById(R.id.donationList);
        database = FirebaseDatabase.getInstance().getReference("Donations");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dList = new ArrayList<>();
        myAdapter = new adapterForDonationSearch(this, dList);
        recyclerView.setAdapter(myAdapter);

        filterLocation = findViewById(R.id.donationLocationFilter);
        filterType = findViewById(R.id.donationTypeFiler);
        filterDonations = findViewById(R.id.filterDonations);


        // Saving Donations when clicking the button
        myAdapter.setOnItemClickListener(new adapterForDonationSearch.OnItemClickListener() {

            @Override
            public void onSaveDonationClick(Donation donation) {
                // Handle Save Donation click event
                saveDonationAndRemoveFromOriginalList(donation);
            }
        });


        // DROP DOWN MENU FOR TYPE
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
                Toast.makeText(viewNewDonationsActivity.this, "Selected: " + selectedType, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(viewNewDonationsActivity.this, "Selected: " + selectedRegion, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        filterDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to filter donations
                filterDonations();
            }
        });

    }


    // Method to filter donations based on selected location and type
    private void filterDonations() {
        // Access User database and get userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();

        // Get selected location and type
        String selectedLocation = filterLocation.getSelectedItem().toString();
        String selectedType = filterType.getSelectedItem().toString();

        // Filter donations based on location and type
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dList.clear(); // Clear the existing data before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String info = dataSnapshot.child("Info").getValue(String.class);
                        String uid = dataSnapshot.child("UserID").getValue(String.class);
                        String location = dataSnapshot.child("Location").getValue().toString();
                        String type = dataSnapshot.child("Catagory").getValue().toString();

                        if (selectedLocation.equals(location) && selectedType.equals(type)) {
                            // Create a new object with only the necessary fields
                            Donation donation = new Donation();
                            donation.setName(name);
                            donation.setInfo(info);
                            donation.setUserID(uid);
                            donation.setDonationLocation(location);
                            donation.setType(type);

                            // Store the donation key
                            donation.setDonationID(dataSnapshot.getKey());

                            // Retrieve "Username" from "User" database based on matching user ID
                            reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        String username = userSnapshot.child("Username").getValue(String.class);
                                        String donorRate = userSnapshot.child("Rate").getValue(String.class);
                                        String donorInfo = userSnapshot.child("Info").getValue(String.class);
                                        String donorPhone = userSnapshot.child("Phone number").getValue(String.class);

                                        donation.setUsername(username);
                                        donation.setDonorRate(donorRate);
                                        donation.setDonorInfo(donorInfo);
                                        donation.setDonorPhone(donorPhone);

                                        dList.add(donation);
                                        myAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

                                    } else {
                                        Toast.makeText(viewNewDonationsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle errors if needed
                                }
                            });
                        }
                        else if (dList.isEmpty()){
                            Toast.makeText(viewNewDonationsActivity.this, "No current kind of donations in this area! Can you travel anywhere else? ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(viewNewDonationsActivity.this, "Donation data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }



    private void saveDonationAndRemoveFromOriginalList(Donation donation) {
        DatabaseReference takenDonationsRef = FirebaseDatabase.getInstance().getReference("TakenDonations");
        DatabaseReference originalDonationsRef = FirebaseDatabase.getInstance().getReference("Donations");
        // Access Firebase and get user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String recipientID = user.getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User").child(recipientID);

        // Push the donation to "TakenDonations" and get the generated key
        String takenDonationKey = donation.getDonationID();

        // Check if the key is null (handle this case accordingly)
        if (takenDonationKey == null) {
            return;
        }

        // Get the recipientName from the User node
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    String recipientName = userSnapshot.child("Username").getValue(String.class);
                    String recipientPhone = userSnapshot.child("Phone number").getValue(String.class);
                    String recipientInfo = userSnapshot.child("Info").getValue(String.class);

                    // Update the donation object with recipientName
                    donation.setRecipientName(recipientName);
                    donation.setRecipientInfo(recipientInfo);
                    donation.setRecipientPhone(recipientPhone);
                    donation.setRecipientID(recipientID);


                    // Save to "TakenDonations" with the generated key as the donation ID
                    takenDonationsRef.child(takenDonationKey).setValue(donation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(viewNewDonationsActivity.this, "Donation saved successfully", Toast.LENGTH_SHORT).show();

                                    // Add the donation ID to the user's list of saved donations
                                    DatabaseReference savedDonationsReference = userReference.child("savedDonations");
                                    savedDonationsReference.child(takenDonationKey).setValue(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(viewNewDonationsActivity.this, "Failed to save donation", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(viewNewDonationsActivity.this, "Recipient data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });

        // Remove from "Donations" using the same generated key as the donation ID

        originalDonationsRef.child(takenDonationKey).removeValue();

    }

}