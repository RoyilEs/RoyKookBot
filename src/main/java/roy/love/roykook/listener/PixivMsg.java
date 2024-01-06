package roy.love.roykook.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.kook.objects.card.*;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import roy.love.roykook.common.PixivUrl;
import roy.love.roykook.utils.Msg;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PixivMsg {

    /**
     *
     */
    @Listener
    @Filter("/img {{tag}}")
    public void ChannelPixivMsg(ChannelMessageEvent event, @FilterValue("tag")String tag) {
        // PixivApi
        PixivUrl pixivUrl = new PixivUrl(0, 1, tag, "small");
        String size = pixivUrl.getSize();
        String s = pixivUrl.GetPixivUrl();
        CardMessageBuilder cardMessageBuilder = new CardMessageBuilder();

        JsonObject jsonObject = new Gson().fromJson(s, JsonObject.class);
        JsonArray data = jsonObject.getAsJsonArray("data");
        data.forEach(jsonElement -> {
            String pid = jsonElement.getAsJsonObject().get("pid").getAsString();
            String uid = jsonElement.getAsJsonObject().get("uid").getAsString();
            String title = jsonElement.getAsJsonObject().get("title").getAsString();
            String author = jsonElement.getAsJsonObject().get("author").getAsString();
            String width = jsonElement.getAsJsonObject().get("width").getAsString();
            String height = jsonElement.getAsJsonObject().get("height").getAsString();
            String text = "Pid\n" + pid + "\n" +
                    "Uid\n" + uid + "\n" +
                    "Title\n" + title + "\n" +
                    "Author\n" + author + "\n";
            var texts = Msg.getTest(text);
            String imgUrl = jsonElement.getAsJsonObject().get("urls").getAsJsonObject().get(size).getAsString();
            String originalUrl = PixivUrl.SmallToRegular(imgUrl);
            var room = new CardElement.PlainText("查看原图");
            var button = new CardElement.Button(Theme.SUCCESS,
                    originalUrl,
                    "link", room);
            var buttons = new ArrayList<CardElement.Button>();
            buttons.add(button);
            List<CardModule> modules = new ArrayList<>();
            texts.forEach(e -> modules.add(new CardModule.Section(e)));
            modules.add(new CardModule.ActionGroup(buttons));
            event.getChannel().sendAsync(new KookCardMessage(cardMessageBuilder
                    .card(Theme.PRIMARY, modules)
                    .build()));
            try {
                event.getChannel().sendAsync(new MessagesBuilder()
                        .image(Resource.of(new URL(imgUrl))).build());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }



}
