package com.cnswan.juggle.module.changeskin.control;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cnswan.juggle.module.changeskin.attr.SkinView;
import com.cnswan.juggle.module.changeskin.callback.ISkinChangedListener;
import com.cnswan.juggle.module.changeskin.callback.ISkinChangingCallback;
import com.cnswan.juggle.utils.PrefUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/9/22.
 */
public class SkinManager {
    private Context         mContext;
    private Resources       mResources;
    private ResourceManager mResourceManager;
    private PrefUtils       mPrefUtils;

    private boolean usePlugin;
    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps         = new HashMap<ISkinChangedListener,
            List<SkinView>>();
    private List<ISkinChangedListener>                mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private SkinManager() {
    }

    //静态内部类,避免内存泄露;
    private static class SingletonHolder {
        static SkinManager sInstance = new SkinManager();
    }

    //单利模式;
    public static SkinManager getInstance() {
        return SingletonHolder.sInstance;
    }


    //先调用这个
    public void init(Context context) {
        mContext = context.getApplicationContext(); //获取一个全局的context,防止内存泄露;
        mPrefUtils = new PrefUtils(mContext);       //pref工具;

        String skinPluginPath = mPrefUtils.getPluginPath();
        String skinPluginPkg = mPrefUtils.getPluginPkgName();
        mSuffix = mPrefUtils.getSuffix(); //后缀;
        //第一次加载时,这个路径是空的,所以直接返回了;
        if (TextUtils.isEmpty(skinPluginPath))
            return;
        File file = new File(skinPluginPath);
        if (!file.exists()) return;
        try {
            //如果路径存在,那么就加载类;loadPlugin只是初始化好了AssertManager;
            loadPlugin(skinPluginPath, skinPluginPkg, mSuffix);
            mCurPluginPath = skinPluginPath;
            mCurPluginPkg = skinPluginPkg;
        } catch (Exception e) {
            mPrefUtils.clear();
            e.printStackTrace();
        }
    }

    //主要目的就是初始化ResourceManager,里面包含了插件中的Resource;
    //NOTE:可以在这里将skin_path写入到sd卡中啊;
    private void loadPlugin(String skinPath, String skinPkgName, String suffix) throws Exception {
        //checkPluginParams(skinPath, skinPkgName);
        File destFile = new File(skinPath);
        //重要改变:不存在我就拷贝进去,getFileDir();
        if (!destFile.exists()) {
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = mContext.getAssets().open(SkinConfig.PLUGIN_NAME);
                out = new FileOutputStream(destFile);
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                System.out.println("拷贝成功!!!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);

        Resources superRes = mContext.getResources();
        mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResourceManager = new ResourceManager(mResources, skinPkgName, suffix);
        usePlugin = true;
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName) {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName)) {
            return false;
        }
        return true;
    }

    /**
     * 检测插件参数是否合法;简单的检测了一下是否为空;
     *
     * @param skinPath    路径
     * @param skinPkgName 包名
     */
    private void checkPluginParamsThrow(String skinPath, String skinPkgName) {
        if (!checkPluginParams(skinPath, skinPkgName)) {
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty ! ");
        }
    }


    public void removeAnySkin() {
        clearPluginInfo();
        notifyChangedListeners();
    }


    public boolean needChangeSkin() {
        return usePlugin || !TextUtils.isEmpty(mSuffix);
    }


    public ResourceManager getResourceManager() {
        if (!usePlugin) {
            //如果恢复默认,那么mResourceManager就使用应用内的;
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }


    /**
     * 应用内换肤，传入资源区别的后缀
     *
     * @param suffix
     */
    public void changeSkin(String suffix) {
        clearPluginInfo();//clear before
        mSuffix = suffix;
        mPrefUtils.putPluginSuffix(suffix);
        notifyChangedListeners();
    }

    //这里最终要的是将usePlugin这是为false,然后清除sp;
    private void clearPluginInfo() {
        mCurPluginPath = null;
        mCurPluginPkg = null;
        usePlugin = false;
        mSuffix = null;
        mPrefUtils.clear();
    }

    private void updatePluginInfo(String skinPluginPath, String pkgName, String suffix) {
        mPrefUtils.putPluginPath(skinPluginPath);
        mPrefUtils.putPluginPkg(pkgName);
        mPrefUtils.putPluginSuffix(suffix);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
        mSuffix = suffix;
    }


    public void changeSkin(final String skinPluginPath, final String pkgName, ISkinChangingCallback callback) {
        changeSkin(skinPluginPath, pkgName, "", callback);
    }


    /**
     * 根据suffix选择插件内某套皮肤，默认为""
     *
     * @param skinPluginPath
     * @param pkgName
     * @param suffix         默认""吧;
     * @param callback
     */
    public void changeSkin(final String skinPluginPath, final String pkgName, final String suffix,
                           ISkinChangingCallback callback) {
        if (callback == null)
            callback = ISkinChangingCallback.DEFAULT_SKIN_CHANGING_CALLBACK;
        final ISkinChangingCallback skinChangingCallback = callback;

        skinChangingCallback.onStart();
        //简单的检测一下是否为""或者null;
        checkPluginParamsThrow(skinPluginPath, pkgName);

        //如果当前的路径和传入的相等,那么就不需要换肤,也就是说你连续点击了两次换肤;那么第二次肯定什么也不做;
        if (skinPluginPath.equals(mCurPluginPath) && pkgName.equals(mCurPluginPkg)) {
            return;
        }

        //开启异步线程;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    //执行成功后,转向UI线程;
                    //首先将此次插件的信息保存的sp中;
                    updatePluginInfo(skinPluginPath, pkgName, suffix);
                    //通知list中的view进行替换;
                    notifyChangedListeners();
                    skinChangingCallback.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

            }
        }.execute();
    }


    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews) {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }

    //传入的是实现了ISkinChangedListener接口的Activity,那么就从map中获取到当前的Activity,对其中的view执行apply;
    public void apply(ISkinChangedListener listener) {
        List<SkinView> skinViews = getSkinViews(listener);

        if (skinViews == null) return;
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void addChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.add(listener);
    }


    public void removeChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }


    public void notifyChangedListeners() {
        for (ISkinChangedListener listener : mSkinChangedListeners) {
            listener.onSkinChanged();
        }
    }

}
