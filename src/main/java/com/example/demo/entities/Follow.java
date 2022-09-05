package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {
    @Id
    @SequenceGenerator(name = "followSeqGen", sequenceName = "followSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="followSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_by_user_id")
    private User followedByUser;
}