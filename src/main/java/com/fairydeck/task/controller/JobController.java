package com.fairydeck.task.controller;

import com.fairydeck.task.exception.TooBigLimitException;
import com.fairydeck.task.model.Job;
import com.fairydeck.task.model.request.AddJobRequest;
import com.fairydeck.task.model.response.AddJobResponse;
import com.fairydeck.task.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
public class JobController {

    private JobService jobService;

    @GetMapping
    public List<Job> getJobs() {
        return jobService.getAllJobs();
    }

    @PostMapping
    public ResponseEntity<AddJobResponse> createJob(@RequestBody @Nullable AddJobRequest addJobRequest) {
        try {
            return ResponseEntity.ok(new AddJobResponse(jobService.createJob(addJobRequest), null));
        } catch (TooBigLimitException tooBigLimitException) {
            return ResponseEntity.badRequest().body(new AddJobResponse(null, tooBigLimitException.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Job> getFileById(@PathVariable int id, HttpServletResponse response) {
        jobService.setFileToResponse(id, response);
        return ResponseEntity.ok().build();
    }
}
