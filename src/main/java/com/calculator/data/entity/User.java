package com.calculator.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
@Builder
public class User extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private boolean status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authentication_detail_id", referencedColumnName = "id")
    private AuthenticationDetail authenticationDetail;

    @OneToMany(mappedBy = "user")
    private List<Credit> credits;

    @OneToMany(mappedBy = "user")
    private List<Record> records;

    public Double getUserBalance() {
        Double allCreditsSum = this.getCredits().stream().map(Credit::getAmount).reduce(0.0, Double::sum);
        Double allRecordsCostSum =  this.getRecords().stream().filter(Record::getActive).map(Record::getAmount).reduce(0.0, Double::sum);;

        return allCreditsSum - allRecordsCostSum;
    }

}
