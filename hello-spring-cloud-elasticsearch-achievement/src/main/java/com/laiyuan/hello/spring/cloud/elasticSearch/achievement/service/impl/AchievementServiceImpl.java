package com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service.impl;

import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.service.AchievementService;
import com.laiyuan.hello.spring.cloud.elasticSearch.achievement.vo.ProjectVO;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author by laiyuan
 * @Date 2019/9/4 14:17
 * @Description: TODO
 * @Version 1.0
 */
@Service
@RefreshScope
public class AchievementServiceImpl implements AchievementService {
    @Value("${elasticsearch.hostname}")
    private String hostname;

    private RestHighLevelClient initClient() {
        //初始化连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, 9200, "http"),
                        new HttpHost(hostname, 9201, "http")));
        return client;
    }

    @Override
    public List<String> suggest(String text) {
        RestHighLevelClient client = this.initClient();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("tech_suggest");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //联想内容
        SuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("keyword").text(text);
        //返回10个结果
        completionSuggestionBuilder.size(7);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_user", completionSuggestionBuilder);
        sourceBuilder.suggest(suggestBuilder);

        //超时
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        List<String> list = new ArrayList<>();
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Suggest suggest = searchResponse.getSuggest();

            CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest_user");
            for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
                for (CompletionSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    list.add(suggestText);
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

        return list;
    }

    @Override
    public Map<String, Object> search(String text, int index, int pageSize) {
        RestHighLevelClient client = this.initClient();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("project_demo");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //搜索内容
        //构建查询语句
        MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(text, "name", "abstract_ch");
        //对不同字段进行权重处理
        multiMatchQueryBuilder.field("name", 1.0f);
        multiMatchQueryBuilder.field("abstract_ch", 5.0f);
        sourceBuilder.query(multiMatchQueryBuilder);

        //分页
        sourceBuilder.from(index);
        sourceBuilder.size(pageSize);

        //超时
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        Map<String, Object> map = new HashMap<>(2);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();
            List<ProjectVO> projects = new ArrayList<>();
            SearchHits searchHits=searchResponse.getHits();
            long projectTotal = searchHits.getTotalHits().value;;
            for (SearchHit searchHit : searchHits) {
                //常规返回
                System.out.println("score:" + searchHit.getScore());
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                String name = sourceAsMap.get("name").toString();
                String unit = sourceAsMap.get("unit").toString();
                String year = sourceAsMap.get("year").toString();
                String member = sourceAsMap.get("member").toString();
                String abstractCh = sourceAsMap.get("abstract_ch").toString();
                ProjectVO projectVO=new ProjectVO(name, unit, member, year, abstractCh);
                projects.add(projectVO);
            };

            map.put("projects", projects);
            map.put("projectTotal", projectTotal);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
