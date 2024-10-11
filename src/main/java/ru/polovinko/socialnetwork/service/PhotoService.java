package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.PhotoDTO;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
  List<PhotoDTO> findAllPhotosForUser(long userId);

  Optional<PhotoDTO> photoById(long id);

  PhotoDTO upload(PhotoDTO dto);
  void deleteById(long id);
}
