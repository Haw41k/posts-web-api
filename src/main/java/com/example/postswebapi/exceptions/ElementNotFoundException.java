package com.example.postswebapi.exceptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(long id) {
        super("Could not find element with id: " + id);
    }

    public ElementNotFoundException(long id, String elementName) {
        super("Could not find " + elementName + " with id: " + id);
    }
}

