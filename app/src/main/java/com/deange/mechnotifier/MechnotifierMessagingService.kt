package com.deange.mechnotifier

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MechnotifierMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(message: RemoteMessage) {
    println("onMessageReceived + ${message.data}")
  }

  override fun onNewToken(token: String) {
    println("onNewToken = $token")
  }
}
