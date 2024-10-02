package main.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.therift.databinding.FragmentNoteBinding;

import main.Model.BDD.AppDatabase;
import main.Model.Notes;

public class FragmentNote extends Fragment {

    private FragmentNoteBinding binding;
    private AppDatabase db;
    private Notes currentNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(getLayoutInflater());
        db = AppDatabase.getInstance(this.getContext());

        EditText noteEditText = binding.etNoteText;

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
            currentNote = db.notesDao().getNoteById(1);
            if (currentNote != null) {
                getActivity().runOnUiThread(() -> binding.etNoteText.setText(currentNote.getContainerText()));
            }
        }).start();
    }

    private void saveOrUpdateNote() {
        String noteText = binding.etNoteText.getText().toString();

        new Thread(() -> {
            if (currentNote == null) {
                currentNote = new Notes();
                currentNote.setContainerText(noteText);
                db.notesDao().insertNote(currentNote);
            } else {
                currentNote.setContainerText(noteText);
                db.notesDao().updateNotes(currentNote);
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
