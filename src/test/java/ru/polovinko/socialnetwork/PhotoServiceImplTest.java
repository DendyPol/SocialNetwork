package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.config.ContainerEnvironment;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.PhotoService;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PhotoServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private PhotoService photoService;
  @Autowired
  private UserService userService;
  @Autowired
  private PhotoRepository photoRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void findAllPhotosForUserShouldReturnPhotosWhenUserExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("password")
      .email("user@example.ru")
      .build()
    );
    var photoOne = photoService.create(PhotoCreateDTO.builder()
      .userId(user.getId())
      .url("http://example.ru/photo1.jpg")
      .build()
    );
    var photoTwo = photoService.create(PhotoCreateDTO.builder()
      .userId(user.getId())
      .url("http://example.ru/photo2.jpg")
      .build()
    );
    Pageable pageable = PageRequest.of(0, 10);
    var searchDTO = new PhotoSearchDTO();
    Page<PhotoDTO> photosPage = photoService.search(searchDTO, pageable);
    var photo = photosPage.getContent();
    assertAll(
      () -> assertEquals(2, photo.size()),
      () -> assertTrue(photo.stream().anyMatch(p -> p.getUrl().equals(photoOne.getUrl()))),
      () -> assertTrue(photo.stream().anyMatch(p -> p.getUrl().equals(photoTwo.getUrl())))
    );
  }

  @Test
  void uploadShouldSavePhotoWhenUserExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("password")
      .email("user@example.ru")
      .build()
    );
    var photo = PhotoCreateDTO.builder()
      .userId(user.getId())
      .url("http://example.ru/photo.jpg")
      .build();
    var savedPhoto = photoService.create(photo);
    assertAll(
      () -> assertNotNull(savedPhoto.getId()),
      () -> assertEquals(photo.getUrl(), savedPhoto.getUrl()),
      () -> assertEquals(user.getId(), savedPhoto.getUserId())
    );
  }

  @Test
  void deleteByIdShouldDeletePhotoWhenPhotoExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("password")
      .email("user@example.ru")
      .build()
    );
    var photo = photoService.create(PhotoCreateDTO.builder()
      .userId(user.getId())
      .url("http://example.com/photo.jpg")
      .build()
    );
    photoService.deleteById(photo.getId());
    assertFalse(photoRepository.existsById(photo.getId()));
  }

  @Test
  void photoByIDShouldReturnPhotoWhenExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("password")
      .email("user@example.ru")
      .build()
    );
    var photo = photoService.create(PhotoCreateDTO.builder()
      .userId(user.getId())
      .url("http://exmaple.ru/photo.jpg")
      .build()
    );
    var result = photoService.photoById(photo.getId());
    assertTrue(result.isPresent());
    assertEquals(photo.getUrl(), result.get().getUrl());
  }

  @Test
  void photoByIdShouldThrowExceptionWhenPhotoDoesNotExist() {
    var exception = assertThrows(ObjectNotFoundException.class, () -> photoService.photoById(999L));
    assertEquals("Photo with ID 999 not found", exception.getMessage());
  }
}
