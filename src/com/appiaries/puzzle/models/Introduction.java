//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.models;

import com.appiaries.baas.sdk.ABCollection;
import com.appiaries.baas.sdk.ABDBObject;
import com.appiaries.baas.sdk.ABField;
import com.appiaries.baas.sdk.ABQuery;

@ABCollection("Introductions")
public class Introduction extends ABDBObject {

    public static class Field extends ABDBObject.Field {
        public static final ABField CONTENT = new ABField("content", String.class);
        public static final ABField ORDER = new ABField("order", int.class);
    }

    public Introduction() {
        super("Introductions");
    }

    public static ABQuery query() {
        return ABQuery.query(Introduction.class);
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

    public String getContent() {
        return get(Field.CONTENT);
    }
    public void setContent(String content) {
        put(Field.CONTENT, content);
    }

    public int getOrder() {
        return get(Field.ORDER);
    }
    public void setOrder(int order) {
        put(Field.ORDER, order);
    }

}
