package com.carlosmartel.project4.data.webServices.soap

import com.carlosmartel.project4.data.webServices.WebData
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception

class OrderWebService {
    fun call(request: SoapObject, method: String): String {
        var result = ""
        val SOAP_ACTION = WebData.URL + method

        val envelope = getSoapSerializationEnvelope(request)

        val httpTransportSE = HttpTransportSE(WebData.URL)

        try {
            httpTransportSE.call(SOAP_ACTION, envelope)
            val soapResponse = envelope.response
            result = soapResponse.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    // Configuración de un sobre para comunicarse con un servicio web.
    // Devuelve un sobre a partir de una petición de Objeto Soap.
    private fun getSoapSerializationEnvelope(request: SoapObject): SoapSerializationEnvelope {
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
        envelope.dotNet = true // Si se trata de un servicio web .NET
        envelope.implicitTypes = true //
        envelope.isAddAdornments = false
        envelope.setOutputSoapObject(request)
        return envelope
    }
}