package cn.spider.framework.code.agent.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class ShellUtil {
    public static Map<String,String> runShell(String shell, String shellParam){
        Map<String,String> runResultInfo = new HashMap<>();
        try {
            // 构建脚本的完整路径
            String scriptPath = Paths.get("conf", "shell", shell).toAbsolutePath().toString();

            // 调用Shell脚本
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", scriptPath + " " + shellParam});

            // 等待脚本执行完成
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Project generated successfully.");
            } else {
                System.err.println("Failed to generate project.");
            }

            // 读取标准输出和错误输出
            String stdout = readStream(process.getInputStream(), "STDOUT");
            String stderr = readStream(process.getErrorStream(), "STDERR");
            runResultInfo.put("stdout",stdout);
            runResultInfo.put("stderr",stderr);
            runResultInfo.put("code","200");
            log.info("执行错误的信为 {}",stderr);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            runResultInfo.put("code","500");
        }
        return runResultInfo;
    }

    private static String readStream(java.io.InputStream stream, String type) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder lineInfoBuild = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(type + ": " + line); // 打印到控制台
            // 你可以在这里添加更多的日志处理逻辑
            lineInfoBuild.append(type + ": " + line).append("/n");
        }
        reader.close();
        return lineInfoBuild.toString();
    }
}
