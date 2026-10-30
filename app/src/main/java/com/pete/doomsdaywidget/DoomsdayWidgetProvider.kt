package com.pete.doomsdaywidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class DoomsdayWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { updateOne(context, manager, it) }
        scheduleNextRefresh(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, DoomsdayWidgetProvider::class.java))
            ids.forEach { updateOne(context, manager, it) }
            scheduleNextRefresh(context)
        }
    }

    companion object {
        private const val ACTION_UPDATE = "com.pete.doomsdaywidget.UPDATE"
        // The announced theatrical release date. Countdown uses the phone's local time zone.
        private val target = LocalDateTime.of(2026, Month.DECEMBER, 17, 0, 0, 0)

        private fun updateOne(context: Context, manager: AppWidgetManager, id: Int) {
            val now = LocalDateTime.now()
            val views = RemoteViews(context.packageName, R.layout.doomsday_widget)

            if (!now.isBefore(target)) {
                views.setTextViewText(R.id.months, "00")
                views.setTextViewText(R.id.days, "00")
                views.setTextViewText(R.id.hours, "00")
                views.setTextViewText(R.id.minutes, "00")
                views.setTextViewText(R.id.seconds, "00")
                views.setTextViewText(R.id.headline, "DOOMSDAY IS HERE")
            } else {
                var cursor = now
                var months = 0
                while (!cursor.plusMonths(1).isAfter(target)) {
                    cursor = cursor.plusMonths(1)
                    months++
                }
                val remaining = Duration.between(cursor, target)
                val days = remaining.toDays()
                val hours = remaining.minusDays(days).toHours()
                val minutes = remaining.minusDays(days).minusHours(hours).toMinutes()
                val seconds = remaining.minusDays(days).minusHours(hours).minusMinutes(minutes).seconds

                views.setTextViewText(R.id.months, "%02d".format(months))
                views.setTextViewText(R.id.days, "%02d".format(days))
                views.setTextViewText(R.id.hours, "%02d".format(hours))
                views.setTextViewText(R.id.minutes, "%02d".format(minutes))
                views.setTextViewText(R.id.seconds, "%02d".format(seconds))
            }
            manager.updateAppWidget(id, views)
        }

        private fun scheduleNextRefresh(context: Context) {
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DoomsdayWidgetProvider::class.java).setAction(ACTION_UPDATE)
            val pending = PendingIntent.getBroadcast(
                context, 4102, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            // Inexact minute refresh: Android may batch widget updates to protect battery.
            alarm.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 60_000L,
                60_000L,
                pending
            )
        }
    }
}
