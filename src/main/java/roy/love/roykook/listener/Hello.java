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
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.ChannelMessageEvent;

import org.springframework.stereotype.Component;
import roy.love.roykook.utils.AlibabaToken;


@Component
public class Hello {

    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public void onChannelMessage(ChannelMessageEvent event) throws NoApiKeyException, InputRequiredException { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // Java中的阻塞式API
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
    @Filter(value = "cnm {{msg}}")
    public static void callWithMessage(ChannelMessageEvent event, @FilterValue("msg")String msg)
            throws NoApiKeyException, ApiException, InputRequiredException {
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
            event.getChannel().sendBlocking(result.getOutput().getChoices().get(0).getMessage().getContent());
    }
}
