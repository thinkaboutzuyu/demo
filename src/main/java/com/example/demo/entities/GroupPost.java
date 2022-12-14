package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "group_posts")
public class GroupPost extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "groupPostSeqGen", sequenceName = "groupPostSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="groupPostSeqGen")
    @Column(name = "group_post_id", nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "parent_group")
    private Integer parentGroup;

    public GroupPost(String title) {
        this.title = title;
    }

    public GroupPost(String title, Integer parentGroup) {
        this.title = title;
        this.parentGroup = parentGroup;
    }
}