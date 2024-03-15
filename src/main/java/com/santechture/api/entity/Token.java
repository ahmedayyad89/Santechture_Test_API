package com.santechture.api.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
@Entity
public class Token {

    @GeneratedValue
    @Id
    @Column(name = "token_id", nullable = false)
    private Integer tokenId;

    @Basic
    @Column(name = "token", nullable = false, length = 250)
    private String token;

    @Column(name ="is_logged_out")
    private Boolean isLoggedOut;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
