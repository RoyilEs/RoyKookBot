package roy.love.roykook.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.kook.objects.card.CardElement;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Text;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;

@Slf4j
public class Msg {

    /*
      从消息中取出文本消息

      @param event
     */
    public static void GroupMsg(ChannelMessageEvent event) {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof Text) {
                log.info(MessageFormat.format("[文本消息: {0} ]", ((Text) message).getText()));
            }
        }
    }

    /**
     * 将long类型Id值转换为ID
     *
     * @param id
     */
    public static long Id(ID id) {
        return Long.parseLong(String.valueOf(id).trim());
    }

    /**
     * 将String 类型Id转换为ID
     *
     * @param id
     */
    public static ID Id(String id) {
        return ID.$(id.trim());
    }

    /**
     * 未知用途
     *
     * @param time
     */
    public static long longId(String time) {
        return Long.parseLong(time.trim());
    }

    @NotNull
    public static ArrayList<CardElement.KMarkdown> getTest(String test) {
        String[] splitS = test.split("\\n");
        var elements = new ArrayList<CardElement.KMarkdown>();
        for (int i = 1; i < splitS.length; i += 2) {
            splitS[i - 1] = "**" + splitS[i - 1]+ "**";
            elements.add(new CardElement.KMarkdown(splitS[i - 1] + "\n" + splitS[i]));
        }
        return elements;
    }
}
