@echo off
cd D:\Workplace\MyEclipse\AndroidSQLiteForensic\����\adb-1.0.31-windows
adb kill-server
adb start-server
adb devices
adb pull /data/data/com.android.providers.telephony/databases/mmssms.db D:\�û�Ŀ¼\�ҵ��ĵ�\���ݿ��ļ�
echo �������ݿ���ȡ���
adb pull /data/data/com.android.providers.contacts/databases/contacts2.db D:\�û�Ŀ¼\�ҵ��ĵ�\���ݿ��ļ�
echo ��ϵ�˺�ͨ����¼���ݿ���ȡ���
adb pull /data/data/com.UCMobile/databases/bookmark.db D:\�û�Ŀ¼\�ҵ��ĵ�\���ݿ��ļ�
echo �������ǩ���ݿ���ȡ���
adb pull /data/data/com.android.providers.media/databases/external.db D:\�û�Ŀ¼\�ҵ��ĵ�\���ݿ��ļ�
echo ��ý�����ݿ���ȡ���
