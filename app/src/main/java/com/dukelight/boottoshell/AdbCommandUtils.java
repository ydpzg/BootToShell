package com.dukelight.boottoshell;

/**
 * Created by YDP on 16/7/26.
 */

public class AdbCommandUtils {
    public static void runToWifi() {
        String[] wifiStrings = new String[]{
                "su -c 'setprop service.adb.tcp.port 5555'",
                "su -c 'stop adbd'",
                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(wifiStrings, false);
    }

    public static void runToUsb() {
        String[] adbStrings = new String[]{
                "su -c 'setprop service.adb.tcp.port -1'",
                "su -c 'stop adbd'",
                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(adbStrings, false);
    }

}
