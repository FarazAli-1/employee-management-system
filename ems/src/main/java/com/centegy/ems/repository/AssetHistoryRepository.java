package com.centegy.ems.repository;


import com.centegy.ems.model.AssetHistory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AssetHistoryRepository {

    private final List<AssetHistory> assetHistoryList = new ArrayList<>();

    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public  AssetHistory save(AssetHistory assetHistory) {
        if(assetHistory.getId() == null) {
            assetHistory.setId(idGenerator.getAndIncrement());
            assetHistoryList.add(assetHistory);

        } else  {
            int index = -1;
            for( int i = 0; i < assetHistoryList.size(); i++ ) {
                if(assetHistoryList.get(i).getId().equals(assetHistory.getId())) {
                    index = i;
                    break;
                }
           }
            if(index == -1) {
                assetHistoryList.set(index, assetHistory);

            }

        }
        return assetHistory;
    }

    public AssetHistory findAssetHistory(Integer assetId) {


        for(AssetHistory assetHistory : assetHistoryList) {
            if(assetHistory.getAssetId().equals(assetId) && assetHistory.getReturnDate() == null) {
                return assetHistory;
            }
        }
        return null;
    }


}
