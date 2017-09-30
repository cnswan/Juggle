package com.cnswan.juggle.bean;

import com.alibaba.android.arouter.facade.service.SerializationService;

/**
 * ARouter如果需要传递自定义对象，需要实现 SerializationService,并使用@Route注解标注(方便用户自行选择序列化方式)
 * Created by 00013259 on 2017/9/30.
 */

public abstract class BaseRouterBean extends BaseBean implements SerializationService {


}
