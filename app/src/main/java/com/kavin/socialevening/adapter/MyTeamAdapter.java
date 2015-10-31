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
public class MyTeamAdapter extends BaseAdapter {

    private Context mContext;
    private List<ParseObject> mTeamObjectList;


    public MyTeamAdapter(Context context, List<ParseObject> parseObjects) {
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

        ParseUser teamAdmin = mTeamObjectList.get(position).getParseUser(Constants.Parse.Team.TEAM_ADMIN);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TeamInfoActivity.class).putExtra(Constants.Intent.OBJECT_ID, mTeamObjectList.get(position).getObjectId()));
            }
        });

        if (teamAdmin.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            viewHolder.status.setText("You are the admin");
        } else {
            List<String> friends = (List<String>) mTeamObjectList.get(position).get(Constants.Parse.Team.JOINED_FRIENDS);
            if (friends.contains(ParseUser.getCurrentUser().getEmail())) {
                viewHolder.status.setText("You are a member");
            } else {
                viewHolder.status.setText("You are invited. Click to join");
            }
        }

        return convertView;
    }

    class ViewHolder {
        public RoundedImageView image;
        public TextView teamName;
        public TextView createdAt;
        public TextView status;
    }

    public enum TeamType {
        ADMIN, MEMBER, REQUESTED
    }
}
