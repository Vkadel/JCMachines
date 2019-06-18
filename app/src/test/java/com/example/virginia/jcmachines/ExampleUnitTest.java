package com.example.virginia.jcmachines;

import com.example.virginia.jcmachines.Data.effcalculation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void effCalculationTest(){
        effcalculation calc=new effcalculation();
        calc.setAx(201);
        calc.setV(150);
        calc.setN(241);
        calc.setVd(1790);
        assertEquals(calc.getEff(),7.0,0);
        calc.setVd(1050);
        assertEquals(calc.getEff(),12.0,0);

    }
    @Test
    public void effCalculationTextBrokendown(){
        effcalculation calc=new effcalculation();
        calc.setC(21);
        calc.setL(24);
        calc.setR(2);
        calc.setW(24);
        calc.setH(24);
        calc.setZ(3);
        calc.setN(211);
        calc.setVd(1790);
        assertEquals(calc.getEff(),73.0,0);
        calc.setVd(1050);
        assertEquals(calc.getEff(),125.0,0);
    }
}