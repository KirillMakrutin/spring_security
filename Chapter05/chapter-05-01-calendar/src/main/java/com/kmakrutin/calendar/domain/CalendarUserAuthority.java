package com.kmakrutin.calendar.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CALENDAR_USER_AUTHORITIES")
@Data
public class CalendarUserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;

    @ManyToOne
    @JoinColumn(name = "calendar_user")
    private CalendarUser user;
}
