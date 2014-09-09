package org.lds.mediafinder.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author Allen Sudweeks
 */
public class TrustEverythingTrustManager implements X509TrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }
}
