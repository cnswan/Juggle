package com.cnswan.juggle.activity.picture;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zx.freetime.R;

import java.util.ArrayList;
import java.util.List;

public class DetailPictureActivity extends AppCompatActivity {

    // 保存图片
    private TextView tv_save_big_image;
    // 接收传过来的uri地址
    ArrayList<String> imageuri;
    // 接收穿过来当前选择的图片的数量
    int index;
    // 用于判断是头像还是文章图片 1:头像 2：文章大图
    int selet;

    // 用于管理图片的滑动
    ViewPager detailPictureViewPager;
    // 当前页数
    private int page;

    /**
     * 显示当前图片的页数
     */
    TextView very_image_viewpager_text;
    /**
     * 用于判断是否是加载本地图片
     */
    private boolean isLocal;

    DetailPicturePagerAdapter adapter;

    /**
     * 本应用图片的id
     */
    private int imageId;
    /**
     * 是否是本应用中的图片
     */
    private boolean isApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_picture);
        //接受传过来的参数;
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt("index");
        imageuri = bundle.getStringArrayList("imageuri");
        very_image_viewpager_text = (TextView) findViewById(R.id.very_image_viewpager_text);
        detailPictureViewPager = (ViewPager) findViewById(R.id.pic_vp);
        detailPictureViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                very_image_viewpager_text.setText((position + 1) + " / " + imageuri.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new DetailPicturePagerAdapter(this, imageuri);
        detailPictureViewPager.setAdapter(adapter);
        detailPictureViewPager.setCurrentItem(index);

    }


}
