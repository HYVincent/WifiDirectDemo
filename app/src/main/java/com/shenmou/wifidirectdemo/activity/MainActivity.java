package com.shenmou.wifidirectdemo.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.util.file.FileSizeUtil;
import com.common.util.util.ThreadPoolManager;
import com.shenmou.wifidirectdemo.R;
import com.shenmou.wifidirectdemo.adapter.MsgAdapter;
import com.shenmou.wifidirectdemo.adapter.WifiDrectAdapter;
import com.shenmou.wifidirectdemo.base.BaseActivity;
import com.shenmou.wifidirectdemo.bean.DataBean;
import com.shenmou.wifidirectdemo.bean.FileBean;
import com.shenmou.wifidirectdemo.bean.MsgBean;
import com.shenmou.wifidirectdemo.utils.FileUtils;
import com.shenmou.wifidirectdemo.utils.GifSizeFilter;
import com.shenmou.wifidirectdemo.utils.Glide4Engine;
import com.shenmou.wifidirectdemo.utils.ImageUtil;
import com.shenmou.wifidirectdemo.utils.Md5Util;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity {
    //服务器端口号
    private static final int SERVICE_PORT = 8888;
    private static final String TAG = "首页";
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    //当前服务器设备
    private WifiP2pDevice groupDevice = null;
    //当前已连接设备列表
    private Collection<WifiP2pDevice> connectList = null;

    private Context mContext;

//    private RecyclerView recyclerView = null;
    private WifiDrectAdapter adapter;

    private RecyclerView recyclerViewMsg;
    private MsgAdapter msgAdapter;
    private List<MsgBean> msgBeans = new ArrayList<>();
    //false clent    true service
//    private boolean isGroup = false;
    //正在扫描 true 未扫描 false
    private boolean isScan = false;
    private EditText etMsg;
    private String imgPath;
    //服务器的ip地址，作为客户端时使用
    private String ipString;
    private Button btnService;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;
        initView();
        initMsgRecycleView();
        initRecycleView();
        initWifiP2p(mContext);
        registerBroadrost();
    }

    private void initView() {
        imageView = findViewById(R.id.imageview);
        Button btnScan = findViewById(R.id.btn_start_scan_device);
        btnScan.setOnClickListener(view -> {
            if (isScan) {
                showMsg("正在扫描设备..");
                return;
            }
            startSearchDevice();
        });
        btnService = findViewById(R.id.btn_start_service);
        btnService.setOnClickListener(view -> createGroup());
       Button btnCloseService = findViewById(R.id.btn_close_service);
        btnCloseService.setOnClickListener(view -> closeGroup());
       Button btnSelectImg = findViewById(R.id.btn_select_img);
        btnSelectImg.setOnClickListener(view -> MainActivityPermissionsDispatcher.getImgWithCheck(MainActivity.this));
        etMsg = findViewById(R.id.et_msg);
        findViewById(R.id.to_send_msg).setOnClickListener(view -> {
            String msg = etMsg.getText().toString();
            DataBean dataBean = new DataBean();
            dataBean.setDataType(1);
            dataBean.setData(msg);
            ThreadPoolManager.getInstance(true).execute(() -> connectServiceSocket(dataBean, ipString, SERVICE_PORT));
        });
    }


    private void initMsgRecycleView() {
        recyclerViewMsg = findViewById(R.id.recycleView_msg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMsg.setLayoutManager(linearLayoutManager);
        msgAdapter = new MsgAdapter(MainActivity.this);
        msgAdapter.setData(msgBeans);
        recyclerViewMsg.setAdapter(msgAdapter);
    }

    /**
     * 刷新消息列表
     * @param msgLevel 消息等级 //0 一般消息  1 错误消息  2 警告消息
     * @param msg 消息
     */
    private void refreshMsgList(int msgLevel,String msg) {
        MsgBean msgBean = new MsgBean();
        msgBean.setTime(System.currentTimeMillis());
        msgBean.setMsg(msg);
        msgBean.setMsgLevel(msgLevel);
        msgBeans.add(msgBean);
        runOnUiThread(() -> msgAdapter.setData(msgBeans));
        if(recyclerViewMsg != null && msgAdapter != null) {
            recyclerViewMsg.smoothScrollToPosition(msgAdapter.getItemCount() - 1);
        }
    }

    /**
     * 刷新消息列表
     * @param msg 消息
     */
    private void refreshMsgList(String msg) {
        MsgBean msgBean = new MsgBean();
        msgBean.setTime(System.currentTimeMillis());
        msgBean.setMsg(msg);
        msgBean.setMsgLevel(0);
        if(msgBeans.size()>100){
            msgBeans.remove(0);
        }
        msgBeans.add(msgBean);
        runOnUiThread(() -> msgAdapter.setData(msgBeans));
        try {
            if(recyclerViewMsg != null && msgAdapter != null) {
                recyclerViewMsg.smoothScrollToPosition(msgAdapter.getItemCount() - 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WifiDrectAdapter(MainActivity.this);
        refreshDevice();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            WifiP2pDevice wifiP2pDevice = list.get(position);
            if (groupDevice != null) {
                refreshMsgList(1,"当前设备为service，不可主动连接client..");
            } else {
                if (wifiP2pDevice.status == WifiP2pDevice.CONNECTED) {
                    disconnect(wifiP2pDevice);
                } else {
                    connectWifiP2pDevice(wifiP2pDevice);
                }
            }
        });
    }

    private void initWifiP2p(Context mContext) {
        wifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(mContext, getMainLooper(), channelListener);
    }

    /**
     * 注册广播
     */
    private void registerBroadrost() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);  // WiFi P2P是否可用
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION); // peers列表发生变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);// WiFi P2P连接发生变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);// WiFi P2P设备信息发生变化(比方更改了设备名)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mContext.registerReceiver(receiver, intentFilter);
        refreshMsgList("注册广播接收器");
    }


    /**
     * 开始搜索设备
     */
    private void startSearchDevice() {
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {
                showMsg("搜索设备失败!");
                refreshMsgList(1,"搜索设备失败!");
            }
        });
        wifiP2pManager.requestPeers(channel, wifiP2pDeviceList -> Log.d(TAG, "onPeersAvailable: ........"));
    }


    /**
     * 停止搜索设备
     */
    private void stopSearchDevice() {
        refreshMsgList(1,"停止搜索设备!");
        wifiP2pManager.stopPeerDiscovery(channel, null);
    }

    /**
     * 连接设备
     *
     * @param wifiP2pDevice 基本就是选择一个要连接的设备，配置一个WifiP2pConfig对象，调用connect方法进行连接，由于p2p相关API大多都是异步的，需要一个监听成功与否的操作。需要注意的是，设备有很多状态，只有处于
     *                      Available状态时才可以尝试连接，另外还有Invited和Connected状态等。Connected就是已经连接，Invited是指请求过连接，对方可能处于另外一个组中并且是GC身份，不能与其他设备连接，请求被搁置。
     */
    private void connectWifiP2pDevice(final WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice.status == WifiP2pDevice.AVAILABLE) {
            showLoadingDialog("正在连接" + wifiP2pDevice.deviceName + "..");
            refreshMsgList("正在连接" + wifiP2pDevice.deviceName + "..");
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    refreshMsgList("设备" + wifiP2pDevice.deviceName + "连接成功!");
                    showMsg("设备" + wifiP2pDevice.deviceName + "连接成功!");
                    dismissionDialog();
                }

                @Override
                public void onFailure(int reason) {
                    dismissionDialog();
                    refreshMsgList(1,"设备" + wifiP2pDevice.deviceName + "连接失败!");
                    showMsg("设备" + wifiP2pDevice.deviceName + "连接失败!");
                }
            });
        } else if (wifiP2pDevice.status == WifiP2pDevice.UNAVAILABLE) {
            dismissionDialog();
            refreshMsgList(1,"设备" + wifiP2pDevice.deviceName + "无效!");
            showMsg("connectWifiP2pDevice: 当前设备无效..");
        } else {
            dismissionDialog();
            refreshMsgList(1,"设备" + wifiP2pDevice.deviceName + "不可连接,状态-->" + wifiP2pDevice.status);
            showMsg("connectWifiP2pDevice: 当前设备不可连接,状态-->" + wifiP2pDevice.status);
        }
        refreshDevice();
        refreshMsgList("status : 连接设备之后刷新列表状态..");
    }

    /**
     * 获取当前已连接设备列表
     */
    private void getCurrentConnectDeviceList() {
        if (wifiP2pManager != null && channel != null) {
            wifiP2pManager.requestGroupInfo(channel, wifiP2pGroup -> {
                //当前服务器设备
                groupDevice = wifiP2pGroup.getOwner();
                connectList = wifiP2pGroup.getClientList();
            });
        }
    }

    /**
     * 创建一个以当前设备为service的group
     */
    private void createGroup() {
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "create group successfuly !");
                refreshMsgList("create group successfuly,will start socket server ..");
                btnService.setText("服务已创建");
                ThreadPoolManager.getInstance(true).execute(() -> createSocketService(SERVICE_PORT));
            }

            @Override
            public void onFailure(int reason) {
                refreshMsgList(1,"server status : create group fail !!!");
                btnService.setText("创建群组失败");
            }
        });
    }




    private void refreshDevice() {
        runOnUiThread(() -> adapter.setDatas(list));
    }


    /**
     * 关闭服务器
     */
    private void closeGroup() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                btnService.setText("创建服务");
                try {
                    if(mServerSocket != null)mServerSocket.close();
                    refreshMsgList(" status : service close..");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i) {
                refreshMsgList(1,"remove service fail!");
            }
        });

    }

    /**
     * 断开连接设备
     *
     * @param wifiP2pDevice 设备
     */
    private void disconnect(final WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice.status == WifiP2pDevice.CONNECTED) {
            //这个方法没有用
           /* wifiP2pManager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    refreshMsgList("device "+wifiP2pDevice.deviceName+" disconnect successfuly!");
                }

                @Override
                public void onFailure(int i) {
                    refreshMsgList("device "+wifiP2pDevice.deviceName+" disconnect fail!");
                }
            });*/
            //这个方法移除当前组的所有连接
            wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    refreshMsgList("device " + wifiP2pDevice.deviceName + " disconnect successfuly!");
                }

                @Override
                public void onFailure(int i) {
                    refreshMsgList(1,"device " + wifiP2pDevice.deviceName + " disconnect fail!");
                }
            });
            startSearchDevice();
        } else {
            refreshMsgList(2,"please connect " + wifiP2pDevice.deviceName + "..");
            showMsg("please connect device!");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(receiver);
        stopSearchDevice();
        if(mServerSocket != null){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //注意，如果采用直接杀死进程的方式，本方法不会被调用
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 移除成功");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: 移除失败..");
            }
        });
    }

    /**
     * 其中channel是许多操作的真正执行者，而且许多操作都是异步的，需要借助很多接口实现。
     * 关于WiFi P2P的开发，大部分都借助于WifiP2pManager实现，而且一些状态的获取都是基于广播的，
     * 所以我们需要建立一个广播接受者，来接受各种相关的广播，首先需要注册下面几个广播：
     */
    private WifiP2pManager.ChannelListener channelListener = () -> Log.d(TAG, "onChannelDisconnected: 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");

    private LinkedList<WifiP2pDevice> list = new LinkedList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                switch (intent.getAction()) {
                    case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                        boolean p2pIsEnable = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, WifiP2pManager.WIFI_P2P_STATE_DISABLED) == WifiP2pManager.WIFI_P2P_STATE_ENABLED;
                        if (p2pIsEnable) {
                            refreshMsgList("Wifi p2p 可用...");
                        } else {
                            refreshMsgList(1,"Wifi p2p 不可用...");
                        }
                        break;
                    case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                        //获取到设备列表信息
                        WifiP2pDeviceList mPeers = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                        list.clear(); //清除旧的信息
                        list.addAll(mPeers.getDeviceList()); //更新信息
                        refreshMsgList("扫描到设备列表，找到" + list.size() + "个设备,刷新设备列表状态..");
                        refreshDevice();
                        break;
                    case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        //这是连接状态发送变化时的广播，如连接了一个设备，断开了一个设备都会接收到广播。着这个广播到来时，
                        //NetworkInfo 的isConnected()可以判断时连接还是断开时接收到的广播。
                        NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                        //WifiP2pInfo保存着一些连接的信息，如groupFormed字段保存是否有组建立，
                        // groupOwnerAddress字段保存GO设备的地址信息，isGroupOwner字段判断自己是否是GO设备。
                        // WifiP2pInfo也可以随时用过wifiP2pManager.requestConnectionInfo来获取。
                        WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                        //WifiP2pGroup存放着当前组成员的信息，这个信息只有GO设备可以获取。同样这个信息也可以通过wifiP2pManager.requestGroupInfo获取
                        WifiP2pGroup wifiP2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
                        if (wifiP2pInfo.isGroupOwner) {
                            //作为service
                            connectList = wifiP2pGroup.getClientList();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("已连接设备-->");
                            Iterator iterable = connectList.iterator();
                            while (iterable.hasNext()) {
                                WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) iterable.next();
                                stringBuilder.append(wifiP2pDevice.deviceName);
                                stringBuilder.append(" ");
                            }
                            refreshMsgList(stringBuilder.toString());
                        } else {
                            //client
                            if (networkInfo.isConnected()) {
                                //表示这是设备连接成功时收到的广播
                                if (wifiP2pInfo.groupFormed) {
                                    InetAddress serviceIp = wifiP2pInfo.groupOwnerAddress;
                                    ipString = serviceIp.getHostAddress();
                                    refreshMsgList("server status : ip -->"+ipString);
                                }
                                //连接服务器ip地址
                                ThreadPoolManager.getInstance(true).execute(() -> {
                                    refreshMsgList("正在连接服务器(" + ipString + ")..");
                                    DataBean dataBean = new DataBean();
                                    dataBean.setDataType(1);
                                    dataBean.setData("测试消息，可以让服务器把在线的客户端都保存起来..");
                                    connectServiceSocket(dataBean, ipString, SERVICE_PORT);
                                });
                            }
                        }
                        refreshMsgList("status : 设备状态发生变化，刷新列表..");
                        refreshDevice();
                        break;
                    case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                        refreshMsgList("Wifi p2p 设备信息状态改变..3");
                        //WIFI_P2P_THIS_DEVICE_CHANGED_ACTION：这个广播与当前设备的改变有关，一般注册这个广播后，就会收到，以此来获取当前设备的信息，
                        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//                    deviceInfo.setText(getString(R.string.device_info) + device.deviceName );
                        break;
                    case WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION:
