package org.chwin.firefighting.apiserver.test;

import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.tool.api.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.chwin.firefighting.apiserver.net.RequestUtil;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_BANDKBD)
@Api(value = "绑定银行卡", tags = "绑定银行卡")
@BackstageController
public class controlerTest {

    RestTemplate RestTemplate1;
    public static void main(String[] args) {
        //RestTemplate1.
        System.out.println("--f");
    }

    @Autowired
    SqlSessionTemplate SqlSessionTemplate1;

    @Test
    @RequestMapping("/query")
    public Object query(HttpServletRequest req) {
        return R.data(SqlSessionTemplate1.selectList(req.getParameter("selectId"), RequestUtil.getMap(req)));
    }
}
