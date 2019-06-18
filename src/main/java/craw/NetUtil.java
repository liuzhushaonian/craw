package craw;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

/**
 * 联网获取json数据
 */
public class NetUtil {

    public static String getJson(int page) {

        String url = "http://video.mtime.com/api/videoSearch/getFilterData?h=movie&y=全部年代&p=3&s=1&i="+page+"&c=30";

        String json = "";

        System.out.println("正在获取--->>"+url);

        try {

            Request.Builder builder1 = new Request.Builder().url(url)
                    .method("GET", null);

            Request request = builder1.build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS);

            OkHttpClient okHttpClient = builder.build();

            Call call = okHttpClient.newCall(request);

            json = call.execute().body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


}
