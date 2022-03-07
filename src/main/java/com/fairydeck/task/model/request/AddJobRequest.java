package com.fairydeck.task.model.request;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AddJobRequest {
    private int min = 1;
    private int max = 8;
    private BigInteger limit = BigInteger.valueOf(1);
    private String charMap = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
