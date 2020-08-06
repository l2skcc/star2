package carrental;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StarRepository extends PagingAndSortingRepository<Star, Long>{


    List<Star> findByResrvNo(String resrvNo);

}