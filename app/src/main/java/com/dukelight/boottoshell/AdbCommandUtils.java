package com.dukelight.boottoshell;

import android.os.Environment;

/**
 * Created by YDP on 16/7/26.
 */

public class AdbCommandUtils {
    public static void runToWifi() {
        String[] wifiStrings = new String[]{
                "sh " + Environment.getExternalStorageDirectory() + "/" + MainActivity.PROJECT_PATH + "/" + MainActivity.ADB_TO_WIFI_FILE
//                "su -c 'setprop service.adb.tcp.port 5555'",
//                "su -c 'stop adbd'",
//                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(wifiStrings, false);
    }

    public static void runToUsb() {
        String[] adbStrings = new String[]{
                "sh " + Environment.getExternalStorageDirectory() + "/" + MainActivity.PROJECT_PATH + "/" + MainActivity.ADB_TO_USB_FILE
//                "su -c 'setprop service.adb.tcp.port -1'",
//                "su -c 'stop adbd'",
//                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(adbStrings, false);
    }

    public static String getAdbStatus() {
        String[] adbStrings = new String[]{
                "getprop | grep service.adb.tcp"
        };
        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(adbStrings, false);
        int inx = commandResult.successMsg.lastIndexOf("[");
        if (inx > 0) {
            return commandResult.successMsg.substring(inx + 1, commandResult.successMsg.length() - 1);
        } else {
            return null;
        }
    }

}
