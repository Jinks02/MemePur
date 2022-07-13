package com.example.memeshareapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySingletonForVolley constructor(context: Context) {  // only single instance creates in singleton
    companion object {
        @Volatile
        private var INSTANCE: MySingletonForVolley? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingletonForVolley(context).also {
                    INSTANCE = it
                }
            }
    }

   private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}