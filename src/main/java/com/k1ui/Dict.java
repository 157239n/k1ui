package com.k1ui;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary for translating KeyNativeEvent to KeyEvent
 */
public class Dict {
    public static Map<Integer, Integer> nativeToJava;
    public static Map<Integer, Integer> javaToNative;
    static {
        nativeToJava = new HashMap<>();

        nativeToJava.put(NativeKeyEvent.VC_ALT, KeyEvent.VK_ALT);
        nativeToJava.put(NativeKeyEvent.VC_CONTROL, KeyEvent.VK_CONTROL);
        nativeToJava.put(NativeKeyEvent.VC_SHIFT, KeyEvent.VK_SHIFT);
        nativeToJava.put(NativeKeyEvent.VC_NUM_LOCK, KeyEvent.VK_NUM_LOCK);
        nativeToJava.put(NativeKeyEvent.VC_CAPS_LOCK, KeyEvent.VK_CAPS_LOCK);
        nativeToJava.put(NativeKeyEvent.VC_SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK);
        nativeToJava.put(NativeKeyEvent.VC_PRINTSCREEN, KeyEvent.VK_PRINTSCREEN);
        nativeToJava.put(NativeKeyEvent.VC_PAUSE, KeyEvent.VK_PAUSE);

        nativeToJava.put(NativeKeyEvent.VC_UP, KeyEvent.VK_UP);
        nativeToJava.put(NativeKeyEvent.VC_DOWN, KeyEvent.VK_DOWN);
        nativeToJava.put(NativeKeyEvent.VC_LEFT, KeyEvent.VK_LEFT);
        nativeToJava.put(NativeKeyEvent.VC_RIGHT, KeyEvent.VK_RIGHT);

        nativeToJava.put(NativeKeyEvent.VC_PAGE_UP, KeyEvent.VK_PAGE_UP);
        nativeToJava.put(NativeKeyEvent.VC_PAGE_DOWN, KeyEvent.VK_PAGE_DOWN);
        nativeToJava.put(NativeKeyEvent.VC_HOME, KeyEvent.VK_HOME);
        nativeToJava.put(NativeKeyEvent.VC_END, KeyEvent.VK_END);
        nativeToJava.put(NativeKeyEvent.VC_INSERT, KeyEvent.VK_INSERT);
        nativeToJava.put(NativeKeyEvent.VC_DELETE, KeyEvent.VK_DELETE);

        nativeToJava.put(NativeKeyEvent.VC_CLEAR, KeyEvent.VK_CLEAR);
        nativeToJava.put(NativeKeyEvent.VC_BACKQUOTE, KeyEvent.VK_BACK_QUOTE);
        nativeToJava.put(NativeKeyEvent.VC_MINUS, KeyEvent.VK_MINUS);
        nativeToJava.put(NativeKeyEvent.VC_UNDERSCORE, KeyEvent.VK_UNDERSCORE);
        nativeToJava.put(NativeKeyEvent.VC_EQUALS, KeyEvent.VK_EQUALS);
        nativeToJava.put(NativeKeyEvent.VC_OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET);
        nativeToJava.put(NativeKeyEvent.VC_CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET);
        nativeToJava.put(NativeKeyEvent.VC_BACK_SLASH, KeyEvent.VK_BACK_SLASH);
        nativeToJava.put(NativeKeyEvent.VC_SEMICOLON, KeyEvent.VK_SEMICOLON);
        nativeToJava.put(NativeKeyEvent.VC_QUOTE, KeyEvent.VK_QUOTE);
        nativeToJava.put(NativeKeyEvent.VC_COMMA, KeyEvent.VK_COMMA);
        nativeToJava.put(NativeKeyEvent.VC_PERIOD, KeyEvent.VK_PERIOD);
        nativeToJava.put(NativeKeyEvent.VC_SLASH, KeyEvent.VK_SLASH);
        nativeToJava.put(NativeKeyEvent.VC_ENTER, KeyEvent.VK_ENTER);
        nativeToJava.put(NativeKeyEvent.VC_ESCAPE, KeyEvent.VK_ESCAPE);
        nativeToJava.put(NativeKeyEvent.VC_TAB, KeyEvent.VK_TAB);
        nativeToJava.put(NativeKeyEvent.VC_SPACE, KeyEvent.VK_SPACE);
        nativeToJava.put(NativeKeyEvent.VC_BACKSPACE, KeyEvent.VK_BACK_SPACE);
        nativeToJava.put(NativeKeyEvent.VC_META, KeyEvent.VK_META);
        nativeToJava.put(NativeKeyEvent.VC_HIRAGANA, KeyEvent.VK_HIRAGANA);
        nativeToJava.put(NativeKeyEvent.VC_KATAKANA, KeyEvent.VK_KATAKANA);
        nativeToJava.put(NativeKeyEvent.VC_KANJI, KeyEvent.VK_KANJI);
        nativeToJava.put(NativeKeyEvent.VC_CONTEXT_MENU, KeyEvent.VK_CONTEXT_MENU);

        nativeToJava.put(NativeKeyEvent.VC_F1, KeyEvent.VK_F1);
        nativeToJava.put(NativeKeyEvent.VC_F2, KeyEvent.VK_F2);
        nativeToJava.put(NativeKeyEvent.VC_F3, KeyEvent.VK_F3);
        nativeToJava.put(NativeKeyEvent.VC_F4, KeyEvent.VK_F4);
        nativeToJava.put(NativeKeyEvent.VC_F5, KeyEvent.VK_F5);
        nativeToJava.put(NativeKeyEvent.VC_F6, KeyEvent.VK_F6);
        nativeToJava.put(NativeKeyEvent.VC_F7, KeyEvent.VK_F7);
        nativeToJava.put(NativeKeyEvent.VC_F8, KeyEvent.VK_F8);
        nativeToJava.put(NativeKeyEvent.VC_F9, KeyEvent.VK_F9);
        nativeToJava.put(NativeKeyEvent.VC_F10, KeyEvent.VK_F10);
        nativeToJava.put(NativeKeyEvent.VC_F11, KeyEvent.VK_F11);
        nativeToJava.put(NativeKeyEvent.VC_F12, KeyEvent.VK_F12);
        nativeToJava.put(NativeKeyEvent.VC_F13, KeyEvent.VK_F13);
        nativeToJava.put(NativeKeyEvent.VC_F14, KeyEvent.VK_F14);
        nativeToJava.put(NativeKeyEvent.VC_F15, KeyEvent.VK_F15);
        nativeToJava.put(NativeKeyEvent.VC_F16, KeyEvent.VK_F16);
        nativeToJava.put(NativeKeyEvent.VC_F17, KeyEvent.VK_F17);
        nativeToJava.put(NativeKeyEvent.VC_F18, KeyEvent.VK_F18);
        nativeToJava.put(NativeKeyEvent.VC_F19, KeyEvent.VK_F19);
        nativeToJava.put(NativeKeyEvent.VC_F20, KeyEvent.VK_F20);
        nativeToJava.put(NativeKeyEvent.VC_F21, KeyEvent.VK_F21);
        nativeToJava.put(NativeKeyEvent.VC_F22, KeyEvent.VK_F22);
        nativeToJava.put(NativeKeyEvent.VC_F23, KeyEvent.VK_F23);
        nativeToJava.put(NativeKeyEvent.VC_F24, KeyEvent.VK_F24);

        nativeToJava.put(NativeKeyEvent.VC_0, KeyEvent.VK_0);
        nativeToJava.put(NativeKeyEvent.VC_1, KeyEvent.VK_1);
        nativeToJava.put(NativeKeyEvent.VC_2, KeyEvent.VK_2);
        nativeToJava.put(NativeKeyEvent.VC_3, KeyEvent.VK_3);
        nativeToJava.put(NativeKeyEvent.VC_4, KeyEvent.VK_4);
        nativeToJava.put(NativeKeyEvent.VC_5, KeyEvent.VK_5);
        nativeToJava.put(NativeKeyEvent.VC_6, KeyEvent.VK_6);
        nativeToJava.put(NativeKeyEvent.VC_7, KeyEvent.VK_7);
        nativeToJava.put(NativeKeyEvent.VC_8, KeyEvent.VK_8);
        nativeToJava.put(NativeKeyEvent.VC_9, KeyEvent.VK_9);

        nativeToJava.put(NativeKeyEvent.VC_A, KeyEvent.VK_A);
        nativeToJava.put(NativeKeyEvent.VC_B, KeyEvent.VK_B);
        nativeToJava.put(NativeKeyEvent.VC_C, KeyEvent.VK_C);
        nativeToJava.put(NativeKeyEvent.VC_D, KeyEvent.VK_D);
        nativeToJava.put(NativeKeyEvent.VC_E, KeyEvent.VK_E);
        nativeToJava.put(NativeKeyEvent.VC_F, KeyEvent.VK_F);
        nativeToJava.put(NativeKeyEvent.VC_G, KeyEvent.VK_G);
        nativeToJava.put(NativeKeyEvent.VC_H, KeyEvent.VK_H);
        nativeToJava.put(NativeKeyEvent.VC_I, KeyEvent.VK_I);
        nativeToJava.put(NativeKeyEvent.VC_J, KeyEvent.VK_J);
        nativeToJava.put(NativeKeyEvent.VC_K, KeyEvent.VK_K);
        nativeToJava.put(NativeKeyEvent.VC_L, KeyEvent.VK_L);
        nativeToJava.put(NativeKeyEvent.VC_M, KeyEvent.VK_M);
        nativeToJava.put(NativeKeyEvent.VC_N, KeyEvent.VK_N);
        nativeToJava.put(NativeKeyEvent.VC_O, KeyEvent.VK_O);
        nativeToJava.put(NativeKeyEvent.VC_P, KeyEvent.VK_P);
        nativeToJava.put(NativeKeyEvent.VC_Q, KeyEvent.VK_Q);
        nativeToJava.put(NativeKeyEvent.VC_R, KeyEvent.VK_R);
        nativeToJava.put(NativeKeyEvent.VC_S, KeyEvent.VK_S);
        nativeToJava.put(NativeKeyEvent.VC_T, KeyEvent.VK_T);
        nativeToJava.put(NativeKeyEvent.VC_U, KeyEvent.VK_U);
        nativeToJava.put(NativeKeyEvent.VC_V, KeyEvent.VK_V);
        nativeToJava.put(NativeKeyEvent.VC_W, KeyEvent.VK_W);
        nativeToJava.put(NativeKeyEvent.VC_X, KeyEvent.VK_X);
        nativeToJava.put(NativeKeyEvent.VC_Y, KeyEvent.VK_Y);
        nativeToJava.put(NativeKeyEvent.VC_Z, KeyEvent.VK_Z);

        javaToNative = new HashMap<>();
        nativeToJava.forEach((key, value) -> javaToNative.put(value, key));
    }

    public static int toJava(int nativeKeyCode) {
        return nativeToJava.getOrDefault(nativeKeyCode, -1);
    }

    public static int toNative(int javaKeyCode) {
        return javaToNative.getOrDefault(javaKeyCode, -1);
    }
}
