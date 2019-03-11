package com.carlosmartel.project3.models.customer

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Customer (
    @PrimaryKey var uid: Int,
    var address: String,
    var name: String
)