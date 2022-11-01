package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.translateapi.TranslateAPI;
import cn.lanink.gamecore.translateapi.utils.Network;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * 谷歌翻译
 *
 * @author LT_Name
 */
public class GoogleTranslateProvider implements TranslateProvider {

    @Override
    public String getProviderName() {
        return "Google";
    }

    public String translate(@NotNull String text) {
        return translate("auto", "zh", text);
    }

    public String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("client", "gtx");
            params.put("dt", "t");
            params.put("sl", sourceLanguage);
            params.put("tl", targetLanguage);
            params.put("q", text);
            String result = Network.get("https://translate.googleapis.com/translate_a/single", params);

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
