package com.salvame.cardcontact;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salvame.cardcontact.db.entity.ContactEntity;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private Context context;
    private List<ContactEntity> data;
    private LayoutInflater mInflater;

    ContactListAdapter(Context contex, List<ContactEntity> data){
        this.context = contex;
        this.data = data;
        this.mInflater = LayoutInflater.from(context);
    }
    ContactListAdapter(){}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.contact_list_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ContactEntity contactEntity = data.get(position);
        holder.name.setText(contactEntity.getName());
        holder.email.setText(contactEntity.getEmail());
        holder.phone_number.setText(contactEntity.getPhone_number());
        holder.whatsapp.setText(contactEntity.getWhatsapp());
        holder.line.setText(contactEntity.getLine());
        holder.company.setText(contactEntity.getCompany());
        holder.webpage.setText(contactEntity.getWebpage());
        holder.id_view.setText(String.valueOf(contactEntity.getC_id()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateItems(List<ContactEntity> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView email;
        public TextView phone_number;
        public TextView whatsapp;
        public TextView line;
        public TextView company;
        public TextView webpage;
        public TextView id_view;

        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            email = itemView.findViewById(R.id.contact_email);
            phone_number = itemView.findViewById(R.id.contact_phone_number);
            whatsapp = itemView.findViewById(R.id.contact_whatsapp);
            line = itemView.findViewById(R.id.contact_line);
            company = itemView.findViewById(R.id.contact_company);
            webpage = itemView.findViewById(R.id.contact_webpage);
            id_view = itemView.findViewById(R.id.contact_id);
        }

    }
}
