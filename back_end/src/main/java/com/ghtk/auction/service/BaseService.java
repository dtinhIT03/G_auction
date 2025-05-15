package com.ghtk.auction.service;

import com.ghtk.auction.dto.response.ModifyDTO;
import com.ghtk.auction.exception.NotFoundException;
import com.ghtk.auction.mapper.BaseMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<E, ID , Rq, Rs, Repo extends JpaRepository<E, ID>
        ,Mp extends BaseMapper<E, Rs , Rq>>
        implements IBaseService<Rq, Rs, ID> {
    @Autowired
    protected Repo repository;
    @Autowired
    protected Mp mapper;

    public Rs getById(ID id){
        E entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found"));
        return mapper.convertEntity2Response(entity);
    }
    protected void validRequestCreate(Rq request) {}
    protected void validRequestUpdate(ID id,Rq request, E entity) {}
    protected void afterMappingRequestToEntity(Rq request, E entity) {}
    protected void afterSaveEntity(E entity,Rq request){}
    protected void afterUpdateEntity(E entity,Rq request){}
    public Rs create(Rq request){
        validRequestCreate(request);
        E entity = mapper.convertRequest2Entity(request);
        afterMappingRequestToEntity(request,entity);
        entity = repository.save(entity);
        afterSaveEntity(entity,request);
        return mapper.convertEntity2Response(entity);
    }

    protected void mapUpdatedEntity(Rq request, E entity){}
    public Rs update(ID id,Rq request){
        E updatedEntity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found"));
        validRequestUpdate(id,request,updatedEntity);
        mapper.updateEntity(request, updatedEntity);
        mapUpdatedEntity(request,updatedEntity);
        updatedEntity = repository.save(updatedEntity);
        afterUpdateEntity(updatedEntity,request);
        return mapper.convertEntity2Response(updatedEntity);
    }

    protected void beforeDelete(ID id, E entity) {}

    @Transactional
    public ModifyDTO delete(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found"));
        beforeDelete(id, entity);
        repository.delete(entity);
        return new ModifyDTO();
    }
}
