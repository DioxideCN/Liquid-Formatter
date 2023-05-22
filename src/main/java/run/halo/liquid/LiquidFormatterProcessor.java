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

/**
 * tocbot 插件
 *
 * @author liuzhihang
 * @date 2022/10/23
 */
@Component
public class LiquidFormatterProcessor implements TemplateHeadProcessor {

    private final SettingFetcher settingFetcher;

    public LiquidFormatterProcessor(SettingFetcher settingFetcher) {
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
        // language=html
        String script = """
                    <script src="/plugins/LiquidFormatter/assets/static/lib/LiquidFormatterInit.js"></script>
                    """;

        if (config.antvG2) {
            script += """
                    <script src="/plugins/LiquidFormatter/assets/static/antv/g2.min.js"></script>
                    """;
        }
        if (config.antvX6) {
            script += """
                    <script src="/plugins/LiquidFormatter/assets/static/antv/x6.min.js"></script>
                    """;
        }

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
    public static class BasicConfig {
        Boolean antvG2;
        Boolean antvX6;
    }
}
