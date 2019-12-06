package cn.zzstc.lzm.common.data.entity

import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["url"])
data class ServerAddress(
    @field:SerializedName("url")
    var url: String,
    @field:SerializedName("title")
    var type: AddressType,
    @field:SerializedName("active")
    var active: Boolean
)

enum class AddressType {
    Other, Debug, Test, Preview, Production
}

class Converters {
    @TypeConverter
    fun toInt(value: AddressType): Int = value.ordinal

    @TypeConverter
    fun fromInt(value: Int): AddressType = AddressType.values()[value]
}