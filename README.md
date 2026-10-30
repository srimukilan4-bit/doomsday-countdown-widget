# Doomsday Countdown Android Widget

A resizable Android home-screen widget using the artwork supplied by the user. It displays:
- DOOMSDAY IS COMING
- Months, days, hours, minutes, seconds
- December 18, 2026 target date

## Build
1. Open this folder in Android Studio (recent version with Android Gradle Plugin 8.6.1 support).
2. Let Gradle sync.
3. Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Install the generated APK on the Moto Edge 70 Fusion.
5. Long-press the home screen > Widgets > Doomsday Countdown, then drag it onto the screen.
6. Resize it with the launcher handles.

## Countdown refresh limitation
The widget uses an inexact minute refresh. Android may batch background widget updates to protect battery, so the seconds display is not guaranteed to tick every second while the widget is idle. The displayed countdown recalculates on widget refresh and when the widget is updated. The date/time is interpreted in the phone's local time zone.

This is an Android Studio project, not a prebuilt APK. Build it with Android Studio to produce the installable APK.
