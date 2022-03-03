package com.wendo.bank.enitity;

import com.wendo.bank.enums.Gender;
import com.wendo.bank.enums.UserType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends MetaEntity {

    @Column(name="customer_number", unique = true)
    private String customerNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserType type;
    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Account> accounts = new HashSet<>();
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User))
            return false;
        User other = (User) o;
        boolean idEquals = (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
        boolean customerNumber = (this.customerNumber == null && other.customerNumber == null)
                || (this.customerNumber != null && this.customerNumber.equals(other.customerNumber));
        return idEquals && customerNumber;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (id != null) {
            result = ((result << 5)-result) + id.hashCode();
        }
        if (customerNumber != null) {
            result = ((result << 5)-result) + customerNumber.hashCode();
        }
        if (firstName != null) {
            result = ((result << 5)-result) + firstName.hashCode();
        }
        if (lastName != null) {
            result = ((result << 5)-result) + lastName.hashCode();
        }
        return result;
    }
}
