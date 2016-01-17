package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.AdvVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MessageAdapter extends BaseAdapter {
    private List<AdvVO> advVOList = new ArrayList<AdvVO>();
    private Context mContext;
    private ImageLoader mImageLoader;
    private float oneDp;

    public MessageAdapter(Context context, ImageLoader mImageLoader) {
        this.mContext = context;
        this.mImageLoader = mImageLoader;
        oneDp = this.mContext.getResources().getDimension(R.dimen.one_dp);
    }

    public void addAll(List<AdvVO> advVOs, boolean clear) {
        if (clear) {
            advVOList.clear();
        }

        advVOList.addAll(advVOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return advVOList.size();
    }

    @Override
    public AdvVO getItem(int position) {
        return advVOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.messagelistviewitem_layout, null);
            holder = new ViewHolder();
            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.listview_img);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        AdvVO advVO = getItem(position);
        holder.imageView.setDefaultImageResId(R.drawable.tupian);
        holder.imageView.setImageUrl(advVO.getPreviewUrl(), mImageLoader, new BitmapProcessor() {
            @Override
            public Bitmap processBitmpa(Bitmap bitmap) {
                return ImageUtils.getOriginRoundBitmap(bitmap, 3*oneDp);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        public NetworkImageView imageView;
    }
}
