/* ---------------------------------------------------------------------
 * @(#)Validator.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.bluenetworks.webapp.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>데이타 타입, 값의 범위 등 입력값 중심의 간단한 유효성 검증 유틸 클래스.</p>
 * 
 * @since 2.0
 * @version 2005-02-01 v1.0
 */
public final class Validator {
    
       
    /**
     * <p>문자 최대길이 확인.</p>
     *
     * @param    data 최대길이를 확인할 대상 문자열.
     * @param    maxLength 최대길이 값.
     *
     * @return   길이를 초과하면 <code>true</code>, 미달하면 <code>false</code>.
     */        
    public static boolean maxLen (String data, int maxLength) {  
        return (data.length() > maxLength);
    }


    /**
     * <p>문자 최소길이 확인.</p>
     *
     * @param    data 최소길이를 확인할 대상 문자열.
     * @param    minLength 최소길이 값.
     *
     * @return   길이보다 작으면(미달하면) <code>true</code>, 초과하면 <code>false</code>.
     */    
    public static boolean minLen (String data, int minLength) {
        return (( data != null ) && ( data.length() < minLength ));
    }    


    /**
     * <p>소수값의 범위 확인.</p>
     *
     * @param    data 값의 범위내에 속하는지 확인할 값.
     * @param    lowerData 값의 최소값(시작값).
     * @param    upperData 값의 최대값(시작값).
     *
     * @return   값의 범위에 포함하면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean betweenVal (double data, double lowerData, double upperData) {
        return ( (lowerData < data) && (data < upperData) );
    }    


    /**
     * <p>정수값의 범위 확인.</p>
     *
     * @param    data 값의 범위내에 속하는지 확인할 값.
     * @param    lowerData 값의 최소값(시작값).
     * @param    upperData 값의 최대값(시작값).
     *
     * @return   값의 범위에 포함하면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean betweenVal (int data, int lowerData, int upperData) {
        return ( (lowerData < data) && (data < upperData) );
    } 


    /**
     * <p>날짜값의 범위 확인.</p>
     *
     * @param    betweenVal 값의 범위내에 속하는지 확인할 날짜값.
     * @param    lowerData 날짜값의 최소값(시작값).
     * @param    upperData 날짜값의 최대값(시작값).
     *
     * @return   값의 범위에 포함하면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean betweenVal (Date data, Date lowerData, Date upperData) {
        return ( (data.after(lowerData)) && (data.before(upperData)) );
    }
    

    /**
     * <p>문자열 길이의 범위 확인.</p>
     *
     * @param    data 길이가 범위내에 속하는지 확인할 문자열.
     * @param    lowerData 길이의 최소값(시작값).
     * @param    upperData 길이의 최대값(시작값).
     *
     * @return   길이의 범위에 포함하면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean betweenVal (String data, String lowerData, String upperData) {
        return ( (lowerData.length()<data.length()) && (data.length()<upperData.length()) );
    }
    

    /**
     * <p>문자열이 Null인지 확인한다.</p>
     *
     * @param    data 대상 문자열.
     *
     * @return   Null이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNull (String data) {
        return ( data == null );
    }

    /**
     * <p>문자열이 Null인지 확인한다.</p>
     *
     * @param    data 대상 문자열.
     *
     * @return   Null이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNull (Object data) {
        return ( data == null );
    }    

    /**
     * <p>문자열이 제로(0)인지 확인한다.</p>
     *
     * @param    data 대상 문자열.
     *
     * @return   제로(0)이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isZero (String data) {
        return  data.equals("0") ;
    }
    
    /**
     * <p>문자열이 공백 또는 null인지 확인한다.</p>
     * 
     * @pi2.example .
     * Validator.isBlank(null)      = true
     * Validator.isBlank("")        = true
     * Validator.isBlank(" ")       = true
     * Validator.isBlank("bob")     = false
     * Validator.isBlank("  bob  ") = false
     *
     * @param str  대상 문자열
     * @return 값이 비어있거나, 공백, 제로(0)이면 <code>true</code>, 아니면 <code>false</code>.
     * @since 2.0
     */
    public static boolean isBlank(String str) {

        /*--
        if (data == null)
            return true;
        else
            return (data.equals("")); 
        --*/

        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!(Character.isWhitespace(str.charAt(i)) ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>문자열에 값이 있는지를 확인한다.</p>
     * 
     * @pi2.example .
     * Validator.isNotBlank(null)      = false
     * Validator.isNotBlank("")        = false
     * Validator.isNotBlank(" ")       = false
     * Validator.isNotBlank("bob")     = true
     * Validator.isNotBlank("  bob  ") = true
     *
     * @param str  대상 문자열
     * @return 값이 비어있거나, 공백, 제로(0)이면 <code>false</code>, 아니면 <code>true</code>.
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!(Character.isWhitespace(str.charAt(i)) ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>스트링이 비어있거나("") null인지를 확인한다.</p>
     * 
     * @pi2.example .
     * Validator.isEmpty(null)      = true
     * Validator.isEmpty("")        = true
     * Validator.isEmpty(" ")       = false
     * Validator.isEmpty("bob")     = false
     * Validator.isEmpty("  bob  ") = false
     * 
     * @param str  대상 문자열
     * @return 스트링이 비어있거나("") null이면 <code>true</code>, 아니면 <code>false</code>
     */
    public static boolean isEmpty(String str) {
    	str = str.trim();
    	return (str == null || str.length() == 0);
    }

    /**
     * <p>스트링이 비어있지 않거나 null이 아닌지를 확인한다.</p>
     * 
     * @pi2.example .
     * Validator.isNotEmpty(null)      = false
     * Validator.isNotEmpty("")        = false
     * Validator.isNotEmpty(" ")       = true
     * Validator.isNotEmpty("bob")     = true
     * Validator.isNotEmpty("  bob  ") = true
     *
     * @param str  대상 문자열
     * @return 스트링이 비어있거나("") null이면 <code>false</code>, 아니면 <code>true</code>
     */
    public static boolean isNotEmpty(String str) {
    	str = str.trim();
    	return (str != null && str.length() > 0);
    }

    /**
     * <p>문자열이 Null 또는 공백인지 확인한다.</p>
     *
     * @param    data 대상 문자열.
     *
     * @return   값이 비어있거나 Null이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNoData (String data) {
        return( data == null || data.length() == 0 );
    }
    

    /**
     * <p>벡터 사이즈가 제로인지 확인한다.</p>
     *
     * @param    data 대상 벡터객체.
     *
     * @return   사이즈가 제로이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNoData (Vector data) {
        return ( data.size() == 0 );    
    }  
    

    /**
     * <p>ArrayList 사이즈가 제로인지 확인한다.</p>
     *
     * @param    data 대상 ArrayList 객체.
     *
     * @return   사이즈가 제로이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNoData (ArrayList data) {
        return ( data.size() == 0 );    
    } 


    /**
     * <p>객체가 Null인지 확인한다.</p>
     *
     * @param    data 대상 객체.
     *
     * @return   Null이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNoData (Object data) {
        return ( data == null );
    }   
    

    /**
     * <p>배열길이가 제로(0)인지 확인한다.</p>
     *
     * @param    data 대상 객체.
     *
     * @return   제로(0)이면 <code>true</code>, 아니면 <code>false</code>.
     */    
    public static boolean isNoData (Object[] data) {
        return ( data.length == 0 );    
    }    
    

    /**
     * <p>스트링이 숫자로만 이루어져 있는지 확인한다.</p>
     *
     * @param    text 대상 문자열.
     *
     * @return   숫자인 문자로만 되어있으면 <code>true</code>, 아니면 <code>false</code>.
     */
    public final static boolean isAlphaNumeric (String text) {
        
        if (text == null)
            return false;

        int size = text.length();
        for(int i = 0 ; i < size ; i++) {
            if(!Character.isDigit(text.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * <p>E-mail주소가 올바른지 확인한다.</p>
     *
     * @param    data 대상 문자열.
     *
     * @return   맞으면 <code>true</code>, 아니면 <code>false</code>.
     */
    public static boolean isEmailAddr (String data) {
       
        if (data == null)
            return false;  
        
        if (data.indexOf("@") == -1) 
            return false;            
      
        return true;
    }
    

    /**
     * <p>주민등록번호가 올바른지 확인한다.</p>
     *
     * @param    serial1 대상 문자열 1.
     * @param    serial2 대상 문자열 2.
     *
     * @return   맞으면 <code>true</code>, 아니면 <code>false</code>.
     */
    public static boolean isSSN (String serial1, String serial2) {
     
        if (serial1.length() != 6) {
            return false;
        }
        else if (serial2.length() != 7) {
            return false;
        }
        else {
            int digit = 0;
            for (int i=0 ; i < serial1.length() ; i++) {
                String str_dig = serial1.substring(i,i+1);
                if (str_dig.length() < 0 || str_dig.length() > 9) { 
                    digit = digit + 1; 
                }
            }

            if ((serial1 == "") || ( digit != 0 )) {
                return false;   
            }

            int digit1=0;
            for(int i=0 ; i < serial2.length() ; i++) {
                String str_dig1 = serial2.substring(i,i+1);
                if(str_dig1.length() < 0 || str_dig1.length() > 9) { 
                    digit1 = digit1 + 1; 
                }
            }

            if((serial2 == "") || ( digit1 != 0 )) {
                return false;   
            }

            if (Integer.parseInt(serial1.substring(2,3)) > 1) {
                return false;
            }

            if (Integer.parseInt(serial1.substring(4,5))> 3) {
                return false;   
            }

            if (Integer.parseInt(serial2.substring(0,1))> 4 || 
                Integer.parseInt(serial2.substring(0,1)) == 0) {
                return false;   
            }

            int a1= Integer.parseInt(serial1.substring(0,1));
            int a2= Integer.parseInt(serial1.substring(1,2));
            int a3= Integer.parseInt(serial1.substring(2,3));
            int a4= Integer.parseInt(serial1.substring(3,4));
            int a5= Integer.parseInt(serial1.substring(4,5));
            int a6= Integer.parseInt(serial1.substring(5,6));

            int check_digit1 = a1*2 + a2*3 + a3*4 + a4*5 + a5*6 + a6*7;

            int  b1 =  Integer.parseInt(serial2.substring(0,1));
            int  b2 =  Integer.parseInt(serial2.substring(1,2));
            int  b3 =  Integer.parseInt(serial2.substring(2,3));
            int  b4 =  Integer.parseInt(serial2.substring(3,4));
            int  b5 =  Integer.parseInt(serial2.substring(4,5));
            int  b6 =  Integer.parseInt(serial2.substring(5,6));
            int  b7 =  Integer.parseInt(serial2.substring(6,7));

            int check_digit = check_digit1 + b1*8 + b2*9 + b3*2 + b4*3 + b5*4 + b6*5;

            check_digit = check_digit % 11;
            check_digit = 11 - check_digit;
            check_digit = check_digit % 10;

            if (check_digit != b7) {
                return false;   
            }
            else {
                return true;      
            }
        }
    }
    
    
    public static final String num 			=	"[0-9]"; 
    public static final String low 			=	"[a-z]"; 
    public static final String upper 		=	"[A-Z]";   
    public static final String space 		= 	" ";
    public static final String illegalStr 	= 	"[~`!@#[$]%\\^&[*]\\(\\)_-[+]=[|]\\]\\}\\[\\{'\";:/?.><\\,]";
    public static final String cnum	    	=   "123456789012345678909876543210987654321";
    public static final String cengupper   	=   "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZYXWVUTSRQPONMLKJIHGFEDCBAZYXWVUTSRQPONMLKJIHGFEDCBA";
    public static final String cenglow	    =   "abcdefghijklmnopqrstuvwxyzyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba";
    public static final String ckeyboard   	=   "qwertyuiopasdfghjklzxcvbnmNBVCXZLKJHGFDSAPOIUYTREWQ";
    public static final String duplicatePattern = "(\\w)\\1{3,}";
    public static String checkPassword (String input) {
    	String pwdVal = input;  
    	int cnt    = 0;
    	
    	if(pwdVal.equals("")){  
    		return "MV0114";  
    	} 
    	
    	System.out.println("==>"+Pattern.compile(duplicatePattern).matcher(pwdVal).find());
        Pattern p = Pattern.compile(duplicatePattern);  
        Matcher m = p.matcher(pwdVal);
       
        if (m.find()) {
        	return "MV0220"; 
        }

    	 
    	for(int i = 0; i < pwdVal.length(); i++){ 
    		if(i+3 <=  pwdVal.length()){
	    		String chkPwdStr = pwdVal.substring(i,i+3);
	    		System.out.println("chkPwdStr==>"+chkPwdStr);
	    		if(chkPwdStr.length() >= 3){ 
	    			if(cnum.indexOf(chkPwdStr) > -1 || cengupper.indexOf(chkPwdStr) > -1 || cenglow.indexOf(chkPwdStr) > -1 ){
	    				return "MV0221"; 
	    			}else if(ckeyboard.indexOf(chkPwdStr) > -1){
	    				return "MV0222"; 
	    			}
	    		}
    		}
    	}
    	 
    	 
    	if(pwdVal !="" &&  pwdVal.indexOf(space) > -1){   
    		return "MV0116";  
    	}

//    	System.out.println("num=1=>"+Pattern.compile(num).matcher(pwdVal).find()); 
    	if(pwdVal !="" && Pattern.compile(num).matcher(pwdVal).find() ){ 
    		cnt++; 
    	} 
//    	System.out.println("low==>"+Pattern.compile(low).matcher(pwdVal).find());
    	if(pwdVal !="" && Pattern.compile(low).matcher(pwdVal).find()){ 
    		cnt++; 
    	} 
//    	System.out.println("upper==>"+Pattern.compile(upper).matcher(pwdVal).find());
    	if(pwdVal !="" && Pattern.compile(upper).matcher(pwdVal).find()){ 
    		cnt++; 
    	}  
//    	System.out.println("illegalStr==>"+Pattern.compile(illegalStr).matcher(pwdVal).find());
    	if(pwdVal !="" && Pattern.compile(illegalStr).matcher(pwdVal).find()){ 
    		cnt++;
    	}
    	 
    	if(cnt == 2 && pwdVal.length() < 12){  
    		return "MV0117"; 
    	}
    	if(cnt > 2 && pwdVal.length() < 8){ 
    		return "MV0118"; 
    	}
    	if(cnt < 2){
    		return "MV0118"; 
    	}
    	
    	return "";
    }
    
    
    public static void main(String args []){
    	String pwd = "1q2w3e4|";
    	System.out.println(">> "+Validator.checkPassword(pwd));
    }

}