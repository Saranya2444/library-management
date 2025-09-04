package com.example.Sample.daoImpl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.example.Sample.dao.MemberDao;
import com.example.Sample.model.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class MemberDaoImpl implements MemberDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Member register(Member member) {
        if (member.getMemberId() == null) {
            entityManager.persist(member);
            return member;
        } else {
            return entityManager.merge(member);
        }
    }
    @Override
    public boolean existsByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Member> root = cq.from(Member.class);

        // SELECT COUNT(m) WHERE m.memberEmail = :email
        cq.select(cb.count(root))
          .where(cb.equal(root.get("memberEmail"), email));

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }


    @Override
    public boolean existsByMobile(String mobile) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Member> root = cq.from(Member.class);

        // SELECT COUNT(m) WHERE m.memberMobileNumber = :mobile
        cq.select(cb.count(root))
          .where(cb.equal(root.get("memberMobileNumber"), mobile));

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }
    @Override
    public Member findById(Integer id) {
        return entityManager.find(Member.class, id);
    }

    @Override
    public List<Member> findAll() {
        return entityManager.createQuery("from Member", Member.class).getResultList();
    }
    
    @Override
    public void update(Member member) {
        Member existingMember = entityManager.find(Member.class, member.getMemberId());
        if (existingMember == null) {
            throw new RuntimeException("Member not found with id: " + member.getMemberId());
        }

      
        if (member.getMemberName() != null) {
            existingMember.setMemberName(member.getMemberName());
        }
        if (member.getMemberEmail() != null) {
            existingMember.setMemberEmail(member.getMemberEmail());
        }
        if (member.getMemberMobileNumber() != null) {
            existingMember.setMemberMobileNumber(member.getMemberMobileNumber());
        }
        if (member.getMemberAddress() != null) {
            existingMember.setMemberAddress(member.getMemberAddress());
        }
        existingMember.setUpdatedAt(LocalDateTime.now());
       
        entityManager.merge(existingMember);
    }
   
    @Override
    public void updateStatus(Integer id, String status) {
        Member existingMember = entityManager.find(Member.class, id);
        if (existingMember == null) {
            throw new RuntimeException("Member not found with id: " + id);
        }

        existingMember.setMembershipStatus(status);
        entityManager.merge(existingMember);
    }
    
    @Override
    public String activateMember(Integer id, String membershipPeriod) {
        Member existingMember = entityManager.find(Member.class, id);
        if (existingMember == null) {
            throw new RuntimeException("Member not found with id: " + id);
        }

        if (!"Inactive".equalsIgnoreCase(existingMember.getMembershipStatus())) {
            return "Member with id " + id + " is already active!";
        }

        existingMember.setMembershipStatus("Active");
        LocalDateTime now = LocalDateTime.now();
        existingMember.setMemberStartDate(now);

        LocalDateTime endDate;
        switch (membershipPeriod.toLowerCase()) {
            case "1":
                endDate = now.plusMonths(1);
                break;
            case "3":
                endDate = now.plusMonths(3);
                break;
            case "6":
                endDate = now.plusMonths(6);
                break;
            case "1Y":
                endDate = now.plusYears(1);
                break;
            default:
            	 return "Membership period is invalid. Only allowed values: 1, 3, 6, 1Y";
        }
        existingMember.setMemberEndDate(endDate);
        existingMember.setMembershipPeriod(membershipPeriod);
        existingMember.setUpdatedAt(LocalDateTime.now());

        entityManager.merge(existingMember);

        return "Member activated successfully with id: " + id ; 
               
    }
    
    @Override
    public List<Member> findByMembershipStatus(String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);

        Root<Member> root = cq.from(Member.class);

        // SELECT m FROM Member m WHERE m.membershipStatus = :status
        cq.select(root)
          .where(cb.equal(root.get("membershipStatus"), status));

        return entityManager.createQuery(cq).getResultList();
    }

    
}


