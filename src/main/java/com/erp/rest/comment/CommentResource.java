package com.erp.rest.comment;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.IntStream;

@Path("/comments")
@Api(value = "/comments")
public class CommentResource {
    @EJB
    private CommentBean  commentBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "create new comment", response = Comment.class, responseContainer = "Comment")
    public Response insertComment(Comment comment) {
        return Response.ok().entity(commentBean.create(comment)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get all comments", response = Comment.class, responseContainer = "Comment")
    public Response getComments() {
        List<Comment> comments = commentBean.getAll();

        if(CollectionUtils.isEmpty(comments)) {
            addDozensOfComments();
            comments = commentBean.getAll();
        }



        return Response.ok().entity(comments).build();
    }

    private void addDozensOfComments() {
        IntStream.range(0, 21).forEach(i -> {
            Comment comment = Comment.builder().content("this is my super content").build();
            commentBean.create(comment);
        });
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "update comment", response = Comment.class, responseContainer = "Comment")
    public Response updateComment(Comment comment) {
        return Response.ok(commentBean.update(comment)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get comment data by id", response = Comment.class, responseContainer = "id")
    public Response getComment(@PathParam("id") Long id) {
        Comment comment = commentBean.get(id);
        return Response.ok(comment).build();
    }
}
