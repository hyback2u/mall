package com.wxl.mall.search.service;

import com.wxl.mall.search.vo.SearchParam;
import com.wxl.mall.search.vo.SearchResult;

/**
 * @author wangxl
 * @since 2022/6/14 21:18
 */
public interface MallSearchService {

    /**
     * 根据传递过来的页面的查询参数, 去ES中检索商品
     *
     * @param param 检索的所有参数
     * @return 返回检索的结果 SearchResult, 包含页面所有需要的信息
     */
    SearchResult search(SearchParam param);
}
