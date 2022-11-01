package cn.lanink.gamecore.translateapi;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.api.Info;
import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.lanink.gamecore.translateapi.provider.*;
import cn.nukkit.scheduler.AsyncTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author LT_Name
 */
@Info("GameCore TranslateAPI 模块")
public class TranslateAPI extends ModuleBase {

    private static TranslateAPI translateAPI;

    public static TranslateAPI getInstance() {
        return translateAPI;
    }

    private TranslateProvider translateProvider;

    @Override
    protected void onEnable() {
        translateAPI = this;

        this.saveDefaultConfig();

        if (this.getConfig().getBoolean("proxy.enable")) {
            System.setProperty("proxyHost", this.getConfig().getString("proxy.host"));
            System.setProperty("proxyPort", this.getConfig().getString("proxy.port"));
        }

        switch (this.getConfig().getString("provider").toLowerCase()) {
            case "aliyun":
                this.translateProvider = new AliyunTranslateProvider();
                break;
            case "baidu":
                this.translateProvider = new BaiduTranslateProvider();
                break;
            case "youdao":
                this.translateProvider = new YoudaoTranslateProvider();
                break;
            case "google":
            default:
                this.translateProvider = new GoogleTranslateProvider();
                break;
        }

        this.getLogger().info("模块加载完成！");

        this.getServer().getScheduler().scheduleAsyncTask(GameCore.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                TranslateAPI.this.getLogger().info("正在测试翻译功能...");
                TranslateAPI.this.getLogger().info("测试翻译结果: Hello World! -> " + TranslateAPI.this.translateProvider.translate("Hello World!"));
                TranslateAPI.this.getLogger().info("测试翻译结果: 你好 世界！ -> " + TranslateAPI.this.translateProvider.translate("zh_CN", "en", "你好 世界！"));
            }
        });
    }

    @Override
    protected void onDisable() {

    }

    public TranslateProvider getTranslateProvider() {
        return this.translateProvider;
    }

    @Info("翻译为中文，调用此方法应放到异步！")
    public String translate(@NotNull String text) {
        return this.translate("auto", "zh", text);
    }

    public void translate(@NotNull String text, @NotNull Consumer<String> consumer) {
        this.getServer().getScheduler().scheduleAsyncTask(GameCore.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                consumer.accept(translate(text));
            }
        });
    }

    @Info("根据输入翻译为指定语言，调用此方法应放到异步！")
    public String translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text) {
        return this.translateProvider.translate(sourceLanguage, targetLanguage, text);
    }

    public void translate(@NotNull String sourceLanguage, @NotNull String targetLanguage, @NotNull String text, @NotNull Consumer<String> consumer) {
        this.getServer().getScheduler().scheduleAsyncTask(GameCore.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                consumer.accept(translate(sourceLanguage, targetLanguage, text));
            }
        });
    }

}