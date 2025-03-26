package QBank.ApiGateway.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfiguration {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("LOGINSIGNUP", r -> r.path("/api/**")
                        .uri("http://localhost:8081"))
                .route("MONEYTRANSFER", r -> r.path("/transfer/**")
                        .uri("http://localhost:8082"))
                .route("TRANSACTIONS", r -> r.path("/transactions/**")
                        .uri("http://localhost:8087"))
                .route("PAYMENTGATEWAY", r -> r.path("/paymentGateway/**")
                        .uri("http://localhost:8086"))
                .route("LOAN", r -> r.path("/loan/**")
                        .uri("http://localhost:8080"))
                .route("BILLPAYMENTS", r -> r.path("/bills/**")
                        .uri("http://localhost:8085"))
                .route("CREDITCARD", r -> r.path("/creditCard/**")
                        .uri("http://localhost:8084"))
                .route("ACOUNTUPDATE", r -> r.path("/accountUpdate/**")
                        .uri("http://localhost:808"))
                .build();

    }
}
