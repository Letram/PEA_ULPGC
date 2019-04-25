package com.carlosmartel.project4.data.webServices.soap

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.data.webServices.WebData
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import java.io.IOException
import java.lang.Exception
import java.util.*
import org.ksoap2.SoapFault
import org.ksoap2.transport.HttpTransportSE


class SOAPCustomerService : CustomerServiceInterface{

    val soapUtils = Utils()

    override fun getCustomers(path: String, completionHandler: (response: SoapObject?) -> Unit) {
        GetCustomers(path)
    }

    inner class GetCustomers constructor(private val action: String): AsyncTask<Void, Void, String>(){
        var req: SoapObject? = null
        var envelope: SoapEnvelope? = null
        var httpTransport: HttpTransportSE? = null
        override fun doInBackground(vararg params: Void?): String? {
            req = SoapObject(WebData.URN, action)

            envelope = soapUtils.getSoapSerializationEnvelope(req!!)
            httpTransport = soapUtils.getHttpTransportSE(WebData.URL_SOAP)

            try {
                val soapAction = WebData.URN + "/" + action
                httpTransport!!.call(soapAction, envelope)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            if (result!!.isNotEmpty())
                println("ON POST EXECUTE ERROR: $result")
            else {
            }
        }

    }

    override fun insertCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}