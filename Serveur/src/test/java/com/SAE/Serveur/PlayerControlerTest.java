package com.SAE.Serveur;

import com.SAE.Serveur.Controler.PlayerControler;
import com.SAE.Serveur.Controler.Repository.PlayerRepository;
import com.SAE.Serveur.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

@WebMvcTest(PlayerControler.class)
public class PlayerControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    private Player player;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        player = new Player();
        player.setId(1L);
        player.setName("Test Player");
    }

    @Test
    public void testGetPlayer() throws Exception {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/api/Player/getPlayer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.name").value(player.getName()));

        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPlayerNotFound() throws Exception {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/Player/getPlayer/1"))
                .andExpect(status().isNotFound());

        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllPlayer() throws Exception {
        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Another Player");

        when(playerRepository.findAll()).thenReturn(Arrays.asList(player, player2));

        mockMvc.perform(get("/api/Player/getAllPlayer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(player.getId()))
                .andExpect(jsonPath("$[0].name").value(player.getName()))
                .andExpect(jsonPath("$[1].id").value(player2.getId()))
                .andExpect(jsonPath("$[1].name").value(player2.getName()));

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testDeletePlayer() throws Exception {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/api/Player/deletePlayer/1"))
                .andExpect(status().isOk());

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    public void testDeletePlayerNotFound() throws Exception {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/Player/deletePlayer/1"))
                .andExpect(status().isNotFound());

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, never()).delete(any(Player.class));
    }

    @Test
    public void testUpdatePlayer() throws Exception {
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        mockMvc.perform(post("/api/Player/updatePlayer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Player\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.name").value("Updated Player"));

        verify(playerRepository, times(1)).save(any(Player.class));
    }
}
