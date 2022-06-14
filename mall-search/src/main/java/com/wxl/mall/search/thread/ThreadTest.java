package com.wxl.mall.search.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步&多线程 复习
 *
 * @author wangxl
 * @since 2022/6/14 22:28
 */
public class ThreadTest {
    // 当前系统中, 池只有一两个, 每个异步任务, 提交给线程池让他自己去执行就行
    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");

        // 1、继承Thread
//        Thread01 thread01 = new Thread01();
//        thread01.start();

        // 2、实现Runnable接口
//        Runnable01 runnable01 = new Runnable01();
//        new Thread(runnable01).start();

        // 3、实现Callable接口 + FeatureTask
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
//
//        // 等待整个线程执行完成, 获取返回结果 --> 阻塞等待整个线程执行完成, 获取返回结果
//        Integer integer = futureTask.get();
//        System.out.println("main...end... 运行结果：" + integer);

        // 4、线程池: 给线程池直接提交任务
        // 当前系统中, 池只有一两个, 每个异步任务, 提交给线程池让他自己去执行就行
        service.execute(new Runnable01());



        System.out.println("main...end...");
    }


    /**
     * 1、继承Thread
     */
    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    /**
     * 2、实现Runnable接口
     */
    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    /**
     * 3、实现Callable接口 + FeatureTask(可以拿到结果, 可以处理异常)
     */
    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }
    }

}
