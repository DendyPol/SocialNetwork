package ru.polovinko.socialnetwork.mapper;

import org.mapstruct.*;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.model.*;

@Mapper(componentModel = "spring")
public interface CommonMapper {
  UserDTO toUserDTO(User user);

  User toUser(UserDTO dto);

  User toCreateUser(UserCreateDTO dto);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateUserFromDTO(UserUpdateDTO dto, @MappingTarget User user);

  PostDTO toPostDTO(Post post);

  Post toPost(PostDTO dto);

  Post toCreatePost(PostCreateDTO dto);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePostFromDTO(PostUpdateDTO dto, @MappingTarget Post post);

  PhotoDTO toPhotoDTO(Photo photo);

  Photo toPhoto(PhotoDTO dto);

  Photo toCreatePhoto(PhotoCreateDTO dto);

  LikeDTO toLikeDTO(Like like);

  FriendRequestDTO toFriendRequestDTO(FriendRequest friendRequest);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFriendRequestFromDTO(FriendRequestUpdateDTO dto, @MappingTarget FriendRequest friendRequest);

  CommentDTO toCommentDTO(Comment comment);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateCommentFromDTO(CommentUpdateDTO dto, @MappingTarget Comment comment);
}
