package engine.configuration;

import engine.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService userDetailsService;


    @Autowired
    public SecurityConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/register", "/api/register/", "/actuator/shutdown").permitAll()
//                .anyRequest().hasRole("USER")
                .anyRequest().hasAuthority("ROLE_USER")
                .and()
                .httpBasic()
                .and()
                .csrf().disable().headers().frameOptions().disable();
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/register").permitAll();
//                .hasRole("USER")
//                .authorizeRequests()
//                .antMatchers("/api/register").permitAll()
//                .and()
        // some more method calls
//                .formLogin();
//                .antMatchers("/admin/**").hasRole("ADMIN")
    }
}