package com.huda.submission_5_made.model

import android.os.Parcel
import android.os.Parcelable

data class RootData(
    var id: Int = 0,
    var name: String? = "",
    var photo: String? = "",
    var description: String? = "",
    var date: String? = "",
    var rate: Double = 0.1,
    var search: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(photo)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeDouble(rate)
        parcel.writeString(search)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RootData> {
        override fun createFromParcel(parcel: Parcel): RootData {
            return RootData(parcel)
        }

        override fun newArray(size: Int): Array<RootData?> {
            return arrayOfNulls(size)
        }
    }
}