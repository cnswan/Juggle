package com.cnswan.juggle.amvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.cnswan.juggle.module.changeskin.attr.SkinAttr;
import com.cnswan.juggle.module.changeskin.attr.SkinAttrSupport;
import com.cnswan.juggle.module.changeskin.attr.SkinView;
import com.cnswan.juggle.module.changeskin.callback.ISkinChangedListener;
import com.cnswan.juggle.module.changeskin.control.SkinManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 15/9/22.
 * adb push  your_plugin.apk /sdcard/  这里直接放到了根目录下;
 */
public class BaseSkinActivity extends AppCompatActivity implements ISkinChangedListener, LayoutInflaterFactory {
    //一下部分是仿照FrameWork层源码写的;
    static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();

    private final Object[] mConstructorArgs = new Object[2];

    private static Method sCreateViewMethod;

    static final Class<?>[] sCreateViewSignature = new Class[]{View.class, String.class,
            Context.class, AttributeSet.class};

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = getLayoutInflater();
        AppCompatDelegate delegate = getDelegate();
        View view = null;
        try {
            //public View createView
            // (View parent, final String name, @NonNull Context context, @NonNull AttributeSet attrs)
            if (sCreateViewMethod == null) {
                //通过反射拿到createView()的方法,这个方法是用来对一些TextView,Button等进行包装的,是5.0提供的新特性;
                Method methodOnCreateView = delegate.getClass().getMethod("createView", sCreateViewSignature);
                sCreateViewMethod = methodOnCreateView;
            }
            Object object = sCreateViewMethod.invoke(delegate, parent, name, context, attrs);
            view = (View) object;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        //这里返回的是list,是因为一个view可能同时具备两个换肤的特性,就是既要换背景也要换文字;
        List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(attrs, context);
        if (skinAttrList.isEmpty()) {
            return view;
        }
        if (view == null) {
            view = createViewFromTag(context, name, attrs);
        }
        injectSkin(view, skinAttrList);
        return view;

    }

    //注入皮肤:将当前Activity中需要换肤的view添加到list中,然后将list添加到全局的view中
    //这里需要注意的是,要先查看是否需要换肤,如果需要,在执行apply()方法;把要展示的皮肤先替换了;
    private void injectSkin(View view, List<SkinAttr> skinAttrList) {
        //do some skin inject
        if (skinAttrList.size() != 0) {
            List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
            if (skinViews == null) {
                skinViews = new ArrayList<SkinView>();
            }
            SkinManager.getInstance().addSkinView(this, skinViews);
            skinViews.add(new SkinView(view, skinAttrList));

            if (SkinManager.getInstance().needChangeSkin()) {
                SkinManager.getInstance().apply(this);
            }
        }
    }

    //新特性反射的类无法加载到,所以使用createViewFromTag()这个方法来创建view;
    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                // try the android.widget prefix first...
                //tag中没有. 所以认为是系统的特性,手动添加上下面的前缀,其实有三个前缀的;
                return createView(context, name, "android.widget.");
            } else {
                //如果tag中有 .  ;说明这是一个我们自定义的view,那么就需要我们手动反射来创建了;
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                //手动反射,通过classLoader来反射;
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            //note:在layout布局中的控件默认会调用两个参数的构造方法;而我们传入的参数列表,也正是一个context和attrs;
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().addChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().removeChangedListener(this);
    }


    @Override
    public void onSkinChanged() {
        SkinManager.getInstance().apply(this);
    }
}
