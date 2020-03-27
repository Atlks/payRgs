package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.google.common.collect.Maps;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor

@BackstageController
public class mybatisT {

    @Autowired
    SqlSession session;
    @Autowired
    SqlSessionFactory SqlSessionFactory1;


    @Autowired
    SqlSessionTemplate sqlSessionTemplate;


    @GetMapping("/mybatisTest3")
    public String get(){
        Map m3= Maps.newHashMap();
        m3.put("user_id","216654713456709632");
        m3.put("user_id","195275261137129472");
        List<Map> li=    sqlSessionTemplate.selectList("recharge_count",m3);
        // List<Map> li=session.selectList("recharge_count",m3);
        System.out.println(li);
        return "ok88";
    }



}
