package com.revelatestudio.revelate.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



fun dateFormatter(source: String?): String {
    val date: Date?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && source != null) {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        date = dateFormat.parse(source)
    } else return ""
    val formatter: DateFormat =
        SimpleDateFormat("HH-mm-ss")
    return if (date != null) formatter.format(date) else formatter.format(Date())
}

fun getRelativeTimeSpanString(source: String?): CharSequence {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && source != null) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time = sdf.parse(source).time
            val now = System.currentTimeMillis()
            return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return "err"
}

fun Activity.recreateActivity() { // for configuration changes
    val intent = intent;
    finish();
    startActivity(intent);
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.getPhoneConnectionStatus(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (connectivityManager != null) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
    }
    return false
}

fun Context.getPhoneConnectionStatusOldApi(): Boolean {
    val connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo //Deprecated API
    return activeNetworkInfo?.isConnected == true
}

fun Context.getPhoneWifiConnectionStatus(): Boolean? {
    val connectivityManager: ConnectivityManager =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mWifi: NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    return mWifi?.isConnected
}


