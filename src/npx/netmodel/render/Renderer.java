package npx.netmodel.render;

import java.util.List;
import npx.netmodel.config.PassiveConfig;

/**
 *
 * @author Luis Dias Costa <luis@theincrediblemachine.org>
 */
abstract class Renderer {
    private final List<PassiveConfig> configList;

    public Renderer(List<PassiveConfig> configList) {
        this.configList=configList;
    }
    
    protected List<PassiveConfig> getConfigList() {
        return configList;
    }
    
    public abstract Object getResults();
    
}
