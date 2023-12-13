package com.ThreeTree.dto;

public record NewPersonRequest(String firstName,
                               String lastName,
                               String email,
                               String phoneNumber,
                               String address,
                               String password
) {
}
