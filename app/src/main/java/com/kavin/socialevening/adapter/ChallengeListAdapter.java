package com.kavin.socialevening.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.activities.ChallengeInfoScreen;
import com.kavin.socialevening.activities.ChallengeScreen;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 01/11/15
 * Author     : Kavin Varnan
 */
public class ChallengeListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ParseObject> mChallenges;
    private List<Boolean> mDidISendTheChallenge = new ArrayList<Boolean>();


    public ChallengeListAdapter(Context context, List<ParseObject> parseObjects, List<Boolean> didISendTheChallenge) {
        this.mContext = context;
        this.mChallenges = parseObjects;
        this.mDidISendTheChallenge = didISendTheChallenge;
    }

    @Override
    public int getCount() {
        return mChallenges.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_challenge_item, null);
            viewHolder.challenge1 = (RoundedImageView) convertView.findViewById(R.id.challenge1);
            viewHolder.challenge2 = (RoundedImageView) convertView.findViewById(R.id.challenge2);
            viewHolder.teamName1 = (TextView) convertView.findViewById(R.id.team_name1);
            viewHolder.teamName2 = (TextView) convertView.findViewById(R.id.team_name2);
            viewHolder.createdAt = (TextView) convertView.findViewById(R.id.created_at);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChallengeInfoScreen.class).putExtra(Constants.Intent.OBJECT_ID, mChallenges.get(position).getObjectId()));
            }
        });

        if (mDidISendTheChallenge.get(position)) {
            ParseFile imageFile1 = mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(mContext).load(imageFile1.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(viewHolder.challenge1);
            viewHolder.teamName1.setText(mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getString(Constants.Parse.Team.NAME));

            ParseFile imageFile2 = mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(mContext).load(imageFile2.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(viewHolder.challenge2);
            viewHolder.teamName2.setText(mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME));

            viewHolder.createdAt.setText("Your team has made the challenge");
        } else {
            ParseFile imageFile1 = mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(mContext).load(imageFile1.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(viewHolder.challenge2);
            viewHolder.teamName2.setText(mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME));


            ParseFile imageFile2 = mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(mContext).load(imageFile2.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(viewHolder.challenge1);
            viewHolder.teamName1.setText(mChallenges.get(position).getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getString(Constants.Parse.Team.NAME));
            viewHolder.createdAt.setText("Your have been challenged");
        }




        return convertView;
    }

    class ViewHolder {
        public RoundedImageView challenge1;
        public RoundedImageView challenge2;
        public TextView teamName1;
        public TextView teamName2;
        public TextView createdAt;
        public TextView status;
    }
}
