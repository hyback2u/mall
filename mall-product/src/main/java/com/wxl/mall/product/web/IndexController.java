package com.wxl.mall.product.web;

import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryService;
import com.wxl.mall.product.vo.Catelog2VO;
import org.redisson.api.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 首页页面跳转Controller
 *
 * @author wangxl
 * @since 2022/6/6 20:22
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 车库停车, 3车位
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        // 获取一个信号, 获取一个值, 占一个车位
        park.acquire();

        return "车子停了一个...";
    }


    @GetMapping("/cargo")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        // 释放一个车位
        park.release();

        return "车走了一个...";
    }


    /**
     * Redisson闭锁
     * 场景:放假锁门, 一共5个班级, 5个班全部走完, 可以锁大门
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        // 等待闭锁都完成
        door.await();

        return "锁大门, 放假了放假了...";
    }

    @GetMapping("/go/{id}")
    @ResponseBody
    public String go(@PathVariable("id") Long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        // 计数减一
        door.countDown();

        return id + " 班的人走了...";
    }


    /**
     * Redisson读写锁测试
     * 写
     *
     * ---> 读写锁, 保证一定能读到最新数据; 修改期间, 写锁是一个排他锁(互斥锁), 读锁是一个共享锁;
     *      写锁没释放, 读就必须等待
     *
     * @see IndexController#readValue()
     */
    @GetMapping(value = "/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        String str = "";

        RLock rLock = lock.writeLock();

        try {
            // 改数据加写锁, 读数据加读锁
            rLock.lock();
            System.out.println("写锁加锁成功" + Thread.currentThread().getId());
            str = UUID.randomUUID().toString();
            Thread.sleep(30000);
            stringRedisTemplate.opsForValue().set("writeValue", str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            rLock.unlock();
            System.out.println("写锁释放" + Thread.currentThread().getId());
        }

        return str;
    }

    /**
     * Redisson读写锁测试
     * 读
     *
     * @see IndexController#writeValue()
     */
    @GetMapping(value = "/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        String str = "";

        // 加读锁
        RLock readLock = lock.readLock();
        readLock.lock();
        System.out.println("读锁加锁成功" + Thread.currentThread().getId());

        try {
            str = stringRedisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println("读锁释放" + Thread.currentThread().getId());
        }

        return str;
    }


    /**
     * 压测简单服务
     *
     * @return 压力测试
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        // 1、获取一把锁, 只要锁名字一样, 就是同一把锁
        RLock rLock = redissonClient.getLock("my-lock");

        // 2、加锁(这个lock, 是阻塞式等待), 默认加的锁是30s
        rLock.lock();
        // 1)、锁的自动续期, 如果业务超长, 运行期间自动给锁续上新的30s ---> 不用担心业务时间长, 锁自动过期被删掉。
        // 2)、加锁的业务只要运行完成, 就不会给当前锁续机, 即使不手动解锁, 锁默认在30s之后自动删除。

        try {
            System.out.println("加锁成功, 执行业务... " + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 3、解锁 ---> 假设解决代码没有运行(宕机了), redisson会不会出现死锁？
            rLock.unlock();
            System.out.println("释放锁... " + Thread.currentThread().getId());
        }

        return "hello";
    }


    /**
     * 首页跳转, 渲染一级分类数据
     *
     * @return classpath:/templates/xx.html
     */
    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {

        // 1、查出所有的一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntityList);

        return "index";
    }


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     *
     * @return data
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatalogJson() {
//        return categoryService.getCatalogJson();
//        return categoryService.getCatalogJsonPlus();
        return categoryService.getCatalogJsonPlusPro();
    }
}
