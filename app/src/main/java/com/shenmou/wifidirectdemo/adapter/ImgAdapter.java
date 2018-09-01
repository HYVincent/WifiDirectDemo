package com.shenmou.wifidirectdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shenmou.wifidirectdemo.R;
import com.shenmou.wifidirectdemo.bean.ImgBean;

import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.adapter
 * @class describe
 * @date 2018/9/1 18:13
 */
public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {
    private Context mContext;
    private List<ImgBean> data;

    public ImgAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ImgBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        return new ImgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_img,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder imgViewHolder, int i) {
        ImgBean imgBean = data.get(i);
        imgViewHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(imgBean.getImgPath()));
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    class ImgViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_img_iv);
        }
    }
}
