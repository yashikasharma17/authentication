package com.example.authentication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="tb_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Userentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String userid;
    private String name;
    private String password;

    @Column(unique = true)
    private String email;

    private String verifyOtp;
    private String isAccountVerified;
    private String verifyOtpExpired;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyOtp() {
        return verifyOtp;
    }

    public void setVerifyOtp(String verifyOtp) {
        this.verifyOtp = verifyOtp;
    }

    public String isAccountVerified() {
        return isAccountVerified();
    }

    public void setAccountVerified(boolean accountVerified) {
        isAccountVerified = String.valueOf(accountVerified);
    }

    public String getVerifyOtpExpired() {
        return verifyOtpExpired;
    }

    public void setVerifyOtpExpired(String verifyOtpExpired) {
        this.verifyOtpExpired = verifyOtpExpired;
    }

    public String getResetOtp() {
        return resetOtp;
    }

    public void setResetOtp(String resetOtp) {
        this.resetOtp = resetOtp;
    }

    public String getResetOtpExpireAt() {
        return resetOtpExpireAt;
    }

    public void setResetOtpExpireAt(String resetOtpExpireAt) {
        this.resetOtpExpireAt = resetOtpExpireAt;
    }

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    private String resetOtp;
    private String resetOtpExpireAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdat;

    @UpdateTimestamp
    private Timestamp updatedat;



}
