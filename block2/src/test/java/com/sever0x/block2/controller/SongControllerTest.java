package com.sever0x.block2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.ShortSongResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.service.SongService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @Test
    void testCreateSong() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "Test Album", "Rock,Pop", 180, 2022);
        SongResponse response = new SongResponse(1L, "Test Song", new ArtistResponse(1L, "Test Artist", "USA"), "Test Album", "Rock,Pop", 180, 2022);

        when(songService.createSong(any(SongRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.artist.id").value(1L))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"))
                .andExpect(jsonPath("$.artist.country").value("USA"))
                .andExpect(jsonPath("$.album").value("Test Album"))
                .andExpect(jsonPath("$.genres").value("Rock,Pop"))
                .andExpect(jsonPath("$.duration").value(180))
                .andExpect(jsonPath("$.releaseYear").value(2022));

        verify(songService).createSong(request);
    }

    @Test
    void testGetSongDetails() throws Exception {
        SongResponse response = new SongResponse(1L, "Test Song", new ArtistResponse(1L, "Test Artist", "USA"), "Test Album", "Rock,Pop", 180, 2022);

        when(songService.getSongById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/song/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.artist.id").value(1L))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"))
                .andExpect(jsonPath("$.artist.country").value("USA"))
                .andExpect(jsonPath("$.album").value("Test Album"))
                .andExpect(jsonPath("$.genres").value("Rock,Pop"))
                .andExpect(jsonPath("$.duration").value(180))
                .andExpect(jsonPath("$.releaseYear").value(2022));

        verify(songService).getSongById(1L);
    }

    @Test
    void testUpdateSong() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "Rock", 200, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(songService).updateSongById(1L, request);
    }

    @Test
    void testDeleteSong() throws Exception {
        when(songService.deleteSongById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/song/1"))
                .andExpect(status().isOk());

        verify(songService).deleteSongById(1L);
    }

    @Test
    void testGetSongs() throws Exception {
        GetSongsRequest request = new GetSongsRequest(1L, "Test Album", 0, 10);
        ShortSongResponse response1 = new ShortSongResponse("Test Song 1", "Test Artist", "Test Album", 180);
        ShortSongResponse response2 = new ShortSongResponse("Test Song 2", "Test Artist", "Test Album", 200);
        GetSongsResponse getSongsResponse = new GetSongsResponse(List.of(response1, response2), 1);

        when(songService.getSongs(any(GetSongsRequest.class))).thenReturn(getSongsResponse);

        mockMvc.perform(post("/api/song/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(2))
                .andExpect(jsonPath("$.list[0].title").value("Test Song 1"))
                .andExpect(jsonPath("$.list[0].artistName").value("Test Artist"))
                .andExpect(jsonPath("$.list[0].album").value("Test Album"))
                .andExpect(jsonPath("$.list[0].duration").value(180))
                .andExpect(jsonPath("$.list[1].title").value("Test Song 2"))
                .andExpect(jsonPath("$.list[1].artistName").value("Test Artist"))
                .andExpect(jsonPath("$.list[1].album").value("Test Album"))
                .andExpect(jsonPath("$.list[1].duration").value(200))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(songService).getSongs(request);
    }
}