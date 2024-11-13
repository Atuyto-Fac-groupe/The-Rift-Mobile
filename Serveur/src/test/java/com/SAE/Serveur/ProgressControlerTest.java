package com.SAE.Serveur;

import com.SAE.Serveur.Controler.ProgressControler;
import com.SAE.Serveur.Controler.Repository.ProgressRepository;
import com.SAE.Serveur.Model.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressControler.class)
public class ProgressControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressRepository progressRepository;

    private Progress progress;

    @BeforeEach
    public void setUp() {
        progress = new Progress();
        progress.setId(1L);
    }

    @Test
    public void testGetProgress() throws Exception {
        when(progressRepository.findById(1L)).thenReturn(Optional.of(progress));

        mockMvc.perform(get("/api/Progress/getProgress/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(progress.getId()));

        verify(progressRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProgressNotFound() throws Exception {
        when(progressRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/Progress/getProgress/1"))
                .andExpect(status().isNotFound());

        verify(progressRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllProgress() throws Exception {
        Progress progress2 = new Progress();
        progress2.setId(2L);


        when(progressRepository.findAll()).thenReturn(Arrays.asList(progress, progress2));

        mockMvc.perform(get("/api/Progress/getAllProgress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(progress.getId()))
                .andExpect(jsonPath("$[1].id").value(progress2.getId()));

        verify(progressRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteProgress() throws Exception {
        when(progressRepository.findById(1L)).thenReturn(Optional.of(progress));

        mockMvc.perform(get("/api/Progress/deleteProgress/1"))
                .andExpect(status().isOk());

        verify(progressRepository, times(1)).findById(1L);
        verify(progressRepository, times(1)).delete(progress);
    }

    @Test
    public void testDeleteProgressNotFound() throws Exception {
        when(progressRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/Progress/deleteProgress/1"))
                .andExpect(status().isNotFound());

        verify(progressRepository, times(1)).findById(1L);
        verify(progressRepository, never()).delete(any(Progress.class));
    }

}
