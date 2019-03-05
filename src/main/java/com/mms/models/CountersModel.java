package com.mms.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CountersModel {

    private Map<String, MutableInt> words = new HashMap<>();
    private Map<String, MutableInt> types = new HashMap<>();

    public void countWords(String word) {
        MutableInt count = words.get(word);
        if (count == null) {
            words.put(word, new MutableInt());
        } else {
            count.increment();
        }
    }

    public void countTypes(String type) {
        MutableInt count = types.get(type);
        if (count == null) {
            types.put(type, new MutableInt());
        } else {
            count.increment();
        }
    }

    private String printMap(Map<String, MutableInt> counters) {
        StringBuffer strb = new StringBuffer();
        counters.entrySet().forEach(entry -> {
            strb.append(entry.getKey()).append(" -> ").append(entry.getValue().get()).append(", ");
        });
        return strb.toString().substring(0, strb.length() - 2);
    }

    public String printTypes() {
        return printMap(types);
    }

    public String printWords() {
        return printMap(words);
    }

    class MutableInt {

        int value = 1;

        public void increment() {
            ++value;
        }

        public int get() {
            return value;
        }
    }
}
