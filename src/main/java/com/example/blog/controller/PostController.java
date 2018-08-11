package com.example.blog.controller;

import com.example.blog.model.entity.Comment;
import com.example.blog.model.entity.Post;
import com.example.blog.model.form.PostForm;
import com.example.blog.service.CommentComparator;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.GeneratedValue;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Controller
public class PostController {


    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/add")
    public String addPost(Model model,Authentication auth) {
        //obiekt do wprowadzania wartości z htmla
        model.addAttribute("postForm", new PostForm());
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        return "addPostForm";
    }

    @PostMapping("/post/add")
    public String addPost(@ModelAttribute @Valid PostForm postForm, BindingResult bindingResult, Authentication auth, Model model) {
        if (bindingResult.hasErrors()) {
            return "addPostForm";
        }
        // zapis do DB przez PostService
        if (auth != null) {
            UserDetails principal = (UserDetails) auth.getPrincipal();
            postService.createPost(postForm, principal.getUsername());
            model.addAttribute("principal",principal);
        } else {
            postService.createPost(postForm, "unknown");
        }
        return "addPostForm";
    }

    @GetMapping("/post/all")
    public String allPosts(Model model, Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        List<Post> postsList = postService.getPosts();
        Collection<GrantedAuthority> authList = (Collection<GrantedAuthority>) principal.getAuthorities();
        Boolean isAdmin = authList.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("postsList", postsList);
        model.addAttribute("isAdmin", isAdmin);
        return "allPostsPage";
    }

    @GetMapping("/post/{id}")
    public String onePost(@PathVariable Long id, Model model,Authentication auth) {
        Post onePost = postService.getOnePost(id);
        model.addAttribute("onePost", onePost);
        UserDetails principal = (UserDetails) auth.getPrincipal();
        Collection<GrantedAuthority> authList = (Collection<GrantedAuthority>) principal.getAuthorities();
        Boolean isAdmin = authList.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("principal",principal);
        List<Comment> commentList = onePost.getComments();
//        sortowanie listy
        commentList.sort(new CommentComparator());
        model.addAttribute("commentList", commentList);
        model.addAttribute("comment", new Comment());
        model.addAttribute("isAdmin", isAdmin);
        return "postPage";
    }
    @PostMapping("/post/{id}")
    public String onePost (@PathVariable Long id, @ModelAttribute @Valid Comment comment, BindingResult bindingResult,Authentication auth, Model model){
        if (bindingResult.hasErrors()){
            return "redirect:/post/"+id;
        }
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        postService.addComment(id,comment,principal.getUsername());
        return "redirect:/post/"+id;
    }

    @GetMapping("/post/delete/{id}")
    public String deleteOnePost(@PathVariable Long id,Authentication auth, Model model) {
        //usunięcie posta przez PostService
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        postService.deleteOnePost(id);
        return "redirect:/post/all";
    }

    @GetMapping("/post/edit/{id}")
    public String editOnePost(@PathVariable Long id, Model model, Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        PostForm postForm = postService.getPostToEdit(id);
        //z serwisu przepisujemy pola z PostForm do Post
        model.addAttribute("postForm", postForm);
        model.addAttribute("id,id");
        return "editPostForm";
    }

    @PostMapping("/post/edit/{id}")
    public String editOnePost(@PathVariable Long id,
                              @ModelAttribute @Valid PostForm postForm,
                              BindingResult bindingResult, Authentication auth, Model model) {
        if (bindingResult.hasErrors()) {
            return "editPostForm";
        }
        UserDetails principal = (UserDetails) auth.getPrincipal();
        model.addAttribute("principal",principal);
        //update przez service
        postService.editPost(id, postForm);
        return "redirect:/post/all";
    }
    @GetMapping("/post/deleteComment/{id_comment}")
    public String deleteComment(@PathVariable Long id_comment){
        Comment deletedComment = postService.getOneComment(id_comment);
        postService.deleteOneComment(id_comment);
        return "redirect:/post/"+deletedComment.getPost().getId();
    }


    //API bloga
    @GetMapping("/api/all")
    public String postAPI(Model model){
       List<Post> postsList = postService.getPosts();
       model.addAttribute("postsList",postsList);
       return "posts";
    }
}
