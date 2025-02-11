package com.retailer.reward.util;

import com.retailer.reward.entity.PurchaseDetails;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardPointsCalc {

    public static void computeRewardPoints(PurchaseDetails purchaseDetails) {
        double transactionAmount = purchaseDetails.getTransactionAmount();
        double rewardPoints = 0;

        if (transactionAmount > 100) {
            rewardPoints = (transactionAmount - 100) * 2 + 50;
        } else if (transactionAmount > 50) {
            rewardPoints = transactionAmount - 50;
        }

        purchaseDetails.setOverallRewards(rewardPoints);
    }
}
