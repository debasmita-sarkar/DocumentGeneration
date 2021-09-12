package com.mavenir.thymeleaf.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {
    String name;
    String storage;
    String color;
}
