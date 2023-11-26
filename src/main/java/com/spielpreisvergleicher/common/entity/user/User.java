package com.spielpreisvergleicher.common.entity.user;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account" )
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String email;
    private String nickname;
    private String password;

}
