package roy.love.roykook.bilibili.api;

import lombok.SneakyThrows;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.resources.URLResource;
import org.springframework.stereotype.Component;
import roy.love.roykook.bilibili.Bili;
import roy.love.roykook.utils.OK3HttpClient;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class BiliFollow {

    @SneakyThrows
    @Listener
    @Filter(value = "查关注 {{Uid}}")
    public void follow(ChannelMessageEvent event, @FilterValue("Uid")String Uid) {
        String s = OK3HttpClient.httpGet(Bili.YuApi + "BiliFollow?sign=" + Uid, null, null);
        // 获取图片
        URLResource of = Resource.of(new URL(s));

        Messages msg =  new MessagesBuilder().image(of).build();
        event.replyAsync(msg);
    }
}
