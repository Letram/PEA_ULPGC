package com.carlosmartel.project3.dao

import android.arch.persistence.room.*
import com.carlosmartel.project3.models.Customer

@Dao
interface CustomerQuery{
    @Query("SELECT * FROM Customer WHERE uid = :uid")
    fun getCustomer(uid: Int): Customer

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): List<Customer>

    @Insert
    fun insertAll(vararg customers: Customer)

    @Delete
    fun deleteCustomer(customer: Customer)

    @Query("DELETE FROM Customer")
    fun deleteAllCustomers()

    @Update
    fun updateCustomer(customer: Customer)
}