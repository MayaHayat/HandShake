package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class MySavedDonationsActivity extends AppCompatActivity {

    RecyclerView recyclerView2;
    DatabaseReference database;
    adapterForMySavedDonations adapter;
    ArrayList<SavedDonation> savedDonationsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saved_donations);

        recyclerView2 = findViewById(R.id.mySavedDonations);
        database = FirebaseDatabase.getInstance().getReference("TakenDonations");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        savedDonationsList = new ArrayList<>();
        adapter = new adapterForMySavedDonations(this, savedDonationsList);
        recyclerView2.setAdapter(adapter);

        // Access Firebase and get user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();

        adapter.setGotOnClickListener(new adapterForMySavedDonations.OnGotDonationClickListener() {
            @Override
            public void OnGotDonationClick(SavedDonation donation) {
                float rating = donation.getRating();
                sendRatingAfterGettingDonation(donation, rating);
            }
        });

        // Show all donations saved by the specific user
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savedDonationsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SavedDonation donation = dataSnapshot.getValue(SavedDonation.class);
                    if (donation.getRecipientID().equals(userID)){
                        savedDonationsList.add(donation);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Rate donation and remove donation from DB completely

    private void sendRatingAfterGettingDonation(SavedDonation donation, float rating) {
        String donorID = donation.getUserID();
        String donationID = donation.getDonationID();

        // Check for valid rating first
        if (rating < 0 || rating > 5) {
            Toast.makeText(this, "Please enter a valid rating (0-5)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for null values and handle gracefully
        if (donorID == null || donationID == null) {
            Toast.makeText(this, "Error: Missing information. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(donorID);
        DatabaseReference ratingRef = userRef.child("Rating").child(donationID);

        // Update rating with success/error handling
        ratingRef.setValue(rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MySavedDonationsActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                        // Optional actions here (disable button, update UI)
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MySavedDonationsActivity.this, "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        DatabaseReference takenDonationsRef = FirebaseDatabase.getInstance().getReference("TakenDonations");
        takenDonationsRef.child(donationID).removeValue();
    }





}