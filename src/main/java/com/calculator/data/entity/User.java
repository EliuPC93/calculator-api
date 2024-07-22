package com.calculator.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Credit> credits;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Record> records;

    public Double getUserBalance() {
        Double allCreditsSum = this.getCredits().stream().map(Credit::getAmount).reduce(0.0, Double::sum);
        Double allRecordsCostSum =  this.getRecords().stream().filter(Record::getActive).map(Record::getAmount).reduce(0.0, Double::sum);;

        return allCreditsSum - allRecordsCostSum;
    }

}
