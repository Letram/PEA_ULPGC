package com.carlosmartel.project4bis2.data.webServices.soap

import org.ksoap2.transport.HttpTransportSE
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.serialization.SoapObject



class Utils {
    fun getSoapSerializationEnvelope(request: SoapObject): SoapSerializationEnvelope {
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.dotNet = true
        envelope.implicitTypes = true
        envelope.isAddAdornments = false
        envelope.setOutputSoapObject(request)

        return envelope
    }

    fun getHttpTransportSE(url: String): HttpTransportSE {
        val ht = HttpTransportSE(url)
        ht.debug = true
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->")
        return ht
    }
}