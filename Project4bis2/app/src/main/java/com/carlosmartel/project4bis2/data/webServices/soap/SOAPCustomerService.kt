package com.carlosmartel.project4bis2.data.webServices.soap

import android.os.AsyncTask
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.webServices.WebData
import org.ksoap2.SoapEnvelope
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.util.*


class SOAPCustomerService : CustomerServiceInterfaceSOAP {

    val soapUtils = Utils()

    override fun getCustomers(completionHandler: (response: List<Customer>?) -> Unit) {
        GetCustomers(WebData.GET_CUSTOMERS, completionHandler).execute()
    }

    inner class GetCustomers constructor(
        private val action: String,
        private val completionHandler: (response: List<Customer>?) -> Unit
    ) : AsyncTask<Void, Void, List<Customer>>() {
        private var customers: MutableList<Customer> = ArrayList()
        private var req: SoapObject? = null
        private var envelope: SoapEnvelope? = null
        private var httpTransport: HttpTransportSE? = null
        override fun doInBackground(vararg params: Void?): List<Customer> {
            req = SoapObject(WebData.URN, action)

            envelope = soapUtils.getSoapSerializationEnvelope(req!!)
            httpTransport = soapUtils.getHttpTransportSE(WebData.URL_SOAP)

            try {
                val soapAction = WebData.URN + "/" + action
                httpTransport!!.call(soapAction, envelope)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var result: Vector<*>? = null

            try {
                result = (envelope as SoapSerializationEnvelope).response as Vector<*>
            } catch (soapFault: SoapFault) {
                soapFault.printStackTrace()
            }
            if (result != null) {
                for (i in 0 until result.size) {
                    val soapCustomer = result[i] as SoapObject

                    val idObject = soapCustomer.getProperty(0) as SoapObject
                    val customerID = Integer.valueOf(idObject.getProperty(1).toString())

                    val nameObject = soapCustomer.getProperty(1) as SoapObject
                    val customerName = nameObject.getProperty(1).toString()

                    val addressObject = soapCustomer.getProperty(2) as SoapObject
                    val customerAddress = addressObject.getProperty(1).toString()

                    val customerAux = Customer(c_name = customerName, address = customerAddress)
                    customerAux.u_id = customerID
                    customers.add(customerAux)
                }
            }
            return customers
        }

        override fun onPostExecute(result: List<Customer>?) {
            completionHandler(result)
        }
    }

    override fun insertCustomer(params: Customer, completionHandler: (response: Int?) -> Unit) {
        InsertCustomer(WebData.INSERT_CUSTOMER, completionHandler).execute(params)
    }

    inner class InsertCustomer constructor(
        private val action: String,
        private val completionHandler: (response: Int?) -> Unit
    ) : AsyncTask<Customer, Void, Int>() {

        private var insertedId: Int = -1
        private var req: SoapObject? = null
        private var envelope: SoapEnvelope? = null
        private var httpTransport: HttpTransportSE? = null

        override fun doInBackground(vararg params: Customer?): Int? {
            req = SoapObject(WebData.URN, action)
            req!!.addProperty(WebData.CUSTOMER_NAME, params[0]!!.c_name)
            req!!.addProperty(WebData.CUSTOMER_ADDRESS, params[0]!!.address)

            envelope = soapUtils.getSoapSerializationEnvelope(req!!)
            httpTransport = soapUtils.getHttpTransportSE(WebData.URL_SOAP)

            try {
                val soapAction = WebData.URN + "/" + action
                httpTransport!!.call(soapAction, envelope)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var result: Int? = null

            try {
                result = (envelope as SoapSerializationEnvelope).response as Int
            } catch (soapFault: SoapFault) {
                soapFault.printStackTrace()
            }
            if (result != null) {
                insertedId = result
            }
            return insertedId
        }

        override fun onPostExecute(result: Int?) {
            completionHandler(result)
        }

    }


    override fun updateCustomer(params: Customer, completionHandler: (response: Boolean?) -> Unit) {
        UpdateCustomer(WebData.UPDATE_CUSTOMER, completionHandler).execute(params)
    }

    inner class UpdateCustomer constructor(
        private val action: String,
        private val completionHandler: (response: Boolean?) -> Unit
    ) : AsyncTask<Customer, Void, Boolean>() {
        private var hasBeenUpdated: Boolean = false
        private var req: SoapObject? = null
        private var envelope: SoapEnvelope? = null
        private var httpTransport: HttpTransportSE? = null

        override fun doInBackground(vararg params: Customer?): Boolean {
            req = SoapObject(WebData.URN, action)

            req!!.addProperty(WebData.CUSTOMER_ID, params[0]!!.u_id)
            req!!.addProperty(WebData.CUSTOMER_NAME, params[0]!!.c_name)
            req!!.addProperty(WebData.CUSTOMER_ADDRESS, params[0]!!.address)

            println(req.toString())
            envelope = soapUtils.getSoapSerializationEnvelope(req!!)
            httpTransport = soapUtils.getHttpTransportSE(WebData.URL_SOAP)

            try {
                val soapAction = WebData.URN + "/" + action
                httpTransport!!.call(soapAction, envelope)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var result = false

            try {
                result = (envelope as SoapSerializationEnvelope).response as Boolean
            } catch (soapFault: SoapFault) {
                soapFault.printStackTrace()
            }
            hasBeenUpdated = result
            return hasBeenUpdated
        }

        override fun onPostExecute(result: Boolean?) {
            completionHandler(result)
        }
    }

    override fun deleteCustomer(params: Int, completionHandler: (response: Boolean?) -> Unit) {
        DeleteCustomer(WebData.DELETE_CUSTOMER, completionHandler).execute(params)
    }

    inner class DeleteCustomer constructor(
        private val action: String,
        private val completionHandler: (response: Boolean?) -> Unit
    ) : AsyncTask<Int, Void, Boolean>() {

        private var hasBeenDeleted: Boolean = false
        private var req: SoapObject? = null
        private var envelope: SoapEnvelope? = null
        private var httpTransport: HttpTransportSE? = null

        override fun doInBackground(vararg params: Int?): Boolean {
            req = SoapObject(WebData.URN, action)

            req!!.addProperty(WebData.CUSTOMER_ID, params[0]!!)

            envelope = soapUtils.getSoapSerializationEnvelope(req!!)
            httpTransport = soapUtils.getHttpTransportSE(WebData.URL_SOAP)

            try {
                val soapAction = WebData.URN + "/" + action
                httpTransport!!.call(soapAction, envelope)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var result = false

            try {
                result = (envelope as SoapSerializationEnvelope).response as Boolean
            } catch (soapFault: SoapFault) {
                soapFault.printStackTrace()
            }
            hasBeenDeleted = result
            return hasBeenDeleted
        }

        override fun onPostExecute(result: Boolean?) {
            completionHandler(result)
        }
    }
}