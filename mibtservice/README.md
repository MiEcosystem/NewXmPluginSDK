# 蓝牙通信Demo

## 工程的用途
该DEMO运行在华米手表上，与米家APP通过蓝牙进行通信

## Demo的使用
1、点击START SERVICE启动蓝牙服务
2、点击START ADVERTISE发送BLE广播，使得设备能够被发现

## SDK提供的API
手表与APP连接上后，会返回一个IMiServiceHandler对象，通过这个IMiServiceHandler对象来进行进一步的操作：
1、doRpc(String did, String method, String params, IMiBTCallback callback);
发送RPC命令，返回结果从callback获取

2、void getDeviceProp(IMiBTCallback callback);
获取设备列表，返回结果从callback获取

3、writeBlock(String msg, IMiBTCallback callback);
往米家插件发送通用数据，callback只返回当前是否发送成功，不返回数据

4、void registerBlockListener(IMiBTCallback callback);
所有从米家插件返回的通用数据，都通过这个callback返回给上层

5、void unregisterBlockListener();
注销registerBlockListener中注册的监听器