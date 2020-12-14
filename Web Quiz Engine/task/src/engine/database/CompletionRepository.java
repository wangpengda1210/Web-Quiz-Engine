package engine.database;

import engine.elements.Completion;
import engine.elements.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends PagingAndSortingRepository<Completion, Integer> {

    Page<Completion> findByUser(User user, Pageable pageable);

}
