package com.kavin.socialevening.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.interfaces.ContactSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public class ContactsAdapter extends BaseAdapter {

    private ContactSelectedListener mContactSelectedListener;

    private final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.Contacts.PHOTO_ID
    };

    private List<ContactsHolder> contactsHolders = new ArrayList<ContactsHolder>();

    public void setContactSelectedListener(ContactSelectedListener mContactSelectedListener) {
        this.mContactSelectedListener = mContactSelectedListener;
    }

    class ContactsHolder {
        public String name;
        public String email;
    }

    private Context mContext;



    public ContactsAdapter(Context context) {
        this.mContext = context;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            try {
                final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                long contactId;
                byte[] thumbnailBytes = new byte[0];
                while (cursor.moveToNext()) {
                    contactId = cursor.getLong(contactIdIndex);
                    ContactsHolder contactsHolder = new ContactsHolder();
                    contactsHolder.email = cursor.getString(emailIndex);
                    contactsHolder.name = cursor.getString(displayNameIndex);
                    contactsHolders.add(contactsHolder);
                }
            } finally {
                cursor.close();
            }
        }
    }

    @Override
    public int getCount() {
        return contactsHolders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.invite_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.sub_title);
            viewHolder.add = (ImageButton) convertView.findViewById(R.id.add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(contactsHolders.get(position).name);
        viewHolder.subTitle.setText(contactsHolders.get(position).email);

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactSelectedListener.onContactSelected(contactsHolders.get(position).email, contactsHolders.get(position).name);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public TextView subTitle;
        public ImageButton add;
    }
}
