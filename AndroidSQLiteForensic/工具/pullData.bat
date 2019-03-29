@echo off
cd D:\Workplace\MyEclipse\AndroidSQLiteForensic\工具\adb-1.0.31-windows
adb kill-server
adb start-server
adb devices
adb pull /data/data/com.android.providers.telephony/databases/mmssms.db D:\用户目录\我的文档\数据库文件
echo 短信数据库提取完成
adb pull /data/data/com.android.providers.contacts/databases/contacts2.db D:\用户目录\我的文档\数据库文件
echo 联系人和通话记录数据库提取完成
adb pull /data/data/com.UCMobile/databases/bookmark.db D:\用户目录\我的文档\数据库文件
echo 浏览器书签数据库提取完成
adb pull /data/data/com.android.providers.media/databases/external.db D:\用户目录\我的文档\数据库文件
echo 多媒体数据库提取完成
