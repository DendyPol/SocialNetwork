package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.service.PhotoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {
  private final PhotoService photoService;

  @GetMapping("/user/{userId}")
  public List<PhotoDTO> findAllPhotosById(@PathVariable long userId) {
    return photoService.findAllPhotosForUser(userId);
  }

  @GetMapping("{id}")
  public PhotoDTO photoById(@PathVariable long id) {
    return photoService.photoById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", id)));
  }

  @PostMapping
  public PhotoDTO uploadPhoto(@RequestBody PhotoDTO dto) {
    return photoService.upload(dto);
  }

  @DeleteMapping("{id}")
  public void deletePhotoById(@PathVariable long id) {
    photoService.deleteById(id);
  }
}
