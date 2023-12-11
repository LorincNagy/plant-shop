package com.ThreeTree.dto;

public record NewPersonRequest(String FirstName,
                               String LastName,
                               String address,
                               String email,
                               String phoneNumber
) {
}