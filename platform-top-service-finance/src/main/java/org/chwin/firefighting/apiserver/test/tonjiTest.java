package org.chwin.firefighting.apiserver.test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import lombok.SneakyThrows;
import org.chwin.firefighting.apiserver.data.MybatisUtil4game;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class tonjiTest {

    @Test     @SneakyThrows
    public  void test_balanceHistoryList(){

//        QueryParam p=new QueryParam();
//        p.setDate("2019-10-21");p.setPlatformId(195275261137129472L);
        //p.setSelectid();
//        List li =MybatisUtil4game.getSqlSession().selectList("balanceHistoryList", p);
//        System.out.println(li);
    }
    @SneakyThrows
    public static void main(String[] args) {

        System.out.println(new Date().getHours());
//if("a"=="a")
//        throw new RuntimeException("aaa");

//                Map m = Maps.newLinkedHashMap();
//                m.put("date", DateTimeFormatter.ofPattern("yyyy-MM-dd").format( LocalDateTime.now()));



   //     balanceHistoryList

       // MybatisUtil4game.getSqlSession().update("balanceHistoryListInsert", rec);

try{
    Map m = Maps.newLinkedHashMap();
    m.put("key1","v1");
   List li = MybatisUtil4game.selectList("balanceHistoryListCurrentGroupbyPlatform_id", m);
    li.forEach(new Consumer() {
        @Override
        @SneakyThrows
        public void accept(Object rec) {

            try {
                MybatisUtil4game.getSqlSession().update("balanceHistoryListInsert", rec);
            } catch (Exception e) {
                //Duplicate entry '195275261137129472-2019-10-21' for key '
                if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("plat_date_uniq")) {
                } else
                    e.printStackTrace();
            }

        }
    });
}catch(Exception e)
{

    System.out.println(JSON.toJSONString(e,true));
}



    }
}
