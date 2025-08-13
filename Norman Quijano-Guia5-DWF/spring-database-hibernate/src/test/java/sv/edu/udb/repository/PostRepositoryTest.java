package sv.edu.udb.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Post;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    @Transactional
    void initData() {
        Post newPost = Post.builder()
                .id(1L)
                .title("SpringBoot as a back-end")
                .postDate(LocalDate.of(2023, 9, 29))
                .build();

        postRepository.save(newPost);
    }

    @AfterEach
    @Transactional
    void cleanData() {
        postRepository.deleteById(1L);
    }

    @Test
    @Transactional
    void shouldHasOnePost_When_FindAll() {
        List<Post> actualPostList = postRepository.findAll();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());
    }

    @Test
    @Transactional
    void shouldGetPost_When_IdExist() {
        Post actualPost = postRepository.findById(1L);
        assertNotNull(actualPost);
        assertEquals(1L, actualPost.getId());
        assertEquals("SpringBoot as a back-end", actualPost.getTitle());
        assertEquals(LocalDate.of(2023, 9, 29), actualPost.getPostDate());
    }

    @Test
    @Transactional
    void shouldSavePost_When_PostIsNew() {
        Post newPost = Post.builder()
                .id(2L)
                .title("Anything you want to write")
                .postDate(LocalDate.of(2024, 8, 24))
                .build();

        postRepository.save(newPost);

        Post actualPost = postRepository.findById(2L);
        assertNotNull(actualPost);
        assertEquals("Anything you want to write", actualPost.getTitle());

        postRepository.delete(newPost);
    }

    @Test
    @Transactional
    void shouldDeletePost_When_PostExist() {
        Post newPost = Post.builder()
                .id(3L)
                .title("Deleted")
                .postDate(LocalDate.of(2024, 8, 24))
                .build();

        postRepository.save(newPost);

        Post actualPost = postRepository.findById(3L);
        assertNotNull(actualPost);

        postRepository.delete(newPost);

        Post deletedPost = postRepository.findById(3L);
        assertNull(deletedPost);
    }
}

