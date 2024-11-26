package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;

public interface PhotoService {
  Page<PhotoDTO> search(PhotoSearchDTO dto, Pageable pageable);

  PhotoDTO create(PhotoCreateDTO dto);

  void delete(long id);
}
