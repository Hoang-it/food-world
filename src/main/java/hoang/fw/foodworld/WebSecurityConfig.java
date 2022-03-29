package hoang.fw.foodworld;

import hoang.fw.foodworld.entities.CustomOAuth2User;
import hoang.fw.foodworld.services.CustomOAuth2UserService;
import hoang.fw.foodworld.services.CustomUserDetailsService;
import hoang.fw.foodworld.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] resource_dirs = new String[]{
                "/resources/**",
                "/static/**",
                "/webjars/**",
                "/css/**",
                "/fonts/**",
                "/images/**",
                "/js/**",
        };
        web.ignoring().antMatchers(resource_dirs);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] allowAll = new String[]{
                "/", "/register", "/process_register", "/oauth2/authorization/**", "/user", "/user/upload_avatar", "/user-photos/*/**",
                "/home", "/food"
        };
        String[] onlyAdmin = new String[]{
                "/users"
        };
        String[] onlyUser = new String[]{
                "/",
        };
        http.authorizeRequests()
                .antMatchers(allowAll).permitAll()
                .antMatchers(onlyAdmin).hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();

        http.oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
                        System.out.println(auth.getAuthorizedClientRegistrationId());
                        if ("google".equals(auth.getAuthorizedClientRegistrationId())) userService.processOAuthPostGoogleLogin(oauthUser.getEmail());
                        if ("facebook".equals(auth.getAuthorizedClientRegistrationId())) userService.processOAuthPostFacebookLogin(oauthUser.getEmail());
                        if ("github".equals(auth.getAuthorizedClientRegistrationId()))  userService.processOAuthPostGithubLogin(oauthUser.getEmail());

                        response.sendRedirect("/");
                    }
                });
    }
}
