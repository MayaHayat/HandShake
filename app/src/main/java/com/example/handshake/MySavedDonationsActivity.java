package com.example.handshake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
                sendRatingAfterGettingDonation();
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

    private void sendRatingAfterGettingDonation() {

    }
}