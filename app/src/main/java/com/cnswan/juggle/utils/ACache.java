package com.cnswan.juggle.utils;


/**
 * 使用方法:
 * (1). ACache mCache = ACache.get(this);
 * (2). String cacheData = mCache.getAsString("cache_data");
 * (3). if (!TextUtils.isEmpty(cacheData)) { 解析、setAdapter等}
 * (4). 然后仍然请求网络，因为缓存的意义是为了在没网的情况下有数据显示。
 * (5). 网络请求后获得的数据再
 * mCache.remove("cache_data");
 * mCache.put("cache_data", data);
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ACache {
    //定义一些常量单位,主要用来制定缓存的保活时间;
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 缓存文件的大小,默认值:50 MB
    private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量

    //map中key是缓存文件的绝对路径名+pid所组成的字符串,val是缓存文件的实例,也就是说map中可以保存多个缓存文件;
    private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
    private ACacheManager mCache;   //缓存管理器类;暂且理解成单利吧,其实这个管理器是对缓存路径下一个文件夹对应一个管理器;
    //同时一个路径对应一个ACache对象;

    /**
     * 由于这个类的构造方法是private的,给外部提供的也只有这个get()方法,得到缓存实例;
     * @param ctx   上下文,创建默认缓存文件名为ACache;
     * @return 缓存实例;
     */
    public static ACache get(Context ctx) {
        return get(ctx, "ACache");
    }

    /**
     * 得到缓存实例
     * @param ctx       上下文
     * @param cacheName 缓存文件的名称
     * @return 缓存实例
     */
    public static ACache get(Context ctx, String cacheName) {
        File f = new File(ctx.getCacheDir(), cacheName);    //创建缓存文件对象;
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static ACache get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    /**
     * 得到缓存实例
     * @param ctx
     * @param max_zise      缓存文件的大小;
     * @param max_count     可以缓存的键值对的条数;
     * @return
     */
    public static ACache get(Context ctx, long max_zise, int max_count) {
        File f = new File(ctx.getCacheDir(), "ACache"); //"/data/data/app-package-name/cache/ACache"
        return get(f, max_zise, max_count);
    }

    /**
     * 得到缓存实例
     * @param cacheDir  缓存文件路径
     * @param max_zise  缓存文件大小
     * @param max_count 缓存文件保存数据的条目
     * @return
     */
    public static ACache get(File cacheDir, long max_zise, int max_count) {
        ACache cache = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (cache == null) {//第一次获取,map中肯定没有这个对象,所以创建一个ACache的实例,接下来看一下这个实例中做什么哪些事情
            cache = new ACache(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), cache);
        }
        return cache;
    }

    /**
     * 获取当前应用进程的pid所对应的字符串;
     * @return 获取当前应用进程的pid;
     */
    private static String myPid() {
//        return "_" + android.os.Process.myPid();
        return "_pid";
    }


    /**
     * 可以看到这个构造函数是私有的,在外部不能new,获取的方法只能通过ACache.get(ctx);
     * @param cacheDir
     * @param max_size
     * @param max_count
     */
    private ACache(File cacheDir, long max_size, int max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {  //只要执行到这个步骤,那肯定是cacheDir是不存在的,需要mkdir
            // 注意这个创建的是文件夹,并不是那个文件;
            //但是如果创建不成功的话,就会抛出异常;
            throw new RuntimeException("can't make dirs in "
                    + cacheDir.getAbsolutePath());
        }
        mCache = new ACacheManager(cacheDir, max_size, max_count);
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================

    /**
     * 保存 String数据 到 缓存中
     * @param key   保存的key,这里每一个key就对应了一个缓存文件夹下的缓存文件
     * @param value 保存的String数据,将数据写到文件中;
     */
    public void put(String key, String value) {
        File file = mCache.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * 保存 String数据 到 缓存中，这里存入的数据不是单纯的value,而是前面用时间结合的数据;
     *
     * @param key 保存的key
     * @param value 保存的String数据
     * @param saveTime　保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime) {
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public String getAsString(String key) {
        File file = mCache.get(key);
        if (!file.exists())
            return null;
        boolean removeFile = false; //判断需不需要删除;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            if (!Utils.isDue(readString)) {  //还没到期;
                return Utils.clearDateInfo(readString);
            } else {  //到期了;
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
    }


    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        File file = mCache.newFile(key);  //此时的file是:"cacheDir + key.hashCode() 的值,
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);  //如果没有这个文件就直接创建了;
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的数据
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = mCache.get(key);
            if (!file.exists())
                return null;
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) RAFile.length()];
            RAFile.read(byteArray);
            if (!Utils.isDue(byteArray)) {
                return Utils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================

    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject = ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    /**
     * 获取缓存文件
     *
     * @param key
     * @return value 缓存的文件
     */
    public File file(String key) {
        File f = mCache.newFile(key);
        if (f.exists())
            return f;
        return null;
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * @title 缓存管理器
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     */
    public class ACacheManager {
        private final AtomicLong cacheSize;     //缓存文件当前的大小;原子类实例,不用加锁保证线程安全;
        private final AtomicInteger cacheCount; //缓存文件当前的数量;原子类实例,不用加锁保证线程安全;
        private final long sizeLimit;
        private final int countLimit;

        //通过Collections类将HashMap修饰为并发类,key是缓存文件夹下的某一缓存文件,val是该缓存文件的最近修改时间;
        private final Map<File, Long> lastUsageDates = Collections
                .synchronizedMap(new HashMap<File, Long>());

        protected File cacheDir;

        private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        /**
         * 计算 cacheSize和cacheCount
         * 开启了新的线程,然后计算Cache目录下所有文件的大小
         * NOTE:这里其实是有问题的,如果你还引用了其它的第三方库,也往app/Cache中写数据,就统计多了... xxx
         * cacheSize和cacheCount在内部类中定义为AtomicLong和AtomicInteger量子类，也就是线程安全的。
         * 其基本的特性就是在多线程环境下，当有多个线程同时执行这些类的实例包含的方法时，具有排他性
         * 即当某个线程进入方法，执行其中的指令时，不会被其他线程打断，而别的线程就像自旋锁一样，
         * 一直等到该方法执行完成，才由JVM从等待队列中选择一个另一个线程进入。
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size += calculateSize(cachedFile);  //注意,这个size是当前Cache目录下所有文件当前的大小;
                            count += 1;                         //注意:这个count是当前Cache目录下的所有文件的数量;
                            lastUsageDates.put(cachedFile,
                                    cachedFile.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        /*
        更新cacheCount和cacheSize  lastUsageDates插入新建文件和时间的键值对
        文件放入程序缓存后，统计缓存总量，总数，文件存放到文件map中（value值为文件最后修改时间，便于根据设置的销毁时间进行销毁）
        缓存没有超过限制，则增加缓存总量，总数的数值
        缓存超过限制，则减少缓存总量，总数的数值
        通过removeNext方法找到最老文件的大小
        这里执行删除的逻辑是:如果文件数量超过了countLimit或者文件大小超过了sizeLimit,那么就删除一个最早的文件;
        超时是在何时判定的呢???
         */
        private void put(File file) {
            int curCacheCount = cacheCount.get();
            while (curCacheCount + 1 > countLimit) {
                long freedSize = removeNext();
                cacheSize.addAndGet(-freedSize);

                curCacheCount = cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);

            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            while (curCacheSize + valueSize > sizeLimit) {
                long freedSize = removeNext();
                curCacheSize = cacheSize.addAndGet(-freedSize);
            }
            cacheSize.addAndGet(valueSize);

            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);

            return file;
        }

        //根据Key的HashCode产生文件名字;
        private File newFile(String key) {
            return new File(cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File image = get(key);
            return image.delete();
        }

        private void clear() {
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /**
         * 移除旧的文件,既然是执行这个方法,那么肯定要删除一个文件;
         *
         * @return
         */
        private long removeNext() {
            if (lastUsageDates.isEmpty()) {
                return 0;
            }

            File mostLongUsedFile = null; //保存时间最长的文件
            Long oldestUsage = null;        //该文件的最后修改时间;
            Set<Entry<File, Long>> entries = lastUsageDates.entrySet();  //得到所有map中键值对(File文件和最近修改时间);
            synchronized (lastUsageDates) { //锁住;循环遍历键值对;这个时间
                for (Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    } else {
                        Long lastValueUsage = entry.getValue();
                        if (lastValueUsage < oldestUsage) {  //如果该文件的最近使用时间上一步的oldestUsage早
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }

            long fileSize = calculateSize(mostLongUsedFile);
            if (mostLongUsedFile.delete()) {
                lastUsageDates.remove(mostLongUsedFile);
            }
            return fileSize;
        }

        //返回该文件的大小;
        private long calculateSize(File file) {
            return file.length();
        }
    }

    /**
     * @title 时间计算工具类
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     */
    private static class Utils {

        /**
         * 判断缓存的String数据是否到期
         *
         * @param str
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr
                            .substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                    return true;
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
                        strInfo.length());
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, mSeparator) + 1,
                        data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == '-'
                    && indexOf(data, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (hasDateInfo(data)) {  //通过判断之前设置的格式是否不是满足到期的设置,然后判断是否是;
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14,
                        indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0)
                throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0,
                    Math.min(original.length - from, newLength));
            return copy;
        }

        private static final char mSeparator = ' ';

        private static String createDateInfo(int second) {
            String currentTime = System.currentTimeMillis() + "";
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + "-" + second + mSeparator;
        }


    }

}

/*
ASimpleCache

ASimpleCache 是一个为android制定的 轻量级的 开源缓存框架。轻量到只有一个java文件（由十几个类精简而来）。

1、它可以缓存什么东西？

普通的字符串、JsonObject、JsonArray、Bitmap、Drawable、序列化的java对象，和 byte数据。

2、它有什么特色？

特色主要是：
1：轻，轻到只有一个JAVA文件。
2：可配置，可以配置缓存路径，缓存大小，缓存数量等。
3：可以设置缓存超时时间，缓存超时自动失效，并被删除。
4：支持多进程。
3、它在android中可以用在哪些场景？

1、替换SharePreference当做配置文件
2、可以缓存网络请求数据，比如oschina的android客户端可以缓存http请求的新闻内容，缓存时间假设为1个小时，超时后自动失效，让客户端重新请求新的数据，减少客户端流量，同时减少服务器并发量。
3、您来说...
4、如何使用 ASimpleCache？

以下有个小的demo，希望您能喜欢：

ACache mCache = ACache.get(this);
mCache.put("test_key1", "test value");
mCache.put("test_key2", "test value", 10);//保存10秒，如果超过10秒去获取这个key，将为null
mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null
获取数据

ACache mCache = ACache.get(this);
String value = mCache.getAsString("test_key1");

 */