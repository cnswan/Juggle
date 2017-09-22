package com.cnswan.juggle.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zx.freetime.R;

public class NavDeedBackActivity extends AppCompatActivity {
    private TextView tvIssues;
    private TextView tvFaq;
    private TextView tvQQ;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_deed_back);

        initView();
        initListener();
    }

    void initView() {
        tvIssues = (TextView) findViewById(R.id.tv_issues);
        tvFaq = (TextView) findViewById(R.id.tv_faq);
        tvQQ = (TextView) findViewById(R.id.tv_qq);
        tvEmail = (TextView) findViewById(R.id.tv_email);
    }

    void initListener() {
        tvIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri issuesUrl = Uri.parse("https://github.com/zachaxy/FreeTime/issues");
                Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
                startActivity(intent);
            }
        });

        //常见问题归纳,还没有做,需要做的是在github中写一个单独的markdown文件来记录问题;
        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=907429707";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:zachaxy@163.com"));
                startActivity(data);
            }
        });
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavDeedBackActivity.class);
        mContext.startActivity(intent);
    }
}
