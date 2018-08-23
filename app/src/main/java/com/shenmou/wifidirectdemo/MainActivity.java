package com.shenmou.wifidirectdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "首页";
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private Context mContext;

    private RecyclerView recyclerView;
    private WifiDrectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;
        initWifiP2p(mContext);
        registerBroadrost();
        initRecycleView();
        startSearchDevice();
    }

    private void initRecycleView() {
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WifiDrectAdapter(MainActivity.this);
        adapter.setDatas(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new WifiDrectAdapter.WifiDrectDeviceOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                connectWifiP2pDevice(list.get(position));
            }
        });
    }

    private void initWifiP2p(Context mContext){
        wifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        channel =  wifiP2pManager.initialize(mContext,getMainLooper(),channelListener);
    }

    /**
     * 注册广播
     */
    private void registerBroadrost(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mContext.registerReceiver(receiver, intentFilter);
    }


    /**
     * 开始搜索设备
     */
    private void startSearchDevice(){
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess:正在搜索..");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "onFailure: 搜索失败!");
            }
        });
    }

    /**
     * 停止搜索设备
     */
    private void stopSearchDevice(){
        wifiP2pManager.stopPeerDiscovery(channel, null);
    }

    /**
     * 连接设备
     * @param wifiP2pDevice
     * 基本就是选择一个要连接的设备，配置一个WifiP2pConfig对象，调用connect方法进行连接，由于p2p相关API大多都是异步的，需要一个监听成功与否的操作。需要注意的是，设备有很多状态，只有处于
    Available状态时才可以尝试连接，另外还有Invited和Connected状态等。Connected就是已经连接，Invited是指请求过连接，对方可能处于另外一个组中并且是GC身份，不能与其他设备连接，请求被搁置。
     */
    private void connectWifiP2pDevice(WifiP2pDevice wifiP2pDevice){
        if (wifiP2pDevice.status == WifiP2pDevice.AVAILABLE) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: 连接成功!");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "onFailure: 连接失败!");
                }
            });
        }else {
            Log.d(TAG, "connectWifiP2pDevice: 当前设备不可连接,状态-->"+wifiP2pDevice.status);
        }
    }

    /**
     * 建立连接时，身份是随机分配的，不过我们可以指定自己作为GO设备，通过wifiP2pManager.createGroup创建组，来等待客户端连接
     * 相当于此时作为服务器等待客户端来连接自己
     * @param wifiP2pDevice
     */
    private void startWaitOtherConnect(WifiP2pDevice wifiP2pDevice){
        if (wifiP2pDevice.status == WifiP2pDevice.AVAILABLE) {
            wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i) {

                }
            });
        }
    }

    /**
     * 断开连接设备
     * @param wifiP2pDevice
     */
    private void disconnect(WifiP2pDevice wifiP2pDevice){
        if (wifiP2pDevice.status == WifiP2pDevice.CONNECTED) {
            wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: remove group success!");
                }
                public void onFailure(int reason) {
                    Log.d(TAG, "onFailure: remove group fail!");
                }
            });
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(receiver);
        stopSearchDevice();
    }

    /**
     * 其中channel是许多操作的真正执行者，而且许多操作都是异步的，需要借助很多接口实现。
     * 关于WiFi P2P的开发，大部分都借助于WifiP2pManager实现，而且一些状态的获取都是基于广播的，
     * 所以我们需要建立一个广播接受者，来接受各种相关的广播，首先需要注册下面几个广播：
     */
    private WifiP2pManager.ChannelListener channelListener = new WifiP2pManager.ChannelListener() {
        @Override
        public void onChannelDisconnected() {

        }
    };

    private LinkedList<WifiP2pDevice> list = new LinkedList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null && !TextUtils.isEmpty(intent.getAction())){
                switch (intent.getAction()){
                    case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                        boolean p2pIsEnable = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,WifiP2pManager.WIFI_P2P_STATE_DISABLED) == WifiP2pManager.WIFI_P2P_STATE_ENABLED;
                        break;
                    case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                        //获取到设备列表信息
                        WifiP2pDeviceList mPeers = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                        list.clear(); //清除旧的信息
                        list.addAll(mPeers.getDeviceList()); //更新信息
                        adapter.setDatas(list);
                        break;
                    case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        //这是连接状态发送变化时的广播，如连接了一个设备，断开了一个设备都会接收到广播。着这个广播到来时，
                        //NetworkInfo 的isConnected()可以判断时连接还是断开时接收到的广播。
                        //WifiP2pInfo保存着一些连接的信息，如groupFormed字段保存是否有组建立，groupOwnerAddress字段保存GO设备的地址信息，isGroupOwner字段判断自己是否是GO设备。WifiP2pInfo也可以随时用过wifiP2pManager.requestConnectionInfo来获取。
                        //WifiP2pGroup存放着当前组成员的信息，这个信息只有GO设备可以获取。同样这个信息也可以通过wifiP2pManager.requestGroupInfo获取
                        NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                        WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                        WifiP2pGroup wifiP2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
                        break;
                    case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                        //WIFI_P2P_THIS_DEVICE_CHANGED_ACTION：这个广播与当前设备的改变有关，一般注册这个广播后，就会收到，以此来获取当前设备的信息，
                        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//                    deviceInfo.setText(getString(R.string.device_info) + device.deviceName );
                        break;
                    case WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION:
//                    WIFI_P2P_DISCOVERY_CHANGED_ACTION：这个是搜索状态有关的广播，开始搜索和结束搜索时会收到，
                        int discoveryState = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE,WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED);
//                    isDiscover = discoveryState == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED;
                        break;
                }
            }
        }
    };

}
