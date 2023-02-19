package com.nadia.noteapplication;

import javax.persistence.*;

@Entity
@Table(name = "tags")

public class Tags {  //Properties --------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    public int tagId;

    @Column(name = "tag_content")
    public String tagContent;

    //Foreign key
    @ManyToOne
    @JoinColumn(name = "note_id")
    public Notes note_id;


    //Constructor ----------------------------------------------------------------------------
    public Tags() {
    }

    public Tags(int tagId, String tagContent) {
        this.tagId = tagId;
        this.tagContent = tagContent;
    }

    public Tags(String text) {

    }

    //Getters & setters --------------------------------------------------------------------------
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}
