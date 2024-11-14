package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.dto.LikeSearchDTO;

public interface LikeService {
  LikeDTO create(LikeCreateDTO dto);

  void delete(long id);

  Page<LikeDTO> search(LikeSearchDTO dto, Pageable pageable);
}
