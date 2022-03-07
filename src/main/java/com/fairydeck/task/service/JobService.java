package com.fairydeck.task.service;

import com.fairydeck.task.exception.TooBigLimitException;
import com.fairydeck.task.model.Job;
import com.fairydeck.task.repository.JobRepository;
import com.fairydeck.task.model.Status;
import com.fairydeck.task.model.request.AddJobRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Add new Job to db. Assert some values.
     * @param addJobRequest job request
     * @return Job
     * @throws TooBigLimitException is thrown when you want more words that we can generate
     */
    public Job createJob(AddJobRequest addJobRequest) throws TooBigLimitException {
        if (addJobRequest == null) {
            addJobRequest = new AddJobRequest();
        }
        if (addJobRequest.getMin() < 1) {
            addJobRequest.setMin(1);
        }
        if (addJobRequest.getMax() < 1) {
            addJobRequest.setMax(1);
        }
        if (addJobRequest.getLimit().compareTo(BigInteger.ONE) < 0) {
            addJobRequest.setLimit(BigInteger.ONE);
        }
        if (addJobRequest.getMin() > addJobRequest.getMax()) {
            addJobRequest.setMin(addJobRequest.getMax());
            addJobRequest.setMax(addJobRequest.getMin());
        }
        if (getPossiblePermutations(addJobRequest).compareTo(addJobRequest.getLimit()) < 0) {
            throw new TooBigLimitException("Number of possible random words is less than number you want.");
        }
        Job job = new Job(addJobRequest.getMin(), addJobRequest.getMax(), addJobRequest.getLimit(), addJobRequest.getCharMap());
        job.setStatus(Status.AWAITING);
        jobRepository.saveAndFlush(job);
        return job;
    }

    /**
     * Get generated file and return is as body of http response.
     * @param id job id
     * @param response actual response
     * @return File
     */
    public File setFileToResponse(int id, HttpServletResponse response) {
        File file = new File(jobRepository.getById(id).getFilename());
        try( FileInputStream fileInputStream = new FileInputStream(file)) {
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Get all possible permutations with repeats
     * @param jobRequest job request
     * @return BigInteger
     */
    public BigInteger getPossiblePermutations(AddJobRequest jobRequest) {
        return IntStream
                .rangeClosed(jobRequest.getMin(), jobRequest.getMax())
                .mapToObj(n -> getPower(n, jobRequest.getCharMap().length()))
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
    }

    /**
     * Get power of BigInteger
     * @param base base
     * @param power exponent
     * @return BigInteger
     */
    public BigInteger getPower(int base, int power) {
        BigInteger baseBig = BigInteger.valueOf(base);
        return baseBig.pow(power);
    }
}
