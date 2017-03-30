package com.dukelight.boottoshell;

import android.os.Environment;

/**
 * Created by YDP on 16/7/26.
 */

public class AdbCommandUtils {
    /**
     * 切换adb wifi
     */
    public static void runToWifi() {
        String[] wifiStrings = new String[]{
                "sh " + Environment.getExternalStorageDirectory() + "/" + MainActivity.PROJECT_PATH + "/" + MainActivity.ADB_TO_WIFI_FILE
//                "su -c 'setprop service.adb.tcp.port 5555'",
//                "su -c 'stop adbd'",
//                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(wifiStrings, false);
    }

    /**
     * 切换adb usb
     */
    public static void runToUsb() {
        System.out.println("runToUsb");
        String[] adbStrings = new String[]{
                "sh " + Environment.getExternalStorageDirectory() + "/" + MainActivity.PROJECT_PATH + "/" + MainActivity.ADB_TO_USB_FILE
//                "su -c 'setprop service.adb.tcp.port -1'",
//                "su -c 'stop adbd'",
//                "su -c 'start adbd'"
        };
        CommandExecution.execCommand(adbStrings, false);
    }

    /**
     * 获取adb的状态
     * @return
     */
    public static String getAdbStatus() {
        String[] adbStrings = new String[]{
                "getprop | grep service.adb.tcp"
        };
        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(adbStrings, false);
        if (commandResult.result == 0) {
            int inx = commandResult.successMsg.lastIndexOf("[");
            if (inx > 0) {
                return commandResult.successMsg.substring(inx + 1, commandResult.successMsg.length() - 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 关闭虚拟按键
     * @return
     */
    public static String runCloseVitualButton() {
        String[] commands = {
                "su",
                "mount -o remount /dev/block/by-name/system /system",
                "sed -i 's/^qemu.*/qemu.hw.mainkeys=1/' /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.successMsg;
    }

    /**
     * 打开虚拟按钮
     * @return
     */
    public static String runOpenVitualButton() {
        String[] commands = {
                "su",
                "mount -o remount /dev/block/by-name/system /system",
                "sed -i 's/^qemu.*/qemu.hw.mainkeys=0/' /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.successMsg;
    }

    /**
     * 是否build.prop有qemu.hw.mainkeys字段
     * @return
     */
    public static boolean isBuildPropExist() {
        String[] commands = {
                "grep -i 'qemu.hw.mainkeys' /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.result == 0;
    }

    /**
     * 追加qemu.hw.mainkeys字段到build.prop
     * @return
     */
    public static boolean runAppendToBuildProp() {
        String[] commands = {
                "su",
                "mount -o remount /dev/block/by-name/system /system",
                "echo '\nqemu.hw.mainkeys=0' >> /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.result == 0;
    }

    /**
     * 重启
     * @return
     */
    public static boolean runReboot() {
        String[] commands = {
                "su",
                "reboot"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.result == 0;
    }

    /**
     * 判断虚拟按键是否开启
     * @return
     */
    public static boolean isVitualButtonOpen() {
        String[] commands = {
                "grep -i 'qemu.hw.mainkeys=0' /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.result == 0;
    }

    /**
     * 判断虚拟按键是否关闭
     * @return
     */
    public static boolean isVitualButtonClose() {
        String[] commands = {
                "grep -i 'qemu.hw.mainkeys=1' /system/build.prop"
        };

        CommandExecution.CommandResult commandResult = CommandExecution.execCommand(commands, false);
        return commandResult.result == 0;
    }

}
