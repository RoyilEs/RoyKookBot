package roy.love.roykook.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.application.BotManagers;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.definition.Channel;
import love.forte.simbot.definition.Guild;
import love.forte.simbot.kook.objects.card.CardElement;
import love.forte.simbot.kook.objects.card.CardMessageBuilder;
import love.forte.simbot.kook.objects.card.CardModule;
import love.forte.simbot.kook.objects.card.Theme;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.utils.item.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import roy.love.roykook.utils.OK3HttpClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotMsg {

    @Autowired
    private BotManagers botManagers;

    @SneakyThrows
    @Scheduled(cron = "0 0 7  * * ? ")
    public void schuedingTest() {
        for (BotManager<?> botManager : botManagers) {
            Bot bot = botManager.get(Identifies.ID("1K5cGi97SBatmQkd"));
            Items<Guild> guilds;
            // 一言api
            String s = OK3HttpClient.httpGet("http://www.wudada.online/Api/MrYy", null, null);
            JsonObject jsonObject = new Gson().fromJson(s, JsonObject.class);
            JsonObject data = jsonObject.getAsJsonObject("data");
            // 内容
            var hitokoto = data.get("hitokoto").getAsString();
            // 来源
            var from = data.get("from").getAsString();

            var kMarkdowns = new ArrayList<CardElement.KMarkdown>();
            kMarkdowns.add(new CardElement.KMarkdown("**"+hitokoto+"**"));
            kMarkdowns.add(new CardElement.KMarkdown(from));
            if (bot != null) {
                guilds = bot.getGuilds();
                guilds.asStream().forEach(guild -> {
                    Items<Channel> channels = guild.getChannels();
                    channels.asStream().forEach(channel -> {

                        List<CardModule> moduleList = new ArrayList<>();
                        kMarkdowns.forEach(e -> moduleList.add(new CardModule.Section(e)));
                        channel.sendAsync(new KookCardMessage(
                                new CardMessageBuilder()
                                        .card(Theme.WARNING, moduleList)
                                        .build()
                        ));
                    });
                });
            }
        }
    }

    // 每天中午一张MC酱
    @SneakyThrows
    @Scheduled(cron = "0 34 18 * * ?")
    public void sendMCCard() {
        for (BotManager<?> botManager : botManagers) {
            Bot bot = botManager.get(Identifies.ID("1K5cGi97SBatmQkd"));
            Items<Guild> guilds;
            // api
            Messages messages = new MessagesBuilder()
                    .image(Resource.of(new URL("https://api.gumengya.com/Api/McImg?format=image")))
                    .build();
            if (bot != null) {
                guilds = bot.getGuilds();
                guilds.asStream().forEach(guild -> {
                    Items<Channel> channels = guild.getChannels();
                    channels.asStream().forEach(channel -> channel.sendAsync(messages));
                });
            }
        }
    }
}
