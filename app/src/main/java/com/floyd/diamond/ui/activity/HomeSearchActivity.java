package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.MyListView;
import com.floyd.diamond.bean.RecordSQLiteOpenHelper;
import java.util.Date;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * Created by hy on 2016/4/6.
 */
public class HomeSearchActivity extends Activity {

    private EditText et_search;
    private TextView tv_tip;
    private MyListView listView;
    private TextView tv_clear,conditon_search;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    ;
    private SQLiteDatabase db;
    private BaseAdapter adapter;

    private boolean isFirst=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_search1);
        // 初始化控件
        initView();

        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
                tv_tip.setVisibility(View.GONE);
                tv_clear.setVisibility(View.GONE);
            }
        });

        // 搜索框的键盘搜索键点击回调
        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    String nickname=et_search.getText().toString().trim();
                    boolean hasData = hasData(nickname);
                    if (!hasData) {
                        insertData(nickname);
                        queryData("");
                    }
                    et_search.setText(null);
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
//                    Toast.makeText(HomeSearchActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
                    if (nickname==null){
                        Toast.makeText(HomeSearchActivity.this,"请输入你要搜索的内容~~",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent(HomeSearchActivity.this,ChooseResultActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type",1+"");
                        intent.putExtra("nickName",nickname);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    conditon_search.setText("筛选条件");
                    conditon_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转条件筛选界面
                            startActivity(new Intent(HomeSearchActivity.this,ChooseActivity1.class));
                        }
                    });
                } else {
                    conditon_search.setText("搜索");
                    final String nickname=et_search.getText().toString().trim();
                    boolean hasData = hasData(nickname);
                    if (!hasData) {
                        insertData(nickname);
                        queryData("");
                    }
                    conditon_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转条件筛选界面
                            Intent intent=new Intent(HomeSearchActivity.this,ChooseResultActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type",1+"");
                            intent.putExtra("nickName",nickname);
                            startActivity(intent);
                        }
                    });
                }
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.text1);
                String name = textView.getText().toString();
//                et_search.setText(name);
//                Toast.makeText(HomeSearchActivity.this, name, Toast.LENGTH_SHORT).show();
                if (name==null){
                    Toast.makeText(HomeSearchActivity.this,"请输入你要搜索的内容~~",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(HomeSearchActivity.this,ChooseResultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("type",1+"");
                    intent.putExtra("nickName",name);
                    startActivity(intent);
                }

                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        });

        // 插入数据，便于测试
//        Date date = new Date();
//        long time = date.getTime();
//        insertData("Leo" + time);
//        if (isFirst){
//            tv_tip.setVisibility(View.GONE);
//            tv_clear.setVisibility(View.GONE);
//            isFirst=false;
//        }else{
//            tv_tip.setVisibility(View.VISIBLE);
//            tv_clear.setVisibility(View.VISIBLE);
//            // 第一次进入查询所有的历史记录
            queryData("");
//        }
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        if (cursor.getCount()==0){
            tv_tip.setVisibility(View.GONE);
            tv_clear.setVisibility(View.GONE);
        }else{
            tv_tip.setVisibility(View.VISIBLE);
            tv_clear.setVisibility(View.VISIBLE);
        }
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(this, R.layout.home_searchlist_item, cursor, new String[] { "name" },
                new int[] {R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        listView = (MyListView) findViewById(R.id.listView);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        findViewById(R.id.clearS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        findViewById(R.id.finishsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        conditon_search= ((TextView) findViewById(R.id.conditon_choose));


//        // 调整EditText左边的搜索按钮的大小
//        Drawable drawable = getResources().getDrawable(R.drawable.search_red);
//        drawable.setBounds(0, 0, 50, 50);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
//        et_search.setCompoundDrawables(drawable, null, null, null);// 只放左边
    }
}