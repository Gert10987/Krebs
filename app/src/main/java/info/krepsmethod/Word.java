package info.krepsmethod;

import android.app.Activity;

/**
 * Created by Paweł on 2016-02-16.
 */
public class Word extends Activity implements Comparable<Word>{


    /*
    TWorzenie zmiennych
     */


    long id;
    String polishWordText;
    String englishWordText;
    String englishWordAudio;
    String polishWordAudio;



    /*
    KOnstruktor
     */

    public Word(String polishWordText, String polishWordAudio) {

        this.polishWordText = polishWordText;
        this.polishWordAudio = polishWordAudio;


    }

    /*

    Konstruktor
     */

    public Word() {

    }

    /*
    getID
     */

    public long getID() {

        return id;
    }

    /*
    setID
     */

    public long setID(long id) {

        return id;

    }

    /*
    setPolishWordText
     */

    public void setPolish(String polishWordText, String polishWordAudio) {

    }

    /*
    getPolishWordText
     */

    public String getPolishWordText() {

        return polishWordText;


    }

    public String getPolishWordAudio() {

        return polishWordAudio;


    }

    /*
    setEnglishWordtext
     */

    public void setEnglish(String englishWordText, String englishWordAudio) {



    }

    /*
    getEnglishWordText
     */

    public String getEnglishWordText() {

        return englishWordText;
    }



    /*
    getEnglishWordText
     */

    public String getEnglishWordAudio() {

        return englishWordAudio;
    }

    @Override
    public int compareTo(Word another) {

        int porownaneE = englishWordText.compareTo(another.englishWordText);

        if(porownaneE == 0) {
            return englishWordText.compareTo(another.englishWordText);
        }
        else {
            return porownaneE;
        }
    }
}
