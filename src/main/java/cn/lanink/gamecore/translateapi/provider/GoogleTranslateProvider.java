package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.translateapi.TranslateAPI;
import cn.lanink.gamecore.translateapi.utils.Network;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.List;

/**
 * @author LT_Name
 */
public class GoogleTranslateProvider implements TranslateProvider {

    private static final String URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";

    public GoogleTranslateProvider() {
        TranslateAPI.getInstance().getLogger().info("TranslateProvider: Google");
    }

    public String translate(String text) {
        return translate("auto", "zh", text);
    }

    public String translate(String sourceLanguage, String targetLanguage, String text) {
        try {
            String url = String.format(URL, sourceLanguage, targetLanguage, URLEncoder.encode(text, "UTF-8"));
            String result = Network.get(url);

            List<List> list = (List) ((List)GameCore.GSON.fromJson(result, new TypeToken<List>() {}.getType())).get(0);

            if (list == null || list.isEmpty()) {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (List l : list) {
                stringBuilder.append(l.get(0)).append(" ");
            }

            return stringBuilder.toString();
        }catch (Exception error) {
            TranslateAPI.getInstance().getLogger().error(error.getMessage());
        }
        return text;
    }

}
