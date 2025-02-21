package com.thanosfisherman.wifiutils.wifiConnect;

import android.net.wifi.ScanResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ConnectionSuccessListener {
    void success(@Nullable ScanResult mScanResult, @NonNull String gateway);

    void failed(@NonNull ConnectionErrorCode errorCode);
}
