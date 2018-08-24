package com.shenmou.wifidirectdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.util.util.DateUtils;
import com.common.view.util.DateUtil;
import com.shenmou.wifidirectdemo.R;
import com.shenmou.wifidirectdemo.bean.MsgBean;

import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.adapter
 * @class describe
 * @date 2018/8/24 11:34
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {

    private Context mContext;
    private List<MsgBean> data;

    public MsgAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MsgBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        return new MsgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_msg,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        MsgBean msgBean = data.get(i);
        msgViewHolder.tvTime.setText(DateUtils.getDateString(DateUtil.DATE_FORMAT_ALL,msgBean.getTime()));
        msgViewHolder.tvMsg.setText(msgBean.getMsg());
    }

    @Override
    public int getItemCount() {
        return data == null || data.size() == 0?0:data.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime,tvMsg;
        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_msg_time);
            tvMsg = itemView.findViewById(R.id.item_msg_msg);
        }
    }
}
