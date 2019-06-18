package craw;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;

@Data
@Table(name = "t_info")
public class MovieInfo extends Model {

    private int id;//唯一编号
    private String name;//名字
    private String score;//得分
    private String url;//电影详情页面


}
