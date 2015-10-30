package com.kavin.socialevening.utils;

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
}
