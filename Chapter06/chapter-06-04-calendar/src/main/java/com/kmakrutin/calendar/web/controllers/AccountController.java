package com.kmakrutin.calendar.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/accounts/my")
    public String view(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        model.addAttribute("user", principal);
        model.addAttribute("isLdapUserDetails", principal instanceof LdapUserDetails);
        model.addAttribute("isLdapPerson", principal instanceof Person);
        model.addAttribute("isLdapInetOrgPerson", principal instanceof InetOrgPerson);

        return "accounts/show";
    }
}
