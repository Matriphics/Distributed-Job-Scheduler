package matriphics.schedulerengine.repository;



import matriphics.schedulerengine.entity.JobEntity;
import matriphics.schedulerengine.entity.JobEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {

    @Query(value = """
SELECT * FROM jobs
WHERE status='CREATED'
AND (next_retry_at IS NULL OR next_retry_at <= NOW())
ORDER BY priority DESC, created_at ASC
LIMIT 10
FOR UPDATE SKIP LOCKED
""", nativeQuery = true)
    List<JobEntity> fetchReadyJobs();

    Optional<JobEntity> findByIdempotencyKey(String key);
}