package me.huangduo.hms.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import me.huangduo.hms.exceptions.TemplateRenderException;
import me.huangduo.hms.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateRenderServiceImpl implements TemplateRenderService {

    private final Configuration freemarkerConfig;

    public TemplateRenderServiceImpl(@Qualifier("stringRenderFreemarkerConfig") Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    @Override
    public String render(String templateString, Map<String, Object> data) throws TemplateRenderException {
        try (StringWriter writer = new StringWriter()) {
            Template template = new Template("dynamicTemplate", new StringReader(templateString), freemarkerConfig);
            template.process(data, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new TemplateRenderException("Failed to render template: " + e.getMessage());
        }
    }

    @Override
    public String render(String templateString, User userInfo) throws TemplateRenderException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("currentUser", userInfo);
        return render(templateString, data);
    }
}
