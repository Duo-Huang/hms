package me.huangduo.hms.service;

import me.huangduo.hms.exceptions.TemplateRenderException;
import me.huangduo.hms.model.User;

import java.util.Map;

public interface TemplateRenderService {

    String render(String template, Map<String, Object> data) throws TemplateRenderException;

    String render(String template, User userInfo) throws TemplateRenderException;
}
