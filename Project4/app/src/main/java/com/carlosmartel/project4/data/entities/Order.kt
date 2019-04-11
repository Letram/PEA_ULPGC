package com.carlosmartel.project4.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

@Entity(
    indices = [
        Index(value = arrayOf("orderID")),
        Index(value = arrayOf("uid")),
        Index(value = arrayOf("productID"))
    ],
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = arrayOf("u_id"),
            childColumns = arrayOf("uid"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = arrayOf("p_id"),
            childColumns = arrayOf("productID"),
            onDelete = ForeignKey.RESTRICT
        )
    ]
)

data class Order(
    @PrimaryKey(autoGenerate = true) var orderID: Int = 0,
    var uid: Int,
    var productID: Int,
    var code: String,
    var date: Date?,
    var quantity: Short
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString()!!,
        source.readSerializable() as Date?,
        source.readInt().toShort()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(orderID)
        writeInt(uid)
        writeInt(productID)
        writeString(code)
        writeSerializable(date)
        writeInt(quantity.toInt())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
            override fun createFromParcel(source: Parcel): Order = Order(source)
            override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
        }
    }
}