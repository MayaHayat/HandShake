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

public class ViewDonationsISavedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    adapterForDonationsISaved adapter;
    ArrayList<TakenDonations> savedDonationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donations_isaved);

        recyclerView = findViewById(R.id.donationsIsavedRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("TakenDonations");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        savedDonationList = new ArrayList<>();
        adapter = new adapterForDonationsISaved(this, savedDonationList);
        recyclerView.setAdapter(adapter);

        // Access Firebase and get user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userID = user.getUid();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    savedDonationList.clear(); // Clear the list before adding new data
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TakenDonations donation = dataSnapshot.getValue(TakenDonations.class);
                        if (donation.getRecId().equals(userID)) {
                           // savedDonationList.add(donation);
                            Toast.makeText(getApplicationContext(), "Donation does not belong to the current user", Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error, you can log it for debugging purposes
            }
        });
    }

}