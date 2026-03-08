package matriphics.schedulerengine;

import lombok.RequiredArgsConstructor;

import matriphics.schedulerengine.entity.JobEntity;
import matriphics.schedulerengine.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class JobSchedulerService {

    private final ExecutorService workerPool = Executors.newFixedThreadPool(5);

    private final JobRepository jobRepository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processJobs() {

        List<JobEntity> jobs = jobRepository.fetchReadyJobs();

        for (JobEntity job : jobs) {

            job.setStatus("RUNNING");
            jobRepository.save(job);

            workerPool.submit(() -> {

                try {

                    executeJob(job);

                    job.setStatus("COMPLETED");

                } catch (Exception e) {

                    handleFailure(job);

                }

                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);

            });

        }
        }

    private void handleFailure(JobEntity job) {

        int retry = job.getRetryCount() + 1;

        if (retry > job.getMaxRetries()) {

            job.setStatus("FAILED");

        } else {

            job.setRetryCount(retry);
            job.setStatus("CREATED");

            job.setNextRetryAt(LocalDateTime.now()
                    .plusSeconds(10 * retry)); // exponential delay
        }
    }

    @Transactional
    public void executeJob(JobEntity job) {

        try {

            System.out.println("Executing job " + job.getId());

            Thread.sleep(2000);

            job.setStatus("COMPLETED");

        } catch (Exception e) {

            job.setStatus("FAILED");

        }

        job.setUpdatedAt(LocalDateTime.now());

        jobRepository.save(job);
    }

}