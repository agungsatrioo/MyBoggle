/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myboggle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
 import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *
 * @author agung
 */
public class MyBoggle {
    
    static JToggleButton[] jb;
    static JFMain jf          ;
    static LoadingFrame lf;
    static Random r           ;
    static String consonants  ;
    static String vowels      ;
    static String current     ;
    static String words       ;
    static String[] letters, letters_pos;
    static Words wds        ;
    static int idx            = 0;
    static int score          = 0;
    static int k              = 0;
    static final int numberofButtons = 25;

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        LoadingFrame ll = new LoadingFrame();
        
        ll.setLocationRelativeTo(null);
        ll.setVisible(true);
        initComponents();
                 
                 
        jf.setLocationRelativeTo(null);
        ll.setVisible(false);
        jf.setVisible(true);
        ll.dispose();
    }
    
    private static void makeAction() {   
        jf.btnSubmit.addActionListener((ActionEvent e) -> {
            wds.submitToWordList(jf.txtWords.getText().toLowerCase());
            resetWords();
            score = wds.getScore();
            setScore();
            
            jf.btnSubmit.setEnabled(false);
            jf.btnReset.setEnabled(false);
        });
        
        jf.btnReset.addActionListener((ActionEvent e) -> {
           resetWords(); 
        });
        
        jf.btnNewGame.addActionListener((ActionEvent e) -> {
            try {
                jf.dispose();
                initComponents();
            } catch (IOException ex) {
                Logger.getLogger(MyBoggle.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyBoggle.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(MyBoggle.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
 
        jf.jToggleButton2.addItemListener((ItemEvent e) -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                jf.jPanel1.setVisible(false);
                jf.paused.setVisible(true);
            } else if(e.getStateChange()==ItemEvent.DESELECTED){
               jf.jPanel1.setVisible(true);
               jf.paused.setVisible(false);
            }
        });
        
        ItemListener words_itemListener = (ItemEvent e) -> {
            
            String index = ((JToggleButton)e.getItem()).getName();
            
            String text = ((JToggleButton)e.getItem()).getText();
            
            if(e.getStateChange()==ItemEvent.SELECTED){
                letters_pos[idx] = index;
                letters[idx]     = text;
                idx++;
                checkEnable();
                
            } else if(e.getStateChange()==ItemEvent.DESELECTED){
                int o=findIndex(letters_pos,index);
                
                letters_pos[o]  = "";
                letters[o]    = "";
                arrange(o);
                
                idx--;   
                checkEnable();
            
            }

            //System.out.println(Arrays.toString(letters));
            generateWords();
            setWords();
        };
        
        for (JToggleButton jb1 : jb) {
            jb1.addItemListener(words_itemListener);
        }
    }
    
    public static void checkEnable() {
         if(idx>2) {
                jf.btnSubmit.setEnabled(true);
                jf.txtWords.setForeground(Color.black);
         } else {
                jf.btnSubmit.setEnabled(false);
                jf.txtWords.setForeground(Color.red);
         }
         
         if(idx>0) 
                jf.btnReset.setEnabled(true);
         else 
                 jf.btnReset.setEnabled(false);
    }
    
    public static int findIndex(String[] my_array, String t) {
        if(my_array==null) return -1;
        
        int len = my_array.length;
        for(int i=0;i<len;i++) {
            if(t.equals(my_array[i])) return i;
        }
        
        return -1;
    }
    
    public static void arrange(int pos) {
        String tmp="";
        
        for(int i=pos+1;i<letters_pos.length;i++) {
            tmp = letters_pos[i];
            letters_pos[i-1] = letters_pos[i];
            letters_pos[i] = tmp;
            
            tmp = letters[i];
            letters[i-1] = letters[i];
            letters[i] = tmp;
        }
    }
    
    public static void resetWords() {
        for (JToggleButton jb1 : jb) {
            jb1.setSelected(false);
            jf.txtWords.setText("");
        }
        
    }
    
    
    private static void setScore() {
        jf.jLabel3.setText(String.valueOf(score));
    }
    
    private static void setWords() {
        jf.txtWords.setText(current);
    }
    
    
    
    private static void antiNull() {
        for(int i=0;i<letters.length;i++) {
            if(letters[i]==null) letters[i]="";
            if(letters_pos[i]==null) letters_pos[i]="";
        }
    }
    
    private static void generateWords() {
        current="";
        for (String letters_po : letters) {
            current+=letters_po;
        }
    }
    
    private static void generateLetters() throws IOException, URISyntaxException{
        int c_len = consonants.length();
        int v_len = vowels.length();
        
        for(int i=0;i<numberofButtons;i++) {
            int c_index = r.nextInt(c_len);
            int v_index = r.nextInt(v_len);
            int idx     = r.nextInt(2);
            
            
            char c = consonants.charAt(c_index);
            char v = vowels.charAt(v_index);
            
            String a = String.valueOf(idx==1?c:v);
            
            
            jb[i].setText(a);
            jb[i].setName(a+i);
            
            words+=a;
        }
        
        System.out.println("Initializing words engine...");
        wds = new Words(words);
    }
    
    private static void initComponents() throws IOException, InterruptedException, URISyntaxException {

        jb              = null;
        jf              = null;
        lf              =null;
        r               = null;
        consonants      = null;
        vowels          = null;
        current         = null;
        words           = null;
        letters         = null;
        letters_pos     = null;
        
        
        jb              = new JToggleButton[numberofButtons];
        jf              = new JFMain();
        r               = new Random();
        lf              = new LoadingFrame();
        consonants      = "BCDFGHJKLMNPQRSTVWXYZ";
        vowels          = "AIUEO";
        current         = "";
        words           = "";
        letters         = new String[numberofButtons];
        letters_pos     = new String[numberofButtons];
        idx             = 0;
        score           = 0;
        k               = 0;
        
        full_init();
    }
    
    public static void init_button() {
         jb[0] = jf.b1;
         jb[1] = jf.b2;
         jb[2] = jf.b3;
         jb[3] = jf.b4;
         jb[4] = jf.b5;
         jb[5] = jf.b6;
         jb[6] = jf.b7;
         jb[7] = jf.b8;
         jb[8] = jf.b9;
         jb[9] = jf.b10;
         jb[10] = jf.b11;
         jb[11] = jf.b12;
         jb[12] = jf.b13;
         jb[13] = jf.b14;
         jb[14] = jf.b15;
         jb[15] = jf.b16;
         jb[16] = jf.b17;
         jb[17] = jf.b18;
         jb[18] = jf.b19;
         jb[19] = jf.b20;
         jb[20] = jf.b21;
         jb[21] = jf.b22;
         jb[22] = jf.b23;
         jb[23] = jf.b24;
         jb[24] = jf.b25;
    }
    
    public static void full_init() throws InterruptedException, IOException, URISyntaxException {


        System.out.println("Initializing button...");
        jf.paused.setVisible(false);
        jf.loadingtable.setVisible(true);
        jf.jPanel1.setVisible(false);
        
        init_button();
         
         antiNull();
         
         System.out.println("Initializing words and score...");
         
         setWords();
         setScore();
         
         generateLetters();
         
         System.out.println("Initializing actions...");
         makeAction();
         

         
         jf.jList2.setModel(wds.toDLM());
         jf.jList1.setModel(wds.toWordList());
         jf.btnSubmit.setEnabled(false);
         jf.btnReset.setEnabled(false);
         jf.jScrollPane2.setVisible(false);
         
         
         Color clr = new Color(0,0,0,0);
         
         jf.jPanel1.setBackground(clr);
         jf.jScrollPane1.setBackground(clr);
         Thread.sleep(1000);
         
         jf.loadingtable.setVisible(false);
         jf.jPanel1.setVisible(true);

         
         jf.setLocationRelativeTo(null);
        jf.setVisible(true);
         System.out.println("Done.");
    }
}