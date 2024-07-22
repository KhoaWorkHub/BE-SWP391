package vn.fpt.diamond_shop.response;

import lombok.Data;

@Data
public class ChangeUserProfileResponse {
    private String email;
    private String phoneNumber;
    private String password;
    private String fullName;
    private boolean isMale;
    private String dateOfBirth;
    private String province;
    private int age;
    private String district;
    private String city;
    private String ward;
    private String extra;
    private String bio;
}
