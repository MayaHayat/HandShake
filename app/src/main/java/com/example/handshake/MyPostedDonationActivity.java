package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

// This class shows all donations that a specific user has posted
public class MyPostedDonationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    adapterForMyPostedDonations adapter;
    ArrayList<postedDonation> postedDonationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_donation);

        recyclerView = findViewById(R.id.posedDonationRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("TakenDonations");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postedDonationArrayList = new ArrayList<>();
        adapter = new adapterForMyPostedDonations(this, postedDonationArrayList);
        recyclerView.setAdapter(adapter);

        // Access Firebase and get user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();


        adapter.setRepostOnClickListener(new adapterForMyPostedDonations.OnRepostDonationClickListener() {
            @Override
            public void onRepostDonationClick(postedDonation donation) {
                repostDonationToFirebase(donation);
            }
        });


        // Get all donations donor posted and were saved
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    postedDonation donation = dataSnapshot.getValue(postedDonation.class);
                    if (donation.getUserID().equals(userID) ){
                        postedDonationArrayList.add(donation);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Move the donation back from "TakenDonations" to "Donations" db
    private void repostDonationToFirebase(postedDonation donation) {
        DatabaseReference donationsRef = FirebaseDatabase.getInstance().getReference("Donations");
        DatabaseReference takenDonationsRef = FirebaseDatabase.getInstance().getReference("TakenDonations");

        String originalDonationKey = donation.getDonationID();
        if (originalDonationKey == null) {
            return;
        }

        // Get the donation data from "Donations"
        takenDonationsRef.child(originalDonationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // Get the donation data
                    postedDonation repostedDonation = dataSnapshot.getValue(postedDonation.class);

                    // Add the donation to "Donations" with a new key
                    String newTakenDonationKey = takenDonationsRef.push().getKey();


                    donationsRef.child(newTakenDonationKey).setValue(donation);

                    // Remove the donation from "TakenDonations"
                    takenDonationsRef.child(originalDonationKey).removeValue();


                    // Show a Toast indicating success
                    Toast.makeText(MyPostedDonationActivity.this, "Donation reposted successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MyPostedDonationActivity.this, "Couldn't find data", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MyPostedDonationActivity.this, "Failed to repost donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}