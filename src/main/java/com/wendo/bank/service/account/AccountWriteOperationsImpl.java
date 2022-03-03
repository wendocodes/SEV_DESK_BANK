package com.wendo.bank.service.account;


import com.wendo.bank.converter.dtoconverter.Converter;
import com.wendo.bank.dto.*;
import com.wendo.bank.enitity.*;
import com.wendo.bank.enums.AccountStatus;
import com.wendo.bank.enums.AccountType;
import com.wendo.bank.enums.CardType;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.repo.AccountRepository;
import com.wendo.bank.repo.CardRepository;
import com.wendo.bank.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;

@Service
public class AccountWriteOperationsImpl implements AccountWriteOperations{

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final Converter<AccountDto, Account> accountConverter;
    private final Converter<StatusDto, Status> statusConverter;
    private final Converter<CardDto, Card> cardConverter;

    public AccountWriteOperationsImpl(AccountRepository accountRepository,
                                      UserRepository userRepository,
                                      CardRepository cardRepository,
                                      Converter<AccountDto, Account> accountConverter,
                                      Converter<StatusDto, Status> statusConverter,
                                      Converter<CardDto, Card> cardConverter) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.accountConverter = accountConverter;
        this.statusConverter = statusConverter;
        this.cardConverter = cardConverter;
    }

    @Override
    public AccountDto createAccount(AccountCreationDto accountCreationDto) {
        String customerNumber = accountCreationDto.getCustomerNumber();
        User user = userRepository.findByCustomerNumber(customerNumber);
        if(user == null)
            throw new ResourceNotFoundException("Customer not found with number : %s", customerNumber);
        Account account = new Account();
        account.setName(accountCreationDto.getName());
        account.setStatus(new Status(AccountStatus.PENDING_ACTIVAION, "Account needs to be activated"));
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setCustomer(user);
        account.setType(accountCreationDto.getType());
        if(AccountType.CURRENT.equals(accountCreationDto.getType()))
            account.setOverdraftLimit(new Amount(accountCreationDto.getOverdraftLimit().getValue(), accountCreationDto.getOverdraftLimit().getCurrency()));
        account = accountRepository.save(account);
        addCard(accountCreationDto, account);
        if(AccountType.FIXED_DEPOSIT.equals(accountCreationDto.getType()) || AccountType.SAVINGS.equals(accountCreationDto.getType())) {
            account.setAnnualInterestRate(accountCreationDto.getAnnualInterestRate());
        }
        return accountConverter.convertToDto(account);
    }

    private void addCard(AccountCreationDto accountCreationDto, Account account) {
        if(!AccountType.FIXED_DEPOSIT.equals(accountCreationDto.getType())){
            Card card = Card.builder()
                    .cardNumber(UUID.randomUUID().toString())
                    .limit(new Amount(10000.0, "EUR"))
                    .status(new Status(AccountStatus.PENDING_ACTIVAION, "Card needs to be activated"))
                    .type(CardType.DEBIT_CARD)
                    .pin("1234")
                    .account(account)
                    .build();
            card = cardRepository.save(card);
            Set<Card> cards = account.getCards();
            cards.add(card);
            account.setCards(cards);
        }
    }

    @Override
    public AccountDto updateAccount(String customerNumber, AccountUpdationDto accountUpdationDto) {
        User user = userRepository.findByCustomerNumber(customerNumber);
        if(user == null)
            throw new ResourceNotFoundException("Customer not found with number : %s", customerNumber);
        Account account = accountRepository.findByAccountNumber(accountUpdationDto.getAccountNumber());
        if(account == null)
            throw new ResourceNotFoundException("Account not found with number : %s", accountUpdationDto.getAccountNumber());
        if(!Objects.equals(accountUpdationDto.getName(), account.getName())) {
            account.setName(accountUpdationDto.getName());
        }
        if(!Objects.equals(accountUpdationDto.getType(), account.getType())) {
            account.setType(accountUpdationDto.getType());
        }
        if(StringUtils.hasText(accountUpdationDto.getNewPin())) {
            account.setPin(accountUpdationDto.getNewPin());
        }
        if(!isEqualAmount.test(accountUpdationDto.getOverdraftLimit(), account.getOverdraftLimit())) {
            account.getOverdraftLimit().setValue(accountUpdationDto.getOverdraftLimit().getValue());
        }
        return accountConverter.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto updateStatusOfAccount(AccountStateChangeRequestDto accountStateChangeRequestDto) {
        User user = userRepository.findByCustomerNumber(accountStateChangeRequestDto.getManagerNumber());
        if(user == null)
            throw new ResourceNotFoundException("Customer not found with number : %s", accountStateChangeRequestDto.getManagerNumber());
        Account account = accountRepository.findByAccountNumber(accountStateChangeRequestDto.getAccountNumber());
        if(account == null)
            throw new ResourceNotFoundException("Account not found with number : %s", accountStateChangeRequestDto.getAccountNumber());
        if(AccountStatus.CLOSED.equals(accountStateChangeRequestDto.getStatus().getStatus())
                && account.getBalance().getValue() < 0 ) {
            throw new ValidationException("Unable to close due to pending balance : " + account.getBalance().toString());
        }
        if(AccountStatus.CLOSED.equals(accountStateChangeRequestDto.getStatus().getStatus())
                && account.getBalance().getValue() > 0 ) {
            throw new ValidationException("Please withdraw the remaining balance of "+ account.getBalance().toString()+" to close account: " );
        }
        account.setStatus(statusConverter.convertToEntity(accountStateChangeRequestDto.getStatus()));
        account.getCards().forEach(card -> {
            card.getStatus().setStatus(accountStateChangeRequestDto.getStatus().getStatus());
            card.getStatus().setDescription("On account "+card.getStatus().getStatus().name());
        });
        cardRepository.saveAll(account.getCards());
        return accountConverter.convertToDto(accountRepository.save(account));
    }

    @Override
    public CardDto updateStatusOfCard(CardStateChangeDto cardStateChangeDto) {
        Account account = accountRepository.findByAccountNumber(cardStateChangeDto.getAccountNumber());
        if(account == null)
            throw new ResourceNotFoundException("Account not found with number : %s", cardStateChangeDto.getAccountNumber());
        Card card = account.getCards().stream()
                .filter(c -> cardStateChangeDto.getCardNumber().equals(c.getCardNumber()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number : %s", cardStateChangeDto.getCardNumber()));
        card.setStatus(statusConverter.convertToEntity(cardStateChangeDto.getStatus()));
        return cardConverter.convertToDto(cardRepository.save(card));
    }

    private final BiPredicate<AmountDto, Amount> isEqualAmount =
            (a, b) -> (a != null && b != null) &&  Objects.equals(a.getValue(), b.getValue());
}
