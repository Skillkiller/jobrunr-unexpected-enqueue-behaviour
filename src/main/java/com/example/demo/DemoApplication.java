package com.example.demo;

import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(JobScheduler jobScheduler,
                                   ArtistRepository artistRepository) {
        return args -> {
            // Create artist fake data
            List.of("Artist 1", "Artist 2", "Artist 3").forEach(name -> {
                Artist artist = new Artist();
                artist.setName(name);
                artistRepository.save(artist);
            });

            // Schedule a job with a stream in bulk
            Stream<Artist> artistStream = artistRepository.findAll().stream();
            jobScheduler.<Task, Artist>enqueue(artistStream, (service, artist) -> service.task(artist.getName(), JobContext.Null));

            // Produces the following output:
            /*
                Hello, Artist 1!
                Hello, Artist 1!
                Hello, Artist 1!
             */

            Thread.sleep(20 * 1000);
            System.out.println();
            System.out.println();

            Stream<String> artistStream2 = artistRepository.findAll().stream().map(Artist::getName);
            jobScheduler.<Task, String>enqueue(artistStream2, (service, artist) -> service.task(artist, JobContext.Null));

            // Produces the following output:
            /*
                Hello, Artist 2!
                Hello, Artist 3!
                Hello, Artist 1!
             */

            Thread.sleep(20 * 1000);
            System.out.println();
            System.out.println();

            Stream<Artist> artistStream3 = Stream.of(
                    new Artist(UUID.randomUUID(), "Artist 4"),
                    new Artist(UUID.randomUUID(), "Artist 5"),
                    new Artist(UUID.randomUUID(), "Artist 6")
            );
            jobScheduler.<Task, Artist>enqueue(artistStream3, (service, artist) -> service.task(artist.getName(), JobContext.Null));

            // Produces the following output:
            /*
                Hello, Artist 4!
                Hello, Artist 4!
                Hello, Artist 4!
             */
        };
    }
}
