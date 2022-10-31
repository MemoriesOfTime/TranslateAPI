package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.translateapi.TranslateAPI;
import cn.lanink.gamecore.translateapi.utils.Network;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LT_Name
 */
public class YoudaoTranslateProvider implements TranslateProvider {

    public YoudaoTranslateProvider() {
        TranslateAPI.getInstance().getLogger().info("TranslateProvider: Youdao");
    }

    @Override
    public String translate(String text) {
        return this.translate("AUTO", null, text);
    }

    @Override
    public String translate(String sourceLanguage, String targetLanguage, String text) {
        /*
            ZH_CN2EN 中文　»　英语
            ZH_CN2JA 中文　»　日语
            ZH_CN2KR 中文　»　韩语
            ZH_CN2FR 中文　»　法语
            ZH_CN2RU 中文　»　俄语
            ZH_CN2SP 中文　»　西语
            EN2ZH_CN 英语　»　中文
            JA2ZH_CN 日语　»　中文
            KR2ZH_CN 韩语　»　中文
            FR2ZH_CN 法语　»　中文
            RU2ZH_CN 俄语　»　中文
            SP2ZH_CN 西语　»　中文
        */

        try {
            if (!sourceLanguage.toLowerCase().startsWith("zh")) {
                sourceLanguage = sourceLanguage.split("_")[0].split("-")[0];
            }else {
                sourceLanguage = "ZH_CN";
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("doctype", "json");
            String type = sourceLanguage.toUpperCase();
            if (targetLanguage != null && "".equals(targetLanguage.trim())) {
                if (!targetLanguage.toLowerCase().startsWith("zh")) {
                    targetLanguage = targetLanguage.split("_")[0].split("-")[0];
                }else {
                    targetLanguage = "ZH_CN";
                }
                type += "2" + targetLanguage.toUpperCase();
            }
            params.put("type", type);
            params.put("i", text);
            String result = Network.get("https://fanyi.youdao.com/translate", params);
            HashMap map = new Gson().fromJson(result, HashMap.class);
            if (map.containsKey("translateResult")) {
                List<List<Map>> list = (List<List<Map>>) map.get("translateResult");
                StringBuilder stringBuilder = new StringBuilder();
                for (List<Map> lm : list) {
                    for (Map l : lm) {
                        stringBuilder.append(l.get("tgt")).append(" ");
                    }
                }
                return stringBuilder.toString();
            }
        }catch (Exception e) {
            TranslateAPI.getInstance().getLogger().error(e.getMessage());
        }
        return text;
    }

}
