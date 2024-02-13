package com.example.handshake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterForMyPostedDonations extends RecyclerView.Adapter<adapterForMyPostedDonations.MyViewHolder3> {
    Context context;
    ArrayList<postedDonation> mydonationsList;

    OnRepostDonationClickListener repostOnClickListener;

    public adapterForMyPostedDonations(Context context, ArrayList<postedDonation> mydonationsList) {
        this.context = context;
        this.mydonationsList = mydonationsList;
    }

    // Interface for Handling Clicks
    public interface OnRepostDonationClickListener {
        void onRepostDonationClick(postedDonation donation) ;
    }

    public void setRepostOnClickListener(OnRepostDonationClickListener listener) {
        this.repostOnClickListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myposteddonations, parent,false);
        return new MyViewHolder3(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        postedDonation donation = mydonationsList.get(position);
        holder.donationName.setText(donation.getDonationName());
        holder.donationInfo.setText(donation.getDonationInfo());
        holder.recipientName.setText(donation.getRecipientName());
        holder.recipientInfo.setText(donation.getRecipientInfo());
        holder.recipientPhone.setText(donation.getRecipientPhone());

        holder.repostDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repostOnClickListener != null) {
                    repostOnClickListener.onRepostDonationClick(donation);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mydonationsList.size();
    }

    public static class MyViewHolder3 extends RecyclerView.ViewHolder{

        TextView donationName, donationInfo, recipientName, recipientPhone, recipientInfo;
        Button repostDonation;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);

            donationName = itemView.findViewById(R.id.postedDonationName);
            donationInfo = itemView.findViewById(R.id.postedDonationInfo);
            recipientName = itemView.findViewById(R.id.postedRecepientName);
            recipientPhone = itemView.findViewById(R.id.postedRecepientPhone);
            recipientInfo = itemView.findViewById(R.id.postedRecepientInfo);
            repostDonation = itemView.findViewById(R.id.repostDonation);


        }
    }
}
