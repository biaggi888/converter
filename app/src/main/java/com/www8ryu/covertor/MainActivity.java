package com.www8ryu.covertor;

import android.view.*;
import android.widget.*;
import java.util.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import java.lang.reflect.Field;
import android.util.Size;

public class MainActivity extends Activity {
  class M {
    public static final int DEC_MODE = 0xff01;
    public static final int HEX_MODE = 0xff02;


    public static final String D_DEC = "DEC . ";
    public static final String D_HEX = "HEX . ";
    public static final String[] FLAGS_MODE = {D_DEC, D_HEX};
  }
  TextView tvDec;
  TextView tvHex;
  TextView tvDebug;
  public Button btnResult;
  public Button btnClear;
  public static String decString = "";
  public static String hexString = "";
  public static int d_result = 0;
  public static int mode;

  List<Button> decBtns ;
  List<Button> hexBtns ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);

    //Initial view
    tvDec = (TextView)findViewById(R.id.mTvDisplayDec);
    tvHex = (TextView)findViewById(R.id.mTvDisplayHex);
    tvDebug = (TextView)findViewById(R.id.mDebug);
    final TextView display[] = {tvDec, tvHex};

    //Initial all number button list[0]digi, [1] a~f
    List<List<Button>> btns = initBtn(display, mode);
    decBtns = btns.get(0);
    hexBtns = btns.get(1);
    changeMode(mode = M.DEC_MODE);

    //Get result
    btnResult = (Button)findViewById(R.id.btn_result);
    btnResult.setOnClickListener(resultListener);

    //Button clear action
    btnClear = (Button)findViewById(R.id.btn_clear);
    btnClear.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View p1) {
//          for (int i = 0,len=display.length; i < len; i++) {
//            display[i].setText(M.FLAGS_MODE[i]);
//          }
//          decString = "";
//          hexString = "";
          clearDisplay();
        }
      });
    //tvDebug.setText(hexBtns.get(0).getText());
//btn change mode 
    Button btnChangeDec = (Button)findViewById(R.id.btn_dec);
    Button btnChangeHex = (Button)findViewById(R.id.btn_hex);
    btnChangeDec.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View p1) {
          mode = changeMode(M.DEC_MODE);
        }
      });
    btnChangeHex.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View p1) {
          mode = changeMode(M.HEX_MODE);
        }
      });
  };

  /**
   List[0] 0~9
   List[1] A~F
   */
  public List<List<Button>> initBtn(TextView[] v, int mode) {
    int len = v.length;
    List<List<Button>> l = new ArrayList<List<Button>>();
    List<Button> d = new ArrayList<Button>();
    List<Button> h = new ArrayList<Button>();
    try {
      Class klazz = Class.forName("com.www8ryu.covertor.R$id");
      Field[] fields = klazz.getFields();
      char ch = 'A';
      int i = 0;
      for (Field f : fields) {
        if (("btn_" + i).equals(f.getName())) {
          Button b = (Button)findViewById(f.getInt(klazz));
          setOnClick(b, v, i + "");
          d.add(b);
          i++;
        } else if (("btn_" + ch).equals(f.getName())) {
          Button b = (Button)findViewById(f.getInt(klazz));
          //setOnClick(b, v[0], ch + "");
          setOnClick(b, v, ch + "");
          ch += 1;
          h.add(b);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    l.add(d);
    l.add(h);
    return l;
  }

  public void setOnClick(Button b, final TextView[] vv, final String s) {
    b.setOnClickListener(new OnClickListener(){

        @Override
        public void onClick(View v) {
          if (mode == M.DEC_MODE) {
            decString += s;
            vv[0].append(s);
          }
          if (mode == M.HEX_MODE) {
            hexString += s;
            vv[1].append(s);
          }
        }
      });
  }

  public OnClickListener resultListener = new OnClickListener() {

    @Override
    public void onClick(View p1) {
      if (mode == M.DEC_MODE) {
        int decInt = Integer.parseInt(decString);
        tvHex.setText(M.D_HEX + Integer.toHexString(decInt).toUpperCase());
        tvDebug.setText(decString);
      }
      if (mode == M.HEX_MODE) {
        tvDec.setText(M.D_DEC + Integer.parseInt(hexString, 16));
      }

    }
  };

  public int changeMode(int mode) {
    if (mode == M.DEC_MODE) {
      for (Button b : hexBtns) {
        b.setEnabled(false);
        mode = M.DEC_MODE;
      }
    }
    if (mode == M.HEX_MODE) {
      for (Button b : hexBtns) {
        b.setEnabled(true);
        mode = M.HEX_MODE;
      }
    }
    clearDisplay();
    return mode;
  }

  public void clearDisplay() {
    tvHex.setText(M.D_HEX);
    tvDec.setText(M.D_DEC);
    decString = "";
    hexString = "";
  }
};
