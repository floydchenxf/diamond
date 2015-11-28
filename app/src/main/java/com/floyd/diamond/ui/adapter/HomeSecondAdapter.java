package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/25.
 */
public class HomeSecondAdapter extends RecyclerView.Adapter<HomeSecondAdapter.MyViewHolder> {
    private Context context;
    private int[]imgId;
    private String[]counts;
    //private List<TypeRecycle.DataEntity.CollectionsEntity> list;
    private  MyOnItemClickListener myOnItemClickListener;
    public HomeSecondAdapter(Context context,int[]list,String[] list2) {
        this.context = context;
        this.imgId=list;
        this.counts=list2;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        MyViewHolder viewHolder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.homerecycleitem_layout, null);
            viewHolder = new MyViewHolder(view);
            viewHolder.imageView= ((ImageView) view.findViewById(R.id.homerecycle_img));
            viewHolder.count= ((TextView) view.findViewById(R.id.count_home));
            view.setTag(viewHolder);
        }else{
            viewHolder= (MyViewHolder) view.getTag();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // holder.textView.setText(list.get(position).getTitle());
        getImage(holder, position);
    }

    @Override
    public int getItemCount() {
        return imgId.length;
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /*
        itemview 指的是一条一条的item
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        ImageView imageView;
        TextView count;

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();//获取位置
            //此处处理具体点击
            if (myOnItemClickListener != null) {
                myOnItemClickListener.onItemClick(v,layoutPosition);
                // Toast.makeText(context,"你眼瞎啊，没看见点击的是第"+layoutPosition+"条Message啊",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public interface  MyOnItemClickListener{
        void onItemClick(View view, int postion);//具体的点击事件
    }

    public void getImage(final MyViewHolder holder,int position){

       // holder.imageView.setBackgroundResource(imgId[position]);
      //  Log.e("count",counts[position]);
        holder.count.setText(counts[position]);
//        BitmapUtils bitmapUtils=new BitmapUtils(context);
//        bitmapUtils.configDefaul tLoadingImage(R.mipmap.l1);//默认背景图片
//        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.h1);
//        bitmapUtils.display(holder.imageView, list.get(position).getBanner_image_url(), new BitmapLoadCallBack<View>() {
//            @Override
//            /**container 前面参数中传递进来的控件
//             * uri 传递进来的url http://bbs.lidroid.com/static/image/common/logo.png
//             * 第三个参数是返回的图片
//             * 第四个参数和第五个参数 不用管
//             *
//             */
//            public void onLoadCompleted(View container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
//                holder.progressBar.setVisibility(View.INVISIBLE);
//                try {
//                    ((ImageView) container).setImageBitmap(bitmap);
//                    File file = new File(Environment.getExternalStorageDirectory() + "/aaa");
//                    if (!file.exists()) {//如果文件夹不存,就创建对应的文件夹,记住,android不会自动创建对应的文件夹
//                        file.mkdir();
//                    }
//                    /**
//                     * 将bitmap写入到文件,第一个参数是保存的文件类型,第二个参数,清晰度,第三个参数是输出流,就是写到什么文件中
//                     */
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "aaa/" + MD5Util.encodeBy32BitMD5(uri) + ".jpg")));
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onLoadFailed(View container, String uri, Drawable drawable) {
//                Toast.makeText(context, "请检查你的网络连接", Toast.LENGTH_SHORT).show();
//            }
//        });
   }
}

