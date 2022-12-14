package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_files")
public class PostFile {
    @Id
    @SequenceGenerator(name = "postFileSeqGen", sequenceName = "postFileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="postFileSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_file_id")
    private File postFile;

    public PostFile(Post post, File postFile) {
        this.post = post;
        this.postFile = postFile;
    }
}