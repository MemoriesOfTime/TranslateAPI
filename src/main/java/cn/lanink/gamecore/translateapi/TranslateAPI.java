package cn.lanink.gamecore.translateapi;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.lanink.gamecore.translateapi.provider.AliyunTranslateProvider;
import cn.lanink.gamecore.translateapi.provider.BaiduTranslateProvider;
import cn.lanink.gamecore.translateapi.provider.GoogleTranslateProvider;
import cn.lanink.gamecore.translateapi.provider.TranslateProvider;
import cn.nukkit.scheduler.AsyncTask;

/**
 * @author LT_Name
 */
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
                String result = TranslateAPI.this.translateProvider.translate("Hello World!");
                if (result != null) {
                    TranslateAPI.this.getLogger().info("翻译结果：" + result);
                }else {
                    TranslateAPI.this.getLogger().info("翻译失败！");
                }
            }
        });
    }

    @Override
    protected void onDisable() {

    }

    public TranslateProvider getTranslateProvider() {
        return this.translateProvider;
    }

}