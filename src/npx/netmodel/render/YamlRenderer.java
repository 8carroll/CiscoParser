package npx.netmodel.render;

import java.util.Iterator;
import java.util.List;
import npx.netmodel.config.PassiveConfig;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Luis Dias Costa <luis@theincrediblemachine.org>
 */
public class YamlRenderer extends Renderer {

    public YamlRenderer(List<PassiveConfig> configList) {
        super(configList);
    }

    @Override
    public Object getResults() {
        Yaml yaml = new Yaml();
        Iterator<PassiveConfig> it = getConfigList().iterator();
        String output = yaml.dumpAll(it);
        return output;
    }
}
