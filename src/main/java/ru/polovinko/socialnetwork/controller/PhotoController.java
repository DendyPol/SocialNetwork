package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;
import ru.polovinko.socialnetwork.service.PhotoService;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {
  private final PhotoService photoService;

  @PostMapping("/search")
  public Page<PhotoDTO> search(@RequestBody PhotoSearchDTO dto, Pageable pageable) {
    return photoService.search(dto, pageable);
  }

  @PostMapping("/create")
  public PhotoDTO create(@RequestBody PhotoCreateDTO dto) {
    return photoService.create(dto);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    photoService.delete(id);
  }
}
