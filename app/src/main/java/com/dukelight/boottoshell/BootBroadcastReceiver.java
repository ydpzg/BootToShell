package com.dukelight.boottoshell;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YDP on 16/7/26.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    // 开机启动为adb wifi模式
    @Override
    public void onReceive(Context context, Intent intent) {
        AdbCommandUtils.runToWifi();
    }

}