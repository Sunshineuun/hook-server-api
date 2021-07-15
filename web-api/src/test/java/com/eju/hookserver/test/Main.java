package com.eju.hookserver.test;


import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Created by zcm on 2021/2/25.
 * @version v0.1.0
 * @see <font color="#0000FF">hook-server-api</font>
 */
public class Main {


    public static void main(String[] args) throws Exception {
        //https://ess.leju.com/house/photo/43-1-dr9K8llO0w1uklmD8PYBOTT57AO8lpHJy5KOx0v16QbZ0DotqeJnItLiEuG8GeKGSW0rKT17bQot4lip.jpeg
        InputStream inputStream = httpClient("https://ess.leju.com/house/photo/43-1-dr9K8llO0w1uklmD8PYBOTT57AO8lpHJy5KOx0v16QbZ0DotqeJnItLiEuG8GeKGSW0rKT17bQot4lip.jpeg");
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1 ) {
            baos1.write(buffer, 0, len);
        }
        baos1.flush();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        System.out.println(baos1.size()/1024.0f/1024.0f);
        Thumbnails.of(new ByteArrayInputStream(baos1.toByteArray()))
                .scale(1f)
                .outputQuality(0.5f)
                .outputFormat("jpg")
                .toOutputStream(baos2);
        baos1.reset();
        baos1.write(baos2.toByteArray());
        FileOutputStream fileOutputStream = new FileOutputStream("d://tmp/a.jpg");
        fileOutputStream.write(baos1.toByteArray());
        System.out.println(baos1.size()/1024.0f/1024.0f);
    }

    public static InputStream httpClient(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            CloseableHttpResponse response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity().getContent();
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
