package com.example.Sample.service;



import com.example.Sample.model.dto.MemberDto;
import com.example.Sample.model.entity.Member;

public interface MemberService {
    String registerMember(MemberDto memberDTO);
    
    String getMemberById(Integer id);
    String getAllMembers();
    String updateMember(Integer id,Member member);
   
    String deactivateMember(Integer id);

    String activateMember(Integer id, String membershipPeriod);
    String getMembersByStatus(String status);

	String checkAndDeactivateExpiredMemberships();

    
}