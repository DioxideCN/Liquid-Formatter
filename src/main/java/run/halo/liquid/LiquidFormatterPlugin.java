package run.halo.liquid;

import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;

/**
 * @author Dioxide.CN
 * @date 2023/5/22 19:30:07
 */
@Component
public class LiquidFormatterPlugin extends BasePlugin {

    public LiquidFormatterPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
