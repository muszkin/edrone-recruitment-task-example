package com.fairydeck.task.repository;

import com.fairydeck.task.model.Job;
import com.fairydeck.task.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findFirstByStatus(Status status);

    boolean existsByFilename(String filename);
}
