package com.example.demoapp.ModelAttributeClasses;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class MyInteger implements Serializable {
    private int value;
}
