package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.dto.LikeSearchDTO;
import ru.polovinko.socialnetwork.exception.AlreadyExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.LikeRepository;
import ru.polovinko.socialnetwork.specification.LikeSpecification;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final PostService postService;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public LikeDTO create(LikeCreateDTO dto) {
    var postDTO = postService.findById(dto.getPostId()).get();
    var userDTO = userService.findById(dto.getUserId()).get();
    var post = modelMapper.map(postDTO, Post.class);
    var user = modelMapper.map(userDTO, User.class);
    if (likeRepository.existsByPostAndUser(post, user)) {
      throw new AlreadyExistException("User has already liked this post");
    }
    var like = new Like();
    like.setUser(user);
    like.setPost(post);
    likeRepository.save(like);
    return modelMapper.map(like, LikeDTO.class);
  }

  @Override
  public void delete(long id) {
    var like = likeRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Like with ID %d not found", id)));
    likeRepository.delete(like);
  }

  @Override
  public Page<LikeDTO> search(LikeSearchDTO dto, Pageable pageable) {
    var spec = new LikeSpecification(dto);
    return likeRepository.findAll(spec, pageable)
      .map(like -> modelMapper.map(like, LikeDTO.class));
  }
}
