package ru.polovinko.socialnetwork.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;

public interface PhotoService {
  Page<PhotoDTO> search(@Valid PhotoSearchDTO dto, Pageable pageable);

  PhotoDTO create(@Valid PhotoCreateDTO dto);

  void delete(long id);
}
