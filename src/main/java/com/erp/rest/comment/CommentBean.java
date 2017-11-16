package com.erp.rest.comment;


import com.erp.rest.base.AbstractCRUDBean;
import com.erp.rest.base.CRUDService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Slf4j
@Stateless
public class CommentBean extends AbstractCRUDBean<Comment> {
    @Inject
    private CRUDService<Comment> commentCRUDService;

    @Override
    public CRUDService<Comment> getCRUDService() {
        return commentCRUDService;
    }

    @Override
    public Comment create(Comment entity) {
        return getCRUDService().create(entity);
    }

    @Override
    public List<Comment> getAll() {
        return commentCRUDService.getAll();
    }
}
