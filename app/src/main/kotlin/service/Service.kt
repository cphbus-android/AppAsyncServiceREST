package service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import org.jetbrains.anko.doAsync

class IntentServiceCustom : IntentService("IntentServiceCustom") {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("DebugIntentService","Handle")

        SystemClock.sleep(10000)
        Log.d("DebugIntentService","Timer: 10")
        SystemClock.sleep(10000)
        Log.d("DebugIntentService","Timer: 20")
        SystemClock.sleep(10000)
        Log.d("DebugIntentService","Timer: 30")
    }
}

class ServiceCustom : Service() {
    override fun onCreate() {
        Log.d("DebugService","Create")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DebugService", "Start")

            SystemClock.sleep(10000)
            Log.d("DebugService", "Timer: 10")
            SystemClock.sleep(10000)
            Log.d("DebugService", "Timer: 20")
            SystemClock.sleep(10000)
            Log.d("DebugService", "Timer: 30")


        return 0
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("DebugService","Bind")

        return null!!
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d("DebugService","Unbind")

        return false
    }

    override fun onRebind(intent: Intent) {
        Log.d("DebugService","Rebind")
    }

    override fun onDestroy() {
        Log.d("DebugService","Destroy")
    }
}