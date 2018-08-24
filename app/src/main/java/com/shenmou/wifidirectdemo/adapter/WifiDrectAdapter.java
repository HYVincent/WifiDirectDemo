package com.shenmou.wifidirectdemo.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenmou.wifidirectdemo.R;

import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo
 * @class describe
 * @date 2018/8/23 17:31
 */
public class WifiDrectAdapter extends RecyclerView.Adapter<WifiDrectAdapter.WifiDrectViewHolder> {
    private static final String TAG = "数据列表适配器";
    private Context mContext;
    private List<WifiP2pDevice> datas;
    private WifiDrectDeviceOnClickListener onItemClickListener;

    public WifiDrectAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnItemClickListener(WifiDrectDeviceOnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDatas(List<WifiP2pDevice> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiDrectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        return new WifiDrectViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_wifi_drect, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WifiDrectViewHolder holder, final int i) {
        WifiP2pDevice device = datas.get(i);
        holder.tvName.setText(device.deviceName);
        holder.tvAddress.setText(device.deviceAddress);
        if (i == datas.size() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, i);
                }
            }
        });
        if(device.isGroupOwner()){
            holder.tvStatus.setText("我是服务器");
        }else {
            if(device.status == WifiP2pDevice.CONNECTED){
                holder.tvStatus.setText("设备已连接");
            }else {
                holder.tvStatus.setText("设备未连接");
            }
        }
        /*holder.tvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null)
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return datas == null || datas.size() == 0 ? 0 : datas.size();
    }

    class WifiDrectViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvAddress, tvStatus,tvService;
        private View vLine;

        public WifiDrectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_wifi_drect_tv_name);
            tvAddress = itemView.findViewById(R.id.item_wifi_drect_tv_address);
            vLine = itemView.findViewById(R.id.item_wifi_drect_v_line);
            tvStatus = itemView.findViewById(R.id.item_wifi_drect_tv_status);
            tvService = itemView.findViewById(R.id.item_wifi_start_service);
        }
    }

    public interface WifiDrectDeviceOnClickListener {
        void onItemClick(View view, int position);
    }


}
