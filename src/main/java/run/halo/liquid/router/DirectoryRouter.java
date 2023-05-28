package run.halo.liquid.router;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.SettingFetcher;
import run.halo.app.theme.router.strategy.ModelConst;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Dioxide.CN
 * @date 2023/5/28
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class DirectoryRouter {

    private final SettingFetcher settingFetcher;

    @Bean
    RouterFunction<ServerResponse> directoryRouter() {
        return route(GET("/directory"), handlerFunction());
    }

    private HandlerFunction<ServerResponse> handlerFunction() {
        return request -> ServerResponse.ok().render(
                "directory",
                Map.of(
                        ModelConst.TEMPLATE_ID,
                        "directory",
                        "title",
                        Mono.fromCallable(() -> this.settingFetcher
                                .get("route")
                                .get("directory")
                                .asText("目录"))
                )
        );
    }

}
