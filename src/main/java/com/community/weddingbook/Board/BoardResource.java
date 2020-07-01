//package com.community.weddingbook.Board;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.Link;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//
//public class BoardResource extends EntityModel<Board> {
//
//    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(BoardController.class);
//
//    private BoardResource(){ }
//
//    public static EntityModel<Board> of(Board board, String profile){
//        List<Link> links = getSelfLink(board);
//        links.add(Link.of(profile, "profile"));
//        return EntityModel.of(board, links);
//    }
//
//    public static EntityModel<Board> of(Board board){
//        List<Link> links = getSelfLink(board);
//        return EntityModel.of(board, links);
//    }
//
//    private static List<Link> getSelfLink(Board board) {
//        selfLinkBuilder.slash(board.getId());
//        List<Link> links = new ArrayList<>();
//        links.add(selfLinkBuilder.withSelfRel());
//        return links;
//    }
//
//    public static URI getCreatedUri(Board board) {
//        return selfLinkBuilder.slash(board.getId()).toUri();
//    }
//}
