package com.deange.mechnotifier.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deange.mechnotifier.model.Post

@Database(version = 1, entities = [Post::class])
abstract class MechnotifierDatabase: RoomDatabase() {
  abstract fun postDao(): PostDao

  companion object {
    fun get(context: Context): MechnotifierDatabase {
      return Room.databaseBuilder(
        context.applicationContext,
        MechnotifierDatabase::class.java,
        "mechnotifier"
      ).build()
    }
  }
}
