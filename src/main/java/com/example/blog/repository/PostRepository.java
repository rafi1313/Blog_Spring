package com.example.blog.repository;

import com.example.blog.model.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    // lista rekordów z tabeli
    List<Post> findAll();
    //posortowana lista rekordów
    List<Post> findAllByCategoryLikeOrderByAddedDesc(String category);
    // zapytanie native SQL
//    @Query(value="select p from post as p order by p.added desc ",nativeQuery = true)
//    List<Post> getOrderedPosts();
//    @Query("select p from post p where p.id=:id")
//    List<Post> myQuery(@Param("id") int id);

    // zwraca post po id
    Post findOneById(Long id);
}
