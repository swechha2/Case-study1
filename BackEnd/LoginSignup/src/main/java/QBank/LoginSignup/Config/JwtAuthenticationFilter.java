package QBank.LoginSignup.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements JwtAuthenticationFilterIn{

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, java.io.IOException {

        String token = extractTokenFromHeader(request);  // Extract token from header

        if (token != null) {
            try {
                Claims claims = jwtTokenUtil.getClaimsFromToken(token);  // Try to parse the token
                String username = claims.getSubject();  // Get username from claims

                // You can add your further authentication logic here (e.g., setting authentication context)
                System.out.println("Authenticated user: " + username);

            } catch (IllegalArgumentException | MalformedJwtException e) {
                // Log the error and handle the invalid token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or malformed JWT token");
                return;  // Prevent further processing
            }
        }

        chain.doFilter(request, response);  // Continue with the filter chain
    }

    // Helper method to extract the token from the Authorization header
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Remove "Bearer " prefix
        }
        return null;  // Return null if no token is found
    }
}
