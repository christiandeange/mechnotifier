package com.deange.mechnotifier.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.deange.mechnotifier.R
import com.deange.mechnotifier.dagger.Scope
import com.deange.mechnotifier.dagger.Scoped
import javax.inject.Inject

class NotificationChannels
@Inject constructor(
  private val application: Application
) : Scoped {
  override fun onEnterScope(scope: Scope) {
    if (Build.VERSION.SDK_INT >= 26) {
      val title = application.getString(R.string.notification_channel_posts_title)
      val description = application.getString(R.string.notification_channel_posts_description)
      val channel = NotificationChannel(POSTS_CHANNEL, title, IMPORTANCE_DEFAULT).also { channel ->
        channel.description = description
      }

      val notificationManager = getSystemService(application, NotificationManager::class.java)!!
      notificationManager.createNotificationChannel(channel)
    }
  }

  companion object {
    const val POSTS_CHANNEL = "new-posts"
  }
}
