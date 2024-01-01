package roy.love.roykook;

import com.alibaba.dashscope.utils.Constants;
import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import roy.love.roykook.utils.AlibabaToken;


@EnableSimbot
@EnableScheduling
@SpringBootApplication
public class RoykookApplication {

    public static void main(String[] args) {
        AlibabaToken alibabaToken = new AlibabaToken();
        alibabaToken.setToken("xxxx");
        // 设置全局的token
        Constants.apiKey= alibabaToken.getToken();
        SpringApplication.run(RoykookApplication.class, args);
    }

}
