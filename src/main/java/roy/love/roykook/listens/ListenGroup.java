package roy.love.roykook.listens;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.definition.GuildMember;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import org.springframework.stereotype.Component;
import roy.love.roykook.utils.Msg;

import java.text.MessageFormat;

@Slf4j
@Component
public class ListenGroup {

    @Listener
    public void getMsg(ChannelMessageEvent event) {
        var groupId = "群: " + event.getChannel().getName() + "(" + event.getChannel().getId() + ")";
        var groupUser = "成员: " + event.getAuthor().getUsername() + "(" + event.getAuthor().getId() + ")";
        var group = event.getChannel();

        MessagesBuilder messagesBuilder = new MessagesBuilder();
        log.info(MessageFormat.format("{0}\t\t{1}", groupId, groupUser));
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof Image<?> image) {
                log.info(MessageFormat.format("[图片消息: {0} ]", image.getResource().getName()));
            }
            if (message instanceof Face) {
                log.info(MessageFormat.format("[Face表情: {0} ]", ((Face) message).getId()));
            }
            if (message instanceof At at) {
                ID target = at.getTarget();
                GuildMember member = group.getMember(target);
                if (member == null) {
                    log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", target));
                } else {
                    log.info(MessageFormat.format("[AT消息: @{0}( {1} )", member.getNickOrUsername(), member.getId()));
                }
            }
        }
        Msg.GroupMsg(event);
    }

}
