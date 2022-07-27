package org.kaczucha.repository.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACCOUNTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "ACCOUNT_ID")
    private long id;
    @Column(name = "BALANCE")
    private double balance;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "USER_ID")
    private long userId;

    public Account(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }
}
