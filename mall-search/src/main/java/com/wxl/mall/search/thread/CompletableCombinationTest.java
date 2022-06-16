package com.wxl.mall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangxl
 * @since 2022/6/16 20:20
 */
public class CompletableCombinationTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        System.out.println("main method... start...");

        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1线程启动：" + Thread.currentThread().getId());
            int i = 10 / 2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1线程结束：" + i);
            return i;
        }, executor);


        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2线程启动：" + Thread.currentThread().getId());
            System.out.println("任务2线程结束");
            return "Hello";
        }, executor);

        // 两任务组合, 都要完成

        // runAfterBothAsync:两个任务组合都要完成
//        future01.runAfterBothAsync(future02, () -> {
//            System.out.println("任务3线程启动：" + Thread.currentThread().getId());
//            System.out.println("任务3线程结束");
//        }, executor);

        // thenAcceptBothAsync:两个任务组合都要完成, 获取前两个任务的返回值
//        future01.thenAcceptBothAsync(future02, (res1, res2) -> {
//            System.out.println("任务3线程启动, 之前的结果: f1 = " + res1 + ", f2 = " + res2);
//            System.out.println("任务3线程结束");
//        }, executor);


        // 既能接收前两个的返回值, 又能自己返回数据
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (res1, res2) -> {
//            System.out.println("任务3线程启动");
//            return "我整合一下:" + res1 + ", " + res2;
//        }, executor);
//
//        System.out.println("任务3最终返回的：" + future.get());

        // -------------------------------------------------------------------------
        // 两个任务, 只要有一个完成, 就执行任务3

//        future01.runAfterEitherAsync(future02, ()-> {
//            System.out.println("任务3线程启动：" + Thread.currentThread().getId());
//            System.out.println("任务3线程结束");
//        }, executor);

        // future01 & future02 需要有相同的返回类型
//        future01.acceptEitherAsync(future02, (res)-> {
//            System.out.println("任务3线程启动：" + Thread.currentThread().getId() + ", 返回值：" + res);
//            System.out.println("任务3线程结束");
//        }, executor);


        CompletableFuture<String> future03 = future01.applyToEitherAsync(future02, (res) -> {
            System.out.println("任务3线程启动：" + Thread.currentThread().getId() + ", 返回值：" + res);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("耗时3s, 任务3线程终于结束");
            return "哈哈哈, 我又来返回了：===> " + res;
        }, executor);

        System.out.println("任务3最终返回结果：" + future03.get());

        System.out.println("main method... end...");
    }
}
