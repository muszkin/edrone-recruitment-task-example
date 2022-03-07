package com.fairydeck.task.service;

import com.fairydeck.task.exception.TooBigLimitException;
import com.fairydeck.task.model.request.AddJobRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class JobServiceTests {

    @InjectMocks
    private JobService jobService;

    @Test
    void testGetPossibleCombinations() {
        AddJobRequest addJobRequest = new AddJobRequest();
        addJobRequest.setCharMap("abcdefghij");
        addJobRequest.setMin(1);
        addJobRequest.setMax(8);
        BigInteger possibleCombinations = jobService.getPossiblePermutations(addJobRequest);

        Assertions
                .assertThat(possibleCombinations)
                .isEqualByComparingTo(BigInteger.valueOf(1427557524L))
        ;
    }

    @Test
    void testGetPow() {
        BigInteger factorial = jobService.getPower(2,2);
        Assertions
                .assertThat(factorial)
                .isEqualByComparingTo(BigInteger.valueOf(4))
        ;
    }

    @Test
    void testCreateJob() {
        AddJobRequest addJobRequest = new AddJobRequest();
        addJobRequest.setLimit(BigInteger.valueOf(1000000));
        addJobRequest.setMin(1);
        addJobRequest.setMax(1);
        Assertions
                .assertThatThrownBy(() -> jobService.createJob(addJobRequest))
                .isInstanceOf(TooBigLimitException.class)
        ;
    }
}
