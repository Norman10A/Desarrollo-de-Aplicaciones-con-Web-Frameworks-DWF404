package sv.edu.udb.repository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Post;
import java.util.List;
import java.util.Objects;
@Repository
public class PostRepository {
    private final SessionFactory sessionFactory;
    public PostRepository(final SessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(sessionFactory);
    }
    public List<Post> findAll() {
        final String QUERY = "from Post p";
        return sessionFactory
                .getCurrentSession()
                .createQuery(QUERY, Post.class)
                .getResultList();
    }
    public Post findById(final Long id) {
        return sessionFactory
                .getCurrentSession()
                .find(Post.class, id);
    }
    @Transactional
    public void save(final Post post) {
        sessionFactory.getCurrentSession().persist(post);
    }
    public void delete(final Post post) {
        sessionFactory.getCurrentSession().remove(post);
    }
    public void deleteById(final Long id) {
        final String QUERY = "delete from Post post where post.id=:id";
        sessionFactory.getCurrentSession()
                .createMutationQuery (QUERY)
                .setParameter("id", id)
                .executeUpdate();
    }
}
