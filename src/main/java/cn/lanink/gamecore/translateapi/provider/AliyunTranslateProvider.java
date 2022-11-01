package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.translateapi.TranslateAPI;
import com.aliyun.alimt20181012.Client;
import com.aliyun.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.alimt20181012.models.TranslateGeneralResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.jetbrains.annotations.NotNull;

/**
 * 阿里云翻译
 *
 * @author LT_Name
 */
public class AliyunTranslateProvider implements TranslateProvider {

    private final String accessKeyId;
    private final String accessKeySecret;

    public AliyunTranslateProvider() {
        this.accessKeyId = TranslateAPI.getInstance().getConfig().getString("aliyun.accessKeyId");
        this.accessKeySecret = TranslateAPI.getInstance().getConfig().getString("aliyun.accessKeySecret");
    }

    public String getProviderName() {
        return "Aliyun";
    }

    public String translate(@NotNull String text) {
        return translate("auto", "zh", text);
    }

    public String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text) {
        try {
            Config config = new Config()
                    .setAccessKeyId(this.accessKeyId)
                    .setAccessKeySecret(this.accessKeySecret);
            config.endpoint = "mt.cn-hangzhou.aliyuncs.com";
            Client client = new Client(config);
            TranslateGeneralRequest translateGeneralRequest = new TranslateGeneralRequest()
                    .setFormatType("text")
                    .setSourceLanguage(this.transcodeLanguage(sourceLanguage))
                    .setTargetLanguage(this.transcodeLanguage(targetLanguage))
                    .setSourceText(text)
                    .setScene("general");
            RuntimeOptions runtime = new RuntimeOptions();
            TranslateGeneralResponse response = client.translateGeneralWithOptions(translateGeneralRequest, runtime);
            return response.getBody().getData().getTranslated();
        }catch (Exception e) {
            TranslateAPI.getInstance().getLogger().error(e.getMessage(), e);
        }
        return text;
    }

    private String transcodeLanguage(String language) {
        if (language.equalsIgnoreCase("zh-tw")) { //阿里云唯一的带横杠的语言代码
            return "zh-tw";
        }
        return language.split("_")[0].split("-")[0].toLowerCase();
    }

}
