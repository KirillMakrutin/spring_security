# Spring Security 
Grab the original code by the [link](https://github.com/PacktPublishing/Spring-Security-Third-Edition)

## Chapter 2 - Getting Started with Spring Security
Covers:
- basic form login setup with custom URL
- redirect on Success login
- basic logout setup with custom URL
- basic resource protection
- conditional rendering on Auth details

## Chapter 3 - Custom Authentication
Covers:
- Discovering how to obtain the details of the currently logged-in user from _SecurityContextHolder_
- Adding the ability to log in after creating a new account
- Learning the simplest method for indicating to Spring Security, that a user is authenticated
- Creating custom UserDetailsService and AuthenticationProvider implementations that properly decouple the rest of the application from Spring Security
- Adding domain-based authentication to demonstrate how to authenticate with more than just a username and password

## Chapter 4 - JDBC-Based Authentication
Covers:
- Using Spring Security's built-in JDBC-based authentication support
- Utilizing Spring Security's group-based authorization to make administering users easier
- Learning how to use Spring Security's UserDetailsManager interface
- Configuring Spring Security to utilize the existing CalendarUser schema to authenticate users
- Learning how we can secure passwords using Spring Security's new cryptography module
- Using Spring Security's default JDBC authentication

## Chapter 5 - Authentication with Spring Data
Covers:
- Some of the basic concepts related to the Spring Data project
- Utilizing Spring Data JPA to authenticate against a relational database
- Utilizing Spring Data MongoDB to authenticate against a document database
- How to customize Spring Security for more flexibility when dealing with Spring Data integration
- Understanding the Spring Data project

## Chapter 6 - LDAP Directory Services
Covers:
-Learning some of the basic concepts related to the LDAP protocol and server implementations
-Configuring a self-contained LDAP server within Spring Security
-Enabling LDAP authentication and authorization
-Understanding the model behind LDAP search and user matching
-Retrieving additional user details from standard LDAP structures
-Differentiating between LDAP authentication methods and evaluating the pros and cons of each type
-Explicitly configuring Spring Security LDAP using Spring bean declarations
-Connecting to external LDAP directories
-Exploring the built-in support for Microsoft AD
-We will also explore how to customize Spring Security for more flexibility when dealing with custom AD deployments

## Chapter 7 - Remember-Me Services
Covers:
-Discussing what remember-me is
-Learning how to use the token-based remember-me feature
-Discussing how secure remember-me is, and various ways of making it more secure
-Enabling the persistent-based remember-me feature, and how to handle additional considerations for using it
-Presenting the overall remember-me architecture
-Learning how to create a custom remember-me implementation that is restricted to the user's IP address

## Chapter 8 - Client Certificate Authentication with TLS
Covers:
-Learning how client certificate authentication is negotiated between the user's browser and a compliant server
-Configuring Spring Security to authenticate users with client certificates
-Understanding the architecture of client certificate authentication in Spring Security
-Exploring advanced configuration options related to client certificate authentication
-Reviewing pros, cons, and common troubleshooting steps when dealing with client certificate authentication
