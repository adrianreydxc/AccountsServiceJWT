package com.bananaapps.MyOnlineShoppingService.domain.services.impl;

import com.bananaapps.MyOnlineShoppingService.domain.dto.request.LoanDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.request.MoneyTransactionsDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.dto.response.AccountDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.*;
import com.bananaapps.MyOnlineShoppingService.domain.mappers.AccountMapper;
import com.bananaapps.MyOnlineShoppingService.domain.mappers.AccountMapper;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.AccountServiceRepository;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.CustomerRepository;
import com.bananaapps.MyOnlineShoppingService.domain.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountServiceRepository accountServiceRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    @Override
    public List<AccountDto> getAllAcounts(){
        try {
            List<Account> list = accountServiceRepository.findAll();
            if (list.isEmpty()) {
                throw new NoSuchAccountsException("No hay ninguna cuenta en la base de datos");
            }
            return list.stream()
                    .map(accountMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new NoSuchAccountsException("Error al obtener todas las cuentas");
        }
    }

    @Override
    public AccountDto getAccountById(Long id, Long userId) {
        return accountMapper.toDto(
                accountServiceRepository.findById(id)
                        .filter(account -> account.getOwner().getId().equals(userId)).orElseThrow(()
                                -> new NoSuchAccountException("Error al obtener la cuenta con id: " + id))
        );
    }

    @Override
    public List<AccountDto> getAccountsByUser(Long userId) {
        return accountServiceRepository.getAccountsByUser(userId)
                .orElseThrow(() -> new AccountsByUserException("El usuario con id: " + userId + " no tiene ninguna cuenta en la base de datos"))
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        try {
            Account account = AccountMapper.toEntity(accountDto, customerRepository.findById(accountDto.getOwner_id()).orElseThrow(()-> new NoSuchCustomerException("No se ha encotrado el usuario")));
            accountServiceRepository.save(account);
            return accountMapper.toDto(account);
        } catch (Exception e) {
            throw new NewAccountException("Error al crear cuenta");
        }
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        try {
            Account account = AccountMapper.toEntity(accountDto, customerRepository.findById(accountDto.getOwner_id()).orElseThrow(()-> new NoSuchCustomerException("No se ha encotrado el usuario")));
            Account updatedAccount = accountServiceRepository.save(account);
            return accountMapper.toDto(updatedAccount);
        }catch (Exception e){
              throw new NewAccountException("Error al actualizar account");
            //return false;
        }
    }

    @Override
    public boolean deleteAccount(Account account) {
          try{
            accountServiceRepository.delete(account);

            return true;
        }catch (Exception e){
            throw new NewAccountException("Error al eliminar account");
//            return false;
        }
    }

    @Override
    public boolean doWithdraw(MoneyTransactionsDto withdrawDto) {
/*      try{
         Account account = accountServiceRepository.findById(withdrawDto.getIdAccount()).orElseThrow(()
            -> new WithDrawnException("No se ha encontrado account en la base de datos con id: " + withdrawDto.getIdAccount()));
       if(account.getBalance() < withdrawDto.getAmount()){
            throw new WithDrawnException("Balance insuficiente para hacer retiro");
            //return false;
        }
        account.setBalance(account.getBalance() - withdrawDto.getAmount());
        accountServiceRepository.save(account);

        return true;
        }catch (Exception e){
         throw new WithDrawnException("Error al hacer retiro de dinero");
         //return false;
     }*/
         boolean neededMoreAccounts = accountServiceRepository.doWithDrawnEvenWithAnotherAccountUser(withdrawDto.getIdAccount(),
                 withdrawDto.getIdUser(), withdrawDto.getAmount());

         return neededMoreAccounts;
    }

    @Override
    public boolean deleteAllAccountByUser(Long userId) {
        try{
            accountServiceRepository.deleteAllAccountsByUser(userId);

            return true;
        }catch (Exception e){
            throw new DeleteAllAccountsException("Error al eliminar todas las accounts del usuario con id: " + userId);
            //return false;
        }
    }

    @Override
    public boolean addMoney(MoneyTransactionsDto request) {
       try{
           Account account = accountServiceRepository.findById(request.getIdAccount()).orElseThrow(()
                   -> new AddMoneyException("Error al obtner account con id: " + request.getIdAccount()));
           account.setBalance(account.getBalance() + request.getAmount());

           accountServiceRepository.save(account);
           return true;
       }catch (Exception e){
           throw new AddMoneyException("Error al agregar dinero a la account con id: " + request.getIdAccount());
           //return false;
       }
    }

    @Override
    public boolean checkLoan(LoanDto request) {
       List<Account> accounts = accountServiceRepository.getAccountsByUser(request.getId()).orElseThrow(()
                    -> new LoanException("No se han encontrado accounst en la base de datos"));
        double totalAmount = 0;

        for(Account u : accounts){
            totalAmount += u.getBalance();
        }

        double finalAmount = totalAmount * 0.8;

        if(request.getAmount() < finalAmount){
           return true;
        }else{
            throw new LoanException("El balance total insuficiente");
        }
    }
}
