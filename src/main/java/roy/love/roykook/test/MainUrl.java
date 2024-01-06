package roy.love.roykook.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainUrl {
    public static void main(String[] args) {
        String url = "https://i.pixiv.re/c/540x540_70/img-master/img/2018/11/23/17/02/01/71793381_p0_master1200.jpg";
        System.out.println(original(url));
    }

    public static String original(String url) {
        List<String> strings = new java.util.ArrayList<>(Arrays.stream(url.split("/")).toList());
        strings.forEach(System.out::println);
        strings.remove(3);
        strings.remove(3);
        String  result;
        result = String.join("/", strings);
        return result;
    }
}
