package com.target.supermarket.domain.models

import android.os.Parcel
import android.os.Parcelable
import android.text.BoringLayout
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalProduct(
    @PrimaryKey val id: Int,
    val category: String,
    val description: String,
    val discount: Int,
    val image: String,
    val name: String,
    val price: String,
    val quantity: Int,
    val unit:String,
    val availability:Boolean,
    var qtyInCart:Int,
    var section:String,
    var recomPoints:Int = 0,
    var dealPoints:Int = 0,
    var deliveryPoints:Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()==1,
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeInt(discount)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(quantity)
        parcel.writeString(unit)
        parcel.writeInt(if (availability) 1 else 0)
        parcel.writeInt(qtyInCart)
        parcel.writeString(section)
        parcel.writeInt(recomPoints)
        parcel.writeInt(dealPoints)
        parcel.writeInt(deliveryPoints)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocalProduct> {
        override fun createFromParcel(parcel: Parcel): LocalProduct {
            return LocalProduct(parcel)
        }

        override fun newArray(size: Int): Array<LocalProduct?> {
            return arrayOfNulls(size)
        }
    }
}
