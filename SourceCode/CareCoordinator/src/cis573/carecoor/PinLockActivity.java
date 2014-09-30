package cis573.carecoor;

import cis573.carecoor.utils.PreferenceUtil;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dblank on 4/24/14.
 */
public class PinLockActivity extends BannerActivity {

    private String code = "";
    private String testCode;
    private int count;
    private TextView pin;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.pin_lock_activity);
        setBannerTitle(R.string.pin_lock_name);
        code = PreferenceUtil.get(getApplicationContext(), 0 );
        pin = (TextView) findViewById(R.id.pin);
        showBackButton(false);
        testCode = "";
        count = 0;
    }

    public void changeView(int num){
        String temp = "";
        for( int i = 0; i < 4; i++){
            if(i == count - 1){
                temp = temp + Integer.toString(num) + " ";
            }else if(i < count){
                temp = temp + testCode.charAt(i) + " ";
            }else{
                temp = temp + "_ ";
            }
        }
        pin.setText(temp);
    }


    public void onPinButtonClick(View v) {
        int id = v.getId();
        if(id == R.id.button_1) {
            System.out.println("1");
            testCode = testCode + "1";
            count++;
            changeView(1);
            if(count == 4)testPin();
        } else if(id == R.id.button_2) {
            System.out.println("2");
            testCode = testCode + "2";
            count++;
            changeView(2);
            if(count == 4)testPin();
        } else if(id == R.id.button_3) {
            System.out.println("3");
            testCode = testCode + "3";
            count++;
            changeView(3);
            if(count == 4)testPin();
        } else if(id == R.id.button_4) {
            System.out.println("4");
            testCode = testCode + "4";
            count++;
            changeView(4);
            if(count == 4)testPin();
        } else if(id == R.id.button_5) {
            System.out.println("5");
            testCode = testCode + "5";
            count++;
            changeView(5);
            if(count == 4)testPin();
        } else if(id == R.id.button_6) {
            System.out.println("6");
            testCode = testCode + "6";
            count++;
            changeView(6);
            if(count == 4)testPin();
        } else if(id == R.id.button_7) {
            System.out.println("7");
            testCode = testCode + "7";
            count++;
            changeView(7);
            if(count == 4)testPin();
        } else if(id == R.id.button_8) {
            System.out.println("8");
            testCode = testCode + "8";
            count++;
            changeView(8);
            if(count == 4)testPin();
        } else if(id == R.id.button_9) {
            System.out.println("9");
            testCode = testCode + "9";
            count++;
            changeView(9);
            if(count == 4)testPin();
        } else if(id == R.id.button_0) {
            System.out.println("0");
            testCode = testCode + "0";
            count++;
            changeView(0);
            if(count == 4)testPin();
        }
    }

    private void testPin(){
        if(testCode.equals(code)){
            setResult(RESULT_OK);
            finish();
        }else{
            testCode = "";
            count = 0;
            changeView(0);
        }
    }
    
    @Override
    public void onBackPressed() {
    }
    
}
