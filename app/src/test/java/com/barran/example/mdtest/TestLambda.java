package com.barran.example.mdtest;

import android.view.View;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * study lambda
 *
 * Created by tanwei on 2017/3/30.
 */

public class TestLambda {

    @Test
    public void testStream(){
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");

        View view = new View(null);
        view.setOnClickListener(v -> System.out.println(11));
    }

}
