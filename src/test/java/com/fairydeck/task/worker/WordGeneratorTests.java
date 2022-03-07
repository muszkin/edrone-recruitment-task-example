package com.fairydeck.task.worker;

import com.fairydeck.task.model.Job;
import com.fairydeck.task.repository.JobRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Set;
import java.util.regex.Pattern;


@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class WordGeneratorTests {

    private final static WordGenerator wordGenerator = new WordGenerator(Mockito.mock(JobRepository.class));

    @Test
    void testGetRandomWord() {
        String word = wordGenerator.getRandomWord(3,new char[]{'a', 'b','c'});

    Assertions.assertThat(word)
        .containsAnyOf("a", "b", "c")
        .doesNotContainPattern(Pattern.compile("[^abc]"))
        .hasSize(3);
    }

    @Test
    void testGenerateStringsList() {
        Job job = new Job(1,3, BigInteger.valueOf(10),"abc");
        Set<String> words = wordGenerator.generateStringsList(job);

        Assertions
                .assertThat(words)
                .doesNotHaveDuplicates()
                .allMatch(e -> e.matches("[abc]*"))
                .allMatch(e -> e.length() >= 1 && e.length() <= 3)
                .hasSize(10)
        ;
    }
}
