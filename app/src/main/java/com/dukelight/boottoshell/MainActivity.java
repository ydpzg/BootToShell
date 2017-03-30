package com.dukelight.boottoshell;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.dukelight.boottoshell.AdbCommandUtils.getAdbStatus;

public class MainActivity extends AppCompatActivity {

    Button button; // 切换adb wifi
    Button button2; // 切换adb usb
    Button button3; // 关闭虚拟按键
    Button button4; // 打开虚拟按键
    Button button5; // 重启
    TextView tvAdbStatus;

    public final static String PROJECT_PATH = "BootToShell/config";
    public final static String ADB_TO_WIFI_FILE = "adbToWifi.sh";
    public final static String ADB_TO_USB_FILE = "adbToUsb.sh";


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        tvAdbStatus = (TextView) findViewById(R.id.tv_adb_status);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换为adb wifi模式
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AdbCommandUtils.runToWifi();
                    }
                }).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = getAdbStatus();

                                if (result != null && result.equals("5555")) {
                                    toast("adb切换Wifi成功");
                                    updateWifiView();
                                } else {
                                    toast("adb切换Wifi失败");
                                }
                            }
                        }).start();
                    }
                }, 500);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换为adb usb模式
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AdbCommandUtils.runToUsb();
                    }
                }).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = getAdbStatus();
                                if (result != null && result.equals("-1")) {
                                    toast("adb切换usb成功");
                                    updateUsbView();
                                } else {
                                    toast("adb切换usb失败");
                                }
                            }
                        }).start();
                    }
                }, 500);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!AdbCommandUtils.isBuildPropExist()) {
                            AdbCommandUtils.runAppendToBuildProp();
                        }
                    }
                }).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AdbCommandUtils.runCloseVitualButton();
                            }
                        }).start();
                    }
                }, 500);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean result = AdbCommandUtils.isVitualButtonClose();
                                if (!result) {
                                    toast("关闭虚拟按钮失败，请检查原因");
                                } else {
                                    toast("关闭成功！请重新启动以生效应用配置");
                                }
                            }
                        }).start();
                    }
                }, 1000);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!AdbCommandUtils.isBuildPropExist()) {
                            AdbCommandUtils.runAppendToBuildProp();
                        }
                    }
                }).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AdbCommandUtils.runOpenVitualButton();
                            }
                        }).start();
                    }
                }, 500);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean result = AdbCommandUtils.isVitualButtonOpen();
                                if (!result) {
                                    toast("打开虚拟按钮失败，请检查原因");
                                } else {
                                    toast("打开成功！请重新启动以生效应用配置");
                                }
                            }
                        }).start();
                    }
                }, 1000);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AdbCommandUtils.runReboot();
                    }
                }).start();
            }
        });
        initFolder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdbStatus();
    }

    private void initAdbStatus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = AdbCommandUtils.getAdbStatus();
                        if (result != null) {
                            if (result.equals("5555")) {
                                updateWifiView();
                            } else {
                                updateUsbView();
                            }
                        } else {
                            updateUsbView();
                        }
                    }
                }).start();
            }
        }, 500);

    }

    private void initFolder() {
        File adbToWifi = new File(Environment.getExternalStorageDirectory() + "/" + PROJECT_PATH + "/" + ADB_TO_WIFI_FILE);
        File adbToUsb = new File(Environment.getExternalStorageDirectory() + "/" + PROJECT_PATH + "/" + ADB_TO_USB_FILE);
        if (!adbToWifi.getParentFile().exists()) {
            adbToWifi.mkdirs();
        }
        if (!adbToUsb.getParentFile().exists()) {
            adbToUsb.mkdirs();
        }

        if (!adbToWifi.exists()) {
            createAdbToWifiFile(adbToWifi);
        } else {
            if (adbToWifi.isDirectory()) {
                adbToWifi.delete();
                createAdbToWifiFile(adbToWifi);
            }
        }
        if (!adbToUsb.exists()) {
            createAdbToUsbFile(adbToUsb);
        } else {
            if (adbToUsb.isDirectory()) {
                adbToUsb.delete();
                createAdbToUsbFile(adbToUsb);
            }
        }

    }

    private void createAdbToWifiFile(File adbToWifi) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("su -c 'setprop service.adb.tcp.port 5555'");
            sb.append("\n");
            sb.append("su -c 'stop adbd'");
            sb.append("\n");
            sb.append("su -c 'start adbd'");
            sb.append("\n");
//            sb.append("su");
//            sb.append("\n");
//            sb.append("setprop service.adb.tcp.port 5555");
//            sb.append("\n");
//            sb.append("stop adbd");
//            sb.append("\n");
//            sb.append("start adbd");
//            sb.append("\n");
            FileOutputStream fileOutputStream = new FileOutputStream(adbToWifi);
            fileOutputStream.write(sb.toString().getBytes());
            toast("创建adbToWifi成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAdbToUsbFile(File adbToUsb) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("su -c 'setprop service.adb.tcp.port -1'");
            sb.append("\n");
            sb.append("su -c 'stop adbd'");
            sb.append("\n");
            sb.append("su -c 'start adbd'");
            sb.append("\n");
//            sb.append("su");
//            sb.append("\n");
//            sb.append("setprop service.adb.tcp.port -1");
//            sb.append("\n");
//            sb.append("stop adbd");
//            sb.append("\n");
//            sb.append("start adbd");
//            sb.append("\n");
            FileOutputStream fileOutputStream = new FileOutputStream(adbToUsb);
            fileOutputStream.write(sb.toString().getBytes());
            toast("创建adbToUsb成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWifiView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvAdbStatus.setText("adb状态：wifi模式");
            }
        });
    }

    private void updateUsbView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvAdbStatus.setText("adb状态：usb模式");
            }
        });
    }

}
