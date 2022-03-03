package com.fairydeck.task.model.response;

import com.fairydeck.task.model.Job;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddJobResponse {
    private Job job;
    private String error;
}
