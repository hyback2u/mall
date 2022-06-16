package com.wxl.mall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangxl
 * @since 2022/6/16 20:20
 */
public class CompletableFutureTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main method... start...");

        // 异步任务, 均提交给自定义的线程池
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//        }, executor);


        // 方法完成后的感知
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).whenComplete((res, exception) -> {
//            // 虽然能得到异常信息, 但是没法修改返回数据
//            System.out.println("异步任务成功完成了..., 结果是：" + res + ", 异常是：" + exception);
//        }).exceptionally((throwable -> {
//            // 感知异常, 同时返回默认值
//            return 10;
//        }));

        // handle:方法执行完成之后的处理
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).handle((res, thr) -> {
//            if (res != null) {
//                return res * 100;
//            }
//            // 出现异常了
//            if (null != thr) {
//                return 100;
//            }
//
//            return 100;
//        });
//
//        System.out.println("future.get(): " + future.get());


        // 线程串行化: 1、thenRunAsync:不能获取到上一步的执行结果, 无返回值
//        CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).thenRunAsync(() -> {
//            System.out.println("任务2启动了...");
//        }, executor);

        // 线程串行化: 2、thenAcceptAsync:能接受上一步结果, 但是无返回值
//        CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).thenAcceptAsync((res) -> {
//            System.out.println("任务2启动了..., 上一步运行的结果：" + res);
//        }, executor);


        // 线程串行化: 3、thenApplyAsync:能接收上一步结果, 有返回值
        CompletableFuture<String> thenApplyAsyncFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).thenApplyAsync((res) -> {
            System.out.println("任务2启动了..., 上一步运行的结果：" + res);

            return "Hello, " + res;
        }, executor);

        // thenApplyAsyncFuture.get()是一个阻塞方法
        System.out.println("thenApplyAsyncFuture: " + thenApplyAsyncFuture.get());

        System.out.println("main method... end...");
    }
}
