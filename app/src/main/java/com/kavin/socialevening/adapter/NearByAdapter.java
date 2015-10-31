package com.kavin.socialevening.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.activities.TeamInfoActivity;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 31/10/15
 * Author     : Kavin Varnan
 */
public class NearByAdapter extends BaseAdapter {
    private Context mContext;
    private List<ParseObject> mTeamObjectList;


    public NearByAdapter(Context context, List<ParseObject> parseObjects) {
        this.mContext = context;
        this.mTeamObjectList = parseObjects;
    }

    @Override
    public int getCount() {
        return mTeamObjectList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_team_item, null);
            viewHolder.image = (RoundedImageView) convertView.findViewById(R.id.image);
            viewHolder.teamName = (TextView) convertView.findViewById(R.id.team_name);
            viewHolder.createdAt = (TextView) convertView.findViewById(R.id.created_at);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ParseFile imageFile = mTeamObjectList.get(position).getParseFile(Constants.Parse.Team.PICTURE);
        Picasso.with(mContext).load(imageFile.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                .into(viewHolder.image);
        viewHolder.teamName.setText(mTeamObjectList.get(position).getString(Constants.Parse.Team.NAME));
        viewHolder.createdAt.setText("Created @ " + mTeamObjectList.get(position).getString(Constants.Parse.Team.LOCATION_NAME));

        viewHolder.status.setText("Click to challenge");

        return convertView;
    }

    class ViewHolder {
        public RoundedImageView image;
        public TextView teamName;
        public TextView createdAt;
        public TextView status;
    }
}
