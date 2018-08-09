package com.example.blog.service;

import com.example.blog.model.entity.Post;
import com.example.blog.model.form.PostForm;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post createPost(PostForm postForm,String email){
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCategory(postForm.getCategory());
        // automatyczne dodanie email zalogowanego usera
        post.setAuthor(email);
        return postRepository.save(post);
    }
    public List<Post> getPosts(){
        return postRepository.findAllByCategoryLikeOrderByAddedDesc("%");
    }
    public Post getOnePost(Long id){
        return postRepository.findOneById(id);
    }
}
