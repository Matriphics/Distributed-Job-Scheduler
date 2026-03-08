package matriphics.schedulerapi.controller;

import matriphics.schedulerapi.entity.JobEntity;
import matriphics.schedulerapi.repository.JobRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @PostMapping
    public JobEntity createJob(@RequestBody JobEntity job) {
        Optional<JobEntity> existing =
                jobRepository.findByIdempotencyKey(job.getIdempotencyKey());

        if(existing.isPresent()) {
            return existing.get();
        }

        // 2️⃣ Create new job
        job.setStatus("CREATED");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }
}