//                    WIFI_P2P_DISCOVERY_CHANGED_ACTION：这个是搜索状态有关的广播，开始搜索和结束搜索时会收到，
                        int discoveryState = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED);
                        if (discoveryState == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {
                            refreshMsgList("开始搜索设备..");
                            isScan = true;
                        } else if (discoveryState == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED) {
                            refreshMsgList("停止搜索设备..");
                            isScan = false;
                        }
                        break;
                }
            }
        }
    };

    private ServerSocket mServerSocket;
    private Socket mSocket;
    private InputStream mInputStream;
    private ObjectInputStream mObjectInputStream;
    private FileOutputStream mFileOutputStream;
    private File mFile;
    private String acceptFileSaveDir = "WifiDirect";//接收的文件保存目录

    /**
     * 当作为服务器时，需开启Socket服务器
     */
    private void createSocketService(int serverPort) {
        try {
            mServerSocket = new ServerSocket();
            mServerSocket.setReuseAddress(true);
            mServerSocket.bind(new InetSocketAddress(serverPort));
            mSocket = mServerSocket.accept();
            refreshMsgList("new client connect successfully(" + mSocket.getRemoteSocketAddress()+")!");

            mInputStream = mSocket.getInputStream();
            mObjectInputStream = new ObjectInputStream(mInputStream);
            FileBean fileBean = (FileBean) mObjectInputStream.readObject();
            if(TextUtils.isEmpty(fileBean.getFilePath())){
                refreshMsgList("service status : accept msg ->"+fileBean.getData());
                return;
            }
            String name = new File(fileBean.getFilePath()).getName();
            refreshMsgList("客户端传递的文件名称 : " + name);
            refreshMsgList("客户端传递的MD5 : " + fileBean.getMd5());
            String fullPath = FileUtils.getExternalStorageRoot(MainActivity.this)+File.separator+acceptFileSaveDir+File.separator;
            //TODO 创建文件路径
            FileUtils.createOrExistsDir(fullPath);
            mFile = new File(fullPath + name);
            mFileOutputStream = new FileOutputStream(mFile);
            refreshMsgList("service status : start accept file ..");
            //开始接收文件
//            mHandler.sendEmptyMessage(40);
            byte bytes[] = new byte[1024];
            int len;
            long total = 0;
            int progress;
            while ((len = mInputStream.read(bytes)) != -1) {
                mFileOutputStream.write(bytes, 0, len);
                total += len;
                progress = (int) ((total * 100) / fileBean.getFileLength());
                refreshMsgList("service status : accept file progress-->"+progress);
                Message message = Message.obtain();
                message.what = 50;
                message.obj = progress;
//                mHandler.sendMessage(message);
            }
            //新写入文件的MD5
            String md5New = Md5Util.getMd5(mFile);
            //发送过来的MD5
            String md5Old = fileBean.getMd5();
            if (md5New != null || md5Old != null) {
                if (md5New.equals(md5Old)) {
//                    mHandler.sendEmptyMessage(60);
                    Log.e(TAG, "文件接收成功");
                    refreshMsgList("service status : 文件接收完成!");
                }
            } else {
//                mHandler.sendEmptyMessage(70);
            }

            mServerSocket.close();
            mInputStream.close();
            mObjectInputStream.close();
            mFileOutputStream.close();
        } catch (Exception e) {
//            mHandler.sendEmptyMessage(70);
            Log.e(TAG, "文件接收异常");
        }
    }


    /**
     * 向服务器发送数据
     * @param dataBean 消息对象
     * @param ipString IP地址
     * @param serverPort 服务器端口
     */
    private void connectServiceSocket(DataBean dataBean,String ipString, int serverPort) {
        try {
            refreshMsgList("client status : start client to send task ..");
            Socket socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ipString, serverPort);
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(dataBean);
            if(TextUtils.isEmpty(dataBean.getFilePath())){
                objectOutputStream.close();
                return;
            }
            refreshMsgList("client status : start client to send file task ..");
            FileInputStream inputStream = new FileInputStream(new File(dataBean.getFilePath()));
            long size = FileSizeUtil.getFileSize(new File(dataBean.getFilePath()));
            long total = 0;
            byte bytes[] = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                total += len;
                int progress = (int) ((total * 100) / size);
                refreshMsgList("client status : send file progress -->"+progress);
                Log.e(TAG, "文件发送进度：" + progress);
                Message message = Message.obtain();
                message.what = 10;
                message.obj = progress;
//                mHandler.sendMessage(message);
            }
            outputStream.close();
            objectOutputStream.close();
            inputStream.close();
            socket.close();
