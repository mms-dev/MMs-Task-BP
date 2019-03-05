package com.mms.models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mms.entities.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Component
public class ProcessModel {

    @Value("${exe_path}")
    String path;

    @Autowired
    CountersModel countService;

    final ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            Process process = Runtime.getRuntime()
                    .exec(path);

            EventStream eventStream
                    = new EventStream(process.getInputStream(), event -> {
                        if (event != null) {
                            countService.countTypes(event.getEvent_type());
                            countService.countWords(event.getData());
                        }
                    });
            Executors.newSingleThreadExecutor().submit(eventStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventStream implements Runnable {

        private InputStream inputStream;
        private Consumer<Event> consumer;

        public EventStream(InputStream inputStream, Consumer<Event> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .map(s -> {
                        Event event = null;
                        try {
                            event = jsonMapper.readValue(s, Event.class);
                        } catch (IOException e) {
                        }
                        return event;
                    }).forEach(consumer);
        }
    }
}
