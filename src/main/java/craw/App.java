package craw;

import io.github.biezhi.anima.Anima;

public class App {

    /**
     * 启动项目
     * @param args 启动参数，默认不使用，使用也没效果
     */
    public static void main(String[] args) {

        //打开数据库连接
        Anima.open("jdbc:mysql://127.0.0.1:3306/xxx", "root", "123456");

        Craw craw=new Craw();

        craw.start(10);//填入需要爬取的页数，每页30个电影

    }


}
