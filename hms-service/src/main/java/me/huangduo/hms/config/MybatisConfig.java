package me.huangduo.hms.config;

import me.huangduo.hms.dao.handler.GenericEnumValueHandler;
import me.huangduo.hms.enums.RoleType;
import me.huangduo.hms.enums.MessageStatus;
import me.huangduo.hms.enums.MessageType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {
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


        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}
