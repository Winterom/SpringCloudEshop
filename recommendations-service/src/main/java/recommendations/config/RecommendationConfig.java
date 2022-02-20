package recommendations.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;
@Configuration
public class RecommendationConfig {
    @Value("${integrations.core-service.url}")
    private String CoreServiceUrl;

    @Value("${integrations.cart-service.url}")
    private String CartServiceUrl;



    @Bean("client_core")
    public WebClient coreServiceWebClient() {
        return getWebClient(CoreServiceUrl);
    }

    @Bean("client_cart")
    public WebClient CartServiceWebClient() {
        return getWebClient(CartServiceUrl);
    }

    private WebClient getWebClient(String serviceUrl) {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(2000, TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(serviceUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }
}
