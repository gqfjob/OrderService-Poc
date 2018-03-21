package com.example.orderservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableFeignClients
@EnableResourceServer
@SpringBootApplication
public class OrderServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return new OAuth2MethodSecurityExpressionHandler();
		}
	}

	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate requestTemplate) {
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)
						SecurityContextHolder.getContext().getAuthentication().getDetails();

				requestTemplate.header("Authorization", "bearer " + details.getTokenValue());
			}
		};
	}
}
