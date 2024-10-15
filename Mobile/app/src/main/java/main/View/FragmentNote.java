package main.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.therift.databinding.FragmentNoteBinding;

import io.objectbox.Box;
import main.Model.BDD.ObjectBox;
import main.Model.Notes;

import java.util.Objects;

public class FragmentNote extends Fragment {

    private FragmentNoteBinding binding;
    private Notes currentNote;
    private Box<Notes> noteBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(getLayoutInflater());
        EditText noteEditText = binding.etNoteText;
        noteBox = ObjectBox.get().boxFor(Notes.class);
        loadLastNote();

        noteEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveOrUpdateNote();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.etNoteText.post(() -> {
            focusAtEndOfText();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveOrUpdateNote();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveOrUpdateNote();
    }

    private void loadLastNote() {
        new Thread(() -> {
            currentNote = noteBox.get(1);
            if (currentNote != null) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> binding.etNoteText.setText(currentNote.getContainerText()));
            }
        }).start();
    }

    private void saveOrUpdateNote() {
        String noteText = binding.etNoteText.getText().toString();

        new Thread(() -> {
            if (currentNote == null) {
                currentNote = new Notes();
                currentNote.setContainerText(noteText);
                noteBox.put(Objects.requireNonNull(currentNote));
            } else {
                currentNote.setContainerText(noteText);
                noteBox.put(Objects.requireNonNull(currentNote));
            }
        }).start();
    }

    private void focusAtEndOfText() {
        binding.etNoteText.requestFocus();
        binding.etNoteText.post(() -> {
            if (binding.etNoteText.getText() != null) {
                // Set the cursor to the end of the text
                binding.etNoteText.setSelection(binding.etNoteText.getText().length());
            }
        });
    }
}
