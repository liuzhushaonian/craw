package craw;

import io.github.biezhi.anima.Anima;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工作流程：
 * 通过api爬取2019的电影信息
 * 解析获取到的json数据并存入数据库内
 */
public class Craw {

    private ThreadPoolExecutor poolExecutor;
    private int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private int CORE_POOL_SIZE = CORE_COUNT + 1;
    private int CORE_POOL_MAX_SIZE = CORE_COUNT * 2 + 1;
    private int KEEP_ALIVE = 10;



    public Craw() {

        BlockingQueue<Runnable> runnableBlockingQueue = new LinkedBlockingQueue<Runnable>();
        poolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_MAX_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, runnableBlockingQueue);

    }

    /**
     * 设置爬取的页数，默认爬2019年的电影
     * @param page 页数
     */
    public void start(int page){


        prepareData(page);

    }


    /**
     * 开启线程池爬取数据
     * @param page 页数
     */
    private void prepareData(int page){

        for (int i=1;i<=page;i++){

            final int finalI = i;
            Runnable runnable=new Runnable() {
                public void run() {

                    String json=NetUtil.getJson(finalI);

                    parseAndSave(json);

                }
            };

            poolExecutor.execute(runnable);


        }


    }


    /**
     * 解析json
     * 封装后保存到数据库内
     */
    private void parseAndSave(String json){

        JSONObject infos=new JSONObject(json);

        if (infos.has("movieIntegrateList")){//存在名为movieIntegrateList的列表

            JSONArray list=infos.getJSONArray("movieIntegrateList");

            for (int i=0;i<list.length();i++){

                JSONObject movie=list.getJSONObject(i);//电影信息

                //封装

                MovieInfo movieInfo=new MovieInfo();

                if (movie.has("movieId")){

                    int id=movie.getInt("movieId");

                    if (isExits(id)){

                        System.out.println("已存在");

                        return;
                    }

                    movieInfo.setId(id);

                }

                if (movie.has("titleCn")){

                    movieInfo.setName(movie.getString("titleCn"));

                }

                if (movie.has("ratingFinal")){

                    movieInfo.setScore(movie.getString("ratingFinal"));

                }

                String url="http://movie.mtime.com/";


                movieInfo.setUrl(url+movieInfo.getId());

                movieInfo.save();//封装完成，保存到数据库内


            }
        }
    }

    private boolean isExits(int id){

        return Anima.select("id").from(MovieInfo.class).where("id",id).one()!=null;
    }
}
