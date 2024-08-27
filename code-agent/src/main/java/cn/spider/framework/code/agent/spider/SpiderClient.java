package cn.spider.framework.code.agent.spider;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.function.entity.AreaDomainFunctionInfo;
import cn.spider.framework.code.agent.spider.data.DeployPluginParam;
import cn.spider.framework.code.agent.spider.data.DeployPluginResult;
import cn.spider.framework.code.agent.spider.data.RegisterAreaFunctionParam;
import cn.spider.framework.code.agent.spider.data.UploadFileResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpiderClient {
    private String requestFileUrl;

    private String deployUrl;

    private OkHttpClient client;

    public SpiderClient(String spiderHost, int spiderPort, String requestFileUrl, OkHttpClient client, String deployUrl) {
        String httpPrefix = "http://" + spiderHost + ":" + spiderPort + "/";
        this.requestFileUrl = httpPrefix + requestFileUrl;
        this.deployUrl = httpPrefix + deployUrl;
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
                .url(this.requestFileUrl)
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

    /**
     * 部署插件
     * @param param 部署插件需要的信息
     * @return 部署的结果
     */
    public DeployPluginResult deployPluginToApplication(DeployPluginParam param) {
        return sendJsonData(JSON.toJSONString(param),this.deployUrl, DeployPluginResult.class);
    }

    public <R> R sendJsonData(String param,String url,Class<R> resutClass) {
        OkHttpClient client = new OkHttpClient();

        // 创建RequestBody对象
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                param
        );

        // 创建Request对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okio.Buffer buffer = null;
        Charset charset = null;

        try {
            // 发送请求
            Response response = client.newCall(request).execute();

            // 处理响应
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            okio.BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            buffer = source.buffer();

            charset = StandardCharsets.UTF_8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        JSONObject response = JSONObject.parseObject(buffer.clone().readString(charset));

        return response.getObject("data",resutClass);
    }
}
