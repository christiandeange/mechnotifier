package com.deange.mechnotifier.dagger

import android.app.Application
import com.deange.mechnotifier.db.MechnotifierDatabase
import com.deange.mechnotifier.db.PostDao
import dagger.Module
import dagger.Provides

@Module
class DbModule {
  @Provides @SingleInApp
  fun providesDatabase(application: Application): MechnotifierDatabase {
    return MechnotifierDatabase.get(application)
  }

  @Provides @SingleInApp
  fun providesPostDao(mechnotifierDatabase: MechnotifierDatabase): PostDao {
    return mechnotifierDatabase.postDao()
  }
}
