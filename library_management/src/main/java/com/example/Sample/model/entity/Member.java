package com.example.Sample.model.entity;



import jakarta.persistence.*;



import java.time.LocalDateTime;



@Entity
@Table(
	    name = "members",
	    uniqueConstraints = {
	        @UniqueConstraint(name = "uq_member_email", columnNames = "member_email"),
	        @UniqueConstraint(name = "uq_member_mobile", columnNames = "member_mobile_number")
	    }
	)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "member_name", nullable = false, length = 25)
    
    
    private String memberName;

    
    @Column(name = "member_email", nullable = false)
    
    private String memberEmail;

    @Column(name = "membership_start_date")
   
    private LocalDateTime memberStartDate;
    
    @Column(name="membershipPeriod")
    private String membershipPeriod;

    @Column(name = "membership_end_date")
    
    private LocalDateTime memberEndDate;

    @Column(name = "membership_status", length = 10)
    private String membershipStatus; 

    @Column(name = "member_address", length = 255)
    private String memberAddress;

    @Column(name = "member_mobile_number", length = 10,nullable = false)
    
    private String memberMobileNumber;

    @Column(name = "member_work_status", length = 20)
    private String memberWorkStatus; 

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

  
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }

    public LocalDateTime getMemberStartDate() { return memberStartDate; }
    public void setMemberStartDate(LocalDateTime memberStartDate) { this.memberStartDate = memberStartDate; }

    public LocalDateTime getMemberEndDate() { return memberEndDate; }
    public void setMemberEndDate(LocalDateTime memberEndDate) { this.memberEndDate = memberEndDate; }

    public String getMembershipStatus() { return membershipStatus; }
    public void setMembershipStatus(String membershipStatus) { this.membershipStatus = membershipStatus; }

    public String getMemberAddress() { return memberAddress; }
    public void setMemberAddress(String memberAddress) { this.memberAddress = memberAddress; }

    public String getMemberMobileNumber() { return memberMobileNumber; }
    public void setMemberMobileNumber(String memberMobileNumber) { this.memberMobileNumber = memberMobileNumber; }

    public String getMemberWorkStatus() { return memberWorkStatus; }
    public void setMemberWorkStatus(String memberWorkStatus) { this.memberWorkStatus = memberWorkStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getMembershipPeriod() { return membershipPeriod; }
    public void setMembershipPeriod(String membershipPeriod) { this.membershipPeriod = membershipPeriod; }
}
