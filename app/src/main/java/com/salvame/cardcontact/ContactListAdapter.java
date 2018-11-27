package com.salvame.cardcontact;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salvame.cardcontact.db.Database;
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = contactEntity.getC_id();
                Database database = Room.databaseBuilder(context, Database.class, "mainDB").allowMainThreadQueries().build();
                database.getContactDao().delete(database.getContactDao().getContactEntityById(id));
                database.close();
                ((ContactList)context).updateContactList();
            }
        });

        holder.phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = contactEntity.getPhone_number();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
                context.startActivity(phoneIntent);
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = contactEntity.getEmail();
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+mail));
                context.startActivity(mailIntent);
            }
        });

        holder.webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = contactEntity.getWebpage();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(browserIntent);
            }
        });
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
        public ImageView delete;

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
            delete = itemView.findViewById(R.id.delete);
        }

    }
}
