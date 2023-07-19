package com.barran.example.mdtest;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    
    @Test
    public void testParseParam(){
        String param = "IVBLjava/lang/String;[IZ[Ljava/lang/String;SB";
        System.out.println(parseParam(param));
    }

    public static List<String> parseParam(String desc) {
        List<String> funcArgs = new ArrayList<>();
        boolean waitArrayEnd = false;
        boolean waitObjectEnd = false;
        StringBuilder extend = new StringBuilder();
        for (char c : desc.toCharArray()) {

            switch (c) {
                case 'L':
                    if(!waitObjectEnd){
                        waitObjectEnd = true;
                    }
                    extend.append(c);
                    continue;
                case '[':
                    if(!waitArrayEnd) {
                        waitArrayEnd = true;
                    }
                    extend.append(c);
                    continue;
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 'V':
                    if (waitObjectEnd) {
                        extend.append(c);
                    } else if (waitArrayEnd) {
                        extend.append(c);
                        funcArgs.add(extend.toString());
                        extend.setLength(0);
                        waitArrayEnd = false;
                    } else {
                        funcArgs.add(String.valueOf(c));
                    }
                    continue;
                case ';':
                    if (waitObjectEnd) {
                        extend.append(c);
                        funcArgs.add(extend.toString());
                        extend.setLength(0);
                        waitObjectEnd = false;
                    } else {
                        System.out.println("parseParam error char ';' but extending=false");
                    }
                    continue;
                default:
                    if (waitObjectEnd) {
                        extend.append(c);
                    } else {
                        System.out.println("parseParam unknown char " + c);
                        break;
                    }
            }
        }
        System.out.println("parseParam " + funcArgs.toString());
        return funcArgs;
    }
}