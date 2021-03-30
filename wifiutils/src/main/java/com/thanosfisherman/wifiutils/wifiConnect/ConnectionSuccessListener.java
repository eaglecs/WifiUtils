package com.thanosfisherman.wifiutils.wifiConnect;

import android.net.wifi.ScanResult;

import androidx.annotation.NonNull;

public interface ConnectionSuccessListener {
    void success(@NonNull ScanResult mSingleScanResult);

    void failed(@NonNull ConnectionErrorCode errorCode);
}
