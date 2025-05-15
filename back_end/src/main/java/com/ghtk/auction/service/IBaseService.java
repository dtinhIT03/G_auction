package com.ghtk.auction.service;

import com.ghtk.auction.dto.model.SearchRequest;
import com.ghtk.auction.dto.response.ModifyDTO;
import org.springframework.data.domain.Page;

public interface IBaseService<Rq, Rs, ID> {
//    <T> Page<T> search(SearchRequest searchRequest);
    Rs getById(ID id);

    Rs create(Rq request);

    Rs update(ID id, Rq request);

    ModifyDTO delete(ID id);
}
