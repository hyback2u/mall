package com.wxl.mall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangxl
 * @since 2022/6/16 21:27
 */
public class CombinationTaskTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        System.out.println("main..... start");

        CompletableFuture<String> imgFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        }, executor);

        CompletableFuture<String> attrFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "雪山白 8 + 256G";
        }, executor);

        CompletableFuture<String> descFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的介绍信息");
            return "HUAWEI";
        }, executor);

//        CompletableFuture<Void> allOf = CompletableFuture.allOf(imgFuture, attrFuture, descFuture);
//        // 等待所有结果完成
//        allOf.get();

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(imgFuture, attrFuture, descFuture);
        System.out.println(anyOf.get());

        System.out.println("main..... end");
    }
}
