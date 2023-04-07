package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Location {
    public Float lat;
    public Float lon;
}
