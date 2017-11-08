package com.cnswan.juggle.module.arouter;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * ARouter 服务实现 继承IProvicer
 * Created by cnswan on 2017/11/8.
 */

public interface ServiceHello extends IProvider {

    void sayHello(String name);

}
