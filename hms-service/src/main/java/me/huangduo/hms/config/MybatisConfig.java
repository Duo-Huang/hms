package me.huangduo.hms.config;

import me.huangduo.hms.dao.handler.GenericEnumValueHandler;
import me.huangduo.hms.enums.MessageStatusEnum;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.enums.RoleTypeEnum;
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
        typeHandlerRegistry.register(RoleTypeEnum.class, new GenericEnumValueHandler<>(RoleTypeEnum.class));
        typeHandlerRegistry.register(MessageTypeEnum.class, new GenericEnumValueHandler<>(MessageTypeEnum.class));
        typeHandlerRegistry.register(MessageStatusEnum.class, new GenericEnumValueHandler<>(MessageStatusEnum.class));

        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}
