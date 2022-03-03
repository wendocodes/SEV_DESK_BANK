package com.wendo.bank.service;

import com.wendo.bank.converter.dtoconverter.AmountDtoConverter;
import com.wendo.bank.converter.dtoconverter.Converter;
import com.wendo.bank.converter.dtoconverter.TransactionConverter;
import com.wendo.bank.dto.MoneyRequestDto;
import com.wendo.bank.dto.MoneyTransferAckDto;
import com.wendo.bank.dto.MoneyTransferRequestDto;
import com.wendo.bank.dto.TransactionDto;
import com.wendo.bank.enitity.Account;
import com.wendo.bank.enitity.Card;
import com.wendo.bank.enitity.Transaction;
import com.wendo.bank.enums.AccountStatus;
import com.wendo.bank.enums.TransactionMode;
import com.wendo.bank.enums.TransactionStatus;
import com.wendo.bank.enums.TransactionType;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.repo.AccountRepository;
import com.wendo.bank.repo.CardRepository;
import com.wendo.bank.repo.TransactionRepository;
import com.wendo.bank.util.CalculatorUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MoneyServiceImpl implements MoneyService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final AmountDtoConverter amountDtoConverter;
    private final Converter<TransactionDto, Transaction> transactionConverter;

    public MoneyServiceImpl(TransactionRepository transactionRepository,
                            AccountRepository accountRepository,
                            CardRepository cardRepository, AmountDtoConverter amountDtoConverter,
                            TransactionConverter transactionConverter) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.amountDtoConverter = amountDtoConverter;
        this.transactionConverter = transactionConverter;
    }

    @Override
    public TransactionDto deposit(MoneyRequestDto moneyRequestDto) {

        Account account = accountRepository.findByAccountNumberAndCustomerCustomerNumber(moneyRequestDto.getAccountNumber(), moneyRequestDto.getCustomerNumber());
        if (account == null || !AccountStatus.ACTIVE.equals(account.getStatus().getStatus()))
            throw new ResourceNotFoundException("Account does not exist with number %s or Account is not in active state to deposit amount", moneyRequestDto.getAccountNumber());

        Transaction transaction = new Transaction();
        transaction.setRef(UUID.randomUUID().toString());
        transaction.setType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.PROCESSING);
        transaction.setAmount(amountDtoConverter.convertToEntity(moneyRequestDto.getAmount()));
        transaction.setCategory(moneyRequestDto.getCategory());
        transaction.setMerchant(moneyRequestDto.getMerchant());
        transaction.setLocation(moneyRequestDto.getLocation());
        transaction.setToAccountNumber(moneyRequestDto.getAccountNumber());
        transaction.setTransactionMode(moneyRequestDto.getTransactionMode());
        transaction.setAccount(account);
        transaction = transactionRepository.saveAndFlush(transaction);

        account.setBalance(CalculatorUtil.addAmount(account.getBalance(), moneyRequestDto.getAmount()));
        account = accountRepository.saveAndFlush(account);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction = transactionRepository.saveAndFlush(transaction);
        return transactionConverter.convertToDto(transaction);
    }

    @Override
    public TransactionDto withdraw(MoneyRequestDto moneyRequestDto) {
        if(TransactionMode.DEBIT_CARD.equals(moneyRequestDto.getTransactionMode()) ||
                TransactionMode.CREDIT_CARD.equals(moneyRequestDto.getTransactionMode())) {
            return WithdrawalByCard(moneyRequestDto);
        } else {
            Account account = accountRepository.findByAccountNumberAndCustomerCustomerNumber(moneyRequestDto.getAccountNumber(), moneyRequestDto.getCustomerNumber());
            if (account == null || !AccountStatus.ACTIVE.equals(account.getStatus().getStatus()))
                throw new ResourceNotFoundException("Account does not exist with number %s or Account is not in active state to withdraw amount", moneyRequestDto.getAccountNumber());

            if (account.getBalance().getValue() + account.getOverdraftLimit().getValue() < moneyRequestDto.getAmount().getValue()) {
                throw new ValidationException("Insufficient Funds on account : " + moneyRequestDto.getAccountNumber());
            }
            Transaction transaction = new Transaction();
            transaction.setRef(UUID.randomUUID().toString());
            transaction.setType(TransactionType.DEBIT);
            transaction.setStatus(TransactionStatus.PROCESSING);
            transaction.setAmount(amountDtoConverter.convertToEntity(moneyRequestDto.getAmount()));
            transaction.setTransactionMode(moneyRequestDto.getTransactionMode());
            transaction.setTransactionModeReferenceNumber(moneyRequestDto.getAccountNumber());
            transaction.setCategory(moneyRequestDto.getCategory());
            transaction.setMerchant(moneyRequestDto.getMerchant());
            transaction.setLocation(moneyRequestDto.getLocation());
            transaction.setAccount(account);
            transaction = transactionRepository.saveAndFlush(transaction);

            account.setBalance(CalculatorUtil.subAmount(account.getBalance(), moneyRequestDto.getAmount()));
            account = accountRepository.saveAndFlush(account);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction = transactionRepository.saveAndFlush(transaction);
            return transactionConverter.convertToDto(transaction);
        }

    }

    private TransactionDto WithdrawalByCard(MoneyRequestDto moneyRequestDto) {
        Card card = cardRepository.findByCardNumber(moneyRequestDto.getCardNumber());
        if (card == null || !AccountStatus.ACTIVE.equals(card.getStatus().getStatus()))
            throw new ResourceNotFoundException("Card does not exist with number %s or Card is not in active state to withdraw amount", moneyRequestDto.getAccountNumber());

        if(moneyRequestDto.getAmount().getValue() > card.getLimit().getValue()) {
            throw new ValidationException("Exceeding the withdrawal limit");
        }

        Account account = card.getAccount();
        if (account.getBalance().getValue() + account.getOverdraftLimit().getValue() < moneyRequestDto.getAmount().getValue()) {
            throw new ValidationException("Insufficient Funds on account : " + moneyRequestDto.getAccountNumber());
        }
        Transaction transaction = new Transaction();
        transaction.setRef(UUID.randomUUID().toString());
        transaction.setType(TransactionType.DEBIT);
        transaction.setStatus(TransactionStatus.PROCESSING);
        transaction.setAmount(amountDtoConverter.convertToEntity(moneyRequestDto.getAmount()));
        transaction.setTransactionMode(moneyRequestDto.getTransactionMode());
        transaction.setCategory(moneyRequestDto.getCategory());
        transaction.setMerchant(moneyRequestDto.getMerchant());
        transaction.setLocation(moneyRequestDto.getLocation());
        transaction.setTransactionModeReferenceNumber(moneyRequestDto.getCardNumber());
        transaction.setAccount(account);
        transaction = transactionRepository.saveAndFlush(transaction);

        account.setBalance(CalculatorUtil.subAmount(account.getBalance(), moneyRequestDto.getAmount()));
        account = accountRepository.saveAndFlush(account);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction = transactionRepository.saveAndFlush(transaction);
        return transactionConverter.convertToDto(transaction);
    }

    @Override
    public TransactionDto transfer(MoneyTransferRequestDto moneyTransferRequestDto) {
        Account account = accountRepository.findByAccountNumberAndCustomerCustomerNumber(moneyTransferRequestDto.getAccountNumber(), moneyTransferRequestDto.getCustomerNumber());
        if (account == null || !AccountStatus.ACTIVE.equals(account.getStatus().getStatus()))
            throw new ResourceNotFoundException("Account does not exist with number %s or Account is not in active state to transfer amount", moneyTransferRequestDto.getAccountNumber());
        if (account.getBalance().getValue() + account.getOverdraftLimit().getValue() < moneyTransferRequestDto.getAmount().getValue())
            throw new ValidationException("Insufficient Funds on account : " + moneyTransferRequestDto.getAccountNumber());
        Account receiverAccount = accountRepository.findByAccountNumber(moneyTransferRequestDto.getAccountNumber());
        if (receiverAccount == null || !AccountStatus.ACTIVE.equals(account.getStatus().getStatus()))
            throw new ResourceNotFoundException("Recipient Account does not exist with number %s or Account is not in active state to transfer amount", moneyTransferRequestDto.getAccountNumber());
        if(TransactionMode.DEBIT_CARD.equals(moneyTransferRequestDto.getTransactionMode()) ||
                TransactionMode.CREDIT_CARD.equals(moneyTransferRequestDto.getTransactionMode())) {
            throw new ValidationException("Invalid transaction mode for transfer of amount");
        }
        // Transaction at sender's side
        Transaction senderTransaction = new Transaction();
        senderTransaction.setRef(UUID.randomUUID().toString());
        senderTransaction.setStatus(TransactionStatus.PROCESSING);
        senderTransaction.setAmount(amountDtoConverter.convertToEntity(moneyTransferRequestDto.getAmount()));
        senderTransaction.setType(TransactionType.DEBIT);
        senderTransaction.setCategory(moneyTransferRequestDto.getCategory());
        senderTransaction.setMerchant(moneyTransferRequestDto.getMerchant());
        senderTransaction.setLocation(moneyTransferRequestDto.getLocation());
        senderTransaction.setToAccountNumber(moneyTransferRequestDto.getToAccountNumber());
        senderTransaction.setTransactionModeReferenceNumber(moneyTransferRequestDto.getToAccountNumber());
        senderTransaction.setAccount(account);
        senderTransaction.setTransactionMode(moneyTransferRequestDto.getTransactionMode());
        senderTransaction = transactionRepository.saveAndFlush(senderTransaction);
        account.setBalance(CalculatorUtil.subAmount(account.getBalance(), moneyTransferRequestDto.getAmount()));
        account = accountRepository.saveAndFlush(account);


        // Transaction at receiver's side
        Transaction reveiverTransaction = new Transaction();
        reveiverTransaction.setRef(UUID.randomUUID().toString());
        reveiverTransaction.setStatus(TransactionStatus.PROCESSING);
        reveiverTransaction.setAmount(amountDtoConverter.convertToEntity(moneyTransferRequestDto.getAmount()));
        reveiverTransaction.setType(TransactionType.CREDIT);
        reveiverTransaction.setCategory(moneyTransferRequestDto.getCategory());
        reveiverTransaction.setMerchant(moneyTransferRequestDto.getMerchant());
        reveiverTransaction.setLocation(moneyTransferRequestDto.getLocation());
        reveiverTransaction.setTransactionModeReferenceNumber(moneyTransferRequestDto.getAccountNumber());
        reveiverTransaction.setToAccountNumber(moneyTransferRequestDto.getToAccountNumber());
        reveiverTransaction.setAccount(receiverAccount);
        reveiverTransaction.setTransactionMode(moneyTransferRequestDto.getTransactionMode());
        reveiverTransaction = transactionRepository.saveAndFlush(reveiverTransaction);
        receiverAccount.setBalance(CalculatorUtil.addAmount(account.getBalance(), moneyTransferRequestDto.getAmount()));
        account = accountRepository.saveAndFlush(receiverAccount);

        reveiverTransaction.setStatus(TransactionStatus.SUCCESS);
        reveiverTransaction = transactionRepository.saveAndFlush(reveiverTransaction);
        senderTransaction.setStatus(TransactionStatus.SUCCESS);
        senderTransaction = transactionRepository.saveAndFlush(reveiverTransaction);

        return transactionConverter.convertToDto(senderTransaction);

    }

    @Override
    public TransactionDto externalTransfer(MoneyTransferRequestDto moneyTransferRequestDto) {
        Account account = accountRepository.findByAccountNumberAndCustomerCustomerNumber(moneyTransferRequestDto.getAccountNumber(), moneyTransferRequestDto.getCustomerNumber());
        if (account == null || !AccountStatus.ACTIVE.equals(account.getStatus().getStatus()))
            throw new ResourceNotFoundException("Account does not exist with number %s or Account is not in active state to transfer amount", moneyTransferRequestDto.getAccountNumber());
        if (account.getBalance().getValue() + account.getOverdraftLimit().getValue() < moneyTransferRequestDto.getAmount().getValue())
            throw new ValidationException("Insufficient Funds on account : " + moneyTransferRequestDto.getAccountNumber());
        if(TransactionMode.DEBIT_CARD.equals(moneyTransferRequestDto.getTransactionMode()) ||
                TransactionMode.CREDIT_CARD.equals(moneyTransferRequestDto.getTransactionMode())) {
            throw new ValidationException("Invalid transaction mode for transfer of amount");
        }
        // Transaction at sender's side
        Transaction senderTransaction = new Transaction();
        senderTransaction.setRef(UUID.randomUUID().toString());
        senderTransaction.setStatus(TransactionStatus.PROCESSING);
        senderTransaction.setAmount(amountDtoConverter.convertToEntity(moneyTransferRequestDto.getAmount()));
        senderTransaction.setType(TransactionType.DEBIT);
        senderTransaction.setCategory(moneyTransferRequestDto.getCategory());
        senderTransaction.setMerchant(moneyTransferRequestDto.getMerchant());
        senderTransaction.setLocation(moneyTransferRequestDto.getLocation());
        senderTransaction.setToAccountNumber(moneyTransferRequestDto.getToAccountNumber());
        senderTransaction.setAccount(account);
        senderTransaction.setExternalTransfer(true);
        senderTransaction.setTransactionMode(moneyTransferRequestDto.getTransactionMode());
        senderTransaction = transactionRepository.saveAndFlush(senderTransaction);
        account.setBalance(CalculatorUtil.subAmount(account.getBalance(), moneyTransferRequestDto.getAmount()));
        account = accountRepository.saveAndFlush(account);

        //Make a call to other bank to deposit amount

        return transactionConverter.convertToDto(senderTransaction);
    }

    @Override
    public TransactionDto externalTransferAcknowledge(MoneyTransferAckDto moneyTransferAckDto) {
        Transaction transaction = transactionRepository.findByRef(moneyTransferAckDto.getRef());
        if(!transaction.isExternalTransfer()) {
            throw new ValidationException("Invalid Reference number to acknowledge transfer");
        }
        if(moneyTransferAckDto.isSuccess()) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            transactionRepository.saveAndFlush(transaction);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            Account account = transaction.getAccount();
            account.setBalance(CalculatorUtil.addAmount(account.getBalance(), transaction.getAmount()));
            transaction.setAccount(account);
            transactionRepository.saveAndFlush(transaction);
        }
        return transactionConverter.convertToDto(transaction);
    }
}
