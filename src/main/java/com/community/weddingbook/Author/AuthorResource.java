//package com.community.weddingbook.Author;
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
//public class AuthorResource extends EntityModel<Author> {
//
//    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(AuthorController.class);
//
//    public AuthorResource(){ }
//
//    public static EntityModel<Author> of(Author author, String profile){
//        List<Link> links = getSelfLink(author);
//        links.add(Link.of(profile, "profile"));
//        return EntityModel.of(author, links);
//    }
//
//    public static EntityModel<Author> of(Author author){
//        List<Link> links = getSelfLink(author);
//        return EntityModel.of(author, links);
//    }
//
//    private static List<Link> getSelfLink(Author author) {
//        selfLinkBuilder.slash(author.getId());
//        List<Link> links = new ArrayList<>();
//        links.add(selfLinkBuilder.withSelfRel());
//        return links;
//    }
//
//    public static URI getCreatedUri(Author author) {
//        return selfLinkBuilder.slash(author.getId()).toUri();
//    }
//}
