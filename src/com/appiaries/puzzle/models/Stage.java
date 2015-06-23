//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.models;

import com.appiaries.baas.sdk.ABCollection;
import com.appiaries.baas.sdk.ABDBObject;
import com.appiaries.baas.sdk.ABField;
import com.appiaries.baas.sdk.ABQuery;

@ABCollection("Stages")
public class Stage extends ABDBObject {

    public static class Field extends ABDBObject.Field {
        public static final ABField NAME       = new ABField("name", String.class);
        public static final ABField IMAGE_ID   = new ABField("image_id", String.class);
        public static final ABField NUMBER_OF_HORIZONTAL_PIECES = new ABField("number_of_horizontal_pieces", int.class);
        public static final ABField NUMBER_OF_VERTICAL_PIECES   = new ABField("number_of_vertical_pieces", int.class);
        public static final ABField TIME_LIMIT = new ABField("time_limit", int.class);
        public static final ABField ORDER      = new ABField("order", int.class);
    }

    public Stage() {
        super("Stages");
    }

    public static ABQuery query() {
        return ABQuery.query(Stage.class);
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

    public String getName() {
        return get(Field.NAME);
    }
    public void setName(String name) {
        put(Field.NAME, name);
    }

    public String getImageID() {
        return get(Field.IMAGE_ID);
    }
    public void setImageID(String imageID) {
        put(Field.IMAGE_ID, imageID);
    }

    public int getNumberOfHorizontalPieces() {
        return get(Field.NUMBER_OF_HORIZONTAL_PIECES);
    }
    public void setNumberOfHorizontalPieces(int numberOfHorizontalPieces) {
        put(Field.NUMBER_OF_HORIZONTAL_PIECES, numberOfHorizontalPieces);
    }

    public int getNumberOfVerticalPieces() {
        return get(Field.NUMBER_OF_VERTICAL_PIECES);
    }
    public void setNumberOfVerticalPieces(int numberOfVerticalPieces) {
        put(Field.NUMBER_OF_VERTICAL_PIECES, numberOfVerticalPieces);
    }

    public int getTimeLimit() {
        return get(Field.TIME_LIMIT);
    }
    public void setTimeLimit(int timeLimit) {
        put(Field.TIME_LIMIT, timeLimit);
    }

    public int getOrder() {
        return get(Field.ORDER);
    }
    public void setOrder(int order) {
        put(Field.ORDER, order);
    }

}
