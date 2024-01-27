package com.example.handshake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterForDonationSearch extends RecyclerView.Adapter<adapterForDonationSearch.MyViewHolder> {

    Context context;
    ArrayList<Donation> list;

    public adapterForDonationSearch(Context context, ArrayList<Donation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Donation donation = list.get(position);
        holder.donationName.setText(donation.getName());
        holder.donationInfo.setText(donation.getInfo());
        holder.donorName.setText(donation.getDonorName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView donationName, donationInfo, donorName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            donationName = itemView.findViewById(R.id.showDonationName);
            donationInfo = itemView.findViewById(R.id.showDonationInfo);
            donorName = itemView.findViewById(R.id.showDonorName);

        }
    }
}
