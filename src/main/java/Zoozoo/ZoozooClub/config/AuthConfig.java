package Zoozoo.ZoozooClub.config;

import Zoozoo.ZoozooClub.commons.auth.LoginInterceptor;
import Zoozoo.ZoozooClub.commons.auth.LoginUserIdPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoginUserIdPrincipalArgumentResolver loginUserIdPrincipalArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .excludePathPatterns("/**")
//                .addPathPatterns("");

//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserIdPrincipalArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

}