package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.User;
import com.newOne.entity.Wallet;
import com.newOne.entity.WalletAudit;
import com.newOne.entity.TransactionType;
import com.newOne.entity.TransactionReason;
import com.newOne.repository.UserRepository;
import com.newOne.repository.WalletRepository;
import com.newOne.repository.WalletAuditRepository;
import com.newOne.request.WalletTopupRequest;
import com.newOne.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletAuditRepository walletAuditRepository;

    // Method to top up the wallet
    @Transactional
    @Override
    public String topUpWallet(WalletTopupRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            throw new EcommerceException(HttpStatus.NOT_FOUND, "User not found.");
        }

        User user = userOptional.get();
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "Wallet not found."));

        // Update wallet balance
        BigDecimal newBalance = BigDecimal.valueOf(wallet.getBalance()).add(request.getAmount());
        wallet.setBalance(newBalance.intValue());
        walletRepository.save(wallet);

        // Record the transaction in WalletAudit
        WalletAudit walletAudit = new WalletAudit(user, wallet, request.getAmount(), TransactionType.CREDIT, TransactionReason.TOPUP);
        walletAuditRepository.save(walletAudit);

        return "Wallet topped up successfully.";
    }

    // Method to delete a wallet
    @Transactional
    @Override
    public String deleteWallet(int userId) {
        Optional<Wallet> walletOptional = walletRepository.findByUserId((long) userId);

        if (walletOptional.isEmpty()) {
            throw new EcommerceException(HttpStatus.NOT_FOUND, "Wallet not found.");
        }

        Wallet wallet = walletOptional.get();
        wallet.setDeleted(true);  // Soft delete
        walletRepository.save(wallet);

        return "Wallet deleted successfully.";
    }

    // Method to get wallet details
    @Override
    public Wallet getWalletDetails(int userId) {
        return walletRepository.findByUserId((long) userId)
                .orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "Wallet not found."));
    }
}
