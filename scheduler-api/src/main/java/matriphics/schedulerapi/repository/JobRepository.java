package matriphics.schedulerapi.repository;

import matriphics.schedulerapi.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
    Optional<JobEntity> findByIdempotencyKey(String idempotencyKey);

}