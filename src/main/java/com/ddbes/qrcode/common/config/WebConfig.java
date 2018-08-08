package com.ddbes.qrcode.common.config;

import com.ddbes.qrcode.common.annotation.UserInjectMethodArgumentResolver;
import com.ddbes.qrcode.common.interceptor.LoginAuthInterceptor;
import com.ddbes.qrcode.common.util.IdWorker;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by daitian on 2018/5/10.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * 跨域解决
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    /**
     * 自定义消息转换器.
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = converter.getObjectMapper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(sdf);
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(getBean()));
        return converter;
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1);
    }

    public BeanSerializerModifier getBean() {
        return new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                for (int i = 0; i < beanProperties.size(); i++) {
                    BeanPropertyWriter writer = beanProperties.get(i);
                    writer.assignNullSerializer(getJS());
                }
                return beanProperties;
            }
        };
    }

    public JsonSerializer getJS() {
        return new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (o == null) {
                    String currentName = jsonGenerator.getOutputContext().getCurrentName();
                    if ("data".equals(currentName)) {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeEndObject();
                    } else {
                        jsonGenerator.writeString("");
                    }
                } else {
                    jsonGenerator.writeObject(o);
                }
            }
        };
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        StringBuffer sb = new StringBuffer();
        sb.append("/open/user/**").append(",");
        sb.append("/plat/open/**").append(",");
        sb.append("/notify/ddbes/**").append(",");

        sb.append("/swagger-resources/**");
        registry.addInterceptor(loginAuthInterceptor()).addPathPatterns("/**").excludePathPatterns(sb.toString().split(","));
        super.addInterceptors(registry);
    }

    @Bean
    public LoginAuthInterceptor loginAuthInterceptor() {
        return new LoginAuthInterceptor();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userInjectMethodArgumentResolver());
    }

    @Bean
    public UserInjectMethodArgumentResolver userInjectMethodArgumentResolver() {
        return new UserInjectMethodArgumentResolver();
    }

}
