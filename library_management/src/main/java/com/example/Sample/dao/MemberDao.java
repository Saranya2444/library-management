package com.example.Sample.dao;

import java.util.List;



import com.example.Sample.model.entity.Member;

public interface MemberDao {
     Member register(Member member);
     boolean existsByEmail(String email);
     boolean existsByMobile(String mobile);
     Member findById(Integer id);
     List<Member> findAll();
     List<Member> findByMembershipStatus(String status);
     void update(Member member);
     void updateStatus(Integer id, String status);
     String activateMember(Integer id, String membershipPeriod);
	
}
