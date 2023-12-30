package roy.love.roykook.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.kook.event.KookMemberJoinedChannelEvent;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.event.MessageEvent;
import love.forte.simbot.kook.objects.card.CardMessage;
import love.forte.simbot.kook.objects.card.CardMessageBuilder;
import love.forte.simbot.kook.objects.card.CardModule;
import love.forte.simbot.kook.objects.card.Theme;
import org.springframework.stereotype.Component;
import roy.love.roykook.utils.OK3HttpClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class Video {

    @Listener
    @Filter(value = "/v")
    public void video(MessageEvent event){
        String s = OK3HttpClient.httpGet("https://api.xn--ei1aa.cn/API/kuaishou_nv.php?type=json", null, null);
        JsonObject jsonObject = new Gson().fromJson(s, JsonObject.class);

        String url = jsonObject.getAsJsonObject("data").get("Video").getAsString();
        String cover = jsonObject.getAsJsonObject("data").get("Cover").getAsString();
        String title = jsonObject.getAsJsonObject("data").get("Title").getAsString();

        CardModule.Files.Video dp = CardModule.Files.Video.video(url, title, cover);

        CardMessageBuilder cardMessageBuilder = new CardMessageBuilder();
        List<CardModule> modules = new ArrayList<>();
        modules.add(dp);
        cardMessageBuilder.card(Theme.SECONDARY, modules);
        CardMessage card = new CardMessage(cardMessageBuilder.build());
        KookCardMessage message = KookCardMessage.asMessage(card);
        event.replyAsync(message);
    }
}