//            mHandler.sendEmptyMessage(20);
            Log.e(TAG, "文件发送成功");
        } catch (Exception e) {
//            mHandler.sendEmptyMessage(30);
            Log.e(TAG, "文件发送异常");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            refreshMsgList(2,"client status : get img local path -->"+Matisse.obtainPathResult(data).get(0));
            imgPath = Matisse.obtainPathResult(data).get(0);
            //连接服务器ip地址
            ThreadPoolManager.getInstance(true).execute(() -> {
                DataBean dataBean = new DataBean();
                dataBean.setDataType(2);
               /*byte[] imgBytes = ImageUtil.imgTobyteArray(MainActivity.this,imgPath);
                if(imgBytes == null || imgBytes.length == 0){
                    refreshMsgList(1,"client status : imgBytes length -- > 0");
                    return;
                }
                refreshMsgList("client status : imgBytes length -- >"+imgBytes.length);
//                dataBean.setImg(imgBytes);
                String imgStr = ImageUtil.imgToBase64(MainActivity.this,imgPath);
                refreshMsgList("client status : 图片base64长度-->"+imgStr.length()+" "+imgStr);
//                dataBean.setData(imgStr);
                dataBean.setBase64(imgStr);*/
                dataBean.setFilePath(imgPath);
                connectServiceSocket(dataBean,ipString, SERVICE_PORT);
                /*try {
                    Socket socket = new Socket(ipString, SERVICE_PORT);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //读取图片到ByteArrayOutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    out.write(bytes);
                    refreshMsgList("client status : send img finish -- >"+bytes.length);
                    out.close();
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }*/
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private static final int REQUEST_CODE_CHOOSE = 23;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_WIFI_STATE})
    void getImg() {
        Set<MimeType> set = new ArraySet<>();
        set.add(MimeType.JPEG);
        Matisse.from(MainActivity.this)
                .choose(set, false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.shenmou.wifidirectdemo.FileProvider"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
//                                            .imageEngine(new GlideEngine())  // for glide-V3
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener((uriList, pathList) -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .setOnCheckedListener(isChecked -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private AlertDialog dialog;
    private boolean isFinish = true;

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_WIFI_STATE})
    void showRationaleForCamera(final PermissionRequest request) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(getString(R.string.permission_request_for_bluetooth))
                    .setPositiveButton(getString(R.string.string_dialog_ok), (dialog, button) -> request.proceed())
                    .setNegativeButton(getString(R.string.string_dialog_cancel), (dialog, button) -> request.cancel()).create();
        }
        if (!dialog.isShowing() && isFinish) {
            isFinish = false;
            dialog.show();
        }
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_WIFI_STATE})
    void showDeniedForCamera() {
        isFinish = true;
        toastMsg(getString(R.string.permission_request_for_bluetooth_result_reject));
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_WIFI_STATE})
    void showNeverAskForCamera() {
        refreshMsgList(1,"status : 没有权限!!!!");
        Log.d(TAG, "showNeverAskForCamera: 不再提醒");
        isFinish = true;
        toastMsg(getString(R.string.permission_request_for_bluetooth_result_reject1));
    }

    private void toastMsg(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
