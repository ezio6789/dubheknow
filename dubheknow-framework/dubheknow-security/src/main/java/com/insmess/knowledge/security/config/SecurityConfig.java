package com.insmess.knowledge.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.insmess.knowledge.security.config.properties.PermitAllUrlProperties;
import com.insmess.knowledge.security.filter.JwtAuthenticationTokenFilter;
import com.insmess.knowledge.security.handle.AuthenticationEntryPointImpl;
import com.insmess.knowledge.security.handle.LogoutSuccessHandlerImpl;

/**
 * spring security配置
 *
 * @author insmess
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig
{
    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 允许匿名访问的地址
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    /**
     * 身份验证实现
     */
    @Bean
    public AuthenticationManager authenticationManager()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 不用 session -> 关闭 CSRF
                .csrf(csrf -> csrf.disable())
                // 响应头
                .headers(h -> h
                        .cacheControl(c -> c.disable())
                        .frameOptions(f -> f.sameOrigin())
                )
                // 认证失败处理
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
                // 基于 token，不创建 session
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 鉴权规则
                .authorizeHttpRequests(auth -> {
                    // 1) 常见静态资源目录直接放行（/static, /public, /resources, /META-INF/resources, /webjars）
                    auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();

                    // 2) 你的白名单（支持通配但避免 /**/*.ext 这种非法写法）
                    permitAllUrl.getUrls().forEach(url -> auth.requestMatchers(url).permitAll());

                    // 3) 登录、注册、验证码、以及业务放行
                    auth.requestMatchers("/login", "/register", "/captchaImage", "/flyflow/**").permitAll();

                    // 4) 根路径和部分顶层静态文件放行（仅根目录后缀，用正则变量，而非 /**/*.html）
                    auth.requestMatchers(HttpMethod.GET,
                            "/",
                            "/{file:.+\\.html}",   // 仅根目录 *.html（不跨 /）
                            "/favicon.ico"
                    ).permitAll();

                    // 5) 其它静态/业务前端目录放行（按目录放，不做“任意深度+后缀”）
                    auth.requestMatchers(
                            "/index/**",
                            "/admin/**",
                            "/assets/**",
                            "/profile/**",
                            "/sso/**",
                            "/static/**"           // 若项目仍用该目录
                    ).permitAll();

                    // 6) 文档、监控、WebSocket 等
                    auth.requestMatchers(
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/*/api-docs",
                            "/v3/api-docs/**",
                            "/druid/**",
                            "/websocket/**",
                            "/payment/**",
                            "/syncData/**",
                            "/app/AppServiceApi/invoke/**",//公共接口调用
                            "/app/graph/qa",//智能问答，测试用
                            "/sys/**",
                            "/oauth2/**",
                            "/file/**"
                    ).permitAll();

                    // 7) 其余请求需要认证
                    auth.anyRequest().authenticated();
                })
                // 登出
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                )
                // 过滤器顺序：先 CORS，再你的 JWT，再 UsernamePassword
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 也可放在 ChannelProcessingFilter 之前：.addFilterBefore(corsFilter, ChannelProcessingFilter.class)

        return http.build();
    }


    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
