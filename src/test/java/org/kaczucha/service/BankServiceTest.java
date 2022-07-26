//package org.kaczucha.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.kaczucha.repository.ClientSpringJpaRepository;
//import org.kaczucha.repository.entity.Account;
//import org.kaczucha.repository.entity.Client;
//
//import java.util.Collections;
//import java.util.NoSuchElementException;
//
//import static org.mockito.Mockito.*;
//
//public class BankServiceTest {
//    private BankService service;
//    private ClientSpringJpaRepository clientRepository;
//
//    @BeforeEach
//    public void setup() {
//        clientRepository = mock(ClientSpringJpaRepository.class);
//        service = new BankService(clientRepository);
//    }
//
//    @Test
//    public void transfer_allParamsOk_fundsTransferred() {
//        //given
//        final String emailFrom = "a@a.pl";
//        final String emailTo = "b@b.pl";
//        final Client clientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final Client clientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(500, "PLN")));
//        final double amount = 100;
//        when(clientRepository.findByEmail(emailFrom)).thenReturn(clientFrom);
//        when(clientRepository.findByEmail(emailTo)).thenReturn(clientTo);
//
//        //when
//        service.transfer(emailFrom, emailTo, amount);
//
//        //then
//        final Client expectedClientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(900, "PLN")));
//
//        final Client expectedClientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(600, "PLN")));
//
//        verify(clientRepository).save(expectedClientFrom);
//        verify(clientRepository).save(expectedClientTo);
//    }
//
//
//    @Test
//    public void transfer_allFounds_fundsTransferred() {
//        //given
//        final String emailFrom = "a@a.pl";
//        final String emailTo = "b@b.pl";
//        final Client clientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final Client clientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(500, "PLN")));
//        final double amount = 1000;
//
//        when(clientRepository.findByEmail(emailFrom)).thenReturn(clientFrom);
//        when(clientRepository.findByEmail(emailTo)).thenReturn(clientTo);
//
//        // when
//        service.transfer(emailFrom, emailTo, amount);
//
//        // then
//        final Client expectedClientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(0, "PLN")));
//        final Client expectedClientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(1500, "PLN")));
//
//        verify(clientRepository).save(expectedClientFrom);
//        verify(clientRepository).save(expectedClientTo);
//    }
//
//    @Test
//    public void transfer_notEnoughFunds_thrownNoSufficientFundsException() {
//        // given
//        final String emailFrom = "a@a.pl";
//        final String emailTo = "b@b.pl";
//        final Client clientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final Client clientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(500, "PLN")));
//        final double amount = 30000;
//
//        when(clientRepository.findByEmail(emailFrom)).thenReturn(clientFrom);
//        when(clientRepository.findByEmail(emailTo)).thenReturn(clientTo);
//        //when/then
//        Assertions.assertThrows(NoSufficientFundsException.class, () -> service.transfer(emailFrom, emailTo, amount));
//    }
//
//    @Test
//    public void transfer_negativeAmount_thrownIllegalArgumentException() {
//        // given
//        final String emailFrom = "a@a.pl";
//        final String emailTo = "b@b.pl";
//        final Client clientFrom = new Client(
//                "Alek",
//                emailFrom,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final Client clientTo = new Client(
//                "Bartek",
//                emailTo,
//                Collections.singletonList(new Account(500, "PLN")));
//
//        final double amount = -1000;
//
//        // when/then
//        Assertions.assertThrows(
//                IllegalArgumentException.class,
//                () -> service.transfer(emailFrom, emailTo, amount)
//        );
//    }
//
//    @Test
//    public void transfer_toSameClient_thrownException() {
//        // given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 200;
//
//        // when/then
//        Assertions.assertThrows(IllegalArgumentException.class, () -> service.transfer(email, email, amount));
//    }
//
//    @Test
//    public void withdraw_correctAmount_balanceChangedCorrectly() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        when(clientRepository.findByEmail(email)).thenReturn(client);
//        final double amount = 100;
//
//        // when
//        service.withdraw(email, amount);
//
//        //then
//        final Client expectedClient = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(900, "PLN")));
//
//        verify(clientRepository).save(expectedClient);
//    }
//
//    @Test
//    public void withdraw_correctFloatingPointAmount_balanceChangedCorrectly() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        when(clientRepository.findByEmail(email)).thenReturn(client);
//        final double amount = 100.71;
//
//        //when
//        service.withdraw(email, amount);
//
//        //then
//        final Client expectedClient = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(899.29, "PLN")));
//
//        verify(clientRepository).save(expectedClient);
//    }
//
//    @Test
//    public void withdraw_allBalance_balanceSetToZero() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        when(clientRepository.findByEmail(email)).thenReturn(client);
//        final double amount = 1000;
//
//        //when
//        service.withdraw(email, amount);
//
//        //then
//        final Client expectedClient = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(0, "PLN")));
//
//        verify(clientRepository).save(expectedClient);
//    }
//
//    @Test
//    public void withdraw_negativeAmount_throwsIllegalArgumentException() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = -100;
//
//        //when/then
//        Assertions.assertThrows(
//                IllegalArgumentException.class,
//                () -> service.withdraw(email, amount)
//        );
//    }
//
//    @Test
//    public void withdraw_zeroAmount_throwsIllegalArgumentException() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 0;
//
//        //when/then
//        Assertions.assertThrows(
//                IllegalArgumentException.class,
//                () -> service.withdraw(email, amount)
//        );
//    }
//
//    @Test
//    public void withdraw_amountBiggerThenBalance_throwsNoSufficientFundsException() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 2000;
//        when(clientRepository.findByEmail(email)).thenReturn(client);
//
//        //when/then
//        Assertions.assertThrows(
//                NoSufficientFundsException.class,
//                () -> service.withdraw(email, amount)
//        );
//    }
//
//    @Test
//    public void withdraw_incorrectEmail_throwsNoSuchElementException() {
//        //given
//        final String email = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 100;
//        when(clientRepository.findByEmail(email)).thenThrow(NoSuchElementException.class);
//
//        //when/then
//        Assertions.assertThrows(
//                NoSuchElementException.class,
//                () -> service.withdraw(email, amount)
//        );
//    }
//
//
//    @Test
//    public void withdraw_upperCaseEmail_balanceChangedCorrectly() {
//        //given
//        final String email = "A@A.PL";
//        final String lowerCaseEmail = "a@a.pl";
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 100;
//        when(clientRepository.findByEmail(lowerCaseEmail)).thenReturn(client);
//
//        //when
//        service.withdraw(email, amount);
//
//        //then
//
//        final Client expectedClient = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(900, "PLN")));
//
//        verify(clientRepository).findByEmail(lowerCaseEmail);
//        verify(clientRepository).save(expectedClient);
//    }
//
//    @Test
//    public void withdraw_nullEmail_throwsIllegalArgumentException() {
//        //given
//        final String email = null;
//        final Client client = new Client(
//                "Alek",
//                email,
//                Collections.singletonList(new Account(1000, "PLN")));
//        final double amount = 100;
//
//        //when/then
//        Assertions.assertThrows(
//                IllegalArgumentException.class,
//                () -> service.withdraw(email, amount)
//        );
//    }
//}
