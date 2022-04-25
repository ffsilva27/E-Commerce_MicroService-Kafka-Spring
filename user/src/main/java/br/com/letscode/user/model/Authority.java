package br.com.letscode.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "authorities")
@Getter @Setter
public class Authority {

    @EmbeddedId
    private AuthorityKey authorityKey;

    @ManyToOne
    @MapsId("userName")
    @JoinColumn(name = "username")
    private User user;
}
