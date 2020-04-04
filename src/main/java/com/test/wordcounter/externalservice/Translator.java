package com.test.wordcounter.externalservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class Translator {

    private static Map<String, String> translatorMap = new ConcurrentHashMap<>();

    static {
        translatorMap.put("flower", "flower");
        translatorMap.put("flor", "flower");
        translatorMap.put("blume", "flower");
    }

    public String translate(String word) {
        return translatorMap.getOrDefault(word, word);
    }
}
