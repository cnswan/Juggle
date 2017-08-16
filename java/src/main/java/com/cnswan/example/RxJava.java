package com.cnswan.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 00013259 on 2017/8/15.
 */

public class RxJava {

    public static void main(String[] args) {
        //        useRxJavaByConsumer();
        //        useRxJavaByObserver();
        //        useRxJavaByMap();
        //        useRxJavaByFlatMap();
        useRxJavaByInterval();
        useRxJavaByTimer();
    }

    public static void useRxJavaByConsumer() {
        Observable.create(new ObservableOnSubscribe<String>() {// observable on subscribe 被观察者被订阅
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {// observable emitter 被观察者发射器(触发者)
                e.onNext("subscribe 订阅 触发该方法");
            }
        }).subscribe(new Consumer<String>() {// consumer 消费者
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    public static void useRxJavaByObserver() {
        Observable.create(new ObservableOnSubscribe<String>() {// observable on subscribe 被观察者被订阅
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {// observable emitter 被观察者发射器(触发者)
                e.onNext("subscribe 订阅 触发该方法");
                e.onComplete();
            }
        }).subscribe(new Observer<String>() {// observer 观察者
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("Observer-onSubscribe" + d);
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println();
            }
        });
    }

    public static void useRxJavaByMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(@NonNull String s) throws Exception {
                return Integer.valueOf(s);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    public static void useRxJavaByFlatMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("10");
            }
        }).flatMap(new Function<String, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull String s) throws Exception {
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    public static void useRxJavaByFlowable() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {

            }
        }, BackpressureStrategy.BUFFER).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
    }

    public static void useRxJavaByInterval() {
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println(aLong);
                    }
                });
    }

    public static void useRxJavaByTimer() {
        //        Flowable.timer(60, TimeUnit.MILLISECONDS)
        //                .onBackpressureDrop()
        //                .subscribe(new Consumer<Long>() {
        //                    @Override
        //                    public void accept(Long aLong) throws Exception {
        //                        System.out.println(aLong);
        //                    }
        //                });

        //        Observable.interval(3, 2, TimeUnit.SECONDS)
        //                .subscribeOn(Schedulers.newThread())
        //                .observeOn(Schedulers.newThread())
        //                .subscribe(new Consumer<Long>() {
        //                    @Override
        //                    public void accept(@NonNull Long aLong) throws Exception {
        //                        System.out.println("interval :" + aLong + " at " + System.currentTimeMillis() + "\n");
        //                    }
        //                });
    }
}
