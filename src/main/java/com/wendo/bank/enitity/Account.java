package com.wendo.bank.enitity;

import com.wendo.bank.converter.dbconverter.EncryptionConverter;
import com.wendo.bank.enums.AccountType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Account extends MetaEntity {

    @Convert(converter = EncryptionConverter.class)
    private String accountNumber;
    private String name;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "value", column = @Column(name = "balance_value")),
            @AttributeOverride( name = "currency", column = @Column(name = "balance_currency"))
    })
    private Amount balance = new Amount(0.0, "EUR");


    @Convert(converter = EncryptionConverter.class)
    private String pin;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "value", column = @Column(name = "overdraft_limit_value")),
            @AttributeOverride( name = "currency", column = @Column(name = "overdraft_limit_currency"))
    })
    private Amount overdraftLimit;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private Set<Transaction> transactions = new HashSet<>();
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    @Embedded
    private Status status;
    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private Set<Card> cards = new HashSet<>();

    private Double annualInterestRate = 0.0;


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Account))
            return false;
        Account other = (Account) o;
        boolean idEquals = (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
        boolean accountNumberEquals = (this.accountNumber == null && other.accountNumber == null)
                || (this.accountNumber != null && this.accountNumber.equals(other.accountNumber));
        return idEquals && accountNumberEquals;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (id != null) {
            result = ((result << 5)-result) + id.hashCode();
        }
        if (accountNumber != null) {
            result = ((result << 5)-result) + accountNumber.hashCode();
        }
        if (name != null) {
            result = ((result << 5)-result) + name.hashCode();
        }
        return result;
    }
}
