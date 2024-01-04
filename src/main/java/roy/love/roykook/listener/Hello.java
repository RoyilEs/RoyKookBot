package roy.love.roykook.listener;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.definition.Guild;
import love.forte.simbot.event.ChannelMessageEvent;

import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.utils.item.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import roy.love.roykook.utils.AccessLimitService;
import roy.love.roykook.utils.AlibabaToken;
import roy.love.roykook.utils.OK3HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Component
public class Hello {

    Random random = new Random();


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public void onChannelMessage(ChannelMessageEvent event) throws NoApiKeyException, InputRequiredException { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // Java中的阻塞式API
        Items<Guild> groups = event.getBot().getGuilds();
        groups.asStream().forEach(e -> System.out.println(e.getId()));
        ID id = event.getBot().getId();
        System.out.println(id);
        event.replyBlocking("你也好!");
    }

    @Listener
    @Filter(value = "#token {{token}}", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public void dashscopeTokenUpdate(ChannelMessageEvent event) {
        var adminID = String.valueOf(event.getAuthor().getId());
        if (adminID.equals("2894702372")) {
            AlibabaToken token = new AlibabaToken();
            token.setToken(event.toString());
            event.replyBlocking("token已更新");
        } else {
            event.replyBlocking("你不是管理员，无法更新token");
        }
    }

    @Listener
    @Filter(value = "来点二次元")
    public void img(ChannelMessageEvent event) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        int i = random.nextInt(3);
        if (i == 1){
            messagesBuilder.image(Resource.of(new URL("https://imgapi.xl0408.top/index.php")));
        }
        if (i == 2){
            messagesBuilder.image(Resource.of(new URL("https://api.oick.cn/random/api.php?type=pc")));
        } else {
            String s = OK3HttpClient.httpGet("https://api.gumengya.com/Api/DmImg?format=image", null, null);
            messagesBuilder.image(Resource.of(new URL(s)));
        }
        event.getChannel().sendBlocking(messagesBuilder.build());
    }

    @Listener
    @Filter(value = "cnm {{msg}}")
    public static void callWithMessage(ChannelMessageEvent event, @FilterValue("msg")String msg)
            throws NoApiKeyException, ApiException, InputRequiredException {

        AccessLimitService accessLimitService = new AccessLimitService();
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(msg).build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_TURBO).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .build();
        GenerationResult result = gen.call(param);
       // 获取令牌
        if (accessLimitService.tryAcquire()) {
            event.getChannel().sendBlocking(result.getOutput().getChoices().get(0).getMessage().getContent());
        } else {
            event.getChannel().sendAsync("限流触发[" + sdf.format(new Date()) + "]");
        }

    }
}
