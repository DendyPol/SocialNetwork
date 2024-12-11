package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.specification.PhotoSpecification;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {
  private final PhotoRepository photoRepository;
  private final UserService userService;
  private final CommonMapper commonMapper;

  @Override
  public Page<PhotoDTO> search(PhotoSearchDTO dto, Pageable pageable) {
    var spec = new PhotoSpecification(dto);
    return photoRepository.findAll(spec, pageable)
      .map(commonMapper::toPhotoDTO);
  }

  @Override
  public PhotoDTO create(PhotoCreateDTO dto) {
    var user = userService.search(UserSearchDTO.builder()
        .userId(dto.getUserId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toUser)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var photo = new Photo(0L, dto.getUrl(), user);
    return commonMapper.toPhotoDTO(photoRepository.save(photo));
  }

  @Override
  public void delete(long id) {
    photoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", id)));
    photoRepository.deleteById(id);
  }
}
