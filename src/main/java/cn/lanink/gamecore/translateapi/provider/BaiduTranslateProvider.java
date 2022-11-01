package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.translateapi.TranslateAPI;
import cn.lanink.gamecore.translateapi.utils.MD5;
import cn.lanink.gamecore.translateapi.utils.Network;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度翻译
 *
 * @author LT_Name
 */
public class BaiduTranslateProvider implements TranslateProvider {

    private final String appid;
    private final String securityKey;

    public BaiduTranslateProvider() {
        this.appid = TranslateAPI.getInstance().getConfig().getString("baidu.appId");
        this.securityKey = TranslateAPI.getInstance().getConfig().getString("baidu.secretKey");

        TranslateAPI.getInstance().getLogger().info("TranslateProvider: Baidu");
    }

    @Override
    public String translate(@NotNull String text) {
        return this.translate("auto", "zh", text);
    }

    @Override
    public String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("q", text);
            params.put("from", sourceLanguage.split("_")[0].split("-")[0]);
            params.put("to", targetLanguage.split("_")[0].split("-")[0]);

            params.put("appid", appid);

            String salt = String.valueOf(System.currentTimeMillis());
            params.put("salt", salt);
            params.put("sign", MD5.md5(appid + text + salt + securityKey));

            String result = Network.get("http://api.fanyi.baidu.com/api/trans/vip/translate", params);
            Map<String, Object> map = GameCore.GSON.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
            if (map.containsKey("error_code")) {
                TranslateAPI.getInstance().getLogger().error("Baidu Translate Error: " + map.get("error_msg"));
                return text;
            }
            List<Map> maps = (List<Map>) map.get("trans_result");
            StringBuilder stringBuilder = new StringBuilder();
            for (Map m : maps) {
                stringBuilder.append(m.get("dst")).append(" ");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            TranslateAPI.getInstance().getLogger().error(e.getMessage(), e);
        }
        return text;
    }

}
