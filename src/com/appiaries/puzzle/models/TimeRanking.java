//
// Copyright (c) 2015 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.models;

import com.appiaries.baas.sdk.ABCollection;
import com.appiaries.baas.sdk.ABDBObject;
import com.appiaries.baas.sdk.ABField;
import com.appiaries.baas.sdk.ABQuery;

@ABCollection("TimeRanking")
public class TimeRanking extends ABDBObject {

    public static class Field extends ABDBObject.Field {
        public static final ABField STAGE_ID  = new ABField("stage_id", String.class);
        public static final ABField PLAYER_ID = new ABField("user_id", String.class);
        public static final ABField NICKNAME  = new ABField("nickname", String.class);
        public static final ABField SCORE     = new ABField("score", int.class);
    }

    public TimeRanking() {
        super("TimeRanking");
    }

    public static ABQuery query() {
        return ABQuery.query(TimeRanking.class);
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

    public String getStageID() {
        return get(Field.STAGE_ID);
    }
    public void setStageID(String stageID) {
        put(Field.STAGE_ID, stageID);
    }

    public String getPlayerID() {
        return get(Field.PLAYER_ID);
    }
    public void setPlayerID(String playerID) {
        put(Field.PLAYER_ID, playerID);
    }

    public String getNickname() {
        return get(Field.NICKNAME);
    }
    public void setNickname(String nickname) {
        put(Field.NICKNAME, nickname);
    }

    public int getScore() {
        return get(Field.SCORE);
    }
    public void setScore(int score) {
        put(Field.SCORE, score);
    }

}
