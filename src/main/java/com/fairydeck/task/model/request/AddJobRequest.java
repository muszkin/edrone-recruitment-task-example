package com.fairydeck.task.model.request;

import lombok.Data;

@Data
public class AddJobRequest {
    private int min = 1;
    private int max = 8;
    private int limit = 1;
    private String charMap = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
