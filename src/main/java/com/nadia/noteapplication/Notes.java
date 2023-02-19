package com.nadia.noteapplication;

import javax.persistence.*;

@Entity
@Table(name = "notes")

public class Notes {

    //Properties --------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    public int noteId;

    @Column(name = "note_title")
    public String noteTitle;
    @Column(name = "note_content")
    public String noteContent;

    //Foreign key
    @ManyToOne
    @JoinColumn(name = "tag_id")
    public Tags tag_id;


    //Constructor --------------------------------------------------------------------------
    public Notes() {
    }

    public Notes(int noteId, String noteTitle, String noteContent) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }


    public Notes(String text, String format) {
    }

    public Notes(String text) {
    }

    //Getters & setters --------------------------------------------------------------------------
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}
