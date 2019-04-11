package com.carlosmartel.project4.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(indices = [Index(value = arrayOf("u_id"))])
data class Customer(
    @PrimaryKey(autoGenerate = true) var u_id: Int = 0,
    var address: String,
    var c_name: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(u_id)
        writeString(address)
        writeString(c_name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Customer> = object : Parcelable.Creator<Customer> {
            override fun createFromParcel(source: Parcel): Customer = Customer(source)
            override fun newArray(size: Int): Array<Customer?> = arrayOfNulls(size)
        }
    }
}