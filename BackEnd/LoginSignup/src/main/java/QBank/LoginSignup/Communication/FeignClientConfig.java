package QBank.LoginSignup.Communication;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

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
}
