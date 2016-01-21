package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;

import org.w3c.dom.Text;

/**
 * Created by hy on 2016/1/21.
 */
public class OrderActivity extends Activity {
    private TextView content, cancle, sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        init();

    }

    public void init() {
        content = ((TextView) findViewById(R.id.content_order));
        cancle = ((TextView) findViewById(R.id.cancle));
        sure = ((TextView) findViewById(R.id.sure));

        content.setText("亲！您已抢单，请半小时之内完成下单并去任务列表填写订单号");
        cancle.setText("继续抢单");
        sure.setText("商品链接");

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getIntent().getStringExtra("url");
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(OrderActivity.this, "无效商品链接!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Intent goodsItemIntent = new Intent(OrderActivity.this, H5Activity.class);
                H5Activity.H5Data goodsData = new H5Activity.H5Data();
                goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                goodsData.data = url;
                goodsData.showProcess = true;
                goodsData.showNav = true;
                goodsData.title = "商品";
                goodsData.canZoom=true;
                goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                startActivity(goodsItemIntent);
                finish();
            }
        });
    }

}
