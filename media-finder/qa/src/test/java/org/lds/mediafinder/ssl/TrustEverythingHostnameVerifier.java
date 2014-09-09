package org.lds.mediafinder.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 *
 * @author Allen Sudweeks
 */
public class TrustEverythingHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String string, SSLSession sslSession) {
        return true;
    }
}
