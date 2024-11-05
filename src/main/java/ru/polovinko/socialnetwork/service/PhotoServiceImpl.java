package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.PhotoCreateDTO;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.specification.PhotoSpecification;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {
  private final PhotoRepository photoRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public Page<PhotoDTO> search(PhotoSearchDTO dto, Pageable pageable) {
    var spec = new PhotoSpecification(dto);
    return photoRepository.findAll(spec, pageable)
      .map(photo -> modelMapper.map(photo, PhotoDTO.class));
  }

  @Override
  public Optional<PhotoDTO> photoById(long id) {
    var photo = photoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", id)));
    return Optional.of(modelMapper.map(photo, PhotoDTO.class));
  }

  @Override
  public PhotoDTO create(PhotoCreateDTO dto) {
    var userDTO = userService.findById(dto.getUserId()).get();
    var user = modelMapper.map(userDTO, User.class);
    var photo = modelMapper.map(dto, Photo.class);
    photo.setUser(user);
    var savedPhoto = photoRepository.save(photo);
    return modelMapper.map(savedPhoto, PhotoDTO.class);
  }

  @Override
  public void deleteById(long id) {
    photoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", id)));
    photoRepository.deleteById(id);
  }
}
