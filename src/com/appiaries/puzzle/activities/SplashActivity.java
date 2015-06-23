//
// Copyright (c) 2014 Appiaries Corporation. All rights reserved.
//
package com.appiaries.puzzle.activities;

import com.appiaries.baas.sdk.AB;
import com.appiaries.puzzle.R;
import com.appiaries.puzzle.models.FirstComeRanking;
import com.appiaries.puzzle.models.FirstComeRankingSequence;
import com.appiaries.puzzle.models.Image;
import com.appiaries.puzzle.models.Introduction;
import com.appiaries.puzzle.models.Player;
import com.appiaries.puzzle.models.Stage;
import com.appiaries.puzzle.models.TimeRanking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // --------------------------------
        //  Initialize SDK
        // --------------------------------
        AB.Config.setDatastoreID(getString(R.string.appiaries__datastore_id));
        AB.Config.setDatastoreID(getString(R.string.appiaries__datastore_id));
        AB.Config.setApplicationID(getString(R.string.appiaries__application_id));
        AB.Config.setApplicationToken(getString(R.string.appiaries__application_token));
        //>> MISC
        AB.Config.setUserClass(Player.class); //NOTE: use custom ABUser class
        //>> activate
        AB.activate(getApplicationContext());

        // --------------------------------
        // Register Classes
        // --------------------------------
        AB.registerClass(Player.class);
        AB.registerClass(Introduction.class);
        AB.registerClass(Image.class);
        AB.registerClass(Stage.class);
        AB.registerClass(FirstComeRanking.class);
        AB.registerClass(FirstComeRankingSequence.class);
        AB.registerClass(TimeRanking.class);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
