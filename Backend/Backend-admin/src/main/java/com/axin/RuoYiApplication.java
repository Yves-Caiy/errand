package com.axin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author axin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args) {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println(
                "         (♥◠‿◠)ﾉﾞ  服务启动成功   ლ(´ڡ`ლ)ﾞ            \n" +
                "       ＡＡ　　　　ＸＸ　　ＸＸ　　ＩＩＩＩＩＩ　　  ＮＮＮＮ   \n" +
                "    ＡＡ　　ＡＡ　　ＸＸ　　ＸＸ　　　　ＩＩ　　　　ＮＮ　　ＮＮ  \n" +
                "    ＡＡ　　ＡＡ　　　　ＸＸ　　　　　　ＩＩ　　　　ＮＮ　　ＮＮ  \n" +
                "    ＡＡＡＡＡＡ　　　　ＸＸ　　　　　　ＩＩ　　　　ＮＮ　　ＮＮ  \n" +
                "    ＡＡ　　ＡＡ　　ＸＸ　　ＸＸ　　　　ＩＩ　　　　ＮＮ　　ＮＮ  \n" +
                "    ＡＡ　　ＡＡ　　ＸＸ　　ＸＸ　　ＩＩＩＩＩＩ　　ＮＮ　　ＮＮ");
    }
}
