/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.io;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Detects the character-set name of a Japanese text.
 * @author Katsuhisa Maruyama
 */
public class DetectCharset {
    
    enum Charset { NO_MATCH, ASCII, UTF8, JIS, SJIS, EUC, EUC_OR_SJIS }
    
    /**
     * Guesses the character-set name of a Japanese text.
     * @param filename the name of a file containing the text
     * @return the character-set name (US-ASCII, UTF-8, ISO-2022-JP, EUC-JP, or SJIS)
     * @throws IOException if an I/O error occurred
     */
    public static String getCharsetName(String filename) throws IOException {
        return getCharsetName(filename, null);
    }
    
    /**
     * Guesses the character-set name of a Japanese text.
     * @param contents the contents of the text
     * @return the character-set name (US-ASCII, UTF-8, ISO-2022-JP, EUC-JP, or SJIS)
     * @throws IOException if an I/O error occurred
     */
    public static String getCharsetName(byte[] contents) throws IOException {
        return getCharsetName(null, contents);
    }
    
    /**
     * Returns a character-set name of a Japanese text.
     * @param filename the name of a file containing the text, or <code>null</code> if the file is not specified
     * @param contents the contents of the text, or <code>null if the contents is not specified
     * @return the character-set name (US-ASCII, UTF-8, ISO-2022-JP, EUC-JP, or SJIS)
     * @throws IOException if an I/O error occurred
     */
    private static String getCharsetName(String filename, byte[] contents) throws IOException {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(createInputStream(filename, contents));
            Charset charset = checkJisOrUnicode(bis);
            bis.close();
            
            if (charset == Charset.ASCII) {
                return "US-ASCII";
            } else if (charset == Charset.UTF8) {
                return "UTF-8";
            } else if (charset == Charset.JIS) {
                return "ISO-2022-JP";
            }
            
            bis = new BufferedInputStream(createInputStream(filename, contents));
            charset = checkEucOrShiftJis(bis);
            if (charset == Charset.EUC) {
                return "EUC-JP";
            } else if (charset == Charset.SJIS) {
                return "SJIS";
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    System.err.println("File close error");
                }
            } 
        }
        return getDefaultCharsetName();
    }
    
    /**
     * Creates an input stream from a file and/or its contents.
     * @param filename the name of the file, or <code>null</code> if the file is not specified
     * @param contents the contents of a file, or <code>null if the contents is not specified
     * @return the input stream. The file name is used if both of them were given
     * @throws IOException if an I/O error occurred
     */
    private static InputStream createInputStream(String filename, byte[] contents) throws IOException {
        if (filename != null) {
            return new FileInputStream(filename);
        }
        return new ByteArrayInputStream(contents);
    }
    
    /**
     * Returns a default character-set name.
     * @return the default character-set name
     */
    private static String getDefaultCharsetName() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return "SJIS";
        } else if (System.getProperty("os.name").startsWith("Mac")) {
            return "UTF-8";
        }
        return "EUC-JP";
    }
    
    /**
     * Checks if the contents of a given stream is JIS or Unicode.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkJisOrUnicode(InputStream is) throws IOException {
        int ch;
        while ((ch = is.read()) != -1) {
            
        	if (ch == 0x1b) {  /* ESC (1B) */
                return checkJis(is);
                
            } else if (ch < 0x80) {
                /* ASCII or control sequence */
                
            } else if (ch < 0xc0) {
                return Charset.NO_MATCH;
                
            } else {
                return checkUnicode(ch, is);
            }
        }
        return Charset.ASCII;
    }
    
    /**
     * Checks if the contents of a given stream is JIS.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkJis(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if (ch == 0x24) {
            if ((ch = is.read()) == -1) {
                return Charset.NO_MATCH;
            }
            
            if (ch == 0x42 || ch == 0x40) {
                if ((ch = is.read()) == -1) {
                    return Charset.NO_MATCH;
                }
                
                if (ch >= 0x21 || ch <= 0x7e) {
                    if ((ch = is.read()) == -1) {
                        return Charset.NO_MATCH;
                    }
                    
                    if (ch >= 0x21 || ch <= 0x7e) {
                        return Charset.JIS;
                    }
                }
            }
            
        } else if (ch == 0x28) {
            if ((ch = is.read()) == -1) {
                return Charset.JIS;
            }
            
            if (ch == 0x4a || ch == 0x42) {
                if ((ch = is.read()) == -1) {
                    return Charset.NO_MATCH;
                }
                
                if (ch >= 0x20 || ch <= 0x7e) {
                    return Charset.JIS;
                }
                
            } else if (ch == 0x49) {
                if ((ch = is.read()) == -1) {
                    return Charset.NO_MATCH;
                }
                
                if (ch >= 0x21 && ch <= 0x5f) {
                    return Charset.JIS;
                }
            }
        }
        
        return Charset.NO_MATCH;
    }
    
    /**
     * Checks if the contents of a given stream is Unicode.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkUnicode(int ch, InputStream is) throws IOException {
        int len;
        if (ch < 0xe0) {
            len = 1;
        } else if (ch < 0xf0) {
            len = 2;
        } else if (ch < 0xf8) {
            len = 3;
        } else if (ch < 0xfc) {
            len = 4;
        } else if (ch < 0xfe) {
            len = 5;
        } else {
            return Charset.NO_MATCH;
        }
        
        for (int i = 0; i < len; i++) {
            if ((ch = is.read()) == -1) {
                return Charset.NO_MATCH;
            }
            
            if (ch < 0x80 || ch > 0xbf) {
                return Charset.NO_MATCH;
            }
        }
        return Charset.UTF8;
    }
    
    /**
     * Checks if the contents of a given stream is EUC or ShiftJIS
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkEucOrShiftJis(InputStream is) throws IOException {
        int ch;
        Charset result = Charset.EUC_OR_SJIS;
        
        while ((ch = is.read()) != -1) {
            if (isShiftJis(ch)) {
                result = checkShiftJis(is);
                
            } else if (isEuc(ch)) {
                result = checkEuc(is);
                
            } else if (ch == 0x8e) {
                result = checkShiftJisOrEUCKana(is);
                
            } else if (ch >= 0xa1 && ch <= 0xdf) {
                result = checkShiftJisOrEUCKanji(is);
                
            } else if (ch >= 0xe0 && ch <= 0xef) {
                result = checkShiftJisOrEUCKanji2(is);
            }
            
            if (result != Charset.EUC_OR_SJIS) {
                break;
            }
        }
        return result;
    }
    
    /**
     * Tests if a character is represented by ShiftJIS.
     * @param ch the character to be checked
     * @return <code>true</code> if a character is represented by ShiftJIS, otherwise <code>false</code>
     */
    private static boolean isShiftJis(int ch) {
        if ((ch >= 0x81 && ch <= 0x8d) || (ch >= 0x8f && ch <= 0x9f)) {
            return true;
        }
        return false;
    }
    
    /**
     * Tests if a character is represented by EUC.
     * @param ch the character to be checked.
     * @return <code>true</code> if a character is represented by EUC, otherwise <code>false</code>
     */
    private static boolean isEuc(int ch) {
        if (ch >= 0xf0 && ch <= 0xfe) {
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the contents of a given stream is ShiftJIS.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkShiftJis(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if ((ch >= 0x40 && ch <= 0x7e) || (ch >= 0x80 && ch <= 0xfc)) {
            return Charset.SJIS;
        }
        
        return Charset.NO_MATCH;
    }
    
    /**
     * Checks if the contents of a given stream is EUC.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkEuc(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if (ch >= 0xa1 && ch <= 0xfe) {
            return Charset.EUC;
        }
        
        return Charset.NO_MATCH;
    }
    
    /**
     * Checks if the contents of a given stream is ShiftJIS or Japanese Kana.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkShiftJisOrEUCKana(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if ((ch >= 0x40 && ch <= 0x7e) || (ch >= 0x80 && ch <= 0xa0) || (ch >= 0xe0 && ch <= 0xfc)) {
            return Charset.SJIS;
            
        } else if (ch >= 0xa1 && ch <= 0xdf) {
            return Charset.EUC_OR_SJIS;
        }
        
        return Charset.NO_MATCH;
    }
    
    /**
     * Checks if the contents of a given stream is ShiftJIS or EUC Kanji.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkShiftJisOrEUCKanji(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if (ch >= 0x81 && ch <= 0x9f) {
            return Charset.SJIS;
            
        } else if (ch >= 0xa1 && ch <= 0xdf) {
            return Charset.EUC_OR_SJIS;
            
        } else if (ch >= 0xe0 && ch <= 0xef) {
            if ((ch = is.read()) == -1) {
                return Charset.NO_MATCH;
            }
            
            while (ch >= 0x40) {
                if (isShiftJis(ch)) {
                    return Charset.SJIS;
                    
                } else if (isEuc(ch)) {
                    return Charset.EUC;
                }
                
                if ((ch = is.read()) == -1) {
                    return Charset.NO_MATCH;
                }
            }
            
        } else if (ch >= 0xf0 && ch <= 0xfe) {
            return Charset.EUC;
        }
        
        return Charset.NO_MATCH;
    }
    
    /**
     * Checks if the contents of a given stream is ShiftJIS or EUC Kanji.
     * @param is the input stream to be checked
     * @return the detected character-set name
     * @throws IOException if an I/O error occurred
     */
    private static Charset checkShiftJisOrEUCKanji2(InputStream is) throws IOException {
        int ch;
        if ((ch = is.read()) == -1) {
            return Charset.NO_MATCH;
        }
        
        if ((ch >= 0x40 && ch <= 0x7e) || (ch >= 0x80 && ch <= 0xa0)) {
            return Charset.SJIS;
            
        } else if (ch >= 0xe0 && ch <= 0xfc) {
            return Charset.EUC_OR_SJIS;
            
        } else if (ch >= 0xfd && ch >= 0xfe){
            return Charset.EUC;
        }
        
        return Charset.NO_MATCH;
    }
}
