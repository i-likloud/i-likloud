package com.ssafy.likloud.data.db

import androidx.room.Dao

@Dao
interface BaseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveArtists(artists : List<Artist>)
//
//    @Query("DELETE FROM popular_artists")
//    suspend fun deleteAllArtists()
//
//    @Query("SELECT * FROM popular_artists")
//    suspend fun getArtists():List<Artist>
}