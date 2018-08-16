package com.bogucki.router.utils;

/**
 * Created by Michał Bogucki
 */

public class ConstantValues {

    public static final String CLIENTS_FIREBASE = "clients";
    public static final String MEETINGS_FIREBASE = "meetings";

    public static final String CLIENT_ID_BUNDLE_KEY = "clientID";
    public static final String CLIENT_NAME_BUNDLE_KEY = "clientName";
    public static final String CLIENT_ADDRESS_BUNDLE_KEY = "clientAdress";

    public static final String CHOOSE_ACTION_BUNDLE_KEY    = "actionID";
    public static final int    REMOVE_CLIENT_BUNDLE_VALUE  = 0;
    public static final int    EDIT_CLIENT_BUNDLE_VALUE    = 1;
    public static final int    ADD_CLIENT_BUNDLE_VALUE     = 2;
    public static final int    REMOVE_MEETING_BUNDLE_VALUE = 4;
    public static final int    EDIT_MEETING_BUNDLE_VALUE   = 5;


    public static final String FROM_MEETINGS_OR_FROM_CLIENTS_BUNDLE_KEY = "clientsOrMeetings";
    public static final String MEETING_DATE_BUNDLE_KEY                  = "meetingDate";
    public static final String MEETING_ID_BUNDLE_KEY                    = "meetingID";
    public static final String MEETING_REASON_BUNDLE_KEY                = "meetingReason";
    public static final String MEETING_ORDER                            = "meetingOrder";
    public static final String EARLIEST_TIME_BUNDLE_KEY                 ="etp";
    public static final String LATEST_TIME_BUNDLE_KEY                   ="ltp";
}
