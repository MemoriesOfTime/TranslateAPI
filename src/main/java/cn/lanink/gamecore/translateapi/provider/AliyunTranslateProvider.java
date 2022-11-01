package cn.lanink.gamecore.translateapi.provider;

import cn.lanink.gamecore.translateapi.TranslateAPI;
import com.aliyun.alimt20181012.Client;
import com.aliyun.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.alimt20181012.models.TranslateGeneralResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

/**
 * 阿里云翻译
 *
 * @author LT_Name
 */
public class AliyunTranslateProvider implements TranslateProvider {

    public AliyunTranslateProvider() {
        TranslateAPI.getInstance().getLogger().info("TranslateProvider: Aliyun");
    }

    public String translate(String text) {
        return translate("auto", "zh", text);
    }

    public String translate(String sourceLanguage, String targetLanguage, String text) {
        try {
            Config config = new Config()
                    .setAccessKeyId(TranslateAPI.getInstance().getConfig().getString("aliyun.accessKeyId"))
                    .setAccessKeySecret(TranslateAPI.getInstance().getConfig().getString("aliyun.accessKeySecret"));
            config.endpoint = "mt.cn-hangzhou.aliyuncs.com";
            Client client = new Client(config);
            TranslateGeneralRequest translateGeneralRequest = new TranslateGeneralRequest()
                    .setFormatType("text")
                    .setSourceLanguage(sourceLanguage) //en
                    .setTargetLanguage(targetLanguage) //zh
                    .setSourceText(text)
                    .setScene("general");
            RuntimeOptions runtime = new RuntimeOptions();
            TranslateGeneralResponse response = client.translateGeneralWithOptions(translateGeneralRequest, runtime);
            return response.getBody().getData().getTranslated();
        }catch (Exception error) {
            TranslateAPI.getInstance().getLogger().error(error.getMessage());
            return text;
        }
    }

}
