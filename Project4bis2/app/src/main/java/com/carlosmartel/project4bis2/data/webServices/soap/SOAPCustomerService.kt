package com.carlosmartel.project4bis2.data.webServices.soap

import android.os.AsyncTask
import com.carlosmartel.project4bis2.data.webServices.WebData
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import java.lang.Exception
import org.ksoap2.transport.HttpTransportSE


class SOAPCustomerService : CustomerServiceInterfaceSOAP{

    val soapUtils = Utils()

    override fun getCustomers(path: String, completionHandler: (response: SoapObject?) -> Unit) {
        GetCustomers(path)
    }

    inner class GetCustomers constructor(private val action: String): AsyncTask<Void, Void, String>(){
        private var req: SoapObject? = null
        private var envelope: SoapEnvelope? = null
        private var httpTransport: HttpTransportSE? = null
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