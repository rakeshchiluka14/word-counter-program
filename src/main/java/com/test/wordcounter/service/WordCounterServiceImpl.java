package com.test.wordcounter.service;

import com.test.wordcounter.externalservice.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Service
public class WordCounterServiceImpl implements WordCounterService {

    private Translator translator;
    private Predicate<String> isAlphabets = word -> Optional.of(word.chars().allMatch(Character::isAlphabetic)).orElse(Boolean.FALSE);
    private Predicate<String> isEmptyWord = word -> !word.isEmpty();

    @Autowired
    public WordCounterServiceImpl(Translator translator) {
        this.translator = translator;
    }

    private Map<String, Integer> wordCounterMap = new ConcurrentHashMap<>();

    public int counter(final String word) {
        final String translatedWord = translator.translate(word.toLowerCase());
        return wordCounterMap.getOrDefault(translatedWord, 0);
    }

    public void add(final String words) {
        Arrays.stream(words.toLowerCase().split("[ ,!&@$%^\\n.':]"))
                .filter(isAlphabets.and(isEmptyWord))
                .map(translator::translate)
                .forEach(this::addToCounter);
    }

    private void addToCounter(String word) {
        final Integer count = wordCounterMap.getOrDefault(word, 0) + 1;
        wordCounterMap.put(word, count);
    }
}
