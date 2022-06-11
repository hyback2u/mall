package com.wxl.mall.product.web;

import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryService;
import com.wxl.mall.product.vo.Catelog2VO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
