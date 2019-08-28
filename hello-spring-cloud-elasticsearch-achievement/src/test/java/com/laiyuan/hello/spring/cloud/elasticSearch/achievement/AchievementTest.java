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
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.beans.Transient;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
            public void onResponse(DeleteResponse  deleteResponse) {
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
     * @throws InterruptedException
     */
    @Test
    public void update() throws InterruptedException{
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
     * @throws InterruptedException
     */
    @Test
    public void bulk() throws InterruptedException{
        RestHighLevelClient client = AchievementTest.initClient();

        BulkRequest request = new BulkRequest();
        //把要处理的请求用方法add添加
        request.add(new DeleteRequest("test", "2"));

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("reason", "daily update");
        request.add(new UpdateRequest("test", "1")
                .doc(jsonMap));

        Map<String, Object> jsonMap2 = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        request.add(new IndexRequest("test").id("3")
                .source(jsonMap2));


        //同步
        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                //具体返回和单独操作差不多，就不细写了
                switch (bulkItemResponse.getOpType()) {
                    case INDEX:
                    case CREATE:
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        System.out.println("添加操作："+indexResponse.getResult().name());
                        break;
                    case UPDATE:
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        System.out.println("更新操作："+updateResponse.getResult().name());
                        break;
                    case DELETE:
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        System.out.println("删除操作："+deleteResponse.getResult().name());
                }
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
     * 通过ik分词器搜索
     */
    @Test
    public void search() throws InterruptedException{
        // TODO: 2019/8/28 明天写 
    }

}
