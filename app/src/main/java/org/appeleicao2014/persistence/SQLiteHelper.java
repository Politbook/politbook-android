package org.appeleicao2014.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.util.Constants;

import java.io.Console;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static String DATABASE_NAME = "brazilBD";
	private static int DATABASE_VERSION = 14;

	public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String[] SCRIPT_DATABASE_TABLES = new String[] {
            "create table if not exists candidate (id text,nickname text,name text,number text,title text,CPF text,enrollment text,jobTitle text,state text,party text,age text,instruction text,occupation text,miniBio text,positions text,prediction text,countertops text,processes text,currentHome text,reelection text,photo text,idJob text, idParty text, flSearch text, average real );",
            "create table if not exists equity (equity text, amount text, year text , idCandidate text);",
            "create table if not exists statistics ( faultsPlenary text, averagePlenary text, faultsCommissions text, averageCommissions text, evolution text, referenceYear text, amendments text, averageAmendments text, idCandidate text);",
            "create table if not exists favorite (id text, nickname text,name text,number text,party text,state text,photo text, idJob text);",
            "create table if not exists candidature (electionYear text, jobTitle text,party text,city text,state text,result text,votes text, financialResources text, idCandidate text);",
            "create table if not exists party (id text, party text);",
    };

    public static final String[] SCRIPT_DATABASE_DROP = new String[]{
            "DROP table IF EXISTS candidate",
            "DROP table IF EXISTS equity",
            "DROP table IF EXISTS statistics",
            "DROP table IF EXISTS candidature",
            "DROP table IF EXISTS party",
    };


    public static final String[] INSERTS_PARTIES = new String[] {
            "insert into party (id, party) values ('35', 'DEM');",
            "insert into party (id, party) values ('17', 'PC do B');",
            "insert into party (id, party) values ('29','PCB');",
            "insert into party (id, party) values ('30','PCO');",
            "insert into party (id, party) values ('10','PDT');",
            "insert into party (id, party) values ('40','PEN');",
            "insert into party (id, party) values ('24','PHS');",
            "insert into party (id, party) values ('2','PMDB');",
            "insert into party (id, party) values ('15','PMN');",
            "insert into party (id, party) values ('33','PP');",
            "insert into party (id, party) values ('37','PPL');",
            "insert into party (id, party) values ('4','PPS');",
            "insert into party (id, party) values ('36','PR');",
            "insert into party (id, party) values ('32','RB');",
            "insert into party (id, party) values ('43','PROS');",
            "insert into party (id, party) values ('26','PRP');",
            "insert into party (id, party) values ('5','PRTB');",
            "insert into party (id, party) values ('7','PSB');",
            "insert into party (id, party) values ('12','PSC');",
            "insert into party (id, party) values ('39','PSD');",
            "insert into party (id, party) values ('8','PSDB');",
            "insert into party (id, party) values ('18','PSDC');",
            "insert into party (id, party) values ('3','PSL');",
            "insert into party (id, party) values ('31','PSOL');",
            "insert into party (id, party) values ('20','PSTU');",
            "insert into party (id, party) values ('1','PT');",
            "insert into party (id, party) values ('19','PT do B');",
            "insert into party (id, party) values ('6','PTB');",
            "insert into party (id, party) values ('25','PTC');",
            "insert into party (id, party) values ('21','PTN');",
            "insert into party (id, party) values ('16','PV');",
            "insert into party (id, party) values ('44','SD');"
    };

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(Constants.DEBUG, "Creating bd");

        for (String sql : SCRIPT_DATABASE_TABLES) {
            db.execSQL(sql);
        }

        for (String sql : INSERTS_PARTIES) {
            db.execSQL(sql);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(Constants.DEBUG, "Update bd");

        for (String sql : SCRIPT_DATABASE_DROP) {
            db.execSQL(sql);
        }

		onCreate(db);
	}
}
