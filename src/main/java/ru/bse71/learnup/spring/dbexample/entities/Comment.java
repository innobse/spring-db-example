package ru.bse71.learnup.spring.dbexample.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by bse71
 * Date: 18.08.2021
 * Time: 23:42
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Integer id;
    private String text;
    private Post post;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
