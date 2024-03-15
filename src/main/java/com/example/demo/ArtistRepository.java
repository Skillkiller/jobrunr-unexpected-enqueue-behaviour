package com.example.demo;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ArtistRepository extends ListCrudRepository<Artist, UUID> {
}
