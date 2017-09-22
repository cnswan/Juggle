package com.cnswan.juggle.activity.news.top.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Editable;
import android.text.Html;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnswan.juggle.R;
import com.cnswan.juggle.amvp.BaseHeaderActivity;
import com.cnswan.juggle.bean.topnews.TopNewsItem;

import org.xml.sax.XMLReader;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopNewsDetailActivity extends BaseHeaderActivity {

    TextView    tvTopNewsDetail;
    ImageView   imgTopNewsDetail;
    ImageView   imgItemBg;
    TopNewsItem item;

    boolean first = true;
    int firstIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_top_news_detail);
        if (getIntent() != null) {
            item = (TopNewsItem) getIntent().getSerializableExtra("bean");
            initHeader();
        }

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(item.getTitle());

        loadTopNewsDetail();

        //拿到进场动画,并添加一个监听器,注意此时动画还没有开始呢,你自己添加了监听器,动画开始结束都在你的掌控中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    //动画结束后,立即移除动画,防止内存泄露?
                    getWindow().getEnterTransition().removeListener(this);
                    Glide.with(TopNewsDetailActivity.this)

                            .load(item.getThumbnail_pic_s())
                            .crossFade(500)  //淡入淡出
//                            .asBitmap()
                            .centerCrop()
                            .dontAnimate()
                            .placeholder(R.drawable.img_default_meizi)
                            .into(imgTopNewsDetail);

                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
    }

    private void loadTopNewsDetail() {
        OkHttpClient client = new OkHttpClient();
        String url = item.getUrl();
        Request request = new Request.Builder().url(url).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showError();
                    }

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String origin = response.body().string();
                String second = Html.fromHtml(origin, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        return null;
                    }
                }, new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                        if (first && output.length() > 0 && opening) {
                            firstIndex = output.length();
                            first = false;
                            output.delete(0, output.length());
                        }
                    }
                }).toString();

                //把标题去掉...
                if (second.substring(0, firstIndex) == second.substring(firstIndex, firstIndex * 2)) {
                    second = second.substring(2 * firstIndex);
                }

                StringBuilder sb = new StringBuilder();
                sb.append("\n").append("\uFFFC").append(" ").append("\n");

                final String third = second.replace(sb, "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTopNewsDetail.setText(third);
                        showContentView();
                    }

                });
            }
        });

    }

    @Override
    protected void initHeaderView(View headerView) {
        imgTopNewsDetail = (ImageView) headerView.findViewById(R.id.iv_topnews_photo);
        imgItemBg = (ImageView) headerView.findViewById(R.id.img_item_bg);
    }

    @Override
    protected void initContentView(View contentView) {
        tvTopNewsDetail = (TextView) contentView.findViewById(R.id.tv_top_news_detail);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_top_news_detail;
    }

    @Override
    protected int getHeaderLayout() {
        return R.layout.activity_top_news_detail_header;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (item == null) {
            return "";
        }
        return item.getThumbnail_pic_s();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return imgItemBg;
    }

    private void initHeader() {
        //加载电影图片;
/*        Glide.with(this)
                .load(item.getThumbnail_pic_s())
//                .crossFade(500)  //淡入淡出
                //.override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R
                .dimen.movie_detail_height))  //不适应图片大小,而是使用这个尺寸;
                .placeholder(R.drawable.img_default_meizi)
                .error(R.drawable.img_default_meizi)
//                .centerCrop()
                .into(imgTopNewsDetail);*/

        /*Glide.with(this)
                .load(item.getThumbnail_pic_s())
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.img_default_meizi)
                .into(imgTopNewsDetail);*/

/*        imgTopNewsDetail.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(TopNewsDetailActivity.this)
                        .load(item.getThumbnail_pic_s())
                        .placeholder(R.drawable.img_default_meizi)
                        .error(R.drawable.img_default_meizi)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgTopNewsDetail);
            }
        });*/

        Glide.with(this)
                .load(item.getThumbnail_pic_s())
                .error(R.drawable.stackblur_default)
                .dontAnimate()
                .placeholder(R.drawable.stackblur_default)
                // .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 23, 4))
                .into(imgItemBg);
    }

/*

    @Override
    public void initNestedScrollView(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //System.out.println("现在再新闻详情页," + v.toString());

        v.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                System.out.println("横向" + v.computeHorizontalScrollOffset());
                System.out.println("纵向" + v.computeVerticalScrollOffset());

                System.out.println(oldScrollY + "-->" + scrollY);

                if (v.computeVerticalScrollOffset() == 0 && oldScrollY > scrollY) { //新的值越来越小,向上

                } else if (v.computeVerticalScrollOffset() == 0 && oldScrollY < scrollY) {  //新的值越来越大,向下
                    View contentView = v.getChildAt(0);
                    //contentView.getMeasuredHeight() <= v.getScrollY() + v.getHeight();
                }
            }
        });


    }
*/

    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, TopNewsItem positionData, ImageView imageView) {
        Intent intent = new Intent(context, TopNewsDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, "transition_news_img");//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
