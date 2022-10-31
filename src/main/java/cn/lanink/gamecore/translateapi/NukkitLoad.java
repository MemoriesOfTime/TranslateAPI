package cn.lanink.gamecore.translateapi;

import cn.lanink.gamecore.GameCore;
import cn.nukkit.plugin.PluginBase;

/**
 * @author LT_Name
 */
public class NukkitLoad extends PluginBase {

    @Override
    public void onEnable() {
        GameCore.getInstance().hotSwapManager.loadModuleFromLocal(this.getFile());
    }

}
