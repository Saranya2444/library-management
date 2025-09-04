package com.example.Sample.model.dto;



import java.time.LocalDateTime;

import com.example.Sample.util.DateDeserializeUtil;
import com.example.Sample.util.DateSerializeUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public class MemberDto {
    private Integer memberId;
    
    @NotBlank(message="name should not be null or empty")
    private String memberName;
    
    
    @NotBlank(message="email should not be null or empty")
    @Email(message="email should be in correct format")
    private String memberEmail;
    
    private String membershipStatus;
    private String memberAddress;
    
    @NotBlank(message="phone number should not be null or empty")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String memberMobileNumber;
    
    private String memberWorkStatus;
    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class)
    private LocalDateTime memberStartDate;
    
    private String membershipPeriod;
    @JsonDeserialize(using = DateDeserializeUtil.class)
    @JsonSerialize(using = DateSerializeUtil.class)
    private LocalDateTime memberEndDate;

    
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }

    public String getMembershipStatus() { return membershipStatus; }
    public void setMembershipStatus(String membershipStatus) { this.membershipStatus = membershipStatus; }

    public String getMemberAddress() { return memberAddress; }
    public void setMemberAddress(String memberAddress) { this.memberAddress = memberAddress; }

    public String getMemberMobileNumber() { return memberMobileNumber; }
    public void setMemberMobileNumber(String memberMobileNumber) { this.memberMobileNumber = memberMobileNumber; }

    public String getMemberWorkStatus() { return memberWorkStatus; }
    public void setMemberWorkStatus(String memberWorkStatus) { this.memberWorkStatus = memberWorkStatus; }

    public LocalDateTime getMemberStartDate() { return memberStartDate; }
    public void setMemberStartDate(LocalDateTime memberStartDate) { this.memberStartDate = memberStartDate; }

    public LocalDateTime getMemberEndDate() { return memberEndDate; }
    public void setMemberEndDate(LocalDateTime memberEndDate) { this.memberEndDate = memberEndDate; }
    
    public String getMembershipPeriod() { return membershipPeriod; }
    public void setMembershipPeriod(String membershipPeriod) { this.membershipPeriod = membershipPeriod; }
}
