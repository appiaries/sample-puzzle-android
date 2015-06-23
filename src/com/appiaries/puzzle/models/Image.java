//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.models;

import com.appiaries.baas.sdk.ABCollection;
import com.appiaries.baas.sdk.ABFile;
import com.appiaries.baas.sdk.ABQuery;


@ABCollection("Images")
public class Image extends ABFile {

    public static class Field extends ABFile.Field {

    }

    public Image() {
        super("Images");
    }

    public static ABQuery query() {
        return ABQuery.query(Image.class);
    }

    @Override
    public Object inputDataFilter(String key, Object value) {
        Object filtered = super.inputDataFilter(key, value);
        //TODO: add some input data filter logic
        return filtered;
    }

    @Override
    public Object outputDataFilter(String key, Object value) {
        Object filtered = super.outputDataFilter(key, value);
        //TODO: add some output data filter logic
        return filtered;
    }

}
