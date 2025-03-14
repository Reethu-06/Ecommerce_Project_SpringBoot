package com.newOne.service;

import com.newOne.entity.Wallet;
import com.newOne.request.WalletTopupRequest;

public interface WalletService {

    /**
     * Tops up the user's wallet with the specified amount.
     *
     * @param request The top-up request containing user ID and amount.
     * @return A success or failure message.
     */
    String topUpWallet(WalletTopupRequest request);

    /**
     * Deletes a wallet for a given user.
     *
     * @param userId The ID of the user whose wallet is to be deleted.
     * @return A success or failure message.
     */
    String deleteWallet(int userId);

    /**
     * Retrieves the wallet details for a given user.
     *
     * @param userId The ID of the user whose wallet details are to be fetched.
     * @return The wallet details for the user.
     */
    Wallet getWalletDetails(int userId);
}
