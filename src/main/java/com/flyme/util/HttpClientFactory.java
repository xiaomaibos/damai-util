package com.flyme.util;

import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.SSLs;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import org.apache.http.client.HttpClient;

/**
 * @author zzzz76
 */
public class HttpClientFactory {
    public static HttpClientFactory httpClientFactory = new HttpClientFactory();

    private HCB hcb = null;

    public HttpClientFactory() {
        try {
            hcb = HCB.custom()
                    .pool(20, 5)
                    .sslpv(SSLs.SSLProtocolVersion.TLSv1_2)
                    .ssl()
                    .retry(5);
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }
    }

    public HttpClient build() {
        return hcb.build();
    }
}
