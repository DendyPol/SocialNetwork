package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.config.ContainerEnvironment;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.PhotoService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PhotoServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private PhotoService service;
  @Autowired
  private PhotoRepository photoRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    photoRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void findAllPhotosForUserShouldReturnPhotosWhenUserExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .posts(new ArrayList<>())
      .gallery(new ArrayList<>())
      .friends(new ArrayList<>())
      .sentRequest(new ArrayList<>())
      .receivedRequests(new ArrayList<>())
      .build()
    );
    var photoOne = photoRepository.save(Photo.builder()
      .url("http://example.ru/photo1.jpg")
      .user(user)
      .build()
    );
    var photoTwo = photoRepository.save(Photo.builder()
      .url("http://example.ru/photo2.jpg")
      .user(user)
      .build()
    );
    user.getGallery().add(photoOne);
    user.getGallery().add(photoTwo);
    userRepository.save(user);
    var photos = service.findAllPhotosForUser(user.getId());
    assertAll(
      () -> assertTrue(photos.stream().anyMatch(p -> p.getUrl().equals(photoOne.getUrl()))),
      () -> assertTrue(photos.stream().anyMatch(p -> p.getUrl().equals(photoTwo.getUrl())))
    );
  }

  @Test
  void uploadShouldSavePhotoWhenUserExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = PhotoDTO.builder()
      .userId(user.getId())
      .url("http://example.ru/photo.jpg")
      .build();
    var savedPhoto = service.upload(photo);
    assertAll(
      () -> assertNotNull(savedPhoto.getId()),
      () -> assertEquals(photo.getUrl(), savedPhoto.getUrl()),
      () -> assertEquals(user.getId(), savedPhoto.getUserId())
    );
  }

  @Test
  void deleteByIdShouldDeletePhotoWhenPhotoExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://example.com/photo.jpg")
      .user(user)
      .build()
    );
    service.deleteById(photo.getId());
    assertFalse(photoRepository.existsById(photo.getId()));
  }

  @Test
  void photoByIDShouldReturnPhotoWhenExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://exmaple.ru/photo.jpg")
      .user(user)
      .build()
    );
    var result = service.photoById(photo.getId());
    assertTrue(result.isPresent());
    assertEquals(photo.getUrl(), result.get().getUrl());
  }

  @Test
  void photoByIdShouldThrowExceptionWhenPhotoDoesNotExist() {
    var exception = assertThrows(ObjectNotFoundException.class, () -> service.photoById(999L));
    assertEquals("Photo with ID 999 not found", exception.getMessage());
  }
}
