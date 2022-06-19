package org.kaczucha.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_generator")
    @SequenceGenerator(name = "client_generator", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;
    @Column(name = "FIRST_NAME")
    private String name;
    @Column(name = "MAIL")
    private String email;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private List<Account> accountList;

    public Client(String name, String email, List<Account> accountList) {
        this.name = name;
        this.email = email;
        this.accountList = accountList;
    }

    public double getBalance() {
        if (!accountList.isEmpty())
            return this.accountList.get(0).getBalance();
        else
            return 0;
    }

    public void setBalance(double newBalance) {
        if (!accountList.isEmpty())
            this.accountList.get(0).setBalance(newBalance);
    }
}
