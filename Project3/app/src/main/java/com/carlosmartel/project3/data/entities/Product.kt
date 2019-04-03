package com.carlosmartel.project3.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(indices = [Index(value = arrayOf("p_id"))])
data class Product(
    @PrimaryKey(autoGenerate = true) var p_id: Int = 0,
    var p_name: String,
    var price: Float,
    var description: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()!!,
        source.readFloat(),
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(p_id)
        writeString(p_name)
        writeFloat(price)
        writeString(description)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }
}