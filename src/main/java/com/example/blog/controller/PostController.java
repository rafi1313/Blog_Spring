package com.example.blog.controller;

import com.example.blog.model.entity.Post;
import com.example.blog.model.form.PostForm;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PostController {


    PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/add")
    public String addPost(Model model){
        //obiekt do wprowadzania wartości z htmla
        model.addAttribute("postForm",new PostForm());
        return "addPostForm";
    }
    @PostMapping("/post/add")
    public String addPost(@ModelAttribute @Valid PostForm postForm, BindingResult bindingResult, Authentication auth){
        if (bindingResult.hasErrors()){
            return "addPostForm";
        }
        // zapis do DB przez PostService
        if (auth != null){
            UserDetails principal = (UserDetails) auth.getPrincipal();
            postService.createPost(postForm,principal.getUsername());
        }else {
            postService.createPost(postForm,"unknown");
        }
        return "addPostForm";
    }
    @GetMapping("/post/all")
    public String allPosts(Model model){
        List<Post> postsList = postService.getPosts();
        model.addAttribute("postsList", postsList);
        return "allPostsPage";
    }
    @GetMapping("/post/{id}")
    public String onePost(@PathVariable Long id, Model model){
        Post onePost = postService.getOnePost(id);
        model.addAttribute("onePost", onePost);
        return "postPage";
    }
}
