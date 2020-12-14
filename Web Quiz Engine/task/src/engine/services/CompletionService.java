package engine.services;

import engine.database.CompletionRepository;
import engine.elements.Completion;
import engine.elements.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {

    @Autowired
    private CompletionRepository completionRepository;

    public Page<Completion> getAllCompletionsByUser(Integer pageNo, Integer pageSize,
                                                    String sortBy, User user) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        return completionRepository.findByUser(user, paging);
    }

}
