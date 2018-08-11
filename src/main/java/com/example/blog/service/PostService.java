package com.example.blog.service;

import com.example.blog.model.entity.Comment;
import com.example.blog.model.entity.Post;
import com.example.blog.model.form.PostForm;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    CommentRepository commentRepository;
    PostRepository postRepository;

    @Autowired
    public PostService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Post createPost(PostForm postForm, String email) {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCategory(postForm.getCategory());
        // automatyczne dodanie email zalogowanego usera
        post.setAuthor(email);
        return postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAllByCategoryLikeOrderByAddedDesc("%");
    }

    public Post getOnePost(Long id) {
        return postRepository.findOneById(id);
    }

    public void deleteOnePost(Long id) {
        //wyszukanie posta po ID
        Post deletedPost = postRepository.getOne(id);
        //usunięcie posta
        postRepository.delete(deletedPost);
    }

    public PostForm getPostToEdit(Long id) {
        Post editedPost = postRepository.getOne(id);
        PostForm postForm = new PostForm();
        postForm.setTitle(editedPost.getTitle());
        postForm.setCategory(editedPost.getCategory());
        postForm.setContent(editedPost.getContent());
        return postForm;
    }

    public Post editPost(Long id, PostForm postForm) {
        //obiekt do edycji
        Post editedPost = postRepository.getOne(id);
        //przepisanie pól z postForm
        editedPost.setTitle(postForm.getTitle());
        editedPost.setCategory(postForm.getCategory());
        editedPost.setContent(postForm.getContent());
        return postRepository.save(editedPost);
    }

    public boolean addComment(Long id, Comment comment, String email) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (!postOptional.isPresent()) {
            System.out.println("brak takiego ID");
            return false;
        }

        Post currentPost = postOptional.get();
        comment.setPost(currentPost);
        comment.setAuthor(email);
        currentPost.setComments(comment);
//        commentRepository.save(comment);
        postRepository.save(currentPost);
//        Post currentPost = postRepository.getOne(id);
//        comment.setAuthor(email);
//        currentPost.setComments(comment);
//        commentRepository.save(comment);
//        postRepository.save(currentPost);
        return true;
    }
    public Comment getOneComment(Long id_comment){
        return commentRepository.getOne(id_comment);
    }
    public void deleteOneComment(Long id_comment){
        Comment comment = commentRepository.getOne(id_comment);
        Post post = comment.getPost();
        post.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
