package therift.example.therift;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import main.Model.MyObjectBox;
import main.Model.Notes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.platform.app.InstrumentationRegistry;


import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class NotesTest {

    private BoxStore boxStore;
    private Box<Notes> notesBox;

    @Before
    public void setUp() {
        // Initialisation de la base de données ObjectBox
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        boxStore = MyObjectBox.builder().androidContext(context).build();
        notesBox = boxStore.boxFor(Notes.class);
    }

    @After
    public void tearDown() {
        // Nettoyage de la base de données après chaque test
        boxStore.close();
    }

    @Test
    public void testInsertNote() {
        // Créer un objet Notes
        Notes note = new Notes();
        note.setContainerText("Test note content");

        // Insérer l'objet dans la base de données
        long id = notesBox.put(note);
        assertTrue(id > 0); // Vérifie que l'ID généré est valide (positif)

        // Récupérer l'objet de la base de données
        Notes fetchedNote = notesBox.get(id);
        assertNotNull(fetchedNote); // Vérifie que l'objet est récupéré
        assertEquals("Test note content", fetchedNote.getContainerText()); // Vérifie que le contenu correspond
    }

    @Test
    public void testUpdateNote() {
        // Créer un objet Notes
        Notes note = new Notes();
        note.setContainerText("Original content");

        // Insérer l'objet dans la base de données
        long id = notesBox.put(note);
        assertTrue(id > 0); // Vérifie que l'ID est valide

        // Mettre à jour le contenu de la note
        note.setContainerText("Updated content");
        notesBox.put(note); // Mise à jour de l'objet

        // Récupérer l'objet mis à jour
        Notes updatedNote = notesBox.get(id);
        assertNotNull(updatedNote); // Vérifie que l'objet est récupéré
        assertEquals("Updated content", updatedNote.getContainerText()); // Vérifie que le contenu a été mis à jour
    }

    @Test
    public void testDeleteNote() {
        // Créer un objet Notes
        Notes note = new Notes();
        note.setContainerText("Content to delete");

        // Insérer l'objet dans la base de données
        long id = notesBox.put(note);
        assertTrue(id > 0); // Vérifie que l'ID est valide

        // Supprimer l'objet de la base de données
        notesBox.remove(id);

        // Vérifier que l'objet a bien été supprimé
        Notes deletedNote = notesBox.get(id);
        assertNull(deletedNote); // L'objet supprimé doit être null
    }
}
