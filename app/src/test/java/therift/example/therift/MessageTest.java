package therift.example.therift;

import main.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class MessageTest {

    private Message message;

    @Before
    public void setUp() {
        // Initialisation de l'objet Message avant chaque test
        message = new Message("Hello", "Alexandre", "John");
    }

    @Test
    public void testMessageConstructor() {
        // Vérification que le constructeur par défaut fonctionne correctement
        assertNotNull(message);
        assertEquals("Hello", message.getMessage());
        assertEquals("Alexandre", message.getFrom());
        assertEquals("John", message.getTo());
        assertFalse(message.isSee());  // Par défaut, 'see' est false
    }

    @Test
    public void testSettersAndGetters() {
        // Vérification des setters et getters
        message.setMessage("New message");
        message.setFrom("Sarah");
        message.setTo("David");

        assertEquals("New message", message.getMessage());
        assertEquals("Sarah", message.getFrom());
        assertEquals("David", message.getTo());
    }

    @Test
    public void testSee() {
        // Vérification du champ 'see'
        assertFalse(message.isSee());  // Par défaut, 'see' est false

        // Modifier le statut de 'see'
        message.setSee(true);
        assertTrue(message.isSee());  // Après modification, 'see' devrait être true
    }
}
