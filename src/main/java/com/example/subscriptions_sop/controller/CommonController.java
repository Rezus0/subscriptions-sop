package com.example.subscriptions_sop.controller;

import com.example.subscriptions_sop.representation_model.BaseRepresentation;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class CommonController {

    @GetMapping("/")
    public ResponseEntity<EntityModel<BaseRepresentation>> getOptions() {
        BaseRepresentation representation = new BaseRepresentation();
        EntityModel<BaseRepresentation> model = EntityModel.of(representation);
        model.add(linkTo(methodOn(UserController.class).register(null))
                .withRel("register")
                .withType("POST")
                .withName("User Register"));
        model.add(linkTo(methodOn(SubscriptionController.class).subscribe(null))
                .withRel("subscribe")
                .withType("POST")
                .withName("Subscribe"));
        return ResponseEntity.ok(model);
    }
}
