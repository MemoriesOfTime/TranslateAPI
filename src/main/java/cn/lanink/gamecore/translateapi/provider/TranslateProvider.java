package cn.lanink.gamecore.translateapi.provider;

/**
 * @author LT_Name
 */
public interface TranslateProvider {

    String translate(String text);

    String translate(String sourceLanguage, String targetLanguage, String text);

}
