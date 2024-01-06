package roy.love.roykook.common;


import roy.love.roykook.dao.Pixiv;
import roy.love.roykook.utils.OK3HttpClient;

import java.util.Arrays;
import java.util.List;

public class PixivUrl extends Pixiv {

    // 获取图片
    public PixivUrl() {
        super();
        initPixiv();
    }

    // 参数获取限定
    public PixivUrl(Integer r18, Integer num, String tag, String size) {
        super();
        this.setR18(r18);
        this.setNum(num);
        this.setTag(tag);
        this.setSize(size);
    }

    public String GetPixivUrl() {
        String urlGroup = "https://api.lolicon.app/setu/v2?r18=" + getR18()
                + "&num=" + getNum()
                + "&tag=" + getTag()
                + "&size=" + getSize();
        System.out.println(urlGroup);
        return OK3HttpClient.httpGet(urlGroup, null, null);
    }

    public String GetPixivUrl(Integer r18, Integer num, String tag, String size) {
        new PixivUrl(r18, num, tag, size);
        String urlGroup = "https://api.lolicon.app/setu/v2?r18=" + getR18()
                + "&num=" + getNum()
                + "&tag=" + getTag()
                + "&size=" + getSize();
        System.out.println(urlGroup);
        return OK3HttpClient.httpGet(urlGroup, null, null);
    }

    public static String SmallToRegular(String url) {
        List<String> strings = new java.util.ArrayList<>(Arrays.stream(url.split("/")).toList());
        strings.remove(3);
        strings.remove(3);
        return String.join("/", strings);
    }
}
