package ru.bse71.learnup.spring.dbexample.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bse71
 * Date: 18.08.2021
 * Time: 23:41
 */

@Entity
@Table(name = "posts")
@NoArgsConstructor
public class Post extends AbstractTextEntity {

    @Column
    @Getter
    @Setter
    private String title;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private List<Comment> comments;

    public Post(Integer id, String title, String text) {
        super(id, text);
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Post id: %d\n%s\n%s\n", id, title, text));

        if (comments != null) {
            for (Comment comment : comments) {
                sb
                        .append(
                                String.format("\t%s\n", comment.getText()));
            }
        }

        return sb.toString();
    }
}
