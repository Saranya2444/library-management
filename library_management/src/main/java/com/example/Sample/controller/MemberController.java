package com.example.Sample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Sample.model.dto.MemberDto;
import com.example.Sample.model.entity.Member;
import com.example.Sample.service.MemberService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/save")
    public String saveMember(@Valid @RequestBody MemberDto memberDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            
            return result.getAllErrors().get(0).getDefaultMessage();
        }
        return memberService.registerMember(memberDTO);
    }

    
    @GetMapping("/{id}")
    public String  getMember(@PathVariable Integer id) {
        return memberService.getMemberById(id);
    }

    @GetMapping
    public String getAllMembers() {
        return memberService.getAllMembers();
    }
    
    @PutMapping("/{id}")
    public String updateMember(@PathVariable Integer id, @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }
    
    @PutMapping("/{id}/activate")
    public String activateMember(
            @PathVariable Integer id,
            @RequestParam String membershipPeriod) {
        return memberService.activateMember(id, membershipPeriod);
    }

    @PutMapping("/{id}/deactivate")
    public String deactivateMember(@PathVariable Integer id) {
        return memberService.deactivateMember(id);
    }
    
 // Fetch Active Members
    @GetMapping("/active")
    public String getActiveMembers() {
        return memberService.getMembersByStatus("Active");
    }

    // Fetch Inactive Members
    @GetMapping("/inactive")
    public String getInactiveMembers() {
        return memberService.getMembersByStatus("Inactive");
    }
    @PutMapping("/check-expired")
    public String checkExpiredMemberships() {
        return memberService.checkAndDeactivateExpiredMemberships();
    }

    }
