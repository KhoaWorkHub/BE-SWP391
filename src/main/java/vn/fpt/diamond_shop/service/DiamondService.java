package vn.fpt.diamond_shop.service;

import vn.fpt.diamond_shop.request.AddDiamondRequest;
import vn.fpt.diamond_shop.response.ListDiamondReponse;
import vn.fpt.diamond_shop.response.GetDetailDiamondResponse;

import java.util.List;

public interface DiamondService {
    void addDiamond(AddDiamondRequest addDiamondRequest);

    List<ListDiamondReponse> listDiamonds();

    GetDetailDiamondResponse getDetailDiamondResponse (long id);
}
