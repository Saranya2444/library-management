package com.example.Sample.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.MemberDao;
import com.example.Sample.model.dto.MemberDto;
import com.example.Sample.model.entity.Member;
import com.example.Sample.service.MemberService;
import com.example.Sample.util.LibraryUtil;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    
    @Autowired
    private AllServiceMail allServiceMail;

    @Override
    public String registerMember(MemberDto dto) {
        if (memberDao.existsByEmail(dto.getMemberEmail())) {
            return LibraryUtil.MEMBER_EMAIL_EXISTS;
        }
        if (memberDao.existsByMobile(dto.getMemberMobileNumber())) {
            return LibraryUtil.MEMBER_MOBILE_EXISTS;
        }

        Member member = new Member();
        member.setMemberId(dto.getMemberId());
        member.setMemberName(dto.getMemberName());
        member.setMemberEmail(dto.getMemberEmail());
        member.setMemberAddress(dto.getMemberAddress());
        member.setMemberMobileNumber(dto.getMemberMobileNumber());
        member.setMembershipStatus(LibraryUtil.STATUS_ACTIVE);
        member.setMemberWorkStatus(dto.getMemberWorkStatus());
        
        LocalDateTime startDate = LocalDateTime.now();
        member.setMemberStartDate(startDate);
        
        LocalDateTime endDate;
        switch (dto.getMembershipPeriod().toUpperCase()) {
        case "1":
            endDate = startDate.plusMonths(1);
            break;
        case "3":
            endDate = startDate.plusMonths(3);
            break;
        case "6":
            endDate = startDate.plusMonths(6);
            break;
        case "1Y":
            endDate = startDate.plusYears(1);
            break;
        default:
        	return LibraryUtil.MEMBER_INVALID_PERIOD;
    }

        member.setMemberEndDate(endDate);
        member.setMembershipPeriod(dto.getMembershipPeriod());
        member.setUpdatedAt(LocalDateTime.now());
        if (dto.getMemberId() == null) {
            member.setCreatedAt(LocalDateTime.now());
        }

        memberDao.register(member);
        
		allServiceMail.sendRegistrationMail(member.getMemberEmail(), member.getMemberName());

        return LibraryUtil.MEMBER_REGISTERED_SUCCESS;
    }
    @Override
    public String getMemberById(Integer id) {
        Member member = memberDao.findById(id);
        if (member == null) {
            return LibraryUtil.MEMBER_NOT_FOUND + id;
        }
        return "Member Id: " + member.getMemberId() +
               ", Name: " + member.getMemberName() +
               ", Email: " + member.getMemberEmail() +
               ", Mobile: " + member.getMemberMobileNumber();
    }
    @Override
    public String getAllMembers() {
        List<Member> members = memberDao.findAll();
        if (members.isEmpty()) {
            return LibraryUtil.NO_MEMBERS_FOUND ;
        }
        StringBuilder sb = new StringBuilder();
        for (Member m : members) {
            sb.append("Id: ").append(m.getMemberId())
              .append(", Name: ").append(m.getMemberName())
              .append(", Email: ").append(m.getMemberEmail())
              .append(", Mobile: ").append(m.getMemberMobileNumber())
              .append("\n");
        }
        return sb.toString();
    }
    @Override
    public String updateMember(Integer id, Member updatedMember) {
        updatedMember.setMemberId(id);  
        memberDao.update(updatedMember);
		allServiceMail.sendUpdateMail(updatedMember.getMemberEmail(), updatedMember.getMemberName());

        return  LibraryUtil.MEMBER_UPDATED_SUCCESS + " with id: " + id;
    }
    
    @Override
    public String activateMember(Integer id, String membershipPeriod) {
    	Member member = memberDao.findById(id);
        if (member == null) {
            return  LibraryUtil.MEMBER_NOT_FOUND + id;
        }
    	 
		allServiceMail.sendActivationMail(member.getMemberEmail(), member.getMemberName());

        return memberDao.activateMember(id, membershipPeriod);
    }

    
   
    @Override
    public String deactivateMember(Integer id) {
        memberDao.updateStatus(id, LibraryUtil.STATUS_INACTIVE);
        return LibraryUtil.MEMBER_DEACTIVATED_SUCCESS + id;
    }
    
    @Override
    public String getMembersByStatus(String status) {
        List<Member> members = memberDao.findByMembershipStatus(status);
        if (members.isEmpty()) {
            return LibraryUtil.NO_STATUS_MEMBERS_FOUND ;
        }
        StringBuilder sb = new StringBuilder();
        for (Member m : members) {
            sb.append("Id: ").append(m.getMemberId())
              .append(", Name: ").append(m.getMemberName())
              .append(", Email: ").append(m.getMemberEmail())
              .append(", Mobile: ").append(m.getMemberMobileNumber())
              .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String checkAndDeactivateExpiredMemberships() {
        List<Member> activeMembers = memberDao.findByMembershipStatus("Active");
        LocalDateTime now = LocalDateTime.now();

        int count = 0;
        for (Member member : activeMembers) {
            if (member.getMemberEndDate() != null &&
                member.getMemberEndDate().isBefore(now)) {
              //  member.setMembershipStatus(LibraryUtil.STATUS_INACTIVE);
                memberDao.updateStatus(member.getMemberId(),LibraryUtil.STATUS_INACTIVE);
                allServiceMail.sendDeActivationMail(member.getMemberEmail(), member.getMemberName());
                count++;
            }
        }
        
        return count + LibraryUtil.MEMBERSHIPS_DEACTIVATED_EXPIRY;
    }

    
    @Scheduled(cron = "0 0 15 * * ?")
    public void scheduledDeactivateExpiredMemberships() {
        checkAndDeactivateExpiredMemberships();
    }

}