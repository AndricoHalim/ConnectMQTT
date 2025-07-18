package com.example.fireapp.mqtt

import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate

object SSLSocketFactoryHelper {
    fun getSocketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        return sslContext.socketFactory
    }
}
