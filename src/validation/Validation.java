package validation;

import javafx.scene.control.*;


public class Validation {
    public static boolean isTextFieldNotEmpty(TextField tf){
        boolean check=false;
        if(tf.getText().length()!=0 || !tf.getText().isEmpty()){
            check=true;
        }
        return check;
    }
    public static boolean isTextFieldNotEmpty(TextField tf, Label lb, String error){
        boolean check=true;
        String msg=null;
        if(!isTextFieldNotEmpty(tf)){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
    public static boolean isDatePickerNotEmpty(DatePicker dp, Label lb, String error){
        boolean check=true;
        String msg=null;

        if(dp.getValue()==null){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
    public static boolean isGenderNotEmpty(RadioButton rbNam, RadioButton rbNu, Label lb, String error){
        boolean check=true;
        String msg=null;

        if(!rbNam.isSelected() && !rbNu.isSelected()){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
    public static boolean isNumber(TextField tf, Label lb, String error){
        boolean check=true;
        String msg=null;
        String k=tf.getText();
        try{
            int n=Integer.parseInt(k);
        }catch(Exception ex){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
    public static boolean isComboBoxNotEmpty(ComboBox cb, Label lb, String error){
        boolean check=true;
        String msg=null;

        if(cb.getValue()==null){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
    public static boolean isPhoneNumberTrue(TextField tf, Label lb, String error){
        boolean check=true;
        String msg=null;
        String k=tf.getText();
        if(k.length()!=10 || k.charAt(0)!='0'){
            check=false;
            msg=error;
        }
        lb.setText(msg);
        return check;
    }
}
