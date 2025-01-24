package me.huangduo.hms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.huangduo.hms.dao.handler.GenericEnumValueHandler;
import me.huangduo.hms.dao.handler.GenericJsonTypeHandler;
import me.huangduo.hms.enums.MessageStatus;
import me.huangduo.hms.enums.MessageType;
import me.huangduo.hms.enums.RoleType;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {

    private final ObjectMapper objectMapper;

    public MybatisConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // mybatis configuration
        org.apache.ibatis.session.Configuration configuration = sessionFactory.getObject().getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);

        // setup global type handle
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(RoleType.class, new GenericEnumValueHandler<>(RoleType.class));
        typeHandlerRegistry.register(MessageType.class, new GenericEnumValueHandler<>(MessageType.class));
        typeHandlerRegistry.register(MessageStatus.class, new GenericEnumValueHandler<>(MessageStatus.class));

        typeHandlerRegistry.register(InvitationEvent.InvitationMessagePayload.class, new GenericJsonTypeHandler<>(objectMapper, InvitationEvent.InvitationMessagePayload.class));
        typeHandlerRegistry.register(HmsEvent.MessagePayload.class, new GenericJsonTypeHandler<>(objectMapper, HmsEvent.MessagePayload.class)); // all general notification message payload mapper

        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}
