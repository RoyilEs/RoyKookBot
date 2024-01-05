package roy.love.roykook.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pixiv {
    /**
     * 0 非 1 是 2 混合
     */
    private Integer r18;

    private Integer num;

    private int[] uid;
    /**
     * 可用tag[] = 替换
     */
    private String keyword;
    /**
     * tag=萝莉|少女
     */
    private String tag;
    /**
     * 超高: original 高: regular 中: small 低: thumb 超低: mini
     */
    private String size;


    /**
     * 默认 false
     */
    private boolean excludeAI;

    public void initPixiv() {
        setR18(0);
        setNum(1);
        setSize("small");
        setTag("明日方舟");
    }

}
