/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myboggle;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author agung
 */
public class Words {
    private static List<String> list, list2;
    private static final ArrayList words_list = new ArrayList();
    private DefaultListModel<String> submited_words = new DefaultListModel<>();
    private DefaultListModel<String> model = new DefaultListModel<>();
    private static int score=0;
    private static int i=0;
    
    private static List<List<String>> lists;
    
    private final static int MIN_CHAR = 3;
    private final static int MAX_CHAR = 15;
    
    private final static int ALPHABET_COUNT=26;
   
    public Words(String current) throws URISyntaxException {
        try {
            score=0;
            
            list = new ArrayList<>();
            
            System.out.printf("Combination for: %s\n",current);
            
            String[] file_name = {"dict_nos.txt","dict_medium.txt"};
            
            for(String fn: file_name) {
                File f_tmp = new File(getClass().getResource(fn).toURI());
                List<String> wordListTemp = Files.readAllLines(f_tmp.toPath(),Charset.forName("utf-8"));
                wordListTemp = wordListTemp.stream().map(s -> s.toLowerCase()).filter( s->s.chars().allMatch(Character::isLetter)).collect( Collectors.toList());
                list.addAll(wordListTemp);
            }
            
            list = list.stream().distinct().collect(Collectors.toList());
            
            list.sort((o1, o2) -> o1.compareTo(o2));
            
            findOptions(current, list );
        } catch (IOException ex) {
            infoBox("Dictionary not found","Error");
            System.exit(0);
        } catch (NullPointerException e) {
            infoBox("Null!","Error");
            System.exit(0);
        }
    }
    
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void findOptions( String string, List<String> list ){
        int[] freq = toFreq(string.toLowerCase());
        
        list.forEach((String l) -> {
            int[] freqIn = toFreq(l);
            if (matches(freq, freqIn)) {
                if(l.length()>=MIN_CHAR && l.length()<=MAX_CHAR) {
                    if(i%20==0) System.out.println();
                    System.out.print(l+", ");
                    words_list.add(l);
                    i++;
                }
            }
        });
        System.out.println();
    }

    /**
     * Returns true if all the frequencies of the letters match.
     * 
     * @param freq
     * @param wordInput
     * @return
     */
    private static boolean matches( int[] freq, int[] wordInput ) {
        for ( int i = 0; i < ALPHABET_COUNT; i++ )
            if ((freq[i] < wordInput[i])) return false;
        
        return true;
    }

    /**
     * Encode a word in to a frequency array. int[0] = #a's, int[1] = #b's etc.
     * 
     * @param string
     * @return
     */
    private static int[] toFreq( String string ) {
        int[] freq = new int[ALPHABET_COUNT];
        for (char c : string.toCharArray()) {
            int _char = c - 'a';
            freq[_char] += (_char>=0 && _char<ALPHABET_COUNT)?1:0;
            
            //if (( c - 'a') >= 0 && (c - 'a') < 26) freq[c - 'a']++;
        }
        return freq;
    }
    
    public DefaultListModel toDLM() {

        words_list.forEach((l) -> {
            model.addElement(l.toString());
        });
        
        return model;
    }
    public void submitToWordList(String l) {
        //submited_words
        words_list.stream().filter((s) -> (((String)s).matches(l))).forEachOrdered((s) -> {
            String st = s.toString();
            if(!submited_words.contains(l)) {
                submited_words.add(0, st);
                model.removeElement(st);
            
                if(st.length()<5) {
                    score+=1;
                } else if(st.length()>=5&&st.length()<7) {
                    score+=2;
                } else if(st.length()>=7&&st.length()<9) {
                    score+=3;
                } else if(st.length()>=9&&st.length()<12) {
                    score+=4;
                } else if(st.length()>=12&&st.length()<15) {
                    score+=5;
                }
            }
        });
        
        
    }
    
    public int getScore() {
        return score;
    }
    
    public DefaultListModel toWordList() {
        return submited_words;
    }
}
