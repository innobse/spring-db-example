package ru.bse71.learnup.spring.dbexample.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by bse71
 * Date: 18.08.2021
 * Time: 23:42
 */

@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Column
    @Getter
    @Setter
    private String text;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Post post;

    @Version
    private long version;

    public Comment(Integer id, String text, Post post) {
        this.id = id;
        this.text = text;
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
