package vn.fpt.diamond_shop.response;

import lombok.Data;

@Data
public class PreOrderDetailResponse {
    private int estimateDelivery = 3;
    private UserProfileResponse userProfile;
}
