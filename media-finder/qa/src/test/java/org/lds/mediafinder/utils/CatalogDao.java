package org.lds.mediafinder.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.*;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.ssl.TrustEverythingHostnameVerifier;
import org.lds.mediafinder.ssl.TrustEverythingTrustManager;
import org.lds.stack.tomcat.decrypt.Base64;

public class CatalogDao {

    private static String credentials;

    static {
        credentials = Base64.byteArrayToBase64((Constants.catalogUser + ":" + Constants.catalogPassword).getBytes());
    }

    public static String search(String externalId) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/search?q=" + externalId + "&user=therrin150");
        return execute(url, RequestMethod.GET);
    }
    
    public static String createCollection(String name, String owner, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/create?name=" + name + "&owner=" + owner + "&user=" + user);
        return execute(url, RequestMethod.POST);
    }
    
    public static String createCollection(String name, String owner, String description, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/create?name=" + name + "&owner=" + owner + "&description=" + description + "&user=" + user);
        return execute(url, RequestMethod.POST);
    }

    public static void deleteCollection(String entryId, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/delete/" + entryId + "?user=" + user);
        execute(url, RequestMethod.DELETE);
    }
    
    public static String addToCollection(String entryId, String childEntryIds, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/addItems/" + entryId + "?childEntryIds=" + childEntryIds + "&user=" + user);
        return execute(url, RequestMethod.POST);
    }
    
    public static String addToCollection(String entryId, String childEntryIds, String childIndexes, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/addItems/" + entryId + "?childEntryIds=" + childEntryIds + "&childIndexes=" + childIndexes + "&user=" + user);
        return execute(url, RequestMethod.POST);
    }
    
    public static String removeFromCollection(String entryId, String childEntryIds, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/removeItems/" + entryId + "?childEntryIds=" + childEntryIds + "&user=" + user);
        return execute(url, RequestMethod.POST);
    }
    
    public static void addUserToCollection(String entryId, String userAdd, UserType userType, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/addUser/" + entryId + "?userAdd=" + userAdd + "&userType=" + userType.toString() + "&user=" + user);
        execute(url, RequestMethod.POST);
    }
    
    public static void removeUserFromCollection(String entryId, String userRemove, String user) throws Exception {
        URL url = new URL("https://" + Constants.catalogShortURL + ".ldschurch.org/entry/collection/removeUser/" + entryId + "?userRemove=" + userRemove + "&user=" + user);
        execute(url, RequestMethod.POST);
    }

    private static HttpsURLConnection getConnection(URL url) throws Exception {
        //Set up connection components
        TrustManager[] trustManager = new TrustManager[] {new TrustEverythingTrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManager, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        return (HttpsURLConnection) url.openConnection();
    }

    private static String execute(URL url, RequestMethod method) throws Exception {
        //Establish connection
        HttpsURLConnection con = getConnection(url);
        con.setHostnameVerifier(new TrustEverythingHostnameVerifier());
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod(method.toString());
        con.setRequestProperty("content-type", "application/xml");
        con.addRequestProperty("Authorization", "Basic " + credentials);
        InputStream in = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String read;
        StringBuilder stringResult = new StringBuilder();
        //Read XML string
        while ((read = br.readLine()) != null) {
            stringResult.append(read);
        }
        //Clean up
        br.close();
        in.close();
        return stringResult.toString();
    }
    
}
