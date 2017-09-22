package com.cnswan.juggle.activity.news.movie.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnswan.juggle.R;
import com.cnswan.juggle.adapter.MovieDetailAdapter;
import com.cnswan.juggle.amvp.BaseHeaderActivity;
import com.cnswan.juggle.bean.movie.MovieDetailBean;
import com.cnswan.juggle.bean.movie.SubjectsBean;
import com.cnswan.juggle.module.http.HttpUtils;
import com.cnswan.juggle.utils.CommonUtils;
import com.cnswan.juggle.utils.StringFormatUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends BaseHeaderActivity {

    private SubjectsBean subjectsBean;
    private String       mMoreUrl;
    private String       mMovieName;

    private ImageView imgItemBg;
    private ImageView imgOnePhoto;
    private TextView tvOneRattingRate;
    private TextView tvOneRatingNumber;
    private TextView tvOneDirector;
    private TextView tvOneCasts;
    private TextView tvOneGenres;
    private TextView tvOneDay;
    private TextView tvOneCity;


    private RecyclerView rvCast;
    private TextView tvOneTitle;
    private TextView tvOneSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_detail2);
        if (getIntent() != null) {
            subjectsBean = (SubjectsBean) getIntent().getSerializableExtra("bean");
            initHeader();
        }

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(subjectsBean.getTitle());
        setSubTitle(String.format("主演：%s", StringFormatUtil.formatName(subjectsBean.getCasts())));

        loadMovieDetail();
    }
    private void initHeader() {
        //加载电影图片;
        Glide.with(imgOnePhoto.getContext())
                .load(subjectsBean.getImages().getLarge())
                .crossFade(500)  //淡入淡出
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R
                        .dimen.movie_detail_height))  //不适应图片大小,而是使用这个尺寸;
                .placeholder(R.drawable.img_default_meizi)
                .error(R.drawable.img_default_meizi)
                .into(imgOnePhoto);

        tvOneRattingRate.setText("评分:" + subjectsBean.getRating().getAverage());
        tvOneRatingNumber.setText(subjectsBean.getCollect_count() + "人评");
        tvOneDirector.setText(StringFormatUtil.formatName(subjectsBean.getDirectors()));
        tvOneCasts.setText(StringFormatUtil.formatName(subjectsBean.getCasts()));
        tvOneGenres.setText("类型:" + StringFormatUtil.formatGenres(subjectsBean.getGenres()));

        // "23":模糊度；"4":图片缩放4倍后再进行模糊,对背景进行模糊;
        Glide.with(this)
                .load(subjectsBean.getImages().getMedium())
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 23, 4))
                .into(imgItemBg);
    }


    @Override
    protected void initHeaderView(View headerView) {
        if (headerView != null) {
            imgItemBg = (ImageView) headerView.findViewById(R.id.img_item_bg);
            imgOnePhoto = (ImageView) headerView.findViewById(R.id.iv_one_photo);
            tvOneRattingRate = (TextView) headerView.findViewById(R.id.tv_one_rating_rate);
            tvOneRatingNumber = (TextView) headerView.findViewById(R.id.tv_one_rating_number);
            tvOneDirector = (TextView) headerView.findViewById(R.id.tv_one_directors);
            tvOneCasts = (TextView) headerView.findViewById(R.id.tv_one_casts);
            tvOneGenres = (TextView) headerView.findViewById(R.id.tv_one_genres);
            tvOneDay = (TextView) headerView.findViewById(R.id.tv_one_day);
            tvOneCity = (TextView) headerView.findViewById(R.id.tv_one_city);
        }
    }

    @Override
    protected void initContentView(View contentView) {
        if (contentView != null) {
            Log.e("###", "初始化content中的view");
            tvOneTitle = (TextView) contentView.findViewById(R.id.tv_one_title);
            tvOneSummary = (TextView) contentView.findViewById(R.id.tv_one_summary);
            rvCast = (RecyclerView) contentView.findViewById(R.id.xrv_cast);
            if (rvCast == null) {
                Log.e("###", "RecyclerView是null");
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected int getHeaderLayout() {
        return R.layout.header_slide_shape;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (subjectsBean == null) {
            return "";
        }
        return subjectsBean.getImages().getMedium();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return imgItemBg;
    }


    private void loadMovieDetail() {
        Subscription get = HttpUtils.getInstance().getMovieDetailClient().getMovieDetail(subjectsBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(final MovieDetailBean movieDetailBean) {
                        // 上映日期
                        tvOneDay.setText(String.format("上映日期：%s", movieDetailBean.getYear()));
                        // 制片国家
                        tvOneCity.setText(String.format("制片国家/地区：%s", StringFormatUtil.formatGenres
                                (movieDetailBean.getCountries())));

                        tvOneTitle.setText(StringFormatUtil.formatGenres(movieDetailBean.getAka()));
                        tvOneSummary.setText(movieDetailBean.getSummary());

                        mMoreUrl = movieDetailBean.getAlt();
                        mMovieName = movieDetailBean.getTitle();

                        transformData(movieDetailBean);
                    }
                });
    }


    /**
     * 异步线程转换数据
     */
    private void transformData(final MovieDetailBean movieDetailBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < movieDetailBean.getDirectors().size(); i++) {
                    movieDetailBean.getDirectors().get(i).setType("导演");
                }
                for (int i = 0; i < movieDetailBean.getCasts().size(); i++) {
                    movieDetailBean.getCasts().get(i).setType("演员");
                }

                MovieDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(movieDetailBean);
                    }
                });
            }
        }).start();
    }

    /**
     * 设置导演&演员adapter
     */
    private void setAdapter(MovieDetailBean movieDetailBean) {
        rvCast.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCast.setLayoutManager(mLayoutManager);

        // 需加，不然滑动不流畅
        rvCast.setNestedScrollingEnabled(false);
        rvCast.setHasFixedSize(false);

        MovieDetailAdapter mAdapter = new MovieDetailAdapter();
        mAdapter.addAll(movieDetailBean.getDirectors());
        mAdapter.addAll(movieDetailBean.getCasts());
        rvCast.setAdapter(mAdapter);
    }


    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, SubjectsBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, "transition_movie_img");//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
