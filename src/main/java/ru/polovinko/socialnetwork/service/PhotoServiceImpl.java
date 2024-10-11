package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.PhotoDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {
  private final PhotoRepository photoRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<PhotoDTO> findAllPhotosForUser(long userId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    return user.getGallery().stream()
      .map(photo -> modelMapper.map(photo, PhotoDTO.class))
      .collect(Collectors.toList());
  }

  @Override
  public Optional<PhotoDTO> photoById(long id) {
    var photo = photoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", id)));
    return Optional.of(modelMapper.map(photo, PhotoDTO.class));
  }

  @Override
  public PhotoDTO upload(PhotoDTO dto) {
    var user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
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
