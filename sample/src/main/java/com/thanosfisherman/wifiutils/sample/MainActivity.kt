package com.thanosfisherman.wifiutils.sample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.sample.databinding.ActivityMainBinding
import com.thanosfisherman.wifiutils.utils.VersionUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private val SSID = "OLLI-Public"
//    private val PASSWORD = "olli-ai2020"

    private val SSID = "MAIKA-9683"
    private val PASSWORD = ""

//    private val SSID = "HCM"
//    private val PASSWORD = "123trang"

//    private val SSID = "MAIKA-ADF7"
//    private val PASSWORD = ""

//    private val SSID = "smarthome"
//    private val PASSWORD = "a1234567890"

    private var isConnectedToInternet = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE), 555)
        WifiUtils.forwardLog { _, tag, message ->
            Log.d("ducanh_$tag", message)
        }
        WifiUtils.enableLog(true)
        binding.textviewSsid.text = SSID
        binding.textviewPassword.text = PASSWORD
        binding.buttonConnect.setOnClickListener { connectWithWpa(applicationContext) }
        binding.buttonConnectWithoutScan.setOnClickListener { connectWithWpaWithoutScan(applicationContext) }
        binding.buttonDisconnect.setOnClickListener { disconnect(applicationContext) }
        binding.buttonRemove.setOnClickListener { remove(applicationContext) }
        binding.buttonCheck.setOnClickListener { check(applicationContext) }
        binding.buttonCheckInternet.setOnClickListener { checkInternet(applicationContext) }

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
                .setTimeout(50000)
                .setNumberRetry(5)
                .onConnectionResult(object : ConnectionSuccessListener {
                    override fun failed(errorCode: ConnectionErrorCode) {
                        Toast.makeText(context, "EPIC FAIL!$errorCode", Toast.LENGTH_SHORT).show()
                    }

                    override fun success(mScanResult: ScanResult?, gateway: String) {
                        scanResult = mScanResult
                        Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
                    }
                })
                .start()
    }

    private var scanResult: ScanResult? = null
    private var isReconnect = false

    private fun connectWithWpaWithoutScan(context: Context) {
        val numRetry = if (VersionUtils.isAndroidQOrLater()){
            0
        } else {
            1
        }
        WifiUtils.withContext(context)
                .connectWith(SSID, PASSWORD)
                .setNumberRetry(numRetry)
                .setTimeout(10000)
                .onConnectionResult(object : ConnectionSuccessListener {
                    override fun success(mScanResult: ScanResult?, gateway: String) {
                        Toast.makeText(context, "SUCCESS!", Toast.LENGTH_SHORT).show()
//                        if (isReconnect){
//                            isReconnect = false
//                        } else {
//                            isReconnect = true
//                            connectWithWpaWithoutScan(context)
//                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("duc_anh","onActivityResult")
    }
}