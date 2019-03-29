@echo off
cd  D:\Workplace\MyEclipse\AndroidSQLiteForensic\¹¤¾ß\adb-1.0.31-windows
adb kill-server
adb start-server
adb shell dumpsys iphonesubinfo 
adb shell busybox df -h /dev/block/platform/msm_sdcc.1/by-name/userdata