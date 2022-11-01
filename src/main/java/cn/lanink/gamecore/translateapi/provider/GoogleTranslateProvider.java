package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.translateapi.TranslateAPI;
import cn.lanink.gamecore.translateapi.utils.Network;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.List;

/**
 * 谷歌翻译
 *
 * @author LT_Name
 */
public class GoogleTranslateProvider implements TranslateProvider {

    private static final String URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";

    @Override
    public String getProviderName() {
        return "Google";
    }

    public String translate(@NotNull String text) {
        return translate("auto", "zh", text);
    }

    public String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text) {
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
        }catch (Exception e) {
            TranslateAPI.getInstance().getLogger().error(e.getMessage(), e);
        }
        return text;
    }

}
