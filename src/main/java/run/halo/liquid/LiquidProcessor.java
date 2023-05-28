package run.halo.liquid;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.SettingFetcher;
import run.halo.app.theme.DefaultTemplateEnum;
import run.halo.app.theme.dialect.TemplateHeadProcessor;
import run.halo.app.theme.router.strategy.ModelConst;

@Component
public class LiquidProcessor implements TemplateHeadProcessor {
    private final SettingFetcher settingFetcher;

    public LiquidProcessor(SettingFetcher settingFetcher) {
        this.settingFetcher = settingFetcher;
    }

    public Mono<Void> process(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
        if (!isContentTemplate(context)) {
            // enable on only single page or content page
            return Mono.empty();
        }
        return settingFetcher.fetch("basic", BasicConfig.class)
                .map(config -> {
                    final IModelFactory modelFactory = context.getModelFactory();
                    model.add(modelFactory.createText(appendSource(config)));
                    return Mono.empty();
                }).orElse(Mono.empty()).then();
    }

    private String appendSource(BasicConfig config) {
        String dataG2 = "data-g2-enable='" + config.antvG2 + "'";
        String dataX6 = "data-x6-enable='" + config.antvX6 + "'";

        String script = "<script src='/plugins/PluginLiquid/assets/static/lib/LiquidFormatterInit.js' id='liquid-formatter' " +
                dataG2 +
                " " +
                dataX6 +
                "></script>";

        if (config.antvG2) {
            script += """
                    <script src="/plugins/PluginLiquid/assets/static/antv/g2.min.js"></script>
                    """;
        }
        if (config.antvX6) {
            script += """
                    <script src="/plugins/PluginLiquid/assets/static/antv/x6.min.js"></script>
                    """;
        }

        // language=html
        return script;
    }

    public boolean isContentTemplate(ITemplateContext context) {
        return DefaultTemplateEnum
                .POST.getValue()
                .equals(context.getVariable(ModelConst.TEMPLATE_ID)) ||
               DefaultTemplateEnum
                .SINGLE_PAGE.getValue()
                .equals(context.getVariable(ModelConst.TEMPLATE_ID));
    }

    @Data
    static class BasicConfig {
        Boolean antvG2;
        Boolean antvX6;
    }
}
