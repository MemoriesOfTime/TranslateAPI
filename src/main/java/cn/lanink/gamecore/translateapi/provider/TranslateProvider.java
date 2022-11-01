package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.api.Info;

/**
 * @author LT_Name
 */
public interface TranslateProvider {

    @Info("是否支持自动识别源语言")
    default boolean isSupportAuto() {
        return true;
    }

    String translate(String text);

    String translate(String sourceLanguage, String targetLanguage, String text);

}
