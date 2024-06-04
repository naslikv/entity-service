package entityportal.service.config;

import entityportal.service.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final EntitySecurityProperties entitySecurityProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public JwtFilter(EntitySecurityProperties entitySecurityProperties,
                     RestTemplate restTemplate) {
        this.entitySecurityProperties = entitySecurityProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(entitySecurityProperties.getVerifyTokenUrl());
        String authHeader = request.getHeader("Authorization");
        String url= entitySecurityProperties.getVerifyTokenUrl();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization",authHeader);
        HttpEntity<Object> requestEntity=new HttpEntity<>(null,headers);
        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class, (Object) null);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                User user = responseEntity.getBody();
                UserDetail userDetail=new UserDetail();
                userDetail.setId(user.getId());
                userDetail.setUsername(user.getUserName());
                userDetail.setPassword(user.getPassword());
                List<SimpleGrantedAuthority> authorityList=new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority(user.getAuthorities().get(0)));
                userDetail.setAUTHORITIES(authorityList);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch(Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
}
