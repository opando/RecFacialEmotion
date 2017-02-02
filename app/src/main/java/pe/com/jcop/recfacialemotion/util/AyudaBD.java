package pe.com.jcop.recfacialemotion.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by jchata on 01/02/2017.
 */

public class AyudaBD extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "BaseDatosFinal.db";


    public static abstract class DatosTabla implements BaseColumns{

        public static final String NOMBRE_TABLA = "Imagenes";
        public static final String COLUMNA_ID = "id";
        public static final String COLUMNA_NOMBRE_ARCHIVO = "nomarchivo";
        public static final String COLUMNA_EMOCION = "emocion";
        public static final String COLUMNA_PORCENTAJE = "porcentaje";

        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = " , ";
        public static final String CREAR_TABLA_1 = "CREATE TABLE " + NOMBRE_TABLA + " (" +
                COLUMNA_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT " +  COMMA_SEP +
                COLUMNA_NOMBRE_ARCHIVO    + TEXT_TYPE + COMMA_SEP +
                COLUMNA_EMOCION + TEXT_TYPE + COMMA_SEP +
                COLUMNA_PORCENTAJE  + TEXT_TYPE + " )";


        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
    }



    public AyudaBD (Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DatosTabla.CREAR_TABLA_1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.d("##########", DatosTabla.CREAR_TABLA_1);
        sqLiteDatabase.execSQL(DatosTabla.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }
}
