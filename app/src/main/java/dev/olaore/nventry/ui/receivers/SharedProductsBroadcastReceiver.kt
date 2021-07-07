package dev.olaore.nventry.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SharedProductsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("Broadcast", "Gotten!")
        p0?.sendBroadcast(Intent("SHARE_COMPLETE"))
    }

}