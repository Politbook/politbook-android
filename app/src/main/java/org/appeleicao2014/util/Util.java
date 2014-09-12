package org.appeleicao2014.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.appeleicao2014.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Created by thaleslima on 5/20/14.
 */
public class Util {

    private static ProgressDialog progressDialog;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void ShowProgressDialog(Context context, String msg)
    {
        progressDialog = ProgressDialog.show(context, "",  msg, true);
    }

    public static void hideProgressDialog()
    {
        progressDialog.hide();
        progressDialog.dismiss();
    }


    public static void alertDialog(Context context, String msg) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.app_name);
            builder.setMessage(msg);

            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } catch (Exception e) {

        }
    }

    public static void alertDialog(Context context, String title, String Message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(Message);

            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } catch (Exception e) {

        }
    }

    public static String formatPrice(String valor)
    {
        double v;

        try {
            v = Double.parseDouble(valor);
        }
        catch(Exception e)
        {
            v = 0;

        }
        return formatPrice(v);
    }

    public static String formatNumber(String valor)
    {
        double v;

        try {
            v = parseFloat(valor);
        }
        catch(Exception e)
        {
            v = 0;

        }
        return formatNumber(v);
    }

    public static float parseFloat(String valor)
    {
        float v;

        try {
            v = Float.parseFloat(valor);
        }
        catch(Exception e)
        {
            v = 0;
        }
        return v;
    }

    public static String formatPrice(double valor)
    {
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat n = NumberFormat.getCurrencyInstance(ptBr);
        return n.format(valor);
    }

    public static String formatPrice(float valor)
    {
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat n = NumberFormat.getNumberInstance(ptBr);
        return n.format(valor);
    }

    public static String formatDecimal(float valor)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        return String.format(df.format(valor));
    }

    public static String formatNumber(double valor)
    {
        Locale ptBr = new Locale("pt", "BR");
        NumberFormat n = NumberFormat.getNumberInstance(ptBr);
        return n.format(valor);
    }

    public static boolean closeVirtualKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static void ToastNoConnection(Context context)
    {
        Toast.makeText(context, context.getText(R.string.no_connection), Toast.LENGTH_LONG).show();
    }


    public static String value(String value)
    {
        if(value == null)
            return "";

        return value;
    }

    public static String valueString(String value)
    {
        if(value == null || value.equals(""))
            return "-";

        return value;
    }

    public static String valueNumber(String value)
    {
        if(value == null || value.equals(""))
            return "0";

        return value;
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String getUfDefault(Context context)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(Constants.KEY_UF_PREF, Constants.UF_DEFAULT);
    }

    public static void setUfDefault(Context context, String uf)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.KEY_UF_PREF, uf);

        editor.commit();
    }


    public static int getFilterParty(Context context, String idJob)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(Constants.KEY_FILTER_PARTY+idJob, Constants.FILTER_PARTY_DEFAULT);
    }

    public static void setFilterParty(Context context, String idJob, int position)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.KEY_FILTER_PARTY+idJob, position);

        editor.commit();
    }

    public static int returnImageUf(Context context)
    {
        String uf = Util.getUfDefault(context);

        if(uf.equals("AC"))
            return R.drawable.acre;

        if(uf.equals("AL"))
            return R.drawable.alagoas;

        if(uf.equals("AM"))
            return R.drawable.amazonas;

        if(uf.equals("AP"))
            return R.drawable.amapa;

        if(uf.equals("BA"))
            return R.drawable.bahia;

        if(uf.equals("CE"))
            return R.drawable.ceara;

        if(uf.equals("DF"))
            return R.drawable.distritofederal;

        if(uf.equals("ES"))
            return R.drawable.espiritosanto;

        if(uf.equals("GO"))
            return R.drawable.goias;

        if(uf.equals("MA"))
            return R.drawable.maranhao;

        if(uf.equals("MG"))
            return R.drawable.minhasgeral;

        if(uf.equals("MS"))
            return R.drawable.matogrossodosul;

        if(uf.equals("MT"))
            return R.drawable.matogrosso;

        if(uf.equals("PA"))
            return R.drawable.para;

        if(uf.equals("PB"))
            return R.drawable.paraiba;

        if(uf.equals("PE"))
            return R.drawable.pernambuco;

        if(uf.equals("PI"))
            return R.drawable.piaui;

        if(uf.equals("PR"))
            return R.drawable.parana;

        if(uf.equals("RJ"))
            return R.drawable.riodejaneiro;

        if(uf.equals("RN"))
            return R.drawable.riograndedonorte;

        if(uf.equals("RO"))
            return R.drawable.rondonia;

        if(uf.equals("RR"))
            return R.drawable.roraima;

        if(uf.equals("RS"))
            return R.drawable.riograndedosul;

        if(uf.equals("SC"))
            return R.drawable.santacatarina;

        if(uf.equals("SE"))
            return R.drawable.sergipe;

        if(uf.equals("SP"))
            return R.drawable.saopaulo;

        if(uf.equals("TO"))
            return R.drawable.tocantins;

        return R.drawable.photo_transparent;
    }

    public static String returnDescricaoUf(Context context)
    {
        String uf = Util.getUfDefault(context);

        if(uf.equals("AC"))
            return "Acre";

        if(uf.equals("AL"))
            return "Alagoas";

        if(uf.equals("AM"))
            return "Amazonas";

        if(uf.equals("AP"))
            return "Amapa";

        if(uf.equals("BA"))
            return "Bahia";

        if(uf.equals("CE"))
            return "Ceará";

        if(uf.equals("DF"))
            return "Distrito Federal";

        if(uf.equals("ES"))
            return "Espírito Santo";

        if(uf.equals("GO"))
            return "Goiás";

        if(uf.equals("MA"))
            return "Maranhão";

        if(uf.equals("MG"))
            return "Minas Gerais";

        if(uf.equals("MS"))
            return "Mato Grosso do Sul";

        if(uf.equals("MT"))
            return "Mato Grosso";

        if(uf.equals("PA"))
            return "Pará";

        if(uf.equals("PB"))
            return "Paraíba";

        if(uf.equals("PE"))
            return "Pernambuco";

        if(uf.equals("PI"))
            return "Piauí";

        if(uf.equals("PR"))
            return "Paraná";

        if(uf.equals("RJ"))
            return "Rio de Janeiro";

        if(uf.equals("RN"))
            return "Rio Grande do Norte";

        if(uf.equals("RO"))
            return "Rondônia";

        if(uf.equals("RR"))
            return "Roraima";

        if(uf.equals("RS"))
            return "Rio Grande do Sul";

        if(uf.equals("SC"))
            return "Santa Catarina";

        if(uf.equals("SE"))
            return "Sergipe";

        if(uf.equals("SP"))
            return "São Paulo";

        if(uf.equals("TO"))
            return "Tocantins";

        return "";
    }

    public static String returnJobTitle(Context context, String id)
    {
        if(id.equals(Constants.PRESIDENT))
            return context.getString(R.string.title_president);

        if(id.equals(Constants.GOVERNOR))
            return context.getString(R.string.title_governor);

        if(id.equals(Constants.SENATOR))
            return context.getString(R.string.title_senator);

        if(id.equals(Constants.DEPUTYSTATE))
            return context.getString(R.string.title_deputy_state);

        if(id.equals(Constants.DEPUTYFEDERAL))
            return context.getString(R.string.title_deputy_federal);

        return "";
    }
}
