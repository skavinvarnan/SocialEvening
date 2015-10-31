package com.kavin.socialevening.utils;

import com.parse.ParseUser;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public final class Constants {
    private static final BaseUrls BASE_URLS = BaseUrls.STAGING;

    public static final String BASE_URL = getBaseUrl(BASE_URLS);

    public static final String OPEN_STREET_BASE_URL = "http://nominatim.openstreetmap.org";

    private static final String BASE_URL_STAGING = "To be added";
    private static final String BASE_URL_PRODUCTION = "To be added";


    private static String getBaseUrl(BaseUrls baseUrls) {
        switch (baseUrls) {
            case PRODUCTION:
                return BASE_URL_PRODUCTION;
            case STAGING:
                return BASE_URL_STAGING;
            default:
                return BASE_URL_STAGING;
        }
    }

    public enum BaseUrls {
        PRODUCTION, STAGING
    }


    public final class Others {
        public final static boolean PRINT_LOG = true;
        public final static boolean PRINT_SERVER_CALL_LOGS = true;
    }

    public final class Parse {
        public final class User {
            public static final String USER = "_User";
            public static final String FB_ID = "fbId";
            public static final String FB_NAME = "fbName";
            public static final String NAME = "name";
            public static final String EMAIL = "email";
        }

        public final class Team {
            public static final String TEAM = "Team";
            public static final String NAME = "name";
            public static final String PICTURE = "picture";
            public static final String LOCATION_NAME = "locationName";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String FRIENDS_LIST = "friendsList";
            public static final String TEAM_ADMIN = "teamAdmin";
            public static final String JOINED_FRIENDS = "joinedFriends";

        }

        public final class Challenge {
            public static final String CHALLENGE = "Challenge";
            public static final String CHALLENGE_TO = "challengeTo";
            public static final String CHALLENGE_FROM = "challengeFrom";
            public static final String CHALLENGE_STRING = "challengeString";
        }
    }

    public final class SP {
        public static final String TEMP_SELFIE_LOCATION = "tempSelfieLocation";
    }

    public final class Location {
        public static final String LAST_FETCHED_TIME = "lastFetchedTime";
        public static final String LAST_FETCHED_NAME = "lastFetchedName";
        public static final String LAST_FETCHED_LAT = "lastFetchedLat";
        public static final String LAST_FETCHED_LONG = "lastFetchedLong";
    }


    public final class Intent {

        public static final String OBJECT_ID = "objectId";
        public static final String GO_TO_HOME = "goToHome";
    }

    public final class PushType {
        public static final int FRIEND_INVITED_TO_TEAM = 101;
        public static final int FRIEND_ACCEPTED_INVITATION = 102;
        public static final int FRIEND_DECLINED_INVITATION = 103;

        public static final int CHALLENGE_CREATED = 104;
        public static final int CHALLENGE_ACCEPTED = 105;
        public static final int CHALLENGE_DECLINED = 106;
    }
}
