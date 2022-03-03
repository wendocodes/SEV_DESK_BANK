package com.wendo.bank.enitity;

import com.wendo.bank.converter.dbconverter.EncryptionConverter;
import com.wendo.bank.enums.TransactionMode;
import com.wendo.bank.enums.TransactionStatus;
import com.wendo.bank.enums.TransactionType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction extends MetaEntity {

   
    private TransactionType type;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "value", column = @Column(name = "transaction_value")),
            @AttributeOverride( name = "currency", column = @Column(name = "transaction_currency"))
    })
    private Amount amount;
    private String category;
    private String merchant;
    private String location;
    @Column(name="reference_number", unique = true)
    private String ref;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private TransactionStatus status;
    @Convert(converter = EncryptionConverter.class)
    private String toAccountNumber;
    @Convert(converter = EncryptionConverter.class)
    private String transactionModeReferenceNumber;
    @Enumerated(EnumType.STRING)
    private TransactionMode transactionMode;
    private boolean isExternalTransfer = false;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Transaction))
            return false;
        Transaction other = (Transaction) o;
        boolean idEquals = (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
        boolean refEquals = (this.ref == null && other.ref == null)
                || (this.ref != null && this.ref.equals(other.ref));
        return idEquals && refEquals;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (id != null) {
            result = ((result << 5)-result) + id.hashCode();
        }
        if (ref != null) {
            result = ((result << 5)-result) + ref.hashCode();
        }
        return result;
    }
}
