package matriphics.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class Job {

    private UUID id;
    private String type;
    private String payload;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Job() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.CREATED;
    }

    // getters and setters
}