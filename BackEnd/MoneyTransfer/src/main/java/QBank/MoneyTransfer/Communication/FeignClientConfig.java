package QBank.MoneyTransfer.Communication;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@Configuration
public class FeignClientConfig {

   /* @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // Get the JWT token from the security context
                String token = getJwtToken();
                if (token != null) {
                    requestTemplate.header("Authorization", "Bearer " + token);
                }
            }
        };
    }*/

        @Bean
        public RequestInterceptor jwtTokenInterceptor() {
            return requestTemplate -> {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
                    String jwtToken = attributes.getRequest().getHeader("Authorization");

                    if (jwtToken != null) {
                        requestTemplate.header("Authorization", jwtToken);
                    }
                }
            };
    }

    private String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            // Assuming token is stored as credentials
            return (String) authentication.getCredentials();
        }
        return "Problem get the token";
    }
}