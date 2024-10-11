package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.socialnetwork.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
