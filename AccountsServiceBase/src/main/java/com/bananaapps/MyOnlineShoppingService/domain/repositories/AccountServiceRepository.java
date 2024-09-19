package com.bananaapps.MyOnlineShoppingService.domain.repositories;

import com.bananaapps.MyOnlineShoppingService.domain.entities.Account;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.InsuficientBalanceException;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.NoSuchAccountException;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.WithdrawException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountServiceRepository extends JpaRepository<Account, Long> {
    @Query("SELECT u FROM Account u WHERE u.owner.id = :id")
    Optional<List<Account>> getAccountsByUser(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Account u WHERE u.owner.id = :id")
    void deleteAllAccountsByUser(@Param("id") Long id);


     @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    default boolean doWithDrawnEvenWithAnotherAccountUser(Long accountId, Long userId, double amount) {
        Account account = this.findById(accountId).orElseThrow(
            () -> new NoSuchAccountException("No existe una cuenta con la id " + accountId)
        );

        if (account.getBalance() < amount) {
            List<Account> accountList = getAccountsByUser(userId).orElseThrow(
                () -> new WithdrawException("No existen cuentas para el usuario con id: " + userId)
            );

            double totalAmount = amount;
            totalAmount -= account.getBalance();
            account.setBalance(0);
            save(account);

            for (Account u : accountList) {
                if (!u.getId().equals(account.getId())) {
                    if (u.getBalance() < totalAmount) {
                        totalAmount -= u.getBalance();
                        u.setBalance(0);
                    } else {
                        u.setBalance(u.getBalance() - totalAmount);
                        totalAmount = 0;
                    }
                    save(u);

                    if (totalAmount <= 0) {
                        break;
                    }
                }
            }
            if (totalAmount > 0) {
                throw new InsuficientBalanceException("Saldo insuficiente");
            }

            return true;
        }else{
            account.setBalance(account.getBalance() - amount);
            save(account);
        }
        return false;
    }
}
