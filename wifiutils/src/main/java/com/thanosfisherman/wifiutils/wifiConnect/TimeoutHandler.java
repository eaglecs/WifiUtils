package com.thanosfisherman.wifiutils.wifiConnect;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import com.thanosfisherman.wifiutils.WeakHandler;

import static com.thanosfisherman.elvis.Elvis.of;
import static com.thanosfisherman.wifiutils.ConnectorUtils.isAlreadyConnected;
import static com.thanosfisherman.wifiutils.ConnectorUtils.isAlreadyConnectedSSID;
import static com.thanosfisherman.wifiutils.ConnectorUtils.reEnableNetworkIfPossible;
import static com.thanosfisherman.wifiutils.WifiUtils.wifiLog;
import static com.thanosfisherman.wifiutils.utils.VersionUtils.isAndroidQOrLater;

public class TimeoutHandler {
    private WifiManager mWifiManager;
    private WeakHandler mHandler;
    private WifiConnectionCallback mWifiConnectionCallback;
    private ScanResult mScanResult;
    private String mSsid;
    private Runnable timeoutCallback = () -> {
        wifiLog("Connection Timed out...");
        if (mWifiManager == null){
            if (mWifiConnectionCallback == null){
                return;
            } else  {
                mWifiConnectionCallback.errorConnect(ConnectionErrorCode.TIMEOUT_OCCURRED);
            }
        }
        if (mScanResult != null) {
            if (!isAndroidQOrLater()) {
                reEnableNetworkIfPossible(mWifiManager, mScanResult);
            }
            if (isAlreadyConnected(mWifiManager, of(mScanResult).next(scanResult -> scanResult.BSSID).get())) {
                mWifiConnectionCallback.successfulConnect();
            } else {
                mWifiConnectionCallback.errorConnect(ConnectionErrorCode.TIMEOUT_OCCURRED);
            }
        } else {
            if (!isAndroidQOrLater()) {
                reEnableNetworkIfPossible(mWifiManager, mSsid);
            }
            if (isAlreadyConnectedSSID(mWifiManager, mSsid)) {
                mWifiConnectionCallback.successfulConnect();
            } else {
                mWifiConnectionCallback.errorConnect(ConnectionErrorCode.TIMEOUT_OCCURRED);
            }
        }
//            mHandler.removeCallbacks(this);
    };

//    private Runnable timeoutCallback = new Runnable() {
//        @Override
//        public void run() {
//            wifiLog("Connection Timed out...");
//
//            if (mScanResult != null){
//                if (!isAndroidQOrLater()) {
//                    reEnableNetworkIfPossible(mWifiManager, mScanResult);
//                }
//                if (isAlreadyConnected(mWifiManager, of(mScanResult).next(scanResult -> scanResult.BSSID).get())) {
//                    mWifiConnectionCallback.successfulConnect();
//                } else {
//                    mWifiConnectionCallback.errorConnect(ConnectionErrorCode.TIMEOUT_OCCURRED);
//                }
//            } else  {
//                if (!isAndroidQOrLater()) {
//                    reEnableNetworkIfPossible(mWifiManager, mSsid);
//                }
//                if (isAlreadyConnectedSSID(mWifiManager, mSsid)) {
//                    mWifiConnectionCallback.successfulConnect();
//                } else {
//                    mWifiConnectionCallback.errorConnect(ConnectionErrorCode.TIMEOUT_OCCURRED);
//                }
//            }
//
//
//
//            mHandler.removeCallbacks(this);
//        }
//    };

    public TimeoutHandler(@NonNull WifiManager wifiManager, @NonNull WeakHandler handler, @NonNull final WifiConnectionCallback wifiConnectionCallback) {
        this.mWifiManager = wifiManager;
        this.mHandler = handler;
        this.mWifiConnectionCallback = wifiConnectionCallback;
    }

    public void startTimeout(final ScanResult scanResult, final long timeout) {
        // cleanup previous connection timeout handler
        mHandler.removeCallbacks(timeoutCallback);

        mScanResult = scanResult;
        mHandler.postDelayed(timeoutCallback, timeout);
    }

    public void startTimeout(final String ssid, final long timeout) {
        // cleanup previous connection timeout handler
        mHandler.removeCallbacks(timeoutCallback);
        mSsid = ssid;
        mHandler.postDelayed(timeoutCallback, timeout);
    }

    public void stopTimeout() {
        mHandler.removeCallbacks(timeoutCallback);
    }
}
