package com.nadia.noteapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.persistence.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController {

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("hibernate");

    private static ObservableList<String> OPTIONS = FXCollections.observableArrayList();
    public List<Notes> notes;
    public List<Notes> notes1;
    public List<Tags> tags;
    public List<Tags> tags1;
  //  public List<NotesTags> notesTags;

    @FXML
    private ListView<String> lvNotes;
    @FXML
    private ListView<String> lvTags;

    @FXML
    private TextArea taContent;

    @FXML
    private TextField tfRubrik;

    @FXML
    private TextField tfSearchNotes;


    @FXML
    private TextField tfSearchTags;

  public HelloController() {
  }

  @FXML
  void initialize() {

    //Connection to database
    EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;

    //SQL query transaction begins
    transaction = entityManager.getTransaction();
    transaction.begin();

    //Selects from the notes table
    TypedQuery<Notes> allNoteQuery = entityManager.createQuery("from Notes", Notes.class);
    notes = allNoteQuery.getResultList();

    //Selects from the tags table
    TypedQuery<Tags> allTagsQuery = entityManager.createQuery("from Tags", Tags.class);
    tags = allTagsQuery.getResultList();


    for (Notes n1 : notes) {
      OPTIONS.add(n1.getNoteTitle());
    }

    lvNotes.setItems(OPTIONS);
  }


  @FXML
    void btnSearchTagsClicked(ActionEvent event) {
        String searchText = tfSearchTags.getText().toLowerCase();
        ObservableList<String> items = lvTags.getItems();
        lvTags.getSelectionModel().clearSelection();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).toLowerCase().contains(searchText)) {
                lvTags.getSelectionModel().select(i);
                return;
            }
        }
        // If the tag is not found, display an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tag not found");
        alert.setHeaderText(null);
        alert.setContentText("The tag you are looking for was not found.");
        alert.showAndWait();
    }

    @FXML
    void btnSearchNotesClicked(ActionEvent event) {
        String searchText = tfSearchNotes.getText().toLowerCase();
        for (int i = 0; i < lvNotes.getItems().size(); i++) {
            String noteText = lvNotes.getItems().get(i).toLowerCase();
            if (noteText.contains(searchText)) {
                lvNotes.getSelectionModel().select(i);
                return;
            }
        }
        // If the note is not found, display an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Note not found");
        alert.setHeaderText(null);
        alert.setContentText("The note you are looking for was not found.");
        alert.showAndWait();
    }


    // Add a new note to the list view
    @FXML
    void btnNewNotesClicked(ActionEvent event) {

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Notes newNote = new Notes();
            newNote.setNoteTitle(tfRubrik.getText());
            newNote.setNoteContent("");

            entityManager.persist(newNote);

            transaction.commit();

            lvNotes.getItems().add(newNote.getNoteTitle());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Delete the selected note from the list view
    @FXML
    void btnDeleteClicked(ActionEvent event) {

        int selectedNoteIndex = lvNotes.getSelectionModel().getSelectedIndex();
        if (selectedNoteIndex >= 0) {
            lvNotes.getItems().remove(selectedNoteIndex);
        }
    }

    // Add a new tag to the list view
    @FXML
    void btnNewTagClicked(ActionEvent event) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        transaction = entityManager.getTransaction();
        transaction.begin();

        Query query1 = entityManager.createNativeQuery("INSERT INTO tag (content) " + "VALUES(?)");

        String newTag = lvTags.getItems().get(lvTags.getItems().size()-1);
        query1.setParameter(1, newTag);
        query1.executeUpdate();

        TypedQuery<Tags> allTagsQuery1 = entityManager.createQuery("from Tags WHERE tagId=(SELECT max(tagId) FROM Tags)", Tags.class);
        tags1 = allTagsQuery1.getResultList();

        transaction.commit();

        // Create new note with title and content
        Notes newNote = new Notes(tfRubrik.getText(), taContent.getText());

        // Set the tags for the new note
        for (String tagContent : lvTags.getSelectionModel().getSelectedItems()) {
            Tags tag = entityManager.createQuery("FROM Tags WHERE tagContent = :content", Tags.class)
                    .setParameter("content", tagContent)
                    .getSingleResult();
            newNote.getTags().add(tag);
        }

        // Save the new note to the database
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(newNote);
        transaction.commit();

        // Add the new note to the ListView
        lvNotes.getItems().add(newNote.getNoteTitle());

        // Clear the text fields
        tfRubrik.clear();
        taContent.clear();
    }

    // Delete the selected tag from the list view
    @FXML
    void btnDeleteTagClicked(ActionEvent event) {
            int selectedTagIndex = lvTags.getSelectionModel().getSelectedIndex();
            if (selectedTagIndex >= 0) {
                lvTags.getItems().remove(selectedTagIndex);

                EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
                EntityTransaction transaction = null;
                transaction = entityManager.getTransaction();
                transaction.begin();

                // Get the selected tag from the ListView
                String selectedTagContent = lvTags.getSelectionModel().getSelectedItem();
                Tags selectedTag = entityManager.createQuery("FROM Tags WHERE tagContent = :content", Tags.class)
                        .setParameter("content", selectedTagContent)
                        .getSingleResult();

                // Delete the tag
                entityManager.remove(selectedTag);

                // Commit the transaction
                transaction.commit();

                // Refresh the ListView
                lvTags.getItems().remove(selectedTagContent);
            }
  }

    // Update the selected note with the new content
    @FXML
    void btnUpdateClicked(ActionEvent event) {
        int selectedNoteIndex = lvNotes.getSelectionModel().getSelectedIndex();
        if (selectedNoteIndex != -1) {
            String noteText = lvNotes.getItems().get(selectedNoteIndex);
            String contentText = taContent.getText();
            noteText = noteText + " - " + contentText;
            lvNotes.getItems().set(selectedNoteIndex, noteText);
        }

     String updateTitle = tfRubrik.getCharacters().toString();
      String updateContent = taContent.getText().toString();

      EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction transaction = null;

      transaction = entityManager.getTransaction();
      transaction.begin();

      Notes noteToUpdate = entityManager.find(Notes.class, notes1);
      noteToUpdate.setnotedata(updateTitle, updateContent);

      entityManager.merge(noteToUpdate);

      transaction.commit();
}
    public void initialize(URL url, ResourceBundle resourceBundle){
        lvNotes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String[] parts = newValue.split(" - "); // or use your desired formatting
                if (parts.length == 2) {
                    taContent.setText(parts[1]);
                } else {
                    taContent.setText("");
                }
            }
        });

    }

}
