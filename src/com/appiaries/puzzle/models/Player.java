//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.models;

import com.appiaries.baas.sdk.ABCollection;
import com.appiaries.baas.sdk.ABField;
import com.appiaries.baas.sdk.ABUser;

import java.util.Map;

@ABCollection
public class Player extends ABUser {

    public static class Field extends ABUser.Field {
        public static final ABField NICKNAME = new ABField("nickname", String.class);
    }

    public Player() {
        super();
    }

    public Player(Map<String, Object> map) {
        super(map);
    }


    public String getNickname() {
        return get(Field.NICKNAME);
    }
    public void setNickname(String nickname) {
        put(Field.NICKNAME, nickname);
    }

}
