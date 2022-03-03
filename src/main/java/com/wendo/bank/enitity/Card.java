package com.wendo.bank.enitity;

import com.wendo.bank.converter.dbconverter.EncryptionConverter;
import com.wendo.bank.enums.CardType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Card extends MetaEntity{

    @Convert(converter = EncryptionConverter.class)
    private String cardNumber;
    @Convert(converter = EncryptionConverter.class)
    private String pin;
    @Enumerated(EnumType.STRING)
    private CardType type;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "value", column = @Column(name = "withdrawal_limit_value")),
            @AttributeOverride( name = "currency", column = @Column(name = "withdrawal_limit_currency"))
    })
    private Amount limit;
    @Embedded
    private Status status;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Card))
            return false;
        Card other = (Card) o;
        boolean idEquals = (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
        boolean accountNumberEquals = (this.cardNumber == null && other.cardNumber == null)
                || (this.cardNumber != null && this.cardNumber.equals(other.cardNumber));
        return idEquals && accountNumberEquals;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (id != null) {
            result = ((result << 5)-result) + id.hashCode();
        }
        if (cardNumber != null) {
            result = ((result << 5)-result) + cardNumber.hashCode();
        }
        return result;
    }
}
