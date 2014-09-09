package org.lds.mediafinder.settings;

import org.lds.stack.qa.TestConfig;

public class Constants {

    static {
        new TestConfig().applyConfiguration(Constants.class);
    }

    public static String defaultSsoUser;
    public static String ssoLoginPage;
    public static String authenticationUrl;
    public static String jiraProjectKey;
    public static String jiraFilterId;
    public static String homeUrl;
    public static long defaultWaitTime;
    public static String username;
    public static String passwordValid;
    public static String passwordInvalid;
    public static String catalogShortURL;
    public static String cmcAdminUsername;
    public static String cmcExternalUsername;
    public static String cmcInternalUsername;
    public static String telescopeURL;
    public static String catalogUser;
    public static String catalogPassword;
    public static String testTerm;

    private Constants() {}
}
