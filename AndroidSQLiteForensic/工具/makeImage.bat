@echo off
cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\工具\adb-1.0.31-windows
adb kill-server
adb start-server
adb devices
adb forward tcp:52336 tcp:52336
start cmd /k "echo 此窗口使用dd命令从安卓设备端制作镜像&&cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\工具\adb-1.0.31-windows&&adb shell busybox nc -l -p 52336 -e dd if=/dev/block/platform/msm_sdcc.1/by-name/userdata"
start cmd /k "echo 此窗口使用nc工具从端口读取镜像&&cd C:\Workspaces\MyEclipse Professional 2014\AndroidSQLiteForensic\工具\nc&&nc 127.0.0.1 52336 > null\AndroidImage\data.img"
