package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.AccountService;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    PaymentProcessorImpl paymentProcessor;

    @Test
    public void testTransfer() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 1L;
            }
        }))).thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 2L;
            }
        }))).thenReturn(List.of(destinationAccount));

        paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                0, 0, BigDecimal.ONE);

    }

    @Test
    public void testMakeTransferSourceException() {
        AccountException exception = new AccountException("Account not found");
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);
        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);
        when(accountService.getAccounts(any())).thenThrow(exception);
        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10));
            }
        });

        assertEquals(exception.getLocalizedMessage(), result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferDestinationException() {
        AccountException exception = new AccountException("Account not found destination");
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        lenient().when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 1L;
            }
        }))).thenReturn(List.of(sourceAccount));
        when(accountService.getAccounts(any(Agreement.class))).thenThrow(exception);
        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10));
            }
        });
        assertEquals(exception.getLocalizedMessage(), result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferWithComissionSourceException() {
        AccountException exception = new AccountException("Account not found");
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);
        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);
        when(accountService.getAccounts(any())).thenThrow(exception);
        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10), BigDecimal.ONE);
            }
        });

        assertEquals(exception.getLocalizedMessage(), result.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferDestinationWithComissionSourceException() {
        AccountException exception = new AccountException("Account not found destinationAccount");
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        lenient().when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 1L;
            }
        }))).thenReturn(List.of(sourceAccount));
        when(accountService.getAccounts(any(Agreement.class))).thenThrow(exception);
        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, new BigDecimal(10), BigDecimal.TEN);
            }
        });
        assertEquals(exception.getLocalizedMessage(), result.getLocalizedMessage());
    }



}
