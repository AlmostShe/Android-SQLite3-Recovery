@echo off
cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\����\adb-1.0.31-windows
adb kill-server
adb start-server
adb devices
adb forward tcp:52336 tcp:52336
start cmd /k "echo �˴���ʹ��dd����Ӱ�׿�豸����������&&cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\����\adb-1.0.31-windows&&adb shell busybox nc -l -p 52336 -e dd if=/dev/block/platform/msm_sdcc.1/by-name/userdata"
start cmd /k "echo �˴���ʹ��nc���ߴӶ˿ڶ�ȡ����&&cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\����\nc&&nc 127.0.0.1 52336 > null\AndroidImage\data.img"
