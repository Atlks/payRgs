package org.chwin.firefighting.apiserver.data;

import com.google.common.reflect.Invokable;
import lombok.SneakyThrows;


import java.lang.reflect.Method;
import java.util.*;

// org.chwin.firefighting.apiserver.data.anothOp

public class anothOp {
@SneakyThrows
    public static void main(String[] args) {

    Timer tmr=new Timer();
    tmr.schedule(new TimerTask() {


        @Override
        public void run() {
            System.out.println("event run..."+new Date());
            try{
                List<Map> li = MybatisUtil4game.selectList("eventQuery", null);
                if(li.size()==0)
                    return;
                Map  m=li.get(0);

                // 到SocketStatistics类中找到这个方法

                Class cls=Class.forName(m.get("class").toString());
                Method method =cls.getDeclaredMethod(m.get("method").toString());

                // Guava的工具类通过反射，动态调用该方法
                Invokable<anothOp, Object> invokable = (Invokable<anothOp, Object>) Invokable
                        .from(method);
                invokable.invoke(null);

                List<Map> li2=  MybatisUtil4game.selectList(m.get("nextStatSelectId").toString(), null);
                System.out.println(li2);
            }catch(Exception e)
            {
                e.printStackTrace();
            }



        }
    },0,5000);


    }
    public static void meth1( ) {
        System.out.println("---anothOp.meth1()");
    }

}
