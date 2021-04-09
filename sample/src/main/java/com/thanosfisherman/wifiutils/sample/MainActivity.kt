package com.thanosfisherman.wifiutils.sample

import android.Manifest
import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

//    private val SSID = "OLLI-Public"
//    private val PASSWORD = "olli-ai2020"

    private val SSID = "MAIKA-9683"
    private val PASSWORD = ""

//    private val SSID = "smarthome"
//    private val PASSWORD = "a1234567890"

    private var isConnectedToInternet = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE), 555)
        WifiUtils.forwardLog { _, tag, message ->
            val customTag = "${tag}.${this::class.simpleName}"
            Log.i(customTag, message)
        }
        WifiUtils.enableLog(true)
        textview_ssid.text = SSID
        textview_password.text = PASSWORD
        button_connect.setOnClickListener { connectWithWpa(applicationContext) }
        button_connect_without_scan.setOnClickListener { connectWithWpaWithoutScan(applicationContext) }
        button_disconnect.setOnClickListener { disconnect(applicationContext) }
        button_remove.setOnClickListener { remove(applicationContext) }
        button_check.setOnClickListener { check(applicationContext) }
        button_check_internet.setOnClickListener { checkInternet(applicationContext) }

    }


    private fun checkInternet(context: Context) {
        if (isConnectedToInternet) {
            Toast.makeText(context, "has internet!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "no internet!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectWithWpa(context: Context) {
        WifiUtils.withContext(context)
                .connectWith(SSID, PASSWORD)
                .setTimeout(10000)
                .setNumberRetry(5)
                .onConnectionResult(object : ConnectionSuccessListener {
                    override fun failed(errorCode: ConnectionErrorCode) {
                        Toast.makeText(context, "EPIC FAIL!$errorCode", Toast.LENGTH_SHORT).show()
                    }

                    override fun success(mScanResult: ScanResult?) {
                        scanResult = mScanResult
                        Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
                    }
                })
                .start()
    }

    private var scanResult: ScanResult? = null

    private fun connectWithWpaWithoutScan(context: Context) {
        WifiUtils.withContext(context)
                .connectWith(SSID, PASSWORD)
                .setNumberRetry(5)
                .setTimeout(10000)
                .onConnectionResult(object : ConnectionSuccessListener {
                    //                    override fun success() {
//                        Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
//                    }
                    override fun success(mScanResult: ScanResult?) {
                        Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
                    }

                    override fun failed(errorCode: ConnectionErrorCode) {
                        Toast.makeText(context, "EPIC FAIL!$errorCode", Toast.LENGTH_SHORT).show()
                    }
                })
                .startWithoutScan()
    }

    private fun disconnect(context: Context) {
        WifiUtils.withContext(context)
                .disconnect(object : DisconnectionSuccessListener {
                    override fun success() {
                        Toast.makeText(context, "Disconnect success!", Toast.LENGTH_SHORT).show()
                    }

                    override fun failed(errorCode: DisconnectionErrorCode) {
                        Toast.makeText(context, "Failed to disconnect: $errorCode", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun remove(context: Context) {
        WifiUtils.withContext(context)
                .remove(SSID, object : RemoveSuccessListener {
                    override fun success() {
                        Toast.makeText(context, "Remove success!", Toast.LENGTH_SHORT).show()
                    }

                    override fun failed(errorCode: RemoveErrorCode) {
                        Toast.makeText(context, "Failed to disconnect and remove: $errorCode", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun check(context: Context) {
        val result = WifiUtils.withContext(context).isWifiConnected(SSID)
        Toast.makeText(context, "Wifi Connect State: $result", Toast.LENGTH_SHORT).show()
    }
}