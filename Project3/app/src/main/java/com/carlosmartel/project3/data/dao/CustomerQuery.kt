package com.carlosmartel.project3.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.carlosmartel.project3.data.entities.Customer

@Dao
interface CustomerQuery{
    @Query("SELECT * FROM Customer WHERE u_id = :uid")
    fun getCustomer(uid: Int): Customer

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Insert
    fun insertAll(vararg customers: Customer)

    @Insert
    fun insert(customer: Customer)

    @Delete
    fun deleteCustomer(customer: Customer)

    @Query("DELETE FROM Customer")
    fun deleteAllCustomers()

    @Update
    fun updateCustomer(customer: Customer)
}