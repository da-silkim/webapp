/**
 * @Class Name  : SignetStringUtil.java
 * @Description : 문자열 데이터 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.01.13     박정규          최초 생성
 *   2009.02.13     이삼섭          내용 추가
 *
 * @author 공통 서비스 개발팀 박정규
 * @since 2009. 01. 13
 * @version 1.0
 * @see
 *
 */

package com.bluenetworks.webapp.common;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the ";License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 숫자를 문자열로 변경
	 */
	private static DecimalFormat df = new DecimalFormat("##################");

    /**
     * 빈 문자열 <code>""</code>.
     */
    public static final String EMPTY = "";
    
    /**
     * <p>두 문자열을 대소문자 구분하여 비교한다.</p>
     *
     * <p><code>null</code>이 입력되어도 예외는 던져지지 않으며, 비교대상 문자열 모두가
     * <code>null</code>일 경우 <code>true</code>를 반환한다.</p>
     *
     * @pi2.example .
     * StringUtil.equals(null, null)   = true
     * StringUtil.equals(null, "abc")  = false
     * StringUtil.equals("abc", null)  = false
     * StringUtil.equals("abc", "abc") = true
     * StringUtil.equals("abc", "ABC") = false
     *  
     * @see String#equals(Object)
     * @param str1  비교대상 문자열 1
     * @param str2  비교대상 문자열 2
     * @return 같으면 <code>true</code>
     */
    public static boolean equals(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }
    
    /**
     * 2개의 객체(문자열 포함)를 비교한다. 한쪽이 null이면 false를 리턴하며, 양쪽 모두가 null이면 true를 리턴한다.
     *
     * @param obj1     비교할 첫번째 문자열
     * @param obj2     비교할 두번째 문자열
     * @return 같다면 true, 아니면 false
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2.equals(obj1);
        }
    }
    
    /**
     * <p>두 문자열을 대소문자 구분없이 비교한다.</p>
     *
     * <p><code>null</code>이 입력되어도 예외는 던져지지 않으며, 비교대상 문자열 모두가
     * <code>null</code>일 경우 <code>true</code>를 반환한다.</p>
     *
     * @pi2.example .
     * StringUtil.equalsIgnoreCase(null, null)   = true
     * StringUtil.equalsIgnoreCase(null, "abc")  = false
     * StringUtil.equalsIgnoreCase("abc", null)  = false
     * StringUtil.equalsIgnoreCase("abc", "abc") = true
     * StringUtil.equalsIgnoreCase("abc", "ABC") = true
     * 
     * @see String#equalsIgnoreCase(String)
     * @param str1  비교대상 문자열 1
     * @param str2  비교대상 문자열 2
     * @return 같으면 <code>true</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
    }
    
    /**
     * 스트링을 지정한 길이만큼 자른다.
     *
     * @pi2.example .
     * StringUtil.setMaxLength("aaaa", 2)  = "aa"
     * StringUtil.setMaxLength("aaaa", 0)  = ""
     * StringUtil.setMaxLength("aaaa", 12) = "aaaa"
     * StringUtil.setMaxLength("aaaa", -1) = "aaaa"
     *
     * @param s      대상 문자열
     * @param len    자를 길이의 최대값
     * @return 지정한 길이만큼 잘려진 문자열
     */
    public static String setMaxLength(String s, int len) {
        if (s == null) {
            return s;
        }
        if (len < 0) {
            return s;
        }
        if (s.length() > len) {
            s = s.substring(0, len);
        }
        return s;
    }
    
    /**
     * 스트링을 지정한 길이만큼 자른다.
     *
     * @pi2.example .
     * StringUtil.fixLength("aaaa", 2)  = "aa"
     * StringUtil.fixLength("aaaa", 0)  = ""
     * StringUtil.fixLength("aaaa", 12) = "aaaa"
     * StringUtil.fixLength("aaaa", -1) = "aaaa"
     *
     * @param s      대상 문자열
     * @param len    자를 길이의 최대값
     * @return 지정한 길이만큼 잘려진 문자열
     */
    public static String fixLength(String s, int len) {
        return setMaxLength(s, len);
    }
    
    /**
     * Converts an object to a String. If object is <code>null</code> it will be
     * not converted.
     *
     * @pi2.example .
     * StringUtil.toString(null) = null
     * StringUtil.toString("aaaa") = "aaaa"
     * 
     * @param obj    object to convert to string
     * @return string created from the object or <code>null</code>
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
    
    /**
     * 객체를 스트링으로 변환한다. <code>null</code>이면 공백 문자열을 반환한다.
     *
     * @pi2.example .
     * StringUtil.toNotNullString(null) = ""
     * StringUtil.toNotNullString("aaaa") = "aaaa"
     *
     * @param obj    문자열로 변환할 객체
     *
     * @return 변환된 문자열
     */
    public static String toNotNullString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }
    
    
    /**
     * Converts an object to a String Array.
     *
     * @param obj    object to convert to string array
     * @return string array created from the object
     */
    public static String[] toStringArray(Object obj) {
        //return (String[]) jodd.bean.ConvertersManager.convert(obj, String[].class);
        return null;
    }

    // ---------------------------------------------------------------- trim
    /**
     * <p>스트링의 양쪽 끝에 있는 캐릭터(char &lt;= 32)를 잘라낸다. 
     * <code>null</code>일 경우에는 null을 리턴한다.</p>
     * 
     * <p>특정 문자열에 대해 잘라낼려면 
     * {@link #strip(String, String)} 메소드를 이용하면 된다.</p>
     * 
     * @pi2.example .
     * StringUtil.trim(null)          = null
     * StringUtil.trim("")            = ""
     * StringUtil.trim("     ")       = ""
     * StringUtil.trim("abc")         = "abc"
     * StringUtil.trim("    abc    ") = "abc"
     *
     * @param str  대상 문자열
     * @return the 잘려진 문자열, <code>null</code>입력시 null 반환.
     */
    public static String trim(String str) {
        return (str == null ? null : str.trim());
    }

    /** 
     * <p>스트링의 양쪽 끝에 있는 캐릭터(char &lt;= 32)를 잘라낸다. 
     * 잘라낸 길이가 '0'이거나 값이 <code>null</code>일 경우에는 null을 리턴한다.</p>
     * 
     * @pi2.example .
     * StringUtil.trimToNull(null)          = null
     * StringUtil.trimToNull("")            = null
     * StringUtil.trimToNull("     ")       = null
     * StringUtil.trimToNull("abc")         = "abc"
     * StringUtil.trimToNull("    abc    ") = "abc"
     *  
     * @param str  대상 문자열
     * @return the 잘려진 문자열, <code>null</code>입력시 null 반환.
     * @since 2.0
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return (ts == null || ts.length() == 0 ? null : ts);
    }

    /** 
     * <p>스트링의 양쪽 끝에 있는 캐릭터(char &lt;= 32)를 제거한다. 
     * <code>null</code>일 경우에는 빈 문자열("")을 리턴한다.</p>
     * 
     * @pi2.example .
     * StringUtil.trimToEmpty(null)          = ""
     * StringUtil.trimToEmpty("")            = ""
     * StringUtil.trimToEmpty("     ")       = ""
     * StringUtil.trimToEmpty("abc")         = "abc"
     * StringUtil.trimToEmpty("    ab c   ") = "ab c"
     *  
     * @param str  대상 문자열
     * @return trim 된 문자열, <code>null</code>은 절대 반환되지 않음.
     * @since 2.0
     */
    public static String trimToEmpty(String str) {
        return (str == null ? EMPTY : str.trim());
    }

    // Stripping
    //-----------------------------------------------------------------------
    /**
     * <p>Strips whitespace from the start and end of a String.</p>
     * 
     * <p>This is similar to {@link #trim(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * 
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.strip(null)     = null
     * StringUtil.strip("")       = ""
     * StringUtil.strip("   ")    = ""
     * StringUtil.strip("abc")    = "abc"
     * StringUtil.strip("  abc")  = "abc"
     * StringUtil.strip("abc  ")  = "abc"
     * StringUtil.strip(" abc ")  = "abc"
     * StringUtil.strip(" ab c ") = "ab c"
     * 
     * @param str  the String to remove whitespace from, may be null
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String strip(String str) {
        return strip(str, null);
    }
    
    /** 
     * <p>Strips whitespace from the start and end of a String  returning
     * <code>null</code> if the String is empty ("") after the strip.</p>
     * 
     * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * 
     * @pi2.example .
     * StringUtil.strip(null)     = null
     * StringUtil.strip("")       = null
     * StringUtil.strip("   ")    = null
     * StringUtil.strip("abc")    = "abc"
     * StringUtil.strip("  abc")  = "abc"
     * StringUtil.strip("abc  ")  = "abc"
     * StringUtil.strip(" abc ")  = "abc"
     * StringUtil.strip(" ab c ") = "ab c"
     *  
     * @param str  the String to be stripped, may be null
     * @return the stripped String, 
     *  <code>null</code> if whitespace, empty or null String input
     * @since 2.0
     */
    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        str = strip(str, null);
        return (str.length() == 0 ? null : str);
    }

    /** 
     * <p>Strips whitespace from the start and end of a String  returning
     * an empty String if <code>null</code> input.</p>
     * 
     * <p>This is similar to {@link #trimToEmpty(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * 
     * @pi2.example .
     * StringUtil.strip(null)     = ""
     * StringUtil.strip("")       = ""
     * StringUtil.strip("   ")    = ""
     * StringUtil.strip("abc")    = "abc"
     * StringUtil.strip("  abc")  = "abc"
     * StringUtil.strip("abc  ")  = "abc"
     * StringUtil.strip(" abc ")  = "abc"
     * StringUtil.strip(" ab c ") = "ab c"
     *  
     * @param str  the String to be stripped, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String stripToEmpty(String str) {
        return (str == null ? EMPTY : strip(str, null));
    }
    
    /**
     * <p>Strips any of a set of characters from the start and end of a String.
     * This is similar to {@link String#trim()} but allows the characters
     * to be stripped to be controlled.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * 
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.
     * Alternatively use {@link #strip(String)}.</p>
     * 
     * @pi2.example .
     * StringUtil.strip(null, *)          = null
     * StringUtil.strip("", *)            = ""
     * StringUtil.strip("abc", null)      = "abc"
     * StringUtil.strip("  abc", null)    = "abc"
     * StringUtil.strip("abc  ", null)    = "abc"
     * StringUtil.strip(" abc ", null)    = "abc"
     * StringUtil.strip("  abcyx", "xyz") = "  abc"
     * 
     * @param str  the String to remove characters from, may be null
     * @param stripChars  the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String strip(String str, String stripChars) {
        if (str == null || str.length() == 0) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    /**
     * <p>Strips any of a set of characters from the start of a String.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * 
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.</p>
     * 
     * @pi2.example .
     * StringUtil.stripStart(null, *)          = null
     * StringUtil.stripStart("", *)            = ""
     * StringUtil.stripStart("abc", "")        = "abc"
     * StringUtil.stripStart("abc", null)      = "abc"
     * StringUtil.stripStart("  abc", null)    = "abc"
     * StringUtil.stripStart("abc  ", null)    = "abc  "
     * StringUtil.stripStart(" abc ", null)    = "abc "
     * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
     * 
     * @param str  the String to remove characters from, may be null
     * @param stripChars  the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        return str.substring(start);
    }

    /**
     * <p>Strips any of a set of characters from the end of a String.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * An empty string ("") input returns the empty string.</p>
     * 
     * <p>If the stripChars String is <code>null</code>, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.</p>
     * 
     * @pi2.example .
     * StringUtil.stripEnd(null, *)          = null
     * StringUtil.stripEnd("", *)            = ""
     * StringUtil.stripEnd("abc", "")        = "abc"
     * StringUtil.stripEnd("abc", null)      = "abc"
     * StringUtil.stripEnd("  abc", null)    = "  abc"
     * StringUtil.stripEnd("abc  ", null)    = "abc"
     * StringUtil.stripEnd(" abc ", null)    = " abc"
     * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
     * 
     * @param str  the String to remove characters from, may be null
     * @param stripChars  the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }
 
        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    // StripAll
    //-----------------------------------------------------------------------
    /**
     * <p>Strips whitespace from the start and end of every String in an array.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <p>A new array is returned each time, except for length zero.
     * A <code>null</code> array will return <code>null</code>.
     * An empty array will return itself.
     * A <code>null</code> array entry will be ignored.</p>
     * 
     * @pi2.example .
     * StringUtil.stripAll(null)             = null
     * StringUtil.stripAll([])               = []
     * StringUtil.stripAll(["abc", "  abc"]) = ["abc", "abc"]
     * StringUtil.stripAll(["abc  ", null])  = ["abc", null]
     * 
     * @param strs  the array to remove whitespace from, may be null
     * @return the stripped Strings, <code>null</code> if null array input
     */
    public static String[] stripAll(String[] strs) {
        return stripAll(strs, null);
    }
 
    /**
     * <p>Strips any of a set of characters from the start and end of every
     * String in an array.</p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <p>A new array is returned each time, except for length zero.
     * A <code>null</code> array will return <code>null</code>.
     * An empty array will return itself.
     * A <code>null</code> array entry will be ignored.
     * A <code>null</code> stripChars will strip whitespace as defined by
     * {@link Character#isWhitespace(char)}.</p>
     * 
     * @pi2.example .
     * StringUtil.stripAll(null, *)                = null
     * StringUtil.stripAll([], *)                  = []
     * StringUtil.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
     * StringUtil.stripAll(["abc  ", null], null)  = ["abc", null]
     * StringUtil.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
     * StringUtil.stripAll(["yabcz", null], "yz")  = ["abc", null]
     * 
     * @param strs  the array to remove characters from, may be null
     * @param stripChars  the characters to remove, null treated as whitespace
     * @return the stripped Strings, <code>null</code> if null array input
     */
    public static String[] stripAll(String[] strs, String stripChars) {
        int strsLen;
        if (strs == null || (strsLen = strs.length) == 0) {
            return strs;
        }
        String[] newArr = new String[strsLen];
        for (int i = 0; i < strsLen; i++) {
            newArr[i] = strip(strs[i], stripChars);
        }
        return newArr;
    }   

    // Splitting
    //-----------------------------------------------------------------------
    /**
     * <p>공백으로 분리된 지정 문자열을 구분한 뒤 배열로 반환한다.
     *
     * <p>구분자는 배열에 포함되지 않으며 인접한 구분자는 하나의 구분자로 다루어진다.</p>
     * 
     * <p><code>null</code> 입력시에는 <code>null</code>이 반환된다.</p>
     *
     * @pi2.example .
     * StringUtil.split(null)       = null
     * StringUtil.split("")         = []
     * StringUtil.split("abc def")  = ["abc", "def"]
     * StringUtil.split("abc  def") = ["abc", "def"]
     * StringUtil.split(" abc ")    = ["abc"]
     * 
     * @param str  배열로 파싱할 문자열
     * @return 파싱된 스트링 배열
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * <p>지정한 구분자(캐릭터)를 이용해 지정 문자열을 구분한 뒤 배열로 반환한다. 
     * 속도가 빠르니 StringTokenizer를 대체해 사용하면 매우 좋다.</p>
     *
     * <p>구분자는 배열에 포함되지 않으며 인접한 구분자는 하나의 구분자로 다루어진다.</p>
     * 
     * <p><code>null</code> 입력시에는 <code>null</code>이 반환된다.</p>
     *
     * @pi2.example .
     * StringUtil.split(null, *)         = null
     * StringUtil.split("", *)           = []
     * StringUtil.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtil.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringUtil.split("a:b:c", '.')    = ["a:b:c"]
     * StringUtil.split("a\tb\nc", null) = ["a", "b", "c"]
     * StringUtil.split("a b c", ' ')    = ["a", "b", "c"]
     * 
     * @param str  배열로 파싱할 문자열
     * @param separatorChar  구분자 캐릭터
     * @return 파싱된 스트링 배열
     * @since 2.0
     */
    public static String[] split(String str, char separatorChar) {
        // Performance tuned for 2.0 (JDK1.4)
        
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int i =0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * <p>지정한 구분자(스트링)를 이용해 지정 문자열을 구분한 뒤 배열로 반환한다. 
     * 속도가 빠르니 StringTokenizer를 대체해 사용하면 매우 좋다.</p>
     *
     * <p>구분자는 배열에 포함되지 않으며 인접한 구분자는 하나의 구분자로 다루어진다.</p>
     * 
     * <p><code>null</code> 입력시에는 <code>null</code>이 반환된다.</p>
     *
     * @pi2.example .
     * StringUtil.split(null, *)         = null
     * StringUtil.split("", *)           = []
     * StringUtil.split("abc def", null) = ["abc", "def"]
     * StringUtil.split("abc def", " ")  = ["abc", "def"]
     * StringUtil.split("abc  def", " ") = ["abc", "def"]
     * StringUtil.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * 
     * @param str  배열로 파싱할 문자열
     * @param separatorChar  구분자 스트링
     * @return 파싱된 스트링 배열
     */
    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }

    /**
     * <p>지정한 구분자(스트링)를 이용해 지정 문자열을 구분한 뒤 배열로 반환한다. 
     * 속도가 빠르니 StringTokenizer를 대체해 사용하면 매우 좋다.</p>
     *
     * <p>구분자는 배열에 포함되지 않으며 인접한 구분자는 하나의 구분자로 다루어진다.</p>
     * 
     * <p><code>null</code> 입력시에는 <code>null</code>이 반환된다.</p>
     * 
     * @pi2.example .
     * StringUtil.split(null, *, *)            = null
     * StringUtil.split("", *, *)              = []
     * StringUtil.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtil.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 2)    = ["ab", "cdef"]
     * 
     * @param str  배열로 파싱할 문자열
     * @param separatorChar  구분자 스트링
     * @param max  배열에 포함될 요소의 최대값, '0'이나 음수는 무제한을 의미함
     * @return 파싱된 스트링 배열
     */
    public static String[] split(String str, String separatorChars, int max) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()
        
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i =0, start = 0;
        boolean match = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    
    /**
     * <p>지정한 구분자(스트링)를 이용해 지정 문자열을 구분한 뒤 배열로 반환한다. 
     * 속도가 빠르니 StringTokenizer를 대체해 사용하면 매우 좋다.</p>
     *
     * <p>구분자는 배열에 포함되지 않으며 인접한 구분자는 하나의 구분자로 다루어진다.</p>
     * 
     * <p><code>null</code> 입력시에는 <code>null</code>이 반환된다.</p>
     * 
     * @pi2.example .
     * StringUtil.split(null, *, *)            = null
     * StringUtil.split("", *, *)              = []
     * StringUtil.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtil.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 2)    = ["ab", "cdef"]
     * 
     * @param str  배열로 파싱할 문자열
     * @param separatorChar  구분자 스트링
     * @param max  배열에 포함될 요소의 최대값, '0'이나 음수는 무제한을 의미함
     * @return 파싱된 스트링 배열
     */
    public static List splitList(String str, String separatorChars) {
    	return splitList(  str,   separatorChars, -1);
    }
    public static List splitList(String str, String separatorChars, int max) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()
        
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new ArrayList();
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i =0, start = 0;
        boolean match = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list;
    }
    
    /**
     * 지정한 구분자로 스트링을 구분해 스트링 배열을 얻는다. 
     *
     * 이 메소드는 <code>String.split()</code>나 <code>StringUtil.split()</code>, 
     * <code>StringTokenizer</code>에 비해 매우 빠르다.
     *
     * @pi2.example .
     * String[] r = StringUtil.fastSplit("1,22,3,44,5", ",");
     * -> r.length = 5
     * -> r = {"1", "22", "3", "44", "5"}
     * 
     * String[] r = StringUtil.fastSplit("1,22,,,5", ",");
     * -> r.length = 5
     * -> r = {"1", "22", "", "", "5"}
     * 
     * String[] r = StringUtil.fastSplit(null, ",");
     * -> r.length = NullPointerException !!
     * -> r = null
     * 
     * String[] r = StringUtil.fastSplit("173", ",");
     * -> r.length = 1
     * -> r = {"173"}
     * 
     * String[] r = StringUtil.fastSplit("173", null);
     * -> r.length = 1
     * -> r = {"173"}
     * 
     * String[] r = StringUtil.fastSplit("1,2", null);
     * -> r.length = 1
     * -> r = {"1,2"}
     * 
     * String[] r = StringUtil.fastSplit(",", ",");
     * -> r.length = 2
     * -> r = {"", ""}
     *
     * @param src       구분할 대상 문자열
     * @param delimeter 구분할 대상 구분자
     *
     * @return 토근으로 구분된 스트링 배열
     */
    public static String[] fastSplit(String src, String delimeter) {
        if (src == null) {
            return null;
        }
        if (delimeter == null) {
            return new String[] {src};
        }
        int maxparts = (src.length() / delimeter.length()) + 2;     // one more for the last
        int[] positions = new int[maxparts];
        int dellen = delimeter.length();
        
        int i = 0, j = 0;
        int count = 0;
        positions[0] = - dellen;
        while ((i = src.indexOf(delimeter, j)) != -1) {
            count++;
            positions[count] = i;           
            j = i + dellen;                     
        }
        count++;
        positions[count] = src.length();
        
        String[] result = new String[count];
        
        for (i = 0; i < count; i++) {
            result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
        }       
        return result;
    }
    
    
    /**
     * 지정한 구분자로 스트링을 구분해 스트링 배열을 얻는다. 
     *
     * 이 메소드는 <code>String.split()</code>나 <code>StringUtil.split()</code>, 
     * <code>StringTokenizer</code>에 비해 매우 빠르다.
     *
     * @pi2.example .
     * String[] r = StringUtil.fastSplit("1,22,3,44,5", ",");
     * -> r.length = 5
     * -> r = {"1", "22", "3", "44", "5"}
     * 
     * String[] r = StringUtil.fastSplit("1,22,,,5", ",");
     * -> r.length = 5
     * -> r = {"1", "22", "", "", "5"}
     * 
     * String[] r = StringUtil.fastSplit(null, ",");
     * -> r.length = NullPointerException !!
     * -> r = null
     * 
     * String[] r = StringUtil.fastSplit("173", ",");
     * -> r.length = 1
     * -> r = {"173"}
     * 
     * String[] r = StringUtil.fastSplit("173", null);
     * -> r.length = 1
     * -> r = {"173"}
     * 
     * String[] r = StringUtil.fastSplit("1,2", null);
     * -> r.length = 1
     * -> r = {"1,2"}
     * 
     * String[] r = StringUtil.fastSplit(",", ",");
     * -> r.length = 2
     * -> r = {"", ""}
     *
     * @param src       구분할 대상 문자열
     * @param delimeter 구분할 대상 구분자
     *
     * @return 토근으로 구분된 스트링 배열
     */
    public static List fastSplitList(String src, String delimeter, int keySize) {
        if (src == null) {
            return null;
        }
        if (delimeter == null) {
            return new ArrayList();
        }
        int maxparts = (src.length() / delimeter.length()) + 2;     // one more for the last
        int[] positions = new int[maxparts];
        int dellen = delimeter.length();
        
        int i = 0, j = 0;
        int count = 0;
        positions[0] = - dellen;
        while ((i = src.indexOf(delimeter, j)) != -1) {
            count++;
            positions[count] = i;           
            j = i + dellen;                     
        }
        count++;
        positions[count] = src.length();
        
        List result = new ArrayList();
        
        for (i = 0; i < count; i++) {
        	if(result.size() < keySize){
        		result.add(src.substring(positions[i] + dellen, positions[i + 1]));
        	}
        }       
        return result;
    }

    /**
     * 지정한 구분자로 스트링을 구분해 스트링 배열을 얻는다. 
     *
     * 이 메소드는 <code>String.split()</code>나 <code>StringUtil.split()</code>, 
     * <code>StringTokenizer</code>에 비해 매우 빠르다.
     * 
     * fastSplitc()와 다른점은 구분자는 어떠한 숫자나 캐릭터라도 상관없다.
     *
     * @pi2.example .
     * String[] r = StringUtil.fastSplitc("1,22,3,44,5", "a,.q");
     * -> r.length = 5
     * -> r = {"1", "22", "3", "44", "5"}
     * 
     * String[] r = StringUtil.fastSplitc("1,22,,,5", ",");
     * -> r.length = 3
     * -> r = {"1", "22", "5"}
     *
     * String[] r = StringUtil.fastSplitc(null, ",");
     * -> r.length = NullPointerException !!
     * -> r = null
     *
     * String[] r = StringUtil.fastSplitc("173", ",");
     * -> r.length = 1
     * -> r = {"173"}
     *
     * String[] r = StringUtil.fastSplitc("173", (String)null);
     * -> r.length = 1
     * -> r = {"173"}
     *
     * String[] r = StringUtil.fastSplitc("1,2", null);
     * -> r.length = 1
     * -> r = {"1,2"}
     *
     * String[] r = StringUtil.fastSplitc(",", ",");
     * -> r.length = 2
     * -> r = {"", ""}
     *
     * String[] r = StringUtil.fastSplitc("1, 22 , 5", ", ");
     * -> r.length = 3
     * -> r = {"1", "22", "5"}
     *
     * String[] r = StringUtil.fastSplitc("   , 22 , 5", ", ");
     * -> r.length = 3
     * -> r = {"", "22", "5"}
     *
     * @param src       구분할 대상 문자열
     * @param delimeter 구분할 대상 구분자
     *
     * @return 토근으로 구분된 스트링 배열
     */
    public static String[] fastSplitc(String src, String d) {
        if (src == null) {
            return null;
        }
        if ((d == null) || (d.length() == 0) || (src.length() == 0) ) {
            return new String[] {src};
        }
        char[] delimeters = d.toCharArray();
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];
        
        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (CharUtil.equals(srcc[0], delimeters) ) { // string starts with delimeter
            end[0] = 0;
            count++;
            s = CharUtil.findFirstDiff(srcc, 1, delimeters);
            if (s == -1) {                          // nothing after delimeters
                return new String[] {"", ""};
            }
            start[1] = s;                           // new start
        }
        while (true) {
            // find new end
            e = CharUtil.findFirstAny(srcc, s, delimeters);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;
            
            // find new start
            count++;
            s = CharUtil.findFirstDiff(srcc, e, delimeters);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }       
        return result;
    }


    // Joining
    //-----------------------------------------------------------------------  
    /**
     * <p>객체 배열의 요소를 문자열로 반환한다.</p>
     *
     * <p>반환되는 문자열에 구분자는 포함되지 않으며 null이나 공백 문자열 요소는 무시된다.</p>
     * 
     * @pi2.example .
     * StringUtil.join(null)            = null
     * StringUtil.join([])              = ""
     * StringUtil.join([null])          = ""
     * StringUtil.join(["a", "b", "c"]) = "abc"
     * StringUtil.join([null, "", "a"]) = "a"
     * 
     * @param array  대상 객체 배열
     * @return 합쳐진 문자열, <code>null</code> 입력시에는 <code>null</code>이 반환됨.
     * @since 2.0
     */
    public static String join(Object[] array) {
        return join(array, null);
    }
    
    /**
     * <p>객체 배열의 요소를 지정된 구분자(캐릭터)로 구분하여 문자열로 반환한다.</p>
     *
     * @pi2.example .
     * StringUtil.join(null, *)               = null
     * StringUtil.join([], *)                 = ""
     * StringUtil.join([null], *)             = ""
     * StringUtil.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtil.join(["a", "b", "c"], null) = "abc"
     * StringUtil.join([null, "", "a"], ';')  = ";;a"
     *
     * @param array  대상 객체 배열
     * @param separator  지정 구분자
     * @return 합쳐진 문자열, <code>null</code> 입력시에는 <code>null</code>이 반환됨.
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>객체 배열의 요소를 지정된 구분자(스트링)로 구분하여 문자열로 반환한다.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String (""). 
     * Null objects or empty strings within the array are represented by 
     * empty strings.</p>
     *
     * @pi2.example .
     * StringUtil.join(null, *)                = null
     * StringUtil.join([], *)                  = ""
     * StringUtil.join([null], *)              = ""
     * StringUtil.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtil.join(["a", "b", "c"], null)  = "abc"
     * StringUtil.join(["a", "b", "c"], "")    = "abc"
     * StringUtil.join([null, "", "a"], ',')   = ",,a"
     * 
     * @param array  대상 객체 배열
     * @param separator  지정 구분자
     * @return 합쳐진 문자열, <code>null</code> 입력시에는 <code>null</code>이 반환됨.
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        int arraySize = array.length;

        // ArraySize ==  0: Len = 0
        // ArraySize > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize 
            = ((arraySize == 0) ? 0 
                : arraySize * ((array[0] == null ? 16 : array[0].toString().length()) 
                    + ((separator != null) ? separator.length(): 0)));

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if ((separator != null) && (i > 0)) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided <code>Iterator</code> into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list. Null objects or empty 
     * strings within the iteration are represented by empty strings.</p>
     *
     * <p>See the examples here: {@link #join(Object[],char)}. </p>
     *
     * @param iterator  the <code>Iterator</code> of values to join together, may be null
     * @param separator  the separator character to use
     * @return the joined String, <code>null</code> if null iterator input
     * @since 2.0
     */
    public static String join(Iterator iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(256);  // Java default is 16, probably too small
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided <code>Iterator</code> into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String ("").</p>
     *
     * <p>See the examples here: {@link #join(Object[],String)}. </p>
     *
     * @param iterator  the <code>Iterator</code> of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null iterator input
     */
    public static String join(Iterator iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(256);  // Java default is 16, probably too small
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if ((separator != null) && iterator.hasNext()) {
                buf.append(separator);
            }
         }
        return buf.toString();
    }

    // Delete
    //-----------------------------------------------------------------------
    /**
     * <p>지정 문자열에서 {@link Character#isSpace(char)}에 정의된 모든 '공백'을 제거한다.</p>
     *
     * @pi2.example .
     * StringUtil.deleteSpaces(null)           = null
     * StringUtil.deleteSpaces("")             = ""
     * StringUtil.deleteSpaces("abc")          = "abc"
     * StringUtil.deleteSpaces(" \t  abc \n ") = "abc"
     * StringUtil.deleteSpaces("ab  c")        = "abc"
     * StringUtil.deleteSpaces("a\nb\tc     ") = "abc"
     *  
     * <p>공백으로 다루는 캐릭터는 <code>{' ', '\t', '\r', '\n', '\b'}</code>이나,<br> 
     * 본 함수는 지역화된 성격이 강하므로 단순히 공백을 제거할 목적이라면<br>
     * {@link #deleteWhitespace(String)}를 사용하는 것이 좋다.</p>
     *
     * @param str  대상 문자열
     * @return 공백이 제거된 문자열, <code>null</code>입력시 null 반환됨.
     */
    public static String deleteSpaces(String str) {
        if (str == null) {
            return null;
        }
        return CharSetUtil.delete(str, " \t\r\n\b");
    }

    /**
     * <p>지정 문자열에서 {@link Character#isSpace(char)}에 정의된 모든 '공백'을 제거한다.</p>
     *
     * @pi2.example .
     * StringUtil.deleteWhitespace(null)         = null
     * StringUtil.deleteWhitespace("")           = ""
     * StringUtil.deleteWhitespace("abc")        = "abc"
     * StringUtil.deleteWhitespace("   ab  c  ") = "abc"
     *  
     * @param str  대상 문자열
     * @return 공백이 제거된 문자열, <code>null</code>입력시 null 반환됨.
     */
    public static String deleteWhitespace(String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            }
        }
        return buffer.toString();
    }

    // Replacing
    //-----------------------------------------------------------------------
    /**
     * <p>문자열을 지정한 패턴에 따라 특정 문자열로 대체한다.</p>
     * <p><code>StringUtil.replace(String, String, String)</code> 함수에 비해 약 20% 정도 빠르다.</p>
     *
     * @pi2.example .
     * StringUtil.fastReplace("12345", "3", "qwe") = "12qwe45"
     * StringUtil.fastReplace("12345", "5", "qwe") = "1234qwe" 
     * StringUtil.fastReplace("12345", "1", "qwe") = "qwe2345" 
     * StringUtil.fastReplace("12345", "0", "qwe") = "12345"
     * StringUtil.fastReplace("12345", "0", null) = "12345"
     * 
     * StringUtil.fastReplace("100010001", "1", "dd") = "dd000dd000dd"
     * StringUtil.fastReplace("100010001", "2", "dd") = "100010001"
     * 
     * StringUtil.fastReplace(null, "1", "2") = null
     *
     * @param s      대상 문자열
     * @param sub    대체 대상 문자열 패턴
     * @param with   대체 문자열
     *
     * @return 대체된 문자열
     */
    public static String fastReplace(String s, String sub, String with) {
        if ((s == null) || (sub == null) || (with == null)) {
            return s;
        }
        int c = 0;
        int i = s.indexOf(sub, c);
        if (i == -1) {
            return s;
        }
        StringBuffer buf = new StringBuffer(s.length() + with.length());
        do {
             buf.append(s.substring(c,i));
             buf.append(with);
             c = i + sub.length();
         } while ((i = s.indexOf(sub, c)) != -1);
         if (c < s.length()) {
             buf.append(s.substring(c,s.length()));
         }
         return buf.toString();
    }

    /**
     * 문자열에서 지정한 캐릭터를 특정 캐릭터로 대체한다.
     *
     * @param s      대상 문자열
     * @param sub    대체할 대상 캐릭터
     * @param with   대체 캐릭터
     *
     * @return 대체된 문자열
     */
    public static String replace(String s, char sub, char with) {
        if (s == null) {
            return s;
        }
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == sub) {
                str[i] = with;
            }
        }
        return new String(str);
    }

    /**
     * 문자열을 지정한 대체 문자열이 나오는 처음 인덱스부터 특정 문자열로 대체한다.
     *
     * @pi2.example .
     * StringUtil.replaceFirst("11221144", "11", "55") = "55221144"
     * StringUtil.replaceFirst("55221144", "11", "55") = "55225544"
     * StringUtil.replaceFirst("55225544", "11", "55") = "55225544"
     * 
     * @param s      대상 문자열
     * @param sub    대체할 대상 문자열
     * @param with   대체 문자열
     *
     * @return 대체된 문자열
     */
    public static String replaceFirst(String s, String sub, String with) {
        if ((s == null) || (sub == null) || (with == null)) {
            return s;
        }
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 문자열에서 지정한 캐릭터가 처음 나오는 부분에 대해 특정 캐릭터로 대체한다.
     *
     * @param s      대상 문자열
     * @param sub    대체할 대상 캐릭터
     * @param with   대체 캐릭터
     *
     * @return 대체된 문자열
     */
    public static String replaceFirst(String s, char sub, char with) {
        if (s == null) {
            return s;
        }
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == sub) {
                str[i] = with;
                break;
            }
        }
        return new String(str);
    }

    /**
     * 문자열에서 지정한 문자열이 제일 마지막에 나오는 부분에 대해 특정 문자열로 대체한다.
     *
     * @pi2.example .
     * StringUtil.replaceLast("55225544", "55", "11") = "55221144"
     * StringUtil.replaceLast("55221144", "55", "11") = "11221144"
     * StringUtil.replaceLast("11221144", "55", "11") = "11221144"
     *
     * @param s      대상 문자열
     * @param sub    대체할 대상 문자열
     * @param with   대체 문자열
     *
     * @return 대체된 문자열
     */
    public static String replaceLast(String s, String sub, String with) {
        if ((s == null) || (sub == null) || (with == null)) {
            return s;
        }
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 문자열에서 지정한 캐릭터가 제일 마지막에 나오는 부분에 대해 특정 캐릭터로 대체한다.
     *
     * @param s      대상 문자열
     * @param sub    대체할 대상 캐릭터
     * @param with   대체 캐릭터
     *
     * @return 대체된 문자열
     */
    public static String replaceLast(String s, char sub, char with) {
        if (s == null) {
            return s;
        }
        char[] str = s.toCharArray();
        for (int i = str.length - 1; i >= 0; i--) {
            if (str[i] == sub) {
                str[i] = with;
                break;
            }
        }
        return new String(str);
    }

    /**
     * <p>문자열에서 지정 문자열에 대해 특정 문자열로 단 한번만 대체한다.</p>
     * 
     * <p><code>null</code>입력값에 대해서는 아무작업도 하지 않는다.</p>
     * 
     * @pi2.example .
     * StringUtil.replaceOnce(null, *, *)        = null
     * StringUtil.replaceOnce("", *, *)          = ""
     * StringUtil.replaceOnce("aba", null, null) = "aba"
     * StringUtil.replaceOnce("aba", null, null) = "aba"
     * StringUtil.replaceOnce("aba", "a", null)  = "aba"
     * StringUtil.replaceOnce("aba", "a", "")    = "aba"
     * StringUtil.replaceOnce("aba", "a", "z")   = "zba"
     * 
     * @see #replace(String text, String repl, String with, int max)
     * @param s      대상 문자열
     * @param sub    대체할 대상 문자열
     * @param with   대체 문자열
     * @return 대체된 문자열
     */
    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    /**
     * <p>문자열에서 지정 문자열에 대해 특정 문자열로 모두 대체한다.</p>
     *
     * <p><code>null</code>입력값에 대해서는 아무작업도 하지 않는다.</p>
     * 
     * @pi2.example .
     * StringUtil.replace(null, *, *)        = null
     * StringUtil.replace("", *, *)          = ""
     * StringUtil.replace("aba", null, null) = "aba"
     * StringUtil.replace("aba", null, null) = "aba"
     * StringUtil.replace("aba", "a", null)  = "aba"
     * StringUtil.replace("aba", "a", "")    = "aba"
     * StringUtil.replace("aba", "a", "z")   = "zbz"
     * 
     * @see #replace(String text, String repl, String with, int max)
     * @see #fastReplace(String text, String repl, String with)
     * @param s      대상 문자열
     * @param sub    대체할 대상 문자열
     * @param with   대체 문자열
     * @return 대체된 문자열
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }
    
    public static String convEtype(String eType, String text, String repl, String with) {
    	if(eType.equals("e_T")){
    		return replace(text, repl, with, -1);
    	}else{
    		return text;
    	}
    }

    /** 
     * <p>문자열에서 지정 문자열에 대해 특정 문자열로 지정한 <code>max</code>만큼만 모두 대체한다.</p>
     *
     * <p><code>null</code>입력값에 대해서는 아무작업도 하지 않는다.</p>
     *
     * @pi2.example .
     * StringUtil.replace(null, *, *, *)         = null
     * StringUtil.replace("", *, *, *)           = ""
     * StringUtil.replace("abaa", null, null, 1) = "abaa"
     * StringUtil.replace("abaa", null, null, 1) = "abaa"
     * StringUtil.replace("abaa", "a", null, 1)  = "abaa"
     * StringUtil.replace("abaa", "a", "", 1)    = "abaa"
     * StringUtil.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtil.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtil.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtil.replace("abaa", "a", "z", -1)  = "zbzz"
     * 
     * @param s      대상 문자열
     * @param sub    대체할 대상 문자열
     * @param with   대체 문자열
     * @param max  대체할 문자열 갯수의 최대값, <code>-1</code>은 무제한.
     * @return 대체된 문자열
     */
    public static String replace(String text, String repl, String with, int max) {
        if (text == null || repl == null || with == null || repl.length() == 0 || max == 0) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    
    /**
     * <p>문자열에서 지정한 값으로 캐릭터 단위로 옮긴다. 
     * 이 함수는 다중 캐릭터에 대해서 대체하는 루틴이 적용된다.</p>
     *
     * @pi2.example .
     * StringUtil.replaceChars(null, *, *)          = null
     * StringUtil.replaceChars("", *, *)            = ""
     * StringUtil.replaceChars("hello", "ho", "jy") = "jelly"
     *
     * StringUtil.fastReplace("hello", "ho", "jy")  = "hello"
     * StringUtil.replace("hello", "ho", "jy")      = "hello"
     *
     * @param str  대상 문자열
     * @param searchChars   찾을 문자열
     * @param replaceChars  변환할 문자열, 공백이거나 null이면 절대 안된다.
     * @return 변환된 문자열, <code>null</code>입력시 null 리턴.
     *
     * @throws NullPointerException 찾을 문자열이나 변환할 문자열이 <code>null</code>이면 발생
     * @throws ArrayIndexOutOfBoundsException 찾을 문자열이 공백이면 발생.
     */
    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        char[] withChrs = replaceChars.toCharArray();
        int sz = chrs.length;
        int withMax = replaceChars.length() - 1;
        for(int i=0; i<sz; i++) {
            int idx = searchChars.indexOf(chrs[i]);
            if(idx != -1) {
                if(idx > withMax) {
                    idx = withMax;
                }
                buffer.append(withChrs[idx]);
            } else {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 대상 스트링을 바이트 배열로 변환한다.
     *
     * @param s 변환할 대상 문자열
     *
     * @return 변환된 바이트 배열
     */
    public static byte[] toByteArray(String s) {
        if (s == null) {
            return null;
        }
        return CharUtil.toByteArray(s.toCharArray());
    }


    /**
     * 문자열에서 대소문자 구분없이 지정한 문자열의 인덱스를 얻는다.
     *
     * @pi2.example .
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "123") = 0
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "1234567890QwErTy") = 0
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "2345") = 1
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "qwe") = 10
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "qwerty") = 10
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "QWERTY") = 10
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "qwertyu") = -1
     * StringUtil.indexOfIgnoreCase("123", "1234") = -1
     * StringUtil.indexOfIgnoreCase("123", "") = 0
     *
     * @param src    대상 문자열
     * @param subS   찾을 인덱스에 대한 문자열
     * @return 찾은 문자열의 인덱스, 값이 없다면 -1을 반환함.
     *
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int indexOfIgnoreCase(String src, String subS) {
        return indexOfIgnoreCase(src, subS, 0);
    }

    /**
     * Finds first index of a substring in the given source string with ignored
     * case. This seems to be the fastest way doing this, with common string
     * length and content (of course, with no use of Boyer-Mayer type of
     * algorithms). Other implementations are slower: getting char array frist,
     * lowercasing the source string, using String.regionMatch etc.
     *
     * @pi2.example .
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "qwerty", 9) = 10
     * StringUtil.indexOfIgnoreCase("1234567890qWeRtY", "qwerty", 11) = -1
     * StringUtil.indexOfIgnoreCase("AAA111aaa", "aaa", 0) = 0
     * StringUtil.indexOfIgnoreCase("AAA111aaa", "aaa", 1) = 6
     *
     * @param src        대상 문자열
     * @param subS       찾을 인덱스에 대한 문자열
     * @param startIndex 검색을 위한 시작 인덱스
     *
     * @return 찾은 문자열의 인덱스, 값이 없다면 -1을 반환함.
     */
    public static int indexOfIgnoreCase(String src, String subS, int startIndex) {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        int total = src.length() - sublen + 1;
        for (int i = startIndex; i < total; i++) {
            int j = 0;
            while (j < sublen) {
                char source = Character.toLowerCase(src.charAt(i + j));
                if (sub.charAt(j) != source) {
                    break;
                }
                j++;
            }
            if (j == sublen) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Finds last index of a substring in the given source string with ignored
     * case.
     *
     * @pi2.example .
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "123") = 0
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "1234567890QwErTy") = 0
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "2345") = 1
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "qwe") = 10
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "qwerty") = 10
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "QWERTY") = 10
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "qwertyu") = -1
     * StringUtil.lastIndexOfIgnoreCase("123", "1234") = -1
     * StringUtil.lastIndexOfIgnoreCase("123", "") = 3
     *
     * @param s
     * @param subS   substring to find
     *
     * @return last index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     * @see #lastIndexOfIgnoreCase(String, String, int)
     */
    public static int lastIndexOfIgnoreCase(String s, String subS) {
        return lastIndexOfIgnoreCase(s, subS, 0);
    }

    /**
     * Finds last index of a substring in the given source string with ignored
     * case.
     *
     * @pi2.example .
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "qwerty", 9) = 10
     * StringUtil.lastIndexOfIgnoreCase("1234567890qWeRtY", "qwerty", 11) = -1
     * StringUtil.lastIndexOfIgnoreCase("AAA111aaa", "aaa", 0) = 6
     * StringUtil.lastIndexOfIgnoreCase("AAA111aaa", "aaa", 1) = 6
     *
     * @param src        source string for examination
     * @param subS       substring to find
     * @param startIndex starting index from where search begins
     *
     * @return last index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int lastIndexOfIgnoreCase(String src, String subS, int startIndex) {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        int total = src.length() - sublen;
        for (int i = total; i >= startIndex; i--) {
            int j = 0;
            while (j < sublen) {
                char source = Character.toLowerCase(src.charAt(i + j));
                if (sub.charAt(j) != source) {
                    break;
                }
                j++;
            }
            if (j == sublen) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Tests if this string starts with the specified prefix with ignored case.
     *
     * @pi2.example .
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "123") = true
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "1234567890QwErTy") = true
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "2345") = false
     *
     * @param src    source string to test
     * @param subS   starting substring
     *
     * @return <code>true</code> if the character sequence represented by the argument is
     *         a prefix of the character sequence represented by this string;
     *         <code>false</code> otherwise.
     */
    public static boolean startsWithIgnoreCase(String src, String subS) {
        return startsWithIgnoreCase(src, subS, 0);
    }
    
    /**
     * Tests if this string starts with the specified prefix with ignored case
     * and with the specified prefix beginning a specified index.
     *
     * @pi2.example .
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "qwe", 10) = true
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "qwerty", 10) = true
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "QWERTY", 10) = true
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "qwertyu", 10) = false
     * StringUtil.startsWithIgnoreCase("1234567890qWeRtY", "qwerty", 11) = false
     *
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "aaa", 0) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "aa", 1) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "a", 2) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "A", 6) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "AA", 6) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "AAA", 6) = true
     * StringUtil.startsWithIgnoreCase("AAA111aaa", "AAAA", 6) = false
     *
     * @param src        source string to test
     * @param subS       starting substring
     * @param startIndex index from where to test
     *
     * @return <code>true</code> if the character sequence represented by the argument is
     *         a prefix of the character sequence represented by this string;
     *         <code>false</code> otherwise.
     */
    public static boolean startsWithIgnoreCase(String src, String subS, int startIndex) {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        if (startIndex + sublen > src.length()) {
            return false;
        }
        int j = 0;
        int i = startIndex;
        while (j < sublen) {
            char source = Character.toLowerCase(src.charAt(i));
            if (sub.charAt(j) != source) {
                return false;
            }
            j++; i++;
        }
        return true;
    }

    /**
     * 대상 문자열이 대소문자 구분없이 지정한 문자열로 끝나는지 여부를 확인함.
     *
     * @pi2.example .
     * StringUtil.endsWithIgnoreCase("1234567890qWeRtY", "y") = true
     * StringUtil.endsWithIgnoreCase("1234567890qWeRtY", "qwerty") = true
     * StringUtil.endsWithIgnoreCase("1234567890qWeRtY", "qwert") = false
     * StringUtil.endsWithIgnoreCase("1234567890qWeRtY", "q") = false
     * StringUtil.endsWithIgnoreCase("1234567890qWeRtY", "1234567890QwErTy") = true
     *
     * @param src    대상 문자열
     * @param subS   확인할 문자열
     *
     * @return 대상 문자열로 끝나면 true, 아니면 false
     */
    public static boolean endsWithIgnoreCase(String src, String subS) {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        int j = 0;
        int i = src.length() - sublen;
        if (i < 0) {
            return false;
        }
        while (j < sublen) {
            char source = Character.toLowerCase(src.charAt(i));
            if (sub.charAt(j) != source) {
                return false;
            }
            j++; i++;
        }
        return true;
    }
    
    // IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     *
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOf(null, *)         = -1
     * StringUtil.indexOf("", *)           = -1
     * StringUtil.indexOf("aabaabaa", 'a') = 0
     * StringUtil.indexOf("aabaabaa", 'b') = 2
     * 
     * @param str  the String to check, may be null
     * @param searchChar  the character to find
     * @return the first index of the search character, 
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return -1;
        }
        return str.indexOf(searchChar);
    }
    
    /**
     * <p>Finds the first index within a String from a start position,
     * handling <code>null</code>.
     * This method uses {@link String#indexOf(int, int)}.</p>
     *
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * A start position greater than the string length returns <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOf(null, *, *)          = -1
     * StringUtil.indexOf("", *, *)            = -1
     * StringUtil.indexOf("aabaabaa", 'b', 0)  = 2
     * StringUtil.indexOf("aabaabaa", 'b', 3)  = 5
     * StringUtil.indexOf("aabaabaa", 'b', 9)  = -1
     * StringUtil.indexOf("aabaabaa", 'b', -1) = 2
     * 
     * @param str  the String to check, may be null
     * @param searchChar  the character to find
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search character, 
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar, int startPos) {
        if (str == null || str.length() == 0) {
            return -1;
        }
        return str.indexOf(searchChar, startPos);
    }
    
    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOf(null, *)          = -1
     * StringUtil.indexOf(*, null)          = -1
     * StringUtil.indexOf("", "")           = 0
     * StringUtil.indexOf("aabaabaa", "a")  = 0
     * StringUtil.indexOf("aabaabaa", "b")  = 2
     * StringUtil.indexOf("aabaabaa", "ab") = 1
     * StringUtil.indexOf("aabaabaa", "")   = 0
     * 
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }
    
    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String, int)}.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * An empty ("") search String always matches.
     * A start position greater than the string length only matches
     * an empty search String.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOf(null, *, *)          = -1
     * StringUtil.indexOf(*, null, *)          = -1
     * StringUtil.indexOf("", "", 0)           = 0
     * StringUtil.indexOf("aabaabaa", "a", 0)  = 0
     * StringUtil.indexOf("aabaabaa", "b", 0)  = 2
     * StringUtil.indexOf("aabaabaa", "ab", 0) = 1
     * StringUtil.indexOf("aabaabaa", "b", 3)  = 5
     * StringUtil.indexOf("aabaabaa", "b", 9)  = -1
     * StringUtil.indexOf("aabaabaa", "b", -1) = 2
     * StringUtil.indexOf("aabaabaa", "", 2)   = 2
     * StringUtil.indexOf("abc", "", 9)        = 3
     * 
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        // JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
        if (searchStr.length() == 0 && startPos >= str.length()) {
            return str.length();
        }
        return str.indexOf(searchStr, startPos);
    }
    
    // LastIndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>Finds the last index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(int)}.</p>
     *
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.lastIndexOf(null, *)         = -1
     * StringUtil.lastIndexOf("", *)           = -1
     * StringUtil.lastIndexOf("aabaabaa", 'a') = 7
     * StringUtil.lastIndexOf("aabaabaa", 'b') = 5
     * 
     * @param str  the String to check, may be null
     * @param searchChar  the character to find
     * @return the last index of the search character, 
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }
    
    /**
     * <p>Finds the last index within a String from a start position,
     * handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(int, int)}.</p>
     *
     * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * A start position greater than the string length searches the whole string.</p>
     * 
     * @pi2.example .
     * StringUtil.lastIndexOf(null, *, *)          = -1
     * StringUtil.lastIndexOf("", *,  *)           = -1
     * StringUtil.lastIndexOf("aabaabaa", 'b', 8)  = 5
     * StringUtil.lastIndexOf("aabaabaa", 'b', 4)  = 2
     * StringUtil.lastIndexOf("aabaabaa", 'b', 0)  = -1
     * StringUtil.lastIndexOf("aabaabaa", 'b', 9)  = 5
     * StringUtil.lastIndexOf("aabaabaa", 'b', -1) = -1
     * StringUtil.lastIndexOf("aabaabaa", 'a', 0)  = 0
     * 
     * @param str  the String to check, may be null
     * @param searchChar  the character to find
     * @param startPos  the start position
     * @return the last index of the search character, 
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar, int startPos) {
        if (str == null || str.length() == 0) {
            return -1;
        }
        return str.lastIndexOf(searchChar, startPos);
    }
    
    /**
     * <p>Finds the last index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(String)}.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.lastIndexOf(null, *)          = -1
     * StringUtil.lastIndexOf(*, null)          = -1
     * StringUtil.lastIndexOf("", "")           = 0
     * StringUtil.lastIndexOf("aabaabaa", "a")  = 0
     * StringUtil.lastIndexOf("aabaabaa", "b")  = 2
     * StringUtil.lastIndexOf("aabaabaa", "ab") = 1
     * StringUtil.lastIndexOf("aabaabaa", "")   = 8
     * 
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @return the last index of the search String,
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }
    
    /**
     * <p>Finds the first index within a String, handling <code>null</code>.
     * This method uses {@link String#lastIndexOf(String, int)}.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * An empty ("") search String always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.</p>
     * 
     * @pi2.example .
     * StringUtil.lastIndexOf(null, *, *)          = -1
     * StringUtil.lastIndexOf(*, null, *)          = -1
     * StringUtil.lastIndexOf("aabaabaa", "a", 8)  = 7
     * StringUtil.lastIndexOf("aabaabaa", "b", 8)  = 5
     * StringUtil.lastIndexOf("aabaabaa", "ab", 8) = 4
     * StringUtil.lastIndexOf("aabaabaa", "b", 9)  = 5
     * StringUtil.lastIndexOf("aabaabaa", "b", -1) = -1
     * StringUtil.lastIndexOf("aabaabaa", "a", 0)  = 0
     * StringUtil.lastIndexOf("aabaabaa", "b", 0)  = -1
     * 
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr, startPos);
    }
    
    // Contains
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if String contains a search character, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     *
     * <p>A <code>null</code> or empty ("") String will return <code>false</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.contains(null, *)    = false
     * StringUtil.contains("", *)      = false
     * StringUtil.contains("abc", 'a') = true
     * StringUtil.contains("abc", 'z') = false
     * 
     * @param str  the String to check, may be null
     * @param searchChar  the character to find
     * @return true if the String contains the search character, 
     *  false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return (str.indexOf(searchChar) >= 0);
    }
    
    /**
     * <p>Find the first index within a String, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.</p>
     *
     * <p>A <code>null</code> String will return <code>false</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.contains(null, *)     = false
     * StringUtil.contains(*, null)     = false
     * StringUtil.contains("", "")      = true
     * StringUtil.contains("abc", "")   = true
     * StringUtil.contains("abc", "a")  = true
     * StringUtil.contains("abc", "z")  = false
     * 
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @return true if the String contains the search character, 
     *  false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return (str.indexOf(searchStr) >= 0);
    }
    
    // IndexOfAny chars
    //-----------------------------------------------------------------------
    /**
     * <p>Search a String to find the first index of any
     * character in the given set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> or zero length search array will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOfAny(null, *)                = -1
     * StringUtil.indexOfAny("", *)                  = -1
     * StringUtil.indexOfAny(*, null)                = -1
     * StringUtil.indexOfAny(*, [])                  = -1
     * StringUtil.indexOfAny("zzabyycdxx",['z','a']) = 0
     * StringUtil.indexOfAny("zzabyycdxx",['b','y']) = 3
     * StringUtil.indexOfAny("aba", ['z'])           = -1
     * 
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
     public static int indexOfAny(String str, char[] searchChars) {
         if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
             return -1;
         }
         for (int i = 0; i < str.length(); i ++) {
             char ch = str.charAt(i);
             for (int j = 0; j < searchChars.length; j++) {
                 if (searchChars[j] == ch) {
                     return i;
                 }
             }
         }
         return -1;
     }

    /**
     * <p>Search a String to find the first index of any
     * character in the given set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> search string will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOfAny(null, *)            = -1
     * StringUtil.indexOfAny("", *)              = -1
     * StringUtil.indexOfAny(*, null)            = -1
     * StringUtil.indexOfAny(*, "")              = -1
     * StringUtil.indexOfAny("zzabyycdxx", "za") = 0
     * StringUtil.indexOfAny("zzabyycdxx", "by") = 3
     * StringUtil.indexOfAny("aba","z")          = -1
     *  
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
    public static int indexOfAny(String str, String searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length() == 0) {
            return -1;
        }
        return indexOfAny(str, searchChars.toCharArray());
    }

    // IndexOfAnyBut chars
    //-----------------------------------------------------------------------
    /**
     * <p>Search a String to find the first index of any
     * character not in the given set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> or zero length search array will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOfAnyBut(null, *)           = -1
     * StringUtil.indexOfAnyBut("", *)             = -1
     * StringUtil.indexOfAnyBut(*, null)           = -1
     * StringUtil.indexOfAnyBut(*, [])             = -1
     * StringUtil.indexOfAnyBut("zzabyycdxx",'za') = 3
     * StringUtil.indexOfAnyBut("zzabyycdxx", '')  = 0
     * StringUtil.indexOfAnyBut("aba", 'ab')       = -1
     * 
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
     public static int indexOfAnyBut(String str, char[] searchChars) {
         if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
             return -1;
         }
         outer: for (int i = 0; i < str.length(); i ++) {
             char ch = str.charAt(i);
             for (int j = 0; j < searchChars.length; j++) {
                 if (searchChars[j] == ch) {
                     continue outer;
                 }
             }
             return i;
         }
         return -1;
     }

    /**
     * <p>Search a String to find the first index of any
     * character not in the given set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> search string will return <code>-1</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOfAnyBut(null, *)            = -1
     * StringUtil.indexOfAnyBut("", *)              = -1
     * StringUtil.indexOfAnyBut(*, null)            = -1
     * StringUtil.indexOfAnyBut(*, "")              = -1
     * StringUtil.indexOfAnyBut("zzabyycdxx", "za") = 3
     * StringUtil.indexOfAnyBut("zzabyycdxx", "")   = 0
     * StringUtil.indexOfAnyBut("aba","ab")         = -1
     *  
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
    public static int indexOfAnyBut(String str, String searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length() == 0) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (searchChars.indexOf(str.charAt(i)) < 0) {
                return i;
            }
        }
        return -1;
    }

    // ContainsOnly
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if the String contains only certain characters.</p>
     *
     * <p>A <code>null</code> String will return <code>false</code>.
     * A <code>null</code> valid character array will return <code>false</code>.
     * An empty String ("") always returns <code>true</code>.</p>
     *
     * @pi2.example .
     * StringUtil.containsOnly(null, *)       = false
     * StringUtil.containsOnly(*, null)       = false
     * StringUtil.containsOnly("", *)         = true
     * StringUtil.containsOnly("ab", '')      = false
     * StringUtil.containsOnly("abab", 'abc') = true
     * StringUtil.containsOnly("ab1", 'abc')  = false
     * StringUtil.containsOnly("abz", 'abc')  = false
     * 
     * @param str  the String to check, may be null
     * @param valid  an array of valid chars, may be null
     * @return true if it only contains valid chars and is non-null
     */
    public static boolean containsOnly(String str, char[] valid) {
        // All these pre-checks are to maintain API with an older version
        if ( (valid == null) || (str == null) ) {
            return false;
        }
        if (str.length() == 0) {
            return true;
        }
        if (valid.length == 0) {
            return false;
        }
        return indexOfAnyBut(str, valid) == -1;
    }

    /**
     * <p>Checks if the String contains only certain characters.</p>
     *
     * <p>A <code>null</code> String will return <code>false</code>.
     * A <code>null</code> valid character String will return <code>false</code>.
     * An empty String ("") always returns <code>true</code>.</p>
     *
     * @pi2.example .
     * StringUtil.containsOnly(null, *)       = false
     * StringUtil.containsOnly(*, null)       = false
     * StringUtil.containsOnly("", *)         = true
     * StringUtil.containsOnly("ab", "")      = false
     * StringUtil.containsOnly("abab", "abc") = true
     * StringUtil.containsOnly("ab1", "abc")  = false
     * StringUtil.containsOnly("abz", "abc")  = false
     * 
     * @param str  the String to check, may be null
     * @param validChars  a String of valid chars, may be null
     * @return true if it only contains valid chars and is non-null
     * @since 2.0
     */
    public static boolean containsOnly(String str, String validChars) {
        if (str == null || validChars == null) {
            return false;
        }
        return containsOnly(str, validChars.toCharArray());
    }
    
    // ContainsNone
    //-----------------------------------------------------------------------
    /**
     * <p>Checks that the String does not contain certain characters.</p>
     *
     * <p>A <code>null</code> String will return <code>true</code>.
     * A <code>null</code> invalid character array will return <code>true</code>.
     * An empty String ("") always returns true.</p>
     *
     * @pi2.example .
     * StringUtil.containsNone(null, *)       = true
     * StringUtil.containsNone(*, null)       = true
     * StringUtil.containsNone("", *)         = true
     * StringUtil.containsNone("ab", '')      = true
     * StringUtil.containsNone("abab", 'xyz') = true
     * StringUtil.containsNone("ab1", 'xyz')  = true
     * StringUtil.containsNone("abz", 'xyz')  = false
     * 
     * @param str  the String to check, may be null
     * @param invalidChars  an array of invalid chars, may be null
     * @return true if it contains none of the invalid chars, or is null
     * @since 2.0
     */
    public static boolean containsNone(String str, char[] invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        int strSize = str.length();
        int validSize = invalidChars.length;
        for (int i = 0; i < strSize; i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < validSize; j++) {
                if (invalidChars[j] == ch) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>Checks that the String does not contain certain characters.</p>
     *
     * <p>A <code>null</code> String will return <code>true</code>.
     * A <code>null</code> invalid character array will return <code>true</code>.
     * An empty String ("") always returns true.</p>
     *
     * @pi2.example .
     * StringUtil.containsNone(null, *)       = true
     * StringUtil.containsNone(*, null)       = true
     * StringUtil.containsNone("", *)         = true
     * StringUtil.containsNone("ab", "")      = true
     * StringUtil.containsNone("abab", "xyz") = true
     * StringUtil.containsNone("ab1", "xyz")  = true
     * StringUtil.containsNone("abz", "xyz")  = false
     * 
     * @param str  the String to check, may be null
     * @param invalidChars  a String of invalid chars, may be null
     * @return true if it contains none of the invalid chars, or is null
     * @since 2.0
     */
    public static boolean containsNone(String str, String invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        return containsNone(str, invalidChars.toCharArray());
    }
    
    // IndexOfAny strings
    //-----------------------------------------------------------------------
    /**
     * <p>Find the first index of any of a set of potential substrings.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> or zero length search array will return <code>-1</code>.
     * A <code>null</code> search array entry will be ignored, but a search
     * array containing "" will return <code>0</code> if <code>str</code> is not
     * null. This method uses {@link String#indexOf(String)}.</p>
     * 
     * @pi2.example .
     * StringUtil.indexOfAny(null, *)                     = -1
     * StringUtil.indexOfAny(*, null)                     = -1
     * StringUtil.indexOfAny(*, [])                       = -1
     * StringUtil.indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
     * StringUtil.indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
     * StringUtil.indexOfAny("zzabyycdxx", ["mn","op"])   = -1
     * StringUtil.indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
     * StringUtil.indexOfAny("zzabyycdxx", [""])          = 0
     * StringUtil.indexOfAny("", [""])                    = 0
     * StringUtil.indexOfAny("", ["a"])                   = -1
     * 
     * @param str  the String to check, may be null
     * @param searchStrs  the Strings to search for, may be null
     * @return the first index of any of the searchStrs in str, -1 if no match
     */
    public static int indexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;

        // String's can't have a MAX_VALUEth index.
        int ret = Integer.MAX_VALUE;

        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            String search = searchStrs[i];
            if (search == null) {
                continue;
            }
            tmp = str.indexOf(search);
            if (tmp == -1) {
                continue;
            }

            if (tmp < ret) {
                ret = tmp;
            }
        }

        return (ret == Integer.MAX_VALUE) ? -1 : ret;
    }

    /**
     * <p>Find the latest index of any of a set of potential substrings.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> search array will return <code>-1</code>.
     * A <code>null</code> or zero length search array entry will be ignored,
     * but a search array containing "" will return the length of <code>str</code> 
     * if <code>str</code> is not null. This method uses {@link String#indexOf(String)}</p>
     * 
     * @pi2.example .
     * StringUtil.lastIndexOfAny(null, *)                   = -1
     * StringUtil.lastIndexOfAny(*, null)                   = -1
     * StringUtil.lastIndexOfAny(*, [])                     = -1
     * StringUtil.lastIndexOfAny(*, [null])                 = -1
     * StringUtil.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
     * StringUtil.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
     * StringUtil.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StringUtil.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StringUtil.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
     * 
     * @param str  the String to check, may be null
     * @param searchStrs  the Strings to search for, may be null
     * @return the last index of any of the Strings, -1 if no match
     */
    public static int lastIndexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;
        int ret = -1;
        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            String search = searchStrs[i];
            if (search == null) {
                continue;
            }
            tmp = str.lastIndexOf(search);
            if (tmp > ret) {
                ret = tmp;
            }
        }
        return ret;
    }

    // Substring
    //-----------------------------------------------------------------------
    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     *
     * <p>A negative start position can be used to start <code>n</code>
     * characters from the end of the String.</p>
     *
     * <p>A <code>null</code> String will return <code>null</code>.
     * An empty ("") String will return "".</p>
     * 
     * @pi2.example .
     * StringUtil.substring(null, *)   = null
     * StringUtil.substring("", *)     = ""
     * StringUtil.substring("abc", 0)  = "abc"
     * StringUtil.substring("abc", 2)  = "c"
     * StringUtil.substring("abc", 4)  = ""
     * StringUtil.substring("abc", -2) = "bc"
     * StringUtil.substring("abc", -4) = "abc"
     * 
     * @param str  the String to get the substring from, may be null
     * @param start  the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @return substring from start position, <code>null</code> if null String input
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }

        return str.substring(start);
    }
    
    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     *
     * <p>A negative start position can be used to start/end <code>n</code>
     * characters from the end of the String.</p>
     *
     * <p>The returned substring starts with the character in the <code>start</code>
     * position and ends before the <code>end</code> position. All postion counting is 
     * zero-based -- i.e., to start at the beginning of the string use 
     * <code>start = 0</code>. Negative start and end positions can be used to 
     * specify offsets relative to the end of the String.</p>
     *
     * <p>If <code>start</code> is not strictly to the left of <code>end</code>, ""
     * is returned.</p>
     * 
     * @pi2.example .
     * StringUtil.substring(null, *, *)    = null
     * StringUtil.substring("", * ,  *)    = "";
     * StringUtil.substring("abc", 0, 2)   = "ab"
     * StringUtil.substring("abc", 2, 0)   = ""
     * StringUtil.substring("abc", 2, 4)   = "c"
     * StringUtil.substring("abc", 4, 6)   = ""
     * StringUtil.substring("abc", 2, 2)   = ""
     * StringUtil.substring("abc", -2, -1) = "b"
     * StringUtil.substring("abc", -4, 2)  = "ab"
     * 
     * @param str  the String to get the substring from, may be null
     * @param start  the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @param end  the position to end at (exclusive), negative means
     *  count back from the end of the String by this many characters
     * @return substring from start position to end positon,
     *  <code>null</code> if null String input
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    // Left/Right/Mid
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the leftmost <code>len</code> characters of a String.</p>
     *
     * <p>If <code>len</code> characters are not available, or the
     * String is <code>null</code>, the String will be returned without
     * an exception. An exception is thrown if len is negative.</p>
     *
     * @pi2.example .
     * StringUtil.left(null, *)    = null
     * StringUtil.left(*, -ve)     = ""
     * StringUtil.left("", *)      = ""
     * StringUtil.left("abc", 0)   = ""
     * StringUtil.left("abc", 2)   = "ab"
     * StringUtil.left("abc", 4)   = "abc"
     * 
     * @param str  the String to get the leftmost characters from, may be null
     * @param len  the length of the required String, must be zero or positive
     * @return the leftmost characters, <code>null</code> if null String input
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    /**
     * <p>Gets the rightmost <code>len</code> characters of a String.</p>
     *
     * <p>If <code>len</code> characters are not available, or the String
     * is <code>null</code>, the String will be returned without an
     * an exception. An exception is thrown if len is negative.</p>
     *
     * @pi2.example .
     * StringUtil.right(null, *)    = null
     * StringUtil.right(*, -ve)     = ""
     * StringUtil.right("", *)      = ""
     * StringUtil.right("abc", 0)   = ""
     * StringUtil.right("abc", 2)   = "bc"
     * StringUtil.right("abc", 4)   = "abc"
     * 
     * @param str  the String to get the rightmost characters from, may be null
     * @param len  the length of the required String, must be zero or positive
     * @return the rightmost characters, <code>null</code> if null String input
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(str.length() - len);
        }
    }

    /**
     * <p>Gets <code>len</code> characters from the middle of a String.</p>
     *
     * <p>If <code>len</code> characters are not available, the remainder
     * of the String will be returned without an exception. If the
     * String is <code>null</code>, <code>null</code> will be returned.
     * An exception is thrown if len is negative.</p>
     *
     * @pi2.example .
     * StringUtil.mid(null, *, *)    = null
     * StringUtil.mid(*, *, -ve)     = ""
     * StringUtil.mid("", 0, *)      = ""
     * StringUtil.mid("abc", 0, 2)   = "ab"
     * StringUtil.mid("abc", 0, 4)   = "abc"
     * StringUtil.mid("abc", 2, 4)   = "c"
     * StringUtil.mid("abc", 4, 2)   = ""
     * StringUtil.mid("abc", -2, 2)  = "ab"
     * 
     * @param str  the String to get the characters from, may be null
     * @param pos  the position to start from, negative treated as zero
     * @param len  the length of the required String, must be zero or positive
     * @return the middle characters, <code>null</code> if null String input
     */
    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return EMPTY;
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    // SubStringAfter/SubStringBefore
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the substring before the first occurance of a separator.
     * The separator is not returned.</p>
     *
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * A <code>null</code> separator will return the input string.</p>
     *
     * @pi2.example .
     * StringUtil.substringBefore(null, *)      = null
     * StringUtil.substringBefore("", *)        = ""
     * StringUtil.substringBefore("abc", "a")   = ""
     * StringUtil.substringBefore("abcba", "b") = "a"
     * StringUtil.substringBefore("abc", "c")   = "ab"
     * StringUtil.substringBefore("abc", "d")   = "abc"
     * StringUtil.substringBefore("abc", "")    = ""
     * StringUtil.substringBefore("abc", null)  = "abc"
     *
     * @param str  the String to get a substring from, may be null
     * @param separator  the String to search for, may be null
     * @return the substring before the first occurance of the separator,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBefore(String str, String separator) {
        if (str == null || separator == null || str.length() == 0) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>Gets the substring after the first occurance of a separator.
     * The separator is not returned.</p>
     *
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * A <code>null</code> separator will return the empty string if the
     * input string is not <code>null</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.substringAfter(null, *)      = null
     * StringUtil.substringAfter("", *)        = ""
     * StringUtil.substringAfter(*, null)      = ""
     * StringUtil.substringAfter("abc", "a")   = "bc"
     * StringUtil.substringAfter("abcba", "b") = "cba"
     * StringUtil.substringAfter("abc", "c")   = ""
     * StringUtil.substringAfter("abc", "d")   = ""
     * StringUtil.substringAfter("abc", "")    = "abc"
     *
     * @param str  the String to get a substring from, may be null
     * @param separator  the String to search for, may be null
     * @return the substring after the first occurance of the separator,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>Gets the substring before the last occurance of a separator.
     * The separator is not returned.</p>
     *
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * An empty or <code>null</code> separator will return the input string.</p>
     * 
     * @pi2.example .
     * StringUtil.substringBeforeLast(null, *)      = null
     * StringUtil.substringBeforeLast("", *)        = ""
     * StringUtil.substringBeforeLast("abcba", "b") = "abc"
     * StringUtil.substringBeforeLast("abc", "c")   = "ab"
     * StringUtil.substringBeforeLast("a", "a")     = ""
     * StringUtil.substringBeforeLast("a", "z")     = "a"
     * StringUtil.substringBeforeLast("a", null)    = "a"
     * StringUtil.substringBeforeLast("a", "")      = "a"
     *
     * @param str  the String to get a substring from, may be null
     * @param separator  the String to search for, may be null
     * @return the substring before the last occurance of the separator,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBeforeLast(String str, String separator) {
        if (str == null || separator == null || str.length() == 0 || separator.length() == 0) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>Gets the substring after the last occurance of a separator.
     * The separator is not returned.</p>
     *
     * <p>A <code>null</code> string input will return <code>null</code>.
     * An empty ("") string input will return the empty string.
     * An empty or <code>null</code> separator will return the empty string if
     * the input string is not <code>null</code>.</p>
     *
     * @pi2.example .
     * StringUtil.substringAfterLast(null, *)      = null
     * StringUtil.substringAfterLast("", *)        = ""
     * StringUtil.substringAfterLast(*, "")        = ""
     * StringUtil.substringAfterLast(*, null)      = ""
     * StringUtil.substringAfterLast("abc", "a")   = "bc"
     * StringUtil.substringAfterLast("abcba", "b") = "a"
     * StringUtil.substringAfterLast("abc", "c")   = ""
     * StringUtil.substringAfterLast("a", "a")     = ""
     * StringUtil.substringAfterLast("a", "z")     = ""
     *
     * @param str  the String to get a substring from, may be null
     * @param separator  the String to search for, may be null
     * @return the substring after the last occurance of the separator,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfterLast(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (separator == null || separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    // Substring between
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the String that is nested in between two instances of the
     * same String.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> tag returns <code>null</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.substringBetween(null, *)            = null
     * StringUtil.substringBetween("", "")             = ""
     * StringUtil.substringBetween("", "tag")          = null
     * StringUtil.substringBetween("tagabctag", null)  = null
     * StringUtil.substringBetween("tagabctag", "")    = ""
     * StringUtil.substringBetween("tagabctag", "tag") = "abc"
     *
     * @param str  the String containing the substring, may be null
     * @param tag  the String before and after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }
    
    /**
     * <p>Gets the String that is nested in between two Strings.
     * Only the first match is returned.</p>
     * 
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> open/close returns <code>null</code> (no match).
     * An empty ("") open/close returns an empty string.</p>
     *
     * @pi2.example .
     * StringUtil.substringBetween(null, *, *)          = null
     * StringUtil.substringBetween("", "", "")          = ""
     * StringUtil.substringBetween("", "", "tag")       = null
     * StringUtil.substringBetween("", "tag", "tag")    = null
     * StringUtil.substringBetween("yabcz", null, null) = null
     * StringUtil.substringBetween("yabcz", "", "")     = ""
     * StringUtil.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtil.substringBetween("yabczyabcz", "y", "z")   = "abc"
     *
     * @param str  the String containing the substring, may be null
     * @param open  the String before the substring, may be null
     * @param close  the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    // Nested extraction
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the String that is nested in between two instances of the
     * same String.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> tag returns <code>null</code>.</p>
     * 
     * @pi2.example .
     * StringUtil.getNestedString(null, *)            = null
     * StringUtil.getNestedString("", "")             = ""
     * StringUtil.getNestedString("", "tag")          = null
     * StringUtil.getNestedString("tagabctag", null)  = null
     * StringUtil.getNestedString("tagabctag", "")    = ""
     * StringUtil.getNestedString("tagabctag", "tag") = "abc"
     *
     * @param str  the String containing nested-string, may be null
     * @param tag  the String before and after nested-string, may be null
     * @return the nested String, <code>null</code> if no match
     * @deprecated Use the better named {@link #substringBetween(String, String)}.
     *             Method will be removed in Commons Lang 3.0.
     */
    @Deprecated
	public static String getNestedString(String str, String tag) {
        return substringBetween(str, tag, tag);
    }
    
    /**
     * <p>Gets the String that is nested in between two Strings.
     * Only the first match is returned.</p>
     * 
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> open/close returns <code>null</code> (no match).
     * An empty ("") open/close returns an empty string.</p>
     *
     * @pi2.example .
     * StringUtil.getNestedString(null, *, *)          = null
     * StringUtil.getNestedString("", "", "")          = ""
     * StringUtil.getNestedString("", "", "tag")       = null
     * StringUtil.getNestedString("", "tag", "tag")    = null
     * StringUtil.getNestedString("yabcz", null, null) = null
     * StringUtil.getNestedString("yabcz", "", "")     = ""
     * StringUtil.getNestedString("yabcz", "y", "z")   = "abc"
     * StringUtil.getNestedString("yabczyabcz", "y", "z")   = "abc"
     *
     * @param str  the String containing nested-string, may be null
     * @param open  the String before nested-string, may be null
     * @param close  the String after nested-string, may be null
     * @return the nested String, <code>null</code> if no match
     * @deprecated Use the better named {@link #substringBetween(String, String, String)}.
     *             Method will be removed in Commons Lang 3.0.
     */
    @Deprecated
	public static String getNestedString(String str, String open, String close) {
        return substringBetween(str, open, close);
    }

    /**
     * <p>스트링이 주어진 와일드카드 패턴과 매칭되는지를 확인한다.</p>
     * 
     * 사용 가능한 패턴은 단일 캐릭터 ('?') 또는 멀티 캐릭터 ('*')이다.<br>
     * 또한 특수문자를 표현하기 위해서 escaped 캐릭터를 사용한다. ('\').<br>
     *
     * <p>이 메소드는 유사한 다른 구현체와 비교해 매우 빠르게 동작한다.
     *
     * @pi2.example .
     * StringUtil.match("CfgOptions.class", "C*class") = true
     * StringUtil.match("CfgOptions.class", "C*clas") = false
     * StringUtil.match("C*r*class", "CfgOptions.class") = false
     * 
     * // wildcard on the start and end of the expression
     * StringUtil.match("CfgOptions.class", "*g*class") = true
     * StringUtil.match("CfgOptions.class", "*C*g*class") = true
     * StringUtil.match("CfgOptions.class", "C*g*cl*") = true
     * StringUtil.match("CfgOptions.class", "*C*g*cl*") = true
     * 
     * // multiple wildcards
     * StringUtil.match("CfgOptions.class", "*g*c**ss") = true
     * StringUtil.match("CfgOptions.class", "*g*c**s") = true
     * StringUtil.match("CfgOptions.class", "*gOpti******ons.c**ss") = true
     * StringUtil.match("CfgOptions.class", "***gOpti*ons.c**ss") = true
     * StringUtil.match("CfgOptions.class", "***gOptions.c**") = true
     * 
     * // wildcard '?':
     * StringUtil.match("CfgOptions.class", "*gOpti*c?ass") = true
     * StringUtil.match("CfgOptions.class", "*gOpti*c?ss") = false
     * 
     * // wildcard on the start and end of the expression:
     * StringUtil.match("CfgOptions.class", "*gOpti*c?as?") = true
     * StringUtil.match("CfgOptions.class", "*gOpti*c?a?") = false
     * StringUtil.match("CfgOptions.class", "?fgOpti*c?ass") = true
     * StringUtil.match("CfgOptions.class", "?gOpti*c?as?") = false
     * StringUtil.match("CfgOptions.class", "??gOpti*c?ass") = true
     * 
     * // multiple wildcards
     * StringUtil.match("CfgOptions.class", "C????ti*c?ass") = true
     * StringUtil.match("CfgOptions.class", "C???ti*c?ass") = false
     * 
     * // wildcards '*' and '?' mixed together:
     * StringUtil.match("CfgOptions.class", "C??*ti*c?ass") = true
     * StringUtil.match("CfgOptions.class", "C*ti*c?as?*") = true
     * StringUtil.match("CfgOptions.class", "C*ti*c?a*?") = true
     * StringUtil.match("CfgOptions.class", "C*ti*?a*") = true
     * StringUtil.match("CfgOptions.class", "C*ti*c?a?*") = true
     * StringUtil.match("CfgOptions.class", "C*ti*c?*la?*") = false
     * 
     * // escaped wildcards:
     * StringUtil.match("CfgOptions.class", "*gOpti\\*c?ass") = false
     * StringUtil.match("CfgOpti*class", "*gOpti\\*class") = true
     * StringUtil.match("CfgOpti*class", "*gOpti\\*c?ass") = true
     * StringUtil.match("CfgOpti*class", "*gOpti\\\\*c?ass") = false
     * StringUtil.match("CfgOpti\\*class", "*gOpti\\\\*c?ass") = true
     * StringUtil.match("CfgOpti?class", "*gOpti\\?c*ass") = true
     * StringUtil.match("CfgOpti\\?class", "*gOpti\\?c*ass") = false
     * StringUtil.match("CfgOptions.class", "CfgOpti\\ons.class") = true
     * StringUtil.match("What's this?", "What*\\?") = true
     *
     * @param string    대상 문자열
     * @param pattern   매칭할 패턴
     * @return          패턴과 일치한다면 <code>true</code>, 아니면 <code>fasle</code>
     */
    public static final boolean match(String string, String pattern) {
        return match(string, pattern, 0, 0);
    }

    /**
     * 클래스 내부적으로 사용되는 재귀적 함수, 외부에서 사용 불가
     */
    private static boolean match(String string, String pattern, int stringStartNdx, int patternStartNdx) {
        int pNdx = patternStartNdx;
        int sNdx = stringStartNdx;
        int pLen = pattern.length();
        int sLen = string.length();
        boolean nextIsNotWildcard = false;
        while (true) {

            // check if end of string and/or pattern occured
            if ((sNdx >= sLen) ) {       // end of string still may have pending '*' in pattern
                while ((pNdx < pLen) && (pattern.charAt(pNdx) == '*')) {
                    pNdx++;
                }
                if (pNdx >= pLen) {             // end of both string and pattern
                    return true;
                }
                return false;
            }
            if (pNdx >= pLen) {                 // end pf pattern, but not end of the string
                return false;
            }
            char p = pattern.charAt(pNdx);      // pattern char

            // perform logic
            if (!nextIsNotWildcard ) {

                if (p == '\\') {
                    pNdx++;
                    nextIsNotWildcard =  true;
                    continue;
                }
                if (p == '?') {
                    sNdx++; pNdx++;
                    continue;
                }
                if (p == '*') {
                    char pnext = 0;                     // next pattern char
                    if (pNdx + 1 < pLen) {
                        pnext = pattern.charAt(pNdx + 1);
                    }
                    if (pnext == '*') {                 // double '*' have the same effect as one '*'
                        pNdx++;
                        continue;
                    }
                    int i;
                    pNdx++;

                    // find recursively if there is any substring from the end of the
                    // line that matches the rest of the pattern !!!
                    for (i = string.length(); i >= sNdx; i--) {
                        if (match(string, pattern, i, pNdx) ) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                nextIsNotWildcard = false;
            }
            
            // check if pattern char and string char are equals
            if (p != string.charAt(sNdx)) {
                return false;
            }

            // everything matches for now, continue
            sNdx++; pNdx++;
        }
    }
    
    /**
     * 대상 문자열에서 지정 문자열의 갯수를 얻는다.
     *
     * @param source    source string
     * @param sub       substring to count
     * @return          number of substring occurences
     */
    public final static int count(String source, String sub) {
        int count = 0;
        int i = 0, j = 0;       
        while (true) {
            i = source.indexOf(sub, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + sub.length();
        }
        return count;
    }
    
    /**
     * 대소문자 구분없이 대상 문자열에서 지정 문자열의 갯수를 얻는다.
     *
     * @param source    source string
     * @param sub       substring to count
     * @return          number of substring occurences
     */
    public final static int countIgnoreCase(String source, String sub) {
        int count = 0;
        int i = 0, j = 0;       
        while (true) {
            i = StringUtil.indexOfIgnoreCase(source, sub, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + sub.length();
        }
        return count;
    }


    /**
     * <p>대상 문자열을 지정 문자셋으로 변경.</p>
     *
     * <a href='http://www.iana.org/assignments/character-sets'><b>지원 문자셋 보기.</b></a>
     *
     * @param    src 대상 문자열.
     * @return   지정 문자셋으로 변경된 문자열. 예외발생시 원본 문자열 반환.
     */    
    public final static String convert(String src, String charSet) {

        String ss = null;

        try {
            ss = new String(src.getBytes("8859_1"), charSet);
        } 
        catch (UnsupportedEncodingException uee) {
            return src;
        } 
        catch (NullPointerException nee) {
            return src;
        }

        return ss;
    }

	/**
	 * 숫자 i를 size 만큼 반복하기.
	 * 
	 * @param i
	 * @param size
	 * @return 반복하여 생성된 숫자 
	 */
	public static int fillSpace(int i,int size)
	{
		String tmpString = String.valueOf(i);
		tmpString = fillSpace(tmpString,size);
		
		return Integer.parseInt(tmpString);
	}
	
	/**
	 * 오른쪽에 문자 채우기.
	 * 
	 * @param srValue 원래 문자열
	 * @param nCount 총 문자열의 길이
	 * @param fillchar 채울 문자
	 * @return 지정된 길이로 변경된 문자열 
	 */
	public static String fillSpaceRight(String srValue,int nCount,char fillchar)
	{
		if(Validator.isEmpty(srValue)) return fillSpace(fillchar+"", nCount);
		
		StringBuffer temp = new StringBuffer();

		temp.append(srValue);
		temp.append(fillSpace(String.valueOf(fillchar),nCount - srValue.length()));

		return temp.toString();
	}	
    
    /**
     * 왼쪽에 문자 채우기.
     * 
     * @param nValue 원래 문자
     * @param nCount 문자열 총 길이
     * @param fillchar 채울 문자
     * @return 지정된 길이로 변경된 문자열 
     */
    public static String fillSpaceLeft(String nValue , int nCount , char fillchar)
    {
        if(Validator.isEmpty(nValue)) return fillSpace(fillchar+"", nCount);
        
        String tmpString = nValue;
        int len = nValue.length();
        tmpString = fillSpace(String.valueOf(fillchar),nCount-len) + nValue;
        return tmpString;
    }
    
    /**
     * 문자 srValue를 size 만큼 반복하기.
     * 
     * @param srValue
     * @param size
     * @return 반복하여 생성된 문자열 
     */
    public static String fillSpace(String srValue,int size)
    {
        StringBuffer temp = new StringBuffer();
        
        for(int i=0;i<size;i++)
            temp.append(srValue);

        return temp.toString();
    }

	//유니코드 를 KSC5601 로 변환하는 Method
	public static String toKSC(String str){
		String change = null;
     	try{
			change = new String(str.getBytes("8859_1"),"KSC5601");
		}catch(UnsupportedEncodingException e){
			System.err.println(e.getMessage());
		}
		return change;
	}
	
	/**
	 * 숫자가 아닌 글 지우기 
	 * 
	 */
	public static String nonNumRemove(String inputStr){  
		
        String patternStr = "([^0-9]+)";
        String replacementStr = ""; 
        
        return pattenToStr(inputStr,patternStr,replacementStr);
	}
	
	
	/**
	 * 자바 정규식을 사용하여 값을 처리 함
	 * 
	 * @param inputStr 입력값
	 * @param patten 정규표현식
	 * @param replaceStr 해당 정규식에 의해 치환될 문자열
	 * @return
	 */
	public static String pattenToStr(String inputStr,String patternStr,String replacementStr){ 
		if(inputStr == null)return ""; 

        // Compile regular expression
        Pattern pattern = Pattern.compile(patternStr);

        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(inputStr);
        String output = matcher.replaceAll(replacementStr);
        
        return output;
	}
	
	/**
	 * 2.2222222E7 형식을 문자열 형식으로
	 * @param w
	 * @return
	 */
	public static String getNumber (Number n) {
        return df.format(n);
    }
	
	
	/**
	 * list (array)형식의 문자열을 trim후 문자열 배열로 return
	 * 임종세 과장님 요청으로 만듬(겨서 만듬)
	 */
	public static String [] splitArray(String str){
		String [] s =	StringUtil.stripAll(StringUtil.fastSplitc(  str.replaceAll("(\\[|\\])", ""),"," ));
		
		return s;
	}
	
    /**
     * 문자열 byte 수 계산 .
     * @param str(문자열)
     * @return
     */
    public static int lengthByte(String str)
    {
    	if(str == null || str.length() == 0) {
    		return 0;
    	}
    	
    	int length = str.length();
    	int strLength = 0;
    	char c;
    	
    	for(int i=0; i<length; i++){
    		c = str.charAt(i);
    		
    		if(c < 0xac00 || 0xd7a3 < c){
    			strLength++;
    		}else{
    			strLength += 2;		//한글일 경우 2를 증가함
    		}
    	}
    	
    	return strLength;
    }
    
	public static String[] getZipCd(String zip) {
		String[] zipcd = new String[2];
		
		if(zip != null){
			zip = zip.trim();
			zip = zip.replaceAll("-", "");
		}
		
		zipcd[0] = "";
		zipcd[1] = "";
		
		if(zip != null && zip.length() == 5 && isNumber(zip))
		{
			zipcd[0] = zip.substring(0, 3);
			zipcd[1] = zip.substring(3, 5);
		}		
		
		if(zip != null && zip.length() >= 6 && isNumber(zip))
		{
			zipcd[0] = zip.substring(0, 3);
			zipcd[1] = zip.substring(3, 6);			
		}
		
		
		return zipcd;
	}	
	
    public static boolean isNumber(String str){
        boolean result = false; 
        try{
        	Double.parseDouble(str);
            result = true ;
        }catch(Exception e){}
        return result ;
    }	
    
	public static String[] getTelephone(String phone) {
		String[] phoneNumber = new String[3];
		
		phoneNumber[0] = "";
		phoneNumber[1] = "";
		phoneNumber[2] = "";
		/*
		if(phone != null && phone.length() >= 10)
		{
			phoneNumber = phone.split("-", 3);
			
		}	
		
		phoneNumber[0] = phoneNumber[0].trim().replaceAll("\t", "").replaceAll(" ", "");
		phoneNumber[1] = phoneNumber[1].trim().replaceAll("\t", "").replaceAll(" ", "");
		phoneNumber[2] = phoneNumber[2].trim().replaceAll("\t", "").replaceAll(" ", "");
		
		return phoneNumber;
		
		*/

		// '-' 없이 들어오는 값들 때문에 아래 코드로 대치 by 이철희
		if(phone != null && phone.length() >= 10)
		{
			//전화번호 리턴안되는 현상때문에 주석처리
			/*
			String regEx = "^(01\\d{1}|02|0505|0506|0502|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})$";
			
			if(!Pattern.matches(regEx, phone)) return phoneNumber;
			
			phoneNumber[0] = phone.replaceAll(regEx, "$1");
			phoneNumber[1] = phone.replaceAll(regEx, "$2");
			phoneNumber[2] = phone.replaceAll(regEx, "$3");
			*/	
			phoneNumber = phone.split("-");
		}
	    return phoneNumber;

	}
	
	public static String getDelPattern(String option)
	{
		if(option == null || option.length() < 4)
		{
			return "0000";
		}
		
		String sEatPattern = option.substring(0, 4);
		if(sEatPattern.equals("EK22") || sEatPattern.equals("EK23") || sEatPattern.equals("EK24") || 
				sEatPattern.equals("EK25") || sEatPattern.equals("EK26"))
		{
			return "EK02";
		}		
		
		return sEatPattern;
	}
	
	public static int[] getDelWeek(String option, int day_del_count) 
	{
		
		int[] week = new int[7];
		
		week[0] = 0;
		week[1] = 0;
		week[2] = 0;
		week[3] = 0;
		week[4] = 0;
		week[5] = 0;
		week[6] = 0;
		
		// 주1회(월)
		if(option.startsWith("EK22"))
		{
			week[0] = 0;
			week[1] = day_del_count;
			week[2] = 0;
			week[3] = 0;
			week[4] = 0;
			week[5] = 0;
			week[6] = 0;		
		}
		// 주1회(화)
		else if(option.startsWith("EK23"))
		{
			week[0] = 0;
			week[1] = 0;
			week[2] = day_del_count;
			week[3] = 0;
			week[4] = 0;
			week[5] = 0;
			week[6] = 0;	
		}
		// 주1회(수)
		else if(option.startsWith("EK24"))
		{
			week[0] = 0;
			week[1] = 0;
			week[2] = 0;
			week[3] = day_del_count;
			week[4] = 0;
			week[5] = 0;
			week[6] = 0;	
		}
		// 주1회(목)
		else if(option.startsWith("EK25"))
		{
			week[0] = 0;
			week[1] = 0;
			week[2] = 0;
			week[3] = 0;
			week[4] = day_del_count;
			week[5] = 0;
			week[6] = 0;	
		}
		// 주1회(금)
		else if(option.startsWith("EK26"))
		{
			week[0] = 0;
			week[1] = 0;
			week[2] = 0;
			week[3] = 0;
			week[4] = 0;
			week[5] = day_del_count;
			week[6] = 0;	
		}
		// 주5회(월~금)
		else if(option.startsWith("EK06"))
		{
			week[0] = 0;
			week[1] = day_del_count;
			week[2] = day_del_count;
			week[3] = day_del_count;
			week[4] = day_del_count;
			week[5] = day_del_count;
			week[6] = 0;	
		}
		// 주6회(월~금,금2개)
		else if(option.startsWith("EK20"))
		{
			week[0] = 0;
			week[1] = day_del_count;
			week[2] = day_del_count;
			week[3] = day_del_count;
			week[4] = day_del_count;
			week[5] = day_del_count * 2;
			week[6] = 0;		
		}// 주3회(월수금)
		else if(option.startsWith("EK03"))
		{
			week[0] = 0;
			week[1] = day_del_count;
			week[2] = 0;
			week[3] = day_del_count;
			week[4] = 0;
			week[5] = day_del_count;
			week[6] = 0;		
		}// 주4회(월~목)
		else if(option.startsWith("EK17"))
		{
			week[0] = 0;
			week[1] = day_del_count;
			week[2] = day_del_count;
			week[3] = day_del_count;
			week[4] = day_del_count;
			week[5] = 0;
			week[6] = 0;		
		}	
		
		return week;
	}	
	
	public static String getLocalServerIp()
	{
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException ex) {}
		return "";
	}
	
	public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }

        if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
            return true;
        }

        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            return (((Object[]) obj).length == 0);
        }

        return false;
    }
	
	public static boolean isNumeric(String string) {   

		if(string.equals("")){
			//문자열이 공백인지 확인
			return false;
		}
		return string.matches("-?\\d+(\\.\\d+)?");
	}
}
