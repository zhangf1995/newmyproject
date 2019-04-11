package com.myproject.es.esQueryUtils;

import com.myproject.es.esDomain.EsTest;
import com.myproject.es.esService.EsTestService;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        builder.must(QueryBuilders.fuzzyQuery("title", "中"));

    /*    //设置要查询博客的标题中含有关键字
        builder.must(new QueryStringQueryBuilder("man").field("springdemo"));*/

        //排序
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

    public void test(){
        //目标：搜索写博客写得最多的用户（一个博客对应一个用户），通过搜索博客中的用户名的频次来达到想要的结果
        //首先新建一个用于存储数据的集合
        List<String> ueserNameList=new ArrayList<>();
        //1.创建查询条件，也就是QueryBuild
        QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();//设置查询所有，相当于不设置查询条件
        //2.构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //2.0 设置QueryBuilder
        nativeSearchQueryBuilder.withQuery(matchAllQuery);
        //2.1设置搜索类型，默认值就是QUERY_THEN_FETCH，参考https://blog.csdn.net/wulex/article/details/71081042
        nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);//指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
        //2.2指定索引库和文档类型
        nativeSearchQueryBuilder.withIndices("test_index").withTypes("test");//指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
        //2.3重点来了！！！指定聚合函数,本例中以某个字段分组聚合为例（可根据你自己的聚合查询需求设置）
        //该聚合函数解释：计算该字段(假设为username)在所有文档中的出现频次，并按照降序排名（常用于某个字段的热度排名）
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("给聚合查询取的名").field("username").order(Terms.Order.count(false));
        nativeSearchQueryBuilder.addAggregation(termsAggregation);
        //2.4构建查询对象
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //3.执行查询
        //3.1方法1,通过reporitory执行查询,获得有Page包装了的结果集
        Page<EsTest> search = esTestService.search(nativeSearchQuery);
        List<EsTest> content = search.getContent();
        for (EsTest esTest : content) {
            ueserNameList.add(esTest.getTitle());
        }
        //获得对应的文档之后我就可以获得该文档的作者，那么就可以查出最热门用户了
        //3.2方法2,通过elasticSearch模板elasticsearchTemplate.queryForList方法查询
        //List<EsTest> queryForList = elasticsearchTemplate.queryForList(nativeSearchQuery, EsTest.class);
        //3.3方法3,通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
/*        Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        //转换成map集合
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
        StringTerms stringTerms = (StringTerms) aggregationMap.get("给聚合查询取的名");
        //获得所有的桶
        List<Bucket> buckets = stringTerms.getBuckets();
        //将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        Iterator<Bucket> iterator = buckets.iterator();

        while(iterator.hasNext()) {
            //bucket桶也是一个map对象，我们取它的key值就可以了
            String username = iterator.next().getKeyAsString();//或者bucket.getKey().toString();
            //根据username去结果中查询即可对应的文档，添加存储数据的集合
            ueserNameList.add(username);
        }*/
        //最后根据ueserNameList搜索对应的结果集
/*
        List<User> listUsersByUsernames = userService.listUsersByUsernames(ueserNameList);
*/
    }
}
