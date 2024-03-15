package com.example.demo;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.springframework.stereotype.Component;

@Component
public class Task {

    @Job(name = "Say hello to %0")
    public void task(String name, JobContext jobContext) {
        System.out.println("Hello, " + name + "!");
    }

}
