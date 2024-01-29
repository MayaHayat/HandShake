package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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


        myAdapter.setOnItemClickListener(new adapterForDonationSearch.OnItemClickListener() {

            @Override
            public void onSaveDonationClick(Donation donation) {
                // Handle Save Donation click event
                saveDonationAndRemoveFromOriginalList(donation);
            }
        });

        // Access User database and get userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();

        // use userID to get the donor's information
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dList.clear(); // Clear the existing data before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String info = dataSnapshot.child("Info").getValue(String.class);
                        String uid = dataSnapshot.child("UserID").getValue(String.class);

                        // Create a new object with only the necessary fields
                        Donation donation = new Donation();
                        donation.setName(name);
                        donation.setInfo(info);
                        donation.setDonorID(uid);

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

        // Save to "TakenDonations"
        takenDonationsRef.push().setValue(donation);

        // Remove from "Donations"
        originalDonationsRef.child(donation.getDonationId()).removeValue();
    }



}