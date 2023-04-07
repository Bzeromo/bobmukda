package com.example.projectui;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {

    private String idToken; // 파이어베이스 아이디 고유 키 정보
    private String emailId; // 이메일 아이디
    private String password; // 비밀번호
    private String name; //이름
    private String birth; //생년월일

    private String phoneNumber; // 전화번호
    // 파이어베이스에서는 빈 생성자를 만들어줘야함
    public UserAccount() {

    }

    /* getter, setter 메서드 생성 */

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
