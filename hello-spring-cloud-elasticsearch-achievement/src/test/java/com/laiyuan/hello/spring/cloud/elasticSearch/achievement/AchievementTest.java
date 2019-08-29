package com.laiyuan.hello.spring.cloud.elasticSearch.achievement;


import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.junit.Test;

import java.beans.Transient;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author by laiyuan
 * @Date 2019/8/28 13:38
 * @Description: TODO
 * @Version 1.0
 */
public class AchievementTest extends AchievementApplicationTests {
    private static final String HOSTNAME = "192.168.0.188";

    public static RestHighLevelClient initClient() {
        //初始化连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOSTNAME, 9200, "http"),
                        new HttpHost(HOSTNAME, 9201, "http")));
        return client;
    }

    /**
     * 添加数据
     */
    @Test
    public void index() throws InterruptedException {
        RestHighLevelClient client = AchievementTest.initClient();

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("test")
                .id("2").source(jsonMap);

//        同步
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("添加一条数据");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("更新一条数据");
            }
        } catch (IOException e) {
            System.out.println("添加失败");
            e.printStackTrace();
        }

//        异步
//        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
//            @Override
//            public void onResponse(IndexResponse indexResponse) {
//                String index = indexResponse.getIndex();
//                String id = indexResponse.getId();
//                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
//                    System.out.println("添加一条数据");
//                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//                    System.out.println("更新一条数据");
//                }
//                //涉及到分片存储的知识
//                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
//                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
//
//                }
//                if (shardInfo.getFailed() > 0) {
//                    for (ReplicationResponse.ShardInfo.Failure failure :
//                            shardInfo.getFailures()) {
//                        String reason = failure.reason();
//                        System.out.println(reason);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                System.out.println("添加失败");
//                e.printStackTrace();
//            }
//        };
//        client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);
//
//        //异步需要暂停下线程等数据返回
//        Thread.sleep(2000);

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询
     */
    @Test
    public void get() throws InterruptedException {
        RestHighLevelClient client = AchievementTest.initClient();

        GetRequest request = new GetRequest(
                "test1",
                "1");


        //同步
//        try {
//            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
//            String index = getResponse.getIndex();
//            String id = getResponse.getId();
//            if (getResponse.isExists()) {
//                long version = getResponse.getVersion();
//                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
//                String about = sourceAsMap.get("about").toString();
//                System.out.println(about);
//            } else {
//                System.out.println("数据不存在");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //异步
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse getResponse) {
                long version = getResponse.getVersion();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                String about = sourceAsMap.get("about").toString();
                System.out.println(about);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("数据不存在");
            }
        };
        client.getAsync(request, RequestOptions.DEFAULT, listener);
        //异步需要暂停下线程等数据返回
        Thread.sleep(2000);

        //关闭连接
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("关闭失败");
            e.printStackTrace();
        }


    }

    /**
     * 删除一条数据
     *
     * @throws InterruptedException
     */
    @Test
    public void delete() throws InterruptedException {
        RestHighLevelClient client = AchievementTest.initClient();

        DeleteRequest request = new DeleteRequest(
                "test",
                "2");

        //同步
//        try {
//            DeleteResponse deleteResponse = client.delete(
//                    request, RequestOptions.DEFAULT);
//            String index = deleteResponse.getIndex();
//            String id = deleteResponse.getId();
//            long version = deleteResponse.getVersion();
//            System.out.println(deleteResponse.getResult().name());//not_found表示没找到这条数据，deleted表示删除成功
//            //分片判断
//            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
//            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
//
//            }
//            if (shardInfo.getFailed() > 0) {
//                for (ReplicationResponse.ShardInfo.Failure failure :
//                        shardInfo.getFailures()) {
//                    String reason = failure.reason();
//                    System.out.println(reason);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //异步
        ActionListener<DeleteResponse> listener = new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {
                System.out.println(deleteResponse.getResult().name());//not_found表示没找到这条数据，deleted表示删除成功
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };
        client.deleteAsync(request, RequestOptions.DEFAULT, listener);
        //异步需要暂停下线程等数据返回
        Thread.sleep(2000);


        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * update
     *
     * @throws InterruptedException
     */
    @Test
    public void update() throws InterruptedException {
        RestHighLevelClient client = AchievementTest.initClient();

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("reason", "daily update");
        UpdateRequest request = new UpdateRequest("test", "1")
                .doc(jsonMap);

        //同步
//        try {
//            UpdateResponse updateResponse = client.update(
//                    request, RequestOptions.DEFAULT);
//            String index = updateResponse.getIndex();
//            String id = updateResponse.getId();
//            long version = updateResponse.getVersion();
//            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
//                System.out.println("CREATED");
//            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//                System.out.println("UPDATED");
//            } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
//                System.out.println("DELETED");
//            } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
//                System.out.println("NOOP");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //异步
        ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {
                if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println("CREATED");
                } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("UPDATED");
                } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
                    System.out.println("DELETED");
                } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
                    System.out.println("NOOP");
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };
        client.updateAsync(request, RequestOptions.DEFAULT, listener);

        Thread.sleep(2000);

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 批量处理
     *
     * @throws InterruptedException
     */
    @Test
    public void bulk() throws InterruptedException {
        RestHighLevelClient client = AchievementTest.initClient();

        BulkRequest request = new BulkRequest();
        //把要处理的请求用方法add添加
//        request.add(new DeleteRequest("test", "2"));
//
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("updated", new Date());
//        jsonMap.put("reason", "daily update");
//        request.add(new UpdateRequest("test", "1")
//                .doc(jsonMap));

        List<String> news = new ArrayList<>();
        news.add("为应对今年第12号台风“杨柳”，国家防总当日启动防台风Ⅳ级应急响应，并派出3个工作组分赴海南、广东、广西，指导协助地方做好有关工作。");
        news.add("网络安全宣传周由中央宣传部、中央网信办、教育部、工业和信息化部、公安部、中国人民银行、国家广播电视总局、全国总工会、共青团中央、全国妇联等部门联合举办。");
        news.add("近日，中央纪委国家监委机关牵头、会同15个中央国家机关制定了《在“不忘初心、牢记使命”主题教育中专项整治漠视侵害群众利益问题的实施方案》。");
        news.add("“不忘初心、牢记使命”主题教育开展以来，自然资源部、生态环境部、水利部、农业农村部结合自身工作实际，奔着问题去、盯着痼疾改，取得阶段性成效。");
        news.add("国家对网络强国建设作出总体部署，对数字经济发展提出明确要求，有关互联网发展及数字化、网络化、智能化建设正在积极有序推进。同时要看到，文化和科技深度融合仍面临许多新的挑战。");
        news.add("政协第十三届全国委员会常务委员会第八次会议27日下午举行全体会议，中共中央政治局常委、全国政协主席汪洋出席。16位全国政协常委围绕“办好人民满意的教育”作大会发言。");
        news.add("今年是中华人民共和国成立70周年，党中央决定，首次开展国家勋章和国家荣誉称号集中评选颁授，隆重表彰一批为新中国建设和发展作出杰出贡献的功勋模范人物。");
        news.add("新修订的药品管理法在总则中明确规定，国家鼓励研究和创制新药，增加和完善了10多个条款，增加了多项制度举措。");
        news.add("从新学期开始，普通高中思想政治、语文、历史三科统编教材将在北京、天津、辽宁、上海、山东、海南6个省（市）率先使用，其他省份也将陆续全面推开。");
        news.add("截至2018年底，中国60岁及以上老年人口约2.49亿，65岁及以上人口约1.67亿。但超过1.8亿老年人患有慢性病，患有一种及以上慢性病的比例高达75%，失能、部分失能老年人约4000万。");

        int id = 1;
        for (String str : news) {
            Map<String, Object> jsonMap2 = new HashMap<>();
            jsonMap2.put("newStr",str);

            request.add(new IndexRequest("news").id(String.valueOf(id))
                    .source(jsonMap2));
            id++;
        }

        //同步
        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            int count = 0;
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                count++;
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();
                //具体返回和单独操作差不多，就不细写了
                switch (bulkItemResponse.getOpType()) {
                    case INDEX:
                    case CREATE:
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        System.out.println("添加操作：" + indexResponse.getResult().name());
                        break;
                    case UPDATE:
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        System.out.println("更新操作：" + updateResponse.getResult().name());
                        break;
                    case DELETE:
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        System.out.println("删除操作：" + deleteResponse.getResult().name());
                }
                System.out.println("共执行"+count+"项操作");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //异步和之前的差不多，参考：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.3/java-rest-high-document-bulk.html

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 搜索
     */
    @Test
    public void search() {
        RestHighLevelClient client = AchievementTest.initClient();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("news");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //搜索内容
        //构建查询语句
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("newStr", "大海");
        sourceBuilder.query(matchQueryBuilder);

        //分页
        sourceBuilder.from(0);
        sourceBuilder.size(10);

        //超时
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
//
//        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("newStr");
//        highlightTitle.highlighterType("unified");
//        highlightBuilder.field(highlightTitle);

        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("newStr");
        highlightBuilder.field(highlightUser);

        sourceBuilder.highlighter(highlightBuilder);


        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();
            for (SearchHit searchHit:searchResponse.getHits()){

                //常规返回
                System.out.println("score:" + searchHit.getScore());
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                String newStr = sourceAsMap.get("newStr").toString();
                System.out.println("普通返回内容:"+newStr);

                //高亮返回（<em></em>标签内的是高亮内容）
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                HighlightField highlight = highlightFields.get("newStr");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                System.out.println("高亮返回内容:"+fragmentString);

            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 联想词
     */
    @Test
    public void suggest(){
        RestHighLevelClient client = AchievementTest.initClient();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("news");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //联想内容

        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("newStr").text("台");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
        sourceBuilder.suggest(suggestBuilder);

        //分页
        sourceBuilder.from(0);
        sourceBuilder.size(10);

        //超时
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
            System.out.println("联想词：");
            for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
                for (TermSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    System.out.println(suggestText);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
