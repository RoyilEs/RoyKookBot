package roy.love.roykook.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.kook.objects.card.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import roy.love.roykook.utils.Msg;
import roy.love.roykook.utils.OK3HttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DDTool {
    Random random = new Random();
    private String DD_Api = "https://cfapi.vtbs.moe";

    @Listener
    public void DDTool_Task(ChannelMessageEvent event) throws MalformedURLException {
        //获得信息
        String plainText = event.getMessageContent().getPlainText();
        switch (plainText) {
            case "#今天看谁" :
            case "#今天d谁"  :
                String res = OK3HttpClient.httpGet(DD_Api + "/v1/vtbs", null, null);
                JsonArray jsonArray = new Gson().fromJson(res, JsonArray.class);
                JsonArray vtbs = jsonArray.getAsJsonArray();
                int index = random.nextInt(vtbs.size());
                //第一次拆解
                String vtbId = vtbs.get(index).getAsJsonObject().get("mid").getAsString();
                //第二次拆解
                String RandVtb = OK3HttpClient.httpGet(DD_Api + "/v1/detail/" + vtbId,  null, null);
                JsonObject RandVtbJsonObject = new Gson().fromJson(RandVtb, JsonObject.class);
                String uname = RandVtbJsonObject.get("uname").getAsString();
                //直播间ID
                String roomId = RandVtbJsonObject.get("roomid").getAsString();
                //头像
                String face = RandVtbJsonObject.get("face").getAsString();
                //粉丝数
                String followers = RandVtbJsonObject.get("follower").getAsString();
                //在线状态
                Integer online = Integer.valueOf(RandVtbJsonObject.get("online").getAsString());
                //签名
                String sign = RandVtbJsonObject.get("sign").getAsString();
                //直播间标题
                String title = RandVtbJsonObject.get("title").getAsString();

                    var image = new CardElement.Image(face, "114", Size.LG);
                    var images = new ArrayList<CardElement.Image>();
                    images.add(image);
                    var test = "名称\n" + uname + "\n" +
                            "粉丝数\n" + followers + "\n" +
                            "签名\n" + sign + "\n";
                    var buttons = getButtons(roomId, vtbId);
                    var texts = Msg.getTest(test);
                    event.replyAsync(DDCardMsg(images, texts, buttons));

                break;
            case "#现在看谁" :
            case "#现在d谁"  :
                String res_now = OK3HttpClient.httpGet(DD_Api + "/v1/living", null, null);
                JsonArray resJsonArray_now = new Gson().fromJson(res_now, JsonArray.class);
                JsonArray vtbs_now = resJsonArray_now.getAsJsonArray();
                String RandVtbID = vtbs_now.get(random.nextInt(vtbs_now.size())).getAsString();
                String RoomInfo = OK3HttpClient.httpGet(DD_Api + "/v1/room/" + RandVtbID, null, null);
                JsonObject resjsonObject_now = new Gson().fromJson(RoomInfo, JsonObject.class);
                String uID = resjsonObject_now.get("uid").getAsString();
                String popularity = resjsonObject_now.get("popularity").getAsString();

                String RandVtb_now = OK3HttpClient.httpGet(DD_Api + "/v1/detail/" + uID, null, null);
                JsonObject RandVtbJsonObject_now = new Gson().fromJson(RandVtb_now, JsonObject.class);
                String uname_now = RandVtbJsonObject_now.get("uname").getAsString();
                //直播间ID
                String roomid_now = RandVtbJsonObject_now.get("roomid").getAsString();
                //头像
                String face_now = RandVtbJsonObject_now.get("face").getAsString();
                //粉丝数
                String followers_now = RandVtbJsonObject_now.get("follower").getAsString();
                //签名
                String sign_now = RandVtbJsonObject_now.get("sign").getAsString();
                //直播间标题
                String title_now = RandVtbJsonObject_now.get("title").getAsString();

                var image_now = new CardElement.Image(face_now, "114514", Size.LG);
                var images_now = new ArrayList<CardElement.Image>();
                images_now.add(image_now);
                var text = "标题\n" + title_now + "\n"
                        + "主播\n" + uname_now + "\n"
                        + "粉丝数\n" + followers_now + "\n"
                        + "签名\n" + sign_now + "\n";
                var buttons_now = getButtons(roomid_now, uID);
                var texts_now = Msg.getTest(text);
                event.replyAsync(DDCardMsg(images_now, texts_now, buttons_now));
        }
    }

    @NotNull
    private static ArrayList<CardElement.Button> getButtons(String roomid_now, String uID) {
        var room = new CardElement.PlainText("进入直播间");
        var uRoom = new CardElement.PlainText("进入主页");
        var buttonRoom = new CardElement.Button(Theme.SUCCESS,
                        "https://live.bilibili.com/" + roomid_now,
                        "link", room);
        var buttonU = new CardElement.Button(Theme.INFO,
                "https://space.bilibili.com/" + uID,
                "link", uRoom);
        var buttons = new ArrayList<CardElement.Button>();
        buttons.add(buttonRoom);
        buttons.add(buttonU);
        return buttons;
    }

    public KookCardMessage DDCardMsg(List<CardElement.Image> images, List<CardElement.KMarkdown> texts, List<CardElement.Button> buttons){
        List<CardModule> modules = new ArrayList<>();
        texts.forEach(e -> modules.add(new CardModule.Section(e)));
        modules.add(new CardModule.ImageGroup(images));
        modules.add(new CardModule.ActionGroup(buttons));

        return new KookCardMessage(new CardMessageBuilder()
                .card(Theme.INFO, modules)
                .build()
        );
    }
}
