package me.huangduo.hms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.huangduo.hms.config.FreemarkerConfig;
import me.huangduo.hms.enums.ErrorCodeEnum;
import me.huangduo.hms.enums.RoleTypeEnum;
import me.huangduo.hms.model.User;
import me.huangduo.hms.service.TemplateRenderServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(ErrorCodeEnum.SYSTEM_ERROR_002.getMessage());
        System.out.println(RoleTypeEnum.SYSTEM_ROLE.getValue());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置全局时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        User user = new User(1, "huangduo", "黄铎", LocalDateTime.now(), LocalDateTime.now());

//        InvitationEvent invitationEvent = new InvitationEvent(null, 1, user, "fdsfa", user);
//
//        String payload = invitationEvent.getMessage().getPayload();
//        System.out.println(payload);
//
//        InvitationEvent.InvitationMessagePayload deserialize = InvitationEvent.InvitationMessagePayload.deserialize(payload, InvitationEvent.InvitationMessagePayload.class);
//        System.out.println(deserialize);

        TemplateRenderServiceImpl templateRenderService = new TemplateRenderServiceImpl(new FreemarkerConfig().freemarkerConfiguration());

        Map o = new HashMap<String, Object>();
        o.put("isInviter", false);
        o.put("isInvitee", false);
        o.put("inviter", user.getNickname());
        o.put("invitee", "wangxiaoying");

        String s = templateRenderService.render("""
                <#if isInviter>
                  你已邀请 ${invitee} 加入你的家庭
                <#elseif isInvitee>
                  ${inviter} 邀请你加入他的家庭
                <#else>
                  ${inviter} 邀请 ${invitee} 加入你的家庭
                </#if>
                """, o);

        System.out.println(s);

        List<? extends Animal> animals = new ArrayList<>(); // readonly

//        Animal animal = animals.get(0);
        List<? super Dog> dogs = new ArrayList<Animal>(); // writable


        dogs.add(new Dog("dog"));

        test(1, 2, 3);

    }

    public static void test(int a, int... ints) {
        System.out.println("First params is: " + a);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    public void eat() {
        System.out.println("dog eat");
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    public void eat() {
        System.out.println("cat eat");
    }
}

class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
