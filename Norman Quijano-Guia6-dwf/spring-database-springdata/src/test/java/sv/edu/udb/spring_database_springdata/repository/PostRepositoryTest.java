package sv.edu.udb.spring_database_springdata.repository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.spring_database_springdata.repository.domain.Post;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void init() {
        Post post = Post.builder()
                .id(1L)
                .title("Anything you want to write")
                .postDate(LocalDate.of(2024, 8, 24))
                .build();
        postRepository.save(post);
    }

    @AfterEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    void shouldHasOnePost_When_FindAll() {
        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertEquals(1, posts.size());
    }

    @Test
    void shouldGetPost_When_IdExist() {
        Post post = postRepository.findById(1L).orElse(null);
        assertNotNull(post);
        assertEquals("Anything you want to write", post.getTitle());
    }

    @Test
    @Transactional
    void shouldSavePost_When_PostIsNew() {
        Post newPost = Post.builder()
                .id(2L)
                .title("New Post")
                .postDate(LocalDate.of(2024, 8, 25))
                .build();

        postRepository.save(newPost);
        Post savedPost = postRepository.findById(2L).orElse(null);

        assertNotNull(savedPost);
        assertEquals("New Post", savedPost.getTitle());
    }

    @Test
    void shouldDeletePost_When_PostExist() {
        Post newPost = Post.builder()
                .id(3L)
                .title("To be deleted")
                .postDate(LocalDate.of(2024, 8, 26))
                .build();

        postRepository.save(newPost);
        postRepository.delete(newPost);

        assertNull(postRepository.findById(3L).orElse(null));
    }
}
