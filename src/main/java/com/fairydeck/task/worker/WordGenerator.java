package com.fairydeck.task.worker;

import com.fairydeck.task.model.Job;
import com.fairydeck.task.model.Status;
import com.fairydeck.task.repository.JobRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@EnableAsync
@Component
public class WordGenerator {

  private JobRepository jobRepository;

  /**
   * Save Random for later use.
   */
  private final Random random = new Random();

  /**
   * Method to generate words. Supplied from database.
   */
  @Async
  @Scheduled(fixedRate = 1000)
  public void generateWords() {
    Job job = jobRepository.findFirstByStatus(Status.AWAITING).orElse(null);
    if (job != null) {
      job.setStatus(Status.IN_PROGRESS);
      jobRepository.saveAndFlush(job);
      Set<String> randomWords = generateStringsList(job);
      job.setFilename(generateFileWithStringsList(randomWords).getName());
      job.setStatus(Status.FINISHED);
      jobRepository.saveAndFlush(job);
    }
  }

  /**
   * Method to save generated words into file
   * @param generatedStrings set of strings
   * @return File
   */
  private File generateFileWithStringsList(Set<String> generatedStrings) {
    File file = generateFileWithName();
    try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      for (String s : generatedStrings) {
        fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return file;
  }

  /**
   * Generate filename.
   * @return File
   */
  private File generateFileWithName() {
    String name;
    do {
      name = UUID.randomUUID() + ".txt";
    } while (jobRepository.existsByFilename(name));
    return new File(name);
  }

  /**
   * Method generates strings.
   * @param job Job
   * @return Set of strings
   */
  public Set<String> generateStringsList(Job job) {
    Set<String> set = new HashSet<>();
    while ( BigInteger.valueOf(set.size()).compareTo(job.getCount()) < 0 ) {
      String generatedString = getRandomWord(random.nextInt(job.getMax() + 1 - job.getMin()) + job.getMin(), job.getCharMap().toCharArray());
      set.add(generatedString);
    }
    return set;
  }

  /**
   * Generate one random word
   * @param length of the word
   * @param allowedChars chars to use
   * @return string
   */
  public String getRandomWord(int length, char[] allowedChars) {
    StringBuilder stringBuilder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char newChar = allowedChars[random.nextInt(allowedChars.length)];
      stringBuilder.append(newChar);
    }
    return stringBuilder.toString();
  }
}
