package me.huangduo.hms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class Test {

    private ObjectMapper objectMapper;

    public Test(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void doTest() throws JsonProcessingException {
        A a = new A(LocalDateTime.now());
        String jsonString = objectMapper.writeValueAsString(a);
        System.out.println(jsonString);
    }
}


class A {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birth;

    public A(LocalDateTime birth) {
        this.birth = birth;
    }

    public A() {
    }

    public void setBirth(LocalDateTime birth) {
        this.birth = birth;
    }

    public LocalDateTime getBirth() {
        return birth;
    }
}