package com.target.supermarket.domain.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TargetNotification(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title:String,
    val thumbnail:String,
    val image:String,
    val description:String,
    val date:String,
    val read:Boolean,
    val target:String,
    val destination:String,
    val notified:Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(thumbnail)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeByte(if (read) 1 else 0)
        parcel.writeString(target)
        parcel.writeString(destination)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TargetNotification> {
        override fun createFromParcel(parcel: Parcel): TargetNotification {
            return TargetNotification(parcel)
        }

        override fun newArray(size: Int): Array<TargetNotification?> {
            return arrayOfNulls(size)
        }
    }
}
