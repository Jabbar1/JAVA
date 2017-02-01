package com.shaik.exception.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jabbars on 2/1/2017.
 */

public class Fault {
    @JsonProperty
    String tid;
    @JsonProperty
    List<Message> messages;

    public Fault(@JsonProperty("tid") String tid, @JsonProperty("messages") List<Fault.Message> messages) {
        this.tid = tid;
        this.messages = messages;
    }

    public Fault(String message, String code) {
        this.messages = new ArrayList(1);
        this.messages.add(new Fault.Message(message, code, Collections.emptyMap()));
    }

    public Fault(String tid, String message, String code) {
        this.tid = tid;
        this.messages = new ArrayList(1);
        this.messages.add(new Fault.Message(message, code, Collections.emptyMap()));
    }

    public Fault(String message, String code, Map<String, Object> argsMaps) {
        this.messages = new ArrayList(1);
        this.messages.add(new Fault.Message(message, code, argsMaps));
    }

    public Fault(String tid, String message, String code, Map<String, Object> argsMaps) {
        this.tid = tid;
        this.messages = new ArrayList(1);
        this.messages.add(new Fault.Message(message, code, argsMaps));
    }

    public List<Fault.Message> getMessages() {
        return this.messages;
    }

    public String getTid() {
        return this.tid;
    }

    public static class Message {
        private String message;
        private String code;
        private Map<String, Object> argsMap = Collections.emptyMap();

        public Message(@JsonProperty("message") String message, @JsonProperty("code") String code, @JsonProperty("argsMap") Map<String, Object> argsMap) {
            this.message = message;
            this.code = code;
            this.argsMap = argsMap;
        }

        public String getMessage() {
            return this.message;
        }

        public String getCode() {
            return this.code;
        }

        public Map<String, Object> getArgsMap() {
            return this.argsMap;
        }
    }
}

