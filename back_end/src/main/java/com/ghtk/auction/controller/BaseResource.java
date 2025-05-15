package com.ghtk.auction.controller;

import com.ghtk.auction.dto.response.ApiResponse;
import com.ghtk.auction.dto.response.ModifyDTO;
import com.ghtk.auction.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

public abstract class BaseResource<Rq, Rs , S extends IBaseService<Rq,Rs,ID>, ID> {

    protected final S service;

    protected BaseResource(S service) {
        super();
        this.service = service;
    }

    @GetMapping
    public ApiResponse<Rs> getById(@RequestParam(value = "id") ID id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<Rs> create(@RequestBody Rq request) {
        return ApiResponse.success(service.create(request));
    }

    @PutMapping
    public ApiResponse<Rs> update(@RequestParam("id") ID id, @RequestBody Rq request) {
        return ApiResponse.success(service.update(id, request));
    }

    @DeleteMapping("")
    public ApiResponse<ModifyDTO> delete(@RequestParam(value = "id") ID id) {
        return ApiResponse.success(service.delete(id));
    }

//    @PostMapping("/search")
//    public BaseResponse<Page<Rs>> search(@RequestBody SearchRequest searchRequest) {
//        return BaseResponse.ok(service.search(searchRequest));
//    }


}
