package Test.Board;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import sample.MyCircle;
import sample.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class GuiTest {
    /*
    Making sure javafx functions are not depricated . Javafx is no longer
    in the hands of oracle and is open source.
    It has been removed from java core after jdk 8
    and is an open source external library

    Mainly checking if the "getColor" getfill function to get the color of a marble works
     */
    @Test
    void MarbleColorCodeCheck(){
        MyCircle circle = new MyCircle(200,200,20, Color.BLACK);
        assertEquals(200, circle.getCenterX());
        assertEquals(200, circle.getCenterY());
        assertEquals("0x000000ff",circle.getFill().toString());
        MyCircle gold = new MyCircle(300,300,20, Color.OLIVE);
        assertEquals(300, gold.getCenterY());
        assertEquals("0x808000ff",gold.getFill().toString());
        MyCircle red = new MyCircle(300,300,20, Color.RED);
        assertEquals("0xff0000ff",red.getFill().toString());
        MyCircle white = new MyCircle(300,300,20, Color.WHITE);
        assertEquals("0xffffffff",white.getFill().toString());
    }
    @Test
    void boardInitTest4Player(){
        View view = new View();
        assertEquals(9,view.CircleInitRed().length);
        assertEquals(9,view.CircleInitWhite().length);
        assertEquals(9,view.CircleInitBlack().length);
        assertEquals(9,view.CircleInitBlue().length);
    }
    @Test
    void boardInitTest2Player(){
        View view = new View();
        assertEquals(14,view.red14().length);
        assertEquals(14,view.white14().length);
        //white
        assertEquals("Circle[centerX=375.0, centerY=574.0, radius=20.0, fill=0xffffffff]",view.white14()[0].toString());
        assertEquals("Circle[centerX=440.0, centerY=574.0, radius=20.0, fill=0xffffffff]",view.white14()[1].toString());
        assertEquals("Circle[centerX=505.0, centerY=574.0, radius=20.0, fill=0xffffffff]",view.white14()[2].toString());
        assertEquals("Circle[centerX=570.0, centerY=574.0, radius=20.0, fill=0xffffffff]",view.white14()[3].toString());
        assertEquals("Circle[centerX=635.0, centerY=574.0, radius=20.0, fill=0xffffffff]",view.white14()[4].toString());
        assertEquals("Circle[centerX=345.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[5].toString());
        assertEquals("Circle[centerX=410.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[6].toString());
        assertEquals("Circle[centerX=475.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[7].toString());
        assertEquals("Circle[centerX=540.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[8].toString());
        assertEquals("Circle[centerX=605.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[9].toString());
        assertEquals("Circle[centerX=670.0, centerY=518.0, radius=20.0, fill=0xffffffff]",view.white14()[10].toString());
        assertEquals("Circle[centerX=440.0, centerY=462.0, radius=20.0, fill=0xffffffff]",view.white14()[11].toString());
        assertEquals("Circle[centerX=505.0, centerY=462.0, radius=20.0, fill=0xffffffff]",view.white14()[12].toString());
        assertEquals("Circle[centerX=570.0, centerY=462.0, radius=20.0, fill=0xffffffff]",view.white14()[13].toString());
        //Red
        assertEquals("Circle[centerX=375.0, centerY=126.0, radius=20.0, fill=0x000000ff]",view.red14()[0].toString());
        assertEquals("Circle[centerX=440.0, centerY=126.0, radius=20.0, fill=0x000000ff]",view.red14()[1].toString());
        assertEquals("Circle[centerX=505.0, centerY=126.0, radius=20.0, fill=0x000000ff]",view.red14()[2].toString());
        assertEquals("Circle[centerX=570.0, centerY=126.0, radius=20.0, fill=0x000000ff]",view.red14()[3].toString());
        assertEquals("Circle[centerX=635.0, centerY=126.0, radius=20.0, fill=0x000000ff]",view.red14()[4].toString());
        assertEquals("Circle[centerX=345.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[5].toString());
        assertEquals("Circle[centerX=410.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[6].toString());
        assertEquals("Circle[centerX=475.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[7].toString());
        assertEquals("Circle[centerX=540.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[8].toString());
        assertEquals("Circle[centerX=605.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[9].toString());
        assertEquals("Circle[centerX=670.0, centerY=182.0, radius=20.0, fill=0x000000ff]",view.red14()[10].toString());
        assertEquals("Circle[centerX=440.0, centerY=238.0, radius=20.0, fill=0x000000ff]",view.red14()[11].toString());
        assertEquals("Circle[centerX=505.0, centerY=238.0, radius=20.0, fill=0x000000ff]",view.red14()[12].toString());
        assertEquals("Circle[centerX=570.0, centerY=238.0, radius=20.0, fill=0x000000ff]",view.red14()[13].toString());
        //assert start marble positions is as expected

    }
    @Test
    void boarInitTest3Player(){
        View view = new View();
        assertEquals(11,view.redPieces3().length);
        assertEquals(11,view.whitePieces3().length);
        assertEquals(11,view.blackPieces3().length);

    }
}
