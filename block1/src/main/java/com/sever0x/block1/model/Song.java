package com.sever0x.block1.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Song {

    private String title;

    private Artist artist;

    private String album;

    private List<String> genres;

    private int duration;

    private int releaseYear;
}
