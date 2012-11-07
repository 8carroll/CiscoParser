package npx.netmodel.render;

import flexjson.JSONSerializer;
import java.util.List;
import npx.netmodel.config.PassiveConfig;

/**
 *
 * @author Luis Dias Costa <luis@theincrediblemachine.org>
 */
public class JsonRenderer extends Renderer {

    public JsonRenderer(List<PassiveConfig> configList) {
        super(configList);
    }

    @Override
    public Object getResults() {
        JSONSerializer serializer = new JSONSerializer();
        String output=serializer.include("*").serialize(getConfigList());
        return output;
    }
}
