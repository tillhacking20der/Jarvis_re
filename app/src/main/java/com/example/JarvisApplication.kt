package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.data.local.JarvisDatabase
import com.example.data.preferences.JarvisPreferences
import com.example.data.repository.JarvisRepository

class JarvisApplication : Application() {

    lateinit var database: JarvisDatabase
        private set

    lateinit var preferences: JarvisPreferences
        private set

    lateinit var repository: JarvisRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = JarvisDatabase.getInstance(this)
        preferences = JarvisPreferences(this)
        repository = JarvisRepository(database.chatDao(), database.customCommandDao(), preferences)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val voiceChannel = NotificationChannel(
                CHANNEL_VOICE_SERVICE,
                "JARVIS Active Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Foreground service notification when JARVIS voice assistant is active"
                setShowBadge(false)
            }

            val alertChannel = NotificationChannel(
                CHANNEL_ALERTS,
                "JARVIS Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important status updates and speech notifications"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(voiceChannel)
            manager?.createNotificationChannel(alertChannel)
        }
    }

    companion object {
        const val CHANNEL_VOICE_SERVICE = "jarvis_voice_service_channel"
        const val CHANNEL_ALERTS = "jarvis_alerts_channel"

        lateinit var instance: JarvisApplication
            private set
    }
}
