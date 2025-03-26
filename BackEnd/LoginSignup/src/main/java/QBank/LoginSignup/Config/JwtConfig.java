package QBank.LoginSignup.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }

}
