Distributed Job Scheduler

A backend system for processing background jobs asynchronously using a database-backed job queue and worker thread pool.

The system separates job creation from job execution, allowing tasks to run asynchronously without blocking API requests.

Architecture
Client
  │
  ▼
scheduler-api
  │
  ▼
MySQL Job Queue
  │
  ▼
scheduler-engine
  │
  ▼
Worker Thread Pool
Components

scheduler-api

Accepts job creation requests

Stores jobs in the database

scheduler-engine

Polls jobs from the database

Executes them using a worker pool

MySQL

Acts as a persistent job queue

Features Implemented
Asynchronous Job Execution

Jobs are executed using a worker thread pool.

ExecutorService workerPool = Executors.newFixedThreadPool(5)

Multiple jobs can run in parallel.

Database Job Queue

Jobs are stored in a database table and picked by the scheduler engine.

Example query used to fetch jobs:

SELECT * FROM jobs
WHERE status='CREATED'
AND (next_retry_at IS NULL OR next_retry_at <= NOW())
ORDER BY priority DESC, created_at ASC
LIMIT 10
FOR UPDATE SKIP LOCKED

This ensures:

safe distributed job processing

no duplicate job execution

Retry Handling

Failed jobs are retried until the maximum retry limit is reached.

Example logic:

retryCount++
nextRetryAt = now + retryDelay

If retries exceed the limit:

status = FAILED
Priority Scheduling

Jobs can be assigned priorities.

Higher priority jobs are executed first.

ORDER BY priority DESC
Idempotent Job Creation

Clients provide an idempotency key when creating a job.

If the same request is retried with the same key, the system returns the existing job instead of creating a duplicate.

Example request:

{
  "type": "EMAIL",
  "payload": "send welcome email",
  "priority": 5,
  "idempotencyKey": "signup-user-42"
}
API
Create Job
POST /jobs

Example request:

{
  "type": "EMAIL",
  "payload": "send welcome email",
  "priority": 5,
  "idempotencyKey": "signup-user-42"
}
Tech Stack

Java 21

Spring Boot

Spring Data JPA

MySQL

Maven

Future Improvements

Planned enhancements:

job monitoring APIs

dispatcher + worker architecture

Kafka-based job queue

job monitoring dashboard
