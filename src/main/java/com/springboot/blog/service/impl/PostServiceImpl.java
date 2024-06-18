package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    // Convert DTO to Entity
    private Post mapToEntity(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .content(postDto.getContent())
                .build();

        return post;
    }

    // Convert Post Entity to PostDto
    private PostDto mapToDTO(Post post) {
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .build();

        return postDto;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // Convert DTO to Entity
        Post post = mapToEntity(postDto);

        // Save to database
        Post savedPost = postRepository.save(post);

        // Convert Entity to DTO
        PostDto savedPostDto = mapToDTO(savedPost);

        return savedPostDto;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        List<PostDto> postsDto = posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        return postsDto;
    }

//    @Override
//    public List<PostDto> getAllPosts(int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//        Page<Post> posts = postRepository.findAll(pageable);
//
//        List<Post> postList = posts.getContent();
//
//        List<PostDto> postsDto = postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
//
//        return postsDto;
//    }

    @Override
    public PostDto getPostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        postRepository.deleteById(postId);
    }

}