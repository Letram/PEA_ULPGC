package com.carlosmartel.project4.data.webServices.soap

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.ksoap2.serialization.SoapObject

class OrderServiceManager : OrderServiceInterface {
    private val orderWebService: OrderWebService = OrderWebService()

    override fun getOrders(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {

    }

    inner class GetOrdersSOAP(
        private val method: String,
        private val objParams: SoapObject,
        private val completionHandler: (response: SoapObject?) -> Unit
    ) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val response = orderWebService.call(objParams, method)
            Log.v("RESPONSE", response)
            return response
        }
/*
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.v("REPONSE", "On post response: $result")
            try {
                // Recuperamos la respuesta según los nombres de las etiquetas XML.
                val response = envelope.bodyIn as SoapObject
                Log.d("response", response.toString())
                val numero = response.getProperty("ConvertNumberObjectResult") as SoapObject
                if (numero.hasProperty("msgError"))
                    Toast.makeText(
                        getApplicationContext(),
                        numero.getPropertyAsString("msgError"),
                        Toast.LENGTH_SHORT
                    ).show()
                if (numero.hasProperty("cardinal")) textViewCardinal.setText(numero.getPropertyAsString("cardinal"))
                if (numero.hasProperty("ordinal")) textViewOrdinal.setText(numero.getPropertyAsString("ordinal"))
                if (numero.hasProperty("fraccionario")) textViewFraccionario.setText(numero.getPropertyAsString("fraccionario"))
            } catch (ignored: Exception) {
            }

        }
        */
    }

    override fun insertOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}