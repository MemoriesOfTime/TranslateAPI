package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.api.Info;
import org.jetbrains.annotations.NotNull;

/**
 * @author LT_Name
 */
public interface TranslateProvider {

    String getProviderName();

    @Info("是否支持自动识别源语言")
    default boolean isSupportAuto() {
        return true;
    }

    String translate(@NotNull String text);

    String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text);

}
