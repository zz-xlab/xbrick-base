package cn.zzstc.lzm.common.data.dao

import androidx.room.*
import cn.zzstc.lzm.common.BaseApp
import cn.zzstc.lzm.common.data.entity.Converters
import cn.zzstc.lzm.common.data.entity.ServerAddress

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<T>)

    @Delete
    fun delete(element: T)

    @Delete
    fun deleteList(elements: List<T>)

    @Delete
    fun deleteSome(vararg elements: T)

    @Update
    fun update(element: T)

}

@Dao
interface ServerAddressDao : BaseDao<ServerAddress> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: ServerAddress)

    @Query("select * from ServerAddress where active = :active")
    fun getActive(active: Boolean): List<ServerAddress>

    @Query("select * from ServerAddress order by url desc ")
    fun getAll(): List<ServerAddress>

    @Query("delete from ServerAddress")
    fun deleteAll()

    @Query("delete from ServerAddress where url = :url")
    fun deleteById(url: String)

}


@TypeConverters(Converters::class)
@Database(entities = [ServerAddress::class], version = 1)
abstract class CommonDb : RoomDatabase() {
    abstract fun getServerAddressDao(): ServerAddressDao

    companion object {
       private var instance :CommonDb?=null
        fun get():CommonDb{
            synchronized(this) {
                if(instance==null){
                    instance = Room.databaseBuilder(
                        BaseApp.instance(),
                        CommonDb::class.java,
                        "lzm_common.db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance!!
        }
    }



}