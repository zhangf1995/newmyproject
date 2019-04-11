package com.myproject.es.esQueryUtils;

import com.myproject.es.esDomain.EsTest;
import com.myproject.es.esService.EsTestService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: myproject
 * @description: 测试查询
 * @author: zf
 * @create: 2019-04-11 11:14
 **/
@Service
public class EsTestQuery {

    @Autowired
    private EsTestService esTestService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<EsTest> elasticSerchTest() {
        //1.创建QueryBuilder(即设置查询条件)这儿创建的是组合查询(也叫多条件查询),后面会介绍更多的查询方法
	 /*组合查询BoolQueryBuilder
	     * must(QueryBuilders)   :AND
	     * mustNot(QueryBuilders):NOT
	     * should:               :OR
	 */
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not

        //设置模糊搜索（只能查tex类型和关键字）
        builder.must(QueryBuilders.fuzzyQuery("title", "张"));

    /*    //设置要查询博客的标题中含有关键字
        builder.must(new QueryStringQueryBuilder("man").field("springdemo"));*/

        //p排序
        FieldSortBuilder sort = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        //分页
        PageRequest page = PageRequest.of(0,10);

        //2.构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //指定索引
        //nativeSearchQueryBuilder.withIndices("");
        //指定type
        //nativeSearchQueryBuilder.withTypes("");
        //将搜索条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将分页设置到构建中
        nativeSearchQueryBuilder.withPageable(page);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //3.执行方法1
        Page<EsTest> page1 = esTestService.search(query);
        //执行方法2：注意，这儿执行的时候还有个方法那就是使用elasticsearchTemplate
        //执行方法2的时候需要加上注解
        //@Autowired
        //private ElasticsearchTemplate elasticsearchTemplate;
        //List<EsTest> blogList = elasticsearchTemplate.queryForList(query, EsTest.class);

        //4.获取总条数(用于前端分页)
        long total = page1.getTotalElements();

        //5.获取查询到的数据内容（返回给前端）
        List<EsTest> content = page1.getContent();

        return content;
    }
}
