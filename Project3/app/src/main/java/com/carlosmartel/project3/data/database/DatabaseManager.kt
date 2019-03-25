package com.carlosmartel.project3.data.database

import android.arch.persistence.room.*
import android.content.Context
import com.carlosmartel.project3.data.models.Customer
import com.carlosmartel.project3.data.dao.CustomerQuery
import com.carlosmartel.project3.data.models.Order
import com.carlosmartel.project3.data.dao.OrderQuery
import com.carlosmartel.project3.data.models.Product
import com.carlosmartel.project3.data.dao.ProductQuery
import android.os.AsyncTask.execute
import android.arch.persistence.db.SupportSQLiteDatabase
import android.support.annotation.NonNull
import android.arch.persistence.room.RoomDatabase
import android.os.AsyncTask


@Database(entities = [Customer::class, Order::class, Product::class], version = 1, exportSchema = true)
@TypeConverters(MyTypeConverters::class)
abstract class DatabaseManager: RoomDatabase(){

    /**
     * This is an abstract method that returns a dao for the Db
     * */
    abstract fun customerQuery(): CustomerQuery
    abstract fun productQuery(): ProductQuery
    abstract fun orderQuery(): OrderQuery

    /**
     * A singleton design pattern is used to ensure that the database instance created is one
     * */
    companion object {
        var INSTANCE: DatabaseManager? = null
        private const val dbName = "DatabaseManager"

        fun getInstance(context: Context): DatabaseManager? {
            if (INSTANCE == null){
                synchronized(DatabaseManager::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseManager::class.java,
                        dbName
                    )
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDatabase(){
            INSTANCE = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE).execute()
            }
        }

        class PopulateDbAsyncTask(instance: DatabaseManager?): AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                customerQuery.insert(Customer(address = "Casa", name = "Pepe"))
                customerQuery.insert(Customer(address = "Casa2", name = "Pepe2"))
                customerQuery.insert(Customer(address = "Casa3", name = "Pepe3"))
                return null
            }

            private var customerQuery: CustomerQuery = instance!!.customerQuery()

        }
    }
}