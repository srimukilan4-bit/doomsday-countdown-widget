package com.pete.doomsdaywidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TextView(this).apply {
            text = "DOOMSDAY COUNTDOWN\n\nAdd the widget to your home screen:\n1. Long-press an empty area on your home screen.\n2. Tap Widgets.\n3. Find Doomsday Countdown.\n4. Drag it onto your home screen.\n5. Resize it using the handles.\n\nThe countdown targets December 18, 2026. Android may batch background widget refreshes, so seconds are approximate between refreshes."
            textSize = 18f
            setPadding(32, 48, 32, 32)
        }
        setContentView(text)
        val manager = AppWidgetManager.getInstance(this)
        val ids = manager.getAppWidgetIds(ComponentName(this, DoomsdayWidgetProvider::class.java))
        if (ids.isNotEmpty()) DoomsdayWidgetProvider().onUpdate(this, manager, ids)
    }
}
