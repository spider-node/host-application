package cn.spider.framework.code.agent.spider;

import cn.spider.framework.code.agent.spider.data.RegisterAreaFunctionParam;
import cn.spider.framework.code.agent.spider.data.UploadFileResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpiderClient {
    private String requestFileUrl;

    private String registerAreaFunctionUrl;

    private OkHttpClient client;

    public SpiderClient(String spiderHost, int spiderPort, String requestFileUrl, String registerAreaFunctionUrl, OkHttpClient client) {
        String httpPrefix = "http://" + spiderHost + ":" + spiderPort + "/";
        this.requestFileUrl = httpPrefix + requestFileUrl;
        this.registerAreaFunctionUrl = httpPrefix + registerAreaFunctionUrl;
        this.client = client;
    }

    public String uploadFile(Path jarFile) throws IOException {
        // 创建RequestBody对象
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/octet-stream"),
                Files.readAllBytes(jarFile)
        );

        // 创建Request对象
        Request request = new Request.Builder()
                .url(requestFileUrl)
                .post(requestBody)
                .build();

        // 发送请求
        Response response = client.newCall(request).execute();

        // 处理响应
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        String uploadFileUrl = response.body().string();
        UploadFileResult patch = JSONObject.parseObject(uploadFileUrl, UploadFileResult.class);
        return patch.getPatch();
    }

    public void registerAreaFunction(RegisterAreaFunctionParam param) {
        param.setServiceTaskType("NORMAL");
        param.setStatus("START");
        try {
            sendJsonData(JSONObject.parseObject(JSON.toJSONString(param)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendJsonData(JSONObject json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // 将JSON对象转换为字符串
        String jsonData = json.toString();

        // 创建RequestBody对象
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonData
        );

        // 创建Request对象
        Request request = new Request.Builder()
                .url(this.registerAreaFunctionUrl)
                .post(requestBody)
                .build();

        // 发送请求
        Response response = client.newCall(request).execute();

        // 处理响应
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        System.out.println("Response body: " + response.body().string());
    }


}
